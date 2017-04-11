<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<title>首页内容</title>
		<%@include file="../../public/head.jsp"%>
		<%@include file="../../public/include.jsp"%>
		<script type="text/javascript" src="<%=path %>/dwr/interface/PortalCtrlr.js"></script>
		<script type="text/javascript" src="<%=path %>/dwr/interface/MenuVisitLogAction.js"></script>
		<script type="text/javascript" src="<%=path %>/portal/resource/js/TableMouse.js"></script>
		<script type="text/javascript" src="<%=path %>/portal/resource/js/formatNumber.js"></script>
		<script type="text/javascript" src="<%=path %>/portal/resource/js/Drag.js"></script>
		<script type="text/javascript" src="<%=path %>/portal/resource/js/FusionCharts.js"></script>
		<link href="images/tab.css" rel="stylesheet" type="text/css" />

		<link type="text/css" rel="stylesheet" href="<%=path %>/portal/resource/css/base_portal.css">
		<style type="text/css">
			.bt{
				background-image: url(./images/bt_bg.jpg);
			 	text-align: middle;
				padding-top: 3px;
				border:0;
			}
			.groupLine
			{
			background-image:url(images/line_bot.gif);
			}
			.pad5
			{
				padding-left:5px;
				padding-right:5px;
			}
			.pad10
			{
				padding-left:10px;
				padding-right:10px;
			}
			DIV.Head_TD{	
				font-family: "宋体";
				font-size: 9pt;
				color: #000000;
				text-decoration: none;
				line-height: 20px;
				background-color:#D0E8FF;
				font-weight:bold;
				color: #000000;	
			}
			.Head_TD 
			{	
				font-family: "宋体";
				font-size: 9pt;
				color: #000000;
				text-decoration: none;
				line-height: 20px;
				background-color:#D0E8FF;
				font-weight:bold;
				color: #000000;	
			}
			.Row_TD
			{
				font-family: "宋体";
				font-size: 9pt;
				color: #000000;
				text-decoration: none;
				line-height: 20px;
			    background-color:#ffffff;
			}
			DIV.Row_TD
			{
				font-family: "宋体";
				font-size: 9pt;
				color: #000000;
				text-decoration: none;
				line-height: 20px;
			    background-color:#FAFBFF;
			}
			.Alt_Row_TD{
				font-family: "宋体";
				font-size: 9pt;
				color: #000000;
				text-decoration: none;
				line-height: 20px;
				background-color:White;
			}
			DIV.Alt_Row_TD
			{
				font-family: "宋体";
				font-size: 9pt;
				color: #000000;
				text-decoration: none;
				line-height: 20px;
				background-color:White;
			}
			.red
			{
				font-family: "宋体";
				font-size: 9pt;
				color: #FF0000;
			}
			.unl
			{
				text-decoration: underline;
				color:#0000FF;
				cursor:pointer;
			}
				.data_grid {

			    }
			    .data_grid td{
			    }
			    .data_grid th{
			        border-color: #eee;
			        text-align: center;
			    }
			    .css_tabTitle
				{
				 font-size:12px;
				 padding:0px;
				 margin:0px;
				 padding-top:5px;
				}
		</style>
	</head>
	<body scroll=no  >
		<div id="div_src"
			style="position: absolute; width: 100%; height:100%; z-index: 1000; top: 0px; left: 0px;
			background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:scroll;" 
			 title="" class="dragBar"  >
         <center style="color: red;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;" >数据加载中，请稍等....</center>
  </div>
  <div id="tab_tip_div"  style="width:200px;padding-right: 30px;float:right;" >日期：20110816 本地网：四川</div>  
		<table border="0" width=100% height=100 cellspacing="1" cellpadding="1" width=100% bgcolor="#88B0BE">
			<tr>
				<td width="210px" bgcolor="#FFFFFF" valign=top id="left_td" >
					<table border="0" width=100% height=100% cellspacing="1" cellpadding="1" width=100% bgcolor="#88B0BE" >
						<tr>
							<td height=180 bgcolor="#FFFFFF" style="padding-top: 5px;">
									<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" 
									codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0"
									width="210" height="180" id="myFlash">
									<param name="allowScriptAccess" value="always" />
									<param name="movie" value="map.swf" />
									<param name="src" value="map.swf" />
									<param name="quality" value="high" />
									<param name="WMode" value="Transparent">
									<param name="AllowScriptAccess" value="always">
									<param name="name" value="myFlash">
									<param name="type" value="application/x-shockwave-flash">
									<param name="pluginspage"
										value="http://www.macromedia.com/go/getflashplayer">
									<embed src="map.swf" quality="high" width="210" height="180" WMode="Transparent" name="myFlash" allowScriptAccess="always" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
								</object>
							</td>
						</tr>
						<tr>
							<td bgcolor="#FFFFFF" height=40px id="flash_legend" style="border: 1px solid #75b8cf;">
								<table width=100% >
								<tr>
								<td colspan=1 id='flegend_index' style='padding-left: 10px;' height=20 >指标：</td></tr>
							<!-- 	<tr>
								<td width=15% style='padding-left: 10px;'>
								 <div id="valueIdBg1" style="width: 12px; height: 12px; background: #00CC00 ; overflow: hidden;display:"></div>
								</td>
								<td width=35% nowrap> 前1-3名
								</td>
								<td width=15% >
								 <div  id="valueIdBg2" style="width: 12px; height: 12px; background: #0068B0 ; overflow: hidden;display:"></div>
								</td>
								<td width=35% nowrap> 中游地市
								</td>
								</tr><tr>
								<td width=15% style='padding-left: 10px;'>
								 <div  id="valueIdBg3" style="width: 12px; height: 12px; background: #FFCC00 ; overflow: hidden;display:"></div>
								</td>
								<td width=35% nowrap> 倒数4-6名
								</td> 
								<td width=5% >
								 <div  id="valueIdBg4" style="width: 12px; height: 12px; background: #FF0000 ; overflow: hidden;display:"></div>
								</td>
								<td width=35% nowrap>后3名
								</td>
								</tr>
								-->
								<tr><td id="index_order_area" style='padding-left: 10px;' height=20></td></tr>
								</table>
							</td>
						</tr>

						<tr>
						  <td 
								style="vertical-align: top; padding-top: 10px;padding-left: 10px;" align="center" 
								bgcolor="#FFFFFF" align="center">
								<div id="_dateDiv" style="position:relative;top:0px;left:0px;">

								</div>
								

							</td>
						</tr>
					</table>
				</td>
				<td valign=top>
					<!-- 右边内容 -->
					<div id="col_comment" 
						style="position:relative;  
						 20px; padding-left: 1px; padding-top: 0px; font-size: 12px;overflow-x:hidden; overflow-y:scroll;">
						<table border="0" width=98% cellspacing="0" id="table_rpt"
							cellpadding="1"   bgcolor="#88B0BE">
							<tr>
								<td bgcolor="#FFFFFF" valign=top height=70 valign='top' >
									<table border="0" width=100% cellspacing="1" id="table_rpt"
										cellpadding="1"
										style="background: #b9dff7 url(./images/title-bg.gif) repeat-x 0 -250px; border: 1px solid #75b8cf;">
										<tr>
											<td height=25px style="padding-left: 10px;">
												公告
											</td>
											<td style="padding-left: 10px;border-left: 2px solid #75b8cf;" id="index_exp_title">
												指标解释
											</td>
										</tr>
										<tr>
											<td bgcolor="#FFFFFF" valign=top width=50%
												style="padding-left: 5px; vertical-align: top;">
												<marquee scrollAmount=1 width=100% height=50
													style="line-height: 125%;" direction=up id="scroll"
													onmousemove="this.stop()" onmouseout="this.start()">
													<div id="notice"></div>
												</marquee>
											</td>
											<td bgcolor="#FFFFFF"
												style="padding-top: 5px; padding-left: 10px;border-left: 2px solid #75b8cf;" valign=top>
												<div id="index_exp_div"></div>

											</td>
										</tr>
									</table>
									
								</td>
							</tr>

							<tr>
								<td bgcolor="#FFFFFF" height=300px
									style="padding-top: 0px;vertical-align: top;" id=rpt_tabbar_td >
									<div id=rpt_tabbar  style="width:100%; height:300px;" >
									</div>
								</td>
							</tr>
							<tr>
								<td height="25px" style="padding-left:10px;background-image: url(./images/index_title_bg.jpg);" id="index_title" width=100% >
								</td>
							</tr>							
							<tr>
								<td bgcolor="#FFFFFF" height=100>
									<div id=chartdiv ></div>
									<div style="position: relative;top:170px,left:0px;width:100%;height:20px;z-index:9999;align=right" align="right" >
								时间段选择：
								  <input name="chartDateRadio" type=radio value="1" checked id="chartDateRadio1" onClick="chartDateChg()" /><label for="chartDateRadio1" >一个月</label>
								<input name="chartDateRadio" type=radio value="2"   id="chartDateRadio2"  onClick="chartDateChg()"/><label for="chartDateRadio2" >二个月</label>
								<input name="chartDateRadio" type=radio value="3"   id="chartDateRadio3"  onClick="chartDateChg()" />
								<label for="chartDateRadio3" >三个月</label> 
								</div>
									
								</td>
							</tr>
						</table>
					</div>
				</td>
				<td width=8px bgcolor="#A6C8ED" style="cursor: hand;"
					onclick="changeRemarkDisplay('remark_td')">
					<img src="./images/frame_to_left.jpg" id="hand_img" />
				</td>
				<td width=150px bgcolor="#FFFFFF" valign=top id='remark_td' style="display:none">

					--
				</td>
			</tr>
		</table>
	</body>
