<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>JTidy servlet extension - Live examples</title>
    <meta http-equiv="Expires" content="-1" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Cache-Control" content="no-cache" />
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
    <meta name="author" content="Vlad Skarzhevskyy"/>
    <meta name="email" content="vlads(at)users.sourceforge.net"/>
    <link rel="stylesheet" href="./styles/examples.css" type="text/css" media="all" />
    <style type="text/css" media="all">
          @import url("./styles/maven-base.css");
          @import url("./styles/maven-theme.css");</style>
</head>
<body>

<%@ taglib uri="http://jtidy.sf.net" prefix="jtidy" %>

<div id="banner">
    <a href="http://sourceforge.net" id="organizationLogo">
        <img alt="sourceforge" src="http://sourceforge.net/sflogo.php?group_id=13153&amp;type=2"></a>
    <a href="http://jtidy.sourceforge.net" id="projectLogo">
        <img alt="JTidy Servlet" src="./images/logo.png"></a>
    <div class="clear"><hr></div>
</div>

<div id="breadcrumbs">
    <div class="xleft">
        JTidy servlet extension - Live examples
        <span class="separator">|</span>
        <a href="index.jsp">Examples Home</a>
        <span class="separator">|</span>
        <a href="http://jtidy.homelinux.net/" class="externalLink">Documentation</a>
    </div>
    <div class="clear"><hr></div>
</div>

<div id="backDiv">
    <a href="javascript:history.go(-1)"><img alt="Back" src="./images/back.gif"></a>
</div>

<div id="showsourceDiv">
    <a href="<%=request.getRequestURI()%>.source">View JSP Source</a><br>
    <a href="<%=request.getRequestURI()%>.generatedhtml?requestID=<jtidy:requestID/>">This HTML Source</a>
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