package com.acciente.nitrogen.init.config;

/**
 * Log
 * Apr 26, 2008 APR  -  created
 */
public class ConfigLoaderException extends Exception
{
   public ConfigLoaderException( String sMessage )
   {
      super( sMessage );
   }

   public ConfigLoaderException( String sMessage, Throwable oCause )
   {
      super( sMessage, oCause );
   }
}

// EOF