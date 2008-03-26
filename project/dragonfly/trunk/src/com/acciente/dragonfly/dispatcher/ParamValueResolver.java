package com.acciente.dragonfly.dispatcher;

import com.acciente.dragonfly.controller.Form;
import com.acciente.dragonfly.controller.HTMLForm;
import com.acciente.dragonfly.controller.HttpRequest;
import com.acciente.dragonfly.controller.HttpResponse;
import com.acciente.dragonfly.controller.Request;
import com.acciente.dragonfly.controller.Response;
import com.acciente.dragonfly.dispatcher.model.ModelPool;
import com.acciente.dragonfly.util.ConstructorNotFoundException;
import com.acciente.dragonfly.util.MethodNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * This class handles the resolution of parameter values used for injection into controller methods.
 *
 * Log
 * Mar 16, 2008 APR  -  created
 */
public class ParamValueResolver
{
   private ModelPool _oModelPool;

   public ParamValueResolver( ModelPool oModelPool )
   {
      _oModelPool = oModelPool;
   }

   public Object getParameterValue( Class oParamClass, HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws ClassNotFoundException, ConstructorNotFoundException, MethodNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException
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
         oParamValue = new HTMLForm( oRequest );
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

      return oParamValue;
   }
}

// EOF