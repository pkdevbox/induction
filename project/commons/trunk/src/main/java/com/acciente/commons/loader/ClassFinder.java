package com.acciente.commons.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * ClassFinder
 *
 * @author Adinath Raveendra Raj
 * @created May 25, 2009
 */
public class ClassFinder
{
   private ClassLoader _oClassLoader;

   public ClassFinder( ClassLoader oClassLoader )
   {
      _oClassLoader = oClassLoader;
   }

   public List<String> find( String[] sPackageNames, Pattern oClassPattern ) throws IOException
   {
      for ( int i = 0; i < sPackageNames.length; i++ )
      {
         String sPackageName = sPackageNames[ i ].replace( '.', '/' );

         Enumeration oResources = _oClassLoader.getResources( sPackageName );

         while ( oResources.hasMoreElements() )
         {
            URL sResourceURL = ( URL ) oResources.nextElement();

            System.out.println( "resource=" + sResourceURL );
         }
      }


      return null;
   }
}
