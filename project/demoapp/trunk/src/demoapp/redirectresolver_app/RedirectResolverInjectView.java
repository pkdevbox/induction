package demoapp.redirectresolver_app;

import com.acciente.induction.resolver.RedirectResolver;
import com.acciente.induction.view.Template;
import demoapp.helloworld1_app.HelloWorld1Controller;

/**
 * Created by IntelliJ IDEA.
 * User: adinath
 * Date: Jun 13, 2010
 * Time: 9:36:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class RedirectResolverInjectView implements Template
{
   private RedirectResolver _oRedirectResolver;

   public RedirectResolverInjectView( RedirectResolver oRedirectResolver )
   {
      _oRedirectResolver = oRedirectResolver;
   }

   public String getTemplateName()
   {
      return "RedirectResolverInjectView.ftl";
   }

   public String getMimeType()
   {
      return "text/html";
   }

   public String getHelloWorld1URL()
   {
      return _oRedirectResolver.resolve( HelloWorld1Controller.class ); 
   }
}
