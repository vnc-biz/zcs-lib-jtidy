/*
 * Created on 26.09.2004
 *
 */
package org.w3c.tidy.servlet;

import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebImage;

/**
 *
 *
 */
public class ValidationImageTagTest extends TidyServletCase {

    /**
     * Instantiates a new test case.
     * @param name test name
     */
    public ValidationImageTagTest(String name)
    {
        super(name);
    }

    /**
     * Check that image source is set properly in Servlet.
     * do not call the redirector
     * @param jspName jsp name, with full path
     * @throws Exception any axception thrown during test.
     */
    public void testAltText() throws Exception
    {
        WebResponse response =  getResponse();

        WebImage[] img = response.getImages();

        assertEquals("Expected 2 images in result.", 2, img.length);

        assertEquals("Expected my name Page Validation", "Page Validation", img[1].getAltText());
        
        String src = img[1].getSource(); 
    	
        WebResponse responseImgSrc = getResponse( src + "&srcOnly=true");
    	
    	assertEquals("Image src", Consts.DEFAULT_IMAGE_NAME_PREFIX + "warning.gif" , responseImgSrc.getText());

    	WebResponse responseImg = getResponse(src);

    	assertEquals("Image type", "image/gif", responseImg.getContentType());
    	
    	int len = responseImg.getContentLength();
    	
    	assertTrue("Large Image size", (len < 2000) && (len > 1000)); 
    }
}
