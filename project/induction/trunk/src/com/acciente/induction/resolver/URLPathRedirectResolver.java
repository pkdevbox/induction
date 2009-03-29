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

import javax.servlet.ServletConfig;

/**
 * This class is an illustrative implementation of a RedirectResolver that works
 * in a manner consistent to the scheme used by the default ControllerResolver
 * class, URLPathControllerResolver. If you are going to use Induction redirect support
 * please replace this class with an implementation that would make sense for your application.
 *
 * @created Jun 21, 2008
 *
 * @author Adinath Raveendra Raj
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