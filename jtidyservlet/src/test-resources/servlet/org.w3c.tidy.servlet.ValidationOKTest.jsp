<%@ taglib uri="/WEB-INF/jtidy-taglib-12.tld" prefix="jtidy" %>
<jtidy:tidy config="show-warnings:yes" validateOnly="true">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2//EN">
<% // @version $Revision$ ($Author$) %>
<html>
  <head>
    <title>Title</title>
  </head>
  <body>
    <table summary="<jtidy:requestID/>">
        <tr>
            <td>Text</td>
        </tr>
    </table>
    <jtidy:validationImage/>
  </body>
</html>
</jtidy:tidy>