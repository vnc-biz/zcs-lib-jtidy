/*
 * @(#)TagTable.java   1.11 2000/08/16
 *
 */

package org.w3c.tidy;

/**
 *
 * Tag dictionary node hash table
 *
 * (c) 1998-2000 (W3C) MIT, INRIA, Keio University
 * See Tidy.java for the copyright notice.
 * Derived from <a href="http://www.w3.org/People/Raggett/tidy">
 * HTML Tidy Release 4 Aug 2000</a>
 *
 * @author  Dave Raggett <dsr@w3.org>
 * @author  Andy Quick <ac.quick@sympatico.ca> (translation to Java)
 * @version 1.0, 1999/05/22
 * @version 1.0.1, 1999/05/29
 * @version 1.1, 1999/06/18 Java Bean
 * @version 1.2, 1999/07/10 Tidy Release 7 Jul 1999
 * @version 1.3, 1999/07/30 Tidy Release 26 Jul 1999
 * @version 1.4, 1999/09/04 DOM support
 * @version 1.5, 1999/10/23 Tidy Release 27 Sep 1999
 * @version 1.6, 1999/11/01 Tidy Release 22 Oct 1999
 * @version 1.7, 1999/12/06 Tidy Release 30 Nov 1999
 * @version 1.8, 2000/01/22 Tidy Release 13 Jan 2000
 * @version 1.9, 2000/06/03 Tidy Release 30 Apr 2000
 * @version 1.10, 2000/07/22 Tidy Release 8 Jul 2000
 * @version 1.11, 2000/08/16 Tidy Release 4 Aug 2000
 */

import java.util.Hashtable;
import java.util.Enumeration;

public class TagTable {

    private Configuration configuration = null;

    public TagTable()
    {
    }

    public void setConfiguration(Configuration configuration)
    {
        this.configuration = configuration;
    }

    public Dict lookup( String name )
    {
        return (Dict)tagHashtable.get( name );
    }

