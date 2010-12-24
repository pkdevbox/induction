/*
 * Copyright 2008-2010 Acciente, LLC
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
package com.acciente.induction.init;

import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.init.config.ConfigLoader;
import com.acciente.induction.init.config.xmlconfigloader.XMLConfigLoader;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.ObjectFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletConfig;
import java.lang.reflect.InvocationTargetException;

/**
 * This class manages the loading of a config loader (yes, the config loader itself
 * is configurable, and the parameter specifying its name is in a sense a bootstrap
 * parameter value).
 *
 * This class also defines the names of any config parameters that may defined
 * directly in the web.xml where the dispatcher servlet is defined. There are few
 * parameters that can be defined directly in the web.xml, since most of the
 * configuration used to initialize the displatcher is expected to be in an XML
 * file named using the convention [servlet-name]-induction.xml residing in the
 * same location as the web.xml file.
 *
 * Currently the only parameter that may be defined in the web.xml is one that
 * allows reading the configuration using a custom configuration reader class
 * in which case no [servlet-name]-induction.xml is expected. In fact the
 * [servlet-name]-induction.xml is what is loaded by the default configuration
 * reader implementation.
 *
 * Any parameter are expected to be
 * prefixed by the name of given to the dispatcher servlet. For example if the
 * dispatcher servlet is named myapp, the an example of a parameter name
 * in the web.xml would look like: myapp.config-loader-class
 *
 * @created Mar 15, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ConfigLoaderInitializer
{
   public static final String CONFIG_LOADER_CLASS = "config-loader-class";

   /**
    * Loads the configuration parameters used to configure every module in this dispatcher servlet.
    *
    * @param oServletConfig provides access to the dispatcher's servlet config
    *
    * @return a container with configuration values
    *
    * @throws ClassNotFoundException propagated exception
    * @throws ConstructorNotFoundException propagated exception
    * @throws IllegalAccessException propagated exception
    * @throws InstantiationException propagated exception
    * @throws InvocationTargetException propagated exception
    *
    * Log
    * Mar 15, 2008 APR  -  created
    */
   public static ConfigLoader getConfigLoader( ServletConfig oServletConfig )
      throws ClassNotFoundException, InvocationTargetException, ConstructorNotFoundException, ParameterProviderException, IllegalAccessException, InstantiationException
   {
      ConfigLoader      oConfigLoader;
      String            sConfigLoaderClassName;
      Log               oLog;

      oLog = LogFactory.getLog( ConfigLoaderInitializer.class );
         
      sConfigLoaderClassName
         =  oServletConfig.getInitParameter( oServletConfig.getServletName()
                                             + "."
                                             + ConfigLoaderInitializer.CONFIG_LOADER_CLASS );

      // first check if there is custom config loader defined
      if ( sConfigLoaderClassName == null )
      {
         // no custom loader defined, use the default XML loader (this is the typical case)
         oConfigLoader = new XMLConfigLoader( "induction-" + oServletConfig.getServletName() + ".xml",
                                              oServletConfig );

         oLog.info( "using default XML config loader" );
      }
      else
      {
         oLog.info( "loading user-defined config loader class: " + sConfigLoaderClassName );

         // note that to load this class we use the default class loader since any of our
         // custom classloaders have to wait until we load in the configuration
         Class oConfigLoaderClass = Class.forName( sConfigLoaderClassName );

         // attempt to find and call the single public constructor
         oConfigLoader
            =  ( ConfigLoader ) ObjectFactory.createObject( oConfigLoaderClass, new Object[]{ oServletConfig }, null );
      }

      return oConfigLoader;
   }
}

// EOF