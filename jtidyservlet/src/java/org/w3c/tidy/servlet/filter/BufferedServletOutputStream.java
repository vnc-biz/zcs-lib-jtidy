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
package org.w3c.tidy.servlet.filter;
/*
 * Created on 02.10.2004
 *
 */
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.w3c.tidy.servlet.TidyProcessor;

/**
 * Substitute ServletOutputStream.
 *
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@hotmail.com">skarzhevskyy@hotmail.com </a>
 * @version $Revision$ ($Author$)
 */
public class BufferedServletOutputStream extends ServletOutputStream
{
    /**
     * stream buffer.
     */
    protected ByteArrayOutputStream buffer = null;

    /**
     * The response with which this servlet output stream is associated.
     */
    protected HttpServletResponse response = null;

    /**
     * Original OutputStream. If in tee configuration
     */
    private ServletOutputStream origOutputStream = null;

    /**
     * Has this stream been closed?
     */
    protected boolean closed = false;

    protected TidyProcessor processor;

    BufferedServletOutputStream(HttpServletResponse httpServletResponse, TidyProcessor tidyProcessor)
    {
        this.buffer = new ByteArrayOutputStream();
        this.response = httpServletResponse;
        this.processor = tidyProcessor;
        this.origOutputStream = null;
    }

    BufferedServletOutputStream(HttpServletResponse httpServletResponse, TidyProcessor tidyProcessor, ServletOutputStream origOutputStream)
    {
        this.buffer = new ByteArrayOutputStream();
        this.response = httpServletResponse;
        this.processor = tidyProcessor;
        this.origOutputStream = origOutputStream;
    }

    /**
     * {@inheritDoc}
     */
    public void write(int b) throws IOException
    {
        this.buffer.write(b);
        if (this.origOutputStream != null)
        {
            this.origOutputStream.write(b);
        }
    }

    /**
     * Close this output stream, causing any buffered data to be flushed and
     * any further output data to throw an IOException.
     */
    public void close() throws IOException
    {

        if (closed)
        {
            throw new IOException("This output stream has already been closed");
        }

        ByteArrayInputStream in = new ByteArrayInputStream(buffer.toByteArray());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PrintWriter html_out = this.response.getWriter();

        if (this.processor.parse(in, out, buffer.toString()))
        {
            html_out.write(out.toString());
        } else
        {
            // Ignore HTML created by tidy, there are errors
            html_out.write(buffer.toString());
        }
        html_out.close();

        closed = true;

    }

}
