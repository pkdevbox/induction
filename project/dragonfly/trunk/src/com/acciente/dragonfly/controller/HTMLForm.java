package com.acciente.dragonfly.controller;

import com.acciente.commons.htmlform.File;
import com.acciente.commons.htmlform.Parser;
import com.acciente.commons.htmlform.ParserException;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.HashMap;

/**
 * Log
 * Feb 18, 2008 APR  -  created
 */
public class HTMLForm implements Form
{
   private HttpServletRequest    _oHttpServletRequest;
   private Map                   _oFormParams;

   public HTMLForm( HttpServletRequest oHttpServletRequest )
   {
      _oHttpServletRequest = oHttpServletRequest;
   }

   public String getString( String sParamName )
   {
      return ( String ) getParamValue( sParamName );
   }

   public int getInteger( String sParamName )
   {
      return ( ( Integer ) getParamValue( sParamName ) ).intValue();
   }

   public float getFloat( String sParamName )
   {
      return ( ( Float ) getParamValue( sParamName ) ).floatValue();
   }

   public long getLong( String sParamName )
   {
      return ( ( Long ) getParamValue( sParamName ) ).longValue();
   }

   public boolean getBoolean( String sParamName )
   {
      return ( ( Boolean ) getParamValue( sParamName ) ).booleanValue();
   }

   public File getFile( String sParamName )
   {
      return ( File ) getParamValue( sParamName );
   }

   private Object getParamValue( String sParamName )
   {
      if ( _oFormParams == null )
      {
         // todo: implement call to parser module
      }
      return _oFormParams.get( sParamName );
   }

   public static Map parse( HttpServletRequest oRequest ) throws ParserException, IOException, FileUploadException
   {
      Map oGETParams = null;

      // first process the GET parameters (if any)
      if ( oRequest.getQueryString() != null )
      {
         oGETParams = parseGETParams( oRequest );
      }

      Map oPOSTParams = null;

      // next process POST parameters
      if ( "post".equals( oRequest.getMethod().toLowerCase() ) )
      {
         // check how the POST data has been encoded
         if ( ServletFileUpload.isMultipartContent( oRequest ) )
         {
            // todo: integrate param parser
            // oPOSTParams = parsePOSTMultiPart( oRequest );
         }
         else
         {
            // we have plain text
            oPOSTParams = parsePOSTParams( oRequest );
         }
      }

      Map oMergedParams;

      // merge the GET and POST parameters
      if ( oGETParams != null )
      {
         oMergedParams = oGETParams;

         if ( oPOSTParams != null )
         {
            oMergedParams.putAll( oPOSTParams );
         }
      }
      else
      {
         // we know that the oGETParams must be null
         oMergedParams = oPOSTParams;
      }

      return oMergedParams;
   }

   private static void parsePOSTMultiPart( HttpServletRequest oRequest )
      throws FileUploadException, IOException, ParserException
   {
      Map   oMultipartParams = new HashMap();

      // we have multi-part content, we currently process it with apache-commons-fileupload
      ServletFileUpload oMultipartParser = new ServletFileUpload();

      FileItemIterator oFileItemIter = oMultipartParser.getItemIterator( oRequest );

      while ( oFileItemIter.hasNext() )
      {
         FileItemStream oFileItem = oFileItemIter.next();

         // we allow the variable name to use the full syntax allowed in non-multipart forms
         // so this allowing the array and map variable syntaxes even in multi-part mode

         // todo: integrate param parser

         if ( oFileItem.isFormField() )
         {
            Reader oParamNameReader = null;

            try
            {
               oParamNameReader = new StringReader( oFileItem.getName() );
               
               Parser.addParameter2Form( oMultipartParams,
                                         Parser.parseParameterSpec( oParamNameReader ),
                                         Streams.asString( oFileItem.openStream() ),
                                         false
                                       );
            }
            finally
            {
               if ( oParamNameReader != null )
               {
                  oParamNameReader.close();
               }
            }
         }
         else
         {
            // todo: handle the file object
         }
      }
   }

   private static Map parseGETParams( HttpServletRequest oRequest )
      throws ParserException, IOException
   {
      Map            oGETParams;
      StringReader   oQueryStringReader = null;

      try
      {
         oQueryStringReader = new StringReader( oRequest.getQueryString() );

         oGETParams = Parser.parseForm( oQueryStringReader, true );
      }
      finally
      {
         if ( oQueryStringReader != null )
         {
            oQueryStringReader.close();
         }
      }

      return oGETParams;
   }

   private static Map parsePOSTParams( HttpServletRequest oRequest )
      throws ParserException, IOException
   {
      return Parser.parseForm( oRequest.getReader(), true );
   }
}

// EOF