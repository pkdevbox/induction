package demoapp.viewinjection_app;

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

   public SecondDemoView( FirstDemoView oFirstDemoView )
   {
      _oFirstDemoView = oFirstDemoView;
   }

   public String getText()
   {
      return "Hello I am the SECOND (2nd) demo view...and.." + _oFirstDemoView.getText();
   }

   public String getMimeType()
   {
      return "text/plain";
   }
}
