package org.w3c.tidy.servlet.sample.filter;
/*
 * Created on 18.10.2004 by vlads
 */
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

/**
 * A request wrapper to support MultipartFilter. Use function getFileItems to access uploaded files or
 * request.getAttribute.
 * 
 * @see <a href="http://java.sun.com/blueprints/corej2eepatterns/Patterns/InterceptingFilter.html"> 
 * Core J2EE Patterns -Intercepting Filter </a>
 * @see <a href="http://jakarta.apache.org/commons/fileupload/">Jakarta Commons FileUpload</a>
 * @see <a href="http://www.servlets.com/cos/index.html">oreilly.servlet package by Jason Hunter</a>
 * 
 * @todo Add More than one File Item of the same name handling.
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com </a>
 * @version $Revision$ ($Author$)
 */

public class MultipartRequestWrapper extends HttpServletRequestWrapper
{
    /**
     * Request attribute. 
     */
    public static final String ATTRIBUTE_FILE_UPLOAD_MAP =  "org.w3c.tidy.servlet.uploaded.files.map";
    
    /**
     * Request attribute prefix to access FileItem by name. 
     */
    public static final String ATTRIBUTE_FILE_PREFIX = "org.w3c.tidy.servlet.uploaded.file.";
    
    /**
     * Logger.
     */
    private Log log = LogFactory.getLog(MultipartRequestWrapper.class);

    /**
     * UploadedFiles.
     */
    private Hashtable files;

    /**
     * name values. 
     */
    private Hashtable parameters;

    public MultipartRequestWrapper(HttpServletRequest request)
    {
        super(request);
        files = new Hashtable();
        parameters = new Hashtable();
        buildParameters(request);
    }

    private void buildParameters(HttpServletRequest request)
    {

        for (Enumeration e = super.getParameterNames(); e.hasMoreElements();)
        {
            String name = (String) e.nextElement();
            String[] origValues = super.getParameterValues(name);
            Vector values = new Vector();
            for (int i = 0; i < origValues.length; i++)
            {
                values.add(origValues[i]);
            }
            if (log.isDebugEnabled())
            {
                log.debug("Parameter:" + name + " = " + values);
            }
            parameters.put(name, values);
        }

        FileUpload fileUpload = new FileUpload(new DefaultFileItemFactory());
        try
        {
            List /* FileItem */items = fileUpload.parseRequest(request);

            // Process the uploaded items
            Iterator iter = items.iterator();
            while (iter.hasNext())
            {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField())
                {
                    // Process a regular form field
                    String name = item.getFieldName();
                    String value = item.getString();
                    if (value == null)
                    {
                        continue;
                    }
                    Vector values = (Vector) this.parameters.get(name);
                    if ((values == null) || (values.size() == 0))
                    {
                        values = new Vector();
                        this.parameters.put(name, values);
                    }
                    values.add(value);
                    log.debug("Form Parameter:" + name + " = " + values);
                }
                else
                {
                    // Process Uploaded Files
                    String name = item.getFieldName();
                    // fileName
                    String value = item.getName();
                    if (value == null)
                    {
                        continue;
                    }
                    Vector values = (Vector) this.parameters.get(name);
                    if ((values == null) || (values.size() == 0))
                    {
                        values = new Vector();
                        this.parameters.put(name, values);
                    }
                    values.add(value);

                    Vector fileValues = (Vector) this.files.get(name);
                    if (fileValues == null)
                    {
                        fileValues = new Vector();
                        this.files.put(name, fileValues);
                    }
                    fileValues.add(item);

                    setAttribute(ATTRIBUTE_FILE_PREFIX + name, item);
                    
                    if (log.isDebugEnabled())
                    {
                        log.debug("File Name Parameter:" + name + " = " + values);
                    }
                }
            }
            if (this.files.size() > 0)
            {
                setAttribute(ATTRIBUTE_FILE_UPLOAD_MAP, this.files); 
            }

        }
        catch (FileUploadException e)
        {
            log.error("FileUpload error", e);
        }
    }

    /**
     * Returns the names of all the parameters as an Enumeration of Strings. It returns an empty Enumeration if there
     * are no parameters.
     * @return the names of all the parameters as an Enumeration of Strings.
     */
    public Enumeration getParameterNames()
    {
        return this.parameters.keys();
    }

    /**
     * Returns the value of the named parameter as a String, or null if the parameter was not sent or was sent without a
     * value. The value is guaranteed to be in its normal, decoded form. If the parameter has multiple values, only the
     * last one is returned (for backward compatibility). For parameters with multiple values, it's possible the last
     * "value" may be null.
     * @param name the parameter name.
     * @return the parameter value.
     */
    public String getParameter(String name)
    {
        try
        {
            Vector values = (Vector) this.parameters.get(name);
            if (values == null || values.size() == 0)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("getParameter:" + name + " = null");
                }
                return null;
            }
            String value = (String) values.elementAt(values.size() - 1);
            if (log.isDebugEnabled())
            {
                log.debug("getParameter:" + name + " = " + values);
            }
            return value;
        }
        catch (Exception e)
        {
            log.error("FileUpload error", e);
            return null;
        }
    }

    /**
     * Returns the values of the named parameter as a String array, or null if the parameter was not sent. The array has
     * one entry for each parameter field sent. If any field was sent without a value that entry is stored in the array
     * as a null. The values are guaranteed to be in their normal, decoded form. A single value is returned as a
     * one-element array.
     * @param name the parameter name.
     * @return the parameter values.
     */
    public String[] getParameterValues(String name)
    {
        try
        {
            Vector values = (Vector) this.parameters.get(name);
            if (values == null || values.size() == 0)
            {
                return null;
            }
            String[] valuesArray = new String[values.size()];
            values.copyInto(valuesArray);
            return valuesArray;
        }
        catch (Exception e)
        {
            log.error("FileUpload error", e);
            return null;
        }
    }

    /**
     * Returns a java.util.Map of the parameters of this request. Request parameters are extra information sent with the
     * request. For HTTP servlets, parameters are contained in the query string or posted form data.
     * @see javax.servlet.ServletRequest#getParameterMap()
     */
    public Map getParameterMap()
    {
        Map map = new HashMap();
        Enumeration enum = getParameterNames();
        while (enum.hasMoreElements())
        {
            String name = (String) enum.nextElement();
            map.put(name, this.getParameterValues(name));
        }
        return map;
    }

    /**
     * Method only in MultipartRequest.
     * @see org.apache.commons.fileupload.FileItem
     * @return Enumeration of org.apache.commons.fileupload.FileItem
     */
    public Enumeration getFileItems()
    {
        return files.elements();
    }

    public FileItem getFileParameter(String name)
    {
        try
        {
            Vector values = (Vector) files.get(name);
            if (values == null || values.size() == 0)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("getFileParameter:" + name + " = null");
                }
                return null;
            }
            FileItem value = (FileItem) values.elementAt(values.size() - 1);
            if (log.isDebugEnabled())
            {
                log.debug("getFileParameter:" + name + " = " + values);
            }
            return value;
        }
        catch (Exception e)
        {
            log.error("FileUpload error", e);
            return null;
        }
    }

    public static FileItem getFileItem(HttpServletRequest request, String name)
    {
        if (!(request instanceof MultipartRequestWrapper))
        {
            return null;
        }
        MultipartRequestWrapper mrequest = (MultipartRequestWrapper) request;
        FileItem file = mrequest.getFileParameter(name);
        return file;
    }
}