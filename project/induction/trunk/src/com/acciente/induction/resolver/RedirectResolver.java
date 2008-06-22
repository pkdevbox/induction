package com.acciente.induction.resolver;

/**
 * This interface is used to abstract the algorithm used to map a redirect request to an actual URL
 *
 * A class implementing this interface is expected to have a single public contructor
 * adhering to the following convention:
 *   - the single constructor should accepts no arguments or
 *   - the single constructor should declare formal parameters using only the
 *     following types,
 *     - javax.servlet.ServletContext
 *
 * Log
 * Jun 21, 2008 APR  -  created
 */
public interface RedirectResolver
{
   /**
    * Called by Induction to resolve a redirect request defined on in terms of a target controller.
    *
    * @param oControllerClass  a class object representing a class that implements the Controller interface
    * @return a string represting a complete URL
    */
   public String resolve( Class oControllerClass );

   /**
    * Called by Induction to resolve a redirect request defined on in terms of a target controller
    * and a specific target method in the controller.
    *
    * @param oControllerClass  a class object representing a class that implements the Controller interface
    * @param sControllerMethodName a specific method name in the controller that the client
    * should redirect to
    * @return a string represting a complete URL
    */
   public String resolve( Class oControllerClass, String sControllerMethodName );

   /**
    * Called by Induction to resolve a redirect request defined on in terms of a target URL,
    * the URL may be a partial URL that this method is expected to complete. The URL may even
    * simply be a mnemonic reference that is mapped to a complete URL by this method.
    *
    * @param sURL a string representing a complete or partial URL
    * @return a string represting a complete URL
    */
   public String resolve( String sURL );
}

// EOF