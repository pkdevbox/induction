package com.acciente.induction.template;

/**
 * TemplatingEngineException.
 *
 * Log
 * @created Jun 25, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class TemplatingEngineException extends Exception
{
   public TemplatingEngineException( String sMessage )
   {
      super( sMessage );
   }

   public TemplatingEngineException( String sMessage, Throwable oCause )
   {
      super( sMessage, oCause );
   }
}
