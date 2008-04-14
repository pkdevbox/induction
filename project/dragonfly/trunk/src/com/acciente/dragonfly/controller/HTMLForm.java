package com.acciente.dragonfly.controller;

import com.acciente.commons.htmlform.FileHandle;
import com.acciente.commons.htmlform.Parser;
import com.acciente.commons.htmlform.ParserException;
import com.acciente.dragonfly.init.config.Config;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Log
 * Feb 18, 2008 APR  -  created
 */
public class HTMLForm implements Form
{
   private HttpServletRequest _oServletRequest;
   private Map                _oFormParams;
   private Config.FileUpload  _oFileUploadConfig;

   public HTMLForm( HttpServletRequest oHttpServletRequest, Config.FileUpload oFileUploadConfig )
   {
      _oServletRequest     = oHttpServletRequest;
      _oFileUploadConfig   = oFileUploadConfig;
   }

   public String getString( String sParamName ) throws IOException, FileUploadException, ParserException
   {
      return ( String ) getParamValue( sParamName );
   }

   public int getInteger( String sParamName ) throws IOException, FileUploadException, ParserException
   {
      return ( ( Integer ) getParamValue( sParamName ) ).intValue();
   }

   public float getFloat( String sParamName ) throws IOException, FileUploadException, ParserException
   {
      return ( ( Float ) getParamValue( sParamName ) ).floatValue();
   }

   public long getLong( String sParamName ) throws IOException, FileUploadException, ParserException
   {
      return ( ( Long ) getParamValue( sParamName ) ).longValue();
   }

   public boolean getBoolean( String sParamName ) throws IOException, FileUploadException, ParserException
   {
      return ( ( Boolean ) getParamValue( sParamName ) ).booleanValue();
   }

   public FileHandle getFile( String sParamName ) throws IOException, FileUploadException, ParserException
   {
      return ( FileHandle ) getParamValue( sParamName );
   }

   private Object getParamValue( String sParamName )
      throws IOException, FileUploadException, ParserException
   {
      if ( _oFormParams == null )
      {
         _oFormParams
            =  Parser.parseForm( _oServletRequest,
                                 _oFileUploadConfig.getStoreOnDiskThresholdInBytes(),
                                 _oFileUploadConfig.getUploadedFileStorageDir() );
      }

      return _oFormParams.get( sParamName );
   }
}

// EOF