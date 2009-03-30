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

import com.acciente.commons.lang.Strings;
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
public class ControllerMappingRule extends Rule
{
   private  Config.ControllerMapping   _oControllerMapping;

   private  String                     _sDefaultHandlerMethodName;
   private  Boolean                    _oIgnoreMethodNameCase;

   public ControllerMappingRule( Config.ControllerMapping oControllerMapping )
   {
      _oControllerMapping = oControllerMapping;
   }

   public SetDefaultHandlerMethodRule createSetDefaultHandlerMethodRule()
   {
      return new SetDefaultHandlerMethodRule();
   }

   public SetIgnoreHandlerMethodCaseRuleRule createSetIgnoreHandlerMethodCaseRule()
   {
      return new SetIgnoreHandlerMethodCaseRuleRule();
   }

   public AddURLToClassMapRule createAddURLToClassMapRule()
   {
      return new AddURLToClassMapRule();
   }

   public void begin( String sNamespace, String sName, Attributes oAttributes )
   {
      // reset data stored in rule
      _sDefaultHandlerMethodName = null;
      _oIgnoreMethodNameCase     = null;
   }

   public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
   {
      if ( _oIgnoreMethodNameCase != null )
      {
         _oControllerMapping.setIgnoreMethodNameCase( _oIgnoreMethodNameCase.booleanValue() );
      }

      if ( ! Strings.isEmpty( _sDefaultHandlerMethodName ) )
      {
         _oControllerMapping.setDefaultHandlerMethodName( _sDefaultHandlerMethodName );
      }
   }

   private class SetDefaultHandlerMethodRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( sText ) )
         {
            throw new XMLConfigLoaderException( "config > controller-mapping > default-handler-method: must specify a valid method name" );
         }
         _sDefaultHandlerMethodName = sText;
      }
   }

   private class SetIgnoreHandlerMethodCaseRuleRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( sText ) )
         {
            throw new XMLConfigLoaderException( "config > controller-mapping > ignore-handler-method-case: must specify a boolean value, specify true or false" );
         }
         _oIgnoreMethodNameCase = Boolean.valueOf( sText );
      }
   }

   public class AddURLToClassMapRule extends Rule
   {
      private  Pattern  _oURLPattern;
      private  Pattern  _oClassPattern;

      public void begin( String sNamespace, String sName, Attributes oAttributes )
      {
         // reset data stored in rule
         _oURLPattern   = null;
         _oClassPattern = null;
      }

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( _oURLPattern == null )
         {
            throw new XMLConfigLoaderException( "config > controller-mapping > url-to-class-map > URL pattern is a required attribute" );
         }

         if ( _oClassPattern == null )
         {
            throw new XMLConfigLoaderException( "config > controller-mapping > url-to-class-map > class pattern is a required attribute" );
         }

         _oControllerMapping.addURLToClassMap( _oURLPattern, _oClassPattern  );
      }

      public ParamURLPatternRule createParamURLPatternRule()
      {
         return new ParamURLPatternRule();
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

      private class ParamClassPatternRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _oClassPattern = Pattern.compile( sText );
         }
      }
   }
}