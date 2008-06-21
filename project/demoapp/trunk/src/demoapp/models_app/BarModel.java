package demoapp.models_app;

/**
 * This class ...
 *
 * Log
 * Jun 20, 2008 APR  -  created
 */
public class BarModel
{
   private FooModel _oFooModel;

   public BarModel( FooModel oFooModel )
   {
      _oFooModel = oFooModel;

      System.out.println( "BarModel - constructor called" );
   }

   public FooModel getFooModel()
   {
      return _oFooModel;
   }
}

// EOF