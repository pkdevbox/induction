package com.acciente.dragonfly.util;

import com.acciente.commons.reflect.Reflector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Some useful reflection methods.
 *
 * (It is not convincing enough that these methods are general enough to be promoted to the
 * com.acciente.reflect package)
 *
 * Log
 * Mar 16, 2008 APR  -  created
 */
public class ReflectUtils
{
   /**
    * This method introspects into the specified class and checks if the class has only one public
    * constructor. If one one constructor is found it is returned otherwise an IllegalArgumentException
    * is thrown.
    *
    * @param oClass the class to introspect into
    * @return the single public constructor
    * @throws IllegalArgumentException if there are no public constructors or if
    *          there is more than one public constructor
    */
   public static Constructor getSingletonConstructor( Class oClass )
   {
      Constructor[] oConstructors = oClass.getConstructors();

      // check if we have at least one public method with the specified name
      if ( oConstructors.length == 0 )
      {
         throw new IllegalArgumentException( "class " + oClass.getName() + " has no public constructors" );
      }

      // check if we have more than one public method with the specified name
      if ( oConstructors.length > 1 )
      {
         throw new IllegalArgumentException( "class " + oClass.getName() + " has " + oConstructors.length + " public constructors, expected one" );
      }

      return oConstructors[ 0 ];
   }

   /**
    * This method introspects into the specified class and checks if the class has only one public
    * method with the specified name. If one one method is found it is returned otherwise an
    * IllegalArgumentException is thrown.
    *
    * @param oClass the class to introspect into
    * @param sMethodName the name of the method
    * @param bIgnoreCase true if the method search should be case-sensitive
    * @return the single public method found
    * @throws IllegalArgumentException if there are no public methods with the specified name or if
    *          there is more than one public method with the specified name
    */
   public static Method getSingletonMethod( Class oClass, String sMethodName, boolean bIgnoreCase )
      throws IllegalArgumentException
   {
      // use performance enhanced reflection to determine the methods in the class with the specified name
      Method[] oMethods = Reflector.getMethods( oClass, sMethodName, bIgnoreCase );

      // check if we have at least one public method with the specified name
      if ( oMethods.length == 0 )
      {
         throw new IllegalArgumentException( "class " + oClass.getName() + " has no public method named " + sMethodName );
      }

      // check if we have more than one public method with the specified name
      if ( oMethods.length > 1 )
      {
         throw new IllegalArgumentException( "class " + oClass.getName() + " has " + oMethods.length + " public methods named " + sMethodName + ", expected one" );
      }

      return oMethods[ 0 ];
   }
}
