<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html>
  <head>
    <title>我收藏的报表</title>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/CollectAction.js"></script>
	<script type="text/javascript" src="myCollectReport.js"></script>
	<style type="text/css">
    	body,a,span,ul,li,div,img {
			margin:0px;
			padding:0px;
			list-style:none;
			border:none;
		}
		ol li {
			MARGIN: 8px;
		
		}
		#con {
			FONT-SIZE: 12px;
			MARGIN: 10px auto;
			WIDTH:98%;
		}
		#tabs {
			PADDING-RIGHT: 0px;
			PADDING-LEFT: 0px;
			PADDING-BOTTOM: 0px;
			MARGIN: 0px 0px 0px 10px;
			WIDTH:100%;
			PADDING-TOP: 0px;
			HEIGHT: 23px;
		}
		.box {
			PADDING:0px;
			MARGIN:0px 0px 20px 0px;
			WIDTH:100%;
			HEIGHT:34px;
			BORDER:none;
			/*
			POSITION:absolute;
			RIGHT:0px;
			TOP:0px;
		    */
		}
		.name {
			height:28px;
			line-height:28px;
			float:left;
			font-size:14px;
			margin-left:8px;
			_margin-left:4px;
			display:block;
		}
		.biaodan {
			float:left;
			border:1px solid #abc1df;
			background:#fafcfe;
			margin-left:10%;
			_margin-left:5%;
			padding:0px;
			width:45%;
			height:28px;
			line-height:28px;
			font-size:16px;
		}
		.btn-tj {
			float:left;
			background: url(img/ss-button.png)  no-repeat;
			width:68px;
			height:30px;
			margin-left:20px;
			display:block;
		}
		
		.btn-tj:hover {
			background:url(img/ss-button-h.png) no-repeat;
			width:68px;
			height:30px;
		}
		
		/*
		.btn-tj:visited {
			background:url(img/ss-button.png) no-repeat;
			width:68px;
			height:30px;
		}
		*/
		#tabs li {
			BACKGROUND: url(img/tag.gif) no-repeat left bottom;
			FLOAT: left;
			MARGIN-RIGHT: 1px;
			LIST-STYLE-TYPE: none;
			HEIGHT: 23px
		}
		#tabs li a {
			PADDING-RIGHT: 10px;
			PADDING-LEFT: 10px;
			BACKGROUND: url(img/yuan.gif) no-repeat right bottom;
			FLOAT: left;
			PADDING-BOTTOM: 0px;
			COLOR: #999;
			LINE-HEIGHT: 23px;
			PADDING-TOP: 0px;
			HEIGHT: 23px;
			TEXT-DECORATION: none
		}
		#tabs LI.emptyTag {
			BACKGROUND: none transparent scroll repeat 0% 0%;
			WIDTH: 4px
		}
		#tabs LI.buttom-tab {
			BACKGROUND-POSITION: left top;
			MARGIN-BOTTOM: -2px;
			POSITION: relative;
			HEIGHT: 25px
		}
		#tabs li.buttom-tab a {
			BACKGROUND-POSITION: right top;
			COLOR: #000;
			LINE-HEIGHT: 25px;
			HEIGHT: 25px
		}
		#tab {
			BORDER-RIGHT: #aecbd4 1px solid;
			PADDING-RIGHT: 1px;
			BORDER-TOP: #aecbd4 1px solid;
			PADDING-LEFT: 1px;
			PADDING-BOTTOM: 1px;
			BORDER-LEFT: #aecbd4 1px solid;
			PADDING-TOP:1px;
			BORDER-BOTTOM: #aecbd4 1px solid;
			BACKGROUND-COLOR: #fff
		}
		.tab {
			PADDING-RIGHT: 10px;
			DISPLAY: none;
			PADDING-LEFT: 10px;
			PADDING-BOTTOM: 10px;
			WIDTH:90%;
			COLOR: #474747;
			PADDING-TOP: 10px;
			min-height:50px;
			margin:0px auto;
		}
		#tab div.buttom-tab {
			DISPLAY: block
		}
		/*tab---end*/
		div.box-1 {
			width:99%;
			height:26px;
			background:url(img/top-bg.png) repeat-x;
			display:block;
			position:relative;
			margin-top:20px;
		}
		div.box-11 {
			width:99%;
			height:26px;
			background:url(img/top-bg.png) repeat-x;
			display:block;
			position:relative;
			margin-top:0px;
		}
		span.bt {
			font-size:14px;
			font-family:"宋体";
			width:150px;
			height:26px;
			line-height:26px;
			display:block;
			margin-left:10px;
			_margin-left:5px;
			position:relative;
		}
		img.pic-1 {
			width:22px;
			height:18px;
			position:absolute;
			right:68px;
			top:3px;
		}
		img.pic-2 {
			width:22px;
			height:9px;
			position:absolute;
			right:40px;
			top:5px;
			
		}
		span.yj-t-l {
			position:absolute;
			left:0px;
			top:0px;
			width:5px;
			height:26px;
			background:url(img/yj-top-left.jpg) no-repeat;
		}
		span.yj-t-r {
			position:absolute;
			right:-2px;
			top:0px;
			width:5px;
			height:26px;
			background:url(img/yj-top-right.jpg) no-repeat;
		}
		a.cjbb {
			font-size:12px;
			text-decoration:none;
			color:#3571bb;
			float:left;
			margin-left:20px;
			display:block;
			margin-top:19px;
			_margin-left:9px;
		}
		a.cjzb {
			font-size:12px;
			text-decoration:none;
			color:#3571bb;
			float:left;
			margin-left:10px;
			display:block;
			margin-top:19px;
			_margin-top:9px;
		}
		a.cjbb:hover {
			color:#01a8ff;
		}
		a.cjzb:hover {
			color:#01a8ff;
		}
		div.tips {
			width:238px;
			height:30px;
			position:absolute;
			right:0px;
			top:10px;
			font-size:12px;
			line-height:30px;
			display:none;
		}
		a.qxsc {
			width:200px;
			height:20px;
			background:url(img/rss-h.png) left top no-repeat;
			display:block;
			float:left;
			color:#3571bb;
			line-height:20px;
		}
		a.sc1 {
			background:url(img/sc.png) no-repeat;
			width:16px;
			height:16px;
			display:block;
			text-decoration:none;
			float:left;
			margin-left:10px;
		}
		a.sc1:hover {
			background:url(img/sc-h.png) no-repeat;
		}
		input.cheb-1 {
			padding:0px;
			width:14px;
			height:14px;
			border:1px solid #3571bb;
			margin-left:5px;
			margin-right:5px;
		}
		div.box-2 {
			width:99%;
			border-left:1px solid #afbccd;
			border-right:1px solid #afbccd;
		}
		ul.content {
			width:90%;
			margin:0px auto;
			display:block;
		}
		li.content-1 {
			width:100%;
			padding-top:10px;
			position:relative;
		}
		li.content-11 {
			width:100%;
			padding-top:10px;
			padding-bottom:10px;
			position:relative;
			border-bottom: 1px #3571bb dashed;
		}
		span.content-name {
			height:20px;
			line-height:20px;
			padding-right:5px;
			font-size:14px;
		
		
		}
		span.content-num {
			width:40%;
			font-size:12px;
			height:20px;
			line-height:20px;
		}
		a.cjb {
			width:50px;
			height:20px;
			line-height:20px;
			font-size:12px;
			position:absolute;
			right:0px;
			top:10px;
			color:#3571bb;
			text-decoration:none;
		}
		a.cjb:hover {
			color:#01a8ff;
		}
		a.cjb1 {
			height:20px;
			line-height:20px;
			font-size:12px;
			position:absolute;
			right:0px;
			top:10px;
			color:#3571bb;
			text-decoration:none;
		}
		a.cjb1:hover {
			color:#01a8ff;
		}
		a.cjb2 {
			height:20px;
			line-height:20px;
			font-size:12px;
			position:absolute;
			right:40px;
			top:10px;
			color:#3571bb;
			text-decoration:none;
		}
		a.cjb2:hover {
			color:#01a8ff;
		}
		a.cjb3 {
			height:20px;
			line-height:20px;
			font-size:12px;
			position:absolute;
			right:60px;
			top:10px;
			color:#3571bb;
			text-decoration:none;
		}
		a.cjb3:hover {
			color:#01a8ff;
		}
		a.sc {
			background:url(img/sc.png) no-repeat;
			width:16px;
			height:16px;
			display:block;
			position:absolute;
			right:0px;
			top:15px;
			text-decoration:none;
		}
		a.sc:hover {
			background:url(img/sc-h.png) no-repeat;
		}
		li.content-2 {
			width:100%;
			border-bottom: 1px #3571bb dashed;
			height:20px;
			color:#3571bb;
		}
		a.content-text {
			width:80%;
			text-overflow:ellipsis; 
			white-space:nowrap;
		    overflow:hidden;
			color:#3571bb;
			display:block;
			font-size:12px;
			padding-top:5px;
		}
		a.content-text:hover {
			color:#01a8ff;
		}
		div.box-3 {
			width:99%;
			height:36px;
			background:url(img/bottom-bg.png)  repeat-x;
			display:block;
			position:relative;
		}
		span.yj-b-l {
			position:absolute;
			left:0px;
			top:0px;
			width:5px;
			height:36px;
			background:url(img/yj-bottom-left.jpg) no-repeat;
		}
		span.yj-b-r {
			position:absolute;
			right:-2px;
			top:0px;
			width:5px;
			height:36px;
			background:url(img/yj-bottom-right.jpg) no-repeat;
		}
		a.more {
			position:absolute;
			right:20px;
			bottom:5px;
			font-size:12px;
			color:#666666;
			text-decoration:none;
		}
		a.more:hover {
			color:#01a8ff;
		}
		/*shop*/
		div.shop {
			width:90%;
			margin:20px auto;
		}
		div.shop1 {
			width:90%;
			margin:0px auto;
		}
		div.left-menu {
			width:20%;
			float:left;
			min-height:400px;
			border:1px solid #abc1df;
		}
		div.bbdy {
			width:70%;
			float:left;
			margin-left:2%;
		}
    </style>
  </head>

