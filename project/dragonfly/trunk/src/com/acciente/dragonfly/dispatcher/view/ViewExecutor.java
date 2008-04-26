package com.acciente.dragonfly.dispatcher.view;

import com.acciente.dragonfly.template.TemplatingEngine;
import com.acciente.dragonfly.view.Image;
import com.acciente.dragonfly.view.ImageStream;
import com.acciente.dragonfly.view.Template;
import com.acciente.dragonfly.view.Text;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * This class ...
 *
 * Log
 * Apr 17, 2008 APR  -  created
 */
public class ViewExecutor
{
   // todo: consider refactoring handling each view type into a separate class
   
   private TemplatingEngine _oTemplatingEngine;

   public ViewExecutor( TemplatingEngine oTemplatingEngine )
   {
      _oTemplatingEngine = oTemplatingEngine;
   }

   public void execute( Object oControllerReturnValue, HttpServletResponse oResponse ) throws IOException, TemplateException
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

   private void processTemplate( HttpServletResponse oResponse, Template oTemplate ) throws IOException, TemplateException
   {
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