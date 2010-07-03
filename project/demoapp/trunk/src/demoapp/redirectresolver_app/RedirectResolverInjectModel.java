package demoapp.redirectresolver_app;

import com.acciente.induction.resolver.RedirectResolver;

/**
 * Mozart - Integrated Medical Information System
 *
 * @author Adinath Raveendra Raj
 * @created Jun 18, 2010 3:45:13 PM
 */
public class RedirectResolverInjectModel
{
   private RedirectResolver _oRedirectResolver;

   public RedirectResolverInjectModel( RedirectResolver oRedirectResolver )
   {
      _oRedirectResolver = oRedirectResolver;
   }

   public RedirectResolver getRedirectResolver()
   {
      return _oRedirectResolver;
   }
}
