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
package com.acciente.induction.dispatcher.redirect;

import com.acciente.induction.controller.Redirect;
import com.acciente.induction.resolver.RedirectResolver;

/**
 * Internal.
 *
 * RedirectResolverFacade, this class wraps the RedirectResolver making it easier to use.
 *
 * @author Adinath Raveendra Raj
 * @created Apr 1, 2009
 */
public class RedirectResolverFacade
{
   private  RedirectResolver   _oRedirectResolver;

   public RedirectResolverFacade( RedirectResolver oRedirectResolver )
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
