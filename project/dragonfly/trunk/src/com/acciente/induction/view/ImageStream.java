package com.acciente.induction.view;

import java.io.OutputStream;

/**
 * This interface should be implemented by a view to render an image. This interface
 * provided maximum control over how the image data is handed over to induction
 *
 * Log
 * Mar 9, 2008 APR  -  created
 */
public interface ImageStream
{
   /**
    * This method when called by dragonFly, should write the image contents
    * to the output stream passed to the method
    *
    * @param oOutputStream the output stream to which the image data should be written out to
    */
   void writeImage( OutputStream oOutputStream );

   /**
    * This method should return the mime type of the text content
    *
    * @return a string representation of a mime type, e.g. text/html
    */
   String getMimeType();
}
