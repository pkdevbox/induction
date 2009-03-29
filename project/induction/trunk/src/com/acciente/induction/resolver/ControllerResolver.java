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
    * A container object containg the resolution information.
    *
    * @created Mar 14, 2008
    * @author Adinath Raveendra Raj
    */
   public static class Resolution
   {
      private String    _sClassName;
      private String    _sMethodName;
      private boolean   _bIsIgnoreMethodNameCase;
      private Map       _oOptions;

      /**
       * Creates a resolution object.
       *
       * @param sClassName the fully qualified name of the controller class
       * @param sMethodName the name of the method to invoke in the controller class
       */
      public Resolution( String sClassName, String sMethodName )
      {
         this( sClassName, sMethodName, false, null );
      }

      /**
       * Creates a resolution object.
       *
       * @param sClassName the fully qualified name of the controller class
       * @param sMethodName the name of the method to invoke in the controller class
       * @param bIsIgnoreMethodNameCase tells Induction to ignore case when attempting to find a match for the
       * method name in this resolution.
       */
      public Resolution( String sClassName, String sMethodName, boolean bIsIgnoreMethodNameCase )
      {
         this( sClassName, sMethodName, bIsIgnoreMethodNameCase, null );
      }

      /**
       * Creates a resolution object.
       *
       * @param sClassName the fully qualified name of the controller class
       * @param sMethodName the name of the method to invoke in the controller class
       * @param bIsIgnoreMethodNameCase tells Induction to ignore case when attempting to find a match for the
       * method name in this resolution.
       * @param oOptions is an optional map (may be null) containing data that the resolver wishes to store as part
       * of the resolution. The controller's handler can access this data by choosing have the resolution object
       * injected. This options maps is useful if the resolver is used to map a wide range of requests to a small
       * number of controllers whose behaviour is parameterized by the options map.
       */
      public Resolution( String sClassName, String sMethodName, boolean bIsIgnoreMethodNameCase, Map oOptions )
      {
         if ( sClassName == null )
         {
            throw new IllegalArgumentException( "Controller resolution must define a class name" );
         }

         _sClassName                =  sClassName;
         _sMethodName               =  sMethodName;
         _bIsIgnoreMethodNameCase   =  bIsIgnoreMethodNameCase;
         _oOptions                  =  oOptions;
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

      public Map getOptions()
      {
         return _oOptions;
      }
   }
}

// EOF