package com.acciente.test.dragonfly.config;

import com.acciente.dragonfly.init.config.Config;
import com.acciente.dragonfly.init.config.ConfigLoader;

import java.io.File;

/**
 * Log
 * Mar 15, 2008 APR  -  created
 */
public class TestConfig_DemoApp implements ConfigLoader
{
   public Config getConfig()
   {
      Config   oConfig = new Config();

      // the paths are intended to be relative the project directory
      oConfig
         .getJavaClassPath()
            .addSourceDir( new File( "../../../demoapp/subversion/src/demoapp/controller/" ), "demoapp.controller" );
      oConfig
         .getJavaClassPath()
            .addCompiledDir( new File( "../../../demoapp/class" ), null );

      oConfig
         .getTemplatePath()
            .addTemplateDir( new File( "../../../demoapp/template" ) );

      return oConfig;
   }
}

// EOF