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

   public static ParameterSpec parseParameterSpec( Reader oReader )
      throws ParserException, IOException
   {
      Tokenizer oTokenizer = new Tokenizer( oReader );

      oTokenizer.nextToken(); // read the first token

      // todo: add special handling in parseParameterSpec() when call with only an param spec not trailed by a '='
      return parseParameterSpec( oTokenizer );
   }

   private static ParameterSpec parseParameterSpec( Tokenizer oTokenizer )
      throws ParserException, IOException
   {
      String         sTypeOrIdentifier;
      String         sType, sIdentifier;
      ParameterSpec oParameterSpec = null;

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
         // todo: see if the this code before the while below is redundant
         // todo: consider entering the while right away!
         
         // this must be a list, map or map-list parameter
         if ( ! oTokenizer.token().equals( Symbols.TOKEN_OPEN_BRACKET ) )
         {
            throw new ParserException( "Syntax error in parameter: " + sIdentifier + ", unexpected token: " + oTokenizer.token() );
         }

         // consume TOKEN_OPEN_BRACKET
         oTokenizer.nextToken();

         // there must be more at least one more token in the stream
         assertNotEOS( oTokenizer );

         // is this an array parameter?
         if ( oTokenizer.token().equals( Symbols.TOKEN_CLOSE_BRACKET ) )
         {
            // we assume that we are done with this parameter spec since the
            // syntax does not permit anything further for this form (if there is
            // something further it will be caught by subsequent stages in the parse)
            oParameterSpec = new ParameterSpec( sIdentifier, sType, true );

            // consume TOKEN_CLOSE_BRACKET
            oTokenizer.nextToken();
         }
         else
         {
            // assume that this is a map parameter
            List  oMapKeys = new ArrayList();

            // store the first map key
            oMapKeys.add( oTokenizer.token() );
            oTokenizer.nextToken();

            // there must be more at least one more token in the stream
            assertNotEOS( oTokenizer );

            if ( ! oTokenizer.token().equals( Symbols.TOKEN_CLOSE_BRACKET ) )
            {
               throw new ParserException( "Syntax error in map parameter: " + sIdentifier + ", unexpected token: " + oTokenizer.token() );
            }

            // consume TOKEN_CLOSE_BRACKET
            oTokenizer.nextToken();

            while ( oTokenizer.hasMoreTokens() && ! oTokenizer.token().equals( Symbols.TOKEN_EQUALS ) )
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
                  // the leaf of this parameter is a list
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
                  // the leaf of this parameter is a map
                  oParameterSpec = new ParameterSpec( sIdentifier, sType, false, oMapKeys );
               }
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