package com.acciente.dragonfly.init.configloader;

import com.acciente.dragonfly.init.config.Config;
import com.acciente.dragonfly.init.config.ConfigLoaderException;
import org.apache.commons.digester.Rule;

/**
 * ModelDefsRule
 *
 * Log
 * May 3, 2008 APR  -  created
 */
public class ModelDefsRule extends Rule
{
   private  Config.ModelDefs    _oModelDefs;

   public ModelDefsRule( Config.ModelDefs oModelDefs )
   {
      _oModelDefs = oModelDefs;
   }

   public AddModelDefRule createAddModelDefRule()
   {
      return new AddModelDefRule();
   }

   public class AddModelDefRule extends Rule
   {
      private  String   _sModelClassName;
      private  String   _sModelFactoryClassName;
      private boolean   _bIsApplicationScope;
      private boolean   _bIsSessionScope;
      private boolean   _bIsRequestScope;

      public void end( String sNamespace, String sName )
      {
         _oModelDefs.addModelDef( _sModelClassName, _sModelFactoryClassName, _bIsApplicationScope, _bIsSessionScope, _bIsRequestScope  );
      }

      public ParamClassRule createParamClassRule()
      {
         return new ParamClassRule();
      }

      public ParamFactoryClassRule createParamFactoryClassRule()
      {
         return new ParamFactoryClassRule();
      }

      public ParamScopeRule createParamScopeRule()
      {
         return new ParamScopeRule();
      }

      private class ParamClassRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sModelClassName = sText;
         }
      }

      private class ParamFactoryClassRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText )
         {
            _sModelFactoryClassName = sText;
         }
      }

      private class ParamScopeRule extends Rule
      {
         public void body( String sNamespace, String sName, String sText ) throws ConfigLoaderException
         {
            if ( "application".equalsIgnoreCase( sText ) )
            {
               _bIsApplicationScope = true;
            }
            else if ( "session".equalsIgnoreCase( sText ) )
            {
               _bIsSessionScope = true;
            }
            else if ( "request".equalsIgnoreCase( sText ) )
            {
               _bIsRequestScope = true;
            }
            else
            {
               throw new ConfigLoaderException( "xml-config-loader: unrecognized scope: " + sText + " for model class: " + _sModelClassName );
            }
         }
      }
   }
}

// EOF