<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html>
  <head>
    <title>报表下线</title>
	<script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_TYPE,DATA_TYPE"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/ReportOfflineAction.js"></script>
	<script type="text/javascript" src="reportOffline.js"></script>
	<script type="text/javascript">
		var reportId=<%=request.getParameter("reportId")%>;			
	</script>
	<style type="text/css">
		.reportOffline_count{
			margin:10px 5px 5px 10px;
		}
	</style>
  </head>
  
<body style="height: 100%;width: 100%">
</body>
</html>
