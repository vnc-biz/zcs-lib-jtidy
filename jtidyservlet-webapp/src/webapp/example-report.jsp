<%@ include file="include/header.jsp" %>

<h3>Customize JTidy reports using &lt;jtidy:report&gt; tag</h3>

<%
    String requestID = request.getParameter("requestID");
    if (requestID == null)
    {
        requestID = "last";
    }
%>

    <p>
        JSP Code: <code>&lt;jtidy:report requestID="<%=requestID%>"/&gt;</code> will generate:
    </p>


    <jtidy:report requestID="<%=requestID%>" wrapLen="80"/>

<!-- This will disable tidy Processing since current Tidy is corrupting HTML -->
<jtidy:pass/>
<%@ include file="include/pass.jsp" %>

<%@ include file="include/footer.jsp" %>