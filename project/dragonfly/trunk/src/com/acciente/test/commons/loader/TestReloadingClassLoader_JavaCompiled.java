package com.acciente.test.commons.loader;

import com.acciente.commons.loader.JavaCompiledClassDefLoader;
import com.acciente.commons.loader.ReloadingClassLoader;

import java.io.File;
import java.io.IOException;

/**
 * Log
 * Feb 29, 2008 APR  -  created
 */
public class TestReloadingClassLoader_JavaCompiled
{
   public static void main( String[] asArgs ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InterruptedException
   {
      System.out.println( "ReloadingClassLoader_JavaCompiled - test" );

      // setup a class definition loader that knows how find/compile java source files
      JavaCompiledClassDefLoader oJavaCompiledClassDefLoader = new JavaCompiledClassDefLoader();
      oJavaCompiledClassDefLoader.setCompiledDirectory( new File( "./" ) );
      //oJavaCompiledClassDefLoader.setPackageNamePrefix( "demoapp.controller" );

      // create a reloading class loader, the class loader uses the classdef loader we provide
      // the class loader is unaware of the origination of the classes it loads, in this case
      // source files
      ReloadingClassLoader oReloadingClassLoader;

      oReloadingClassLoader = new ReloadingClassLoader();
      oReloadingClassLoader.addClassDefLoader( oJavaCompiledClassDefLoader );
      //oReloadingClassLoader = TestReloadingClassLoader_JavaCompiled.class.getClassLoader();

      // now have some fun reloading classes
      String[]   sClassNames
         = new String[]{   "demoapp.helloworld3_app.HelloWorldController",
                       };
      Class    oClass;

      for ( int i = 0; i < sClassNames.length; i++ )
      {
         String sClassName = sClassNames[ i ];

         System.out.println( "Testing w/ class: " + sClassName );

         int j = 1;
         while ( j-- > 0 )
         {
            oClass = oReloadingClassLoader.loadClass( sClassName, true );
            System.out.println( "Class object     : " + oClass );
            System.out.println( "Class hash       : " + oClass.hashCode() );
            System.out.println( "Created instance : " + oClass.newInstance() );

            if ( j > 0 )
            {
               Thread.sleep( 6000 );
            }
         }
      }
   }
}
