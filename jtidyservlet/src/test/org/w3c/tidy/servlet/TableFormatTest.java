/*
 * Created on 26.09.2004
 *
 */
package org.w3c.tidy.servlet;

import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

/**
 * 
 * 
 */
public class TableFormatTest extends TidyServletCase {
    
    /**
     * Instantiates a new test case.
     * @param name test name
     */
    public TableFormatTest(String name)
    {
        super(name);
    }
    
    /**
     * Check that the table has one row even so it is not well formated initialy
     * @throws Exception any axception thrown during test.
     */
    public void testTableResponse() throws Exception
    {
        WebResponse response =  getResponse();

        WebTable[] tables = response.getTables();

        assertEquals("Expected 1 table in result.", 1, tables.length);
        assertEquals("Expected 1 table row in result.", 1, tables[0].getRowCount());
    }
}
