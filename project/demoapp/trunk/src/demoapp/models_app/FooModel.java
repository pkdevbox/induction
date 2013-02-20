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
 * This is a simple model class. This model illustrates how Induction can create instances of
 * a model by calling the model's public ocnstructor (no factory class is needed).<p>
 * <p>
 * This model also illustrates <b>model-to-model injection<b>, the instance of BarModel
 * required by the FooModel constructor below is automatically injected since BarModel
 * is also a known model class declared in the Induction configuration XML.
 *
 * Log
 * Jun 20, 2008 APR  -  created
 */
public class FooModel
{
   private BarModel _barModel;

   public FooModel( BarModel barModel )
   {
      _barModel = barModel;

      System.out.println( "FooModel: constructor called @time: " + System.currentTimeMillis() );
   }

   public BarModel getBarModel()
   {
      return _barModel;
   }
}

// EOF