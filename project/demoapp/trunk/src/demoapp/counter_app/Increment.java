package demoapp.counter_app;

import com.acciente.induction.controller.Response;
import com.acciente.induction.controller.Controller;

import java.io.IOException;

/**
 * Log
 * Jun 8, 2008 APR  -  created
 */
public class Increment implements Controller
{
   public void handler( Response oResponse, Counter oCounter )
      throws IOException
   {
      oCounter.increment();
      oResponse.out().println( "count incremented to: " + oCounter.getCount() );
   }
}

// EOF