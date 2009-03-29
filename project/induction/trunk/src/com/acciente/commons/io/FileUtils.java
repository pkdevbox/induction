/**
 *   Copyright 2009 Acciente, LLC
 *
 *   Acciente, LLC licenses this file to you under the
 *   Apache License, Version 2.0 (the "License"); you
 *   may not use this file except in compliance with the
 *   License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in
 *   writing, software distributed under the License is
 *   distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 *   OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing
 *   permissions and limitations under the License.
 */
package com.acciente.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * A collection of general purpose file utils.
 *
 * @created Feb 20, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class FileUtils
{
	private static final Random __oRandom = new Random();

	/**
	 * Creates a temp directory
	 *
	 * @param sPrefix an optional prefix for the temp directory name, if not specified the default value of "tmp" is used
	 * @param sSuffix an optional suffix for the temp directory name, if not specified no suffix is used
	 * @return a File with the temp directory path
	 * @throws IOException
	 */
	public static File createTempDir( String sPrefix, String sSuffix )
		throws IOException
	{
		return createTempDir( sPrefix, sSuffix, null );
	}

	/**
	 * Creates a temp directory
	 *
	 * @param sPrefix an optional prefix for the temp directory name, if not specified the default value of "tmp" is used
	 * @param sSuffix an optional suffix for the temp directory name, if not specified no suffix is used
	 * @param oRootDir an optional parent directory in which to create the new temp directory
	 * @return a File with the temp directory path
	 * @throws IOException
	 */
	public static File createTempDir( String sPrefix, String sSuffix, File oRootDir )
		throws IOException
	{
		if ( sPrefix == null )	{	sPrefix = "";	}
		if ( sSuffix == null )	{	sSuffix = "tmp";		}
		if ( oRootDir == null )	{	oRootDir = new File( getSystemTempDir() );	}

		File	oTempDir = null;

		for ( int i = 0; i < 3; i++ )
		{
			synchronized ( __oRandom )
			{
				oTempDir = new File( oRootDir, sPrefix + Math.abs( __oRandom.nextInt() ) + sSuffix );
			}

         // ensure that directory does not already exist, and we were able to create it
         if ( ! oTempDir.isDirectory() && oTempDir.mkdirs() )
			{
				break;
			}
			oTempDir = null;
		}

		if ( oTempDir == null )
		{
			throw new IOException( "Attempting to create temp dir in: " + oRootDir + " failed after 3 tries" );
		}

		return oTempDir;
	}

	private static String getSystemTempDir() throws IOException
	{
		String sSystemTempDir;

		if ( ( sSystemTempDir = System.getProperty( "java.io.tmpdir" ) ) == null )
		{
			throw new IOException( "No default temp directory defined in system property: java.io.tmpdir" );
		}

		return sSystemTempDir;
	}
}
