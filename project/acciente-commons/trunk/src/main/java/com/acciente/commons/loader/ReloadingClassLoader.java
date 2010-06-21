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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This classloader loads classes retrieved via a class definition loader
 * (aka ClassDefLoader). This class loader uses the methods provided by
 * a ClassDef (a ClassDef is returned by the ClassDefLoader) to determine
 * if the class has been modified and transparently reloads the class
 * into the JVM if the ClassDef reports that the class has been modified.<p>
 * <p>
 * This class loader also reads dependency information from the ClassDef.
 * When the load of a class, say A, is requested it recursively checks for
 * modificiations to dependent classes. If any of the the dependent classes
 * are modified it first reloads the dependent classes and then proceeds to
 * reload the class A.
 *
 * @see ClassDefLoader
 * @see ClassDef
 *
 * @created Feb 23, 2008
 * @change-summary Feb 27, 2008 APR  refactored SourceClassLoader -> ReloadingClassLoader
 * @change-summary May 21, 2008 APR  refactored to support a list of ClassDefLoaders to enable
 * searching in multiple load locations without the need to to chain classloaders.
 *
 * @author Adinath Raveendra Raj
 */
public class ReloadingClassLoader extends SecureClassLoader
{
   private  List           _oClassDefLoaderList          = new ArrayList();
   private  Map            _oClassControlBlockMap        = new HashMap();
   private  Set            _oIgnoredClassNameSet         = new HashSet();
   private  List           _oIgnoredClassNamePrefixList  = new ArrayList();
   private  Map            _oResourceDefMap              = new HashMap();

   private  ThreadLocal    _oLoadInProgressClassNameSet  = new ClassNameSet();

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

   /**
    * A list of ClassDefLoaders searched by this class loader.
    *
    * @return a list containing (in order of search) the classdefloader in use
    */
   public List getClassDefLoaders()
   {
      return _oClassDefLoaderList;
   }

   /**
    * This method is used to add to the list classnames that should be "ignored" as regards the
    * dependency checking.
    *
    * By "ignored" is meant the following:
    * If class A depends on class B, and if class B changes, then if class B is on the
    * list of classes to be "ignored" then the behavior is identical to that if class A had no dependency
    * on class B.
    *
    * @param sIgnoredClassName a string representing a fully qualified classname
    */
   public void addIgnoredClassName( String sIgnoredClassName )
   {
      _oIgnoredClassNameSet.add( sIgnoredClassName );
   }

   /**
    * This method is used to add to list of classname prefixes that should be "ignored" as regards the
    * dependency checking. For example specifying "com.foo.bar." will cause all classesname starting with
    * com.foo.bar. package to be ignored.
    *
    * By "ignored" is meant the following:
    * If class A depends on class B, and if class B changes, then if class B is on the
    * list of classes to be "ignored" then the behavior is identical to if class A had no dependency
    * on class B
    *
    * @param sIgnoredClassNamePrefix a string representing the prefix of one or more fully qualified classnames
    */
   public void addIgnoredClassNamePrefix( String sIgnoredClassNamePrefix )
   {
      _oIgnoredClassNamePrefixList.add( sIgnoredClassNamePrefix );
   }

   /**
    * Returns the set of classnames that are being "ignored" as regards the dependency checking
    *
    * @return a set of strings representing classnames that should be ignored by the
    * dependency checker.
    */
   public Collection getIgnoredClassNames()
   {
      return _oIgnoredClassNameSet;
   }

   /**
    * Returns the set of classname prefixes that are being "ignored" as regards the dependency checking
    *
    * @return a set of strings representing classname prefixes that should be ignored by the
    * dependency checker.
    */
   public Collection getIgnoredClassNamePrefixes()
   {
      return _oIgnoredClassNamePrefixList;
   }

   /**
    * Used to load a resource via this classloader
    *
    * @return an input stream, or null if the resource could not be loaded
    */
   public InputStream getResourceAsStream( String sResourceName )
   {
      ResourceDef oResourceDef = ( ResourceDef ) _oResourceDefMap.get( sResourceName );

      if ( oResourceDef == null )
      {
         // first try using the standard implementation that delegates to the parent classloaders
         InputStream oInputStream = super.getResourceAsStream( sResourceName );

         if ( oInputStream != null )
         {
            return oInputStream;
         }
         else
         {
            oResourceDef = loadResourceDef( sResourceName );

            if ( oResourceDef != null )
            {
               _oResourceDefMap.put( sResourceName, oResourceDef );

               return new ByteArrayInputStream( oResourceDef.getContent() );
            }
            else
            {
               return null;
            }
         }
      }
      else
      {
         if ( oResourceDef.isModified() )
         {
            oResourceDef.reload();
         }

         return new ByteArrayInputStream( oResourceDef.getContent() );
      }
   }

