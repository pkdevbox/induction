/*
 * Copyright 2008-2013 Acciente, LLC
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
package com.acciente.induction.interceptor;

/**
 * This is an interface does not enforce any methods at compile time. Implementations of this interface
 * are expected to have one or more of the following methods:<p>
 *
 *    <code>preResolution()</code><p>
 *    <code>postResolution()</code><p>
 *    <code>preResponse()</code><p>
 *    <code>postResponse()</code><p>
 *
 * Induction checks for the existence of these methods dynamically at runtime.<p>
 *
 * <code>Object preResolution()</code><p>
 *    This interceptor method is invoked PRIOR to attempting to resolve the target of the
 *    http request using the controller and view resolvers.<p>
 *
 *    For params available for injection please see:<p>
 *       {@see <a href="Param Injection I (Reference)">/param-injection-1-reference.html#InterceptorMETHODScommonlyusedparametertypes</a>}<p>
 *
 *    Meaning of return value:<p>
 *       If true is returned then no further processing is done on this request, this means no further
 *       interceptors are executed and no view or controller will be executed. If a value other than true is
 *       returned or no value is returned the execution continues to the next interceptor in the chain (if any).<p>
 *
 * <code>Object postResolution()</code><p>
 *    This interceptor method is invoked AFTER attempting to resolve the target of the
 *    http request using the controller and view resolvers.<p>
 *
 *    For params available for injection please see:<p>
 *       {@see <a href="Param Injection I (Reference)">/param-injection-1-reference.html#InterceptorMETHODScommonlyusedparametertypes</a>}<p>
 *
 *    Meaning of return value:<p>
 *       If true is returned then no further processing is done on this request, this means no further
 *       interceptors are executed and no view or controller will be executed. If a value other than true is
 *       returned or no value is returned the execution continues to the next interceptor in the chain (if any).<p>
 *
 * <code>Object preResponse()</code><p>
 *    This interceptor method called as follows:<p>
 *       -  if the request resolved to a controller then this interceptor method is called
 *          AFTER the controller is executed but BEFORE the view or redirect object returned
 *          by the controller is processed.<p>
 *       -  if the request resolved to a view then this interceptor method is called
 *          BEFORE the view is processed.<p>
 *
 *       Note: even though the interceptor is called <code>preResponse</code>, it is possible
 *       that the controller may have written to the response directly using a response object.
 *       But the name is consistent with the fact that this method called before any response
 *       that Induction sends to the client by executing a view (returned by a controller or
 *       view resolver) or a redirect object (returned by a controller).<p>
 *
 *    For params available for injection please see:<p>
 *       {@see <a href="Param Injection I (Reference)">/param-injection-1-reference.html#InterceptorMETHODScommonlyusedparametertypes</a>}<p>
 *
 *    Meaning of return value:<p>
 *       If true is returned then no further processing is done on this request, this means no further
 *       interceptors are executed and the view or controller response value will not be processed. If a value
 *       other than true is returned or no value is returned the execution continues to the next interceptor
 *       in the chain (if any).<p>
 *
 * <code>Object postResponse()</code><p>
 *    This interceptor method is invoked AFTER processing a view or redirect object.<p>
 *
 *    For params available for injection please see:<p>
 *       {@see <a href="Param Injection I (Reference)">/param-injection-1-reference.html#InterceptorMETHODScommonlyusedparametertypes</a>}<p>
 *
 *    Meaning of return value:<p>
 *       If true is returned then no further processing is done on this request, this means no further
 *       interceptors are executed. If a value other than fasle is returned or no value is returned the execution
 *       continues to the next interceptor in the chain (if any).<p>
 *
 * @author Adinath Raveendra Raj
 * @created Aug 30, 2009
 */
public interface RequestInterceptor
{
}