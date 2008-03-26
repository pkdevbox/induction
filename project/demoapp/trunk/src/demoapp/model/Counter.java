package demoapp.model;

/**
 * Log
 * Mar 26, 2008 APR  -  created
 */
public class Counter
{
   private int iCount = 0;

   public void increment()
   {
      iCount+=2;
   }

   public void decrement()
   {
      iCount++;
   }

   public int getCount()
   {
      return iCount;
   }
}
