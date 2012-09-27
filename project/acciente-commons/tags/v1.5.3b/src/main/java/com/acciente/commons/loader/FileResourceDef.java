/*
 * Copyright 2008-2012 Acciente, LLC
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

/**
 * An implementation of a ResourceDef backed by files.
 *
 * @see ClassDef
 *
 * @created Sep 30, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class FileResourceDef implements ResourceDef
{
   // compiled file data
   private File         _oResourceFile;
   private long         _iLastModifiedTimeAtLastLoad;

   // class data
   private String       _sResourceName;
   private byte[]       _ayContent;

   FileResourceDef( String sResourceName, File oResourceFile )
   {
      _sResourceName = sResourceName;
      _oResourceFile = oResourceFile;

      loadResourceFile();
   }

   public String getResourceName()
   {
      return _sResourceName;
   }

   public boolean isModified()
   {
      return ( _oResourceFile.lastModified() != _iLastModifiedTimeAtLastLoad );
   }

   public void reload()
   {
      loadResourceFile();
   }

   public byte[] getContent()
   {
      return _ayContent;
   }

   private void loadResourceFile()
   {
      try
      {
         // checkpoint the compiled file's modified date before the compile
         long iLastModifiedTimeAtLastLoad = _oResourceFile.lastModified();

         _ayContent = FileUtils.readFileToByteArray( _oResourceFile );

         _iLastModifiedTimeAtLastLoad = iLastModifiedTimeAtLastLoad;
      }
      catch ( IOException e )
      {
         _ayContent                    = null;
         _iLastModifiedTimeAtLastLoad  = 0;
      }
   }
}

// EOF