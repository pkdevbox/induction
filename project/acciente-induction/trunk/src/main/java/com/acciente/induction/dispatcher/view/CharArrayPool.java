/*
 * Copyright 2008-2013 Acciente, LLC
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
