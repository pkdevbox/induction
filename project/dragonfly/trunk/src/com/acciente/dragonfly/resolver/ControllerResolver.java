package com.acciente.dragonfly.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This interface is used to abstract the algorithm used to map a HTTP request to a specific
 * controller invocation.
 *
 * A class implementing this interface is expected to have a single public contructor
 * adhering to the following convention:
 *   - the single constructor should accepts no arguments or
 *   - the single constructor should declare formal parameters using only the
 *     following types,
 *     - javax.servlet.ServletContext
 *     - javax.servlet.ServletConfig
 *
 * Log
 * Mar 14, 2008 APR  -  created
 */
public interface ControllerResolver
{
   /**
    * This method should resolve the class name of the controller and method name within same
    * to be invoked in response to the specified HTTP request
    *
    * @param oRequest the HTTP request context in which the resolution is requested
    * @return  an object containing the class name of the controller to be invoked and
    *          the method name within same
    */
   Resolution resolve( HttpServletRequest oRequest );

   /**
    * Log
    * Mar 14, 2008 APR  -  created
    */
   public static class Resolution
   {
      private String    _sClassName;
      private String    _sMethodName;
      private boolean   _bIsIgnoreMethodNameCase;

      public Resolution( String sClassName, String sMethodName, boolean bIsIgnoreMethodNameCase )
      {
         if ( sClassName == null )
         {
            throw new IllegalArgumentException( "Controller resolution must define a class name" );
         }

         _sClassName                =  sClassName;
         _sMethodName               =  sMethodName;
         _bIsIgnoreMethodNameCase   =  bIsIgnoreMethodNameCase;
      }

      public String getClassName()
      {
         return _sClassName;
      }

      public String getMethodName()
      {
         return _sMethodName;
      }

      public boolean isIgnoreMethodNameCase()
      {
         return _bIsIgnoreMethodNameCase;
      }
   }
}
