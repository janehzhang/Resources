/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       batchImport.js
 *Description：
 *       报表管理-应用下线-模型下线
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       吴喜丽
 *Date：
 *       2012-03-16
 ********************************************************/
/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var pageSize = 20;
var queryForm;
var grid;
/*判断模型是否被报表引用并且存在不下线报表：是-false；否-true*/
var isHaveData = true;
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();

dwrCaller.addAutoAction("exeDataOffline", "DataOfflineAction.exeDataOffline");
dwrCaller.isShowProcess("exeDataOffline",false);
var loadDate = function(){
	dwrCaller.executeAction('exeDataOffline',tableId,issueId,function(data) {
		var tableInfo = data.DATA_SOURCE_NAME;
		if(data.TABLE_OWNER != null)
			tableInfo += "."+data.TABLE_OWNER;
		tableInfo += "."+data.TABLE_NAME;
//		$('_queryDataModelTitle').innerHTML = "查看数据模型:"+data.TABLE_NAME;
		$('_tableInfo').innerText = tableInfo;
		var tableAlias = " ";
		if(data.TABLE_ALIAS != null && data.TABLE_ALIAS != "null")
			tableAlias = data.TABLE_ALIAS;
		$('_tableAlias').innerText = tableAlias;
		var auditType = " ";
		if(data.AUDIT_TYPE == "0")
			auditType = "自动审核";
		else if(data.AUDIT_TYPE == "1")
			auditType = "人工审核";
		$('_auditType').innerText = auditType;
		var dataC = " ";
		if(data.DATA_AUDIT_TYPE != null && data.DATA_AUDIT_TYPE != "null")
			dataC = data.DATA_AUDIT_TYPE;
		$('_dataC').innerText = dataC;
		var maxDate = " ";
		if(data.MAX_DATE != null && data.MAX_DATE != "null")
			maxDate = data.MAX_DATE;
		$('_maxDate').innerText = maxDate;
		var keyWord = " ";
		if(data.TABLE_KEYWORD != null && data.TABLE_KEYWORD != "null")
			keyWord = data.TABLE_KEYWORD;
		$('_keyWord').innerText = keyWord;
		var issueState = " ";
		if(data.ISSUE_STATE == "0")
			issueState = "已下线";
		else if(data.ISSUE_STATE == "1")
			issueState = "正常";
		$('_issueState').innerText = issueState;
		var issueNote = " ";
		if(data.ISSUE_NOTE != null && data.ISSUE_NOTE != "null")
			issueNote = data.ISSUE_NOTE;
		$('_issueNote').innerText = issueNote;
	});
}

var typeConverter = new dhtmxGridDataConverter({
	isFormatColumn:false,
    filterColumns:["COL_NAME","COL_ALIAS","COL_TYPE_NAME","COL_BUS_TYPE","DIM_LEVELS","DIM_CODES"],
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData) {
        var userData = {};
        return userData;
    },
    getRowID:function(rowIndex, rowData) {
        return rowIndex;
    },
    /**
     * 获取下拉框Button的值
     * @param rowIndex
     * @param columnIndex
     * @param cellValue
     * @param rowData
     */
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
    	if(columnName == "COL_BUS_TYPE"){
    		var str = str = getNameByTypeValue("COL_BUS_TYPE",rowData["COL_BUS_TYPE"])
    		return str;
    	}else if(columnName == "DIM_LEVELS"){
    		if(rowData["DIM_LEVELS"] != null && rowData["DIM_LEVELS"] != "" && rowData["DIM_LEVELS"] != "null"){
    			var s = rowData["DIM_LEVELS"].split(",");
    			var str = "<span>";
    			if(s.length > 0){
    				for(var i = 0; i < s.length; i++){
    					str += "<input type='checkbox' checked disabled='disabled'/>";
    					str += s[i];
    				}
    			}
    			str += "</span>";
    			return str;
    		}
    	}
    		
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
});

//查询字段信息
dwrCaller.addAutoAction("getColMes", "DataOfflineAction.getColMes",{
    dwrConfig:true,
    converter:typeConverter,
    async:false,
    isShowProcess:false
});

