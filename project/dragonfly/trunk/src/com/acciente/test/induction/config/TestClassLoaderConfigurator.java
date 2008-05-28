package com.acciente.test.induction.config;

import com.acciente.induction.init.ClassLoaderInitializer;

import java.io.IOException;

/**
 * Log
 * Mar 18, 2008 APR  -  created
 */
public class TestClassLoaderConfigurator
{
   public static void main( String[] asArgs ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InterruptedException
   {
      System.out.println( "Test - ClassLoaderInitializer" );

      ClassLoader
         oClassLoader
            = ClassLoaderInitializer.getClassLoader( ( new TestConfig_DemoApp() ).getConfig().getJavaClassPath(),
                                                      TestClassLoaderConfigurator.class.getClassLoader(),
                                                      new TestLogger()
                                                    );

      // now have some fun reloading classes
      String[]   sClassNames
         = new String[]{   "demoapp.controller.HelloArizona",
                           "demoapp.controller.HelloWorld",
                           "demoapp.controller.HelloArizona",
                           "demoapp.controller.HelloWorld",
                           //"com.acciente.commons.io.FileUtils",
                       };
      Class    oClass;

      for ( int i = 0; i < sClassNames.length; i++ )
      {
         String sClassName = sClassNames[ i ];

         System.out.println( "Testing w/ class: " + sClassName );

         int j = 2;
         while ( j-- > 0 )
         {
            oClass = oClassLoader.loadClass( sClassName );
            System.out.println( "Class    : " + oClass );
            for ( int k = 0; k < oClass.getClasses().length; k++ )
            {
               if ( k == 0 )
               {
                  System.out.print( "Classes  : " );
               }
               System.out.print( oClass.getClasses()[ k ] );
               if ( k == oClass.getClasses().length - 1 )
               {
                  System.out.println();
               }
            }
            System.out.println( "Hash     : " + oClass.hashCode() );
            System.out.println( "Instance : " + oClass.newInstance() );
            System.out.println( "-------------------" );

            if ( j > 0 )
            {
               Thread.sleep( 10000 );
            }
         }
      }
   }
}

// EOF