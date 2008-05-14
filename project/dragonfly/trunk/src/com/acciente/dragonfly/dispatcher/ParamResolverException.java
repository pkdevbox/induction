package com.acciente.dragonfly.dispatcher;

/**
 * Log
 * Apr 14, 2008 APR  -  created
 */
public class ParamResolverException extends Exception
{
   public ParamResolverException( String sMessage )
   {
      super( sMessage );
   }

   public ParamResolverException( String sMessage, Throwable oCause )
   {
      super( sMessage, oCause );
   }
}

// EOF