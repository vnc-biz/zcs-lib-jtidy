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
 * Created on 02.10.2004 by vlads
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.servlet.jsp.tagext.ValidationImageTag;
import org.w3c.tidy.servlet.properties.JTidyServletProperties;


/**
 * Common class used by Filter and Tag to process responce.
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com </a>
 * @version $Revision$ ($Author$)
 */
public class TidyProcessor
{

    /**
     * The request with which this Processor is associated.
     */
    HttpSession httpSession;

    HttpServletRequest request;

    HttpServletResponse response;

    /**
     * JTidy Parser configutation string Examples of config string: indent: auto; indent-spaces: 2
     */
    private String config;

    /**
     * validateOnly only do not change output.
     */
    private boolean validateOnly;

    /**
     * Performs validation of html processed by &lt;jtidy:tidy&gt; jsp tag By default this is not done. Only Usefull for
     * testing JTidy This will create second requestID to store the data
     */
    private boolean doubleValidation;

    private boolean commentsSubst;

    /**
     * Logger.
     */
    private Log log = LogFactory.getLog(TidyProcessor.class);

    /**
     * Initialize Processor.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public TidyProcessor(
        HttpSession httpSession,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse)
    {
        this.httpSession = httpSession;
        this.request = httpServletRequest;
        this.response = httpServletResponse;
    }

    /*
     * Parser for JTidy configutation. Examples of config string: indent: auto; indent-spaces: 2 @param JTidy
     * Configuration to change
     */
    private void parsConfig(Configuration configuration)
    {
        if (config == null)
        {
            return;
        }
        Properties properties = new Properties();
        // assume Java 1.3 with no regex
        StringTokenizer st = new StringTokenizer(config, ";");
        while (st.hasMoreTokens())
        {
            String nv = st.nextToken();
            int split = nv.indexOf(':');
            if (split > 0)
            {
                String n = nv.substring(0, split).trim();
                String v = nv.substring(split + 1).trim();
                if (Configuration.isKnownOption(n))
                {
                    properties.put(n, v);
                    log.debug("add option " + n + "=" + v);
                }
                else
                {
                    log.warn("TidyTag unknown option " + n);
                }
            }
        }
        configuration.addProps(properties);
        configuration.adjust();
    }

    public boolean parse(InputStream in, OutputStream out, String html)
    {
        if (this.request.getAttribute(Consts.ATTRIBUTE_IGNORE) != null)
        {
            log.debug("IGNORE");
            return false;
        }

        RepositoryFactory factory = JTidyServletProperties.getInstance().getRepositoryFactoryInstance();

        Object requestID = factory.getResponseID(this.httpSession, this.request, this.response, false);
        if (requestID == null)
        {
            log.debug("IGNORE requestID == null");
            return false;
        }

        boolean secondPass = false;
        // Avoid Double processing by Tag and Filter
        if (this.request.getAttribute(Consts.ATTRIBUTE_PROCESSED) != null)
        {
            if (!doubleValidation)
            {
                log.debug("IGNORE !doubleValidation");
                return false;
            }
            requestID = factory.getResponseID(this.httpSession, this.request, this.response, true);
            secondPass = true;
        }

        if (!secondPass)
        {
            log.debug("addCookie");
            this.response.addCookie(new Cookie(Consts.ATTRIBUTE_REQUEST_ID, requestID.toString()));
        }

        boolean rc = parse(in, out, html, requestID, factory);

        if (!secondPass)
        {
            // this.request.setAttribute(Consts.ATTRIBUTE_PROCESSED, shortMessage);
            this.request.setAttribute(Consts.ATTRIBUTE_PROCESSED, requestID);
        }

        if (rc && (!this.validateOnly) && (this.request.getAttribute(Consts.ATTRIBUTE_PASS) != null))
        {
            rc = false;
        }

        return rc;
    }

