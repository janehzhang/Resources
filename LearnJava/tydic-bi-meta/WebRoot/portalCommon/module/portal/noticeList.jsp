<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../public/head.jsp"%>
<%@include file="../../public/include.jsp"%>
<html>
  <head>
    <title>公告信息列表页</title>
	<script type="text/javascript" src="<%=path %>/dwr/interface/PortalCommonCtrlr.js"></script>
	<script type="text/javascript" src="openLayer.js"></script>
	<script type="text/javascript" src="noticeList.js"></script>
	<style type="text/css"> 
	body{margin:0;padding:0 0 12px 0;font-size:12px;line-height:22px;font-family:"宋体","Arial Narrow",HELVETICA;background:#fff;}
form,ul,li,p,h1,h2,h3,h4,h5,h6{margin:0;padding:0;}input,select{font-size:12px;line-height:16px;}img{border:0;}ul,li{list-style-type:none}
	.gonggao{ width:auto; margin:30px auto;}
	.gonggao li{ height:auto; line-height:25px; list-style:none; border-bottom:1px dotted #dadada;}
	.gonggao li span{ height:25px;line-height:25px;float:right; margin-right:50px}
	.gonggao li a:link{ padding-left:16px;height:25px;line-height:25px; text-decoration:none; color:#737576;}
	.gonggao li a:visited{ padding-left:16px;height:25px;line-height:25px; text-decoration:none; color:#737576;}
	.gonggao li a:hover{ padding-left:16px;height:25px;line-height:25px; text-decoration:none; color:#0096ff;}
	.gonggao li a:active{ padding-left:16px;height:25px;line-height:25px; text-decoration:none; color:#0096ff;}
	
	.page{ float:right; margin:10px 20px;}
	.page a:link{ padding:5px 7px; margin:0 5px; text-decoration:none; border:1px solid #d6d6d6; color:#006a92;}
	.page a:visited{ padding:5px 7px; margin:0 5px; text-decoration:none; border:1px solid #3090bc; color:#fff; background:#73b2da;}
	.page a:hover{ padding:5px 7px; margin:0 5px; text-decoration:none; border:1px solid #3090bc;color:#fff; background:#73b2da;}
	.page a:active{ padding:5px 7px; margin:0 5px; text-decoration:none; border:1px solid #d6d6d6; color:#006a92;}
	
	.show_content{ width:500px; margin:5px auto; text-align:center;}
	.gg_title{ font-size:12px; font-weight:bold; color:#0182dd;margin:10px auto;}
	.xian{ width:90%; margin:0 auto; height:1px; overflow:hidden; background:#0082de;}
	.gg_info{ width:auto; margin-left:15%;}
	.gg_info li{ float:left; list-style:none; height:25px; line-height:25px; text-align:left; color:#9c9b9b; margin-right:25px;}
	.gg_content{ text-align:left; padding-left:30px; margin-top:10px; }
	.closed{ float:right; display:block; width:50px; height:22px; line-height:25px; text-decoration:none; cursor:pointer; color:#333; margin-top:-5px;}
	.clear{ clear:both;}
</style>
  </head>
  
  <body>
  <form id="queryform" name="queryform">
    <ul class="gonggao" id="noticeListId">
    	<%-- 
		<li><span>[2012-06-07]</span><a href="###">·广东客户服务分析系统一阶段内容2012年5月31日正式上线！</a></li>
		<li><span>[2012-06-07]</span><a href="###">·广东客户服务分析系统一阶段内容2012年5月31日正式上线！</a></li>
		<li><span>[2012-06-07]</span><a href="###">·广东客户服务分析系统一阶段内容2012年5月31日正式上线！</a></li>
		<li><span>[2012-06-07]</span><a href="###">·广东客户服务分析系统一阶段内容2012年5月31日正式上线！</a></li>
		<li><span>[2012-06-07]</span><a href="###">·广东客户服务分析系统一阶段内容2012年5月31日正式上线！</a></li>
		<li><span>[2012-06-07]</span><a href="###">·广东客户服务分析系统一阶段内容2012年5月31日正式上线！</a></li>
		<li><span>[2012-06-07]</span><a href="###">·广东客户服务分析系统一阶段内容2012年5月31日正式上线！</a></li>
		--%>
	</ul>
	<div class="page" id="noticListPageId">
	<%-- 
		<font style="color:#3090bc;">总共3页，每页30条,共90条。</font>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="###">上一页</a><font color='red'>1</font><a href="###">2</a><a href="###">3</a><a href="###">下一页</a>
	--%>
	</div>
	<div class="show_content" id="showMesId">
	</div>
</form>
  </body>
  <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</html>
