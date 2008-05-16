package com.acciente.dragonfly.util;

import com.acciente.commons.reflect.Invoker;

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
   public static Object createObject( Class oClass, Object[] aoInitArgs )
      throws ConstructorNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException
   {
      Object oObject = Invoker.invoke( ReflectUtils.getSingletonConstructor( oClass ), aoInitArgs );

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
         Invoker.invoke( oOptionalInitializer, oObject, aoInitArgs );
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