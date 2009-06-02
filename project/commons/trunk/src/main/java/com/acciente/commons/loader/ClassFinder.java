package com.acciente.commons.loader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
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

   private ClassLoader _oClassLoader;

   public ClassFinder( ClassLoader oClassLoader )
   {
      _oClassLoader = oClassLoader;
   }

   public List find( String[] sPackageNames, Pattern oClassPattern ) throws IOException
   {
      for ( int i = 0; i < sPackageNames.length; i++ )
      {
         String sPackageName = sPackageNames[ i ].replace( '.', '/' );

         Enumeration oResources = _oClassLoader.getResources( sPackageName );

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

                  return findInJar( oJarFile, sPackageName, oClassPattern );
               }
            }
            else if ( sResourceURL.startsWith( URL_PREFIX_FILE ) )
            {
               File oDirectory = new File( sResourceURL.substring( URL_PREFIX_FILE.length() ) );

               System.out.println( "scanning directory: " + oDirectory );

               return findInDirectory( oDirectory, sPackageName, oClassPattern );
            }
         }
      }

      return Collections.EMPTY_LIST;
   }

   private List findInDirectory( File oPath, String sPackageName, Pattern oClassNamePattern )
   {
      List oClassNameList = new LinkedList();

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
                  oClassNameList.addAll( findInDirectory( oFile, oFile.getName(), oClassNamePattern ) );
               }
               else
               {
                  oClassNameList.addAll( findInDirectory( oFile, sPackageName + "." + oFile.getName(), oClassNamePattern ) );
               }
            }
            else if ( oFile.isFile() )
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
                  oClassNameList.add( sClassname );
               }
            }
         }
      }

      return oClassNameList;
   }

   private List findInJar( File oJarFile, String sPackageName, Pattern oClassNamePattern ) throws IOException
   {
      List           oClassNameList;
      ZipFile        oZipFile;
      Enumeration    oZipFileEntries;

      oClassNameList    = new LinkedList();
      oZipFile          = new ZipFile( oJarFile );
      oZipFileEntries   = oZipFile.entries();

      while ( oZipFileEntries.hasMoreElements() )
      {
         ZipEntry oZipFileEntry  = ( ZipEntry ) oZipFileEntries.nextElement();
         String   sClassname     = getClassname( oZipFileEntry );

         if ( ! oZipFileEntry.isDirectory() && sClassname.startsWith( sPackageName ) )
         {
            if ( oClassNamePattern.matcher( sClassname ).matches() )
            {
               oClassNameList.add( sClassname );
            }
         }
      }

      return oClassNameList;
   }

   /**
    * Extracts a class name from a file name
    * @param oFile a file object to extract the class name
    * @return a classname (not fully qualified)
    */
   private String getClassname( File oFile )
   {
      String sFileName = oFile.getName();

      if ( sFileName.endsWith( ".class" ) )
      {
         return sFileName.substring( 0, sFileName.length() - ".class".length() );
      }

      return sFileName;
   }

   /**
    * Extracts a fully qualified class name from a zip file entry
    * @param oZipFileEntry a zip file entry
    * @return a fully qualified classname
    */
   private String getClassname( ZipEntry oZipFileEntry )
   {
      String sFileName = oZipFileEntry.getName();

      if ( sFileName.endsWith( ".class" ) )
      {
         return sFileName.substring( 0, sFileName.length() - ".class".length() );
      }

      return sFileName.replace( '/', '.' );
   }
}