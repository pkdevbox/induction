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
package com.acciente.induction.init.config;

/**
 * This interface abstracts the mechanism used to load the configuration settings
 * used by the dispatcher.<p>
 *
 * A class implementing this interface is expected to have a single public contructor
 * adhering to the following convention:<p>
 *   - the single constructor should accepts no arguments or<p>
 *   - the single constructor should declare formal parameters using only the
 *     following types:<p>
 *     - javax.servlet.ServletContext<p>
 *     - javax.servlet.ServletConfig<p>
 *
 * @created Mar 15, 2008
 *
 * @author Adinath Raveendra Raj
 */
public interface ConfigLoader
{
   Config getConfig() throws ConfigLoaderException;
}

// EOF
