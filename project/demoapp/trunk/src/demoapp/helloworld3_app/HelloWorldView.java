package demoapp.helloworld3_app;

import com.acciente.induction.view.Template;

/**
 * Log
 * Apr 26, 2008 APR  -  created
 */
public class HelloWorldView implements Template
{
   public String samplePublic = "What can I say?";

   public String getTemplateName()
   {
      return "HelloWorld.ftl";
   }

   public String getSampleValue()
   {
      return "FooBar Flintstone";
   }

   public String getMimeType()
   {
      return "text/plain";
   }
}
