package com.acciente.dragonfly.dispatcher.controller;

/**
 * This class ...
 *
 * Log
 * Apr 17, 2008 APR  -  created
 */
public class ControllerExecutorException extends Exception
{
   public ControllerExecutorException( String sMessage )
   {
      super( sMessage );
   }

   public ControllerExecutorException( String sMessage, Throwable oCause )
   {
      super( sMessage, oCause );
   }
}

// EOF