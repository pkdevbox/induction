package com.acciente.dragonfly.controller;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Class description goes here
 *
 * Log
 * Feb 25, 2008 APR -  created
 */
public interface Response extends HttpServletResponse
{
   PrintWriter out() throws IOException;
}
