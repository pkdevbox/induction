package com.acciente.dragonfly.dispatcher.form;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is parser that parses the parameters submitted in an HTML form
 * and populates a Map with the form data.
 *
 * Log
 * Feb 12, 2008 APR  -  created
 */
public class Parser
{
   public static Map parse( Reader oInput, boolean bURLEncoded ) throws ParserException, IOException
   {
      Tokenizer   oTokenizer = new Tokenizer( oInput );
      Map         oForm      = new HashMap();

      if ( oTokenizer.nextToken() )
      {
         addParameter2Form( oForm, parseParameterSpec( oTokenizer ), parseData( oTokenizer ), bURLEncoded );

         while ( oTokenizer.hasMoreTokens() )
         {
            if ( ! oTokenizer.token().equals( Symbols.TOKEN_AMPERSAND ) )
            {
               throw new ParserException( "Syntax error, expected " + Symbols.TOKEN_AMPERSAND + " got: " + oTokenizer.token() );
            }
            oTokenizer.nextToken();

            addParameter2Form( oForm, parseParameterSpec( oTokenizer ), parseData( oTokenizer ), bURLEncoded );
         }
      }

      return oForm;
   }

   private static ParameterSpec parseParameterSpec( Tokenizer oTokenizer )
      throws ParserException, IOException
   {
      String         sSavedToken;
      String         sType, sIdentifier;
      ParameterSpec oParameterSpec = null;

      // step 1: determine the parameter type and identifier
      sSavedToken = oTokenizer.token().trim().toLowerCase();
      oTokenizer.nextToken(); assertNotEOS( oTokenizer );

      if ( oTokenizer.token().equals( Symbols.TOKEN_COLON ) )
      {
         sType = sSavedToken;

         // the next token should be the identifier
         oTokenizer.nextToken(); assertNotEOS( oTokenizer );
         sIdentifier = oTokenizer.token().trim().toLowerCase();

         // consume identifier token
         oTokenizer.nextToken(); assertNotEOS( oTokenizer );
      }
      else
      {
         // no type was given, so the saved token is the identifier name
         // and the type defaults to STRING
         sIdentifier = sSavedToken;
         sType = Symbols.TOKEN_VARTYPE_STRING;
      }

      // step 2: proces the list or map parameter syntax, if any
      if ( oTokenizer.token().equals( Symbols.TOKEN_EQUALS ) )
      {
         // this is a simple parameter (i.e. neither list nor map)
         oParameterSpec = new ParameterSpec( sIdentifier, sType );
      }
      else
      {
         // this must be a list, map or map-list parameter
         if ( ! oTokenizer.token().equals( Symbols.TOKEN_OPEN_BRACKET ) )
         {
            throw new ParserException( "Syntax error in parameter: " + sIdentifier + ", unexpected token: " + oTokenizer.token() );
         }
         oTokenizer.nextToken(); assertNotEOS( oTokenizer );

         // is this an array parameter?
         if ( oTokenizer.token().equals( Symbols.TOKEN_CLOSE_BRACKET ) )
         {
            oParameterSpec = new ParameterSpec( sIdentifier, sType, true );
            oTokenizer.nextToken(); assertNotEOS( oTokenizer );
         }
         else
         {
            // assume that this is a map parameter
            List  oMapKeys = new ArrayList();

            // store the first map key
            oMapKeys.add( oTokenizer.token() );
            oTokenizer.nextToken(); assertNotEOS( oTokenizer );

            if ( ! oTokenizer.token().equals( Symbols.TOKEN_CLOSE_BRACKET ) )
            {
               throw new ParserException( "Syntax error in map parameter: " + sIdentifier + ", unexpected token: " + oTokenizer.token() );
            }
            oTokenizer.nextToken(); assertNotEOS( oTokenizer );

            while ( ! oTokenizer.token().equals( Symbols.TOKEN_EQUALS ) )
            {
               if ( ! oTokenizer.token().equals( Symbols.TOKEN_OPEN_BRACKET ) )
               {
                  throw new ParserException( "Syntax error in map parameter: " + sIdentifier + ", unexpected token: " + oTokenizer.token() );
               }
               oTokenizer.nextToken(); assertNotEOS( oTokenizer );

               // are we done?
               if ( oTokenizer.token().equals( Symbols.TOKEN_CLOSE_BRACKET ) )
               {
                  // the leaf of this parameter is a list
                  oParameterSpec = new ParameterSpec( sIdentifier, sType, true, oMapKeys );
                  oTokenizer.nextToken(); assertNotEOS( oTokenizer );

                  // the dragonfly syntax spec only allow arrays to be at a leaf node,
                  // so we should stop parsing parameter structure
                  break;
               }

               oMapKeys.add( oTokenizer.token() );                            // store subsequent keys
               oTokenizer.nextToken(); assertNotEOS( oTokenizer );

               if ( ! oTokenizer.token().equals( Symbols.TOKEN_CLOSE_BRACKET ) )
               {
                  throw new ParserException( "Syntax error in map parameter: " + sIdentifier + ", unexpected token: " + oTokenizer.token() );
               }
               oTokenizer.nextToken(); assertNotEOS( oTokenizer );

               // are we done?
               if ( oTokenizer.token().equals( Symbols.TOKEN_EQUALS ) )
               {
                  // the leaf of this parameter is a map
                  oParameterSpec = new ParameterSpec( sIdentifier, sType, false, oMapKeys );
               }
            }
         }
      }

      // expecting EQUALS token at this point
      if ( ! oTokenizer.token().equals( Symbols.TOKEN_EQUALS ) )
      {
         throw new ParserException( "Syntax error in parameter: " + sIdentifier + ", unexpected token: " + oTokenizer.token() );
      }
      oTokenizer.nextToken();                                        // consume EQUALS token, ok to hit EOS here

      return oParameterSpec;
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

   private static void addParameter2Form( Map oForm, ParameterSpec oParameterSpec, String sData, boolean bURLEncoded )
      throws ParserException, UnsupportedEncodingException
   {
      Object   oDataAtIdentifier = oForm.get( oParameterSpec.getIdentifier() );

      if ( ! oParameterSpec.isStructured() )
      {
         // we complain if a form has more than one value for an unstructured parameter
         if ( oDataAtIdentifier != null )
         {
            throw new ParserException( "Duplicate use of parameter : " + oParameterSpec.getIdentifier() );
         }
         oForm.put( oParameterSpec.getIdentifier(), convertData( sData, oParameterSpec.getDataType(), bURLEncoded ) );
      }
      else
      {
         // handle lists and maps
         if ( oParameterSpec.isMap() )
         {
            addMapParameter2Form( oForm, oParameterSpec, sData, bURLEncoded );
         }
         else if ( oParameterSpec.isList() )
         {
            addListParameter2Form( oForm, oParameterSpec, sData, bURLEncoded );
         }
         else
         {
            throw new ParserException( "Internal error, structured parameter neither list nor map!" );
         }
      }
   }

   private static void addMapParameter2Form( Map oForm, ParameterSpec oParameterSpec, String sData, boolean bURLEncoded )
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
            throw new ParserException( "Invalid reuse of non-map parameter: " + oParameterSpec.getIdentifier() + " as map, map key(s): " + oParameterSpec.getMapKeys() + ", new value: " + sData );
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
                  throw new ParserException( "Map-list parameter: " + oParameterSpec.getIdentifier() + " attempt to use non-list value at key: " + oMapKeys.toString() + " as a list, value: " + oDataAtKey + " new value: " + sData );
               }
               oList.add( sData );
            }
            else
            {
               if ( oDataAtKey != null )
               {
                  throw new ParserException( "Map parameter: " + oParameterSpec.getIdentifier() + " value overwrite at key: " + oMapKeys.toString() + " has value: " + oDataAtKey + " new value: " + sData );
               }
               oNestedMap.put( sKey, convertData( sData, oParameterSpec.getDataType(), bURLEncoded ) );
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

   private static void addListParameter2Form( Map oForm, ParameterSpec oParameterSpec, String sData, boolean bURLEncoded )
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
         throw new ParserException( "List parameter: " + oParameterSpec.getIdentifier() + " attempt to use non-list value as a list, value: " + oDataAtIdentifier + " new value: " + sData );
      }
      oList.add( convertData( sData, oParameterSpec.getDataType(), bURLEncoded ) );
   }

   private static Object convertData( String sData, String sDataType, boolean bURLEncoded )
      throws ParserException, UnsupportedEncodingException
   {
      Object oData = null;

      if ( bURLEncoded )
      {
         sData = URLDecoder.decode( sData,  "UTF-8");
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
         throw new ParserException( "Parameters of type \"file\" are not currently supported" );
      }
      else
      {
         throw new ParserException( "Internal error, unrecognized parameter type: " + sData );
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