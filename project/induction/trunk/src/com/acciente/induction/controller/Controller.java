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
package com.acciente.induction.controller;

/**
 * This interface is used to mark a specific class as a controller. This interface exists
 * primarily to enhance security by explicitly marking a class that may be invoked as a
 * controller by the framework.<p>
 *
 * A class implementing this interface is expected to have a single public contructor
 * with its formal parameter list observing the following convention:<p>
 *   - the single constructor should accept no formal parameters or<p>
 *   - the single constructor should declare formal parameters using only the<p>
 *     following types:<p>
 *     - javax.servlet.ServletContext<p>
 *     - javax.servlet.ServletConfig<p>
 *
 * A class implementing this interface may optionally define a single public method
 * named init(). If defined this method will be called after the controller is
 * created using the constructor. The init() method if defined should have its
 * formal parameter list observe the convention described above for the constructor.<p>
 *
 * A class implementing this interface may optionally define a single public method
 * named destroy(). If defined this method will be called when the controller instance is
 * taken out of service. If there is more than one destroy method or if it the single
 * destroy method requires arguments then the method will be ignored.<p>
 *
 * @created Mar 14, 2008
 *
 * @author Adinath Raveendra Raj
 */
public interface Controller
{
}

// EOF