<%@ taglib uri="http://jtidy.sf.net" prefix="jtidy" %>

<jtidy:tidy>

<%@ include file="include/header.jsp" %>
<!-- this file contains html <html> <body> tags -->

<h3>Format JSP page using &lt;jtidy:tidy&gt; tag</h3>

    <p>
        To do this:
    </p>
    <ul>
        <li>define tag extension <code>&lt;%@ taglib uri="http://jtidy.sf.net" prefix="jtidy" %&gt;</code> </li>
        <li>the page code should be inside <code>&lt;jtidy:tidy&gt;</code> JSP tag</li>
        <li>optionaly include <code>&lt;jtidy:validationImage/&gt;</code> inside common JSP header to see
            if the page you are testing is well formatted.</li>
    </ul>

<!-- this file contains html </body> </html> tags -->
<%@ include file="include/footer.jsp" %>

</jtidy:tidy>