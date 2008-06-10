package com.acciente.commons.loader;

import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * May 21, 2008 APR  -  refactored to support a list of ClassDefLoaders to enable
 *                      searching in multiple load locations without the need to
 *                      to chain classloaders.
 */
public class ReloadingClassLoader extends SecureClassLoader
{
   private  List           _oClassDefLoaderList    = new ArrayList();
   private  Map            _oClassControlBlockMap  = new HashMap();

   /**
    * Creates a class loader with no parent class loader, this is expected to
    * cause the system class loader to be used as the parent class loader
    *
    */
   public ReloadingClassLoader()
   {
      super();
   }

   /**
    * Creates a class loader that delegates to the specified parent class loader
    *
    * @param oParentClassLoader the parent class loader
    */
   public ReloadingClassLoader( ClassLoader oParentClassLoader )
   {
      super( oParentClassLoader );
   }

   /**
    * Adds a class definition loader to the list of class definition loaders to search
    *
    * @param oClassDefLoader an object that implements the class ClassDefLoader interface
    */
   public void addClassDefLoader( ClassDefLoader oClassDefLoader )
   {
      _oClassDefLoaderList.add( oClassDefLoader );
   }

   public Class loadClass( String sClassName, boolean bResolve )
      throws ClassNotFoundException
   {
      //System.out.println( "re-cl > " + sClassName +  " > start" );      // todo: remove
      Class oClass;

      // the implementation of this method was adapted from the
      // corresponding implementation in java.lang.ClassLoader to
      // ensure that we are a well-behaved classloader

      // we first look for the ClassControlBlock for this class to determine if we previously
      // loaded this class. We cannot use the findLoadedClass() method since this classloader
      // loads a class every time using a new classloader instance (of type ByteCodeClassLoader).
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
         boolean  bReload = false;

         // ok, we have previously loaded this class so check if we need to reload
         if ( oClassControlBlock.getClassDef().isModified() )
         {
            bReload = true;
            //System.out.println( "re-cl > " + sClassName +  " > reloading (class modified)" );      // todo: remove
         }
         else
         {
            //System.out.println( "re-cl > checking dependent classes for: " + sClassName );      // todo: remove
            // before we can determine if we need to reload this class we need to
            // check and if needed reload the classes referenced by this class
            Class[]  aoReferencedClasses = oClassControlBlock.getReferencedClasses();

            // if any of the classes referenced by this class has changed then
            // we will proceed to reload this class
            for ( int i = 0; i < aoReferencedClasses.length; i++ )
            {
               //System.out.println( "re-cl > " + sClassName +  " > checking class: " + aoReferencedClasses[ i ] );      // todo: remove
               Class oCurrentReferencedClass = loadClass( aoReferencedClasses[ i ].getName() );

               if ( aoReferencedClasses[ i ] != oCurrentReferencedClass )
               {
                  aoReferencedClasses[ i ] = oCurrentReferencedClass;
                  bReload = true;
                  //System.out.println( "re-cl > " + sClassName +  " > dependent class: " + aoReferencedClasses[ i ] + " changed" );      // todo: remove
               }
            }
         }

         if ( bReload )
         {
            // reload new class def
            oClassControlBlock.getClassDef().reload();

            // now load in the new version of the class
            oClass = findClass( oClassControlBlock );
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
      // first find the byte codes for this class using the class def loaders defined
      ClassDef oClassDef = loadClassDef( sClassName );

      // first load the classes referenced by this class to ensure that we can in the future,
      // after this class is loaded, reliably detect reloads of these referenced classes
      String[] asReferencedClasses = oClassDef.getReferencedClasses();
      Class[]  aoReferencedClasses = new Class[ asReferencedClasses.length ];

      for ( int i = 0; i < asReferencedClasses.length; i++ )
      {
         aoReferencedClasses[ i ] = loadClass( asReferencedClasses[ i ] );
      }

      // create a new class control block to keep track of this class
      ClassControlBlock oClassControlBlock
         = new ClassControlBlock( sClassName, oClassDef, aoReferencedClasses );

      // now load the class and update the class control block
      Class oClass = findClass( oClassControlBlock );

      // save the class control block, after the class loads without errors
      _oClassControlBlockMap.put( sClassName, oClassControlBlock );

      return oClass;
   }

   private ClassDef loadClassDef( String sClassName )
      throws ClassNotFoundException
   {
      ClassDef oClassDef = null;

      for ( int i = 0; i < _oClassDefLoaderList.size(); i++ )
      {
         ClassDefLoader oClassDefLoader = ( ClassDefLoader ) _oClassDefLoaderList.get( i );

         if ( ( oClassDef = oClassDefLoader.getClassDef( sClassName ) ) != null )
         {
            break;
         }
      }

      // if we could not locate the class above we cannot proceed, so complain
      if ( oClassDef == null )
      {
         throw new ClassNotFoundException( "Unable to locate a definition for class: " + sClassName );
      }

      return oClassDef;
   }

   private Class findClass( ClassControlBlock oClassControlBlock )
      throws ClassNotFoundException
   {
      ClassDef oClassDef = oClassControlBlock.getClassDef();

      // load the compile class using a new classloader, we set ourself as the parent classloader
      // so that this new child correctly delegates to us before delegating to our parent classloader
      ByteCodeClassLoader oByteCodeClassLoader = new ByteCodeClassLoader( this );

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

      // NOTE: it is crucial that we use findClass() below instead of loadClass() to load the new class,
      // since the parent of oByteCodeClassLoader is this classloader instance (set in call to ByteCodeClassLoader
      // constructor above) so if loadClass() called due to the way it loadClass() always delegates to the parent
      // we would enter into an infinite recursion
      oClassControlBlock.setLastLoadedClass( oByteCodeClassLoader.findClass( oClassControlBlock.getClassName() ) );

      return oClassControlBlock.getLastLoadedClass();
   }

   /**
    * This class is used to keep track of each class loaded by this classloader
    */
   static private class ClassControlBlock
   {
      private  String      _sClassName;
      private  Class       _oLastLoadedClass;
      private  ClassDef    _oClassDef;
      private  Class[]     _aoReferencedClasses;

      private ClassControlBlock( String sClassName, ClassDef oClassDef, Class[] aoReferencedClasses )
      {
         _sClassName          = sClassName;
         _oClassDef           = oClassDef;
         _aoReferencedClasses = aoReferencedClasses;
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

      public Class[] getReferencedClasses()
      {
         return _aoReferencedClasses;
      }
   }
}

// EOF