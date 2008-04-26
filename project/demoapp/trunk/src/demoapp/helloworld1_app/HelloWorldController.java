package demoapp.helloworld1_app;

import com.acciente.dragonfly.controller.Response;

import java.io.IOException;

/**
 * A very simple controller that does the customary "Hello World!"
 *
 * Log
 * Apr 26, 2008 APR  -  created
 */
public class HelloWorldController
{
   public void handler( Response oReponse ) throws IOException
   {
      oReponse.setContentType( "text/plain" );
      oReponse.out().println( "Hello World!!" );
      oReponse.out().println( "(from probably the simplest controller you could write!)" );
   }
}
