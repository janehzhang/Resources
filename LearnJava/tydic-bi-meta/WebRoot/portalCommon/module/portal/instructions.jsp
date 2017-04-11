<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.util.*"%>
<%@include file="../../public/head.jsp"%>
<%@include file="../../public/include.jsp"%>
<html>
  <head>
    <title>指标批示页</title>
	<script type="text/javascript" src="<%=path %>/dwr/interface/PortalCommonCtrlr.js"></script>
	<script type="text/javascript" src="openLayer.js"></script>
	<script type="text/javascript" src="noticeList.js"></script>
	<style type="text/css"> 
	body{margin:0;padding:0 0 12px 0;font-size:12px;line-height:22px;font-family:"宋体","Arial Narrow",HELVETICA;background:#fff;}
	form,ul,li,p,h1,h2,h3,h4,h5,h6{margin:0;padding:0;}input,select{font-size:12px;line-height:16px;}img{border:0;}ul,li{list-style-type:none}.tl{ text-align:right;}
	.ps_main{ width:100%;}
	.ps_main li{ list-style:none; border-bottom:1px solid #e2e2e2; padding:5px;}
	.ps_name{font-size:12px; font-weight:bold; color:#1d668b;}
	.ps_content{ color:#6a6a6a;}
	.ps_date{ color:#a8a8a8;}
	.ps_txt{ width:90%; height:25px; line-height:25px; border:1px solid #e2e2e2; margin:5px auto; padding-left:5px; color:#e2e2e2;}
	.ps_txt_subim{ width:95%; height:100px; line-height:25px; border:1px solid #e2e2e2; margin:5px auto; padding-left:5px; color:#e2e2e2;}
	.ps_ul2{ margin-left:70px;}
	.ps_ul2 li{list-style:none; border-bottom:1px dotted #e2e2e2;}
	.ps_ul3{ margin-left:70px;}
	.ps_ul3 li{list-style:none; border-bottom:none;}
	.btn{ width:56px; height:21px; line-height:21px; text-align:center; background:url(images/btn_link.png) left 0; color:#383838; border-style:none; cursor:pointer;}
	.btn:hover{width:56px; height:21px; line-height:21px; text-align:center; background:url(images/btn_hover.png) no-repeat; color:#383838;border-style:none; cursor:pointer;}
</style>
  </head>
  <%
  	String isWD = request.getParameter("isWD");
  %>
  <body>
    <!--批示-->
<ul class="ps_main">
	<li>
		<span class="ps_name">wenqiang：</span><span class="ps_content">障碍申告量偏高，请查明原因</span>
		<p class="ps_ul2"><span class="ps_date"> 2012-06-07 14:40</span></p>
		<ul class="ps_ul2">
			<li>
				<span class="ps_name">wenqiang：</span><span class="ps_content">障碍申告量偏高，请查明原因</span>
				<p><span class="ps_date">2012-06-07 14:40</span> <a href="###">回复</a></p>
				<ul class="ps_ul3">
					<li>
						<span class="ps_name">wenqiang：</span><span class="ps_content">障碍申告量偏高，请查明原因</span>
						<p><span class="ps_date">2012-06-07 14:40</span> <a href="###">回复</a></p>
						<input type="text" class="ps_txt_subim" value="我也说在句" />
						<p><input name="" type="button" value="发 布" class="btn" /></p>
					</li>
				</ul>
			</li>
			<li>
				<span class="ps_name">ShangHai：</span><span class="ps_content">障碍申告量偏高，请查明原因</span>
				<p><span class="ps_date">2012-06-07 14:40</span> <a href="###">回复</a></p>
			</li>
		</ul>
	</li>
	<li>
		<span class="ps_name">Tobenet：</span><span class="ps_content">障碍申告量偏高，请查明原因</span>
		<p class="ps_ul2"><span class="ps_date">2012-06-07 14:40</span></p>
		<input type="text" class="ps_txt ps_ul2" value="我也说在句" />
	</li>
</ul>
  </body>
  <script type="text/javascript">
  	var isWD = <%=isWD%>;
  </script>
</html>
