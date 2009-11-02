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

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * This interface is used to abstract the algorithm used to map a HTTP request to a specific
 * view invocation.<p>
 * <p>
 * A class implementing this interface is expected to have a single public contructor
 * adhering to the following convention:<p>
 *   - the single constructor should accepts no arguments or<p>
 *   - the single constructor should declare formal parameters using only the
 *     following types:<p>
 *     - javax.servlet.ServletConfig<p>
 *     - com.acciente.induction.init.config.Config.ViewResolver<p>
 *     - java.lang.ClassLoader (to get the Induction reloading classloader)
 *     - any user defined model class (to get the respective model object)
 *
 * @created Mar 29, 2009
 *
 * @author Adinath Raveendra Raj
 */
public interface ViewResolver
{
   /**
    * This method should resolve the class name of the view and method name within same
    * to be invoked in response to the specified HTTP request
    *
    * @param oRequest the HTTP request context in which the resolution is requested
    * @return  an object containing the class name of the view to be invoked and
    *          the method name within same
    */
   Resolution resolve( HttpServletRequest oRequest );

   /**
    * A container object containg the resolution information.
    *
    * @created Mar 14, 2008
    * @author Adinath Raveendra Raj
    */
   public static class Resolution
   {
      private String    _sClassName;
      private Map       _oOptions;

      /**
       * Creates a resolution object.
       *
       * @param sClassName the fully qualified name of the view class
       */
      public Resolution( String sClassName )
      {
         this( sClassName, null );
      }

      /**
       * Creates a resolution object.
       *
       * @param sClassName the fully qualified name of the view class
       * method name in this resolution.
       * @param oOptions is an optional map (may be null) containing data that the resolver wishes to store as part
       * of the resolution. The view's handler can access this data by choosing have the resolution object
       * injected. This options maps is useful if the resolver is used to map a wide range of requests to a small
       * number of views whose behaviour is parameterized by the options map.
       */
      public Resolution( String sClassName, Map oOptions )
      {
         if ( sClassName == null )
         {
            throw new IllegalArgumentException( "View resolution must define a class name" );
         }

         _sClassName                =  sClassName;
         _oOptions                  =  oOptions;
      }


      public String getClassName()
      {
         return _sClassName;
      }

      public Map getOptions()
      {
         return _oOptions;
      }
   }
}