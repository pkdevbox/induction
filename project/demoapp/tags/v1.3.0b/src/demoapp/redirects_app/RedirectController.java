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
package demoapp.redirects_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Redirect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Log
 * Jun 21, 2008 APR  -  created
 */
public class RedirectController implements Controller
{
   public Redirect goto1() throws IOException, InterruptedException
   {
      Thread.sleep( 2000 );

      return new Redirect( Page1View.class );
   }

   public Redirect goto2() throws IOException, InterruptedException
   {
      Thread.sleep( 2000 );

      return new Redirect( Page2View.class );
   }

   public Redirect goto3() throws IOException, InterruptedException
   {
      Thread.sleep( 2000 );

      return new Redirect( Action1Controller.class );
   }

   public Redirect goto4() throws IOException, InterruptedException
   {
      Thread.sleep( 2000 );

      return new Redirect( Action1Controller.class, "anotherHandler" );
   }

   public Redirect goto5() throws IOException, InterruptedException
   {
      Thread.sleep( 2000 );

      Map oQueryParams = new HashMap();

      oQueryParams.put( "name",     "John Doe" );
      oQueryParams.put( "message",  "You have a message in a bottle" );

      return new Redirect( Action1Controller.class, "anotherHandler", oQueryParams );
   }
}

// EOF