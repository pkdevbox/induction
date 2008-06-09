package com.acciente.commons_junit.loader;

import com.acciente.commons.loader.ClassFile;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class ...
 *
 * Log
 * Jun 5, 2008 APR  -  created
 */
public class Test_ClassFile
{
   @Test
   public void test_1() throws IOException
   {
      Set oReferences = new ClassFile( new File( "com/acciente/commons_junit/loader/TestClass1.class" ) ).getReferencedClasses();

      compareArraysAsSets( new String[]
                           {  "java.lang.Object",
                           }, oReferences );
   }

   @Test
   public void test_2() throws IOException
   {
      Set oReferences = new ClassFile( new File( "com/acciente/commons_junit/loader/TestClass2.class" ) ).getReferencedClasses();

      compareArraysAsSets( new String[]
                           {  "java.lang.Object",
                              "com.acciente.commons_junit.loader.TestClass1",
                           }, oReferences );
   }

   @Test
   public void test_3() throws IOException
   {
      Set oReferences = new ClassFile( new File( "com/acciente/commons_junit/loader/TestClass3.class" ) ).getReferencedClasses();

      compareArraysAsSets( new String[]
                           { "java.lang.Object",
                             "com.acciente.commons_junit.loader.TestClass1",
                             "com.acciente.commons_junit.loader.TestClass2"
                           }, oReferences );
   }

   @Test
   public void test_4() throws IOException
   {
      Set oReferences = new ClassFile( new File( "com/acciente/commons_junit/loader/TestClass4.class" ) ).getReferencedClasses();

      compareArraysAsSets( new String[]
                           { "java.lang.Object",
                             "com.acciente.commons_junit.loader.TestClass1",
                           }, oReferences );
   }

   @Test
   public void test_5() throws IOException
   {
      Set oReferences = new ClassFile( new File( "com/acciente/commons_junit/loader/TestClass5.class" ) ).getReferencedClasses();

      compareArraysAsSets( new String[]
                           { "java.lang.Object",
                             "com.acciente.commons_junit.loader.TestClass1",
                           }, oReferences );
   }

   @Test
   public void test_6() throws IOException
   {
      Set oReferences = new ClassFile( new File( "com/acciente/commons_junit/loader/TestClass6.class" ) ).getReferencedClasses();

      compareArraysAsSets( new String[]
                           { "java.lang.Object",
                             "com.acciente.commons_junit.loader.TestClass1",
                           }, oReferences );
   }

   @Test
   public void test_7() throws IOException
   {
      Set oReferences = new ClassFile( new File( "com/acciente/commons_junit/loader/TestClass7.class" ) ).getReferencedClasses();

      compareArraysAsSets( new String[]
                           { "java.lang.String",
                             "com.acciente.commons_junit.loader.TestClass1",
                             "com.acciente.commons_junit.loader.TestClass3",
                             "com.acciente.commons_junit.loader.TestClass4",
                             "com.acciente.commons_junit.loader.TestClass5",
                           }, oReferences );
   }

   private void compareArraysAsSets( String[] asExpectedSetAsArray, Set oActualSet )
   {
      Set oExpectedSet = new HashSet();

      oExpectedSet.addAll( Arrays.asList( asExpectedSetAsArray ) );

      assertEquals( oExpectedSet, oActualSet );
   }

   private void printArray( String[] asArray )
   {
      for ( int i = 0; i < asArray.length; i++ )
      {
         System.out.println( asArray[ i ] );
      }
   }
}

// EOF