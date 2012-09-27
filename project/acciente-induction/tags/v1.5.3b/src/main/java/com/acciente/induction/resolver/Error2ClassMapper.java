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
package com.acciente.induction.resolver;

/**
 * Error2ClassMapper
 *
 * @author Adinath Raveendra Raj
 * @created Nov 27, 2009
 */
public class Error2ClassMapper
{
   private  Class    _oErrorClass;
   private  boolean  _bIncludeDerived;
   private  String   _sControllerClassName;
   private  String   _sControllerMethodName;

   public Error2ClassMapper( String       sErrorClassName,
                             boolean      bIncludeDerived,
                             String       sControllerClassName,
                             String       sControllerMethodName,
                             ClassLoader  oClassLoader ) throws ClassNotFoundException
   {
      _oErrorClass            = oClassLoader.loadClass( sErrorClassName );
      _bIncludeDerived        = bIncludeDerived;
      _sControllerClassName   = sControllerClassName;
      _sControllerMethodName  = sControllerMethodName;
   }

   ClassAndMethod mapError2Class( Throwable oThrowable )
   {
      Class oThrowableClass = oThrowable.getClass();

      if ( _bIncludeDerived
           ? _oErrorClass.isAssignableFrom( oThrowableClass )
           : _oErrorClass.equals( oThrowableClass ) )
      {
         return new ClassAndMethod( _sControllerClassName, _sControllerMethodName );
      }

      return null;
   }
}
