package com.acciente.test.commons.javac;

import com.acciente.commons.javac.CompiledClass;

import java.io.File;
import java.io.IOException;

/**
 * Log
 * Feb 21, 2008 APR  -  created
 */
public class TestCompiledClass
{
	public static void main( String[] asArgs ) throws IOException
	{
		System.out.println( "Commons - CompiledClass - test" );

		CompiledClass[]	oCompiledClasses = CompiledClass.getCompiledClasses( new File( "c:/temp/javac/prod" ), "" );

		for ( int i = 0; i < oCompiledClasses.length; i++ )
		{
			System.out.println( "class: " + oCompiledClasses[ i ].getClassName() + " (size: " + oCompiledClasses[ i ].getByteCode().length + ")" );
		}
	}

}

// EOF