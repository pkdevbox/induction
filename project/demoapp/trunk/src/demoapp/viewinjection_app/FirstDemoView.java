package demoapp.viewinjection_app;

import com.acciente.induction.controller.Form;
import com.acciente.induction.controller.FormException;
import com.acciente.induction.view.Text;

/**
 * Created by IntelliJ IDEA.
 * User: adinath
 * Date: Jun 27, 2010
 * Time: 9:31:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class FirstDemoView implements Text
{
   private Form _form;

   public FirstDemoView( Form form )
   {
      _form = form;
   }

   public String getText()
   {
      String sName = "Stranger";

      try
      {
         if ( _form.containsParam( "name" ) )
         {
            sName = _form.getString( "name" );
         }
      }
      catch ( FormException e )
      {
         throw new RuntimeException( e );
      }

      return "Hello " + sName + ", I am the FIRST (1st) demo view";
   }

   public String getMimeType()
   {
      return "text/plain";
   }
}
