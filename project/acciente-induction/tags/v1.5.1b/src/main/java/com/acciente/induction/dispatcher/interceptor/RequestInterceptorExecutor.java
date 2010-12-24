/*
 * Copyright 2008-2010 Acciente, LLC
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
package com.acciente.induction.dispatcher.interceptor;

import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.controller.Redirect;
import com.acciente.induction.interceptor.RequestInterceptor;
import com.acciente.induction.resolver.ControllerResolver;
import com.acciente.induction.resolver.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * RequestInterceptor
 *
 * @author Adinath Raveendra Raj
 * @created Sep 8, 2009
 */
public class RequestInterceptorExecutor
{
   private  RequestInterceptorFacade[]    _aoRequestInterceptorFacadeArray;

   public RequestInterceptorExecutor( RequestInterceptor[]                       aoRequestInterceptorArray,
                                      RequestInterceptorParameterProviderFactory oRequestInterceptorParameterProviderFactory )
   {
      _aoRequestInterceptorFacadeArray = new RequestInterceptorFacade[ aoRequestInterceptorArray.length ];

      for ( int i = 0; i < aoRequestInterceptorArray.length; i++ )
      {
         _aoRequestInterceptorFacadeArray[ i ] = new RequestInterceptorFacade( aoRequestInterceptorArray[ i ],
                                                                               oRequestInterceptorParameterProviderFactory );
      }

   }

   public Object preResolution( HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws InvocationTargetException, ParameterProviderException, IllegalAccessException
   {
      Object   oReturnValue = null;

      for ( int i = 0; i < _aoRequestInterceptorFacadeArray.length; i++ )
      {
         oReturnValue = _aoRequestInterceptorFacadeArray[ i ].preResolution( oRequest, oResponse );

         if ( isStopAfterThisInterceptor( oReturnValue ) )
         {
            return oReturnValue;
         }
      }

      return oReturnValue;
   }

   public Object postResolution( HttpServletRequest oRequest, HttpServletResponse oResponse, ControllerResolver.Resolution oControllerResolution, ViewResolver.Resolution oViewResolution ) throws InvocationTargetException, ParameterProviderException, IllegalAccessException
   {
      Object   oReturnValue = null;

      for ( int i = 0; i < _aoRequestInterceptorFacadeArray.length; i++ )
      {
         oReturnValue = _aoRequestInterceptorFacadeArray[ i ].postResolution( oRequest,
                                                                              oResponse,
                                                                              oControllerResolution,
                                                                              oViewResolution );
         if ( isStopAfterThisInterceptor( oReturnValue ) )
         {
            return oReturnValue;
         }
      }

      return oReturnValue;
   }

   public Object preResponse( HttpServletRequest oRequest, HttpServletResponse oResponse, ControllerResolver.Resolution oControllerResolution, ViewResolver.Resolution oViewResolution ) throws InvocationTargetException, ParameterProviderException, IllegalAccessException
   {
      Object   oReturnValue = null;

      for ( int i = 0; i < _aoRequestInterceptorFacadeArray.length; i++ )
      {
         oReturnValue = _aoRequestInterceptorFacadeArray[ i ].preResponse( oRequest,
                                                                           oResponse,
                                                                           oControllerResolution,
                                                                           oViewResolution );
         if ( isStopAfterThisInterceptor( oReturnValue ) )
         {
            return oReturnValue;
         }
      }

      return oReturnValue;
   }

   public Object postResponse( HttpServletRequest oRequest, HttpServletResponse oResponse, ControllerResolver.Resolution oControllerResolution, ViewResolver.Resolution oViewResolution ) throws InvocationTargetException, ParameterProviderException, IllegalAccessException
   {
      Object   oReturnValue = null;

      for ( int i = 0; i < _aoRequestInterceptorFacadeArray.length; i++ )
      {
         oReturnValue = _aoRequestInterceptorFacadeArray[ i ].postResponse( oRequest,
                                                                            oResponse,
                                                                            oControllerResolution,
                                                                            oViewResolution );
         if ( isStopAfterThisInterceptor( oReturnValue ) )
         {
            return oReturnValue;
         }
      }

      return oReturnValue;
   }

   private boolean isStopAfterThisInterceptor( Object oReturnValue )
   {
      if ( oReturnValue != null )
      {
         if ( oReturnValue instanceof Boolean )
         {
            return ( ( ( Boolean ) oReturnValue ).booleanValue() );
         }
         else if ( oReturnValue instanceof Redirect )
         {
            return true;
         }
      }

      return false;
   }
}