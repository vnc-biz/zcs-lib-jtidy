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
package org.w3c.tidy.servlet.sample.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.ComponentDefinition;
import org.apache.struts.tiles.DefinitionsFactoryException;
import org.apache.struts.tiles.DefinitionsUtil;
import org.apache.struts.tiles.FactoryNotFoundException;
import org.apache.struts.tiles.NoSuchDefinitionException;

/**
 * Implementation of <strong>Action</strong> that populates an instance of
 * <code>SubscriptionForm</code> from the currently specified subscription.
 * 
 * Taken from: /home/cvs/jakarta-struts/src/tiles-documentation/org/apache/struts/webapp/tiles/test/TestActionTileAction.java
 *  
 * @version $Revision$ ($Author$)
 */

/**
 *
 */
public class TestActionTileAction extends Action
{

    // --------------------------------------------------------- Public Methods

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response (or forward to another web
     * component that will create it). Return an <code>ActionForward</code> instance describing where and how control
     * should be forwarded, or <code>null</code> if the response has already been completed.
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @exception Exception if the application business logic throws an exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {

        // Try to retrieve tile context
        ComponentContext context = ComponentContext.getContext(request);
        if (context == null)
        {
            request.setAttribute("actionError", "Can't get component context.");
            return (mapping.findForward("failure"));
        }
        // Get requested test from tile parameter
        String param;

        // Set a definition in this action
        param = (String) context.getAttribute("set-definition-name");
        if (param != null)
        {
            try
            {
                // Read definition from factory, but we can create it here.
                ComponentDefinition definition = DefinitionsUtil.getDefinition(param, request, getServlet()
                    .getServletContext());
                //definition.putAttribute( "attributeName", "aValue" );
                DefinitionsUtil.setActionDefinition(request, definition);
            }
            catch (FactoryNotFoundException ex)
            {
                request.setAttribute("actionError", "Can't get definition factory.");
                return (mapping.findForward("failure"));
            }
            catch (NoSuchDefinitionException ex)
            {
                request.setAttribute("actionError", "Can't get definition '" + param + "'.");
                return (mapping.findForward("failure"));
            }
            catch (DefinitionsFactoryException ex)
            {
                request.setAttribute("actionError", "General error '" + ex.getMessage() + "'.");
                return (mapping.findForward("failure"));
            }
        }

        // Overload a parameter
        param = (String) context.getAttribute("set-attribute");
        if (param != null)
        {
            context.putAttribute(param, context.getAttribute("set-attribute-value"));
        } // end if

        return (mapping.findForward("success"));

    }
}
