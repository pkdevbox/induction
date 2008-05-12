package com.acciente.nitrogen.init.config.xmlconfigloader;

import com.acciente.nitrogen.init.config.ConfigLoaderException;

/**
 * XMLConfigLoaderException
 *
 * Log
 * May 8, 2008 APR  -  created
 */
public class XMLConfigLoaderException extends ConfigLoaderException
{
   public XMLConfigLoaderException( String sMessage )
   {
      super( sMessage );
   }

   public XMLConfigLoaderException( String sMessage, Throwable oCause )
   {
      super( sMessage, oCause );
   }
}

// EOF