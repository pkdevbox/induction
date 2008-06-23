/**
 *   Copyright 2008 Acciente, LLC
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
import com.acciente.commons.htmlform.Parser;
import com.acciente.induction.init.config.Config;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Log
 * Feb 18, 2008 APR  -  created
 */
public class HTMLForm implements Form
{
   private HttpServletRequest _oServletRequest;
   private Map                _oFormParams;
   private Config.FileUpload  _oFileUploadConfig;
   private boolean            _bFormParsed;

   public HTMLForm( HttpServletRequest oHttpServletRequest, Config.FileUpload oFileUploadConfig )
   {
      _oServletRequest     = oHttpServletRequest;
      _oFileUploadConfig   = oFileUploadConfig;
      _bFormParsed         = false;
   }

   public Object getObject( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException
   {
      return getParamValue( sParamName );
   }

   public String getString( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException
   {
      return ( String ) getParamValue( sParamName );
   }

   public int getInteger( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException
   {
      return ( ( Integer ) getParamValue( sParamName ) ).intValue();
   }

   public float getFloat( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException
   {
      return ( ( Float ) getParamValue( sParamName ) ).floatValue();
   }

   public long getLong( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException
   {
      return ( ( Long ) getParamValue( sParamName ) ).longValue();
   }

   public boolean getBoolean( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException
   {
      return ( ( Boolean ) getParamValue( sParamName ) ).booleanValue();
   }

   public FileHandle getFile( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException
   {
      return ( FileHandle ) getParamValue( sParamName );
   }

   public Set getParamNames() throws IOException, FileUploadException, ParserException
   {
      parseForm();

      return _oFormParams.keySet();
   }

   public boolean containsParam( String sParamName ) throws IOException, FileUploadException, ParserException, HTMLFormException
   {
      parseForm();

      return _oFormParams.containsKey( sParamName );
   }

   private Object getParamValue( String sParamName )
      throws IOException, FileUploadException, ParserException, HTMLFormException
   {
      parseForm();

      if ( _oFormParams != null )
      {
         if ( ! _oFormParams.containsKey( sParamName ) )
         {
            throw ( new HTMLFormException( "Attempt to access undefined HTML form parameter: " + sParamName ) );
         }

         return _oFormParams.get( sParamName );
      }

      return null;
   }

   private void parseForm() throws IOException, FileUploadException, ParserException
   {
      if ( ! _bFormParsed )
      {
         _oFormParams
            =  Parser.parseForm( _oServletRequest,
                                 _oFileUploadConfig.getStoreOnDiskThresholdInBytes(),
                                 _oFileUploadConfig.getUploadedFileStorageDir() );
         _bFormParsed = true;
      }
   }
}

// EOF