</html>

<script type="text/javascript">
 //alert(window.parent.tabHeight+"_"+window.parent.tabWidth);
var tabHeight,tabWidth;
if(window.parent&&window.parent.tabHeight&&window.parent.tabHeight&&tabWidth)
{
	tabHeight=window.parent.tabHeight;
	tabWidth=window.parent.tabWidth;
}
else
{
 	tabHeight=$("div_src").offsetHeight;
	tabWidth=$("div_src").offsetWidth;
}
	//alert(tabHeight+"  "+$("div_src").offsetWidth+"  "+$("div_src").offsetHeight)

var col_comment=$("col_comment");
var table_rpt=$("table_rpt");
var flash_legend=$("flash_legend");
var remarkWidth=150;//$("remark_td").offsetWidth;
col_comment.style.overflowX ="auto";//hidden
col_comment.style.height=(tabHeight)+"px";
var leftWidth=$("left_td").offsetWidth;
col_comment.style.width=(tabWidth-leftWidth)+"px";
$("table_rpt").style.width=(col_comment.offsetWidth-20)+"px";

var menuTab=new dhtmlXTabBar("rpt_tabbar","top");
menuTab.setSkin('dhx_skyblue');
menuTab.setImagePath("../../../meta/resource/dhtmlx/imgs/");
menuTab.setHrefMode("ajax-html");
menuTab.enableTabCloseButton(false);
menuTab.enableScroll(false);


