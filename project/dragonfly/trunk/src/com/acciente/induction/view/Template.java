package com.acciente.induction.view;

/**
 * This interface should be implemented by a view to render a text template
 * the view.
 *
 * Log
 * Mar 9, 2008 APR  -  created
 */
public interface Template
{
   /**
    * Returns the name of the template that dragon will use to render the
    * contents an instance of this class.
    *
    * @return a string representing a template name
    */
   String getTemplateName();

   /**
    * This method should return the mime type of the text content
    *
    * @return a string representation of a mime type, e.g. text/html
    */
   String getMimeType();
}

// EOF