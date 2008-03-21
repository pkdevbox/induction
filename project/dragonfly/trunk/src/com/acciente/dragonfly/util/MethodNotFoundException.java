package com.acciente.dragonfly.util;

/**
 * This class ...
 *
 * Log
 * Mar 20, 2008 APR  -  created
 */
public class MethodNotFoundException extends Exception
{
   public MethodNotFoundException( String sMessage )
   {
      super( sMessage );
   }

   public MethodNotFoundException( String sMessage, Throwable oCause )
   {
      super( sMessage, oCause );
   }
}
