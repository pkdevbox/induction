/*
 * Copyright 2008-2012 Acciente, LLC
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
package demoapp.errorhandler_app;

/**
 * A sample model class to illustrate what happens when a model constructor (or factory) throws an erro
 *
 * @author Adinath Raveendra Raj
 * @created 4/23/12
 */
public class BuggyModel
{
   public BuggyModel()
   {
      throw new IllegalStateException( "Hello, I am a buggy model, I just throw exceptions" );
   }

   public void print()
   {
      // we will never get here since the constructor above always fails
   }
}
