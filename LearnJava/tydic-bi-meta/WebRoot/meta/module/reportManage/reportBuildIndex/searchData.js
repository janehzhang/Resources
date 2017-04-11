
/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        newestData2More.js
 *Description：
 *       搜索数据
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        tanwc
 ********************************************************/
/**
 * 报表创建主页面
 * 
 */
var searchData = function() {
	var r_self = this;
	//报表模型数据HTML模板
	r_self.template4searchData = '<li class="content-11" onmouseover="mouseOver(\'buildNew_{issueId}\')"  onmouseout="mouseOut(\'buildNew_{issueId}\')" >'+
					'<a class="content-name"  onClick="openIssueModelInfo(\'{id}\')">{tableAlias}</a> '+
					'<div class="tips"  id="buildNew_{issueId}" > '+
						'<a class="sc12" onClick="openRptBuildPage(\'{id}\')">创建报表</a> '+
					'</div> '+
          			'<span class="content-text" href="javascript:void(0)" >{issueNote}（{cols}）</span> '+
          		'</li>';
	r_self.dwrCaller = new biDwrCaller();
	r_self.dwrCaller.addAutoAction("queryRptModelDataByCondition", "ReportBuildIndexAction.queryRptModelDataByCondition");
	
	this.replaceSearchTemplate = function(issueId,tableAlias,issueNote,cols) {
		var random = Math.floor(Math.random() * ( 10000 + 1));
		var result = r_self.template4searchData.replaceAll('{issueId}',issueId+'_'+random);
		result = result.replaceAll('{tableAlias}',tableAlias);
		result = result.replaceAll('{issueNote}',issueNote);
		result = result.replaceAll('{id}',issueId);
		result = result.replaceAll('{cols}',cols);
		return result.toString();
	}
	/**
	 * 初始化类
	 */
	this.init = function() {
		try {
			this.search._init();
			this.searchData._init();
		}catch(e) {
			alert(e);
		}
	};
	
	/**
	 * 搜索数据
	 */
	this.searchData = {
		_init:function(){
			var self = this;
			self.loadData();
		},
		loadData:function() {
			var self = this;
			r_self.dwrCaller.executeAction('queryRptModelDataByCondition',pageBean,searchCondition,function(data){
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
						html += r_self.replaceSearchTemplate(issueId,tableAlias,issueNote,cols);
					}
					$('searchDataContainer').innerHTML = html;
				}
				if(data.length == 0) {
					totalCount = 0;
				}
				$('showPage').innerHTML = showPage();  //加载分页
			})
		}
	};
	
	//跳转分页，pageValue需要查出的页数
	doPage = function(pageValue){
		setPage(pageValue);		//设置分页
		r_self.searchData.loadData();		//加载数据
	}
	
	this.search =  {
		_init:function() {
			var self = this;
			var $searchInput 	= $('searchInput');
			var $searchButton 	= $('searchButton');
			$searchInput.value = searchCondition;
			Tools.addTip4Input($searchInput,null,"请输入您要查找的报表数据...");
			Tools.addEvent($searchInput,'keyup',function(e){
				if(e.keyCode == 13) {
					searchCondition = Tools.getInputValue($searchInput);
					r_self.searchData.loadData();
				}
			});
			Tools.addEvent($searchButton,'click',function(){
				searchCondition = Tools.getInputValue($searchInput);
				r_self.searchData.loadData();
			});
		}
	};
}
/**
 * 生成字段分类管理类实例
 */
var genObj = function() {
	var instance = new searchData();
	instance.init();
}
dhx.ready(genObj);


function openRptBuildPage(issueId) {
	openMenu("报表创建","/meta/module/reportManage/build/reportConfig.jsp?issueId="+issueId,"top",null,true);
}


function openIssueModelInfo(issueId) {
	openMenu("报表模型详细信息","meta/module/reportManage/publishDataModel/searchPublishDataModel.jsp?issueId="+issueId,"top",null,true);
}



