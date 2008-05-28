package com.acciente.induction.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Log
 * Feb 18, 2008 APR  -  created
 */
public class HttpRequest extends HttpServletRequestWrapper implements Request
{
   private HttpServletRequest _oRequest;

   public HttpRequest( HttpServletRequest oRequest )
   {
      super( oRequest );
      _oRequest = oRequest;
   }
}

// EOF