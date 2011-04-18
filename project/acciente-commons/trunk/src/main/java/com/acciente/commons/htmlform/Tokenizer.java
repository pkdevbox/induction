/*
 * Copyright 2008-2011 Acciente, LLC
 *
 * Acciente, LLC licenses this file to you under the
 * Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.acciente.commons.htmlform;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class tokenizes a URL encoded character stream, with additional considerations due
 * to support for an extended syntax for HTML form variables in Induction.
 *
 * @created Feb 15, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class Tokenizer
{
   public Tokenizer( Reader oInput )
   {
      _oStreamTokenizer       = new StreamTokenizer ( oInput );
      _oVariableTokenQueue    = new LinkedList();

      // every stream has at least one token (i.e. the the null string)
      _bHasMoreTokens         = true;
      _sCurrentToken          = null;
      _sVariableSplitterRegex = "["
                                 + Symbols.TOKEN_COLON
                                 + "\\" + Symbols.TOKEN_OPEN_BRACKET
                                 + "\\" + Symbols.TOKEN_CLOSE_BRACKET
                                 + "]";

      updateTokenizerTable( MODE_EXPECTING_VARIABLE );
   }

   public boolean hasMoreTokens()
   {
      return _bHasMoreTokens;
   }

   public boolean nextToken() throws IOException
   {
      if ( ! _oVariableTokenQueue.isEmpty() )
      {
         _sCurrentToken = ( String ) _oVariableTokenQueue.remove();

         return true;
      }

      if ( _bHasMoreTokens )
      {
         int iTokenType = _oStreamTokenizer.nextToken();

         if ( iTokenType == StreamTokenizer.TT_EOF )
         {
            _bHasMoreTokens = false;
         }
         else if ( iTokenType == StreamTokenizer.TT_WORD )
         {
            if ( _iCurrentMode == MODE_EXPECTING_DATA )
            {
               _sCurrentToken = _oStreamTokenizer.sval;
            }
            else
            {
               // we find that the variable may be URL encoded in both POST and GET contexts

               String sDecodedToken = URLDecoder.decode ( _oStreamTokenizer.sval, "UTF-8" );

               // only do complex decoding if needed, otherwise we take a performance hit for naught
               if ( sDecodedToken.contains( Symbols.TOKEN_COLON )
                     || sDecodedToken.contains( Symbols.TOKEN_OPEN_BRACKET )
                     || sDecodedToken.contains( Symbols.TOKEN_CLOSE_BRACKET ) )
               {
                  StringBuffer sToken = new StringBuffer();

                  // if the split cause the token to yield several tokens, the we consume only the first token
                  for ( int i = 0; i < sDecodedToken.length(); i++  )
                  {
                     char cChar = sDecodedToken.charAt( i );

                     if ( cChar == Symbols.CHAR_COLON
                           || cChar == Symbols.CHAR_OPEN_BRACKET
                           || cChar == Symbols.CHAR_CLOSE_BRACKET )
                     {
                        if ( sToken.length() > 0 )
                        {
                           _oVariableTokenQueue.add( new String( sToken ) );
                           sToken.setLength( 0 );
                        }
                        _oVariableTokenQueue.add( "" + cChar );
                     }
                     else
                     {
                        sToken.append( cChar );
                     }
                  }

                  _sCurrentToken = ( String ) _oVariableTokenQueue.remove();
               }
               else
               {
                  _sCurrentToken = sDecodedToken;
               }
            }
         }
         else if ( iTokenType == Symbols.CHAR_COLON
                     || iTokenType == Symbols.CHAR_OPEN_BRACKET
                     || iTokenType == Symbols.CHAR_CLOSE_BRACKET )
         {
            _sCurrentToken = String.valueOf( ( char ) iTokenType );
         }
         else if ( iTokenType == Symbols.CHAR_EQUALS )
         {
            _sCurrentToken = String.valueOf( ( char ) iTokenType );
            updateTokenizerTable( MODE_EXPECTING_DATA );
         }
         else if ( iTokenType == Symbols.CHAR_AMPERSAND )
         {
            _sCurrentToken = String.valueOf( ( char ) iTokenType );
            updateTokenizerTable( MODE_EXPECTING_VARIABLE );
         }
      }

      return _bHasMoreTokens;
   }

   public String token()
   {
      return _sCurrentToken;
   }

   private void updateTokenizerTable( int iMode )
   {
      _oStreamTokenizer.resetSyntax();

      if ( iMode == MODE_EXPECTING_VARIABLE )
      {
         // every unicode character not defined as a char token below is considered as part of a word token
         _oStreamTokenizer.wordChars( 0, 255 );

         _oStreamTokenizer.ordinaryChar( Symbols.CHAR_COLON );
         _oStreamTokenizer.ordinaryChar( Symbols.CHAR_OPEN_BRACKET );
         _oStreamTokenizer.ordinaryChar( Symbols.CHAR_CLOSE_BRACKET );
         _oStreamTokenizer.ordinaryChar( Symbols.CHAR_EQUALS );
         _oStreamTokenizer.ordinaryChar( Symbols.CHAR_AMPERSAND );
      }
      else if ( iMode == MODE_EXPECTING_DATA )
      {
         // every unicode character not defined as a char token below is considered as part of a word token
         _oStreamTokenizer.wordChars( 0, 255 );

         _oStreamTokenizer.ordinaryChar( Symbols.CHAR_AMPERSAND );
      }

      _iCurrentMode = iMode;
   }

   private        StreamTokenizer _oStreamTokenizer;
   private        int             _iCurrentMode;
   private        String          _sVariableSplitterRegex;
   private        Queue           _oVariableTokenQueue;
   private        boolean         _bHasMoreTokens;
   private        String          _sCurrentToken;

   private static int             MODE_EXPECTING_VARIABLE = 1;
   private static int             MODE_EXPECTING_DATA     = 2;
}

// EOF