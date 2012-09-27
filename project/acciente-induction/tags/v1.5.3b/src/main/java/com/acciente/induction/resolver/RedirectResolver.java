/*
 * Copyright 2008-2012 Acciente, LLC
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

import java.util.Map;

/**
 * This interface is used to abstract the algorithm used to map a redirect specified in terms of a class type to a URL.
 * <p>
 * A class implementing this interface is expected to have a single public contructor
 * adhering to the following convention:
 * <ol>
 *   <li>the single constructor should accepts no arguments or</li>
 *   <li>the single constructor should declare formal parameters using only the
 *     following types documented at the URL below:</li>
 * </ol>
 *    http://www.inductionframework.org/param-injection-2-reference.html#RedirectresolverCONSTRUCTOR
 * <p>
 * <p>This interface no longer enforces any methods at compile time (Induction 1.4.0b was the last version
 * to enforce compile time resolve() methods), instead this interface now simply serves as a marker now.<p>
 * <p>
 * Induction looks for a method at runtime for implementations of this interface, the details of
 * the the method is given below:<p>
 * <p>
 * Resolution resolveRedirect( ... )<p>
 * <p>
 * This method will be called by Induction when it needs to resolve an Induction redirect object (typically this object
 * contains the Java class type of the controller or view which is the target of the redirect) to a fully qualified URL.
 * The method is expected to return a fully qualified URL string.
 * This method may request the injection of the Induction Redirect object and additionally any value available to a
 * controller, the full list of additional values available for injection are detailed at the URL below:<p>
 * <p>
 * http://www.inductionframework.org/param-injection-1-reference.html#ControllerMETHODScommonlyusedparametertypes<p>
 * <p>
 *
 * @created Jun 21, 2008
 * @updated Jul 04, 2010
 *
 * @author Adinath Raveendra Raj
 */
public interface RedirectResolver
{
}

// EOF