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
 * This class implements a view resolver that maps the path info in the URL
 * to a view class name and method name. Only the path returned by the servlet
 * API call getPathInfo() is used. So assuming the servlet's context path is /myapp/cgi
 * then given a request URL:<p>
 * <p>
 *    /myapp/cgi/myapp/cart/CartView?item_id=widget-202&qty=500<p>
 *
 * the getPathInfo() API call returns the following part of the URL (which will be used for 
 * determining which view/method to invoke):<p>
 * <p>
 *    /myapp/cart/CartView<p>
 * <p>
 * which would resolve to:<p>
 * <p>
 *    class-name:    myapp.cart.CartView<p>
 * <p>
 * If the path ends with a trailing slash, it is ignored<p>
 *
 * @created Mar 30, 2009
 *
 * @author Adinath Raveendra Raj
 */
public class FullyQualifiedClassnameViewResolver implements ViewResolver
{
   public FullyQualifiedClassnameViewResolver( Config.ViewMapping  oViewMappingConfig )
   {
   }

   public Resolution resolve( HttpServletRequest oRequest )
   {
      String     sClassName;

      String   sPath = oRequest.getPathInfo();

      if ( sPath != null && sPath.length() > 1 && sPath.indexOf( "View" ) != -1 )
      {
         if ( sPath.endsWith( "/" ) )
         {
            sClassName  =  sPath.substring( 1, sPath.length() - 1 ).replace( '/', '.' );
         }
         else
         {
            sClassName  =  sPath.substring( 1, sPath.length() ).replace( '/', '.' );
         }

         return new Resolution( sClassName );
      }

      return null;
   }
}