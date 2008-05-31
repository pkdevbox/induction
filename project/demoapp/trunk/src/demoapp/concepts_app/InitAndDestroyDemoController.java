package demoapp.concepts_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;

import java.io.IOException;

public class InitAndDestroyDemoController implements Controller
{
	public void init()
	{
      System.out.println( "InitAndDestroyDemoController(): init() called" );
	}

	public void handler( Response oResponse ) throws IOException
   {
      oResponse.out().println( "InitAndDestroyDemoController: handler( Response oResponse ): called" );
	}

   public void destroy()
   {
      System.out.println( "InitAndDestroyDemoController: destroy() called" );
   }
}

// EOF