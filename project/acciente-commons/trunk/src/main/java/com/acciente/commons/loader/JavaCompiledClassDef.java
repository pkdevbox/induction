/*
 * Copyright 2008-2010 Acciente, LLC
 *
 * Acciente, LLC licenses this file to you under the
 * Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.acciente.commons.loader;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * An implementation of a ClassDef backed by compiled Java class files.
 *
 * @see com.acciente.commons.loader.ClassDef
 *
 * @created Feb 29, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class JavaCompiledClassDef implements ClassDef
{
   // compiled file data
   private File         _oCompiledFile;
   private long         _iLastModifiedTimeAtLastLoad;

   // class data
   private String       _sClassName;
   private byte[]       _ayClassByteCode;
   private String[]     _asReferencedClassNames;

   JavaCompiledClassDef( String sClassName, File oCompiledFile )
      throws ClassNotFoundException
   {
      _sClassName    = sClassName;
      _oCompiledFile = oCompiledFile;

      loadCompiledClassFile();
   }

   public String getClassName()
   {
      return _sClassName;
   }

   public boolean isModified()
   {
      return ( _oCompiledFile.lastModified() != _iLastModifiedTimeAtLastLoad );
   }

   public void reload() throws ClassNotFoundException
   {
      loadCompiledClassFile();
   }

   public byte[] getByteCode()
   {
      return _ayClassByteCode;
   }

   public ClassDef[] getBundledClassDefs()
   {
      return null;
   }

   public String[] getReferencedClasses()
   {
      return _asReferencedClassNames;
   }

   private void loadCompiledClassFile()
      throws ClassNotFoundException
   {
      try
      {
         // checkpoint the compiled file's modified date before the compile
         long iLastModifiedTimeAtLastLoad = _oCompiledFile.lastModified();

         _ayClassByteCode = FileUtils.readFileToByteArray( _oCompiledFile );

         _iLastModifiedTimeAtLastLoad = iLastModifiedTimeAtLastLoad;

         readReferencedClasses( _ayClassByteCode, _oCompiledFile.getName() );
      }
      catch ( IOException e )
      {
         throw new ClassNotFoundException( "Error loading class definition", e );
      }
   }

   private void readReferencedClasses( byte[] ayClassByteCode, String sFilename ) throws IOException
   {
      Set oReferencedClassNameSet  = new ClassFile( ayClassByteCode ).getReferencedClasses();

      // convert the list to an array
      _asReferencedClassNames = new String[ oReferencedClassNameSet.size() ];
      oReferencedClassNameSet.toArray( _asReferencedClassNames );
   }
}

// EOF