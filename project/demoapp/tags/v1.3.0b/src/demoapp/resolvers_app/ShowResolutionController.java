package demoapp.resolvers_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;
import com.acciente.induction.resolver.ControllerResolver;

import java.io.IOException;

/**
 * ShowResolutionController
 *
 * @author Adinath Raveendra Raj
 * @created Jun 26, 2008
 */
public class ShowResolutionController implements Controller
{
   public void handler( Response oResponse, ControllerResolver.Resolution oResolution ) throws IOException
   {
      oResponse.setContentType( "text/plain" );

      oResponse.out().println( "resolution.className  : " + oResolution.getClassName() );
      oResponse.out().println( "resolution.methodName : " + oResolution.getMethodName() );
      oResponse.out().println( "resolution.options    : " + oResolution.getOptions() );
   }
}

// EOF