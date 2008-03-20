package com.acciente.commons.javac;

import com.acciente.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Log
 * Feb 20, 2008 APR  -  created
 */
public abstract class JavaCompiler_JDK_Common
{
	// compiler class output
	protected CompiledClass[] _aoCompiledClasses;

	// compiler status  output
	protected boolean         	_bCompileOK;
	protected String 				_sCompilerMessages;
	protected int             	_iErrorCode;

	// general environment
	protected File					_oWorkingDir;

	/**
	 * This noargs contructor is required to support dynamic instantiation
	 * of this class by dragonFly
	 */
	public JavaCompiler_JDK_Common()
	{
	}

	public void setWorkingDir( File oWorkingDir )
	{
		_oWorkingDir = oWorkingDir;
	}

	public boolean isCompileOK()
	{
		return _bCompileOK;
	}

	public CompiledClass[] getCompiledClasses()
	{
		return _aoCompiledClasses;
	}

	public int getErrorCode()
	{
		return _iErrorCode;
	}

	public String getMessages()
	{
		return _sCompilerMessages;
	}

	protected File createTempSourceDir()
      throws IOException
   {
      return FileUtils.createTempDir( "src", "", _oWorkingDir );
   }

   protected File getSourceFile( File oSourceDir, String sClassName )
   {
      int   iIndex;

      if ( ( iIndex = sClassName.lastIndexOf( "." ) ) != -1 )
      {
         sClassName = sClassName.substring( iIndex + 1 );
      }
      return new File( oSourceDir, sClassName + ".java" );
   }

   protected File createTempClassDir()
      throws IOException
   {
		return FileUtils.createTempDir( "cls", "", _oWorkingDir );
   }
}

// EOF