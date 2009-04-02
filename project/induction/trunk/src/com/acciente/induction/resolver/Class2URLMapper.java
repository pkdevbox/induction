package com.acciente.induction.resolver;

import com.acciente.commons.lang.Strings;
import com.acciente.commons.loader.ClassDefLoader;
import com.acciente.commons.loader.ReloadingClassLoader;
import com.acciente.induction.init.config.Config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

   Class2URLMapper( Pattern oClassPattern, String sURLFormat, String sAlternateURLFormat, ReloadingClassLoader oClassLoader )
   {
      _sURLFormat = sURLFormat;
      _sAlternateURLFormat = sAlternateURLFormat;

      _bURLFormatHasMethodName = _sURLFormat.indexOf( Config.RedirectMapping.ClassToURLMap.METHODNAME_LITERAL ) != -1;

      // build a mapping for all classes we can find matching the specified class pattern
      _oClassName2ShortNameMap = new HashMap();

      for ( Iterator oClassDefLoaderIter = oClassLoader.getClassDefLoaders().iterator(); oClassDefLoaderIter.hasNext(); )
      {
         ClassDefLoader oClassDefLoader = ( ClassDefLoader ) oClassDefLoaderIter.next();
         Collection     oClassNames     = oClassDefLoader.findClassNames( oClassPattern );

         for ( Iterator oClassNameIter = oClassNames.iterator(); oClassNameIter.hasNext(); )
         {
            String   sClassName = ( String ) oClassNameIter.next();
            Matcher  oClassMatcher;

            oClassMatcher = oClassPattern.matcher( sClassName );

            if ( oClassMatcher.matches() )
            {
               String   sShortName = oClassMatcher.group( 1 );

               if ( sShortName != null )
               {
                  _oClassName2ShortNameMap.put( sClassName, sShortName.toLowerCase() );
               }
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

         if ( Strings.isEmpty( sMethodName ) )
         {
            sURL = _sURLFormat.replaceAll( Config.RedirectMapping.ClassToURLMap.SHORTNAME_SEARCH_REGEX, sShortName );

            if ( _bURLFormatHasMethodName )
            {
               if ( sMethodName == null )
               {
                  sMethodName = "";
               }

               sURL = sURL.replaceAll( Config.RedirectMapping.ClassToURLMap.METHODNAME_SEARCH_REGEX, sMethodName );
            }
         }
         else
         {
            if ( _sAlternateURLFormat != null )
            {
               sURL = _sAlternateURLFormat.replaceAll( Config.RedirectMapping.ClassToURLMap.SHORTNAME_SEARCH_REGEX, sShortName );
               sURL = sURL.replaceAll( Config.RedirectMapping.ClassToURLMap.METHODNAME_SEARCH_REGEX, sMethodName );
            }
            else
            {
               sURL = _sURLFormat.replaceAll( Config.RedirectMapping.ClassToURLMap.SHORTNAME_SEARCH_REGEX, sShortName );

               if ( _bURLFormatHasMethodName )
               {
                  if ( sMethodName == null )
                  {
                     sMethodName = "";
                  }

                  sURL = sURL.replaceAll( Config.RedirectMapping.ClassToURLMap.METHODNAME_SEARCH_REGEX, sMethodName );
               }
            }
         }

         return sURL;
      }

      return null;
   }
}
