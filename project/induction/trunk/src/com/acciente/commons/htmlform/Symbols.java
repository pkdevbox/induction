package com.acciente.commons.htmlform;

/**
 * Log
 * Feb 16, 2008 APR  -  created
 */
public interface Symbols
{
   char CHAR_COLON           =  ':';
   char CHAR_EQUALS          =  '=';
   char CHAR_AMPERSAND       =  '&';
   char CHAR_OPEN_BRACKET    =  '[';
   char CHAR_CLOSE_BRACKET   =  ']';

   // symbols used in DragonFly's URL encoded parameter syntax

   String TOKEN_COLON           =  ":";
   String TOKEN_EQUALS          =  "=";
   String TOKEN_AMPERSAND       =  "&";
   String TOKEN_OPEN_BRACKET    =  "[";
   String TOKEN_CLOSE_BRACKET   =  "]";

   String TOKEN_VARTYPE_STRING  =  "string";
   String TOKEN_VARTYPE_INT     =  "int";
   String TOKEN_VARTYPE_FLOAT   =  "float";
   String TOKEN_VARTYPE_BOOLEAN =  "boolean";
   String TOKEN_VARTYPE_FILE    =  "file";
}
