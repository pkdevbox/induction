package com.acciente.commons.reflect;

/**
 * Log
 * Jun 21, 2008 APR  -  created
 */
public class ParameterProviderException extends Exception
{
   public ParameterProviderException( String sMessage )
   {
      super( sMessage );
   }

   public ParameterProviderException( String sMessage, Throwable oCause )
   {
      super( sMessage, oCause );
   }
}
