package com.acciente.dragonfly.init.config;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.ServletConfig;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
   private File            _oConfigFile;

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
      DocumentBuilder oXMLDocumentBuilder;

      try
      {
         // instantiate a DOM XML parser
         oXMLDocumentBuilder
            =  DocumentBuilderFactory
               .newInstance()
                  .newDocumentBuilder();
      }
      catch ( ParserConfigurationException e )
      {
         throw new ConfigLoaderException( "config-load: XML parser configuration error", e );
      }

      Document    oXMLDocument;

      try
      {
         if ( _oServletConfig != null )
         {
            // first compute the expected name of config file name
            String   sConfigFileName = "/WEB-INF/dragonfly-" + _oServletConfig.getServletName() + ".xml";

            InputStream oXMLConfigFile;

            oXMLConfigFile = _oServletConfig.getServletContext().getResourceAsStream( sConfigFileName );

            if ( oXMLConfigFile == null )
            {
               throw new ConfigLoaderException( "config-load: error opening: " + sConfigFileName );
            }

            oXMLDocument =  oXMLDocumentBuilder.parse( oXMLConfigFile );
         }
         else if ( _oConfigFile != null )
         {
            oXMLDocument =  oXMLDocumentBuilder.parse( _oConfigFile );
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

      return XML2Config( oXMLDocument );
   }

   private Config XML2Config( Document oXMLDocument )
   {
      Config   oConfig = new Config();

      NodeList oJavaClassPath = oXMLDocument.getElementsByTagName( XMLTagName.JAVA_CLASS_PATH );

      for ( int i = 0; i < oJavaClassPath.getLength(); i++ )
      {
         System.out.println( oJavaClassPath.item( i ) );
      }

      return oConfig;
   }
}

// EOF