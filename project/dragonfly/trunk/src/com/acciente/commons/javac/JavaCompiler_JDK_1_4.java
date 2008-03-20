package com.acciente.commons.javac;

import com.acciente.commons.io.IOUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;

/**
 * Log
 * Feb 20, 2008 APR  -  created
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