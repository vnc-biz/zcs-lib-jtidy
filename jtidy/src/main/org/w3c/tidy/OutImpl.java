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

import java.io.IOException;
import java.io.OutputStream;

import org.w3c.tidy.EncodingUtils.PutBytes;


/**
 * Output Stream Implementation.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a> (translation to Java)
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class OutImpl implements Out
{

    /**
     * output encoding.
     */
    private int encoding;

    /**
     * actual state for ISO 2022.
     */
    private int state;

    /**
     * output stream.
     */
    private OutputStream out;

    /**
     * putter callback.
     */
    private PutBytes putBytes;

    /**
     * reference to configuration.
     */
    private Configuration configuration;

    /**
     * Constructor.
     * @param configuration actual configuration instance (needed for newline configuration).
     */
    public OutImpl(Configuration configuration)
    {
        this.configuration = configuration;

        this.putBytes = new PutBytes()
        {

            OutImpl out;

            PutBytes setOut(OutImpl out)
            {
                this.out = out;
                return this;
            }

            public void doPut(byte[] buf, int[] count)
            {
                out.outcUTF8Bytes(buf, count);
            };
        } // set the out instance direclty
            .setOut(this);
    }

    /**
     * Getter for <code>encoding</code>.
     * @return Returns the encoding.
     */
    public int getEncoding()
    {
        return this.encoding;
    }

    /**
     * Getter for <code>out</code>.
     * @return Returns the out.
     */
    public OutputStream getOut()
    {
        return this.out;
    }

    /**
     * Getter for <code>state</code>.
     * @return Returns the state.
     */
    public int getState()
    {
        return this.state;
    }

    /**
     * output UTF-8 bytes to output stream.
     * @param buf array of bytes
     * @param count number of bytes in buf to write
     */
    void outcUTF8Bytes(byte[] buf, int[] count)
    {
        try
        {
            for (int i = 0; i < count[0]; i++)
            {
                out.write(buf[i]);
            }
        }
        catch (IOException e)
        {
            System.err.println("OutImpl.outcUTF8Bytes: " + e.toString());
        }
    }

    /**
     * .
     * @see org.w3c.tidy.Out#outc(byte)
     */
    public void outc(byte c)
    {
        outc(c & 0xFF); // Convert to unsigned.
    }

    /**
     * @see org.w3c.tidy.Out#outc(int)
     */
    public void outc(int c)
    {
        int ch;

        try
        {

            if (this.encoding == Configuration.MACROMAN)
            {
                if (c < 128)
                {
                    out.write(c);
                }
                else
                {
                    int i;

                    for (i = 128; i < 256; i++)
                    {
                        if (EncodingUtils.decodeMacRoman(i - 128) == c)
                        {
                            out.write(i);
                            break;
                        }
                    }
                }
            }
            else

            if (this.encoding == Configuration.WIN1252)
            {
                if (c < 128 || (c > 159 && c < 256))
                {
                    out.write(c);
                }
                else
                {
                    int i;

                    for (i = 128; i < 160; i++)
                    {
                        if (EncodingUtils.decodeWin1252(i - 128) == c)
                        {
                            out.write(i);
                            break;
                        }
                    }
                }
            }
            else if (this.encoding == Configuration.UTF8)
            {
                int[] count = new int[]{0};

                EncodingUtils.encodeCharToUTF8Bytes(c, null, this.putBytes, count);
                if (count[0] <= 0)
                {
                    /* ReportEncodingError(in->lexer, INVALID_UTF8 | REPLACED_CHAR, c); */
                    /* replacement char 0xFFFD encoded as UTF-8 */
                    out.write(0xEF);
                    out.write(0xBF);
                    out.write(0xBF);
                }
            }
            else if (this.encoding == Configuration.ISO2022)
            {
                if (c == 0x1b) /* ESC */
                {
                    this.state = StreamIn.FSM_ESC;
                }
                else
                {
                    switch (this.state)
                    {
                        case StreamIn.FSM_ESC :
                            if (c == '$')
                            {
                                this.state = StreamIn.FSM_ESCD;
                            }
                            else if (c == '(')
                            {
                                this.state = StreamIn.FSM_ESCP;
                            }
                            else
                            {
                                this.state = StreamIn.FSM_ASCII;
                            }
                            break;

                        case StreamIn.FSM_ESCD :
                            if (c == '(')
                            {
                                this.state = StreamIn.FSM_ESCDP;
                            }
                            else
                            {
                                this.state = StreamIn.FSM_NONASCII;
                            }
                            break;

                        case StreamIn.FSM_ESCDP :
                            this.state = StreamIn.FSM_NONASCII;
                            break;

                        case StreamIn.FSM_ESCP :
                            this.state = StreamIn.FSM_ASCII;
                            break;

                        case StreamIn.FSM_NONASCII :
                            c &= 0x7F;
                            break;

                        default :
                            // should not reach here
                            break;
                    }
                }

                this.out.write(c);
            }
            else if (this.encoding == Configuration.UTF16LE
                || this.encoding == Configuration.UTF16BE
                || this.encoding == Configuration.UTF16)
            {
                int i = 1;
                int numChars = 1;
                int[] theChars = new int[2];

                if (c > EncodingUtils.MAX_UTF16_FROM_UCS4)
                {
                    // invalid UTF-16 value
                    /* ReportEncodingError(in.lexer, INVALID_UTF16 | DISCARDED_CHAR, c); */
                    c = 0;
                    numChars = 0;
                }
                else if (c >= EncodingUtils.UTF16_SURROGATES_BEGIN)
                {
                    // encode surrogate pairs

                    // check for invalid pairs
                    if (((c & 0x0000FFFE) == 0x0000FFFE) || ((c & 0x0000FFFF) == 0x0000FFFF))
                    {
                        /* ReportEncodingError(in.lexer, INVALID_UTF16 | DISCARDED_CHAR, c); */
                        c = 0;
                        numChars = 0;
                    }
                    else
                    {
                        theChars[0] = (c - EncodingUtils.UTF16_SURROGATES_BEGIN)
                            / 0x400
                            + EncodingUtils.UTF16_LOW_SURROGATE_BEGIN;
                        theChars[1] = (c - EncodingUtils.UTF16_SURROGATES_BEGIN)
                            % 0x400
                            + EncodingUtils.UTF16_HIGH_SURROGATE_BEGIN;

                        // output both
                        numChars = 2;
                    }
                }
                else
                {
                    // just put the char out
                    theChars[0] = c;
                }

                for (i = 0; i < numChars; i++)
                {
                    c = theChars[i];

                    if (this.encoding == Configuration.UTF16LE)
                    {
                        ch = c & 0xFF;
                        out.write(ch);
                        ch = (c >> 8) & 0xFF;
                        out.write(ch);
                    }

                    else if (this.encoding == Configuration.UTF16BE || this.encoding == Configuration.UTF16)
                    {
                        ch = (c >> 8) & 0xFF;
                        out.write(ch);
                        ch = c & 0xFF;
                        out.write(ch);
                    }
                }
            }
            // #431953 - start RJ
            else if (this.encoding == Configuration.BIG5 || this.encoding == Configuration.SHIFTJIS)
            {
                if (c < 128)
                {
                    this.out.write(c);
                }
                else
                {
                    ch = (c >> 8) & 0xFF;
                    this.out.write(ch);
                    ch = c & 0xFF;
                    this.out.write(ch);
                }
            }
            // #431953 - end RJ
            else
            {
                this.out.write(c);
            }
        }
        catch (IOException e)
        {
            System.err.println("OutImpl.outc: " + e.toString());
        }
    }

    /**
     * @see org.w3c.tidy.Out#newline()
     */
    public void newline()
    {
        try
        {
            this.out.write(configuration.newline);
            this.out.flush();
        }
        catch (IOException e)
        {
            System.err.println("OutImpl.newline: " + e.toString());
        }
    }

    /**
     * Setter for <code>encoding</code>.
     * @param encoding The encoding to set.
     */
    public void setEncoding(int encoding)
    {
        this.encoding = encoding;
    }

    /**
     * Setter for <code>out</code>.
     * @param out The out to set.
     */
    public void setOut(OutputStream out)
    {
        this.out = out;
    }

    /**
     * Setter for <code>state</code>.
     * @param state The state to set.
     */
    public void setState(int state)
    {
        this.state = state;
    }

    /**
     * @see org.w3c.tidy.Out#outBOM()
     */
    public void outBOM()
    {
        if (this.encoding == Configuration.UTF8
            || this.encoding == Configuration.UTF16LE
            || this.encoding == Configuration.UTF16BE
            || this.encoding == Configuration.UTF16)
        {
            outc(EncodingUtils.UNICODE_BOM); // this will take care of encoding the BOM correctly
        }
    }

}