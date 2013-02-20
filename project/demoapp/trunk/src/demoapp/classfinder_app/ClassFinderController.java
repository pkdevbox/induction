package demoapp.classfinder_app;

import com.acciente.commons.loader.ClassFinder;
import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * ClassFinderController
 *
 * @author Adinath Raveendra Raj
 * @created Jun 1, 2009
 */
public class ClassFinderController implements Controller
{
   // URL: /classfinder.test_1.action
   public void test_1( Response response, ClassLoader classLoader ) throws IOException
   {
      // the regex should match all class names starting with the letter "C"
      //run( oResponse, oClassLoader, new String[]{ "com", "org.apache", "demoapp" }, "((\\w+\\.)+)?Cou(\\w+)" );

      // the regex should match all class names ending in "Controller"
      //run( oResponse, oClassLoader, new String[]{ "demoapp" }, "((\\w+\\.)+)?(\\w+)Controller" );

      run( response, classLoader, new String[]{ "demoapp" }, "((\\w+\\.)+)?(\\w+)" );
   }

   private void run( Response response, ClassLoader classLoader, String[] packageNames, String pattern ) throws IOException
   {
      long           iStartTime     = System.currentTimeMillis();

      Set oClassnameSet = ClassFinder.find( classLoader, packageNames, Pattern.compile( pattern ) );

      response.out().println( "results >> start >> package: " + Arrays.asList( packageNames ) + ", pattern: " + pattern );

      for ( Iterator oIter = oClassnameSet.iterator(); oIter.hasNext(); )
      {
         String sClassname = ( String ) oIter.next();

         response.out().println( sClassname );
      }

      response.out().println( "results >> end >> package: " + Arrays.asList( packageNames ) + ", pattern: " + pattern );

      response.out().println( "find took: " + ( System.currentTimeMillis() - iStartTime ) );
   }
}