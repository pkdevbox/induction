package com.acciente.induction.dispatcher.interceptor;

import com.acciente.induction.interceptor.RequestInterceptor;
import com.acciente.induction.resolver.ControllerResolver;
import com.acciente.induction.resolver.ViewResolver;
import com.acciente.commons.reflect.ParameterProviderException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * RequestInterceptor
 *
 * @author Adinath Raveendra Raj
 * @created Sep 8, 2009
 */
public class RequestInterceptorExecutor
{
   private  RequestInterceptorFacade[]    _aoRequestInterceptorFacadeArray;

   public RequestInterceptorExecutor( RequestInterceptor[]                       aoRequestInterceptorArray,
                                      RequestInterceptorParameterProviderFactory oRequestInterceptorParameterProviderFactory )
   {
      _aoRequestInterceptorFacadeArray = new RequestInterceptorFacade[ aoRequestInterceptorArray.length ];

      for ( int i = 0; i < aoRequestInterceptorArray.length; i++ )
      {
         _aoRequestInterceptorFacadeArray[ i ] = new RequestInterceptorFacade( aoRequestInterceptorArray[ i ],
                                                                               oRequestInterceptorParameterProviderFactory );
      }

   }

   public boolean preResolution( HttpServletRequest oRequest, HttpServletResponse oResponse )
      throws InvocationTargetException, ParameterProviderException, IllegalAccessException
   {
      for ( int i = 0; i < _aoRequestInterceptorFacadeArray.length; i++ )
      {
         if ( isFalse( _aoRequestInterceptorFacadeArray[ i ].preResolution( oRequest, oResponse ) ) )
         {
            return false;
         }
      }

      return true;
   }

   public boolean postResolution( HttpServletRequest oRequest, HttpServletResponse oResponse, ControllerResolver.Resolution oControllerResolution, ViewResolver.Resolution oViewResolution ) throws InvocationTargetException, ParameterProviderException, IllegalAccessException
   {
      for ( int i = 0; i < _aoRequestInterceptorFacadeArray.length; i++ )
      {
         if ( isFalse( _aoRequestInterceptorFacadeArray[ i ].postResolution( oRequest,
                                                                            oResponse,
                                                                            oControllerResolution,
                                                                            oViewResolution ) ) )
         {
            return false;
         }
      }

      return true;
   }

   public boolean preResponse( HttpServletRequest oRequest, HttpServletResponse oResponse, ControllerResolver.Resolution oControllerResolution, ViewResolver.Resolution oViewResolution ) throws InvocationTargetException, ParameterProviderException, IllegalAccessException
   {
      for ( int i = 0; i < _aoRequestInterceptorFacadeArray.length; i++ )
      {
         if ( isFalse( _aoRequestInterceptorFacadeArray[ i ].preResponse( oRequest,
                                                                         oResponse,
                                                                         oControllerResolution,
                                                                         oViewResolution ) ) )
         {
            return false;
         }
      }

      return true;
   }

   public boolean postResponse( HttpServletRequest oRequest, HttpServletResponse oResponse, ControllerResolver.Resolution oControllerResolution, ViewResolver.Resolution oViewResolution ) throws InvocationTargetException, ParameterProviderException, IllegalAccessException
   {
      for ( int i = 0; i < _aoRequestInterceptorFacadeArray.length; i++ )
      {
         if ( isFalse( _aoRequestInterceptorFacadeArray[ i ].postResponse( oRequest,
                                                                          oResponse,
                                                                          oControllerResolution,
                                                                          oViewResolution ) ) )
         {
            return false;
         }
      }

      return true;
   }

   private boolean isFalse( Object oReturnValue )
   {
      return ( oReturnValue instanceof Boolean ) && ( ( Boolean ) oReturnValue ).booleanValue();
   }
}