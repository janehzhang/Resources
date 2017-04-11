<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html lang="en" xml:lang="en">
<head>
    <title>查看报告</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/WriteReportAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
</head>
	<body style="height: 100%;width: 100%">
    <form id="queryform" name="queryform">
     <input type="hidden" id="monthId" name="monthId">
     <input type="hidden" id="zoneId"  name="zoneId">
	</form>
	<iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
	</body>
</html>
<script type="text/javascript" src="lookReport.js"></script>
