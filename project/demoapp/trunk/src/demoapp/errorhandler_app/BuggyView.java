package demoapp.errorhandler_app;

import com.acciente.induction.view.Template;

/**
 * BuggyView
 *
 * @author Adinath Raveendra Raj
 * @created Nov 30, 2009
 */
public class BuggyView implements Template
{
   public String getTemplateName()
   {
      return "Buggy.ftl";
   }

   public String getMimeType()
   {
      return "text/html";
   }
}
