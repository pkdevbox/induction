/**
 *   Copyright 2009 Acciente, LLC
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
package com.acciente.induction.resolver;

import com.acciente.induction.init.config.Config;

import javax.servlet.http.HttpServletRequest;

/**
 * This class implements a controller resolver that maps the path info in the URL
 * to a controller class name and method name. On the path following the servlet's
 * context path is used. So assuming the servlet's context path is /myapp/cgi then
 * given the URL:<p>
 * <p>
 *    /myapp/cgi/myapp/cart/CartController/addItem?item_id=widget-202&qty=500<p>
 *
 * the following part of the URL will be used for determining which controller/method
 * to invoke:<p>
 * <p>
 *    /myapp/cart/CartController/additem<p>
 * <p>
 * which would resolve to:<p>
 * <p>
 *    class-name:    myapp.cart.CartController<p>
 *    method-name:   addItem<p>
 * <p>
 * If the path ends with a trailing slash the, it is assumed that no method name is given
 * and the resolver will set the method name to null, which will cause the framework to
 * invoke a default user-configurable controller method name.<p>
 *
 * @created Mar 15, 2008
 *
 * @author Adinath Raveendra Raj
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

      if ( sPath != null && sPath.length() > 1 )
      {
         if ( sPath.endsWith( "/" ) )
         {
            // no method name specified
            sClassName  =  sPath.substring( 1, sPath.length() - 1 ).replace( '/', '.' );
            sMethodName =  _sDefaultHandlerMethodName;
         }
         else
         {
            int iIndexOfLastSlash;

            // we assume that a path always has a / as the first character
            iIndexOfLastSlash = sPath.substring( 1 ).lastIndexOf( "/" );

            if ( iIndexOfLastSlash == -1 )
            {
               // no slashes found in the path, so assume no method name specified
               sClassName  =  sPath.substring( 1 ).replace( '/', '.' );
               sMethodName =  _sDefaultHandlerMethodName;
            }
            else
            {
               // ok slash found, so assume what comes after the last slash is a method name
               sClassName  =  sPath.substring( 1, iIndexOfLastSlash + 1 ).replace( '/', '.' );
               sMethodName =  sPath.substring( iIndexOfLastSlash + 2 );
            }
         }

         oResolution = new Resolution( sClassName, sMethodName, _bIsIgnoreMethodNameCase );
      }

      return oResolution;
   }
}

// EOF