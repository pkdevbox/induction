package com.acciente.dragonfly.init.configloader;

import com.acciente.dragonfly.init.config.Config;
import org.apache.commons.digester.Rule;

import java.io.File;

/**
 * TemplatingRule
 *
 * Log
 * May 6, 2008 APR  -  created
 */
public class TemplatingRule extends Rule
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

   public class TemplatePathAddDirRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText )
      {
         _oTemplating.getTemplatePath().addDir( new File( sText ) );
      }
   }

   public class TemplatePathAddLoaderClassRule extends Rule
   {
      private  String      _sLoaderClassName;
      private  String      _sPath;

      public void end( String sNamespace, String sName )
      {
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

   public class TemplatePathAddWebAppPathRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText )
      {
         _oTemplating.getTemplatePath().addWebappPath( sText );
      }
   }

   public class LocaleRule
   {

   }

   public class TemplatingEngineRule
   {

   }
}

// EOF