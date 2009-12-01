/**
 *   Copyright 2009 Acciente, LLC
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
package com.acciente.induction.dispatcher.controller;

import com.acciente.commons.reflect.Invoker;
import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.controller.Controller;
import com.acciente.induction.resolver.ControllerResolver;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.MethodNotFoundException;
import com.acciente.induction.util.ReflectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Internal.
 * This class executes a specified method on a specified controller instance
 *
 * @created Apr 17, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ControllerExecutor
{
   private  ControllerPool                      _oControllerPool;
   private  ControllerParameterProviderFactory  _oControllerParameterProviderFactory;

   /**
    * Creates a new controller executor instance
    * @param oControllerPool a controller pool
    * @param oControllerParameterProviderFactory a parameter value resolver
    */
   public ControllerExecutor( ControllerPool                      oControllerPool,
                              ControllerParameterProviderFactory  oControllerParameterProviderFactory )
   {
      _oControllerPool                       = oControllerPool;
      _oControllerParameterProviderFactory   = oControllerParameterProviderFactory;
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
   public Object execute( ControllerResolver.Resolution  oResolution,
                          HttpServletRequest             oRequest,
                          HttpServletResponse            oResponse )
      throws ControllerExecutorException
   {
      Controller  oController;

      try
      {
         oController = _oControllerPool.getController( oResolution.getClassName() );
      }
      catch ( ClassNotFoundException e )
      {
         throw new ControllerExecutorException( "Controller " + oResolution.getClassName() + ", unable to load class definition", e );
      }
      catch ( ConstructorNotFoundException e )
      {
         throw new ControllerExecutorException( "Controller " + oResolution.getClassName() + ", unable to find constructor", e );
      }
      catch ( InstantiationException e )
      {
         throw new ControllerExecutorException( "Controller " + oResolution.getClassName() + ", instantiate error", e );
      }
      catch ( InvocationTargetException e )
      {
         throw new ControllerExecutorException( "Controller " + oResolution.getClassName() + ", target exception", e );
      }
      catch ( IllegalAccessException e )
      {
         throw new ControllerExecutorException( "Controller " + oResolution.getClassName() + ", access exception", e );
      }
      catch ( ParameterProviderException e )
      {
         throw new ControllerExecutorException( "Controller " + oResolution.getClassName() + ", parameter exception", e );
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
         throw new ControllerExecutorException( "Controller " + oResolution.getClassName()
                                                + ", method " + oResolution.getMethodName()
                                                + ", method not found", e );
      }

      // finally call the controller method!
      Object   oControllerReturnValue;
      try
      {
         oControllerReturnValue = Invoker.invoke( oControllerMethod,
                                                  oController,
                                                  null,
                                                  _oControllerParameterProviderFactory
                                                     .getParameterProvider( oRequest,
                                                                            oResponse,
                                                                            oResolution ) );
      }
      catch ( IllegalAccessException e )
      {
         throw new ControllerExecutorException( "Controller " + oResolution.getClassName()
                                                + ", method " + oResolution.getMethodName()
                                                + ", method access error", e );
      }
      catch ( InvocationTargetException e )
      {
         throw new ControllerExecutorException( "Controller " + oResolution.getClassName()
                                                + ", method " + oResolution.getMethodName()
                                                + ", method threw exception", e );
      }
      catch ( ParameterProviderException e )
      {
         throw new ControllerExecutorException( "Controller " + oResolution.getClassName()
                                                + ", method " + oResolution.getMethodName()
                                                + ", parameter error", e );
      }

      return oControllerReturnValue;
   }
}

// EOF