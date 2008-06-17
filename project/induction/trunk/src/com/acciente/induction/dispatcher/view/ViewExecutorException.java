package com.acciente.induction.dispatcher.view;

/**
 * Log
 * Apr 26, 2008 APR  -  created
 */
public class ViewExecutorException extends Exception
{
   public ViewExecutorException( String sMessage )
   {
      super( sMessage );
   }

   public ViewExecutorException( String sMessage, Throwable oCause )
   {
      super( sMessage, oCause );
   }
}
