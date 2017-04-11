<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html>
  <head>
    <title>推荐数据</title>
	<link href="baobiao.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/ReportBuildIndexAction.js"></script>
	<script type="text/javascript" src="recommendData2More.js"></script>
	<link href="<%=rootPath%>/meta/module/reportManage/supermarket/baobiao.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=rootPath%>/meta/module/reportManage/page.js"></script>
	<link href="<%=rootPath%>/meta/module/reportManage/supermarket/baobiao.css" rel="stylesheet" type="text/css" />	
	<script type="text/javascript">
		function mouseOver(id){
			document.getElementById(id).style.display = "block"
		}
		function mouseOut(id){
			document.getElementById(id).style.display = "none"
		}
	</script>

	<script language=javascript for=aho event=onclick>
 		var srcElem = findParentObj(event.srcElement, "a")
		srcElem.runtimeStyle.background ? srcElem.runtimeStyle.removeAttribute("background") : srcElem.runtimeStyle.background = "url(img/rss.png) left top no-repeat"
	</script>
	<script>
	function findParentObj(obj, strTagName, strId){
		while ( obj &&
    		!(!strTagName || obj.tagName == strTagName.toUpperCase()) &&
     		(!strId || obj.id == strId)
		)
  		obj = obj.parentElement
 		return obj
	}
	</script>
	<script language="JavaScript"> 
	function preview(relationId) { 
		if(document.getElementById(relationId).style.display=='') { 
			document.getElementById(relationId).style.display='none'; 
		}else { 
			document.getElementById(relationId).style.display=''; 
		} 
	} 
	function download(file){ 
  		window.location.href=file; 
	} 
	</script> 

</head>
  
 <body style="overflow:auto;">
<div class="shop">
	<div class="box">
		<span class="name" style="margin-left: 61px;">模型数据查询：</span>
		<input class="biaodan" type="text"  id="searchInput"/>
        <a class="btn-tj" id="searchButton"></a> 
	</div>
	
	<div class="portlet">
		<div class="box-2" id="portlet1" style="border: 0;">
			<ul class="content" id="recommendDataContainer" >
				
			</ul>
		</div>
	</div>
	<div class="list-Page" id="showPage"></div>
    <div class="favDiv" id="favDiv"></div>
    <div class="subDiv" id="subDiv"></div>
</div>
</body>
</html>
