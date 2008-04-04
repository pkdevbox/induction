package com.acciente.test.dragonfly.form;

import com.acciente.dragonfly.dispatcher.form.ParameterSpec;
import com.acciente.dragonfly.dispatcher.form.Parser;
import com.acciente.dragonfly.dispatcher.form.ParserException;
import org.junit.Test;

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
      System.out.println( "result=" + parseParameterSpec( "name" ) );
   }

   @Test
   public void test_2() throws IOException, ParserException
   {
      System.out.println( "result=" + parseParameterSpec( "name[]" ) );
   }

   @Test
   public void test_3() throws IOException, ParserException
   {
      System.out.println( "result=" + parseParameterSpec( "name[foo]" ) );
   }

   @Test
   public void test_4() throws IOException, ParserException
   {
      System.out.println( "result=" + parseParameterSpec( "name[foo][bar]" ) );
   }

   @Test
   public void test_5() throws IOException, ParserException
   {
      System.out.println( "result=" + parseParameterSpec( "name[foo][]" ) );
   }

   private static ParameterSpec parseParameterSpec( String sParameterSpec ) throws IOException, ParserException
   {
      return Parser.parseParameterSpec( new StringReader( sParameterSpec ) );
   }

}

// EOF