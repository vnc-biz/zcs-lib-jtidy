<%@ taglib uri="/WEB-INF/jtidy-taglib-12.tld" prefix="jtidy" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2//EN">
<% // @version $Revision$ ($Author$) %>
<html>
  <head>
    <title>Title</title>
  </head>
  <body>
    <%
        String sessionRepository = "n/a";
        if (session.getAttribute(org.w3c.tidy.servlet.data.SessionRepositoryFactory.ATTRIBUTE_REPOSITORY) != null)
        {
            sessionRepository = "RepositoryInSession";
        }
    %>
    <table summary="<%=sessionRepository%>">
        <tr>
            <td>Text</td>
        </tr>
    </table>
  </body>
</html>
