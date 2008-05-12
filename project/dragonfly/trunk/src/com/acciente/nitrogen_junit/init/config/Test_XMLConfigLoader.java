package com.acciente.nitrogen_junit.init.config;

import com.acciente.nitrogen.init.config.Config;
import com.acciente.nitrogen.init.config.ConfigLoaderException;
import com.acciente.nitrogen.init.config.xmlconfigloader.XMLConfigLoader;
import org.junit.Test;

import java.io.File;

/**
 * Log
 * Apr 27, 2008 APR  -  created
 */
public class Test_XMLConfigLoader
{
   private File _oTestConfigFile = new File( "c:/acciente/acciente-projects/software/project/nitrogen/subversion/conf/nitrogen-complete-sample-config.xml" );
   //private File _oTestConfigFile = new File( "t:/acciente-projects/software/project/nitrogen/subversion/conf/nitrogen-complete-sample-config.xml" );

   @Test
   public void test_1() throws ConfigLoaderException
   {
      Config oConfig = getConfig();

      System.out.println( oConfig );
   }

   private Config getConfig() throws ConfigLoaderException
   {
      return new XMLConfigLoader( _oTestConfigFile ).getConfig();
   }
}

// EOF