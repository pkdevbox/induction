package demoapp.controller;

import com.acciente.commons.javac.CompiledClass;
import com.acciente.dragonfly.controller.Controller;
import com.acciente.dragonfly.controller.Response;

import java.io.IOException;

public class HelloArizona implements Controller
{
	public void init()
	{
      System.out.println( "HelloArizona(): init" );
	}

	public void handler( Response oResponse ) throws IOException
   {
      oResponse.out().println( "handler( Response oResponse ): hello" );
	}

   public void another( Response oResponse ) throws IOException
   {
      oResponse.out().println( "another( Response oResponse ): hello" );
      for ( int i = 0; i < 900; i++ )
      {
         oResponse.out().println( "<br>" + i + ": random text" );
      }
   }

   public void destroy()
   {
      System.out.println( "HelloArizona(): destroy - 4" );
   }
}
