package com.acciente.test.commons.loader;

import com.acciente.commons.javac.CompiledClass;
import com.acciente.commons.javac.JavaCompiler;
import com.acciente.commons.javac.JavaCompilerManager;
import com.acciente.commons.loader.ByteCodeClassLoader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Log
 * Feb 22, 2008 APR  -  created
 */
public class TestByteCodeClassLoader
{
   public static void main( String[] asArgs ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException
   {
      System.out.println( "ClassLoader - test" );

      JavaCompilerManager oJavaCompilerManager;
		oJavaCompilerManager = new JavaCompilerManager( "com.acciente.commons.javac.JavaCompiler_JDK_1_4",
																		new File( "c:/temp/javac" ) );

		JavaCompiler oCompiler = oJavaCompilerManager.getCompiler();

		FileReader oSource = new FileReader( "../demoapp/controller/HelloWorld.java" );
      oCompiler.compile( "HelloWorld", oSource );
      oSource.close();

		System.out.println( "compile: returned  : " + oCompiler.isCompileOK() );
		System.out.println( "compile: error code: " + oCompiler.getErrorCode() );
		System.out.println( "compile: messages  : " + oCompiler.getMessages() );

		if ( oCompiler.isCompileOK() )
		{
			CompiledClass[]	oCompiledClasses = oCompiler.getCompiledClasses();

			for ( int i = 0; i < oCompiledClasses.length; i++ )
			{
            String   sClassName = oCompiledClasses[ i ].getClassName();
            System.out.println( "loader: attempting to load class: " + sClassName );

            ByteCodeClassLoader  oByteCodeClassLoader1 = new ByteCodeClassLoader( TestByteCodeClassLoader.class.getClassLoader() );
            ByteCodeClassLoader  oByteCodeClassLoader2 = new ByteCodeClassLoader( TestByteCodeClassLoader.class.getClassLoader() );

            oByteCodeClassLoader1.addClassDef( sClassName, oCompiledClasses[ i ].getByteCode() );
            oByteCodeClassLoader2.addClassDef( sClassName, oCompiledClasses[ i ].getByteCode() );

            Class oClass1 = oByteCodeClassLoader1.loadClass( sClassName );
            System.out.println( "loader 1: class name       : " + oClass1 );
            System.out.println( "loader 1: class hash       : " + oClass1.hashCode() );
            System.out.println( "loader 1: loader name      : " + oClass1.getClassLoader() );
            System.out.println( "loader 1: created instance : " + oClass1.newInstance() );

            Class oClass2 = oByteCodeClassLoader2.loadClass( sClassName );
            System.out.println( "loader 2: class name       : " + oClass2 );
            System.out.println( "loader 2: class hash       : " + oClass2.hashCode() );
            System.out.println( "loader 2: loader name      : " + oClass2.getClassLoader() );
            System.out.println( "loader 2: created instance : " + oClass2.newInstance() );
         }
		}
   }
}
