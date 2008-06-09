package demoapp.counter_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;

import java.io.IOException;

/**
 * Log
 * Jun 8, 2008 APR  -  created
 */
public class Show implements Controller
{
   public void handler( Response oResponse, Counter oCounter )
      throws IOException
   {
      oResponse.out().println( "count is: " + oCounter.getCount() );
   }
}

// EOF