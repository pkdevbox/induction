package demoapp.urlresolver_app;

import com.acciente.induction.dispatcher.resolver.URLResolver;

/**
 * Mozart - Integrated Medical Information System
 *
 * @author Adinath Raveendra Raj
 * @created Jun 18, 2010 3:45:13 PM
 */
public class URLResolverModel
{
   private URLResolver _urlResolver;

   public URLResolverModel( URLResolver urlResolver )
   {
      _urlResolver = urlResolver;
   }

   public URLResolver getURLResolver()
   {
      return _urlResolver;
   }
}
