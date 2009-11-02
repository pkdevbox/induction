/**
 *   Copyright 2009 Acciente, LLC
 *
 *   Acciente, LLC licenses this file to you under the
 *   Apache License, Version 2.0 (the "License"); you
 *   may not use this file except in compliance with the
 *   License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in
 *   writing, software distributed under the License is
 *   distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 *   OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing
 *   permissions and limitations under the License.
 */
package com.acciente.induction.dispatcher.model;

import com.acciente.commons.reflect.Invoker;
import com.acciente.commons.reflect.ParameterProvider;
import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.init.config.Config;
import com.acciente.induction.template.TemplatingEngine;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.MethodNotFoundException;
import com.acciente.induction.util.ObjectFactory;
import com.acciente.induction.util.ReflectUtils;
import com.acciente.induction.controller.Form;
import com.acciente.induction.controller.HTMLForm;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * Internal.
 * This class is the factory used to instantiate new Model object instances
 *
 * @created Mar 15, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ModelFactory
{
   private  ClassLoader                   _oClassLoader;
   private  ServletConfig                 _oServletConfig;
   private  TemplatingEngine              _oTemplatingEngine;
   private  Config.FileUpload             _oFileUploadConfig;

   private  ConfiguredModelFactoryPool    _oConfiguredModelFactoryPool;
   private  ModelPool                     _oModelPool;
   private  ThreadLocal                   _oCreateInProgressModelClassNameSet  = new ModelClassNameSet();

   public ModelFactory( ClassLoader       oClassLoader,
                        ServletConfig     oServletConfig,
                        TemplatingEngine  oTemplatingEngine,
                        Config.FileUpload oFileUploadConfig )
   {
      _oClassLoader      = oClassLoader;
      _oServletConfig    = oServletConfig;
      _oTemplatingEngine = oTemplatingEngine;
      _oFileUploadConfig = oFileUploadConfig;

      _oConfiguredModelFactoryPool = new ConfiguredModelFactoryPool( oClassLoader, oServletConfig );
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
      throws InvocationTargetException, ConstructorNotFoundException, ParameterProviderException, IllegalAccessException, InstantiationException, MethodNotFoundException, ClassNotFoundException
   {
      // we keep track of the model classname we are loading in a thread local set, this allows
      // us to detect cyclic dependencies which would otherwise cause infinite recursion
      Set oCreateInProgressModelClassNameSet = ( Set ) _oCreateInProgressModelClassNameSet.get();

      if ( oCreateInProgressModelClassNameSet.contains( oModelDef.getModelClassName() ) )
      {
         throw new IllegalArgumentException( "model-create-error: cyclical dependency detected between model: "
                                             + oModelDef.getModelClassName()
                                             + " and model(s): "
                                             + oCreateInProgressModelClassNameSet );
      }

      oCreateInProgressModelClassNameSet.add( oModelDef.getModelClassName() );

      Object   oModel;

      try
      {
         Object[]                oParameterValues        = new Object[]{ _oServletConfig, oModelDef, oHttpServletRequest, _oTemplatingEngine, _oClassLoader };
         ModelParameterProvider  oModelParameterProvider = new ModelParameterProvider( _oModelPool, _oFileUploadConfig, oHttpServletRequest );

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
      }
      finally
      {
         ( ( Set ) _oCreateInProgressModelClassNameSet.get() ).remove( oModelDef.getModelClassName() );
      }

      return oModel;
   }

   public boolean isModelStale( Config.ModelDefs.ModelDef oModelDef, Object oModel )
      throws ClassNotFoundException, ConstructorNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException
   {
      boolean bStale;

      if ( oModelDef.hasModelFactoryClassName() )
      {
         // we have a factory class, also check if the factory class reloaded since the model was created
         bStale = _oConfiguredModelFactoryPool.isConfiguredModelFactoryStale( oModelDef.getModelFactoryClassName() ); // todo: reinspect this line
      }
      else
      {
         // first check if the model class has been reloaded since this model was created
         bStale = _oClassLoader.loadClass( oModelDef.getModelClassName() ).hashCode() != oModel.getClass().hashCode();
      }

      return bStale;
   }

   /**
    * Internal.
    */
   private static class ModelParameterProvider implements ParameterProvider
   {
      private ModelPool          _oModelPool;
      private HttpServletRequest _oHttpServletRequest;
      private Config.FileUpload  _oFileUploadConfig;

      public ModelParameterProvider( ModelPool oModelPool, Config.FileUpload oFileUploadConfig, HttpServletRequest oHttpServletRequest )
      {
         _oModelPool          = oModelPool;
         _oHttpServletRequest = oHttpServletRequest;
         _oFileUploadConfig   = oFileUploadConfig;
      }

      public Object getParameter( Class oParamClass ) throws ParameterProviderException
      {
         final String sMessagePrefix = "model-factory: error resolving value for type: ";

         Object   oParamValue = null;

         try
         {
            if ( oParamClass.isAssignableFrom( Form.class ) && _oHttpServletRequest != null )
            {
               // NOTE: since the HTMLForm is per-request no caching is needed, since parameters
               // are resolved before controller invocation, and become local variables in the
               // controller for the duration of the request
               oParamValue = new HTMLForm( _oHttpServletRequest, _oFileUploadConfig );
            }
            else if ( oParamClass.isAssignableFrom( HttpServletRequest.class ) && _oHttpServletRequest != null )
            {
               oParamValue = _oHttpServletRequest;
            }
            else
            {
               oParamValue = _oModelPool.getModel( oParamClass.getName(), _oHttpServletRequest );
            }

            if ( oParamValue == null )
            {
               throw ( new ParameterProviderException( sMessagePrefix + oParamClass ) );
            }

            return oParamValue;
         }
         catch ( MethodNotFoundException e )
         {  throw new ParameterProviderException( sMessagePrefix + oParamClass, e );     }
         catch ( InvocationTargetException e )
         {  throw new ParameterProviderException( sMessagePrefix + oParamClass, e );     }
         catch ( ClassNotFoundException e )
         {  throw new ParameterProviderException( sMessagePrefix + oParamClass, e );     }
         catch ( ConstructorNotFoundException e )
         {  throw new ParameterProviderException( sMessagePrefix + oParamClass, e );     }
         catch ( IllegalAccessException e )
         {  throw new ParameterProviderException( sMessagePrefix + oParamClass, e );     }
         catch ( InstantiationException e )
         {  throw new ParameterProviderException( sMessagePrefix + oParamClass, e );     }
      }
   }

   private static class ModelClassNameSet extends ThreadLocal
   {
      public synchronized Object initialValue()
      {
         return new HashSet();
      }
   }
}

// EOF