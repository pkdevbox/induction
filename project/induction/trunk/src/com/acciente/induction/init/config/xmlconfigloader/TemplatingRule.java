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

import java.util.Locale;
import java.io.File;

/**
 * Internal.
 * TemplatingRule
 *
 * NOTE: we do not extend Rule in this class, since this class while a rules "container",
 * but is not itself a rule
 *
 * @created May 6, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class TemplatingRule
{
   private  Config.Templating    _oTemplating;

   public TemplatingRule( Config.Templating oTemplating )
   {
      _oTemplating = oTemplating;
   }

   public ExposePublicFieldsRule createExposePublicFieldsRule()
   {
      return new ExposePublicFieldsRule();
   }

   public TemplatePathAddDirRule createTemplatePathAddDirRule()
   {
      return new TemplatePathAddDirRule();
   }

   public TemplatePathAddLoaderClassRule createTemplatePathAddLoaderClassRule()
   {
      return new TemplatePathAddLoaderClassRule();
   }

   public TemplatePathAddWebAppPathRule createTemplatePathAddWebAppPathRule()
   {
      return new TemplatePathAddWebAppPathRule();
   }

   public SetLocaleRule createSetLocaleRule()
   {
      return new SetLocaleRule();
   }

   public TemplatingEngineRule createTemplatingEngineRule()
   {
      return new TemplatingEngineRule();
   }

   /**
    * TemplatePathAddDirRule
    */
   private class ExposePublicFieldsRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         _oTemplating.setExposePublicFields( Boolean.valueOf( sText ).booleanValue() );
      }
   }

   /**
    * TemplatePathAddDirRule
    */
   private class TemplatePathAddDirRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( sText ) )
         {
            throw new XMLConfigLoaderException( "config > templating > template-path: directory item cannot be empty" );
         }
         _oTemplating.getTemplatePath().addDir( new File( sText ) );
      }
   }

   /**
    * TemplatePathAddLoaderClassRule
    */
   public class TemplatePathAddLoaderClassRule extends Rule
   {
      private  String      _sLoaderClassName;
      private  String      _sPath;

      public void begin( String sNamespace, String sName, Attributes oAttributes )
      {
         // reset data stored in rule
         _sLoaderClassName = null;
         _sPath            = null;
      }

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( _sLoaderClassName ) || Strings.isEmpty( _sPath ) )
         {
            throw new XMLConfigLoaderException( "config > templating > template-path: loader class must specify a class and a path" );
         }
         _oTemplating.getTemplatePath().addLoaderClass( _sLoaderClassName, _sPath );
      }

      public ParamClassRule createParamClassRule()
      {
         return new ParamClassRule();
      }

      public ParamPathRule createParamPathRule()
      {
         return new ParamPathRule();
      }

      /**
       * ParamClassRule
       */
      private class ParamClassRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sLoaderClassName = sText;
         }
      }

      /**
       * ParamPathRule
       */
      private class ParamPathRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sPath = sText;
         }
      }
   }

   /**
    * TemplatePathAddWebAppPathRule
    */
   public class TemplatePathAddWebAppPathRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( sText ) )
         {
            throw new XMLConfigLoaderException( "config > templating > template-path: web app path must specify a path" );
         }
         _oTemplating.getTemplatePath().addWebappPath( sText );
      }
   }

   /**
    * SetLocaleRule
    */
   public class SetLocaleRule extends Rule
   {
      private String _sISOLanguage;
      private String _sISOCountry;

      public void begin( String sNamespace, String sName, Attributes oAttributes )
      {
         // reset data stored in rule
         _sISOLanguage  = null;
         _sISOCountry   = null;
      }

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( _sISOLanguage ) )
         {
            throw new XMLConfigLoaderException( "config > templating > template-path: locale must specify an ISO language code" );
         }

         if ( ! Strings.isEmpty( _sISOCountry ) )
         {
            _oTemplating.setLocale( new Locale( _sISOLanguage, _sISOCountry ) );
         }
         else
         {
            _oTemplating.setLocale( new Locale( _sISOLanguage ) );
         }
      }

      public ParamISOLanguageRule createParamISOLanguageRule()
      {
         return new ParamISOLanguageRule();
      }

      public ParamISOCountryRule createParamISOCountryRule()
      {
         return new ParamISOCountryRule();
      }

      private class ParamISOLanguageRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sISOLanguage = sText;
         }
      }

      private class ParamISOCountryRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sISOCountry = sText;
         }
      }
   }

   /**
    * TemplatingEngineRule
    */
   public class TemplatingEngineRule extends Rule
   {
      private String    _sClassName;

      public void begin( String sNamespace, String sName, Attributes oAttributes )
      {
         // reset data stored in rule
         _sClassName = null;
      }

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( _sClassName ) )
         {
            throw new XMLConfigLoaderException( "config > templating > template-engine: must specify a class name" );
         }
         _oTemplating.getTemplatingEngine().setClassName( _sClassName );
      }

      public ParamClassRule createParamClassRule()
      {
         return new ParamClassRule();
      }

      /**
       * ParamClassRule
       */
      private class ParamClassRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sClassName = sText;
         }
      }
   }
}

// EOF