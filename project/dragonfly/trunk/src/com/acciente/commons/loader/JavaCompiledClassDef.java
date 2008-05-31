package com.acciente.commons.loader;

import org.apache.commons.io.FileUtils;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.ConstantPool;

import java.io.File;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Log
 * Feb 29, 2008 APR  -  created
 */
public class JavaCompiledClassDef implements ClassDef
{
   // compiled file data
   private File         _oCompiledFile;
   private long         _iLastModifiedTimeAtLastLoad;

   // class data
   private String       _sClassName;
   private byte[]       _ayClassByteCode;
   private String[]     _asReferencedClassNames;

   JavaCompiledClassDef( String sClassName, File oCompiledFile )
      throws ClassNotFoundException
   {
      _sClassName    = sClassName;
      _oCompiledFile = oCompiledFile;

      loadCompiledClassFile();
   }

   public String getClassName()
   {
      return _sClassName;
   }

   public boolean isModified()
   {
      return ( _oCompiledFile.lastModified() != _iLastModifiedTimeAtLastLoad );
   }

   public void reload() throws ClassNotFoundException
   {
      loadCompiledClassFile();
   }

   public byte[] getByteCode()
   {
      return _ayClassByteCode;
   }

   public ClassDef[] getBundledClassDefs()
   {
      return null;
   }

   public String[] getReferencedClasses()
   {
      return _asReferencedClassNames;
   }

   private void loadCompiledClassFile()
      throws ClassNotFoundException
   {
      try
      {
         // checkpoint the compiled file's modified date before the compile
         long iLastModifiedTimeAtLastLoad = _oCompiledFile.lastModified();

         _ayClassByteCode = FileUtils.readFileToByteArray( _oCompiledFile );

         _iLastModifiedTimeAtLastLoad = iLastModifiedTimeAtLastLoad;

         readReferencedClasses( _ayClassByteCode, _oCompiledFile.getName() );
      }
      catch ( IOException e )
      {
         throw new ClassNotFoundException( "Error loading class definition", e );
      }
   }

   private void readReferencedClasses( byte[] ayClassByteCode, String sFilename ) throws IOException
   {
      ArrayList   oReferencedClassNameList;
      JavaClass   oJavaClass;

      oJavaClass = ( new ClassParser( new ByteArrayInputStream( ayClassByteCode ), sFilename ) ).parse();

      oReferencedClassNameList = new ArrayList();

      ConstantPool oConstantPool = oJavaClass.getConstantPool();

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

            // the sReferencedClassname.startsWith( _sClassName ) is to exclude a self-reference
            // and any inner classes
            if ( ! ( sReferencedClassname.startsWith( _sClassName ) || sReferencedClassname.startsWith( "java." ) || sReferencedClassname.startsWith( "javax." ) ) )
            {
               oReferencedClassNameList.add( sReferencedClassname );
               System.out.println( "referenced-classname: " + sReferencedClassname );
            }
         }
      }

      // convert the list to an array
      _asReferencedClassNames = new String[ oReferencedClassNameList.size() ];

      oReferencedClassNameList.toArray( _asReferencedClassNames );
   }
}

// EOF