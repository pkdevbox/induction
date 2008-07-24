package demoapp.models_app;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

/**
 * This class is an Induction model factory. The only requirement is that this class have single
 * method named createMmodel().
 *
 * @created Jun 26, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class BarModelFactory
{
   /**
    * Our createModel() is very simple, but you are free make it as fanciful as your
    * model instantiation demands.
    *
    * @return a BarModel object
    */
   public BarModel createModel( HttpServletRequest oRequest, ServletConfig oConfig )
   {
      System.out.println( "request=" + oRequest  );
      System.out.println( "config=" + oConfig  );

      return new BarModel( System.currentTimeMillis() );
   }
}
