/*
 * Copyright 2008-2010 Acciente, LLC
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
package com.acciente.induction.util;

import com.acciente.commons.reflect.Reflector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Internal. Some useful reflection methods.
 *
 * (It is not convincing enough at this time that these methods are general enough to
 * be promoted to the com.acciente.reflect package)
 *
 * @created Mar 16, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ReflectUtils
{
   /**
    * This method introspects into the specified class and checks if the class has only one public
    * constructor. If one one constructor is found it is returned otherwise an exception is thrown.
    *
    * @param oClass the class to introspect into
    * @return the single public constructor
    * @throws ConstructorNotFoundException if there are no public constructors or if
    *          there is more than one public constructor
    */
   public static Constructor getSingletonConstructor( Class oClass )
      throws ConstructorNotFoundException
   {
      Constructor[] oConstructors = oClass.getConstructors();

      // check if we have at least one public method with the specified name
      if ( oConstructors.length == 0 )
      {
         throw new ConstructorNotFoundException( "class " + oClass.getName() + " has no public constructors" );
      }

      // check if we have more than one public method with the specified name
      if ( oConstructors.length > 1 )
      {
         throw new ConstructorNotFoundException( "class " + oClass.getName() + " has " + oConstructors.length + " public constructors, expected one" );
      }

      return oConstructors[ 0 ];
   }

   /**
    * This method introspects into the specified class and checks if the class has a public
    * constructor with formal parameters (i.e. a no-arg constructor). If found it is returned
    * otherwise an exception is thrown.
    *
    * @param oClass the class to introspect into
    * @return the no-arg constructor
    * @throws ConstructorNotFoundException if there are no no-arg constructors
    */
   public static Constructor getNoArgConstructor( Class oClass )
      throws ConstructorNotFoundException
   {
      Constructor[] oConstructors = oClass.getConstructors();

      // check if we have at least one public method with the specified name
      if ( oConstructors.length == 0 )
      {
         throw new ConstructorNotFoundException( "class " + oClass.getName() + " has no public constructors" );
      }

      Constructor oConstructor = null;
      for ( int i = 0; i < oConstructors.length; i++ )
      {
         oConstructor = oConstructors[ i ];

         if ( oConstructor.getParameterTypes().length == 0 )
         {
            break;
         }

      }
      // check the best case first
      if ( oConstructor == null )
      {
         throw new ConstructorNotFoundException( "class " + oClass.getName() + " has " + oConstructors.length + " public constructors, but none are no-arg constructors" );
      }

      return oConstructor;
   }

   /**
    * This method introspects into the specified class and checks if the class has only one public
    * method with the specified name. If one one method is found it is returned otherwise an
    * IllegalArgumentException is thrown.
    *
    * @param oClass the class to introspect into
    * @param sMethodName the name of the method (case-sensitive)
    * @return the single public method found
    * @throws MethodNotFoundException if there are no public methods with the specified name or if
    *          there is more than one public method with the specified name
    */
   public static Method getSingletonMethod( Class oClass, String sMethodName )
      throws MethodNotFoundException
   {
      return getSingletonMethod( oClass, sMethodName, false );
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
    * @throws MethodNotFoundException if there are no public methods with the specified name or if
    *          there is more than one public method with the specified name
    */
   public static Method getSingletonMethod( Class oClass, String sMethodName, boolean bIgnoreCase )
      throws MethodNotFoundException
   {
      // use performance enhanced reflection to determine the methods in the class with the specified name
      Method[] oMethods = Reflector.getMethods( oClass, sMethodName, bIgnoreCase );

      // check if we have at least one public method with the specified name
      if ( oMethods.length == 0 )
      {
         throw new MethodNotFoundException( "class " + oClass.getName() + " has no public method named " + sMethodName );
      }

      // check if we have more than one public method with the specified name
      if ( oMethods.length > 1 )
      {
         throw new MethodNotFoundException( "class " + oClass.getName() + " has " + oMethods.length + " public methods named " + sMethodName + ", expected one" );
      }

      return oMethods[ 0 ];
   }
}
