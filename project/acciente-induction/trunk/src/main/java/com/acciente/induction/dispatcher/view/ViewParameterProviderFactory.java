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
package com.acciente.induction.dispatcher.view;

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
import com.acciente.induction.resolver.RedirectResolver;
import com.acciente.induction.resolver.ViewResolver;
import com.acciente.induction.template.TemplatingEngine;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.MethodNotFoundException;
import com.acciente.induction.view.Image;
import com.acciente.induction.view.ImageStream;
import com.acciente.induction.view.Template;
import com.acciente.induction.view.Text;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * Internal.
 *
 * ViewParameterProviderFactory.
 *
 * This class is a ViewParameterProvider factory that returns a ViewParameterProvider object to provide
 * parameter values for view constructors.
 */
public class ViewParameterProviderFactory
{
   private ModelPool                _oModelPool;
   private Config.FileUpload        _oFileUploadConfig;
   private TemplatingEngine         _oTemplatingEngine;
   private RedirectResolverExecutor _oRedirectResolverExecutor;
   private ViewFactory              _oViewFactory;
   private ClassLoader              _oClassLoader;

   public ViewParameterProviderFactory( ModelPool                oModelPool,
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

   public void setViewFactory( ViewFactory oViewFactory )
   {
      _oViewFactory = oViewFactory;
   }

   public ViewParameterProvider getParameterProvider( HttpServletRequest      oRequest,
                                                      HttpServletResponse     oResponse,
                                                      ViewResolver.Resolution oResolution )
   {
      return new ViewParameterProvider( oRequest, oResponse, oResolution );
   }

   private class ViewParameterProvider implements ParameterProvider
   {
      private  HttpServletRequest            _oRequest;
      private  HttpServletResponse           _oResponse;
      private  ViewResolver.Resolution       _oResolution;

      private ViewParameterProvider( HttpServletRequest        oRequest,
                                     HttpServletResponse       oResponse,
                                     ViewResolver.Resolution   oResolution )
      {
         _oRequest         = oRequest;
         _oResponse        = oResponse;
         _oResolution      = oResolution;
      }

      public Object getParameter( Class oParamClass ) throws ParameterProviderException
      {
         final String sMessagePrefix = "view-parameter-provider: error resolving value for type: ";

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
            else if ( oParamClass.isAssignableFrom( ViewResolver.Resolution.class ) )
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
            else if ( Template.class.isAssignableFrom( oParamClass )
                        || Text.class.isAssignableFrom( oParamClass )
                        || Image.class.isAssignableFrom( oParamClass )
                        || ImageStream.class.isAssignableFrom( oParamClass ) )
            {
               oParamValue = _oViewFactory.getView( oParamClass.getName(), _oRequest, _oResponse, null );
            }
            else
            {
               // check to see if this is a model class
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
