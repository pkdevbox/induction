package demoapp.helloworld2_app;

import com.acciente.induction.view.Text;

public class HelloWorldView implements Text
{
   public String getText()
   {
      return "Hello World, using a Text view";
   }

   public String getMimeType()
   {
      return "text/plain";
   }
}
