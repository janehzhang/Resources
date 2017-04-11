/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        repSupermarket.js
 *Description：
 *      报表超市
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        tanwc
 ********************************************************/


var repSupermarket = function() {

	/**
	 * 获取全局变量。
	 */
	dhtmlx.image_path = getDefaultImagePath();
	dhtmlx.skin = getSkin();
	var a_self = this;
	a_self.dwrCaller = new biDwrCaller();
	a_self.reportId = "";		//报表id
	
	//查询推荐报表
	a_self.dwrCaller.addAutoAction("getRepListByPost", "RepSupermarketAction.getRepListByPost");
	a_self.dwrCaller.isShowProcess("getRepListByPost",true);
	
	//查询人气最旺报表
	a_self.dwrCaller.addAutoAction("getRepListBySub", "RepSupermarketAction.getRepListBySub");
	a_self.dwrCaller.isShowProcess("getRepListBySub",true);
	
	//查询新鲜出炉报表
	a_self.dwrCaller.addAutoAction("getRepListByTime", "RepSupermarketAction.getRepListByTime");
	a_self.dwrCaller.isShowProcess("getRepListByTime",true);
	

	/**
	 * 初始化类
	 */
	this.init = function() {
		initForm();				//加载表单
		a_self.loadData();		//加载数据
		a_self.binding();		//绑定事件
	};
	
	//数据加载
	this.loadData = function() {
		var template = "<li class=\"content-11\" onmouseover=\"mouseOver('#EMP_ID#')\" onmouseout=\"mouseOut('#EMP_ID#')\" >" +
			 "<a class=\"content-name\" onClick=\"reportOpen('#REPORT_ID#','#REPORT_NAME#')\">#REPORT_NAME#</a> " +
			 "<span class=\"content-num\">（#SUB_COUNT#）</span><div class=\"tips\" id=\"#EMP_ID#\" >#FAV_STR#" +
			 "<a class=\"sc11-1\" onClick=\"reportDetail('#REPORT_ID#','#REPORT_NAME#')\">详细</a></div>" +
			 "<span class=\"content-text\" >#REPORT_NOTE#</span></li>";
		//查询推荐报表
		a_self.dwrCaller.executeAction('getRepListByPost',function(data){
 			if(data){
 				var EMP_ID = "a";	//替换添加收藏夹div的id
 				var showContentA = "";
				for(var i=0;i<data.length;i++){
					var favStr = "<a class=\"sc11\" onClick=\"favorites('#REPORT_ID#','#REPORT_NAME#','#AUDIT_TYPE#')\">欢迎收藏</a>";
					if(data[i].FAVORITE_FLAG==1){
						favStr = "";
					}
					showContentA += template.replaceAll("#FAV_STR#",favStr).replaceAll("#EMP_ID#",EMP_ID+i).
					replaceAll("#REPORT_NAME#",data[i].REPORT_NAME).replaceAll("#REPORT_ID#",data[i].REPORT_ID).
					replaceAll("#SUB_COUNT#","同岗位订阅数："+data[i].SUB_COUNT).replaceAll("#AUDIT_TYPE#",data[i].AUDIT_TYPE).
					replaceAll("#REPORT_NOTE#",data[i].REPORT_NOTE+"("+data[i].COL_ALIAS+")");
				}
				$('showContentA').innerHTML 		= showContentA;
			}
		});
		//查询人气最旺报表
		a_self.dwrCaller.executeAction('getRepListBySub',function(data){
 			if(data){
 				var EMP_ID = "b";
 				var showContentB = "";
				for(var i=0;i<data.length;i++){
					var favStr = "<a class=\"sc11\" onClick=\"favorites('#REPORT_ID#','#REPORT_NAME#','#AUDIT_TYPE#')\">欢迎收藏</a>";
					if(data[i].FAVORITE_FLAG==1){
						favStr = "";
					}
					showContentB += template.replaceAll("#FAV_STR#",favStr).replaceAll("#EMP_ID#",EMP_ID+i).
					replaceAll("#REPORT_NAME#",data[i].REPORT_NAME).replaceAll("#REPORT_ID#",data[i].REPORT_ID).
					replaceAll("#SUB_COUNT#","总订阅数："+data[i].SUB_COUNT).replaceAll("#AUDIT_TYPE#",data[i].AUDIT_TYPE).
					replaceAll("#REPORT_NOTE#",data[i].REPORT_NOTE+"("+data[i].COL_ALIAS+")");
				}
				$('showContentB').innerHTML 		= showContentB;
			}
		});
		//查询新鲜出炉报表
		a_self.dwrCaller.executeAction('getRepListByTime',function(data){
 			if(data){
 				var EMP_ID = "c";
 				var showContentC = "";
				for(var i=0;i<data.length;i++){
					var favStr = "<a class=\"sc11\" onClick=\"favorites('#REPORT_ID#','#REPORT_NAME#','#AUDIT_TYPE#')\">欢迎收藏</a>";
					if(data[i].FAVORITE_FLAG==1){
						favStr = "";
					}
					showContentC += template.replaceAll("#FAV_STR#",favStr).replaceAll("#EMP_ID#",EMP_ID+i).
					replaceAll("#REPORT_NAME#",data[i].REPORT_NAME).replaceAll("#REPORT_ID#",data[i].REPORT_ID).
					replaceAll("#SUB_COUNT#","上架时间："+data[i].START_DATE).replaceAll("#AUDIT_TYPE#",data[i].AUDIT_TYPE).
					replaceAll("#REPORT_NOTE#",data[i].REPORT_NOTE+"("+data[i].COL_ALIAS+")");
				}
				$('showContentC').innerHTML 		= showContentC;
			}
		});
	}
	
	//绑定事件
	this.binding = function() {
		//为搜索按钮绑定事件
		Tools.addEvent($("search"),'click',function(){ 
			openSearch();		//搜索报表
		});
	}
	
	//查看更多
	openList = function(orderId){
		var menuName = "报表超市";
		if(orderId==1){
			menuName += "推荐报表";
		}else if(orderId==2){
			menuName += "人气最旺";
		}else if(orderId==3){
			menuName += "新鲜出炉";
		}
		openMenu(menuName,"/meta/module/reportManage/supermarket/reportList.jsp?orderId="+orderId,"top");
	}
	
	//搜索报表
	openSearch = function(){
		var searchName = $('searchName').value;
		openMenu("报表搜索","/meta/module/reportManage/supermarket/searchReport.jsp?searchName="+encodeURIComponent(encodeURIComponent(searchName)),"top");
	}
	
	//打开报表
	reportOpen = function(reportId,reportName){
		openMenu(reportName,"/meta/module/reportManage/supermarket/reportOpen.jsp?reportId="+reportId,"top");
	}
	
	//查看报表详细
	reportDetail = function(reportId,reportName){
		openMenu(reportName+"报表详情","/meta/module/reportManage/supermarket/reportDetail.jsp?reportId="+reportId,"top");
		
	}

}

var genObj = function() {
	var instance = new repSupermarket();
	instance.init();
}
dhx.ready(genObj);