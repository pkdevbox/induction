package com.acciente.induction.util;

/**
 * This class ...
 *
 * Log
 * Mar 20, 2008 APR  -  created
 */
public class ConstructorNotFoundException extends Exception
{
   public ConstructorNotFoundException( String sMessage )
   {
      super( sMessage );
   }

   public ConstructorNotFoundException( String sMessage, Throwable oCause )
   {
      super( sMessage, oCause );
   }
}
// EOF