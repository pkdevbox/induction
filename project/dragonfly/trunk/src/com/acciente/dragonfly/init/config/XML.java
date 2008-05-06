package com.acciente.dragonfly.init.config;

/**
 * Log
 * Apr 27, 2008 APR  -  created
 */
public class XML
{
   // todo: refactor to use this class

   public static final XML Config   = new XML( "config" );

   public static final XML Config_JavaClassPath       = new XML( "java-class-path" );
   public static final XML Config_Templating          = new XML( "templating" );
   public static final XML Config_ModelDefs           = new XML( "model-defs" );
   public static final XML Config_ControllerResolver  = new XML( "controller-resolver" );
   public static final XML Config_FileUpload          = new XML( "file-upload" );

   public final String  OPEN;
   public final String  CLOSE;

   public XML( String sTagName )
   {
      OPEN  = "<" + sTagName + ">";
      CLOSE = "</" + sTagName + ">";
   }
}

// EOF