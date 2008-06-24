/**
 *   Copyright 2008 Acciente, LLC
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
package com.acciente.induction.dispatcher;

import com.acciente.induction.controller.Form;
import com.acciente.induction.controller.HTMLForm;
import com.acciente.induction.controller.HttpRequest;
import com.acciente.induction.controller.HttpResponse;
import com.acciente.induction.controller.Request;
import com.acciente.induction.controller.Response;
import com.acciente.induction.dispatcher.model.ModelPool;
import com.acciente.induction.init.config.Config;
import com.acciente.induction.util.MethodNotFoundException;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.commons.reflect.ParameterProviderException;

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
public class ParamResolver
{
   private ModelPool          _oModelPool;
   private Config.FileUpload  _oFileUploadConfig;

   public ParamResolver( ModelPool oModelPool, Config.FileUpload oFileUploadConfig )
   {
      _oModelPool          = oModelPool;
      _oFileUploadConfig   = oFileUploadConfig;
   }

   public Object getParameterValue( Class oParamClass, HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws ParamResolverException, MethodNotFoundException, ClassNotFoundException, InvocationTargetException, ParameterProviderException, ConstructorNotFoundException, InstantiationException, IllegalAccessException
   {
      Object   oParamValue = null;

      if ( oParamClass.isAssignableFrom( Request.class ) )
      {
         oParamValue = new HttpRequest( oRequest );
      }
      else if ( oParamClass.isAssignableFrom( Response.class ) )
      {
         oParamValue = new HttpResponse( oResponse );
      }
      else if ( oParamClass.isAssignableFrom( Form.class ) )
      {
         // NOTE: since the HTMLForm is per-request no caching is needed, since parameters
         // are resolved before controller invocation, and become local variables in the
         // controller for the duration of the request
         oParamValue = new HTMLForm( oRequest, _oFileUploadConfig );
      }
      else if ( oParamClass.isAssignableFrom( HttpServletRequest.class ) )
      {
         oParamValue = oRequest;
      }
      else if ( oParamClass.isAssignableFrom( HttpServletResponse.class ) )
      {
         oParamValue = oResponse;
      }
      else
      {
         // check to see if this is a model class
         oParamValue = _oModelPool.getModel( oParamClass.getName(), oRequest );
      }

      if ( oParamValue == null )
      {
         throw ( new ParamResolverException( "Value resolution yielded no value for parameter type: " + oParamClass.getName() ) );
      }

      return oParamValue;
   }
}

// EOF