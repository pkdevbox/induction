package demoapp.counter_app;

import com.acciente.nitrogen.controller.Controller;
import com.acciente.nitrogen.controller.Response;

import java.io.IOException;

/**
 * This class ...
 *
 * Log
 * Mar 25, 2008 APR  -  created
 */
public class CounterController implements Controller
{
   public void post( Response oResponse )
      throws IOException
   {
      oResponse.out().println( "post( Response oResponse ): hello 1" );
   }

   public void save( Response oResponse )
      throws IOException
   {
      oResponse.setContentType( "text/plain" );

      oResponse.out().println( "save( Response oResponse ): hello 1" );
      oResponse.out().println( "save( Response oResponse ): hello 2" );
   }

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