<%
    // Prohibit caching of application pages.
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", -1);
%>

<%@ include file="include/header.jsp" %>

<%@ taglib uri="http://jtidy.sf.net/jtidysamples" prefix="jtidysamples" %>

<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>

<%@ page import="org.apache.commons.httpclient.*" %>
<%@ page import="org.apache.commons.httpclient.methods.*" %>

<%@ page import="org.w3c.tidy.servlet.util.HTMLEncode" %>
<%@ page import="org.w3c.tidy.servlet.TidyServletHelper" %>
<%@ page import="org.w3c.tidy.servlet.sample.SiteSecurityHelper" %>
<%@ page import="org.w3c.tidy.servlet.sample.filter.MultipartRequestWrapper" %>

<h3>Validate URL</h3>

This form allows you to enter the URL of an HTML document to validate using jtidy.
<br>
<%
    final String ATTRIBUTE_USER_URL = "userURL";
    String sessionUrl = (String)session.getAttribute(ATTRIBUTE_USER_URL);
    String paramUrl = MultipartRequestWrapper.getMultipartParameter(request, "url");
    String userURL = paramUrl;
    if (userURL == null)
    {
        userURL = sessionUrl;
    }
    if (userURL == null)
    {
        userURL = "http://";
    }

    String requestID = request.getParameter("requestID");
    if (paramUrl != null)
    {
%>
    <jtidysamples:TransactionToken verify="true" message="<B class='error'>Error: Session Token required, try again.</B>">
<%
        HttpMethod httpMmethod = null;
        try
        {
            SiteSecurityHelper.verifyUrl(paramUrl, request);
            session.setAttribute(ATTRIBUTE_USER_URL, paramUrl);
            requestID = null;
            InputStream in;
            /*
                URL url = new URL(paramUrl);
                // Open a HTTP connection to the URL usnig default java implemntation
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                in = conn.getInputStream();
             */
            // Use Jakarta HttpClient
            HttpClient client = new HttpClient();
            httpMmethod = new GetMethod(paramUrl);
            int statusCode = client.executeMethod(httpMmethod);
            in = httpMmethod.getResponseBodyAsStream();

            String newRequestID = TidyServletHelper.process(in, session);
            response.sendRedirect(response.encodeRedirectURL(SiteSecurityHelper.getApplicationURL(request) + request.getRequestURI() + "?requestID=" + newRequestID));
            return;
        }
        catch (MalformedURLException e)
        {
            out.print("<B class=\"error\">Error: malformed URL " + HTMLEncode.encode(e.getMessage()) + "</B>");
        }
        catch (UnknownHostException e)
        {
            out.print("<B class=\"error\">Error: unknown host " + HTMLEncode.encode(e.getMessage()) + "</B>");
        }
        catch (IOException e)
        {
            out.print("<B class=\"error\">Error: IO Error " + HTMLEncode.encode(e.getMessage()) + "</B>");
        } finally {
            // Release the connection.
            if (httpMmethod != null)
            {
                httpMmethod.releaseConnection();
            }
        }
%>
    </jtidysamples:TransactionToken>
<%
    }

    // Use multipart/form-data and Security atributes to avoid demo system abuse
%>
<br>
<form name="urlForm" method="post" action="<%=request.getRequestURI()%>" enctype="multipart/form-data">

    URL:<br/>

    <input type="text" name="url" value="<%=HTMLEncode.encode(userURL)%>" size="50"><br/>

    <jtidysamples:TransactionToken/>

    <input type="submit" value="Validate">

</form>
<br>

<%
    if (requestID != null)
    {
%>
    User URL: <%=HTMLEncode.encode(sessionUrl)%>
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