    public boolean parse(InputStream in, OutputStream out, String html, Object requestID, RepositoryFactory factory)
    {
        long start = System.currentTimeMillis();

        Tidy tidy = new Tidy();
        parsConfig(tidy.getConfiguration());
        tidy.setSmartIndent(true);
        tidy.setQuiet(true);

        ByteArrayOutputStream mesageBuffer = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(mesageBuffer);
        tidy.setErrout(pw);

        boolean useOut = false;

        ResponseRecord result = factory.createRecord(requestID, this.httpSession, this.request, this.response);
        result.setRequestID(requestID);
        tidy.setMessageListener(result);

        boolean fatalError = false;

        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();

        try
        {
            log.debug("processing request " + requestID + "...");
            tidy.parse(in, outBuffer);
            useOut = (result.getParseErrors() == 0);
            if (commentsSubst)
            {
                doCommentsSubst(outBuffer, requestID);
            }
            if (out != null)
            {
                outBuffer.writeTo(out);
            }
        }
        catch (Throwable e)
        {
            log.error("JTidy parsing error", e);
            result.messageReceived(new TidyMessage(0, 0, 0, TidyMessage.Level.ERROR, "JTidy parsing error"
                + e.getMessage()));
            fatalError = true;
        }

        // result.setParseErrors(tidy.getParseErrors());
        // result.setParseWarnings(tidy.getParseWarnings());

        result.setHtmlInput(html);

        if ((result.getParseErrors() > 0) || fatalError)
        {
            result.setHtmlOutput(html);
        }
        else
        {
            result.setHtmlOutput(outBuffer.toString());
        }

        if (!fatalError)
        {
            // @todo can't flush in tag body
            // pw.flush();
            // result.setReport(mesageBuffer.toString());
        }

        long time = System.currentTimeMillis() - start;
        result.setParsTime(time);
        if (log.isDebugEnabled())
        {
            log.debug("processed in " + time + " millis");
        }

        ResponseRecordRepository repository = factory.getRepositoryInstance(this.httpSession);
        repository.addRecord(result);

        if (JTidyServletProperties.getInstance().getBooleanProperty(
            JTidyServletProperties.PROPERTY_BOOLEAN_LOG_VALIDATION_MESSAGES,
            false))
        {
            for (Iterator iter = result.getMessages().iterator(); iter.hasNext();)
            {
                TidyMessage message = (TidyMessage) iter.next();
                StringBuffer msg = new StringBuffer(); 
                msg.append(message.getLevel());
                msg.append(" (L").append(message.getLine());
                msg.append(":C").append(message.getColumn()).append(") ");
                msg.append(message.getMessage());
                log.info(msg.toString());
            }
        }

        String shortMessage;
        if ((result.getParseErrors() != 0) || (result.getParseWarnings() != 0))
        {
            if (result.getParseErrors() == 0)
            {
                shortMessage = "found " + result.getParseWarnings() + " warnings in generated HTML";
            }
            else
            {
                shortMessage = "found "
                    + result.getParseErrors()
                    + " errors and "
                    + result.getParseWarnings()
                    + " warnings in generated HTML";
            }
        }
        else
        {
            shortMessage = "no problems found";
        }

        log.info(shortMessage + " request " + requestID);

        return (useOut && (out != null));
    }

    private void doCommentsSubst(ByteArrayOutputStream outBuffer, Object requestID)
    {
        log.debug("doCommentsSubst");
        // Prohibit caching of application pages.
        if (response != null)
        {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", -1);
        }

        String html = outBuffer.toString();
        html = replaceAll(html, "<!--jtidy:requestID-->", requestID.toString());
        String aLink = ValidationImageTag.getImageHTML(requestID.toString(), null, null, request);
        html = replaceAll(html, "<!--jtidy:validationImage-->", aLink);
        outBuffer.reset();
        try
        {
            // to-do charsetName
            outBuffer.write(html.getBytes());
        }
        catch (IOException e)
        {
            log.error("Internal error", e);
        }
    }

    /**
     * @param config The config to set.
     */
    public void setConfig(String config)
    {
        this.config = config;
    }

    /**
     * @return Returns the doubleValidation.
     */
    public boolean isDoubleValidation()
    {
        return doubleValidation;
    }

    /**
     * @param doubleValidation The doubleValidation to set.
     */
    public void setDoubleValidation(boolean doubleValidation)
    {
        this.doubleValidation = doubleValidation;
    }

    /**
     * @param validateOnly The validateOnly to set.
     */
    public void setValidateOnly(boolean validateOnly)
    {
        this.validateOnly = validateOnly;
    }

    /**
     * @param commentsSubst The commentsSubst to set.
     */
    public void setCommentsSubst(boolean commentsSubst)
    {
        this.commentsSubst = commentsSubst;
    }

    /**
     * jre 1.3 compatible replaceAll.
     * @param str text to search and replace in
     * @param replace the String to search for
     * @param replacement the String to replace with
     * @return the text with any replacements processed
     */
    public String replaceAll(String str, String replace, String replacement)
    {
        StringBuffer sb = new StringBuffer(str);
        int firstOccurrence = str.indexOf(replace);

        while (firstOccurrence != -1)
        {
            sb.replace(firstOccurrence, firstOccurrence + replace.length(), replacement);
            firstOccurrence = sb.toString().indexOf(replace);
        }

        return sb.toString();
    }

}
