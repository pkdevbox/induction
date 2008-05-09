package com.acciente.dragonfly.init.config.xmlconfigloader;

import com.acciente.commons.lang.Strings;
import com.acciente.dragonfly.init.config.Config;
import org.apache.commons.digester.Rule;

import java.io.File;
import java.util.Locale;

/**
 * TemplatingRule
 *
 * Log
 * May 6, 2008 APR  -  created
 */
public class TemplatingRule   // we do not extend Rule, since this class contains rules but is not a rule itself
{
   private  Config.Templating    _oTemplating;

   public TemplatingRule( Config.Templating oTemplating )
   {
      _oTemplating = oTemplating;
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
   private class TemplatePathAddDirRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         if ( ! Strings.isEmpty( sText ) )
         {
            _oTemplating.getTemplatePath().addDir( new File( sText ) );
         }
         else
         {
            throw new XMLConfigLoaderException( "templating config: template path directory item cannot be empty" );
         }
      }
   }

   public class TemplatePathAddLoaderClassRule extends Rule
   {
      private  String      _sLoaderClassName;
      private  String      _sPath;

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( ! Strings.isEmpty( _sLoaderClassName ) && ! Strings.isEmpty( _sPath ) )
         {
            _oTemplating.getTemplatePath().addLoaderClass( _sLoaderClassName, _sPath );
         }
         else
         {
            throw new XMLConfigLoaderException( "templating config: loader class in  template path must specify a class and path" );
         }
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
      public void body( String sNamespace, String sName, String sText )
      {
         _oTemplating.getTemplatePath().addWebappPath( sText );
      }
   }

   public class SetLocaleRule extends Rule
   {
      private String _sISOLanguage;
      private String _sISOCountry;

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( ! Strings.isEmpty( _sISOLanguage ) && ! Strings.isEmpty( _sISOCountry ) )
         {
            _oTemplating.setLocale( new Locale( _sISOLanguage, _sISOCountry ) );
         }
         else if ( ! Strings.isEmpty( _sISOLanguage ) )
         {
            _oTemplating.setLocale( new Locale( _sISOLanguage ) );
         }
         else
         {
            throw new XMLConfigLoaderException( "templating config: locale must specify an ISO language code" );
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

   public class TemplatingEngineRule extends Rule
   {
      private String    _sClassName;

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( ! Strings.isEmpty( _sClassName ) )
         {
            _oTemplating.getTemplatingEngine().setClassName( _sClassName );
         }
         else
         {
            throw new XMLConfigLoaderException( "templating config: template engine config does not specify a class name" );
         }
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