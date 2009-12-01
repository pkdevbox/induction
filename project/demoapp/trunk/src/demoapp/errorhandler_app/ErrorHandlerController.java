package demoapp.errorhandler_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;

import java.io.IOException;

/**
 * ErrorHandlerController
 *
 * @author Adinath Raveendra Raj
 * @created Nov 30, 2009
 */
public class ErrorHandlerController implements Controller
{
   public void handler( Response oResponse ) throws IOException
   {
      oResponse.out().println( "Oops..you encountered an error in the demoapp, this is the error handler controller: " + getClass().getName() );
   }
}
