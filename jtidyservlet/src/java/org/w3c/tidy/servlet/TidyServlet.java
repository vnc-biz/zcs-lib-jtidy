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
package org.w3c.tidy.servlet;
/*
 * Created on 19.09.2004 by vlads
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.tidy.servlet.reports.Report;
import org.w3c.tidy.servlet.properties.JTidyServletProperties;

/**
 * This calss produce JTidy processing results and icons in your web application.
 *
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com</a>
 * @version $Revision$ ($Author$)
 */
public class TidyServlet extends HttpServlet
{
    /**
     * name of the parameter containing the properties file path.
     */
    public static final String CONFIG_PROPERTIES_FILE_NAME = Consts.CONFIG_PROPERTIES_FILE_NAME;

    /**
     * The form parameter which defines what should be returned.
     */
    public static final String PARAM_REQUEST_ID = "requestID";
    
    public static final String PARAM_ACTION = "action";
    
    public static final String ACTION_IMAGE = "image";
    public static final String ACTION_IMAGE_PARAM_SRC_ONLY = "srcOnly";

    public static final String ACTION_VIEW = "view";
    
    public static final String ACTION_REPORT = "report";
    
    public static final String ACTION_REPORT_PARAM_SRC_ORG = "src";
    public static final String ACTION_REPORT_PARAM_SRC_RESULT = "result";


    
    private static final String RESOURCE_PREFIX = "";

    /**
     * Logger.
     */
    private Log log = LogFactory.getLog(TidyServlet.class);

    private JTidyServletProperties properties = JTidyServletProperties.getInstance();

    /**
     * Retrieve the confiuration parameters and set the properties.
     * @param servletConfig ServletConfig
     * @throws ServletException generic exception
     * @see javax.servlet.Servlet#init(ServletConfig)
     */
    public final void init(ServletConfig servletConfig) throws ServletException
    {

        super.init(servletConfig);

        // get the parameter and initialize properties
        properties.loadFile(getInitParameter(CONFIG_PROPERTIES_FILE_NAME));
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException
    {
        selectAction(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        selectAction(request, response);
    }

    void selectAction(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException
    {
        String action = request.getParameter(PARAM_ACTION);
        String id = request.getParameter(PARAM_REQUEST_ID);
        if (action == null)
        {
            return;
        }

        if (action.equalsIgnoreCase(ACTION_IMAGE))
        {
            redirect2Image(request, response, id);
        } 
        else if (action.equalsIgnoreCase(ACTION_REPORT))
        {
            printReport(request, response, id, false);
        }
        else if (action.equalsIgnoreCase(ACTION_VIEW))
        {
            printReport(request, response, id, true);
        }
    }

    void redirect2Image(HttpServletRequest request, HttpServletResponse response, String id)
            throws IOException, ServletException
    {

        String imageNamePrefix = properties.getProperty(JTidyServletProperties.PROPERTY_STRING_IMAGENAMEPREFIX,
                Consts.DEFAULT_IMAGE_NAME_PREFIX);
        String imageNameExtension = properties.getProperty(JTidyServletProperties.PROPERTY_STRING_IMAGENAMEEXTENSION,
                ".gif");

        ResponseRecordRepository rrr = JTidyServletProperties.getInstance().getRepositoryInstance(request.getSession());
        if (rrr == null)
        {
            log.info("No ResponseRecordRepository");
            // Return empty image
            response.setContentType("image/gif");
            response.setContentLength(0);
            return;
        }
        Object key = rrr.getResponseID(id);

        ResponseRecord record = rrr.getRecord(key, properties.getIntProperty(
                JTidyServletProperties.PROPERTY_INT_IMAGEGETTIMEOUT, 2000));

        String imageName = "unknown";
        if (record != null)
        {
            imageName = record.getImageName();
        }
        else
        {
            log.debug("ResponseRecord not found for ID " + id);
        }

        StringBuffer imageURL = new StringBuffer(40);
        imageURL.append(imageNamePrefix).append(imageName).append(imageNameExtension);
        if (log.isDebugEnabled())
        {
            log.debug("ResultsDO for ID " + id + " -> " + imageURL);
        }

        String src = request.getParameter(ACTION_IMAGE_PARAM_SRC_ONLY);
        if (src != null)
        {
            PrintWriter out = response.getWriter();
            out.print(imageURL);
        }
        else
        {
            if (!streamResource(imageURL.toString(), response))
            {
                getServletContext().getRequestDispatcher(imageURL.toString()).forward(request, response);
            }
        }
    }

    private boolean streamResource(String resource, HttpServletResponse response) throws IOException
    {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(RESOURCE_PREFIX + resource);

        if (in == null)
        {
            // Load image from application if not found in jar
            in = getServletContext().getResourceAsStream("/" + RESOURCE_PREFIX + resource);
        }

        if (in == null)
        {
            log.warn("resource not found:" + resource);
            return false;
        }
        if (resource.endsWith(".gif"))
        {
            response.setContentType("image/gif");
        }
        else if (resource.endsWith(".png"))
        {
            response.setContentType("image/png");
        }
        else if (resource.endsWith(".jpg") || resource.endsWith(".jpeg"))
        {
            response.setContentType("image/jpeg");
        }
        else
        {
            in.close();
            return false;
        }

        final int BUFFER_SIZE = 2048;
        byte[] buffer = new byte[BUFFER_SIZE];
        int r = 0;
        OutputStream out = response.getOutputStream();
        if ((r = in.read(buffer)) != -1)
        {
            if (r < BUFFER_SIZE)
            {
                // Image fit completely in buffer.
                response.setContentLength(r);
            }
            out.write(buffer, 0, r);
        }
        while ((r = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, r);
        }
        in.close();
        return true;
    }

    void printReport(HttpServletRequest request, HttpServletResponse response, String id, boolean view)
        throws IOException, ServletException
    {
        response.setContentType("text/html");

        Report report = new Report(request.getSession());

        report.setCompletePage(true);

        report.setView(view);

        if (request.getParameter(ACTION_REPORT_PARAM_SRC_ORG) != null)
        {
            report.setPrintSource(true);
        }
        if (request.getParameter(ACTION_REPORT_PARAM_SRC_RESULT) != null)
        {
            report.setPrintHtmlResult(true);
        }

        report.print(response.getWriter(), id);
    }
}
