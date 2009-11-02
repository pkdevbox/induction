package com.acciente.induction.resolver;

/**
 * FindReplaceDirective
 *
 * @author Adinath Raveendra Raj
 * @created Oct 18, 2009
 */
class FindReplaceDirective
{
   private  String   _sFindStr;
   private  String   _sReplaceStr;

   FindReplaceDirective( String sFindStr, String sReplaceStr )
   {
      _sFindStr      = sFindStr;
      _sReplaceStr   = sReplaceStr;
   }

   public String getFindString()
   {
      return _sFindStr;
   }

   public String getReplaceString()
   {
      return _sReplaceStr;
   }
}
