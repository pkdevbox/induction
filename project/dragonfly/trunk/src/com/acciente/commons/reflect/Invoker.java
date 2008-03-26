package com.acciente.commons.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class contains java reflection helper methods  to invoke constructors and
 * methods using supplied arguments being positioned using type compatibility.
 *
 * Log
 * Mar 15, 2008 APR  -  created
 */
public class Invoker
{
   /**
    * This method calls the specified class constructor using the specified args as follows.
    * Each arg in aoArgs is expected to be of a unique type, if there is more than one value
    * with the same type the first value is used.
    *
    * This method provides a value to each parameter of the constructor from aoArgs based on
    * the type of the expected parameter.
    *
    * @param oConstructor a constructor object to be invoked
    * @param aoArgs an array containing a set of arguments each of a distinct type
    *
    * @return the new object instance created by calling the constructor
    *
    * @throws IllegalAccessException propagated from Constructor.newInstance() call
    * @throws InstantiationException propagated from Constructor.newInstance() call
    * @throws InvocationTargetException propagated from Constructor.newInstance() call
    */
   public static Object invoke( Constructor oConstructor, Object[] aoArgs )
      throws InvocationTargetException, IllegalAccessException, InstantiationException
   {
      Class[]  aoParameterTypes  = oConstructor.getParameterTypes();
      Object[] aoParameterValues = new Object[ aoParameterTypes.length ];

      if ( aoParameterValues.length != 0 )
      {
         // build the arg-list to the constructor positioning arg values provided
         // based matching up the parameter type to the type of the value provided
         for ( int i = 0; i < aoParameterValues.length; i++ )
         {
            aoParameterValues[ i ] = getByType( aoArgs, aoParameterTypes[ i ] );
         }
      }

      return oConstructor.newInstance( aoParameterValues );
   }

   /**
    * This method calls the specified method using the specified args as follows.
    * Each arg in aoArgs is expected to be of a unique type, if there is more than one value
    * with the same type the first value is used.
    *
    * This method provides a value to each parameter of the method from aoArgs based on
    * the type of the expected parameter.
    *
    * @param oMethod a method object to be invoked
    * @param oTarget the target object on which the method should be invoked
    * @param aoArgs an array containing a set of arguments each of a distinct type
    *
    * @return the value returned by the called method
    *
    * @throws IllegalAccessException propagated from Method.invoke()
    * @throws InvocationTargetException propagated from Method.invoke()
    */
   public static Object invoke( Method oMethod, Object oTarget, Object[] aoArgs )
      throws InvocationTargetException, IllegalAccessException
   {
      Class[]  aoParameterTypes  = oMethod.getParameterTypes();
      Object[] aoParameterValues = new Object[ aoParameterTypes.length ];

      if ( aoParameterValues.length != 0 )
      {
         // build the arg-list to the constructor positioning arg values provided
         // based matching up the parameter type to the type of the value provided
         for ( int i = 0; i < aoParameterValues.length; i++ )
         {
            aoParameterValues[ i ] = getByType( aoArgs, aoParameterTypes[ i ] );
         }
      }

      return oMethod.invoke( oTarget, aoParameterValues );
   }

   /**
    * This method returns the first element in the specified array that matches the specified type.
    *
    * @param aoArgs an array of values
    * @param oType the type to search within the array of values
    * @return the element from the array that matches the specified type
    */
   private static Object getByType( Object[] aoArgs, Class oType )
   {
      Object oArgMatch = null;

      if ( oType.isPrimitive() )
      {
         for ( int i = 0; i < aoArgs.length; i++ )
         {
            if ( aoArgs[ i ] != null && isAssignableToPrimitive( aoArgs[ i ].getClass(), oType ) )
            {
               oArgMatch = aoArgs[ i ];
               break;
            }
         }
      }
      else
      {
         for ( int i = 0; i < aoArgs.length; i++ )
         {
            if ( aoArgs[ i ] != null && aoArgs[ i ].getClass().equals( oType ) )
            {
               oArgMatch = aoArgs[ i ];
               break;
            }
         }
      }

      return oArgMatch;
   }

   private static boolean isAssignableToPrimitive( Class oClassType, Class oPrimitiveType )
   {
      return ( ( oPrimitiveType == Boolean.TYPE && oClassType.equals( Boolean.class ) )
               || ( oPrimitiveType == Character.TYPE && oClassType.equals( Character.class ) )
               || ( oPrimitiveType == Byte.TYPE && oClassType.equals( Byte.class ) )
               || ( oPrimitiveType == Short.TYPE && oClassType.equals( Short.class ) )
               || ( oPrimitiveType == Integer.TYPE && oClassType.equals( Integer.class ) )
               || ( oPrimitiveType == Long.TYPE && oClassType.equals( Long.class ) )
               || ( oPrimitiveType == Float.TYPE && oClassType.equals( Float.class ) )
               || ( oPrimitiveType == Double.TYPE && oClassType.equals( Double.class ) )
             );
   }
}

// EOF