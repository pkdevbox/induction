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

import com.acciente.induction.init.config.Config;
import com.acciente.induction.resolver.ControllerResolver;
import com.acciente.induction.resolver.URLPathControllerResolver;
import com.acciente.induction.util.ObjectFactory;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.commons.reflect.ParameterProviderException;

import javax.servlet.ServletConfig;
import java.lang.reflect.InvocationTargetException;

/**
 * Internal.
 * This is helper class that focuses on setting up the controller resolver used
 * by the dispatcher servlet.
 *
 * @created Mar 15, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ControllerResolverInitializer
{
   public static ControllerResolver getControllerResolver( Config.ControllerResolver   oControllerResolverConfig,
                                                           ClassLoader                 oClassLoader,
                                                           ServletConfig               oServletConfig,
                                                           Logger                      oLogger )
      throws ClassNotFoundException, InvocationTargetException, ConstructorNotFoundException, ParameterProviderException, IllegalAccessException, InstantiationException
   {
      ControllerResolver   oControllerResolver;
      String               sControllerResolverClassName = oControllerResolverConfig.getClassName();

      if ( sControllerResolverClassName == null )
      {
         oControllerResolver = new URLPathControllerResolver( oControllerResolverConfig );
      }
      else
      {
         oLogger.log( "loading user-defined controller resolver: " + sControllerResolverClassName );

         Class    oControllerResolverClass  = oClassLoader.loadClass( sControllerResolverClassName );

         // attempt to find and call the single public constructor
         oControllerResolver
            =  ( ControllerResolver )
               ObjectFactory.createObject( oControllerResolverClass,
                                           new Object[]{ oServletConfig,
                                                         oControllerResolverConfig }, null );
      }

      return oControllerResolver;
   }
}

// EOF