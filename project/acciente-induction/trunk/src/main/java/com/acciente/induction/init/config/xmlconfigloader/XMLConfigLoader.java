/*
 * Copyright 2008-2012 Acciente, LLC
 *
 * Acciente, LLC licenses this file to you under the
 * Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.acciente.induction.init.config.xmlconfigloader;

import com.acciente.induction.init.config.Config;
import com.acciente.induction.init.config.ConfigLoader;
import com.acciente.induction.init.config.ConfigLoaderException;
import org.xml.sax.SAXException;

import javax.servlet.ServletConfig;
import java.io.IOException;
import java.io.InputStream;

/**
 * An implementation of the ConfigLoader that loads the configuration from an
 * XML file.
 *
 * @created Apr 26, 2008
 *
 * @author Adinath Raveendra Raj
 *
 * todo: support for a DTD validator to catch simple config errors, otherwise hard to trace
 */
public class XMLConfigLoader implements ConfigLoader
{
   private  String         _sResourceName;
   private  ResourceLoader _oResourceLoader;

   public XMLConfigLoader( String sResourceName )
   {
      _sResourceName   = sResourceName;
      _oResourceLoader = ResourceLoader.getResourceLoader();
   }

   public XMLConfigLoader( String sResourceName, ServletConfig oServletConfig )
   {
      _sResourceName   = sResourceName;
      _oResourceLoader = ResourceLoader.getResourceLoader( oServletConfig.getServletContext() );
   }

   public Config getConfig() throws ConfigLoaderException
   {
      InputStream oResourceStream = null;
      Config      oConfig;

      if ( _sResourceName == null )
      {
         throw new ConfigLoaderException( "config-load: no resource to load the config specified!" );
      }

      try
      {
         oResourceStream = _oResourceLoader.getResourceAsStream( _sResourceName );

         if ( oResourceStream == null )
         {
            throw new ConfigLoaderException( "config-load: error opening: " + _sResourceName );
         }

         // load the configuration into a new Config object
         DigesterFactory.getDigester( oConfig = new Config(), _oResourceLoader ).parse( oResourceStream );
      }
      catch ( IOException e )
      {
         throw new ConfigLoaderException( "config-load: I/O error on: " + _sResourceName, e );
      }
      catch ( SAXException e )
      {
         throw new ConfigLoaderException( "config-load: XML parse error in: " + _sResourceName, e );
      }
      finally
      {
         try
         {
            if ( oResourceStream != null )
            {
               oResourceStream.close();
            }
         }
         catch ( IOException e )
         {
            throw new ConfigLoaderException( "config-load: stream close error on: " + _sResourceName, e );
         }
      }

      return oConfig;
   }
}

// EOF






