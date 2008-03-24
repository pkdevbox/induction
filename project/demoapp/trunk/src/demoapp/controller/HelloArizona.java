package demoapp.controller;

import com.acciente.commons.javac.CompiledClass;
import com.acciente.dragonfly.controller.Controller;
import com.acciente.dragonfly.controller.Response;

import java.io.IOException;

public class HelloArizona implements Controller
{
	CompiledClass oCompiledClass;

	public HelloArizona()
	{
		oCompiledClass = new CompiledClass( "HelloArizona.CompiledClass", null );

      System.out.println( "oCompiledClass" + oCompiledClass );

      System.out.println( "Hello from HelloArizona(Phoenix) constructor" );
      System.out.println( "Hello from HelloArizona(Scottsdale) constructor" );
      System.out.println( "Hello from HelloArizona(Kayenta) constructor" );
	}

	public void handler( Response oResponse ) throws IOException
   {
		oResponse.out().println( "Hello Generic! from handler() in HelloArizona" );
	}

   public void john( Response oResponse ) throws IOException
   {
      for ( int i = 0; i < 900; i++ )
      {
         oResponse.out().println( "<br>" + i + ": Hello John! this is the HelloArizona controller" );
      }
   }
}
