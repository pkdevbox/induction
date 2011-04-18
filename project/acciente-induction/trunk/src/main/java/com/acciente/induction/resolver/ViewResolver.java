/*
 * Copyright 2008-2011 Acciente, LLC
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
package com.acciente.induction.resolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * This interface is used to abstract the algorithm used to:
 *
 * <ol>
 * <li>map a HTTP request to a view and </li>
 * <li>map a an unhandled exception to a an error handler view</li>
 * </ol>
 * <p>
 * A class implementing this interface is expected to have a single public contructor
 * adhering to the following convention:
 * <ol>
 *   <li>the single constructor should accepts no arguments or</li>
 *   <li>the single constructor should declare formal parameters using only the
 *     following types documented at the URL below:</li>
 * </ol>
 *    http://www.inductionframework.org/param-injection-2-reference.html#ViewresolverCONSTRUCTOR
 * <p>
 * <p>This interface no longer enforces any methods at compile time (Induction 1.4.0b was the last version
 * to enforce compile time resolve() methods), instead this interface now simply serves as a marker now.<p>
 * <p>
 * Induction looks for a method at runtime for implementations of this interface, the details of
 * the methods is given below:<p>
 * <p>
 * Resolution resolveRequest( ... )<p>
 * <p>
 * This method will be called by Induction when it needs to resolve an incoming HTTP request to a view.
 * The method is expected to return a Resolution object describing the view to be invoked, or null if the request
 * did not resolve to a view.
 * This method may request the injection of any value available to a view (including of course an instance
 * of javax.servlet.http.HttpServletRequest), the full list of values available for injection are detailed
 * at the URL below: <p>
 * <p>
 * http://www.inductionframework.org/param-injection-2-reference.html#ViewresolverMETHODScommonlyusedparametertypes<p>
 * <p>
 *
 * @created Mar 29, 2009
 * @updated Jul 04, 2010
 *
 * @author Adinath Raveendra Raj
 */
public interface ViewResolver
{
   /**
    * A container object containing the resolution information.
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