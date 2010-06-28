package demoapp.viewinjection_app;

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
   public String getText()
   {
      return "Hello I am the FIRST (1st) demo view";
   }

   public String getMimeType()
   {
      return "text/plain";
   }
}
