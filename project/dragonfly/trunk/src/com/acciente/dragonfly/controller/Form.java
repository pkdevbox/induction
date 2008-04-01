package com.acciente.dragonfly.controller;

import com.acciente.dragonfly.dispatcher.form.File;

/**
 * This interface provides access to all parameters submitted as part of the request. Both GET and
 * POST parameters are accesible. If the same parameter is defined via GET and POST the value sent
 * via POST prevails.
 *
 * Log
 * Feb 25, 2008 APR  -  created
 */
public interface Form
{
   String   getString( String sParamName );

   int      getInteger( String sParamName );

   float    getFloat( String sParamName );

   long     getLong( String sParamName );

   boolean  getBoolean( String sParamName );

   File     getFile( String sParamName );
}

// EOF