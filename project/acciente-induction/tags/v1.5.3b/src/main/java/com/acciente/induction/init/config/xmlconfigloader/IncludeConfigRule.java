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
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * IncludeConfigRule
 *
 * @author Adinath Raveendra Raj
 * @created Feb 24, 2009
 */
public class IncludeConfigRule extends Rule
{
   public static final String Config_LoadConfig_Resource = "resource";

   private  Config            _oConfig;
   private  ResourceLoader    _oResourceLoader;

   public IncludeConfigRule( Config oConfig, ResourceLoader oResourceLoader )
   {
      _oConfig          = oConfig;
      _oResourceLoader  = oResourceLoader;
   }

   public void begin( String sNamespace, String sName, Attributes oAttributes ) throws XMLConfigLoaderException
   {
      String      sConfigResourceName     = oAttributes.getValue( Config_LoadConfig_Resource );
      InputStream oResourceStream = null;

      if ( sConfigResourceName == null )
      {
         throw new XMLConfigLoaderException( "config > load-config > a value must be specified for attribute > " + Config_LoadConfig_Resource );
      }

      try
      {
         oResourceStream = _oResourceLoader.getResourceAsStream( sConfigResourceName );

         if ( oResourceStream == null )
         {
            throw new XMLConfigLoaderException( "config > load-config > " + sConfigResourceName + " > open error" );
         }

         // cumulate the "included" configuration into _oConfig
         DigesterFactory.getDigester( _oConfig, _oResourceLoader ).parse( oResourceStream );
      }
      catch ( IOException e )
      {
         throw new XMLConfigLoaderException( "config > load-config > I/O error > " + sConfigResourceName, e );
      }
      catch ( SAXException e )
      {
         throw new XMLConfigLoaderException( "config > load-config> XML parse error > " + sConfigResourceName, e );
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
            throw new XMLConfigLoaderException( "config > load-config > error closing resource > " + sConfigResourceName + "", e );
         }
      }
   }
}

// EOF






