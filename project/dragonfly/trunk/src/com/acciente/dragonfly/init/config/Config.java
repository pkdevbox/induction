package com.acciente.dragonfly.init.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores all the configuration for a single dispatcher instance.
 * We use inner classes in this class to represent each type of configuration information,
 * sometimes there may be two identical looking inner classes but this is done since though
 * identical the two classes represent different concepts and as such may diverge in the future.
 *
 * Log
 * Feb 29, 2008 APR  -  created
 */
public class Config
{
   private JavaClassPath         _oJavaClassPath      = new JavaClassPath();
   private Template              _oTemplate           = new Template();
   private ModelDefs             _oModelDefs          = new ModelDefs();
   private ControllerResolver    _oControllerResolver = new ControllerResolver();
   private FileUpload            _oFileUpload         = new FileUpload();

   /**
    * Defined the classpath to be used for loading java class files. The classpath is
    * a list of directories, each of which may contain java class sources or compiled
    * class files. Whatever classpath effective with the default classloader prevails,
    * otherwise the paths defined by this configuration are searched after the default
    * system defined classpath is searched.
    *
    * @return a java classpath definition
    */
   public JavaClassPath getJavaClassPath()
   {
      return _oJavaClassPath;
   }

   /**
    * A list of directories from which the view templating engine will load templates.
    *
    * @return a template path definition
    */
   public Template getTemplatePath()
   {
      return _oTemplate;
   }

   /**
    * This method is used to access the model definitions, in particular to add to, and read from
    * the model definitions stored in this configuration.
    *
    * @return an object reference that keeps the model definition information
    */
   public ModelDefs getModelDefs()
   {
      return _oModelDefs;
   }

   /**
    * This method is used to access the settings used to configure the controller resolver.
    *
    * @return an object reference that keeps the controller resolver config settings
    */
   public ControllerResolver getControllerResolver()
   {
      return _oControllerResolver;
   }

   /**
    * This method is used to access config parameters that control file uploads
    *
    * @return an object reference that keeps the file upload settings
    */
   public FileUpload getFileUpload()
   {
      return _oFileUpload;
   }

   /**
    * Inner classes used to structure the configuration information follows
    */
   public static class FileUpload
   {
      private static final int _1_KB = 1024;
      private static final int _1_MB = 1024 * 1024;

      private int    _iMaxUploadSizeInBytes              = 10 * _1_MB;  // by default files larger than 10 megabytes are not allowed
      private int    _iStoreFileOnDiskThresholdInBytes   = 10 * _1_KB;  // by default files larger than 10 kilobytes are stored on disk instead of memory
      private File   _oUploadedFileStorageDir            = null;

      public void setUploadedFileStorageDir( File oUploadedFileStorageDir )
      {
         _oUploadedFileStorageDir = oUploadedFileStorageDir;
      }

      /**
       * Returns the path to which uploaded files that are too large to be kept in memory
       * should be written, if no directory is specified all files are kept in memory
       *
       * @return a File object representing a path to which the uploaded files should be saved
       */
      public File getUploadedFileStorageDir()
      {
         return _oUploadedFileStorageDir;
      }

      public int getMaxUploadSizeInBytes()
      {
         return _iMaxUploadSizeInBytes;
      }

      /**
       * Sets the maximum upload file size in bytes will be accepted
       *
       * @param iMaxUploadSizeInBytes a file size in bytes
       */
      public void setMaxUploadSizeInBytes( int iMaxUploadSizeInBytes )
      {
         _iMaxUploadSizeInBytes = iMaxUploadSizeInBytes;
      }

      public int getStoreOnDiskThresholdInBytes()
      {
         return _iStoreFileOnDiskThresholdInBytes;
      }

      /**
       * Set a file size in bytes above which the uploaded file will be stored on disk
       *
       * @param iStoreOnDiskThresholdInBytes a file size in bytes
       */
      public void setStoreOnDiskThresholdInBytes( int iStoreOnDiskThresholdInBytes )
      {
         _iStoreFileOnDiskThresholdInBytes = iStoreOnDiskThresholdInBytes;
      }
   }

   public static class ControllerResolver
   {
      private String    _sClassName;
      private String    _sDefaultHandlerMethodName = "handler";
      private boolean   _bIgnoreMethodNameCase = true;

      /**
       * Used to set a fully qualified class name used to resolve a HTTP request to a controller name (and method).
       * This parameter is usually not set. When not set, a default controller resolver that maps a URL path to
       * a controller name and method is used.
       *
       * @param sClassName a string representing fully qualified classname or null to use the default resolver
       */
      public void setClassName( String sClassName )
      {
         _sClassName = sClassName;
      }

      /**
       * Returns the name of the class used to resolve a HTTP request to a controller name (and method).
       *
       * @return a string representing a fully qualified class name
       */
      public String getClassName()
      {
         return _sClassName;
      }

