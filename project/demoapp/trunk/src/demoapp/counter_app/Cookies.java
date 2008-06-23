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
package demoapp.counter_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Request;
import com.acciente.induction.controller.Response;

import javax.servlet.http.Cookie;
import java.io.IOException;

/**
 * Log
 * Jun 8, 2008 APR  -  created
 */
public class Cookies implements Controller
{
   public void handler( Request oRequest, Response oResponse )
      throws IOException
   {
      if ( oRequest.getCookies() == null )
      {
         oResponse.out().println( "The request has no cookies!" );
      }
      else
      {
         for ( int i = 0; i < oRequest.getCookies().length; i++ )
         {
            Cookie oCookie = oRequest.getCookies()[ i ];

            oResponse.out().println( "Name=" + oCookie.getName() );
            oResponse.out().println( "Domain=" + oCookie.getDomain() );
            oResponse.out().println( "Path=" + oCookie.getPath() );
            oResponse.out().println( "Secure=" + oCookie.getSecure() );
            oResponse.out().println( "Value=" + oCookie.getValue() );
            oResponse.out().println( "Version=" + oCookie.getVersion() );
         }
      }
   }
}

// EOF
