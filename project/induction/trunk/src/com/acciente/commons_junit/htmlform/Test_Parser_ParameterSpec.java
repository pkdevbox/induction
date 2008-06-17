package com.acciente.commons_junit.htmlform;

import com.acciente.commons.htmlform.ParameterSpec;
import com.acciente.commons.htmlform.Parser;
import com.acciente.commons.htmlform.ParserException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

/**
 * Log
 * Apr 4, 2008 APR  -  created
 */
public class Test_Parser_ParameterSpec
{
   @Test
   public void test_1() throws IOException, ParserException
   {
      parseAndVerify( "name", "string:name" );
   }

   @Test
   public void test_2() throws IOException, ParserException
   {
      parseAndVerify( "name[]", "string:name[]" );
   }

   @Test
   public void test_3() throws IOException, ParserException
   {
      parseAndVerify( "name[foo]", "string:name[foo]" );
   }

   @Test
   public void test_4() throws IOException, ParserException
   {
      parseAndVerify( "name[foo][bar]", "string:name[foo][bar]" );
   }

   @Test
   public void test_5() throws IOException, ParserException
   {
      parseAndVerify( "name[foo][]", "string:name[foo][]" );
   }

   @Test
   public void test_6() throws IOException, ParserException
   {
      parseAndVerify( "name[foo][bar][]", "string:name[foo][bar][]" );
   }

   private void parseAndVerify( String sInput, String sExpected ) throws IOException, ParserException
   {
      assertEquals( "parsed=" + sInput +", expected=" + sExpected, sExpected, parseParameterSpec( sInput ).toString() );
      System.out.println( "OK: " + sInput );
   }

   private static ParameterSpec parseParameterSpec( String sParameterSpec ) throws IOException, ParserException
   {
      return Parser.parseParameterSpec( new StringReader( sParameterSpec ) );
   }
}

// EOF