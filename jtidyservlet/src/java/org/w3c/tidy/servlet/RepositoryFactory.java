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
 * Created on 08.10.2004 by vlads
 */
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Public interface for user to redefine validation results handling.
 * Default implementation is provided by jtidyservlet.
 * You define class to be used by servlet using JTidyServlet.properties file.
 * 
 * @see org.w3c.tidy.servlet.data.DefaultRepositoryFactory
 * @see org.w3c.tidy.servlet.data.SessionRepositoryFactory
 *
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com</a>
 * @version $Revision$ ($Author$)
 */

public interface RepositoryFactory
{
    /**
     * Get the Repository where to store validation results.
     * @param httpSession Session to bound to.
     * @return Returns the repository instance for given Session, or null if nothing should be stored.
     */
    ResponseRecordRepository getRepositoryInstance(HttpSession httpSession);

    /**
     * Get the ResponseID for given request. This bunction is called
     * @param httpSession HttpSession that could store ID as attribute 
     * @param request  HttpServletRequest that could store ID as attribute, could be null
     * @param response HttpServletResponse that could store ID as attribute, could be null
     * @param newResponse Create new ResponseID anyway
     * @return Returns the Request/Response ID or null if it should be ignored by JTidy. Object should have proper
     * toString() function.
     */
    Object getResponseID(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response, boolean newResponse);

    /**
     * Create new validation results Response Record.  
     * @param request  HttpServletRequest 
     * @param response HttpServletResponse
     * @return Returns new ResponseRecord or null if record for request should be ignored.
     */
    ResponseRecord createRecord(Object key, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response);
}
