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

package org.w3c.tidy.servlet.jsp.tagext;
/*
 * Created on 17.09.2004
 *
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.JspException;

import org.w3c.tidy.servlet.TidyProcessor;

/**
 * HTML pretty printer tag.
 * See tagExample.jsp for usage example.
 *
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@hotmail.com">skarzhevskyy@hotmail.com</a>
 * @version $Revision$ ($Author$)
 */
public class TidyTag extends BodyTagSupport
{

    /**
     * JTidy Parser configutation string
     * Examples of config string: indent: auto; indent-spaces: 2
     */
    private String config = null;

    /**
     * validateOnly only do not change output.
     */
    private boolean validateOnly = false;

    /**
     * Do noting just buffer this page
     */
    public int doStartTag() throws JspException
    {
        return EVAL_BODY_BUFFERED;
    }


    /**
     * Perform the page formating using JTidy
     */
    public int doEndTag() throws JspException
    {

        TidyProcessor tidyProcessor = new TidyProcessor((HttpServletRequest)pageContext.getRequest(),
                (HttpServletResponse) pageContext.getResponse());
        tidyProcessor.setValidateOnly(validateOnly);
        tidyProcessor.setDoubleValidation(true);
        tidyProcessor.setConfig(config);

        String html = getBodyContent().getString();
        ByteArrayInputStream in = new ByteArrayInputStream(html.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        boolean useOut = tidyProcessor.parse(in, out, html);

        try
        {
            if ((useOut) && (!validateOnly))
            {
                pageContext.getOut().clear();
                pageContext.getOut().write(out.toString());
            } else
            {
                // Ignore HTML created by tidy, there are errors
                pageContext.getOut().write(html);
            }

        } catch (Exception e)
        {
            System.out.println("TidyTag write error" + e.getMessage());
            //e.printStackTrace();
            throw new JspException(e);
        }

        return SKIP_PAGE;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release()
    {
        super.release();
        config = null;
        validateOnly = false;
    }

    /**
     * @return Returns the configuration.
     */
    public String getConfig()
    {
        return config;
    }

    /**
     * @param configuration
     *            The configuration to set.
     */
    public void setConfig(String configuration)
    {
        this.config = configuration;
    }

    /**
     * @return Returns the validateOnly.
     */
    public boolean isValidateOnly()
    {
        return validateOnly;
    }

    /**
     * @param validateOnly
     *            The validateOnly to set.
     */
    public void setValidateOnly(boolean validateOnly)
    {
        this.validateOnly = validateOnly;
    }
}
