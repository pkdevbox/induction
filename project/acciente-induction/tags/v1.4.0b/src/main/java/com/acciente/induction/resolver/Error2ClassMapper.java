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
