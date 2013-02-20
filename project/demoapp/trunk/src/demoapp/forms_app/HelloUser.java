package demoapp.forms_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Form;
import com.acciente.induction.controller.Response;

/**
 * HelloUser
 *
 * @author Adinath Raveendra Raj
 * @created Jun 29, 2008
 */
public class HelloUser implements Controller
{
   public void handler( Form form, Response response ) throws Exception
   {
      String sUserName = form.getString( "userName" );

      response.out().println( "Hello " + sUserName + "!" );
   }
}

// EOF