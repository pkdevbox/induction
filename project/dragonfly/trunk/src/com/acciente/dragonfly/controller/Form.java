package com.acciente.dragonfly.controller;

import com.acciente.commons.htmlform.FileHandle;
import com.acciente.commons.htmlform.ParserException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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
   /**
    * Return the value of the specified HTML form parameter as an object value
    * @param sParamName the name of the HTML form parameter
    * @return a string value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws HTMLFormException
    */
   Object      getObject( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException;

   /**
    * Return the value of the specified HTML form parameter as a string value
    * @param sParamName the name of the HTML form parameter
    * @return a string value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws HTMLFormException
    */
   String      getString( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException;

   /**
    * Return the value of the specified HTML form parameter as an integer value
    * @param sParamName the name of the HTML form parameter
    * @return a string value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws HTMLFormException
    */
   int         getInteger( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException;

   /**
    * Return the value of the specified HTML form parameter as a float value
    * @param sParamName the name of the HTML form parameter
    * @return a string value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws HTMLFormException
    */
   float       getFloat( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException;

   /**
    * Return the value of the specified HTML form parameter as a long value
    * @param sParamName the name of the HTML form parameter
    * @return a string value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws HTMLFormException
    */
   long        getLong( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException;

   /**
    * Return the value of the specified HTML form parameter as a boolean value
    * @param sParamName the name of the HTML form parameter
    * @return a string value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws HTMLFormException
    */
   boolean     getBoolean( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException;

   /**
    * Return the value of the specified HTML form parameter as a file handle, this method should only be used to access
    * a form parameter that represents an uploded file
    * @param sParamName the name of the HTML form parameter
    * @return a string value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws HTMLFormException
    */
   FileHandle  getFile( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException;

   /**
    * Returns a list of the parameter names in the HTML form
    * @return a list containing strings, each string being a parameter name
    */
   Set getParamNames();
}

// EOF