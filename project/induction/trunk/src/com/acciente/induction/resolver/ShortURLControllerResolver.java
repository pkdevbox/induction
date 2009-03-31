package com.acciente.induction.resolver;

import com.acciente.commons.lang.Strings;
import com.acciente.commons.loader.ClassDefLoader;
import com.acciente.commons.loader.ReloadingClassLoader;
import com.acciente.induction.init.config.Config;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * ShortURLControllerResolver
 *
 * @author Adinath Raveendra Raj
 * @created Mar 29, 2009
 */
public class ShortURLControllerResolver implements ControllerResolver
{
   private  Config.ControllerMapping      _oControllerMapping;
   private  List                          _oShorURL2ClassnameMappingList;

   public ShortURLControllerResolver( Config.ControllerMapping    oControllerMapping,
                                      ReloadingClassLoader        oClassLoader )
   {
      _oControllerMapping              = oControllerMapping;
      _oShorURL2ClassnameMappingList   = new ArrayList( oControllerMapping.getURLToClassMapList().size() );

      for ( Iterator oURLToClassMapIter = oControllerMapping.getURLToClassMapList().iterator(); oURLToClassMapIter.hasNext(); )
      {
         Config.ControllerMapping.URLToClassMap oURLToClassMap;
         
         oURLToClassMap = ( Config.ControllerMapping.URLToClassMap ) oURLToClassMapIter.next();

         // build a mapping for all classes foun in the system that match the specified class pattern
         Map oShorURL2ClassnameMap = new HashMap();

         for ( Iterator oClassDefLoaderIter = oClassLoader.getClassDefLoaders().iterator(); oClassDefLoaderIter.hasNext(); )
         {
            ClassDefLoader oClassDefLoader = ( ClassDefLoader ) oClassDefLoaderIter.next();
            Collection     oClassnames     = oClassDefLoader.findClassNames( oURLToClassMap.getClassPattern() );

            for ( Iterator oClassnameIter = oClassnames.iterator(); oClassnameIter.hasNext(); )
            {
               String   sClassName = ( String ) oClassnameIter.next();
               Matcher  oClassMatcher;

               oClassMatcher = oURLToClassMap.getClassPattern().matcher( sClassName );

               if ( oClassMatcher.matches() )
               {
                  if ( oClassMatcher.groupCount() == 0 )
                  {
                     throw new IllegalArgumentException( "short-url-controller-resolver: must have at least one matching group in class name pattern: "
                                                         + oClassMatcher.pattern().pattern() );
                  }

                  oShorURL2ClassnameMap.put( oClassMatcher.group( 1 ).toLowerCase(), sClassName );
               }
            }
         }

         // store the URL pattern and the classname map in the list
         _oShorURL2ClassnameMappingList.add( new ShorURL2ClassnameMapping( oURLToClassMap.getURLPattern(), oShorURL2ClassnameMap ) );
      }
   }

   public Resolution resolve( HttpServletRequest oRequest )
   {
      String   sURLPath = oRequest.getPathInfo();

      for ( Iterator oIter = _oShorURL2ClassnameMappingList.iterator(); oIter.hasNext(); )
      {
         ShorURL2ClassnameMapping   oShorURL2ClassnameMapping = ( ShorURL2ClassnameMapping ) oIter.next();

         Matcher oURLMatcher = oShorURL2ClassnameMapping.getURLPattern().matcher( sURLPath );

         if ( oURLMatcher.matches() )
         {
            String   sShortName;
            String   sClassName;
            String   sMethodname = null;

            if ( oURLMatcher.groupCount() == 0 )
            {
               throw new IllegalArgumentException( "short-url-controller-resolver: must have at least one matching group in URL pattern: "
                                                   + oURLMatcher.pattern().pattern() );
            }

            sShortName = oURLMatcher.group( 1 ).toLowerCase();
            sClassName = ( String ) oShorURL2ClassnameMapping.getShortName2ClassnameMap().get( sShortName );

            if ( oURLMatcher.groupCount() > 1 )
            {
               sMethodname = oURLMatcher.group( 2 );
            }

            if ( Strings.isEmpty( sMethodname ) )
            {
               sMethodname = _oControllerMapping.getDefaultHandlerMethodName();
            }

            if ( sClassName != null )
            {
               return new Resolution( sClassName, sMethodname, _oControllerMapping.isIgnoreMethodNameCase() );
            }            
         }
      }
      
      return null;
   }
}
