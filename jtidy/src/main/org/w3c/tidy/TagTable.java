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
 * Tag dictionary node hash table.
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org</a>
 * @author Andy Quick <a href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca</a> (translation to Java)
 * @version $Revision$ ($Author$)
 */
import java.util.Hashtable;

public class TagTable
{

    private Configuration configuration = null;

    public TagTable()
    {
        for (int i = 0; i < tags.length; i++)
        {
            install(tags[i]);
        }
        tagHtml = lookup("html");
        tagHead = lookup("head");
        tagBody = lookup("body");
        tagFrameset = lookup("frameset");
        tagFrame = lookup("frame");
        tagNoframes = lookup("noframes");
        tagMeta = lookup("meta");
        tagTitle = lookup("title");
        tagBase = lookup("base");
        tagHr = lookup("hr");
        tagPre = lookup("pre");
        tagListing = lookup("listing");
        tagH1 = lookup("h1");
        tagH2 = lookup("h2");
        tagP = lookup("p");
        tagUl = lookup("ul");
        tagOl = lookup("ol");
        tagDir = lookup("dir");
        tagLi = lookup("li");
        tagDt = lookup("dt");
        tagDd = lookup("dd");
        tagDl = lookup("dl");
        tagTd = lookup("td");
        tagTh = lookup("th");
        tagTr = lookup("tr");
        tagCol = lookup("col");
        tagBr = lookup("br");
        tagA = lookup("a");
        tagLink = lookup("link");
        tagB = lookup("b");
        tagI = lookup("i");
        tagStrong = lookup("strong");
        tagEm = lookup("em");
        tagBig = lookup("big");
        tagSmall = lookup("small");
        tagParam = lookup("param");
        tagOption = lookup("option");
        tagOptgroup = lookup("optgroup");
        tagImg = lookup("img");
        tagMap = lookup("map");
        tagArea = lookup("area");
        tagNobr = lookup("nobr");
        tagWbr = lookup("wbr");
        tagFont = lookup("font");
        tagSpacer = lookup("spacer");
        tagLayer = lookup("layer");
        tagCenter = lookup("center");
        tagStyle = lookup("style");
        tagScript = lookup("script");
        tagNoscript = lookup("noscript");
        tagTable = lookup("table");
        tagCaption = lookup("caption");
        tagForm = lookup("form");
        tagTextarea = lookup("textarea");
        tagBlockquote = lookup("blockquote");
        tagApplet = lookup("applet");
        tagObject = lookup("object");
        tagDiv = lookup("div");
        tagSpan = lookup("span");
    }

    public void setConfiguration(Configuration configuration)
    {
        this.configuration = configuration;
    }

    public Dict lookup(String name)
    {
        return (Dict) tagHashtable.get(name);
    }

    public Dict install(Dict dict)
    {
        Dict d = (Dict) tagHashtable.get(dict.name);
        if (d != null)
        {
            d.versions = dict.versions;
            d.model |= dict.model;
            d.parser = dict.parser;
            d.chkattrs = dict.chkattrs;
            return d;
        }
        else
        {
            tagHashtable.put(dict.name, dict);
            return dict;
        }
    }

    /* public interface for finding tag by name */
    public boolean findTag(Node node)
    {
        Dict np;

        if (configuration != null && configuration.XmlTags)
        {
            node.tag = xmlTags;
            return true;
        }

        if (node.element != null)
        {
            np = lookup(node.element);
            if (np != null)
            {
                node.tag = np;
                return true;
            }
        }

        return false;
    }

    public Parser findParser(Node node)
    {
        Dict np;

        if (node.element != null)
        {
            np = lookup(node.element);
            if (np != null)
            {
                return np.parser;
            }
        }

        return null;
    }

    private Hashtable tagHashtable = new Hashtable();

