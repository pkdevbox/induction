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
package com.acciente.induction.dispatcher.interceptor;

import com.acciente.commons.reflect.Invoker;
import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.interceptor.RequestInterceptor;
import com.acciente.induction.resolver.ControllerResolver;
import com.acciente.induction.resolver.ViewResolver;
import com.acciente.induction.util.MethodNotFoundException;
import com.acciente.induction.util.ReflectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * RequestInterceptorFacade
 *
 * @author Adinath Raveendra Raj
 * @created Oct 20, 2009
 */
public class RequestInterceptorFacade
{
   private RequestInterceptor                            _oRequestInterceptor;
   private RequestInterceptorParameterProviderFactory    _oRequestInterceptorParameterProviderFactory;

   // we cache the method here to minimize delays, even though our reflection utility already caches
   private Method             _oMethod_preResolution;
   private Method             _oMethod_postResolution;
   private Method             _oMethod_preResponse;
   private Method             _oMethod_postResponse;

   public RequestInterceptorFacade( RequestInterceptor                           oRequestInterceptor,
                                    RequestInterceptorParameterProviderFactory   oRequestInterceptorParameterProviderFactory )
   {
      _oRequestInterceptor                         = oRequestInterceptor;
      _oRequestInterceptorParameterProviderFactory = oRequestInterceptorParameterProviderFactory;

      try
      {
         _oMethod_preResolution  = ReflectUtils.getSingletonMethod( _oRequestInterceptor.getClass(), "preResolution" );
      }
      catch ( MethodNotFoundException e )
      {
         // this is ok, since implementing any of the handlers is optional, of course at
         // least one handler should be defined for the interceptor to be of any use
      }

      try
      {
         _oMethod_postResolution = ReflectUtils.getSingletonMethod( _oRequestInterceptor.getClass(), "postResolution" );
      }
      catch ( MethodNotFoundException e )
      {
         // this is ok, since implementing any of the handlers is optional, of course at
         // least one handler should be defined for the interceptor to be of any use
      }

      try
      {
         _oMethod_preResponse    = ReflectUtils.getSingletonMethod( _oRequestInterceptor.getClass(), "preResponse" );
      }
      catch ( MethodNotFoundException e )
      {
         // this is ok, since implementing any of the handlers is optional, of course at
         // least one handler should be defined for the interceptor to be of any use
      }

      try
      {
         _oMethod_postResponse   = ReflectUtils.getSingletonMethod( _oRequestInterceptor.getClass(), "postResponse" );
      }
      catch ( MethodNotFoundException e )
      {
         // this is ok, since implementing any of the handlers is optional, of course at
         // least one handler should be defined for the interceptor to be of any use
      }
   }

   public Object preResolution( HttpServletRequest    oRequest,
                                HttpServletResponse   oResponse )
      throws InvocationTargetException, ParameterProviderException, IllegalAccessException
   {
      if ( _oMethod_preResolution != null )
      {
         return Invoker.invoke( _oMethod_preResolution,
                                _oRequestInterceptor,
                                null,
                                _oRequestInterceptorParameterProviderFactory.getParameterProvider( oRequest,
                                                                                                   oResponse,
                                                                                                   null,
                                                                                                   null ) );
      }

      return null;
   }

   public Object postResolution( HttpServletRequest            oRequest,
                                 HttpServletResponse           oResponse,
                                 ControllerResolver.Resolution oControllerResolution,
                                 ViewResolver.Resolution       oViewResolution )
      throws InvocationTargetException, ParameterProviderException, IllegalAccessException
   {
      if ( _oMethod_postResolution != null )
      {
         return Invoker.invoke( _oMethod_postResolution,
                                _oRequestInterceptor,
                                null,
                                _oRequestInterceptorParameterProviderFactory.getParameterProvider( oRequest,
                                                                                                   oResponse,
                                                                                                   oControllerResolution,
                                                                                                   oViewResolution ) );
      }

      return null;
   }

   public Object preResponse( HttpServletRequest            oRequest,
                              HttpServletResponse           oResponse,
                              ControllerResolver.Resolution oControllerResolution,
                              ViewResolver.Resolution       oViewResolution )
      throws InvocationTargetException, ParameterProviderException, IllegalAccessException
   {
      if ( _oMethod_preResponse != null )
      {
         return Invoker.invoke( _oMethod_preResponse,
                                _oRequestInterceptor,
                                null,
                                _oRequestInterceptorParameterProviderFactory.getParameterProvider( oRequest,
                                                                                                   oResponse,
                                                                                                   oControllerResolution,
                                                                                                   oViewResolution ) );
      }

      return null;
   }

   public Object postResponse( HttpServletRequest              oRequest,
                               HttpServletResponse             oResponse,
                               ControllerResolver.Resolution   oControllerResolution,
                               ViewResolver.Resolution         oViewResolution )
      throws InvocationTargetException, ParameterProviderException, IllegalAccessException
   {
      if ( _oMethod_postResponse != null )
      {
         return Invoker.invoke( _oMethod_postResponse,
                                _oRequestInterceptor,
                                null,
                                _oRequestInterceptorParameterProviderFactory.getParameterProvider( oRequest,
                                                                                                   oResponse,
                                                                                                   oControllerResolution,
                                                                                                   oViewResolution ) );
      }

      return null;
   }
}