    public Dict install( Dict dict )
    {
        Dict d = (Dict)tagHashtable.get(dict.name);
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
    public boolean findTag( Node node )
    {
        Dict np;

        if ( configuration != null && configuration.XmlTags ) {
            node.tag = xmlTags;
            return true;
        }

        if ( node.element != null ) {
            np = lookup( node.element );
            if ( np != null ) {
                node.tag = np;
                return true;
            }
        }

        return false;
    }

    public Parser findParser(Node node)
    {
        Dict np;

        if (node.element != null) {
            np = lookup(node.element);
            if (np != null) {
                return np.parser;
            }
        }

        return null;
    }

    private Hashtable tagHashtable = new Hashtable();

    private static TagTable defaultTagTable = null;

    private static Dict[] tags = {

    new Dict( "html",       (short)(Dict.VERS_ALL|Dict.VERS_FRAMES),     (Dict.CM_HTML|Dict.CM_OPT|Dict.CM_OMITST),  ParserImpl.getParseHTML(), CheckAttribsImpl.getCheckHTML() ),

    new Dict( "head",       (short)(Dict.VERS_ALL|Dict.VERS_FRAMES),     (Dict.CM_HTML|Dict.CM_OPT|Dict.CM_OMITST), ParserImpl.getParseHead(), null ),

    new Dict( "title",      (short)(Dict.VERS_ALL|Dict.VERS_FRAMES),     Dict.CM_HEAD, ParserImpl.getParseTitle(), null ),
    new Dict( "base",       (short)(Dict.VERS_ALL|Dict.VERS_FRAMES),     (Dict.CM_HEAD|Dict.CM_EMPTY), null, null ),
    new Dict( "link",       (short)(Dict.VERS_ALL|Dict.VERS_FRAMES),     (Dict.CM_HEAD|Dict.CM_EMPTY), null, CheckAttribsImpl.getCheckLINK() ),
    new Dict( "meta",       (short)(Dict.VERS_ALL|Dict.VERS_FRAMES),     (Dict.CM_HEAD|Dict.CM_EMPTY), null, null ),
    new Dict( "style",      (short)(Dict.VERS_FROM32|Dict.VERS_FRAMES),  Dict.CM_HEAD, ParserImpl.getParseScript(), CheckAttribsImpl.getCheckSTYLE() ),
    new Dict( "script",     (short)(Dict.VERS_FROM32|Dict.VERS_FRAMES),  (Dict.CM_HEAD|Dict.CM_MIXED|Dict.CM_BLOCK|Dict.CM_INLINE), ParserImpl.getParseScript(), CheckAttribsImpl.getCheckSCRIPT() ),
    new Dict( "server",     Dict.VERS_NETSCAPE,  (Dict.CM_HEAD|Dict.CM_MIXED|Dict.CM_BLOCK|Dict.CM_INLINE), ParserImpl.getParseScript(), null ),

    new Dict( "body",       Dict.VERS_ALL,     (Dict.CM_HTML|Dict.CM_OPT|Dict.CM_OMITST), ParserImpl.getParseBody(), null ),
    new Dict( "frameset",   Dict.VERS_FRAMES,  (Dict.CM_HTML|Dict.CM_FRAMES), ParserImpl.getParseFrameSet(), null ),

    new Dict( "p",          Dict.VERS_ALL,     (Dict.CM_BLOCK|Dict.CM_OPT), ParserImpl.getParseInline(), null ),
    new Dict( "h1",         Dict.VERS_ALL,     (Dict.CM_BLOCK|Dict.CM_HEADING), ParserImpl.getParseInline(), null ),
    new Dict( "h2",         Dict.VERS_ALL,     (Dict.CM_BLOCK|Dict.CM_HEADING), ParserImpl.getParseInline(), null ),
    new Dict( "h3",         Dict.VERS_ALL,     (Dict.CM_BLOCK|Dict.CM_HEADING), ParserImpl.getParseInline(), null ),
    new Dict( "h4",         Dict.VERS_ALL,     (Dict.CM_BLOCK|Dict.CM_HEADING), ParserImpl.getParseInline(), null ),
    new Dict( "h5",         Dict.VERS_ALL,     (Dict.CM_BLOCK|Dict.CM_HEADING), ParserImpl.getParseInline(), null ),
    new Dict( "h6",         Dict.VERS_ALL,     (Dict.CM_BLOCK|Dict.CM_HEADING), ParserImpl.getParseInline(), null ),
    new Dict( "ul",         Dict.VERS_ALL,     Dict.CM_BLOCK, ParserImpl.getParseList(), null ),
    new Dict( "ol",         Dict.VERS_ALL,     Dict.CM_BLOCK, ParserImpl.getParseList(), null ),
    new Dict( "dl",         Dict.VERS_ALL,     Dict.CM_BLOCK, ParserImpl.getParseDefList(), null ),
    new Dict( "dir",        Dict.VERS_LOOSE,   (Dict.CM_BLOCK|Dict.CM_OBSOLETE), ParserImpl.getParseList(), null ),
    new Dict( "menu",       Dict.VERS_LOOSE,   (Dict.CM_BLOCK|Dict.CM_OBSOLETE), ParserImpl.getParseList(), null ),
    new Dict( "pre",        Dict.VERS_ALL,     Dict.CM_BLOCK, ParserImpl.getParsePre(), null ),
    new Dict( "listing",    Dict.VERS_ALL,     (Dict.CM_BLOCK|Dict.CM_OBSOLETE), ParserImpl.getParsePre(), null ),
    new Dict( "xmp",        Dict.VERS_ALL,     (Dict.CM_BLOCK|Dict.CM_OBSOLETE), ParserImpl.getParsePre(), null ),
    new Dict( "plaintext",  Dict.VERS_ALL,     (Dict.CM_BLOCK|Dict.CM_OBSOLETE), ParserImpl.getParsePre(), null ),
    new Dict( "address",    Dict.VERS_ALL,     Dict.CM_BLOCK, ParserImpl.getParseBlock(), null ),
    new Dict( "blockquote", Dict.VERS_ALL,     Dict.CM_BLOCK, ParserImpl.getParseBlock(), null ),
    new Dict( "form",       Dict.VERS_ALL,     Dict.CM_BLOCK, ParserImpl.getParseBlock(), null ),
    new Dict( "isindex",    Dict.VERS_LOOSE,   (Dict.CM_BLOCK|Dict.CM_EMPTY), null, null ),
    new Dict( "fieldset",   Dict.VERS_HTML40,  Dict.CM_BLOCK, ParserImpl.getParseBlock(), null ),
    new Dict( "table",      Dict.VERS_FROM32,  Dict.CM_BLOCK, ParserImpl.getParseTableTag(), CheckAttribsImpl.getCheckTABLE() ),
    new Dict( "hr",         Dict.VERS_ALL,     (Dict.CM_BLOCK|Dict.CM_EMPTY),  null, CheckAttribsImpl.getCheckHR() ),
    new Dict( "div",        Dict.VERS_FROM32,  Dict.CM_BLOCK, ParserImpl.getParseBlock(), null ),
    new Dict( "multicol",   Dict.VERS_NETSCAPE,  Dict.CM_BLOCK, ParserImpl.getParseBlock(), null ),
    new Dict( "nosave",     Dict.VERS_NETSCAPE, Dict.CM_BLOCK, ParserImpl.getParseBlock(), null ),
    new Dict( "layer",      Dict.VERS_NETSCAPE, Dict.CM_BLOCK, ParserImpl.getParseBlock(), null ),
    new Dict( "ilayer",     Dict.VERS_NETSCAPE, Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "nolayer",    Dict.VERS_NETSCAPE, (Dict.CM_BLOCK|Dict.CM_INLINE|Dict.CM_MIXED), ParserImpl.getParseBlock(), null ),
    new Dict( "align",      Dict.VERS_NETSCAPE, Dict.CM_BLOCK, ParserImpl.getParseBlock(), null ),
    new Dict( "center",     Dict.VERS_LOOSE,   Dict.CM_BLOCK, ParserImpl.getParseBlock(), null ),
    new Dict( "ins",        Dict.VERS_HTML40,  (Dict.CM_INLINE|Dict.CM_BLOCK|Dict.CM_MIXED), ParserImpl.getParseInline(), null ),
    new Dict( "del",        Dict.VERS_HTML40,  (Dict.CM_INLINE|Dict.CM_BLOCK|Dict.CM_MIXED), ParserImpl.getParseInline(), null ),

    new Dict( "li",         Dict.VERS_ALL,     (Dict.CM_LIST|Dict.CM_OPT|Dict.CM_NO_INDENT), ParserImpl.getParseBlock(), null ),
    new Dict( "dt",         Dict.VERS_ALL,     (Dict.CM_DEFLIST|Dict.CM_OPT|Dict.CM_NO_INDENT), ParserImpl.getParseInline(), null ),
    new Dict( "dd",         Dict.VERS_ALL,     (Dict.CM_DEFLIST|Dict.CM_OPT|Dict.CM_NO_INDENT), ParserImpl.getParseBlock(), null ),

    new Dict( "caption",    Dict.VERS_FROM32,  Dict.CM_TABLE, ParserImpl.getParseInline(), CheckAttribsImpl.getCheckCaption() ),
    new Dict( "colgroup",   Dict.VERS_HTML40,  (Dict.CM_TABLE|Dict.CM_OPT), ParserImpl.getParseColGroup(), null ),
    new Dict( "col",        Dict.VERS_HTML40,  (Dict.CM_TABLE|Dict.CM_EMPTY),  null, null ),
    new Dict( "thead",      Dict.VERS_HTML40,  (Dict.CM_TABLE|Dict.CM_ROWGRP|Dict.CM_OPT), ParserImpl.getParseRowGroup(), null ),
    new Dict( "tfoot",      Dict.VERS_HTML40,  (Dict.CM_TABLE|Dict.CM_ROWGRP|Dict.CM_OPT), ParserImpl.getParseRowGroup(), null ),
    new Dict( "tbody",      Dict.VERS_HTML40,  (Dict.CM_TABLE|Dict.CM_ROWGRP|Dict.CM_OPT), ParserImpl.getParseRowGroup(), null ),
    new Dict( "tr",         Dict.VERS_FROM32,  (Dict.CM_TABLE|Dict.CM_OPT), ParserImpl.getParseRow(), null ),
    new Dict( "td",         Dict.VERS_FROM32,  (Dict.CM_ROW|Dict.CM_OPT|Dict.CM_NO_INDENT), ParserImpl.getParseBlock(), CheckAttribsImpl.getCheckTableCell() ),
    new Dict( "th",         Dict.VERS_FROM32,  (Dict.CM_ROW|Dict.CM_OPT|Dict.CM_NO_INDENT), ParserImpl.getParseBlock(), CheckAttribsImpl.getCheckTableCell() ),

    new Dict( "q",          Dict.VERS_HTML40,  Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "a",          Dict.VERS_ALL,     Dict.CM_INLINE, ParserImpl.getParseInline(), CheckAttribsImpl.getCheckAnchor() ),
    new Dict( "br",         Dict.VERS_ALL,     (Dict.CM_INLINE|Dict.CM_EMPTY), null, null ),
    new Dict( "img",        Dict.VERS_ALL,     (Dict.CM_INLINE|Dict.CM_IMG|Dict.CM_EMPTY), null, CheckAttribsImpl.getCheckIMG() ),
    new Dict( "object",     Dict.VERS_HTML40,  (Dict.CM_OBJECT|Dict.CM_HEAD|Dict.CM_IMG|Dict.CM_INLINE|Dict.CM_PARAM), ParserImpl.getParseBlock(), null ),
    new Dict( "applet",     Dict.VERS_LOOSE,   (Dict.CM_OBJECT|Dict.CM_IMG|Dict.CM_INLINE|Dict.CM_PARAM), ParserImpl.getParseBlock(), null ),
    new Dict( "servlet",    Dict.VERS_SUN,     (Dict.CM_OBJECT|Dict.CM_IMG|Dict.CM_INLINE|Dict.CM_PARAM), ParserImpl.getParseBlock(), null ),
    new Dict( "param",      Dict.VERS_FROM32,  (Dict.CM_INLINE|Dict.CM_EMPTY), null, null ),
    new Dict( "embed",      Dict.VERS_NETSCAPE, (Dict.CM_INLINE|Dict.CM_IMG|Dict.CM_EMPTY), null, null ),
    new Dict( "noembed",    Dict.VERS_NETSCAPE, Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "iframe",     Dict.VERS_HTML40_LOOSE, Dict.CM_INLINE, ParserImpl.getParseBlock(), null ),
    new Dict( "frame",      Dict.VERS_FRAMES,  (Dict.CM_FRAMES|Dict.CM_EMPTY), null, null ),
    new Dict( "noframes",   Dict.VERS_IFRAMES, (Dict.CM_BLOCK|Dict.CM_FRAMES), ParserImpl.getParseNoFrames(),  null ),
    new Dict( "noscript",   (short)(Dict.VERS_FRAMES|Dict.VERS_HTML40),  (Dict.CM_BLOCK|Dict.CM_INLINE|Dict.CM_MIXED), ParserImpl.getParseBlock(), null ),
    new Dict( "b",          Dict.VERS_ALL,     Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "i",          Dict.VERS_ALL,     Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "u",          Dict.VERS_LOOSE,   Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "tt",         Dict.VERS_ALL,     Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "s",          Dict.VERS_LOOSE,   Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "strike",     Dict.VERS_LOOSE,   Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "big",        Dict.VERS_FROM32,  Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "small",      Dict.VERS_FROM32,  Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "sub",        Dict.VERS_FROM32,  Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "sup",        Dict.VERS_FROM32,  Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "em",         Dict.VERS_ALL,     Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "strong",     Dict.VERS_ALL,     Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "dfn",        Dict.VERS_ALL,     Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "code",       Dict.VERS_ALL,     Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "samp",       Dict.VERS_ALL,     Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "kbd",        Dict.VERS_ALL,     Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "var",        Dict.VERS_ALL,     Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "cite",       Dict.VERS_ALL,     Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "abbr",       Dict.VERS_HTML40,  Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "acronym",    Dict.VERS_HTML40,  Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "span",       Dict.VERS_FROM32,  Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "blink",      Dict.VERS_PROPRIETARY, Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "nobr",       Dict.VERS_PROPRIETARY, Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "wbr",        Dict.VERS_PROPRIETARY, (Dict.CM_INLINE|Dict.CM_EMPTY), null, null ),
    new Dict( "marquee",    Dict.VERS_MICROSOFT, (Dict.CM_INLINE|Dict.CM_OPT), ParserImpl.getParseInline(), null ),
    new Dict( "bgsound",    Dict.VERS_MICROSOFT, (Dict.CM_HEAD|Dict.CM_EMPTY), null, null ),
    new Dict( "comment",    Dict.VERS_MICROSOFT, Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "spacer",     Dict.VERS_NETSCAPE, (Dict.CM_INLINE|Dict.CM_EMPTY), null, null ),
    new Dict( "keygen",     Dict.VERS_NETSCAPE, (Dict.CM_INLINE|Dict.CM_EMPTY), null, null ),
    new Dict( "nolayer",    Dict.VERS_NETSCAPE, (Dict.CM_BLOCK|Dict.CM_INLINE|Dict.CM_MIXED), ParserImpl.getParseBlock(), null ),
    new Dict( "ilayer",     Dict.VERS_NETSCAPE, Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "map",        Dict.VERS_FROM32,  Dict.CM_INLINE, ParserImpl.getParseBlock(), CheckAttribsImpl.getCheckMap() ),
    new Dict( "area",       Dict.VERS_ALL,     (Dict.CM_BLOCK|Dict.CM_EMPTY), null, CheckAttribsImpl.getCheckAREA() ),
    new Dict( "input",      Dict.VERS_ALL,     (Dict.CM_INLINE|Dict.CM_IMG|Dict.CM_EMPTY), null, null ),
    new Dict( "select",     Dict.VERS_ALL,     (Dict.CM_INLINE|Dict.CM_FIELD), ParserImpl.getParseSelect(), null ),
    new Dict( "option",     Dict.VERS_ALL,     (Dict.CM_FIELD|Dict.CM_OPT), ParserImpl.getParseText(), null ),
    new Dict( "optgroup",   Dict.VERS_HTML40,  (Dict.CM_FIELD|Dict.CM_OPT), ParserImpl.getParseOptGroup(), null ),
    new Dict( "textarea",   Dict.VERS_ALL,     (Dict.CM_INLINE|Dict.CM_FIELD), ParserImpl.getParseText(), null ),
    new Dict( "label",      Dict.VERS_HTML40,  Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "legend",     Dict.VERS_HTML40,  Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "button",     Dict.VERS_HTML40,  Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "basefont",   Dict.VERS_LOOSE,   (Dict.CM_INLINE|Dict.CM_EMPTY), null, null ),
    new Dict( "font",       Dict.VERS_LOOSE,   Dict.CM_INLINE, ParserImpl.getParseInline(), null ),
    new Dict( "bdo",        Dict.VERS_HTML40,  Dict.CM_INLINE, ParserImpl.getParseInline(), null ),

    };

    /* create dummy entry for all xml tags */
    public static Dict xmlTags = new Dict( null, Dict.VERS_ALL, Dict.CM_BLOCK, null, null );

    public static Dict tagHtml = null;
    public static Dict tagHead = null;
    public static Dict tagBody = null;
    public static Dict tagFrameset = null;
    public static Dict tagFrame = null;
    public static Dict tagNoframes = null;
    public static Dict tagMeta = null;
    public static Dict tagTitle = null;
    public static Dict tagBase = null;
    public static Dict tagHr = null;
    public static Dict tagPre = null;
    public static Dict tagListing = null;
    public static Dict tagH1 = null;
    public static Dict tagH2 = null;
    public static Dict tagP  = null;
    public static Dict tagUl = null;
    public static Dict tagOl = null;
    public static Dict tagDir = null;
    public static Dict tagLi = null;
    public static Dict tagDt = null;
    public static Dict tagDd = null;
    public static Dict tagDl = null;
    public static Dict tagTd = null;
    public static Dict tagTh = null;
    public static Dict tagTr = null;
    public static Dict tagCol = null;
    public static Dict tagBr = null;
    public static Dict tagA = null;
    public static Dict tagLink = null;
    public static Dict tagB = null;
    public static Dict tagI = null;
    public static Dict tagStrong = null;
    public static Dict tagEm = null;
    public static Dict tagBig = null;
    public static Dict tagSmall = null;
    public static Dict tagParam = null;
    public static Dict tagOption = null;
    public static Dict tagOptgroup = null;
    public static Dict tagImg = null;
    public static Dict tagMap = null;
    public static Dict tagArea = null;
    public static Dict tagNobr = null;
    public static Dict tagWbr = null;
    public static Dict tagFont = null;
    public static Dict tagSpacer = null;
    public static Dict tagLayer = null;
    public static Dict tagCenter = null;
    public static Dict tagStyle = null;
    public static Dict tagScript = null;
    public static Dict tagNoscript = null;
    public static Dict tagTable = null;
    public static Dict tagCaption = null;
    public static Dict tagForm = null;
    public static Dict tagTextarea = null;
    public static Dict tagBlockquote = null;
    public static Dict tagApplet = null;
    public static Dict tagObject = null;
    public static Dict tagDiv = null;
    public static Dict tagSpan = null;

    public void defineInlineTag( String name )
    {
        install( new Dict( name, Dict.VERS_PROPRIETARY,
                           (Dict.CM_INLINE|Dict.CM_NO_INDENT|Dict.CM_NEW),
                           ParserImpl.getParseBlock(), null ) );
    }

    public void defineBlockTag( String name )
    {
        install( new Dict( name, Dict.VERS_PROPRIETARY,
                           (Dict.CM_BLOCK|Dict.CM_NO_INDENT|Dict.CM_NEW),
                           ParserImpl.getParseBlock(), null ) );
    }

    public void defineEmptyTag(String name)
    {
        install(new Dict(name, Dict.VERS_PROPRIETARY,
                         (Dict.CM_EMPTY|Dict.CM_NO_INDENT|Dict.CM_NEW),
                         ParserImpl.getParseBlock(), null));
    }

    public void definePreTag(String name)
    {
        install(new Dict(name, Dict.VERS_PROPRIETARY,
                         (Dict.CM_BLOCK|Dict.CM_NO_INDENT|Dict.CM_NEW),
                         ParserImpl.getParsePre(), null));
    }

    public static TagTable getDefaultTagTable()
    {
        if ( defaultTagTable == null ) {
            defaultTagTable = new TagTable();
            for ( int i = 0; i < tags.length; i++ ) {
                defaultTagTable.install( tags[i] );
            }
            tagHtml = defaultTagTable.lookup("html");
            tagHead = defaultTagTable.lookup("head");
            tagBody = defaultTagTable.lookup("body");
            tagFrameset = defaultTagTable.lookup("frameset");
            tagFrame = defaultTagTable.lookup("frame");
            tagNoframes = defaultTagTable.lookup("noframes");
            tagMeta = defaultTagTable.lookup("meta");
            tagTitle = defaultTagTable.lookup("title");
            tagBase = defaultTagTable.lookup("base");
            tagHr = defaultTagTable.lookup("hr");
            tagPre = defaultTagTable.lookup("pre");
            tagListing = defaultTagTable.lookup("listing");
            tagH1 = defaultTagTable.lookup("h1");
            tagH2 = defaultTagTable.lookup("h2");
            tagP  = defaultTagTable.lookup("p");
            tagUl = defaultTagTable.lookup("ul");
            tagOl = defaultTagTable.lookup("ol");
            tagDir = defaultTagTable.lookup("dir");
            tagLi = defaultTagTable.lookup("li");
            tagDt = defaultTagTable.lookup("dt");
            tagDd = defaultTagTable.lookup("dd");
            tagDl = defaultTagTable.lookup("dl");
            tagTd = defaultTagTable.lookup("td");
            tagTh = defaultTagTable.lookup("th");
            tagTr = defaultTagTable.lookup("tr");
            tagCol = defaultTagTable.lookup("col");
            tagBr = defaultTagTable.lookup("br");
            tagA = defaultTagTable.lookup("a");
            tagLink = defaultTagTable.lookup("link");
            tagB = defaultTagTable.lookup("b");
            tagI = defaultTagTable.lookup("i");
            tagStrong = defaultTagTable.lookup("strong");
            tagEm = defaultTagTable.lookup("em");
            tagBig = defaultTagTable.lookup("big");
            tagSmall = defaultTagTable.lookup("small");
            tagParam = defaultTagTable.lookup("param");
            tagOption = defaultTagTable.lookup("option");
            tagOptgroup = defaultTagTable.lookup("optgroup");
            tagImg = defaultTagTable.lookup("img");
            tagMap = defaultTagTable.lookup("map");
            tagArea = defaultTagTable.lookup("area");
            tagNobr = defaultTagTable.lookup("nobr");
            tagWbr = defaultTagTable.lookup("wbr");
            tagFont = defaultTagTable.lookup("font");
            tagSpacer = defaultTagTable.lookup("spacer");
            tagLayer = defaultTagTable.lookup("layer");
            tagCenter = defaultTagTable.lookup("center");
            tagStyle = defaultTagTable.lookup("style");
            tagScript = defaultTagTable.lookup("script");
            tagNoscript = defaultTagTable.lookup("noscript");
            tagTable = defaultTagTable.lookup("table");
            tagCaption = defaultTagTable.lookup("caption");
            tagForm = defaultTagTable.lookup("form");
            tagTextarea = defaultTagTable.lookup("textarea");
            tagBlockquote = defaultTagTable.lookup("blockquote");
            tagApplet = defaultTagTable.lookup("applet");
            tagObject = defaultTagTable.lookup("object");
            tagDiv = defaultTagTable.lookup("div");
            tagSpan = defaultTagTable.lookup("span");
        }
        return defaultTagTable;
    }

}
