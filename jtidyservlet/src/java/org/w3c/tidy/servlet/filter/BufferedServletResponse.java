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
import java.io.PrintWriter;
import java.io.OutputStreamWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.w3c.tidy.servlet.TidyProcessor;

/**
 * Buffers the response
 *
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@hotmail.com">skarzhevskyy@hotmail.com </a>
 * @version $Revision$ ($Author$)
 */
public class BufferedServletResponse extends HttpServletResponseWrapper
{
    /**
     * The wrapped response.
     */
    private HttpServletResponse response;

    /**
     * The ServletOutputStream that has been returned by
     * <code>getOutputStream()</code>, if any.
     */

    protected ServletOutputStream stream = null;

    /**
     * The PrintWriter that has been returned by <code>getWriter()</code>, if
     * any.
     */

    protected PrintWriter writer = null;

    /**
     *  Do not buffer the output, Preform validation only
     */
    private boolean tee = false;

    protected TidyProcessor processor;

    /**
     * Calls the parent constructor which creates a ServletResponse adaptor
     * wrapping the given response object.
     */
    public BufferedServletResponse(HttpServletResponse httpServletResponse, TidyProcessor tidyProcessor)
    {
        super(httpServletResponse);
        this.response = httpServletResponse;
        this.processor = tidyProcessor;
    }

    /**
     * Create and return a ServletOutputStream to write the content associated with this Response.
     *
     * @exception IOException
     *                if an input/output error occurs
     */
    public ServletOutputStream createOutputStream() throws IOException
    {

        BufferedServletOutputStream stream;
        if (tee)
        {
            stream = new BufferedServletOutputStream(this.response, this.processor,
                    response.getOutputStream());
        } else
        {
            stream = new BufferedServletOutputStream(this.response, this.processor);
        }

        return stream;
    }

    /**
     * Return the servlet output stream associated with this Response.
     *
     * @exception IllegalStateException
     *                if <code>getWriter</code> has already been called for this response
     * @exception IOException
     *                if an input/output error occurs
     */
    public ServletOutputStream getOutputStream() throws IOException
    {

        if (this.writer != null)
        {
            throw new IllegalStateException("getWriter() has already been called for this response");
        }

        if (this.stream == null)
        {
            this.stream = createOutputStream();
        }

        return (this.stream);

    }

    /**
     * Return the writer associated with this Response.
     *
     * @exception IllegalStateException
     *                if <code>getOutputStream</code> has already been called for this response
     * @exception IOException
     *                if an input/output error occurs
     */
    public PrintWriter getWriter() throws IOException
    {

        if (this.writer != null)
        {
            return (this.writer);
        }

        if (this.stream != null)
        {
            throw new IllegalStateException(
                    "getOutputStream() has already been called for this response");
        }

        this.stream = createOutputStream();

        //String charset = getCharsetFromContentType(contentType);
        String charEnc = this.response.getCharacterEncoding();

        // HttpServletResponse.getCharacterEncoding() shouldn't return null
        // according the spec, so feel free to remove that "if"
        if (charEnc != null)
        {
            this.writer = new PrintWriter(new OutputStreamWriter(this.stream, charEnc));
        } else
        {
            this.writer = new PrintWriter(this.stream);
        }

        return (this.writer);
    }

    /**
     * Finish a response.
     */
    public void finishResponse()
    {
        try
        {
            if (this.writer != null)
            {
                this.writer.close();
            } else
            {
                if (this.stream != null)
                    this.stream.close();
            }
        } catch (IOException e)
        {
        }
    }

    /**
     * @param tee The tee to set.
     */
    public void setTee(boolean tee)
    {
        this.tee = tee;
    }
}