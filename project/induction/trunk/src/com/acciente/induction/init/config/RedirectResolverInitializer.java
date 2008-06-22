package com.acciente.induction.init.config;

import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.init.Logger;
import com.acciente.induction.resolver.RedirectResolver;
import com.acciente.induction.resolver.URLPathRedirectResolver;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.ObjectFactory;

import javax.servlet.ServletConfig;
import java.lang.reflect.InvocationTargetException;

/**
 * Log
 * Jun 21, 2008 APR  -  created
 */
public class RedirectResolverInitializer
{
   public static RedirectResolver getRedirectResolver(   Config.RedirectResolver    oRedirectResolverConfig,
                                                         ClassLoader                oClassLoader,
                                                         ServletConfig              oServletConfig,
                                                         Logger oLogger )
      throws ClassNotFoundException, InvocationTargetException, ConstructorNotFoundException, ParameterProviderException, IllegalAccessException, InstantiationException
   {
      RedirectResolver  oRedirectResolver;
      String            sRedirectResolverClassName = oRedirectResolverConfig.getClassName();

      if ( sRedirectResolverClassName == null )
      {
         oRedirectResolver = new URLPathRedirectResolver( oRedirectResolverConfig, oServletConfig );
      }
      else
      {
         oLogger.log( "loading user-defined redirect resolver: " + sRedirectResolverClassName );

         Class    oRedirectResolverClass  = oClassLoader.loadClass( sRedirectResolverClassName );

         // attempt to find and call the single public constructor
         oRedirectResolver
            =  ( RedirectResolver )
               ObjectFactory.createObject( oRedirectResolverClass,
                                           new Object[]{ oServletConfig,
                                                         oRedirectResolverConfig }, null );
      }

      return oRedirectResolver;
   }
}

// EOF