   private ResourceDef loadResourceDef( String sResourceName )
   {
      ResourceDef oResourceDef = null;

      for ( int i = 0; i < _oClassDefLoaderList.size(); i++ )
      {
         ClassDefLoader oClassDefLoader = ( ClassDefLoader ) _oClassDefLoaderList.get( i );

         if ( ( oResourceDef = oClassDefLoader.getResourceDef( sResourceName ) ) != null )
         {
            break;
         }
      }

      return oResourceDef;
   }

   public Class loadClass( String sClassName, boolean bResolve )
      throws ClassNotFoundException
   {
      // we keep track of the classname we are loading in a thread local set, this allows
      // us to detect cyclic dependencies which would otherwise cause infinite recursion
      ( ( Set ) _oLoadInProgressClassNameSet.get() ).add( sClassName );

      Class oClass;

      try
      {
         // the implementation of this method was adapted from the corresponding implementation
         // in java.lang.ClassLoader to ensure that we are a well-behaved classloader

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
            }
            else
            {
               // before we can determine if we need to reload this class we need to
               // check and if need to reload the classes referenced by this class
               Class[]  aoReferencedClasses = oClassControlBlock.getReferencedClasses();

               // if any of the classes referenced by this class has changed then
               // we will proceed to reload this class
               for ( int i = 0; i < aoReferencedClasses.length; i++ )
               {
                  // the findClass() method leaves nulls in aoReferencedClasses for "ignored" classes
                  if ( aoReferencedClasses[ i ] == null )
                  {
                     // if this is an ignored class, just go to the next referenced class
                     break;
                  }

                  if ( ( ( Set ) _oLoadInProgressClassNameSet.get() ).contains( aoReferencedClasses[ i ] ) )
                  {
                     // if the referenced class is currently being loaded, do not proceed since this would
                     // cause an infinite recursion!
                     break;
                  }

                  Class oCurrentReferencedClass = loadClass( aoReferencedClasses[ i ].getName() );

                  if ( aoReferencedClasses[ i ] != oCurrentReferencedClass )
                  {
                     aoReferencedClasses[ i ] = oCurrentReferencedClass;
                     bReload = true;
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
      }
      finally
      {
         ( ( Set ) _oLoadInProgressClassNameSet.get() ).remove( sClassName );
      }

      return oClass;
   }

   private boolean isIgnoredDependency( String sDependencyClassName )
   {
      boolean bIsIgnoredDependency = false;

      if ( _oIgnoredClassNameSet.size() != 0 && _oIgnoredClassNameSet.contains( sDependencyClassName ) )
      {
         bIsIgnoredDependency = true;
      }
      else if ( _oIgnoredClassNamePrefixList.size() != 0 )
      {
         for ( int i = 0; i < _oIgnoredClassNamePrefixList.size(); i++ )
         {
            if ( sDependencyClassName.startsWith( ( String ) _oIgnoredClassNamePrefixList.get( i ) ) )
            {
               bIsIgnoredDependency = true;
               break;
            }
         }
      }

      // this is not on the "ignored" list, but we may still ignore this dependency if we
      // detect a cyclic dependency
      if ( ! bIsIgnoredDependency )
      {
         // here we check if the "dependency" class is identical to a class that is pending
         // load in this thread, if so then it must be that there is cyclical dependency
         if ( ( ( Set ) _oLoadInProgressClassNameSet.get() ).contains( sDependencyClassName ) )
         {
            bIsIgnoredDependency = true;
         }
      }

      return bIsIgnoredDependency;
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
         if ( ! isIgnoredDependency( asReferencedClasses[ i ] ) )
         {
            aoReferencedClasses[ i ] = loadClass( asReferencedClasses[ i ] );
         }
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

   private static class ClassNameSet extends ThreadLocal
   {
      public synchronized Object initialValue()
      {
         return new HashSet();
      }
   }
}

// EOF