      public String getDefaultHandlerMethodName()
      {
         return _sDefaultHandlerMethodName;
      }

      public void setDefaultHandlerMethodName( String sDefaultHandlerMethodName )
      {
         if ( sDefaultHandlerMethodName == null )
         {
            throw new IllegalArgumentException( "config-error: default handler name cannot be set to null or empty" );
         }

         if ( Character.isJavaIdentifierStart( sDefaultHandlerMethodName.charAt( 0 )) )
         {
            throw new IllegalArgumentException( "config-error: default handler name cannot starts with an invalid character" );
         }

         _sDefaultHandlerMethodName = sDefaultHandlerMethodName;
      }

      public boolean isIgnoreMethodNameCase()
      {
         return _bIgnoreMethodNameCase;
      }

      /**
       * Controls if the default resolver should respect method case when looking for a method.
       *
       * @param bIgnoreMethodNameCase
       */
      public void setIgnoreMethodNameCase( boolean bIgnoreMethodNameCase )
      {
         _bIgnoreMethodNameCase = bIgnoreMethodNameCase;
      }
   }

   public static class ModelDefs
   {
      private Map _oModelDefMap = new HashMap();

      /**
       * Adds a new model definition
       *
       * @param sModelClassName string representing a fully qualified model classname
       * @param sModelFactoryClassName string representing a fully qualified model factory classname
       * @param bIsApplicationScope true if a single model object should be created per application
       * @param bIsSessionScope true if a single model object should be created per session
       * @param bIsRequestScope true if a single model object should be created per request
       */
      public void addModelDef( String sModelClassName, String sModelFactoryClassName, boolean bIsApplicationScope, boolean bIsSessionScope, boolean bIsRequestScope )
      {
         sModelClassName = sModelClassName.trim();

         if ( _oModelDefMap.containsKey( sModelClassName ) )
         {
            throw new IllegalArgumentException( "config-error: model classname: " + sModelClassName + " already defined" );
         }

         _oModelDefMap.put( sModelClassName, new ModelDef( sModelClassName, sModelFactoryClassName, bIsApplicationScope, bIsSessionScope, bIsRequestScope ) );
      }

      public ModelDef getModelDef( String sModelClassName )
      {
         return ( ModelDef ) _oModelDefMap.get( sModelClassName );
      }

      public Collection getModelDefList()
      {
         return _oModelDefMap.values();
      }

      public static class ModelDef
      {
         private String    _sModelClassName;
         private String    _sModelFactoryClassName;
         private boolean   _bIsApplicationScope;
         private boolean   _bIsSessionScope;
         private boolean   _bIsRequestScope;

         private ModelDef( String sModelClassName, String sModelFactoryClassName, boolean bIsApplicationScope, boolean bIsSessionScope, boolean bIsRequestScope )
         {
            if ( sModelClassName == null )
            {
               throw new IllegalArgumentException( "config-error: model classname not defined" );
            }

            sModelClassName = sModelClassName.trim();

            if ( sModelClassName.length() == 0 )
            {
               throw new IllegalArgumentException( "config-error: model classname empty" );
            }

            if ( ! ( bIsApplicationScope || bIsSessionScope || bIsRequestScope ) )
            {
               throw new IllegalArgumentException( "config-error: model scope not defined" );
            }

            // check that only one of the booleans has a true value
            if ( bIsApplicationScope )
            {
               if ( bIsSessionScope || bIsRequestScope )
               {
                  throw new IllegalArgumentException( "config-error: model scope defined ambiguously" );
               }
            }
            else if ( bIsSessionScope )
            {
               if ( bIsRequestScope )
               {
                  throw new IllegalArgumentException( "config-error: model scope defined ambiguously" );
               }
            }

            _sModelClassName        = sModelClassName;
            _sModelFactoryClassName = sModelFactoryClassName;
            _bIsApplicationScope    = bIsApplicationScope;
            _bIsSessionScope        = bIsSessionScope;
            _bIsRequestScope        = bIsRequestScope;
         }

         public String getModelClassName()
         {
            return _sModelClassName;
         }

         public String getModelFactoryClassName()
         {
            return _sModelFactoryClassName;
         }

         public boolean hasModelFactoryClassName()
         {
            return _sModelFactoryClassName != null && _sModelFactoryClassName.trim().length() != 0;
         }

         public boolean isApplicationScope()
         {
            return _bIsApplicationScope;
         }

         public boolean isSessionScope()
         {
            return _bIsSessionScope;
         }

         public boolean isRequestScope()
         {
            return _bIsRequestScope;
         }
      }
   }

   public static class Template
   {
      private  LoaderPath  _oLoaderPath = new LoaderPath();

      public LoaderPath getLoaderPath()
      {
         return _oLoaderPath;
      }

