/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
var moduleMenu = null; 
var resData={};
var menuTab=null;
var pageSize = 12;//页面显示条数
var dataCounts = 0;//数据总数
var pageCounts = 0;//总页数
var globZoneTree = null;

/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCode");
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
var zoneConverter=dhx.extend({idColumn:"zoneCode",pidColumn:"zoneParCode",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);

dwrCaller.addAction("querySubZoneCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZoneCode(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
/** Tree head **/
//by qx
tZoneCode=$("zoneCode").value;
tDefaultZoneCode=$("defaultZoneCode").value;
if(tZoneCode!=tDefaultZoneCode){
    $("changeCode").value='1';
    $("changeName").value='横向对比';
}else{
	$("changeCode").value='2';
	$("changeName").value='横向对比';
}
var changeCode = $("changeCode").value;
var changeName=$("changeName").value;
var indexInit=function(){
	//1.加载地域树 
    loadZoneTreeChkBox(tZoneCode,queryform);
    //2.加载图形
    loadSetChart($("tabId").value,changeCode);
    //初始化公告列表
    excuteNotice(1,dataCounts,pageCounts,4);
    //初始化待办
    toDeal();
}
function  onSelect(obj){
  if(obj.value=='1'){ //日重点报表
		$("tdDateTime").innerHTML="日 期:";
	    $("divDay").style.display =   "block";
	    $("divMonth").style.display = "none";
	}else{//月重点报表
		$("tdDateTime").innerHTML="月 份:";
	    $("divDay").style.display =   "none";
	    $("divMonth").style.display = "block";
	}
     loadSetChart($("tabId").value,$("changeCode").value);
	 
}
//查询
var queryData=function(){
	changeCode=getChangeCode(); //by qx
	$("changeName").value="横向对比";
	loadSetChart($("tabId").value,changeCode);
    return true;
}

//加载配置图形
function loadSetChart(tabId,changeCode){
	 var map=new Object();
	     map.tabId=tabId;
	     map.zoneCode= $("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
	     map.changeCode=changeCode;
	   if(tabId=="1"){
		   map.dataDate=$("dayTime").value;
		}else{
	       map.dataDate=$("monthTime").value;
	    }
	 dhx.showProgress("正在执行......");
     PortalCommonCtrlr.loadSetChart_1(map, {callback:function (res) {
   		dhx.closeProgress();
    	         if(res != null){
                        buildChart(res,changeCode);
		         }else{
		        	  //$('top').innerHTML='<br/>没找到任何数据';
		         }
		   }
		});
}

//构建图形
function buildChart(res,changeCode){
	var map=new Object();
    map.changeCode=changeCode;
	var tableStr="<table id='topTable' width='100%' height='auto!important' border='0'  cellpadding='0px' cellspacing='2px'>";
	     tableStr +="<tr>";
	     for(var i=0;i<res.length;i++){
			   if((i+1)%2 != 0){
		tableStr +="<td width='50%'>"
						      +"<table style='border: 1px solid #87CEFF;' width='100%' height='auto!important'  border='0'  cellpadding='0' cellspacing='0'>"
							                +"<tr height='20px' style='background:url("+base+"/images/fpage_04.gif);'>"
							                   +"<td nowrap align='left' class='title_ma1'>"
							                     +"<span style='font:12px;font-weight:bold;'>"+res[i].TAB_NAME+'->'+res[i].INDEX_NAME+'->'+res[i].SHOW_NAME+"</span>"                                    
							                   +"</td>"
							                   	+"<td align='right'>"
							                       +"<input type='button' id='ps' name='ps' class='poster_btn1'  value='批 示'  onclick=\"psInfo('"+res[i].INDEX_CD+"','"+res[i].INDEX_NAME+"')\"  style='width:60px;'/>"
							                   +"</td>"
							                 +"</tr>"
							                 
							                 +"<tr>"
							                        +"<td colspan='2'>"
							                              +"<div id='chartdiv_"+res[i].ID+"'></div>"
							                         +"</td>"
							                 +"</tr>"
							  +"</table>" 
			        +"</td>";
				}else{
		  tableStr +="<td width='50%'>"
						      +"<table style='border: 1px solid #87CEFF;' width='100%' height='auto!important'  border='0'  cellpadding='0' cellspacing='0'>"
							                +"<tr height='20px' style='background:url("+base+"/images/fpage_04.gif);'>"
							                   +"<td nowrap align='left' class='title_ma1'>"
							                     +"<span style='font:12px;font-weight:bold;'>"+res[i].TAB_NAME+'->'+res[i].INDEX_NAME+'->'+res[i].SHOW_NAME+"</span>"                                       
							                   +"</td>"
							                   	+"<td align='right'>"
							                       +"<input type='button' id='city' name='city' class='poster_btn1'  value='"+$("changeName").value+"'  onclick=\"lookCity(this,'"+res[i].INDEX_CD+"','"+res[i].SHOW_ID+"','"+res[i].ID+"')\"  style='width:70px;'/>"
							                   +"</td>"
							                 +"</tr>"
							                 
							                 +"<tr>"
							                        +"<td colspan='2'>"
							                               +"<div id='chartdiv_"+res[i].ID+"'></div>"
							                         +"</td>"
							                 +"</tr>"
							  +"</table>" 
			           +"</td>";
		  if((i+1)!= res.length)
		  {
		   tableStr +="</tr>"
                     +"<tr>";
		  }	
		}
	   }
	       tableStr+="</tr>";
	 tableStr +="</table>";
    $('top').innerHTML=tableStr;
    //新建图形
    newChart(res);
}
//新建图形
function newChart(res){
	 for(var i=0;i<res.length;i++){
		   var chart = new FusionCharts(base+"/js/Charts/"+res[i].CHART_NAME, "ChartId_"+res[i].ID, "100%", "200","0", "0");//MSLine.swf
							   chart.setDataXML(res[i].XML);
					           chart.render("chartdiv_"+res[i].ID);
		}
	    $("tabId").style.display = "block";
	 	$("div_src").style.display = "none";
        $("div_src").style.zIndex = "-1";
        
}


function loadExp(){
var map=new Object();
    map.tabId=$("tabId").value;
    PortalCommonCtrlr.getIndexExp(map, {callback:function (data) {
    	indexExp(data);
	}});
}
function indexExp(data){
	var tableStr="<table width='100%' border='0'  cellpadding='0' cellspacing='0'>";
		for(var i=0;i<data.length;i++){
        tableStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
				      +"<td align=\"left\"><font style=\"font-weight:bold;\" >"+data[i].INDEX_NAME+'：'+"</font>"+data[i].INDEX_EXPLAIN+"</td>"
				  +"</tr>";
		}
	   tableStr +="</table>"; 
       index_exp.innerHTML=tableStr;
}

/**
 * 钻取
 * @param {Object} tabId
 * @param {Object} indexId
 */
function openArea(tabId,tabName,indexId,indexName){
	   var  dataDate="";
	
	   if(tabId=="1"){dataDate=$("dayTime").value;}else{dataDate=$("monthTime").value;}
        var  zoneCode=  $("zoneCode").value;
        var url = base+"/portalCommon/module/portal/areaDrill.jsp?dataDate=" + dataDate + "&tabId=" + tabId
                   + "&indexId=" + indexId + "&indexName=" + indexName + "&zoneCode=" + zoneCode +"&tabName=" +tabName;
        url = encodeURI(url);
        var param="height="+screen.availHeight+"px,width="+screen.availWidth+"px,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no";
        window.open (url,'newwindow',param);
      //openTab(tabId + "_" + indexId, indexName, url, 0);
}

function openTab(menu_id, menu_name, url, type) {
    return openMenu(menu_name, url, 'top');
}
/**
 * 指标批示
 */
function psInfo(indexCd,indexName){
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
            {type:"input", offsetLeft:40, label:"批示名称：", inputWidth:370, name:"noticeTitle", validate:"NotEmpty,MaxLength[64]",value:indexName}
        ]},
        {type:"block",list:[
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
        {type:"hidden", name:"noticeDisplayZones"},
        {type:"block", offsetTop:10, list:[
            {type:"button", label:"保存", name:"save", value:"保存", offsetLeft:200},
            {type:"newcolumn"},
            {type:"button", label:"关闭", name:"close", value:"关闭"}
        ]}
    ]);
    //添加验证
    //查询表单事件处理
       //查询表单事件处理
    addForm.attachEvent("onButtonClick", function (id) {    
    	if (id == "close") {
            addForm.clear();
            addWindow.close();
            dhxWindow.unload();
        }
    });
}

dwrCaller.addAutoAction("indexToDeal","SerProManageAction.indexToDeal");
var excuteNotice = function(currentPage,dataCounts,pageCounts,pageSize){
	var noticeStr = "";
	PortalCommonCtrlr.getNoticeByPara(currentPage,dataCounts,pageCounts,pageSize,{//初始化调用：第1页
		async:false,
		callback:function(obj){
			dataCounts = obj[0];
			pageCounts = obj[2];
			var data = obj[1];
			if(data != null){
				for(var i = 0; i < data.length; i++){ 
                  		noticeStr +="<li class='li_2'><span>"+data[i]["INIT_DATE"]+"</span><a href=\"javascript:showNotice("+data[i]["NOTICE_ID"]+");\" style='cursor:hand'>"+data[i]["NOTICE_TITLE"]+" </a></li>"
					            +"<div id='u156_line'></div>";
				}
			}
		}
	});
	
	document.getElementById("noticeList").innerHTML = noticeStr;
}

/**
 * 打开公告信息列表
 * @param groupId
 */
function openNotice(groupId){
	openMenu("公告信息","/portalCommon/module/portal/noticeList.jsp","top")
}
/**
 *打开登录排名
 * @param groupId
 */
function openVisitRank(groupId){
    openMenu("用户访问情况","/meta/module/mag/log/loginLog.jsp?groupId="+groupId,"top");
}
/**
 *打开应用排名
 * @param groupId
 */
function openAppRank(groupId){
    //openMenu("报表访问情况","/meta/module/mag/log/menuVisitInfo.jsp?groupId="+groupId,"top");
	  openMenu("用户访问情况","/meta/module/mag/log/userVisitNum.jsp?groupId="+groupId,"top");
}
/**
 *打开登录排名
 * @param groupId
 */
function openApplyUser(){
     openMenu("账号申请","/meta/public/userRegister.jsp","top");
}
/**
 *打开省公司审核
 * @param groupId
 */
function openProvAduit(){
     openMenu("省公司审核","/meta/public/provicialAudit.jsp","top");
}
/**
 *打开分公司审核
 * @param groupId
 */
function openPartAduit(){
     openMenu("分公司审核","/meta/public/partAudit.jsp","top");
}

var openMenu=function(menuName,menuUrl,target,menuId,isRefresh){
	var param="height=500px,width=1200px,top=200,left=100,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no";
        window.open (base+menuUrl,'newwindow',param);
}
//展示公告的详细信息
var showNotice = function(noticeId){
	var showMes = "";
	//查找公告信息
	PortalCommonCtrlr.getNoticeById(noticeId,{
		async:false,
		callback:function(data){
			//标题
			showMes += "<div class='show_content2' onMousedown=\"StartDrag(this)\" onMouseup=\"StopDrag(this)\" onMousemove=\"Drag(this)\">";
			showMes += "<a href=\"javascript:closeLayer();\" class=\"closed2\">× 关闭</a><h2 class=\"gg_title2\">"+data["NOTICE_TITLE"]+"</h2>";
			showMes += "<div class=\"xian2\"></div>";
			showMes += "<p><ul class=\"gg_info2\">";
			showMes += "<li>发布日期："+data["INIT_DATE"]+"</li>";
			//公告等级
			var levelname = levelName(1*data["NOTICE_LEVEL"]);
			showMes += "<li>公告等级：<span>"+levelname+"</span></li>";
			showMes += "</ul></p>";
			//公告内容
			showMes += "<p class=\"clear2\"></p>";
			showMes += "<p class=\"gg_content2\">公告内容：<br/>";
			showMes += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + data["NOTICE_CONTENT"] + "</p>";
		    showMes += "<br/><br/>";
			showMes += "<p><ul class=\"gg_info3\"><li>附件下载：<a href=\"#\" onclick=\"downloadattrs('"+data["AFFIX_PATH"]+"')\">"+data["AFFIX_NAME"]+"</a>&nbsp;&nbsp;&nbsp;&nbsp;</li>";
			showMes += "</ul></p></div>";
		}
	});
	//弹出div层
	//document.getElementById("showMesId").innerHTML = showMes;
	openLayer('showMesId',showMes,500,300);
}
//公告等级
var levelName = function(levelId){
	var levelName = "";
	if(levelId == 1)
		levelName = "一般";
	else if(levelId == 2)	
		levelName = "较急";
	else if(levelId == 3)	
		levelName = "加急";
	else if(levelId == 4)	
		levelName = "紧急";
	return levelName;
}
function downloadattrs(file)
{
	document.forms[0].target="hiddenFrame";
 	var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ file;
	window.open(url,"hiddenFrame","");
}
//待办列表
var toDeal=function(){
    var loadParam=initParam();
	$("num_0").innerHTML='';//待处理
	$("num_1").innerHTML='';//处理中
	$("num_2").innerHTML='';//待评估
	$("num_3").innerHTML='';//归档
	var num_0=0;//待处理
	var num_1=0;
	var num_2=0;
	var num_3=0;
	dwrCaller.executeAction("indexToDeal",loadParam,function(data){
			if(data != null){
			        for(var i=0;i<data.length;i++){
			     			 if(data[i].MAIN_STATE==1){
			     				num_0=num_0+1;
							}else if(data[i].MAIN_STATE==2){
								num_1=num_1+1;
							}else if(data[i].MAIN_STATE==3){
							}else if(data[i].MAIN_STATE==4){
							}else if(data[i].MAIN_STATE==5){
								num_3=num_3+1;
							}else if(data[i].MAIN_STATE==6){
								num_2=num_2+1;
								}else{
								}
							}  
			        }
			slowExecute(num_0,num_1,num_2,num_3);
			
	});
}

function slowExecute(num_0,num_1,num_2,num_3){
	
    $("num_0").innerHTML=num_0;
	$("num_1").innerHTML=num_1;
	$("num_2").innerHTML=num_2;
	$("num_3").innerHTML=num_3;
}
//参数初始化公共部分
var initParam=function(){
    var loadParam = new biDwrMethodParam();
    loadParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
        	var  formData=new Object();
            return formData;
        }
        }
    ]);
    return loadParam;
}
/**
 * 打开待办列表
 * @param groupId
 */
