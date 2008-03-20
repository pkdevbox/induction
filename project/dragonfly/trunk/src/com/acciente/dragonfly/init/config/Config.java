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
   private TemplatePath          _oTemplatePath       = new TemplatePath();
   private ModelDefs             _oModelDefs          = new ModelDefs();
   private ControllerResolver    _oControllerResolver = new ControllerResolver();

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
   public TemplatePath getTemplatePath()
   {
      return _oTemplatePath;
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
    * Inner classes used to structure the configuration information follows
    */
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

   public static class TemplatePath
   {
      private  List  _oDirList = new ArrayList();

      /**
       * Adds a directory to the list of directories in which the dispatcher
       * look for a template.
       *
       * @param oDir a File object representing a directory
       */
      public void addTemplateDir( File oDir )
      {
         _oDirList.add( new TemplateDir( oDir ) );
      }

      public List getDirList()
      {
         return _oDirList;
      }

      public static class TemplateDir
      {
         private  File     _oDir;

         private TemplateDir( File oDir )
         {
            _oDir =  oDir;
         }

         public File getDir()
         {
            return _oDir;
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