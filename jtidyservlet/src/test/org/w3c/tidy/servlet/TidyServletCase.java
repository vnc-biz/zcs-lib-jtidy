/*
 * Created on 26.09.2004
 *
 */
package org.w3c.tidy.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletTestCase;

import com.meterware.servletunit.ServletRunner;

/**
 *
 */
public abstract class TidyServletCase extends ServletTestCase
{
    /**
     * Context mapped to the test application.
     */
    public static final String CONTEXT = "/context";
    
	/**
     * HttpUnit ServletRunner.
     */
    protected ServletRunner runner;

    /**
     * logger.
     */
    protected final Log log = LogFactory.getLog(this.getClass());
    
    /**
     * Instantiates a new test case.
     * @param name test name
     */
    public TidyServletCase(String name)
    {
        super(name);
    }
    
    /**
     * Returns the tested jsp name.
     * @return jsp name
     */
    public String getJspName() 
    {
    	return "servlet/" + this.getClass().getName() + ".jsp";
    }

    /**
     * Get the Response to analize using default JSP name.
     * @throws Exception any axception thrown during test.
     */
    
    public WebResponse getResponse() throws Exception {
        return getJSPResponse(getJspName()); 
    }

    /**
     * Get the Response to analize using Given JSP name.
     * @throws Exception any axception thrown during test.
     */
    
    public WebResponse getJSPResponse(String jspName) throws Exception {
        cleanupTempFile(jspName);
        return getResponse(CONTEXT + "/" + jspName); 
    }
    
    /**
     * Get the Response to analize.
     * @throws Exception any axception thrown during test.
     */
    public WebResponse getResponse(String url) throws Exception {
        String urlString = "http://localhost" + url;
        if (log.isDebugEnabled())
        {
            log.debug("REQUEST: " + urlString);
        }
        WebRequest request = new GetMethodWebRequest(urlString);

    	WebResponse response;
        try
        {
            response = runner.getResponse(request);
        }
        catch (MalformedURLException e)
        {
            log.debug("MalformedURL ", e);
            throw e;
        }
        catch (IOException e)
        {
            log.debug("IO Error ", e);
            throw e;
        }
        catch (SAXException e)
        {
            log.debug("Parsing Error ", e);
            throw e;
        }
        catch (Exception e)
        {
            log.debug("Request Error ", e);
            throw e;
        }
        
        if (log.isDebugEnabled())
        {
    	    if (response.getContentType().startsWith("image"))
    	    {
    	        log.debug("RESPONSE: is Image");
    	    } else
    	    {
    	        log.debug("RESPONSE: [" + response.getText() + "]");
    	    }
        }
    	return response;
    }
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        // need to pass a web.xml file to setup servletunit working directory
    	final String web_xml = "WEB-INF/web.xml";
        ClassLoader classLoader = getClass().getClassLoader();
        URL webXmlUrl = classLoader.getResource(web_xml);
        if (webXmlUrl == null) {
        	throw new Exception(web_xml + " not in class path");
        }
        File webXml = new File(webXmlUrl.getFile());
        log.debug("web.xml:" + webXml.getAbsolutePath());

        cleanupTempFile(getJspName());
        
        // start servletRunner
        runner = new ServletRunner(webXml, CONTEXT);
        
        Hashtable initParameters = new Hashtable(); 
        setServletInitParameters(initParameters);
        String servletURI = "/JTidy";
        runner.registerServlet(servletURI, "org.w3c.tidy.servlet.TidyServlet", initParameters);
        // initialize it - load-on-startup
        getResponse(CONTEXT + servletURI + "?initialize");
        super.setUp();
    }    

    /**
     * Define servlet init Parameters in child class
     * @param initParameters
     */
    public void setServletInitParameters(Hashtable initParameters) 
    {
        // initParameters.put("properties.filename", "JTidyServletTest.properties");
    }
    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        // shutdown servlet engine
        runner.shutDown();
        super.tearDown();
    }
    
    /**
     * Clean up temporary files from a previous test.
     * @param jspName jsp name, with full path
     */
    private void cleanupTempFile(String jspName)
    {
        URL resourceUrl = getClass().getResource("/" + jspName);
        if (resourceUrl != null && SystemUtils.JAVA_IO_TMPDIR != null)
        {
            File jspFile = new File(resourceUrl.getFile());
            long jspModified = jspFile.lastModified();

            String path = SystemUtils.JAVA_IO_TMPDIR + jspName;

            File tempFile = new File(StringUtils.replace(path, ".jsp", "$jsp.java"));

            // delete file only if jsp has been modified
            if (tempFile.exists() && tempFile.lastModified() < jspModified)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Deleting temporary file " + tempFile.getPath());
                }
                tempFile.delete();
            }
            tempFile = new File(StringUtils.replace(path, ".jsp", "$jsp.class"));
            if (tempFile.exists() && tempFile.lastModified() < jspModified)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Deleting temporary file " + tempFile.getPath());
                }
                tempFile.delete();
            }
        }
    }
}
