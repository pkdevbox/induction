package com.acciente.dragonfly.dispatcher.controller;

import com.acciente.dragonfly.controller.Controller;
import com.acciente.dragonfly.dispatcher.ParamResolver;
import com.acciente.dragonfly.dispatcher.ParamResolverException;
import com.acciente.dragonfly.resolver.ControllerResolver;
import com.acciente.dragonfly.util.ConstructorNotFoundException;
import com.acciente.dragonfly.util.MethodNotFoundException;
import com.acciente.dragonfly.util.ReflectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class execute a specified method on a specified controller instance
 *
 * Log
 * Apr 17, 2008 APR  -  created
 */
public class ControllerExecutor
{
   private  ControllerPool          _oControllerPool;
   private  ParamResolver           _oParamResolver;

   /**
    * Creates a new controller executor instance
    * @param oControllerPool a controller pool
    * @param oParamResolver a parameter value resolver
    */
   public ControllerExecutor( ControllerPool oControllerPool, ParamResolver oParamResolver )
   {
      _oControllerPool     = oControllerPool;
      _oParamResolver      = oParamResolver;
   }

   /**
    * Executes the controller specified in the oResolution parameter
    * @param oResolution specifies the controller to execute
    * @param oRequest passed thru to the controller if the controller so requests
    * @param oResponse passed thru to the controller if the controller so requests
    * @throws ControllerExecutorException if an error was encountered during controller loading, executing,
    * or post post processing, the exception's cause will always contain the actual underlying cause of the error
    * @return the value returned by the controller
    */
   public Object execute( ControllerResolver.Resolution oResolution, HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws ControllerExecutorException
   {
      Controller oController;
      try
      {
         oController = _oControllerPool.getController( oResolution.getClassName() );
      }
      catch ( ClassNotFoundException e )
      {
         throw new ControllerExecutorException( "load: unable to load definition", e );
      }
      catch ( ConstructorNotFoundException e )
      {
         throw new ControllerExecutorException( "load: unable to find constructor", e );
      }
      catch ( InstantiationException e )
      {
         throw new ControllerExecutorException( "load: instantiate error", e );
      }
      catch ( InvocationTargetException e )
      {
         throw new ControllerExecutorException( "load: target exception", e );
      }
      catch ( IllegalAccessException e )
      {
         throw new ControllerExecutorException( "load: access exception", e );
      }

      // use performance enhanced reflection to determine the methods in the controller with the specified name
      Method oControllerMethod;
      try
      {
         oControllerMethod = ReflectUtils.getSingletonMethod( oController.getClass(),
                                                              oResolution.getMethodName(),
                                                              oResolution.isIgnoreMethodNameCase() );
      }
      catch ( MethodNotFoundException e )
      {
         throw new ControllerExecutorException( "load: method not found", e );
      }

      // ok, we have a controller, now compute values for its formal parameter list
      Class[]  aoParameterTypes  = oControllerMethod.getParameterTypes();
      Object[] aoParameterValues = new Object[ aoParameterTypes.length ];

      for ( int i = 0; i < aoParameterTypes.length; i++ )
      {
         Class oParameterType = aoParameterTypes[ i ];

         try
         {
            aoParameterValues[ i ] = _oParamResolver.getParameterValue( oParameterType, oRequest, oResponse );
         }
         catch ( ClassNotFoundException e )
         {
            throw new ControllerExecutorException( "model-load:  unable to load model definition", e );
         }
         catch ( ConstructorNotFoundException e )
         {
            throw new ControllerExecutorException( "model-load:  constructor not found", e );
         }
         catch ( MethodNotFoundException e )
         {
            throw new ControllerExecutorException( "model-load:  method not found", e );
         }
         catch ( InvocationTargetException e )
         {
            throw new ControllerExecutorException( "model-load:  target exception", e );
         }
         catch ( IllegalAccessException e )
         {
            throw new ControllerExecutorException( "model-load:  access exception", e );
         }
         catch ( InstantiationException e )
         {
            throw new ControllerExecutorException( "model-load:  instantiate exception", e );
         }
         catch ( ParamResolverException e )
         {
            throw new ControllerExecutorException( "model-load:  parameter resolution exception", e );
         }
      }

      // finally call the controller method!
      Object   oControllerReturnValue;
      try
      {
         // todo: Problem  : currently if any of the model classes reload above this invoke fails
         // todo: Solution : if the param resolver reloaded any models, then reload the controller
         oControllerReturnValue = oControllerMethod.invoke( oController, aoParameterValues );
      }
      catch ( IllegalAccessException e )
      {
         throw new ControllerExecutorException( "invoke: method access error", e );
      }
      catch ( InvocationTargetException e )
      {
         throw new ControllerExecutorException( "invoke: method threw exception", e );
      }

      return oControllerReturnValue;
   }
}

// EOF