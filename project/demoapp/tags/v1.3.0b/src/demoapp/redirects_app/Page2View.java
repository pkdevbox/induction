package demoapp.redirects_app;

import com.acciente.induction.view.Text;

/**
 * Page1View
 *
 * @author Adinath Raveendra Raj
 * @created Apr 1, 2009
 */
public class Page2View implements Text
{
   public String getText()
   {
      return "Hello! I am Page 2 (Two)";
   }

   public String getMimeType()
   {
      return "text/html";
   }
}