package com.acciente.test.dragonfly.form;

import com.acciente.dragonfly.dispatcher.form.Parser;
import com.acciente.dragonfly.dispatcher.form.ParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Iterator;

/**
 * Log
 * Feb 12, 2008 APR  -  created
 */
public class TestParser
{
   public static void main( String[] asArgs ) throws IOException, ParserException
   {
      System.out.println( "dragonFly - Parser class - test" );

      String[] asTestQueryString = TestData.getQueryStrings();

      for ( int i = 0; i < asTestQueryString.length; i++ )
      {
         String sQueryString = asTestQueryString[ i ];

         System.out.println( "------------" );
         System.out.println( "query string: \"" + sQueryString + "\"" );

         Map oForm = Parser.parse( new StringReader( sQueryString ), true );

         for ( Iterator oIter = oForm.keySet().iterator(); oIter.hasNext(); )
         {
            String sVarName = ( String ) oIter.next();

            Object oVarValue = oForm.get( sVarName );

            System.out.println( "name : " + sVarName );
            System.out.println( "type : " + oVarValue.getClass().getName() );
            System.out.println( "value: \"" + oVarValue + "\"");
         }
      }
   }
}

// EOF