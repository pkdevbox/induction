package com.acciente.induction.interceptor;

/**
 * RequestInterceptor
 *
 * @author Adinath Raveendra Raj
 * @created Aug 30, 2009
 */
public interface RequestInterceptor
{
   void preResolution();

   void postResolution();

   void postResponse();
}
