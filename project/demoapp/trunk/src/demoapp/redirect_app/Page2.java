package demoapp.redirect_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;

import java.io.IOException;

/**
 * Log
 * Jun 21, 2008 APR  -  created
 */
public class Page2 implements Controller
{
   public void handler( Response oResponse ) throws IOException, InterruptedException
   {
      oResponse.out().println( "This is page 2, this page does not redirect" );

      return;
   }

   public void another( Response oResponse ) throws IOException, InterruptedException
   {
      oResponse.out().println( "This is another handler in page 2, this page does not redirect" );

      return;
   }
}

// EOF