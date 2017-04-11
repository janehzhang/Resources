
/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        keywordCount.js
 *Description：
 *       关键字统计，对各人员使用的关键字进行统计
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        tanwc
 ********************************************************/

/**
 * 个人应用情况类
 * 
 */
var keywordCount = function() {
	var k_self = this;
	/**
	 * 定义全局变量
	 */
	k_self.layout = new dhtmlXLayoutObject(document.getElementById('keywordCotent'), "2E");
	k_self.layout.hideConcentrate();
	
	k_self.dwrCaller = new biDwrCaller();
	
	var config = {
	    idColumnName:"groupId",
	    filterColumns:["groupName","","","","_buttons"],
	    userData:function(rowIndex, rowData) {
	        var userData = {};
	        return userData;
	    },
	    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
	        if (columnName == '_buttons') {
	            return "getButtons";
	        }
	        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
	    }
	}
	
	//定义action
	k_self.dwrCaller.addAutoAction("loadGroup", "GroupAction.queryGroup");
	k_self.dwrCaller.addDataConverter("loadGroup", new dhtmxGridDataConverter(config));
	
	
	/**
	 * 初始化类
	 */
	this.init = function() {
		this.contentPannel._init();
		this.searchPannel._init();
	};
	
	/**
	 * 查询区域
	 * @memberOf {TypeName} 
	 */
	this.searchPannel = {
		
		_init:function() {
			var self = this;
			self.build();
		},
		build:function() {
			var self = this;
			k_self.layout.cells("a").setText("搜索关键字统计");
			k_self.layout.cells("a").setHeight(80);
			k_self.layout.cells("a").attachObject('keyword_count');
		}
	}
	
	/**
	 * 内容面板
	 */
	this.contentPannel = {
		/**
		 * 初始化内容区域
		 */
		_init:function(){
			var self = this;
			self.build();
			self.loadData();
		},
		/**
		 * 构建字段分类内容表格
		 * @memberOf {TypeName}
		 */
		build:function() {
			var self = this;
		    
			k_self.layout.cells("b").hideHeader();
			
			self.grid = k_self.layout.cells("b").attachGrid();
			self.grid.setHeader("搜索关键字,搜索次数,最近使用时间" );
			self.grid.setInitWidthsP("39,30,30");
		    self.grid.setColAlign("left,center,center");
		    self.grid.setHeaderAlign("center,center,center");
		    self.grid.enableResizing("true,true,true");
		    self.grid.setColTypes("ro,ro,ro");
		    self.grid.setColSorting("na,na,na");
		    self.grid.enableTooltips("true,true,true");
		    self.grid.enableTreeGridLines();
		    self.grid.setEditable(false);
		    self.grid.enableDragAndDrop(true);
		    self.grid.setDragBehavior("complex");
		    self.grid.init();
		},
		
		/**
		 * 加载数据
		 */
		loadData:function() {
			var self = this;
			self.grid.load(k_self.dwrCaller.loadGroup, "json");
   			//self.addButtons2Layout();
		},
		/**
		 * 刷新内容区域列表数据，用于查询、修改、删除等重新加载列表
		 * @memberOf {TypeName} 
		 */
		refreshList :function() {
			var self = this;
		},
		
		/**
		 * 添加按钮功能。for example : 增加同级分类、修改 、删除 等操作
		 * @memberOf {TypeName} 
		 */
		addButtons2Layout:function() {
			var self = this;
			var buttons = {
	        	open:{name:"open",text:"打开明细",
		            onclick:function(rowData){
		                self.open(rowData);
		            }}
            }
			var buttonTags = ["open"];
			var bottonRoleRow = [];
	    	for(var i = 0; i < buttonTags.length; i++){
	           		bottonRoleRow.push(buttonTags[i]);
		    }
	    	//全局方法getButtons
			window["getButtons"] = function(){
			    var res = [];
			    for(var i = 0; i < bottonRoleRow.length; i++){
			        res.push(buttons[bottonRoleRow[i]]);
			    }
			    return res;
			};
		},
		
		/**
		 * 打开使用情况统计明细
		 * @param obj  当前选中的报表数据对象
		 */
		open:function(obj) { 
			var self = this;
			alert('打开ID为【'+obj.id+'】的使用情况明细');
		}
	}
}


/**
 * 生成字段分类管理类实例
 */
var genObj = function() {
	var instance = new keywordCount();
	instance.init()
}
dhx.ready(genObj);











