<%@ include file="include/header.jsp" %>

<h3>Validate by File Upload</h3>

This form allows you to upload files from your computer and have them validated using jtidy

<form name="uploadForm" method="post" action="" enctype="multipart/form-data">

    Please select the file that you would like to upload:<br/>

    <input type="file" name="htmlFile" value="" size="30"><br/>

    <input type="submit" value="Validate this file">

</form>

<%@ include file="include/footer.jsp" %>