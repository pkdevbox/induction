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
package com.acciente.commons.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * A collection of general purpose I/O utils.
 *
 * @created Feb 20, 2008
 * @author APR
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