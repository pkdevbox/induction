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
package com.acciente.induction.init.config;

import com.acciente.commons.lang.Strings;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This class is the container for the configuration information used by Induction.
 * It uses inner classes to represent each type of category of configuration information,
 * sometimes there may be two identical looking inner classes but this is done since, though
 * identical, the two classes represent different concepts and as such may diverge in the future.
 *
 * @created Feb 29, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class Config
{
   private JavaClassPath         _oJavaClassPath      = new JavaClassPath();
   private ModelDefs             _oModelDefs          = new ModelDefs();
   private Templating            _oTemplating         = new Templating();
   private ControllerMapping     _oControllerMapping  = new ControllerMapping();
   private ViewMapping           _oViewMapping        = new ViewMapping();
   private RedirectMapping       _oRedirectMapping    = new RedirectMapping();
   private ControllerResolver    _oControllerResolver = new ControllerResolver();
   private ViewResolver          _oViewResolver       = new ViewResolver();
   private RedirectResolver      _oRedirectResolver   = new RedirectResolver();
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
    * This method provides access to the configuration of the template engine.
    *
    * @return a configuration object
    */
   public Templating getTemplating()
   {
      return _oTemplating;
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
    * This method is used to access the settings used to configure the controller mapping.
    *
    * @return an object reference that keeps the controller mapping config settings
    */
   public ControllerMapping getControllerMapping()
   {
      return _oControllerMapping;
   }

   /**
    * This method is used to access the settings used to configure the view mapping.
    *
    * @return an object reference that keeps the view mapping config settings
    */
   public ViewMapping getViewMapping()
   {
      return _oViewMapping;
   }

   /**
    * This method is used to access the settings used to configure the redirect mapping.
    *
    * @return an object reference that keeps the redirect mapping config settings
    */
   public RedirectMapping getRedirectMapping()
   {
      return _oRedirectMapping;
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
    * This method is used to access the settings used to configure the view resolver.
    *
    * @return an object reference that keeps the view resolver config settings
    */
   public ViewResolver getViewResolver()
   {
      return _oViewResolver;
   }

   /**
    * This method is used to access the settings used to configure the redirect resolver.
    *
    * @return an object reference that keeps the redirect resolver config settings
    */
   public RedirectResolver getRedirectResolver()
   {
      return _oRedirectResolver;
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

   public String toString()
   {
      return toXML();
   }

   public String toXML()
   {
      StringBuffer   oBuffer = new StringBuffer();

      oBuffer.append( XML.Config.OPEN_IND );

      oBuffer.append( _oJavaClassPath.toXML() );
      oBuffer.append( _oModelDefs.toXML() );
      oBuffer.append( _oTemplating.toXML() );

      oBuffer.append( _oControllerMapping.toXML() );
      oBuffer.append( _oViewMapping.toXML() );
      oBuffer.append( _oRedirectMapping.toXML() );

      oBuffer.append( _oControllerResolver.toXML() );
      oBuffer.append( _oViewResolver.toXML() );
      oBuffer.append( _oRedirectResolver.toXML() );

      oBuffer.append( _oFileUpload.toXML() );
      oBuffer.append( "\n" );
      oBuffer.append( XML.Config.CLOSE_IND );

      return oBuffer.toString();
   }

   /**
    * Modular configuration container
    */
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

      public String toString()
      {
         return toXML();
      }

      public String toXML()
      {
         String   sXML_JavaCompiler = _oJavaCompiler.toXML();

         if ( _oDirList.size() == 0 && sXML_JavaCompiler.equals( "" ) )
         {
            return "";
         }
         else
         {
            StringBuffer   oBuffer = new StringBuffer();

            oBuffer.append( "\n" );
            oBuffer.append( XML.Config_JavaClassPath.OPEN_IND );

            for ( Iterator oIter = _oDirList.iterator(); oIter.hasNext(); )
            {
               Object oPathItem = oIter.next();

               if ( oPathItem instanceof CompiledDir )
               {
                  oBuffer.append ( ( ( CompiledDir ) oPathItem ).toXML() );
               }
               else if ( oPathItem instanceof SourceDir )
               {
                  oBuffer.append ( ( ( SourceDir ) oPathItem ).toXML() );
               }
               else
               {
                  throw new IllegalArgumentException( "config-error: internal error: unknown java class path item : " + oPathItem + ", of type: " + oPathItem.getClass() );
               }
            }

            oBuffer.append( sXML_JavaCompiler );

            oBuffer.append( "\n" );
            oBuffer.append( XML.Config_JavaClassPath.CLOSE_IND );

            return oBuffer.toString();
         }
      }

      /**
       * Modular configuration container
       */
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

         public String toString()
         {
            return toXML();
         }

         public String toXML()
         {
            return
               XML.Config_JavaClassPath_CompiledDirectory
                  .toXML( XML.Config_JavaClassPath_CompiledDirectory_Directory.toXML( _oDir )
                          + XML.Config_JavaClassPath_CompiledDirectory_PackagePrefix.toXML( _sPackageNamePrefix )
                        );
         }
      }

      /**
       * Modular configuration container
       */
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

         public String toString()
         {
            return toXML();
         }

         public String toXML()
         {
            return
               XML.Config_JavaClassPath_SourceDirectory
                  .toXML( XML.Config_JavaClassPath_SourceDirectory_Directory.toXML( _oDir )
                          + XML.Config_JavaClassPath_SourceDirectory_PackagePrefix.toXML( _sPackageNamePrefix )
                        );
         }
      }

      /**
       * Modular configuration container
       */
      public static class JavaCompiler
      {
         private static final String    DEFAULT = "com.acciente.commons.javac.JavaCompiler_JDK_1_4";

         private String    _sJavaCompilerClassName;

         public String getJavaCompilerClassName()
         {
            return ( _sJavaCompilerClassName != null
                     ? _sJavaCompilerClassName
                     : DEFAULT
                   );
         }

         public void setJavaCompilerClassName( String sJavaCompilerClassName )
         {
            _sJavaCompilerClassName = sJavaCompilerClassName;
         }

         public String toString()
         {
            return toXML();
         }

         public String toXML()
         {
            if ( _sJavaCompilerClassName == null )
            {
               return "";
            }
            else
            {
               return
                  XML.Config_JavaClassPath_JavaCompiler
                     .toXML( XML.Config_JavaClassPath_JavaCompiler_Class.toXML( _sJavaCompilerClassName ) );
            }
         }
      }
   }

   /**
    * Modular configuration container
    */
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

      public String toString()
      {
         return toXML();
      }

      public String toXML()
      {
         if ( _oModelDefMap.size() == 0 )
         {
            return "";
         }
         else
         {
            StringBuffer   oBuffer = new StringBuffer();

            oBuffer.append( "\n" );
            oBuffer.append( XML.Config_ModelDefs.OPEN_IND );

            for ( Iterator oIter = _oModelDefMap.values().iterator(); oIter.hasNext(); )
            {
               oBuffer.append( ( ( ModelDef ) oIter.next() ).toXML() );
            }

            oBuffer.append( "\n" );
            oBuffer.append( XML.Config_ModelDefs.CLOSE_IND );

            return oBuffer.toString();
         }
      }

      /**
       * Modular configuration container
       */
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

         public String toString()
         {
            return toXML();
         }

         public String toXML()
         {
            return
               XML.Config_ModelDefs_ModelDef
                  .toXML( XML.Config_ModelDefs_ModelDef_Class.toXML( _sModelClassName )
                          + XML.Config_ModelDefs_ModelDef_FactoryClass.toXML( _sModelFactoryClassName )
                          + XML.Config_ModelDefs_ModelDef_Scope.toXML( _bIsApplicationScope
                                                                       ? "Application"
                                                                       : ( _bIsSessionScope
                                                                           ? "Session"
                                                                           : ( _bIsRequestScope
                                                                               ? "Request"
                                                                               : "!! invalid value !!"
                                                                             )
                                                                         )
                                                                     )
                        );
         }
      }
   }

   /**
    * Modular configuration container
    */
   public static class Templating
   {
      private TemplatePath             _oTemplatePath       = new TemplatePath();
      private Locale                   _oLocale;
      private TemplatingEngine         _oTemplatingEngine   = new TemplatingEngine();
      private boolean                  _bExposePublicFields;

      public TemplatePath getTemplatePath()
      {
         return _oTemplatePath;
      }

      public TemplatingEngine getTemplatingEngine()
      {
         return _oTemplatingEngine;
      }

      public Locale getLocale()
      {
         return _oLocale;
      }

      public void setLocale( Locale oLocale )
      {
         _oLocale = oLocale;
      }

      public boolean isExposePublicFields()
      {
         return _bExposePublicFields;
      }

      public void setExposePublicFields( boolean bExposePublicFields )
      {
         _bExposePublicFields = bExposePublicFields;
      }

      public String toString()
      {
         return toXML();
      }

      public String toXML()
      {
         String   sXML_TemplatePath             =  _oTemplatePath.toXML();
         String   sXML_TemplatingEngineProvider =  _oTemplatingEngine.toXML();
         String   sXML_Locale                   =  toXML_Locale();

         if ( sXML_TemplatePath.equals( "" )
               && sXML_Locale.equals( "" )
               && sXML_TemplatingEngineProvider.equals( "" )
            )
         {
            return "";
         }
         else
         {
            StringBuffer   oBuffer = new StringBuffer();

            oBuffer.append( "\n" );
            oBuffer.append( XML.Config_Templating.OPEN_IND );

            oBuffer.append( sXML_TemplatePath );
            oBuffer.append( sXML_Locale );
            oBuffer.append( sXML_TemplatingEngineProvider );

            oBuffer.append( "\n" );
            oBuffer.append( XML.Config_Templating.CLOSE_IND );

            return  oBuffer.toString();
         }
      }

      private String toXML_Locale()
      {
         StringBuffer oBuffer = new StringBuffer();

         if ( _oLocale == null )
         {
            return "";
         }
         else
         {
            return
               XML.Config_Templating_Locale
                  .toXML( XML.Config_Templating_Locale_ISOLanguage.toXML( _oLocale.getLanguage() )
                          + XML.Config_Templating_Locale_ISOCountry.toXML( _oLocale.getCountry() )
                        );
         }
      }

      /**
       * Modular configuration container
       */
      public static class TemplatingEngine
      {
         private String    _sClassName;

         /**
          * Used to set a fully qualified class name that implements the templating engine interface
          * (the templating engine interface is com.acciente.induction.template.TemplatingEngine)
          *
          * @param sClassName a string representing fully qualified classname or null to use the default templating engine
          */
         public void setClassName( String sClassName )
         {
            _sClassName = sClassName;
         }

         /**
          * Returns the name of the templating engine class configured or null if no value was provided
          *
          * @return a string representing a fully qualified class name
          */
         public String getClassName()
         {
            return _sClassName;
         }

         public String toString()
         {
            return toXML();
         }

         public String toXML()
         {
            if ( _sClassName == null )
            {
               return "";
            }
            else
            {
               return
                  XML.Config_Templating_TemplatingEngine
                     .toXML( XML.Config_Templating_TemplatingEngine_Class.toXML( _sClassName ) );
            }
         }
      }

      /**
       * Modular configuration container
       */
      public static class TemplatePath
      {
         private  List _oTemplatePath = new ArrayList();

         /**
          * Adds a directory to the list of locations in which a template will be searched
          *
          * @param oDir a File object representing a directory
          */
         public void addDir( File oDir )
         {
            _oTemplatePath.add( new Dir( oDir ) );
         }

         /**
          * Adds a class to the list of locations in which a template will be searched,
          * this setting will only be used by a templating engine that supports it (e.g: Freemarker)
          * in these cases the templating engine will call the getResource() method on the specified
          * class to retrieve a template
          *
          * @param sLoaderClassName a prefix to append to the template name before attempting
          * @param sPath a path to where the templates are located, if the path starts with a /
          * then the path is absolute, otherwise it is assumed to be relative to the package name
          * of the loader class
          */
         public void addLoaderClass( String sLoaderClassName, String sPath )
         {
            _oTemplatePath.add( new LoaderClass( sLoaderClassName, sPath ) );
         }

         /**
          * Adds a web application path to the list of locations in which a template will be searched,
          * this setting will only be used by a templating engine that supports it (e.g: Freemarker)
          * in these cases the templating engine will call the Servlet context's getResource() method
          *
          * to passing the name to getResource()
          * @param sRelativePath a path of the web application root (the parent of the WEB-INF folder)
          */
         public void addWebappPath( String sRelativePath )
         {
            _oTemplatePath.add( new WebappPath( sRelativePath ) );
         }

         /**
          * Returns the list of items added to this path
          * @return a list, each element in the list is one of the following types:
          * Dir, Class or WebappPath
          */
         public List getList()
         {
            return _oTemplatePath;
         }

         public String toString()
         {
            return toXML();
         }

         public String toXML()
         {
            if ( _oTemplatePath.size() == 0 )
            {
               return "";
            }
            else
            {
               StringBuffer   oBuffer = new StringBuffer();

               oBuffer.append( "\n" );
               oBuffer.append( XML.Config_Templating_TemplatePath.OPEN_IND );

               for ( Iterator oIter = _oTemplatePath.iterator(); oIter.hasNext(); )
               {
                  Object oPathItem = oIter.next();

                  if ( oPathItem instanceof Dir )
                  {
                     oBuffer.append ( ( ( Dir ) oPathItem ).toXML() );
                  }
                  else if ( oPathItem instanceof LoaderClass )
                  {
                     oBuffer.append ( ( ( LoaderClass ) oPathItem ).toXML() );
                  }
                  else if ( oPathItem instanceof WebappPath )
                  {
                     oBuffer.append ( ( ( WebappPath ) oPathItem ).toXML() );
                  }
                  else
                  {
                     throw new IllegalArgumentException( "config-error: internal error: unknown template path item : " + oPathItem + ", of type: " + oPathItem.getClass() );
                  }
               }

               oBuffer.append( "\n" );
               oBuffer.append( XML.Config_Templating_TemplatePath.CLOSE_IND );

               return oBuffer.toString();
            }
         }

         /**
          * Modular configuration container
          */
         public static class Dir
         {
            private  File     _oDir;

            private Dir( File oDir )
            {
               _oDir = oDir;
            }

            public File getDir()
            {
               return _oDir;
            }

            public String toString()
            {
               return toXML();
            }

            public String toXML()
            {
               return
                  XML.Config_Templating_TemplatePath_Directory.toXML( _oDir );
            }
         }

         /**
          * Modular configuration container
          */
         public static class LoaderClass
         {
            private  String _sLoaderClassName;
            private  String _sPath;

            private LoaderClass( String sLoaderClassName, String sPath )
            {
               _sLoaderClassName = sLoaderClassName;
               _sPath            = sPath;
            }

            public String getLoaderClassName()
            {
               return _sLoaderClassName;
            }

            public String getPath()
            {
               return _sPath;
            }

            public String toString()
            {
               return toXML();
            }

            public String toXML()
            {
               return
                  XML.Config_Templating_TemplatePath_LoaderClass
                     .toXML( XML.Config_Templating_TemplatePath_LoaderClass_Class.toXML( _sLoaderClassName )
                             + XML.Config_Templating_TemplatePath_LoaderClass_Path.toXML( _sPath )
                           );
            }
         }

         /**
          * Modular configuration container
          */
         public static class WebappPath
         {
            private String _sPath;

            public WebappPath( String sPath )
            {
               _sPath = sPath;
            }

            /**
             * This path is relative to the parent of the WEB-INF directory of the web application
             * @return a string path
             */
            public String getPath()
            {
               return _sPath;
            }

            public String toString()
            {
               return toXML();
            }

            public String toXML()
            {
               return
                  XML.Config_Templating_TemplatePath_WebAppPath.toXML( _sPath );
            }
         }
      }
   }

   /**
    * Modular configuration container
    */
   public static class ControllerMapping
   {
      private List      _oURLToClassMapList        = new ArrayList();
      private String    _sDefaultHandlerMethodName = "handler";
      private boolean   _bIgnoreMethodNameCase     = false;

      public void addURLToClassMap( Pattern oURLPattern, Pattern oClassPattern )
      {
         _oURLToClassMapList.add( new URLToClassMap( oURLPattern, oClassPattern ) );
      }

      public List getURLToClassMapList()
      {
         return _oURLToClassMapList;
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

         if ( ! Character.isJavaIdentifierStart( sDefaultHandlerMethodName.charAt( 0 )) )
         {
            throw new IllegalArgumentException( "config-error: default handler name starts with an invalid character" );
         }

         _sDefaultHandlerMethodName = sDefaultHandlerMethodName;
      }

      public boolean isIgnoreMethodNameCase()
      {
         return _bIgnoreMethodNameCase;
      }

      /**
       * Controls if the default resolver should respect method case when looking for a method. A resolver
       * implementation is may choose to ignore this setting.
       *
       * @param bIgnoreMethodNameCase if true is specified, it tells the resolver implementation to
       * search for a handler method name ignoring case.
       */
      public void setIgnoreMethodNameCase( boolean bIgnoreMethodNameCase )
      {
         _bIgnoreMethodNameCase = bIgnoreMethodNameCase;
      }

      public String toString()
      {
         return toXML();
      }

      public String toXML()
      {
         StringBuffer   oBuffer = new StringBuffer();

         oBuffer.append( "\n" );
         oBuffer.append( XML.Config_ControllerMapping.OPEN_IND );

         for ( Iterator oIter = _oURLToClassMapList.iterator(); oIter.hasNext(); )
         {
            oBuffer.append ( ( ( URLToClassMap ) oIter.next() ).toXML() );
         }

         oBuffer.append( XML.Config_ControllerMapping_DefaultHandlerMethod.toXML( _sDefaultHandlerMethodName ) );
         oBuffer.append( XML.Config_ControllerMapping_IgnoreHandlerMethodCase.toXML( _bIgnoreMethodNameCase ) );

         oBuffer.append( "\n" );
         oBuffer.append( XML.Config_ControllerMapping.CLOSE_IND );

         return oBuffer.toString();
      }

      /**
       * Modular configuration container
       */
      public static class URLToClassMap
      {
         private Pattern   _oURLPattern;
         private Pattern   _oClassPattern;

         private URLToClassMap( Pattern oURLPattern, Pattern oClassPattern )
         {
            validateURLPattern( oURLPattern );
            validateClassPattern( oClassPattern );

            _oURLPattern   = oURLPattern;
            _oClassPattern = oClassPattern;
         }

         public Pattern getURLPattern()
         {
            return _oURLPattern;
         }

         public Pattern getClassPattern()
         {
            return _oClassPattern;
         }

         public String toString()
         {
            return toXML();
         }

         public String toXML()
         {
            return
               XML.Config_ControllerMapping_URLToClassMap
                  .toXML( XML.Config_ControllerMapping_URLToClassMap_URLPattern.toXML( _oURLPattern )
                          + XML.Config_ControllerMapping_URLToClassMap_ClassPattern.toXML( _oClassPattern )
                        );
         }
      }
   }

   /**
    * Modular configuration container
    */
   public static class ViewMapping
   {
      private List      _oURLToClassMapList        = new ArrayList();

      public void addURLToClassMap( Pattern oURLPattern, Pattern oClassPattern )
      {
         _oURLToClassMapList.add( new URLToClassMap( oURLPattern, oClassPattern ) );
      }

      public List getURLToClassMapList()
      {
         return _oURLToClassMapList;
      }

      public String toString()
      {
         return toXML();
      }

      public String toXML()
      {
         StringBuffer   oBuffer = new StringBuffer();

         oBuffer.append( "\n" );
         oBuffer.append( XML.Config_ViewMapping.OPEN_IND );

         for ( Iterator oIter = _oURLToClassMapList.iterator(); oIter.hasNext(); )
         {
            oBuffer.append ( ( ( URLToClassMap ) oIter.next() ).toXML() );
         }

         oBuffer.append( "\n" );
         oBuffer.append( XML.Config_ViewMapping.CLOSE_IND );

         return oBuffer.toString();
      }

      /**
       * Modular configuration container
       */
      public static class URLToClassMap
      {
         private Pattern   _oURLPattern;
         private Pattern   _oClassPattern;

         private URLToClassMap( Pattern oURLPattern, Pattern oClassPattern )
         {
            validateURLPattern( oURLPattern );
            validateClassPattern( oClassPattern );

            _oURLPattern   = oURLPattern;
            _oClassPattern = oClassPattern;
         }

         public Pattern getURLPattern()
         {
            return _oURLPattern;
         }

         public Pattern getClassPattern()
         {
            return _oClassPattern;
         }

         public String toString()
         {
            return toXML();
         }

         public String toXML()
         {
            return
               XML.Config_ViewMapping_URLToClassMap
                  .toXML( XML.Config_ViewMapping_URLToClassMap_URLPattern.toXML( _oURLPattern )
                          + XML.Config_ViewMapping_URLToClassMap_ClassPattern.toXML( _oClassPattern )
                        );
         }
      }
   }

   /**
    * Modular configuration container
    */
   public static class RedirectMapping
   {
      private List   _oClassToURLMapList = new ArrayList();
      private String _sURLBase           = "";

      public void addClassToURLMap( Pattern oClassPattern, String sURLFormat )
      {
         _oClassToURLMapList.add( new ClassToURLMap( oClassPattern, sURLFormat ) );
      }

      public List getClassToURLMapList()
      {
         return _oClassToURLMapList;
      }

      public String getURLBase()
      {
         return _sURLBase;
      }

      public void setURLBase( String sURLBase )
      {
         _sURLBase = sURLBase;
      }

      public String toString()
      {
         return toXML();
      }

      public String toXML()
      {
         if ( _oClassToURLMapList.size() == 0 && Strings.isEmpty( _sURLBase ) )
         {
            return "";
         }

         StringBuffer   oBuffer = new StringBuffer();

         oBuffer.append( "\n" );
         oBuffer.append( XML.Config_RedirectMapping.OPEN_IND );

         for ( Iterator oIter = _oClassToURLMapList.iterator(); oIter.hasNext(); )
         {
            oBuffer.append ( ( ( ClassToURLMap ) oIter.next() ).toXML() );
         }

         if ( ! Strings.isEmpty( _sURLBase ) )
         {
            oBuffer.append( XML.Config_RedirectMapping_URLBase.toXML( _sURLBase ) );
         }

         oBuffer.append( "\n" );
         oBuffer.append( XML.Config_RedirectMapping.CLOSE_IND );

         return oBuffer.toString();
      }

      /**
       * Modular configuration container
       */
      public static class ClassToURLMap
      {
         private Pattern   _oClassPattern;
         private String    _sURLFormat;

         private ClassToURLMap( Pattern oClassPattern, String sURLFormat )
         {
            validateClassPattern( oClassPattern );

            _oClassPattern = oClassPattern;
            _sURLFormat    = sURLFormat;
         }

         public Pattern getClassPattern()
         {
            return _oClassPattern;
         }

         public String getURLFormat()
         {
            return _sURLFormat;
         }

         public String toString()
         {
            return toXML();
         }

         public String toXML()
         {
            return
               XML.Config_RedirectMapping_ClassToURLMap
                  .toXML( XML.Config_RedirectMapping_ClassToURLMap_ClassPattern.toXML( _oClassPattern )
                          + XML.Config_RedirectMapping_ClassToURLMap_URLFormat.toXML( _sURLFormat )
                        );
         }
      }
   }

   /**
    * Modular configuration container
    */
   public static class ControllerResolver
   {
      private String    _sClassName;

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

      public String toString()
      {
         return toXML();
      }

      public String toXML()
      {
         return
            XML.Config_ControllerResolver
               .toXML( XML.Config_ControllerResolver_Class.toXML( _sClassName ) );
      }
   }

   /**
    * Modular configuration container
    */
   public static class ViewResolver
   {
      private String    _sClassName;

      /**
       * Used to set a fully qualified class name used to resolve a HTTP request to a view name (and method).
       * This parameter is usually not set. When not set, a default view resolver that maps a URL path to
       * a view name and method is used.
       *
       * @param sClassName a string representing fully qualified classname or null to use the default resolver
       */
      public void setClassName( String sClassName )
      {
         _sClassName = sClassName;
      }

      /**
       * Returns the name of the class used to resolve a HTTP request to a view name (and method).
       *
       * @return a string representing a fully qualified class name
       */
      public String getClassName()
      {
         return _sClassName;
      }

      public String toString()
      {
         return toXML();
      }

      public String toXML()
      {
         return
            XML.Config_ViewResolver
               .toXML( XML.Config_ViewResolver_Class.toXML( _sClassName ) );
      }
   }

   /**
    * Modular configuration container
    */
   public static class RedirectResolver
   {
      private String    _sClassName;

      /**
       * Used to set a fully qualified class name used to resolve a redirect request.
       *
       * @param sClassName a string representing fully qualified classname or null to use the default resolver
       */
      public void setClassName( String sClassName )
      {
         _sClassName = sClassName;
      }

      /**
       * Returns the name of the class used to resolve a redirect request.
       *
       * @return a string representing a fully qualified class name
       */
      public String getClassName()
      {
         return _sClassName;
      }

      public String toString()
      {
         return toXML();
      }

      public String toXML()
      {
         return
            XML.Config_RedirectResolver
               .toXML( XML.Config_RedirectResolver_Class.toXML( _sClassName ) );
      }
   }

   /**
    * Modular configuration container
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

      public String toString()
      {
         return toXML();
      }

      public String toXML()
      {
         return
            XML.Config_FileUpload
               .toXML( XML.Config_FileUpload_MaxUploadSize.toXML( _iMaxUploadSizeInBytes )
                       + XML.Config_FileUpload_StoreFileOnDiskThreshold.toXML( _iStoreFileOnDiskThresholdInBytes )
                       + XML.Config_FileUpload_UploadedFileStorageDir.toXML( _oUploadedFileStorageDir )
                     );
      }
   }

   private static void validateURLPattern( Pattern oURLPattern )
   {
      // we validate the regex to ensure that it has atleast on matching group
      int iGroupCount = oURLPattern.matcher( "" ).groupCount();

      if ( iGroupCount != 1 && iGroupCount != 2 )
      {
         throw new IllegalArgumentException( "config-error: must have exactly 1, or exactly 2, matching group(s) in URL pattern: "
                                             + oURLPattern.pattern() );
      }
   }

   private static void validateClassPattern( Pattern oClassPattern )
   {
      // we validate the regex to ensure that it has atleast on matching group
      if ( oClassPattern.matcher( "" ).groupCount() != 1 )
      {
         throw new IllegalArgumentException( "config-error: must have exactly 1 matching group in class pattern: "
                                             + oClassPattern.pattern() );
      }
   }
}

// EOF