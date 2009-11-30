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

import com.acciente.induction.init.config.Config;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ShortURLViewResolver
 *
 * @author Adinath Raveendra Raj
 * @created Mar 31, 2009
 */
public class ShortURLViewResolver implements ViewResolver
{
   private List _oURL2ClassMapperList;

   public ShortURLViewResolver( Config.ViewMapping    oViewMapping,
                                ClassLoader           oClassLoader ) throws IOException
   {
      _oURL2ClassMapperList = createURL2ClassMapperList( oViewMapping, oClassLoader );
   }

   public ViewResolver.Resolution resolve( HttpServletRequest oRequest )
   {
      String   sURLPath = oRequest.getPathInfo();

      if ( sURLPath != null )
      {
         for ( Iterator oIter = _oURL2ClassMapperList.iterator(); oIter.hasNext(); )
         {
            ClassAndMethod oClassAndMethod;

            oClassAndMethod = ( ( URL2ClassMapper ) oIter.next() ).mapURL2Class( sURLPath );

            if ( oClassAndMethod != null )
            {
               return new ViewResolver.Resolution( oClassAndMethod.getClassName() );
            }
         }
      }

      return null;
   }

   private List createURL2ClassMapperList( Config.ViewMapping oViewMapping, ClassLoader oClassLoader ) throws IOException
   {
      List oURL2ClassMapperList;

      oURL2ClassMapperList = new ArrayList( oViewMapping.getURLToClassMapList().size() );

      for ( Iterator oURLToClassMapIter = oViewMapping.getURLToClassMapList().iterator(); oURLToClassMapIter.hasNext(); )
      {
         Config.ViewMapping.URLToClassMap oURLToClassMap;

         oURLToClassMap = ( Config.ViewMapping.URLToClassMap ) oURLToClassMapIter.next();

         // store the URL pattern and the classname map in the list
         oURL2ClassMapperList.add( new URL2ClassMapper( oURLToClassMap.getURLPattern(),
                                                        oURLToClassMap.getClassPackages(),
                                                        oURLToClassMap.getClassPattern(),
                                                        asFindReplaceDirectiveArray( oURLToClassMap.getClassFindReplaceDirectives() ),
                                                        oClassLoader ) );
      }

      return oURL2ClassMapperList;
   }

   private FindReplaceDirective[] asFindReplaceDirectiveArray( List oClassFindReplaceDirectives )
   {
      FindReplaceDirective[]  aoFindReplaceDirectives = new FindReplaceDirective[ oClassFindReplaceDirectives.size() ];

      for ( int i = 0; i < oClassFindReplaceDirectives.size(); i++ )
      {
         Config.ViewMapping.URLToClassMap.FindReplaceDirective
            oFindReplaceDirective
            = ( Config.ViewMapping.URLToClassMap.FindReplaceDirective ) oClassFindReplaceDirectives.get( i );

         aoFindReplaceDirectives[ i ] = new FindReplaceDirective( oFindReplaceDirective.getFindString(),
                                                                  oFindReplaceDirective.getReplaceString() );
      }

      return aoFindReplaceDirectives;
   }
}
