/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        auditData.js
 *Description：
 *      我创建的报表
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        tanwc
 ********************************************************/


var myReport = function() {

	/**
	 * 获取全局变量。
	 */
	dhtmlx.image_path = getDefaultImagePath();
	dhtmlx.skin = getSkin();
	var a_self = this;
	a_self.dwrCaller = new biDwrCaller();
	
	//根据排序方查询报表
	a_self.dwrCaller.addAutoAction("getMyReportList", "MyReportAction.getMyReportList");
	a_self.dwrCaller.isShowProcess("getMyReportList",true);
	
	//根据id删除报表
	a_self.dwrCaller.addAutoAction("deleteReportById", "MyReportAction.deleteReportById");
	a_self.dwrCaller.isShowProcess("deleteReportById",true);

	/**
	 * 初始化类
	 */
	this.init = function() {
		a_self.loadData();		//加载数据
		a_self.binding();		//绑定事件
	};
	
	//数据加载
	this.loadData = function() {
		
		var template = "<li class=\"content-11\" onmouseover=\"mouseOver('#EMP_ID#')\" onmouseout=\"mouseOut('#EMP_ID#')\" >" +
			 "<a class=\"content-name\" onClick=\"reportOpen('#REPORT_ID#','#REPORT_NAME#')\">#REPORT_NAME#</a> " +
			 "<span class=\"content-num\">（#CREATE_TIME#）</span>" +
			 "<div class=\"tips\" id=\"#EMP_ID#\" ><a class=\"sc11\" onClick=\"reportDetail('#REPORT_ID#','#REPORT_NAME#')\">详细</a>" +
			 "#DEL_STR#</div>" +
			 "<span class=\"content-text\" >#REPORT_NOTE#</span></li>";
		
		//查询推荐报表
		a_self.dwrCaller.executeAction('getMyReportList',pageBean,function(data){
 			if(data){
 				var EMP_ID = "a";	//替换添加收藏夹div的id
 				var showContentList = "";
				for(var i=0;i<data.length;i++){
					if(i==0){
						totalCount = data[i].TOTAL_COUNT_;			//设置总条数
					}
					var delStr = "";
					if(data[i].FLAG == 0){
						delStr = "<a class=\"sc11-1\" onClick=\"deleteReport('#REPORT_ID#')\">删除报表</a>";
					}
					showContentList += template.replaceAll("#DEL_STR#",delStr).
					replaceAll("#EMP_ID#",EMP_ID+i).replaceAll("#REPORT_NAME#",data[i].REPORT_NAME).
					replaceAll("#CREATE_TIME#","创建时间："+data[i].CREATE_TIME).
					replaceAll("#REPORT_ID#",data[i].REPORT_ID).
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
		openMenu("打开报表-"+reportName,"/meta/module/reportManage/supermarket/reportOpen.jsp?reportId="+reportId,"top");
	}
	
	//查看报表详细
	reportDetail = function(reportId,reportName){
		openMenu("报表详情-"+reportName,"/meta/module/reportManage/supermarket/reportDetail.jsp?reportId="+reportId,"top");
		
	}
	
	deleteReport = function(reportId){
		
		dhx.confirm("确定要删除该报表？",function(r){
			if(r){
				//删除对应id的报表
				a_self.dwrCaller.executeAction('deleteReportById',reportId,function(rs){
					if(rs){
						dhx.alert("删除成功！");
						a_self.loadData();		//重新加载数据
					}else{
						dhx.alert("删除失败！请联系管理员！");
					}
				});
			}
		});
	}
	
	//跳转分页，pageValue需要查出的页数
	doPage = function(pageValue){
		setPage(pageValue);		//设置分页
		a_self.loadData();		//加载数据
	}
}

var genObj = function() {
	var instance = new myReport();
	instance.init();
}
dhx.ready(genObj);