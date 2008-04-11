package com.acciente.commons.htmlform;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class implements a parser that parses the parameters submitted in an HTML form
 * and populates a Map with the form data.
 *
 * Log
 * Feb 12, 2008 APR  -  created
 * Apr 04, 2008 APR  -  refactoring
 */
public class Parser
{
   /**
    * This method parses all the parameters contained in the http servlet request, in particular it parses and
    * merges parameters from the sources listed below. If a parameter is defined in more than on source the higher
    * numbered source below prevails.
    *
    *    1  - parameters in the URL's query query string (GET params)
    *    2  - parameters submitted using POST including multi-part parameters such as fileuploads
    *
    * @param oRequest a http servlet request object
    * @param iStoreFileOnDiskThresholdInBytes a file size in bytes above which the uploaded file will be stored on disk
    * @param oUploadedFileStorageDir a File object representing a path to which the uploaded files should be saved,
    * if null is specified a temporary directory is created in the java system temp directory path
    *
    * @return a map containing the paramater names and values, the paramter names are keys in the map
    *
    * @throws ParserException thrown if there is an error parsing the parameter data
    * @throws IOException thrown if there is an I/O error
    * @throws FileUploadException thrown if there is an error processing the multi-part data
    */
   public static Map parseForm( HttpServletRequest oRequest,
                                int                iStoreFileOnDiskThresholdInBytes,
                                File               oUploadedFileStorageDir
                              )
      throws ParserException, IOException, FileUploadException
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
            oPOSTParams = parsePOSTMultiPart( oRequest, iStoreFileOnDiskThresholdInBytes, oUploadedFileStorageDir );
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

