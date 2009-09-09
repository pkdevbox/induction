package com.acciente.induction.dispatcher.interceptor;

import com.acciente.induction.interceptor.RequestInterceptor;
import com.acciente.induction.resolver.ControllerResolver;
import com.acciente.induction.resolver.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * RequestInterceptor
 *
 * @author Adinath Raveendra Raj
 * @created Sep 8, 2009
 */
public class RequestInterceptorExecutor
{
   private  RequestInterceptor[]  _aoRequestInterceptorArray;

   public RequestInterceptorExecutor( RequestInterceptor[] aoRequestInterceptorArray )
   {
      _aoRequestInterceptorArray = aoRequestInterceptorArray;
   }

   public void preResolution( HttpServletRequest oRequest, HttpServletResponse oResponse )
   {
      for ( int i = 0; i < _aoRequestInterceptorArray.length; i++ )
      {
         if ( ! _aoRequestInterceptorArray[ i ].preResolution( oRequest, oResponse ) )
         {
            break;
         }
      }
   }

   public void postResolution( HttpServletRequest oRequest, HttpServletResponse oResponse, ControllerResolver.Resolution oControllerResolution, ViewResolver.Resolution oViewResolution )
   {
      for ( int i = 0; i < _aoRequestInterceptorArray.length; i++ )
      {
         if ( ! _aoRequestInterceptorArray[ i ].postResolution( oRequest, oResponse, oControllerResolution, oViewResolution ) )
         {
            break;
         }
      }
   }

   public void preResponse( HttpServletRequest oRequest, HttpServletResponse oResponse, ControllerResolver.Resolution oControllerResolution, ViewResolver.Resolution oViewResolution )
   {
      for ( int i = 0; i < _aoRequestInterceptorArray.length; i++ )
      {
         if ( ! _aoRequestInterceptorArray[ i ].preResponse( oRequest, oResponse, oControllerResolution, oViewResolution ) )
         {
            break;
         }
      }
   }

   public void postResponse( HttpServletRequest oRequest, HttpServletResponse oResponse, ControllerResolver.Resolution oControllerResolution, ViewResolver.Resolution oViewResolution )
   {
      for ( int i = 0; i < _aoRequestInterceptorArray.length; i++ )
      {
         if ( ! _aoRequestInterceptorArray[ i ].postResponse( oRequest, oResponse, oControllerResolution, oViewResolution ) )
         {
            break;
         }
      }
   }
}