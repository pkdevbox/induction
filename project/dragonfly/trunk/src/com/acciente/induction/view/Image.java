package com.acciente.induction.view;

/**
 * This interface should be implemented by a view to render an image
 *
 * Log
 * Mar 9, 2008 APR  -  created
 */
public interface Image
{
   /**
    * This method should return the contents of the image as a byte array
    *
    * @return a byte array with the image data
    */
   byte[] getImage();

   /**
    * This method should return the mime type of the text content
    *
    * @return a string representation of a mime type, e.g. text/html
    */
   String getMimeType();
}
