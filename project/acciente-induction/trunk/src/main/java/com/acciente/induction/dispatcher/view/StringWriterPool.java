/*
 * Copyright 2008-2011 Acciente, LLC
 *
 * Acciente, LLC licenses this file to you under the
 * Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */
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
