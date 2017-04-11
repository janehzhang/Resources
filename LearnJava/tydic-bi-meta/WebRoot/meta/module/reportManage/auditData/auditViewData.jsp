<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  <head>
    <title>审核日志</title>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_TYPE,DATA_TYPE"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/AuditDataAction.js"></script>
    <script type="text/javascript">
     var issueId='<%=request.getParameter("issueId")%>';		//模型id
     var dataPeriod='<%=request.getParameter("dataPeriod")%>';	//按月还是按天
     var auditProp='<%=request.getParameter("auditProp")%>';	//审核模式（全省审还是分公司审）
    </script>
	<script type="text/javascript" src="auditViewData.js"></script>
  </head>
  
<body style="height: 100%;width: 100%;">
</body>
</html>
