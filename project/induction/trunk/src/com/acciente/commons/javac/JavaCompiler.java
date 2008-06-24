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
package com.acciente.commons.javac;

import java.io.File;
import java.io.Reader;
import java.io.IOException;

/**
 * This class abstracts the interface to a java compiler. The motivation for this
 * class was to create a unified interface to diffrent compiler providers such as
 * Sun JDK versions: 1.3.x, 1.4.x and 1.6.x, I discovered the apache-commons-jci
 * project a day after I wrote this, but decided to use this implementation for now.
 *
 * @created Feb 20, 2008
 *
 * @author Adinath Raveendra Raj
 */
public interface JavaCompiler
{
	/**
    * Used to provide a directory to which the compiler may,
    * if necessary, write any intermediate files required during its
    * operation
    *
    * @param oWorkingDir should point to a valid folder on the filesys
    */
   void setWorkingDir( File oWorkingDir );

   /**
    * This method compiles the source code provided via the
    * oSourceReader parameter. isCompileOK() should be called
    * to check if the compilation succeeded
    *
    * @param sClassname
    *@param oSourceReader @throws IOException
    */
   void compile( String sClassname, Reader oSourceReader )
      throws IOException;

   /**
    *  Returns if the last compilation was sucessful
    *
    * @return true if sucessful, false otherwise
    */
   boolean isCompileOK();

   /**
    * Returns the compiled classes. The results are in objects of
    * type CompiledClass. Each object contains one class file.
    *
    * @return an array CompiledClass objects
    */
   CompiledClass[] getCompiledClasses();

   /**
    * Any error codes returned by the java compiler. Some compilers
    * may not set an error code after the compile in which case this
    * method will always return 0.
    *
    * @return a non-zero integer for compiler dependent error code, or zero
    * if there were no compiler errors or if the compiler does not set error
    * code
    */
   int getErrorCode();

   /**
    * Returns the textual output of any errors or warning during compilation
    *
    * @return a string with human readable error and warning messages
    */
   String getMessages();
}
