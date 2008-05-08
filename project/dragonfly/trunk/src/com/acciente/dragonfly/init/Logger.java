package com.acciente.dragonfly.init;

/**
 * This interface is used to provide access to centralized logger with having
 * to pass in the entire servlet object (which has the log() method) to each
 * class/method that needs access to the logger.
 *
 * Log
 * Mar 16, 2008 APR  -  created
 */
// todo: investigate the use of a more standard logging api
public interface Logger
{
   void log( String sMessage );

   void log( String sMessage, Throwable oThrowable );
}
