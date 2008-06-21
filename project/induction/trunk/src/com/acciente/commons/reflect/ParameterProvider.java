package com.acciente.commons.reflect;

/**
 * This class ...
*
* Log
* Jun 20, 2008 APR  -  created
*/
public interface ParameterProvider
{
   /**
    * Returns the value for a parameter given the Parameter type
    *
    * @param oValueType a class object representing the parameter type
    * @return null or an object representing the parameter type
    * @throws Exception any exception that the implementation class may wish to propagate
    */
   public Object getParameter( Class oValueType ) throws Exception;
}

// EOF