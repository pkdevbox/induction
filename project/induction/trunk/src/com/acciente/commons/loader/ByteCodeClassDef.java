package com.acciente.commons.loader;

/**
 * A simple implementation of ClassDef to hold a compiled java class file
 */
class ByteCodeClassDef implements ClassDef
{
   private String    _sClassName;
   private byte[]    _ayByteCode;

   ByteCodeClassDef( String sClassName, byte[] ayByteCode )
   {
      _sClassName =  sClassName;
      _ayByteCode =  ayByteCode;
   }

   public String getClassName()
   {
      return _sClassName;
   }

   public boolean isModified()
   {
      return false;
   }

   public void reload()
   {
      // this method does nothing in this container class
   }

   public byte[] getByteCode()
   {
      return _ayByteCode;
   }

   public ClassDef[] getBundledClassDefs()
   {
      return null;
   }

   public String[] getReferencedClasses()
   {
      return null;
   }
}

// EOF