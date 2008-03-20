package com.acciente.test.commons.loader;

import com.acciente.commons.javac.JavaCompilerManager;
import com.acciente.commons.loader.JavaSourceClassDefLoader;
import com.acciente.commons.loader.ReloadingClassLoader;

import java.io.File;
import java.io.IOException;

/**
 * Log
 * Feb 24, 2008 APR  -  created
 */
public class TestReloadingClassLoader_JavaSource
{
   public static void main( String[] asArgs ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InterruptedException
   {
      System.out.println( "ReloadingClassLoader_JavaSource - test" );

      // setup a class definition loader that knows how find/compile java source files
      JavaSourceClassDefLoader oJavaSourceClassDefLoader = new JavaSourceClassDefLoader();
      oJavaSourceClassDefLoader.setJavaCompilerManager( new JavaCompilerManager( "com.acciente.commons.javac.JavaCompiler_JDK_1_4" ) );
      oJavaSourceClassDefLoader.setSourceDirectory( new File( "../demoapp/src/demoapp/controller/" ) );
      oJavaSourceClassDefLoader.setPackageNamePrefix( "demoapp.controller" );

      // create a reloading class loader, the class loader uses the classdef loader we provide
      // the class loader is unaware of the origination of the classes it loads, in this case
      // source files
      ReloadingClassLoader oReloadingClassLoader = new ReloadingClassLoader( oJavaSourceClassDefLoader );

      // now have some fun reloading classes
      String[]   sClassNames
         = new String[]{   //"com.acciente.commons.io.FileUtils",
                           "demoapp.controller.HelloArizona",
                           "demoapp.controller.HelloWorld" };
      Class    oClass;

      for ( int i = 0; i < sClassNames.length; i++ )
      {
         String sClassName = sClassNames[ i ];

         System.out.println( "Testing w/ class: " + sClassName );

         int j = 4;
         while ( j-- > 0 )
         {
            oClass = oReloadingClassLoader.loadClass( sClassName );
            System.out.println( "Class object     : " + oClass );
            System.out.println( "Class hash       : " + oClass.hashCode() );
            System.out.println( "Created instance : " + oClass.newInstance() );

            if ( j > 0 )
            {
               Thread.sleep( 4000 );
            }
         }
      }
   }
}

// EOF