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
public class ImageFormatTest extends TidyServletCase {

    /**
     * Instantiates a new test case.
     * @param name test name
     */
    public ImageFormatTest(String name)
    {
        super(name);
    }

    /**
     * Check that alt in image set properly.
     * @throws Exception any axception thrown during test.
     */
    public void testAltText() throws Exception
    {
        WebResponse response =  getResponse();

        WebImage[] img = response.getImages();

        assertEquals("Expected 2 images in result.", 2, img.length);
        
        assertEquals("alt-text set in config", "Alternate", img[1].getAltText()); 

    }
}
