<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title>首页内容</title>
    <%@include file="../../public/head.jsp" %>
    <%@include file="../../public/include.jsp" %>
    <script type="text/javascript" src="<%=path %>/dwr/interface/PortalCommonCtrlr.js"></script>
    <script type="text/javascript" src="<%=path %>/dwr/interface/MenuVisitLogAction.js"></script>
    <script type="text/javascript" src="<%=path %>/portal/resource/js/TableMouse.js"></script>
    <script type="text/javascript" src="<%=path %>/portal/resource/js/formatNumber.js"></script>
    <script type="text/javascript" src="<%=path %>/portal/resource/js/Drag.js"></script>
    <script type="text/javascript" src="<%=path %>/portal/resource/js/FusionCharts.js"></script>
    <script type="text/javascript" src="<%=path %>/dwr/interface/PortalInstructionAction.js"></script>
    <script type="text/javascript" src="<%=path %>/meta/public/code.jsp?types=NOTICE_LEVEL,NOTICE_FUNCITON,IS_SHOW"></script>
    <script type="text/javascript" src="<%=path %>/dwr/interface/ReportConfigAction.js"></script>
    <link href="images/tab.css" rel="stylesheet" type="text/css"/>

    <link type="text/css" rel="stylesheet" href="<%=path %>/portal/resource/css/base_portal.css"/>
    <style type="text/css">
        .bt {
            background-image: url(./images/bt_bg.jpg);
            text-align: middle;
            padding-top: 3px;
            border: 0;
        }

        .groupLine {
            background-image: url(images/line_bot.gif);
        }

        .pad5 {
            padding-left: 5px;
            padding-right: 5px;
        }

        .pad10 {
            padding-left: 10px;
            padding-right: 10px;
        }

        DIV.Head_TD {
            font-family: "宋体";
            font-size: 9pt;
            color: #000000;
            text-decoration: none;
            line-height: 20px;
            background-color: #D0E8FF;
            font-weight: bold;
            color: #000000;
        }

        .Head_TD {
            font-family: "宋体";
            font-size: 9pt;
            color: #000000;
            text-decoration: none;
            line-height: 20px;
            background-color: #D0E8FF;
            font-weight: bold;
            color: #000000;
        }

        .Row_TD {
            font-family: "宋体";
            font-size: 9pt;
            color: #000000;
            text-decoration: none;
            line-height: 20px;
            background-color: #ffffff;
        }

        DIV.Row_TD {
            font-family: "宋体";
            font-size: 9pt;
            color: #000000;
            text-decoration: none;
            line-height: 20px;
            background-color: #FAFBFF;
        }

        .Alt_Row_TD {
            font-family: "宋体";
            font-size: 9pt;
            color: #000000;
            text-decoration: none;
            line-height: 20px;
            background-color: #E3EFFF;
        }

        DIV.Alt_Row_TD {
            font-family: "宋体";
            font-size: 9pt;
            color: #000000;
            text-decoration: none;
            line-height: 20px;
            background-color: White;
        }

        .red {
            font-family: "宋体";
            font-size: 9pt;
            color: #FF0000;
        }

        .unl {
            text-decoration: underline;
            color: #0000FF;
            cursor: pointer;
        }

        .data_grid {

        }

        .data_grid td {
        }

        .data_grid th {
            border-color: #eee;
            text-align: center;
        }

        .css_tabTitle {
            font-size: 12px;
            padding: 0px;
            margin: 0px;
            padding-top: 5px;
        }
    </style>
</head>
<body style="overflow: hidden;">
<div id="div_src"
     style="position: absolute; width: 100%; height:100%; z-index: 1000; top: 0px; left: 0px;
			background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:scroll;"
     title="" class="dragBar">
    <center style="color: red;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;">数据加载中，请稍等....
    </center>
</div>
<div id="tab_tip_div" style="width:300px;padding-right: 30px;float:right;display: none;">日期：20110816 本地网：四川</div>
<table border="0" cellspacing="1" cellpadding="1" style="width: 100%;height:50px;bgcolor:#88B0BE;" >
    <tr>
        <td width="192px" bgcolor="#FFFFFF" valign=top id="left_td">
         <div style="position:relative;top:0px;left:0px;height:450px;overflow: hidden;" id="left_td1" >
            <table border="0" width=100% height="100px" cellspacing="1" cellpadding="1" width=100% bgcolor="#88B0BE">
                <tr>
                    <td height=180 bgcolor="#FFFFFF" style="padding-top: 5px;">
                        <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="192px" height="180" id="myFlash">
			                   <param name="allowScriptAccess" value="always" />
			                   <param name="movie" value="map.swf" />
			                   <param name="quality" value="high" />
			                   <embed src="map.swf" quality="high" width="192px" height="180" name="myFlash" allowScriptAccess="always" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
			           </object>
                    </td>
                </tr>
                <tr>
                    <td style="vertical-align: top; padding-top: 0px;padding-left: 2px;border-top:1px solid #75b8cf;"
                            align="center"
                            bgcolor="#FFFFFF" align="center" height="195px">
                        <div id="_dateDiv" style="position:relative;top:0px;left:0px;">

                        </div>


                    </td>
                </tr>
            </table>
        </div>
        </td>
        
        <td valign=top>
            <!-- 右边内容 -->
            <div id="col_comment"
                 style="position:relative;padding-left: 1px; padding-top: 0px; font-size: 12px;overflow-x:auto; overflow-y:auto;">
                <table border="0" width="100%" cellspacing="0" id="table_rpt"
                       cellpadding="1" bgcolor="#88B0BE">
                    <tr>
                        <td bgcolor="#FFFFFF" valign=top valign='top'>
                        <div id="_showDetailDiv" style="display:">
                            <table border="0" width=100% cellspacing="1" id="table_rpt"
                                   cellpadding="1"
                                   style="background: #b9dff7 url(./images/title-bg.gif) repeat-x 0 -250px; border: 1px solid #75b8cf;">
                                <tr>
                                    <td height=25px style="padding-left: 10px;">
                                    <div style="float:left;">公告/批示</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <span onclick="configure_user_indicators()" style="cursor: pointer;">
								<img src="<%=rootPath %>/meta/resource/images/tableImport.png" width="18px" height="12px"></img>
								指标与用户挂钩</span>
                                    </td>
                                    <td style="padding-left: 10px;border-left: 2px solid #75b8cf;" id="index_exp_title">
                                    <div style="float:left;">指标解释</div>
                                        
                                    </td>
                                </tr>
                                <tr>
                                    <td style="vertical-align: top; padding-top: 0px;padding-left: 5px;" width="50%" align="center" bgcolor="#FFFFFF" align="center">
				                        <div id="_instructionDiv" style="position:relative;top:0px;left:0px;height:50px;">
				                            <marquee scrollAmount=1 width=100% height=50
                                                 style="line-height: 125%;" direction=up id="scroll"
                                                 onmousemove="this.stop()" onmouseout="this.start()">
                                            	<div id="instructionList" style="text-align: left;overflow:hidden;"></div>
                                        	</marquee>
				                        </div>
				                    </td>
                                    <td bgcolor="#FFFFFF"
                                        style="padding-top: 5px; padding-left: 10px;border-left: 2px solid #75b8cf;"
                                        valign=top>
                                        <table width="100%">
                                            <tbody>
                                            <tr>
                                                <td colspan="1" style="padding:0px;padding-left: 10px;">
                                                    <div id="index_exp" style="padding:0px;margin:0px;height:40px;overflow: auto;">
                                                    	
                                                    </div>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                        <%--<div id="index_exp"></div>--%>

                                    </td>
                                </tr>
                            </table>
						</div>
                        </td>
                    </tr>
                    <tr>
                        <td  style="padding-top: 0px;vertical-align: top;height: 200px;bgcolor:#FFFFFF" id=rpt_tabbar_td>
                            <div id=rpt_tabbar style="width:100%; height:100%;">
                            </div>
                        </td>
                    </tr>
                    <!-- 图形隐藏地址 -->
			        <tr id="hiddenChartTrId1" sytle="display:">
			             <td height="25px" style="padding-left:10px;background-image: url(./images/index_title_bg.jpg);display:"
			                            id="index_title" width=100%>
			             </td>
			        </tr>
			        <tr id="hiddenChartTrId2" sytle="display:">
			           <td bgcolor="#FFFFFF" height=100>
			           <div id=chartdiv></div>
			           <div id="RadioOption" style="position: relative;top:170px,left:0px;width:100%;height:20px;z-index:9999;align=right;display:"
			                                 align="right">
			           </div>
			
			           </td>
			        </tr>
                </table>
            </div>
        </td>
        <td width=8px bgcolor="#A6C8ED" style="cursor: hand;"
            onclick="changeRemarkDisplay('remark_td')">
            <img src="./images/frame_to_left.jpg" id="hand_img"/>
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
    dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
    var isWidthScreen = false;
    if(window.screen.height>768){
        isWidthScreen = true;
    }
    if(isWidthScreen){
        document.getElementById('left_td1').style.height = 800+"px";
    }
    var tabHeight, tabWidth;
    if (window.parent && window.parent.tabHeight && window.parent.tabHeight && tabWidth) {
        tabHeight = window.parent.tabHeight;
        tabWidth = window.parent.tabWidth;
    }
    else {
        tabHeight = $("div_src").offsetHeight;
        tabWidth = $("div_src").offsetWidth;
    }
    //alert(tabHeight+"  "+$("div_src").offsetWidth+"  "+$("div_src").offsetHeight)
    var col_comment = $("col_comment");
    var table_rpt = $("table_rpt");
    var remarkWidth = 150;//$("remark_td").offsetWidth;
    col_comment.style.overflowX = "hidden";//hidden
    col_comment.style.height = (tabHeight-1) + "px";
    var leftWidth = $("left_td").offsetWidth;
    col_comment.style.width = (tabWidth - leftWidth-5) + "px";
    $("table_rpt").style.width = (col_comment.offsetWidth - 19) + "px";
    var menuTab = new dhtmlXTabBar("rpt_tabbar", "top");
    menuTab.setSkin('dhx_skyblue');
    menuTab.setImagePath("../../../meta/resource/dhtmlx/imgs/");
    menuTab.setHrefMode("ajax-html");
    menuTab.enableTabCloseButton(false);
    menuTab.enableScroll(false);
    function changeRemarkDisplay(td) {
        return;
        if ($(td).style.display != "none") {
            $(td).style.display = "none";
            $(hand_img).src = "./images/frame_to_left.jpg";
            col_comment.style.width = (tabWidth - leftWidth) + "px";
        }
        else {
            $(td).style.display = "block";
            $(hand_img).src = "./images/frame_to_right.jpg";
            col_comment.style.width = (tabWidth - leftWidth - remarkWidth) + "px";
        }
        table_rpt.style.width = (col_comment.offsetWidth - 19) + "px";
        menuTab.setSize(table_rpt.offsetWidth, table_rpt.offsetHeight);
    }
