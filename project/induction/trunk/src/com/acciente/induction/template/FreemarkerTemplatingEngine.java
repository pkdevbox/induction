/**
 *   Copyright 2008 Acciente, LLC
 *
 *   Acciente, LLC licenses this file to you under the
 *   Apache License, Version 2.0 (the "License"); you
 *   may not use this file except in compliance with the
 *   License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in
 *   writing, software distributed under the License is
 *   distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 *   OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing
 *   permissions and limitations under the License.
 */
package com.acciente.induction.template;

import com.acciente.induction.init.Logger;
import com.acciente.induction.init.config.Config;
import com.acciente.induction.view.Template;
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
 * An implementation of the Induction template engine interface that plugs in the Freemarker templating engine.
 *
 * @created Apr 17, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class FreemarkerTemplatingEngine implements TemplatingEngine
{
   private  Configuration  _oConfiguration;

   public FreemarkerTemplatingEngine( Config.Templating        oConfig,
                                      ClassLoader              oClassLoader,
                                      ServletConfig            oServletConfig,
                                      Logger                   oLogger )
      throws IOException, ClassNotFoundException
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

            if ( ! oDir.getDir().exists() )
            {
               oLogger.log( "freemarker > template load path > ignoring missing directory > "
                              + oDir.getDir() );
            }
            else
            {
               oLogger.log( "freemarker > template load path > adding directory > "
                              + oDir.getDir() );
               oTemplateLoaderList.add( new FileTemplateLoader( oDir.getDir() ) );
            }
         }
         else if ( oLoaderPathItem instanceof Config.Templating.TemplatePath.LoaderClass )
         {
            Config.Templating.TemplatePath.LoaderClass oLoaderClass = ( Config.Templating.TemplatePath.LoaderClass ) oLoaderPathItem;

            Class oClass = Class.forName( oLoaderClass.getLoaderClassName() );

            oLogger.log( "freemarker > template load path > adding class > "
                           + oLoaderClass.getLoaderClassName()
                           + ", prefix: "
                           + oLoaderClass.getPath() );

            oTemplateLoaderList.add( new ClassTemplateLoader( oClass, oLoaderClass.getPath() ) );
         }
         else if ( oLoaderPathItem instanceof Config.Templating.TemplatePath.WebappPath )
         {
            Config.Templating.TemplatePath.WebappPath oWebappPath = ( Config.Templating.TemplatePath.WebappPath ) oLoaderPathItem;

            oLogger.log( "freemarker > template load path > adding webapp path > "
                           + oWebappPath.getPath() );

            oTemplateLoaderList.add( new WebappTemplateLoader( oServletConfig.getServletContext(),
                                                               oWebappPath.getPath() ) );
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
      DefaultObjectWrapper oDefaultObjectWrapper = new DefaultObjectWrapper();

      // should publics fields in views be available in the templates
      oDefaultObjectWrapper.setExposeFields( oConfig.isExposePublicFields() );

      oLogger.log( "freemarker > expose public fields > " + oConfig.isExposePublicFields() );

      _oConfiguration.setObjectWrapper( oDefaultObjectWrapper );

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

   public void process( Template oTemplate, Writer oWriter ) throws TemplatingEngineException, IOException
   {
      if ( oTemplate.getTemplateName() == null )
      {
         throw new IllegalArgumentException( "Templating must specify a template name" );
      }

      try
      {
         _oConfiguration
         .getTemplate( oTemplate.getTemplateName() )
               .process( oTemplate, oWriter );
      }
      catch ( TemplateException e )
      {
         throw new TemplatingEngineException( "Freemaker threw exception during template processing: ", e );
      }
   }

   /**
    * This method was added to allow classes to extend this class to modify the Freemarker configuration
    * @return
    */
   public Configuration getConfiguration()
   {
      return _oConfiguration;
   }
}

// EOF