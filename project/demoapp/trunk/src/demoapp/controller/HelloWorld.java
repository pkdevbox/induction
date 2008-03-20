package demoapp.controller;

import com.acciente.commons.javac.CompiledClass;   

public class HelloWorld
{
	CompiledClass	oCompiledClass;

	public HelloWorld()
	{
		oCompiledClass = new CompiledClass( "HelloWorld.CompiledClass", null );

		System.out.println( "Hello from HelloWorld(North-America) constructor, " );
      //System.out.println( "Hello from HelloWorld(Africa) constructor, " );
      //System.out.println( "Hello from HelloWorld(Iceland) constructor, " );

		System.out.println( "oCompiledClass.getClassName()=" + oCompiledClass.getClassName() );
		//System.out.println( "oCompiledClass.getByteCode()=" + oCompiledClass.getByteCode() );

      HelloArizona oHelloArizona =  new HelloArizona();
   }

	public void handler()
	{
		System.out.println( "Hello from handler()" );
	}
}
