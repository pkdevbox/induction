package com.acciente.test.misc;

/**
 * Created by IntelliJ IDEA.
 * User: adinath
 * Date: Mar 18, 2008
 * Time: 4:14:33 PM
 * To change this template use File | Settings | File Templates.
 */
class Class1
{
   void someMethod_A()
   {
      System.out.println( "Class 1: someMethod_A() called" );

      someMethod_B();
   }

   void someMethod_B()
   {
      System.out.println( "Class 1: someMethod_B() called" );
   }
}
