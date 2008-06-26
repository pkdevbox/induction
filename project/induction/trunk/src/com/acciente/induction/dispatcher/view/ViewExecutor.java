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
package com.acciente.induction.dispatcher.view;

import com.acciente.induction.template.TemplatingEngine;
import com.acciente.induction.template.TemplatingEngineException;
import com.acciente.induction.view.Image;
import com.acciente.induction.view.ImageStream;
import com.acciente.induction.view.Template;
import com.acciente.induction.view.Text;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Internal.
 *
 * @created Apr 17, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ViewExecutor
{
   // todo: consider refactoring handling each view type into a separate class

   private TemplatingEngine _oTemplatingEngine;

   public ViewExecutor( TemplatingEngine oTemplatingEngine )
   {
      _oTemplatingEngine = oTemplatingEngine;
   }

   public void execute( Object oControllerReturnValue, HttpServletResponse oResponse ) throws ViewExecutorException
   {
      if ( oControllerReturnValue != null )
      {
         try
         {
            if ( oControllerReturnValue instanceof Text )
            {
               processText( oResponse, ( Text ) oControllerReturnValue );
            }
            else if ( oControllerReturnValue instanceof Image )
            {
               processImage( oResponse, ( Image ) oControllerReturnValue );
            }
            else if ( oControllerReturnValue instanceof ImageStream )
            {
               processImageStream( oResponse, ( ImageStream ) oControllerReturnValue );
            }
            else if ( oControllerReturnValue instanceof Template )
            {
               processTemplate( oResponse, ( Template ) oControllerReturnValue );
            }
            else
            {
               // just print the object to the response
               oResponse.getWriter().print( oControllerReturnValue );
            }
         }
         catch ( IOException e )
         {
            throw new ViewExecutorException( "exec: unable to load definition", e );
         }
         catch ( TemplatingEngineException e )
         {
            throw new ViewExecutorException( "exec: templating engine exception", e.getCause() );
         }
      }
   }

   private void processText( HttpServletResponse oResponse, Text oText ) throws IOException
   {
      oResponse.setContentType( oText.getMimeType() == null ? "text/plain": oText.getMimeType() );

      BufferedWriter oWriter = new BufferedWriter( oResponse.getWriter() );
      try
      {
         oWriter.write( oText.getText() );
      }
      finally
      {
         oWriter.flush();
      }
   }

   private void processImage( HttpServletResponse oResponse, Image oImage ) throws IOException
   {
      if ( oImage.getMimeType() == null )
      {
         throw new IllegalArgumentException( "Image must specify a mime type" );
      }

      if ( oImage.getImage() == null )
      {
         throw new IllegalArgumentException( "Image has no content" );
      }

      oResponse.setContentType( oImage.getMimeType() );

      BufferedOutputStream oStream = new BufferedOutputStream( oResponse.getOutputStream() );
      try
      {
         oStream.write( oImage.getImage() );
      }
      finally
      {
         oStream.flush();
      }
   }

   private void processImageStream( HttpServletResponse oResponse, ImageStream oImageStream ) throws IOException
   {
      if ( oImageStream.getMimeType() == null )
      {
         throw new IllegalArgumentException( "ImageStream must specify a mime type" );
      }

      if ( oImageStream.getMimeType() == null )
      {
         throw new IllegalArgumentException( "ImageStream must specify a mime type" );
      }

      oResponse.setContentType( oImageStream.getMimeType() );

      BufferedOutputStream oStream = new BufferedOutputStream( oResponse.getOutputStream() );
      try
      {
         oImageStream.writeImage( oStream );
      }
      finally
      {
         oStream.flush();
      }
   }

   private void processTemplate( HttpServletResponse oResponse, Template oTemplate ) throws TemplatingEngineException, IOException
   {
      oResponse.setContentType( oTemplate.getMimeType() == null ? "text/plain": oTemplate.getMimeType() );

      BufferedWriter oWriter = new BufferedWriter( oResponse.getWriter() );
      try
      {
         _oTemplatingEngine.process( oTemplate, oWriter );
      }
      finally
      {
         oWriter.flush();
      }
   }
}

// EOF