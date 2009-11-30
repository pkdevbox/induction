/**
 *   Copyright 2009 Acciente, LLC
 *
 *   Acciente, LLC licenses this file to you under the
 *   Apache License, Version 2.0 (the "License"); you
 *   may not use this file except in compliance with the
 *   License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in
 *   writing, software distributed under the License is
 *   distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 *   OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing
 *   permissions and limitations under the License.
 */
package com.acciente.induction.dispatcher;

import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.controller.Redirect;
import com.acciente.induction.dispatcher.controller.ControllerExecutor;
import com.acciente.induction.dispatcher.controller.ControllerExecutorException;
import com.acciente.induction.dispatcher.controller.ControllerParameterProviderFactory;
import com.acciente.induction.dispatcher.controller.ControllerPool;
import com.acciente.induction.dispatcher.interceptor.RequestInterceptorExecutor;
import com.acciente.induction.dispatcher.interceptor.RequestInterceptorParameterProviderFactory;
import com.acciente.induction.dispatcher.model.ModelFactory;
import com.acciente.induction.dispatcher.model.ModelPool;
import com.acciente.induction.dispatcher.redirect.RedirectResolverFacade;
import com.acciente.induction.dispatcher.view.ViewExecutor;
import com.acciente.induction.dispatcher.view.ViewExecutorException;
import com.acciente.induction.dispatcher.view.ViewFactory;
import com.acciente.induction.dispatcher.view.ViewParameterProviderFactory;
import com.acciente.induction.init.ClassLoaderInitializer;
import com.acciente.induction.init.ConfigLoaderInitializer;
import com.acciente.induction.init.ControllerResolverInitializer;
import com.acciente.induction.init.RedirectResolverInitializer;
import com.acciente.induction.init.RequestInterceptorInitializer;
import com.acciente.induction.init.TemplatingEngineInitializer;
import com.acciente.induction.init.ViewResolverInitializer;
import com.acciente.induction.init.config.Config;
import com.acciente.induction.init.config.ConfigLoaderException;
import com.acciente.induction.interceptor.RequestInterceptor;
import com.acciente.induction.resolver.ControllerResolver;
import com.acciente.induction.resolver.RedirectResolver;
import com.acciente.induction.resolver.ViewResolver;
import com.acciente.induction.template.TemplatingEngine;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.MethodNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

