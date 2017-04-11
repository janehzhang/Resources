/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       batchImport.js
 *Description：
 *       报表管理-应用下线-报表信息
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       吴喜丽
 *Date：
 *       2012-03-23
 ********************************************************/
/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var pageSize = 20;
var queryForm;
var grid;
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();
dwrCaller.addAutoAction("getDataModelMesByRepId",
        "ReportOfflineAction.getDataModelMesByRepId", {
            dwrConfig:true,
            ansync:true
        });
dwrCaller.isShowProcess("getDataModelMesByRepId", false);
var loadDate = function () {
    dwrCaller.executeAction('getDataModelMesByRepId', reportId, function (data) {
//        $('_queryDataModelTitle').innerHTML = "查看数据模型:";
    	var tableInfo = data.DATA_SOURCE_NAME;
		if(data.TABLE_OWNER != null)
			tableInfo += "."+data.TABLE_OWNER;
		tableInfo += "."+data.TABLE_NAME;
		$('_tableInfo').innerText = tableInfo;
//        $('_dataSource').innerHTML = data.DATA_SOURCE_NAME;
//        $('_tableOwer').innerHTML = data.TABLE_OWNER;
//        $('_tableName').innerHTML = data.TABLE_NAME;
        $('_dataC').innerText = data.DATA_AUDIT_TYPE;

        var auditType = "";
        if (data.AUDIT_TYPE == "0")
            auditType = "自动审核";
        else if (data.AUDIT_TYPE == "1")
            auditType = "人工审核";
        $('_dataAudit').innerHTML = auditType;
        $('_tableAlias').innerHTML = data.TABLE_ALIAS;
        var issueNote = "";
        if (data.ISSUE_NOTE != null && data.ISSUE_NOTE != "null")
            issueNote = data.ISSUE_NOTE;
        $('_issueNote').innerText = issueNote;
        //issueId
        issueId = data.ISSUE_ID;

        //alert("call back"+issueId);
    });
}

var init = function(){
	loadDate();
    var dhxLayout = new dhtmlXLayoutObject(document.body, "1C");
    dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var cell = dhxLayout.cells("a");
    var dhxAttachObject = cell.attachObject("_modelMes");
    cell.hideHeader();
}
dhx.ready(init);