var loadColDate = function(){
	var secGrid;
	secGrid = new dhtmlXGridObject("_colMesId");
	secGrid.setHeader("字段名,别名,字段分类,字段属性,已支撑维度粒度,维度值");
    secGrid.setInitWidthsP("15,15,15,10,25,20");
    secGrid.setColAlign("left,left,left,left,left,left");
    secGrid.setHeaderAlign("center,center,center,center,center,center")
    secGrid.setColTypes("ro,ro,ro,ro,ro,ro");
    secGrid.setColSorting("na,na,na,na,na,na");
    secGrid.enableResizing("true,true,true,true,true,true");
    secGrid.enableMultiselect(false);
    secGrid.enableCtrlC();
    secGrid.init();
    //数据加载
    secGrid.load(dwrCaller.getColMes+"?issueId="+issueId, "json");
}
var reportTypeConverter = new dhtmxGridDataConverter({
	isFormatColumn:false,
    filterColumns:["REPORT_NAME","CREATEPEOMES","OPERATE_TIME","REPORT_STATE"],
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData) {
        var userData = {};
        return userData;
    },
    getRowID:function(rowIndex, rowData) {
        return rowIndex;
    },
    /**
     * 获取下拉框Button的值
     * @param rowIndex
     * @param columnIndex
     * @param cellValue
     * @param rowData
     */
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
    	if(columnName == "REPORT_STATE"){
    		var str = getNameByTypeValue("REPORT_STATE",rowData.REPORT_STATE);
    		if(rowData.REPORT_STATE == 1 && isHaveData)
    			isHaveData = false;
    		return str;
    	}
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
});
//查询报表引用信息
dwrCaller.addAutoAction("getReportMes", "DataOfflineAction.getReportMes",{
    dwrConfig:true,
    converter:reportTypeConverter,
    async:false,
    isShowProcess:false
});
var importForm;
var init = function(){
	loadDate();
	var dhxLayout;
	if(typeId == "1"){
		window.parent.addTabRefreshOnActiveByName("报表管理");
		dhxLayout = new dhtmlXLayoutObject(document.body, "3E");
	}else
		dhxLayout = new dhtmlXLayoutObject(document.body, "2E");
	
	dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.setHeight(250);
    topCell.fixSize(true,true);
    var dhxAttachObject = topCell.attachObject("_exeDataOfflineMes");
    loadColDate();
    topCell.hideHeader();
    
    var midCell = dhxLayout.cells("b");
//    midCell.setHeight(309);
    midCell.fixSize(true,false);
    if(typeId == "1"){
    	var bottomCell = dhxLayout.cells("c");
	    bottomCell.setHeight(55);
	    bottomCell.fixSize(true,true);
	    bottomCell.hideHeader();
	    importForm = bottomCell.attachForm(
    	 	[
    	 		{type:"button",name:"offlineBtn",value:"正式下线",offsetLeft : 1040,offsetTop : 0}
            ]
    	);
	    importForm.attachEvent("onButtonClick", function(id) {
	        if(id == "offlineBtn"){
	            offLine();
	        }
	    });
    }
    
    
    midCell.setText("相关引用报表");
    grid = midCell.attachGrid();
    grid.setHeader("相关报表,创建人,创建时间,状态");
    grid.setInitWidthsP("20,30,15,35");
    grid.setColAlign("left,left,center,left");
    grid.setHeaderAlign("center,center,center,center")
    grid.setColTypes("ro,ro,ro,ro");
    grid.setColSorting("na,na,na,na");
    grid.enableResizing("true,true,true,true");
    grid.enableMultiselect(false);
    grid.enableCtrlC();
    grid.init();
    grid.defaultPaging(pageSize);
    var param = new biDwrMethodParam();
    param.setParam(0,issueId,false);
    //数据加载
    grid.load(dwrCaller.getReportMes+param, "json");
}
dwrCaller.addAutoAction("offLine", "DataOfflineAction.offLine");
dwrCaller.isShowProcess("offLine",false);
var offLine = function(){
	if(!isHaveData){
		dhx.alert("该模型存在没下线报表引用，不能在此下线，谢谢！");
	}else{
		dwrCaller.executeAction('offLine',issueId,function(boo) {
			if(boo){
				importForm.disableItem("offlineBtn");
				dhx.alert("下线成功！");
			}else{
				dhx.alert("下线失败，请重新操作！");
			}
		});
	}
}
dhx.ready(init);