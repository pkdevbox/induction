package com.acciente.dragonfly.init.configloader;

import org.apache.commons.digester.Digester;
import com.acciente.dragonfly.init.config.Config;

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

      JavaClassPathRule oJavaClassPathRule = new JavaClassPathRule( oConfig.getJavaClassPath() );
      
      oDigester.addRule( "dispatcher-config/java-class-path/java-compiler/class",               oJavaClassPathRule.createSetJavaCompilerClassNameRule() );

      JavaClassPathRule.AddCompiledDirRule oAddCompiledDirRule = oJavaClassPathRule.createAddCompiledDirRule();
      oDigester.addRule( "dispatcher-config/java-class-path/compiled-directory",                oAddCompiledDirRule );
      oDigester.addRule( "dispatcher-config/java-class-path/compiled-directory/directory",      oAddCompiledDirRule.createGetDirRule() );
      oDigester.addRule( "dispatcher-config/java-class-path/compiled-directory/package-prefix", oAddCompiledDirRule.createGetPackageNamePrefixRule() );

      JavaClassPathRule.AddSourceDirRule oAddSourceDirRule = oJavaClassPathRule.createAddSourceDirRule();
      oDigester.addRule( "dispatcher-config/java-class-path/source-directory",                  oAddSourceDirRule );
      oDigester.addRule( "dispatcher-config/java-class-path/source-directory/directory",        oAddSourceDirRule.createGetDirRule() );
      oDigester.addRule( "dispatcher-config/java-class-path/source-directory/package-prefix",   oAddSourceDirRule.createGetPackageNamePrefixRule() );

      return oDigester;
   }
}

// EOF