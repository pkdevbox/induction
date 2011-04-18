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
package com.acciente.induction.template;

import com.acciente.induction.view.Template;

import java.io.IOException;
import java.io.Writer;

/**
 * This interface is used to abstract access to templating engine used.
 * <p>
 * A class implementing this interface is expected to have a single public contructor
 * adhering to the following convention:<p>
 *   - the single constructor should accepts no arguments or<p>
 *   - the single constructor should declare formal parameters using only the
 *     following types:<p>
 *     - javax.servlet.ServletConfig<p>
 *     - com.acciente.induction.init.config.Config.Templating<p>
 *
 * @created Apr 17, 2008
 *
 * @author Adinath Raveendra Raj
 */
public interface TemplatingEngine
{
   /**
    * This method is used to process a template via this instance of the templating engine
    * @param oTemplate the template object to process
    */
   public void process( Template oTemplate, Writer oWriter ) throws TemplatingEngineException, IOException;
}

// EOF