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
package demoapp.urlresolver_app;

import com.acciente.induction.dispatcher.resolver.URLResolver;
import com.acciente.induction.view.Template;
import demoapp.helloworld1_app.HelloWorld1Controller;

/**
 * Created by IntelliJ IDEA.
 * User: adinath
 * Date: Jun 13, 2010
 * Time: 9:36:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class URLResolverView implements Template
{
   private URLResolver _urlLResolver;

   public URLResolverView( URLResolver urlResolver )
   {
      _urlLResolver = urlResolver;
   }

   public String getTemplateName()
   {
      return "URLResolverView.ftl";
   }

   public String getMimeType()
   {
      return "text/html";
   }

   public String getHelloWorld1URL()
   {
      return _urlLResolver.resolve( HelloWorld1Controller.class );
   }
}
