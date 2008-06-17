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
 * (this class uses the Apache BCEL library to do the hard work)
 *
 * Log
 * Jun 5, 2008 APR  -  created
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

            String sReferencedClassname = oConstantUtf8.getBytes().replace( "/", "." );

            oReferences.add( sReferencedClassname );
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
         sReferencedClassname = sSignature.substring( 1, sSignature.length() - 1 ).replace( "/", "." );

         oReferences.add( sReferencedClassname );
      }
   }
}

// EOF