package demoapp.controller;

import com.acciente.commons.javac.CompiledClass;

public class HelloArizona
{
	CompiledClass	oCompiledClass;

	public HelloArizona()
	{
		oCompiledClass = new CompiledClass( "HelloArizona.CompiledClass", null );

		System.out.println( "Hello from HelloArizona(Phoenix) constructor" );
      System.out.println( "Hello from HelloArizona(Scottsdale) constructor" );
      System.out.println( "Hello from HelloArizona(Kayenta) constructor" );
	}

	public void handler()
	{
		System.out.println( "Hello from handler() in HelloArizona" );
	}
}
