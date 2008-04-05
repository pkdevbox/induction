package com.acciente.test.commons.javac;

import com.acciente.commons.javac.CompiledClass;
import com.acciente.commons.javac.JavaCompiler;
import com.acciente.commons.javac.JavaCompilerManager;
import com.acciente.commons.htmlform.ParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Log
 * Feb 20, 2008 APR  -  created
 */
public class TestCompiler
{
   public static void main( String[] asArgs )
		throws IOException, ParserException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
      System.out.println( "dragonFly - JavaCompilerManager class - test" );

      JavaCompilerManager oJavaCompilerManager;
		oJavaCompilerManager = new JavaCompilerManager( "com.acciente.commons.javac.JavaCompiler_JDK_1_4",
                                                      new File( "c:/temp/javac" ) );

		JavaCompiler oCompiler = oJavaCompilerManager.getCompiler();

		FileReader        oSource = new FileReader( "../demoapp/controller/HelloWorld.java" );
      oCompiler.compile( "HelloWorld", oSource );
      oSource.close();

		System.out.println( "returned  : " + oCompiler.isCompileOK() );
		System.out.println( "error code: " + oCompiler.getErrorCode() );
		System.out.println( "messages  : " + oCompiler.getMessages() );

		if ( oCompiler.isCompileOK() )
		{
			CompiledClass[]	oCompiledClasses = oCompiler.getCompiledClasses();

			for ( int i = 0; i < oCompiledClasses.length; i++ )
			{
				FileOutputStream  oClassFile;

				oClassFile = new FileOutputStream( "../demoapp/temp/" + oCompiledClasses[ i ].getClassName() + ".class" );
				oClassFile.write( oCompiledClasses[ i ].getByteCode() );
				oClassFile.close();
			}
		}
	}
}

// EOF