package com.acciente.dragonfly.init.configloader;

import org.apache.commons.digester.Digester;
import com.acciente.dragonfly.init.config.Config;
import com.acciente.dragonfly.init.config.XML;

/**
 * This class ...
 *
 * Log
 * May 1, 2008 APR  -  created
 */
public class XMLConfigDigesterFactory
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
            oDigester.addRule( XML.Config_JavaClassPath_CompiledDirectory_Directory.PATTERN,       oAddCompiledDirRule.createGetDirRule() );
            oDigester.addRule( XML.Config_JavaClassPath_CompiledDirectory_PackagePrefix.PATTERN,   oAddCompiledDirRule.createGetPackageNamePrefixRule() );
         }

         {
            JavaClassPathRule.AddSourceDirRule oAddSourceDirRule = oJavaClassPathRule.createAddSourceDirRule();
            oDigester.addRule( XML.Config_JavaClassPath_SourceDirectory.PATTERN,                   oAddSourceDirRule );
            oDigester.addRule( XML.Config_JavaClassPath_SourceDirectory_Directory.PATTERN,         oAddSourceDirRule.createGetDirRule() );
            oDigester.addRule( XML.Config_JavaClassPath_SourceDirectory_PackagePrefix.PATTERN,     oAddSourceDirRule.createGetPackageNamePrefixRule() );
         }
      }

      // model-defs processing rules
      {
         ModelDefsRule oModelDefsRule = new ModelDefsRule( oConfig.getModelDefs() );

         ModelDefsRule.AddModelDefRule oAddModelDefRule = oModelDefsRule.createAddModelDefRule();
         oDigester.addRule( XML.Config_ModelDefs_ModelDef.PATTERN,               oAddModelDefRule );
         oDigester.addRule( XML.Config_ModelDefs_ModelDef_Class.PATTERN,         oAddModelDefRule.createGetClassRule() );
         oDigester.addRule( XML.Config_ModelDefs_ModelDef_FactoryClass.PATTERN,  oAddModelDefRule.createGetFactoryClassRule() );
         oDigester.addRule( XML.Config_ModelDefs_ModelDef_Scope.PATTERN,         oAddModelDefRule.createGetScopeRule() );
      }

      // todo: load of the config file

      return oDigester;
   }
}

// EOF