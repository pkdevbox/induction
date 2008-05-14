package demoapp.helloworld3_app;

import com.acciente.dragonfly.view.Template;

/**
 * Log
 * Apr 26, 2008 APR  -  created
 */
public class HelloWorldView implements Template
{
   public String getTemplateName()
   {
      return "HelloWorld.ftl";
   }

   public String getMimeType()
   {
      return null;
   }
}
