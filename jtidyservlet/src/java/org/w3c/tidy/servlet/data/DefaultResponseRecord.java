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
package org.w3c.tidy.servlet.data;
/*
 * Created on 18.09.2004
 *
 */
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.TidyMessageListener;
import org.w3c.tidy.servlet.ResponseRecord;

import java.util.Vector;

/**
 * Data to store Validation results and error.
 *
 * @todo Create the API interface for adding additional attributes like JSP name, action
 *
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@hotmail.com">skarzhevskyy@hotmail.com </a>
 * @version $Revision$ ($Author$)
 *
 */
public class DefaultResponseRecord implements TidyMessageListener, ResponseRecord
{

    private Object requestID;

    private int parseErrors;

    private int parseWarnings;

    private String html;

    private String htmlResult;

    private String report;

    private Vector messages;

    private long parsTime;

    private long when;

    public DefaultResponseRecord()
    {
        messages = new Vector();
        when = System.currentTimeMillis();
    }

    public void messageReceived(TidyMessage message)
    {
        if (message.getLevel().equals(TidyMessage.Level.ERROR))
        {
            parseErrors++;
        } else if (message.getLevel().equals(TidyMessage.Level.WARNING))
        {
            parseWarnings++;
        }
        messages.add(message);
    }

    /**
     * @return Returns the requestID.
     */
    public Object getRequestID()
    {
        return requestID;
    }

    /**
     * @param requestID
     *            The requestID to set.
     */
    public void setRequestID(Object requestID)
    {
        this.requestID = requestID;
    }

    /**
     * @return Returns the html.
     */
    public String getHtmlInput()
    {
        return html;
    }

    /**
     * @param html The html to set.
     */
    public void setHtmlInput(String html)
    {
        this.html = html;
    }

    /**
     * @return Returns the parseErrors.
     */
    public int getParseErrors()
    {
        return parseErrors;
    }

    /**
     * @param parseErrors
     *            The parseErrors to set.
     */
    public void setParseErrors(int parseErrors)
    {
        this.parseErrors = parseErrors;
    }

    /**
     * @return Returns the parseWarnings.
     */
    public int getParseWarnings()
    {
        return parseWarnings;
    }

    /**
     * @param parseWarnings
     *            The parseWarnings to set.
     */
    public void setParseWarnings(int parseWarnings)
    {
        this.parseWarnings = parseWarnings;
    }

    /**
     * @return Returns the report.
     */
    public String getReport()
    {
        return report;
    }

    /**
     * @param report
     *            The report to set.
     */
    public void setReport(String report)
    {
        this.report = report;
    }

    /**
     * @return Returns the messages.
     */
    public Vector getMessages()
    {
        return messages;
    }

    /**
     * @return Returns the parsTime.
     */
    public long getParsTime()
    {
        return parsTime;
    }

    /**
     * @param parsTime The parsTime to set.
     */
    public void setParsTime(long parsTime)
    {
        this.parsTime = parsTime;
    }

    /**
     * @return Returns the when.
     */
    public long getWhen()
    {
        return when;
    }
    /**
     * @return Returns the htmlResult.
     */
    public String getHtmlOutput()
    {
        return htmlResult;
    }
    /**
     * @param html The htmlResult to set.
     */
    public void setHtmlOutput(String html)
    {
        this.htmlResult = html;
    }
    
	/**
	 * @return Returns the part of ImageName shown as icon or null to use default implementation
	 */
	public String getImageName()
    {
        String imageName = "unknown";
        if (getParseErrors() != 0)
        {
            imageName = "error";
        }
        else if (getParseWarnings() != 0)
        {
            imageName = "warning";
        }
        else
        {
            imageName = "ok";
        }
        return imageName;
    }
}