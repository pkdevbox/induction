package com.acciente.commons.javac;

import java.io.File;

/**
 * Log
 * Feb 20, 2008 APR  -  created
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
