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
package com.acciente.commons.loader;

/**
 * A simple implementation of ClassDef thats reads the definition from a compiled java class file.
 *
 * @author Adinath Raveendra Raj
 */
class ByteCodeClassDef implements ClassDef
{
   private String    _sClassName;
   private byte[]    _ayByteCode;

   ByteCodeClassDef( String sClassName, byte[] ayByteCode )
   {
      _sClassName =  sClassName;
      _ayByteCode =  ayByteCode;
   }

   public String getClassName()
   {
      return _sClassName;
   }

   public boolean isModified()
   {
      return false;
   }

   public void reload()
   {
      // this method does nothing in this container class
   }

   public byte[] getByteCode()
   {
      return _ayByteCode;
   }

   public ClassDef[] getBundledClassDefs()
   {
      return null;
   }

   public String[] getReferencedClasses()
   {
      return null;
   }
}

// EOF