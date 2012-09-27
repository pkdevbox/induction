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
package com.acciente.induction.dispatcher.view;

import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.resolver.ViewResolver;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.ObjectFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * ViewFactory
 *
 * @author Adinath Raveendra Raj
 * @created Mar 30, 2009
 */
public class ViewFactory
{
   private  ClassLoader                   _oClassLoader;
   private  ViewParameterProviderFactory  _oViewParameterProviderFactory;

   public ViewFactory( ClassLoader oClassLoader, ViewParameterProviderFactory oViewParameterProviderFactory )
   {
      _oClassLoader                    = oClassLoader;
      _oViewParameterProviderFactory   = oViewParameterProviderFactory;
   }

   public Object getView( String                   sViewClassName,
                          HttpServletRequest       oRequest,
                          HttpServletResponse      oResponse,
                          ViewResolver.Resolution  oResolution )
      throws InvocationTargetException, ConstructorNotFoundException, ParameterProviderException, IllegalAccessException, InstantiationException, ClassNotFoundException
   {
      Class oViewClass = _oClassLoader.loadClass( sViewClassName );

      return ObjectFactory.createObject( oViewClass,
                                         null,
                                         _oViewParameterProviderFactory
                                            .getParameterProvider( oRequest, oResponse, oResolution ) );
   }
}
