<%@ taglib uri="/WEB-INF/jtidy-taglib-12.tld" prefix="jtidy" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2//EN">
<% // @version $Revision$ ($Author$) %>
<html>
  <head>
    <title>JTidy Messages</title>
  </head>
  <body>
    <jtidy:pass/>
    <jtidy:report requestID="<%=request.getParameter("requestID")%>" />
  </body>
</html>
