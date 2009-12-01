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
package com.acciente.induction.dispatcher.view;

import com.acciente.induction.resolver.ViewResolver;
import com.acciente.induction.template.TemplatingEngine;
import com.acciente.induction.template.TemplatingEngineException;
import com.acciente.induction.view.Image;
import com.acciente.induction.view.ImageStream;
import com.acciente.induction.view.Template;
import com.acciente.induction.view.Text;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.commons.reflect.ParameterProviderException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Internal.
 *
 * @created Apr 17, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ViewExecutor
{
   private  ViewFactory          _oViewFactory;
   private  TemplatingEngine     _oTemplatingEngine;

   public ViewExecutor( ViewFactory oViewFactory, TemplatingEngine oTemplatingEngine )
   {
      _oViewFactory      = oViewFactory;
      _oTemplatingEngine = oTemplatingEngine;
   }

   public void execute( ViewResolver.Resolution    oViewResolution,
                        HttpServletRequest         oRequest,
                        HttpServletResponse        oResponse ) throws ViewExecutorException
   {
      execute( getView( oViewResolution.getClassName(), oRequest, oResponse, oViewResolution ), oResponse );
   }

   /**
    * This method is used to execute a view specified in terms of a type (Class object), this method
    * is used to support processing view types (i.e. class objects) returned from a controller, this is in
    * addition to the current support for processing a view object returned from a controller.
    * 
    * @param oViewClass a class object representing a type that implements a view class
    * @param oRequest the servlet request object
    * @param oResponse the servlet response object
    * @throws ViewExecutorException thrown if an error occurs during view processing
    */
   public void execute( Class                      oViewClass,
                        HttpServletRequest         oRequest,
                        HttpServletResponse        oResponse ) throws ViewExecutorException
   {
      if ( Text.class.isAssignableFrom( oViewClass ) )
      {
         processText( oResponse, ( Text ) getView( oViewClass.getName(), oRequest, oResponse, null ) );
      }
      else if ( Image.class.isAssignableFrom( oViewClass ) )
      {
         processImage( oResponse, ( Image ) getView( oViewClass.getName(), oRequest, oResponse, null ) );
      }
      else if ( ImageStream.class.isAssignableFrom( oViewClass ) )
      {
         processImageStream( oResponse, ( ImageStream ) getView( oViewClass.getName(), oRequest, oResponse, null ) );
      }
      else if ( Template.class.isAssignableFrom( oViewClass ) )
      {
         processTemplate( oResponse, ( Template ) getView( oViewClass.getName(), oRequest, oResponse, null ) );
      }
   }

   public void execute( Object oViewObject, HttpServletResponse oResponse ) throws ViewExecutorException
   {
      if ( oViewObject instanceof Text )
      {
         processText( oResponse, ( Text ) oViewObject );
      }
      else if ( oViewObject instanceof Image )
      {
         processImage( oResponse, ( Image ) oViewObject );
      }
      else if ( oViewObject instanceof ImageStream )
      {
         processImageStream( oResponse, ( ImageStream ) oViewObject );
      }
      else if ( oViewObject instanceof Template )
      {
         processTemplate( oResponse, ( Template ) oViewObject );
      }
      else
      {
         processObject( oResponse, oViewObject );
      }
   }

   private Object getView( String sViewClassName, HttpServletRequest oRequest, HttpServletResponse oResponse, ViewResolver.Resolution oResolution )
      throws ViewExecutorException
   {
      Object oViewObject;

      try
      {
         oViewObject = _oViewFactory.getView( sViewClassName,
                                              oRequest,
                                              oResponse,
                                              null );
      }
      catch ( ClassNotFoundException e )
      {
         throw new ViewExecutorException( "View " + sViewClassName + ", unable to load class definition", e );
      }
      catch ( ConstructorNotFoundException e )
      {
         throw new ViewExecutorException( "View " + sViewClassName + ", unable to find constructor", e );
      }
      catch ( InstantiationException e )
      {
         throw new ViewExecutorException( "View " + sViewClassName + ", instantiate error", e );
      }
      catch ( InvocationTargetException e )
      {
         throw new ViewExecutorException( "View " + sViewClassName + ", target exception", e );
      }
      catch ( IllegalAccessException e )
      {
         throw new ViewExecutorException( "View " + sViewClassName + ", access exception", e );
      }
      catch ( ParameterProviderException e )
      {
         throw new ViewExecutorException( "View " + sViewClassName + ", parameter error", e );
      }

      return oViewObject;
   }

   private void processText( HttpServletResponse oResponse, Text oText ) throws ViewExecutorException
   {
      try
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
      catch ( IOException e )
      {
         throw new ViewExecutorException( "View " + oText.getClass().getName() + ", text view I/O error", e );
      }
   }

   private void processImage( HttpServletResponse oResponse, Image oImage ) throws ViewExecutorException
   {
      try
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
         oResponse.setHeader( "Content-Type", oImage.getMimeType() );

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
      catch ( IOException e )
      {
         throw new ViewExecutorException( "View " + oImage.getClass().getName() + ", image view I/O error", e );
      }
   }

   private void processImageStream( HttpServletResponse oResponse, ImageStream oImageStream ) throws ViewExecutorException
   {
      if ( oImageStream.getMimeType() == null )
      {
         throw new ViewExecutorException( "View " + oImageStream.getClass().getName() + ", ImageStream must specify a mime type" );
      }

      oResponse.setContentType( oImageStream.getMimeType() );      
      oResponse.setHeader( "Content-Type", oImageStream.getMimeType() );

      oImageStream.writeImage( oResponse );
   }

   private void processTemplate( HttpServletResponse oResponse, Template oTemplate ) throws ViewExecutorException
   {
      try
      {
         if ( oTemplate.getTemplateName() == null )
         {
            throw new ViewExecutorException( "View: " + oTemplate.getClass().getName() + ", returned null for template name" );
         }

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
      catch ( IOException e )
      {
         throw new ViewExecutorException( "View " + oTemplate.getClass().getName() + ", template view: I/O error", e );
      }
      catch ( TemplatingEngineException e )
      {
         throw new ViewExecutorException( "View " + oTemplate.getClass().getName() + ", template view: templating engine error", e.getCause() );
      }
      catch ( Exception e )
      {
         throw new ViewExecutorException( "View " + oTemplate.getClass().getName() + ", template view: general error", e );
      }
   }

   private void processObject( HttpServletResponse oResponse, Object oViewObject ) throws ViewExecutorException
   {
      // just print the object to the response
      try
      {
         oResponse.getWriter().print( oViewObject );
      }
      catch ( IOException e )
      {
         throw new ViewExecutorException( "View " + oViewObject.getClass().getName() + ", generic object view I/O error", e );
      }
   }
}

// EOF