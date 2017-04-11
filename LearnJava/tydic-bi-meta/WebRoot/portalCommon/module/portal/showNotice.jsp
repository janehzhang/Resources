<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../public/head.jsp"%>
<%@include file="../../public/include.jsp"%>
<html>
  <head>
    <title>公告信息详情页</title>
	<script type="text/javascript" src="<%=path %>/dwr/interface/PortalCommonCtrlr.js"></script>
	<script type="text/javascript" src="noticeList.js"></script>
	<style type="text/css"> 
	body{margin:0;padding:0 0 12px 0;font-size:12px;line-height:22px;font-family:"宋体","Arial Narrow",HELVETICA;background:#fff;}
form,ul,li,p,h1,h2,h3,h4,h5,h6{margin:0;padding:0;}input,select{font-size:12px;line-height:16px;}img{border:0;}ul,li{list-style-type:none}
	.gonggao{ width:auto;}
	.gonggao li{ height:25px; line-height:25px; list-style:none;}
	.gonggao li span{ height:25px;line-height:25px;float:right; margin-right:10px}
	.gonggao li a:link{ padding-left:16px;height:25px;line-height:25px; text-decoration:none; color:#737576;}
	.gonggao li a:visited{ padding-left:16px;height:25px;line-height:25px; text-decoration:none; color:#737576;}
	.gonggao li a:hover{ padding-left:16px;height:25px;line-height:25px; text-decoration:none; color:#0096ff;}
	.gonggao li a:active{ padding-left:16px;height:25px;line-height:25px; text-decoration:none; color:#0096ff;}
	
	.page{ float:right; margin:10px 20px;}
	.page a:link{ padding:5px 7px; margin:0 5px; text-decoration:none; border:1px solid #d6d6d6; color:#006a92;}
	.page a:visited{ padding:5px 7px; margin:0 5px; text-decoration:none; border:1px solid #3090bc; color:#fff; background:#73b2da;}
	.page a:hover{ padding:5px 7px; margin:0 5px; text-decoration:none; border:1px solid #3090bc;color:#fff; background:#73b2da;}
	.page a:active{ padding:5px 7px; margin:0 5px; text-decoration:none; border:1px solid #d6d6d6; color:#006a92;}
	
	.show_content{ width:528px; margin:5px auto; text-align:center;}
	.gg_title{ font-size:12px; font-weight:bold; color:#0182dd;margin:10px auto;}
	.xian{ width:90%; height:1px; overflow:hidden; background:#0082de;}
	.gg_info{ width:auto; margin-left:15%;}
	.gg_info li{ float:left; list-style:none; height:25px; line-height:25px; text-align:left; color:#9c9b9b; margin-right:25px;}
	.gg_content{ float:left; text-align:left;}
</style>
<%
String groupId = request.getParameter("groupId");
%>
  </head>
  
  <body>
  <div class="show_content">
	<h2 class="gg_title">关于2012年端午节放假的通知</h2>
	<h3 class="xian"></h3>
	<p>
		<ul class="gg_info">
		<li>发布日期：2012-06-04</li>
		<li>公告等级：<span>较急</span></li>
		<li>公告所属：广东省</li>
		</ul>
	</p>
	<p class="clear"></p>
	<p class="gg_content">各位同事：　<br />　
    根据国务院颁布的有关规定，现将我司2012年端午节放假安排通知如下：<br />
6月22日至24日放假，共3天。其中6月23日为端午节法定节假日，原周六公休调至6月22日，<br />
6月24日为周日公休，25日正常上班。请提前做好假期安排，外出旅行的同事请注意旅途安
全。
<br /> 祝阖家幸福安康！
	</p>
</div>
  </body>
</html>
