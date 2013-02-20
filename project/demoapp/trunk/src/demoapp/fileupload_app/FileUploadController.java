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
package demoapp.fileupload_app;

import com.acciente.commons.htmlform.FileHandle;
import com.acciente.commons.htmlform.ParserException;
import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Form;
import com.acciente.induction.controller.Response;
import org.apache.commons.fileupload.FileUploadException;

import java.io.File;

/**
 * This class ...
 *
 * @created Apr 16, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class FileUploadController implements Controller
{
   public void handler( Response response, Form form ) throws Exception, FileUploadException, ParserException
   {
      response.setContentType("text/plain");
      response.out().println( "Induction - File Upload Demo - Response" );
      response.out().println( "----------------------------------------" );

      response.out().println( "" );
      response.out().println( "" );

      response.out().println( "General info" );
      response.out().println( "------------" );
      response.out().println("\tform parameters: " + form.getParamNames());

      response.out().println( "" );
      response.out().println( "" );

      response.out().println( "About the string you entered" );
      response.out().println( "----------------------------" );
      response.out().println( "\tvalue: \"" + form.getString( "test_string" )+ "\"" );

      FileHandle uploadedFile = form.getFile( "test_file" );

      File dumpFile = null;
      if ( uploadedFile.getSize() > 0 )
      {
         dumpFile = File.createTempFile( "test-", "-" + ( new File( uploadedFile.getOriginFilename() ) ).getName(), new File( "c:/temp" ) );

         uploadedFile.write(dumpFile);
      }

      response.out().println( "" );
      response.out().println( "" );
      response.out().println( "About the file you uploaded" );
      response.out().println( "---------------------------" );
      response.out().println("\tfile name   : " + uploadedFile.getOriginFilename());
      response.out().println( "\tcontent type: " + uploadedFile.getContentType() );
      response.out().println("\tsize        : " + uploadedFile.getSize());

      if ( dumpFile != null )
      {
         response.out().println( "contents of uploaded file written to: " + dumpFile.getCanonicalPath() );
      }
   }
}

// EOF