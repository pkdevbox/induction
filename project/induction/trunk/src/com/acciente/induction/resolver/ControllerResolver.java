/**
 *   Copyright 2008 Acciente, LLC
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

/**
 * This interface is used to abstract the algorithm used to map a HTTP request to a specific
 * controller invocation.<p>
 * <p>
 * A class implementing this interface is expected to have a single public contructor
 * adhering to the following convention:<p>
 *   - the single constructor should accepts no arguments or<p>
 *   - the single constructor should declare formal parameters using only the
 *     following types:<p>
 *     - javax.servlet.ServletContext<p>
 *     - com.acciente.induction.init.config.Config.ControllerResolver<p>
 *
 * @created Mar 14, 2008
 *
 * @author Adinath Raveendra Raj
 */
public interface ControllerResolver
{
   /**
    * This method should resolve the class name of the controller and method name within same
    * to be invoked in response to the specified HTTP request
    *
    * @param oRequest the HTTP request context in which the resolution is requested
    * @return  an object containing the class name of the controller to be invoked and
    *          the method name within same
    */
   Resolution resolve( HttpServletRequest oRequest );

   /**
    * Log
    * Mar 14, 2008 APR  -  created
    */
   public static class Resolution
   {
      private String    _sClassName;
      private String    _sMethodName;
      private boolean   _bIsIgnoreMethodNameCase;

      public Resolution( String sClassName, String sMethodName, boolean bIsIgnoreMethodNameCase )
      {
         if ( sClassName == null )
         {
            throw new IllegalArgumentException( "Controller resolution must define a class name" );
         }

         _sClassName                =  sClassName;
         _sMethodName               =  sMethodName;
         _bIsIgnoreMethodNameCase   =  bIsIgnoreMethodNameCase;
      }

      public String getClassName()
      {
         return _sClassName;
      }

      public String getMethodName()
      {
         return _sMethodName;
      }

      public boolean isIgnoreMethodNameCase()
      {
         return _bIsIgnoreMethodNameCase;
      }
   }
}
