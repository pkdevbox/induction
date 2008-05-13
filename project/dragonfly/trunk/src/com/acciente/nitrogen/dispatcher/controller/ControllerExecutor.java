package com.acciente.nitrogen.dispatcher.controller;

import com.acciente.nitrogen.controller.Controller;
import com.acciente.nitrogen.dispatcher.ParamResolver;
import com.acciente.nitrogen.dispatcher.ParamResolverException;
import com.acciente.nitrogen.resolver.ControllerResolver;
import com.acciente.nitrogen.util.ConstructorNotFoundException;
import com.acciente.nitrogen.util.MethodNotFoundException;
import com.acciente.nitrogen.util.ReflectUtils;

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
      Controller  oController;
      Method      oControllerMethod;

      // get a controller instance from the pool
      oController = getController( oResolution, false );

      // use performance enhanced reflection to determine the methods in the controller with the specified name
      oControllerMethod = getControllerMethod( oResolution, oController );

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
      Object   oControllerReturnValue = null;

      for ( int i = 0; i < 2; i++ )
      {
         try
         {
            // todo: Problem  : currently if any of the model classes reload above this invoke fails
            // todo: Solution : if the param resolver reloaded any models, then reload the controller
            oControllerReturnValue = oControllerMethod.invoke( oController, aoParameterValues );
            break;
         }
         catch ( IllegalAccessException e )
         {
            throw new ControllerExecutorException( "invoke: method access error", e );
         }
         catch ( InvocationTargetException e )
         {
            throw new ControllerExecutorException( "invoke: method threw exception", e );
         }
         catch ( IllegalArgumentException e )
         {
            System.out.println( "i=" + i );

            if ( i == 0 )
            {
               // try reloading the controller
               // get a controller instance from the pool
               oController = getController( oResolution, true );

               // use performance enhanced reflection to determine the methods in the controller with the specified name
               oControllerMethod = getControllerMethod( oResolution, oController );
            }
            else
            {
               throw new ControllerExecutorException( "invoke: argument exception", e );
            }
         }
      }

      return oControllerReturnValue;
   }

   private Method getControllerMethod( ControllerResolver.Resolution oResolution, Controller oController )
      throws ControllerExecutorException
   {
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
      return oControllerMethod;
   }

   private Controller getController( ControllerResolver.Resolution oResolution, boolean bForceReload )
      throws ControllerExecutorException
   {
      Controller oController;
      try
      {
         oController = _oControllerPool.getController( oResolution.getClassName(), bForceReload );
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
      return oController;
   }
}

// EOF