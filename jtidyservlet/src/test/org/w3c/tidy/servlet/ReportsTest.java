/*
 * Created on 26.09.2004
 *
 */
package org.w3c.tidy.servlet;

import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebImage;
import com.meterware.httpunit.WebLink;

import org.w3c.tidy.servlet.Consts;

/**
 *
 *
 */
public class ReportsTest extends TidyServletCase {

    /**
     * Instantiates a new test case.
     * @param name test name
     */
    public ReportsTest(String name)
    {
        super(name);
    }

    /**
     * Check that HTML is correct and validation is fine.
     * Also verify theat Reports are generated.
     * @param jspName jsp name, with full path
     * @throws Exception any axception thrown during test.
     */
    public void testServlet() throws Exception
    {
        WebResponse response =  getJSPResponse("servlet/formatedByTagOK.jsp");

        WebImage[] img = response.getImages();
        assertEquals("Expected 1 image in result.", 1, img.length);

        String RequestID1 = response.getNewCookieValue(Consts.ATTRIBUTE_REQUEST_ID);
        
        WebLink servletLink = response.getLinkWithName("JTidyValidationImageLink");
        
        WebResponse reportResponse =  getResponse(servletLink.getURLString());
        assertNotNull("Messages exists exists", reportResponse.getElementsWithName("JTidyMessagesTable"));
        assertNotNull("Source code exists", reportResponse.getElementsWithName("JTidyOriginalSource"));
    }
}
