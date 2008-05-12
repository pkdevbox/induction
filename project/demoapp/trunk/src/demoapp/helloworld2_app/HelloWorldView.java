package demoapp.helloworld2_app;

import com.acciente.nitrogen.view.Text;

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
