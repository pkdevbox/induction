package com.acciente.induction.resolver;

import com.acciente.commons.loader.ReloadingClassLoader;
import com.acciente.induction.init.config.Config;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * ShortURLRedirectResolver
 *
 * @author Adinath Raveendra Raj
 * @created Mar 31, 2009
 */
public class ShortURLRedirectResolver implements RedirectResolver
{
   private List                     _oClass2URLMapperList;
   private Config.RedirectMapping   _oRedirectMapping;

   public ShortURLRedirectResolver( Config.RedirectMapping  oRedirectMapping,
                                    ClassLoader             oClassLoader )
   {
      _oRedirectMapping = oRedirectMapping;

      if ( oClassLoader instanceof ReloadingClassLoader )
      {
         _oClass2URLMapperList = createClass2URLMapperList( oRedirectMapping, ( ReloadingClassLoader ) oClassLoader );
      }
      else
      {
         // todo: need to determine a way to scan for classes using a general classloader
         _oClass2URLMapperList = Collections.EMPTY_LIST;
      }
   }

   public String resolve( Class oClass )
   {
      return resolve( oClass, null );
   }

   public String resolve( Class oClass, String sMethodName )
   {
      for ( Iterator oIter = _oClass2URLMapperList.iterator(); oIter.hasNext(); )
      {
         String sURL = ( ( Class2URLMapper ) oIter.next() ).mapClass2URL( oClass, sMethodName );

         if ( sURL != null )
         {
            return _oRedirectMapping.getURLBase() + sURL;
         }
      }

      return null;
   }

   public String resolve( String sURL )
   {
      return _oRedirectMapping.getURLBase() + sURL;
   }

   private List createClass2URLMapperList( Config.RedirectMapping oRedirectMapping, ReloadingClassLoader oClassLoader )
   {
      List oClass2URLMapperList;

      oClass2URLMapperList = new ArrayList( oRedirectMapping.getClassToURLMapList().size() );

      for ( Iterator oClassToURLMapIter = oRedirectMapping.getClassToURLMapList().iterator(); oClassToURLMapIter.hasNext(); )
      {
         Config.RedirectMapping.ClassToURLMap oClassToURLMap;

         oClassToURLMap = ( Config.RedirectMapping.ClassToURLMap ) oClassToURLMapIter.next();

         // store the URL pattern and the classname map in the list
         oClass2URLMapperList.add( new Class2URLMapper( oClassToURLMap.getClassPattern(),
                                                        oClassToURLMap.getURLFormat(),
                                                        oClassLoader ) );
      }

      return oClass2URLMapperList;
   }
}
