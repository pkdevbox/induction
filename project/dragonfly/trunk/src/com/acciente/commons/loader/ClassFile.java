package com.acciente.commons.loader;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.Type;
import org.apache.bcel.Constants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * This class has has methods to read information from a class file.
 *
 * This class uses the Apache BCEL to do most of the hard work.
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

   public String[] getReferencedClasses()
   {
      Set            oReferences    = new HashSet();
      String         sClassName     = _oJavaClass.getClassName();

      // our strategy is aggregate the reference from all source eliminating duplicates

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

      Method[] aoMethods = _oJavaClass.getMethods();
      for ( int i = 0; i < aoMethods.length; i++ )
      {
         Method oMethod = aoMethods[ i ];

         Type[] oTypes = oMethod.getArgumentTypes();
         for ( int j = 0; j < oTypes.length; j++ )
         {
            Type oType = oTypes[ j ];

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

      Field[] aoFields = _oJavaClass.getFields();
      for ( int i = 0; i < aoFields.length; i++ )
      {
         Field oField = aoFields[ i ];

         Type oType = oField.getType();

         if ( oType.getType() == Constants.T_OBJECT )
         {
            String   sSignature;
            String   sReferencedClassname;

            sSignature           = oType.getSignature();
            sReferencedClassname = sSignature.substring( 1, sSignature.length() - 1 ).replace( "/", "." );

            oReferences.add( sReferencedClassname );
         }
      }

      // remove the self-reference that we collected
      oReferences.remove( _sClassName );

      System.out.println( "class > " + sClassName + " > " + oReferences );

      return null;
   }
}

// EOF