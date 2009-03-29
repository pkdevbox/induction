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

import com.acciente.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @created Feb 20, 2008
 *
 * @author Adinath Raveendra Raj
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
	 * of this class by Induction
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