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
package com.acciente.induction.dispatcher.view;

/**
 * Internal.
 *
 * @created Apr 26, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ViewExecutorException extends Exception
{
   private  String   _sViewClassName;

   public ViewExecutorException( String sViewClassName, String sMessage )
   {
      super( sMessage );

      _sViewClassName = sViewClassName;
   }

   public ViewExecutorException( String sViewClassName, String sMessage, Throwable oCause )
   {
      super( sMessage, oCause );

      _sViewClassName = sViewClassName;
   }

   public String getViewClassName()
   {
      return _sViewClassName;
   }

   public String getMessage()
   {
      return "View: " + _sViewClassName + ", " + super.getMessage();
   }
}
