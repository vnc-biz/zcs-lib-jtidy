/*
 * @(#)DOMDocumentImpl.java   1.11 2000/08/16
 *
 */

package org.w3c.tidy;

import org.w3c.dom.DOMException;

/**
 *
 * DOMDocumentImpl
 *
 * (c) 1998-2000 (W3C) MIT, INRIA, Keio University
 * See Tidy.java for the copyright notice.
 * Derived from <a href="http://www.w3.org/People/Raggett/tidy">
 * HTML Tidy Release 4 Aug 2000</a>
 *
 * @author  Dave Raggett <dsr@w3.org>
 * @author  Andy Quick <ac.quick@sympatico.ca> (translation to Java)
 * @version 1.4, 1999/09/04 DOM Support
 * @version 1.5, 1999/10/23 Tidy Release 27 Sep 1999
 * @version 1.6, 1999/11/01 Tidy Release 22 Oct 1999
 * @version 1.7, 1999/12/06 Tidy Release 30 Nov 1999
 * @version 1.8, 2000/01/22 Tidy Release 13 Jan 2000
 * @version 1.9, 2000/06/03 Tidy Release 30 Apr 2000
 * @version 1.10, 2000/07/22 Tidy Release 8 Jul 2000
 * @version 1.11, 2000/08/16 Tidy Release 4 Aug 2000
 */

public class DOMDocumentImpl extends DOMNodeImpl implements org.w3c.dom.Document {

    private TagTable tt;      // a DOM Document has its own TagTable.

    protected DOMDocumentImpl(Node adaptee)
    {
        super(adaptee);
        tt = new TagTable();
    }

    public void setTagTable(TagTable tt)
    {
        this.tt = tt;
    }

    /* --------------------- DOM ---------------------------- */

    /**
     * @see org.w3c.dom.Node#getNodeName
     */
    public String getNodeName()
    {
        return "#document";
    }

    /**
     * @see org.w3c.dom.Node#getNodeType
     */
    public short getNodeType()
    {
        return org.w3c.dom.Node.DOCUMENT_NODE;
    }

    /**
     * @see org.w3c.dom.Document#getDoctype
     */
    public org.w3c.dom.DocumentType       getDoctype()
    {
        Node node = adaptee.content;
        while (node != null) {
            if (node.type == Node.DocTypeTag) break;
            node = node.next;
        }
        if (node != null)
            return (org.w3c.dom.DocumentType)node.getAdapter();
        else
            return null;
    }

    /**
     * @see org.w3c.dom.Document#getImplementation
     */
    public org.w3c.dom.DOMImplementation  getImplementation()
    {
        // NOT SUPPORTED
        return null;
    }

    /**
     * @see org.w3c.dom.Document#getDocumentElement
     */
    public org.w3c.dom.Element            getDocumentElement()
    {
        Node node = adaptee.content;
        while (node != null) {
            if (node.type == Node.StartTag ||
                node.type == Node.StartEndTag) break;
            node = node.next;
        }
        if (node != null)
            return (org.w3c.dom.Element)node.getAdapter();
        else
            return null;
    }

    /**
     * @see org.w3c.dom.Document#createElement
     */
    public org.w3c.dom.Element            createElement(String tagName)
                                            throws DOMException
    {
        Node node = new Node(Node.StartEndTag, null, 0, 0, tagName, tt);
        if (node != null) {
            if (node.tag == null)           // Fix Bug 121206
              node.tag = tt.xmlTags;
            return (org.w3c.dom.Element)node.getAdapter();
        }
        else
            return null;
    }

    /**
     * @see org.w3c.dom.Document#createDocumentFragment
     */
    public org.w3c.dom.DocumentFragment   createDocumentFragment()
    {
        // NOT SUPPORTED
        return null;
    }

    /**
     * @see org.w3c.dom.Document#createTextNode
     */
    public org.w3c.dom.Text               createTextNode(String data)
    {
        byte[] textarray = Lexer.getBytes(data);
        Node node = new Node(Node.TextNode, textarray, 0, textarray.length);
        if (node != null)
            return (org.w3c.dom.Text)node.getAdapter();
        else
            return null;
    }

    /**
     * @see org.w3c.dom.Document#createComment
     */
    public org.w3c.dom.Comment            createComment(String data)
    {
        byte[] textarray = Lexer.getBytes(data);
        Node node = new Node(Node.CommentTag, textarray, 0, textarray.length);
        if (node != null)
            return (org.w3c.dom.Comment)node.getAdapter();
        else
            return null;
    }

    /**
     * @see org.w3c.dom.Document#createCDATASection
     */
    public org.w3c.dom.CDATASection       createCDATASection(String data)
                                                 throws DOMException
    {
        // NOT SUPPORTED
        return null;
    }

    /**
     * @see org.w3c.dom.Document#createProcessingInstruction
     */
    public org.w3c.dom.ProcessingInstruction createProcessingInstruction(String target, 
                                                          String data)
                                                          throws DOMException
    {
        throw new DOMExceptionImpl(DOMException.NOT_SUPPORTED_ERR,
                                   "HTML document");
    }

    /**
     * @see org.w3c.dom.Document#createAttribute
     */
    public org.w3c.dom.Attr               createAttribute(String name)
                                              throws DOMException
    {
        AttVal av = new AttVal(null, null, (int)'"', name, null);
        if (av != null) {
            av.dict =
                AttributeTable.getDefaultAttributeTable().findAttribute(av);
            return (org.w3c.dom.Attr)av.getAdapter();
        } else {
            return null;
        }
    }

    /**
     * @see org.w3c.dom.Document#createEntityReference
     */
    public org.w3c.dom.EntityReference    createEntityReference(String name)
                                                    throws DOMException
    {
        // NOT SUPPORTED
        return null;
    }

    /**
     * @see org.w3c.dom.Document#getElementsByTagName
     */
    public org.w3c.dom.NodeList           getElementsByTagName(String tagname)
    {
        return new DOMNodeListByTagNameImpl(this.adaptee, tagname);
    }

    /**
     * DOM2 - not implemented.
     * @exception   org.w3c.dom.DOMException
     */
    public org.w3c.dom.Node importNode(org.w3c.dom.Node importedNode, boolean deep)
        throws org.w3c.dom.DOMException
    {
	return null;
    }

    /**
     * DOM2 - not implemented.
     * @exception   org.w3c.dom.DOMException
     */
    public org.w3c.dom.Attr createAttributeNS(String namespaceURI,
                                              String qualifiedName)
        throws org.w3c.dom.DOMException
    {
	return null;
    }

    /**
     * DOM2 - not implemented.
     * @exception   org.w3c.dom.DOMException
     */
    public org.w3c.dom.Element createElementNS(String namespaceURI,
                                               String qualifiedName)
        throws org.w3c.dom.DOMException
    {
	return null;
    }

    /**
     * DOM2 - not implemented.
     */
    public org.w3c.dom.NodeList getElementsByTagNameNS(String namespaceURI,
                                                       String localName)
    {
	return null;
    }

    /**
     * DOM2 - not implemented.
     */
    public org.w3c.dom.Element getElementById(String elementId)
    {
	return null;
    }

}
