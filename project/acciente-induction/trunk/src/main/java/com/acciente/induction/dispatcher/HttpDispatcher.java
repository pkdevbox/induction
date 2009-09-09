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
      ModelPool      oModelPool     = null;

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

         _oRequestInterceptorExecutor = new RequestInterceptorExecutor( oRequestInterceptorArray );
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
      throws ServletException, IOException
   {
      dispatch( oRequest, oResponse );
   }

   public void dispatch( HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws IOException
   {
      // fire the preResolution interceptor
      try
      {
         _oRequestInterceptorExecutor.preResolution( oRequest, oResponse );
      }
      catch ( Exception e )
      {
         logAndRespond( oResponse, "interceptor-exec-" + e.getMessage(), e.getCause() == null ? e : e.getCause() );
      }

      // first try to resolve the request to a controller
      ControllerResolver.Resolution    oControllerResolution   = null;
      ViewResolver.Resolution          oViewResolution         = null;

      oControllerResolution = _oControllerResolver.resolve( oRequest );

      if ( oControllerResolution != null )
      {
         // fire the postResolution interceptor
         try
         {
            _oRequestInterceptorExecutor.postResolution( oRequest, oResponse, oControllerResolution, null );
         }
         catch ( Exception e )
         {
            logAndRespond( oResponse, "interceptor-exec-" + e.getMessage(), e.getCause() == null ? e : e.getCause() );
         }

         // execute the controller
         Object   oControllerReturnValue = null;

         try
         {
            oControllerReturnValue = _oControllerExecutor.execute( oControllerResolution, oRequest, oResponse );
         }
         catch ( ControllerExecutorException e )
         {
            logAndRespond( oResponse, "controller-exec-" + e.getMessage(), e.getCause() == null ? e : e.getCause() );
         }

         // fire the preResponse interceptor
         try
         {
            _oRequestInterceptorExecutor.preResponse( oRequest, oResponse, oControllerResolution, null );
         }
         catch ( Exception e )
         {
            logAndRespond( oResponse, "interceptor-exec-" + e.getMessage(), e.getCause() == null ? e : e.getCause() );
         }

         // process the controller's return value (if any)
         if ( oControllerReturnValue != null )
         {
            // is it a redirect?
            if ( oControllerReturnValue instanceof Redirect )
            {
               String sRedirectURL = _oRedirectResolverFacade.resolve( ( Redirect ) oControllerReturnValue );

               if ( sRedirectURL != null )
               {
                  oResponse.sendRedirect( sRedirectURL );
               }
               else
               {
                  logAndRespond( oResponse, "redirect-resolve: could not resolve redirect request: " + oControllerReturnValue, null );
               }
            }
            else
            {
               // otherwise assume it is a view
               try
               {
                  // check if the value returned is type..
                  if ( oControllerReturnValue instanceof Class )
                  {
                     // if so process as a view type..
                     _oViewExecutor.execute( ( Class ) oControllerReturnValue, oRequest, oResponse );
                  }
                  else
                  {
                     // ...otherwise if so process as a view object
                     _oViewExecutor.execute( oControllerReturnValue, oResponse );
                  }
               }
               catch ( ViewExecutorException e )
               {
                  logAndRespond( oResponse, "view-executor-" + e.getMessage(), e.getCause() == null ? e : e.getCause() );
               }
            }
         }
      }
      else
      {
         // next try to resolve the request to a view
         oViewResolution = _oViewResolver.resolve( oRequest );

         if ( oViewResolution != null )
         {
            // fire the postResolution and preResponse interceptors
            try
            {
               _oRequestInterceptorExecutor.postResolution( oRequest, oResponse, null, oViewResolution );
               _oRequestInterceptorExecutor.preResponse( oRequest, oResponse, null, oViewResolution );
            }
            catch ( Exception e )
            {
               logAndRespond( oResponse, "interceptor-exec-" + e.getMessage(), e.getCause() == null ? e : e.getCause() );
            }

            // now execute the view
            try
            {
               _oViewExecutor.execute( oViewResolution, oRequest, oResponse );
            }
            catch ( ViewExecutorException e )
            {
               logAndRespond( oResponse, "view-executor-" + e.getMessage(), e.getCause() == null ? e : e.getCause() );
            }
         }
         else
         {
            logAndRespond( oResponse, "dispatch-resolve: request did not resolve to a controller or view: " + oRequest.getPathInfo(), null );
         }
      }

      // fire the postResponse interceptors
      try
      {
         _oRequestInterceptorExecutor.postResponse( oRequest, oResponse, oControllerResolution, oViewResolution );
      }
      catch ( Exception e )
      {
         logAndRespond( oResponse, "interceptor-exec-" + e.getMessage(), e.getCause() == null ? e : e.getCause() );
      }
   }

   private void logAndRespond( HttpServletResponse oResponse, String sError, Throwable oError )
      throws IOException
   {
      if ( oError == null )
      {
         _oLog.error( "dispatch-error: " + sError );
         oResponse.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, sError );
      }
      else
      {
         _oLog.error( "dispatch-error: " + sError, oError );
         //oError.printStackTrace();
         oResponse.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, sError + " (more details in dispatcher log)" );
      }
   }
}

// EOF