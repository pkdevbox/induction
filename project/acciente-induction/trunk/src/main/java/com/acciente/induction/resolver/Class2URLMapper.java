/**
 *   Copyright 2009 Acciente, LLC
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
package com.acciente.induction.resolver;

import com.acciente.commons.lang.Strings;
import com.acciente.commons.loader.ClassFinder;
import com.acciente.induction.init.config.Config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class2URLMapper
 *
 * @author Adinath Raveendra Raj
 * @created Mar 31, 2009
 */
class Class2URLMapper
{
   private Map       _oClassName2ShortNameMap;

   private String    _sURLFormat;
   private boolean   _bURLFormatHasMethodName;

   private String    _sAlternateURLFormat;

   Class2URLMapper( String[]                 asClassPackages, 
                    Pattern                  oClassPattern,
                    FindReplaceDirective[]   aoClassFindReplaceDirectives,
                    String                   sURLFormat,
                    String                   sAlternateURLFormat,
                    ClassLoader              oClassLoader ) throws IOException
   {
      _sURLFormat                = sURLFormat;
      _sAlternateURLFormat       = sAlternateURLFormat;
      _bURLFormatHasMethodName   = _sURLFormat.indexOf( Config.RedirectMapping.ClassToURLMap.METHODNAME_LITERAL ) != -1;

      // build a mapping for all classes we can find matching the specified class pattern
      _oClassName2ShortNameMap = new HashMap();

      Set oClassNameSet = ClassFinder.find( oClassLoader, asClassPackages, oClassPattern );

      for ( Iterator oClassNameIter = oClassNameSet.iterator(); oClassNameIter.hasNext(); )
      {
         String   sClassName = ( String ) oClassNameIter.next();
         Matcher  oClassMatcher;

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

               _oClassName2ShortNameMap.put( sClassName, sShortName.toLowerCase() );
            }
         }
      }
   }

   String mapClass2URL( Class oClass, String sMethodName )
   {
      String   sShortName = ( String ) _oClassName2ShortNameMap.get( oClass.getName() );

      if ( sShortName != null )
      {
         String   sURL;

         // in the code below we generate a URL using the primary URL format, in the cases explained
         // below we will use the alternate URL format (if one is defined)

         // does the URL need to encode a method name?
         if ( Strings.isEmpty( sMethodName ) )
         {
            // no method name, the primary URL format is used
            sURL = _sURLFormat.replaceAll( Config.RedirectMapping.ClassToURLMap.SHORTNAME_SEARCH_REGEX, sShortName );

            if ( _bURLFormatHasMethodName )
            {
               sURL = sURL.replaceAll( Config.RedirectMapping.ClassToURLMap.METHODNAME_SEARCH_REGEX, "" );
            }
         }
         else
         {
            // yes, there is method name, now check if the primary URL format is capable of encoding method names
            if ( _bURLFormatHasMethodName )
            {
               // yes, use the primary URL format
               sURL = _sURLFormat
                        .replaceAll( Config.RedirectMapping.ClassToURLMap.SHORTNAME_SEARCH_REGEX, sShortName )
                           .replaceAll( Config.RedirectMapping.ClassToURLMap.METHODNAME_SEARCH_REGEX, sMethodName );
            }
            else
            {
               // no, the primary URL format does not handle method names, is there is an alternate URL format provided?
               if ( _sAlternateURLFormat != null )
               {
                  // yes, there is an alternate URL format so use it
                  sURL = _sAlternateURLFormat
                           .replaceAll( Config.RedirectMapping.ClassToURLMap.SHORTNAME_SEARCH_REGEX, sShortName )
                              .replaceAll( Config.RedirectMapping.ClassToURLMap.METHODNAME_SEARCH_REGEX, sMethodName );
               }
               else
               {
                  // no, so we inform the developer that we are unable to map this encoding request
                  throw new IllegalArgumentException( "Unable to map class: "
                                                      + oClass.getName()
                                                      + " to a URL, primary URL format provided does not handle method names and no alternate format specified" );
               }
            }
         }

         return sURL;
      }

      return null;
   }
}
