package com.acciente.dragonfly.dispatcher.controller;

import com.acciente.dragonfly.view.Text;
import com.acciente.dragonfly.view.Image;
import com.acciente.dragonfly.view.ImageStream;
import com.acciente.dragonfly.view.Template;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * This class ...
 *
 * Log
 * Apr 17, 2008 APR  -  created
 */
public class ViewProcessor
{
   // todo: complete implementation

   public void process( Object oControllerReturnValue, HttpServletResponse oResponse ) throws IOException
   {
      if ( oControllerReturnValue != null )
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
   }

   private void processText( HttpServletResponse oResponse, Text oText ) throws IOException
   {
      oResponse.setContentType( oText.getMimeType() == null ? "text/plain": oText.getMimeType() );
      oResponse.getWriter().print( oText.getText() );
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
      oResponse.getOutputStream().write( oImage.getImage() );
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
      oImageStream.writeImage( oResponse.getOutputStream() );
   }

   private void processTemplate( HttpServletResponse oResponse, Template oTemplate )
   {
      // todo: implement
   }
}

// EOF