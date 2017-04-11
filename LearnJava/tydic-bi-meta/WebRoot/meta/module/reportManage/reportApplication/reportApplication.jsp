<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html>
  <head>
    <title>报表应用分析</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/ReportStatisAction.js"></script>
	<script type="text/javascript" src="reportApplication.js"></script>
	<STYLE type=text/css>
		BODY {
			FONT-SIZE: 14px;
		}
		OL LI {
			MARGIN: 8px;
		    
		}
		#con {
			FONT-SIZE: 12px;
			MARGIN: 0px auto;
			WIDTH:100%;
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
			MARGIN:0px 0px 5px 0px;
			WIDTH:100%;
			HEIGHT:24px;
			BORDER:none;
			/*
			POSITION:absolute;
			RIGHT:0px;
			TOP:0px;
		    */
		}
		.name {
			height:24px;
			line-height:24px;
			float:left;
			font-size:12px;
			margin-left:8px;
			_margin-left:4px;
			display:block;
		}
		.biaodan {
			float:left;
			border:1px solid #ABDFE0;
			margin-left:4px;
			_margin-left:2px;
			padding:0px;
			width:90px;
			height:20px;
			line-height:20px;
		}
		.btn-tj {
			float:left;
			background:url(tj.png) no-repeat;
			width:54px;
			height:21px;
			margin-left:20px;
			display:block;
		}
		.btn-tj:hover {
			background:url(tj-h.png) no-repeat;
		}
		.btn-tj:visited {
			background:url(tj-a.png) no-repeat;
		}
		#tabs LI {
			BACKGROUND: url(tag.gif) no-repeat left bottom;
			FLOAT: left;
			MARGIN-RIGHT: 1px;
			LIST-STYLE-TYPE: none;
			HEIGHT: 23px;
		}
		#tabs LI A {
			PADDING-RIGHT: 10px;
			PADDING-LEFT: 10px;
			BACKGROUND: url(yuan.gif) no-repeat right bottom;
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
		#tabs LI.buttom-tab A {
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
/*			BACKGROUND: url(bg.gif) repeat-x; */
			PADDING-BOTTOM: 10px;
			WIDTH:100%;
			COLOR: #474747;
			PADDING-TOP: 10px;
			HEIGHT:250px;
			z-index:-10;
		}
		#tab DIV.buttom-tab {
			DISPLAY: block
		}
	</STYLE>
  </head>
  <body style="height: 100%;width: 100%">
    <div id="con">
  	<UL id="tabs">
      <LI class="buttom-tab"><A onClick="select_zbb('tab0',this)" href="javascript:void(0)">省公司部门</A> </LI>
      <LI><A onClick="select_zbb('tab1',this)" href="javascript:void(0)">分公司部门</A> </LI>
   </UL>
   <DIV id="tab">
      <DIV class="tab buttom-tab" id="tab0"></DIV>
      <DIV class="tab" id="tab1"></DIV>
   </DIV>
  </DIV>
  </body>
</html>