function changeRemarkDisplay(td)
 {return;
 	if($(td).style.display!="none")
 	{ 
 		$(td).style.display="none";
 		$(hand_img).src="./images/frame_to_left.jpg";
 		col_comment.style.width=(tabWidth-leftWidth)+"px";
  	}
 	else
 	{
 		$(td).style.display="block";
 		$(hand_img).src="./images/frame_to_right.jpg";
 		col_comment.style.width=(tabWidth-leftWidth-remarkWidth)+"px";
  	}
	table_rpt.style.width=(col_comment.offsetWidth-20)+"px";
	menuTab.setSize(table_rpt.offsetWidth,table_rpt.offsetHeight);
 } 
</script>


<script type="text/javascript">
//全局用户数据
var dataLocalCode=userInfo['localCode'];
//alert("dataLocalCode="+dataLocalCode);
var dataAreaId=userInfo['areaId'];
var dataDateNo="20110821";	//数据时间
var dataDateNoBak="";
var moduleMenu={};	//menu_name:{TAB_ID:5,TAB_NAME:'XX',url:'xxx',dataDateNo:xx,dataLocalCode:xx,dataAreaId:xx,}
var userIndexMenuId=1;	//	用户首页菜单ID 
var currentTab="";
var report_level_id=1;
var user_define_id=0;
var loadSucceed=false;
var chartFieldName=""; //字段名
var chartIndexCd="";//图形字段名
var chartRadio="1";//时间连续类型
var init_rpt_level_id=1;

var mapColors=['0x00CC00','0xF4F3F0','0xFFCC00','0xF46F42'];
//$("valueIdBg1").style.backgroundColor=mapColors[0].replace('0x','#'); 
//$("valueIdBg2").style.backgroundColor=mapColors[1].replace('0x','#'); 
//$("valueIdBg3").style.backgroundColor=mapColors[2].replace('0x','#'); 
//$("valueIdBg4").style.backgroundColor=mapColors[3].replace('0x','#'); 

if(dataLocalCode=='0000')
report_level_id=1;
else if(dataAreaId!="" && dataAreaId!="0")
report_level_id=3;
else
report_level_id=2

dhtmlXCalendarObject.prototype.langData["zh"] = {
    dateformat: '%Y%m%d',//2011-08
    monthesFNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    monthesSNames: ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"],
    daysFNames: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
    daysSNames: ["日", "一", "二", "三", "四", "五", "六"],
    weekstart: 7
}
var myCalendar = new dhtmlXCalendarObject("_dateDiv");
myCalendar.loadUserLanguage('zh');
myCalendar.hideTime();
myCalendar.show();
myCalendar.setDateFormat('%Y%m%d');
myCalendar.attachEvent("onClick",dateTimeClick);
//myCalendar.setSkin('dhtmlxcalendar_omega');
var areaNames={'0000':'四川省','99900':'省本部'};
var inited=false;
PortalCtrlr.getAreaName(function(res)
{
	if(res)
	{
		for(var i=0;i<res.length;i++)
		{
			areaNames[res[i][0]]=res[i][1];
		}
	}
});

