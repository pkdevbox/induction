/**
 *   Copyright 2008 Acciente, LLC
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
import com.acciente.induction.dispatcher.controller.ControllerPool;
import com.acciente.induction.dispatcher.model.ModelFactory;
import com.acciente.induction.dispatcher.model.ModelPool;
import com.acciente.induction.dispatcher.view.ViewExecutor;
import com.acciente.induction.dispatcher.view.ViewExecutorException;
import com.acciente.induction.init.ClassLoaderInitializer;
import com.acciente.induction.init.ConfigLoaderInitializer;
import com.acciente.induction.init.ControllerResolverInitializer;
import com.acciente.induction.init.Logger;
import com.acciente.induction.init.TemplatingEngineInitializer;
import com.acciente.induction.init.config.Config;
import com.acciente.induction.init.config.ConfigLoaderException;
import com.acciente.induction.init.config.RedirectResolverInitializer;
import com.acciente.induction.resolver.ControllerResolver;
import com.acciente.induction.resolver.RedirectResolver;
import com.acciente.induction.util.ConstructorNotFoundException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Log
 * Feb 29, 2008 APR  -  created
 */
public class HttpDispatcher extends HttpServlet
{
   private  ControllerResolver   _oControllerResolver;
   private  RedirectResolver     _oRedirectResolver;
   private  ControllerExecutor   _oControllerExecutor;
   private  ViewExecutor         _oViewExecutor;
   private  Logger               _oLogger;

   /**
    * This method is called by the webcontainer to initialize this servlet
    *
    * @param oServletConfig web container-provided access to this servlet's configuration
    * @throws ServletException
    */
   public void init( ServletConfig oServletConfig )
      throws   ServletException
   {
      // first setup a logger
      _oLogger = new ServletLogger( oServletConfig );

      Config oConfig;

      // load the configuration for the dispatcher
      try
      {
         oConfig
         =  ConfigLoaderInitializer
                  .getConfigLoader( oServletConfig, _oLogger ).getConfig();

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
                  .getClassLoader( oConfig.getJavaClassPath(), getClass().getClassLoader(), _oLogger );
      }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: class-loader-initializer", e );    }

      // setup a resolver that maps a request to a controller
      try
      {
         _oControllerResolver
            =  ControllerResolverInitializer
                  .getControllerResolver( oConfig.getControllerResolver(), oClassLoader, oServletConfig, _oLogger );
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

      // setup a resolver that maps a redirect to a URL
      try
      {
         _oRedirectResolver
            =  RedirectResolverInitializer
                  .getRedirectResolver( oConfig.getRedirectResolver(), oClassLoader, oServletConfig, _oLogger );
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

      // the ControllerPool manages a pool of controllers, reloading if the underlying controller def changes
      ControllerPool oControllerPool = new ControllerPool( oClassLoader, oServletConfig, _oLogger );

      // the ParamResolver manages resolution of parameter values based on the parameter type
      ModelFactory   oModelFactory  = new ModelFactory( oClassLoader, oServletConfig, _oLogger );
      ModelPool      oModelPool     = new ModelPool( oConfig.getModelDefs(), oModelFactory );

      // now set the pool for the model factory to use in model-to-model injection
      oModelFactory.setModelPool( oModelPool );

      ParamResolver  oParamResolver = new ParamResolver( oModelPool, oConfig.getFileUpload() );

      // the ControllerExecutor manages the execution of controllers
      _oControllerExecutor = new ControllerExecutor( oControllerPool, oParamResolver );

      // the ViewExecutor manages the processing any view returned by a controller
      try
      {
         _oViewExecutor
            =  new ViewExecutor( TemplatingEngineInitializer
                                    .getTemplatingEngine( oConfig.getTemplating(),
                                                          oClassLoader,
                                                          oServletConfig,
                                                          _oLogger ) );
      }
      catch ( IOException e )
      {  throw new ServletException( "init-error: view-processor-initializer", e ); }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: view-processor-initializer", e ); }
      catch ( InvocationTargetException e )
      {  throw new ServletException( "init-error: view-processor-initializer", e ); }
      catch ( IllegalAccessException e )
      {  throw new ServletException( "init-error: view-processor-initializer", e ); }
      catch ( InstantiationException e )
      {  throw new ServletException( "init-error: view-processor-initializer", e ); }
      catch ( ConstructorNotFoundException e )
      {  throw new ServletException( "init-error: view-processor-initializer", e ); }
      catch ( ParameterProviderException e )
      {  throw new ServletException( "init-error: view-processor-initializer", e ); }
   }

   public void service( HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws ServletException, IOException
   {
      dispatch( oRequest, oResponse );
   }

   public void dispatch( HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws IOException
   {
      // ask the resolver what controller should be invoked
      ControllerResolver.Resolution oResolution = _oControllerResolver.resolve( oRequest );
      if ( oResolution != null )
      {
         Object   oControllerReturnValue = null;
         try
         {
            oControllerReturnValue = _oControllerExecutor.execute( oResolution, oRequest, oResponse );
         }
         catch ( ControllerExecutorException e )
         {
            logAndRespond( oResponse, "controller-exec-" + e.getMessage(), e.getCause() == null ? e : e.getCause() );
         }

         if ( oControllerReturnValue != null )
         {
            if ( oControllerReturnValue instanceof Redirect )
            {
               Redirect oRedirect = ( Redirect ) oControllerReturnValue;

               if ( oRedirect.getControllerClass() != null )
               {
                  if ( oRedirect.getControllerMethodName() != null )
                  {
                     oResponse.sendRedirect( _oRedirectResolver
                                                .resolve(   oRedirect.getControllerClass(),
                                                            oRedirect.getControllerMethodName() ) );
                  }
                  else
                  {
                     oResponse.sendRedirect( _oRedirectResolver
                                                .resolve( oRedirect.getControllerClass() ) );
                  }
               }
               else if ( oRedirect.getURL() != null )
               {
                  oResponse.sendRedirect( _oRedirectResolver
                                             .resolve( oRedirect.getURL() ) );
               }
               else
               {
                  logAndRespond( oResponse, "redirect-resolve: invalid redirect request", null );
               }
            }
            else
            {
               try
               {
                  _oViewExecutor.execute( oControllerReturnValue, oResponse );
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
         logAndRespond( oResponse, "dispatch-resolve: request did not resolve to a controller", null );
      }
   }

   private void logAndRespond( HttpServletResponse oResponse, String sError, Throwable oError )
      throws IOException
   {
      if ( oError == null )
      {
         _oLogger.log( "dispatch-error: " + sError );
         oResponse.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, sError );
      }
      else
      {
         _oLogger.log( "dispatch-error: " + sError, oError );
         oError.printStackTrace();
         oResponse.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, sError + " (more details in dispatcher log)" );
      }
   }

   private static class ServletLogger implements Logger
   {
      private ServletConfig _oServletConfig;

      private ServletLogger( ServletConfig oServletConfig )
      {
         _oServletConfig = oServletConfig;
      }

      public void log( String sMessage )
      {
         _oServletConfig.getServletContext().log( sMessage );
      }

      public void log( String sMessage, Throwable oThrowable )
      {
         _oServletConfig.getServletContext().log( sMessage );
      }
   }
}

// EOF