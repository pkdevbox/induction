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
   public void handler( Response oResponse, Form oForm ) throws Exception, FileUploadException, ParserException
   {
      oResponse.setContentType( "text/plain" );
      oResponse.out().println( "Induction - File Upload Test - Response" );
      oResponse.out().println( "----------------------------------------" );

      oResponse.out().println( "" );
      oResponse.out().println( "" );

      oResponse.out().println( "General info" );
      oResponse.out().println( "------------" );
      oResponse.out().println( "\tform parameters: " + oForm.getParamNames() );

      oResponse.out().println( "" );
      oResponse.out().println( "" );

      oResponse.out().println( "About the string you entered" );
      oResponse.out().println( "----------------------------" );
      oResponse.out().println( "\tvalue: \"" + oForm.getString( "test_string" )+ "\"" );

      FileHandle oUploadedFile = oForm.getFile( "test_file" );

      File oDumpFile = null;
      if ( oUploadedFile.getSize() > 0 )
      {
         oDumpFile = File.createTempFile( "test-", "-" + ( new File( oUploadedFile.getOriginFilename() ) ).getName(), new File( "c:/temp" ) );

         oUploadedFile.write( oDumpFile );
      }

      oResponse.out().println( "" );
      oResponse.out().println( "" );
      oResponse.out().println( "About the file you uploaded" );
      oResponse.out().println( "---------------------------" );
      oResponse.out().println( "\tfile name   : " + oUploadedFile.getOriginFilename() );
      oResponse.out().println( "\tcontent type: " + oUploadedFile.getContentType() );
      oResponse.out().println( "\tsize        : " + oUploadedFile.getSize() );

      if ( oDumpFile != null )
      {
         oResponse.out().println( "contents of uploaded file written to: " + oDumpFile.getCanonicalPath() );
      }
   }
}

// EOF