<body>
<div class="shop">
 <div class="box">
<%-- 搜索输入框 --%>
   <input class="biaodan" type="text"  />
<%-- 查询按钮 --%>
   <a href="#" class="btn-tj" id="search"></a>
   <a class="cjbb" href="#" >创建报表</a>
   <a class="cjzb" href="#" >创建指标</a>
 </div>
<%-- 收藏夹树 --%>
	<form action="" id="collectTreeForm">
      <div class="left-menu" id="collectTree">
      	<a class="cjzb" href="#" id="clearFavorite">整理收藏夹</a>
      </div>
    </form> 
<%-- 显示报表区域 --%>
      <div class="bbdy" >
      <div class="box-11"> <span class="bt">我收藏的报表</span> <span class="yj-t-l"></span> <span class="yj-t-r"></span> 
      </div>
        <div class="box-2"  style="height:550px;overflow:auto;">
          <ul class="content" id="repMsg">
<%--			<li class="content-11" onmouseover="mouseOver('b1')" onmouseout="mouseOut('b1')" style="display:none;" >--%>
<%--				<span class="content-name">用户流量分群情况</span> --%>
<%--	          	<span class="content-num">（岗位应用数：232）</span>--%>
<%--				 选择发送方式 --%>
<%--	          	<div class="tips"  id="b1" >--%>
<%--		          	<a class="qxsc" >--%>
<%--			          	<span style="margin-left:15px;display:inline;">--%>
<%--			          	     （<input class="cheb-1" value="邮件" type=checkbox id="aho" />邮件--%>
<%--			          		<input class="cheb-1" value="彩信" type=checkbox id="aho" />彩信--%>
<%--			          		<input class="cheb-1" value="邮件" type=checkbox id="aho" />短信）--%>
<%--			          	</span>--%>
<%--		          	</a>--%>
<%--					<a class="sc1" href="#"></a>--%>
<%--          		</div>--%>
<%--          		<a class="content-text" href="#">产生流量手机用户情况（用户数、经营收入ARU值产生流量手机用户情况（用户数、经营收入ARPU值</a>--%>
<%--          	</li>--%>
          </ul>
        </div>
      <div class="box-3"> <span class="yj-b-l"></span> <span class="yj-b-r"></span></div>
</div>
   
</div>
</body>
</html>

