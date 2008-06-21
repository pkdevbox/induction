package com.acciente.induction.init;

import com.acciente.commons.lang.Strings;
import com.acciente.induction.init.config.Config;
import com.acciente.induction.template.FreemarkerTemplatingEngine;
import com.acciente.induction.template.TemplatingEngine;
import com.acciente.induction.util.ObjectFactory;

import javax.servlet.ServletConfig;

/**
 * This class manages initialization of the templating engine based on the configured settings
 *
 * Log
 * Apr 23, 2008 APR  -  created
 */
public class TemplatingEngineInitializer
{
   public static TemplatingEngine getTemplatingEngine( Config.Templating oTemplatingConfig, ClassLoader oClassLoader, ServletConfig oServletConfig, Logger oLogger )
      throws Exception
   {
      TemplatingEngine  oTemplatingEngine;

      if ( Strings.isEmpty( oTemplatingConfig.getTemplatingEngine().getClassName() ) )
      {
         // if no templating engine is configured use the freemarker engine as the default
         oTemplatingEngine = new FreemarkerTemplatingEngine( oTemplatingConfig, oClassLoader, oServletConfig, oLogger );
      }
      else
      {
         Class oTemplatingEngineClass = oClassLoader.loadClass( oTemplatingConfig.getTemplatingEngine().getClassName() );

         oTemplatingEngine
            =  ( TemplatingEngine )
               ObjectFactory.createObject( oTemplatingEngineClass,
                                           new Object[]{ oServletConfig,
                                                         oTemplatingConfig,
                                                         oClassLoader
                                                       },
                                           null );
      }

      return oTemplatingEngine;
   }
}

// EOF