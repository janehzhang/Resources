<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html>
  <head>
    <title>搜索关键字统计</title>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/GroupAction.js"></script>
	<script type="text/javascript" src="keywordCount.js"></script>
	
	<style type="text/css">
		.keyword_count{
			margin:10px 5px 5px 10px;
		}
	</style>
  </head>
  
<body style="height: 100%;width: 100%">
	<div id="keywordCotent" style="height: 100%;width: 100%"></div>
	<div id="keyword_count" style='display:none;' class="keyword_count">
		<div style="position:relative;margin:2 2 2 2;display:inline;"><span>日期：</span><input type="text"/><span>--></span><input type="text" /></div>
		<div style="display:inline;"><span>统计范围：</span><select id="coutRange"><option>省公司</option></select></div>
		<div style="display:inline;"><span>关键字：</span><input id="keyword" type='text' value=''/></div>
		<div style="display:inline;"><input id="count" type='button' value='统计'/></div>
	</div>
</body>
</html>
