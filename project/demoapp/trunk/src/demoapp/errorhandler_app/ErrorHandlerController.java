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
   public void handler( Response oResponse, Exception oError ) throws IOException
   {
      oResponse.out().println( "Oops..you encountered an error in the demoapp, this is the error handler controller: " + getClass().getName() );

      if ( oError != null )
      {
         oResponse.out().println( "Error: " + oError );

         if ( oError.getCause() != null )
         {
            oResponse.out().println( "Cause: " + oError.getCause() );
         }
      }
   }
}
