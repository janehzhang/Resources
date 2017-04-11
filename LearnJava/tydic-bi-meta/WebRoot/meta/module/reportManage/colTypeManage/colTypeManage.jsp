<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html>
  <head>
    <title>字段分类管理</title>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/GroupAction.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/ColTypeAction.js"></script>
	<script type="text/javascript" src="colTypeManage.js"></script>

  </head>
  
<body style="height: 100%;width: 100%">
<div style="display:none;" id="filed">
	<div><span>名称：</span><input type='text' id="colTypeName"/></div>
</div>
</body>
</html>
