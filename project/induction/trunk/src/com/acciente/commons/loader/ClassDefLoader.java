package com.acciente.commons.loader;

/**
 * Class description goes here
 * <p/>
 * Log
 * Feb 27, 2008 APR  -  created
 * May 21, 2008 APR  -  getClassDef() can now return null
 */
public interface ClassDefLoader
{
   /**
    * Load the byte code and other information about the specified class
    *
    * @param sClassName the name of the class for which a definition is requested
    * @return a class definition if this class definition loader is able to locate and successfully
    * load the byte code for the class. If the class could not be located a null is returned, if the
    * class was located but the load failed an exception is thrown.
    * @throws ClassNotFoundException if this loader was able to locate the class definition was
    * unable to sucessfully load it
    */
   ClassDef    getClassDef( String sClassName )
      throws ClassNotFoundException;
}

// EOF