package com.acciente.dragonfly.init.config;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import javax.servlet.ServletConfig;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
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
    * This constructor was written to faciliate testing by testing the config loader
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

            oConfig = digestConfigFile( oConfigStream );
         }
         else if ( _oConfigFile != null )
         {
            oConfig = digestConfigFile( _oConfigFile );
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

   private Config digestConfigFile( InputStream oConfigStream ) throws IOException, SAXException
   {
      Config   oConfig = new Config();

      getConfigFileDigester( oConfig ).parse( oConfigStream );

      return oConfig;
   }

   private Config digestConfigFile( File oConfigFile ) throws IOException, SAXException
   {
      Config   oConfig = new Config();

      getConfigFileDigester( oConfig ).parse( oConfigFile );

      return oConfig;
   }

   /**
    * Some constants for naming the stacks on the digester
    */
   private static interface DigesterStack
   {
      static final String ControllerResolver    = "stack:ControllerResolver";
      static final String FileUpload            = "stack:FileUpload";
      static final String JavaClassPath         = "stack:JavaClassPath";
      static final String ModelDefs             = "stack:ModelDefs";
      static final String Templating            = "stack:Templating";
   }

   private Digester getConfigFileDigester( Config oConfig )
   {
      Digester oDigester = new Digester();

      oDigester.push( DigesterStack.ControllerResolver,  oConfig.getControllerResolver() );
      oDigester.push( DigesterStack.FileUpload,          oConfig.getFileUpload() );
      oDigester.push( DigesterStack.JavaClassPath,       oConfig.getJavaClassPath() );
      oDigester.push( DigesterStack.ModelDefs,           oConfig.getModelDefs() );
      oDigester.push( DigesterStack.Templating,          oConfig.getTemplating() );

      oDigester.addCallMethod(      "dispatcher-config/java-class-path/compiled-directory",                 "addCompiledDir", 2,  );
      oDigester.addCallParam(       "dispatcher-config/java-class-path/compiled-directory/directory",       0 );
      oDigester.addCallParam(       "dispatcher-config/java-class-path/compiled-directory/package-prefix",  1 );

      //oDigester.addSetTop(  );

      return oDigester;
   }
}

// EOF