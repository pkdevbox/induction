package com.acciente.commons.loader;

/**
 * This interface abstracts access to a object capable of providing the byte codes for
 * a java class definition. An example of such an object is a java source file, another
 * example is a java class file.
 *
 * Log
 * Feb 27, 2008 APR -  created
 */
public interface ClassDef
{
   /**
    * Returns a fully qualified class name of the that this object manages
    *
    * @return a string class name
    */
   String      getClassName();

   /**
    * Returns true if the underlying source of the class data has changed since the
    * last call to getByteCode()
    *
    * @return true if changed, false otherwise
    */
   boolean     isModified();

   /**
    * Reloads the class byte code from the underlying class definition
    * @throws ClassNotFoundException
    */
   void        reload() throws ClassNotFoundException;

   /**
    * This method loads and returns the the byte code for class from the underlying
    * source.
    *
    * @return
    */
   byte[]      getByteCode();

   /**
    * Returns the definitions of any non-public package, private, or inner classes that came the this class
    *
    * @return if there are no bundled classes null is returned, otherwise an array of ClassDef objects is returned
    */
   ClassDef[]  getBundledClassDefs();

   /**
    * Returns the names of the classes that the byte code has references
    * @return a string array containing clasnames 
    */
   String[]    getReferencedClasses();
}

// EOF