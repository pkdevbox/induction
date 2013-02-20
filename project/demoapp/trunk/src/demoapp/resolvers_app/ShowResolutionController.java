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
   public void handler( Response response, ControllerResolver.Resolution resolution ) throws IOException
   {
      response.setContentType("text/plain");

      response.out().println("resolution.className  : " + resolution.getClassName());
      response.out().println("resolution.methodName : " + resolution.getMethodName());
      response.out().println( "resolution.options    : " + resolution.getOptions() );
   }
}

// EOF