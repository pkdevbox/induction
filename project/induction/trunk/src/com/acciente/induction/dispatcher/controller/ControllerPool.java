package com.acciente.induction.dispatcher.controller;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.init.Logger;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.ObjectFactory;
import com.acciente.commons.reflect.ParameterProviderException;

import javax.servlet.ServletConfig;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Map;

/**
 * This class manages a pool of Controller object instances. If a controller object is in the pool and it's underlying
 * class has not since reloaded the object in the pool is used, otherwise a new controller object is instantiated. There
 * is a single instance of this class per dispatcher servlet.
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
    * @throws ParameterProviderException propagated exception
    */
   public Controller getController( String sControllerClassName )
      throws ClassNotFoundException, InvocationTargetException, ParameterProviderException, ConstructorNotFoundException, InstantiationException, IllegalAccessException
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
            ObjectFactory.destroyObject( oController );

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
      throws InvocationTargetException, ConstructorNotFoundException, ParameterProviderException, IllegalAccessException, InstantiationException
   {
      return ( Controller ) ObjectFactory.createObject( oControllerClass, new Object[]{ _oServletConfig }, null );
   }
}

// EOF