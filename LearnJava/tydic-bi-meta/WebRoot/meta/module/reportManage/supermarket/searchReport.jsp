<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@page import="java.net.URLDecoder"%>
<%@include file="../../../public/head.jsp" %>
<% 
String path = request.getContextPath();
%>
<html>
  <head>
    <title>报表搜索</title>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=PERORT_SEND_TYPE"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/RepSupermarketAction.js"></script>
	<script type="text/javascript" src="<%=path %>/portal/resource/js/OPString.js"></script>
	<script type="text/javascript" src="searchReport.js"></script>
	<script type="text/javascript" src="favCommon.js"></script>
	<script type="text/javascript" src="../page.js"></script>
	
	<link href="baobiao.css" rel="stylesheet" type="text/css" />
	
	<script type="text/javascript">
     	var searchName='<%=URLDecoder.decode(request.getParameter("searchName"),"UTF-8")%>';	//搜索条件
	</script>
	<script type="text/javascript">
		function mouseOver(id){
			document.getElementById(id).style.display = "block"
		}
		function mouseOut(id){
			document.getElementById(id).style.display = "none"
		}
	</script>

 
	
  </head>

<body>
<div class="shop">
 <DIV class="searchBox"> <SPAN class="name">报表查询：</SPAN>
        <INPUT class="biaodan" type="text" id="searchName" name="searchName" />
        <a onClick="searchList()" class="btn-tj"></a> </DIV>
      <div class="box-search" id="gift">
        <div class="noListShow" id="noListShow">未找到需要查询的数据，请重新搜索！</div>
        <ul class="content" id="showContentSearch">
        
        </ul>
      </div>
      <div class="list-Page" id="showPage"></div>
     <div class="favDiv" id="favDiv"></div>
     <div class="subDiv" id="subDiv"></div>
</div>
</body>
</html>
