package demoapp.redirect_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Redirect;
import com.acciente.induction.controller.Response;

import java.io.IOException;

/**
 * Log
 * Jun 21, 2008 APR  -  created
 */
public class Page1 implements Controller
{
   public Redirect handler( Response oResponse ) throws IOException, InterruptedException
   {
      System.out.println( "This is page 1, it will redirect to Page 2 in 5 seconds" );
      System.out.flush();

      Thread.sleep( 2000 );

      return new Redirect( Page2.class );
   }

   public Redirect handler2( Response oResponse ) throws IOException, InterruptedException
   {
      System.out.println( "This is handler 2 in page 1, it will redirect to a handler named 'another' in Page 2 in 5 seconds" );
      System.out.flush();

      Thread.sleep( 2000 );

      return new Redirect( Page2.class, "another" );
   }
}

// EOF