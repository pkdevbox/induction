package com.acciente.induction.init.config;

/**
 * Log
 * Apr 27, 2008 APR  -  created
 */
public class XML
{
   public static final XML Config                                                = new XML( "config" );

   public static final XML Config_JavaClassPath                                  = new XML( "java-class-path",    Config );
   public static final XML Config_JavaClassPath_CompiledDirectory                = new XML( "compiled-directory", Config_JavaClassPath );
   public static final XML Config_JavaClassPath_CompiledDirectory_Directory      = new XML( "directory",          Config_JavaClassPath_CompiledDirectory );
   public static final XML Config_JavaClassPath_CompiledDirectory_PackagePrefix  = new XML( "package-prefix",     Config_JavaClassPath_CompiledDirectory );
   public static final XML Config_JavaClassPath_SourceDirectory                  = new XML( "source-directory",   Config_JavaClassPath );
   public static final XML Config_JavaClassPath_SourceDirectory_Directory        = new XML( "directory",          Config_JavaClassPath_SourceDirectory );
   public static final XML Config_JavaClassPath_SourceDirectory_PackagePrefix    = new XML( "package-prefix",     Config_JavaClassPath_SourceDirectory );
   public static final XML Config_JavaClassPath_JavaCompiler                     = new XML( "java-compiler",      Config_JavaClassPath );
   public static final XML Config_JavaClassPath_JavaCompiler_Class               = new XML( "class",              Config_JavaClassPath_JavaCompiler );

   public static final XML Config_ModelDefs                                      = new XML( "model-defs",         Config );
   public static final XML Config_ModelDefs_ModelDef                             = new XML( "model-def",          Config_ModelDefs );
   public static final XML Config_ModelDefs_ModelDef_Class                       = new XML( "class",              Config_ModelDefs_ModelDef );
   public static final XML Config_ModelDefs_ModelDef_FactoryClass                = new XML( "factory-class",      Config_ModelDefs_ModelDef );
   public static final XML Config_ModelDefs_ModelDef_Scope                       = new XML( "scope",              Config_ModelDefs_ModelDef );

   public static final XML Config_Templating                                     = new XML( "templating",         Config );
   public static final XML Config_Templating_TemplatePath                        = new XML( "template-path",      Config_Templating );
   public static final XML Config_Templating_TemplatePath_Directory              = new XML( "directory",          Config_Templating_TemplatePath );
   public static final XML Config_Templating_TemplatePath_LoaderClass            = new XML( "loader-class",       Config_Templating_TemplatePath );
   public static final XML Config_Templating_TemplatePath_LoaderClass_Class      = new XML( "class",              Config_Templating_TemplatePath_LoaderClass );
   public static final XML Config_Templating_TemplatePath_LoaderClass_Path       = new XML( "path",               Config_Templating_TemplatePath_LoaderClass );
   public static final XML Config_Templating_TemplatePath_WebAppPath             = new XML( "web-app-path",       Config_Templating_TemplatePath );
   public static final XML Config_Templating_Locale                              = new XML( "locale",             Config_Templating );
   public static final XML Config_Templating_Locale_ISOLanguage                  = new XML( "iso-language",       Config_Templating_Locale );
   public static final XML Config_Templating_Locale_ISOCountry                   = new XML( "iso-country",        Config_Templating_Locale );
   public static final XML Config_Templating_TemplatingEngine                    = new XML( "templating-engine",  Config_Templating );
   public static final XML Config_Templating_TemplatingEngine_Class              = new XML( "class",              Config_Templating_TemplatingEngine );

   public static final XML Config_ControllerResolver                             = new XML( "controller-resolver",         Config );
   public static final XML Config_ControllerResolver_Class                       = new XML( "class",                       Config_ControllerResolver );
   public static final XML Config_ControllerResolver_DefaultHandlerMethod        = new XML( "default-handler-method",      Config_ControllerResolver );
   public static final XML Config_ControllerResolver_IgnoreHandlerMethodCase     = new XML( "ignore-handler-method-case",  Config_ControllerResolver );

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
      return toXML( Integer.valueOf( iValue ) );
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