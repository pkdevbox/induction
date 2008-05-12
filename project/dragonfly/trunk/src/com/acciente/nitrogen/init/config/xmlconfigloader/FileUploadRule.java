package com.acciente.nitrogen.init.config.xmlconfigloader;

import com.acciente.nitrogen.init.config.Config;
import com.acciente.commons.lang.Strings;
import org.apache.commons.digester.Rule;

import java.io.File;

/**
 * FileUploadRule
 *
 * Log
 * May 8, 2008 APR  -  created
 */
public class FileUploadRule extends Rule
{
   private  Config.FileUpload    _oFileUpload;

   private  Integer              _oMaxUploadSize;
   private  Integer              _oStoreFileOnDiskThreshold;
   private  File                 _oUploadedFileStorageDir;

   public FileUploadRule( Config.FileUpload oFileUpload )
   {
      _oFileUpload = oFileUpload;
   }

   public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
   {
      if ( _oMaxUploadSize != null )
      {
         _oFileUpload.setMaxUploadSizeInBytes( _oMaxUploadSize.intValue() );
      }

      if ( _oStoreFileOnDiskThreshold != null )
      {
         _oFileUpload.setStoreOnDiskThresholdInBytes( _oStoreFileOnDiskThreshold.intValue() );
      }

      if ( _oUploadedFileStorageDir != null )
      {
         _oFileUpload.setUploadedFileStorageDir( _oUploadedFileStorageDir );
      }
   }

   public ParamMaxUploadSizeRule createParamMaxUploadSizeRule()
   {
      return new ParamMaxUploadSizeRule();
   }

   public ParamStoreFileOnDiskThresholdRule createParamStoreFileOnDiskThresholdRule()
   {
      return new ParamStoreFileOnDiskThresholdRule();
   }

   public ParamUploadedFileStorageDirRule createParamUploadedFileStorageDirRule()
   {
      return new ParamUploadedFileStorageDirRule();
   }

   private class ParamMaxUploadSizeRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( sText ) )
         {
            throw new XMLConfigLoaderException( "config > file-upload > max-upload-size: must specify a valid integer value" );
         }
         _oMaxUploadSize = new Integer( sText );
      }
   }

   private class ParamStoreFileOnDiskThresholdRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( sText ) )
         {
            throw new XMLConfigLoaderException( "config > file-upload > store-file-on-disk-threshold: must specify a valid integer value" );
         }
         _oStoreFileOnDiskThreshold = new Integer( sText );
      }
   }


   private class ParamUploadedFileStorageDirRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( sText ) )
         {
            throw new XMLConfigLoaderException( "config > file-upload > uploaded-file-storage-dir: must specify a directory name" );
         }
         _oUploadedFileStorageDir = new File( sText );
      }
   }
}

// EOF