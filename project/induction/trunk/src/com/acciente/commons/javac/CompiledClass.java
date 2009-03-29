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
package com.acciente.commons.javac;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a simple container class to hold the compiled code for a Java class. This class
 *	also provides a static method: getCompiledClasses() to read a set of compiled class files
 * from a directory. getCompiledClasses() computes the fully qualified class name based on the
 * subdirectory in which the .class is found.
 *
 *
 * @created Feb 20, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class CompiledClass
{
	private String	_sClassName;
	private byte[]	_ayByteCode;

	public CompiledClass( String sClassName, byte[] ayByteCode )
	{
		_sClassName	=	sClassName;
		_ayByteCode	=	ayByteCode;
	}

	/**
	 * Returns the fully qualified name of the contained class
	 *
	 * @return string class name
	 */
	public String getClassName()
	{
		return _sClassName;
	}

	/**
	 * Returns the byte code for the compiled class
	 *
	 * @return
	 */
	public byte[] getByteCode()
	{
		return _ayByteCode;
	}

	/**
	 * Reads a set of compiled class files from a directory. This method computes the fully
	 * qualified class name based on the subdirectory in which the .class is found. The
	 * computed package name for the class only uses the subdirectories below oClassDir, in
	 * other words the path to oClassDir itself is not included in the classes package name.
	 *
	 * @param oClassDir the root directory to load class files from
	 * @param sParentPackageName an additional parent package name (without a trailing period)
	 * that will be prepended to each class name
	 * @return an array of CompiledClass objects
	 * @throws IOException
	 */
	public static CompiledClass[] getCompiledClasses( File oClassDir, String sParentPackageName )
		throws IOException
   {
		List	oCompiledClassList = getCompiledClassList( oClassDir, sParentPackageName );

		CompiledClass[]	oCompiledClasses = new CompiledClass[ oCompiledClassList.size() ];

		oCompiledClassList.toArray( oCompiledClasses );

		return oCompiledClasses;
	}

	private static List getCompiledClassList( File oClassDir, String sPackageName )
		throws IOException
   {
		List		oCompiledClasses;
		File[]	oDirContents;

		oCompiledClasses = new ArrayList();
		oDirContents
			= oClassDir.listFiles(	new FileFilter()
											{	public boolean accept( File oFile )
												{
													return oFile.isDirectory() || oFile.getName().endsWith( ".class" );
												}
											}
										);

		for ( int i = 0; i < oDirContents.length; i++ )
		{
			File	oDirOrFile = oDirContents[ i ];

			if ( oDirOrFile.isDirectory() )
			{
				oCompiledClasses
					.addAll( getCompiledClassList(	oDirOrFile,
																sPackageName.equals( "" )
																? oDirOrFile.getName()
																: sPackageName + "." + oDirOrFile.getName()
														  )
							 );
			}
			else if ( oDirOrFile.isFile() )
			{
				String	sClassName = oDirOrFile.getName().substring( 0, oDirOrFile.getName().length() - 6 );

				oCompiledClasses
					.add( new CompiledClass(	sPackageName.equals( "" )
														?	sClassName
														:	sPackageName + "." + sClassName,
														FileUtils.readFileToByteArray( oDirOrFile )
												  )
						 );
			}
		}

		return oCompiledClasses;
	}
}

// EOF