package demoapp.view;

import com.acciente.dragonfly.view.Text;

public class HelloWorldView implements Text
{
   public String getText()
   {
      return "A new kind of Hello World!!";
   }

   public String getMimeType()
   {
      return "text/plain";
   }
}
