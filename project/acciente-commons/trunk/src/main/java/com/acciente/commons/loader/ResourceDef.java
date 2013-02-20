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
 * This interface abstracts access to resource to be accessed via the classloader
 *
 * @created Sep 30, 2008
 *
 * @author Adinath Raveendra Raj
 */
public interface ResourceDef
{
   /**
    * Returns a fully qualified class name of the that this object manages
    *
    * @return a string class name
    */
   String      getResourceName();

   /**
    * Returns true if the underlying source of the class data has changed since the
    * last call to getData()
    *
    * @return true if changed, false otherwise
    */
   boolean     isModified();

   /**
    * Reloads the class byte code from the underlying source
    */
   void        reload();

   /**
    * This method loads and returns the contents of this resource
    *
    * @return the contents of the resource as a byte array
    */
   byte[]      getContent();
}

// EOF