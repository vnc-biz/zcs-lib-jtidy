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
package org.w3c.tidy.servlet.jsp.tagext;
/*
 * Created on 30.09.2004
 */
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.tidy.servlet.Consts;
import org.w3c.tidy.servlet.RepositoryFactory;
import org.w3c.tidy.servlet.TidyServlet;
import org.w3c.tidy.servlet.properties.JTidyServletProperties;
import org.w3c.tidy.servlet.reports.Report;
import org.w3c.tidy.servlet.util.HTMLEncode;

/**
 * Print the same report as TidyServlet base on JTidy HTML Validation
 * See tagExample.jsp for usage example.
 *
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com</a>
 * @version $Revision$ ($Author$)
 *
 */
public class ReportTag extends TagSupport
{

    private boolean href;
    private boolean view;
    
    private boolean source = true;

    private boolean sourceResult = false;

    private boolean wrapSource = true;

    private int wrapLen = 0;

    private String requestID = null;

    /**
     * logger.
     */
    private static Log log = LogFactory.getLog(ReportTag.class);

    /**
     * {@inheritDoc}
     */
    public int doEndTag() throws JspException
    {
        try
        {
            if (href)
            {
                StringBuffer out = new StringBuffer(120);
                
                out.append(((HttpServletRequest) pageContext.getRequest()).getContextPath());
                out.append(JTidyServletProperties.getInstance().getProperty(JTidyServletProperties.JTIDYSERVLET_URI, Consts.DEFAULT_JTIDYSERVLET_URI));

                HashMap params = new HashMap();
                String requestID = this.requestID;
                if (requestID.equalsIgnoreCase("this"))
                {
                    RepositoryFactory factory = JTidyServletProperties.getInstance().getRepositoryFactoryInstance();
                    Object key = factory.getResponseID(
                        (HttpServletRequest) pageContext.getRequest(),
                        (HttpServletResponse) pageContext.getResponse(),
                        false);
                    requestID = key.toString();
                }
                params.put(TidyServlet.PARAM_REQUEST_ID, requestID);
                String TRUE = "1";
                
                if (this.view)
                {
                    params.put(TidyServlet.PARAM_ACTION, TidyServlet.ACTION_VIEW);
                }
                else
                {
                    params.put(TidyServlet.PARAM_ACTION, TidyServlet.ACTION_REPORT);
                }
                
                if (this.source) 
                {
                    params.put(TidyServlet.ACTION_REPORT_PARAM_SRC_ORG, TRUE);
                }
                
                if (this.sourceResult) 
                {
                    params.put(TidyServlet.ACTION_REPORT_PARAM_SRC_RESULT, TRUE);
                }
                
                out.append(HTMLEncode.encodeHREFQuery("", params, true)); 

                
                pageContext.getOut().print(out);
            }
            else
            {
                Report report = new Report(pageContext.getSession());

                report.setCompletePage(false);
                report.setPrintSource(this.source);
                report.setPrintHtmlResult(this.sourceResult);
                report.setWrapSource(this.wrapSource);
                report.setWrapLen(this.wrapLen);
                report.print(pageContext.getOut(), this.requestID);
            }

        }
        catch (IOException e)
        {
            log.error("ReportTag write error", e);
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    /**
     * {@inheritDoc}
     */
    public void release()
    {
        super.release();
        // Set the default values they are not set is attribute is absent
        this.source = true;
        this.wrapSource = true;
        this.requestID = null;
        this.sourceResult = false;
    }

    /**
     * @param requestID The requestID to set.
     */
    public void setRequestID(String requestID)
    {
        this.requestID = requestID;
    }
    /**
     * @param source The source to set.
     */
    public void setSource(boolean source)
    {
        this.source = source;
    }
    /**
     * @param wrapSource The wrapSource to set.
     */
    public void setWrapSource(boolean wrapSource)
    {
        this.wrapSource = wrapSource;
    }
    /**
     * @param wrapLen The wrapLen to set.
     */
    public void setWrapLen(int wrapLen)
    {
        this.wrapLen = wrapLen;
    }
    /**
     * @param sourceResult The sourceResult to set.
     */
    public void setSourceResult(boolean sourceResult)
    {
        this.sourceResult = sourceResult;
    }
    
    /**
     * @param href The href to set.
     */
    public void setHref(boolean href)
    {
        this.href = href;
    }
    /**
     * @param view The view to set.
     */
    public void setView(boolean view)
    {
        this.view = view;
    }
}