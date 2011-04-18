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
package com.acciente.commons.loader;

import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * A classloader to load a class using the byte code data
 *
 * @created Feb 23, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ByteCodeClassLoader extends SecureClassLoader
{
   private Map    _oClassDefs =  new HashMap();

   /**
    * Creates a class loader with no parent class loader, this is expected to
    * cause the system class loader to be used as the parent class loader
    */
   public ByteCodeClassLoader()
   {
      super();
   }

   /**
    * Creates a class loader that delegates to the specified parent class loader
    *
    * @param oParentClassLoader the parent class loader
    */
   public ByteCodeClassLoader( ClassLoader oParentClassLoader )
   {
      super( oParentClassLoader );
   }

   /**
    * Add a class defined by its byte code to this loader.
    *
    * @param sClassName the class name
    * @param ayClassByteCode the byte code buffer
    */
   public void addClassDef( String sClassName, byte[] ayClassByteCode )
   {
      if ( _oClassDefs.get( sClassName ) != null )
      {
         throw new IllegalArgumentException( "Byte code definition already added for class name: " + sClassName );
      }
      _oClassDefs.put( sClassName, ayClassByteCode );
   }

   /**
    * Override the default implementation of the standard findClass() method to load in our classes
    * defined via addClassDef()
    *
    * @param sClassName the name of the class to load
    * @return a newly loaded Class object representing sClassName
    * @throws ClassNotFoundException thrown if this method was unable to load a clas corresponding to sClassName
    */
   protected Class findClass( String sClassName ) throws ClassNotFoundException
   {
      byte[]   ayClassByteCode;
      Class    oClass;

      if ( ( ayClassByteCode = ( byte[] ) _oClassDefs.get( sClassName ) ) == null )
      {
         throw new ClassNotFoundException( "No byte code definition found for class name: " + sClassName );
      }

      oClass = defineClass( sClassName, ayClassByteCode, 0, ayClassByteCode.length );

      // we can release our reference to the byte code, since we won't need it again
      _oClassDefs.put( sClassName, null );

      return oClass;
   }
}

// EOF