package com.acciente.commons.htmlform;

import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.IOException;

/**
 * This class is a facade to isolate from any fileupload implementation provided by the
 * the multi-part parser used.
 *
 * Log
 * Apr 5, 2008 APR  -  created
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
    */
   public void getInputStream() throws IOException
   {
      _oFileItem.getInputStream();
   }
}
