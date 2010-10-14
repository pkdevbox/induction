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
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This interface provides access to the HTML form submitted as part of the request (if any). Both GET and
 * POST parameters are accesible. If the same parameter is defined via GET and POST the value sent
 * via POST prevails.
 *
 * @created Feb 25, 2008
 *
 * @author Adinath Raveendra Raj
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
    * @throws FormException
    */
   Object      getObject( String sParamName ) throws FormException;

   /**
    * Return the value of the specified map HTML form parameter
    * @param sParamName the name of the HTML form parameter
    * @return a map with string keys and data or other maps in the value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws FormException
    */
   Map         getMap( String sParamName ) throws FormException;

   /**
    * Return the value of the specified list HTML form parameter
    * @param sParamName the name of the HTML form parameter
    * @return a list of strings (and/or otehr types as specified on the form)
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws FormException
    */
   List        getList( String sParamName ) throws FormException;

   /**
    * Return the value of the specified HTML form parameter as a string value
    * @param sParamName the name of the HTML form parameter
    * @return a string value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws FormException
    */
   String      getString( String sParamName ) throws FormException;

   /**
    * Return the value of the specified HTML form parameter as an integer value
    * @param sParamName the name of the HTML form parameter
    * @return a string value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws FormException
    */
   int         getInteger( String sParamName ) throws FormException;

   /**
    * Return the value of the specified HTML form parameter as a float value
    * @param sParamName the name of the HTML form parameter
    * @return a string value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws FormException
    */
   float       getFloat( String sParamName ) throws FormException;

   /**
    * Return the value of the specified HTML form parameter as a long value
    * @param sParamName the name of the HTML form parameter
    * @return a string value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws FormException
    */
   long        getLong( String sParamName ) throws FormException;

   /**
    * Return the value of the specified HTML form parameter as a boolean value
    * @param sParamName the name of the HTML form parameter
    * @return a string value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws FormException
    */
   boolean     getBoolean( String sParamName ) throws FormException;

   /**
    * Return the value of the specified HTML form parameter as a file handle, this method should only be used to access
    * a form parameter that represents an uploded file
    * @param sParamName the name of the HTML form parameter
    * @return a string value
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws FormException
    */
   FileHandle  getFile( String sParamName ) throws FormException;

   /**
    * Returns a list of the parameter names in the HTML form
    * @return a list containing strings, each string being a parameter name
    */
   Set getParamNames() throws FormException;

   /**
    * Used to check if the specified HTML form has the specified parameter. Parameter names are case-sensitive.
    * @param sParamName the name of the HTML form parameter
    * @return true if the parameter is defined in the form, false otherwise
    * @throws IOException
    * @throws FileUploadException
    * @throws ParserException
    * @throws FormException
    */
   boolean containsParam( String sParamName ) throws FormException;
}

// EOF