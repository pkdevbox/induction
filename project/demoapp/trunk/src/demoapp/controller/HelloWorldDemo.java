package demoapp.controller;

import com.acciente.dragonfly.controller.Controller;
import demoapp.view.HelloWorldView;

/**
 * A really simple controller
 *
 * Log
 * Apr 23, 2008 APR  -  created
 */
public class HelloWorldDemo implements Controller
{
   public HelloWorldView handler()
   {
      // typically we would do some processing of the user input here

      return new HelloWorldView();
   }
}
