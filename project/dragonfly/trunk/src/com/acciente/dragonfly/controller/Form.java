package com.acciente.dragonfly.controller;

import com.acciente.commons.htmlform.FileHandle;
import com.acciente.commons.htmlform.ParserException;

import java.io.IOException;

import org.apache.commons.fileupload.FileUploadException;

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
   String      getString( String sParamName ) throws IOException, FileUploadException, ParserException;

   int         getInteger( String sParamName ) throws IOException, FileUploadException, ParserException;

   float       getFloat( String sParamName ) throws IOException, FileUploadException, ParserException;

   long        getLong( String sParamName ) throws IOException, FileUploadException, ParserException;

   boolean     getBoolean( String sParamName ) throws IOException, FileUploadException, ParserException;

   FileHandle  getFile( String sParamName ) throws IOException, FileUploadException, ParserException;
}

// EOF