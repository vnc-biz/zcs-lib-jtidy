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
 * Created on 18.09.2004
 */
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;

import org.w3c.tidy.servlet.Consts;
import org.w3c.tidy.servlet.TidyServlet;
import org.w3c.tidy.servlet.RepositoryFactory;
import org.w3c.tidy.servlet.properties.JTidyServletProperties;
import org.w3c.tidy.servlet.util.HTMLEncode;
/**
 * Show Image base on JTidy HTML Validation See tagExample.jsp for usage example.
 *
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com</a> 
 * @version $Revision$ ($Author$)
 */
public class ValidationImageTag extends TagSupport
{

    private boolean srcOnly;

    private String requestID;

    private String imgName;

    /**
     * javascript Event
     */
    private String onclick;

    public int doEndTag() throws JspException
    {
        try
        {
            StringBuffer out = new StringBuffer(120);

            String requestID = this.requestID;
            
            JTidyServletProperties properties = JTidyServletProperties.getInstance();
            
            if (requestID == null)
            {
                RepositoryFactory factory = properties.getRepositoryFactoryInstance();
                Object key = factory.getResponseID(
                    (HttpServletRequest) pageContext.getRequest(),
                    (HttpServletResponse) pageContext.getResponse(),
                    false);
                requestID = key.toString();
            }

            String servletURI = ((HttpServletRequest) pageContext.getRequest()).getContextPath()
                    + properties.getProperty(JTidyServletProperties.JTIDYSERVLET_URI, Consts.DEFAULT_JTIDYSERVLET_URI);

            if (srcOnly)
            {
                out.append(servletURI).append("?");
                out.append(TidyServlet.PARAM_REQUEST_ID).append("=").append(requestID);
                out.append(TidyServlet.PARAM_ACTION).append("=").append(TidyServlet.ACTION_IMAGE);
            } 
            else
            {
                String imgName = this.imgName;
                if (imgName == null)
                {
                    imgName = "JTidyValidationImage";
                }

                out.append("<a name=\"").append(imgName).append("Link\" href=\"");
                out.append(HTMLEncode.encodeHREFQuery(servletURI, 
                    new String[]{
                    TidyServlet.PARAM_REQUEST_ID, requestID,
                    TidyServlet.PARAM_ACTION, TidyServlet.ACTION_REPORT,
                    TidyServlet.ACTION_REPORT_PARAM_SRC_ORG, "1"}));
                
                out.append("\" ");

                if ((onclick != null) && (onclick.length() > 0))
                {
                    out.append("onclick=\"").append(onclick).append("\"");
                }
                out.append(">");

                out.append("<img name=\"").append(imgName).append("\" alt=\"Page Validation\" ");
                out.append("src=\"");
                out.append(HTMLEncode.encodeHREFQuery(servletURI, 
                    new String[]{
                    TidyServlet.PARAM_REQUEST_ID, requestID,
                    TidyServlet.PARAM_ACTION, TidyServlet.ACTION_IMAGE}));
                
                out.append("\" width=\"").append(
                    properties.getProperty(JTidyServletProperties.PROPERTY_STRING_IMAGE_WIDTH, "32"));
                out.append("\" height=\"").append(
                    properties.getProperty(JTidyServletProperties.PROPERTY_STRING_IMAGE_HEIGHT, "26"));
                out.append("\" border=\"0\" hspace=\"0\" align=middle></a>");
            }

            pageContext.getOut().write(out.toString());
        }
        catch (IOException e)
        {
            LogFactory.getLog(this.getClass()).error("ValidationImageTag write error", e);
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release()
    {
        super.release();
        this.srcOnly = false;
        this.requestID = null;
        this.onclick = null;
        this.imgName = null;
    }

    /**
     * @param srcOnly The srcOnly to set.
     */
    public void setSrcOnly(boolean srcOnly)
    {
        this.srcOnly = srcOnly;
    }

    /**
     * @param requestID The requestID to set.
     */
    public void setRequestID(String requestID)
    {
        this.requestID = requestID;
    }
    /**
     * @param onclick The onclick to set.
     */
    public void setOnclick(String onclick)
    {
        this.onclick = onclick;
    }
    /**
     * @param imgName The imgName to set.
     */
    public void setImgName(String imgName)
    {
        this.imgName = imgName;
    }
}