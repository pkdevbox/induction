package com.acciente.dragonfly.dispatcher;

import com.acciente.commons.reflect.Invoker;
import com.acciente.dragonfly.controller.Controller;
import com.acciente.dragonfly.init.Logger;
import com.acciente.dragonfly.util.ConstructorNotFoundException;
import com.acciente.dragonfly.util.MethodNotFoundException;
import com.acciente.dragonfly.util.ReflectUtils;

import javax.servlet.ServletConfig;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

/**
 * This class manages a pool of Controller object instances. If a controller object is in the pool and it's underlying
 * class has not since reloaded the object in the pool is used, otherwise a new controller object is instantiated. There
 * is a single instance of this class per dispatcher servlet.
 *
 * todo: testing pending
 *
 * Log
 * Mar 20, 2008 APR  -  created
 */
public class ControllerPool
{
   private Map             _oControllerCache = new Hashtable();
   private Logger          _oLogger;
   private ClassLoader     _oClassLoader;
   private ServletConfig   _oServletConfig;

   public ControllerPool( ClassLoader oClassLoader, ServletConfig oServletConfig, Logger oLogger )
   {
      _oClassLoader     = oClassLoader;
      _oServletConfig   = oServletConfig;
      _oLogger          = oLogger;
   }

   /**
    * Returns a controller from the controller pool, instantiating if needed
    *
    * @param sControllerClassName fully qualified controller class name
    * @return an instance of a controller object
    *
    * @throws ClassNotFoundException propagated exception
    * @throws InvocationTargetException propagated exception
    * @throws IllegalAccessException propagated exception
    * @throws ConstructorNotFoundException propagated exception
    * @throws InstantiationException propagated exception
    */
   public Controller getController( String sControllerClassName )
      throws ClassNotFoundException, ConstructorNotFoundException, InstantiationException, InvocationTargetException, IllegalAccessException
   {
      // the class definition may have changed so we have to load the class unconditionally our
      // classloader caches so if the class is unchanged this call just returns the last loaded class
      Class oLatestControllerClass = _oClassLoader.loadClass( sControllerClassName );

      // check if we have a cached controller instance
      Controller  oController = ( Controller ) _oControllerCache.get( sControllerClassName );

      if ( oController == null )
      {
         // no controller instance in cache so create one and cache
         _oControllerCache.put( sControllerClassName, oController = createController( oLatestControllerClass ) );
      }
      else
      {
         // ok there is a cached entry, check if the controller class has changed since the instance was cached
         if ( oController.getClass().hashCode() != oLatestControllerClass.hashCode() )
         {
            // yes the class has been reloaded so decommission the previous controller
            // and then create a new controller instance
            destroyController( oController );

            // we have to remove the invalid controller from the cache, otherwise if the createController()
            // below throws an exception an invalid controller would be left in the cache
            _oControllerCache.remove( sControllerClassName );

            // create a new controller and cache it
            _oControllerCache.put( sControllerClassName, oController = createController( oLatestControllerClass ) );
         }
      }

      return oController;
   }

   private Controller createController( Class oControllerClass )
      throws ConstructorNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException
   {
      Controller  oController;

      oController
         =  ( Controller )
            Invoker.invoke( ReflectUtils.getSingletonConstructor( oControllerClass ),
                            new Object[]{ _oServletConfig }
                          );

      Method   oConstructorMethod = null;
      try
      {
         oConstructorMethod = ReflectUtils.getSingletonMethod( oController.getClass(), Controller.CONSTRUCTOR_METHOD_NAME );
      }
      catch ( MethodNotFoundException e )
      {
         // ok if no destructor is defined
      }

      // if we found a single public method
      if ( oConstructorMethod != null )
      {
         oConstructorMethod.invoke( oController, new Object[]{ _oServletConfig } );
      }

      return oController;
   }

   private void destroyController( Controller oController )
      throws InvocationTargetException, IllegalAccessException
   {
      Method   oDestructorMethod = null;

      try
      {
         oDestructorMethod = ReflectUtils.getSingletonMethod( oController.getClass(), Controller.DESTRUCTOR_METHOD_NAME );
      }
      catch ( MethodNotFoundException e )
      {
         // ok if no destructor is defined
      }

      // if we found a single public method, use it only it expects no parameters
      if ( oDestructorMethod != null && oDestructorMethod.getParameterTypes().length == 0 )
      {
         oDestructorMethod.invoke( oController, null );
      }
   }
}

// EOF