/*
 * Copyright 2008-2013 Acciente, LLC
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
package demoapp.resolvers_app;

import com.acciente.induction.init.config.Config;
import com.acciente.induction.resolver.ShortURLViewResolver;
import com.acciente.induction.resolver.ViewResolver;
import demoapp.SiteHomePage;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This is a simple example to illustrate how to write a simple custom view resolver. This example also illustrates
 * how the a resolver can leverage the built-in short URL resolvers and build on there capabilities.
 *
 * @author Adinath Raveendra Raj
 * @created Aug 1, 2010
 */
public class MyCustomViewResolver implements ViewResolver
{
   private ShortURLViewResolver _shortURLViewResolver;

   public MyCustomViewResolver( Config.ViewMapping viewMapping, ClassLoader classLoader ) throws IOException
   {
      _shortURLViewResolver = new ShortURLViewResolver( viewMapping, classLoader );
   }

   public Resolution resolveRequest( HttpServletRequest request )
   {
      // if the request only specifies a hostname or just contains the root path, then
      // resolve to our custom home page
      if ( request.getPathInfo() == null || request.getPathInfo().equals( "/" ) )
      {
         return new Resolution( SiteHomePage.class.getName() );
      }

      // otherwise just delegate to the standard short url resolver
      return _shortURLViewResolver.resolveRequest( request );
   }
}
