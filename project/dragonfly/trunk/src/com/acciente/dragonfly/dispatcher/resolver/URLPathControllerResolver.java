package com.acciente.dragonfly.dispatcher.resolver;

import com.acciente.dragonfly.dispatcher.resolver.ControllerResolver;
import com.acciente.dragonfly.init.config.Config;

import javax.servlet.http.HttpServletRequest;

/**
 * This class implements a controller resolver that maps the path info in the URL
 * to a controller class name and method name. On the path following the servlet's
 * context path is used. So assuming the servlet's context path is /myapp/cgi then
 * given the URL:
 *
 *    /myapp/cgi/myapp/cart/CartController/addItem?item_id=widget-202&qty=500
 *
 * the following part of the URL will be used for determining which controller/method
 * to invoke:
 *
 *    /myapp/cart/CartController/additem
 *
 * which would resolve to:
 *
 *    class-name:    myapp.cart.CartController
 *    method-name:   addItem
 *
 * If the path ends with a trailing slash the, it is assumed that no method name is given
 * and the resolver will set the method name to null, which will cause the framework to
 * invoke a default user-configurable controller method name.
 *
 * Log
 * Mar 15, 2008 APR  -  created
 */
public class URLPathControllerResolver implements ControllerResolver
{
   private String    _sDefaultHandlerMethodName;
   private boolean   _bIsIgnoreMethodNameCase;

   public URLPathControllerResolver( Config.ControllerResolver oConfig )
   {
      _sDefaultHandlerMethodName = oConfig.getDefaultHandlerMethodName();
      _bIsIgnoreMethodNameCase   = oConfig.isIgnoreMethodNameCase();
   }

   public Resolution resolve( HttpServletRequest oRequest )
   {
      Resolution oResolution = null;
      String     sClassName, sMethodName;

      String   sPath = oRequest.getPathInfo();

      if ( sPath != null )
      {
         if ( sPath.endsWith( "/" ) )
         {
            // no method name specified
            sClassName  =  sPath.substring( 0, sPath.length() - 1 ).replace( '/', '.' );
            sMethodName =  _sDefaultHandlerMethodName;
         }
         else
         {
            int iIndexOfLastSlash = sPath.lastIndexOf( "/" );

            if ( iIndexOfLastSlash == -1 )
            {
               // no slashes found in the path, so assume no method name specified
               sClassName  =  sPath;
               sMethodName =  _sDefaultHandlerMethodName;
            }
            else
            {
               // ok slash found, so assume what comes after the last slash is a method name
               sClassName  =  sPath.substring( 0, iIndexOfLastSlash ).replace( '/', '.' );
               sMethodName =  sPath.substring( iIndexOfLastSlash + 1 );
            }
         }

         oResolution = new Resolution( sClassName, sMethodName, _bIsIgnoreMethodNameCase );
      }

      return oResolution;
   }
}

// EOF