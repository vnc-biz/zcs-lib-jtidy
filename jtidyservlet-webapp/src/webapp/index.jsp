<%@ include file="include/header.jsp" %>

<h2>JTidy servlet Examples</h2>

<p>
    The following examples show the functionality of the JTidy tags and Filter. These
    example pages also allow you to view the JSP source, so you can see how you
    might interface with JTidy in your own application.
</p>

<ul>
    <li><a href="example-tidy-tag.jsp">Format JSP page using &lt;jtidy:tidy&gt; tag</a></li>
    <li><a href="example-tidy-filter.jsp">Format JSP page using Filter</a></li>
    <li><a href="example-tidy-html.html">Format HTML page using Filter</a></li>
    <li><a href="example-report.jsp">Customize JTidy reports using &lt;jtidy:report&gt; tag</a></li>
    <li><a href="example-bugs-life.jsp">Simple application with bugs. Show how JTidy could help to identify this bugs</a></li>
    <li><a href="example-validate.jsp">Validate your HTML using JTidy</a></li>
    <li><a href="example-validate-url.jsp">Validate your HTML by URL</a></li>
    <!--
    See how your URL will look like after JTidy. Hard to do I need to change all the image/styls src inside downloaded HTML since it will be shown from my web url.
    -->
</ul>

<p>
    Source code for this application may also be usefull since it is build using JTidy:
</p>

<ul>
    <li><a href="include/header.jsp.source">include/header.jsp</a></li>
    <li><a href="iframe_main.jsp.source">iframe_main.jsp</a></li>
    <li><a href="iframe_body_report.jsp.source">iframe_body_report.jsp</a></li>
    <li><a href="include/footer.jsp.source">include/footer.jsp</a></li>
</ul>

<%@ include file="include/footer.jsp" %>