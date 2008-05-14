package com.acciente.dragonfly.dispatcher.model;

import com.acciente.commons.reflect.Invoker;
import com.acciente.dragonfly.init.Logger;
import com.acciente.dragonfly.init.config.Config;
import com.acciente.dragonfly.util.ConstructorNotFoundException;
import com.acciente.dragonfly.util.MethodNotFoundException;
import com.acciente.dragonfly.util.ObjectFactory;
import com.acciente.dragonfly.util.ReflectUtils;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

/**
 * This class is the factory used to instantiate new Model object instances
 *
 * Log
 * Mar 15, 2008 APR  -  created
 */
public class ModelFactory
{
   private ClassLoader                 _oClassLoader;
   private ServletConfig               _oServletConfig;
   private Logger                      _oLogger;
   private ConfiguredModelFactoryPool  _oConfiguredModelFactoryPool;

   public ModelFactory( ClassLoader oClassLoader, ServletConfig oServletConfig, Logger oLogger )
   {
      _oClassLoader      = oClassLoader;
      _oServletConfig    = oServletConfig;
      _oLogger           = oLogger;

      _oConfiguredModelFactoryPool = new ConfiguredModelFactoryPool( oClassLoader, oServletConfig, oLogger );
   }

   public Object createModel( Config.ModelDefs.ModelDef oModelDef, HttpServletRequest oHttpServletRequest )
      throws ClassNotFoundException, MethodNotFoundException, ConstructorNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException
   {
      Object   oModel;

      // does this model class have a factory class defined?
      if ( ! oModelDef.hasModelFactoryClassName() )
      {
         // no factory class, then we expect a single public constructor, which we use to
         // instantiate the model via a a direct parameter injected constructor call
         oModel
            =  ObjectFactory.createObject( _oClassLoader.loadClass( oModelDef.getModelClassName() ),
                                           new Object[]{  _oServletConfig, _oLogger } );
      }
      else
      {
         // we have a factory class, so a few more steps in this case

         // first get a model factory instance
         Object   oConfiguredModelFactory = _oConfiguredModelFactoryPool.getConfiguredModelFactory( oModelDef.getModelFactoryClassName() );

         // next call the createModel() method on the factory instance
         oModel
            =  Invoker
                  .invoke( ReflectUtils.getSingletonMethod( oConfiguredModelFactory.getClass(), "createModel", true ),
                           oConfiguredModelFactory,
                           new Object[]
                           {  _oServletConfig,
                              _oLogger,
                              oModelDef,
                              oHttpServletRequest
                           }
                         );
      }

      return oModel;
   }

   public boolean isModelStale( Config.ModelDefs.ModelDef oModelDef, Object oModel )
      throws ClassNotFoundException, ConstructorNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException
   {
      boolean bStale;

      // first check if the model class has been reloaded since this model was created
      bStale = _oClassLoader.loadClass( oModelDef.getModelClassName() ).hashCode() != oModel.getClass().hashCode();

      if ( ! bStale && oModelDef.hasModelFactoryClassName() )
      {
         // todo: reinspect below
         // we have a factory class, also check if the factory class reloaded since the model was created
         bStale = _oConfiguredModelFactoryPool.isConfiguredModelFactoryStale( oModelDef.getModelFactoryClassName() ); 
      }

      return bStale;
   }
}

// EOF