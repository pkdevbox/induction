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
package com.acciente.commons.htmlform;

import java.util.Iterator;
import java.util.List;

/**
 * This class models a parameter on the HTML form. The form parser creates
 * an instance of this class for parameter in the form.
 *
 * @created Feb 16, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ParameterSpec
{
   private  String   _sIdentifier;
   private  List     _oMapKeys;
   private  String   _sDataType;
   private  boolean  _bIsList;

   ParameterSpec( String sIdentifier, String sDataType ) throws ParserException
   {
      validateIdentifierAndDataType( sIdentifier, sDataType );

      _sIdentifier   =  sIdentifier;
      _sDataType     =  sDataType;
      _bIsList       =  false;
      _oMapKeys      =  null;
   }

   ParameterSpec( String sIdentifier, String sDataType, boolean bIsList ) throws ParserException
   {
      validateIdentifierAndDataType( sIdentifier, sDataType );

      _sIdentifier   =  sIdentifier;
      _sDataType     =  sDataType;
      _bIsList       =  bIsList;
      _oMapKeys      =  null;
   }

   ParameterSpec( String sIdentifier, String sDataType, boolean bIsList, List oMapKeys ) throws ParserException
   {
      validateIdentifierAndDataType( sIdentifier, sDataType );

      _sIdentifier   =  sIdentifier;
      _sDataType     =  sDataType;
      _bIsList       =  bIsList;
      _oMapKeys      =  oMapKeys;
   }

   public String getIdentifier()
   {
      return _sIdentifier;
   }

   public String getDataType()
   {
      return _sDataType;
   }

   public List getMapKeys()
   {
      return _oMapKeys;
   }

   public boolean isStructured()
   {
      return _bIsList || ( _oMapKeys != null );
   }

   public boolean isList()
   {
      return _bIsList;
   }

   public boolean isMap()
   {
      return _oMapKeys != null;
   }

   public String toString()
   {
      StringBuffer oPSpecAsString = new StringBuffer();

      oPSpecAsString.append( _sDataType );
      oPSpecAsString.append( ":" );
      oPSpecAsString.append( _sIdentifier );

      if ( _oMapKeys != null )
      {
         for ( Iterator oIter = _oMapKeys.iterator(); oIter.hasNext(); )
         {
            oPSpecAsString.append( "[" );
            oPSpecAsString.append( oIter.next() );
            oPSpecAsString.append( "]" );
         }
      }

      if ( _bIsList )
      {
         oPSpecAsString.append( "[" );
         oPSpecAsString.append( "]" );
      }

      return oPSpecAsString.toString();
   }

   private void validateIdentifierAndDataType( String sIdentifier, String sDataType ) throws ParserException
   {
      if ( ! ( Symbols.TOKEN_VARTYPE_STRING.equals( sDataType )
               || Symbols.TOKEN_VARTYPE_INT.equals( sDataType )
               || Symbols.TOKEN_VARTYPE_FLOAT.equals( sDataType )
               || Symbols.TOKEN_VARTYPE_BOOLEAN.equals( sDataType )
               || Symbols.TOKEN_VARTYPE_FILE.equals( sDataType )
             )
         )
      {
         throw new ParserException( "Unrecognized type: " + sDataType + " for variable: " + sIdentifier );
      }
   }
}

// EOF