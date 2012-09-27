/*
 * Copyright 2008-2012 Acciente, LLC
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
 * This interface abstracts access to a object capable of providing the byte codes for
 * a java class definition. An example of such an object is a java source file, another
 * example is a java class file.
 *
 * @created Feb 27, 2008
 *
 * @author Adinath Raveendra Raj
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