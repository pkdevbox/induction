package com.acciente.test.dragonfly.form;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

/**
 * Log
 * Feb 18, 2008 APR  -  created
 */
public class TestData
{
   public static String[] getQueryStrings() throws UnsupportedEncodingException
   {
      return   new String[]
               {  "name=John",
                  "name=John&message=" + URLEncoder.encode( "Hi Mary hows it going :=), called to remind you to water the plants", "UTF-8" ),
                  "name=John&message=Hi Mary hows it going :=), called to remind you to water the plants",
                  "name=John&age=20",
                  "name=Jack+and+Jill&poem_type=Nursery++rhymes&int:age=10",
                  "name=Jack&comments=&int:age=10"
               };
   }
}

// EOF