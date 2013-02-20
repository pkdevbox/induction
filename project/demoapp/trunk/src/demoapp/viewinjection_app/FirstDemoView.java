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
package demoapp.viewinjection_app;

import com.acciente.induction.controller.Form;
import com.acciente.induction.controller.FormException;
import com.acciente.induction.view.Text;

/**
 * Created by IntelliJ IDEA.
 * User: adinath
 * Date: Jun 27, 2010
 * Time: 9:31:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class FirstDemoView implements Text
{
   private Form _form;

   public FirstDemoView( Form form )
   {
      _form = form;
   }

   public String getText()
   {
      String sName = "Stranger";

      try
      {
         if ( _form.containsParam( "name" ) )
         {
            sName = _form.getString( "name" );
         }
      }
      catch ( FormException e )
      {
         throw new RuntimeException( e );
      }

      return "Hello " + sName + ", I am the FIRST (1st) demo view";
   }

   public String getMimeType()
   {
      return "text/plain";
   }
}
