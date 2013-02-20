/*
 * Copyright 2008-2013 Acciente, LLC
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
package com.acciente.induction.init.config.xmlconfigloader;

import com.acciente.induction.init.config.Config;
import com.acciente.commons.lang.Strings;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

import java.io.File;

/**
 * Internal.
 * FileUploadRule
 *
 * @created May 8, 2008
 *
 * @author Adinath Raveendra Raj
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

   public void begin( String sNamespace, String sName, Attributes oAttributes )
   {
      _oMaxUploadSize            = null;
      _oStoreFileOnDiskThreshold = null;
      _oUploadedFileStorageDir   = null;
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