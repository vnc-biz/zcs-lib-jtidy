<%@ include file="include/header.jsp" %>

<%@ page import="org.w3c.tidy.servlet.util.HTMLEncode" %>
<%@ page import="org.w3c.tidy.servlet.TidyServletHelper" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="org.w3c.tidy.servlet.sample.filter.MultipartRequestWrapper" %>

<h3>Validate by File Upload</h3>

This form allows you to upload files from your computer and have them validated using jtidy

<form name="uploadForm" method="post" action="<%=request.getRequestURI()%>" enctype="multipart/form-data">

    Please select the file that you would like to upload:<br/>

    <input type="file" name="htmlFile" value="" size="30"><br/>

    <input type="submit" value="Validate this file">

</form>

<%
    FileItem f = MultipartRequestWrapper.getFileItem(request, "htmlFile");
    if (f != null)
    {
        if (f.getSize() == 0)
        {
            out.print("<B class=\"error\">Received file '" + HTMLEncode.encode(f.getName()) + "' is empty </B>");
        }
        else
        {
            String newRequestID = TidyServletHelper.process(f.getInputStream(), session);
            response.sendRedirect(response.encodeRedirectURL(request.getRequestURI() + "?requestID=" + newRequestID));
            return;
        }
    }
%>
<%
    String requestID = request.getParameter("requestID");
    if (requestID != null)
    {
%>
    <p>
        <jtidy:link requestID="<%=requestID%>" report="false" result="false" source="true" text="View original HTML"/> |
        <jtidy:link requestID="<%=requestID%>" report="false" result="true" source="false" text="View result HTML after JTidy"/>
    </p>

    <jtidy:report requestID="<%=requestID%>" wrapLen="80"/>
<%
    }
%>

<!-- This will disable tidy Processing since current Tidy is corrupting HTML -->
<jtidy:pass/>
<%@ include file="include/pass.jsp" %>


<%@ include file="include/footer.jsp" %>