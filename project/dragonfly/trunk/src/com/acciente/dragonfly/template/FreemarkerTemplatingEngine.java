package com.acciente.dragonfly.template;

import com.acciente.dragonfly.init.Logger;
import com.acciente.dragonfly.init.config.Config;
import com.acciente.dragonfly.view.Template;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;

import javax.servlet.ServletConfig;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class implements a template engine that uses the Freemarker templating engine
 *
 * Log
 * Apr 17, 2008 APR  -  created
 */
public class FreemarkerTemplatingEngine implements TemplatingEngine
{
   private  Configuration  _oConfiguration;

   public FreemarkerTemplatingEngine( Config.Templating oConfig, ClassLoader oClassLoader, ServletConfig oServletConfig, Logger oLogger ) throws IOException, ClassNotFoundException
   {
      _oConfiguration = new Configuration();

      // set up the template loading path
      List oTemplateLoaderList = new ArrayList( oConfig.getTemplatePath().getList().size() );

      for ( Iterator oIter = oConfig.getTemplatePath().getList().iterator(); oIter.hasNext(); )
      {
         Object oLoaderPathItem = oIter.next();

         if ( oLoaderPathItem instanceof Config.Templating.TemplatePath.Dir )
         {
            Config.Templating.TemplatePath.Dir oDir = ( Config.Templating.TemplatePath.Dir ) oLoaderPathItem;

            oTemplateLoaderList.add( new FileTemplateLoader( oDir.getDir() ) );

            oLogger.log( "freemarker > template load path > adding directory > "
                           + oDir.getDir() );
         }
         else if ( oLoaderPathItem instanceof Config.Templating.TemplatePath.LoaderClass )
         {
            Config.Templating.TemplatePath.LoaderClass oLoaderClass = ( Config.Templating.TemplatePath.LoaderClass ) oLoaderPathItem;

            Class oClass = Class.forName( oLoaderClass.getLoaderClassName() );

            oTemplateLoaderList.add( new ClassTemplateLoader( oClass, oLoaderClass.getPath() ) );

            oLogger.log( "freemarker > template load path > adding class > "
                           + oLoaderClass.getLoaderClassName()
                           + ", prefix: "
                           + oLoaderClass.getPath() );
         }
         else if ( oLoaderPathItem instanceof Config.Templating.TemplatePath.WebappPath )
         {
            Config.Templating.TemplatePath.WebappPath oWebappPath = ( Config.Templating.TemplatePath.WebappPath ) oLoaderPathItem;

            oTemplateLoaderList.add( new WebappTemplateLoader( oServletConfig.getServletContext(),
                                                               oWebappPath.getPath() ) );

            oLogger.log( "freemarker > template load path > adding webapp path > "
                           + oWebappPath.getPath() );
         }
         else
         {
            throw new IllegalArgumentException( "Unexpected template path type in configuration: " + oLoaderPathItem.getClass() );
         }
      }

      TemplateLoader[] oTemplateLoaderArray = new TemplateLoader[ oTemplateLoaderList.size() ];
      oTemplateLoaderList.toArray( oTemplateLoaderArray );
      _oConfiguration.setTemplateLoader( new MultiTemplateLoader( oTemplateLoaderArray ) );

      // next set the object wrapper handler
      _oConfiguration.setObjectWrapper( new DefaultObjectWrapper() );

      if ( oConfig.getLocale() != null )
      {
         _oConfiguration.setLocale( oConfig.getLocale() );
         oLogger.log( "freemarker > using configured locale > " + oConfig.getLocale() );
      }
      else
      {
         oLogger.log( "freemarker > no locale configured, using default > " + _oConfiguration.getLocale() );
      }
   }

   public void process( Template oTemplate, Writer oWriter ) throws IOException, TemplateException
   {
      if ( oTemplate.getTemplateName() == null )
      {
         throw new IllegalArgumentException( "Templating must specify a template name" );
      }

      _oConfiguration.getTemplate( oTemplate.getTemplateName() ).process( oTemplate, oWriter );
   }
}

// EOF