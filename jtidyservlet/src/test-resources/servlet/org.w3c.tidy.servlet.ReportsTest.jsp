<%@ taglib uri="/WEB-INF/jtidy-taglib-12.tld" prefix="jtidy" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2//EN">
<% // @version $Revision$ ($Author$) %>
<html>
  <head>
    <title>JTidy Messages</title>
  </head>
  <body>
    <jtidy:pass/>
    <%
        String requestID = request.getParameter("requestID");
    %>
    <jtidy:validationImage requestID="<%=requestID%>" />

    <jtidy:report requestID="<%=requestID%>" wrapSource="true" wrapLen="50" sourceResult="false" />

  </body>
</html>
