/*
 * Copyright 2008-2013 Acciente, LLC
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

/**
 * Contains the set of symbols used by the parser to identify tokens.
 *
 * @created Feb 16, 2008
 *
 * @author Adinath Raveendra Raj
 */
public interface Symbols
{
   char CHAR_COLON           =  ':';
   char CHAR_EQUALS          =  '=';
   char CHAR_AMPERSAND       =  '&';
   char CHAR_OPEN_BRACKET    =  '[';
   char CHAR_CLOSE_BRACKET   =  ']';

   // symbols used in Induction's URL encoded parameter syntax

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
