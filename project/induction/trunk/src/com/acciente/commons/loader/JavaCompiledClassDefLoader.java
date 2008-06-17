package com.acciente.commons.loader;

import java.io.File;

/**
 * Log
 * Feb 29, 2008 APR  -  created
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
         throw new IllegalArgumentException( "class definition loader requires a classname, none specified!" );
      }

      File oCompiledFile = new File( _oCompiledDirectory, getFileNameFromClassName( sClassName ) );

      JavaCompiledClassDef oClassDef = null;

      if ( oCompiledFile.exists() )
      {
         oClassDef = new JavaCompiledClassDef( sClassName, oCompiledFile );
      }

      return oClassDef;
   }

   private String getFileNameFromClassName( String sClassName )
   {
      String   sFilename;

      if ( _sPackageNamePrefix == null )
      {
         sFilename = sClassName.replace( '.', '/' ) + ".class";
      }
      else
      {
         // first check if the classname is ok
         if ( ! sClassName.startsWith( _sPackageNamePrefix ) )
         {
            throw new IllegalArgumentException( "class definition loader is restricted to classnames that start with: " + _sPackageNamePrefix + ", classname: " + sClassName );
         }

         if ( sClassName.equals( _sPackageNamePrefix ) )
         {
            throw new IllegalArgumentException( "specified classname is identical to package name prefix!" );
         }

         sFilename = sClassName.substring( _sPackageNamePrefix.length() + 1 ).replace( '.', '/' ) + ".class";
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
