package com.acciente.induction.dispatcher.view;

import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.resolver.ViewResolver;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.ObjectFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * ViewFactory
 *
 * @author Adinath Raveendra Raj
 * @created Mar 30, 2009
 */
public class ViewFactory
{
   private  ClassLoader                   _oClassLoader;
   private  ViewParameterProviderFactory  _oViewParameterProviderFactory;

   public ViewFactory( ClassLoader oClassLoader, ViewParameterProviderFactory oViewParameterProviderFactory )
   {
      _oClassLoader                    = oClassLoader;
      _oViewParameterProviderFactory   = oViewParameterProviderFactory;
   }

   public Object getView( String                   sViewClassName,
                          HttpServletRequest       oRequest,
                          HttpServletResponse      oResponse,
                          ViewResolver.Resolution  oResolution )
      throws InvocationTargetException, ConstructorNotFoundException, ParameterProviderException, IllegalAccessException, InstantiationException, ClassNotFoundException
   {
      Class oViewClass = _oClassLoader.loadClass( sViewClassName );

      return ObjectFactory.createObject( oViewClass,
                                         null,
                                         _oViewParameterProviderFactory
                                            .getParameterProvider( oRequest, oResponse, oResolution ) );
   }
}
