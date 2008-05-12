package com.acciente.nitrogen.dispatcher;

import com.acciente.nitrogen.controller.Form;
import com.acciente.nitrogen.controller.HTMLForm;
import com.acciente.nitrogen.controller.HttpRequest;
import com.acciente.nitrogen.controller.HttpResponse;
import com.acciente.nitrogen.controller.Request;
import com.acciente.nitrogen.controller.Response;
import com.acciente.nitrogen.dispatcher.model.ModelPool;
import com.acciente.nitrogen.init.config.Config;
import com.acciente.nitrogen.util.ConstructorNotFoundException;
import com.acciente.nitrogen.util.MethodNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * This class handles the resolution of parameter values used for injection into controller methods.
 *
 * Log
 * Mar 16, 2008 APR  -  created
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
      throws ClassNotFoundException, ConstructorNotFoundException, MethodNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, ParamResolverException
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
         oParamValue = _oModelPool.getModel( oParamClass, oRequest );
      }

      if ( oParamValue == null )
      {
         throw ( new ParamResolverException( "Value resolution yielded no value for paramter type: " + oParamClass.getName() ) );
      }

      return oParamValue;
   }
}

// EOF