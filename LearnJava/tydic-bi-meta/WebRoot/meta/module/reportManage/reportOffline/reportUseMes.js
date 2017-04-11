/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       batchImport.js
 *Description：
 *       报表管理-应用下线-报表使用信息
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
dwrCaller.addAutoAction("getReportUseMesByRepId",
        "ReportOfflineAction.getReportUseMesByRepId", {
            dwrConfig:true,
            ansync:true
        });
dwrCaller.isShowProcess("getReportUseMesByRepId", false);
var loadDate = function(){
	dwrCaller.executeAction('getReportUseMesByRepId', reportId, function (data) {
		if(data){
			$('_scnum').innerHTML = data.SC_NUM;
	        $('_dynum').innerHTML = data.DY_NUM;
	        var zjAccess = data.ZONE_NAME+data.DEPT_NAME+data.USER_NAMECN;
	        $('_zjAccess').innerHTML = zjAccess;
	        $('_zjAccessTime').innerText = data.USE_TIME;
	        $('_tel').innerHTML = data.USER_MOBILE;
	        $('_email').innerHTML = data.USER_EMAIL;
		}
	});
}
var init = function(){
	loadDate();
    var dhxLayout = new dhtmlXLayoutObject(document.body, "1C");
    dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var cell = dhxLayout.cells("a");
    var dhxAttachObject = cell.attachObject("_reportUseMes");
    cell.hideHeader();
}
dhx.ready(init);