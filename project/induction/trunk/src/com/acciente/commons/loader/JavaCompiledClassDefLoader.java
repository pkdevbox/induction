/**
 *   Copyright 2009 Acciente, LLC
 *
 *   Acciente, LLC licenses this file to you under the
 *   Apache License, Version 2.0 (the "License"); you
 *   may not use this file except in compliance with the
 *   License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in
 *   writing, software distributed under the License is
 *   distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 *   OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing
 *   permissions and limitations under the License.
 */
package com.acciente.commons.loader;

import java.io.File;

/**
 * A class definition loader that loads compiled Java class files.
 *
 * @created Feb 29, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class JavaCompiledClassDefLoader implements ClassDefLoader
{
   private File      _oCompiledDirectory;
   private String    _sPackageNamePrefix;

   public JavaCompiledClassDefLoader()
   {
   }

   public JavaCompiledClassDefLoader( File oCompiledDirectory )
   {
      _oCompiledDirectory = oCompiledDirectory;
   }

   public ClassDef getClassDef( String sClassName ) throws ClassNotFoundException
   {
      if ( sClassName == null )
      {
         throw new IllegalArgumentException( "class definition loader requires a class name, none specified!" );
      }

      File oCompiledFile = new File( _oCompiledDirectory, getFileName( sClassName, false ) );

      JavaCompiledClassDef oClassDef = null;

      if ( oCompiledFile.exists() )
      {
         oClassDef = new JavaCompiledClassDef( sClassName, oCompiledFile );
      }

      return oClassDef;
   }

   public ResourceDef getResourceDef( String sResourceName )
   {
      if ( sResourceName == null )
      {
         throw new IllegalArgumentException( "class definition loader requires a resource name, none specified!" );
      }

      File oResourceFile = new File( _oCompiledDirectory, getFileName( sResourceName, true ) );

      FileResourceDef oClassDef = null;

      if ( oResourceFile.exists() )
      {
         oClassDef = new FileResourceDef( sResourceName, oResourceFile );
      }

      return oClassDef;
   }

   private String getFileName( String sClassOrResourceName, boolean bIsResourceName )
   {
      String   sFilename;

      if ( _sPackageNamePrefix == null )
      {
         if ( bIsResourceName )
         {
            sFilename = sClassOrResourceName;
         }
         else
         {
            sFilename = sClassOrResourceName.replace( '.', '/' ) + ".class";
         }
      }
      else
      {
         // first check if the classname is ok
         if ( ! sClassOrResourceName.startsWith( _sPackageNamePrefix ) )
         {
            throw new IllegalArgumentException( "class definition loader is restricted to "
                                                + ( bIsResourceName ? "resource names" : "class names" )
                                                + " that start with: "
                                                + _sPackageNamePrefix
                                                + ", "
                                                + ( bIsResourceName ? "resource name" : "class name" )
                                                + ": "
                                                + sClassOrResourceName );
         }

         if ( sClassOrResourceName.equals( _sPackageNamePrefix ) )
         {
            throw new IllegalArgumentException( "specified "
                                                + ( bIsResourceName ? "resource name" : "class name" )
                                                + " is identical to package name prefix!" );
         }

         if ( bIsResourceName )
         {
            sFilename = sClassOrResourceName;
         }
         else
         {
            sFilename = sClassOrResourceName.substring( _sPackageNamePrefix.length() + 1 ).replace( '.', '/' ) + ".class";
         }
      }

      return sFilename;
   }

   /**
    * Returns the compiled directory from which this classloader loads java compiled files
    *
    * @return a File object representing a directory path
    */
   public File getCompiledDirectory()
   {
      return _oCompiledDirectory;
   }

   /**
    * Sets the directory from which this classloader will attempt to java compiled
    * files
    *
    * @param sCompiledDirectory  a File object representing a directory path
    */
   public void setCompiledDirectory( File sCompiledDirectory )
   {
      _oCompiledDirectory = sCompiledDirectory;
   }

   /**
    * Returns the package name prefix set by a call to setPackageNamePrefix() or
    * a null string if no package name prefix was ever set.
    *
    * @return null or a package name without a terminating period (e.g: foo.bar and NOT foo.bar.)
    */
   public String getPackageNamePrefix()
   {
      return _sPackageNamePrefix;
   }

   /**
    * A package name prefix that represents the prefix that should be removed from
    * the class name passed to loadClass() before attempting to convert the class's
    * package name to a sub-directory off the specified compiled directory.
    *
    * For consistency this setting also causes the class loader to enfore that all
    * the package name of classes loaded by this classloader start with the specified
    * package name prefix.
    *
    * Following is an illustration of how is parameter may be used. Assume we have
    * compiled files in a directory /project/foobar/src, lets also assume that we want to
    * reliably restrict this class loader to only load compiled files in the folder
    * /project/foobar/src/foobar/controller. So for so security we set the compiled
    * directory of this classoader to /project/foobar/src/foobar/controller.
    *
    * Now if we ask this classloader to load the class foobar.controller.HelloWorld
    * it would (unsuccessfully) try to look for a compiled file named:
    * "/project/foobar/src/foobar/controller/foobar/controller/HelloWorld.class"
    * computed using:
    * CompiledDir="/project/foobar/src/foobar/controller"
    * + InferredCompiledFilePath="/foobar/controller/HelloWorld.class"
    *
    * To get the correct behaviour in the above example the PackageNamePrefix of this
    * class loader must be set to "foobar.controller". The prefix "foobar.controller"
    * is then removed from the package name of class before using it to compute the
    * compiled file path for the class foobar.controller.HelloWorld.
    *
    * @param sPackageNamePrefix null or a package name without a terminating period (e.g: foo.bar and NOT foo.bar.)
    */
   public void setPackageNamePrefix( String sPackageNamePrefix )
   {
      if ( sPackageNamePrefix != null && sPackageNamePrefix.endsWith( "." ) )
      {
         throw new IllegalArgumentException( "the package name prefix should be not be terminated by a period" );
      }
      _sPackageNamePrefix = sPackageNamePrefix;
   }
}

// EOF