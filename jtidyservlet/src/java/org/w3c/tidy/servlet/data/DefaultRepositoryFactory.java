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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.w3c.tidy.servlet.Consts;
import org.w3c.tidy.servlet.RepositoryFactory;
import org.w3c.tidy.servlet.ResponseRecord;
import org.w3c.tidy.servlet.ResponseRecordRepository;


/**
 * Default Factory implementation, Singleton.
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com </a>
 * @version $Revision$ ($Author$)
 */
public class DefaultRepositoryFactory implements RepositoryFactory
{

    /**
     * singleton.
     */
    private static ResponseRecordRepository repositoryInstance;

    /**
     * Global Request key sequence.
     */
    private static long staticRequestID;

    private static synchronized ResponseRecordRepository getStaticRepositoryInstance()
    {
        if (repositoryInstance == null)
        {
            repositoryInstance = new DefaultResponseRecordRepository();
        }
        return repositoryInstance;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseRecordRepository getRepositoryInstance(HttpSession httpSession)
    {
        return getStaticRepositoryInstance();
    }

    /**
     * Globabl sequence generator
     * @return Returns the requst new ID when asked.
     */
    private static synchronized long getNewRequestID()
    {
        staticRequestID++;
        return staticRequestID;
    }

    /**
     * Implementation of sequence generator. Could be overdriven in descendant calss
     * @return Returns the requst new ID when asked.
     */
    public long generateNewRequestID(HttpSession httpSession)
    {
        return getNewRequestID();
    }

    /**
     * Disable some pages validation or, save numbers for not important pages. Could be overdriven in descendant calss
     * @return Returns boolean
     */
    public boolean allowURI(String uri)
    {
        if (uri.endsWith("empty.html"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object getResponseID(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response,
        boolean newResponse)
    {
        // Save the numbers
        if ((request != null) && (!allowURI(request.getRequestURI())))
        {
            return null;
        }

        Long atribRequestID = null;
        if ((!newResponse) && (request != null))
        {
            atribRequestID = (Long) request.getAttribute(Consts.ATTRIBUTE_REQUEST_ID);
        }

        if (atribRequestID == null)
        {
            atribRequestID = new Long(generateNewRequestID(httpSession));
            if (request != null)
            {
                request.setAttribute(Consts.ATTRIBUTE_REQUEST_ID, atribRequestID);

            }
        }
        return atribRequestID;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseRecord createRecord(Object key, HttpSession httpSession, HttpServletRequest request,
        HttpServletResponse response)
    {
        return new DefaultResponseRecord();
    }

}
