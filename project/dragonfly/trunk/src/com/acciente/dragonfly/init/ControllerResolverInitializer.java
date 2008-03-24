package com.acciente.dragonfly.init;

import com.acciente.commons.reflect.Invoker;
import com.acciente.dragonfly.dispatcher.resolver.ControllerResolver;
import com.acciente.dragonfly.dispatcher.resolver.URLPathControllerResolver;
import com.acciente.dragonfly.init.config.Config;
import com.acciente.dragonfly.util.ConstructorNotFoundException;
import com.acciente.dragonfly.util.ReflectUtils;

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
   public static ControllerResolver getControllerResolver( Config.ControllerResolver oConfig, ClassLoader oClassLoader, ServletConfig oServletConfig, Logger oLogger )
      throws ClassNotFoundException, ConstructorNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException
   {
      ControllerResolver   oControllerResolver;
      String               sControllerResolverClassName = oConfig.getClassName();

      if ( sControllerResolverClassName == null )
      {
         oControllerResolver = new URLPathControllerResolver( oConfig );
      }
      else
      {
         oLogger.log( "loading user-defined config loader class: " + sControllerResolverClassName );

         Class    oControllerResolverClass  = oClassLoader.loadClass( sControllerResolverClassName );

         // attempt to find and call the single public constructor
         oControllerResolver
            =  ( ControllerResolver )
               Invoker.invoke( ReflectUtils.getSingletonConstructor( oControllerResolverClass ),
                               new Object[]{ oServletConfig,
                                             oConfig
                                           }
                             );
      }

      return oControllerResolver;
   }
}

// EOF