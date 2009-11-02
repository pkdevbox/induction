package demoapp.info_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Response;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * SystemInfoController
 *
 * @author Adinath Raveendra Raj
 * @created Apr 2, 2009
 */
public class SystemInfoController implements Controller
{
   public void handler( Response oResponse, ClassLoader oClassLoader )
      throws IOException
   {
      ClassLoader oPreviousClassLoader = null;

      int iCount = 0;

      while ( oClassLoader != null && oClassLoader != oPreviousClassLoader )
      {
         oResponse.out().println( "\nclassLoader-" + iCount + " > " + oClassLoader );

         if ( oClassLoader instanceof URLClassLoader )
         {
            oResponse.out().println( "classLoader-" + iCount + " > is a URL class loader" );

            URL[] aoURL = ( ( URLClassLoader ) oClassLoader ).getURLs();

            for ( int i = 0; i < aoURL.length; i++ )
            {
               oResponse.out().println( "\t\tclassLoader-" + iCount + " > URL: " + aoURL[ i ] );
            }
         }

         oPreviousClassLoader = oClassLoader;
         oClassLoader         = oClassLoader.getParent();

         iCount++;
      }
   }   
}
