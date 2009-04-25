package com.acciente.induction.init;

import com.acciente.commons.reflect.ParameterProvider;
import com.acciente.commons.reflect.ParameterProviderException;
import com.acciente.induction.dispatcher.model.ModelPool;
import com.acciente.induction.util.MethodNotFoundException;
import com.acciente.induction.util.ConstructorNotFoundException;

import java.lang.reflect.InvocationTargetException;

/**
 * InitializerParameterProvider, used by the initializer classes to inject models
 * into the intializer constructor.
 *
 * @created Sep 15, 2008
 * @author  Adinath Raveendra Raj
 */
public class InitializerParameterProvider implements ParameterProvider
{
   private  ModelPool      _oModelPool;
   private  String         _sMessagePrefix;

   public InitializerParameterProvider( ModelPool oModelPool, String sMessagePrefix )
   {
      _oModelPool       = oModelPool;
      _sMessagePrefix   = sMessagePrefix;
   }

   public Object getParameter( Class oValueType ) throws ParameterProviderException
   {
      try
      {
         return _oModelPool.getModel( oValueType.getName(), null );
      }
      catch ( MethodNotFoundException e )
      {  throw new ParameterProviderException( _sMessagePrefix + ": error resolving value for type: " + oValueType, e );     }
      catch ( InvocationTargetException e )
      {  throw new ParameterProviderException( _sMessagePrefix + ": error resolving value for type: " + oValueType, e );     }
      catch ( ClassNotFoundException e )
      {  throw new ParameterProviderException( _sMessagePrefix + ": error resolving value for type: " + oValueType, e );     }
      catch ( ConstructorNotFoundException e )
      {  throw new ParameterProviderException( _sMessagePrefix + ": error resolving value for type: " + oValueType, e );     }
      catch ( IllegalAccessException e )
      {  throw new ParameterProviderException( _sMessagePrefix + ": error resolving value for type: " + oValueType, e );     }
      catch ( InstantiationException e )
      {  throw new ParameterProviderException( _sMessagePrefix + ": error resolving value for type: " + oValueType, e );     }
   }
}

// EOF