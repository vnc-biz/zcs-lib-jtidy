/*
 * Created on 25.10.2004
 *
 */
package org.w3c.tidy.servlet;

import java.util.Hashtable;

import com.meterware.httpunit.WebImage;
import com.meterware.httpunit.WebResponse;


/**
 * 
 * 
 */
public class FilterTeeTest extends FilterTest
{
    public FilterTeeTest(String name)
    {
        super(name);
    }
    
    /**
     * Define filter init Parameters
     * @param initParameters
     */
    public void setFilterInitParameters(Hashtable initParameters) 
    {
        super.setFilterInitParameters(initParameters);
        initParameters.put("tee", "true");
    }
    
    /**
     * Check that alt in image not set.
     * @throws Exception any axception thrown during test.
     */
    public void testImageAltText() throws Exception
    {
        WebResponse response =  getResponse();

        WebImage[] img = response.getImages();

        assertEquals("Expected 2 images in result.", 2, img.length);
        
        assertEquals("alt-text set in config", "", img[0].getAltText()); 

        WebResponse reportResponse =  getReportResponse(response);
        
        validateReport(reportResponse);
    }
}
