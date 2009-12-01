package demoapp.errorhandler_app;

import com.acciente.induction.controller.Controller;

/**
 * BuggyController
 *
 * @author Adinath Raveendra Raj
 * @created Nov 30, 2009
 */
public class BuggyController implements Controller
{
   public void handler()
   {
      // this generates a divide by zero exception
      int iPerPage   = 0;
      int iPageCount = 100 / iPerPage;
   }
}
