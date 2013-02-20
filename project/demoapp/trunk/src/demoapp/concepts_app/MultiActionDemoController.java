/*
 * Copyright 2008-2013 Acciente, LLC
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
package demoapp.concepts_app;

import com.acciente.commons.htmlform.ParserException;
import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Form;
import com.acciente.induction.controller.Request;
import com.acciente.induction.controller.Response;
import com.acciente.induction.controller.FormException;
import org.apache.commons.fileupload.FileUploadException;

import java.io.IOException;

public class MultiActionDemoController implements Controller
{
   // http://samples.com/cgi/helloworld/message_2

   public void action_1( Response response ) throws IOException
   {
      response.out().println( "Hello World!" );
   }

   public void action_2( Form form, Response response )
      throws IOException, FileUploadException, ParserException, FormException
   {
      response.out().println( "Hello " + form.getString( "name" ) + " using Induction form support" );
   }

   public void action_3( Request request, Response response ) throws IOException
   {
      response.out().println( "Hello " + request.getParameter( "name" ) + " using servlet's form support" );
   }

   public void action_4( Object app, Form form, Response response ) throws IOException
   {
      response.out().println( "Hello World!" );
   }
}

// EOF