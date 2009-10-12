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
package com.acciente.induction.init.config.xmlconfigloader;

import com.acciente.induction.init.config.Config;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

// EOF

/**
 * Internal.
 * RedirectMappingRule
 *
 * NOTE: we do not extend Rule in this class, since this class while a rules "container",
 * but is not itself a rule
 *
 * @created Mar 29, 2009
 *
 * @author Adinath Raveendra Raj
 */
public class RedirectMappingRule extends Rule
{
   private  Config.RedirectMapping  _oRedirectMapping;
   private  String                  _sURLBase;

   public RedirectMappingRule( Config.RedirectMapping oRedirectMapping )
   {
      _oRedirectMapping = oRedirectMapping;
   }

   public SetURLBaseRule createSetURLBaseRule()
   {
      return new SetURLBaseRule();
   }

   public AddClassToURLMapRule createAddClassToURLMapRule()
   {
      return new AddClassToURLMapRule();
   }

   public void begin( String sNamespace, String sName, Attributes oAttributes )
   {
      // reset data stored in rule
      _sURLBase = null;
   }

   public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
   {
      if ( _sURLBase != null )
      {
         _oRedirectMapping.setURLBase( _sURLBase.trim() );
      }
   }

   private class SetURLBaseRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         _sURLBase = sText;
      }
   }

   public class AddClassToURLMapRule extends Rule
   {
      private  String[] _oClassPackages;
      private  Pattern  _oClassPattern;
      private  List     _oClassFindReplaceDirectives;
      private  String   _sURLFormat;
      private  String   _sAlternateURLFormat;

      public void begin( String sNamespace, String sName, Attributes oAttributes )
      {
         // reset data stored in rule
         _oClassPackages               = null;
         _oClassPattern                = null;
         _oClassFindReplaceDirectives  = new ArrayList();
         _sURLFormat                   = null;
         _sAlternateURLFormat          = null;
      }

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( _oClassPackages == null )
         {
            throw new XMLConfigLoaderException( "config > redirect-mapping > class-to-url-map > class packages is a required attribute" );
         }

         if ( _oClassPattern == null )
         {
            throw new XMLConfigLoaderException( "config > redirect-mapping > class-to-url-map > class pattern is a required attribute" );
         }

         if ( _sURLFormat == null )
         {
            throw new XMLConfigLoaderException( "config > redirect-mapping > class-to-url-map > url format is a required attribute" );
         }

         Config.RedirectMapping.ClassToURLMap oClassToURLMap
            = _oRedirectMapping.addClassToURLMap( _oClassPackages, _oClassPattern, _sURLFormat, _sAlternateURLFormat );

         for ( int i = 0; i < _oClassFindReplaceDirectives.size(); i++)
         {
            String[] asFindReplace = ( String[] ) _oClassFindReplaceDirectives.get( i );

            oClassToURLMap.addClassFindReplaceDirective( asFindReplace[ 0 ], asFindReplace[ 1 ] );
         }
      }

      public ParamClassPackagesRule createParamClassPackagesRule()
      {
         return new ParamClassPackagesRule();
      }

      public ParamClassPatternRule createParamClassPatternRule()
      {
         return new ParamClassPatternRule();
      }

      public AddClassFindReplaceDirectiveRule createAddClassFindReplaceDirectiveRule()
      {
         return new AddClassFindReplaceDirectiveRule();
      }

      public ParamURLFormatRule createParamURLFormatRule()
      {
         return new ParamURLFormatRule();
      }

      public ParamURLFormatAltRule createParamURLFormatAltRule()
      {
         return new ParamURLFormatAltRule();
      }

      private class ParamClassPackagesRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _oClassPackages = sText.split( ";|," );
         }
      }

      private class ParamClassPatternRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _oClassPattern = Pattern.compile( sText );
         }
      }

      private class ParamURLFormatRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sURLFormat = sText;
         }
      }

      private class ParamURLFormatAltRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sAlternateURLFormat = sText;
         }
      }

      public class AddClassFindReplaceDirectiveRule extends Rule
      {
         private  String   _sFindStr;
         private  String   _sReplaceStr;

         public void begin( String sNamespace, String sName, Attributes oAttributes )
         {
            // reset data stored in rule
            _sFindStr      = null;
            _sReplaceStr   = null;
         }

         public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
         {
            if ( _sFindStr == null )
            {
               throw new XMLConfigLoaderException( "config > redirect-mapping > class-to-url-map > class-replace > find string is a required attribute" );
            }

            if ( _sReplaceStr == null )
            {
               throw new XMLConfigLoaderException( "config > redirect-mapping > class-to-url-map > class-replace > replace string is a required attribute" );
            }

            _oClassFindReplaceDirectives.add( new String[]{ _sFindStr, _sReplaceStr } );
         }

         public ParamFindRule createParamFindRule()
         {
            return new ParamFindRule();
         }

         public ParamReplaceRule createParamReplaceRule()
         {
            return new ParamReplaceRule();
         }

         private class ParamFindRule extends Rule
         {
            public void body( String sNamespace, String sName, String sText )
            {
               _sFindStr = sText;
            }
         }

         private class ParamReplaceRule extends Rule
         {
            public void body( String sNamespace, String sName, String sText )
            {
               _sReplaceStr = sText;
            }
         }
      }
   }
}