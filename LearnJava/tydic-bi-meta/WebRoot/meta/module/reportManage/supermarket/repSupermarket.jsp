<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<% 
String path = request.getContextPath();
%>
<html>
  <head>
    <title>报表超市</title>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=PERORT_SEND_TYPE"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/RepSupermarketAction.js"></script>
	<script type="text/javascript" src="<%=path %>/portal/resource/js/OPString.js"></script>
	<script type="text/javascript" src="repSupermarket.js"></script>
	<script type="text/javascript" src="favCommon.js"></script>
	
	<link href="baobiao.css" rel="stylesheet" type="text/css" />
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
			while ( obj &&!(!strTagName || obj.tagName == strTagName.toUpperCase()) && (!strId || obj.id == strId))
  				obj = obj.parentElement
 			return obj
		}
	</script>
	<script language="JavaScript"> 
		function preview(DivId){ 
			if(document.getElementById(DivId).style.display==''){ 
				document.getElementById(DivId).style.display='none'; 
			}else{ 
				document.getElementById(DivId).style.display='';
			} 
		} 
		function download(file){ 
  			window.location.href=file; 
		} 
	</script> 
	
  </head>

<body>
<div class="shop">
 <div class="box"> <span class="name">报表查询：</span>
        <input class="biaodan" type="text" id="searchName" name="searchName" />
        <a id="search" class="btn-tj"></a> </div>
      <div class="box-1" onClick="preview('gift')" title="展开/收起"> <span class="bt">推荐报表<img class="pic-1" src="img/pinter.png" /></span> <span class="yj-t-l"></span> <span class="yj-t-r"></span><a href="#" class="zhankai"></a>
      </div>
      <div class="box-2" id="gift">
        <ul class="content" id="showContentA">
        
        </ul>
      </div>
      <div class="box-3"> <span class="yj-b-l"></span> <span class="yj-b-r"></span> <a class="more" onClick="openList('1')"></a> </div>
      
      <div class="box-1" onClick="preview('gift1')" title="展开/收起"> <span class="bt">人气最旺报表<img class="pic-2" src="img/hot.gif" /></span> <span class="yj-t-l"></span> <span class="yj-t-r"></span> </div>
      <div class="box-2" id="gift1">
        <ul class="content" id="showContentB">
        
        </ul>
      </div>
      <div class="box-3"> <span class="yj-b-l"></span> <span class="yj-b-r"></span> <a class="more" onClick="openList('2')"></a> </div>
       
      <div class="box-1" onClick="preview('gift2')" title="展开/收起"> <span class="bt">新鲜出炉报表<img class="pic-2" src="img/new.gif" /></span> <span class="yj-t-l"></span> <span class="yj-t-r"></span> </div>
      <div class="box-2" id="gift2">
        <ul class="content" id="showContentC">
        
        </ul>
      </div>
      <div class="box-3"> <span class="yj-b-l"></span> <span class="yj-b-r"></span> <a class="more" onClick="openList('3')"></a> </div>
   
     <div class="favDiv" id="favDiv"></div>
     <div class="subDiv" id="subDiv"></div>
</div>
</body>
</html>
