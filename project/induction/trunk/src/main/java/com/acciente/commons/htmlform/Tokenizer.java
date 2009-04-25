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
package com.acciente.commons.htmlform;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

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
      _oStreamTokenizer = new StreamTokenizer( oInput );

      // every stream has at least one token (i.e. the the null string)
      _bHasMoreTokens   = true;
      _sCurrentToken    = null;

      updateTokenizerTable( MODE_EXPECTING_VARIABLE );
   }

   public boolean hasMoreTokens()
   {
      return _bHasMoreTokens;
   }

   public boolean nextToken() throws IOException
   {
      if ( _bHasMoreTokens )
      {
         int iTokenType = _oStreamTokenizer.nextToken();

         if ( iTokenType == StreamTokenizer.TT_EOF )
         {
            _bHasMoreTokens = false;
         }
         else if ( iTokenType == StreamTokenizer.TT_WORD )
         {
            _sCurrentToken = _oStreamTokenizer.sval;
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
   }

   private StreamTokenizer       _oStreamTokenizer;
   private boolean               _bHasMoreTokens;
   private String                _sCurrentToken;

   private static int MODE_EXPECTING_VARIABLE   = 1;
   private static int MODE_EXPECTING_DATA       = 2;
}

// EOF