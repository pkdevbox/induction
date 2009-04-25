/**
 *   Copyright 2009 Acciente, LLC
 *
 *   Acciente, LLC licenses this file to you under the
 *   Apache License, Version 2.0 (the "License"); you
 *   may not use this file except in compliance with the
 *   License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in
 *   writing, software distributed under the License is
 *   distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 *   OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing
 *   permissions and limitations under the License.
 */
package com.acciente.induction.controller;

import com.acciente.commons.htmlform.FileHandle;
import com.acciente.commons.htmlform.ParserException;
import org.apache.commons.fileupload.FileUploadException;

import java.io.IOException;
import java.util.Set;

/**
 * This interface provides access to the HTML form submitted as part of the request (if any). Both GET and
 * POST parameters are accesible. If the same parameter is defined via GET and POST the value sent
 * via POST prevails.
 *
 * @created Feb 25, 2008
 *
 * @author Adinath Raveendra Raj
 *
 * todo: remove dependency on FileUploadException
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
   Set getParamNames() throws IOException, FileUploadException, ParserException;

   /**
    * Used to check if the specified HTML form has the specified parameter. Parameter names are case-sensitive.
    * @param sParamName the name of the HTML form parameter
    * @return true if the parameter is defined in the form, false otherwise
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws HTMLFormException
    */
   boolean containsParam( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException;
}

// EOF