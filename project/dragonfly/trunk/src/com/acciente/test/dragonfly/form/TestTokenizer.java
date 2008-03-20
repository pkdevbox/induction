package com.acciente.test.dragonfly.form;

import com.acciente.dragonfly.dispatcher.form.Tokenizer;

import java.io.IOException;
import java.io.StringReader;

/**
 * Log
 * Feb 15, 2008 APR  -  created
 */
public class TestTokenizer
{
   public static void main( String[] asArgs ) throws IOException
   {
      System.out.println( "dragonFly - Tokenizer class - test" );

      String[] asTestQueryString = TestData.getQueryStrings();

      for ( int i = 0; i < asTestQueryString.length; i++ )
      {
         String sQueryString = asTestQueryString[ i ];

         System.out.println( "------------" );
         System.out.println( "query string: \"" + sQueryString + "\"" );

         Tokenizer oTokenizer = new Tokenizer( new StringReader( sQueryString ) );

         oTokenizer.nextToken();
         while ( oTokenizer.hasMoreTokens() )
         {
            System.out.println( "token: \"" + oTokenizer.token() + "\"" );
            oTokenizer.nextToken();
         }
      }
   }
}

// EOF