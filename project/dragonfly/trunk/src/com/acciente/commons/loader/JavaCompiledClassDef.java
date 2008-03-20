package com.acciente.commons.loader;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Log
 * Feb 29, 2008 APR  -  created
 */
public class JavaCompiledClassDef implements ClassDef
{
   // compiled file data
   private File         _oCompiledFile;
   private long         _iLastModifiedTimeAtLastLoad;

   // class data
   private String       _sClassName;
   private byte[]       _ayClassByteCode;

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

   private void loadCompiledClassFile()
      throws ClassNotFoundException
   {
      try
      {
         // checkpoint the compiled file's modified date before the compile
         long iLastModifiedTimeAtLastLoad = _oCompiledFile.lastModified();

         _ayClassByteCode = FileUtils.readFileToByteArray( _oCompiledFile );

         _iLastModifiedTimeAtLastLoad = iLastModifiedTimeAtLastLoad;
      }
      catch ( IOException e )
      {
         throw new ClassNotFoundException( "Error loading class definition", e );
      }
   }
}

// EOF