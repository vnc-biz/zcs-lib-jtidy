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
package org.w3c.tidy.servlet.properties;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.tidy.servlet.RepositoryFactory;
import org.w3c.tidy.servlet.ResponseRecordRepository;
import org.w3c.tidy.servlet.data.DefaultRepositoryFactory;


/**
 * @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com </a>
 * @version $Revision$ ($Author$)
 */

public class JTidyServletProperties
{

    /**
     * name of the default properties file <code>JTidyServlet.properties</code>.
     */
    public static final String DEFAULT_FILENAME = "JTidyServlet.properties";

    /**
     * property <code>repositoryFactory.class</code>.
     */
    public static final String PROPERTY_CLASS_REPOSITORYFACTORY = "repositoryFactory.class";

    /**
     * property <code>imageNamePrefix</code>.
     */
    public static final String PROPERTY_STRING_IMAGENAMEPREFIX = "imageNamePrefix";

    /**
     * property <code>imageNameExtension</code>.
     */
    public static final String PROPERTY_STRING_IMAGENAMEEXTENSION = "imageNameExtension";

    /**
     * property <code>imageWidth</code>.
     */
    public static final String PROPERTY_STRING_IMAGE_WIDTH = "imageWidth";

    /**
     * property <code>imageHeight</code>.
     */
    public static final String PROPERTY_STRING_IMAGE_HEIGHT = "imageHeight";

    /**
     * property <code>imageGetTimeout</code>.
     */
    public static final String PROPERTY_INT_IMAGEGETTIMEOUT = "imageGetTimeout";

    /**
     * property <code>JTidyServletURI</code>.
     */
    public static final String JTIDYSERVLET_URI = "JTidyServletURI";

    /**
     * property <code>xhtml</code>.
     */
    public static final String PROPERTY_BOOLEAN_XHTML = "xhtml";

    /**
     * property <code>logValidationMessages</code>.
     */
    public static final String PROPERTY_BOOLEAN_LOG_VALIDATION_MESSAGES = "logValidationMessages";

    /**
     * Logger.
     */
    private static Log log = LogFactory.getLog(JTidyServletProperties.class);

    /**
     * Loaded properties
     */
    private Properties properties;

    /**
     * Singleton.
     */
    private static JTidyServletProperties instance;

    private JTidyServletProperties()
    {
        this.properties = new Properties();
        loadFile(DEFAULT_FILENAME);
    }

    public void loadFile(String fileName)
    {
        if (fileName == null)
        {
            return;
        }
        Properties tmpProperties = new Properties();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
        if (in != null)
        {
            try
            {
                tmpProperties.load(in);
                this.properties.putAll(tmpProperties);
                log.info("property file " + fileName + " loaded");
            }
            catch (IOException e)
            {
                log.error("Error loading JTidy property file " + fileName, e);
            }
        }
        else
        {
            log.error("Properties file [" + fileName + "] not found in class path");
        }
    }

    public static JTidyServletProperties getInstance()
    {
        if (instance == null)
        {
            instance = new JTidyServletProperties();
        }
        return instance;
    }

    /**
     * Reads a String property.
     * @param key property name
     * @return property value or <code>null</code> if property is not found
     */
    public String getProperty(String key)
    {
        return this.properties.getProperty(key);
    }

    /**
     * Reads a String property.
     * @param key property name
     * @param defaultValue default value returned if property is not found value
     * @return property value
     */
    public String getProperty(String key, String defaultValue)
    {
        String val = getProperty(key);
        if (val == null)
        {
            val = defaultValue;
        }
        return val;
    }

    /**
     * Reads an int property.
     * @param key property name
     * @param defaultValue default value returned if property is not found or not a valid int value
     * @return property value
     */
    public int getIntProperty(String key, int defaultValue)
    {
        int intValue = defaultValue;

        try
        {
            String sValue = getProperty(key);
            if (sValue == null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Value " + key + " does not exists");
                }
            }
            else
            {
                intValue = Integer.parseInt(sValue);
            }
        }
        catch (NumberFormatException e)
        {
            log.warn("Invalid value for \""
                + key
                + "\" property: value=\""
                + getProperty(key)
                + "\"; using default \""
                + defaultValue
                + "\"");
        }

        return intValue;
    }

    /**
     * Reads a boolean property.
     * @param key property name
     * @param defaultValue default value returned if property is not found or not a valid boolean value
     * @return property value
     */
    public boolean getBooleanProperty(String key, boolean defaultValue)
    {
        boolean intValue = defaultValue;

        String sValue = getProperty(key);
        if (sValue == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Value " + key + " does not exists");
            }
        }
        else
        {
            intValue = "true".equalsIgnoreCase(sValue);
        }

        return intValue;
    }

    /**
     * Returns an instance of configured RepositoryFactory. No Exception are thrown in case of any error use
     * DefaultRepositoryFactory.
     * @return RepositoryFactory instance.
     */
    public RepositoryFactory getRepositoryFactoryInstance()
    {
        String className = getProperty(PROPERTY_CLASS_REPOSITORYFACTORY);

        if (className != null)
        {
            try
            {
                Class classProperty = Class.forName(className);
                return (RepositoryFactory) classProperty.newInstance();
            }
            catch (Throwable e)
            {
                log.error("Error creating RepositoryFactory " + className, e);
            }
        }
        return new DefaultRepositoryFactory();
    }

    /**
     * Returns an instance of configured ResponseRecordRepository. No Exception are thrown in case of any error use
     * DefaultRepositoryFactory.
     * @return ResponseRecordRepository instance for given Session.
     */
    public ResponseRecordRepository getRepositoryInstance(HttpSession httpSession)
    {
        return getRepositoryFactoryInstance().getRepositoryInstance(httpSession);
    }

}
