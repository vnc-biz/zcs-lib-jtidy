<%@ include file="../include/header.jsp" %>

<h3>Test dynamic include and Filter</h3>

<p>
    Start.
</p>

<%
    String include1 = "test-filter1-include1.jsp";
%>
<jsp:include page="<%=include1%>" flush="false" />

<%
    String include2 = "test-filter1-include2-forward.jsp";
    //include2 = "test-filter1-include2-forward2txt.jsp";
%>
<jsp:include page="<%=include2%>" flush="false" />

<p>
    End.
</p>

<%@ include file="../include/footer.jsp" %>