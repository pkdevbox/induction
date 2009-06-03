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

// EOF

/**
 * Internal.
 * ControllerMappingRule
 *
 * NOTE: we do not extend Rule in this class, since this class while a rules "container",
 * but is not itself a rule
 *
 * @created Mar 29, 2009
 *
 * @author Adinath Raveendra Raj
 */
public class ViewMappingRule extends Rule
{
   private  Config.ViewMapping   _oViewMapping;

   public ViewMappingRule( Config.ViewMapping oViewMapping )
   {
      _oViewMapping = oViewMapping;
   }

   public AddURLToClassMapRule createAddURLToClassMapRule()
   {
      return new AddURLToClassMapRule();
   }

   public class AddURLToClassMapRule extends Rule
   {
      private  Pattern  _oURLPattern;
      private  String[] _oClassPackages;
      private  Pattern  _oClassPattern;

      public void begin( String sNamespace, String sName, Attributes oAttributes )
      {
         // reset data stored in rule
         _oURLPattern      = null;
         _oClassPackages   = null;
         _oClassPattern    = null;
      }

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( _oURLPattern == null )
         {
            throw new XMLConfigLoaderException( "config > view-mapping > url-to-class-map > URL pattern is a required attribute" );
         }

         if ( _oClassPackages == null )
         {
            throw new XMLConfigLoaderException( "config > view-mapping > url-to-class-map > class packages is a required attribute" );
         }

         if ( _oClassPattern == null )
         {
            throw new XMLConfigLoaderException( "config > view-mapping > url-to-class-map > class pattern is a required attribute" );
         }

         _oViewMapping.addURLToClassMap( _oURLPattern, _oClassPackages, _oClassPattern  );
      }

      public ParamURLPatternRule createParamURLPatternRule()
      {
         return new ParamURLPatternRule();
      }

      public ParamClassPackagesRule createParamClassPackagesRule()
      {
         return new ParamClassPackagesRule();
      }

      public ParamClassPatternRule createParamClassPatternRule()
      {
         return new ParamClassPatternRule();
      }

      private class ParamURLPatternRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _oURLPattern = Pattern.compile( sText );
         }
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
   }
}