</script>

<script type="text/javascript">
//全局用户数据
var dataLocalCode = userInfo['localCode'];
//alert("dataLocalCode="+dataLocalCode);
var dataAreaId = userInfo['areaId'];
var dataDateNo = "20110821";    //数据时间
var dataDateNoBak = "";
var moduleMenu = {};    //menu_name:{TAB_ID:5,TAB_NAME:'XX',url:'xxx',dataDateNo:xx,dataLocalCode:xx,dataAreaId:xx,}
var currentTab = "";
var report_level_id = 1;
var loadSucceed = false;
var chartFieldName = ""; //字段名
var chartIndexCd = "";//图形字段名
var chartRadio = "1";//时间连续类型
var init_rpt_level_id = 1;
var instructionId = -1;
var dwrCaller = new biDwrCaller();
var dvMsg;   //提示框弹出的div
var instructFlag = false;
var submitFlag = true;
var instructData;
var callInputCount = 0;
var isShowChart = true;
//地图颜色
var mapColors = ['0x00CC00', '0xF4F3F0', '0xFFCC00', '0xF46F42'];
dhtmlXCalendarObject.prototype.langData["zh"] = {
    dateformat:'%Y%m%d', //2011-08
    monthesFNames:["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    monthesSNames:["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"],
    daysFNames:["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
    daysSNames:["日", "一", "二", "三", "四", "五", "六"],
    weekstart:7
}
var myCalendar = new dhtmlXCalendarObject("_dateDiv");
myCalendar.loadUserLanguage('zh');
myCalendar.hideTime();
myCalendar.show();
myCalendar.setDateFormat('%Y%m%d');
myCalendar.setSensitiveRange("20110601", "20110603");
myCalendar.attachEvent("onClick", dateTimeClick);
//地域编码和地域名称的映射
var areaNames = {};
//地域编码和地域层级的映射
var areaLevel = {};
//地域层级和地域编码的映射,存储结构为:{1:[code1...],2:[code2..]}
var levelArea = {};
//省地域Code
var provinceCode = '';
var inited = false;
PortalCommonCtrlr.getAreaName({callback:function (res) {
	try{
		if (res) {
	        for (var i = 0; i < res.length; i++) {
	            areaNames[res[i][0]] = res[i][1];
	            areaLevel[res[i][0]] = res[i][2];
	            //初始化地域层级和每一层所属的地域
	            var codes = levelArea[res[i][2]];
	            if (codes) {
	                codes.push(res[i][0]);
	            } else {
	                levelArea[res[i][2]] = [res[i][0]];
	            }
	            //初始化省地域
	            if (res[i][2] == 1) {//如果是第一级，则是省地域
	                provinceCode = res[i][0];
	            }
	        }
	        setData();
	    }
	}catch(e){}
    //初始化报表的层次
    if (dataLocalCode) {
        report_level_id = areaLevel[dataLocalCode];
    }
    page_init();
}});
function writeLog(menuId) {
    MenuVisitLogAction.writeMenuLog({userId:userInfo['userId'], menuId:menuId});
}
$("div_src").style.display = "none";
$("div_src").style.Zindex = "-1";
//int define_id, int report_level_id)
function page_init() {
    //获取当前页面所需要的数据 tab页定义，时间区间
    PortalCommonCtrlr.getViewTabs(function (res) {
        //List<Map<String, Object>> t.TAB_ID, t.TAB_NAME, t.TAB_DESC   MIN_DATENO MAX_DATENO   EFFECT_DATENO
        if (res == null) {
            $("rpt_tabbar").innerHTML = "获取数据失败！";
            return;
        }
        if (res.length == 0) {
            $("rpt_tabbar").innerHTML = "未配置菜单项或者您无权限查看重点指标！";
            return;
        }
        for (var i = 0; i < res[0].length; i++) {
            moduleMenu[res[0][i]["TAB_NAME"]] = res[0][i];
            moduleMenu[res[0][i]["TAB_NAME"]]["dataDateNo"] = "";
            moduleMenu[res[0][i]["TAB_NAME"]]["dataLocalCode"] = "";
            moduleMenu[res[0][i]["TAB_NAME"]]["dataAreaId"] = "";
            moduleMenu[res[0][i]["TAB_NAME"]]["rpt_index"] = res[1][i];//指标列表
            moduleMenu[res[0][i]["TAB_NAME"]]["rpt_index_exp"] = res[2][i];//指标解释
            isShowChart = res[5];
        }
        //var tabHeight,tabWidth;//rpt_tabbar_td
        if(!isShowChart){//没有图形
        	noShowChart();
        }else
        	showChart();
        for (var key in moduleMenu) {
            if (currentTab == "") {
                currentTab = key;
                dataDateNoBak = moduleMenu[key]["MAX_DATENO"];
            }
            menuTab.addTab(key, formatTabname(key), getShowLen(key, charPoint, minTabLen));
            var ik = document.createElement("DIV");
            ik.id = "menu_tab_" + moduleMenu[key]["TAB_ID"];
            document.body.appendChild(ik);
            menuTab.setContent(key, ik.id);
            ik.style.width = "100%";
            ik.style.height = "100%";
            ik.innerHTML = "<center>数据加载中，请稍等....<center>";
            moduleMenu[key]["div_id"] = ik.id;
            ik.innerHTML = key;
        }
        $("div_src").style.display = "none";
        $("div_src").style.Zindex = "-1";
        menuTab.setTabActive(currentTab);//getActiveTab()
        menuTab.attachEvent("onSelect", checkDataFresh);
        checkDataFresh(currentTab);
        //批示数据 
        dwrCaller.addAction("queryInstructions",function(afterCall,param){
	      PortalInstructionAction.queryInstructions(instructionId,function(data){
		    if(data){
		    	instructData = data;
		    	var noticeColor=['black','#f08080','#dc143c','red'];
		    	var span = "";
		    	var n =0;
		    	for (var i = 0,len = data.length; i < len; i++){
		    		if(!data[i]["INSTRUCTION_ID"]){
		    			span =span + '<div id="divlist'+i+'" style="border: 0px;padding: 0;margin: 0;">' +
		    			'<span id="showyear'+i+'" style="font-weight: bold;color:'+noticeColor[parseInt(data[i]['NOTICE_LEVEL'])]+'">公告：</span>' 
			    		+ data[i]["NOTICE_CONTENT"];
		    		}else if(data[i]["INSTRUCTION_LEVEL"] == 1){
		    			var instructionComment = "" + data[i]["INSTRUCTION_COMMENT"];
		    			if(instructionComment == null || instructionComment == "null"){
		    				instructionComment = "";
		    			}
		    			span =span + '<div id="divlist'+i+'" style="border: 0px;padding: 0;margin: 0;color:red;"><span id="showyear'+i+'" style="font-weight: bold;">批示：</span>' 
			    		+ instructionComment+'&nbsp;&nbsp;'+data[i]["USER_NAME"]+'('+data[i]["SUBMIT_DATE"]+')'+
			    		 '&nbsp;&nbsp;<a href="#" onclick="instructionCall(divlist'+i+','+i+');return false;" style="color: #915833">回复</a></div> </br>';
		    		}
		    		
		    	}
		    	document.getElementById("instructionList").innerHTML = span;
		    }
	      })
        });
         dwrCaller.executeAction("queryInstructions");
    });
}
//领导批示 回复 
function instructionCall(instructionId,arrayIndex){
	var dhxWindow=new dhtmlXWindows();
	dhxWindow.createWindow("applyWindow",0,0,600,300);
	 var callBackWindow=dhxWindow.window("applyWindow");
	 callBackWindow.setModal(true);
    callBackWindow.stick();
    callBackWindow.setDimension(600,300);
    callBackWindow.center();
    callBackWindow.denyResize();
    callBackWindow.denyPark();
    //关闭一些不用的按钮。
    callBackWindow.button("minmax1").hide();
    callBackWindow.button("park").hide();
    callBackWindow.button("stick").hide();
    callBackWindow.button("sticked").hide();
    callBackWindow.setText("批示回复");
    callBackWindow.keepInViewport(true);
    callBackWindow.show();
    var msg = instructionId.innerText.substr(0,instructionId.innerText.length - 2);
    var msgTitle = '<span style="color: #000000">批示信息： </span>';
    var innerMsg = "";
    //建立表单 
    var callBackForm = callBackWindow.attachForm([
    	{type:"block",offsetTop:15,list:[  		
    		{type:"label",offsetLeft:40, label:"<span id='winInstruction'></span>"}
    		]},
       {type:"block", list:[
            {type:"label", offsetLeft:40, label:"<span id='winInstructionCall'> </span>"}
        ]},
       {type:"block", list:[
            {type:"input", offsetLeft:40, rows:4, label:"回复内容 ：", inputWidth:370, name:"winCallbackMsg", validate:"NotEmpty,MaxLength[1024]"},
            {type:"newcolumn"},
            {type:"label", label:"<span style='color: red'>*</span>"}
        ]},
       {type:"block", offsetTop:10, list:[
            {type:"button", label:"保存", name:"saveInstrction", value:"保存", offsetLeft:200},
            {type:"newcolumn"},
            {type:"button", label:"关闭", name:"closeInstruction", value:"关闭"}
        ]}
    	]);
    callBackForm.defaultValidateEvent();
    document.getElementById("winInstruction").innerHTML = msgTitle + msg;
    
    //查询 指标批示的所有回复 
    PortalInstructionAction.queryAllChildrenInst(instructData[arrayIndex]["INSTRUCTION_ID"],function(chidData){
       if(chidData){
    	  for (var i = 0;i< chidData.length;i ++){
    		  for(var j = 0; j < chidData[i]["INSTRUCTION_LEVEL"];j++){
    			  innerMsg = innerMsg + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
    		  }
    		  callInputCount = i;
    	    innerMsg = innerMsg +chidData[i]["USER_NAME"]+'：  '+chidData[i]["INSTRUCTION_COMMENT"]+'<span id="ms'+chidData[i]["INSTRUCTION_ID"]+'"></span> ' +
    	    '<a id="msgIdBak'+i+'"  onclick="instructioMsgBack(ms'+chidData[i]["INSTRUCTION_ID"]+","+i+');return false;"   style="color: #915833;">回复</a>'+ 
    	    '<a id="msgIdcommit'+i+'" onmouseover="submouseover(false)"  onmouseout="submouseover(true)" onclick="insertCallMsg('+chidData[i]["INSTRUCTION_ID"]+','+instructData[arrayIndex]["INSTRUCTION_ID"]+',callBakMsg'+callInputCount+');return false;"   style="color: #915833;display:none">提交</a></br>' ;
          }
    	  document.getElementById("winInstructionCall").innerHTML = innerMsg;
       }
       });
    //按钮处理 addForm.attachEvent("onButtonClick", function (id) {
    	callBackForm.attachEvent("onButtonClick",function(id){
    		if(id == "saveInstrction"){
    			if(callBackForm.validate()){
    				var data = {value:callBackForm.getItemValue("winCallbackMsg")};
    				insertCallMsg(instructData[arrayIndex]["INSTRUCTION_ID"],instructData[arrayIndex]["INSTRUCTION_ID"],data);
    				callBackForm.setItemValue("winCallbackMsg","");
    			}
    		}
    		if(id == "closeInstruction"){
    			 callBackForm.clear();
                 callBackWindow.close();
                 dhxWindow.unload();
    		}
    	})
    
    
};

//鼠标移动到提交按钮上则为false
function submouseover(flag){
	submitFlag = flag;
}

//提交数据 
function insertCallMsg(instructionId,intructRootId,callBackId){
	if(callBackId.value.replace(/(^\s*)|(\s*$)/g, "") != "" && callBackId.value.replace(/(^\s*)|(\s*$)/g, "") != null){
		var data={replyId:instructionId,instructionComment:callBackId.value};
		PortalInstructionAction.insertInstrucInfo(data,function(res){
			if(res == true){
				document.getElementById("winInstructionCall").innerHTML = "";
				var innerMsg = "";
                 PortalInstructionAction.queryAllChildrenInst(intructRootId,function(chidData){
                       if(chidData){
    	                   for (var i = 0;i< chidData.length;i ++){
    		                      for(var j = 0; j < chidData[i]["INSTRUCTION_LEVEL"];j++){
    			                      innerMsg = innerMsg + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
    		                       }
    	                           innerMsg = innerMsg +chidData[i]["USER_NAME"]+'：  '+chidData[i]["INSTRUCTION_COMMENT"]+'<span id="ms'+chidData[i]["INSTRUCTION_ID"]+'"></span> ' +
    	                           '<a id="msgIdBak'+i+'"  onclick="instructioMsgBack(ms'+chidData[i]["INSTRUCTION_ID"]+","+i+');return false;"   style="color: #915833;">回复</a>'+ 
    	                           '<a id="msgIdcommit'+i+'" onmouseover="submouseover(false)"  onmouseout="submouseover(true)" onclick="insertCallMsg('+chidData[i]["INSTRUCTION_ID"]+','+intructRootId+',callBakMsg'+i+');return false;"   style="color: #915833;display:none">提交</a></br>' ;
                           }
    	                  document.getElementById("winInstructionCall").innerHTML = innerMsg;
    	                  dhx.alert("提交成功  !");
                      }
               });
			}else{
				dhx.alert("提交失败 !");
			}
		});
	}else{
		dhx.alert("回复内容不能为空!");
	}
	
	
}







//当前节点的所有下一级节点 
function getChild(arrayIndex){
	var replyId = instructData[arrayIndex]["INSTRUCTION_ID"];
	var allchidData = [];
	for(var i = 0; i < instructData.length; i++){
		if(instructData[i]["REPLY_ID"] == replyId){
			allchidData.push(instructData[i]);
		}
	}
	return allchidData;
}


//领导批示 回复 回复 
function instructioMsgBack(msid,i){
	    var spanId =msid.id;
		document.getElementById("msgIdBak"+i).style.display="none";
		document.getElementById("msgIdcommit"+i).style.display="";
		if(document.getElementById("callBakMsg"+i)){
			emement = document.getElementById("callBakMsg"+i);
			 emement.parentNode.removeChild(emement)
			//input.setAttribute("id","callBakMsg"+i);
			//document.getElementById("callBakMsg"+i).style.display="";
			//document.getElementById("callBakMsg"+i).focus();
		}
		var input = document.createElement("textarea");
		input.setAttribute("id","callBakMsg"+i);
		input.setAttribute("onBlur","displayFun(callBakMsg"+i+","+i+","+spanId+")");
		input.style.width="300px";
		document.getElementById(spanId).innerHTML = '</br>'  + ' &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ' ;
		document.getElementById(spanId).appendChild(input);
		input.focus();
		
};

//鼠标 离开回复框
function displayFun(inputId,i,spanId){
	if(submitFlag){
			while(spanId.childNodes.length > 0){
				spanId.removeChild(spanId.childNodes[0]);
			}
	document.getElementById("msgIdBak"+i).style.display="";
	document.getElementById("msgIdcommit"+i).style.display="none";
	inputId.style.display="none";
	}
}


function resetTabHeight(curNames) {
    var divId = moduleMenu[curName]["div_id"];	//	获取容器
    var div = $(divId);
    div.style.overflowY = "scroll";//overflow-y: scroll;
    div.style.overflowX = "auto";//hidden
    return;
    var tabId = moduleMenu[curName]["TAB_ID"];
    var ht = $('rpt_table_' + tabId).rows * 21 + 60;
    if (ht < 200)
        ht = 200;
    div.style.height = (ht - 30) + "px";
    $("rpt_tabbar").style.height = ht + "px";
    //alert($("rpt_tabbar").style.height+"_"+div.style.height);
}
//检查条件是变化并刷新数据
function checkDataFresh() {
    var curName;
    if (arguments.length){
    	curName = arguments[0];
    }else{
    	curName = currentTab;
    }
        
    if (report_level_id > moduleMenu[curName].ROLLDOWN_LAYER) {
            dataAreaId = "";
            report_level_id = moduleMenu[curName].ROLLDOWN_LAYER;
    }
    if (inited == false || ( curName != currentTab && moduleMenu[curName]["MIN_DATENO"] && moduleMenu[curName]["MAX_DATENO"])) {
        var min_date = moduleMenu[curName]["MIN_DATENO"] + "";
        var max_date = moduleMenu[curName]["MAX_DATENO"] + "";
        if (min_date && max_date) {
            min_date = min_date.substring(0, 4) + "-" + min_date.substring(4, 6) + "-" + min_date.substring(6);
            max_date = max_date.substring(0, 4) + "-" + max_date.substring(4, 6) + "-" + max_date.substring(6);
            myCalendar.setSensitiveRange(min_date, max_date);
            if (dataDateNoBak > parseInt(moduleMenu[curName]["MAX_DATENO"]) || dataDateNoBak < parseInt(moduleMenu[curName]["MIN_DATENO"])) {
                dataDateNoBak = moduleMenu[curName]["MAX_DATENO"] + "";
            }
        }
    }
    auditData(curName);
    return true;
}
function auditData(curName) {
    PortalCommonCtrlr.getDataAudit(moduleMenu[curName]["TAB_ID"]
            , dataDateNoBak ? dataDateNoBak : dataDateNo, provinceCode, function (res) // 以全省则只传 '0000' dataLocalCode
       {
           if (res && !res[0]) {
               alert("友好提示( " + curName + " , " + (dataDateNoBak ? dataDateNoBak : dataDateNo) + "日):\n       " + (res[1]?res[1]:'') +
                       "\n       系统将自动跳转到最新可用数据日期:" + moduleMenu[curName]["EFFECT_DATENO"]);
               dataDateNoBak = moduleMenu[curName]["EFFECT_DATENO"] + "";
               myCalendar.setDate(dataDateNoBak.substring(0, 4) + '-' + dataDateNoBak.substring(4, 6) + '-' + dataDateNoBak.substring(6, 8));
           } else if (res && res[0]) {
               $("index_exp_title").innerHTML = "<div style='float:left;'>数据情况</div>";
               if (res[2]){
            	    $("index_exp").innerHTML = "<p style='font-weight:600;color:red;'>" + curName + dataDateNoBak + "日异动说明：" + (res[2] ? res[2] : "" + "</p>");
               }else{
            	    $("index_exp").innerHTML = "";//curName+dataDateNoBak+"日数据无异常";
               }
           } else {
               $("index_exp_title").innerHTML = "<div style='float:left;'>数据情况</div>";
               $("index_exp").innerHTML = "";//curName+dataDateNoBak+"日数据无异常";
           }
           loadTabData(curName);
       });
}
/**
 * 加载某个标签页的数据
 * @param curName
 */
function loadTabData(curName) {
    var tbb_rpt = $("rpt_tabbar");
    var padiv = tbb_rpt.getElementsByTagName("DIV")[2];
    padiv.style.textAlign = "right";
    padiv.insertBefore($("tab_tip_div"), padiv.lastChild);
    $("tab_tip_div").style.display = "inline";
    $("tab_tip_div").style.textAlign = "right";
    $("tab_tip_div").innerHTML = "日期：" + dataDateNoBak + " 本地网：" + areaNames[dataLocalCode];
    if (report_level_id > 2) {
        $("tab_tip_div").innerHTML += "->" + areaNames[dataAreaId];
    }
    if (loadSucceed && dataLocalCode == moduleMenu[curName]["dataLocalCode"] &&
            dataDateNoBak == moduleMenu[curName]["dataDateNo"] &&
            dataAreaId == moduleMenu[curName]["dataAreaId"]) // 条件发生变化，需要刷新
    {
        currentTab = curName;
        resetTabHeight(curName);
        return;
    }
    loadSucceed = false;
    //设置日历控件的日期
    myCalendar.setDate(dataDateNoBak.substring(0, 4) + '-' + dataDateNoBak.substring(4, 6) + '-' + dataDateNoBak.substring(6, 8));
    var po = {};//ReportPO
    po.localCode = ((dataAreaId&&dataAreaId!='0')?dataAreaId:dataLocalCode);
    po.indexCd = "";
    po.dateNo = dataDateNoBak;
    po.tabId = moduleMenu[curName]["TAB_ID"];
    var rptIndexs = moduleMenu[curName]["rpt_index"];
    var cols = [];
    for (var i = 0; i < rptIndexs.length; i++) {
        cols[i] = rptIndexs[i]["COL_EN_NAME"];
    }
    //alert("cols:"+cols);
    var divId = moduleMenu[curName]["div_id"];
    var div = $(divId);
    div.innerHTML = "<center>数据加载中，请稍等....<center>";
    writeLog(moduleMenu[curName].TAB_ID);
    PortalCommonCtrlr.getTableData(po, cols, {callback:function (res) {
        if (res == null) {
            alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
            return;
        }
        dataDateNo = dataDateNoBak;
        moduleMenu[curName]["dataDateNo"] = dataDateNo;
        moduleMenu[curName]["dataLocalCode"] = dataLocalCode;
        //moduleMenu[curName]["dataAreaId"] = dataAreaId;
        currentTab = curName;
        buildGrid(res);
        loadSucceed = true;
        $("div_src").style.display = "none";
        $("div_src").style.zIndex = "-1";
    }
    });
}
var indexCdColWaring;
//是否预警
var isWaring = function(indexArray,dataValue,col){
	if(parseInt(dataValue) == "NaN" || (typeof indexArray == "undefined" || indexArray == undefined || indexArray == "undefined" || indexArray == null))
		return false;
	if(indexArray.length < 0)
		return false;
	for(var r = 0; r < indexArray.length; r++){
		var waringType = indexArray[r].WARING_TYPE;
		var value = indexArray[r].WARING_VALUE;
		var value2 = "";
		var columnId = indexArray[r].COLUMN_ID;
		if((columnId == null || columnId == "null" || columnId == "") ||
				((1*col+1) == 1*indexArray[r].SHOW_ORDER_ID)){//整行或相同列才判断
			if(waringType == "BTWEEN AND"){
				value2 = indexArray[r].WARING_VALUE2;
			}
			return judWaring(waringType,dataValue,value,value2);
		}
	}
	return false;
}
judWaring = function(waringType,dataValue,value,value2){
	if(waringType == ">="){
		if(1*dataValue >= 1*value)
			return true;
		else
			return false;
	}else if(waringType == "<="){
		if(1*dataValue <= 1*value)
			return true;
		else
			return false;
	}else if(waringType == "<"){
		if(1*dataValue < 1*value)
			return true;
		else
			return false;
	}else if(waringType == ">"){
		if(1*dataValue > 1*value)
			return true;
		else
			return false;
	}else if(waringType == "="){
		if(1*dataValue == 1*value)
			return true;
		else
			return false;
	}else if(waringType == "!="){
		if(1*dataValue != 1*value)
			return true;
		else
			return false;
	}else if(waringType == "BTWEEN AND"){
		if(1*dataValue <= 1*value2 && 1*dataValue >= 1*value)
			return true;
		else
			return false;
	}
}
/**
 * 创建报表表格
 * @param data
 */
function buildGrid(data) {
    var divId = moduleMenu[currentTab]["div_id"];	//	获取容器
    var div = $(divId);
    var tabId = moduleMenu[currentTab]["TAB_ID"];
    /*根据tabId获取预警信息*/
    PortalCommonCtrlr.getIndexCdWaringMes(tabId,{
    	async:false,
    	callback:function(mapList) {
    		indexCdColWaring = mapList;
    	}
    });
    var rptIndexs = moduleMenu[currentTab]["rpt_index"];
    var columnLength = rptIndexs.length;//表格拥有的数据列总数
    var str = '<table id=rpt_table_' + tabId + ' rows=' + data.length + '  border="0" style="background-color:#eeeeee;border-spacing:1px;border-collapse: separate;" cellspacing="1" cellpadding="1" width=' +
            (div.offsetWidth - 20) + 'px bgcolor="#eeeeee" class="data_grid"><thead calss=fixedHeader ><tr class=Head_TD >';
    //根据列单位位置：0:位于表头1:位于数据后 显示单位
    for (var i = 0; i < rptIndexs.length; i++) {
        var COL_EN_NAME = rptIndexs[i]["COL_EN_NAME"];
        //单位
        var columnCompany = "";
        if(rptIndexs[i]["COLUMN_COMPANY_POS"] != null && 1*rptIndexs[i]["COLUMN_COMPANY_POS"] == 0){
        	if(rptIndexs[i]["COLUMN_COMPANY"] != null && rptIndexs[i]["COLUMN_COMPANY"] != "" && rptIndexs[i]["COLUMN_COMPANY"] != "null")
        		columnCompany = "("+rptIndexs[i]["COLUMN_COMPANY"]+")";
        }
        
        str += "<th height='23px' class='pad5' align='center' onmouseover='tipHit()' " +
                " width='" + parseInt(100 / rptIndexs.length) + "%' id='rpt_" + tabId + "_head_" + i;
        str += "' YName='DIM_NAME' YValue='" + rptIndexs[i]["COL_CHN_NAME"] + "' dim_name='" + rptIndexs[i]["COL_CHN_NAME"] + "' ";
        str += " style='" + tdStyle + "'  >" + rptIndexs[i]["COL_CHN_NAME"] + columnCompany + "</th>";
    }
    
    if (report_level_id < moduleMenu[currentTab].ROLLDOWN_LAYER) {
        str += "<th class='pad5' nowrap algin='center'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>";
    }
    str += "</tr></thead>";
    var indexCdCode = "";
    for (var i = 0; i < data.length; i++) {
        if (i % 2 == 0){
        	str += "<tr class='Row_TD " + "' onmouseover='tr_onmouseover(this)' onmouseout='tr_onmouseoutR(this)' onclick='tr_onclick(this)'>";
        }else{
        	str += "<tr class='Alt_Row_TD " + "' onmouseover='tr_onmouseover(this)' onmouseout='tr_onmouseoutA(this)' onclick='tr_onclick(this)'>";
        }
        var start = 0;
        
        for (var j = start; j < columnLength; j++) {
        	if(j == 0)
        		indexCdCode = data[i][2 + columnLength];
        	//判断是否需要预警
        	var isWaringFlag = false;
        	if(j > 0)
    			isWaringFlag = isWaring(indexCdColWaring[indexCdCode],data[i][j],j);
        	var fontStyle_s = "";//预警的颜色
        	var fontStyle_e = "";//预警的颜色e
        	if(isWaringFlag){
        		fontStyle_s = "<font style=\"color='red'\">";
        		fontStyle_e = "</font>"
        	}
            /*            var index = j - 4;
             index = (index > 0 ? index : 0);*/
            //单位
	        var columnCompany = "";
	        if(rptIndexs[j]["COLUMN_COMPANY_POS"] != null && 1*rptIndexs[j]["COLUMN_COMPANY_POS"] == 1){
	        	if(rptIndexs[j]["COLUMN_COMPANY"] != null && rptIndexs[j]["COLUMN_COMPANY"] != "" && rptIndexs[j]["COLUMN_COMPANY"] != "null")
        			columnCompany = "("+rptIndexs[j]["COLUMN_COMPANY"]+")";
	        }
            var COL_EN_NAME = "";
            str += "<td id='rpt_" + tabId + "_" + i + "_" + j + "' class='pad10' nowrap onclick='openArea(" + i + "," + j + ")' ";
            var tdStyle = "";
            if (j == 0) {
                str += " align=left  style='padding-left:15px;' index_cd='" + data[i][2 + columnLength] + "' index_name='" + data[i][3 + columnLength] + "' ";
                str += "  onmouseover='tipHit()' ";
                str += " >" + data[i][3 + columnLength] + columnCompany + "</td>";
            }
            else {
                str += "onmouseover='tdInsertDiv(this.id)' onmouseout='tdDeleteDiv(this.id)'  align=right  style='padding-right:0px;" + tdStyle + "' ";
                str += " >"+ fontStyle_s + formatDataView(data[i][j], rptIndexs[j], data[i][3 + columnLength]) + columnCompany + fontStyle_e + "<img title='点击添加批示' src='./images/20.png'  onclick='insertNotice("
                                        + i + "," + j + ","+formatDataView(data[i][j], rptIndexs[j], data[i][3 + columnLength])+");' style='visibility: hidden;height: 20px;width: 10px;margin-left: 5px;margin-right: 0px;padding-left: 0px;' id='rpt_" + tabId + "_" + i + "_" + j + "div' /></td>";
            }
//            if (rptType != 0 && j == 3)j++;
        }
        if (report_level_id <= moduleMenu[currentTab].ROLLDOWN_LAYER) {
            str += "<td nowrap align='center' onclick='openArea(" + i + "," + rptIndexs.length +
                    ")' id='rpt_" + tabId + "_" + rptIndexs.length + "' class=unl  >辖区</td>";
        }
        str += "</tr>";
    }
    str += "</table>";
    div.innerHTML = str;
    if (data.length) {//有数据
    	if(isShowChart){//有图形（是默认有图形）
    		if( moduleMenu[currentTab].DEFAULT_GRID){//有默认值
    			var tmp = moduleMenu[currentTab].DEFAULT_GRID.split(",");
		        //openArea调试
		        openArea(parseInt(tmp[0]), parseInt(tmp[1]));
    		}else{
    			openArea(0, 1);
    		}
    	}else {//没有图形
        	noShowChart();
    	}
    }else {//没有数据
        noShowChart();
    }
    
    resetTabHeight(currentTab);
    delete str;
}
//无图形式样设置
noShowChart = function(){
	$("rpt_tabbar_td").style.height = 400+"px";//变更数据表格高度
    $("hiddenChartTrId1").style.display = "none";
    $("hiddenChartTrId2").style.display = "none";
    $("index_title").style.display = "none";
    $("RadioOption").style.display = "none";
}
//有图形式样
showChart = function(){
	$("rpt_tabbar_td").style.height = 200+"px";//变更数据表格高度
    $("hiddenChartTrId1").style.display = "";
    $("hiddenChartTrId2").style.display = "";
    $("index_title").style.display = "";
    $("RadioOption").style.display = "";
}
var ORTD = null;
/**
 * 设置有效表格单元格的样式
 * @param row
 * @param col
 * @param src
 */
function setActiveTD(row, col) {
	var tabId = moduleMenu[currentTab].TAB_ID;
    var __ORTD = $("rpt_" + tabId+ "_" + row + "_" + col);
    if (ORTD) {
        ORTD.style.backgroundColor = "";
        ORTD.style.color = "";
    }
    ORTD = __ORTD;
    if (ORTD && col != 0) {
        ORTD.style.backgroundColor = "highlight";
        ORTD.style.color = "highlighttext";
    } else {
        return;
    }
    var indexName=$("rpt_"+tabId+"_"+row+"_0").getAttribute("index_name").replaceAll("\\#",'').replaceAll("\\$",'');
    var dimName =$("rpt_"+tabId+"_head_"+col).getAttribute("dim_name").replaceAll("\\#",'').replaceAll("\\$",'')
    $('index_title').innerHTML = currentTab+'->'+indexName+'->'+dimName;
}
//单元格单击事件 
function openArea(row, col) {
    setActiveTD(row, col);
    if (col == 0) {
        return;
    }
    var rptIndexs = moduleMenu[currentTab]["rpt_index"];
    var tabId = moduleMenu[currentTab].TAB_ID;
    var rd = $("rpt_" + tabId + "_" + row + "_0");
    var indexCd = rd.getAttribute("index_cd");
    if(indexCd){
    	//钻取
    	if (col == rptIndexs.length){//dateNo=20110821&indexTypeId=8&indexCd=JZFX_IND_1&indexName=%E4%B8%AD%E5%9B%BD%E7%94%B5%E4%BF%A1&reportLevelId=2
		    var rl = parseInt(report_level_id) + 1;
		    var indexCd = rd.getAttribute("index_cd");
		    var indexFormat = (rd.getAttribute("index_name").indexOf("\%") >= 0);
		    var indexName = rd.getAttribute("index_name").replaceAll("\\#", '').replaceAll("\\$", '');
		    if (userInfo['localCode'] != '0000') {
		        dataLocalCode = userInfo['localCode'];
		    }
		    var url = "/portalCommon/module/portal/areaDrill.jsp?dateNo=" + dataDateNoBak + "&indexTypeId=" + tabId
		                + "&indexCd=" + indexCd + "&indexName=" + indexName + "&report_level_id=" + rl + "&localCode=" + dataLocalCode + "&indexFormat=" + (indexFormat ? 1 : 0);
		    url = encodeURI(url);
		    openTab(tabId + "_" + rl + "_" + indexCd, indexName, url, 0);
		 }else{//图形刷新
		        var hd = $("rpt_" + tabId + "_head_" + col);
		        chartFieldName = rptIndexs[col].COL_EN_NAME;
		        chartIndexCd = rd.getAttribute("index_cd");
		        //需要判断是否有图形
		        PortalCommonCtrlr.isShowChartByColIdAndTabId(indexCd,tabId,(1*col+1),function(boo){
		        	if(boo){
		        		showChart();
		        		fchart();
		        	}else{
		        		noShowChart();
		        	}
		        });
		        
		 }
    	PortalInstructionAction.getTimeRodi(indexCd,tabId,function(data){
    		var radioInput = "";
    		if(data && data.length > 0){
    			radioInput = "时间段选择：";
    			for (var i = 0;i<data.length;i++){
    				if(i == 0 ){
    					chartRadio = data[i]["INTERVAL_VALUE"];
    					radioInput = radioInput + ' <input name="chartDateRadio" type=radio value="'+data[i]["INTERVAL_VALUE"]+'" checked id="chartDateRadio'+data[i]["RULR_ID"]+'" onClick="chartDateChg()"/><label for="chartDateRadio'+data[i]["RULR_ID"]+'">'+data[i]["RULE_NAME"]+'</label>';
    				}else{
    					radioInput = radioInput + ' <input name="chartDateRadio" type=radio value="'+data[i]["INTERVAL_VALUE"]+'"  id="chartDateRadio'+data[i]["RULR_ID"]+'" onClick="chartDateChg()"/><label for="chartDateRadio'+data[i]["RULR_ID"]+'">'+data[i]["RULE_NAME"]+'</label>';
    				}
    			}
    			document.getElementById("RadioOption").innerHTML = radioInput;
    		}
    	});
    }
}


//曲线图
function fchart() {
    var localCode = dataLocalCode;
    var chart = new FusionCharts("<%=path%>/portal/resource/swf/FusionCharts/MSLine.swf", "ChartId", $("rpt_tabbar").offsetWidth + "", "200");
    PortalInstructionAction.queryChartData(moduleMenu[currentTab]["TAB_ID"],dataDateNoBak,chartIndexCd,dataLocalCode,chartRadio,function(data){ 
   	  if(!data){
		 alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
		 return;
	  }
   	  
   	  var chartData={};
   	  var maxVal=0;
   	  var minVal=0;
   	  for(var j = 0; j < data.length; j ++){
   		  var chartData=data[j]["listData"];
   		   if(chartData.length > 0){
   			  for(var i = 0;i < chartData.length;i++){
   		         if(!chartFieldName){
   			        chartFieldName = "VALUE2";
   		          }
   		         if(chartData[i][chartFieldName] > maxVal){
   			        maxVal = chartData[i][chartFieldName];
   		         }
   		         if(minVal == 0 && chartData[i][chartFieldName] != 0){
   			         minVal = chartData[i][chartFieldName];
   		         }else if(chartData[i][chartFieldName] < minVal){
   		        	  minVal = chartData[i][chartFieldName];
   		         }
   	         }
   		  }
   	  }
   	  
   	  var dataStr = "";
   	  var ruleType = -1;
   	  
   	  for(var j = 0; j < data.length; j ++){
   		  if(j == 0){
   			  monthDate = data[j]["listDate"];
   			  ruleType = data[j]["RULE_TYPE"];
   		  }
   		  if(1*ruleType == 1 || 1*ruleType == 3)
   		  	  dataStr = dataStr +" <dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+data[j]["BROKEN_LINE_COLOR"]+"' seriesName='"+data[j]["RULE_NAME"]+"' color='"+data[j]["BROKEN_LINE_COLOR"]+"' anchorBorderColor='FFFFFF' >" + geSetVal(data[j]["listData"],data[j]["listDate"],data[j]["RULE_NAME"]) +"</dataset>";
   	  	  else if(data[j]["RULE_TYPE"] == 2){//月
   	  		  dataStr = dataStr +" <dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+data[j]["BROKEN_LINE_COLOR"]+"' seriesName='"+data[j]["RULE_NAME"]+"' color='"+data[j]["BROKEN_LINE_COLOR"]+"' anchorBorderColor='FFFFFF' >" + geSetValMoth(data[j]["listData"],data[j]["listDate"],data[j]["RULE_NAME"]) +"</dataset>";
   	  	  }
   	  }
   	  
   	  /*没图形信息，那么不显示*/
   	  if(dataStr.length > 5){
   		  showChart();
   		  var res = "<chart   formatNumber='1' labelStep='2' lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	   	  "baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='0' " +
	   	  "hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	   	  "showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	   	  "numdivlines='4' adjustDiv='0' numVdivlines='6'  yaxisminvalue='"+minVal+"' yaxismaxvalue='"+maxVal+"' rotateNames='0'"+
	   	  "chartRightMargin='20' chartLeftMargin='10'>";
	   	  if(1*ruleType == 1 || 1*ruleType == 3)
	   	  	  res = res + "<categories>" + getCategory(monthDate) + "</categories> " + dataStr +"</chart>";
	   	  else if(1*ruleType == 2){//月
	   		  res = res + "<categories>" + getCategoryMoth(monthDate) + "</categories> " + dataStr +"</chart>";
	   	  }
	   	  chart.setDataXML(res);
	      chart.render("chartdiv");
   	  }else{//隐藏图形域
   		  noShowChart();
   	  }
    });
}
/**
 * 获取月图形时间节点
 */
getCategoryMoth = function(date){
	var categoryStr = "";
	for(var i = 0;i < date.length;i++){
	      categoryStr = categoryStr + "<category name='"+date[i].substr(2,2)+"."+date[i].substr(4,2)+"'/>"
	}
	return categoryStr;
}


/**
 *获取图形时间节点 
 * @param data 
 */
function getCategory(monthDate){
	var categoryStr = "";
	for(var i = 0;i < monthDate.length;i++){
	      categoryStr = categoryStr + "<category name='"+monthDate[i].substr(4,2)+"."+monthDate[i].substr(6,2)+"'/>"
	}
	return categoryStr;
	
}
/**
 * 获取月图形当前值
 */
geSetValMoth = function(data,date,hoverText){
	var setStr = "";
	for(var j = 0; j< date.length;j++){
		var temp = "";
		for(var i = 0;i < data.length;i++){
			if( date[j].substr(0,6) == data[i]["MONTH_NO"]){
				temp=  "<set value='"+data[i][chartFieldName]+"' hoverText='"+hoverText+"： "+data[i][chartFieldName]+"'/>";
			}
	   }
		if(temp == ""){
			setStr = setStr + "<set value='0' hoverText='"+hoverText+"： 0'/>";
		}else{
			setStr = setStr + temp;
		}
	}
	return setStr;
}
/**
 *获取图形当期值
 * @param data 
 */
function geSetVal(data,date,hoverText){
	var setStr = "";
	for(var j = 0; j< date.length;j++){
		var temp = "";
		for(var i = 0;i < data.length;i++){
			if( date[j] == data[i]["DATE_NO"]){
				temp=  "<set value='"+data[i][chartFieldName]+"' hoverText='"+hoverText+"： "+data[i][chartFieldName]+"'/>";
			}
	   }
		if(temp == ""){
			setStr = setStr + "<set value='0' hoverText='"+hoverText+"： 0'/>";
		}else{
			setStr = setStr + temp;
		}
	}
	   
	return setStr;
}
/**
 *获取图形当期值
 * @param data 
 */
function gelastSetVal(date,date){
	var setStr = "";
	if(lastData.length == 0){
		for(var i = 0;i < monthDate.length;i++){
	      setStr = setStr + "<set value='0' hoverText='上月同期 ： 0'/>";
	}
	}else{
		for(var j = 0; j< monthDate.length;j++){
		var temp = "";
		for(var i = 0;i < lastData.length;i++){
			if( monthDate[j] == lastData[i]["DATE_NO"]){
				temp=  "<set value='"+lastData[i][chartFieldName]+"' hoverText='当期值： "+lastData[i][chartFieldName]+"'/>";
			}
	   }
		if(temp == ""){
			setStr = setStr + "<set value='0' hoverText='当期值： 0'/>";
		}else{
			setStr = setStr + temp;
		}
	  }
	}
	
	return setStr;
	
}


/**
 *格式化数据
 * @param data
 * @param colAttr
 * @param rowName
 */
function formatDataView(data, colAttr, rowName) {
    try {
        if (data == null)
            return "-";
        //alert(typeof data);
        //#$我的e家
        var styleData;
        if (colAttr) {
            //re = new RegExp('\\#',"g");
            //data=data.replace(re,'&nbsp;&nbsp;&nbsp;&nbsp;');
            if (data == '-88888888')
                styleData = '-';
            else {
                styleData = "dataNum";
                var fl = parseInt(data) < 0;
                if (fl) {
                    styleData = "<span class=red >dataNum</span>";
                    data = data * -1;
                }
                var formats = "#,###.##";
                if (colAttr["COL_CHN_NAME"].indexOf("\\%") > 0 || rowName.indexOf("\\%") > 0)
                    data += "%";
                else
                    data = formatNumber(data, {pattern:formats});
                if (fl)data = "-" + data;
                styleData = styleData.replace("dataNum", data);
            }
        }
        else {
            var re = new RegExp('\\#', "g");
            var fl = data.indexOf("\\$") > 0;
            if (data.charAt(0) == '#') {
                data = data.substring(1);
            }
            styleData = data.replace(re, '&nbsp;&nbsp;&nbsp;&nbsp;');
            if (fl)
                styleData = styleData.replace("\\$", "<img src='./images/jianhao.gif' border=0 value=1 onclick='changCol()' />");
        }
        return styleData;
    } catch (e) {
        return data;
    }
}
function changCol() {
    return;
    var el = event.srcElement;
    if (el.value == 1) {
        el.value = 0;
        el.src = './images/jiahao.gif';
    }
    else {
        el.value = 1;
        el.src = './images/jianhao.gif';
    }
}
/**
 * 显示指标解释。
 * @param flag
 */
function tipHit(flag) {
    var el = event.srcElement || event.target;
    var index_exp = moduleMenu[currentTab]["rpt_index_exp"];
    var indexName = "";
    var indexNum = -1;
    if (el.id.indexOf("_head") > 0) {
        indexName = el.getAttribute("dim_name");
    }
    else {
        indexName = el.getAttribute("index_name");
    }
    if (!indexName)return;
    var _indexName = indexName.replaceAll("\\#", "").replaceAll("\\$", "").trim();
    if (_indexName.indexOf("(") > 0 || _indexName.indexOf("（") > 0) {
        if (_indexName.indexOf("(") > 0)
            _indexName = _indexName.substring(0, _indexName.indexOf("(")).trim();
        else
            _indexName = _indexName.substring(0, _indexName.indexOf("（")).trim();
    }
    for (var i = 0; i < index_exp.length; i++) {
        if (index_exp[i]["INDEX_NAME"] == indexName) {
            indexNum = i;
        }
        else if (index_exp[i]["INDEX_NAME"] == _indexName) {
            indexName = _indexName;
            indexNum = i;
        }
    }
    if (indexNum == -1)    return;
    $("index_exp_title").innerHTML = "<div style='float:left;'>数据解释："+indexName+"</div>";
    $("index_exp").innerHTML = index_exp[indexNum]["INDEX_NAME"] + ":" + index_exp[indexNum]["INDEX_EXPLAIN"];
}
function chartDateChg() {
    if (chartRadio != event.srcElement.value) {
        chartRadio = event.srcElement.value;
        fchart();
    }
}
function dateTimeClick(date, state) {
    var dateNo = myCalendar.getDate(true);//从控件获取
    if (loadSucceed && dateNo == dataDateNo)return;//同一时间点多次点击
    dataDateNoBak = dateNo;
    //alert(dataDateNoBak);
    checkDataFresh();
}
//单击地图调用的js方法
function callJs(v) {
    if (v == '0')    //	返回权限判断
    {
        dataLocalCode = '0000';
        dataAreaId = "0";
    }else {
    	report_level_id = areaLevel[v];
    	if(report_level_id==2){
    		dataAreaId = '0';
    		dataLocalCode = v;
    	}else{
    		dataAreaId = v;
    	}
    }
    checkDataFresh();
}
//界面加载与SWF加载的同步
var isReady = false;
function JsReady() {
    return isReady;
}
isReady = true;

//取得flash对象
function getFlashMovieObject(movieName) {
    if (window.document[movieName]) {
        return window.document[movieName];
    } else if (navigator.appName.indexOf("Microsoft") == -1) {
        if (document.embeds && document.embeds[movieName])
            return document.embeds[movieName];
    }
    else {
        return document.getElementById(movieName);
    }
}

var isOnly = false;

/**
 * 此方法用于地图Falsh与JS方法的绑定，调用时机在于地图Falsh初始化完成之后进行的地图设置。
 */
function setData(mapData) {
	if (userInfo['localCode']&&userInfo['localCode'] != '0000' && userInfo['areaId']) {
	    isOnly = true;
	    gotoArea('A_' + userInfo['localCode'], true);
	}
		
	//[地区,颜色,是否下转]
    var initData = [];
    //读取地域第二级信息
    var localCode = levelArea[2];
    if (localCode) {
        for (var i = 0; i < localCode.length; i++) {
            if (userInfo['localCode'] == provinceCode)
                initData[i] = ['Area_' + localCode[i], '0xF4F3F0','0', 'A_'+localCode[i]];
            else if (userInfo['localCode'] == localCode[i])
                initData[i] = ['Area_' + localCode[i], '0xd2691e', '0', 'A_'+localCode[i]];
            else
                initData[i] = ['Area_' + localCode[i], '0xF4F3F0', '0', 'A_'+localCode[i]];
            if(mapData){
            	if(localCode[i] == mapData[0]["LOCAL_CODE"] ||localCode[i] == mapData[1]["LOCAL_CODE"] ||localCode[i] == mapData[2]["LOCAL_CODE"]  ){
        		  initData[i] = ['Area_'+localCode[i],'0xE86418','0','A_'+localCode[i]];
        	   }
            }
        }
    }
	//alert(initData);
    getFlashMovieObject("myFlash").setMap(initData);
    initData.length=0;
}
function gotoArea(area, isOnly) {
    getFlashMovieObject("myFlash").gotoArea(area, isOnly);
}

function debug(info){
	alert(info);
}

function openTab(menu_id, menu_name, url, type, formname) {
    return parent.openMenu(menu_name, url, 'top');
}
var tdInsertDiv = function (tdId) {
    $(tdId + 'div').style.visibility = "visible";
};
var tdDeleteDiv = function (tdId) {
    $(tdId + 'div').style.visibility = "hidden";
};
var insertNotice = function (rid, cid, value) {
    var __ORTD = $("rpt_" + moduleMenu[currentTab].TAB_ID + "_" + rid + "_" + cid);
    if (ORTD) {
        ORTD.style.backgroundColor = "";
        ORTD.style.color = "";
    }
    ORTD = __ORTD;
    if (ORTD && cid != 0) {
        ORTD.style.backgroundColor = "highlight";
        ORTD.style.color = "highlighttext";
    } else {
        return;
    }
    var rptIndexs = moduleMenu[currentTab]["rpt_index"];
    if (cid == rptIndexs.length)return;
    var tabId = moduleMenu[currentTab].TAB_ID;
    var rptType = moduleMenu[currentTab].RPT_TYPE;
    var rd = $("rpt_" + tabId + "_" + rid + "_0");
    var hd = $("rpt_" + tabId + "_head_" + cid);
    var rname = rd.getAttribute("index_name").replaceAll("\\#", '').replaceAll("\\$", '');
    var dname = hd.getAttribute("YValue").replaceAll("\\#", '').replaceAll("\\$", '');
    var title = "";
    if (rname.indexOf("（") > 0)
        rname = rname.substring(0, rname.indexOf("（"));
    else if (rname.indexOf("(") > 0)
        rname = rname.substring(0, rname.indexOf("("));
    if (dname.indexOf("（") > 0)
        dname = dname.substring(0, dname.indexOf("（"));
    else if (dname.indexOf("(") > 0)
        dname = dname.substring(0, dname.indexOf("("));
    if (rptType == 1) {
        title = dname + "-" + rname;
    }
    else {
        title = rname + "-" + dname;
    }
//	alert(title);
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("addWindow", 0, 0, 250, 310);
    var addWindow = dhxWindow.window("addWindow");
    addWindow.setModal(true);
    addWindow.stick();
    addWindow.setDimension(550, 310);
    addWindow.center();
    addWindow.setPosition(addWindow.getPosition()[0], addWindow.getPosition()[1] - 100);
    addWindow.denyResize();
    addWindow.denyPark();
    addWindow.button("minmax1").hide();
    addWindow.button("park").hide();
    addWindow.button("stick").hide();
    addWindow.button("sticked").hide();
    addWindow.setText("批示内容");
    addWindow.keepInViewport(true);
    addWindow.show();
    //建立表单。
    var addForm = addWindow.attachForm([
        {type:"block", offsetTop:15, list:[
            {type:"input", offsetLeft:40, label:"批示标题：", inputWidth:370, name:"noticeTitle", validate:"NotEmpty,MaxLength[64]"},
            {type:"newcolumn"},
            {type:"label", label:"<span style='color: red'>*</span>"}
        ]},
        {type:"hidden", name:"noticeUser", value:userInfo['userId']},
        {type:"block", list:[
            {type:"input", offsetLeft:40, rows:4, label:"批示内容：", inputWidth:370, name:"noticeContent", validate:"NotEmpty,MaxLength[600]"},
            {type:"newcolumn"},
            {type:"label", label:"<span style='color: red'>*</span>"}
        ]},
        {type:"hidden", name:"noticeDisplayZones", value:dataLocalCode},
        {type:"block", offsetTop:10, list:[
            {type:"button", label:"保存", name:"save", value:"保存", offsetLeft:200},
            {type:"newcolumn"},
            {type:"button", label:"关闭", name:"close", value:"关闭"}
        ]}
    ]);
    //添加验证
    addForm.defaultValidateEvent();
    //查询表单事件处理
    addForm.attachEvent("onButtonClick", function (id) {
        if (id == "save") {
            if (addForm.validate()) {
                //保存
                var data = addForm.getFormData();
                var instructionComment = data.noticeContent;
                data.noticeContent = title + ":" + data.noticeContent;
                data.displayZones = dataLocalCode;
                data.instructionIndexName = title;
                data.instructionIndexCd = rptIndexs[cid].COL_ID;
                data.dataDate = dataDateNo;
                data.instructionLevel = 1;
                data.indexValue = value;
                data.instructionComment = instructionComment;
                PortalInstructionAction.insertInstrucInfo(data,function(rs){
                    if(rs){
                        dhx.alert("新增成功");
                        addForm.clear();
                        addWindow.close();
                        dhxWindow.unload();
                    }else{
                        dhx.alert("对不起，新增出错，请重试！");
                    }
                });
            }
                    
       }
       if (id == "close") {
           addForm.clear();
           addWindow.close();
           dhxWindow.unload();
       }
    });
}
	


/**
 * 指标Data转换器
 */
var indexConvertConfig = {
    idColumnName:"indexCd",
    filterColumns:["","indexName"],
    
     userData:function(rowIndex, rowData) {
        var userData = {};
        userData["tabId"] = rowData["tabId"];
        userData["indexCd"] = rowData["indexCd"];
        return userData;
    },
     cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
        if(columnIndex == 0 && rowData['bj'] == 1) {
        	return 1;
        }else if(columnIndex == 0 && rowData['bj'] == 0){
        	return 0;
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}

var indexDataConverter = new dhtmxGridDataConverter(indexConvertConfig);
var loadRoleParam = new biDwrMethodParam();//loadRole Action参数设置。
var queryform = null;
function configure_user_indicators(){
	var dhxWindow = new dhtmlXWindows();
    var winSize=Tools.propWidthDycSize(15,20,15,20);
    winSize.width=550;
    dhxWindow.createWindow("configureWindow", 0, 0, 750, 480);
    var configureWindow = dhxWindow.window("configureWindow");
    configureWindow.setModal(true);
    configureWindow.setDimension(winSize.width, 400);
    configureWindow.center();
    configureWindow.denyResize();
    configureWindow.denyPark();
    var buttonstr = "";
	configureWindow.setText("配置指标与用户挂钩");	
	configureWindow.keepInViewport(true);
    configureWindow.show();
    var roleLayout = new dhtmlXLayoutObject(configureWindow, "3E");
    roleLayout.cells("a").hideHeader();
    roleLayout.cells("b").hideHeader();
    roleLayout.cells("a").setHeight(50);
    roleLayout.cells("a").fixSize(false, true);
    roleLayout.hideConcentrate();
    roleLayout.hideSpliter();//移除分界边框。
    
    //加载查询表单
    queryform = roleLayout.cells("a").attachForm([
                {type:"settings",position: "label-left"},
                {type:"combo",label:"报表名称：",name:"reportname",inputWidth:270,inputHeight:17,readonly:true},
                {type:"newcolumn"}
            ]);
    
    queryform.defaultValidateEvent();
   
     //加载所有报表类型
     var options = [];
  ReportConfigAction.getReportTabMes(null,{
    	async:false,
    	callback:function(list) {
	    	for(var i = 0; i < list.length; i++){
	    		options.push({text:list[i].TAB_NAME,value:list[i].TAB_ID});
	    	}
    	}
    });
    queryform.getCombo("reportname").addOption(options);
    queryform.getCombo("reportname").selectOption(0,false,false);
     //定义loadRole Action的参数来源于表单queryform数据。
    loadRoleParam.setParamConfig([{
            index:0,type:"fun",value:function(){
        		var data = {};
           	 	var formData=queryform.getFormData();
            	data.formData = formData;
            	return data;
        	}
        }
    ]);
    dwrCaller.addAutoAction("getUserIndicators", "ReportConfigAction.getUserIndicators", loadRoleParam);
    dwrCaller.addDataConverter("getUserIndicators", indexDataConverter);
        
    //添加datagrid
    var middleCell = roleLayout.cells("b");
	middleCell.setHeight(500);
	middleCell.fixSize(true,true);
	middleCell.hideHeader();
    mygrid = middleCell.attachGrid();
    mygrid.setHeader("{#checkBox},指标名称");
    mygrid.setInitWidthsP("5,95");
    mygrid.setColAlign("right,left,center,justify");
    mygrid.setHeaderAlign("center,center");
    mygrid.enableResizing("true,true");
    mygrid.setColTypes("ch,ro");
    mygrid.setColSorting("na,na");
//    mygrid.setEditable(false);    //去除此处代码是为了checkbox行能够点击
    mygrid.setColumnCustFormat(2, validOrNot);//第二列进行转义
    mygrid.enableMultiselect(true);
    mygrid.enableSelectCheckedBoxCheck(1);
    mygrid.enableCtrlC();
    mygrid.setColumnIds("'',indexName");
    mygrid.enableTooltips("true,true");
    mygrid.init();
//    mygrid.showRowNumber();//显示行号
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.getUserIndicators, "json");
    
    //查询表单事件处理
    queryform.attachEvent("onChange", function(id) {
    	    mygrid.clearAll();
            mygrid.load(dwrCaller.getUserIndicators, "json");
    });
    
    var bottomCell = roleLayout.cells("c");
	bottomCell.setHeight(10);
	bottomCell.fixSize(true,true);
	bottomCell.hideHeader();
	importForm = bottomCell.attachForm(
		[{type:"block",list:[
					{type:"button",name:"importBtn",value:"保存",offsetLeft:document.body.clientWidth-1150},
					{type:"newcolumn"},
					{type:"button",name:"close",value:"关闭"}
				]
			}
		]
	);
	importForm.attachEvent("onButtonClick", function(id) {
        if (id == "importBtn") {
                saveUserConfigure(mygrid);
        }else if(id == "close"){
        	configureWindow.hide();
            importForm.unload();
            dhxWindow.unload();
        }
    });
}
var saveUserConfigure = function(mygrid){
	var str="确认是否对用户指标配置修改！";
	dhx.confirm(str,function(r){
		if(r)
		{
			var data = getData(mygrid);
			ReportConfigAction.saveUserConfigure(data,function(rs)
			{
				if(rs)
				{
					dhx.alert("用户指标配置已成功！");
				}else
				{
					dhx.alert("用户指标配置失败，请重试！");
				}
			});
			configureWindow.close();
		}
	});
}
	
	//得到选中需要导入的数据
var getData = function(mygrid){
	var checkRows = mygrid.getCheckedRows(0);
	var columnData = [];
	if(checkRows){
		var rowsId = checkRows.split(',');
		for(var i=0;i<rowsId.length;i++){
        	var rowData = {};
			//var data = mygrid.getRowData(rowsId[i]).data;
			var data = mygrid.getUserData(rowsId[i]);
            rowData["tabId"] = mygrid.getUserData(rowsId[i],"tabId");
            rowData["indexCd"] = mygrid.getUserData(rowsId[i],"indexCd");
        	columnData.push(rowData);
		}
	}
	var obj = queryform.getCombo("reportname");
	var reportname = queryform.getCombo("reportname").getSelectedValue();
	columnData.push(reportname);
	return columnData;
}
</script>
    
