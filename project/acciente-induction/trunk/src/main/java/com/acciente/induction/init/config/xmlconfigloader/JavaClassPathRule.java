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
package com.acciente.induction.init.config.xmlconfigloader;

import com.acciente.commons.lang.Strings;
import com.acciente.induction.init.config.Config;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

import java.io.File;

/**
 * Internal.
 * JavaClassPathRule
 *
 * @created May 1, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class JavaClassPathRule
{
   private  Config.JavaClassPath    _oJavaClassPath;

   public JavaClassPathRule( Config.JavaClassPath oJavaClassPath )
   {
      _oJavaClassPath = oJavaClassPath;
   }

   /**
    * Factory methods for the "nested"-rules
    */

   public AddCompiledDirRule createAddCompiledDirRule()
   {
      return new AddCompiledDirRule();
   }

   /**
    * AddCompiledDirRule
    */
   public class AddCompiledDirRule extends Rule
   {
      private  File        _oDir;
      private  String      _sPackageNamePrefix;

      public void begin( String sNamespace, String sName, Attributes oAttributes )
      {
         // reset data stored in rule
         _oDir                = null;
         _sPackageNamePrefix  = null;
      }

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( _oDir == null || Strings.isEmpty( _oDir.getPath() ) )
         {
            throw new XMLConfigLoaderException( "config > java-class-path > compiled-directory: must specify a directory name" );
         }
         _oJavaClassPath.addCompiledDir( _oDir, _sPackageNamePrefix );
      }

      /**
       * Factory methods for the "nested"-rules
       */

      public ParamDirRule createParamDirRule()
      {
         return new ParamDirRule();
      }

      public ParamPackageNamePrefixRule createParamPackageNamePrefixRule()
      {
         return new ParamPackageNamePrefixRule();
      }

      /**
       * ParamDirRule
       */
      private class ParamDirRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _oDir = new File( sText );
         }
      }

      /**
       * ParamPackageNamePrefixRule
       */
      private class ParamPackageNamePrefixRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sPackageNamePrefix = sText;
         }
      }
   }
}

// EOF