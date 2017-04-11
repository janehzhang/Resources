/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       batchImport.js
 *Description：
 *       通用门户--首页--公告信息列表
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       吴喜丽
 *Date：
 *       2012-06-08
 ********************************************************/
var pageSize = 15;//页面显示条数
var dataCounts = 0;//数据总数
var pageCounts = 0;//总页数
var init = function(){
	content(1,dataCounts,pageCounts,pageSize);
}
var content = function(currentPage,dataCounts,pageCounts,pageSize){
	//查找公告信息
	var noticeStr = "";
	PortalCommonCtrlr.getNoticeByPara(currentPage,dataCounts,pageCounts,pageSize,{//初始化调用：第1页
		async:false,
		callback:function(obj){
			dataCounts = obj[0];
			pageCounts = obj[2];
			var data = obj[1];
			if(data != null){
				for(var i = 0; i < data.length; i++){
					noticeStr += "<li><span>"+data[i]["INIT_DATE"]+"</span><a href=\"javascript:showNotice("+data[i]["NOTICE_ID"]+");\">·"+data[i]["NOTICE_CONTENT"]+"</a></li>";
				}
			}
		}
	});
	document.getElementById("noticeListId").innerHTML = noticeStr;
	/*分页下标*/
	var target = "";
	/**
	 * 分页下标
	 */
	for(var i = 0; i < pageCounts; i++){
		if((i+1) == currentPage){
			//target += "<font style='color:#3090bc;'>";
			//target += "总"+pageCounts+"页，每页"+pageSize+"条，共"+dataCounts+"条。"
			//target += "</font>&nbsp;&nbsp;&nbsp;&nbsp;";
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
	document.getElementById("noticListPageId").innerHTML = target;
}
//下标分页事件
var pagination = function(currentPage,dataCounts,pageCounts,pageSize){
	content(currentPage,dataCounts,pageCounts,pageSize);
}
/* 功能: 禁用一个a元素
* 参数:
* link: a元素对象
*/
function disableLink(link) {
   //设置disabled属性
   link.setAttribute("disabled", "disabled");
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



//下载附件信息
var downloadattrs=function(attchmetPath){
if(attchmetPath != "" && attchmetPath != "null"){
  document.forms[0].target="hiddenFrame";
	var url = getBasePath() + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ attchmetPath;
	window.open(url,"hiddenFrame","");
	}else{
		 dhx.alert("对不起，没有附件可下载！");
	}
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
dhx.ready(init);