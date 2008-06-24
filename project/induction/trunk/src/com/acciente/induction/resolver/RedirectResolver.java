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

/**
 * This interface is used to abstract the algorithm used to map a redirect request to an actual URL
 * <p>
 * A class implementing this interface is expected to have a single public contructor
 * adhering to the following convention:<p>
 *   - the single constructor should accepts no arguments or<p>
 *   - the single constructor should declare formal parameters using only the
 *     following types:<p>
 *     - javax.servlet.ServletContext<p>
 * 
 * @created Jun 21, 2008
 *
 * @author Adinath Raveendra Raj
 */
public interface RedirectResolver
{
   /**
    * Called by Induction to resolve a redirect request defined on in terms of a target controller.
    *
    * @param oControllerClass  a class object representing a class that implements the Controller interface
    * @return a string represting a complete URL
    */
   public String resolve( Class oControllerClass );

   /**
    * Called by Induction to resolve a redirect request defined on in terms of a target controller
    * and a specific target method in the controller.
    *
    * @param oControllerClass  a class object representing a class that implements the Controller interface
    * @param sControllerMethodName a specific method name in the controller that the client
    * should redirect to
    * @return a string represting a complete URL
    */
   public String resolve( Class oControllerClass, String sControllerMethodName );

   /**
    * Called by Induction to resolve a redirect request defined on in terms of a target URL,
    * the URL may be a partial URL that this method is expected to complete. The URL may even
    * simply be a mnemonic reference that is mapped to a complete URL by this method.
    *
    * @param sURL a string representing a complete or partial URL
    * @return a string represting a complete URL
    */
   public String resolve( String sURL );
}

// EOF