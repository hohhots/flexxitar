/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nm114.xitar.filters;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nm114.xitar.classes.CONFIG;

/**
 *
 * @author brgd
 */
public class accessStageFilter implements Filter {

    private static final boolean debug = false;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public accessStageFilter() {
    } 

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
	throws IOException, ServletException {
            if (debug) log("accessStageFilter:DoBeforeProcessing");

            Boolean validUser = true;

            HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse resp = (HttpServletResponse)response;

            req.setAttribute(CONFIG.NO_SESSION, false);
            HttpSession hs = req.getSession(false);

            String uri = req.getRequestURI();
            try {
                //hs.setMaxInactiveInterval(CONFIG.SESSION_OUTTIME);
                if(!req.getRemoteAddr().equals((String)hs.getAttribute(CONFIG.REMOTEADDR)) &&
                  (!req.getHeader("user-agent").equals((String)hs.getAttribute(CONFIG.USERAGENT)))){ // if remote address is same and browser type is same
                    hs.invalidate();
                    validUser = false;
                    req.setAttribute(CONFIG.NO_SESSION, true);
                }
            } catch(NullPointerException e) { //If has no session
                if(!uri.contains(CONFIG.SESSION_SERVLET_URL)){
                    validUser = false;
                }
            }

            if(!validUser){ //Return signal for no session
                PrintWriter out = resp.getWriter();
                try {
                    out.print(CONFIG.REFRESH);
                } finally {
                    out.close();
                }
            }
    } 

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
	throws IOException, ServletException {
	if (debug) log("accessStageFilter:DoAfterProcessing");
	
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
	throws IOException, ServletException {

	if (debug) log("accessStageFilter:doFilter()");

	doBeforeProcessing(request, response);
	
	Throwable problem = null;
	try {
	    chain.doFilter(request, response);
	}
	catch(Throwable t) {
	    // If an exception is thrown somewhere down the filter chain,
	    // we still want to execute our after processing, and then
	    // rethrow the problem after that.
	    problem = t;
	    t.printStackTrace();
	}

	doAfterProcessing(request, response);

	// If there was a problem, we want to rethrow it if it is
	// a known type, otherwise log it.
	if (problem != null) {
	    if (problem instanceof ServletException) throw (ServletException)problem;
	    if (problem instanceof IOException) throw (IOException)problem;
	    sendProcessingError(problem, response);
	}
    }
    
    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
	return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
	this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter 
     */
    public void destroy() { 
    }

    /**
     * Init method for this filter 
     */
    public void init(FilterConfig filterConfig) { 
	this.filterConfig = filterConfig;
	if (filterConfig != null) {
	    if (debug) { 
		log("accessStageFilter:Initializing filter");
	    }
	}
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
	if (filterConfig == null) return ("accessStageFilter()");
	StringBuffer sb = new StringBuffer("accessStageFilter(");
	sb.append(filterConfig);
	sb.append(")");
	return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
	String stackTrace = getStackTrace(t); 

	if(stackTrace != null && !stackTrace.equals("")) {
	    try {
		response.setContentType("text/html");
		PrintStream ps = new PrintStream(response.getOutputStream());
		PrintWriter pw = new PrintWriter(ps); 
		pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N
		    
		// PENDING! Localize this for next official release
		pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n"); 
		pw.print(stackTrace); 
		pw.print("</pre></body>\n</html>"); //NOI18N
		pw.close();
		ps.close();
		response.getOutputStream().close();
	    }
	    catch(Exception ex) {}
	}
	else {
	    try {
		PrintStream ps = new PrintStream(response.getOutputStream());
		t.printStackTrace(ps);
		ps.close();
		response.getOutputStream().close();
	    }
	    catch(Exception ex) {}
	}
    }

    public static String getStackTrace(Throwable t) {
	String stackTrace = null;
	try {
	    StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    t.printStackTrace(pw);
	    pw.close();
	    sw.close();
	    stackTrace = sw.getBuffer().toString();
	}
	catch(Exception ex) {}
	return stackTrace;
    }

    public void log(String msg) {
	filterConfig.getServletContext().log(msg); 
    }

}
