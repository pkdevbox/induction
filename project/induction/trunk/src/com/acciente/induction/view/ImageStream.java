/**
 *   Copyright 2009 Acciente, LLC
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
package com.acciente.induction.view;

import java.io.OutputStream;

/**
 * This view interface should be implemented to send an image to the browser. This interface
 * provided maximum control over how the image data is handed over to Induction.
 *
 * @created Mar 9, 2008

 * @author Adinath Raveendra Raj
 */
public interface ImageStream
{
   /**
    * This method when called by Induction, should write the image contents
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
