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
 * Created on 31.10.2004 by vlads
 */
import java.io.InputStream;
import java.io.*;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.tidy.servlet.properties.JTidyServletProperties;

/**
 * 
 * 
 */
public class TidyServletHelper
{
    
    /**
     * Logger.
     */
    private static Log log = LogFactory.getLog(TidyServletHelper.class);
    
    public static ResponseRecordRepository getRecordRepository(HttpSession httpSession) 
    {
        return JTidyServletProperties.getInstance().getRepositoryInstance(httpSession);
    }
    
    public static String getResponseID(String keyString, HttpSession httpSession)
    {
        ResponseRecordRepository rep = getRecordRepository(httpSession);
        if (rep == null)
        {
            return "";
        }
        Object key = rep.getResponseID(keyString);
        if (key != null)
        {
            return key.toString();
        }
        else
        {
            return "";
        }
    }
    
    public static String process(InputStream in, HttpSession httpSession) {
        RepositoryFactory factory = JTidyServletProperties.getInstance().getRepositoryFactoryInstance();
        
        TidyProcessor tidyProcessor = new TidyProcessor(
            httpSession,
            null,
            null);
        tidyProcessor.setValidateOnly(true);
        
        Object requestID = factory.getResponseID(httpSession, null, null, true);
        
        StringBuffer html = new StringBuffer();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
        String line;
        try
        {
            while ((line = buffer.readLine()) != null)
            {
                html.append(line).append("\n");
            }
            buffer.close();
        }
        catch (IOException e)
        {
            log.error("Error Reading file", e);
        }
        
        ByteArrayInputStream processorIn = new ByteArrayInputStream(html.toString().getBytes());
        
        tidyProcessor.parse(processorIn, null, html.toString(), requestID, factory);
        return requestID.toString();
    }
}
