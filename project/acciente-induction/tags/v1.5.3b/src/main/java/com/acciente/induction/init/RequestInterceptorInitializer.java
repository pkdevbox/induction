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
package com.acciente.induction.init;

import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.dispatcher.model.ModelPool;
import com.acciente.induction.init.config.Config;
import com.acciente.induction.interceptor.RequestInterceptor;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.ObjectFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletConfig;
import java.lang.reflect.InvocationTargetException;

/**
 * RequestInterceptorInitializer
 *
 * @author Adinath Raveendra Raj
 * @created Sep 7, 2009
 */
public class RequestInterceptorInitializer
{
   public static RequestInterceptor[] getRequestInterceptor(   Config.RequestInterceptors oRequestInterceptorsConfig,
                                                               ModelPool                  oModelPool,
                                                               ClassLoader                oClassLoader,
                                                               ServletConfig              oServletConfig )
      throws ClassNotFoundException, InvocationTargetException, ConstructorNotFoundException, ParameterProviderException, IllegalAccessException, InstantiationException
   {
      RequestInterceptor[]    oRequestInterceptorArray;
      String                  sRequestInterceptorClassName;
      Log oLog;

      oLog = LogFactory.getLog( RequestInterceptorInitializer.class );

      oRequestInterceptorArray = new RequestInterceptor[ oRequestInterceptorsConfig.getRequestInterceptorList().size() ];

      for ( int i = 0; i < oRequestInterceptorsConfig.getRequestInterceptorList().size(); i++ )
      {
         Config.RequestInterceptors.RequestInterceptor   oRequestInterceptorConfig;

         oRequestInterceptorConfig = ( Config.RequestInterceptors.RequestInterceptor )
                                       oRequestInterceptorsConfig.getRequestInterceptorList().get( i );

         sRequestInterceptorClassName = oRequestInterceptorConfig.getClassName();

         oLog.info( "loading user-defined request interceptor: " + sRequestInterceptorClassName );

         Class    oRequestInterceptorClass  = oClassLoader.loadClass( sRequestInterceptorClassName );

         // attempt to find and call the single public constructor
         oRequestInterceptorArray[ i ]
            =  ( RequestInterceptor )
               ObjectFactory.createObject( oRequestInterceptorClass,
                                           new Object[]{ oServletConfig,
                                                         oRequestInterceptorConfig,
                                                         oClassLoader },
                                           new InitializerParameterProvider( oModelPool, "request-interceptor-init" ) );
      }

      return oRequestInterceptorArray;
   }
}
