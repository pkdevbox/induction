package demoapp.urlresolver_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;
import com.acciente.induction.dispatcher.resolver.URLResolver;
import demoapp.helloworld1_app.HelloWorld1Controller;
import demoapp.helloworld2_app.HelloWorld2Controller;
import demoapp.helloworld2_app.HelloWorld2View;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: adinath
 * Date: Jun 13, 2010
 * Time: 8:56:03 PM
 *
 * This controller shows how to inject the redirect resolver into your controller.
 *
 * The redirect resolver is used  to resolve a class name into a URL. The ability
 * to inject a redirect resolver into a controller/view is a necessary feature to
 * enable writing applications that are independent of the URL mapping configuration
 * of the application.
 *
 * Unfortunately this feature inadvertently left out until now! (Jun 2010)
 */
public class URLResolverController implements Controller
{
   public void handler( URLResolver oURLResolver, Response oResponse ) throws IOException
   {
      oResponse.out().println( "injected redirect resolver: "              + oURLResolver                                        );

      oResponse.out().println();
      oResponse.out().println( "HelloWorld1Controller.class resolved to: " + oURLResolver.resolve( HelloWorld1Controller.class ) );
      oResponse.out().println( "HelloWorld2Controller.class resolved to: " + oURLResolver.resolve( HelloWorld2Controller.class ) );
      oResponse.out().println( "HelloWorld2View.class resolved to: "       + oURLResolver.resolve( HelloWorld2View.class       ) );
   }

   public void modelDemo( URLResolverModel oURLResolverModel, Response oResponse ) throws IOException
   {
      oResponse.out().println( "model: " + oURLResolverModel );

      oResponse.out().println();
      oResponse.out().println( "URLResolverModel.getURLResolver(): " + oURLResolverModel.getURLResolver() );
   }
}
