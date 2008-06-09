package demoapp.counter_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;

import java.io.IOException;

/**
 * This class ...
 *
 * Log
 * Mar 25, 2008 APR  -  created
 */
public class CounterController implements Controller
{
   public void showCounter( Response oResponse, Counter oCounter )
      throws IOException
   {
      oResponse.out().println( "count is: " + oCounter.getCount() );
   }

   public void incrementCounter( Response oResponse, Counter oCounter )
      throws IOException
   {
      oCounter.increment();
      oResponse.out().println( "count incremented to: " + oCounter.getCount() );
   }

   public void decrementCounter( Response oResponse, Counter oCounter )
      throws IOException
   {
      oCounter.decrement();
      oResponse.out().println( "count decremented to: "  + oCounter.getCount() );
   }
}

// EOF