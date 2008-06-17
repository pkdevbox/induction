package com.acciente.induction.init.config.xmlconfigloader;

import com.acciente.commons.lang.Strings;
import com.acciente.induction.init.config.Config;
import org.apache.commons.digester.Rule;

import java.io.File;

/**
 * JavaClassPathRule
 *
 * Log
 * May 1, 2008 APR  -  created
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

   public AddSourceDirRule createAddSourceDirRule()
   {
      return new AddSourceDirRule();
   }

   public SetJavaCompilerClassNameRule createSetJavaCompilerClassNameRule()
   {
      return new SetJavaCompilerClassNameRule();
   }

   /**
    * AddCompiledDirRule
    */
   public class AddCompiledDirRule extends Rule
   {
      private  File        _oDir;
      private  String      _sPackageNamePrefix;

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

   /**
    * AddSourceDirRule
    *
    * Log
    * May 1, 2008 APR  -  created
    */
   public class AddSourceDirRule extends Rule
   {
      private  File        _oDir;
      private  String      _sPackageNamePrefix;

      public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
      {
         if ( _oDir == null || Strings.isEmpty( _oDir.getPath() ) )
         {
            throw new XMLConfigLoaderException( "config > java-class-path > source-directory: must specify a directory name" );
         }
         _oJavaClassPath.addSourceDir( _oDir, _sPackageNamePrefix );
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
       * ParamPackageNamePrefixRule rule
       */
      private class ParamPackageNamePrefixRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sPackageNamePrefix = sText;
         }
      }
   }

   /**
    * SetJavaCompilerClassNameRule
    */
   private class SetJavaCompilerClassNameRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         if ( Strings.isEmpty( sText ) )
         {
            throw new XMLConfigLoaderException( "config > java-class-path > java-compiler: must specify a class name" );
         }
         _oJavaClassPath.getJavaCompiler().setJavaCompilerClassName( sText );
      }
   }
}

// EOF