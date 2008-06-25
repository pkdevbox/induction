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
package demoapp.models_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;
import com.acciente.induction.resolver.ControllerResolver;

import java.io.IOException;

/**
 * This class ...
 *
 * Log
 * Jun 20, 2008 APR  -  created
 */
public class FooController implements Controller
{
   public void handler( Response oResponse, BarModel oBarModel, ControllerResolver.Resolution oResolution ) throws IOException
   {
      oResponse.setContentType( "text/plain" );

      oResponse.out().println( "barModel               : " + oBarModel );
      oResponse.out().println( "barModel.getFooModel() : " + oBarModel.getFooModel() );


      oResponse.out().println( "resolution.className  : " + oResolution.getClassName() );
      oResponse.out().println( "resolution.methodName : " + oResolution.getMethodName() );
      oResponse.out().println( "resolution.options    : " + oResolution.getOptions() );
   }
}

// EOF