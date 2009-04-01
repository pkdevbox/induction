package com.acciente.induction.resolver;

import com.acciente.commons.loader.ClassDefLoader;
import com.acciente.commons.loader.ReloadingClassLoader;

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
   private static final String   FORMAT_PLACEHOLDER_SHORTNAME  = "\\$Name";
   private static final String   FORMAT_PLACEHOLDER_METHODNAME = "\\$Method";

   private Map       _oClassName2ShortNameMap;
   private String    _sURLFormat;
   private boolean   _bURLFormatRequiresMethodName;

   Class2URLMapper( Pattern oClassPattern, String sURLFormat, ReloadingClassLoader oClassLoader )
   {
      _sURLFormat = sURLFormat;

      _bURLFormatRequiresMethodName = _sURLFormat.indexOf( FORMAT_PLACEHOLDER_METHODNAME ) != -1;

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

         sURL = _sURLFormat.replaceAll( FORMAT_PLACEHOLDER_SHORTNAME, sShortName );

         if ( _bURLFormatRequiresMethodName )
         {
            if ( sMethodName == null )
            {
               sMethodName = "";
            }

            sURL = sURL.replaceAll( FORMAT_PLACEHOLDER_METHODNAME, sMethodName );
         }

         return sURL;
      }

      return null;
   }
}
