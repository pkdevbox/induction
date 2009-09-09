package com.acciente.induction.interceptor;

import com.acciente.induction.resolver.ControllerResolver;
import com.acciente.induction.resolver.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * RequestInterceptor
 *
 * @author Adinath Raveendra Raj
 * @created Aug 30, 2009
 */
public interface RequestInterceptor
{
   /**
    * This interceptor method is invoked PRIOR to attempting to resolve the target of the
    * http request using the controller and view resolvers.
    *
    * @param oRequest the HTTP request object provided by the servlet container
    * @param oResponse the HTTP response object provided by the servlet container
    * @return if true is returned the next interceptor in the chain gets control, if false is returned
    * the no interceptors are executed after this interceptor
    */
   boolean  preResolution( HttpServletRequest oRequest, HttpServletResponse oResponse );

   /**
    * This interceptor method is invoked AFTER attempting to resolve the target of the
    * http request using the controller and view resolvers.
    *
    * @param oRequest the HTTP request object provided by the servlet container
    * @param oResponse the HTTP response object provided by the servlet container
    * @param oControllerResolution the controller resolution to which the request was resolved,
    * or <code>null</code> if the request did not resolve to a controller.
    * @param oViewResolution the view resolution to which the request was resolved, or <code>null</code>
    * if the request did not resolve to a view.
    * @return if true is returned the next interceptor in the chain gets control, if false is returned
    * the no interceptors are executed after this interceptor
    */
   boolean  postResolution(   HttpServletRequest               oRequest,
                              HttpServletResponse              oResponse,
                              ControllerResolver.Resolution    oControllerResolution,
                              ViewResolver.Resolution          oViewResolution );

   /**
    * This interceptor method called as follows:
    * -  if the request resolved to a controller then this interceptor method is called
    *    AFTER the controller is executed but BEFORE the view or redirect object returned
    *    by the controller is processed.
    * -  if the request resolved to a view then this interceptor method is called
    *    BEFORE the view is processed.
    *
    * Note: even though the interceptor is called <code>preResponse</code>, it is possible
    * that the controller may have written to the response directly using a response object.
    * But the name is consistent with the fact that this method called before any response
    * that Induction sends to the client by executing a view (returned by a controller or
    * view resolver) or a redirect object (returned by a controller).
    *
    * @param oRequest the HTTP request object provided by the servlet container.
    * @param oResponse the HTTP response object provided by the servlet container.
    * @param oControllerResolution the controller resolution to which the request was resolved,
    * or <code>null</code> if the request did not resolve to a controller.
    * @param oViewResolution the view resolution to which the request was resolved, or <code>null</code>
    * if the request did not resolve to a view.
    * @return if true is returned the next interceptor in the chain gets control, if false is returned
    * the no interceptors are executed after this interceptor
    */
   boolean  preResponse(   HttpServletRequest               oRequest,
                           HttpServletResponse              oResponse,
                           ControllerResolver.Resolution    oControllerResolution,
                           ViewResolver.Resolution          oViewResolution );

   /**
    * This interceptor method is invoked AFTER processing a view or redirect object.
    *
    * @param oRequest the HTTP request object provided by the servlet container
    * @param oResponse the HTTP response object provided by the servlet container
    * @param oControllerResolution the controller resolution to which the request was resolved,
    * or <code>null</code> if the request did not resolve to a controller.
    * @param oViewResolution the view resolution to which the request was resolved, or <code>null</code>
    * if the request did not resolve to a view.
    * @return if true is returned the next interceptor in the chain gets control, if false is returned
    * the no interceptors are executed after this interceptor
    */
   boolean  postResponse(  HttpServletRequest               oRequest,
                           HttpServletResponse              oResponse,
                           ControllerResolver.Resolution    oControllerResolution,
                           ViewResolver.Resolution          oViewResolution );
}