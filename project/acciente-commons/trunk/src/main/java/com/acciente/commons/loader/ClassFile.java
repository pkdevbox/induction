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
package com.acciente.commons.loader;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.Type;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * This class has methods to read information from a class file.
 *
 * (this class makes heavy use of the Apache BCEL library)
 *
 * @created Jun 5, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ClassFile
{
   private JavaClass    _oJavaClass;
   private String       _sClassName;

   public ClassFile( File oByteCodeFile ) throws IOException
   {
      _oJavaClass = ( new ClassParser( new FileInputStream( oByteCodeFile ), "" ) ).parse();
      _sClassName = _oJavaClass.getClassName();
   }

   public ClassFile( byte[] ayClassByteCode ) throws IOException
   {
      _oJavaClass = ( new ClassParser( new ByteArrayInputStream( ayClassByteCode ), "" ) ).parse();
      _sClassName = _oJavaClass.getClassName();
   }

   /**
    * This method returns the names of classes referenced by this class, including
    * inner classes.
    * @return a set containing strings representing fully qualified class names
    */
   public Set getReferencedClasses()
   {
      Set            oReferences    = new HashSet();
      String         sClassName     = _oJavaClass.getClassName();

      // we determine the dependencies by looking at class references
      // from in the constant pool, method args (and return type), and
      // class fields

      // first look at the constant pool
      ConstantPool oConstantPool = _oJavaClass.getConstantPool();
      for ( int i = 0; i < oConstantPool.getLength(); i++ )
      {
         Constant oConstant = oConstantPool.getConstant( i );

         if ( oConstant instanceof ConstantClass )
         {
            ConstantUtf8 oConstantUtf8
               =  ( ConstantUtf8 )
                     oConstantPool
                        .getConstant( ( ( ConstantClass ) oConstant ).getNameIndex() );

            String sReferencedClassname = oConstantUtf8.getBytes().replace( '/', '.' );

            // only consider the class if it is not an array, since classloaders do not
            // handle arrays, the JVM does this directly
            if ( ! sReferencedClassname.startsWith( "[" ))
            {
               oReferences.add( sReferencedClassname );
            }
         }
      }

      // next look at the types used in the method signatures
      Method[] aoMethods = _oJavaClass.getMethods();
      for ( int i = 0; i < aoMethods.length; i++ )
      {
         Method oMethod = aoMethods[ i ];

         // look at the type of the method return type
         addReference( oReferences, oMethod.getReturnType() );

         // look at the types of the method arguments
         Type[] oTypes = oMethod.getArgumentTypes();
         for ( int j = 0; j < oTypes.length; j++ )
         {
            addReference( oReferences, oTypes[ j ] );
         }
      }

      // next look at the types of the any privates and publics in the class
      Field[] aoFields = _oJavaClass.getFields();
      for ( int i = 0; i < aoFields.length; i++ )
      {
         addReference( oReferences, aoFields[ i ].getType() );
      }

      // remove the self-reference that we collected (there is always one in the constant pool)
      oReferences.remove( _sClassName );

      return oReferences;
   }

   private void addReference( Set oReferences, Type oType )
   {
      if ( oType.getType() == Constants.T_OBJECT )
      {
         String   sSignature;
         String   sReferencedClassname;

         sSignature           = oType.getSignature();
         sReferencedClassname = sSignature.substring( 1, sSignature.length() - 1 ).replace( '/', '.' );

         oReferences.add( sReferencedClassname );
      }
   }
}

// EOF