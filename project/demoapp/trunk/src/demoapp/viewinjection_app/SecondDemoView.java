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
   private FirstDemoView _oFirstDemoView;
   private Form _oForm;

   public SecondDemoView( FirstDemoView oFirstDemoView, Form oForm )
   {
      _oFirstDemoView = oFirstDemoView;
      _oForm = oForm;
   }

   public String getText()
   {
      String sName = "Stranger";

      try
      {
         if ( _oForm.containsParam( "name" ) )
         {
            sName = _oForm.getString( "name" );

         }
      }
      catch ( FormException e )
      {
         throw new RuntimeException( e );
      }

      return "Hello " + sName + ", I am the SECOND (2nd) demo view...and.." + _oFirstDemoView.getText();
   }

   public String getMimeType()
   {
      return "text/plain";
   }
}
