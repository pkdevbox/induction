package demoapp.counter_app;

import com.acciente.induction.controller.Controller;
import com.acciente.induction.controller.Request;
import com.acciente.induction.controller.Response;

import javax.servlet.http.Cookie;
import java.io.IOException;

/**
 * Log
 * Jun 8, 2008 APR  -  created
 */
public class Cookies implements Controller
{
   public void handler( Request oRequest, Response oResponse )
      throws IOException
   {
      if ( oRequest.getCookies() == null )
      {
         oResponse.out().println( "The request has no cookies!" );
      }
      else
      {
         for ( int i = 0; i < oRequest.getCookies().length; i++ )
         {
            Cookie oCookie = oRequest.getCookies()[ i ];

            oResponse.out().println( "Name=" + oCookie.getName() );
            oResponse.out().println( "Domain=" + oCookie.getDomain() );
            oResponse.out().println( "Path=" + oCookie.getPath() );
            oResponse.out().println( "Secure=" + oCookie.getSecure() );
            oResponse.out().println( "Value=" + oCookie.getValue() );
            oResponse.out().println( "Version=" + oCookie.getVersion() );
         }
      }
   }
}

// EOF
