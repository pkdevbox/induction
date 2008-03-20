package com.acciente.dragonfly.dispatcher;

import com.acciente.dragonfly.dispatcher.resolver.ControllerResolver;
import com.acciente.dragonfly.init.ClassLoaderInitializer;
import com.acciente.dragonfly.init.ConfigLoaderInitializer;
import com.acciente.dragonfly.init.ControllerResolverInitializer;
import com.acciente.dragonfly.init.Logger;
import com.acciente.dragonfly.init.config.Config;
import com.acciente.dragonfly.model.ModelFactory;
import com.acciente.dragonfly.model.ModelLifeCycleManager;
import com.acciente.dragonfly.util.ReflectUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
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
   private ClassLoader           _oClassLoader;
   private ControllerResolver    _oControllerResolver;
   private ParamValueResolver    _oParamValueResolver;
   private Logger _oLogger;

   /**
    * This method is called by the webcontainer to initialize this servlet
    *
    * @param oServletConfig web container-provided access to this servlet's configuration
    * @throws ServletException
    */
   public void init( ServletConfig oServletConfig )
      throws   ServletException
   {
      ServletContext oServletContext = getServletContext();

      // first setup a logger
      _oLogger = new ServletLogger( this );

      Config oConfig;

      // load the configuration for the dispatcher
      try
      {
         oConfig
         =  ConfigLoaderInitializer
                  .getConfigLoader( oServletContext, oServletConfig, _oLogger ).getConfig();
      }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: ConfigLoaderInitializer", e );    }
      catch ( InvocationTargetException e )
      {  throw new ServletException( "init-error: ConfigLoaderInitializer", e );    }
      catch ( IllegalAccessException e )
      {  throw new ServletException( "init-error: ConfigLoaderInitializer", e );    }
      catch ( InstantiationException e )
      {  throw new ServletException( "init-error: ConfigLoaderInitializer", e );    }

      // setup up our classloader
      try
      {
         _oClassLoader
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
                  .getControllerResolver( oConfig.getControllerResolver(), _oClassLoader, oServletContext, oServletConfig, _oLogger );
      }
      catch ( ClassNotFoundException e )
      {  throw new ServletException( "init-error: ControllerResolverInitializer", e ); }
      catch ( InvocationTargetException e )
      {  throw new ServletException( "init-error: ControllerResolverInitializer", e ); }
      catch ( IllegalAccessException e )
      {  throw new ServletException( "init-error: ControllerResolverInitializer", e ); }
      catch ( InstantiationException e )
      {  throw new ServletException( "init-error: ControllerResolverInitializer", e ); }

      // setup ...
      _oParamValueResolver
         =  new ParamValueResolver( new ModelLifeCycleManager( oConfig.getModelDefs(),
                                                               new ModelFactory( _oClassLoader,
                                                                                 oServletContext,
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
      ControllerResolver.Resolution oResolution = _oControllerResolver.resolve( oRequest, oResponse );
      if ( oResolution == null )
      {
         _oLogger.log( "dispatch-error: request did not resolve to a controller" );
         oResponse.sendError( HttpServletResponse.SC_NOT_FOUND, "Request did not resolve to a controller" );
         return;
      }

      // try to load the controller class
      Class oControllerClass;
      try
      {
         oControllerClass = _oClassLoader.loadClass( oResolution.getClassName() );
      }
      catch ( ClassNotFoundException e )
      {
         _oLogger.log( "dispatch-error: " + e.getMessage() );
         oResponse.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Loading controller failed" );
         return;
      }

      // use performance enhanced reflection to determine the methods in the controller with the specified name
      Method oControllerMethod;
      try
      {
         oControllerMethod = ReflectUtils.getSingletonMethod( oControllerClass,
                                                              oResolution.getMethodName(),
                                                              oResolution.isIgnoreMethodNameCase() );
      }
      catch ( IllegalArgumentException e )
      {
         _oLogger.log( "dispatch-error: " + e.getMessage() );
         oResponse.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage() );
         return;
      }

      // first see if there is a definition for this model class name that tells us
      // how to make objects of this model class
   }

   public static class ServletLogger implements Logger
   {
      private HttpServlet _oHttpServlet;

      private ServletLogger( HttpServlet oHttpServlet )
      {
         _oHttpServlet = oHttpServlet;
      }

      public void log( String sMessage )
      {
         _oHttpServlet.log( sMessage );
      }

      public void log( String sMessage, Throwable oThrowable )
      {
         _oHttpServlet.log( sMessage );
      }
   }
}

// EOF