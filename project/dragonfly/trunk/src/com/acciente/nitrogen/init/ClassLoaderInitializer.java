package com.acciente.nitrogen.init;

import com.acciente.commons.javac.JavaCompilerManager;
import com.acciente.commons.loader.JavaCompiledClassDefLoader;
import com.acciente.commons.loader.JavaSourceClassDefLoader;
import com.acciente.commons.loader.ReloadingClassLoader;
import com.acciente.nitrogen.init.config.Config;

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
      // we start with the parent classloader, if no other class loader config are defined
      // we will end up using this as our class loader, otherwise we will "chain" the user
      // configured classloaders to this one
      ClassLoader oClassLoader = oParentClassLoader;

      if ( oJavaClassPathConfig.getDirList().size() != 0 )
      {
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

               // chain in the new classloader
               oClassLoader = new ReloadingClassLoader( oJavaCompiledClassDefLoader, oClassLoader );
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

               // chain in the new classloader
               oClassLoader = new ReloadingClassLoader( oJavaSourceClassDefLoader, oClassLoader );
            }
         }
      }

      return oClassLoader;
   }
}

// EOF
