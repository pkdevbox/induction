package demoapp.concepts_app;

import com.acciente.dragonfly.controller.Controller;
import com.acciente.dragonfly.controller.Response;

import java.io.IOException;

public class InitAndDestroyDemoController implements Controller
{
	public void init()
	{
      System.out.println( "InitAndDestroyDemoController(): init called" );
	}

	public void handler_1( Response oResponse ) throws IOException
   {
      oResponse.out().println( "handler_1( Response oResponse ): hello!" );
	}

   public void handler_2( Response oResponse ) throws IOException
   {
      oResponse.out().println( "handler_2( Response oResponse ): hello!" );

      for ( int i = 0; i < 100; i++ )
      {
         oResponse.out().println( "<br>" + i + ": random text" );
      }
   }

   public void destroy()
   {
      System.out.println( "InitAndDestroyDemoController(): destroy called" );
   }
}
