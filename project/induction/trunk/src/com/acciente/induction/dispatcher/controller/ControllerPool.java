/**
 *   Copyright 2009 Acciente, LLC
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
package com.acciente.induction.dispatcher.controller;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.init.Logger;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.ObjectFactory;
import com.acciente.commons.reflect.ParameterProviderException;

import javax.servlet.ServletConfig;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Internal.
 * This class manages a pool of Controller object instances. If a controller object is in the pool and it's underlying
 * class has not since reloaded the object in the pool is used, otherwise a new controller object is instantiated. There
 * is a single instance of this class per dispatcher servlet.
 *
 * @created Mar 20, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ControllerPool
{
   private Map             _oControllerCache = new Hashtable();
   private Logger          _oLogger;
   private ClassLoader     _oClassLoader;
   private ServletConfig   _oServletConfig;

   public ControllerPool( ClassLoader oClassLoader, ServletConfig oServletConfig, Logger oLogger )
   {
      _oClassLoader     = oClassLoader;
      _oServletConfig   = oServletConfig;
      _oLogger          = oLogger;
   }

   /**
    * Returns a controller from the controller pool, instantiating if needed
    *
    * @param sControllerClassName fully qualified controller class name
    * @return an instance of a controller object
    *
    * @throws ClassNotFoundException propagated exception
    * @throws InvocationTargetException propagated exception
    * @throws IllegalAccessException propagated exception
    * @throws ConstructorNotFoundException propagated exception
    * @throws InstantiationException propagated exception
    * @throws ParameterProviderException propagated exception
    */
   public Controller getController( String sControllerClassName )
      throws ClassNotFoundException, InvocationTargetException, ParameterProviderException, ConstructorNotFoundException, InstantiationException, IllegalAccessException
   {
      // the class definition may have changed so we have to load the class unconditionally our
      // classloader caches so if the class is unchanged this call just returns the last loaded class
      Class oLatestControllerClass = _oClassLoader.loadClass( sControllerClassName );

      // check if we have a cached controller instance
      Controller  oController = ( Controller ) _oControllerCache.get( sControllerClassName );

      if ( oController == null )
      {
         // no controller instance in cache so create one and cache
         _oControllerCache.put( sControllerClassName, oController = createController( oLatestControllerClass ) );
      }
      else
      {
         // ok there is a cached entry, check if the controller class has changed since the instance was cached
         if ( oController.getClass().hashCode() != oLatestControllerClass.hashCode() )
         {
            // yes the class has been reloaded so decommission the previous controller
            // and then create a new controller instance
            ObjectFactory.destroyObject( oController );

            // we have to remove the invalid controller from the cache, otherwise if the createController()
            // below throws an exception an invalid controller would be left in the cache
            _oControllerCache.remove( sControllerClassName );

            // create a new controller and cache it
            _oControllerCache.put( sControllerClassName, oController = createController( oLatestControllerClass ) );
         }
      }

      return oController;
   }

   private Controller createController( Class oControllerClass )
      throws InvocationTargetException, ConstructorNotFoundException, ParameterProviderException, IllegalAccessException, InstantiationException
   {
      return ( Controller ) ObjectFactory.createObject( oControllerClass, new Object[]{ _oServletConfig }, null );
   }
}

// EOF