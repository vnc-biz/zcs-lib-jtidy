/*
 * Created on 26.09.2004
 *
 */
package org.w3c.tidy.servlet;

import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebImage;
import com.meterware.httpunit.WebTable;

import org.w3c.tidy.servlet.Consts;


/**
 *
 *
 */
public class ValidationOKTest extends TidyServletCase
{

    /**
     * Instantiates a new test case.
     * @param name test name
     */
    public ValidationOKTest(String name)
    {
        super(name);
    }

    /**
     * Check that HTML is correct and validation is fine. Also verify theat Request ID is growning after each request.
     * @param jspName jsp name, with full path
     * @throws Exception any axception thrown during test.
     */
    public void testValidateOnly() throws Exception
    {
        WebResponse response = getResponse();

        WebImage[] img = response.getImages();
        assertEquals("Expected 1 image in result.", 1, img.length);
        assertEquals("Expected my name Page Validation", "Page Validation", img[0].getAltText());

        WebTable[] tables = response.getTables();
        assertEquals("Expected 1 table in result.", 1, tables.length);
        String RequestID1 = response.getNewCookieValue(Consts.ATTRIBUTE_REQUEST_ID);
        assertEquals("Expected RequestID in Cookie.", RequestID1, tables[0].getSummary());

        response = getResponse();
        tables = response.getTables();
        assertEquals("Expected 1 table in result.", 1, tables.length);
        String RequestID2 = response.getNewCookieValue(Consts.ATTRIBUTE_REQUEST_ID);
        assertEquals("Expected Next RequestID in Cookie", RequestID2, tables[0].getSummary());
        assertFalse("Expected Different RequestID in Cookie", RequestID2.equals(RequestID1));

        String src = img[0].getSource();

        WebResponse responseImg = getResponse(src + "&srcOnly=true");

        assertEquals("Image", Consts.DEFAULT_IMAGE_NAME_PREFIX + "ok.png", responseImg.getText());
    }
}
