package com.acciente.commons.htmlform;

import org.apache.commons.fileupload.FileItem;

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

   FileHandle( FileItem oFileItem )
   {
      _oFileItem = oFileItem;
   }

   // todo: design interface and implement
}
