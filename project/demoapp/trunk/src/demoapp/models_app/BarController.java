package demoapp.models_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;

import java.io.IOException;

/**
 * BarController
 *
 * @created Jun 26, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class BarController implements Controller
{
   public void handler( Response response, BarModel barModel ) throws IOException
   {
      response.setContentType("text/plain");

      response.out().println( "BarModel : " + barModel );
   }
}

// EOF