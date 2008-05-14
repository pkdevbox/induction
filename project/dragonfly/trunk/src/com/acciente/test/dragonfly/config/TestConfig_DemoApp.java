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

      // java classpath - the 2 paths are intended try the location(s) on the work and home machines
      oConfig
         .getJavaClassPath()
            .addCompiledDir( new File( "t:/acciente-projects/software/project/demoapp/class/demoapp" ), "demoapp" );

      oConfig
         .getJavaClassPath()
            .addCompiledDir( new File( "c:/acciente/acciente-projects/software/project/demoapp/class/demoapp" ), "demoapp" );

      // model config
      oConfig
         .getModelDefs()
            .addModelDef( "demoapp.model.Counter", null, false, true, false );

      // template config
      oConfig
         .getTemplating()
            .getTemplatePath()
               .addDir( new File( "c:/acciente/acciente-projects/software/project/demoapp/subversion/src/demoapp/helloworld3_app" ) );

      return oConfig;
   }
}

// EOF