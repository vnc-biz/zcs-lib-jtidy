/*
 * Created on 26.09.2004
 *
 */
package org.w3c.tidy.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.tidy.servlet.util.HTMLEncode;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletTestCase;

import com.meterware.servletunit.ServletRunner;


/**
 * Base TestCase class for tests.
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com </a>
 * @version $Revision$ ($Author$)
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
     * @see junit.framework.TestCase#getName()
     */
    public String getName()
    {
        return getClass().getName() + "." + super.getName();
    }

    /**
     * Get the Response to analize using default JSP name.
     * @throws Exception any axception thrown during test.
     */

    public WebResponse getResponse() throws Exception
    {
        return getJSPResponse(getJspName());
    }

    public WebResponse getResponseQuery(String[] args) throws Exception
    {
        return getJSPResponse(HTMLEncode.encodeQuery(getJspName(), args));
    }

    /**
     * Get the Response to analize using Given JSP name.
     * @throws Exception any axception thrown during test.
     */

    public WebResponse getJSPResponse(String jspName) throws Exception
    {
        cleanupTempFile(jspName);
        return getResponse(CONTEXT + "/" + jspName);
    }

    /**
     * Get the Response to analize.
     * @throws Exception any axception thrown during test.
     */
    public WebResponse getResponse(String url) throws Exception
    {
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
                log.debug("RESPONSE: is Image Len:" + response.getContentLength());
            }
            else
            {
                log.debug("RESPONSE: [" + response.getText() + "]");
                /*
                 * String elemetns[] = response.getElementNames(); for(int i =0; i < elemetns.length; i ++) {
                 * log.debug("RESPONSE element [" + i + "]=" + elemetns[i]); }
                 */
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
        if (webXmlUrl == null)
        {
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
        runner.registerServlet(servletURI, TidyServlet.class.getName(), initParameters);
        // initialize it - load-on-startup
        getResponse(CONTEXT + servletURI + "?initialize");

        // register the filter servlet
        Hashtable filterInitParameters = new Hashtable();
        setFilterInitParameters(filterInitParameters);
        runner.registerServlet(
            "*" + MockFilterSupport.FILTERED_EXTENSION,
            MockFilterSupport.class.getName(),
            filterInitParameters);

        log.debug("ServletRunner setup OK");

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
     * Define filter init Parameters in child class
     * @param initParameters
     */
    public void setFilterInitParameters(Hashtable initParameters)
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

    public void validateReport(WebResponse reportResponse) throws Exception
    {
        // assertEquals("Messages exists exists", 1, reportResponse.getElementsWithName("JTidyMessagesTable").length);
        // assertEquals("Source code exists", 1, reportResponse.getElementsWithName("JTidyOriginalSource").length);
        assertTrue("Messages exists exists", reportResponse.getText().indexOf("JTidyMessagesTable") > 0);
        assertTrue("Source code exists", reportResponse.getText().indexOf("JTidyOriginalSource") > 0);
    }

    public void validateBinaryContext(WebResponse response) throws Exception
    {
        assertFalse("Type is HTML", "text/html".equals(response.getContentType()));
        InputStream in = response.getInputStream();
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        // write in to data;
        final int BUFFER_SIZE = 2048;
        byte[] buffer = new byte[BUFFER_SIZE];
        int r = 0;
        while ((r = in.read(buffer)) != -1)
        {
            data.write(buffer, 0, r);
        }
        assertEquals("Content Length", data.size(), response.getContentLength());

    }

    public WebResponse getReportResponse(WebResponse response) throws Exception
    {
        WebLink servletLink = response.getLinkWithName("JTidyValidationImageLink");

        return getResponse(servletLink.getURLString());
    }

    /**
     * Clean up temporary files from a previous test.
     * @param jspName jsp name, with full path
     */
    private void cleanupTempFile(String uri)
    {
        String jspName = StringUtils.replace(uri, MockFilterSupport.FILTERED_EXTENSION, "");

        URL resourceUrl = getClass().getResource("/" + jspName);
        if (resourceUrl != null && SystemUtils.JAVA_IO_TMPDIR != null)
        {
            File jspFile = new File(resourceUrl.getFile());
            long jspModified = jspFile.lastModified();

            String path = SystemUtils.JAVA_IO_TMPDIR + StringUtils.replace(jspName, ".", "$");

            File tempFile = new File(path + ".java");
            // log.debug("JSP file " + jspFile.getPath() + " " + (new java.util.Date(jspFile.lastModified())));
            // log.debug("Java file " + tempFile.getPath() + " "+ (new java.util.Date(tempFile.lastModified())));

            // delete file only if jsp has been modified
            if (tempFile.exists())
            {
                if (tempFile.lastModified() < jspModified)
                {
                    if (log.isDebugEnabled())
                    {
                        log.debug("Deleting temporary file " + tempFile.getPath());
                    }
                    tempFile.delete();
                }
            }

            tempFile = new File(path + ".class");
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
