package com.acciente.dragonfly.dispatcher;

import com.acciente.dragonfly.controller.Form;
import com.acciente.dragonfly.controller.HTMLForm;
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

      if ( oParamClass.isAssignableFrom( HttpServletRequest.class ) )
      {
         oParamValue = oRequest;
      }
      else if ( oParamClass.isAssignableFrom( HttpServletResponse.class ) )
      {
         oParamValue = oResponse;
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