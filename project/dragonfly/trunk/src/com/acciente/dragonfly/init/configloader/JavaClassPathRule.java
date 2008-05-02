package com.acciente.dragonfly.init.configloader;

import org.apache.commons.digester.Rule;
import com.acciente.dragonfly.init.config.Config;

import java.io.File;

/**
 * JavaClassPathRule
 *
 * Log
 * May 1, 2008 APR  -  created
 */
public class JavaClassPathRule extends Rule
{
   private  Config.JavaClassPath    _oJavaClassPath;

   public JavaClassPathRule( Config.JavaClassPath oJavaClassPath )
   {
      _oJavaClassPath = oJavaClassPath;
   }

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
      private File _oDir;
      private  String                  _sPackageNamePrefix;

      public void end( String sNamespace, String sName )
      {
         _oJavaClassPath.addCompiledDir( _oDir, _sPackageNamePrefix );
      }

      public GetDirRule createGetDirRule()
      {
         return new GetDirRule();
      }

      public GetPackageNamePrefixRule createGetPackageNamePrefixRule()
      {
         return new GetPackageNamePrefixRule();
      }

      /**
       * createGetDirRule rule
       */
      private class GetDirRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _oDir = new File( sText );
         }
      }

      /**
       * createGetPackageNamePrefixRule rule
       */
      private class GetPackageNamePrefixRule extends Rule
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
      private  File                    _oDir;
      private  String                  _sPackageNamePrefix;

      public void end( String sNamespace, String sName )
      {
         _oJavaClassPath.addSourceDir( _oDir, _sPackageNamePrefix );
      }

      public GetDirRule createGetDirRule()
      {
         return new GetDirRule();
      }

      public GetPackageNamePrefixRule createGetPackageNamePrefixRule()
      {
         return new GetPackageNamePrefixRule();
      }

      /**
       * createGetDirRule rule
       */
      private class GetDirRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _oDir = new File( sText );
         }
      }

      /**
       * createGetPackageNamePrefixRule rule
       */
      private class GetPackageNamePrefixRule extends Rule
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
      public void body( String sNamespace, String sName, String sText )
      {
         _oJavaClassPath.getJavaCompiler().setJavaCompilerClassName( sText );
      }

   }
}

// EOF