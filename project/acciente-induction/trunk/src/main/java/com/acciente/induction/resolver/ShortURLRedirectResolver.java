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

import com.acciente.commons.htmlform.Symbols;
import com.acciente.induction.init.config.Config;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
                                    ClassLoader             oClassLoader ) throws IOException
   {
      _oRedirectMapping       = oRedirectMapping;
      _oClass2URLMapperList   = createClass2URLMapperList( oRedirectMapping, oClassLoader );
   }

   public String resolve( Class oClass )
   {
      return resolve( oClass, null, null );
   }

   public String resolve( Class oClass, Map oURLQueryParameters )
   {
      return resolve( oClass, null, oURLQueryParameters );
   }

   public String resolve( Class oClass, String sMethodName )
   {
      return resolve( oClass, sMethodName, null );
   }

   public String resolve( Class oClass, String sMethodName, Map oURLQueryParameters )
   {
      for ( Iterator oIter = _oClass2URLMapperList.iterator(); oIter.hasNext(); )
      {
         String sURLPart = ( ( Class2URLMapper ) oIter.next() ).mapClass2URL( oClass, sMethodName );

         if ( sURLPart != null )
         {
            return createCompleteURL( _oRedirectMapping.getURLBase(), sURLPart, oURLQueryParameters );
         }
      }

      return null;
   }

   public String resolve( String sURLPart )
   {
      return createCompleteURL( _oRedirectMapping.getURLBase(), sURLPart, null );
   }

   public String resolve( String sURLPart, Map oURLQueryParameters )
   {
      return createCompleteURL( _oRedirectMapping.getURLBase(), sURLPart, oURLQueryParameters );
   }

   private String createCompleteURL( String sURLBase, String sURLPart, Map oURLQueryParameters )
   {
      StringBuffer oQueryString = new StringBuffer();

      if ( sURLBase != null )
      {
         oQueryString.append( sURLBase );
      }

      oQueryString.append( sURLPart );

      if ( oURLQueryParameters != null && oURLQueryParameters.size() > 0 )
      {
         oQueryString.append( "?" );

         for ( Iterator oIter = oURLQueryParameters.entrySet().iterator(); oIter.hasNext(); )
         {
            Map.Entry oEntry = ( Map.Entry ) oIter.next();

            if ( oEntry.getValue() instanceof Integer )
            {
               oQueryString.append( Symbols.TOKEN_VARTYPE_INT );
               oQueryString.append( ":" );
            }
            else if ( oEntry.getValue() instanceof Float )
            {
               oQueryString.append( Symbols.TOKEN_VARTYPE_FLOAT );
               oQueryString.append( ":" );
            }
            else if ( oEntry.getValue() instanceof Boolean )
            {
               oQueryString.append( Symbols.TOKEN_VARTYPE_BOOLEAN );
               oQueryString.append( ":" );
            }

            oQueryString.append( oEntry.getKey().toString().trim() );
            oQueryString.append( "=" );

            try
            {
               oQueryString.append( URLEncoder.encode( oEntry.getValue().toString(), "UTF-8" ) );
            }
            catch ( UnsupportedEncodingException e )
            {
               oQueryString.append( "[encode-error]" );
            }

            if ( oIter.hasNext() )
            {
               oQueryString.append( "&" );
            }
         }
      }

      return oQueryString.toString();
   }

   private List createClass2URLMapperList( Config.RedirectMapping oRedirectMapping, ClassLoader oClassLoader ) throws IOException
   {
      List oClass2URLMapperList;

      oClass2URLMapperList = new ArrayList( oRedirectMapping.getClassToURLMapList().size() );

      for ( Iterator oClassToURLMapIter = oRedirectMapping.getClassToURLMapList().iterator(); oClassToURLMapIter.hasNext(); )
      {
         Config.RedirectMapping.ClassToURLMap oClassToURLMap;

         oClassToURLMap = ( Config.RedirectMapping.ClassToURLMap ) oClassToURLMapIter.next();

         // store the URL pattern and the classname map in the list
         oClass2URLMapperList.add( new Class2URLMapper( oClassToURLMap.getClassPackages(),
                                                        oClassToURLMap.getClassPattern(),
                                                        oClassToURLMap.getURLFormat(),
                                                        oClassToURLMap.getAlternateURLFormat(),
                                                        oClassLoader ) );
      }

      return oClass2URLMapperList;
   }
}