/**
 *   Copyright 2008 Acciente, LLC
 *
 *   Acciente, LLC licenses this file to you under the
 *   Apache License, Version 2.0 (the "License"); you
 *   may not use this file except in compliance with the
 *   License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in
 *   writing, software distributed under the License is
 *   distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 *   OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing
 *   permissions and limitations under the License.
 */
package com.acciente.commons.loader;

/**
 * This is an interface that abstracts the loading of class definitions. 
 *
 * @created Feb 27, 2008
 *
 * @author Adinath Raveendra Raj
 */
public interface ClassDefLoader
{
   /**
    * Load the byte code and other information about the specified class
    *
    * @param sClassName the name of the class for which a definition is requested
    * @return a class definition if this class definition loader is able to locate and successfully
    * load the byte code for the class. If the class could not be located a null is returned, if the
    * class was located but the load failed an exception is thrown.
    * @throws ClassNotFoundException if this loader was able to locate the class definition was
    * unable to sucessfully load it
    */
   ClassDef    getClassDef( String sClassName )
      throws ClassNotFoundException;
}

// EOF