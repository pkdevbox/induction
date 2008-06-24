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

/**
 * Internal.
 * ControllerResolverRule
 *
 * NOTE: we do not extend Rule in this class, since this class while a rules "container",
 * but is not itself a rule
 *
 * @created May 8, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ControllerResolverRule extends Rule
{
   private  Config.ControllerResolver  _oControllerResolver;

   private  String                     _sClassName;
   private  String                     _sDefaultHandlerMethodName;
   private  Boolean                    _oIgnoreMethodNameCase;

   public ControllerResolverRule( Config.ControllerResolver oControllerResolver )
   {
      _oControllerResolver = oControllerResolver;
   }

   public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
   {
      if ( Strings.isEmpty( _sClassName ) )
      {
         throw new XMLConfigLoaderException( "config > controller-resolver > class: must specify a class name" );
      }

      _oControllerResolver.setClassName( _sClassName );

      if ( _oIgnoreMethodNameCase != null )
      {
         _oControllerResolver.setIgnoreMethodNameCase( _oIgnoreMethodNameCase.booleanValue() );
      }

      if ( ! Strings.isEmpty( _sDefaultHandlerMethodName ) )
      {
         _oControllerResolver.setDefaultHandlerMethodName( _sDefaultHandlerMethodName );
      }
   }

   public ParamClassRule createParamClassRule()
   {
      return new ParamClassRule();
   }

   public ParamDefaultHandlerMethodRule createParamDefaultHandlerMethodRule()
   {
      return new ParamDefaultHandlerMethodRule();
   }

   public ParamIgnoreHandlerMethodCaseRuleRule createParamIgnoreHandlerMethodCaseRule()
   {
      return new ParamIgnoreHandlerMethodCaseRuleRule();
   }

   private class ParamClassRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         _sClassName = sText;
      }
   }

   private class ParamDefaultHandlerMethodRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( sText ) )
         {
            throw new XMLConfigLoaderException( "config > controller-resolver > default-handler-method: must specify a valid method name" );
         }
         _sDefaultHandlerMethodName = sText;
      }
   }

   private class ParamIgnoreHandlerMethodCaseRuleRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( sText ) )
         {
            throw new XMLConfigLoaderException( "config > controller-resolver > ignore-handler-method-case: must specify a boolean value, specify true or false" );
         }
         _oIgnoreMethodNameCase = Boolean.valueOf( sText );
      }
   }
}

// EOF