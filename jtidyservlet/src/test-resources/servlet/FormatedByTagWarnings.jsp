<%@ taglib uri="../../tld/jtidy-taglib-12.tld" prefix="jtidy" %>
<jtidy:tidy config="show-warnings:yes" validateOnly="true">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2//EN">
<% // @version $Revision$ ($Author$) %>
<html>
  <head>
    <title>Defalut page that Do Not Pass validation, Used for testing Reports</title>
  </head>
  <body>
    <table summary="<jtidy:requestID/>">
        <!-- tr removed -->
            <td>Text</td>
        </tr>
    </table>
    <jtidy:validationImage/>
  </body>
</html>
</jtidy:tidy>