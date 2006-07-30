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
package org.w3c.tidy;

/**
 * Testcase for Tidy resolved bugs (encodings).
 * <p>
 * see <code>http://sourceforge.net/support/tracker.php?aid=(item number)</code>
 * </p>
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class TidyEncodingBugsTest extends TidyTestCase
{

    /**
     * Instantiate a new Test case.
     * @param name test name
     */
    public TidyEncodingBugsTest(String name)
    {
        super(name);
    }

    /**
     * test for Tidy [647255] : UTF16.
     * @throws Exception any exception generated during the test
     */
    public void test647255() throws Exception
    {
        executeTidyTest("647255.html");
    }

    /**
     * test for Tidy [649812] : Does TidyLib correctly handle Mac files?. (test is UTF16)
     * @throws Exception any exception generated during the test
     */
    public void test649812() throws Exception
    {
        // doesn't work for missing encoding support in test case!
        executeTidyTest("649812.html");
    }

    /**
     * test for Tidy [658230] : Big5.
     * @throws Exception any exception generated during the test
     */
    public void test658230() throws Exception
    {
        throw new Exception("disabled");
        // executeTidyTest("658230.html");
    }

    /**
     * test for Tidy [660397] : Add support for IBM-858 and ISO-8859-15.
     * @throws Exception any exception generated during the test
     */
    public void test660397() throws Exception
    {
        executeTidyTest("660397.html");
    }

    /**
     * test for Tidy [676156] : tidy --input-encoding is broken.
     * @throws Exception any exception generated during the test
     */
    public void test676156() throws Exception
    {
        executeTidyTest("676156.html");
        assertWarnings(4);
    }

    /**
     * test for Tidy [688746] : incorrect charset value for utf-8.
     * @throws Exception any exception generated during the test
     */
    public void test688746() throws Exception
    {
        executeTidyTest("688746.html");
    }

}