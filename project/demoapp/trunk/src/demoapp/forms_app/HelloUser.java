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
   public void handler( Form oForm, Response oResponse ) throws Exception
   {
      String sUserName = oForm.getString( "userName" );

      oResponse.out().println( "Hello " + sUserName + "!" );
   }
}

// EOF