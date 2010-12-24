/*
 * Copyright 2008-2010 Acciente, LLC
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

import com.acciente.commons.loader.ClassFinder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Internal.
 *
 * URL2ClassMapper
*
* @author Adinath Raveendra Raj
* @created Mar 30, 2009
*/
class URL2ClassMapper
{
   private  Pattern  _oURLPattern;
   private  Map      _oShortName2ClassNameMap;

   URL2ClassMapper( Pattern                  oURLPattern,
                    String[]                 asClassPackages,
                    Pattern                  oClassPattern,
                    FindReplaceDirective[]   aoClassFindReplaceDirectives,
                    ClassLoader              oClassLoader ) throws IOException
   {
      // record the URL pattern
      _oURLPattern = oURLPattern;

      // build a mapping for all classes we can find matching the specified class pattern
      _oShortName2ClassNameMap = new HashMap();

      Set oClassNameSet = ClassFinder.find( oClassLoader, asClassPackages, oClassPattern );

      for ( Iterator oClassNameIter = oClassNameSet.iterator(); oClassNameIter.hasNext(); )
      {
         String   sClassName = ( String ) oClassNameIter.next();
         Matcher oClassMatcher;

         oClassMatcher = oClassPattern.matcher( sClassName );

         if ( oClassMatcher.matches() )
         {
            String   sShortName = oClassMatcher.group( 1 );

            if ( sShortName != null )
            {
               for ( int i = 0; i < aoClassFindReplaceDirectives.length; i++ )
               {
                  FindReplaceDirective oFindReplaceDirective = aoClassFindReplaceDirectives[ i ];

                  sShortName = sShortName.replaceAll( oFindReplaceDirective.getFindString(),
                                                      oFindReplaceDirective.getReplaceString() );
               }

               _oShortName2ClassNameMap.put( sShortName.toLowerCase(), sClassName );
            }
         }
      }
   }

   ClassAndMethod mapURL2Class( String sURLPath )
   {
      Matcher oURLMatcher = _oURLPattern.matcher( sURLPath );

      if ( oURLMatcher.matches() )
      {
         String   sShortName;

         sShortName = oURLMatcher.group( 1 );

         if ( sShortName != null )
         {
            String   sClassName;

            sClassName = ( String ) this._oShortName2ClassNameMap.get( sShortName.toLowerCase() );

            if ( sClassName != null )
            {
               String   sMethodname = null;

               if ( oURLMatcher.groupCount() > 1 )
               {
                  sMethodname = oURLMatcher.group( 2 );
               }

               return new ClassAndMethod( sClassName, sMethodname );
            }
         }
      }

      return null;
   }

}
