package com.acciente.dragonfly.init.config.xmlconfigloader;

import com.acciente.dragonfly.init.config.Config;
import com.acciente.dragonfly.init.config.ConfigLoader;
import com.acciente.dragonfly.init.config.ConfigLoaderException;
import org.xml.sax.SAXException;

import javax.servlet.ServletConfig;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * An implementation of the ConfigLoader that loads the configuration from an
 * XML file.
 *
 * Log
 * Apr 26, 2008 APR  -  created
 */
public class XMLConfigLoader implements ConfigLoader
{
   private  ServletConfig  _oServletConfig;
   private  File           _oConfigFile;

   public XMLConfigLoader( ServletConfig oServletConfig )
   {
      _oServletConfig = oServletConfig;
   }

   /**
    * This constructor was written to faciliate testing the config loader
    * by loading a file directly from the filesystem.
    *
    * @param oConfigFile
    */
   public XMLConfigLoader( File oConfigFile )
   {
      _oConfigFile = oConfigFile;
   }

   public Config getConfig() throws ConfigLoaderException
   {
      Config   oConfig;

      try
      {
         if ( _oServletConfig != null )
         {
            // first compute the expected name of config file name
            String   sConfigFileName = "/WEB-INF/dragonfly-" + _oServletConfig.getServletName() + ".xml";

            InputStream oConfigStream;

            oConfigStream = _oServletConfig.getServletContext().getResourceAsStream( sConfigFileName );

            if ( oConfigStream == null )
            {
               throw new ConfigLoaderException( "config-load: error opening: " + sConfigFileName );
            }

            oConfig = readConfigFile( oConfigStream );
         }
         else if ( _oConfigFile != null )
         {
            oConfig = readConfigFile( _oConfigFile );
         }
         else
         {
            throw new ConfigLoaderException( "config-load: internal error, unrecognized config source" );
         }
      }
      catch ( IOException e )
      {
         throw new ConfigLoaderException( "config-load: I/O error", e );
      }
      catch ( SAXException e )
      {
         throw new ConfigLoaderException( "config-load: XML parse error", e );
      }

      return oConfig;
   }

   private Config readConfigFile( InputStream oConfigStream ) throws IOException, SAXException
   {
      Config   oConfig = new Config();

      DigesterFactory.getDigester( oConfig ).parse( oConfigStream );

      return oConfig;
   }

   private Config readConfigFile( File oConfigFile ) throws IOException, SAXException
   {
      Config   oConfig = new Config();

      DigesterFactory.getDigester( oConfig ).parse( oConfigFile );

      return oConfig;
   }
}

// EOF