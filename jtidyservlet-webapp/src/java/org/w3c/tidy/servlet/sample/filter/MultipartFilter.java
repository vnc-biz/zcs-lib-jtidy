/*
 * Created on 18.10.2004 by vlads
 */
package org.w3c.tidy.servlet.sample.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUpload;

/**
 *  Use jakarta apache Commons FileUpload to handle multipart/form-data.
 * Add this fileter to web.xml and multipart request will look as ordinary request.
 * Use MultipartRequestWrapper class to access the files uploaded. 
 * 
 * @see <a href="http://java.sun.com/blueprints/corej2eepatterns/Patterns/InterceptingFilter.html">
 * Core J2EE Patterns - Intercepting Filter</a>
 * @see <a href="http://jakarta.apache.org/commons/fileupload/">Jakarta Commons FileUpload</a>
 * @see <a href="http://www.servlets.com/cos/index.html">oreilly.servlet package by Jason Hunter</a>
 * 
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com</a> 
 * @version $Revision$ ($Author$)
 */

public class MultipartFilter implements Filter
{

    /**
     * {@inheritDoc}
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    /**
     * {@inheritDoc}
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     * javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException
    {

        HttpServletRequest hrequest = (HttpServletRequest) request;
        // Check "Content-Type"
        if (!FileUpload.isMultipartContent(hrequest))
        {
            chain.doFilter(request, response);
        }
        else
        {
            MultipartRequestWrapper mrequest = new MultipartRequestWrapper(hrequest);
            chain.doFilter(mrequest, response);
        }
    }

    /**
     * {@inheritDoc}
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy()
    {
    }
}