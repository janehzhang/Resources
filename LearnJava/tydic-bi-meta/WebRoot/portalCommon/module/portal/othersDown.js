/**
 * 页面初始化。
 */
//确认
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();

//当前系统的主页Path
var base = getBasePath();

//DWR
var dwrCaller = new biDwrCaller();
var user= getSessionAttribute("user");

var pageSize = 12;//页面显示条数
var dataCounts = 0;//数据总数
var pageCounts = 0;//总页数

//查询系统参数
var startDate = null;
var endDate = null;

var startDate=$("startDate").value;
var endDate=$("endDate").value;

dwrCaller.addAutoAction("queryProblem","SerProManageAction.queryProblem");
dwrCaller.addAutoAction("indexToDeal","SerProManageAction.indexToDeal");
var userInit=function(){
    excuteNotice(1,dataCounts,pageCounts,4);//通知公告
    toDeal();//待办
    content(startDate,endDate,1,dataCounts,pageCounts,pageSize);//服务问题
}
var excuteNotice = function(currentPage,dataCounts,pageCounts,pageSize){
	var noticeStr = "";
	PortalCommonCtrlr.getNoticeByPara(currentPage,dataCounts,pageCounts,pageSize,{//初始化调用：第1页
		async:false,
		callback:function(obj){
			dataCounts = obj[0];
			pageCounts = obj[2];
			var data = obj[1];
			if(data != null){
				for(var i = 0; i < data.length; i++){ //href="#" onclick="openVisitRank(<%=systemId %>);"
					noticeStr += "<li><a href=\"javascript:showNotice("+data[i]["NOTICE_ID"]+");\">"+data[i]["NOTICE_TITLE"]+"</a>&nbsp;&nbsp;<span>"+data[i]["INIT_DATE"]+"</span></li>";
					if(data.length-i>1){
						noticeStr=noticeStr;
					}
				}
			}
		}
	});
	document.getElementById("noticeList").innerHTML = noticeStr;
}
var content=function(startDate,endDate,currentPage,dataCounts,pageCounts,pageSize){
	$("tasksList").innerHTML='';
	 var tableStr="<table width='880' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
		  +"<tr>"
		    +"<td height='16' bgcolor='#cde5fd'><strong>工单流水号</strong></td>"
		    +"<td bgcolor='#cde5fd'><strong>工单类型</strong></td>"
		    +"<td bgcolor='#cde5fd'><strong>紧急程度</strong></td>"
		    +"<td bgcolor='#cde5fd'><strong>申告时间</strong></td>"
		    +"<td bgcolor='#cde5fd'><strong>计划完成时间</strong></td>"
		    +"<td bgcolor='#cde5fd'><strong>投诉类型</strong></td>"
		    +"<td bgcolor='#cde5fd'><strong>受理号码</strong></td>"
		    +"<td bgcolor='#cde5fd'><strong>投诉内容</strong></td>"  
		    +"<td bgcolor='#cde5fd'><strong>操作</strong></td>"
		 +"</tr>";
	SerProManageAction.indexQueryProblem(startDate,endDate,currentPage,dataCounts,pageCounts,pageSize,{
		async:false,
		callback:function(obj){
			dataCounts = obj[0];
			pageCounts = obj[2];
			var data = obj[1];
			if(data != null){
					var str="";
			        for(var i=0;i<data.length;i++){
		              var str ="<a href=\"javascript:void(0)\"  onclick=\"checkProcess('"+data[i].MAIN_PROBLEM_ID+"')\" >查看流程</a>";
			          tableStr =tableStr+
			             "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
			  		    +"<td>"+data[i].MAIN_PROBLEM_ID+"</td>"
			  		     +"<td>"+data[i].SOURCE+"</td>"
			  		     +"<td>"+data[i].URGENCY_DEGREE+"</td>"
			  		     +"<td>"+data[i].CREATE_TIME+"</td>"
			  		     +"<td>"+data[i].PLANNED_FINISH_TIME+"</td>"
			  		     +"<td>"+data[i].PROBLEM_TYPE+"</td>"
			  		     +"<td>"+data[i].CREATE_ACTOR_NO+"</td>"
			  		     +"<td>"+data[i].PROBLEM_DESCRIPTION+"</td>"
			  		     +"<td>"+str+"</td>"
			  		    +"</tr>";
			        } 
				}
			}
	});//初始化调用：第1页
	$("tasksList").innerHTML=tableStr;	
	/*分页下标*/
	var target = "";
	/**
	 * 分页下标
	 */
	for(var i = 0; i < pageCounts; i++){
		if((i+1) == currentPage){
			target += "<a href=\"\" disabled><font color='red'>"+(i+1)+"</font></a>";
		}else{
			if(i == 0){
				target += "<a href=\"javascript:pagination("+(currentPage-1)+","+dataCounts+","+pageCounts+","+pageSize+");\">上一页</a>";
			}
			target += "<a href=\"javascript:pagination("+(i+1)+","+dataCounts+","+pageCounts+","+pageSize+");\"><font color='red'>"+(i+1)+"</font></a>";
			if(i == pageCounts - 1){
				target += "<a href=\"javascript:pagination("+(currentPage+1)+","+dataCounts+","+pageCounts+","+pageSize+");\">下一页</a>";
			}
		}
		if(pageCounts == 1){
			target += "<a href=\"javascript:disableLink(this);\">下一页</a>";
		}
	}
	document.getElementById("taskListPage").innerHTML = target;
}
//下标分页事件
var pagination = function(currentPage,dataCounts,pageCounts,pageSize){
	var startDate=$("startDate").value;
	var endDate=$("endDate").value;
	content(startDate,endDate,currentPage,dataCounts,pageCounts,pageSize);
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
        	formData.startDate=$("startDate").value; 
            formData.endDate=$("endDate").value;
            return formData;
        }
        }
    ]);
    return loadParam;
}
//查询
var queryData=function(){
	startDate=$("startDate").value;
	endDate=$("endDate").value;
    content(startDate,endDate,1,dataCounts,pageCounts,pageSize);
}
//附件下载
function downloadattrs(file)
{
	document.forms[0].target="hiddenFrame";
 	var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ file;
	window.open(url,"hiddenFrame","");
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
    openMenu("报表访问情况","/meta/module/mag/log/menuVisitInfo.jsp?groupId="+groupId,"top");
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
//查看流程
function checkProcess(mainProblemId){
    openMenu("工单"+mainProblemId+"的处理流程","/portalCommon/module/serviceManage/serProManage/problemDetail.jsp?mainProblemId="+mainProblemId,"top");
}
var openMenu=function(menuName,menuUrl,target,menuId,isRefresh){
	 var param="height=500px,width=1200px,top=200,left=100,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no";
    window.open (base+menuUrl,'newwindow',param);
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
//展示公告的详细信息
var showNotice = function(noticeId){
	var showMes = "";
	//查找公告信息
	PortalCommonCtrlr.getNoticeById(noticeId,{
		async:false,
		callback:function(data){
		
			//标题
			showMes += "<div class='show_content' onMousedown=\"StartDrag(this)\" onMouseup=\"StopDrag(this)\" onMousemove=\"Drag(this)\">";
			showMes += "<a href=\"javascript:closeLayer();\" class=\"closed\">× 关闭</a><h2 class=\"gg_title\">"+data["NOTICE_TITLE"]+"</h2>";
			showMes += "<div class=\"xian\"></div>";
			showMes += "<p><ul class=\"gg_info\">";
			showMes += "<li>发布日期："+data["INIT_DATE"]+"</li>";
			
			//公告等级
			var levelname = levelName(1*data["NOTICE_LEVEL"]);
			showMes += "<li>公告等级：<span>"+levelname+"</span></li>";
			showMes += "</ul></p>";

			//公告内容
			showMes += "<p class=\"clear\"></p>";
			showMes += "<p class=\"gg_content\">公告内容：<br/>";
			showMes += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + data["NOTICE_CONTENT"] + "</p>";
			
		    showMes += "<br/><br/>";
			showMes += "<p><ul class=\"gg_info3\"><li>附件下载：<a href=\"#\" onclick=\"downloadattrs('"+data["AFFIX_PATH"]+"')\">"+data["AFFIX_NAME"]+"</a>&nbsp;&nbsp;&nbsp;&nbsp;</li>";
			showMes += "</ul></p></div>";
		}
	});
	//弹出div层
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
dhx.ready(userInit);

