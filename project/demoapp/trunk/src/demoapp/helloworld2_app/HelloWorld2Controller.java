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
package demoapp.helloworld2_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;

import java.io.IOException;

/**
 * A simple controller that uses a Text view to display hello world
 *
 * @created Apr 23, 2008
 * @author Adinath Raveendra Raj
 */
public class HelloWorld2Controller implements Controller
{
   public HelloWorld2View handler()
   {
      // typically we would do some processing of the user input here
      // and pass some data into the view via its constructor
      return new HelloWorld2View();
   }

   public Class handler2( Response oResponse ) throws IOException
   {
      return HelloWorld2View.class;
   }
}
