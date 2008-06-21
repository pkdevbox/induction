package demoapp.models_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;

import java.io.IOException;

/**
 * This class ...
 *
 * Log
 * Jun 20, 2008 APR  -  created
 */
public class FooController implements Controller
{
   public void handler( Response oResponse, FooModel oFooModel, BarModel oBarModel ) throws IOException
   {
      oResponse.setContentType( "text/plain" );
      oResponse.out().println( "oFooModel=" + oFooModel );
      oResponse.out().println( "oBarModel=" + oBarModel );
      oResponse.out().println( "oBarModel.getFooModel()=" + oBarModel.getFooModel() );
   }
}

// EOF