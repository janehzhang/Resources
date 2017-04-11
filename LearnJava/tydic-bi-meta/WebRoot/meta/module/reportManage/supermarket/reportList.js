/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        reportList.js
 *Description：
 *      报表列表
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
	
	//根据排序方查询报表
	a_self.dwrCaller.addAutoAction("getRepListByOrderId", "RepSupermarketAction.getRepListByOrderId");
	a_self.dwrCaller.isShowProcess("getRepListByOrderId",true);

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
		var menuName = "报表超市 -> ";
		if(orderId==1){
			menuName += "推荐报表";
		}else if(orderId==2){
			menuName += "人气最旺的报表";
		}else if(orderId==3){
			menuName += "新鲜出炉的报表";
		}
		$('menuName').innerHTML = menuName;
		//查询推荐报表
		a_self.dwrCaller.executeAction('getRepListByOrderId',orderId,pageBean,function(data){
 			if(data){
 				var EMP_ID = "a";	//替换添加收藏夹div的id
 				var showContentList = "";
				for(var i=0;i<data.length;i++){
					if(i==0){
						totalCount = data[i].TOTAL_COUNT_;			//设置总条数
					}
					var showCol = "";
					if(orderId==1){
						showCol += "同岗位订阅数："+data[i].SUB_COUNT;
					}else if(orderId==2){
						showCol += "总订阅数："+data[i].SUB_COUNT;
					}else if(orderId==3){
						showCol += "上架时间："+data[i].START_DATE;
					}
					var favStr = "<a class=\"sc11\" onClick=\"favorites('#REPORT_ID#','#REPORT_NAME#','#AUDIT_TYPE#')\">欢迎收藏</a>";
					if(data[i].FAVORITE_FLAG==1){
						favStr = "";
					}
					showContentList += template.replaceAll("#FAV_STR#",favStr).replaceAll("#EMP_ID#",EMP_ID+i).
					replaceAll("#REPORT_NAME#",data[i].REPORT_NAME).replaceAll("#AUDIT_TYPE#",data[i].AUDIT_TYPE).
					replaceAll("#SUB_COUNT#",showCol).replaceAll("#REPORT_ID#",data[i].REPORT_ID).
					replaceAll("#REPORT_NOTE#",data[i].REPORT_NOTE+"("+data[i].COL_ALIAS+")");
				}
				$('showContentList').innerHTML = showContentList;
			}
 			
			$('showPage').innerHTML = showPage();  //加载分页
		});
	}
	
	//绑定事件
	this.binding = function() {
		//为搜索按钮绑定事件
		Tools.addEvent($("search"),'click',function(){ 
			openSearch();		//搜索报表
		});
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
	
	//跳转分页，pageValue需要查出的页数
	doPage = function(pageValue){
		setPage(pageValue);		//设置分页
		a_self.loadData();		//加载数据
	}
}

var genObj = function() {
	var instance = new repSupermarket();
	instance.init();
}
dhx.ready(genObj);