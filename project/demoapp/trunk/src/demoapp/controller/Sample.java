package demoapp.controller;

import com.acciente.dragonfly.controller.Form;
import com.acciente.dragonfly.controller.Request;
import com.acciente.dragonfly.controller.Response;

import java.io.IOException;

public class Sample
{
   // http://samples.com/cgi/helloworld/message_2

   // http://10.100.1.32:8080/biz/cgi/helloworld.hd?name=John
   // http://10.100.1.32:8080/biz/cgi/HelloWorld/message_2?name=John
   // http://10.100.1.32:8080/biz/cgi/demoapp/controller/HelloWorld/message_2?name=John

   public void action_1( Response response ) throws IOException
   {
      response.out().println( "Hello World!" );
   }

   public void action_2( Form form, Response response ) throws IOException
   {
      response.out().println( "Hello " + form.getString( "name" ) + " using dragonFly form support" );
   }

   public void action_3( Request request, Response response ) throws IOException
   {
      response.out().println( "Hello " + request.getParameter( "name" ) + " using servlet's form support" );
   }

   public void action_4( Object app, Form form, Response response ) throws IOException
   {
      response.out().println( "Hello World!" );
   }
}

// EOF