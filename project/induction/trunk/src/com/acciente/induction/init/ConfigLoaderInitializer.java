package com.acciente.induction.init;

import com.acciente.induction.init.config.ConfigLoader;
import com.acciente.induction.init.config.xmlconfigloader.XMLConfigLoader;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.ObjectFactory;

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
 * Log
 * Mar 15, 2008 APR  -  created
 */
public class ConfigLoaderInitializer
{
   public static final String CONFIG_LOADER_CLASS = "config-loader-class";

   /**
    * Loads the configuration parameters used to configure every module in this dispatcher servlet.
    *
    * @param oLogger provides access to the dispatcher's primary logger
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
   public static ConfigLoader getConfigLoader( ServletConfig oServletConfig, Logger oLogger )
      throws Exception
   {
      ConfigLoader oConfigLoader;
      String         sConfigLoaderClassName;

      sConfigLoaderClassName
         =  oServletConfig.getInitParameter( oServletConfig.getServletName()
                                             + "."
                                             + ConfigLoaderInitializer.CONFIG_LOADER_CLASS );

      // first check if there is custom config loader defined
      if ( sConfigLoaderClassName == null )
      {
         // no custom loader defined, use the default XML loader (this is the typical case)
         oConfigLoader = new XMLConfigLoader( oServletConfig );

         oLogger.log( "using default XML config loader" );
      }
      else
      {
         oLogger.log( "loading user-defined config loader class: " + sConfigLoaderClassName );

         // not that to load this class we use the default class loader since any of our
         // custom classloaders have to wait until later
         Class oConfigLoaderClass = Class.forName( sConfigLoaderClassName );

         // attempt to find and call the single public constructor
         oConfigLoader
            =  ( ConfigLoader ) ObjectFactory.createObject( oConfigLoaderClass, new Object[]{ oServletConfig }, null );
      }

      return oConfigLoader;
   }
}

// EOF