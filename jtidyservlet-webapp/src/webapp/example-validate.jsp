<%@ include file="include/header.jsp" %>

<%@ page import="org.w3c.tidy.servlet.util.HTMLEncode"%>

<h3>Validate your HTML using JTidy</h3>

<%
    String badHtml = request.getParameter("badHtml");
    if (badHtml == null) {
        badHtml = "";
    }
%>

Enter the HTML Fragmet to be validated as part of this page.

Predefined examples:
<script type="text/javascript">
function setNewHtmlCode() {
    var s = document.getElementById("predefinedHtml");
    var h = document.getElementById("badHtml");
    if ((h == null) || (s == null)) {
        alert("javascript could find required elements on this form");
        return;
    }
    var v = s.options[s.selectedIndex].value;
    var html = "";
    switch (v) {
        case "1": html = "<table>\n <td> td here <\/td>\n <\/tr>\n<\/table>";
                  break;
        case "2": html = "<a href=\" missing closing a";
                  break;
        case "3": html = "Enter Name: <input type=text value=\"Audio & Video\" >";
                  break;
    }
    h.value = html;
}
</script>

<SELECT id="predefinedHtml" onChange="setNewHtmlCode();">
        <OPTION SELECTED value=""></OPTION>
        <OPTION value="0">Clear</OPTION>
        <OPTION value="1">Bad Table</OPTION>
        <OPTION value="2">Bad HREF</OPTION>
        <OPTION value="3">Bad input value</OPTION>
</SELECT>

<form action="<%=request.getRequestURI()%>" method=post>
    <textarea name="badHtml" id="badHtml" rows=10 cols=62><%=HTMLEncode.encode(badHtml).trim()%></textarea>
    <input type=submit value="Try it!">
</form>

Click here to see what was wrong: <jtidy:validationImage/><br>

Start of you HTML fragemt:<br>
<div>
<!-- ## TEST CODE STARTS HERE ### -->
<jtidy:tidy config="show-body-only:true"><%=badHtml%></jtidy:tidy>
<!-- ## TEST CODE ENDS HERE ### -->
</div>
<br>End of you HTML fragemt.<br>


<!-- This will disable tidy Processing since current Tidy is corrupting HTML -->
<jtidy:pass/>
<%@ include file="include/pass.jsp" %>

<%@ include file="include/footer.jsp" %>