      public static class LoaderPath
      {
         private  List _oLoaderPath = new ArrayList();

         /**
          * Adds a directory to the list of locations in which a template will be searched
          *
          * @param oDir a File object representing a directory
          */
         public void addDirForLoading( File oDir )
         {
            _oLoaderPath.add( new DirForLoading( oDir ) );
         }

         /**
          * Adds a class to the list of locations in which a template will be searched,
          * this setting will only be used by a templating engine that supports it (e.g: Freemarker)
          * in these cases the templating engine will call the getResource() method on the specified
          * class to retrieve a template
          *
          * @param oClass a class object
          * @param sPackageNamePrefix a prefix to append to the template name before attempting
          * to passing the name to getResource()
          */
         public void addClassForLoading( Class oClass, String sPackageNamePrefix )
         {
            _oLoaderPath.add( new ClassForLoading( oClass, sPackageNamePrefix ) );
         }

         /**
          * Adds a web application path to the list of locations in which a template will be searched,
          * this setting will only be used by a templating engine that supports it (e.g: Freemarker)
          * in these cases the templating engine will call the Servlet context's getResource() method
          *
          * to passing the name to getResource()
          * @param sRelativePath a path of the web application root (the parent of the WEB-INF folder)
          */
         public void addServeletContextForLoading( String sRelativePath )
         {
            _oLoaderPath.add( new ServletContextForLoading( sRelativePath ) );
         }

         /**
          * Returns the list of items added to this path
          * @return a list, each element in the list is one of the following types:
          * DirForLoading, ClassForLoading or ServletContextForLoading
          */
         public List getList()
         {
            return _oLoaderPath;
         }

         public static class DirForLoading
         {
            private  File     _oDir;

            private DirForLoading( File oDir )
            {
               _oDir = oDir;
            }

            public File getDir()
            {
               return _oDir;
            }
         }

         public static class ClassForLoading
         {
            private  Class    _oClass;
            private  String   _sPackageNamePrefix;

            private ClassForLoading( Class oClass, String sPackageNamePrefix )
            {
               _oClass              = oClass;
               _sPackageNamePrefix  = sPackageNamePrefix;
            }

            public Class getLoaderClass()
            {
               return _oClass;
            }

            public String getPackageNamePrefix()
            {
               return _sPackageNamePrefix;
            }
         }

         public static class ServletContextForLoading
         {
            private String _sRelativePath;

            public ServletContextForLoading( String sRelativePath )
            {
               _sRelativePath = sRelativePath;
            }

            /**
             * This path is relative to the parent of the WEB-INF directory of the web application
             * @return a string path
             */
            public String getRelativePath()
            {
               return _sRelativePath;
            }
         }
      }
   }

   public static class JavaClassPath
   {
      private  List           _oDirList      = new ArrayList();
      private  JavaCompiler   _oJavaCompiler = new JavaCompiler();

      public void addCompiledDir( File oDir, String sPackagePrefixName )
      {
         _oDirList.add( new CompiledDir( oDir, sPackagePrefixName ) );
      }

      public void addSourceDir( File oDir, String sPackagePrefixName )
      {
         _oDirList.add( new SourceDir( oDir, sPackagePrefixName ) );
      }

      public List getDirList()
      {
         return _oDirList;
      }

      /**
       * This method is used to configure the java compiler used for compiling java sources at runtime.
       *
       * @return an object reference that has the compiler settings
       */
      public JavaCompiler getJavaCompiler()
      {
         return _oJavaCompiler;
      }

      public static class CompiledDir
      {
         private  File     _oDir;
         private  String   _sPackageNamePrefix;

         private CompiledDir( File oDir, String sPackagePrefixName )
         {
            _oDir                =  oDir;
            _sPackageNamePrefix  =  sPackagePrefixName;
         }

         public File getDir()
         {
            return _oDir;
         }

         public String getPackageNamePrefix()
         {
            return _sPackageNamePrefix;
         }
      }

      public static class SourceDir
      {
         private  File     _oDir;
         private  String   _sPackageNamePrefix;

         private SourceDir( File oDir, String sPackagePrefixName )
         {
            _oDir                =  oDir;
            _sPackageNamePrefix  =  sPackagePrefixName;
         }

         public File getDir()
         {
            return _oDir;
         }

         public String getPackageNamePrefix()
         {
            return _sPackageNamePrefix;
         }
      }

      public static class JavaCompiler
      {
         private String    _sJavaCompilerClassName;

         public String getJavaCompilerClassName()
         {
            return ( _sJavaCompilerClassName != null
                     ? _sJavaCompilerClassName
                     : "com.acciente.commons.javac.JavaCompiler_JDK_1_4"
                   );
         }

         public void setJavaCompilerClassName( String sJavaCompilerClassName )
         {
            _sJavaCompilerClassName = sJavaCompilerClassName;
         }
      }
   }
}

// EOF