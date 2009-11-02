package com.acciente.induction.resolver;

import com.acciente.commons.lang.Strings;
import com.acciente.induction.init.config.Config;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ShortURLControllerResolver
 *
 * @author Adinath Raveendra Raj
 * @created Mar 29, 2009
 */
public class ShortURLControllerResolver implements ControllerResolver
{
   private  Config.ControllerMapping   _oControllerMapping;
   private  List                       _oURL2ClassMapperList;

   public ShortURLControllerResolver( Config.ControllerMapping    oControllerMapping,
                                      ClassLoader                 oClassLoader ) throws IOException
   {
      _oControllerMapping   = oControllerMapping;
      _oURL2ClassMapperList = createURL2ClassMapperList( oControllerMapping, oClassLoader );
   }

   public Resolution resolve( HttpServletRequest oRequest )
   {
      String   sURLPath = oRequest.getPathInfo();

      if ( sURLPath != null )
      {
         for ( Iterator oIter = _oURL2ClassMapperList.iterator(); oIter.hasNext(); )
         {
            URL2ClassMapper.ClassAndMethod oClassAndMethod;

            oClassAndMethod = ( ( URL2ClassMapper ) oIter.next() ).mapURL2Class( sURLPath );

            if ( oClassAndMethod != null )
            {
               if ( Strings.isEmpty( oClassAndMethod.getMethodName() ) )
               {
                  return new Resolution( oClassAndMethod.getClassName(),
                                         _oControllerMapping.getDefaultHandlerMethodName(),
                                         _oControllerMapping.isIgnoreMethodNameCase() );
               }
               else
               {
                  return new Resolution( oClassAndMethod.getClassName(),
                                         oClassAndMethod.getMethodName(),
                                         _oControllerMapping.isIgnoreMethodNameCase() );
               }
            }
         }
      }

      return null;
   }

   private List createURL2ClassMapperList( Config.ControllerMapping oControllerMapping, ClassLoader oClassLoader ) throws IOException
   {
      List oURL2ClassMapperList;

      oURL2ClassMapperList = new ArrayList( oControllerMapping.getURLToClassMapList().size() );

      for ( Iterator oURLToClassMapIter = oControllerMapping.getURLToClassMapList().iterator(); oURLToClassMapIter.hasNext(); )
      {
         Config.ControllerMapping.URLToClassMap oURLToClassMap;

         oURLToClassMap = ( Config.ControllerMapping.URLToClassMap ) oURLToClassMapIter.next();

         // store the URL pattern and the classname map in the list
         oURL2ClassMapperList.add( new URL2ClassMapper( oURLToClassMap.getURLPattern(),
                                                        oURLToClassMap.getClassPackages(),
                                                        oURLToClassMap.getClassPattern(),
                                                        oClassLoader ) );
      }

      return oURL2ClassMapperList;
   }
}