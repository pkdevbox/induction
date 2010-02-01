package com.acciente.induction.dispatcher.view;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * CharArrayPool
 *
 * @author Adinath Raveendra Raj
 * @created Dec 1, 2009
 */
public class CharArrayPool
{
   private int    _iCharArraySize;
   private Set    _oCharArrayPool   = new HashSet();

   public CharArrayPool( int iCharArraySize )
   {
      _iCharArraySize = iCharArraySize;
   }

   public synchronized char[] acquire()
   {
      char[] acCharArray;

      if ( _oCharArrayPool.size() == 0 )
      {
         acCharArray = new char[ _iCharArraySize ];
      }
      else
      {
         // get a CharArray from the pool
         Iterator oPoolIterator = _oCharArrayPool.iterator();

         acCharArray = ( char[] ) oPoolIterator.next();

         // remove the item we are about to return from the pool
         oPoolIterator.remove();
      }

      return acCharArray;
   }

   public synchronized void release( char[] acCharArray )
   {
      _oCharArrayPool.add( acCharArray );
   }
}
