package com.acciente.commons.loader;

/**
 * Class description goes here
 * <p/>
 * Log
 * Feb 27, 2008 APR -  created
 */
public interface ClassDefLoader
{
   ClassDef    getClassDef( String sClassName )
      throws ClassNotFoundException;
}

// EOF