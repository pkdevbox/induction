package com.acciente.dragonfly.init;

import com.acciente.commons.javac.JavaCompilerManager;
import com.acciente.commons.loader.JavaCompiledClassDefLoader;
import com.acciente.commons.loader.JavaSourceClassDefLoader;
import com.acciente.commons.loader.ReloadingClassLoader;
import com.acciente.dragonfly.init.config.Config;

/**
 * This is helper class that focuses on setting up the primary classloader used
 * by the dispatcher servlet.
 *
 * Log
 * Mar 15, 2008 APR  -  created
 */
public class ClassLoaderInitializer
{
   public static ClassLoader getClassLoader( Config.JavaClassPath oJavaClassPathConfig, ClassLoader oParentClassLoader, Logger oLogger )
      throws ClassNotFoundException
   {
      if ( oJavaClassPathConfig.getDirList().size() == 0 )
      {
         return oParentClassLoader;
      }
      else
      {
         ReloadingClassLoader oClassLoader = new ReloadingClassLoader( oParentClassLoader );

         // if there is a classpath defined setup a reloading classloader to handle the specified directories
         for ( int i = 0; i < oJavaClassPathConfig.getDirList().size(); i++ )
         {
            if ( oJavaClassPathConfig.getDirList().get( i ) instanceof Config.JavaClassPath.CompiledDir )
            {
               Config.JavaClassPath.CompiledDir oCompiledDir = ( Config.JavaClassPath.CompiledDir ) oJavaClassPathConfig.getDirList().get( i );

               oLogger.log( "configuring reloading classloader for compiled classes in: " + oCompiledDir.getDir() );

               // set up a compiled class definition loader
               JavaCompiledClassDefLoader oJavaCompiledClassDefLoader = new JavaCompiledClassDefLoader();
               oJavaCompiledClassDefLoader.setCompiledDirectory( oCompiledDir.getDir() );
               oJavaCompiledClassDefLoader.setPackageNamePrefix( oCompiledDir.getPackageNamePrefix() );

               // add the class def loader to the search list
               oClassLoader.addClassDefLoader( oJavaCompiledClassDefLoader );
            }
            else if ( oJavaClassPathConfig.getDirList().get( i ) instanceof Config.JavaClassPath.SourceDir )
            {
               Config.JavaClassPath.SourceDir oSourceDir = ( Config.JavaClassPath.SourceDir ) oJavaClassPathConfig.getDirList().get( i );

               oLogger.log( "configuring reloading classloader for source classes in  : " + oSourceDir.getDir() );

               // set up a source class definition loader
               JavaSourceClassDefLoader oJavaSourceClassDefLoader = new JavaSourceClassDefLoader();

               oJavaSourceClassDefLoader.setJavaCompilerManager( new JavaCompilerManager( oJavaClassPathConfig.getJavaCompiler().getJavaCompilerClassName() ) );
               oJavaSourceClassDefLoader.setSourceDirectory( oSourceDir.getDir() );
               oJavaSourceClassDefLoader.setPackageNamePrefix( oSourceDir.getPackageNamePrefix() );

               // add the class def loader to the search list
               oClassLoader.addClassDefLoader( oJavaSourceClassDefLoader );
            }
         }

         return oClassLoader;
      }
   }
}

// EOF
