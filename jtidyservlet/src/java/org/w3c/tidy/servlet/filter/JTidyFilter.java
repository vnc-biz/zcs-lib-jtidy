/*
 *  Java HTML Tidy - JTidy
 *  HTML parser and pretty printer
 *
 *  Copyright (c) 1998-2000 World Wide Web Consortium (Massachusetts
 *  Institute of Technology, Institut National de Recherche en
 *  Informatique et en Automatique, Keio University). All Rights
 *  Reserved.
 *
 *  Contributing Author(s):
 *
 *     Dave Raggett <dsr@w3.org>
 *     Andy Quick <ac.quick@sympatico.ca> (translation to Java)
 *     Gary L Peskin <garyp@firstech.com> (Java development)
 *     Sami Lempinen <sami@lempinen.net> (release management)
 *     Fabrizio Giustina <fgiust at users.sourceforge.net>
 *     Vlad Skarzhevskyy <vlads at users.sourceforge.net> (JTidy servlet  development)
 *
 *  The contributing author(s) would like to thank all those who
 *  helped with testing, bug fixes, and patience.  This wouldn't
 *  have been possible without all of you.
 *
 *  COPYRIGHT NOTICE:
 *
 *  This software and documentation is provided "as is," and
 *  the copyright holders and contributing author(s) make no
 *  representations or warranties, express or implied, including
 *  but not limited to, warranties of merchantability or fitness
 *  for any particular purpose or that the use of the software or
 *  documentation will not infringe any third party patents,
 *  copyrights, trademarks or other rights.
 *
 *  The copyright holders and contributing author(s) will not be
 *  liable for any direct, indirect, special or consequential damages
 *  arising out of any use of the software or documentation, even if
 *  advised of the possibility of such damage.
 *
 *  Permission is hereby granted to use, copy, modify, and distribute
 *  this source code, or portions hereof, documentation and executables,
 *  for any purpose, without fee, subject to the following restrictions:
 *
 *  1. The origin of this source code must not be misrepresented.
 *  2. Altered versions must be plainly marked as such and must
 *     not be misrepresented as being the original source.
 *  3. This Copyright notice may not be removed or altered from any
 *     source or altered source distribution.
 *
 *  The copyright holders and contributing author(s) specifically
 *  permit, without fee, and encourage the use of this source code
 *  as a component for supporting the Hypertext Markup Language in
 *  commercial products. If you use this source code in a product,
 *  acknowledgment is not required but would be appreciated.
 *
 */
package org.w3c.tidy.servlet.filter;
/*
 * Created on 02.10.2004 by vlads
 *
 */
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.tidy.servlet.Consts;
import org.w3c.tidy.servlet.TidyProcessor;
import org.w3c.tidy.servlet.properties.JTidyServletProperties;


/**
 * Wrapp the Response and creates TidyProcessor who does all the work.
 *
 * Use this filter instead of TidyTag if you don't
 * want to modify your JSP pages or HTML is denerated by non JSP servlets.
 *
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com</a> 
 * @version $Revision$ ($Author$)
 */
public class JTidyFilter implements Filter
{
    /**
     * name of the parameter <code>properties.filename</code> containing the properties file path.
     */
    public static final String CONFIG_PROPERTIES_FILE_NAME = Consts.CONFIG_PROPERTIES_FILE_NAME;

    /**
     * name of the parameter <code>config</code> containing JTidy Parser configutation string.
     * 
     * JTidy Parser configutation string
     * Examples of config string: indent: auto; indent-spaces: 2
     */
    public static final String CONFIG_CONFIG = "config";

    /**
     * name of the parameter <code>tee</code>.
     * Do not buffer the output, Preform validation only
     * This may send the responce back to browser while we are still parsing the HTML
     * May solve problem for some applications that are flushing the OutputStream
     */
    public static final String CONFIG_TEE = "tee";

    /**
     * name of the parameter <code>validateOnly</code>.
     * 
     * validateOnly only do not change output.
     */
    public static final String CONFIG_VALIDATE_ONLY = "validateOnly";

    /**
     * name of the parameter <code>doubleValidation</code>.
     * 
     * Performs validation of html processed by &lt;jtidy:tidy&gt; jsp tag
     * By default this is not done. Only Usefull for testing JTidy
     * This will create second requestID to store the data
     */
    public static final String CONFIG_DOUBLE_VALIDATION = "doubleValidation";

    /**
     * name of the parameter <code>commentsSubst</code>.
     * Special html comments:
     * &lt;!--jtidy:requestID--&gt;
     * &lt;!--jtidy:validationImage--&gt;
     * 
     */
    public static final String CONFIG_COMMENTS_SUBST = "commentsSubst";
    /**
     * Logger.
     */
    private Log log;

    /**
     * @see #CONFIG_CONFIG.
     */
    private String config;

    /**
     * @see #CONFIG_VALIDATE_ONLY
     */
    private boolean validateOnly;

    /**
     * @see #CONFIG_TEE.
     */
    private boolean tee;

    /**
     * @see #CONFIG_DOUBLE_VALIDATION.
     */
    private boolean doubleValidation;

    /**
     * @see #CONFIG_COMMENTS_SUBST.
     */
    private boolean commentsSubst;
    
    /**
     * Convert String to beelean.
     * @param value "true"
     * @param defaultValue if value anything else but true.
     * @return Returns boolean value
     */
    private boolean getBoolean(String value, boolean defaultValue)
    {
        if (value != null)
        {
            return value.equalsIgnoreCase("true");
        }
        else
        {
            return defaultValue;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void init(FilterConfig filterConfig)
    {
        log = LogFactory.getLog(JTidyFilter.class);

        JTidyServletProperties.getInstance().loadFile(filterConfig.getInitParameter(CONFIG_PROPERTIES_FILE_NAME));

        tee = getBoolean(filterConfig.getInitParameter(CONFIG_TEE), false);
        doubleValidation = getBoolean(filterConfig.getInitParameter(CONFIG_DOUBLE_VALIDATION), false);
        validateOnly = getBoolean(filterConfig.getInitParameter(CONFIG_VALIDATE_ONLY), false);
        config = filterConfig.getInitParameter(CONFIG_CONFIG);
        commentsSubst = getBoolean(filterConfig.getInitParameter(CONFIG_COMMENTS_SUBST), false);
    }

    /**
     * {@inheritDoc}
     */
    public void destroy()
    {
        // nothing to destroy
    }

    /**
     * Buffer the Response.
     * {@inheritDoc}
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException
    {
        if (!(servletRequest instanceof HttpServletRequest))
        {
            log.debug("Filter has been called, Request is not HTML");
            // don't filter!
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        TidyProcessor tidyProcessor = new TidyProcessor(
            (HttpServletRequest) servletRequest,
            (HttpServletResponse) servletResponse);
        tidyProcessor.setValidateOnly(validateOnly);
        tidyProcessor.setDoubleValidation(doubleValidation);
        tidyProcessor.setConfig(config);
        tidyProcessor.setCommentsSubst(commentsSubst);

        log.debug(((HttpServletRequest) servletRequest).getRequestURI());

        BufferedServletResponse wrappedResponse = new BufferedServletResponse(
            (HttpServletResponse) servletResponse,
            tidyProcessor);
        wrappedResponse.setTee(tee);

        try
        {
            filterChain.doFilter(servletRequest, wrappedResponse);
            log.debug("Filter finished"); 
        }
        finally
        {
            wrappedResponse.finishResponse();
        }
    }

}