package com.acciente.induction.dispatcher.redirect;

import javax.servlet.http.HttpServletResponse;

/**
 * RedirectExecutor
 *
 * @author Adinath Raveendra Raj
 * @created Nov 30, 2009
 */
public class RedirectExecutor
{
   public static void redirect( HttpServletResponse oResponse, String sRedirectURL )
      throws RedirectExecutorException
   {
      try
      {
         oResponse.sendRedirect( sRedirectURL );
      }
      catch ( Exception e )
      {
         throw new RedirectExecutorException( "exec: Error during sendRedirect( ... ) >", e );
      }
   }
}
