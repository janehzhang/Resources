<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html>
  <head>
    <title>个人应用情况</title>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/ReportStatisAction.js"></script>
	<script type="text/javascript" src="personalApplication.js"></script>
	
	<style type="text/css">
		.personalApp_count{
			margin:10px 5px 5px 10px;
		}
	</style>
  </head>
  
<body style="height: 100%;width: 100%">
	<!--  
	<div id="personalApp" style="height: 100%;width: 100%"></div>
	<div id="personalApp_count" style='display:none;' class="personalApp_count">
		<div style="position:relative;margin:2 2 2 2;display:inline;"><span>日期：</span><select id="data"><option>当天</option><option>本周</option><option>每月</option></select></div>
		<div style="display:inline;"><span>报表分类：</span><select id="reportType"><option>经营分析</option></select></div>
		<div style="display:inline;"><span>报表：</span><input id="report" type='text' value=''/></div>
		<div style="display:inline;"><input id="count" type='button' value='统计'/></div>
	</div>
	-->
</body>
</html>
