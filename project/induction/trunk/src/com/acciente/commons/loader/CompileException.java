package com.acciente.commons.loader;

/**
 * Log
 * Feb 23, 2008 APR  -  created
 */
public class CompileException extends Throwable
{
   public CompileException( String sMessage )
   {
      super( sMessage );
   }

   public CompileException( String sMessage, Throwable oCause )
   {
      super( sMessage, oCause );
   }
}
