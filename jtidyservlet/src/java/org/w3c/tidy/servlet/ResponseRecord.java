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
import java.util.List;

import org.w3c.tidy.TidyMessageListener;

/**
 * Data to store Validation results and error.
 *
 * @see org.w3c.tidy.servlet.data.DefaultResponseRecord
 * 
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com</a>
 * @version $Revision$ ($Author$)
 */
public interface ResponseRecord extends TidyMessageListener
{
    /**
     * @return Returns the requestID.
     */
    Object getRequestID();
    /**
     * @param requestID The requestID to set.
     */
    void setRequestID(Object requestID);
    /**
     * @return Returns the input html.
     */
    String getHtmlInput();
    /**
     * @param html The input html to set.
     */
    void setHtmlInput(String html);
    /**
     * @return Returns the htmlResult.
     */
    String getHtmlOutput();
    /**
     * @param html The htmlOutput to set.
     */
    void setHtmlOutput(String html);
    /**
     * @return Returns the number of parse errors.
     */
   int getParseErrors();
    /**
     * @return Returns the number of parse warnings.
     */
    int getParseWarnings();
    /**
     * @return Returns the JTidy messages.
     * @see org.w3c.tidy.TidyMessage
     */
    List getMessages();
    /**
     * @return Returns the JTidy parsing time in milliseconds.
     */
    long getParsTime();
    /**
     * @param parsTime The parsTime to set.
     */
    void setParsTime(long parsTime);
    /**
     * @return Returns the when.
     */
    long getWhen();
    /**
     * @return Returns the part of ImageName shown as icon or null to use default implementation
     */
    String getImageName();
}