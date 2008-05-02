package com.acciente.dragonfly_junit.init.config;

import com.acciente.dragonfly.init.configloader.XMLConfigLoader;
import com.acciente.dragonfly.init.config.ConfigLoaderException;
import com.acciente.dragonfly.init.config.Config;
import org.junit.Test;

import java.io.File;

/**
 * Log
 * Apr 27, 2008 APR  -  created
 */
public class Test_XMLConfigLoader
{
   //private File _oTestConfigFile = new File( "c:/acciente/acciente-projects/software/project/demoapp/subversion/web/WEB-INF/dragonfly-demoapp.xml" );
   private File _oTestConfigFile = new File( "t:/acciente-projects/software/project/demoapp/subversion/web/WEB-INF/dragonfly-demoapp.xml" );

   @Test
   public void test_1() throws ConfigLoaderException
   {
      Config oConfig = getXMLConfigLoader().getConfig();

      System.out.println( "Config=" + oConfig );
   }

   private XMLConfigLoader getXMLConfigLoader()
   {
      return new XMLConfigLoader( _oTestConfigFile );
   }
}

// EOF