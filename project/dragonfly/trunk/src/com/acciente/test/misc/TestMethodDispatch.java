package com.acciente.test.misc;

/**
 * Log
 * Feb 23, 2008 APR  -  created
 */
public class TestMethodDispatch
{
   public static void main( String[] asArgs )
   {
      System.out.println( "TestMethodDispatch - start" );

      Class2 o = new Class2();

      o.someMethod_A();
   }
}

// EOF