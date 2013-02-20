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
package demoapp.counter_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;

import java.io.IOException;

/**
 * This class ...
 *
 * Log
 * Mar 25, 2008 APR  -  created
 */
public class CounterController implements Controller
{
   public void showCounter( Response response, Counter counter )
      throws IOException
   {
      response.out().println("count is: " + counter.getCount());
   }

   public void incrementCounter( Response response, Counter counter )
      throws IOException
   {
      counter.increment();
      response.out().println("count incremented to: " + counter.getCount());
   }

   public void decrementCounter( Response response, Counter counter )
      throws IOException
   {
      counter.decrement();
      response.out().println("count decremented to: " + counter.getCount());
   }
}

// EOF