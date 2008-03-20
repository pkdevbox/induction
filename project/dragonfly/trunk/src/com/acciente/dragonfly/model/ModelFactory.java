package com.acciente.dragonfly.model;

import com.acciente.commons.reflect.Invoker;
import com.acciente.dragonfly.init.config.Config;
import com.acciente.dragonfly.init.Logger;
import com.acciente.dragonfly.util.ReflectUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Log
 * Mar 15, 2008 APR  -  created
 */
public class ModelFactory
{
   private ClassLoader     _oClassLoader;
   private ServletContext  _oServletContext;
   private ServletConfig   _oServletConfig;
   private Logger          _oLogger;
   private Map             _oFactoryInstanceMap;

   public ModelFactory( ClassLoader oClassLoader, ServletContext oServletContext, ServletConfig oServletConfig, Logger oLogger )
   {
      _oClassLoader     = oClassLoader;
      _oServletContext  = oServletContext;
      _oServletConfig   = oServletConfig;
      _oLogger          = oLogger;

      _oFactoryInstanceMap = new Hashtable(); // Hashtable for concurrency safety
   }

   public Object createModel( Config.ModelDefs.ModelDef oModelDef, HttpServletRequest oHttpServletRequest )
      throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException
   {
      Object   oModel;

      // check is this model class has a factory class defined
      if ( oModelDef.getModelFactoryClassName() == null || oModelDef.getModelFactoryClassName().isEmpty() )
      {
         // no factory class, then we expect a single public constructor, which we use to
         // instantiate the model via a a direct parameter injected constructor call
         // try to load the controller class

         oModel
            =  Invoker
                  .invoke( ReflectUtils
                                       .getSingletonConstructor( _oClassLoader.loadClass( oModelDef.getModelClassName() ) ),
                                    new Object[]
                                    {  _oServletContext,
                                       _oServletConfig,
                                       _oLogger
                                    }
                                  );
      }
      else
      {
         // ok we have a factory class things are a little fancier here, first we have to setup the factory class

         // first get an instance of the factory class
         Object   oFactoryInstance = getFactoryInstance( oModelDef.getModelClassName() );

         // next call the createModel() method on the factory instance

         oModel = null;

         // todo: fix call below

//         oModel
//            =  Invoker
//                  .invokeMethod( ReflectUtils
//                                    .getSingletonMethod( oFactoryInstance.getClass(), "createModel", true ),
//                                 oFactoryInstance,
//                                 new Object[]
//                                 {  _oServletContext,
//                                    _oServletConfig,
//                                    _oLogger,
//                                    oModelDef,
//                                    oHttpServletRequest
//                                 }
//                               );
      }

      // todo: implement
      // todo-1: pass ModelDef to factory method for all model scopes
      // todo-2: support HttpServletRequest to factory method for request scope models
      // todo-2: hmmm...how to do above? Since request is per thread and no parameters were planned
      // todo-2: for createModel() (is this method name OK?) of factory class, hmmm...i guess it
      // todo-2: just got planned :)
      // todo-3: factory instances need to be cached!!

      return oModel;
   }

   private Object getFactoryInstance( String sFactoryClassName )
      throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException
   {
      // first load the class (the class loader caches)
      Class oFactoryClass = _oClassLoader.loadClass( sFactoryClassName );

      // the instance key is intended to be unique for each factory instance+class, we add the hashCode() to
      // so that if the factory class is dynamically reloaded the factory instance will get reinstantiated
      String   sFactoryInstanceKey  = oFactoryClass.getName() + oFactoryClass.hashCode();

      // see if we have a cached instance of the factory class
      Object   oFactoryInstance     = _oFactoryInstanceMap.get( sFactoryInstanceKey );

      if ( oFactoryInstance == null )
      {
         // no cached instance, create a new factory class instance and cache
         oFactoryInstance
            =  Invoker
                  .invoke( ReflectUtils
                                       .getSingletonConstructor( oFactoryClass ),
                                       new Object[]
                                       {  _oServletContext,
                                          _oServletConfig,
                                          _oLogger
                                       }
                                    );

         _oFactoryInstanceMap.put( sFactoryInstanceKey, oFactoryInstance );
      }

      return oFactoryInstance;
   }
}

// EOF