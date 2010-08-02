/*
 * Copyright 2010 Acciente, LLC
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

import com.acciente.induction.view.Text;

/**
 * Induction Internal class.
 *
 * @author Adinath Raveendra Raj
 * @created Aug 1, 2010
 */
public class SiteHomePage implements Text
{
   public String getText()
   {
      return "Hello this the home page for the demo site!";
   }

   public String getMimeType()
   {
      return "text/html";
   }
}
