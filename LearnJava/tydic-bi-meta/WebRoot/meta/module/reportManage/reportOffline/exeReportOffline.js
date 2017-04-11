/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       batchImport.js
 *Description：
 *       报表管理-应用下线-报表下线
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       吴喜丽
 *Date：
 *       2012-03-22
 ********************************************************/
/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var pageSize = 20;
var queryForm;
var grid;
//数据模型ID
var issueId;
var midCell;
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();
dwrCaller.addAutoAction("getReportMesByRepId",
        "ReportOfflineAction.getReportMesByRepId", {
            dwrConfig:true,
            ansync:true
        });
dwrCaller.isShowProcess("getReportMesByRepId", false);
var loadDate = function(){
	dwrCaller.executeAction('getReportMesByRepId', reportId, function (data) {
		if(data){
			$('_reportName').innerHTML = data.REPORT_NAME;
	        $('_createDept').innerHTML = data.DEPT_NAME;
	        $('_createPeo').innerHTML = data.USER_NAMECN;
	        var tel = "";
	        if(data.USER_MOBILE != null && data.USER_MOBILE != "null")
	        	tel = data.USER_MOBILE;
	        $('_tel').innerText = tel;
	        var emil = "";
	        if(data.USER_EMAIL != null && data.USER_EMAIL != "null")
	        	emil = data.USER_EMAIL;
	        $('_email').innerHTML = emil;
	        $('_reportState').innerHTML = getNameByTypeValue("REPORT_STATE",data.REPORT_STATE);
	        var reportKeyWord = "";
	        if(data.REPORT_KEYWORD != null && data.REPORT_KEYWORD != "null")
	        	reportKeyWord = data.REPORT_KEYWORD;
	        $('_reportKeyWord').innerHTML = reportKeyWord;
	        var reportNote = " ";
	        if (data.REPORT_NOTE != null && data.REPORT_NOTE != "null")
	            reportNote = data.REPORT_NOTE;
	        $('_reportNote').innerText = reportNote;
	        $('_scNum').innerText = data.SC_SUMNUM;
	        $('_dyNum').innerText = data.DY_SUMNUM;
	        issueId = data.ISSUE_ID;
	        tabbarFun();
		}
	});
}

var importForm;
var init = function () {
    loadDate();
    var dhxLayout;
    if(typeId == "1"){
    	//window.parent.addTabRefreshOnActiveByName("报表管理");
    	dhxLayout = new dhtmlXLayoutObject(document.body, "3E");
    }else if(typeId == "0"){
    	dhxLayout = new dhtmlXLayoutObject(document.body, "2E");
    }
    dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.setHeight(153);
    topCell.fixSize(true,true);
    var dhxAttachObject = topCell.attachObject("_exeReportOfflineMes");
    topCell.hideHeader();

    midCell = dhxLayout.cells("b");
	
    if(typeId == "1"){
    	var bottomCell = dhxLayout.cells("c");
	    bottomCell.setHeight(55);
	    bottomCell.fixSize(true, true);
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

}
var tabbarFun = function(){
	var tabbar = midCell.attachTabbar();
	tabbar.setHrefMode("iframes-on-demand");
    tabbar.addTab("modelMes", "数据模型信息", "120px");
    tabbar.addTab("colMes", "数据模型字段信息", "120px");
    //tabbar.addTab("reportBrwor", "报表预览信息", "120px");
//    tabbar.addTab("reportUseMes", "报表使用信息", "120px");
    tabbar.setContentHref("modelMes",getBasePath()+"/meta/module/reportManage/reportOffline/modelMes.jsp?reportId="+reportId);
    tabbar.setContentHref("colMes",getBasePath()+"/meta/module/reportManage/reportOffline/colMes.jsp?issueId="+issueId);
    //tabbar.setContentHref("reportBrwor",getBasePath()+"/meta/module/reportManage/reportOffline/reportBrwor.jsp?reportId="+reportId);
//    tabbar.setContentHref("reportUseMes",getBasePath()+"/meta/module/reportManage/reportOffline/reportUseMes.jsp?reportId="+reportId);
    tabbar.attachEvent("onSelect", function (id, last_id) {
        if (id != null) {
//            if (id == "modelMes") {
//                var obj = tabbar.cells(id).attachGrid();
//                //colMesBar(obj);
//                modelMesBar(obj);
//            }
            return true;
        } else
            return false;
    });
    tabbar.setTabActive("modelMes");
}



dwrCaller.addAutoAction("updateReportStateByRepId", "ReportOfflineAction.updateReportStateByRepId");
dwrCaller.isShowProcess("updateReportStateByRepId",false);
var offLine = function(){
	dwrCaller.executeAction('updateReportStateByRepId',reportId,function(boo) {
		if(boo){
			importForm.disableItem("offlineBtn");
			dhx.alert("下线成功！");
		}else{
			dhx.alert("下线失败，请重新操作！");
		}
	});
}
var typeConverter = new dhtmxGridDataConverter(
        {
            isFormatColumn:false,
            filterColumns:[ "COL_NAME", "COL_ALIAS", "COL_TYPE_NAME",
                "COL_BUS_TYPE", "DIM_LEVELS", "DIM_CODES" ],
            /**
             * 实现 userData，将一些数据作为其附加属性
             * @param rowIndex
             * @param rowData
             * @return
             */
            userData:function (rowIndex, rowData) {
                var userData = {};
                return userData;
            },
            getRowID:function (rowIndex, rowData) {
                return rowIndex;
            },
            /**
             * 获取下拉框Button的值
             * @param rowIndex
             * @param columnIndex
             * @param cellValue
             * @param rowData
             */
            cellDataFormat:function (rowIndex, columnIndex, columnName, cellValue, rowData) {
                if (columnName == "COL_BUS_TYPE") {
                    var str = str = getNameByTypeValue("COL_BUS_TYPE",rowData["COL_BUS_TYPE"])
                    return str;
                } else if (columnName == "DIM_LEVELS") {
                    if (rowData["DIM_LEVELS"] != null
                            && rowData["DIM_LEVELS"] != ""
                            && rowData["DIM_LEVELS"] != "null") {
                        var s = rowData["DIM_LEVELS"].split(",");
                        var str = "<span>";
                        if (s.length > 0) {
                            for (var i = 0; i < s.length; i++) {
                                str += "<input type='checkbox' checked disabled='disabled'/>";
                                str += s[i];
                            }
                        }
                        str += "</span>";
                        return str;
                    }
                }

                return this._super.cellDataFormat(rowIndex, columnIndex,
                        columnName, cellValue, rowData);
            }
        });

//查询字段信息
dwrCaller.addAutoAction("getColMes", "DataOfflineAction.getColMes", {
    dwrConfig:true,
    converter:typeConverter,
    isShowProcess:false
});
var modelMesBar = function(obj){
	var oldArg=arguments;
    setTimeout(function(){
       	oldArg.callee.call(this,obj);
    },7);
}


var colMesBar = function (obj) {
    if (issueId) {
    } else {
        var oldArg=arguments;
        setTimeout(function(){
        	oldArg.callee.call(this,obj);
        },10);
    }
}
dhx.ready(init);