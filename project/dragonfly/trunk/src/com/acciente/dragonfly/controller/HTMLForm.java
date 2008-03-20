package com.acciente.dragonfly.controller;

import com.acciente.dragonfly.dispatcher.form.File;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Log
 * Feb 18, 2008 APR  -  created
 */
public class HTMLForm implements Form
{
   private HttpServletRequest    _oHttpServletRequest;
   private Map                   _oFormParams;

   public HTMLForm( HttpServletRequest oHttpServletRequest )
   {
      _oHttpServletRequest = oHttpServletRequest;
   }

   public String getString( String sParamName )
   {
      return ( String ) getParamValue( sParamName );
   }

   public int getInteger( String sParamName )
   {
      return ( ( Integer ) getParamValue( sParamName ) ).intValue();
   }

   public float getFloat( String sParamName )
   {
      return ( ( Float ) getParamValue( sParamName ) ).floatValue();
   }

   public long getLong( String sParamName )
   {
      return ( ( Long ) getParamValue( sParamName ) ).longValue();
   }

   public boolean getBoolean( String sParamName )
   {
      return ( ( Boolean ) getParamValue( sParamName ) ).booleanValue();
   }

   public File getFile( String sParamName )
   {
      return ( File ) getParamValue( sParamName );
   }

   private Object getParamValue( String sParamName )
   {
      if ( _oFormParams == null )
      {
         // todo: implement call to parser module
      }
      return _oFormParams.get( sParamName );
   }
}

// EOF