package com.acciente.dragonfly.dispatcher;

import com.acciente.dragonfly.controller.Controller;
import com.acciente.dragonfly.dispatcher.resolver.ControllerResolver;
import com.acciente.dragonfly.init.ClassLoaderInitializer;
import com.acciente.dragonfly.init.ConfigLoaderInitializer;
import com.acciente.dragonfly.init.ControllerResolverInitializer;
import com.acciente.dragonfly.init.Logger;
import com.acciente.dragonfly.init.config.Config;
import com.acciente.dragonfly.model.ModelFactory;
import com.acciente.dragonfly.model.ModelLifeCycleManager;
import com.acciente.dragonfly.util.ConstructorNotFoundException;
import com.acciente.dragonfly.util.MethodNotFoundException;
import com.acciente.dragonfly.util.ReflectUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Log
 * Feb 29, 2008 APR  -  created
 */
public class HttpDispatcher extends HttpServlet
{
   private  ControllerResolver      _oControllerResolver;
   private  ControllerPool          _oControllerPool;
   private  ParamValueResolver      _oParamValueResolver;
   private  Logger _oLogger;

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
      {  throw new ServletException( "init-error: ConfigLoaderInitializer", e );    }
      catch ( InvocationTargetException e )
      {  throw new ServletException( "init-error: ConfigLoaderInitializer", e );    }
      catch ( IllegalAccessException e )
      {  throw new ServletException( "init-error: ConfigLoaderInitializer", e );    }
      catch ( InstantiationException e )
      {  throw new ServletException( "init-error: ConfigLoaderInitializer", e );    }
      catch ( ConstructorNotFoundException e )
      {  throw new ServletException( "init-error: ConfigLoaderInitializer", e );    }

      // setup up our classloader
      ClassLoader oClassLoader;
      try
      {
         oClassLoader
         =  ClassLoaderInitializer
                  .getClassLoader( oConfig.getJavaClassPath(), getClass().getClassLoader(), _oLogger );
      }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: ClassLoaderInitializer", e );    }

      // setup a resolver that maps a request to a controller
      try
      {
         _oControllerResolver
         =  ControllerResolverInitializer
                  .getControllerResolver( oConfig.getControllerResolver(), oClassLoader, oServletConfig, _oLogger );
      }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: ControllerResolverInitializer", e ); }
      catch ( InvocationTargetException e )
      {  throw new ServletException( "init-error: ControllerResolverInitializer", e ); }
      catch ( IllegalAccessException e )
      {  throw new ServletException( "init-error: ControllerResolverInitializer", e ); }
      catch ( InstantiationException e )
      {  throw new ServletException( "init-error: ControllerResolverInitializer", e ); }
      catch ( ConstructorNotFoundException e )
      {  throw new ServletException( "init-error: ControllerResolverInitializer", e ); }

      // setup a controller pool
      _oControllerPool = new ControllerPool( oClassLoader, oServletConfig, _oLogger );

      // setup ...
      _oParamValueResolver
         =  new ParamValueResolver( new ModelLifeCycleManager( oConfig.getModelDefs(),
                                                               new ModelFactory( oClassLoader,
                                                                                 oServletConfig,
                                                                                 _oLogger
                                                                               )
                                                             )
                                  );
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
         logAndRespond( oResponse, "dispatch: request did not resolve to a controller", null );
         return;
      }

      Controller  oController;
      try
      {
         oController = _oControllerPool.getController( oResolution.getClassName() );
      }
      catch ( ClassNotFoundException e )
      {
         logAndRespond( oResponse, "dispatch: unable to load controller definition", e );
         return;
      }
      catch ( ConstructorNotFoundException e )
      {
         logAndRespond( oResponse, "dispatch: unable to find controller constructor", e );
         return;
      }
      catch ( InstantiationException e )
      {
         logAndRespond( oResponse, "dispatch: controller instantiate error", e );
         return;
      }
      catch ( InvocationTargetException e )
      {
         logAndRespond( oResponse, "dispatch: controller constructor or destructor threw an exception", e );
         return;
      }
      catch ( IllegalAccessException e )
      {
         logAndRespond( oResponse, "dispatch: controller constructor or destructor access error", e );
         return;
      }

      // use performance enhanced reflection to determine the methods in the controller with the specified name
      Method oControllerMethod;
      try
      {
         oControllerMethod = ReflectUtils.getSingletonMethod( oController.getClass(),
                                                              oResolution.getMethodName(),
                                                              oResolution.isIgnoreMethodNameCase() );
      }
      catch ( MethodNotFoundException e )
      {
         logAndRespond( oResponse, "dispatch: controller method not found", e );
         return;
      }

      // ok, we have a controller, now compute values for its formal parameter list
      Class[]  aoParameterTypes  = oControllerMethod.getParameterTypes();
      Object[] aoParameterValues = new Object[ aoParameterTypes.length ];

      for ( int i = 0; i < aoParameterTypes.length; i++ )
      {
         Class oParameterType = aoParameterTypes[ i ];

         aoParameterValues[ i ] = _oParamValueResolver.getParameterValue( oParameterType, oRequest, oResponse );
      }

      // finally call the controller method!
      try
      {
         oControllerMethod.invoke( oController, aoParameterValues );
      }
      catch ( IllegalAccessException e )
      {
         logAndRespond( oResponse, "dispatch: method access error", e );
         return;
      }
      catch ( InvocationTargetException e )
      {
         logAndRespond( oResponse, "dispatch: method threw exception", e );
         return;
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