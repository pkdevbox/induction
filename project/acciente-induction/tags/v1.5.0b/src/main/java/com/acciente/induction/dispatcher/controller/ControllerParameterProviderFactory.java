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
package com.acciente.induction.dispatcher.controller;

import com.acciente.commons.reflect.ParameterProvider;
import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.controller.Form;
import com.acciente.induction.controller.HTMLForm;
import com.acciente.induction.controller.HttpRequest;
import com.acciente.induction.controller.HttpResponse;
import com.acciente.induction.controller.Request;
import com.acciente.induction.controller.Response;
import com.acciente.induction.dispatcher.model.ModelPool;
import com.acciente.induction.dispatcher.resolver.RedirectResolverExecutor;
import com.acciente.induction.dispatcher.resolver.URLResolver;
import com.acciente.induction.init.config.Config;
import com.acciente.induction.resolver.ControllerResolver;
import com.acciente.induction.resolver.RedirectResolver;
import com.acciente.induction.template.TemplatingEngine;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.MethodNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * Internal.
 * This class handles the resolution of parameter values used for injection into controller methods.
 *
 * @created Mar 16, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ControllerParameterProviderFactory
{
   private ModelPool                _oModelPool;
   private Config.FileUpload        _oFileUploadConfig;
   private TemplatingEngine         _oTemplatingEngine;
   private RedirectResolverExecutor _oRedirectResolverExecutor;
   private ClassLoader              _oClassLoader;

   public ControllerParameterProviderFactory( ModelPool                oModelPool,
                                              Config.FileUpload        oFileUploadConfig,
                                              TemplatingEngine         oTemplatingEngine,
                                              RedirectResolverExecutor oRedirectResolverExecutor,
                                              ClassLoader              oClassLoader )
   {
      _oModelPool                = oModelPool;
      _oFileUploadConfig         = oFileUploadConfig;
      _oTemplatingEngine         = oTemplatingEngine;
      _oRedirectResolverExecutor = oRedirectResolverExecutor;
      _oClassLoader              = oClassLoader;
   }

   public ControllerParameterProvider getParameterProvider( HttpServletRequest             oRequest,
                                                            HttpServletResponse            oResponse,
                                                            ControllerResolver.Resolution  oResolution )
   {
      return new ControllerParameterProvider( oRequest, oResponse, oResolution );
   }

   private class ControllerParameterProvider implements ParameterProvider
   {
      private  HttpServletRequest               _oRequest;
      private  HttpServletResponse              _oResponse;
      private  ControllerResolver.Resolution    _oResolution;

      private ControllerParameterProvider( HttpServletRequest              oRequest,
                                           HttpServletResponse             oResponse,
                                           ControllerResolver.Resolution   oResolution )
      {
         _oRequest         = oRequest;
         _oResponse        = oResponse;
         _oResolution      = oResolution;
      }

      public Object getParameter( Class oParamClass ) throws ParameterProviderException
      {
         final String sMessagePrefix = "controller-parameter-provider: error resolving value for type: ";

         try
         {
            Object   oParamValue = null;

            if ( oParamClass.isAssignableFrom( Request.class ) )
            {
               oParamValue = new HttpRequest( _oRequest );
            }
            else if ( oParamClass.isAssignableFrom( Response.class ) )
            {
               oParamValue = new HttpResponse( _oResponse );
            }
            else if ( oParamClass.isAssignableFrom( Form.class ) )
            {
               oParamValue = _oModelPool.getSystemModel( oParamClass, _oRequest );
            }
            else if ( oParamClass.isAssignableFrom( HttpServletRequest.class ) )
            {
               oParamValue = _oRequest;
            }
            else if ( oParamClass.isAssignableFrom( HttpServletResponse.class ) )
            {
               oParamValue = _oResponse;
            }
            else if ( oParamClass.isAssignableFrom( ControllerResolver.Resolution.class ) )
            {
               oParamValue = _oResolution;
            }
            else if ( oParamClass.isAssignableFrom( TemplatingEngine.class ) )
            {
               oParamValue = _oTemplatingEngine;
            }
            else if ( oParamClass.isAssignableFrom( URLResolver.class ) )
            {
               oParamValue = _oModelPool.getSystemModel( oParamClass, _oRequest );
            }
            else if ( oParamClass.isAssignableFrom( ClassLoader.class ) )
            {
               oParamValue = _oClassLoader;
            }
            else
            {
               // check to see if this is a user model class
               oParamValue = _oModelPool.getModel( oParamClass, _oRequest );
            }

            if ( oParamValue == null )
            {
               throw ( new ParameterProviderException( sMessagePrefix + oParamClass ) );
            }

            return oParamValue;
         }
         catch ( MethodNotFoundException e )
         {  throw new ParameterProviderException( sMessagePrefix + oParamClass, e );     }
         catch ( InvocationTargetException e )
         {  throw new ParameterProviderException( sMessagePrefix + oParamClass, e );     }
         catch ( ClassNotFoundException e )
         {  throw new ParameterProviderException( sMessagePrefix + oParamClass, e );     }
         catch ( ConstructorNotFoundException e )
         {  throw new ParameterProviderException( sMessagePrefix + oParamClass, e );     }
         catch ( IllegalAccessException e )
         {  throw new ParameterProviderException( sMessagePrefix + oParamClass, e );     }
         catch ( InstantiationException e )
         {  throw new ParameterProviderException( sMessagePrefix + oParamClass, e );     }
      }
   }
}

// EOF