<%@ taglib uri="../../tld/jtidy-taglib-12.tld" prefix="jtidy" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2//EN">
<% // @version $Revision$ ($Author$) %>
<html>
  <head>
    <title>JTidy Messages</title>
  </head>
  <body>
    <jtidy:ignore/>
    <%
        String requestID = request.getParameter("requestID");
    %>
    <jtidy:validationImage requestID="<%=requestID%>" />

    <jtidy:report requestID="<%=requestID%>" wrapSource="true" wrapLen="60" source="false" result="true" />

  </body>
</html>
