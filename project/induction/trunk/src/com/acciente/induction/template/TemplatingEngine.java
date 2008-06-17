package com.acciente.induction.template;

import com.acciente.induction.view.Template;

import java.io.IOException;
import java.io.Writer;

import freemarker.template.TemplateException;

/**
 * This interface is used to abstract access to templating engine used.
 *
 * A class implementing this interface is expected to have a single public contructor
 * adhering to the following convention:
 *   - the single constructor should accepts no arguments or
 *   - the single constructor should declare formal parameters using only the
 *     following types,
 *     - javax.servlet.ServletConfig
 *     - com.acciente.induction.init.config.Config.Templating
 *
 * Log
 * Apr 17, 2008 APR  -  created
 */
public interface TemplatingEngine
{
   /**
    * This method is used to process a template via this instance of the templating engine
    * @param oTemplate the template object to process
    */
   public void process( Template oTemplate, Writer oWriter ) throws IOException, TemplateException;
}

// EOF