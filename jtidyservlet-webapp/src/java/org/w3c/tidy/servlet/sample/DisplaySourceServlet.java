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
package org.w3c.tidy.servlet.sample;
/*
 * Created on 06.10.2004 by vlads
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.tidy.servlet.util.HTMLEncode;

/**
 * Servlet used to display jsp source for example pages.
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class DisplaySourceServlet extends HttpServlet
{

    /**
     * D1597A17A6.
     */
    private static final long serialVersionUID = 899149338534L;

    /**
     * the folder containg example pages.
     */
    private static final String EXAMPLE_FOLDER = "";

    /**
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException
    {

        //String jspFile = request.getRequestURI();
        String jspFile = request.getServletPath();

        // lastIndexOf(".") can't be null, since the servlet is mapped to ".source"
        int dotIdx = jspFile.lastIndexOf(".");

        String srcType = jspFile.substring(dotIdx);

        jspFile = jspFile.substring(0, dotIdx);

        // .getContextPath()

        /*
         * if (jspFile.lastIndexOf("/") != -1) { jspFile = jspFile.substring(jspFile.lastIndexOf("/") + 1); }
         */

        if (jspFile.equals("/"))
        {
            jspFile = "index.jsp";
        }

        // only want to show sample pages, don't play with url!
        /*
         * if (!jspFile.startsWith("example-")) { throw new ServletException("Invalid file selected: " + jspFile); }
         */

        if ((jspFile.indexOf("..") >= 0) ||
        //  (jspFile.toUpperCase().indexOf("/WEB-INF/") >= 0) ||
            (jspFile.toUpperCase().indexOf("/META-INF/") >= 0))
        {
            throw new ServletException("Invalid file selected: " + jspFile);
        }

        if (srcType.equals(".source"))
        {
            printResourceSrc(response, jspFile);
        }
    }

    private void printResourceSrc(HttpServletResponse response, String jspFile) throws ServletException, IOException
    {
        String fullName = EXAMPLE_FOLDER + jspFile;

        InputStream inputStream = getServletContext().getResourceAsStream(fullName);

        if (inputStream == null)
        {
            throw new ServletException("Unable to find JSP file: [" + jspFile + "]");
        }

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        printHeader(out, jspFile);

        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(inputStream));
        String str;
        int ln = 0;
        while ((str = lnr.readLine()) != null)
        {
            ln = lnr.getLineNumber();
            out.println(HTMLEncode.encode(str));
        }
        printFooter(out, jspFile);

        lnr.close();
        inputStream.close();

    }

    private void printHeader(PrintWriter out, String jspFile) throws IOException
    {
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
        out.println("<head>");
        out.println("<title>");
        out.println("source for " + jspFile);
        out.println("</title>");
        out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" />");
        out.println("</head>");
        out.println("<body>");
        out.println("<pre>");
    }

    private void printFooter(PrintWriter out, String jspFile) throws IOException
    {
        out.println("</pre>");
        out.println("</body>");
        out.println("</html>");
    }

}
