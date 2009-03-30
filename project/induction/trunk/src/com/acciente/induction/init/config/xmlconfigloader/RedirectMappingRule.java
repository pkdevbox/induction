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
   private  Config.RedirectMapping   _oRedirectMapping;

   public RedirectMappingRule( Config.RedirectMapping oRedirectMapping )
   {
      _oRedirectMapping = oRedirectMapping;
   }

   public AddClassToURLMapRule createAddClassToURLMapRule()
   {
      return new AddClassToURLMapRule();
   }

   public class AddClassToURLMapRule extends Rule
   {
      private  Pattern  _oClassPattern;
      private  String   _sURLFormat;

      public void begin( String sNamespace, String sName, Attributes oAttributes )
      {
         // reset data stored in rule
         _oClassPattern = null;
         _sURLFormat    = null;
      }

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( _oClassPattern == null )
         {
            throw new XMLConfigLoaderException( "config > redirect-mapping > class-to-url-map > class pattern is a required attribute" );
         }

         if ( _sURLFormat == null )
         {
            throw new XMLConfigLoaderException( "config > redirect-mapping > class-to-url-map > url format is a required attribute" );
         }

         _oRedirectMapping.addClassToURLMap( _oClassPattern, _sURLFormat );
      }

      public ParamClassPatternRule createParamClassPatternRule()
      {
         return new ParamClassPatternRule();
      }

      public ParamURLFormatRule createParamURLFormatRule()
      {
         return new ParamURLFormatRule();
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
   }
}