package com.acciente.dragonfly.init;

import com.acciente.dragonfly.dispatcher.view.ViewProcessor;
import com.acciente.dragonfly.init.config.Config;
import com.acciente.dragonfly.template.FreemarkerTemplatingEngine;
import com.acciente.dragonfly.template.TemplatingEngine;
import com.acciente.dragonfly.util.ObjectFactory;
import com.acciente.dragonfly.util.ConstructorNotFoundException;
import com.acciente.commons.lang.Strings;

import javax.servlet.ServletConfig;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * This class ...
 *
 * Log
 * Apr 23, 2008 APR  -  created
 */
public class ViewProcessorInitializer
{
   public static ViewProcessor getViewProcessor( Config.Template oTemplateConfig, ClassLoader oClassLoader, ServletConfig oServletConfig, Logger oLogger )
      throws IOException, ClassNotFoundException, InvocationTargetException, ConstructorNotFoundException, IllegalAccessException, InstantiationException
   {
      ViewProcessor  oViewProcessor;

      if ( Strings.isEmpty( oTemplateConfig.getTemplatingEngineProvider().getClassName() ) )
      {
         oViewProcessor = new ViewProcessor( new FreemarkerTemplatingEngine( oTemplateConfig, oServletConfig, oLogger ) );
      }
      else
      {
         Class oTemplatingEngineClass = oClassLoader.loadClass( oTemplateConfig.getTemplatingEngineProvider().getClassName() );

         TemplatingEngine oTemplatingEngine
            =  ( TemplatingEngine )
               ObjectFactory.createObject( oTemplatingEngineClass, new Object[]{ oServletConfig, oTemplateConfig } );

         oViewProcessor = new ViewProcessor( oTemplatingEngine );
      }

      return oViewProcessor;
   }
}

// EOF