<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html>
  <head>
    <title>报表创建</title>
	<link href="baobiao.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/ReportBuildIndexAction.js"></script>
	<script type="text/javascript" src="reportBuildIndex.js"></script>
	<link href="baobiaoIndex.css" rel="stylesheet" type="text/css" />
	
	
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
  
 <body>
 <div class="over">
<div class="shop">
	<div class="box">
		<span class="name">数据查询：</span>
		<input class="biaodan" type="text"  id="searchInput"/>
        <a class="btn-tj" id="searchButton"></a> 
	</div>
	
	<div class="portlet">
		<div class="box-1" onClick="preview('portlet1')" title="展开/收起">
			<span class="bt">推荐数据<img class="pic-1" src="img/pinter.png" /></span>
			<span class="yj-t-l"></span> <span class="yj-t-r"></span>
			<a class="zhankai"></a>
		</div>
		<div class="box-2" id="portlet1">
			<ul class="content" id="recommendDataContainer" >
				
			</ul>
		</div>
		<div class="box-3">
			<span class="yj-b-l"></span>
			<span class="yj-b-r"></span>
			<a class="more" href="#" onClick="recommendData2More()"></a>
		</div>
	</div>
	
	<div class="portlet">
		<div class="box-1" onClick="preview('portlet2')" title="展开/收起">
			<span class="bt">人气最旺数据<img class="pic-2" src="img/hot.gif" /></span>
			<span class="yj-t-l"></span> <span class="yj-t-r"></span>
			<a class="zhankai"></a>
		</div>
		<div class="box-2" id="portlet2">
			<ul class="content" id="popularDataContainer">
				
			</ul>
		</div>
		<div class="box-3"> 
			<span class="yj-b-l"></span> 
			<span class="yj-b-r"></span> 
			<a class="more" href="#" onClick="popularData2More()"></a> 
		</div>
	</div>
	
	<div class="portlet">
		<div class="box-1" onClick="preview('portlet3')" title="展开/收起">
			<span class="bt">新鲜出炉数据<img class="pic-2" src="img/new.gif" /></span>
			<span class="yj-t-l"></span> <span class="yj-t-r"></span>
			<a class="zhankai"></a>
		</div>
		<div class="box-2" id="portlet3">
			<ul class="content" id="newestDataContainer">
				
			</ul>
		</div>
		<div class="box-3"> 
			<span class="yj-b-l"></span> 
			<span class="yj-b-r"></span> 
			<a class="more" href="#" onClick="newestData2More()"></a> 
		</div>
	</div>
   </div>
</div>
</body>
</html>
