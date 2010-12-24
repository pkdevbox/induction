/*
 * Copyright 2008-2010 Acciente, LLC
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
package com.acciente.commons.reflect;

/**
 * This interface is used by the Invoker to allow its caller to provide parameter
 * values based on parameter type. The Invoker calls the getParameter() method on
 * the provided implementation once for each argument of a constructor or method
 * for which the Invoker needs a value. The Invoker will not call getParameter()
 * if the a value for a parameter could be determined based on the aoArgs array
 * passed to an Invoker method.
 *
 * @see Invoker
 *
 * @created Jun 20, 2008
 *
 * @author Adinath Raveendra Raj
 */
public interface ParameterProvider
{
   /**
    * Returns the value for a parameter given the Parameter type
    *
    * @param oParamClass a class object representing the parameter type
    * @return null or an object representing the parameter type
    * @throws ParameterProviderException should be used to wrap any exception that the implementation class may wish to propagate
    */
   public Object getParameter( Class oParamClass ) throws ParameterProviderException;
}

// EOF