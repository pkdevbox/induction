package com.acciente.dragonfly.dispatcher;

import com.acciente.dragonfly.controller.Form;
import com.acciente.dragonfly.controller.HTMLForm;
import com.acciente.dragonfly.controller.HttpRequest;
import com.acciente.dragonfly.controller.HttpResponse;
import com.acciente.dragonfly.controller.Request;
import com.acciente.dragonfly.controller.Response;
import com.acciente.dragonfly.model.ModelLifeCycleManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class handles the resolution of parameter values used for injection into controller methods.
 *
 * Log
 * Mar 16, 2008 APR  -  created
 */
public class ParamValueResolver
{
   private ModelLifeCycleManager _oModelLifeCycleManager;

   public ParamValueResolver( ModelLifeCycleManager oModelLifeCycleManager )
   {
      _oModelLifeCycleManager = oModelLifeCycleManager;
   }

   public Object getParameterValue( Class oParamClass, HttpServletRequest oRequest, HttpServletResponse oResponse )
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
      else
      {
         // check to see if this is a model class
         oParamValue = _oModelLifeCycleManager.getModel( oParamClass, oRequest );
      }

      return oParamValue;
   }
}

// EOF