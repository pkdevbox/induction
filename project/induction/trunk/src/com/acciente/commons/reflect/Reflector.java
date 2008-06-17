package com.acciente.commons.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A set of reflection utility methods.
 *
 * Log
 * Mar 15, 2008 APR  -  created
 */
public class Reflector
{
   private static Map __oReflectorCache = new HashMap();

   /**
    * Returns the methods in the specified class with the specified method name.
    *
    * @param oClass the class to reflect into
    * @param sMethodName the name of the method
    * @param bIgnoreCase true if the case of the method should be ignored
    * @return an array of method objects
    */
   public static Method[] getMethods( Class oClass, String sMethodName, boolean bIgnoreCase )
   {
      CacheKey_getMethods  oKey;
      Method[]             aoMethods;

      // first check the cache
      oKey        = new CacheKey_getMethods( oClass, sMethodName, bIgnoreCase );
      aoMethods   = ( Method[] ) __oReflectorCache.get( oKey );

      if ( aoMethods == null )
      {
         // not in cache, so compute
         ArrayList oMethodList = new ArrayList();

         for ( int i = 0; i < oClass.getMethods().length; i++ )
         {
            Method oMethod = oClass.getMethods()[ i ];

            if ( bIgnoreCase
                  ? oMethod.getName().equalsIgnoreCase( sMethodName )
                  : oMethod.getName().equals( sMethodName )
               )
            {
               oMethodList.add( oMethod );
            }
         }

         aoMethods = new Method[ oMethodList.size() ];

         oMethodList.toArray( aoMethods );

         __oReflectorCache.put( oKey, aoMethods );
      }

      return aoMethods;
   }

   private static class CacheKey_getMethods
   {
      private  Class    _oClass;
      private  String   _sMethodName;
      private  boolean  _bIgnoreCase;

      private CacheKey_getMethods( Class oClass, String sMethodName, boolean bIgnoreCase )
      {
         _oClass        =  oClass;
         _sMethodName   =  sMethodName;
         _bIgnoreCase   =  bIgnoreCase;
      }

      public boolean equals( Object oOther )
      {
         boolean  bEquals = false;

         if ( oOther instanceof CacheKey_getMethods )
         {
            CacheKey_getMethods  oTypedOther = ( CacheKey_getMethods ) oOther;

            bEquals = _oClass.equals( oTypedOther._oClass )
                        && _sMethodName.equals( oTypedOther._sMethodName )
                        && _bIgnoreCase == oTypedOther._bIgnoreCase;
         }

         return bEquals;
      }

      public int hashCode()
      {
         // this hash satisfies the contract but does not try hard to prevent collisions
         // which I am not convinced is likely
         return _oClass.hashCode() + _sMethodName.hashCode() + ( _bIgnoreCase ? 0 : 1 );
      }
   }
}

// EOF