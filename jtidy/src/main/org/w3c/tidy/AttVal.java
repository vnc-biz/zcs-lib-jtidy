/**
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
package org.w3c.tidy;

/**
 * Attribute/Value linked list node.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org</a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca</a> (translation to Java)
 * @version $Revision $ ($Author $)
 */
public class AttVal extends Object implements Cloneable
{

    public AttVal next;
    public Attribute dict;
    public Node asp;
    public Node php;
    public int delim;
    public String attribute;
    public String value;

    public AttVal()
    {
        this.next = null;
        this.dict = null;
        this.asp = null;
        this.php = null;
        this.delim = 0;
        this.attribute = null;
        this.value = null;
    }

    public AttVal(AttVal next, Attribute dict, int delim, String attribute, String value)
    {
        this.next = next;
        this.dict = dict;
        this.asp = null;
        this.php = null;
        this.delim = delim;
        this.attribute = attribute;
        this.value = value;
    }

    public AttVal(AttVal next, Attribute dict, Node asp, Node php, int delim, String attribute, String value)
    {
        this.next = next;
        this.dict = dict;
        this.asp = asp;
        this.php = php;
        this.delim = delim;
        this.attribute = attribute;
        this.value = value;
    }

    protected Object clone()
    {
        AttVal av = new AttVal();
        if (next != null)
        {
            av.next = (AttVal) next.clone();
        }
        if (attribute != null)
            av.attribute = attribute;
        if (value != null)
            av.value = value;
        av.delim = delim;
        if (asp != null)
        {
            av.asp = (Node) asp.clone();
        }
        if (php != null)
        {
            av.php = (Node) php.clone();
        }
        av.dict = AttributeTable.getDefaultAttributeTable().findAttribute(this);
        return av;
    }

    public boolean isBoolAttribute()
    {
        Attribute attribute = this.dict;
        if (attribute != null)
        {
            if (attribute.attrchk == AttrCheckImpl.getCheckBool())
            {
                return true;
            }
        }

        return false;
    }

    /* ignore unknown attributes for proprietary elements */
    public Attribute checkAttribute(Lexer lexer, Node node)
    {
        TagTable tt = lexer.configuration.tt;

        if (this.asp == null && this.php == null)
            this.checkUniqueAttribute(lexer, node);

        Attribute attribute = this.dict;
        if (attribute != null)
        {
            /* title is vers 2.0 for A and LINK otherwise vers 4.0 */
            if (attribute == AttributeTable.attrTitle && (node.tag == tt.tagA || node.tag == tt.tagLink))
                lexer.versions &= Dict.VERS_ALL;
            else if ((attribute.versions & Dict.VERS_XML) != 0)
            {
                if (!(lexer.configuration.XmlTags || lexer.configuration.XmlOut))
                    Report.attrError(lexer, node, this.attribute, Report.XML_ATTRIBUTE_VALUE);
            }
            else
                lexer.versions &= attribute.versions;

            if (attribute.attrchk != null)
                attribute.attrchk.check(lexer, node, this);
        }
        else if (
            !lexer.configuration.XmlTags
                && !(node.tag == null)
                && this.asp == null
                && !(node.tag != null && ((node.tag.versions & Dict.VERS_PROPRIETARY) != 0)))
            Report.attrError(lexer, node, this.attribute, Report.UNKNOWN_ATTRIBUTE);

        return attribute;
    }

    /*
     * the same attribute name can't be used more than once in each element
     */
    public void checkUniqueAttribute(Lexer lexer, Node node)
    {
        AttVal attr;
        int count = 0;

        for (attr = this.next; attr != null; attr = attr.next)
        {
            if (this.attribute != null
                && attr.attribute != null
                && attr.asp == null
                && attr.php == null
                && Lexer.wstrcasecmp(this.attribute, attr.attribute) == 0)
                ++count;
        }

        if (count > 0)
            Report.attrError(lexer, node, this.attribute, Report.REPEATED_ATTRIBUTE);
    }

    /* --------------------- DOM ---------------------------- */

    protected org.w3c.dom.Attr adapter = null;

    protected org.w3c.dom.Attr getAdapter()
    {
        if (adapter == null)
        {
            adapter = new DOMAttrImpl(this);
        }
        return adapter;
    }
    /* --------------------- END DOM ------------------------ */

}