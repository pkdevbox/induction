package com.acciente.induction.dispatcher.model;

import com.acciente.commons.reflect.Invoker;
import com.acciente.commons.reflect.ParameterProvider;
import com.acciente.induction.init.Logger;
import com.acciente.induction.init.config.Config;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.ObjectFactory;
import com.acciente.induction.util.ReflectUtils;

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
   private ModelPool                   _oModelPool;

   public ModelFactory( ClassLoader oClassLoader, ServletConfig oServletConfig, Logger oLogger )
   {
      _oClassLoader      = oClassLoader;
      _oServletConfig    = oServletConfig;
      _oLogger           = oLogger;

      _oConfiguredModelFactoryPool = new ConfiguredModelFactoryPool( oClassLoader, oServletConfig, oLogger );
   }

   /**
    * Used to set a model pool for use in model-to-model dendency injection (note the cyclic relationship
    * between ModelFactory class and the ModelPool class, also a the same relationship between the object
    * instances)
    *
    * @param oModelPool a model pool instance
    */
   public void setModelPool( ModelPool oModelPool )
   {
      _oModelPool = oModelPool;
   }

   public Object createModel( Config.ModelDefs.ModelDef oModelDef, HttpServletRequest oHttpServletRequest )
      throws Exception
   {
      Object   oModel;

      Object[]                oParameterValues        = new Object[]{ _oServletConfig, _oLogger,  oModelDef, oHttpServletRequest };
      ModelParameterProvider  oModelParameterProvider = new ModelParameterProvider( _oModelPool, oHttpServletRequest );

      // does this model class have a factory class defined?
      if ( ! oModelDef.hasModelFactoryClassName() )
      {
         // no factory class, then we expect a single public constructor, which we use to
         // instantiate the model via a a direct parameter injected constructor call
         oModel
            =  ObjectFactory.createObject( _oClassLoader.loadClass( oModelDef.getModelClassName() ),
                                           oParameterValues, oModelParameterProvider );
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
                           oParameterValues,
                           oModelParameterProvider );
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

   public static class ModelParameterProvider implements ParameterProvider
   {
      private ModelPool          _oModelPool;
      private HttpServletRequest _oHttpServletRequest;

      public ModelParameterProvider( ModelPool oModelPool, HttpServletRequest oHttpServletRequest )
      {
         _oModelPool = oModelPool;
         _oHttpServletRequest = oHttpServletRequest;
      }

      public Object getParameter( Class oValueType ) throws Exception
      {
         return _oModelPool.getModel( oValueType.getName(), _oHttpServletRequest );
      }
   }
}

// EOF