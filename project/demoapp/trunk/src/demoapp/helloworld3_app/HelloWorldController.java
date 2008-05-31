package demoapp.helloworld3_app;

import com.acciente.induction.controller.Controller;

/**
 * Log
 * Apr 26, 2008 APR  -  created
 */
public class HelloWorldController implements Controller
{
   public HelloWorldView handler()
   {
      // typically we would do some processing of the user input here

      return new HelloWorldView();
   }
}
