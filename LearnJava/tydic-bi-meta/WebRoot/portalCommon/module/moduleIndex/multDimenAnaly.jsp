<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>客户投诉多维智能分析</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../public/head.jsp"%>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
    <link href="css/lyz.calendar.css" rel="stylesheet" type="text/css" />
    <link type="text/css" rel="stylesheet" href="<%=rootPath%>/portalCommon/module/moduleIndex/css/index.css">
    <link type="text/css" rel="stylesheet" href="<%=rootPath%>/portalCommon/module/moduleIndex/css/jquery_pop.css"   charset="utf-8">
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/MenuVisitLogAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/portalCommon/module/moduleIndex/css/jquery-1.7.2.min.js"></script>
	<script src="<%=rootPath%>/js/lyz.calendar.min.js" type="text/javascript"></script>

    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/MultDimenAnalyAction.js"  charset="utf-8"></script>
    <!--<script type="text/javascript" src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>-->
    
    <!-- 下拉宽多选插件 -->
	<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/mutiselect/multiselectSrc/jquery.multiselect.css" />
	<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/mutiselect/assets/style.css" />
	<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/mutiselect/assets/prettify.css" />
	<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/mutiselect/jquery-ui.min.css" />
	<script type="text/javascript" src="<%=rootPath%>/js/mutiselect/jquery-ui.min.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/js/mutiselect/ui/jquery.ui.core.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/js/mutiselect/ui/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/js/mutiselect/assets/prettify.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/js/mutiselect/multiselectSrc/jquery.multiselect.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/js/organictabs.jquery.js"></script>
	      
	<script type="text/javascript" src="<%=rootPath%>/portalCommon/module/moduleIndex/fusionCharts/js/fusioncharts.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/portalCommon/module/moduleIndex/fusionCharts/js/themes/fusioncharts.theme.fint.js"></script>
	<script type="text/javascript" src="multCommon.js"></script>
	<script type="text/javascript" src="secondViewAnaly.js"></script>
	<script type="text/javascript" src="thirdViewAnaly.js"></script>
	<script type="text/javascript" src="fifthViewAnaly.js"></script>
	<script type="text/javascript" src="firstViewAnaly.js"></script>
	<script type="text/javascript" src="fourthViewAnaly.js"></script>	
	<script type="text/javascript" src="multViewAnaly.js"></script>
	
	<%
		Map<String,Object> userInfo=(Map<String,Object>)session.getAttribute(LoginConstant.SESSION_KEY_USER);
		String userNameen = userInfo.get("userNameen").toString();
		String userNamecn = userInfo.get("userNamecn").toString();
		out.println(
	                "<script type=\"text/javascript\">var userNameen = '"+userNameen+"'; var userNamecn = '"+userNamecn+"';</script>"
        );
	 %>
	 
	<style type="text/css">
		*{margin:0;padding:0;}
		.header_line {
			width:99.7%;
			line-height:100%;
			margin-left:0px;
			margin-right:0px;
			color:#000000;
			background-color:#a6c8ed !important;
		}
		span{
			margin-left:1px;
		}
		.timer{
			margin-left:27%;
		}
		font{
			margin-left:2000px;
			font-size:34px;
		}
		.menu{
			width:99.7%;
			height:99%;
			margin-left:0px;
			margin-right:0px;
		}
		.tab{
			text-align:left;
			margin-left:2px;
		}
		.tab_div{
			margin-left:0%;
			margin-top:0%;
			width:33%;
			height:200px;
			text-align:center;
			background-color:#999999;
		}
		.tab_div_product{
			margin-left:0.5%;
			margin-top:0%;
			width:32%;
			height:200px;
			text-align:center;
			background-color:#999999;
		}
		.tab_div_performance{
			margin-left:0.5%;
			margin-top:0%;
			width:33%;
			height:200px;
			text-align:center;
			background-color:#999999;
		}
		.tab_div_customer_property{
			margin-left:0%;
			margin-top:1%;
			width:33%;
			height:200px;
			text-align:center;
			background-color:#999999;
		}
		.tab_div_examine{
			margin-left:0.5%;
			margin-top:1%;
			width:32%;
			height:200px;
			text-align:center;
			background-color:#999999;
		}
		.tab_div_personality{
			margin-left:0.5%;
			margin-top:1%;
			width:33%;
			height:200px;
			text-align:center;
			background-color:#999999;
		}
		.tab_div_t{
			width:100%;
			height:23px;
			text-align:center;
			background-color:#d3e7ff;
			border-bottom:solid 1px #999999;
		}
		.full_div{
			width:100%;
			height:100%;
			text-align:center;
			background-color:#d3e7ff;
		}
		.tab_div_c{
			position:relative;
			width:100%;
			height:100%;
			text-align:center;
			background-color:#d3e7ff;
		}
		.tab_div_u{
			width:100%;
			height:13%;
		}
		.tab_div_b{
			width:100%;
			height:87%;
		}
		.tab_div_c_up{
			width:100%;
			height:36%;
			text-align:center;
			background-color:#d3e7ff;
		}
		.tab_div_c_down{
			width:100%;
			height:64%;
			text-align:center;
			background-color:#d3e7ff;
		}
		.tab_div_d{
			width:100%;
			height:50%;
			text-align:center;
		}
		.tab_div_4_2{
			width:55%;
			height:100%;
			text-align:center;
		}
		.tab_div_4_1{
			width:45%;
			height:100%;
			text-align:center;
			background-color:#d3e7ff;
		}
		.clear{
			clear:both;		
		}
		table{
			margin-left:0%;
		}
		.td_1{
			margin-left:6%;
			color:#000000;
			font-size:11px;
		}
		.td_2{
			margin-left:6%;
			font-size:10px;
			width:60px;
		}
		.button_1{
			color:#07639d;
			height:20px;
			width:20px;
			float:right;
			margin-top:0px;
			border:solid 1px #d3e7ff;
		}
		.title_img{
			width:151px;
			height:21px;
			line-height:100%;
			margin-top:1px;
			vertical-align: middle;
		}
		.magnifier1_pic{
			height:21px;
			width:20px;
		}
		.radio_map{}
		.radio_pie{}
		.radio_asse{}
		
		#main_div_id .nav{
			overflow:hidden
		}
		#main_div_id .nav li{
			width:97px;
			float:left;
			margin:0 10px 0 0
		}
		#main_div_id .nav li.last{
			margin-right:0.5%
		}
		#main_div_id .nav li a{
			display:block;
			padding:5px;
			background:#07639d;
			color:white;
			font-size:10px;
			text-align:center;
			border:0
		}
		#main_div_id li a.current,#main_div_id li a.current:hover{
			background-color:#d3e7ff !important;
			color:black
		}
		#main_div_id .nav li a:hover, #main_div_id .nav li a:focus{
			background:#999
		}
		
		.checkbox{position: relative; width: 45%;height: 200px;float: left;background-color: #d3e7ff;}
		.checkbox ul{margin-top: 4px;}
		.checkbox li{margin-bottom:5px;}
		input{cursor: pointer;}
		.btn_n {
			position: relative;
			cursor: pointer;
			display: inline-block;
			vertical-align: middle;
			font-size: 11px;
			height: 26px;
			line-height: 24px;
			width: 50px;
			float: right;
			margin:-3px 4px 3px 0;
			text-align: center;
			text-decoration: none;
			border-radius: 2px;
			border: 1px solid #3079ED;
			color: #F3F7FC;
			background:#1060e4;
			background-color: #4D90FE;
			background: -webkit-linear-gradient(top, #4D90FE, #4787ED);
			background: -moz-linear-gradient(top, #4D90FE, #4787ED);
			background: linear-gradient(top, #4D90FE, #4787ED);
		}		
		.btn_n:hover {
			border-color:#2F5BB7;
			color:#000000;
			background-color: #4D90FE;
			background: -webkit-linear-gradient(top, #4D90FE, #357AE8);
			background: -moz-linear-gradient(top, #4D90FE, #357AE8);
			background: linear-gradient(top, #4D90FE, #357AE8);
		}	
		.btn_n:active{
			box-shadow:#2176D3 0 1px 2px 0 inset;
			border-color: #3079ED;
		}
		.btn_n:focus{
			border-color:#4d90fe;
			outline:none
		}
		
		.value_1{text-align: right;margin-right:10px;}
		.value_4{text-align: left;padding:24px 3px 5px 10px;}
		.value_4 li{padding-bottom:5px; }
		.value_7{text-align: left;margin-left:11px;padding-top: 31px;}
		.value_7 li{padding-bottom: 5px;}
		.value_8{text-align: right;float: left; margin-left:25px;padding-top: 8px;font-size: 13px;}
		.value_8 li{padding-bottom: 5px;}
		.value_1{text-align: right;float: left; margin-left:25px;padding-top: 8px;font-size: 13px;}
		.value_1 li{padding-bottom: 3px;}
		.true_false{text-align: right;float: left; margin-left:25px; margin-top:25px; }
		.true_false li{padding-bottom: 5px;}
		.mark_value3{
			position: absolute;
			left: 10px;
			top: 10px;
			width: 300px;
			height: 200px;
			text-align:left;
			background: #fff;
			border:1px solid #000;
			box-shadow: 0 0 5px #666;
			z-index: 999;

    	}
    	.mark_value4{
			position: absolute;
			left: 10px;
			top: 20px;
			width: 160px;
			height: 140px;
			background: #fff;
			border:1px solid #000;
			box-shadow: 0 0 5px #666;
			z-index: 999;
    	}
    	.mark_value8{
			position: absolute;
			left: 5px;
			top: 5px;
			width: 230px;
			height: 200px;
			background: #fff;
			border:1px solid #000;
			box-shadow: 0 0 5px #666;
			z-index: 999;
    	}
    	.mark_value7{
    		position: absolute;
			left: 10px;
			top: 20px;
			width: 160px;
			height: 140px;
			background: #fff;
			border:1px solid #000;
			box-shadow: 0 0 5px #666;
			z-index: 999;
    	}
    	.mark_value1{
    		position: absolute;
			left: 5px;
			top: 5px;
			width: 200px;
			height: 210px;
			background: #fff;
			border:1px solid #000;
			box-shadow: 0 0 5px #666;
			z-index: 999;
    	}
		.mark_value2{
			position: absolute;
			left: 10px;
			top: 20px;
			width: 160px;
			height: 140px;
			background: #fff;
			border:1px solid #000;
			box-shadow: 0 0 5px #666;
			z-index: 999;
		}
		 .mark_value5{
			position: absolute;
			left: 10px;
			top: 20px;
			width: 160px;
			height: 140px;
			background: #fff;
			border:1px solid #000;
			box-shadow: 0 0 5px #666;
			z-index: 999;
		}
		 .mark_value6{
			position: absolute;
			left: 10px;
			top: 20px;
			width: 160px;
			height: 140px;
			background: #fff;
			border:1px solid #000;
			box-shadow: 0 0 5px #666;
			z-index: 999;
		}
    	.value_3{margin-top:12px; margin-left:20px; color: #000;}
    	.value_3 li{margin-bottom:8px;}
    	.title_bar{position: relative; padding: 6px;border-bottom: 1px solid #ddd;}
    	.title_bar .bar_close{float: right;color: #999;text-shadow: 0 1px 0 #ddd;font: bold 14px/14px simsun;text-decoration: none;cursor: pointer;padding-right: 5px;}
		
		.rate_select{
			color:#07639d;
			height:20px;
			width:50px;
			float:right;
			margin-top:1px;
			margin-left:1px;
			border:solid 1px #d3e7ff;
		}
	</style>

	<script type="text/javascript">
		$(function() {
			// 调用插件
			$("#main_div_id").organicTabs({
				"speed": 100
			});
		})
					
</script>
	
  </head>
  
  <body>
    <div class="header_line">
    	<span><label>可视化投诉管理视图</label></span> 
		 <span>|</span> 
		 <span id="ods_link"><label>高级统计分析报表</label></span>
		 <span class="timer"><label>统计时间</label></span>
		 <span>
		 	<select id="selectDate">
				<option value="mon">上月</option>
				<option value="week">上周</option>
				<option value="yest">昨日</option>
				<option value="autoDefine">自定义时间</option>
			</select>
		 </span>
		 <span style="visibility:hidden" ><label id="startTime">开始时间</label></span>
		 <span style="visibility:hidden"><input id="txtBeginDate"  /></span>
		 <span style="visibility:hidden"><label id="endTime">结束时间</label></span>
		 <span style="visibility:hidden"><input id="txtEndDate"  /></span>
		 <input style="visibility:hidden" type="button" value="查询" id="queryData">
		 <span style="visibility:hidden" ><input type="text" name="Name" id="date" value="mon"></span>
	</div>
	<div class="menu" style="background-color:#A6C8ED;" id="main_div_id">
		<div>
		<ul class="nav">
			<li class="nav-one"><a href="#div1" class="current">本地投诉</a></li>
			<li class="nav-two" style="display:none"><a href="#div2">越级投诉</a></li>
		</ul>
		</div>
		<div class="clear"></div>
		<!--二级菜单-->
	    <div class="menu_con">
	        <div id="div1" class="tab">
	            <div style="display:inline">
		            <div class="tab_div" style="float:left;" id="p_1">
		           		<div class="tab_div_t" id="p_1_1">
							<input type="image" title="显示全部" value="▽" src="images/magnifier1.png" class="button_1" id="img_hm"/>
							<input type="image" title="返回上一层" value="▽" src="images/return.png" class="button_1"  id="imgView1" />
							<img alt="号码归属地域维度" src="images/hmgs.png" class="title_img">
						</div>
						<div class="tab_div_c" id="p_1_2">
							<div class="tab_div_u" id="p_1_2_1">
								<input type="radio" name="map" value="2" style="margin-top:8px;margin-left:5px;" class="radio_map">
								<label style="color:#000000; font-size:11px;text-align:center;margin-top:8px;">按地市</label>
								<input type="radio" name="map" value="1" style="margin-top:8px;" checked="checked" class="radio_map">
								<label  style="color:#000000; font-size:11px;text-align:center;margin-top:8px;">按片区</label>
								<select style="font-size:11px;text-align:center;width:70px;margin-left:5px;" id="map" >
									<option value="0">请选择</option>
		            				<option value="2">珠三角1</option>
		            				<option value="3">珠三角2</option>
		            				<option value="4">粤东</option>
		            				<option value="5">粤西</option>
		            				<option value="6">粤北</option>
		            			</select>
								<label>投诉率单位:‰</label>
							</div>
							<div class="tab_div_b" id="p_1_2_2">
								<img src="./images/chart_loading.gif" align="middle" height="96px" width="100px"  alt="数据加载中..." />
							</div>
						</div>
		            </div>
					<div class="tab_div_product" style="float:left;" id="p_2">
						<div class="tab_div_t" id="p_2_1" >						
							<input type="image" title="显示全部" value="▽" src="images/magnifier1.png" class="button_1"  id="img1" />
							<input type="image" title="返回上一层" value="▽" src="images/return.png" class="button_1"  id="imgView2" />
							<span>
							 	<select id="rate_prod" class="rate_select">
									<option value="0">---</option>
									<option value="1">同比</option>
									<option value="2">环比</option>
								</select>
							</span>
							<!--<label style="float:right;">占比单位:%</label>-->	
							<img alt="产品维度" src="images/cpwd.png" class="title_img">
						</div>
						<div class="tab_div_c" id="p_5_2">
		            	<div class="tab_div_u" id="p_1_2_1">
								<label>占比单位:%</label>
						</div>
						<!--<div class="tab_div_c" id="p_2_2">-->
						<div class="tab_div_b" id="p_2_2">
							<img src="./images/chart_loading.gif" align="middle" height="96px" width="100px"  alt="数据加载中..." />
						</div>
						</div>
					</div>
					<div class="tab_div_performance" style="float:left;" id="p_3">
						<div class="tab_div_t" id="p_3_1">
							<input type="image" title="显示全部" value="▽" src="images/magnifier1.png" class="button_1" id="img2"/>
							<input type="image" title="返回上一层" value="▽" src="images/return.png" class="button_1"  id="imgView3" />
							<span>
							 	<select id="rate_busi" class="rate_select">
									<option value="0">---</option>
									<option value="1">同比</option>
									<option value="2">环比</option>
								</select>
							</span>
							<!--<label style="float:right;">占比单位:%</label>-->
							<img alt="表象维度" src="images/bxwd.png" class="title_img" style="margin-left:45px;" >
						</div>
						<div class="tab_div_c" id="p_5_2">
		            	<div class="tab_div_u" id="p_1_2_1">
								<label>占比单位:%</label>
						</div>
		            	<!--<div class="tab_div_c" id="p_3_2">把投诉率单位移到图中前使用的div样式-->
		            	<div class="tab_div_b" id="p_3_2">
		            		<img src="./images/chart_loading.gif" align="middle" height="96px" width="100px"  alt="数据加载中..." />
		            	</div>
		            	</div>
					</div>
				</div>
				<div class="clear"></div>
				<div style="margin-top:15px;">
					<div class="tab_div_customer_property" style="float:left;" id="p_4">
						<div class="tab_div_t" id="p_4_1">
							<img alt="客户属性" src="images/khsx.png" class="title_img">
						</div>
						<div class="clear"></div>
		            	<div class="tab_div_c" id="p_4_2">

	<div class="checkbox">
		<ul style="float: left;padding-left:1px;text-align: left; ">
			<li><input type="radio" name="field＿name" value="8" class="radio_pie" checked="checked"><label class="td_1">六大市场</label></li>
			<li><input type="radio" name="field＿name" value="1" class="radio_pie" ><label class="td_1">星级等级</label></li>
			<li><input type="radio" name="field＿name" value="2" class="radio_pie" ><label class="td_1">是否4G用户</label></li>
			<li><input type="radio" name="field＿name" value="3" class="radio_pie" ><label class="td_1">光宽带类型</label></li>
			<li><input type="radio" name="field＿name" value="4" class="radio_pie" ><label class="td_1">付费类型</label></li>
			<li><input type="radio" name="field＿name" value="5" class="radio_pie" ><label class="td_1">是否光带用户</label></li>
			<li><input type="radio" name="field＿name" value="6" class="radio_pie" ><label class="td_1">是否智能终端</label></li>
			<li><input type="radio" name="field＿name" value="7" class="radio_pie" ><label class="td_1">服务分群</label></li>
		</ul>
		<input class="btn_n choose" type="button" value="二级筛选" style="width: 59px; margin-top:80px;margin-right:25px; ">
	</div>




	<div class="mark_value8 smc" style="display: none;">
		<div class="title_bar">
			<a href="javascript:;" title="关闭" class="bar_close">X</a>
			<h4>六大市场</h4>
		</div>
		<ul class="value_8 v_cho">
			<li>校园市场 <input type="checkbox" name="val_8" value="校园市场"></input></li>
            <li>农村市场 <input type="checkbox" name="val_8" value="农村市场"></input></li>
            <li>大客户 <input type="checkbox" name="val_8" value="大客户"></input></li>
            <li>商业客户 <input type="checkbox" name="val_8" value="商业客户"></input></li>
            <li>城市家庭 <input type="checkbox" name="val_8" value="城市家庭"></input></li>
            <li>流动市场 <input type="checkbox" name="val_8" value="流动市场"></input></li>
		</ul>
		<input class="btn_n select" type="button" value="查询" style="top: 95px;right: 20px;">
	</div>

	<div class="mark_value1 smc" style="display: none;">
		<div class="title_bar">
			<a href="javascript:;" title="关闭" class="bar_close">X</a>
			<h4>星级等级</h4>
		</div>
		<ul class="value_1 v_cho">
			<li>1星  <input type="checkbox" name="val_1" value="1星"></input></li>
            <li>2星  <input type="checkbox" name="val_1" value="2星"></input></li>
            <li>3星  <input type="checkbox" name="val_1" value="3星"></input></li>
            <li>4星  <input type="checkbox" name="val_1" value="4星"></input></li>
            <li>5星  <input type="checkbox" name="val_1" value="5星"></input></li>
            <li>6星  <input type="checkbox" name="val_1" value="6星"></input></li>
            <li>7星  <input type="checkbox" name="val_1" value="7星"></input></li>           
		</ul>
		<input class="btn_n select" type="button" value="查询" style="top: 100px;right: 20px;">
	</div>

	<div class="mark_value2 smc" style="display: none;">
		<div class="title_bar">
			<a href="javascript:;" title="关闭" class="bar_close">X</a>
			<h4>是否4G用户</h4>
		</div>
		<ul class="true_false v_cho">
			<li>是  <input type="checkbox" name="val_2" value="1"></input></li>
			<li>否 <input type="checkbox" name="val_2" value="0"></input></li>
		</ul>
		<input class="btn_n select" type="button" value="查询" style="top: 43px;right: 10px;">
	</div>
	
	<div class="mark_value5 smc" style="display: none;">
		<div class="title_bar">
			<a href="javascript:;" title="关闭" class="bar_close">X</a>
			<h4>是否光宽用户</h4>
		</div>
		<ul class="true_false v_cho">
			<li>是  <input type="checkbox" name="val_5" value="1"></input></li>
			<li>否 <input type="checkbox" name="val_5" value="0"></input></li>
		</ul>
		<input class="btn_n select" type="button" value="查询" style="top: 43px;right: 10px;">
	</div>
	
	<div class="mark_value6 smc" style="display: none;">
		<div class="title_bar">
			<a href="javascript:;" title="关闭" class="bar_close">X</a>
			<h4>是否智能终端</h4>
		</div>
		<ul class="true_false v_cho">
			<li>是  <input type="checkbox" name="val_6" value="1"></input></li>
			<li>否 <input type="checkbox" name="val_6" value="0"></input></li>
		</ul>
		<input class="btn_n select" type="button" value="查询" style="top: 43px;right: 10px;">
	</div>

	<div class="mark_value3 smc" style="display: none;">
		<div class="title_bar">
			<a href="javascript:;" title="关闭" class="bar_close">X</a>
			<h4>光宽带类型</h4>
		</div>
		<ul class="value_3" style="float: left;width: 110px;">
			<li><input type="checkbox" name="val_3" value="FTTO(拨号)"> FTTO(拨号)</input></li>
            <li><input type="checkbox" name="val_3" value="FTTO(专线)"> FTTO(专线)</input></li>
            <li><input type="checkbox" name="val_3" value="FTTH(拨号)"> FTTH(拨号)</input></li>
            <li><input type="checkbox" name="val_3" value="FTTH(专线"> FTTH(专线)</input></li>
            <li><input type="checkbox" name="val_3" value="IP城域网(专线)"> IP城域网(专线)</input></li>
        </ul>
        <ul class="value_3" style="margin-left:130px;" >
            <li><input type="checkbox" name="val_3" value="FTTA(拨号)"> FTTA(拨号)</input></li>
            <li><input type="checkbox" name="val_3" value="FTTB+LAN(拨号)"> FTTB+LAN(拨号)</input></li>
            <li><input type="checkbox" name="val_3" value="FTTB+LAN(专线)"> FTTB+LAN(专线)</input></li>
            <li><input type="checkbox" name="val_3" value="其他"> 其他</input></li>
            <li><input class="btn_n select" type="button" value="查询" style="
            margin-right:65px; "></li>
		</ul>
	</div>

	<div class="mark_value4 smc" style="display: none;">
		<div class="title_bar">
			<a href="javascript:;" title="关闭" class="bar_close">X</a>
			<h4>付费类型</h4>
		</div>
		<ul class="value_4 v_cho">
			<li>预付费 <input type="checkbox" name="val_4" value="预付费"></input></li>
            <li>后付费 <input type="checkbox" name="val_4" value="后付费"></input></li>      
		</ul>
		<input class="btn_n select" type="button" value="查询" style="bottom:40px; right: 10px;">
	</div>

	<div class="mark_value7 smc" style="display: none;">
		<div class="title_bar">
			<a href="javascript:;" title="关闭" class="bar_close">X</a>
			<h4>服务分群</h4>
		</div>
		<ul class="value_7 v_cho">
			<li>政企 <input type="checkbox" name="val_7" value="政企"></input></li>
            <li>公众 <input type="checkbox" name="val_7" value="公众"></input></li>      
		</ul>
		<input class="btn_n select" type="button" value="查询" style="bottom:30px; right: 10px;">
	</div>

							<div class="tab_div_4_2" style="float:left;" id="p_4_2_2">
								<img src="./images/chart_loading.gif" align="middle" height="96px" width="100px"  alt="数据加载中..." />
							</div>
		            	</div>
						
					</div>
					<div class="tab_div_examine" style="float:left;" id="p_5">
						<div class="tab_div_t" id="p_5_1">
							<input type="image" title="显示全部" value="▽" src="images/magnifier1.png" class="button_1" id="img_khwd"/>
						    <input type="image" title="返回上一层" value="▽" src="images/return.png" class="button_1"  id="imgView5" />
							<span>
							 	<select id="rate_asse" class="rate_select">
									<option value="0">---</option>
									<option value="1">同比</option>
									<option value="2">环比</option>
								</select>
							</span>
							<img alt="考核维度" src="images/khwd.png" class="title_img">
						</div>
						
						<div class="tab_div_c" id="p_5_2">
							<div class="tab_div_u" id="p_5_2_1">
								<input type="radio" name="asse" value="1" style="margin-top:8px;" checked="checked" class="radio_asse">
								<label  style="color:#000000; font-size:11px;text-align:center;margin-top:8px;">按省公司</label>
								<input type="radio" name="asse" value="2" style="margin-top:8px;margin-left:5px;" class="radio_asse">
								<label style="color:#000000; font-size:11px;text-align:center;margin-top:8px;">按地市</label>
								<label style="margin-left:15px;">投诉率单位:‰</label>
							</div>
							<div class="tab_div_b" id="p_5_2_2">
								<img src="./images/chart_loading.gif" align="middle" height="96px" width="100px"  alt="数据加载中..." />
							</div>
						</div>
					</div>
					<div class="tab_div_personality" style="float:left;" id="p_6">
						<div class="tab_div_t" id="p_6_1">
							<img alt="个性维度" src="images/gxwd.png" class="title_img">
						</div>
						<div class="tab_div_c" id="p_6_2">
							
						</div>
					</div>
				</div>
	         </div> 
	        <div id="div2" class="tab" style="background-color:#d3e7ff;with:100%;height:100%">
	            
	         </div>  
		</div>
	</div>
	<div id="pop" class="pop_main">
		<div class="theme-popover" id="pop1">
		     <div class="theme-poptit">
		          <a href="javascript:;" title="关闭" class="close">×</a>
		          <h3 id="title">标题</h3>
		     </div>
		     <div class="theme-popbod dform" id="detail">
		          <img src="./images/chart_loading.gif" align="middle" height="96px" width="100px"  alt="数据加载中..." />
		     </div>
		</div>
		<div class="theme-popover-mask" id="pop2"></div>
	</div>
  </body>
</html>
