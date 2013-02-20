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
package demoapp.models_app;

/**
 * This is also a simple model class, but in this case we illustrate the use of a model factory.<p>
 * <p>
 * The BarModel constructor requires the current time as a long, since Induction will not be able to
 * infer this injection we will use our factory class to do it.
 *
 * @created Jun 20, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class BarModel
{
   public BarModel( long currentTime )
   {
      System.out.println( "BarModel: constructor called @time: " + currentTime );
   }
}

// EOF