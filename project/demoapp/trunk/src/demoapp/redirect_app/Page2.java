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
package demoapp.redirect_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;

import java.io.IOException;

/**
 * Log
 * Jun 21, 2008 APR  -  created
 */
public class Page2 implements Controller
{
   public void handler( Response oResponse ) throws IOException, InterruptedException
   {
      oResponse.out().println( "This is page 2, this page does not redirect" );

      return;
   }

   public void another( Response oResponse ) throws IOException, InterruptedException
   {
      oResponse.out().println( "This is another handler in page 2, this page does not redirect" );

      return;
   }
}

// EOF