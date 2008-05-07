package com.acciente.dragonfly.init.configloader;

import org.apache.commons.digester.Rule;
import com.acciente.dragonfly.init.config.Config;

/**
 * TemplatingRule
 *
 * Log
 * May 6, 2008 APR  -  created
 */
public class TemplatingRule extends Rule
{
   private  Config.Templating    _oTemplating;

   public TemplatingRule( Config.Templating oTemplating )
   {
      _oTemplating = oTemplating;
   }

   public class TemplatePathRule
   {

   }

   public class LocaleRule
   {

   }

   public class TemplatingEngineRule
   {

   }
}

// EOF