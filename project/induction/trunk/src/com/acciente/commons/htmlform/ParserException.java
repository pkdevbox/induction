/**
 *   Copyright 2008 Acciente, LLC
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

/**
 * This is parser that parses the variables submitted in an HTML form
 * and populates a Map with the form data.
 *
 * Log
 * Feb 16, 2008 APR  -  created
 */
public class ParserException extends Exception
{
   public ParserException( String s )
   {
      super( s );
   }
}
