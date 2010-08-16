/*
 * Copyright 2010 Acciente, LLC
 *
 * Acciente, LLC licenses this file to you under the
 * Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.acciente.induction.dispatcher.resolver;

import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.controller.Redirect;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * This class is used to generate URLs dynamically from controller/view class names using the
 * mapping rules defined for redirects.
 *
 * @author Adinath Raveendra Raj
 * @created Jul 5, 2010
 */
public class URLResolver
{
   private RedirectResolverExecutor _oRedirectResolverExecutor;
   private HttpServletRequest       _oHttpServletRequest;

   public URLResolver( RedirectResolverExecutor oRedirectResolverExecutor,
                       HttpServletRequest       oHttpServletRequest )
   {
      _oRedirectResolverExecutor = oRedirectResolverExecutor;
      _oHttpServletRequest       = oHttpServletRequest;
   }


   /**
    * Call to resolve a URL from a target controller or view.
    *
    * @param oTargetClass  a class object representing a class that implements the Controller or interface or a view
    * @return a string representing a complete URL
    */
   public String resolve( Class oTargetClass )
   {
      return _oRedirectResolverExecutor.resolveRedirect( _oHttpServletRequest, new Redirect( oTargetClass ) );
   }

   /**
    * Call to resolve a URL from a target controller or view and a map of query parameters.
    *
    * @param oTargetClass  a class object representing a class that implements the Controller or interface or a view
    * @param oURLQueryParameters a map to be converted to URL query parameters
    * @return a string representing a complete URL
    */
   public String resolve( Class oTargetClass, Map oURLQueryParameters )
   {
      return _oRedirectResolverExecutor.resolveRedirect( _oHttpServletRequest, new Redirect( oTargetClass, oURLQueryParameters ) );
   }

   /**
    * Call to resolve a URL from a target controller and a specific target method in the controller.
    *
    * @param oControllerClass  a class object representing a class that implements the Controller interface
    * @param sControllerMethodName a specific method name in the controller that the client
    * should redirect to
    * @return a string representing a complete URL
    */
   public String resolve( Class oControllerClass, String sControllerMethodName )
   {
      return _oRedirectResolverExecutor.resolveRedirect( _oHttpServletRequest, new Redirect( oControllerClass, sControllerMethodName ) );
   }

   /**
    * Call to resolve a URL from a target controller and a specific target method in the 
    * controller and has URL query parameters.
    *
    * @param oControllerClass  a class object representing a class that implements the Controller interface
    * @param sControllerMethodName a specific method name in the controller that the client
    * should redirect to
    * @param oURLQueryParameters a map to be converted to URL query parameters
    * @return a string representing a complete URL
    */
   public String resolve( Class oControllerClass, String sControllerMethodName, Map oURLQueryParameters )
   {
      return _oRedirectResolverExecutor.resolveRedirect( _oHttpServletRequest, new Redirect( oControllerClass, sControllerMethodName, oURLQueryParameters ) );
   }

   /**
    * Call to resolve a URL from a target URL,
    * the URL may be a partial URL that this method is expected to complete. The URL may even
    * simply be a mnemonic reference that is mapped to a complete URL by this method.
    *
    * @param sURLPart a string representing a complete or partial URL
    * @return a string representing a complete URL
    */
   public String resolve( String sURLPart )
   {
      return _oRedirectResolverExecutor.resolveRedirect( _oHttpServletRequest, new Redirect( sURLPart ) );
   }
      

   /**
    * Use to resolve a URL from a target URL
    * and URL query parameters. The URL may be a partial URL that this method is expected to
    * complete. The URL may even simply be a mnemonic reference that is mapped to a complete
    * URL by this method.
    *
    * @param sURLPart a string representing a complete or partial URL
    * @param oURLQueryParameters a map to be converted to URL query parameters
    * @return a string representing a complete URL
    */
   public String resolve( String sURLPart, Map oURLQueryParameters )
   {
      return _oRedirectResolverExecutor.resolveRedirect( _oHttpServletRequest, new Redirect( sURLPart, oURLQueryParameters ) );
   }
}
