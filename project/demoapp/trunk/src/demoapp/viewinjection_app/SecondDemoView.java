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
public class SecondDemoView implements Text
{
   private FirstDemoView    _firstDemoView;
   private Form             _form;

   public SecondDemoView( FirstDemoView firstDemoView, Form form )
   {
      _firstDemoView = firstDemoView;
      _form          = form;
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

      return "Hello " + sName + ", I am the SECOND (2nd) demo view...and.." + _firstDemoView.getText();
   }

   public String getMimeType()
   {
      return "text/plain";
   }
}
