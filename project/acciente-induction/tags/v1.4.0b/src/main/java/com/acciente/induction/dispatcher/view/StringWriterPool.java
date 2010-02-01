package com.acciente.induction.dispatcher.view;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

/**
 * StringWriterPool
 *
 * @author Adinath Raveendra Raj
 * @created Dec 1, 2009
 */
public class StringWriterPool
{
   private int    _iStringWriterInitBufferSize;
   private Set    _oStringWriterPool = new HashSet();

   public StringWriterPool( int iStringWriterInitBufferSize )
   {
      _iStringWriterInitBufferSize = iStringWriterInitBufferSize;
   }

   public synchronized StringWriter acquire()
   {
      StringWriter   oStringWriter;

      if ( _oStringWriterPool.size() == 0 )
      {
         oStringWriter = new StringWriter( _iStringWriterInitBufferSize );
      }
      else
      {
         // get a StringWriter from the pool
         Iterator oPoolIterator = _oStringWriterPool.iterator();

         oStringWriter = ( StringWriter ) oPoolIterator.next();

         // remove the item we are about to return from the pool
         oPoolIterator.remove();
      }

      return oStringWriter;
   }

   public synchronized void release( StringWriter oStringWriter )
   {
      // reset the StringBuffer for reuse
      oStringWriter.getBuffer().setLength( 0 );

      // return the buffer back to the pool
      _oStringWriterPool.add( oStringWriter );
   }
}
