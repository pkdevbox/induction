/*
 * Copyright 2008-2011 Acciente, LLC
 *
 * Acciente, LLC licenses this file to you under the
 * Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */
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
