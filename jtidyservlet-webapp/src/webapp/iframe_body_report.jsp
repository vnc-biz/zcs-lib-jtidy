<%@ taglib uri="http://jtidy.sf.net" prefix="jtidy" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2//EN">
<html>
  <head>
    <title>JTidy Messages</title>
    <script type="text/javascript" src="./js/jtidyReport.js"></script>
  </head>
  <body onLoad="childLoadSourceFinish('<%=request.getParameter("notifyOnLoadID")%>');">

    <jtidy:pass/>

    <div id="contentsDiv">
        <!-- this is not working in WebLogic -->
        <!-- jtidy:report requestID="<%=request.getParameter("requestID")%>" wrapLen="80"/-->

        <%
            String requestID = request.getParameter("requestID");
        %>
        <jtidy:report requestID="<%=requestID%>" wrapLen="80"/>
    </div>
  </body>
</html>
