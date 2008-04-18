package com.acciente.dragonfly.renderer;

import com.acciente.dragonfly.view.Template;

/**
 * This is the interface that should be implemented by template renderer modules
 *
 * Log
 * Apr 17, 2008 APR  -  created
 */
public interface TemplateRenderer
{
   /**
    * This method is called to request that this renderer renders the specified template
    * @param oTemplate the template object to render
    */
   public void render( Template oTemplate );   
}

// EOF