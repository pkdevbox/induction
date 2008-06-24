/**
 *   Copyright 2008 Acciente, LLC
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

import com.acciente.commons.io.IOUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;

/**
 * @created Feb 20, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class JavaCompiler_JDK_1_4 extends JavaCompiler_JDK_Common implements JavaCompiler
{
   public void compile( String sClassName, Reader oSourceReader )
      throws IOException
   {
		_aoCompiledClasses	= null;

      File  oSourceDir = null, oClassDir = null, oSourceFile;

		try
		{
			oSourceDir  = createTempSourceDir();
			oClassDir   = createTempClassDir();
         oSourceFile = getSourceFile( oSourceDir, sClassName );

         IOUtils.copy( oSourceReader, oSourceFile );

         String[] asCompilerArgs = new String[] {  "-d", oClassDir.getCanonicalPath(),
																	oSourceFile.getCanonicalPath() };
			StringWriter oErrors = new StringWriter();
			_bCompileOK = ( _iErrorCode = com.sun.tools.javac.Main.compile( asCompilerArgs, new PrintWriter( oErrors ) ) ) == 0;
			_sCompilerMessages = oErrors.toString();

			if ( _bCompileOK )
			{
				_aoCompiledClasses = CompiledClass.getCompiledClasses( oClassDir, "" );
			}
		}
		finally
		{
			if ( oSourceDir != null )
			{
            FileUtils.deleteDirectory( oSourceDir );
			}

			if ( oClassDir != null )
			{
				FileUtils.deleteDirectory( oClassDir );
			}
		}
   }
}

// EOF