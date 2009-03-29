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

import java.io.File;

/**
 * @created Feb 20, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class JavaCompilerManager
{
	private Class		_oJavaCompilerClass;
	private File		_oWorkingDirectory;

	/**
	 *	This is a factory constructor to get a java compiler instance
	 *
	 * @param sJavaCompilerClassname the name of the class providing the compilation services
	 * @throws ClassNotFoundException
	 */
	public JavaCompilerManager( String sJavaCompilerClassname )
		throws ClassNotFoundException
	{
		this( sJavaCompilerClassname, null );
	}

	/**
	 *	This is a factory method to get a new instance of a java compiler
	 *
	 * @param sJavaCompilerClassname the name of the class providing the compilation services
	 * @param oWorkingDirectory a specific directory location for any intemediate compiler files, note that not
	 * 			all compiler implementation may use a working directory
	 * @throws ClassNotFoundException
	 */
	public JavaCompilerManager( String	sJavaCompilerClassname,
										 File		oWorkingDirectory
									  )
		throws ClassNotFoundException
	{
		_oJavaCompilerClass	=	Class.forName( sJavaCompilerClassname );
		_oWorkingDirectory	=	oWorkingDirectory;
	}

	/**
	 * Returns a new java compiler instance
	 *
	 * @return a new java compiler instance
	 *
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public JavaCompiler getCompiler()
		throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		JavaCompiler	oJavaCompiler	=	( JavaCompiler ) _oJavaCompilerClass.newInstance();

		oJavaCompiler.setWorkingDir( _oWorkingDirectory );

		return oJavaCompiler;
	}
}

// EOF
