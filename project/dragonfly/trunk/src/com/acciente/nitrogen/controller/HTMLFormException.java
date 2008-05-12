package com.acciente.nitrogen.controller;

/**
 * This class ...
 *
 * Log
 * Apr 16, 2008 APR  -  created
 */
public class HTMLFormException extends Exception
{
   public HTMLFormException( String sMessage )
   {
      super( sMessage );
   }

   public HTMLFormException( String sMessage, Throwable oCause )
   {
      super( sMessage, oCause );
   }
}

// EOF