package com.acciente.induction.dispatcher.model;

import com.acciente.induction.init.config.Config;
import com.acciente.induction.util.MethodNotFoundException;
import com.acciente.induction.util.ConstructorNotFoundException;
import com.acciente.induction.util.ObjectFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Hashtable;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;

/**
 * This class manages access to the pool model objects
 *
 * Log
 * Mar 16, 2008 APR  -  created
 */
public class ModelPool
{
   private Config.ModelDefs   _oModelDefs;
   private Map                _oAppScopeModelMap;
   private ModelFactory       _oModelFactory;

   public ModelPool( Config.ModelDefs oModelDefs, ModelFactory oModelFactory )
   {
      _oModelDefs          = oModelDefs;
      _oModelFactory       = oModelFactory;
      _oAppScopeModelMap   = new Hashtable();   // we use a hashtable instead of a HashMap for safe concurrent access
   }

   public Object getModel( Class oModelClass, HttpServletRequest oRequest )
      throws ClassNotFoundException, ConstructorNotFoundException, MethodNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException
   {
      // first find the model definition object for this model class to determine the scope of this model
      Config.ModelDefs.ModelDef oModelDef = _oModelDefs.getModelDef( oModelClass.getName() );

      if ( oModelDef == null )
      {
         throw new IllegalArgumentException( "model-error: no definition for model class: " + oModelClass.getName() );
      }

      // next see if we already have an object instance
      Object oModel;

      if ( oModelDef.isApplicationScope() )
      {
         oModel = getApplicationScopeModel( oModelDef, oModelClass );
      }
      else if ( oModelDef.isSessionScope() )
      {
         oModel = getSessionScopeModel( oModelDef, oModelClass, oRequest );
      }
      else if ( oModelDef.isRequestScope() )
      {
         oModel = getRequestScopeModel( oModelDef, oRequest );
      }
      else
      {
         throw new IllegalArgumentException( "model-error: unknown scope for model class: " + oModelClass.getName() );
      }

      return oModel;
   }

   private Object getApplicationScopeModel( Config.ModelDefs.ModelDef oModelDef, Class oModelClass )
      throws ClassNotFoundException, ConstructorNotFoundException, MethodNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException
   {
      Object   oModel;

      oModel = _oAppScopeModelMap.get( oModelClass.getName() );

      if ( oModel != null && _oModelFactory.isModelStale( oModelDef, oModel ) )
      {
         ObjectFactory.destroyObject( oModel );

         _oAppScopeModelMap.remove( oModelClass.getName() );

         oModel = null;
      }

      if ( oModel == null )
      {
         oModel = _oModelFactory.createModel( oModelDef, null );

         _oAppScopeModelMap.put( oModelClass.getName(), oModel );
      }

      return oModel;
   }

   private Object getSessionScopeModel( Config.ModelDefs.ModelDef oModelDef, Class oModelClass, HttpServletRequest oRequest )
      throws ClassNotFoundException, ConstructorNotFoundException, MethodNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException
   {
      HttpSession oHttpSession;
      Object      oModel;

      oHttpSession = oRequest.getSession( true );

      oModel = oHttpSession.getAttribute( oModelClass.getName() );

      if ( oModel != null && _oModelFactory.isModelStale( oModelDef, oModel ) )
      {
         ObjectFactory.destroyObject( oModel );

         oHttpSession.removeAttribute( oModelClass.getName() );

         oModel = null;
      }

      if ( oModel == null )
      {
         oModel = _oModelFactory.createModel( oModelDef, null );

         oHttpSession.setAttribute( oModelClass.getName(), oModel );
      }

      return oModel;
   }

   private Object getRequestScopeModel( Config.ModelDefs.ModelDef oModelDef, HttpServletRequest oRequest )
      throws ClassNotFoundException, ConstructorNotFoundException, MethodNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException
   {
      // a request scope object essentially only lasts for the duration of the method invocation so
      // there is no need to pool a copy of the model instance for reuse
      return _oModelFactory.createModel( oModelDef, oRequest );
   }
}

// EOF