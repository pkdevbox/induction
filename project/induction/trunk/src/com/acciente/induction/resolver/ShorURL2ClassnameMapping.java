package com.acciente.induction.resolver;

import java.util.regex.Pattern;
import java.util.Map;

/**
 * Internal.
 *
 * ShorURL2ClassnameMapping
*
* @author Adinath Raveendra Raj
* @created Mar 30, 2009
*/
class ShorURL2ClassnameMapping
{
   private  Pattern  _oURLPattern;
   private  Map      _oShortName2ClassnameMap;

   ShorURL2ClassnameMapping( Pattern oURLPattern, Map oShortName2ClassnameMap )
   {
      _oURLPattern               = oURLPattern;
      _oShortName2ClassnameMap   = oShortName2ClassnameMap;
   }

   Pattern getURLPattern()
   {
      return _oURLPattern;
   }

   Map getShortName2ClassnameMap()
   {
      return _oShortName2ClassnameMap;
   }
}
