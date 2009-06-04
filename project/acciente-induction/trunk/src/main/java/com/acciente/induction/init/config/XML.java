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

/**
 * Internal.
 *
 * @created Apr 27, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class XML
{
   public static final XML Config                                                = new XML( "config" );

   public static final XML Config_IncludeConfig                                  = new XML( "include-config", Config );

   public static final XML Config_JavaClassPath                                  = new XML( "java-class-path",    Config );
   public static final XML Config_JavaClassPath_CompiledDirectory                = new XML( "compiled-directory", Config_JavaClassPath );
   public static final XML Config_JavaClassPath_CompiledDirectory_Directory      = new XML( "directory",          Config_JavaClassPath_CompiledDirectory );
   public static final XML Config_JavaClassPath_CompiledDirectory_PackagePrefix  = new XML( "package-prefix",     Config_JavaClassPath_CompiledDirectory );

   public static final XML Config_ModelDefs                                      = new XML( "model-defs",         Config );
   public static final XML Config_ModelDefs_ModelDef                             = new XML( "model-def",          Config_ModelDefs );
   public static final XML Config_ModelDefs_ModelDef_Class                       = new XML( "class",              Config_ModelDefs_ModelDef );
   public static final XML Config_ModelDefs_ModelDef_FactoryClass                = new XML( "factory-class",      Config_ModelDefs_ModelDef );
   public static final XML Config_ModelDefs_ModelDef_Scope                       = new XML( "scope",              Config_ModelDefs_ModelDef );
   public static final XML Config_ModelDefs_ModelDef_InitOnStartUp               = new XML( "init-on-startup",    Config_ModelDefs_ModelDef );

   public static final XML Config_Templating                                     = new XML( "templating",            Config );
   public static final XML Config_Templating_TemplatePath                        = new XML( "template-path",         Config_Templating );
   public static final XML Config_Templating_TemplatePath_Directory              = new XML( "directory",             Config_Templating_TemplatePath );
   public static final XML Config_Templating_TemplatePath_LoaderClass            = new XML( "loader-class",          Config_Templating_TemplatePath );
   public static final XML Config_Templating_TemplatePath_LoaderClass_Class      = new XML( "class",                 Config_Templating_TemplatePath_LoaderClass );
   public static final XML Config_Templating_TemplatePath_LoaderClass_Path       = new XML( "path",                  Config_Templating_TemplatePath_LoaderClass );
   public static final XML Config_Templating_TemplatePath_WebAppPath             = new XML( "web-app-path",          Config_Templating_TemplatePath );
   public static final XML Config_Templating_Locale                              = new XML( "locale",                Config_Templating );
   public static final XML Config_Templating_Locale_ISOLanguage                  = new XML( "iso-language",          Config_Templating_Locale );
   public static final XML Config_Templating_Locale_ISOCountry                   = new XML( "iso-country",           Config_Templating_Locale );
   public static final XML Config_Templating_ExposePublicFields                  = new XML( "expose-public-fields",  Config_Templating );
   public static final XML Config_Templating_TemplatingEngine                    = new XML( "templating-engine",     Config_Templating );
   public static final XML Config_Templating_TemplatingEngine_Class              = new XML( "class",                 Config_Templating_TemplatingEngine );

   public static final XML Config_ControllerMapping                              = new XML( "controller-mapping",          Config );
   public static final XML Config_ControllerMapping_URLToClassMap                = new XML( "url-to-class-map",            Config_ControllerMapping );
   public static final XML Config_ControllerMapping_URLToClassMap_URLPattern     = new XML( "url-pattern",                 Config_ControllerMapping_URLToClassMap );
   public static final XML Config_ControllerMapping_URLToClassMap_ClassPackages  = new XML( "class-packages",              Config_ControllerMapping_URLToClassMap );
   public static final XML Config_ControllerMapping_URLToClassMap_ClassPattern   = new XML( "class-pattern",               Config_ControllerMapping_URLToClassMap );
   public static final XML Config_ControllerMapping_DefaultHandlerMethod         = new XML( "default-handler-method",      Config_ControllerMapping );
   public static final XML Config_ControllerMapping_IgnoreHandlerMethodCase      = new XML( "ignore-handler-method-case",  Config_ControllerMapping );

   public static final XML Config_ViewMapping                                    = new XML( "view-mapping",                Config );
   public static final XML Config_ViewMapping_URLToClassMap                      = new XML( "url-to-class-map",            Config_ViewMapping );
   public static final XML Config_ViewMapping_URLToClassMap_URLPattern           = new XML( "url-pattern",                 Config_ViewMapping_URLToClassMap );
   public static final XML Config_ViewMapping_URLToClassMap_ClassPackages        = new XML( "class-packages",              Config_ViewMapping_URLToClassMap );
   public static final XML Config_ViewMapping_URLToClassMap_ClassPattern         = new XML( "class-pattern",               Config_ViewMapping_URLToClassMap );

   public static final XML Config_RedirectMapping                                = new XML( "redirect-mapping",            Config );
   public static final XML Config_RedirectMapping_ClassToURLMap                  = new XML( "class-to-url-map",            Config_RedirectMapping );
   public static final XML Config_RedirectMapping_ClassToURLMap_ClassPackages    = new XML( "class-packages",              Config_RedirectMapping_ClassToURLMap );
   public static final XML Config_RedirectMapping_ClassToURLMap_ClassPattern     = new XML( "class-pattern",               Config_RedirectMapping_ClassToURLMap );
   public static final XML Config_RedirectMapping_ClassToURLMap_URLFormat        = new XML( "url-format",                  Config_RedirectMapping_ClassToURLMap );
   public static final XML Config_RedirectMapping_ClassToURLMap_URLFormatAlt     = new XML( "url-format-alt",              Config_RedirectMapping_ClassToURLMap );
   public static final XML Config_RedirectMapping_URLBase                        = new XML( "url-base",                    Config_RedirectMapping );

   public static final XML Config_ControllerResolver                             = new XML( "controller-resolver",         Config );
   public static final XML Config_ControllerResolver_Class                       = new XML( "class",                       Config_ControllerResolver );

   public static final XML Config_ViewResolver                                   = new XML( "view-resolver",               Config );
   public static final XML Config_ViewResolver_Class                             = new XML( "class",                       Config_ViewResolver );

   public static final XML Config_RedirectResolver                               = new XML( "redirect-resolver",           Config );
   public static final XML Config_RedirectResolver_Class                         = new XML( "class",                       Config_RedirectResolver );

   public static final XML Config_FileUpload                                     = new XML( "file-upload",                    Config );
   public static final XML Config_FileUpload_MaxUploadSize                       = new XML( "max-upload-size",                Config_FileUpload );
   public static final XML Config_FileUpload_StoreFileOnDiskThreshold            = new XML( "store-file-on-disk-threshold",   Config_FileUpload );
   public static final XML Config_FileUpload_UploadedFileStorageDir              = new XML( "uploaded-file-storage-dir",      Config_FileUpload );

   public final   String   OPEN;
   public final   String   CLOSE;
   public final   String   OPEN_IND;
   public final   String   CLOSE_IND;
   public final   String   PATTERN;
   public final   int      LEVEL;

   private boolean _bIsLeaf;

   private XML( String sTagName )
   {
      OPEN        = "<" + sTagName + ">";
      CLOSE       = "</" + sTagName + ">";
      PATTERN     = sTagName;
      LEVEL       = 0;
      OPEN_IND    = OPEN;
      CLOSE_IND   = CLOSE;

      _bIsLeaf    = true;
   }

   private XML( String sTagName, XML oParent )
   {
      OPEN        = "<" + sTagName + ">";
      CLOSE       = "</" + sTagName + ">";
      PATTERN     = oParent.PATTERN + "/" + sTagName;
      LEVEL       = oParent.LEVEL + 1;

      StringBuffer oOPEN_INDBuffer = new StringBuffer();
      for ( int i = 0; i < LEVEL; i++ )
      {
         oOPEN_INDBuffer.append( "\t" );
      }
      oOPEN_INDBuffer.append( OPEN );
      OPEN_IND = oOPEN_INDBuffer.toString();

      StringBuffer oCLOSE_INDBuffer = new StringBuffer();
      for ( int i = 0; i < LEVEL; i++ )
      {
         oCLOSE_INDBuffer.append( "\t" );
      }
      oCLOSE_INDBuffer.append( CLOSE );
      CLOSE_IND = oCLOSE_INDBuffer.toString();

      _bIsLeaf = true;
      oParent._bIsLeaf = false;
   }

   public String toXML( int iValue )
   {
      return toXML( new Integer( iValue ) );
   }

   public String toXML( boolean iValue )
   {
      return toXML( Boolean.valueOf( iValue ) );
   }

   public String toXML( Object oValue )
   {
      StringBuffer   oBuffer = new StringBuffer();

      if ( _bIsLeaf )
      {
         if ( oValue != null )
         {
            oBuffer.append( "\n" );
            for ( int i = 0; i < LEVEL; i++ )
            {
               oBuffer.append( "\t" );
            }
            oBuffer.append( OPEN );
            oBuffer.append( oValue );
            oBuffer.append( CLOSE );
         }
      }
      else
      {
         oBuffer.append( "\n" );
         for ( int i = 0; i < LEVEL; i++ )
         {
            oBuffer.append( "\t" );
         }
         oBuffer.append( OPEN );

         oBuffer.append( oValue );

         oBuffer.append( "\n" );
         for ( int i = 0; i < LEVEL; i++ )
         {
            oBuffer.append( "\t" );
         }
         oBuffer.append( CLOSE );
      }

      return oBuffer.toString();
   }
}

// EOF