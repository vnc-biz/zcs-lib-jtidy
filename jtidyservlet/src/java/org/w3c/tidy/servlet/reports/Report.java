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
package org.w3c.tidy.servlet.reports;
/*
 * Created on 30.09.2004 by vlads
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.InputStreamReader;
import java.io.Writer;
import java.io.StringReader;
import java.util.Vector;
import java.util.Iterator;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.servlet.ResponseRecord;
import org.w3c.tidy.servlet.ResponseRecordRepository;
import org.w3c.tidy.servlet.properties.JTidyServletProperties;
import org.w3c.tidy.servlet.util.HTMLEncode;

/**
 *
 * Report code used by ReportTag and TidyServlet
 *
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com</a> 
 * @version $Revision$ ($Author$)
 */
public class Report
{
    /**
     * Properties
     */
    private boolean completePage = true;
    
    private boolean view;

    private boolean printSource;

    private boolean wrapSource = true;

    private boolean printHtmlResult = false;

    private int wrapLen = 0;

    private final String RESOURCE_JAVASCRIPT = "JTidyServletReport.js";
    /**
     * Logger.
     */
    private Log log = LogFactory.getLog(Report.class);
    
    /**
     * Internal variables for convenience
     */
    StringBuffer out;
    ResponseRecordRepository responseRecordRepository;
    
    public Report(ResponseRecordRepository responseRecordRepository)
    {
        this.out = new StringBuffer(1024);
        this.responseRecordRepository = responseRecordRepository;
    }

    public Report(HttpSession httpSession)
    {
        this.out = new StringBuffer(1024);
        this.responseRecordRepository = JTidyServletProperties.getInstance().getRepositoryInstance(httpSession);
    }
    
    public void print(Writer writer, String key) throws IOException
    {
        format(key);
        writer.write(this.out.toString());
    }

    void td(String str1, String str2)
    {
        td(str1);
        td(str2);
    }

    void td(String str)
    {
        this.out.append("<td>").append(str).append("</td>\n");
    }

    void tr()
    {
        this.out.append("</tr><tr>\n");
    }
    
    void identSpace(int ln) 
    {
        if (ln < 10)
        {
            this.out.append("   ");
        }
        else if (ln < 100)
        {
            this.out.append("  ");
        }
        else if (ln < 1000)
        {
            this.out.append(" ");
        }
	}
    
    void format(String keyString) throws IOException
    {

        ResponseRecord record = null;

        if (this.responseRecordRepository == null)
        {
            log.info("No ResponseRecordRepository");
        }
        else
        {
            Object key = this.responseRecordRepository.getResponseID(keyString);
            record = this.responseRecordRepository.getRecord(key);
        }

        if (record == null)
        {
            this.out.append("No data for ").append(keyString);
            return;
        }

        if (this.view)
        {
            formatView(record);
        }
        else
        {
            formatReport(record);
        }
    }
    
    void formatView(ResponseRecord record) throws IOException
    {
        if (printHtmlResult)
        {
            this.out.append(record.getHtmlOutput());
        }
        else
        {
            this.out.append(record.getHtmlInput());
        }
    }

