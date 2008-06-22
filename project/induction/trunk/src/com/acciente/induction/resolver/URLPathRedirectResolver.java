package com.acciente.induction.resolver;

import com.acciente.induction.init.config.Config;

import javax.servlet.ServletConfig;

/**
 * This class is simple implementation of a default RedirectResolver that works
 * in a manner consistent to the scheme used by the default ControllerResolver
 * class, URLPathControllerResolver.
 *
 * Log
 * Jun 21, 2008 APR  -  created
 */
public class URLPathRedirectResolver implements RedirectResolver
{
   private  ServletConfig     _oServletConfig;

   public URLPathRedirectResolver( Config.RedirectResolver oRedirectResolverConfig, ServletConfig oServletConfig )
   {
      _oServletConfig = oServletConfig;
   }

   public String resolve( Class oControllerClass )
   {
      return   "http://localhost:8080/"
               // + _oServletConfig.getServletName()
               // + "/"
               + oControllerClass.getName().replace( '.', '/' )
               + "/";
   }

   public String resolve( Class oControllerClass, String sControllerMethodName )
   {
      return   "http://localhost:8080/"
               // + _oServletConfig.getServletName()
               // + "/"
               + oControllerClass.getName().replace( '.', '/' )
               + "/"
               + sControllerMethodName;
   }

   public String resolve( String sURL )
   {
      return sURL;
   }
}

// EOF