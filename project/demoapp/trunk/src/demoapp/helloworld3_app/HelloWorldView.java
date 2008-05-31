package demoapp.helloworld3_app;

import com.acciente.induction.view.Template;
import demoapp.testclass.TestClass1;

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

   public String getSampleValue()
   {
      return "Crazy2 Flintstone";
   }

   public String getMimeType()
   {
      return "text/plain";
   }
}
