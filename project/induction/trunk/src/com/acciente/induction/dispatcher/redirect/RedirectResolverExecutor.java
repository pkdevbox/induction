package com.acciente.induction.dispatcher.redirect;

import com.acciente.induction.controller.Redirect;
import com.acciente.induction.resolver.RedirectResolver;

/**
 * Internal.
 *
 * RedirectResolverExecutor, this class wraps the RedirectResolver making it easier to use.
 *
 * @author Adinath Raveendra Raj
 * @created Apr 1, 2009
 */
public class RedirectResolverExecutor
{
   private RedirectResolver   _oRedirectResolver;

   public RedirectResolverExecutor( RedirectResolver oRedirectResolver )
   {
      _oRedirectResolver = oRedirectResolver;
   }

   public String resolve( Redirect oRedirect )
   {
      if ( oRedirect.getTargetClass() != null )
      {
         if ( oRedirect.getTargetMethodName() != null )
         {
            if ( oRedirect.getTargetURLQueryParameters() != null )
            {
               return _oRedirectResolver.resolve( oRedirect.getTargetClass(),
                                                  oRedirect.getTargetMethodName(),
                                                  oRedirect.getTargetURLQueryParameters() );
            }
            else
            {
               return _oRedirectResolver.resolve( oRedirect.getTargetClass(),
                                                  oRedirect.getTargetMethodName() );
            }
         }
         else
         {
            if ( oRedirect.getTargetURLQueryParameters() != null )
            {
               return _oRedirectResolver.resolve( oRedirect.getTargetClass(), oRedirect.getTargetURLQueryParameters() );
            }
            else
            {
               return _oRedirectResolver.resolve( oRedirect.getTargetClass() );
            }
         }
      }
      else if ( oRedirect.getTargetURL() != null )
      {
         if ( oRedirect.getTargetURLQueryParameters() != null )
         {
            return _oRedirectResolver.resolve( oRedirect.getTargetURL(), oRedirect.getTargetURLQueryParameters() );
         }
         else
         {
            return _oRedirectResolver.resolve( oRedirect.getTargetURL() );
         }
      }

      return null;
   }
}
