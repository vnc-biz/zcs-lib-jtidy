<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
// Created on 17.08.2004 by vlads
//
%>
<html>
<head>
    <title>Internal processing details frame</title>
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta name="author" content="Vlad Skarzhevskyy"/>
    <meta name="email" content="vlads(at)users.sourceforge.net">
    <link rel="stylesheet" href="./styles/examples.css" type="text/css" media="all">
    <style type="text/css" media="all">
          @import url("./styles/maven-base.css");
          @import url("./styles/maven-theme.css");</style>
    <link rel="stylesheet" href="./styles/print.css" type="text/css" media="print">
    <script type="text/javascript" src="./js/jtidyReport.js"></script>
</head>
<body style="margin:0; padding:0;">
<div id="outerDiv" style="border:1px solid #A0C6E5;">
 <table id="outerTable" border=0
        summary="Main table use to format this document"
        cellpadding="1" cellspacing="1">
    <tr>
        <td align=left style="padding-right:5px">
            <img src="./images/cancel.gif"
                 onclick="ppmHide();"
                 border=0 align="left" hspace=3 alt="Close">
        </td>
        <td style="font-family: Arial, Helvetica, sans-serif; font-size: 9pt;">
            Processing details
        </td>
        <td align=right style="padding-right:5px">
            <img src="./images/cancel.gif"
                 onclick="ppmHide();"
                 border=0 align="right" hspace=3 alt="Close">
        </td>
    </tr>
    <tr>
        <td colspan=3>
            <div id="contentsDiv"></div>
            <!-- must have bufferFrame in the body somewhere, empty.html is Hack for LoadRunner -->
            <iframe src="./include/empty.html" style="display:none" name="bufferFrame"></iframe>
        </td>
    </tr>
    <tr>
        <td align=left style="padding-right:5px">
            <img src="./images/cancel.gif"
                 onclick="ppmHide();"
                 border=0 align="left" hspace=3 alt="Close">
        </td>
        <td>
        </td>
        <td align=right style="padding-right:5px">
            <img src="./images/cancel.gif"
                 onclick="ppmHide();"
                 border=0 align="right" hspace=3 alt="Close">
        </td>
    </tr>
 </table>
</div>

<%@ taglib uri="http://jtidy.sf.net" prefix="jtidy" %>
<jtidy:ignore/>

<%
    String sRequestID = request.getParameter("requestID");
    if (sRequestID == null) {
        sRequestID = "0";
    }
%>

<script type="text/javascript">
    currentRequestID = "<%=sRequestID%>";
</script>

</body>
</html>