function openToDeal(dealId){
	if(dealId=='1'){
	   openMenu("主单待处理","/portalCommon/module/serviceManage/serProManage/indexProblemMain.jsp?problemStep="+dealId,"top");
	}
	if(dealId=='2'){
		openMenu("副单待处理","/portalCommon/module/serviceManage/serProManage/indexProblemMain.jsp?problemStep="+dealId,"top");
	}
	if(dealId=='3'){
		openMenu("待确认","/portalCommon/module/serviceManage/serProManage/indexProblemMain.jsp?problemStep="+dealId,"top");
	}
	if(dealId=='4'){
		openMenu("被退回","/portalCommon/module/serviceManage/serProManage/indexProblemMain.jsp?problemStep="+dealId,"top");
	}
	if(dealId=='5'){
		openMenu("已归档","/portalCommon/module/serviceManage/serProManage/indexProblemMain.jsp?problemStep="+dealId,"top");
	}
	if(dealId=='6'){
		openMenu("待评估","/portalCommon/module/serviceManage/serProManage/indexProblemMain.jsp?problemStep="+dealId,"top");
	}
}

var lookCity = function (obj,indexCd,showId,chartId){
	 // alert(indexCd+showId+chartId);
       var map=new Object();
           map.tabId  =$("tabId").value;
           map.zoneCode =$("zoneCode").value;
	   if($("tabId").value=="1"){
		   map.dataDate=$("dayTime").value;
		}else{
	       map.dataDate=$("monthTime").value;
	    }
		   map.indexCd=indexCd;
		   map.field=showId;
		   tZoneCode=$("zoneCode").value;
	     	if(tZoneCode!='0000') { 
	 			  if(obj.value=='横向对比'){//地市
	 				  obj.value='返回';
	 				  map.changeCode="0";//地市同级
	 				  $("changeCode").value='0';
	 				 $("changeName").value="返回";
	 			  }else if(obj.value=='返回'){//地市
	 				  obj.value='横向对比';
	 				  map.changeCode="1";//地市下一级
	 				  $("changeCode").value='1';
	 				 $("changeName").value="横向对比";
	 			  }
	   		}else{
	   		     if(obj.value=='横向对比'){//省公司下一级
	 				  obj.value='返回';
	 				  map.changeCode="1";//省公司下一级
	 				$("changeCode").value='1';
	 				$("changeName").value="返回";
	 			  }else if(obj.value=='返回'){//省公司下两级
	 				  obj.value='横向对比';
	 				  map.changeCode="2";//省公司下两级
	 				$("changeCode").value='2';
	 				$("changeName").value="横向对比";
	 			  }
	   		}
		dhx.showProgress("正在执行......");   
		PortalCommonCtrlr.loadSet21AreaChart(map, {callback:function (res) {
			 dhx.closeProgress();
    	         if(res != null){
                        build21Chart(res,chartId);
		         }
		   }
		});
}

function  build21Chart(map,chartId){
   $("chartdiv_"+chartId).innerHTML="";
   var chart = new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId_"+chartId, "100%", "200","0", "0");//MSLine.swf
   chart.setDataXML(map.XML);
   chart.render("chartdiv_"+chartId);
}
//获取changeCode
function getChangeCode(){
	 var changeCode="";
	 tZoneCode=$("zoneCode").value;
	if(tZoneCode!="0000"){
		 $("changeCode").value="1";
		 changeCode="1";
	}else{
	    $("changeCode").value="2";
	    changeCode="2";
	}
	$("city").value='横向对比';
	return changeCode;
}
dhx.ready(indexInit);