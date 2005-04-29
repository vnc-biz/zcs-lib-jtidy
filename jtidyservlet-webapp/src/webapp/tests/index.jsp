<%--
  @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com</a>
  @version $Revision$ ($Author$)
--%>

<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insert definition="mainLayout" flush="true">
    <tiles:put name="body.str">

        <h2>JTidy servlet and Struts/tiles Examples</h2>


        <ul>
            <li><a href="testStrutsAction.jsp">testStrutsAction.jsp</a></li>
            <li><a href="test-filter1.jsp">test-filter1.jsp</a></li>
        </ul>
    </tiles:put>
</tiles:insert>