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
 * Created on 02.11.2004 by vlads
 */
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com</a>
 * @version $Revision$ ($Author$)
 */

/**
 *
 */
public class SiteSecurityHelper
{
    /**
     * Logger.
     */
    private static Log log = LogFactory.getLog(SiteSecurityHelper.class);
    
    private static final String RESOURCE_SECURE_FLAG = "org.w3c.tidy.servlet.sample.secure";
    
    public static boolean isSiteSecure() 
    {
        if ((SiteSecurityHelper.class.getClassLoader().getResourceAsStream(RESOURCE_SECURE_FLAG)) == null)
        {
            log.debug("Security disabled flag no found in classpath:" + RESOURCE_SECURE_FLAG);
            return false;
        }
        return true;
        
    }
    
    public static void verifyUrl(String paramUrl, HttpServletRequest request) throws MalformedURLException, IOException

    {
        // Write to loger to prevent system abuse
        log.info("URL [" + paramUrl + "] for " + request.getRemoteAddr());

        if (log.isDebugEnabled())
        {
            log.debug("Verify URL:" + paramUrl);
            log.debug("http.proxyHost:" + System.getProperty("http.proxyHost"));
            log.debug("http.proxyPort:" + System.getProperty("http.proxyPort"));
        }
        URL url = new URL(paramUrl);
        
        log.debug("host:" + url.getHost());
        
        if ((url.getHost() == null) || (url.getHost().length() == 0))
        {
            throw new MalformedURLException("Host must not be null; URL [" + paramUrl + "]");
        }
        
        if (!isSiteSecure())
        {
            return;
        }
        

        String protocol = url.getProtocol();
        if (!(protocol.equalsIgnoreCase("http") 
               || protocol.equalsIgnoreCase("https"))) {
            throw new IOException("Permission denied for Protocol [" + protocol + "]; URL [" + paramUrl + "]");
        }
        
        InetAddress remoteIP = InetAddress.getByName(url.getHost());
        InetAddress localIP = InetAddress.getLocalHost();
        
        if (log.isDebugEnabled())
        {
            log.debug("Local IP:" + localIP.getHostAddress());
            log.debug("Remote IP is Loopback" + remoteIP.isLoopbackAddress());
            log.debug("Remote IP is SiteLocal" + remoteIP.isSiteLocalAddress());
            log.debug("Remote IP is AnyLocal" + remoteIP.isAnyLocalAddress());
        }
        
        if (remoteIP.equals(localIP)
            || remoteIP.isLoopbackAddress()
            || remoteIP.isSiteLocalAddress()
            || remoteIP.isAnyLocalAddress())
        {
            throw new IOException("Permission denied to access  " + remoteIP.getHostAddress() + "; URL [" + paramUrl + "]");
        }
        // I'm too paranoid?
        InetAddress[] allLocalIP  = InetAddress.getAllByName("localhost");
        for (int i = 0; i < allLocalIP.length; i++)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Local IP:" + allLocalIP[i].getHostAddress());
            }
            if (remoteIP.equals(allLocalIP[i]))
            {
                throw new IOException("Permission denied to access  " + remoteIP.getHostAddress() + "; URL [" + paramUrl  + "]");
            }
        }
        
    }
}
