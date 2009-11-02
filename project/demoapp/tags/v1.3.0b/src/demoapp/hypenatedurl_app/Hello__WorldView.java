/**
 *   Copyright 2008 Acciente, LLC
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
package demoapp.hypenatedurl_app;

import com.acciente.induction.view.Text;

/**
 * The demoapp configuration XML is setup to map the double underscore in the to a hyphen
 * so this view will only respond to .../hello-world
 */
public class Hello__WorldView implements Text
{
   public String getText()
   {
      return "Hello I am responding to a hyphenated URL using the new <class-replace/> feature in Induction!";
   }

   public String getMimeType()
   {
      return "text/plain";
   }
}