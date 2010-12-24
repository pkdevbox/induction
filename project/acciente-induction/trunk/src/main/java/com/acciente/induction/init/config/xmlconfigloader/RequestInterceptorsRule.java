/*
 * Copyright 2008-2010 Acciente, LLC
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
package com.acciente.induction.init.config.xmlconfigloader;

import com.acciente.commons.lang.Strings;
import com.acciente.induction.init.config.Config;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

/**
 * Internal.
 * RequestInterceptorsRule
 *
 * @created Aug 30, 2009
 *
 * @author Adinath Raveendra Raj
 */
public class RequestInterceptorsRule extends Rule
{
   private  Config.RequestInterceptors    _oRequestInterceptors;

   public RequestInterceptorsRule( Config.RequestInterceptors oRequestInterceptors )
   {
      _oRequestInterceptors = oRequestInterceptors;
   }

   public AddRequestInterceptorRule createAddRequestInterceptorRule()
   {
      return new AddRequestInterceptorRule();
   }

   public class AddRequestInterceptorRule extends Rule
   {
      private String       _sClassName;

      public void begin( String sNamespace, String sName, Attributes oAttributes )
      {
         // reset data stored in rule
         _sClassName = null;
      }

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( _sClassName ) )
         {
            throw new XMLConfigLoaderException( "config > request-interceptors > requestinterceptor: class is a required attribute" );
         }

         _oRequestInterceptors.addRequestInterceptor( _sClassName );
      }

      public ParamClassRule createParamClassRule()
      {
         return new ParamClassRule();
      }

      private class ParamClassRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sClassName = sText;
         }
      }
   }
}