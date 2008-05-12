package com.acciente.nitrogen.dispatcher.model;

import com.acciente.nitrogen.init.Logger;
import com.acciente.nitrogen.util.ConstructorNotFoundException;
import com.acciente.nitrogen.util.ObjectFactory;

import javax.servlet.ServletConfig;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Map;

/**
 * This class manages instances of the the model factory classes configured in the system.
 *
 * Log
 * Mar 25, 2008 APR  -  created
 */
public class ConfiguredModelFactoryPool
{
   private Map             _oConfiguredModelFactoryMap;
   private ClassLoader     _oClassLoader;

   private ServletConfig   _oServletConfig;
   private Logger          _oLogger;

   public ConfiguredModelFactoryPool( ClassLoader oClassLoader, ServletConfig oServletConfig, Logger oLogger )
   {
      _oClassLoader     = oClassLoader;
      _oServletConfig   = oServletConfig;
      _oLogger          = oLogger;

      _oConfiguredModelFactoryMap = new Hashtable(); // Hashtable for concurrency safety
   }

   public Object getConfiguredModelFactory( String sModelFactoryClassName )
      throws ClassNotFoundException, ConstructorNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException
   {
      // first load the class (the class loader caches)
      Class oModelFactoryClass = _oClassLoader.loadClass( sModelFactoryClassName );

      // see if we have a cached instance of the factory class
      Object   oModelFactory  = _oConfiguredModelFactoryMap.get( oModelFactoryClass.getName() );

      if ( oModelFactory == null )
      {
         // no cached instance, create a new factory class instance and cache
         oModelFactory = createConfiguredModelFactory( oModelFactoryClass );

         _oConfiguredModelFactoryMap.put( oModelFactoryClass.getName(), oModelFactory );
      }
      else
      {
         if ( oModelFactory.getClass().hashCode() != oModelFactoryClass.hashCode() )
         {
            ObjectFactory.destroyObject( oModelFactory );

            // to be safe remove and add instead of overwrite to prevent leaving
            // an invalid obect in the cache in case creating the new instance
            // fails
            _oConfiguredModelFactoryMap.remove( oModelFactoryClass.getName() );

            // create a new factory class instance and cache
            oModelFactory = createConfiguredModelFactory( oModelFactoryClass );

            _oConfiguredModelFactoryMap.put( oModelFactoryClass.getName(), oModelFactory );
         }
      }

      return oModelFactory;
   }

   /**
    * Use to check if the specified model factory classname has been refined since it was last used by this pool
    *
    * @param sModelFactoryClassName the name of model factory to check
    * @return true if the model factory classname has been refined since it was last used by this pool
    * @throws ClassNotFoundException propagated exception
    */
   public boolean isConfiguredModelFactoryStale( String sModelFactoryClassName )
      throws ClassNotFoundException
   {
      // first load the class (the class loader caches)
      Class oModelFactoryClass = _oClassLoader.loadClass( sModelFactoryClassName );

      // see if we have a cached instance of the factory class
      Object   oModelFactory  = _oConfiguredModelFactoryMap.get( oModelFactoryClass.getName() );

      // ( oModelFactory == null ) is unexpected unless we dynamically reload model def configs so we consider it stale
      // if we encounter this situation, since it is more future safe to consider it stale in this case

      return ( ( oModelFactory == null ) || ( oModelFactory.getClass().hashCode() != oModelFactoryClass.hashCode() ) );
   }

   private Object createConfiguredModelFactory( Class oModelFactoryClass )
      throws ConstructorNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException
   {
      return ObjectFactory.createObject( oModelFactoryClass, new Object[]{ _oServletConfig, _oLogger } );
   }
}

// EOF