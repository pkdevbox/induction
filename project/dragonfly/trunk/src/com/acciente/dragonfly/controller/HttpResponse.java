package com.acciente.dragonfly.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Log
 * Feb 18, 2008 APR  -  created
 */
public class HttpResponse extends HttpServletResponseWrapper implements Response
{
   private  HttpServletResponse  _oResponse;

   public HttpResponse( HttpServletResponse oResponse )
   {
      super( oResponse );
      _oResponse = oResponse;
   }

   public PrintWriter out() throws IOException
   {
      return _oResponse.getWriter();
   }
}

// EOF