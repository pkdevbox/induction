package com.acciente.test.dragonfly.form;

import com.acciente.dragonfly.dispatcher.form.Parser;
import com.acciente.dragonfly.dispatcher.form.ParserException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Log
 * Feb 12, 2008 APR  -  created
 */
public class Test_Parser_Form
{
   private static int      PARAM_NAME = 0;
   private static int      PARAM_VALUE = 1;
   private static int      PARAM_TYPE = 2;
   private static int      PARAM_ENCTYPE = 3;

   private static String   PARAM_ENCTYPE_URL_ENCODE = "url-encode";

   @Test
   public void test_1() throws IOException, ParserException
   {
      String[][]  aaExpected
         =  {  { "name",         "John" },
            };

      verifyResults( aaExpected, parseForm( createForm( aaExpected ) ) );
   }

   @Test
   public void test_2() throws IOException, ParserException
   {
      String[][]  aaExpected
         =  {  { "name",         "John" },
               { "message",      "Hi Mary hows it going :=), called to remind you to water the plants", null, PARAM_ENCTYPE_URL_ENCODE },
            };

      verifyResults( aaExpected, parseForm( createForm( aaExpected ) ) );
   }

   @Test
   public void test_3() throws IOException, ParserException
   {
      String[][]  aaExpected
         =  {  { "name",         "John" },
               { "message",      "Hi Mary hows it going :=), called to remind you to water the plants" },
            };

      verifyResults( aaExpected, parseForm( createForm( aaExpected ) ) );
   }

   @Test
   public void test_4() throws IOException, ParserException
   {
      Object[][]  aaExpected
         =  {  { "name",         "John" },
               { "age",          "20" },
            };

      verifyResults( aaExpected, parseForm( createForm( aaExpected ) ) );
   }

   @Test
   public void test_5() throws IOException, ParserException
   {
      Object[][]  aaExpected
         =  {  { "name",         "Jack and Jill", null, PARAM_ENCTYPE_URL_ENCODE },
               { "poem_type",    "Nursery  rhymes", null, PARAM_ENCTYPE_URL_ENCODE },
               { "age",          new Integer( 10 ), "int" }
            };

      verifyResults( aaExpected, parseForm( createForm( aaExpected ) ) );
   }

   @Test
   public void test_6() throws IOException, ParserException
   {
      Object[][]  aaExpected
         =  {  { "name",         "Jack" },
               { "comments",     "Nursery  rhymes", null, PARAM_ENCTYPE_URL_ENCODE },
               { "age",          new Integer( 10 ), "int" },
            };

      verifyResults( aaExpected, parseForm( createForm( aaExpected ) ) );
   }

   // methods to parameterize and minimize the code for each test

   private static Map parseForm( String sForm )
      throws ParserException, IOException
   {
      System.out.println( "parsing form: " + sForm );
      return Parser.parseForm( new StringReader( sForm ), true );
   }

   private static String createForm( Object[][] aaFormVars ) throws UnsupportedEncodingException
   {
      StringBuffer   oForm =  new StringBuffer();

      for ( int i = 0; i < aaFormVars.length; i++ )
      {
         Object[] aFormVar = aaFormVars[ i ];

         if ( aFormVar.length > PARAM_TYPE
              && aFormVar[ PARAM_TYPE ] != null
              && ( ( String ) aFormVar[ PARAM_TYPE ] ).trim().length() > 0 )
         {
            oForm.append( aFormVar[ PARAM_TYPE ] );   // explicit type
            oForm.append( ':' );
         }

         oForm.append( aFormVar[ PARAM_NAME ] );   // name
         oForm.append( '=' );

         if ( aFormVar.length > PARAM_ENCTYPE && aFormVar[ PARAM_ENCTYPE ] != null )
         {
            if ( aFormVar[ PARAM_ENCTYPE ].equals( PARAM_ENCTYPE_URL_ENCODE ) )
            {
               oForm.append( URLEncoder.encode( ( String ) aFormVar[ PARAM_VALUE ], "UTF-8" ) );   // value
            }
         }
         else
         {
            oForm.append( aFormVar[ PARAM_VALUE ] );   // value
         }

         if ( i < ( aaFormVars.length - 1 )  )
         {
            oForm.append( '&' );
         }
      }

      return oForm.toString();
   }

   private static String verifyResults( Object[][] aaFormVars, Map oForm )
   {
      for ( int i = 0; i < aaFormVars.length; i++ )
      {
         Object[] aFormVar = aaFormVars[ i ];

         assertEquals( ( String ) aFormVar[ PARAM_NAME ], aFormVar[ PARAM_VALUE ], oForm.get( aFormVar[ PARAM_NAME ] ));
      }

      return oForm.toString();
   }
}

// EOF