function writeLog(menuId)
{
	MenuVisitLogAction.writeMenuLog({userId:userInfo['userId'],menuId:menuId});
}
//int define_id, int report_level_id)
function page_init()
{
	PortalCtrlr.getNotice(function(res){
		if(res)
		{
			var str='<table border="0px" cellspacing="0" cellpadding="0" >';
			for(var i=0;i<res.length;i++)
			{
				str+="<tr><td style='width:120px' valign=top >"+res[0]["UPDATE_DATE"]+"</td> <td>"+HTMLEncode(res[0]["NOTICE_CONTENT"])+"</td></tr>";
			}
			$("notice").innerHTML=str;
		}});
	
	//获取当前页面所需要的数据 tab页定义，时间区间  
	PortalCtrlr.getViewTabs(userIndexMenuId,user_define_id,init_rpt_level_id,"0",function(res)
	{
		//List<Map<String, Object>> t.TAB_ID, t.TAB_NAME, t.TAB_DESC   MIN_DATENO MAX_DATENO   EFFECT_DATENO
		if(res==null || res.length==0)
			alert("获取数据失败，或者未配置对应的菜单项。");
		if(res[0].length==0)
			alert("未配置表单项");
		for(var i=0;i<res[0].length;i++)
		{
			moduleMenu[res[0][i]["TAB_NAME"]]=res[0][i];
			moduleMenu[res[0][i]["TAB_NAME"]]["dataDateNo"]="";
			moduleMenu[res[0][i]["TAB_NAME"]]["dataLocalCode"]="";
			moduleMenu[res[0][i]["TAB_NAME"]]["dataAreaId"]="";
			moduleMenu[res[0][i]["TAB_NAME"]]["rpt_index"]=res[1][i];//指标列表
			moduleMenu[res[0][i]["TAB_NAME"]]["rpt_index_exp"]=res[2][i];//指标解释
		}
		//var tabHeight,tabWidth;//rpt_tabbar_td
		for(var key in moduleMenu)
	    {
	    	if(currentTab=="")
	    	{
	    		currentTab=key;
	    		dataDateNoBak=moduleMenu[key]["MAX_DATENO"];
	    	}
	    	menuTab.addTab(key,formatTabname(key) ,getShowLen(key,charPoint,minTabLen));
			var ik=document.createElement("DIV");
			ik.id="menu_tab_"+moduleMenu[key]["TAB_ID"];
			document.body.appendChild(ik);
			menuTab.setContent(key, ik.id);
			ik.style.width="100%";
			ik.style.height="100%";
			ik.innerHTML="<center>数据加载中，请稍等....<center>";
			moduleMenu[key]["div_id"]=ik.id;
			ik.innerHTML=key;
	    }
		$("div_src").style.display="none";
		$("div_src").style.Zindex="-1";
		menuTab.setTabActive(currentTab);//getActiveTab()
	    menuTab.attachEvent("onSelect",checkDataFresh);
		checkDataFresh(currentTab);
	});
}
function resetTabHeight(curName)
{
	var divId=moduleMenu[curName]["div_id"];	//	获取容器
	var div=$(divId);
	div.style.overflowY="scroll";//overflow-y: scroll;
	div.style.overflowX="auto";//hidden
	return;
	var tabId=moduleMenu[curName]["TAB_ID"];
	var ht=$('rpt_table_'+tabId).rows*21+60;
	if(ht<200)
		ht=200;
	div.style.height=(ht-30)+"px";
	$("rpt_tabbar").style.height=ht+"px";
	
	//alert($("rpt_tabbar").style.height+"_"+div.style.height);
}

