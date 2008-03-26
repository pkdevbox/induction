package demoapp.controller;

import com.acciente.dragonfly.controller.Controller;
import com.acciente.dragonfly.controller.Response;

import java.io.IOException;

/**
 * This class ...
 *
 * Log
 * Mar 25, 2008 APR  -  created
 */
public class Signup implements Controller
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
}

// EOF