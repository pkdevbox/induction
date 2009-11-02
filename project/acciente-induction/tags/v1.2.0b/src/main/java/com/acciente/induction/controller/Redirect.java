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
package com.acciente.induction.controller;

import java.util.Map;

/**
 * Used to instruct the client to redirect.
 *
 * @created Jun 21, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class Redirect
{
   private  Class       _oTargetClass;
   private  String      _sTargetMethodName;
   private  String      _sTargetURL;
   private  Map         _oURLQueryParameters;

   /**
    * A redirect object defined in terms of the controller or view class to which the client should redirect,
    * Induction uses the redirect resolver to map the controller or view name to a URL.
    *
    * @param oTargetClass a class object representing a class that implements the Controller interface or one of the
    * view interfaces. 
    */
   public Redirect( Class oTargetClass )
   {
      // we cannot validate here since a view can be of any type
      _oTargetClass = oTargetClass;
   }

   /**
    * A redirect object defined in terms of the controller or view class to which the client should redirect,
    * Induction uses the redirect resolver to map the controller or view name to a URL.
    *
    * @param oTargetClass a class object representing a class that implements the Controller interface or one of the
    * view interfaces.
    * @param oURLQueryParameters a map to be converted to URL query parameters
    */
   public Redirect( Class oTargetClass, Map oURLQueryParameters )
   {
      validateURLQueryParameters( oURLQueryParameters );

      // we cannot validate here since a view can be of any type
      _oTargetClass        = oTargetClass;
      _oURLQueryParameters = oURLQueryParameters;
   }

   /**
    * A redirect object defined in terms of the controller and method to which the client should redirect,
    * Induction uses the redirect resolver to map the controller or view name to a URL. This method should
    * not be used to redirect to a view since a methid name does not make sense for a view.
    *
    * @param oTargetClass a class representing that implements the Controller interface
    * @param sTargetMethodName a specific method name in the controller that the client
    * should redirect to
    */
   public Redirect( Class oTargetClass, String sTargetMethodName )
   {
      if ( ! Controller.class.isAssignableFrom( oTargetClass ) )
      {
         throw new IllegalArgumentException( "The form of the redirect used requires a controller class, "
                                             + oTargetClass
                                             + " is not a controller class");
      }

      _oTargetClass        = oTargetClass;
      _sTargetMethodName   = sTargetMethodName;
   }

   /**
    * A redirect object defined in terms of the controller and method to which the client should redirect,
    * Induction uses the redirect resolver to map the controller or view name to a URL. This method should
    * not be used to redirect to a view since a methid name does not make sense for a view.
    *
    * @param oTargetClass a class representing that implements the Controller interface
    * @param sTargetMethodName a specific method name in the controller that the client
    * should redirect to
    * @param oURLQueryParameters a map to be converted to URL query parameters
    */
   public Redirect( Class oTargetClass, String sTargetMethodName, Map oURLQueryParameters )
   {
      if ( ! Controller.class.isAssignableFrom( oTargetClass ) )
      {
         throw new IllegalArgumentException( "The form of the redirect used requires a controller class, "
                                             + oTargetClass
                                             + " is not a controller class");
      }

      validateURLQueryParameters( oURLQueryParameters );

      _oTargetClass        = oTargetClass;
      _sTargetMethodName   = sTargetMethodName;
      _oURLQueryParameters = oURLQueryParameters;
   }

   /**
    * A redirect object defined directly in terms of the URL to which the client should
    * redirect to. The URL is still passed thru the redirect resolver which may choose
    * to complete partial URLs or provide other services. The defualt resolver prefixes
    * the URL the URL base parameter defined in the induction config.
    *
    * @param sURL a string representing a complete or partial URL
    */
   public Redirect( String sURL )
   {
      _sTargetURL = sURL;
   }

   /**
    * A redirect object defined directly in terms of the URL to which the client should
    * redirect to. The URL is still passed thru the redirect resolver which may choose
    * to complete partial URLs or provide other services. The defualt resolver prefixes
    * the URL the URL base parameter defined in the induction config.
    *
    * @param sURL a string representing a complete or partial URL
    */
   public Redirect( String sURL, Map oURLQueryParameters )
   {
      validateURLQueryParameters( oURLQueryParameters );

      _sTargetURL          = sURL;
      _oURLQueryParameters = oURLQueryParameters;
   }

   public Class getTargetClass()
   {
      return _oTargetClass;
   }

   public String getTargetMethodName()
   {
      return _sTargetMethodName;
   }

   public String getTargetURL()
   {
      return _sTargetURL;
   }

   public Map getTargetURLQueryParameters()
   {
      return _oURLQueryParameters;
   }

   private void validateURLQueryParameters( Map oURLQueryParameters )
   {
      if ( oURLQueryParameters == null )
      {
         throw new IllegalArgumentException( "URL query parameters map cannot be null" );
      }
   }
}