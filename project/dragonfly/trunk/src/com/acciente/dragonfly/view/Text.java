package com.acciente.dragonfly.view;

/**
 * This interface should be implemented by a view to output text directly
 *
 * Log
 * Mar 10, 2008 APR  -  created
 */
public interface Text
{
   /**
    * This method should return the text
    *
    * @return a text string
    */
   String getText();

   /**
    * This method should return the mime type of the text content
    *
    * @return a string representation of a mime type, e.g. text/html
    */
   String getMimeType();
}
