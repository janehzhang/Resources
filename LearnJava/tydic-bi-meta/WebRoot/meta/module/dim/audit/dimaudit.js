/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       dimaudit.js
 *Description：
 *       审核维度编码js
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，公共JS
 *Author:
 *       程钰
 *Date：
 *       12-1-31
 ********************************************************/
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller = new biDwrCaller();
var typeData = null;
var mygrid = null;
var popWindow = null;


/**
 *审核类型
 */
var auditType = function (value) {
    if (value == 2) {
        return "编码映射申请";
    }
    else {
        return "编码归并申请";
    }
}
/****************全局设置end*******************************************/
//申请分类名称的加载方法
var typeConverter = new dhtmlxComboDataConverter({
    valueColumn:"codeId",
    textColumn:"codeName",
    userData:function (rowdata) {
        var userdata = {};
        userdata["codeItem"] = rowdata["codeItem"];
        return userdata;
    }
});
//dwrCaller.addAutoAction("queryType","DimAuditAction.queryApplyType");
//dwrCaller.addDataConverter("queryType",typeConverter);
//加载表格数据
var converterConfig = {
    idColumnName:"batchId",
    filterColumns:["tableNamecn", "tableName", "userNamecn", "modDate", "codeItem", "auditRes", "_buttons"],
    userData:function (rowIndex, rowData) {
        var userData = {};
        userData["tableId"] = rowData["dimTableId"];
        userData["typeId"] = rowData["dimTypeId"];
        userData["itemId"] = rowData["itemId"];
        userData["parId"] = rowData["itemParId"];
        userData["auditFlag"] = rowData["auditFlag"];
        userData["codeItem"] = rowData["codeItem"];
        return userData;
    },
    cellDataFormat:function (rowIndex, columnIndex, columnName, cellValue, rowData) {
        if (columnName == '_buttons') {
            var state = rowData["auditFlag"];
            var codeItem = rowData["codeItem"];
            //归并映射申请
            if (codeItem == 2 || codeItem == 8 || codeItem == 9) {
                if (state == 2) {
                    return"<a href='#' onclick=\"mappShowInfo(" + rowData.dimTableId + ",'" + rowData.tableName + "'," + rowData.batchId + ",'"+rowData.tableOwner+"');return false;\">查看明细</a>";
                } else {
                    return "<a href='#' onclick=\"mappAuditInfo(" + rowData.dimTableId + ",'" + rowData.tableName + "','" + rowData.tableDimPrefix + "','" + rowData.tableOwner + "'," + rowData.batchId + ");return false;\">审核明细</a>";
                }
            } else {
                if (state == 2) {
                    return"<a href='#' onclick=\"showInfo(" + rowData.dimTableId + ",'" + rowData.tableName + "','" + rowData.tableDimPrefix + "'," + rowData.batchId + ",'" + rowData.tableOwner + "');return false;\">查看明细</a>";
                } else {
                    return "<a href='#' onclick=\"auditInfo(" + rowData.dimTableId + ",'" + rowData.tableName + "','" + rowData.tableDimPrefix + "','" + rowData.tableOwner + "'," + rowData.batchId + "," + rowData.dimTypeId + ");return false;\">审核明细</a>";
                }
            }
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}
var gridConverter = new dhtmxGridDataConverter(converterConfig);
dwrCaller.addAutoAction("queryDate", DimAuditNewAction.queryDimTableDataByCondition, {
    dwrConfig:true,
    converter:gridConverter
});
//初始化grid
var initPage = function () {
    var typeLayout = new dhtmlXLayoutObject("container", "2E");
    typeLayout.cells("a").setText("审核维度编码");
    typeLayout.cells("b").hideHeader();
    typeLayout.cells("a").setHeight(80);
    typeLayout.cells("a").fixSize(false, true);
    typeLayout.hideConcentrate();
    typeLayout.hideSpliter();
    var queryData = [
        {type:"setting", position:"label-left"},
        {type:"block", list:[
            {type:"settings", labelWidth:55, inputWidth:120,  position:"label-left"},
            {type:"select", label:"审核状态：", name:"auditState", options:[
                {text:'全部', value:''},
                {text:'所有未审核', value:'1', selected:true},
                {text:'已审核完成', value:'2'},
                {text:'部分已审核', value:'3'}
            ], className:'inputStyle queryItem'},
            {type:"newcolumn"},
            {type:"select", label:"申请类型：", name:"auditStyle", options:[
                {text:"全部", value:"", selected:true},
                {text:"编码归并申请", value:"1"},
                {text:"编码映射申请", value:"2"}
            ]},
            {type:"newcolumn"},
            {type:"input", label:"申请人：", labelWidth:43, name:"auditApplyer"},
            {type:"newcolumn"},
            {type:"button", name:"query", value:"查询", className:'inputStyle', offsetLeft:0}
        ]}

    ]//end queryData
    var queryform = typeLayout.cells("a").attachForm(queryData);
    var dimauditParam = new biDwrMethodParam();
    dimauditParam.setParamConfig([
        {
            index:0, type:"fun", value:function () {
            var formData = queryform.getFormData();
            formData.auditApplyer = Tools.trim(queryform.getInput("auditApplyer").value);
            return formData;
        }
        }
    ]);

    //查询事件
    queryform.attachEvent("onButtonClick", function () {
        mygrid.clearAll();
        mygrid.load(dwrCaller.queryDate + dimauditParam, "json");

    })
    // Enter查询事件
    queryform.getInput("auditApplyer").onkeypress = function (e) {
        e = e || window.event;
        var keyCode = e.keyCode;
        if (keyCode == 13) {
            mygrid.clearAll();
            mygrid.load(dwrCaller.queryDate + dimauditParam, "json");
        }
    }
    mygrid = typeLayout.cells("b").attachGrid();
    mygrid.setHeader("维度表中文名称,维度表名称,申请人,申请时间,审核类型,审核状态,操作");
    mygrid.setInitWidthsP("15,15,12,15,15,13,15");
    mygrid.setHeaderAlign("center,center,center,center,center,center,center");
    mygrid.setColAlign("left,left,left,center,center,center,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,ro,ro");
    mygrid.setColSorting("na,na,na,na,na,na,na");
    mygrid.setEditable(false);
    mygrid.setColumnCustFormat(4, auditType);
    mygrid.setColumnIds("dimTypeName", "tableName", "userNamecn", "modDate", "codeName", "auditRes", "_buttons");
    mygrid.init();
    mygrid.defaultPaging(20, true);
    mygrid.load(dwrCaller.queryDate + dimauditParam, "json");

}//end initPage
/**
 * 审核的处理办法
 * @param tId
 * @param tVersion
 */
var auditInfo = function (dimTableId, tableName, tableDimPrefix, tableOwner, batchId, dimTypeId) {
    parent.openMenu(tableName + "审核维度编码", "/meta/module/dim/audit/dimauditinfo.jsp?tableId=" + dimTableId + "&batchId=" + batchId + "&tableDimPrefix=" + tableDimPrefix + "&tableName=" + tableName + "&tableOwner=" + tableOwner, "top", null, true);
}

//编码映射审核
var mappAuditInfo = function (dimTableId, tableName, tableDimPrefix, tableOwner, batchId) {
    var url = "/meta/module/dim/audit/mappingAudit.jsp?dimTableId=" + dimTableId + "&batchId=" + batchId + "&tableDimPrefix=" + tableDimPrefix + "&tableName=" + tableName + "&tableOwner=" + tableOwner;
    parent.openMenu(tableName + "审核编码映射", url, "top", null, true);
}
/**
 * 查看的处理方法
 */
var showInfo = function (dimTableId, tableName, tableDimPrefix, batchId, tableOwner) {
    var url = "/meta/module/dim/audit/dimShowinfo.jsp?tableId=" + dimTableId + "&tableName=" + tableName + "&batchId=" + batchId + "&tableDimPrefix=" + tableDimPrefix + "&tableOwner=" + tableOwner;
    parent.openMenu(tableName + "查看维度审核信息", url, "top", null, true);
};

//查看映射
var mappShowInfo = function (dimTableId, tableName, batchId, tableOwner) {
    var url = "/meta/module/dim/audit/showMappingAudit.jsp?dimTableId=" + dimTableId + "&batchId=" + batchId + "&tableOwner=" + tableOwner;
    parent.openMenu(tableName + "映射审核结果查看", url, "top", null, true);
}

dhx.ready(initPage);