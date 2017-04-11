<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<% 
String path = request.getContextPath();
%>
<html>
  <head>
    <title>报表列表</title>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=PERORT_SEND_TYPE"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/RepSupermarketAction.js"></script>
	<script type="text/javascript" src="<%=path %>/portal/resource/js/OPString.js"></script>
	<script type="text/javascript" src="reportList.js"></script>
	<script type="text/javascript" src="favCommon.js"></script>
	<script type="text/javascript" src="../page.js"></script>
	
	<link href="baobiao.css" rel="stylesheet" type="text/css" />	
	<script type="text/javascript">
     	var orderId='<%=request.getParameter("orderId")%>';	//排序方式
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
	<DIV class="listBox"> <SPAN class="name">报表查询：</SPAN>
        <INPUT class="biaodan" type="text" id="searchName" name="searchName" />
        <a id="search" class="btn-tj"></a>
    </DIV>
    <div class="box-List" id="gift">
        <div class="posi" id="menuName"></div>
        <ul class="content" id="showContentList">
        
        </ul>
    </div>
    <div class="list-Page" id="showPage"></div>
    <div class="favDiv" id="favDiv"></div>
    <div class="subDiv" id="subDiv"></div>
</div>
</body>
</html>