/**
 * This is the Induction dispatcher servlet.
 *
 * @created Feb 29, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class HttpDispatcher extends HttpServlet
{
   private RequestInterceptorExecutor  _oRequestInterceptorExecutor;
   private ControllerResolver          _oControllerResolver;
   private ViewResolver                _oViewResolver;
   private RedirectResolverFacade      _oRedirectResolverFacade;

   private ControllerExecutor          _oControllerExecutor;
   private ViewExecutor                _oViewExecutor;

   private Log                         _oLog;

   /**
    * This method is called by the webcontainer to initialize this servlet
    *
    * @param oServletConfig web container-provided access to this servlet's configuration
    * @throws javax.servlet.ServletException
    */
   public void init( ServletConfig oServletConfig )
      throws   ServletException
   {
      // first setup a logger
      _oLog = LogFactory.getLog( HttpDispatcher.class );

      Config oConfig;

      // load the configuration for the dispatcher
      try
      {
         oConfig
         =  ConfigLoaderInitializer
                  .getConfigLoader( oServletConfig ).getConfig();

         //System.out.println( "config: \n" + oConfig );
      }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: config-loader-initializer", e );    }
      catch ( InvocationTargetException e )
      {  throw new ServletException( "init-error: config-loader-initializer", e );    }
      catch ( IllegalAccessException e )
      {  throw new ServletException( "init-error: config-loader-initializer", e );    }
      catch ( InstantiationException e )
      {  throw new ServletException( "init-error: config-loader-initializer", e );    }
      catch ( ConstructorNotFoundException e )
      {  throw new ServletException( "init-error: config-loader-initializer", e );    }
      catch ( ParameterProviderException e )
      {  throw new ServletException( "init-error: config-loader-initializer", e );    }
      catch ( ConfigLoaderException e )
      {  throw new ServletException( "init-error: config-loader", e );    }

      // setup up our classloader
      ClassLoader oClassLoader;
      try
      {
         oClassLoader
         =  ClassLoaderInitializer
                  .getClassLoader( oConfig.getJavaClassPath(), getClass().getClassLoader() );
      }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: class-loader-initializer", e );    }

      // we instantiate the templating engine early since we now support injecting the
      // TemplatingEngine instance into models
      TemplatingEngine oTemplatingEngine;
      try
      {
         oTemplatingEngine
         =  TemplatingEngineInitializer
               .getTemplatingEngine( oConfig.getTemplating(),
                                     oClassLoader,
                                     oServletConfig );
      }
      catch ( IOException e )
      {  throw new ServletException( "init-error: templating-engine-initializer", e ); }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: templating-engine-initializer", e ); }
      catch ( InvocationTargetException e )
      {  throw new ServletException( "init-error: templating-engine-initializer", e ); }
      catch ( IllegalAccessException e )
      {  throw new ServletException( "init-error: templating-engine-initializer", e ); }
      catch ( InstantiationException e )
      {  throw new ServletException( "init-error: templating-engine-initializer", e ); }
      catch ( ConstructorNotFoundException e )
      {  throw new ServletException( "init-error: templating-engine-initializer", e ); }
      catch ( ParameterProviderException e )
      {  throw new ServletException( "init-error: templating-engine-initializer", e ); }

      // we setup the model factory and pool managers early since we now support inject models into the
      // controller and redirect resolver initializers
      ModelFactory   oModelFactory  = new ModelFactory( oClassLoader,
                                                        oServletConfig,
                                                        oTemplatingEngine,
                                                        oConfig.getFileUpload() );
      ModelPool      oModelPool;

      try
      {
         oModelPool = new ModelPool( oConfig.getModelDefs(), oModelFactory );
      }
      catch ( MethodNotFoundException e )
      {  throw new ServletException( "init-error: model-pool", e ); }
      catch ( InvocationTargetException e )
      {  throw new ServletException( "init-error: model-pool", e ); }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: model-pool", e ); }
      catch ( ConstructorNotFoundException e )
      {  throw new ServletException( "init-error: model-pool", e ); }
      catch ( ParameterProviderException e )
      {  throw new ServletException( "init-error: model-pool", e ); }
      catch ( IllegalAccessException e )
      {  throw new ServletException( "init-error: model-pool", e ); }
      catch ( InstantiationException e )
      {  throw new ServletException( "init-error: model-pool", e ); }

      // now set the pool for the model factory to use in model-to-model injection
      oModelFactory.setModelPool( oModelPool );

      // pre-initialize any app scope models that requested it
      Log oLog = LogFactory.getLog( ModelPool.class );

      for ( Iterator oIter = oConfig.getModelDefs().getModelDefList().iterator(); oIter.hasNext(); )
      {
         Config.ModelDefs.ModelDef oModelDef = ( Config.ModelDefs.ModelDef ) oIter.next();

         if ( oModelDef.isApplicationScope() && oModelDef.isInitOnStartUp() )
         {
            oLog.info( "model-pool: initializing model: " + oModelDef.getModelClassName() );

            try
            {
               oModelPool.getModel( oModelDef.getModelClassName(), null );
            }
            catch ( MethodNotFoundException e )
            {  throw new ServletException( "init-error: model-init-on-startup", e ); }
            catch ( InvocationTargetException e )
            {  throw new ServletException( "init-error: model-init-on-startup", e ); }
            catch ( ClassNotFoundException e )
            {  throw new ServletException( "init-error: model-init-on-startup", e ); }
            catch ( ConstructorNotFoundException e )
            {  throw new ServletException( "init-error: model-init-on-startup", e ); }
            catch ( ParameterProviderException e )
            {  throw new ServletException( "init-error: model-init-on-startup", e ); }
            catch ( IllegalAccessException e )
            {  throw new ServletException( "init-error: model-init-on-startup", e ); }
            catch ( InstantiationException e )
            {  throw new ServletException( "init-error: model-init-on-startup", e ); }
         }
      }

      // setup the interceptor chain
      try
      {
         RequestInterceptor[] oRequestInterceptorArray;

         oRequestInterceptorArray
            =  RequestInterceptorInitializer
                  .getRequestInterceptor( oConfig.getRequestInterceptors(),
                                          oModelPool,
                                          oClassLoader,
                                          oServletConfig );

         _oRequestInterceptorExecutor
            = new RequestInterceptorExecutor( oRequestInterceptorArray,
                                              new RequestInterceptorParameterProviderFactory( oModelPool,
                                                                                              oConfig.getFileUpload(),
                                                                                              oTemplatingEngine,
                                                                                              oClassLoader ) );
      }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: request-interceptor-initializer", e ); }
      catch ( InvocationTargetException e )
      {  throw new ServletException( "init-error: request-interceptor-initializer", e ); }
      catch ( IllegalAccessException e )
      {  throw new ServletException( "init-error: request-interceptor-initializer", e ); }
      catch ( InstantiationException e )
      {  throw new ServletException( "init-error: request-interceptor-initializer", e ); }
      catch ( ConstructorNotFoundException e )
      {  throw new ServletException( "init-error: request-interceptor-initializer", e ); }
      catch ( ParameterProviderException e )
      {  throw new ServletException( "init-error: request-interceptor-initializer", e ); }

      // setup a resolver that maps a request to a controller
      try
      {
         _oControllerResolver
            =  ControllerResolverInitializer
                  .getControllerResolver( oConfig.getControllerResolver(),
                                          oConfig.getControllerMapping(),
                                          oModelPool,
                                          oClassLoader,
                                          oServletConfig );
      }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: controller-resolver-initializer", e ); }
      catch ( InvocationTargetException e )
      {  throw new ServletException( "init-error: controller-resolver-initializer", e ); }
      catch ( IllegalAccessException e )
      {  throw new ServletException( "init-error: controller-resolver-initializer", e ); }
      catch ( InstantiationException e )
      {  throw new ServletException( "init-error: controller-resolver-initializer", e ); }
      catch ( ConstructorNotFoundException e )
      {  throw new ServletException( "init-error: controller-resolver-initializer", e ); }
      catch ( ParameterProviderException e )
      {  throw new ServletException( "init-error: controller-resolver-initializer", e ); }
      catch ( IOException e )
      {  throw new ServletException( "init-error: controller-resolver-initializer", e ); }

      // setup a resolver that maps a request to a view
      try
      {
         _oViewResolver
            =  ViewResolverInitializer
                  .getViewResolver( oConfig.getViewResolver(),
                                    oConfig.getViewMapping(),
                                    oModelPool,
                                    oClassLoader,
                                    oServletConfig );
      }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: view-resolver-initializer", e ); }
      catch ( InvocationTargetException e )
      {  throw new ServletException( "init-error: view-resolver-initializer", e ); }
      catch ( IllegalAccessException e )
      {  throw new ServletException( "init-error: view-resolver-initializer", e ); }
      catch ( InstantiationException e )
      {  throw new ServletException( "init-error: view-resolver-initializer", e ); }
      catch ( ConstructorNotFoundException e )
      {  throw new ServletException( "init-error: view-resolver-initializer", e ); }
      catch ( ParameterProviderException e )
      {  throw new ServletException( "init-error: view-resolver-initializer", e ); }
      catch ( IOException e )
      {  throw new ServletException( "init-error: view-resolver-initializer", e ); }

      // setup a resolver that maps a redirect to a URL
      try
      {
         RedirectResolver oRedirectResolver;

         oRedirectResolver
            =  RedirectResolverInitializer
                  .getRedirectResolver( oConfig.getRedirectResolver(),
                                        oConfig.getRedirectMapping(),
                                        oModelPool,
                                        oClassLoader,
                                        oServletConfig );

         _oRedirectResolverFacade = new RedirectResolverFacade( oRedirectResolver );
      }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: redirect-resolver-initializer", e ); }
      catch ( InvocationTargetException e )
      {  throw new ServletException( "init-error: redirect-resolver-initializer", e ); }
      catch ( IllegalAccessException e )
      {  throw new ServletException( "init-error: redirect-resolver-initializer", e ); }
      catch ( InstantiationException e )
      {  throw new ServletException( "init-error: redirect-resolver-initializer", e ); }
      catch ( ConstructorNotFoundException e )
      {  throw new ServletException( "init-error: redirect-resolver-initializer", e ); }
      catch ( ParameterProviderException e )
      {  throw new ServletException( "init-error: redirect-resolver-initializer", e ); }
      catch ( IOException e )
      {  throw new ServletException( "init-error: redirect-resolver-initializer", e ); }

      // the ControllerPool manages a pool of controllers, reloading if the underlying controller def changes
      ControllerPool oControllerPool = new ControllerPool( oClassLoader, oServletConfig );

      // the ControllerExecutor manages the execution of controllers
      _oControllerExecutor = new ControllerExecutor( oControllerPool,
                                                     new ControllerParameterProviderFactory( oModelPool,
                                                                                             oConfig.getFileUpload(),
                                                                                             oTemplatingEngine,
                                                                                             oClassLoader ) );

      // the ViewExecutor manages the loading (when needed) and processing of views
      _oViewExecutor = new ViewExecutor( new ViewFactory( oClassLoader,
                                                          new ViewParameterProviderFactory( oModelPool,
                                                                                            oConfig.getFileUpload(),
                                                                                            oTemplatingEngine,
                                                                                            oClassLoader ) ),
                                         oTemplatingEngine );
   }

   public void service( HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws IOException
   {
      dispatchRequest( oRequest, oResponse );
   }

   public void dispatchRequest( HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws IOException
   {
      try
      {
         // fire the preResolution interceptor
         dispatchInterceptors_preResolution( oRequest, oResponse );

         // first try to resolve the request to a controller
         if ( ! dispatchControllerRequest( oRequest, oResponse ) )
         {
            // try to dispatch the request to a view
            if ( ! dispatchViewRequest( oRequest, oResponse ) )
            {
               // even if we did not resolve to a contoller or view since we attempted resolution,
               // we fire the postResolution interceptor
               dispatchInterceptors_postResolution( oRequest, oResponse, null, null );

               throw new StopRequestProcessingSignal( "dispatch-request >",
                                                      "Request did not resolve to a controller or view, path > "
                                                         + oRequest.getPathInfo() );
            }
         }
      }
      catch ( StopRequestProcessingSignal oSignal )
      {
         if ( oSignal.isError() )
         {
            logAndRespond( oResponse, oSignal );
         }
      }
   }

   private void dispatchInterceptors_preResolution( HttpServletRequest  oRequest,
                                                    HttpServletResponse oResponse )
      throws StopRequestProcessingSignal
   {
      try
      {
         Object oInterceptorReturnValue = _oRequestInterceptorExecutor.preResolution( oRequest, oResponse );

         dispatchLastInterceptorReturnValue( oRequest, oResponse, null, null,
                                             oInterceptorReturnValue );
      }
      catch ( Exception e )
      {
         throw new StopRequestProcessingSignal( "dispatch-interceptors > pre-resolution >", e );
      }
   }

   private void dispatchInterceptors_postResolution( HttpServletRequest             oRequest,
                                                     HttpServletResponse            oResponse,
                                                     ControllerResolver.Resolution  oControllerResolution,
                                                     ViewResolver.Resolution        oViewResolution )
      throws StopRequestProcessingSignal
   {
      try
      {
         Object oInterceptorReturnValue = _oRequestInterceptorExecutor.postResolution( oRequest,
                                                                                       oResponse,
                                                                                       oControllerResolution,
                                                                                       oViewResolution );

         dispatchLastInterceptorReturnValue( oRequest, oResponse, oControllerResolution, oViewResolution,
                                             oInterceptorReturnValue );
      }
      catch ( Exception e )
      {
         throw new StopRequestProcessingSignal( "dispatch-interceptors > post-resolution >", e );
      }
   }

   private void dispatchInterceptors_preResponse( HttpServletRequest             oRequest,
                                                  HttpServletResponse            oResponse,
                                                  ControllerResolver.Resolution  oControllerResolution,
                                                  ViewResolver.Resolution        oViewResolution )
      throws StopRequestProcessingSignal
   {
      try
      {
         Object oInterceptorReturnValue = _oRequestInterceptorExecutor.preResponse( oRequest,
                                                                                    oResponse,
                                                                                    oControllerResolution,
                                                                                    oViewResolution );

         dispatchLastInterceptorReturnValue( oRequest, oResponse, oControllerResolution, oViewResolution,
                                             oInterceptorReturnValue );
      }
      catch ( Exception e )
      {
         throw new StopRequestProcessingSignal( "dispatch-interceptors > pre-response >", e );
      }
   }

   private void dispatchInterceptors_postResponse( HttpServletRequest            oRequest,
                                                   HttpServletResponse           oResponse,
                                                   ControllerResolver.Resolution oControllerResolution,
                                                   ViewResolver.Resolution       oViewResolution )
      throws StopRequestProcessingSignal
   {
      try
      {
         Object oInterceptorReturnValue = _oRequestInterceptorExecutor.postResponse( oRequest,
                                                                                     oResponse,
                                                                                     oControllerResolution,
                                                                                     oViewResolution );

         dispatchLastInterceptorReturnValue( oRequest, oResponse, oControllerResolution, oViewResolution,
                                             oInterceptorReturnValue );
      }
      catch ( Exception e )
      {
         throw new StopRequestProcessingSignal( "dispatch-interceptors > post-response >", e );
      }
   }

   private void dispatchLastInterceptorReturnValue( HttpServletRequest              oRequest,
                                                    HttpServletResponse             oResponse,
                                                    ControllerResolver.Resolution   oControllerResolution,
                                                    ViewResolver.Resolution         oViewResolution,
                                                    Object                          oInterceptorReturnValue )
      throws StopRequestProcessingSignal
   {
      if ( oInterceptorReturnValue != null )
      {
         if ( oInterceptorReturnValue instanceof Boolean )
         {
            if ( ( ( Boolean ) oInterceptorReturnValue ).booleanValue() )
            {
               // if the last interceptor returns true, then stop further processing of the request, per our spec
               throw new StopRequestProcessingSignal();
            }
         }
         else
         {
            if ( oInterceptorReturnValue instanceof Redirect )
            {
               dispatchRedirect( oResponse, ( Redirect ) oInterceptorReturnValue );
            }
            else
            {
               // assume its a view
               try
               {
                  dispatchViewClassOrInstance( oRequest, oResponse, oInterceptorReturnValue );
               }
               catch ( ViewExecutorException e1 )
               {
                  // note that the error handler controller below always issues a stop signal
                  dispatchErrorController( oRequest, oResponse, oControllerResolution, oViewResolution, true, e1.getCause() );
               }
            }
         }
      }
   }

   private boolean dispatchControllerRequest( HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws StopRequestProcessingSignal
   {
      ControllerResolver.Resolution    oControllerResolution;

      // first try to resolve the request to a controller
      oControllerResolution = _oControllerResolver.resolve( oRequest );

      if ( oControllerResolution != null )
      {
         // fire the postResolution interceptor
         dispatchInterceptors_postResolution( oRequest, oResponse, oControllerResolution, null );

         // now execute the controller, this try/catch is to handle any exceptions thrown during controller execution
         Object   oControllerReturnValue;

         try
         {
            oControllerReturnValue = _oControllerExecutor.execute( oControllerResolution, oRequest, oResponse );
         }
         catch ( ControllerExecutorException e1 )
         {
            // note that the error handler controller below always issues a stop signal
            dispatchErrorController( oRequest, oResponse, oControllerResolution, null, false, e1.getCause() );

            // the redundant stop signal below is to inform the compiler of the above fact to prevent it
            // from giving an error that oControllerReturnValue below may be uninitialized
            throw new StopRequestProcessingSignal();
         }

         // process the controller's return value (if any)
         if ( oControllerReturnValue != null )
         {
            // fire the preResponse interceptor
            dispatchInterceptors_preResponse( oRequest, oResponse, oControllerResolution, null );

            // is it a redirect?
            if ( oControllerReturnValue instanceof Redirect )
            {
               dispatchRedirect( oResponse, ( Redirect ) oControllerReturnValue );
            }
            else
            {
               // otherwise assume it is a view
               try
               {
                  dispatchViewClassOrInstance( oRequest, oResponse, oControllerReturnValue );
               }
               catch ( ViewExecutorException e1 )
               {
                  // note that the error handler controller below always issues a stop signal
                  dispatchErrorController( oRequest, oResponse, oControllerResolution, null, true, e1.getCause() );
               }
            }

            // fire the postResponse interceptors
            dispatchInterceptors_postResponse( oRequest, oResponse, oControllerResolution, null );
         }

         return true;
      }

      return false;
   }

   private void dispatchErrorController( HttpServletRequest             oRequest,
                                         HttpServletResponse            oResponse,
                                         ControllerResolver.Resolution  oControllerResolution,
                                         ViewResolver.Resolution        oViewResolution,
                                         boolean                        bPreResponseInterceptorCalled,
                                         Throwable oError )
      throws StopRequestProcessingSignal
   {
      // there was an exception, first try to invoke the error handler controller (if any)
      ControllerResolver.Resolution    oErrorControllerResolution;

      oErrorControllerResolution = _oControllerResolver.resolve( oError );

      if ( oErrorControllerResolution != null )
      {
         Object   oErrorControllerReturnValue;

         try
         {
            oErrorControllerReturnValue = _oControllerExecutor.execute( oErrorControllerResolution, oRequest, oResponse );
         }
         catch ( ControllerExecutorException e2 )
         {
            // there was an error executing the error handler!! so we abort
            throw new StopRequestProcessingSignal( "dispatch-error-controller > During execution of error-handler-controller >", e2 );
         }

         // process the controller's return value (if any)
         if ( oErrorControllerReturnValue != null )
         {
            // fire the preResponse interceptor
            if ( ! bPreResponseInterceptorCalled )
            {
               dispatchInterceptors_preResponse( oRequest, oResponse, oControllerResolution, oViewResolution );
            }

            // now process the return value of the error controller
            if ( oErrorControllerReturnValue instanceof Redirect )  // is it a redirect?
            {
               dispatchRedirect( oResponse, ( Redirect ) oErrorControllerReturnValue );
            }
            else
            {
               // otherwise assume it is a view
               try
               {
                  dispatchViewClassOrInstance( oRequest, oResponse, oErrorControllerReturnValue );
               }
               catch ( ViewExecutorException e1 )
               {
                  // there was an error executing the error handler!! so we abort
                  throw new StopRequestProcessingSignal( "dispatch-error-controller > During execution of view returned by error-handler-controller >", e1 );
               }
            }

            // fire the postResponse interceptors
            dispatchInterceptors_postResponse( oRequest, oResponse, oControllerResolution, oViewResolution );

            // we sucessfully ran the error handler controller, so we are done with this request
            throw new StopRequestProcessingSignal();
         }
      }
      else
      {
         // there is no error handler!! so we abort
         throw new StopRequestProcessingSignal( "dispatch-error-controller >",
                                                "Could not resolve error-handler-controller for error: " + oError.getClass() );
      }
   }

   private boolean dispatchViewRequest( HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws StopRequestProcessingSignal
   {
      ViewResolver.Resolution    oViewResolution;

      // try to resolve the request to a view
      oViewResolution = _oViewResolver.resolve( oRequest );

      if ( oViewResolution != null )
      {
         // fire the postResolution interceptor
         dispatchInterceptors_postResolution( oRequest, oResponse, null, oViewResolution );

         // fire the preResponse interceptor
         dispatchInterceptors_preResponse( oRequest, oResponse, null, oViewResolution );

         // now execute the view
         try
         {
            _oViewExecutor.execute( oViewResolution, oRequest, oResponse );
         }
         catch ( ViewExecutorException e1 )
         {
            // note that the error handler controller below always issues a stop signal
            dispatchErrorController( oRequest, oResponse, null, oViewResolution, true, e1.getCause() );
         }

         // fire the postResponse interceptors
         dispatchInterceptors_postResponse( oRequest, oResponse, null, oViewResolution );

         return true;
      }

      return false;
   }

   private void dispatchViewClassOrInstance( HttpServletRequest   oRequest,
                                             HttpServletResponse  oResponse,
                                             Object               oControllerReturnValue )
      throws ViewExecutorException
   {
      // check if the value returned is a type ...
      if ( oControllerReturnValue instanceof Class )
      {
         // if so process as a view type ...
         _oViewExecutor.execute( ( Class ) oControllerReturnValue, oRequest, oResponse );
      }
      else
      {
         // ... otherwise process as a view object
         _oViewExecutor.execute( oControllerReturnValue, oResponse );
      }
   }

   private void dispatchRedirect( HttpServletResponse oResponse, Redirect oRedirect )
      throws StopRequestProcessingSignal
   {
      String sRedirectURL = _oRedirectResolverFacade.resolve( oRedirect );

      if ( sRedirectURL != null )
      {
         try
         {
            oResponse.sendRedirect( sRedirectURL );
         }
         catch ( IOException e )
         {
            throw new StopRequestProcessingSignal( "dispatch-redirect > Error during sendRedirect( ... ) >", e );
         }
      }
      else
      {
         // note that we are choosing to not call the error-handler controller here, wondering if
         // this is the right decision
         throw new StopRequestProcessingSignal( "dispatch-redirect >",
                                                "Could not resolve redirect request: " + oRedirect );
      }

      throw new StopRequestProcessingSignal();
   }

   private void logAndRespond( HttpServletResponse oResponse, StopRequestProcessingSignal oSRPSignal )
      throws IOException
   {
      String sError;

      if ( oSRPSignal.getErrorDescription() == null )
      {
         if ( oSRPSignal.getErrorCause() == null )
         {
            sError = oSRPSignal.getErrorLocation();
         }
         else
         {
            sError = oSRPSignal.getErrorLocation() + " " + oSRPSignal.getErrorCause().getMessage();
         }
      }
      else
      {
         sError = oSRPSignal.getErrorLocation() + " " + oSRPSignal.getErrorDescription();
      }

      if ( oSRPSignal.getErrorCause() != null )
      {
         _oLog.error( sError, oSRPSignal.getErrorCause() );
      }
      else
      {
         _oLog.error( sError );
      }

      oResponse.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, sError );
   }

   public static class StopRequestProcessingSignal extends Exception
   {
      private  String      _bErrorLocation;
      private  String      _bErrorDescription;
      private  Throwable   _oErrorCause;

      public StopRequestProcessingSignal()
      {
      }

      public StopRequestProcessingSignal( String bErrorLocation, Throwable oErrorCause )
      {
         _bErrorLocation   = bErrorLocation;
         _oErrorCause      = oErrorCause;
      }

      public StopRequestProcessingSignal( String bErrorLocation, String bErrorDescription )
      {
         _bErrorLocation      = bErrorLocation;
         _bErrorDescription   = bErrorDescription;
      }

      public boolean isError()
      {
         return _bErrorLocation != null;
      }

      public String getErrorLocation()
      {
         return _bErrorLocation;
      }

      public String getErrorDescription()
      {
         return _bErrorDescription;
      }

      public Throwable getErrorCause()
      {
         return _oErrorCause;
      }
   }
}

// EOF