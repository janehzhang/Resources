
/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        colTypeManage.js
 *Description：
 *       推荐数据
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        tanwc
 ********************************************************/
/**
 * 报表创建主页面
 * 
 */
var reportBuildIndex = function() {
	var r_self = this;
	//报表模型数据HTML模板
	r_self.template4RecommendData = '<li class="content-11" onmouseover="mouseOver(\'build_{issueId}\')"  onmouseout="mouseOut(\'build_{issueId}\')" >'+
					'<a onClick="openIssueModelInfo(\'{id}\')" class="content-name">{tableAlias}</a> '+
					'<span class="content-num">（岗位应用数：{count}）</span> '+
					'<div class="tips"  id="build_{issueId}" > '+
						'<a class="sc12" onClick="openRptBuildPage(\'{id}\')">创建报表</a> '+
					'</div> '+
          			'<span class="content-text" href="javascript:void(0)">{issueNote}（{cols}）</span> '+
          		'</li>';
	
	r_self.dwrCaller = new biDwrCaller();
	r_self.dwrCaller.addAutoAction("queryRecommendData", "ReportBuildIndexAction.queryRecommendData");
	r_self.dwrCaller.addAutoAction("queryPopularData", "ReportBuildIndexAction.queryPopularData");
	r_self.dwrCaller.addAutoAction("queryNewestData", "ReportBuildIndexAction.queryNewestData");
	
	
	/**
	 * 初始化类
	 */
	this.init = function() {
		try {
			this.search._init();
			this.recommendData._init();
//			this.popularData._init(); 
//			this.newestData._init();
		}catch(e) {
			alert(e);
		}
	};
	
	this.replaceRecommendTemplate = function(issueId,tableAlias,count,issueNote,cols) {
		var random = Math.floor(Math.random() * ( 10000 + 1));
		var result = r_self.template4RecommendData.replaceAll('{issueId}',issueId+'_'+random);
		result = result.replaceAll('{tableAlias}',tableAlias);
		result = result.replaceAll('{id}',issueId);
		result = result.replaceAll('{count}',count);
		result = result.replaceAll('{issueNote}',issueNote);
		result = result.replaceAll('{cols}',cols);
		return result.toString();
	}
		
	/**
	 * 搜索
	 * @memberOf {TypeName} 
	 */
	this.search =  {
		_init:function() {
			var self = this;
			var $searchInput 	= $('searchInput');
			var $searchButton 	= $('searchButton');
			Tools.addTip4Input($searchInput,null,"请输入您要查找的报表数据...");
			
			Tools.addEvent($searchInput,'keydown',function(e){
				if(e.keyCode == 13) {
					var condition = Tools.getInputValue($searchInput);
					openMenu("搜索报表数据","/meta/module/reportManage/reportBuildIndex/searchData.jsp?searchCondition="+encodeURIComponent(encodeURIComponent(condition)),"top",null,true);
				}
			})
			
			Tools.addEvent($searchButton,'click',function(){
				var condition = Tools.getInputValue($searchInput);
				openMenu("搜索报表数据","/meta/module/reportManage/reportBuildIndex/searchData.jsp?searchCondition="+encodeURIComponent(encodeURIComponent(condition)),"top",null,true);
			});
		}
	};
	/**
	 * 推荐数据
	 */
	this.recommendData = {
		_init:function(){
			var self = this;
			self.loadData();
		},
		
		loadData:function() {
			var self = this;
			r_self.dwrCaller.executeAction('queryRecommendData',pageBean,function(data){
				var html = "";
				if(data) {
					for(var i=0;i<data.length;i++) {
						if(i==0){
							totalCount = data[i].TOTAL_COUNT_;			//设置总条数
						}
						var issueId = data[i].ISSUE_ID;
						var tableAlias = data[i].TABLE_ALIAS;
						var issueNote = data[i].ISSUE_NOTE;
						var cols = data[i].COLS;
						var count = data[i].COUNT;
						html += r_self.replaceRecommendTemplate(issueId,tableAlias,count,issueNote,cols);
					}
					$('recommendDataContainer').innerHTML = html;
					if(data.length == 0) {
						totalCount = 0;
					}
				}
				$('showPage').innerHTML = showPage();  //加载分页
			})
		}
	
	};
	
	//跳转分页，pageValue需要查出的页数
	doPage = function(pageValue){
		setPage(pageValue);		//设置分页
		r_self.newestData.loadData();		//加载数据
	}
}
/**
 * 生成字段分类管理类实例
 */
var genObj = function() {
	var instance = new reportBuildIndex();
	instance.init();
}
dhx.ready(genObj);


function openRptBuildPage(issueId) {
	openMenu("报表创建","/meta/module/reportManage/build/reportConfig.jsp?issueId="+issueId,"top",null,true);
}
function openIssueModelInfo(issueId) {
	openMenu("报表模型详细信息","meta/module/reportManage/publishDataModel/searchPublishDataModel.jsp?issueId="+issueId,"top",null,true);
}