//检查条件是变化并刷新数据 
function checkDataFresh()
{

	var curName;
	if(arguments.length)
		curName=arguments[0];
	else
		curName=currentTab;
	if(report_level_id> moduleMenu[curName].ROLLDOWN_LAYER)
	{
		if(moduleMenu[curName].ROLLDOWN_LAYER==2)
		{
			//dataLocalCode=
			dataAreaId="";
			report_level_id=2
		}
	}
	if(inited==false ||( curName!=currentTab && moduleMenu[curName]["MIN_DATENO"] && moduleMenu[curName]["MAX_DATENO"]))
	{
		var min_date=moduleMenu[curName]["MIN_DATENO"]+"";
		var max_date=moduleMenu[curName]["MAX_DATENO"]+"";
		if(min_date && max_date)
		{
			//alert(min_date+"__"+max_date);
			min_date=min_date.substring(0,4)+"-"+min_date.substring(4,6)+"-"+min_date.substring(6);
			max_date=max_date.substring(0,4)+"-"+max_date.substring(4,6)+"-"+max_date.substring(6);
			myCalendar.setSensitiveRange(min_date,max_date);
			if(dataDateNoBak>parseInt(moduleMenu[curName]["MAX_DATENO"]) || dataDateNoBak<parseInt(moduleMenu[curName]["MIN_DATENO"]))
			{
				dataDateNoBak=moduleMenu[curName]["EFFECT_DATENO"]+"";
			}
		}
		
	}
	//alert(dataDateNoBak+"__"+moduleMenu[curName]["EFFECT_DATENO"]+"__"+moduleMenu[curName]["MAX_DATENO"]);
	//if(dataDateNoBak>=parseInt(moduleMenu[curName]["MIN_DATENO"]) && dataDateNoBak<=parseInt(moduleMenu[curName]["EFFECT_DATENO"]))
		//dataLocalCode==moduleMenu[curName]["dataLocalCode"]
	//{
	//	loadTabData(curName); 	//营业区发生变化
	//}
	//else
	//{
auditData(curName)
	//}
	return true;
}
function auditData(curName)
{
		PortalCtrlr.getDataAudit(userIndexMenuId,moduleMenu[curName]["TAB_ID"],moduleMenu[curName]["INDEX_TYPE_ID"]
			,dataDateNoBak?dataDateNoBak:dataDateNo,'0000',function(res) // 以全省则只传 '0000' dataLocalCode
		{
			if(res && parseInt(res[0])==0)
			{
				alert("友好提示( "+curName+" , "+(dataDateNoBak?dataDateNoBak:dataDateNo)+"日):\n       "+res[1]+
					"\n       系统将自动跳转到最新可用数据日期:"+moduleMenu[curName]["EFFECT_DATENO"]);
				dataDateNoBak=moduleMenu[curName]["EFFECT_DATENO"]+"";
				myCalendar.setDate(dataDateNoBak.substring(0,4)+'-'+dataDateNoBak.substring(4,6)+'-'+dataDateNoBak.substring(6,8));
				//auditData(curName);	return;
			}
			else if(res && parseInt(res[0])!=0)
			{
				$("index_exp_title").innerHTML="数据提醒";
				if(res[2])
					$("index_exp_div").innerHTML="<p style='font-weight:600;color:red;'>"+curName+dataDateNoBak+"日异动说明："+(res[2]?res[2]:""+"</p>");
				else
					$("index_exp_div").innerHTML="";//curName+dataDateNoBak+"日数据无异常";
			}
			else
			{
				$("index_exp_title").innerHTML="数据情况";
				$("index_exp_div").innerHTML="";//curName+dataDateNoBak+"日数据无异常";
			}
			loadTabData(curName);
		});
}
function loadTabData(curName)
{	
	var tbb_rpt=$("rpt_tabbar");
	var padiv=tbb_rpt.getElementsByTagName("DIV")[2];
	padiv.style.textAlign="right";
	padiv.insertBefore($("tab_tip_div"), padiv.lastChild);
	$("tab_tip_div").style.textAlign="right";
	$("tab_tip_div").innerHTML="日期："+dataDateNoBak+" 本地网："+areaNames[dataLocalCode];
	if(report_level_id>2){
		$("tab_tip_div").innerHTML+="->"+areaNames[dataAreaId];
	}
		

	if(loadSucceed && dataLocalCode==moduleMenu[curName]["dataLocalCode"] &&
		dataDateNoBak==moduleMenu[curName]["dataDateNo"] &&
		dataAreaId==moduleMenu[curName]["dataAreaId"]) // 条件发生变化，需要刷新
	{
		currentTab=curName;
		resetTabHeight(curName);
		return;
	}
	loadSucceed=false;

	
	//设置日历控件的日期
	//alert(typeof dataDateNoBak);
	myCalendar.setDate(dataDateNoBak.substring(0,4)+'-'+dataDateNoBak.substring(4,6)+'-'+dataDateNoBak.substring(6,8));
	var po={};//ReportPO
	po.indexTypeId=moduleMenu[curName].INDEX_TYPE_ID;
	po.localCode=dataLocalCode;
	po.areaId=dataAreaId;
	po.indexCd="";
	po.reportLevelId=report_level_id;
	po.dateNo=dataDateNoBak;
	var rptIndexs=moduleMenu[curName]["rpt_index"];
	var cols=[];
	for(var i=0;i<rptIndexs.length;i++)
	{
		cols[i]=rptIndexs[i]["COL_EN_NAME"];
	}
	//alert("cols:"+cols);
	var divId=moduleMenu[curName]["div_id"];
	var div=$(divId);
	div.innerHTML="<center>数据加载中，请稍等....<center>";
	writeLog(moduleMenu[curName].TAB_ID);
	PortalCtrlr.getTableData(po,cols,1,function(res){
		if(res==null)
		{
			alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
			return;
		}
		dataDateNo=dataDateNoBak;
		moduleMenu[curName]["dataDateNo"]=dataDateNo;
		moduleMenu[curName]["dataLocalCode"]=dataLocalCode;
		moduleMenu[curName]["dataAreaId"]=dataAreaId;
		currentTab=curName;
		buildGrid(res);
		loadSucceed=true;
		$("div_src").style.display="none";
		$("div_src").style.zIndex="-1";
	});
}
function buildGrid(data)
{
	var divId=moduleMenu[currentTab]["div_id"];	//	获取容器
	var div=$(divId);
	var rptIndexs=moduleMenu[currentTab]["rpt_index"];//COL_EN_NAME COL_CN_NAME T.INDEX_CD,T.INDEX_NAME,T.FLAG 
	var str="";
	var tabId=moduleMenu[currentTab]["TAB_ID"];
	var rptType=moduleMenu[currentTab]["RPT_TYPE"];
	var rptIndexs=moduleMenu[currentTab]["rpt_index"];
	str='<table id=rpt_table_'+tabId+' rows='+data.length+'  border="0" style="background-color:#eeeeee;border-spacing:1px;border-collapse: separate;" cellspacing="1" cellpadding="1" width='+
	(div.offsetWidth-20)+'px bgcolor="#eeeeee" class="data_grid"><thead calss=fixedHeader ><tr class=Head_TD >';
	for(var i=0;i<rptIndexs.length;i++)
	{
		var COL_EN_NAME=rptIndexs[i]["COL_EN_NAME"];
		str+="<th height='23px' class='pad5' align='center' onmouseover='tipHit()' "+
			" width='"+parseInt(100/rptIndexs.length)+"%' id='rpt_"+tabId+"_head_"+i;
		var tdStyle="";
		if(currentTab=="过网用户" && i==5)
		{
			tdStyle+="border-left: 1px solid #75b8cf;";
		}
		if(rptType==1)
			str+="' YName='DIM_NAME' YValue='"+rptIndexs[i]["COL_CN_NAME"]+"' dim_name='"+rptIndexs[i]["COL_CN_NAME"]+"' ";
		else
			str+="' YName='INDEX_CD' YValue='"+rptIndexs[i]["INDEX_CD"]+"' dim_name='"+rptIndexs[i]["COL_CN_NAME"]+"' ";
		str+=" style='"+tdStyle+"'  >"+rptIndexs[i]["COL_CN_NAME"]+"</th>";
	}
	if(report_level_id< moduleMenu[currentTab].ROLLDOWN_LAYER)
	{
		str+="<th class='pad5' nowrap algin='center'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>";
	}
	str+="</tr></thead>";
	for(var i=0;i<data.length;i++)
	{
		var groupLine=(data[i][data[0].length-2]==1)?"groupLine":"";
		if(i%2==0)
			str+="<tr class='Row_TD "+groupLine+"' onmouseover='tr_onmouseover(this)' onmouseout='tr_onmouseoutR(this)' onclick='tr_onclick(this)'>";
		else
			str+="<tr class='Alt_Row_TD "+groupLine+"' onmouseover='tr_onmouseover(this)' onmouseout='tr_onmouseoutA(this)' onclick='tr_onclick(this)'>";
		var start=3;
		if(rptType==0)start=4;else start=3;
		for(var j=start;j<data[0].length-2;j++)
		{
			var index=j-4;
			index=(index>0?index:0);
			var COL_EN_NAME="";
			str+="<td id='rpt_"+tabId+"_"+i+"_"+index+"' calss='pad10' nowrap onclick='openArea("+i+","+index+")' ";
			var tdStyle="";
			if(currentTab=="过网用户" && j==6+start)
			{
				tdStyle+="border-left: 1px solid #75b8cf;";
			}
			if(j==3 || j==4)
			{
				str+=" align=left  style='padding-left:15px;' index_cd='"+data[i][2]+"' index_name='"+data[i][j]+"' ";
				/* //不需要动态算类型
				if(rptType==1)
				{
					str+="XName='INDEX_CD' XValue='"+data[i][0]+"' ";
					COL_EN_NAME="";
				}
				else
				{
					str+="XName='DIM_NAME' XValue='"+data[i][j]+"' ";
				}*/
				str+="  onmouseover='tipHit()' ";
				str+=" >"+ formatDataView(data[i][j],null)+"</td>";
			}
			else
			{
				
				str+=" align=right  style='padding-right:10px;"+tdStyle+"' ";
				str+=" >"+ formatDataView(data[i][j],rptIndexs[index],data[i][start])+"</td>";
			}
			if(rptType!=0 && j==3)j++;
		}
		if(report_level_id< moduleMenu[currentTab].ROLLDOWN_LAYER)
		{
			str+="<td nowrap align='center' onclick='openArea("+i+","+rptIndexs.length+
			   ")' id='rpt_"+tabId+"_"+rptIndexs.length+"' class=unl  >辖区</td>";
		}
		str+="</tr>";
	}
	str+="</table>";
	div.innerHTML=str;
	if(data.length)
	if(moduleMenu[currentTab].DEFAULT_GRID)
	{
		var tmp=moduleMenu[currentTab].DEFAULT_GRID.split(",");
		openArea(parseInt(tmp[0]),parseInt(tmp[1]),true);
	}
	else
	{
		openArea(0,1,true);
	}
 	resetTabHeight(currentTab);
	delete str;
}
var ORTD=null;
function setActiveTD(row,col,src)
{
	var __ORTD=$("rpt_"+moduleMenu[currentTab].TAB_ID+"_"+row+"_"+col);
	if(ORTD)
	{
		ORTD.style.backgroundColor="";
		ORTD.style.color="";
	}
	ORTD=__ORTD;
	if(ORTD&&col!=0){
		ORTD.style.backgroundColor = "highlight";
		ORTD.style.color = "highlighttext";
	}else{
		return;
	}
	
	var rptIndexs=moduleMenu[currentTab]["rpt_index"];
	if(col==rptIndexs.length)return;
	var tabId=moduleMenu[currentTab].TAB_ID;
	var rptType=moduleMenu[currentTab].RPT_TYPE;
	var rd=$("rpt_"+tabId+"_"+row+"_0");
	var hd=$("rpt_"+tabId+"_head_"+col);
	var rname=rd.getAttribute("index_name").replaceAll("\\#",'').replaceAll("\\$",'');
	var dname=getAttr(hd,"YValue").replaceAll("\\#",'').replaceAll("\\$",'');
	var title="";
	if(rname.indexOf("（")>0)
		rname=rname.substring(0,rname.indexOf("（"));
	else if(rname.indexOf("(")>0)
		rname=rname.substring(0,rname.indexOf("("));
	if(dname.indexOf("（")>0)
		dname=dname.substring(0,dname.indexOf("（"));
	else if(dname.indexOf("(")>0)
		dname=dname.substring(0,dname.indexOf("("));
	if(rptType==1)
	{
		title=dname+"->"+rname;
		rname='指标:'+dname+rname;
	}
	else
	{
		title=rname+"->"+dname;
		rname='指标:'+rname+dname;
	}
	$('flegend_index').innerHTML=rname;
	$("index_title").innerHTML=currentTab+"->"+title;
}
//单元格单击事件 
function openArea(row,col,src)
{
	setActiveTD(row,col,!src);
	
	if(col==0)
	{
		return;
	}
	var rptIndexs=moduleMenu[currentTab]["rpt_index"];
	var tabId=moduleMenu[currentTab].TAB_ID;
	var rd=$("rpt_"+tabId+"_"+row+"_0");
	if(col==rptIndexs.length)	//	辖区钻取
	{//dateNo=20110821&indexTypeId=8&indexCd=JZFX_IND_1&indexName=%E4%B8%AD%E5%9B%BD%E7%94%B5%E4%BF%A1&reportLevelId=2
		var rl=parseInt(report_level_id)+1;
		var indexCd=rd.getAttribute("index_cd");
		var indexFormat=(rd.getAttribute("index_name").indexOf("\%")>=0);
		var indexName=rd.getAttribute("index_name").replaceAll("\\#",'').replaceAll("\\$",'');
		if(userInfo['localCode']!='0000'){
			dataLocalCode=userInfo['localCode'];	
		}
		var url="/portal/module/portal/areaDrill.jsp?dateNo="+dataDateNoBak+"&indexTypeId="+moduleMenu[currentTab].INDEX_TYPE_ID
			+"&indexCd="+indexCd+"&indexName="+indexName+"&report_level_id="+rl+"&localCode="+dataLocalCode+"&indexFormat="+(indexFormat?1:0);
		url=encodeURI(url);
		openTab(tabId+"_"+rl+"_"+indexCd,indexName,url,0);
	}
	else	//	图形刷新
	{
		var hd=$("rpt_"+tabId+"_head_"+col);
		chartFieldName=rptIndexs[col].COL_EN_NAME;
		//alert(hd.xval);
		//alert(hd.getAttribute("YName")+"_ r _"+hd.getAttribute("YValue"));
		if(hd.getAttribute("YName")=='DIM_NAME')
		{
			chartIndexCd=rd.getAttribute("index_cd");
		}
		else
		{
			chartIndexCd=hd.getAttribute("YValue");
		}
		chartRadio=1;
		$("chartDateRadio1").checked=true;
		fchart();
	}
}
function fchart()
{
	// String indexTypeId,String reportLevelId,String indexCd,
	// String areaId,String localCode,String fieldName,int radio,String dateNo
	var indexTypeId=moduleMenu[currentTab].INDEX_TYPE_ID;
	var reportLevelId=report_level_id;
	var localCode=dataLocalCode;
	var areaId=dataAreaId;
	PortalCtrlr.getTableChart(indexTypeId,reportLevelId,chartIndexCd,dataLocalCode,dataAreaId,chartFieldName,chartRadio,dataDateNo,function(res)
	{
		if(!res)
		{
			alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
			return;
		}
		$("chartdiv").style.width=$("rpt_tabbar").offsetWidth+"px";
		$("chartdiv").style.height="200px";
		var chart = new FusionCharts("<%=path%>/portal/resource/swf/FusionCharts/MSLine.swf", "ChartId", $("rpt_tabbar").offsetWidth+"", "200");
		//chart.setDataURL("Data/MSLine.xml");	
		chart.setDataXML(res[0]); 
		chart.render("chartdiv");
		if(res[1])
		{
			var colorMapData=[];
			var areas="后三名：";
			for(var i=0;i<res[1].length;i++)
			{
				/*if(i<3)
					colorMapData[i]=['Area_'+res[1][i]["LOCAL_CODE"],mapColors[0]];
			   	else */
			   		if(i>=res[1].length-3)
			   		{
			   			colorMapData[i]=['Area_'+res[1][i]["LOCAL_CODE"],mapColors[3]];
			   			if(areaNames[res[1][i]["LOCAL_CODE"]])
			   			areas+=areaNames[res[1][i]["LOCAL_CODE"]]+"&nbsp;&nbsp;";
			   		}
				/*else if(i>=res[1].length-6)
			   		colorMapData[i]=['Area_'+res[1][i]["LOCAL_CODE"],mapColors[2]];*/
			   	else	
			 		colorMapData[i]=['Area_'+res[1][i]["LOCAL_CODE"],mapColors[1]];
			 }
			 getFlashMovieObject("myFlash").setMap(colorMapData);
			 delete colorMapData;
			 $("index_order_area").innerHTML=areas;
		}
	});
} 
function formatDataView(data,colAttr,rowName)
{
	if(data==null)
		return "-";
	//alert(typeof data);
	//#$我的e家
	var styleData;
	if(colAttr)
	{
		//re = new RegExp('\\#',"g");
		//data=data.replace(re,'&nbsp;&nbsp;&nbsp;&nbsp;');
		if(data=='-88888888')
			styleData='-';
		else
		{
			styleData="dataNum";
			var fl=parseInt(data)<0;
			if(fl)
			{
				styleData="<span class=red >dataNum</span>";
				data=data*-1;
			}
			var formats="#,###.##";
			if(colAttr["COL_CN_NAME"].indexOf("\\%")>0 || rowName.indexOf("\\%")>0)
				data+="%";
			else
				data=formatNumber(data,{pattern:formats});
			if(fl)data="-"+data;
			styleData=styleData.replace("dataNum",data);
		}
	}
	else
	{
		re = new RegExp('\\#',"g");
		var fl=data.indexOf("\\$")>0;
		if(data.charAt(0)=='#'){
			data=data.substring(1);
		}
		styleData=data.replace(re,'&nbsp;&nbsp;&nbsp;&nbsp;');
		
		if(fl)
			styleData=styleData.replace("\\$","<img src='./images/jianhao.gif' border=0 value=1 onclick='changCol()' />");
		
	}
	return styleData;
}
function changCol()
{return;
	var el=event.srcElement;
	if(el.value==1)
	{
		el.value=0;
		el.src='./images/jiahao.gif';
	}
	else
	{
		el.value=1;
		el.src='./images/jianhao.gif';
	}
}
function tipHit(flag)
{
	var el=event.srcElement;

	//var div_src=$("div_src");
	 

	var index_exp=moduleMenu[currentTab]["rpt_index_exp"];
	var indexName="";
	var indexNum=-1;

	if(el.id.indexOf("_head")>0)
	{
		indexName=el.getAttribute("dim_name");
	}
	else
	{
		indexName=el.getAttribute("index_name");
	}
	if(!indexName)return;
	var _indexName=indexName.replaceAll("\\#","").replaceAll("\\$","").trim();
	
	if(_indexName.indexOf("(")>0 || _indexName.indexOf("（")>0)
	{
		if(_indexName.indexOf("(")>0)
			_indexName=_indexName.substring(0,_indexName.indexOf("(")).trim();
		else
			_indexName=_indexName.substring(0,_indexName.indexOf("（")).trim();
	}
	for(var i=0;i<index_exp.length;i++)
	{
		if(index_exp[i]["INDEX_NAME"]==indexName)
		{
			indexNum=i;
		}
		else if(index_exp[i]["INDEX_NAME"]==_indexName)
		{
			indexName=_indexName;indexNum=i;
		}
	}
	//alert(indexNum);alert(_indexName);
	//for(var key in index_exp)alert(index_exp[key]["INDEX_NAME"]);
	if(indexNum==-1)	return;
	$("index_exp_title").innerHTML="指标解释";
	$("index_exp_div").innerHTML=index_exp[indexNum]["INDEX_NAME"]+":"+index_exp[indexNum]["INDEX_EXPRESS"];
//			HTMLEncode(index_exp[indexNum]["INDEX_EXPRESS"]);

	//显示指标解释
	//div_src.style.position='absolute';
/*	div_src.style.width="200px";
	div_src.style.height="100px";
	div_src.style.zIndex="1000";
	div_src.style.oveflowY="scroll";
	div_src.style.oveflowX="hidden";
	div_src.style.left=event.x;
	div_src.style.top=event.y;
	setdiv("div_src");
	if(el.id!="div_src")
	{
		//div_src.style.left=parseInt(div_src.style.left)+el.offsetWidth;
	}
*/	// t.index_cd,d.index_name,d.index_express 
}
function chartDateChg()
{
	if(chartRadio!=event.srcElement.value)
	{
		chartRadio=event.srcElement.value;
		fchart();
	}
}
function dateTimeClick(date, state)
{
	var dateNo=myCalendar.getDate(true);//从控件获取 
	if(loadSucceed && dateNo==dataDateNo)return;//同一时间点多次点击
	dataDateNoBak=dateNo;
	//alert(dataDateNoBak);
	checkDataFresh();
}

