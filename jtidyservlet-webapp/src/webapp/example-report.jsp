<%@ include file="include/header.jsp" %>

<h3>Customize JTidy reports using &lt;jtidy:report&gt; tag</h3>

<%
    String requestID = "last";
%>

<!-- This will disable tidy Processing since current Tidy is corrupting HTML -->
<jtidy:pass/>

    <p>
        <code>&lt;jtidy:report requestID="last"/&gt;</code> will generate:
    </p>


    <jtidy:report requestID="<%=requestID%>" wrapLen="80"/>

<%@ include file="include/footer.jsp" %>