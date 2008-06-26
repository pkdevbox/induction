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
package com.acciente.induction.init.config.xmlconfigloader;

import com.acciente.commons.lang.Strings;
import com.acciente.induction.init.config.Config;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

/**
 * Internal.
 * ModelDefsRule
 *
 * @created May 3, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ModelDefsRule extends Rule
{
   private  Config.ModelDefs    _oModelDefs;

   public ModelDefsRule( Config.ModelDefs oModelDefs )
   {
      _oModelDefs = oModelDefs;
   }

   public AddModelDefRule createAddModelDefRule()
   {
      return new AddModelDefRule();
   }

   public class AddModelDefRule extends Rule
   {
      private  String   _sModelClassName;
      private  String   _sModelFactoryClassName;
      private boolean   _bIsApplicationScope;
      private boolean   _bIsSessionScope;
      private boolean   _bIsRequestScope;

      public void begin( String sNamespace, String sName, Attributes oAttributes )
      {
         // reset data stored in rule
         _sModelClassName        = null;
         _sModelFactoryClassName = null;
         _bIsApplicationScope    = false;
         _bIsSessionScope        = false;
         _bIsRequestScope        = false;

      }

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( _sModelClassName ) )
         {
            throw new XMLConfigLoaderException( "config > modeldefs > model: class is a required attribute" );
         }
         if ( ! ( _bIsApplicationScope || _bIsSessionScope || _bIsRequestScope ) )
         {
            throw new XMLConfigLoaderException( "config > modeldefs > model: scope is a required attribute" );
         }
         _oModelDefs.addModelDef( _sModelClassName, _sModelFactoryClassName, _bIsApplicationScope, _bIsSessionScope, _bIsRequestScope  );
      }

      public ParamClassRule createParamClassRule()
      {
         return new ParamClassRule();
      }

      public ParamFactoryClassRule createParamFactoryClassRule()
      {
         return new ParamFactoryClassRule();
      }

      public ParamScopeRule createParamScopeRule()
      {
         return new ParamScopeRule();
      }

      private class ParamClassRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sModelClassName = sText;
         }
      }

      private class ParamFactoryClassRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sModelFactoryClassName = sText;
         }
      }

      private class ParamScopeRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
         {
            if ( "application".equalsIgnoreCase( sText ) )
            {
               _bIsApplicationScope = true;
            }
            else if ( "session".equalsIgnoreCase( sText ) )
            {
               _bIsSessionScope     = true;
            }
            else if ( "request".equalsIgnoreCase( sText ) )
            {
               _bIsRequestScope     = true;
            }
            else
            {
               throw new XMLConfigLoaderException( "modeldefs config: unrecognized scope: " + sText + " for model class: " + _sModelClassName );
            }
         }
      }
   }
}

// EOF