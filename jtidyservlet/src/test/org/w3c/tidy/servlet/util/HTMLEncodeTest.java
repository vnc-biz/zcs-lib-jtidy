/*
 * Created on 29.09.2004
 *
 */
package org.w3c.tidy.servlet.util;

import junit.framework.TestCase;

/**
 * 
 * 
 */
public class HTMLEncodeTest extends TestCase {

	public void validate(String expected, String input)
	{
		String r = HTMLEncode.encode(input);
		assertEquals(expected, r);
	}
	
	public void testEncode() throws Exception
    {
		validate("", null);
	    validate("&gt;", ">");
		validate("&lt;", "<");
		validate("&lt;&gt;", "<>");
		validate("&lt;\n&gt;", "<\n>");
		validate("&lt;EOL", "<EOL");
    }

	public void validateHREF(String expected, String[] input)
	{
		String r = HTMLEncode.encodeHREFQuery("", input);
		assertEquals(expected, r);
	}
	
	public void testEncodeHREF() throws Exception
	{
	    validateHREF("", null);
	    validateHREF("?p1", new String[]{"p1"});
	    validateHREF("?p1=p1v", new String[]{"p1", "p1v"});
	    validateHREF("?p1=p1v&p2", new String[]{"p1", "p1v", "p2"});
	}
}
