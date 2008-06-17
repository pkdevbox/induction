package com.acciente.commons.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Log
 * Feb 20, 2008 APR  -  created
 */
public class IOUtils
{
   public static int   FILEIO_BUFFER_SIZE = 512;

   public static void copy( File oFrom, OutputStream oTo ) throws IOException
   {
      FileInputStream oFromStream = new FileInputStream( oFrom );

      try
      {
         byte[]   ayBuffer = new byte[ FILEIO_BUFFER_SIZE ];
         int      iBytesRead;
         while ( ( iBytesRead = oFromStream.read( ayBuffer ) ) == FILEIO_BUFFER_SIZE )
         {
            oTo.write( ayBuffer );
         }
         oTo.write( ayBuffer, 0, iBytesRead );
         oTo.flush();
      }
      finally
      {
         oFromStream.close();
      }
   }

   public static void copy( Reader oFrom, File oTo ) throws IOException
   {
      Writer   oToWriter = new FileWriter( oTo );

      try
      {
         char[]   acBuffer = new char[ FILEIO_BUFFER_SIZE ];
         int      iCharsRead;
         while ( ( iCharsRead = oFrom.read( acBuffer ) ) == FILEIO_BUFFER_SIZE )
         {
            oToWriter.write( acBuffer );
         }
         oToWriter.write( acBuffer, 0, iCharsRead );
         oToWriter.flush();
      }
      finally
      {
         oToWriter.close();
      }
   }
}

// EOF