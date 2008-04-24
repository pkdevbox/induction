package com.acciente.dragonfly.dispatcher;

import com.acciente.dragonfly.resolver.ControllerResolver;
import com.acciente.dragonfly.init.ClassLoaderInitializer;
import com.acciente.dragonfly.init.ConfigLoaderInitializer;
import com.acciente.dragonfly.init.ControllerResolverInitializer;
import com.acciente.dragonfly.init.Logger;
import com.acciente.dragonfly.init.ViewProcessorInitializer;
import com.acciente.dragonfly.init.config.Config;
import com.acciente.dragonfly.dispatcher.model.ModelFactory;
import com.acciente.dragonfly.dispatcher.model.ModelPool;
import com.acciente.dragonfly.dispatcher.controller.ControllerPool;
import com.acciente.dragonfly.dispatcher.controller.ControllerExecutor;
import com.acciente.dragonfly.dispatcher.controller.ControllerExecutorException;
import com.acciente.dragonfly.dispatcher.view.ViewProcessor;
import com.acciente.dragonfly.util.ConstructorNotFoundException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import freemarker.template.TemplateException;

/**
 * Log
 * Feb 29, 2008 APR  -  created
 */
public class HttpDispatcher extends HttpServlet
{
   private  ControllerResolver   _oControllerResolver;
   private  ControllerExecutor   _oControllerExecutor;
   private  ViewProcessor        _oViewProcessor;
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

      // the ControllerPool manages a pool of controllers, reloading if the underlying controller def changes
      ControllerPool oControllerPool = new ControllerPool( oClassLoader, oServletConfig, _oLogger );

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

      // the ControllerExecutor manages the execution of controllers
      _oControllerExecutor = new ControllerExecutor( oControllerPool, oParamResolver );

      // the ViewProcessor manages the processing any view returned by a controller
      try
      {
         _oViewProcessor
            =  ViewProcessorInitializer
                  .getViewProcessor( oConfig.getTemplate(), oClassLoader, oServletConfig, _oLogger );
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
   }

   public void dispatch( HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws IOException
   {
      // ask the resolver what controller should be invoked
      ControllerResolver.Resolution oResolution = _oControllerResolver.resolve( oRequest );
      if ( oResolution == null )
      {
         logAndRespond( oResponse, "dispatch-resolve: request did not resolve to a controller", null );
      }

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
         try
         {
            _oViewProcessor.process( oControllerReturnValue, oResponse );
         }
         catch ( TemplateException e )
         {
            logAndRespond( oResponse, "view-processor-" + e.getMessage(), e.getCause() == null ? e : e.getCause() );
         }
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