package org.w3c.tidy.servlet;
/* orig: package org.displaytag.filter; */

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.tidy.servlet.filter.JTidyFilter;

/**
 * Simulates the behaviour of a filter using a simple servlet. The servlet must be mapped to the "*.filtered" extension;
 * request include this extension after the name of the tested jsp. Since servletunit doesn't support filter testing, we
 * are passing the request to this servlet which calls the filter and then forward the request to the given path without
 * ".filtered".
 * 
 * @author Fabrizio Giustina
 * @author Vlad Skarzhevskyy
 * @version $OrigRevision: 1.5 $ ($OrigAuthor: fgiust $)
 * @version $Revision$ ($Author$)
 */
public class MockFilterSupport extends HttpServlet
{

    /**
     * extension mapped to this servlet.
     */
    public static final String FILTERED_EXTENSION = ".filtered";

    /**
     * logger.
     */
    protected static Log log = LogFactory.getLog(MockFilterSupport.class);

    /**
     * D1597A17A6.
     */
    private static final long serialVersionUID = 899149338534L;

    /**
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        log.debug("Mock servlet called, simulating filter");
        //orig: Filter filter = new ResponseOverrideFilter();
        Filter filter = new JTidyFilter();
        filter.init(new MockFilterConfig(getServletConfig()));
        filter.doFilter(request, response, new MockFilterChain());
    }
    
    /**
     * Simple FilterConfig used to test Filters.
     */
    public static class MockFilterConfig implements FilterConfig
    {
        private ServletConfig servletConfig;
        
        public MockFilterConfig(ServletConfig servletConfig) 
        {
            this.servletConfig = servletConfig;
        }
        
        public String getFilterName()
        {
            return "MockFilter";
        }

        public ServletContext getServletContext()
        {
            return this.servletConfig.getServletContext();
        }

        public String getInitParameter(String s)
        {
            return this.servletConfig.getInitParameter(s);
        }

        public Enumeration getInitParameterNames()
        {
            return this.servletConfig.getInitParameterNames();
        }
    }
    /**
     * Simple FilterChain used to test Filters.
     */
    public static class MockFilterChain implements FilterChain
    {

        /**
         * @see javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
         */
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException
        {
            String uri = ((HttpServletRequest) request).getRequestURI();
            String requestContext = ((HttpServletRequest) request).getContextPath();

            if (StringUtils.isNotEmpty(requestContext) && uri.startsWith(requestContext))
            {
                uri = uri.substring(requestContext.length());
            }

            uri = StringUtils.replace(uri, FILTERED_EXTENSION, "");

            if (log.isDebugEnabled())
            {
                log.debug("Redirecting to [" + uri + "]");
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher(uri);
            dispatcher.forward(request, response);
        }

    }

}