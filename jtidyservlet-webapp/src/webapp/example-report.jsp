<%@ include file="include/header.jsp" %>

<h3>Customize JTidy reports using &lt;jtidy:report&gt; tag</h3>

<%@ page import="org.w3c.tidy.servlet.TidyServletHelper" %>
<%
    String requestID = request.getParameter("requestID");
    if (requestID == null)
    {
        requestID = "last";
    }
    // Conver names like last to real request number so links would work properly
    requestID = TidyServletHelper.getResponseID(requestID, session);
%>

    <p>
    <jtidy:link requestID="<%=requestID%>" report="false" result="false" source="true" text="View original HTML"/> |
    <jtidy:link requestID="<%=requestID%>" report="false" result="true" source="false" text="View result HTML after JTidy"/>
    </p>

    <p>
        JSP Code: <code>&lt;jtidy:report requestID="<%=requestID%>"/&gt;</code> will generate:
    </p>


    <jtidy:report requestID="<%=requestID%>" wrapLen="80"/>

<!-- This will disable tidy Processing since current Tidy is corrupting HTML -->
<jtidy:pass/>
<%@ include file="include/pass.jsp" %>

<%@ include file="include/footer.jsp" %>