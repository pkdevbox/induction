package com.acciente.dragonfly.init.config;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.servlet.ServletConfig;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Log
 * Apr 26, 2008 APR  -  created
 */
public class XMLConfigLoader implements ConfigLoader
{
   private  ServletConfig  _oServletConfig;

   public XMLConfigLoader( ServletConfig oServletConfig )
   {
      _oServletConfig = oServletConfig;
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

      // first compute the expected name of config file name
      String   sConfigFileName = "/WEB-INF/dragonfly-" + _oServletConfig.getServletName() + ".xml";

      try
      {
         InputStream oXMLConfigFile;

         oXMLConfigFile = _oServletConfig.getServletContext().getResourceAsStream( sConfigFileName );

         if ( oXMLConfigFile == null )
         {
            throw new ConfigLoaderException( "config-load: error opening: " + sConfigFileName );
         }

         oXMLDocument   =  oXMLDocumentBuilder.parse( oXMLConfigFile );
      }
      catch ( IOException e )
      {
         throw new ConfigLoaderException( "config-load: I/O error", e );
      }
      catch ( SAXException e )
      {
         throw new ConfigLoaderException( "config-load: XML parse error", e );
      }

      return readConfig( oXMLDocument );
   }

   private Config readConfig( Document oXMLDocument )
   {
      System.out.println( "XML config: " + oXMLDocument );

      return null;
   }
}

// EOF