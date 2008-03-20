package com.acciente.commons.loader;

import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * This classloader loads classes retrieved vis a class definition loader
 * (aka ClassDefLoader). This class loader uses the methods provided by
 * a ClassDef to determine if the class has been modified and transparently
 * reloads the class if its modified.
 *
 * Log
 * Feb 23, 2008 APR  -  created
 * Feb 27, 2008 APR  -  refactored SourceClassLoader -> ReloadingClassLoader
 */
public class ReloadingClassLoader extends SecureClassLoader
{
   private ClassDefLoader  _oClassDefLoader;
   private Map             _oClassControlBlockMap =  new HashMap();

   /**
    * Creates a class loader with no parent class loader, this is expected to
    * cause the system class loader to be used as the parent class loader
    *
    * @param oClassDefLoader an object that implements the class ClassDefLoader interface
    */
   public ReloadingClassLoader( ClassDefLoader oClassDefLoader )
   {
      super();

      _oClassDefLoader = oClassDefLoader;
   }

   /**
    * Creates a class loader that delegates to the specified parent class loader
    *
    * @param oClassDefLoader an object that implements the class ClassDefLoader interface
    * @param oParentClassLoader the parent class loader
    */
   public ReloadingClassLoader( ClassDefLoader oClassDefLoader, ClassLoader oParentClassLoader )
   {
      super( oParentClassLoader );

      _oClassDefLoader = oClassDefLoader;
   }

   public Class loadClass( String sClassName, boolean bResolve )
      throws ClassNotFoundException
   {
      Class oClass;

      // the implementation of this method was adapted from the
      // corresponding implementation in java.lang.ClassLoader to
      // ensure that we are a well-behaved classloader

      // we first look for the ClassControlBlock for this class to determine if we previously
      // loaded this class. We cannot use the findLoadedClass() method since this classloader
      // loads a every time using a new classloader instance (of type ByteCodeClassLoader).
      // We need to use newly created classloader since if this classloader loaded the class
      // directly we would have not way of unloading the class.
      ClassControlBlock oClassControlBlock = ( ClassControlBlock ) _oClassControlBlockMap.get( sClassName );

      if ( oClassControlBlock == null )
      {
         // this class has not previously been loaded by us, so ensure the standard
         // delegation semantics by calling the super classes loadClass()
         oClass = super.loadClass( sClassName, bResolve );
      }
      else
      {
         // ok, we have previously loaded this class so check if we need to reload
         if ( oClassControlBlock.getClassDef().isModified() )
         {
            // reload new class def
            oClassControlBlock.getClassDef().reload();

            // now load in new class
            oClass = loadClass( oClassControlBlock );
         }
         else
         {
            oClass = oClassControlBlock.getLastLoadedClass();
         }
      }

      return oClass;
   }

   protected Class findClass( String sClassName )
      throws ClassNotFoundException
   {
      // create a new class control block to keep track of this class
      ClassControlBlock oClassControlBlock
         = new ClassControlBlock( sClassName, _oClassDefLoader.getClassDef( sClassName ) );

      // load the class from source and update the class control block
      Class oClass = loadClass( oClassControlBlock );

      // save the class control block, after the class loads without errors
      _oClassControlBlockMap.put( sClassName, oClassControlBlock );

      return oClass;
   }

   private Class loadClass( ClassControlBlock oClassControlBlock )
      throws ClassNotFoundException
   {
      ClassDef oClassDef = oClassControlBlock.getClassDef();

      // load the compile class using a new classloader, I am not setting this classloader
      // as the parent since that would seem to cause a cyclical relationship
      ByteCodeClassLoader oByteCodeClassLoader = new ByteCodeClassLoader( getParent() );

      // put the definitions in the classloader
      oByteCodeClassLoader.addClassDef( oClassDef.getClassName(), oClassDef.getByteCode() );
      if ( oClassDef.getBundledClassDefs() != null )
      {
         ClassDef[]  oBundledClassDefs = oClassDef.getBundledClassDefs();

         for ( int i = 0; i < oBundledClassDefs.length; i++ )
         {
            ClassDef oBundledClassDef = oBundledClassDefs[ i ];

            oByteCodeClassLoader.addClassDef( oBundledClassDef.getClassName(), oBundledClassDef.getByteCode() );
         }
      }

      // load the main (i.e. public) class defined in the source file using our new classloader
      // and cache the new class in the class control block
      oClassControlBlock.setLastLoadedClass( oByteCodeClassLoader.loadClass( oClassControlBlock.getClassName() ) );

      return oClassControlBlock.getLastLoadedClass();
   }

   /**
    * This class is used to keep track of each class loaded by this classloader
    */
   static private class ClassControlBlock
   {
      private String    _sClassName;
      private Class     _oLastLoadedClass;
      private ClassDef  _oClassDef;

      private ClassControlBlock( String sClassName, ClassDef oClassDef )
      {
         _sClassName = sClassName;
         _oClassDef  = oClassDef;
      }

      /**
       * Returns the class name object managed by this class control block
       *
       * @return
       */
      public String getClassName()
      {
         return _sClassName;
      }

      /**
       * Returns the ClassDef used to load/reload the class managed in this class control block
       *
       * @return an object that implements the ClassDef interface
       */
      public ClassDef getClassDef()
      {
         return _oClassDef;
      }

      /**
       * Returns last class loaded into the JVM for the class managed in this class control block
       *
       * @return
       */
      private Class getLastLoadedClass()
      {
         return _oLastLoadedClass;
      }

      /**
       * Sets last class loaded into the JVM for the class managed in this class control block
       *
       * @param oClass
       * @return
       */
      private void setLastLoadedClass( Class oClass )
      {
         _oLastLoadedClass = oClass;
      }
   }
}

// EOF