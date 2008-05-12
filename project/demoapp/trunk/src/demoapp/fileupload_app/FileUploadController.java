package demoapp.fileupload_app;

import com.acciente.nitrogen.controller.Response;
import com.acciente.nitrogen.controller.Controller;
import com.acciente.nitrogen.controller.Form;
import com.acciente.commons.htmlform.ParserException;
import com.acciente.commons.htmlform.FileHandle;

import java.io.File;

import org.apache.commons.fileupload.FileUploadException;

/**
 * This class ...
 *
 * Log
 * Apr 16, 2008 APR  -  created
 */
public class FileUploadController implements Controller
{
   public void handler( Response oResponse, Form oForm ) throws Exception, FileUploadException, ParserException
   {
      oResponse.setContentType( "text/plain" );
      oResponse.out().println( "DragonFly - File Upload Test - Response" );
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