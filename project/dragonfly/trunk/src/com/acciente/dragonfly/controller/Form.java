package com.acciente.dragonfly.controller;

import com.acciente.dragonfly.dispatcher.form.File;

/**
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