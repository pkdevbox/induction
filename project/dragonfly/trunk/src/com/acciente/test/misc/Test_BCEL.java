package com.acciente.test.misc;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Log
 * May 25, 2008 APR  -  created
 */
public class Test_BCEL
{
   public static void main( String[] asArgs ) throws IOException
   {
      System.out.println( "BCEL - proof of concept test" );

      File oFile = new File( "/foo/bar/HelloWorldController.class" );

      //System.out.println( "file name: " + oFile.getName() );

      byte[] ayClassByteCode = FileUtils.readFileToByteArray( oFile );

      JavaClass oJavaClass = ( new ClassParser( new ByteArrayInputStream( ayClassByteCode ), "HelloWorldController.class" ) ).parse();

      //System.out.println( "constant pool: " + oJavaClass.getConstantPool() );

      System.out.println( "pool size: " + oJavaClass.getConstantPool().getLength() );

      for ( int i = 0; i < oJavaClass.getConstantPool().getLength(); i++ )
      {
         Constant oConstant = oJavaClass.getConstantPool().getConstant( i );

         if ( oConstant instanceof ConstantClass )
         {
            //System.out.println( "constant: " + oConstant );

            ConstantUtf8 oConstantUtf8 = ( ConstantUtf8 ) oJavaClass.getConstantPool().getConstant( ( ( ConstantClass ) oConstant ).getNameIndex() );

            System.out.println( "class name: " + oConstantUtf8.getBytes() );
         }
      }
   }
}

// EOF