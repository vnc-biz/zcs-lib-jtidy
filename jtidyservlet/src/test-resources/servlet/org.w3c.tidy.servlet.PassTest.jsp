<%@ taglib uri="/WEB-INF/jtidy-taglib-12.tld" prefix="jtidy" %>
<jtidy:tidy config="alt-text:Alternate;show-warnings:no">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2//EN">
<% // @version $Revision$ ($Author$) %>
<html>
  <head>
    <title>Test img alt attribute changes</title>
  </head>
  <body>
    <p>Test pass tag</p>
    <img src="" width="2">
    <jtidy:pass/>
  </body>
</html>
</jtidy:tidy>