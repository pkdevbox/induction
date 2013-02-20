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
package com.acciente.induction.resolver;

/**
 * FindReplaceDirective
 *
 * @author Adinath Raveendra Raj
 * @created Oct 18, 2009
 */
class FindReplaceDirective
{
   private  String   _sFindStr;
   private  String   _sReplaceStr;

   FindReplaceDirective( String sFindStr, String sReplaceStr )
   {
      _sFindStr      = sFindStr;
      _sReplaceStr   = sReplaceStr;
   }

   public String getFindString()
   {
      return _sFindStr;
   }

   public String getReplaceString()
   {
      return _sReplaceStr;
   }
}
