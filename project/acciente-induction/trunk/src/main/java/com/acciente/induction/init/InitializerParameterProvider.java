/*
 * Copyright 2008-2011 Acciente, LLC
 *
 * Acciente, LLC licenses this file to you under the
 * Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.acciente.induction.init;

import com.acciente.commons.reflect.ParameterProvider;
import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.dispatcher.model.ModelPool;
import com.acciente.induction.util.MethodNotFoundException;
import com.acciente.induction.util.ConstructorNotFoundException;

import java.lang.reflect.InvocationTargetException;

/**
 * InitializerParameterProvider, used by the initializer classes to inject models
 * into the intializer constructor.
 *
 * @created Sep 15, 2008
 * @author  Adinath Raveendra Raj
 */
public class InitializerParameterProvider implements ParameterProvider
{
   private  ModelPool      _oModelPool;
   private  String         _sMessagePrefix;

   public InitializerParameterProvider( ModelPool oModelPool, String sMessagePrefix )
   {
      _oModelPool       = oModelPool;
      _sMessagePrefix   = sMessagePrefix;
   }

   public Object getParameter( Class oParamClass ) throws ParameterProviderException
   {
      try
      {
         return _oModelPool.getModel( oParamClass, null );
      }
      catch ( MethodNotFoundException e )
      {  throw new ParameterProviderException( _sMessagePrefix + ": error resolving value for type: " + oParamClass, e );     }
      catch ( InvocationTargetException e )
      {  throw new ParameterProviderException( _sMessagePrefix + ": error resolving value for type: " + oParamClass, e );     }
      catch ( ClassNotFoundException e )
      {  throw new ParameterProviderException( _sMessagePrefix + ": error resolving value for type: " + oParamClass, e );     }
      catch ( ConstructorNotFoundException e )
      {  throw new ParameterProviderException( _sMessagePrefix + ": error resolving value for type: " + oParamClass, e );     }
      catch ( IllegalAccessException e )
      {  throw new ParameterProviderException( _sMessagePrefix + ": error resolving value for type: " + oParamClass, e );     }
      catch ( InstantiationException e )
      {  throw new ParameterProviderException( _sMessagePrefix + ": error resolving value for type: " + oParamClass, e );     }
   }
}

// EOF