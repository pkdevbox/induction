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
package com.acciente.commons.loader;

import com.acciente.commons.javac.CompiledClass;
import com.acciente.commons.javac.JavaCompiler;
import com.acciente.commons.javac.JavaCompilerManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

/**
 * Class definitions loaded directly from Java source files.
 *
 * @created Feb 27, 2008
 *
 * @author Adinath Raveendra Raj
 */
class JavaSourceClassDef implements ClassDef
{
   // source file data
   private File                  _oSourceFile;
   private long                  _iLastModifiedTimeAtLastCompile;

   // class data
   private String                _sClassName;
   private byte[]                _ayClassByteCode;
   private ClassDef[]            _aoBundledClassDefs;

   // compiler service
   private JavaCompilerManager   _oJavaCompilerManager;

   JavaSourceClassDef( String sClassName, File oSourceFile, JavaCompilerManager oJavaCompilerManager )
      throws ClassNotFoundException
   {
      _sClassName             = sClassName;
      _oSourceFile            = oSourceFile;
      _oJavaCompilerManager   = oJavaCompilerManager;

      loadClassFromSource();
   }

   public String getClassName()
   {
      return _sClassName;
   }

   public boolean isModified()
   {
      return ( _oSourceFile.lastModified() != _iLastModifiedTimeAtLastCompile );
   }

   public void reload() throws ClassNotFoundException
   {
      loadClassFromSource();
   }

   public byte[] getByteCode()
   {
      return _ayClassByteCode;
   }

   public ClassDef[] getBundledClassDefs()
   {
      return _aoBundledClassDefs;
   }

   public String[] getReferencedClasses()
   {
      return null;
   }

   private void loadClassFromSource()
      throws ClassNotFoundException
   {
      // checkpoint the source file's modified date before the compile
      long iLastModifiedTimeAtLastCompile = _oSourceFile.lastModified();

      // compile the source file
      CompiledClass[]   oCompiledClasses;
      try
      {
         oCompiledClasses = compileSource( _sClassName, _oSourceFile );
      }
      catch ( CompileException e )
      {
         throw new ClassNotFoundException( "Compiling source file for class: " + _sClassName + " failed", e );
      }

      // transfer the compile class code into the ClassDef structure
      ArrayList   oBundledClasses = null;
      byte[]      ayClassByteCode = null;

      // put the definitions in the classloader
      for ( int i = 0; i < oCompiledClasses.length; i++ )
      {
         CompiledClass oCompiledClass = oCompiledClasses[ i ];

         if ( oCompiledClass.getClassName().equals( _sClassName ) )
         {
            ayClassByteCode = oCompiledClass.getByteCode();
         }
         else
         {
            if ( oBundledClasses == null )
            {
               oBundledClasses = new ArrayList();
            }
            oBundledClasses.add( new ByteCodeClassDef( oCompiledClass.getClassName(), oCompiledClass.getByteCode() ) );
         }
      }

      if ( ayClassByteCode == null )
      {
         // hmmm..this should not happen except perhaps due to a bug in our code
         throw new ClassNotFoundException( "Main class: " + _sClassName + " not found in compiled output" );
      }

      // we update the instance variables from locals to minimize the chance that an exception will
      // leave this object in an invalid state
      _ayClassByteCode  =  ayClassByteCode;

      if ( oBundledClasses != null )
      {
         _aoBundledClassDefs =  new ClassDef[ oBundledClasses.size() ];
         oBundledClasses.toArray( _aoBundledClassDefs );
      }

      _iLastModifiedTimeAtLastCompile = iLastModifiedTimeAtLastCompile;
   }

   private CompiledClass[] compileSource( String sClassName, File oSourceFile )
      throws CompileException
   {
      JavaCompiler oJavaCompiler;
      Reader oSourceReader  =  null;

      try
      {
         oJavaCompiler = _oJavaCompilerManager.getCompiler();
         oSourceReader = new BufferedReader( new FileReader( oSourceFile ) );
         oJavaCompiler.compile( sClassName, oSourceReader );
      }
      catch ( Throwable o )
      {
         throw new CompileException( "Exception during java compilation", o );
      }
      finally
      {
         try
         {
            if ( oSourceReader != null )
            {
               oSourceReader.close();
            }
         }
         catch ( Throwable o )
         {
            throw new CompileException( "Exception during java compilation", o );
         }
      }

      if ( ! oJavaCompiler.isCompileOK() )
      {
         throw new CompileException( oJavaCompiler.getMessages() );
      }

      return oJavaCompiler.getCompiledClasses();
   }
}

// EOF