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
package com.acciente.induction.dispatcher.resolver;

import com.acciente.commons.reflect.Invoker;
import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.controller.Redirect;
import com.acciente.induction.resolver.RedirectResolver;
import com.acciente.induction.util.MethodNotFoundException;
import com.acciente.induction.util.ReflectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Induction Internal class.
 *
 * @author Adinath Raveendra Raj
 * @created Jul 5, 2010
 */
public class RedirectResolverExecutor
{
   private RedirectResolver                         _oRedirectResolver;
   private RedirectResolverParameterProviderFactory _oRedirectResolverParameterProviderFactory;

   private Method                                   _oMethod_resolveRedirect;

   public RedirectResolverExecutor( RedirectResolver                         oRedirectResolver,
                                    RedirectResolverParameterProviderFactory oRedirectResolverParameterProviderFactory )
      throws MethodNotFoundException
   {
      _oRedirectResolver                         = oRedirectResolver;
      _oRedirectResolverParameterProviderFactory = oRedirectResolverParameterProviderFactory;
      _oMethod_resolveRedirect                   = ReflectUtils.getSingletonMethod( _oRedirectResolver.getClass(),
                                                                                    "resolveRedirect" );
   }

   public String resolveRedirect( HttpServletRequest oRequest, Redirect oRedirect  )
   {
      Object oReturnValue;

      try
      {
         oReturnValue = Invoker.invoke ( _oMethod_resolveRedirect,
                                         _oRedirectResolver,
                                         null,
                                         _oRedirectResolverParameterProviderFactory.getParameterProvider( oRequest,
                                                                                                          oRedirect ) );
      }
      catch ( InvocationTargetException e )
      {
         throw new RuntimeException( e );
      }
      catch ( IllegalAccessException e )
      {
         throw new RuntimeException( e );
      }
      catch ( ParameterProviderException e )
      {
         throw new RuntimeException( e );
      }

      if ( ! ( oReturnValue instanceof String ) )
      {
         throw new IllegalArgumentException( "Redirect resolver: resolveRequest(...) did not return a string, returned type was: "
                                             + oReturnValue.getClass() );
      }

      return ( String ) oReturnValue;
   }
}
