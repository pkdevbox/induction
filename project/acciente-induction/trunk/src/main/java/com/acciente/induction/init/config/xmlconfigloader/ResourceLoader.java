/*
 * Copyright 2008-2013 Acciente, LLC
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

import javax.servlet.ServletContext;
import java.io.InputStream;

/**
 * ResourceLoader
 *
 * This class was introduced to factor out the mechanism used to load config files.
 * The factoring was motivated to provide both the XMLConfigLoader and the IncludeRule classes
 * a consistent way to load a configuration file. 
 *
 * @author Adinath Raveendra Raj
 * @created Feb 25, 2009
 */
public class ResourceLoader
{
   private static ResourceLoader    __oSingletonResourceLoader;

   private  ServletContext          _oServletContext;

   public static ResourceLoader getResourceLoader()
   {
      if ( __oSingletonResourceLoader == null )
      {
         __oSingletonResourceLoader = new ResourceLoader();
      }

      return __oSingletonResourceLoader;
   }

   public static ResourceLoader getResourceLoader( ServletContext oServletContext )
   {
      return new ResourceLoader( oServletContext );
   }

   // private constructors

   private ResourceLoader()
   {
   }

   private ResourceLoader( ServletContext oServletContext )
   {
      _oServletContext = oServletContext;
   }

   public InputStream getResourceAsStream( String sResourceName )
   {
      InputStream oResourceStream = null;

      // if we are running in a servlet container, first try to load the resource using the servlet context
      if ( _oServletContext != null )
      {
         oResourceStream = _oServletContext.getResourceAsStream( "/WEB-INF/" + sResourceName );
      }

      // if we are not in a servlet container or if loading via the servlet context failed,
      // try the using the classloader that loaded this class
      if ( oResourceStream == null )
      {
         oResourceStream = getClass().getClassLoader().getResourceAsStream( sResourceName );
      }

      return oResourceStream;
   }
}

// EOF