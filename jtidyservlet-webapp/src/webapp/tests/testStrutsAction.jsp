<%--
  @author Vlad Skarzhevskyy <a href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com</a>
  @version $Revision$ ($Author$)
--%>

<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

   <tiles:insert page="/testAction.do" >
     <tiles:put name="title" value="Test definition set in action, and action forward to another definition. Title is overloaded from insert" />
       <%-- header and body values come from definition used in action's forward --%>
       <%-- name of definition to use in action --%>
     <tiles:put name="set-definition"   value="layout.test1" />
   </tiles:insert>
