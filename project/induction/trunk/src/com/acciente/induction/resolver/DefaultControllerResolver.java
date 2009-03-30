package com.acciente.induction.resolver;

import com.acciente.induction.init.config.Config;

import javax.servlet.http.HttpServletRequest;

/**
 * DefaultControllerResolver
 *
 * @author Adinath Raveendra Raj
 * @created Mar 29, 2009
 */
public class DefaultControllerResolver implements ControllerResolver
{
   public DefaultControllerResolver( Config.ControllerResolver oConfig )
   {
   }

   public Resolution resolve( HttpServletRequest oRequest )
   {
      return null;
   }
}
