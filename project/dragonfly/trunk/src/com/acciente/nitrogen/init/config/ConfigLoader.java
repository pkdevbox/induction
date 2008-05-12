package com.acciente.nitrogen.init.config;

/**
 * This interface abstracts the mechanism used to load the configuration settings
 * used by the dispatcher.
 *
 * A class implementing this interface is expected to have a single public contructor
 * adhering to the following convention:
 *   - the single constructor should accepts no arguments or
 *   - the single constructor should declare formal parameters using only the
 *     following types,
 *     - javax.servlet.ServletContext
 *     - javax.servlet.ServletConfig
 *
 * Log
 * Mar 15, 2008 APR  -  created
 */
public interface ConfigLoader
{
   Config getConfig() throws ConfigLoaderException;
}

// EOF
