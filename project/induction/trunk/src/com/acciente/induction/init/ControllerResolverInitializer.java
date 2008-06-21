package com.acciente.induction.init;

import com.acciente.induction.init.config.Config;
import com.acciente.induction.resolver.ControllerResolver;
import com.acciente.induction.resolver.URLPathControllerResolver;
import com.acciente.induction.util.ObjectFactory;

import javax.servlet.ServletConfig;

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
      throws Exception
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
                                                         oControllerResolverConfig }, null );
      }

      return oControllerResolver;
   }
}

// EOF