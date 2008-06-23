/**
 *   Copyright 2008 Acciente, LLC
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
package com.acciente.induction.util;

import com.acciente.commons.reflect.Invoker;
import com.acciente.commons.reflect.ParameterProvider;
import com.acciente.commons.reflect.ParameterProviderException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class creates (and destroys) objects using a uniform convention
 *
 * Log
 * Mar 25, 2008 APR  -  created
 */
public class ObjectFactory
{
   public static Object createObject( Class oClass, Object[] aoInitArgs, ParameterProvider oParameterProvider )
      throws InvocationTargetException, ParameterProviderException, InstantiationException, IllegalAccessException, ConstructorNotFoundException
   {
      Object oObject = Invoker.invoke( ReflectUtils.getSingletonConstructor( oClass ), aoInitArgs, oParameterProvider );

      Method oOptionalInitializer = null;
      try
      {
         oOptionalInitializer = ReflectUtils.getSingletonMethod( oObject.getClass(), "init" );
      }
      catch ( MethodNotFoundException e )
      {
         // ok if no additional initializer is defined
      }

      // if we found a single public method
      if ( oOptionalInitializer != null )
      {
         Invoker.invoke( oOptionalInitializer, oObject, aoInitArgs, oParameterProvider );
      }

      return oObject;
   }

   public static void destroyObject( Object oTargetObject )
      throws InvocationTargetException, IllegalAccessException
   {
      Method oDestructorMethod = null;

      try
      {
         oDestructorMethod = ReflectUtils.getSingletonMethod( oTargetObject.getClass(), "destroy" );
      }
      catch ( MethodNotFoundException e )
      {
         // ok if no destructor is defined
      }

      // if we found a single public method, use it only it expects no parameters
      if ( oDestructorMethod != null && oDestructorMethod.getParameterTypes().length == 0 )
      {
         oDestructorMethod.invoke( oTargetObject, new Object[]{} );
      }
   }
}

// EOF