    void printScript() 
    {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(RESOURCE_JAVASCRIPT);
        if (in == null)
        {
            log.warn("resource not found:" + RESOURCE_JAVASCRIPT);
            return;
        }
        
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(in));
        String strSource;
        try
        {
            while ((strSource = lnr.readLine()) != null)
            {
                this.out.append(strSource).append("\n");
            }
            lnr.close();
        }
        catch (IOException e)
        {
            log.error("Error Reading file", e);
        }
    }
    
    void formatReport(ResponseRecord record) throws IOException
    {
        if (completePage)
        {
            out.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n");
            out.append("<HTML><head><title>JTidy Messages</title></head><body>\n");
        }

        printScript();
        
        out.append("<table name=\"JTidyMessagesTable\" summary=\"\"><tr>");
        out.append("<td colspan=\"4\">JTidy Messages for request:" + record.getRequestID());
        out.append(" processed in " + record.getParsTime() + " milliseconds");
        tr();
        out.append("<td colspan=\"4\">Validation Errors " + record.getParseErrors() + "</td>\n");
        tr();
        out.append("<td colspan=\"4\">Validation Warnings " + record.getParseWarnings() + "</td>\n");

        HashMap map = new HashMap();

        // Build source list lines
        Vector source = new Vector();
        LineNumberReader lnr = new LineNumberReader(new StringReader(record.getHtmlInput()));
        String strSource;
        while ((strSource = lnr.readLine()) != null)
        {
            // ignore lnr.getLineNumber() for now since lines should be one after another.
            source.add(strSource);
        }

        for (Iterator i = record.getMessages().iterator(); i.hasNext();)
        {
            TidyMessage message = (TidyMessage) i.next();
            tr();
            Integer ln = new Integer(message.getLine());

            StringBuffer lineStr = new StringBuffer(300);
            lineStr.append("Line ");
            if (printSource)
            {
                lineStr.append("<a href=\"#line").append(ln).append("\" ");
                lineStr.append(" onclick=\"jTidyReportHighlight('").append(ln).append("')\" ");
                lineStr.append(">").append(ln).append("</a>");
            }
            else
            {
                lineStr.append(ln);
            }

            if (map.get(ln) == null)
            {
                // Back to Error anchor
                lineStr.append("<a name=\"errline").append(ln).append("\"></a>");
            }
            lineStr.append(", ");

            td(lineStr.toString());

            td("column " + message.getColumn());
            td(message.getLevel().toString() + ":");

            StringBuffer messageStr = new StringBuffer(300);
            // Add popup message
            messageStr.append("<A title=\"");
            messageStr.append(HTMLEncode.encode((String) source.get(message.getLine() - 1)));
            messageStr.append("\">");

            messageStr.append(HTMLEncode.encode(message.getMessage()));

            messageStr.append("</A>");

            td(messageStr.toString());

            map.put(ln, message);
        }

        out.append("</tr>\n");
        out.append("</table>\n");

        if (printSource)
        {
            out.append("<br>Below is the source used for this validation:");

            out.append("<a name=\"JTidyOriginalSource\"></a>");
            out.append("<pre>");

            int ln = 0;
            for (int lnIdx = 0; lnIdx < source.size(); lnIdx++)
            {
                ln = lnIdx + 1;
                String str = (String) source.get(lnIdx);
                TidyMessage message = (TidyMessage) map.get(new Integer(ln));

                out.append("<span id=\"srcline").append(ln).append("\"");
                
                if (message != null)
                {
                    out.append(" style=\"background: #F0DFDF\" ");
                }    
                out.append(">");

                identSpace(ln);
                
                out.append("<a name=\"line");
                out.append(ln);
                out.append("\"></a><strong>");

                if (message == null)
                {
                    out.append(ln);
                }
                else
                {
                    out.append("<a href=\"#errline" + ln + "\">");
                    out.append(ln);
                    out.append("</a>");
                }

                out.append("</strong>");
               
                if ((this.wrapSource) || (this.wrapLen != 0))
                {
                    int useWrapLen = this.wrapLen;
                    if (useWrapLen == 0)
                    {
                        useWrapLen = 100;
                    }
                    str = wrap(str, useWrapLen);
                }

                if (message != null)
                {
                    // Add popup message
                    out.append("<A title=\"");
                    out.append(message.getLevel().toString() + " :");
                    out.append(HTMLEncode.encode(message.getMessage()));
                    out.append("\">");
                }
                // Print Source code
                if (str.length() > 0)
                {
                    out.append("<code class=\"html\">&nbsp;");
                	out.append(HTMLEncode.encode(str));
                	out.append("</code>");
                }
                
                if (message != null)
                {
                    out.append("</A>");
                }
                out.append("</span>");
                out.append("\n");
            }

            // End of file last line + 1
            out.append("<a name=\"line");
            out.append((ln + 1));
            out.append("\"></a>");
            out.append("EOF");
            out.append("</pre>");
        }

        if (printHtmlResult)
        {
            out.append("<br>Below is the generated html code:");

            LineNumberReader lnrr = new LineNumberReader(new StringReader(record.getHtmlOutput()));

            out.append("<a name=\"JTidyHtmlResult\"></a>");
            out.append("<pre>");
            String str;
            int ln = 0;
            while ((str = lnrr.readLine()) != null)
            {
                ln = lnrr.getLineNumber();

                identSpace(ln);
                out.append("<a name=\"resultLine");
                out.append(ln);
                out.append("\"></a><strong>");
                out.append(ln);
                out.append("</strong>");
                
                if ((this.wrapSource) || (this.wrapLen != 0))
                {
                    int useWrapLen = this.wrapLen;
                    if (useWrapLen == 0)
                    {
                        useWrapLen = 100;
                    }
                    str = wrap(str, useWrapLen);
                }

                //  Print Source code
                if (str.length() > 0)
                {
                    out.append("<code class=\"html\">&nbsp;");
                	out.append(HTMLEncode.encode(str));
                	out.append("</code>");
                }
                
                out.append("\n");
            }
            out.append("<a name=\"resultLine");
            out.append((ln + 1));
            out.append("\"></a>");
            out.append("EOF");
            out.append("</pre>");
        }

        if (completePage)
        {
            out.append("</body></HTML>");
        }
    }

    /**
     * Simple line Wrapper by End of Tag.
     * @param str
     * @param wrapLen
     * @return Wraped line
     */
    private String wrap(String str, int wrapLen)
    {
        final String identLine = "    ";
        int strLen = str.length();
        StringBuffer buffer = new StringBuffer(strLen + strLen / wrapLen + 3);

        char[] charAry = str.toCharArray();
        int idx = 0;
        int lineLen = 0;
        boolean inString = false;
        while (idx < charAry.length)
        {
            char c = charAry[idx];
            idx++;
            lineLen++;
            buffer.append(c);
            if ((lineLen >= wrapLen) && (c == '>'))
            {
                buffer.append('\n').append(identLine);
                lineLen = 0;
            }

            if (c == '\"')
            {
                if (inString && (lineLen >= (wrapLen + 10)))
                {
                    buffer.append('\n').append(identLine);
                    lineLen = 0;
                }
                inString = !inString;
            }
        }

        return buffer.toString();
    }

    /**
     * @param completePage The completePage to set.
     */
    public void setCompletePage(boolean completePage)
    {
        this.completePage = completePage;
    }

    /**
     * @param view The view to set.
     */
    public void setView(boolean view)
    {
        this.view = view;
    }

    
    /**
     * @param printSource The printSource to set.
     */
    public void setPrintSource(boolean printSource)
    {
        this.printSource = printSource;
    }

    /**
     * @param wrapSource The wrapSource to set.
     */
    public void setWrapSource(boolean wrapSource)
    {
        this.wrapSource = wrapSource;
    }

    /**
     * @param printHtmlResult The printHtmlResult to set.
     */
    public void setPrintHtmlResult(boolean printHtmlResult)
    {
        this.printHtmlResult = printHtmlResult;
    }

    /**
     * @param wrapLen The wrapLen to set.
     */
    public void setWrapLen(int wrapLen)
    {
        this.wrapLen = wrapLen;
    }
}