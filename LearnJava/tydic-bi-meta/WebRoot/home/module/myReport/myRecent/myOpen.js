/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *
 *Description：
 *
 *Dependent：
 *
 *Author:
 *        刘斌
 *Finished：
 *       2012-04-16
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/

/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller = new biDwrCaller();

/**
 * 数据转换Convert
 */
var convertConfig = {
    idColumnName:"reportId",
    filterColumns:["reportName","useTime","count"],
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        if(columnName=="reportName"){
            var tmp = "'"+cellValue+"'";
            return '<a href="javascript:openReport('+rowData.reportId+','+tmp+')">'+cellValue+'</a>'+(rowData.reportState==0?"（已下线）":"");
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};

var convert = new dhtmxGridDataConverter(convertConfig);

dwrCaller.addAutoAction("loadRecent", "MyRecentAction.queryRecentOpen");
dwrCaller.addDataConverter("loadRecent", convert);

/**
 * 初始化方法
 */
var myOpenInit = function(){
    var myOpenLayout = new dhtmlXLayoutObject(document.getElementById("container"), "1C");
    myOpenLayout.cells("a").hideHeader();
    mygrid = myOpenLayout.cells("a").attachGrid();
    mygrid.setHeader("报表名称,打开时间,打开次数");
    mygrid.setInitWidthsP("60,25,15");
    mygrid.setColAlign("left,center,center");
    mygrid.setHeaderAlign("left,center,center");
    mygrid.setColTypes("ro,ro,ro");
    mygrid.init();
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.loadRecent, "json");
};


/**
 * 打开报表链接
 * @param reportId
 */
var openReport = function(reportId, reportName){
    window.open(urlEncode(getBasePath()+"/home/module/commentRpt/commentRpt.jsp?rptId="+reportId));
};

dhx.ready(myOpenInit);