    private static Dict[] tags =
        {
            new Dict(
                "html",
                (short) (Dict.VERS_ALL | Dict.VERS_FRAMES),
                (Dict.CM_HTML | Dict.CM_OPT | Dict.CM_OMITST),
                ParserImpl.getParseHTML(),
                CheckAttribsImpl.getCheckHTML()),
            new Dict(
                "head",
                (short) (Dict.VERS_ALL | Dict.VERS_FRAMES),
                (Dict.CM_HTML | Dict.CM_OPT | Dict.CM_OMITST),
                ParserImpl.getParseHead(),
                null),
            new Dict(
                "title",
                (short) (Dict.VERS_ALL | Dict.VERS_FRAMES),
                Dict.CM_HEAD,
                ParserImpl.getParseTitle(),
                null),
            new Dict("base", (short) (Dict.VERS_ALL | Dict.VERS_FRAMES), (Dict.CM_HEAD | Dict.CM_EMPTY), null, null),
            new Dict(
                "link",
                (short) (Dict.VERS_ALL | Dict.VERS_FRAMES),
                (Dict.CM_HEAD | Dict.CM_EMPTY),
                null,
                CheckAttribsImpl.getCheckLINK()),
            new Dict("meta", (short) (Dict.VERS_ALL | Dict.VERS_FRAMES), (Dict.CM_HEAD | Dict.CM_EMPTY), null, null),
            new Dict(
                "style",
                (short) (Dict.VERS_FROM32 | Dict.VERS_FRAMES),
                Dict.CM_HEAD,
                ParserImpl.getParseScript(),
                CheckAttribsImpl.getCheckSTYLE()),
            new Dict(
                "script",
                (short) (Dict.VERS_FROM32 | Dict.VERS_FRAMES),
                (Dict.CM_HEAD | Dict.CM_MIXED | Dict.CM_BLOCK | Dict.CM_INLINE),
                ParserImpl.getParseScript(),
                CheckAttribsImpl.getCheckSCRIPT()),
            new Dict(
                "server",
                Dict.VERS_NETSCAPE,
                (Dict.CM_HEAD | Dict.CM_MIXED | Dict.CM_BLOCK | Dict.CM_INLINE),
                ParserImpl.getParseScript(),
                null),
            new Dict(
                "body",
                Dict.VERS_ALL,
                (Dict.CM_HTML | Dict.CM_OPT | Dict.CM_OMITST),
                ParserImpl.getParseBody(),
                null),
            new Dict(
                "frameset",
                Dict.VERS_FRAMES,
                (Dict.CM_HTML | Dict.CM_FRAMES),
                ParserImpl.getParseFrameSet(),
                null),
            new Dict("p", Dict.VERS_ALL, (Dict.CM_BLOCK | Dict.CM_OPT), ParserImpl.getParseInline(), null),
            new Dict("h1", Dict.VERS_ALL, (Dict.CM_BLOCK | Dict.CM_HEADING), ParserImpl.getParseInline(), null),
            new Dict("h2", Dict.VERS_ALL, (Dict.CM_BLOCK | Dict.CM_HEADING), ParserImpl.getParseInline(), null),
            new Dict("h3", Dict.VERS_ALL, (Dict.CM_BLOCK | Dict.CM_HEADING), ParserImpl.getParseInline(), null),
            new Dict("h4", Dict.VERS_ALL, (Dict.CM_BLOCK | Dict.CM_HEADING), ParserImpl.getParseInline(), null),
            new Dict("h5", Dict.VERS_ALL, (Dict.CM_BLOCK | Dict.CM_HEADING), ParserImpl.getParseInline(), null),
            new Dict("h6", Dict.VERS_ALL, (Dict.CM_BLOCK | Dict.CM_HEADING), ParserImpl.getParseInline(), null),
            new Dict("ul", Dict.VERS_ALL, Dict.CM_BLOCK, ParserImpl.getParseList(), null),
            new Dict("ol", Dict.VERS_ALL, Dict.CM_BLOCK, ParserImpl.getParseList(), null),
            new Dict("dl", Dict.VERS_ALL, Dict.CM_BLOCK, ParserImpl.getParseDefList(), null),
            new Dict("dir", Dict.VERS_LOOSE, (Dict.CM_BLOCK | Dict.CM_OBSOLETE), ParserImpl.getParseList(), null),
            new Dict("menu", Dict.VERS_LOOSE, (Dict.CM_BLOCK | Dict.CM_OBSOLETE), ParserImpl.getParseList(), null),
            new Dict("pre", Dict.VERS_ALL, Dict.CM_BLOCK, ParserImpl.getParsePre(), null),
            new Dict("listing", Dict.VERS_ALL, (Dict.CM_BLOCK | Dict.CM_OBSOLETE), ParserImpl.getParsePre(), null),
            new Dict("xmp", Dict.VERS_ALL, (Dict.CM_BLOCK | Dict.CM_OBSOLETE), ParserImpl.getParsePre(), null),
            new Dict("plaintext", Dict.VERS_ALL, (Dict.CM_BLOCK | Dict.CM_OBSOLETE), ParserImpl.getParsePre(), null),
            new Dict("address", Dict.VERS_ALL, Dict.CM_BLOCK, ParserImpl.getParseBlock(), null),
            new Dict("blockquote", Dict.VERS_ALL, Dict.CM_BLOCK, ParserImpl.getParseBlock(), null),
            new Dict("form", Dict.VERS_ALL, Dict.CM_BLOCK, ParserImpl.getParseBlock(), null),
            new Dict("isindex", Dict.VERS_LOOSE, (Dict.CM_BLOCK | Dict.CM_EMPTY), null, null),
            new Dict("fieldset", Dict.VERS_HTML40, Dict.CM_BLOCK, ParserImpl.getParseBlock(), null),
            new Dict(
                "table",
                Dict.VERS_FROM32,
                Dict.CM_BLOCK,
                ParserImpl.getParseTableTag(),
                CheckAttribsImpl.getCheckTABLE()),
            new Dict("hr", Dict.VERS_ALL, (Dict.CM_BLOCK | Dict.CM_EMPTY), null, CheckAttribsImpl.getCheckHR()),
            new Dict("div", Dict.VERS_FROM32, Dict.CM_BLOCK, ParserImpl.getParseBlock(), null),
            new Dict("multicol", Dict.VERS_NETSCAPE, Dict.CM_BLOCK, ParserImpl.getParseBlock(), null),
            new Dict("nosave", Dict.VERS_NETSCAPE, Dict.CM_BLOCK, ParserImpl.getParseBlock(), null),
            new Dict("layer", Dict.VERS_NETSCAPE, Dict.CM_BLOCK, ParserImpl.getParseBlock(), null),
            new Dict("ilayer", Dict.VERS_NETSCAPE, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict(
                "nolayer",
                Dict.VERS_NETSCAPE,
                (Dict.CM_BLOCK | Dict.CM_INLINE | Dict.CM_MIXED),
                ParserImpl.getParseBlock(),
                null),
            new Dict("align", Dict.VERS_NETSCAPE, Dict.CM_BLOCK, ParserImpl.getParseBlock(), null),
            new Dict("center", Dict.VERS_LOOSE, Dict.CM_BLOCK, ParserImpl.getParseBlock(), null),
            new Dict(
                "ins",
                Dict.VERS_HTML40,
                (Dict.CM_INLINE | Dict.CM_BLOCK | Dict.CM_MIXED),
                ParserImpl.getParseInline(),
                null),
            new Dict(
                "del",
                Dict.VERS_HTML40,
                (Dict.CM_INLINE | Dict.CM_BLOCK | Dict.CM_MIXED),
                ParserImpl.getParseInline(),
                null),
            new Dict(
                "li",
                Dict.VERS_ALL,
                (Dict.CM_LIST | Dict.CM_OPT | Dict.CM_NO_INDENT),
                ParserImpl.getParseBlock(),
                null),
            new Dict(
                "dt",
                Dict.VERS_ALL,
                (Dict.CM_DEFLIST | Dict.CM_OPT | Dict.CM_NO_INDENT),
                ParserImpl.getParseInline(),
                null),
            new Dict(
                "dd",
                Dict.VERS_ALL,
                (Dict.CM_DEFLIST | Dict.CM_OPT | Dict.CM_NO_INDENT),
                ParserImpl.getParseBlock(),
                null),
            new Dict(
                "caption",
                Dict.VERS_FROM32,
                Dict.CM_TABLE,
                ParserImpl.getParseInline(),
                CheckAttribsImpl.getCheckCaption()),
            new Dict("colgroup", Dict.VERS_HTML40, (Dict.CM_TABLE | Dict.CM_OPT), ParserImpl.getParseColGroup(), null),
            new Dict("col", Dict.VERS_HTML40, (Dict.CM_TABLE | Dict.CM_EMPTY), null, null),
            new Dict(
                "thead",
                Dict.VERS_HTML40,
                (Dict.CM_TABLE | Dict.CM_ROWGRP | Dict.CM_OPT),
                ParserImpl.getParseRowGroup(),
                null),
            new Dict(
                "tfoot",
                Dict.VERS_HTML40,
                (Dict.CM_TABLE | Dict.CM_ROWGRP | Dict.CM_OPT),
                ParserImpl.getParseRowGroup(),
                null),
            new Dict(
                "tbody",
                Dict.VERS_HTML40,
                (Dict.CM_TABLE | Dict.CM_ROWGRP | Dict.CM_OPT),
                ParserImpl.getParseRowGroup(),
                null),
            new Dict("tr", Dict.VERS_FROM32, (Dict.CM_TABLE | Dict.CM_OPT), ParserImpl.getParseRow(), null),
            new Dict(
                "td",
                Dict.VERS_FROM32,
                (Dict.CM_ROW | Dict.CM_OPT | Dict.CM_NO_INDENT),
                ParserImpl.getParseBlock(),
                CheckAttribsImpl.getCheckTableCell()),
            new Dict(
                "th",
                Dict.VERS_FROM32,
                (Dict.CM_ROW | Dict.CM_OPT | Dict.CM_NO_INDENT),
                ParserImpl.getParseBlock(),
                CheckAttribsImpl.getCheckTableCell()),
            new Dict("q", Dict.VERS_HTML40, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict(
                "a",
                Dict.VERS_ALL,
                Dict.CM_INLINE,
                ParserImpl.getParseInline(),
                CheckAttribsImpl.getCheckAnchor()),
            new Dict("br", Dict.VERS_ALL, (Dict.CM_INLINE | Dict.CM_EMPTY), null, null),
            new Dict(
                "img",
                Dict.VERS_ALL,
                (Dict.CM_INLINE | Dict.CM_IMG | Dict.CM_EMPTY),
                null,
                CheckAttribsImpl.getCheckIMG()),
            new Dict(
                "object",
                Dict.VERS_HTML40,
                (Dict.CM_OBJECT | Dict.CM_HEAD | Dict.CM_IMG | Dict.CM_INLINE | Dict.CM_PARAM),
                ParserImpl.getParseBlock(),
                null),
            new Dict(
                "applet",
                Dict.VERS_LOOSE,
                (Dict.CM_OBJECT | Dict.CM_IMG | Dict.CM_INLINE | Dict.CM_PARAM),
                ParserImpl.getParseBlock(),
                null),
            new Dict(
                "servlet",
                Dict.VERS_SUN,
                (Dict.CM_OBJECT | Dict.CM_IMG | Dict.CM_INLINE | Dict.CM_PARAM),
                ParserImpl.getParseBlock(),
                null),
            new Dict("param", Dict.VERS_FROM32, (Dict.CM_INLINE | Dict.CM_EMPTY), null, null),
            new Dict("embed", Dict.VERS_NETSCAPE, (Dict.CM_INLINE | Dict.CM_IMG | Dict.CM_EMPTY), null, null),
            new Dict("noembed", Dict.VERS_NETSCAPE, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("iframe", Dict.VERS_HTML40_LOOSE, Dict.CM_INLINE, ParserImpl.getParseBlock(), null),
            new Dict("frame", Dict.VERS_FRAMES, (Dict.CM_FRAMES | Dict.CM_EMPTY), null, null),
            new Dict(
                "noframes",
                Dict.VERS_IFRAMES,
                (Dict.CM_BLOCK | Dict.CM_FRAMES),
                ParserImpl.getParseNoFrames(),
                null),
            new Dict(
                "noscript",
                (short) (Dict.VERS_FRAMES | Dict.VERS_HTML40),
                (Dict.CM_BLOCK | Dict.CM_INLINE | Dict.CM_MIXED),
                ParserImpl.getParseBlock(),
                null),
            new Dict("b", Dict.VERS_ALL, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("i", Dict.VERS_ALL, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("u", Dict.VERS_LOOSE, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("tt", Dict.VERS_ALL, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("s", Dict.VERS_LOOSE, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("strike", Dict.VERS_LOOSE, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("big", Dict.VERS_FROM32, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("small", Dict.VERS_FROM32, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("sub", Dict.VERS_FROM32, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("sup", Dict.VERS_FROM32, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("em", Dict.VERS_ALL, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("strong", Dict.VERS_ALL, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("dfn", Dict.VERS_ALL, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("code", Dict.VERS_ALL, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("samp", Dict.VERS_ALL, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("kbd", Dict.VERS_ALL, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("var", Dict.VERS_ALL, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("cite", Dict.VERS_ALL, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("abbr", Dict.VERS_HTML40, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("acronym", Dict.VERS_HTML40, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("span", Dict.VERS_FROM32, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("blink", Dict.VERS_PROPRIETARY, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("nobr", Dict.VERS_PROPRIETARY, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("wbr", Dict.VERS_PROPRIETARY, (Dict.CM_INLINE | Dict.CM_EMPTY), null, null),
            new Dict("marquee", Dict.VERS_MICROSOFT, (Dict.CM_INLINE | Dict.CM_OPT), ParserImpl.getParseInline(), null),
            new Dict("bgsound", Dict.VERS_MICROSOFT, (Dict.CM_HEAD | Dict.CM_EMPTY), null, null),
            new Dict("comment", Dict.VERS_MICROSOFT, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("spacer", Dict.VERS_NETSCAPE, (Dict.CM_INLINE | Dict.CM_EMPTY), null, null),
            new Dict("keygen", Dict.VERS_NETSCAPE, (Dict.CM_INLINE | Dict.CM_EMPTY), null, null),
            new Dict(
                "nolayer",
                Dict.VERS_NETSCAPE,
                (Dict.CM_BLOCK | Dict.CM_INLINE | Dict.CM_MIXED),
                ParserImpl.getParseBlock(),
                null),
            new Dict("ilayer", Dict.VERS_NETSCAPE, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict(
                "map",
                Dict.VERS_FROM32,
                Dict.CM_INLINE,
                ParserImpl.getParseBlock(),
                CheckAttribsImpl.getCheckMap()),
            new Dict("area", Dict.VERS_ALL, (Dict.CM_BLOCK | Dict.CM_EMPTY), null, CheckAttribsImpl.getCheckAREA()),
            new Dict("input", Dict.VERS_ALL, (Dict.CM_INLINE | Dict.CM_IMG | Dict.CM_EMPTY), null, null),
            new Dict("select", Dict.VERS_ALL, (Dict.CM_INLINE | Dict.CM_FIELD), ParserImpl.getParseSelect(), null),
            new Dict("option", Dict.VERS_ALL, (Dict.CM_FIELD | Dict.CM_OPT), ParserImpl.getParseText(), null),
            new Dict("optgroup", Dict.VERS_HTML40, (Dict.CM_FIELD | Dict.CM_OPT), ParserImpl.getParseOptGroup(), null),
            new Dict("textarea", Dict.VERS_ALL, (Dict.CM_INLINE | Dict.CM_FIELD), ParserImpl.getParseText(), null),
            new Dict("label", Dict.VERS_HTML40, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("legend", Dict.VERS_HTML40, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("button", Dict.VERS_HTML40, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("basefont", Dict.VERS_LOOSE, (Dict.CM_INLINE | Dict.CM_EMPTY), null, null),
            new Dict("font", Dict.VERS_LOOSE, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            new Dict("bdo", Dict.VERS_HTML40, Dict.CM_INLINE, ParserImpl.getParseInline(), null),
            };

    /* create dummy entry for all xml tags */
    public Dict xmlTags = new Dict(null, Dict.VERS_ALL, Dict.CM_BLOCK, null, null);

    public Dict tagHtml = null;
    public Dict tagHead = null;
    public Dict tagBody = null;
    public Dict tagFrameset = null;
    public Dict tagFrame = null;
    public Dict tagNoframes = null;
    public Dict tagMeta = null;
    public Dict tagTitle = null;
    public Dict tagBase = null;
    public Dict tagHr = null;
    public Dict tagPre = null;
    public Dict tagListing = null;
    public Dict tagH1 = null;
    public Dict tagH2 = null;
    public Dict tagP = null;
    public Dict tagUl = null;
    public Dict tagOl = null;
    public Dict tagDir = null;
    public Dict tagLi = null;
    public Dict tagDt = null;
    public Dict tagDd = null;
    public Dict tagDl = null;
    public Dict tagTd = null;
    public Dict tagTh = null;
    public Dict tagTr = null;
    public Dict tagCol = null;
    public Dict tagBr = null;
    public Dict tagA = null;
    public Dict tagLink = null;
    public Dict tagB = null;
    public Dict tagI = null;
    public Dict tagStrong = null;
    public Dict tagEm = null;
    public Dict tagBig = null;
    public Dict tagSmall = null;
    public Dict tagParam = null;
    public Dict tagOption = null;
    public Dict tagOptgroup = null;
    public Dict tagImg = null;
    public Dict tagMap = null;
    public Dict tagArea = null;
    public Dict tagNobr = null;
    public Dict tagWbr = null;
    public Dict tagFont = null;
    public Dict tagSpacer = null;
    public Dict tagLayer = null;
    public Dict tagCenter = null;
    public Dict tagStyle = null;
    public Dict tagScript = null;
    public Dict tagNoscript = null;
    public Dict tagTable = null;
    public Dict tagCaption = null;
    public Dict tagForm = null;
    public Dict tagTextarea = null;
    public Dict tagBlockquote = null;
    public Dict tagApplet = null;
    public Dict tagObject = null;
    public Dict tagDiv = null;
    public Dict tagSpan = null;

    public void defineInlineTag(String name)
    {
        install(
            new Dict(
                name,
                Dict.VERS_PROPRIETARY,
                (Dict.CM_INLINE | Dict.CM_NO_INDENT | Dict.CM_NEW),
                ParserImpl.getParseBlock(),
                null));
    }

    public void defineBlockTag(String name)
    {
        install(
            new Dict(
                name,
                Dict.VERS_PROPRIETARY,
                (Dict.CM_BLOCK | Dict.CM_NO_INDENT | Dict.CM_NEW),
                ParserImpl.getParseBlock(),
                null));
    }

    public void defineEmptyTag(String name)
    {
        install(
            new Dict(
                name,
                Dict.VERS_PROPRIETARY,
                (Dict.CM_EMPTY | Dict.CM_NO_INDENT | Dict.CM_NEW),
                ParserImpl.getParseBlock(),
                null));
    }

    public void definePreTag(String name)
    {
        install(
            new Dict(
                name,
                Dict.VERS_PROPRIETARY,
                (Dict.CM_BLOCK | Dict.CM_NO_INDENT | Dict.CM_NEW),
                ParserImpl.getParsePre(),
                null));
    }
}