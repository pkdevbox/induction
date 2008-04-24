package com.acciente.commons.lang;

/**
 * This class has some frequently encountered string related methods
 *
 * Log
 * Apr 23, 2008 APR  -  created
 */
public class Strings
{
   /**
    * Returns true if the specified string value is null or only contains whitespace, false otherwise
    * @param s a string value to check
    * @return a boolean value
    */
   public static boolean isEmpty( String s )
   {
      return ( s == null || s.trim().length() == 0 );
   }
}

// EOF