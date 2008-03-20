package com.acciente.dragonfly.model;

import com.acciente.dragonfly.init.config.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Hashtable;
import java.util.Map;

/**
 * This class manages access to the model objects.
 *
 * Log
 * Mar 16, 2008 APR  -  created
 */
public class ModelLifeCycleManager
{
   private Config.ModelDefs   _oModelDefs;
   private Map                _oAppScopeModelMap;
   private ModelFactory       _oModelFactory;

   public ModelLifeCycleManager( Config.ModelDefs oModelDefs, ModelFactory oModelFactory )
   {
      _oModelDefs          = oModelDefs;
      _oModelFactory       = oModelFactory;
      _oAppScopeModelMap   = new Hashtable();   // we use a hashtable instead of a HashMap for safe concurrent access
   }

   public Object getModel( Class oModelClass, HttpServletRequest oRequest )
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
         oModel = getRequestScopeModel( oModelDef );
      }
      else
      {
         throw new IllegalArgumentException( "model-error: unknown scope for model class: " + oModelClass.getName() );
      }

      return oModel;
   }

   private Object getApplicationScopeModel( Config.ModelDefs.ModelDef oModelDef, Class oModelClass )
   {
      String   sInstanceKey;
      Object   oModel;

      // the instance key is intended to be unique for each model instance+class, we add the hashCode() to
      // so that if the model class is dynamically reloaded the model instance will get recreated
      sInstanceKey = oModelClass.getName() + oModelClass.hashCode();
      oModel       = _oAppScopeModelMap.get( sInstanceKey );

      if ( oModel == null )
      {
         // todo: fix call below

         //_oAppScopeModelMap.put( sInstanceKey, _oModelFactory.createModel( oModelDef ) );
      }

      return oModel;
   }

   private Object getSessionScopeModel( Config.ModelDefs.ModelDef oModelDef, Class oModelClass, HttpServletRequest oRequest )
   {
      HttpSession oHttpSession;
      String      sInstanceKey;
      Object      oModel;

      oHttpSession = oRequest.getSession( true );
      // the instance key is intended to be unique for each model instance+class, we add the hashCode() to
      // so that if the model class is dynamically reloaded the model instance will get recreated
      sInstanceKey = oModelClass.getName() + oModelClass.hashCode();
      oModel       = oHttpSession.getAttribute( sInstanceKey );

      if ( oModel == null )
      {
         // todo: fix call below
         //oHttpSession.setAttribute( sInstanceKey, _oModelFactory.createModel( oModelDef ) );
      }

      return oModel;
   }

   private Object getRequestScopeModel( Config.ModelDefs.ModelDef oModelDef )
   {
      // a request scope object essentially only lasts for the duration of the method invocation so
      // there is no need to cache a copy of the model instance for reuse

      // todo: fix call below
      //return _oModelFactory.createModel( oModelDef );
      return null;
   }
}

// EOF