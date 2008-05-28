package com.acciente.test.induction.config;

import com.acciente.induction.init.Logger;

/**
 * This class implments a logger used by the test classes
 *
 * Log
 * Mar 18, 2008 adinath APR  -  created
 */
public class TestLogger implements Logger
{
   public void log( String sMessage )
   {
      System.out.println( sMessage );
   }

   public void log( String sMessage, Throwable oThrowable )
   {
      System.out.println( sMessage );

      oThrowable.printStackTrace( System.out );
   }
}

// EOF