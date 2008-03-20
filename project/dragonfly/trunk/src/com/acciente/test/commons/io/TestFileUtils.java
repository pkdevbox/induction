package com.acciente.test.commons.io;

import com.acciente.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Log
 * Feb 20, 2008 APR  -  created
 */
public class TestFileUtils
{
	public static void main( String[] asArgs ) throws IOException
	{
		System.out.println( "Commons - FileUtils" );

		File	oTempDir1, oTempDir2;

		System.out.println( "created temp dir 1: " + ( oTempDir1 = FileUtils.createTempDir( "cls", null, new File( "c:/temp/javac" ) ) ) );
		System.out.println( "created temp dir 2: " + ( oTempDir2 = FileUtils.createTempDir( "cls", null ) ) );

		oTempDir1.deleteOnExit();
		oTempDir2.deleteOnExit();
	}
}

// EOF