   private static Map parsePOSTMultiPart( HttpServletRequest oRequest, int iStoreFileOnDiskThresholdInBytes, File oUploadedFileStorageDir )
      throws FileUploadException, IOException, ParserException
   {
      Map   oMultipartParams = new HashMap();

      // we have multi-part content, we process it with apache-commons-fileupload

      ServletFileUpload
         oMultipartParser = new ServletFileUpload( new DiskFileItemFactory( iStoreFileOnDiskThresholdInBytes,
                                                                            oUploadedFileStorageDir
                                                                          )
                                                 );

      List oFileItemList = oMultipartParser.parseRequest( oRequest );

      for ( Iterator oIter = oFileItemList.iterator(); oIter.hasNext(); )
      {
         FileItem oFileItem = ( FileItem ) oIter.next();

         // we support the variable name to use the full syntax allowed in non-multipart forms
         // so we use parseParameterSpec() to support array and map variable syntaxes in multi-part mode
         Reader         oParamNameReader = null;
         ParameterSpec  oParameterSpec   = null;

         try
         {
            oParamNameReader = new StringReader( oFileItem.getName() );

            oParameterSpec = Parser.parseParameterSpec( oParamNameReader );
         }
         finally
         {
            if ( oParamNameReader != null )
            {
               oParamNameReader.close();
            }
         }

         if ( oFileItem.isFormField() )
         {
            Parser.addParameter2Form( oMultipartParams,
                                      oParameterSpec,
                                      oFileItem.getString(),
                                      false
                                    );
         }
         else
         {
            Parser.addParameter2Form( oMultipartParams,
                                      oParameterSpec,
                                      oFileItem
                                    );
         }
      }

      return oMultipartParams;
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

   /**
    * This method parses a character stream containing an HTML form encoded using the
    * application/x-www-form-urlencoded scheme. This parser does not handle multi-part
    * data streams
    *
    * @param oInput the character stream containg the encoded HTML form
    * @param bURLEncoded if true the data value in the form are assumed to be URL encoded and the parser will
    * decoded the values
    * @return a map with a key for each form parameter with respective map value containing the data value of the parameter
    * @throws ParserException if there was an error parsing the HTML form
    * @throws IOException if there was an error reading the input character stream
    */
   public static Map parseForm( Reader oInput, boolean bURLEncoded ) throws ParserException, IOException
   {
      Tokenizer   oTokenizer = new Tokenizer( oInput );
      Map         oForm      = new HashMap();

      if ( oTokenizer.nextToken() )
      {
         ParameterSpec  oParameterSpec;

         oParameterSpec = parseParameterSpec( oTokenizer );

         parseEQUALS( oTokenizer, oParameterSpec.getIdentifier() );

         addParameter2Form( oForm, oParameterSpec, parseData( oTokenizer ), bURLEncoded );

         while ( oTokenizer.hasMoreTokens() )
         {
            parseAMPERSAND( oTokenizer );

            oParameterSpec = parseParameterSpec( oTokenizer );

            parseEQUALS( oTokenizer, oParameterSpec.getIdentifier() );

            addParameter2Form( oForm, oParameterSpec, parseData( oTokenizer ), bURLEncoded );
         }
      }

      return oForm;
   }

   /**
    * This method is not typicaly used by applications. It is used to parse out the structure of the
    * parameter name according to the supported syntax. One use of this method is to process the
    * parameter names in a multi-part stream to enable the full parameter syntax supported by this
    * parser for multi-part datastream that is being processed using a separate multi-part parser such as the
    * Apache's FileUpload mutli-part parser.
    *
    * @param oReader a character stream containing the parameter, this stream will read until either
    * the end of the stream is reached or until a TOKEN_EQUALS is encountered
    * @return a ParameterSpec structure describing the structure of the parameter
    * @throws ParserException if there was an error parsing the HTML form
    * @throws IOException  if there was an error reading the input character stream
    */
   public static ParameterSpec parseParameterSpec( Reader oReader )
      throws ParserException, IOException
   {
      Tokenizer oTokenizer = new Tokenizer( oReader );

      oTokenizer.nextToken(); // read the first token

      return parseParameterSpec( oTokenizer );
   }

   private static ParameterSpec parseParameterSpec( Tokenizer oTokenizer )
      throws ParserException, IOException
   {
      String         sTypeOrIdentifier;
      String         sType, sIdentifier;
      ParameterSpec oParameterSpec;

      // -- step 1: determine the parameter type and identifier

      // a parameter spec must have at least one token, the simplest parameter
      // spec would be a just an identifier
      assertNotEOS( oTokenizer );

      // the first token we read is either a parameter type or an identifier name,
      // in the latter case the parameter type is implied to be string
      sTypeOrIdentifier = oTokenizer.token().trim();
      oTokenizer.nextToken();

      if ( oTokenizer.hasMoreTokens() && oTokenizer.token().equals( Symbols.TOKEN_COLON ) )
      {
         // we see a TOKEN_COLON so sTypeOrIdentifier must be an explicit
         // type spec for the parameter
         sType = sTypeOrIdentifier.toLowerCase();

         // consume TOKEN_COLON
         oTokenizer.nextToken();

         // if there was a TOKEN_COLON there must be something after it
         assertNotEOS( oTokenizer );

         // this token should be the identifier
         sIdentifier = oTokenizer.token().trim();

         // consume identifier token
         oTokenizer.nextToken();
      }
      else
      {
         // no type was given, so sTypeOrIdentifier contains the identifier name
         // and the type defaults to STRING
         sIdentifier = sTypeOrIdentifier;
         sType       = Symbols.TOKEN_VARTYPE_STRING;
      }

      // -- step 2: process the list or map parameter syntax, if any

      if ( ( ! oTokenizer.hasMoreTokens() ) || oTokenizer.token().equals( Symbols.TOKEN_EQUALS ) )
      {
         // this is a simple parameter (i.e. neither list nor map)
         oParameterSpec = new ParameterSpec( sIdentifier, sType );
      }
      else
      {
         List  oMapKeys = new ArrayList();

         while ( true )
         {
            if ( ! oTokenizer.token().equals( Symbols.TOKEN_OPEN_BRACKET ) )
            {
               throw new ParserException( "Syntax error in map parameter: " + sIdentifier + ", unexpected token: " + oTokenizer.token() );
            }

            // consume TOKEN_OPEN_BRACKET
            oTokenizer.nextToken();

            // there must be more at least one more token in the stream
            assertNotEOS( oTokenizer );

            // is this an array? then we are done with this parameter spec
            if ( oTokenizer.token().equals( Symbols.TOKEN_CLOSE_BRACKET ) )
            {
               // the leaf of this parameter is a list, not that oMapKeys may be null if
               // this is encountered on the first loop iteration, and this is a valid case
               oParameterSpec = new ParameterSpec( sIdentifier, sType, true, oMapKeys );

               // consume TOKEN_CLOSE_BRACKET
               oTokenizer.nextToken();

               // since the parameter spec syntax only allow arrays to be at a leaf node,
               // so we can stop parsing at this point
               break;
            }

            // the token was not TOKEN_CLOSE_BRACKET so it must be
            // key in the map variable
            oMapKeys.add( oTokenizer.token() );                            // store subsequent keys

            // consume map key
            oTokenizer.nextToken();

            // there must be more at least one more token in the stream
            assertNotEOS( oTokenizer );

            if ( ! oTokenizer.token().equals( Symbols.TOKEN_CLOSE_BRACKET ) )
            {
               throw new ParserException( "Syntax error in map parameter: " + sIdentifier + ", unexpected token: " + oTokenizer.token() );
            }

            // consume TOKEN_CLOSE_BRACKET
            oTokenizer.nextToken();

            // are we done?
            if ( ( ! oTokenizer.hasMoreTokens() ) || oTokenizer.token().equals( Symbols.TOKEN_EQUALS ) )
            {
               // yes, and the leaf of this parameter is a map
               oParameterSpec = new ParameterSpec( sIdentifier, sType, false, oMapKeys );

               // we either reached the end of the token stream or hit TOKEN_EQUALS so we are done
               break;
            }
         }
      }

      return oParameterSpec;
   }

   private static void parseEQUALS( Tokenizer oTokenizer, String sIdentifier ) throws IOException, ParserException
   {
      // we expect at least one token to move along
      assertNotEOS( oTokenizer );

      // and that token must be TOKEN_EQUALS
      if ( ! oTokenizer.token().equals( Symbols.TOKEN_EQUALS ) )
      {
         throw new ParserException( "Syntax error after parameter: " + sIdentifier + ", unexpected token: " + oTokenizer.token() );
      }

      // consume TOKEN_EQUALS
      oTokenizer.nextToken();
   }

   private static String parseData( Tokenizer oTokenizer ) throws IOException
   {
      String   sData = "";

      if ( oTokenizer.hasMoreTokens() && ! oTokenizer.token().equals( Symbols.TOKEN_AMPERSAND ) )
      {
         sData = oTokenizer.token();
         oTokenizer.nextToken();
      }
      return sData;
   }

   private static void parseAMPERSAND( Tokenizer oTokenizer )
      throws ParserException, IOException
   {
      // we expect at least one token to move along
      assertNotEOS( oTokenizer );

      // and that token must be TOKEN_AMPERSAND
      if ( ! oTokenizer.token().equals( Symbols.TOKEN_AMPERSAND ) )
      {
         throw new ParserException( "Syntax error, expected " + Symbols.TOKEN_AMPERSAND + " got: " + oTokenizer.token() );
      }

      // consume TOKEN_AMPERSAND
      oTokenizer.nextToken();
   }

   /**
    * This method is not a typical entry point into the parser. It is provided for use in conjunction with
    * parseParameterSpec() to store a data value into a java map based on the structure in a ParameterSpec
    * object
    *
    * @param oForm a Map into which the data should be stored
    * @param oParameterSpec the parameter spec according to which the data value will be stored in the map
    * @param sData the data value to be stored in the map
    * @param bURLEncoded true if the data value is in URL encoded form and needs to be decoded before storing into the map
    * @throws ParserException thrown if the parameter spec will cause an overwrite of an existing value or if the
    *         parameter spec is in an unexpected format
    * @throws UnsupportedEncodingException if there was an exception thrown during any URL decoding that was attempted
    */
   public static void addParameter2Form( Map oForm, ParameterSpec oParameterSpec, String sData, boolean bURLEncoded )
      throws ParserException, UnsupportedEncodingException
   {
      Object   oDataAtIdentifier = oForm.get( oParameterSpec.getIdentifier() );

      if ( ! oParameterSpec.isStructured() )
      {
         // we complain if a form has more than one value for an unstructured parameter
         if ( oDataAtIdentifier != null )
         {
            throw new ParserException( "Duplicate use of parameter: " + oParameterSpec.getIdentifier() );
         }
         oForm.put( oParameterSpec.getIdentifier(), convertData( sData, oParameterSpec.getDataType(), bURLEncoded ) );
      }
      else
      {
         // handle lists and maps
         if ( oParameterSpec.isMap() )
         {
            addMapParameter2Form( oForm, oParameterSpec, convertData( sData, oParameterSpec.getDataType(), bURLEncoded ) );
         }
         else if ( oParameterSpec.isList() )
         {
            addListParameter2Form( oForm, oParameterSpec, convertData( sData, oParameterSpec.getDataType(), bURLEncoded ) );
         }
         else
         {
            throw new ParserException( "Internal error, structured parameter neither list nor map!" );
         }
      }
   }

   /**
    * This method is not a typical entry point into the parser. It is provided for use in conjunction with
    * parseParameterSpec() to store a data value into a java map based on the structure in a ParameterSpec
    * object
    *
    * @param oForm a Map into which the data should be stored
    * @param oParameterSpec the parameter spec according to which the data value will be stored in the map
    * @param oFileItem an uploaded file containing the data to be stored in the map
    * @throws ParserException thrown if the parameter spec will cause an overwrite of an existing value or if the
    *         parameter spec is in an unexpected format
    * @throws UnsupportedEncodingException if there was an exception thrown during any URL decoding that was attempted
    */
   public static void addParameter2Form( Map oForm, ParameterSpec oParameterSpec, FileItem oFileItem )
      throws ParserException, UnsupportedEncodingException
   {
      Object   oDataAtIdentifier = oForm.get( oParameterSpec.getIdentifier() );

      if ( ! oParameterSpec.isStructured() )
      {
         // we complain if a form has more than one value for an unstructured parameter
         if ( oDataAtIdentifier != null )
         {
            throw new ParserException( "Duplicate use of parameter: " + oParameterSpec.getIdentifier() );
         }
         oForm.put( oParameterSpec.getIdentifier(), convertData( oFileItem, oParameterSpec.getDataType() ) );
      }
      else
      {
         // handle lists and maps
         if ( oParameterSpec.isMap() )
         {
            addMapParameter2Form( oForm, oParameterSpec, convertData( oFileItem, oParameterSpec.getDataType() ) );
         }
         else if ( oParameterSpec.isList() )
         {
            addListParameter2Form( oForm, oParameterSpec, convertData( oFileItem, oParameterSpec.getDataType() ) );
         }
         else
         {
            throw new ParserException( "Internal error, structured parameter neither list nor map!" );
         }
      }
   }

   private static void addMapParameter2Form( Map oForm, ParameterSpec oParameterSpec, Object oData )
      throws ParserException, UnsupportedEncodingException
   {
      Object   oDataAtIdentifier = oForm.get( oParameterSpec.getIdentifier() );
      Map      oNestedMap;

      if ( oDataAtIdentifier == null )
      {
         oForm.put( oParameterSpec.getIdentifier(), oNestedMap = new HashMap() );
      }
      else
      {
         // verify that this is a Map
         if ( ! ( oDataAtIdentifier instanceof Map ) )
         {
            throw new ParserException( "Invalid reuse of non-map parameter: " + oParameterSpec.getIdentifier() + " as map, map key(s): " + oParameterSpec.getMapKeys() + ", new value: " + oData );
         }
         oNestedMap = ( Map ) oDataAtIdentifier;
      }

      List oMapKeys = oParameterSpec.getMapKeys();

      for ( int i = 0; i < oMapKeys.size(); i++ )
      {
         String sKey       = ( String ) oMapKeys.get( i );
         Object oDataAtKey = oNestedMap.get( sKey );

         // leaf contains the actual data value, or a list
         if ( i == oMapKeys.size() )
         {
            if ( oParameterSpec.isList() )
            {
               List  oList;

               if ( oDataAtKey == null )
               {
                  oNestedMap.put( sKey, oList = new ArrayList() );
               }
               else if ( oDataAtKey instanceof List )
               {
                  oList = ( List ) oDataAtKey;
               }
               else
               {
                  throw new ParserException( "Map-list parameter: " + oParameterSpec.getIdentifier() + " attempt to use non-list value at key: " + oMapKeys.toString() + " as a list, value: " + oDataAtKey + " new value: " + oData );
               }
               oList.add( oData );
            }
            else
            {
               if ( oDataAtKey != null )
               {
                  throw new ParserException( "Map parameter: " + oParameterSpec.getIdentifier() + " value overwrite at key: " + oMapKeys.toString() + " has value: " + oDataAtKey + " new value: " + oData );
               }
               oNestedMap.put( sKey, oData );
            }
         }
         else
         {
            if ( oDataAtKey == null )
            {
               oNestedMap.put( sKey, new HashMap() );
            }
            else
            {
               // verify that this is a Map
               if ( ! ( oDataAtKey instanceof Map ) )
               {
                  throw new ParserException( "Map parameter: " + oParameterSpec.getIdentifier() + " type mismatch at key: " + sKey + " key chain: " + oMapKeys.toString() + " has value: " + oDataAtKey );
               }
               oNestedMap = ( Map ) oDataAtKey;
            }
         }
      }
   }

   private static void addListParameter2Form( Map oForm, ParameterSpec oParameterSpec, Object oData )
      throws ParserException, UnsupportedEncodingException
   {
      Object   oDataAtIdentifier = oForm.get( oParameterSpec.getIdentifier() );
      List     oList;

      if ( oDataAtIdentifier == null )
      {
         oForm.put( oParameterSpec.getIdentifier(), oList = new ArrayList() );
      }
      else if ( oDataAtIdentifier instanceof List )
      {
         oList = ( List ) oDataAtIdentifier;
      }
      else
      {
         throw new ParserException( "List parameter: " + oParameterSpec.getIdentifier() + " attempt to use non-list value as a list, value: " + oDataAtIdentifier + " new value: " + oData );
      }
      oList.add( oData );
   }

   private static Object convertData( String sData, String sDataType, boolean bURLEncoded )
      throws ParserException, UnsupportedEncodingException
   {
      Object oData;

      if ( bURLEncoded )
      {
          sData = URLDecoder.decode( sData,  "UTF-8" );
      }

      if ( Symbols.TOKEN_VARTYPE_STRING.equals( sDataType ) )
      {
         oData = sData;
      }
      else if ( Symbols.TOKEN_VARTYPE_INT.equals( sDataType ) )
      {
         oData = new Integer( sData );
      }
      else if ( Symbols.TOKEN_VARTYPE_FLOAT.equals( sDataType ) )
      {
         oData = new Float( sData );
      }
      else if ( Symbols.TOKEN_VARTYPE_BOOLEAN.equals( sDataType ) )
      {
         oData = Boolean.valueOf( sData );
      }
      else if ( Symbols.TOKEN_VARTYPE_FILE.equals( sDataType ) )
      {
         throw new ParserException( "Parameters of type \"file\" are not supported in this context (use multi-part encoding)" );
      }
      else
      {
         throw new ParserException( "Internal error, unrecognized parameter type: " + sDataType  );
      }

      return oData;
   }

   private static Object convertData( FileItem oFileItem, String sDataType )
      throws ParserException, UnsupportedEncodingException
   {
      Object oData;

      if ( Symbols.TOKEN_VARTYPE_STRING.equals( sDataType ) )
      {
         oData = oFileItem.getString();
      }
      else if ( Symbols.TOKEN_VARTYPE_INT.equals( sDataType ) )
      {
         oData = new Integer( oFileItem.getString() );
      }
      else if ( Symbols.TOKEN_VARTYPE_FLOAT.equals( sDataType ) )
      {
         oData = new Float( oFileItem.getString() );
      }
      else if ( Symbols.TOKEN_VARTYPE_BOOLEAN.equals( sDataType ) )
      {
         oData = Boolean.valueOf( oFileItem.getString() );
      }
      else if ( Symbols.TOKEN_VARTYPE_FILE.equals( sDataType ) )
      {
         oData = new FileHandle( oFileItem );
      }
      else
      {
         throw new ParserException( "Internal error, unrecognized parameter type: " + sDataType  );
      }

      return oData;
   }

   private static void assertNotEOS( Tokenizer oTokenizer ) throws ParserException, IOException
   {
      if ( ! oTokenizer.hasMoreTokens() )
      {
         throw new ParserException( "Unexpected end of stream after: " + oTokenizer.token() );
      }
   }
}

// EOF