package com.acciente.nitrogen.dispatcher;

import com.acciente.nitrogen.dispatcher.controller.ControllerExecutor;
import com.acciente.nitrogen.dispatcher.controller.ControllerExecutorException;
import com.acciente.nitrogen.dispatcher.controller.ControllerPool;
import com.acciente.nitrogen.dispatcher.model.ModelFactory;
import com.acciente.nitrogen.dispatcher.model.ModelPool;
import com.acciente.nitrogen.dispatcher.view.ViewExecutor;
import com.acciente.nitrogen.dispatcher.view.ViewExecutorException;
import com.acciente.nitrogen.init.ClassLoaderInitializer;
import com.acciente.nitrogen.init.ConfigLoaderInitializer;
import com.acciente.nitrogen.init.ControllerResolverInitializer;
import com.acciente.nitrogen.init.Logger;
import com.acciente.nitrogen.init.TemplatingEngineInitializer;
import com.acciente.nitrogen.init.config.Config;
import com.acciente.nitrogen.init.config.ConfigLoaderException;
import com.acciente.nitrogen.resolver.ControllerResolver;
import com.acciente.nitrogen.util.ConstructorNotFoundException;

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
      catch ( ConfigLoaderException e )
      {  throw new ServletException( "init-error: config-loader-initializer", e );    }

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

      // the ParamResolver manages resolution of parameter values based on the parameter type
      ParamResolver oParamResolver
         =  new ParamResolver( new ModelPool( oConfig.getModelDefs(),
                                              new ModelFactory( oClassLoader,
                                                                oServletConfig,
                                                                _oLogger
                                                              )
                                            ),
                               oConfig.getFileUpload()
                             );

      // the ControllerPool manages a pool of controllers, reloading if the underlying controller def changes
      ControllerPool oControllerPool = new ControllerPool( oClassLoader, oServletConfig, _oLogger );

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
   }

   public void service( HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws ServletException, IOException
   {
      dispatch( oRequest, oResponse );

      System.out.println( "** debug: received request @" + System.currentTimeMillis() );    // todo: remove after debug completed
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
            // todo: Problem : we need to handle the change detection/reload of the view classes somewhere!
            // todo: Ideas   : if we can find via the classloader all the class a controller depends on (including
            // todo:           the views) then we can reload these classes *before* the controller is invoked

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