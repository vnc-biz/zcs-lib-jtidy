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
import java.util.Hashtable;

import org.w3c.tidy.servlet.ResponseRecord;
import org.w3c.tidy.servlet.ResponseRecordRepository;

/**
 * Static Class to store Validation results and Error
 *
 * @todo implement API interface ResultsRepository
 * @todo aotomaticaly remove old data
 *
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@hotmail.com">skarzhevskyy@hotmail.com </a>
 * @version $Revision$ ($Author$)
 *
 */
public class DefaultResponseRecordRepository implements ResponseRecordRepository
{

    private Hashtable data;

    private Object lastPK;

    public DefaultResponseRecordRepository()
    {
        data = new Hashtable();
    }

    public void addRecord(ResponseRecord result)
    {
        Object key = result.getRequestID();
        data.put(key, result);
        lastPK = key;
    }

    public Object getLastPK()
    {
        return lastPK;
    }

    /**
     * @return Returns the Request ID converted from String or null if it should be ignored by JTidy.
     */
    public Object getResponseID(String keyString)
    {

        if (keyString == null)
        {
            return new Long(0);
        }

        if (keyString.equalsIgnoreCase("last"))
        {
            return getLastPK();
        }
        else
        {
            return new Long(keyString);
        }
    }

    /**
     * Immediately get the results.
     * @return Returns processing results for given resultID, or null if the repository contains no mapping for this key.
     */
    public ResponseRecord getRecord(Object key)
    {
        if (key == null)
        {
            return null;
        }
        return (ResponseRecord) data.get(key);
    }

    /**
     * Get results sleep if not yet avalable.
     * Used to get validation image Request from browser will come before JTidy processed the HTML
     * @return Returns processing results for given resultID, or null if the repository contains no mapping for this key.
     */
    public ResponseRecord getRecord(Object key, int sleep)
    {
        if (key == null)
        {
            return null;
        }
        ResponseRecord item = null;
        //System.out.println("doGet:" + resultID + " from : " + data.size());
        long stop = System.currentTimeMillis() + sleep;
        while ((item == null) && (stop > System.currentTimeMillis()))
        {
            item = getRecord(key);
            if ((item != null) || (sleep == 0))
            {
                //System.out.println("Found");
                break;
            }
            try
            {
                //System.out.println("sleep");
                Thread.sleep(100);
            }
            catch (InterruptedException ignore)
            {
                //break;
            }
        }
        return item;
    }
}