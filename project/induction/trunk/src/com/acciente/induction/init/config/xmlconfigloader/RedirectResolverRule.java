package com.acciente.induction.init.config.xmlconfigloader;

import com.acciente.induction.init.config.Config;
import com.acciente.commons.lang.Strings;
import org.apache.commons.digester.Rule;

/**
 * RedirectResolverRule
 *
 * Log
 * Jun 21, 2008 APR  -  created
 */
public class RedirectResolverRule extends Rule
{
   private  Config.RedirectResolver    _oRedirectResolver;
   private  String                     _sClassName;

   public RedirectResolverRule( Config.RedirectResolver oRedirectResolver )
   {
      _oRedirectResolver = oRedirectResolver;
   }

   public void end( String sNamespace, String sName ) throws XMLConfigLoaderException
   {
      if ( Strings.isEmpty( _sClassName ) )
      {
         throw new XMLConfigLoaderException( "config > redirect-resolver > class: must specify a class name" );
      }

      _oRedirectResolver.setClassName( _sClassName );
   }

   public ParamClassRule createParamClassRule()
   {
      return new ParamClassRule();
   }

   private class ParamClassRule extends Rule
   {
      public void body( String sNamespace, String sName, String sText ) throws XMLConfigLoaderException
      {
         _sClassName = sText;
      }
   }
}

// EOF