<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://jtidy.sf.net" prefix="jtidy" %>
<%@ page import="org.w3c.tidy.servlet.sample.TimeZoneDetection" %>

<%-- Layout component
  parameters : title, body
--%>
<html>
<head>
    <title><tiles:getAsString name="title"/></title>
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta name="author" content="Vlad Skarzhevskyy">
    <meta name="email" content="vlads(at)users.sourceforge.net">
    <link rel="stylesheet" href="../styles/examples.css" type="text/css" media="all">
    <style type="text/css" media="all">
          @import url("../styles/maven-base.css");
          @import url("../styles/maven-theme.css");</style>
    <script type="text/javascript" src="../js/timeZoneDetection.js"></script>
</head>
<body onload="setTimezoneOffsetCookie()">

<div id="banner">
    <a href="http://sourceforge.net" id="organizationLogo">
        <img alt="sourceforge" src="http://sourceforge.net/sflogo.php?group_id=13153&amp;type=2"></a>
    <a href="http://jtidy.sourceforge.net" id="projectLogo">
        <img alt="JTidy Servlet" src="../images/logo.png"></a>
    <div class="clear"><hr></div>
</div>

<div id="breadcrumbs">
    <div class="xleft">
        JTidy servlet extension - Live examples
        <span class="separator">|</span>
        <a href="../index.jsp">Examples Home</a>
        <span class="separator">|</span>
        <a href="http://jtidy.homelinux.net/" class="externalLink">Documentation</a>
    </div>
    <div class="xright">
        RequestID: <jtidy:requestID/> <span class="separator">|</span> Time: <%=TimeZoneDetection.getUserTime(request)%>
    </div>
    <div class="clear"><hr></div>
</div>

<div id="backDiv">
    <a href="javascript:history.go(-1)"><img alt="Back" src="../images/back.gif"></a>
</div>

<div id="showsourceDiv">
    <a href="<%=request.getRequestURI()%>.source">View JSP Source</a><br>
    <jtidy:link report="true" result="true" source="false" text="This HTML Source"/>
</div>

<div id="JTidyValidationImageDiv">

<script type="text/javascript">

function showJTidyReport_iframe() {
    if ((ppJTidyReport != null) && (ppJTidyReport.ppmShow != null)) {
        return ppJTidyReport.ppmShow();
    } else {
        // No frame support or some other error
        return true;
    }
}

</script>

    <jtidy:validationImage onclick="this.blur();return showJTidyReport_iframe();" imgName="JTidyValidationImageCommon" />
    <iframe name="ppJTidyReport"
        id="ppJTidyReport"
        src="./iframe_main.jsp?requestID=<jtidy:requestID/>"
        scrolling="no" frameborder="0" style="visibility:hidden; z-index:999; left:50px; position:absolute;">
        Sory No support for iframe in your browser
    </iframe>
</div>

<div id="body">
    <tiles:insert attribute='body'/>
    <tiles:getAsString name="body.str"/>
</div>

<div id="firefox">
    <a href="http://www.spreadfirefox.com/?q=affiliates&amp;id=0&amp;t=68"><img border="0" alt="Get Firefox!" title="Get Firefox!" src="http://www.spreadfirefox.com/community/images/affiliates/Buttons/88x31/take.gif"/></a>
</div>

<div id="validxhtml">
  <a href="http://validator.w3.org/check?uri=referer">
  <img border="0"
       src="http://www.w3.org/Icons/valid-html401"
       alt="Valid HTML 4.01!" height="31" width="88"></a>
</div>

<div id="footer">
    <div class="xleft">
        Please send any questions or suggestions to
        <a href="mailto:vlads(at)users.sourceforge.net">vlads(at)users.sourceforge.net</a>
    </div>
</div>

</body>
</html>
