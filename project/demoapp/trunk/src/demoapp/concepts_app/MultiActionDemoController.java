package demoapp.concepts_app;

import com.acciente.commons.htmlform.ParserException;
import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Form;
import com.acciente.induction.controller.Request;
import com.acciente.induction.controller.Response;
import com.acciente.induction.controller.HTMLFormException;
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
      throws IOException, FileUploadException, ParserException, HTMLFormException
   {
      response.out().println( "Hello " + form.getString( "name" ) + " using dragonFly form support" );
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