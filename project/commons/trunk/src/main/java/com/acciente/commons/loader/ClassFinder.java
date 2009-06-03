package com.acciente.commons.loader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ClassFinder
 *
 * @author Adinath Raveendra Raj
 * @created May 25, 2009
 */
public class ClassFinder
{
   private static final String URL_PREFIX_FILE     = "file:/";
   private static final String URL_PREFIX_JARFILE  = "jar:file:/";
   private static final String URL_DELIM_JARFILE   = ".jar!/";

   public static Set find( ClassLoader oClassLoader, String[] asPackageNames, Pattern oClassPattern ) throws IOException
   {
      Set oClassNameSet = new HashSet();

      for ( int i = 0; i < asPackageNames.length; i++ )
      {
         String sPackageName = asPackageNames[ i ];

         Enumeration oResources = oClassLoader.getResources( sPackageName.replace( '.', '/' ) );

         while ( oResources.hasMoreElements() )
         {
            String   sResourceURL = URLDecoder.decode( ( ( URL ) oResources.nextElement() ).toString(), "UTF-8" );

            if ( sResourceURL.startsWith( URL_PREFIX_JARFILE ) )
            {
               int iDelimPos = sResourceURL.indexOf( URL_DELIM_JARFILE );

               if ( iDelimPos != -1 )
               {
                  File oJarFile = new File( sResourceURL.substring( URL_PREFIX_JARFILE.length(), iDelimPos + 4 ) );

                  System.out.println( "scanning jar: " + oJarFile );

                  findInJar( oJarFile, sPackageName, oClassPattern, oClassNameSet );
               }
            }
            else if ( sResourceURL.startsWith( URL_PREFIX_FILE ) )
            {
               File oDirectory = new File( sResourceURL.substring( URL_PREFIX_FILE.length() ) );

               System.out.println( "scanning directory: " + oDirectory );

               findInDirectory( oDirectory, sPackageName, oClassPattern, oClassNameSet );
            }
         }
      }

      // if the classloader is a ReloadingClassLoader the above loop would have processed the classed reachable via
      // the parent classloader, now we process the classes reachable via this classloader
      if ( oClassLoader instanceof ReloadingClassLoader )
      {
         ReloadingClassLoader oReloadingClassLoader = ( ReloadingClassLoader ) oClassLoader;

         for ( Iterator oClassDefLoaderIter = oReloadingClassLoader.getClassDefLoaders().iterator(); oClassDefLoaderIter.hasNext(); )
         {
            ClassDefLoader oClassDefLoader = ( ClassDefLoader ) oClassDefLoaderIter.next();

            Set oAddClassNameSet = oClassDefLoader.findClassNames( asPackageNames, oClassPattern );

            oClassNameSet.addAll( oAddClassNameSet );
         }
      }

      return oClassNameSet;
   }

   private static Set findInDirectory( File oPath, String sPackageName, Pattern oClassNamePattern, Set oClassNameSet )
   {
      File[] oFileList = oPath.listFiles();

      if ( oFileList != null )
      {
         // iterate thru the classes in this directory
         for ( int i = 0; i < oFileList.length; i++ )
         {
            File oFile = oFileList[ i ];

            if ( oFile.isDirectory() )
            {
               if ( sPackageName == null )
               {
                  findInDirectory( oFile, oFile.getName(), oClassNamePattern, oClassNameSet );
               }
               else
               {
                  findInDirectory( oFile, sPackageName + "." + oFile.getName(), oClassNamePattern, oClassNameSet );
               }
            }
            else if ( oFile.isFile() && oFile.getName().endsWith( ".class" ) )
            {
               String   sClassname;

               if ( sPackageName == null )
               {
                  sClassname = getClassname( oFile );
               }
               else
               {
                  sClassname = sPackageName + "." + getClassname( oFile );
               }

               if ( oClassNamePattern.matcher( sClassname ).matches() )
               {
                  oClassNameSet.add( sClassname );
               }
            }
         }
      }

      return oClassNameSet;
   }

   private static Set findInJar( File oJarFile, String sPackageName, Pattern oClassNamePattern, Set oClassNameSet ) throws IOException
   {
      ZipFile        oZipFile;
      Enumeration    oZipFileEntries;

      oZipFile          = new ZipFile( oJarFile );
      oZipFileEntries   = oZipFile.entries();

      while ( oZipFileEntries.hasMoreElements() )
      {
         ZipEntry oZipFileEntry  = ( ZipEntry ) oZipFileEntries.nextElement();
         String   sClassname     = getFQClassname( oZipFileEntry );

         if ( ! oZipFileEntry.isDirectory() && sClassname.startsWith( sPackageName ) )
         {
            if ( oClassNamePattern.matcher( sClassname ).matches() )
            {
               oClassNameSet.add( sClassname );
            }
         }
      }

      return oClassNameSet;
   }

   /**
    * Extracts a class name from a file name
    * @param oFile a file object to extract the class name
    * @return a classname (not fully qualified)
    */
   private static String getClassname( File oFile )
   {
      String sFileName = oFile.getName();

      if ( sFileName.endsWith( ".class" ) )
      {
         sFileName = sFileName.substring( 0, sFileName.length() - ".class".length() );
      }

      return sFileName;
   }

   /**
    * Extracts a fully qualified class name from a zip file entry
    * @param oZipFileEntry a zip file entry
    * @return a fully qualified classname
    */
   private static String getFQClassname( ZipEntry oZipFileEntry )
   {
      String sFileName = oZipFileEntry.getName();

      if ( sFileName.endsWith( ".class" ) )
      {
         sFileName = sFileName.substring( 0, sFileName.length() - ".class".length() );
      }

      return sFileName.replace( '/', '.' );
   }
}