<%@ taglib uri="/WEB-INF/jtidy-taglib-11.tld" prefix="jtidy" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2//EN">
<html>
  <head>
    <title>JTidy Messages</title>
    <script type="text/javascript" src="./js/jtidyReport.js"></script>
  </head>
  <body onLoad="childLoadSourceFinish('<%=request.getParameter("notifyOnLoadID")%>');">
    <jtidy:pass/>
    <div id="contentsDiv">
        <jtidy:report requestID="<%=request.getParameter("requestID")%>" wrapLen="80"/>
    </div>
  </body>
</html>
