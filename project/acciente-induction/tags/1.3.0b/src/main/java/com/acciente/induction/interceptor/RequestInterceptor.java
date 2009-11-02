package com.acciente.induction.interceptor;

/**
 * RequestInterceptor
 *
 * This is a interface does not enforce any methods at compile time. Implementations of this interface
 * are expected to have one or more of the following methods:
 *
 *    preResolution( ... )
 *    postResolution( ... )
 *    preResponse( ... )
 *    postResponse( ... )
 *
 * Induction checks for the existence of these methods dynamically at runtime.
 *
 * Object preResolution( ... );
 *    This interceptor method is invoked PRIOR to attempting to resolve the target of the
 *    http request using the controller and view resolvers.
 *
 *    For params available for injection please see:
 *       http://www.inductionframework.org/param-injection-1-reference.html#InterceptorMETHODScommonlyusedparametertypes
 *
 *    Meaning of return value:
 *       If false is returned then no further processing is done on this request, this means no further
 *       interceptors are executed and no view or controller will be executed. If a value other than false is
 *       returned or no value is returned the execution continues to the next interceptor in the chain (if any).
 *
 * Object postResolution( ... )
 *    This interceptor method is invoked AFTER attempting to resolve the target of the
 *    http request using the controller and view resolvers.
 *
 *    For params available for injection please see:
 *       http://www.inductionframework.org/param-injection-1-reference.html#InterceptorMETHODScommonlyusedparametertypes
 *
 *    Meaning of return value:
 *       If false is returned then no further processing is done on this request, this means no further
 *       interceptors are executed and no view or controller will be executed. If a value other than false is
 *       returned or no value is returned the execution continues to the next interceptor in the chain (if any).
 *
 * Object preResponse( ... )
 *    This interceptor method called as follows:
 *       -  if the request resolved to a controller then this interceptor method is called
 *          AFTER the controller is executed but BEFORE the view or redirect object returned
 *          by the controller is processed.
 *       -  if the request resolved to a view then this interceptor method is called
 *          BEFORE the view is processed.
 *
 *       Note: even though the interceptor is called <code>preResponse</code>, it is possible
 *       that the controller may have written to the response directly using a response object.
 *       But the name is consistent with the fact that this method called before any response
 *       that Induction sends to the client by executing a view (returned by a controller or
 *       view resolver) or a redirect object (returned by a controller).
 *
 *    For params available for injection please see:
 *       http://www.inductionframework.org/param-injection-1-reference.html#InterceptorMETHODScommonlyusedparametertypes
 *
 *    Meaning of return value:
 *       If false is returned then no further processing is done on this request, this means no further
 *       interceptors are executed and the view or controller response value will not be processed. If a value
 *       other than false is returned or no value is returned the execution continues to the next interceptor
 *       in the chain (if any).
 *
 * Object postResponse( ... )
 *    This interceptor method is invoked AFTER processing a view or redirect object.
 *
 *    For params available for injection please see:
 *       http://www.inductionframework.org/param-injection-1-reference.html#InterceptorMETHODScommonlyusedparametertypes
 *
 *    Meaning of return value:
 *       If false is returned then no further processing is done on this request, this means no further
 *       interceptors are executed. If a value other than fasle is returned or no value is returned the execution
 *       continues to the next interceptor in the chain (if any).
 *
 * @author Adinath Raveendra Raj
 * @created Aug 30, 2009
 */
public interface RequestInterceptor
{
}