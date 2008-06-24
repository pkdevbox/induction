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
package com.acciente.induction.init;

import com.acciente.commons.lang.Strings;
import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.init.config.Config;
import com.acciente.induction.template.FreemarkerTemplatingEngine;
import com.acciente.induction.template.TemplatingEngine;
import com.acciente.induction.util.ObjectFactory;
import com.acciente.induction.util.ConstructorNotFoundException;

import javax.servlet.ServletConfig;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Internal.
 * This class manages initialization of the templating engine based on the configured settings
 *
 * @created Apr 23, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class TemplatingEngineInitializer
{
   public static TemplatingEngine getTemplatingEngine( Config.Templating   oTemplatingConfig,
                                                       ClassLoader         oClassLoader,
                                                       ServletConfig       oServletConfig,
                                                       Logger oLogger )
      throws ClassNotFoundException, IOException, InvocationTargetException, ConstructorNotFoundException, ParameterProviderException, IllegalAccessException, InstantiationException
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