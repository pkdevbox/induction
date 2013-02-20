package demoapp.urlresolver_app;

import com.acciente.induction.dispatcher.resolver.URLResolver;
import com.acciente.induction.view.Template;
import demoapp.helloworld1_app.HelloWorld1Controller;

/**
 * Created by IntelliJ IDEA.
 * User: adinath
 * Date: Jun 13, 2010
 * Time: 9:36:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class URLResolverView implements Template
{
   private URLResolver _urlLResolver;

   public URLResolverView( URLResolver urlResolver )
   {
      _urlLResolver = urlResolver;
   }

   public String getTemplateName()
   {
      return "URLResolverView.ftl";
   }

   public String getMimeType()
   {
      return "text/html";
   }

   public String getHelloWorld1URL()
   {
      return _urlLResolver.resolve( HelloWorld1Controller.class );
   }
}
