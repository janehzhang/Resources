<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="../../../public/head.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
String reportId = request.getParameter("reportId");	 //报表id
String showTypeId = "1";		//是否显示头信息(1为是，0为否)
%>
<head>
    <title>打开报表</title>
    <script type="text/javascript" src="reportOpen.js"></script>
	<script type="text/javascript">
	</script>
</head>
  <body>
	  <iframe id="reportOpen" name="reportOpen" align="middle" marginwidth=0 marginheight=0 hspace=0 
	  src="../showReport/showReport.jsp?reportId=<%=reportId %>&showTypeId=<%=showTypeId %>"  
	  frameborder=no width="100%"  height="100%"></iframe>
  </body>
</html>

