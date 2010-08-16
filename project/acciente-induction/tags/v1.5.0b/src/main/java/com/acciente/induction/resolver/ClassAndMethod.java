package com.acciente.induction.resolver;

/**
 * ClassAndMethod
*
* @author Adinath Raveendra Raj
* @created Nov 27, 2009
*/
public class ClassAndMethod
{
   private  String   _sClassName;
   private  String   _sMethodName;

   public ClassAndMethod( String sClassName, String sMethodName )
   {
      _sClassName    = sClassName;
      _sMethodName   = sMethodName;
   }

   public String getClassName()
   {
      return _sClassName;
   }

   public String getMethodName()
   {
      return _sMethodName;
   }
}
