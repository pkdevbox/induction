package com.acciente.nitrogen.init;

import com.acciente.nitrogen.resolver.ControllerResolver;
import com.acciente.nitrogen.resolver.URLPathControllerResolver;
import com.acciente.nitrogen.init.config.Config;
import com.acciente.nitrogen.util.ConstructorNotFoundException;
import com.acciente.nitrogen.util.ObjectFactory;

import javax.servlet.ServletConfig;
import java.lang.reflect.InvocationTargetException;

/**
 * This is helper class that focuses on setting up the controller resolver used
 * by the dispatcher servlet.
 *
 * Log
 * Mar 15, 2008 APR  -  created
 */
public class ControllerResolverInitializer
{
   public static ControllerResolver getControllerResolver( Config.ControllerResolver oControllerResolverConfig, ClassLoader oClassLoader, ServletConfig oServletConfig, Logger oLogger )
      throws ClassNotFoundException, ConstructorNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException
   {
      ControllerResolver   oControllerResolver;
      String               sControllerResolverClassName = oControllerResolverConfig.getClassName();

      if ( sControllerResolverClassName == null )
      {
         oControllerResolver = new URLPathControllerResolver( oControllerResolverConfig );
      }
      else
      {
         oLogger.log( "loading user-defined config loader class: " + sControllerResolverClassName );

         Class    oControllerResolverClass  = oClassLoader.loadClass( sControllerResolverClassName );

         // attempt to find and call the single public constructor
         oControllerResolver
            =  ( ControllerResolver )
               ObjectFactory.createObject( oControllerResolverClass,
                                           new Object[]{ oServletConfig,
                                                         oControllerResolverConfig } );
      }

      return oControllerResolver;
   }
}

// EOF