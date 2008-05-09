package com.acciente.dragonfly.init.config.xmlconfigloader;

import org.apache.commons.digester.Digester;
import com.acciente.dragonfly.init.config.Config;
import com.acciente.dragonfly.init.config.XML;

/**
 * This class ...
 *
 * Log
 * May 1, 2008 APR  -  created
 */
public class DigesterFactory
{
   public static Digester getDigester( Config oConfig )
   {
      Digester oDigester = new Digester();

      // java-class-path processing rules
      {
         JavaClassPathRule oJavaClassPathRule = new JavaClassPathRule( oConfig.getJavaClassPath() );

         oDigester.addRule( XML.Config_JavaClassPath_JavaCompiler_Class.PATTERN, oJavaClassPathRule.createSetJavaCompilerClassNameRule() );

         {
            JavaClassPathRule.AddCompiledDirRule oAddCompiledDirRule = oJavaClassPathRule.createAddCompiledDirRule();
            oDigester.addRule( XML.Config_JavaClassPath_CompiledDirectory.PATTERN,                 oAddCompiledDirRule );
            oDigester.addRule( XML.Config_JavaClassPath_CompiledDirectory_Directory.PATTERN,       oAddCompiledDirRule.createParamDirRule() );
            oDigester.addRule( XML.Config_JavaClassPath_CompiledDirectory_PackagePrefix.PATTERN,   oAddCompiledDirRule.createParamPackageNamePrefixRule() );
         }

         {
            JavaClassPathRule.AddSourceDirRule oAddSourceDirRule = oJavaClassPathRule.createAddSourceDirRule();
            oDigester.addRule( XML.Config_JavaClassPath_SourceDirectory.PATTERN,                   oAddSourceDirRule );
            oDigester.addRule( XML.Config_JavaClassPath_SourceDirectory_Directory.PATTERN,         oAddSourceDirRule.createParamDirRule() );
            oDigester.addRule( XML.Config_JavaClassPath_SourceDirectory_PackagePrefix.PATTERN,     oAddSourceDirRule.createParamPackageNamePrefixRule() );
         }
      }

      // model-defs processing rules
      {
         ModelDefsRule oModelDefsRule = new ModelDefsRule( oConfig.getModelDefs() );

         ModelDefsRule.AddModelDefRule oAddModelDefRule = oModelDefsRule.createAddModelDefRule();
         oDigester.addRule( XML.Config_ModelDefs_ModelDef.PATTERN,               oAddModelDefRule );
         oDigester.addRule( XML.Config_ModelDefs_ModelDef_Class.PATTERN,         oAddModelDefRule.createParamClassRule() );
         oDigester.addRule( XML.Config_ModelDefs_ModelDef_FactoryClass.PATTERN,  oAddModelDefRule.createParamFactoryClassRule() );
         oDigester.addRule( XML.Config_ModelDefs_ModelDef_Scope.PATTERN,         oAddModelDefRule.createParamScopeRule() );
      }

      // templating processing rules
      {
         TemplatingRule oTemplatingRule = new TemplatingRule( oConfig.getTemplating() );

         oDigester.addRule( XML.Config_Templating_TemplatePath_Directory.PATTERN,   oTemplatingRule.createTemplatePathAddDirRule() );
         {
            TemplatingRule.TemplatePathAddLoaderClassRule oTemplatePathAddLoaderClassRule = oTemplatingRule.createTemplatePathAddLoaderClassRule();
            oDigester.addRule( XML.Config_Templating_TemplatePath_LoaderClass.PATTERN,       oTemplatePathAddLoaderClassRule );
            oDigester.addRule( XML.Config_Templating_TemplatePath_LoaderClass_Class.PATTERN, oTemplatePathAddLoaderClassRule.createParamClassRule() );
            oDigester.addRule( XML.Config_Templating_TemplatePath_LoaderClass_Path.PATTERN,  oTemplatePathAddLoaderClassRule.createParamPathRule() );
         }
         oDigester.addRule( XML.Config_Templating_TemplatePath_WebAppPath.PATTERN,   oTemplatingRule.createTemplatePathAddWebAppPathRule() );
         {
            TemplatingRule.SetLocaleRule oSetLocaleRule = oTemplatingRule.createSetLocaleRule();
            oDigester.addRule( XML.Config_Templating_Locale.PATTERN,             oSetLocaleRule );
            oDigester.addRule( XML.Config_Templating_Locale_ISOLanguage.PATTERN, oSetLocaleRule.createParamISOLanguageRule() );
            oDigester.addRule( XML.Config_Templating_Locale_ISOCountry.PATTERN,  oSetLocaleRule.createParamISOCountryRule() );
         }
         {
            TemplatingRule.TemplatingEngineRule oTemplatingEngineRule = oTemplatingRule.createTemplatingEngineRule();
            oDigester.addRule( XML.Config_Templating_TemplatingEngine.PATTERN,         oTemplatingEngineRule );
            oDigester.addRule( XML.Config_Templating_TemplatingEngine_Class.PATTERN,   oTemplatingEngineRule.createParamClassRule() );
         }
      }

      // todo: load the rest of the config file

      return oDigester;
   }
}

// EOF