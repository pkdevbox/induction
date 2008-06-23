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
package com.acciente.induction.init;

/**
 * This interface is used to provide access to centralized logger with having
 * to pass in the entire servlet object (which has the log() method) to each
 * class/method that needs access to the logger.
 *
 * Log
 * Mar 16, 2008 APR  -  created
 */
// todo: investigate the use of a more standard logging api
public interface Logger
{
   void log( String sMessage );

   void log( String sMessage, Throwable oThrowable );
}