//单击地图调用的js方法
function callJs(v)
{
	if(v=='0')	//	返回权限判断
	{
		if(userInfo['localCode']!='0000' )
		{
			v=userInfo['localCode'];
			report_level_id=2;
		}
		else
		{
			v="0000";
			report_level_id=1;
		}
		dataLocalCode=v;
		dataAreaId="0";
	}
	else
	{
		if(userInfo['localCode']!='0000' )
		{
			if(v.charAt(0)=='0')
			{
				dataLocalCode=v;
				dataAreaId="0";
				report_level_id=2;
				if(v!=userInfo['localCode'])
				return;
			}
			else
			{
				if(userInfo['areaId']!="" && userInfo['areaId']!="0")
				{
					dataAreaId=userInfo['areaId'];
				}
				else
				{
					dataAreaId=v;
				}
				report_level_id=3;
				
			}
		}
		else
		{
			
			if(v.charAt(0)=='0')
			{
				dataLocalCode=v;
				dataAreaId="0";
				report_level_id=2;
			}
			else
			{
				dataAreaId=v;
				report_level_id=3;
			}
		}
	}
	
 	checkDataFresh();
} 
    
//界面加载与SWF加载的同步
var isReady = false;
function JsReady(){return isReady;}
//取得flash对象
function getFlashMovieObject(movieName)
{
    if(window.document[movieName])
    {
        return window.document[movieName];
    }else if(navigator.appName.indexOf("Microsoft")==-1)
    {
        if(document.embeds && document.embeds[movieName])
            return document.embeds[movieName];
    }
    else
    {
        return document.getElementById(movieName);
    }
}

//[地区,颜色,是否下转]
var initData =[];
var localCode=["028","0813","0812","0830","0838","0816","0839","0825","0832","0833","0817","0831","0826","0818","0835","0837","0836","0834","0827","0840","0841"];
for(var i=0;i<localCode.length;i++)
{
	if(userInfo['localCode']=='0000' || userInfo['localCode']==localCode[i])
		initData[i]=['Area_'+localCode[i],'0xF4F3F0',true];
	else
		initData[i]=['Area_'+localCode[i],'0xF4F3F0',false];
}
 //地图flash方法与js方法进行绑定，使操作js方法可以调用flash方法
function setData()
{
    getFlashMovieObject("myFlash").setMap(initData);
    delete initData;
    if(userInfo['localCode']!='0000' && userInfo['areaId']!="" && userInfo['areaId']!="0")
  	{
  		gotoArea('A_'+userInfo['localCode'],true);
	}
}

function gotoArea(area,isOnly){
    getFlashMovieObject("myFlash").gotoArea(area,isOnly);
}
  
window.onload = function ()
{
    isReady = true;
    page_init();
}
function openTab(menu_id,menu_name,url,type,formname)
{
	return parent.openMenu(menu_name,url,'top');
}
             

    </script>
    
