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
package com.acciente.commons.htmlform;

import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is a facade to isolate the fileupload implementation from the multi-part parser used.
 *
 * @created Apr 5, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class FileHandle
{
   private FileItem  _oFileItem;

   public FileHandle( FileItem oFileItem )
   {
      _oFileItem = oFileItem;
   }

   /**
    * Returns the content type set by the browser that uploded the file, if not
    * content type was set <code>null<code> is returned
    *
    * @return the mime type of the content
    */
   public String getContentType()
   {
      return _oFileItem.getContentType();
   }

   /**
    * Returns the size of the file in bytes
    *
    * @return the size of the file in bytes
    */
   public long getSize()
   {
      return _oFileItem.getSize();
   }

   /**
    * Returns the original name of the uploaded file on the system from which it originated
    *
    * @return a string filename including possibly a path
    */
   public String getOriginFilename()
   {
      return _oFileItem.getName();
   }

   /**
    * Write out the contents of the uploaded file to the specified destination
    *
    * @param oToFile the file to write to
    * @throws Exception if an error occurs
    */
   public void write( File oToFile ) throws Exception
   {
      _oFileItem.write( oToFile );
   }

   /**
    * Returns an input stream that may used to read the contents of the uploaded file
    *
    * @throws IOException if an error occurs
    * @return an inputstream that provides access to the uploaded file
    */
   public InputStream getInputStream() throws IOException
   {
      return _oFileItem.getInputStream();
   }
}
