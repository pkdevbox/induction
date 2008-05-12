package demoapp.helloworld2_app;

import com.acciente.nitrogen.controller.Controller;

/**
 * A simple controller that uses a Text view to display hello world
 *
 * Log
 * Apr 23, 2008 APR  -  created
 */
public class HelloWorldController implements Controller
{
   public HelloWorldView handler()
   {
      // typically we would do some processing of the user input here

      return new HelloWorldView();
   }
}
