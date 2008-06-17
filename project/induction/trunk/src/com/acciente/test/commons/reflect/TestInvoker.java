package com.acciente.test.commons.reflect;

import com.acciente.commons.reflect.Invoker;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Log
 * Mar 15, 2008 APR  -  created
 */
public class TestInvoker
{
   public static void main( String[] asArgs ) throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException
   {
      System.out.println( "Commons - Reflect - Invoker" );

      String sClassName = "com.acciente.test.commons.reflect.TestInvoker.InvokeTestTarget";

      System.out.println( "invoker-test: loading class: " + sClassName );
      Class oClass = Class.forName( sClassName );

      Constructor[] oConstructors = oClass.getConstructors();
      System.out.println( "invoker-test: constructor[0]: " + oConstructors[ 0 ] );

      Object oInstance = Invoker.invoke( oConstructors[ 0 ], new Object[]{ "String-Value", new Integer( 2008 ), new Float( 19.1817 ) } );
      System.out.println( "invoker-test: instance: " + oInstance );
   }

   public static class InvokeTestTarget
   {
      public InvokeTestTarget( String sParam_1, String sParam_2, Integer iParam_3, Float fParam_4, int iParam_5 )
      {
         System.out.println( "sParam_1: " + sParam_1 );
         System.out.println( "sParam_2: " + sParam_2 );
         System.out.println( "iParam_3: " + iParam_3 );
         System.out.println( "fParam_4: " + fParam_4 );
         System.out.println( "iParam_5: " + iParam_5 );
      }
   }
}

// EOF