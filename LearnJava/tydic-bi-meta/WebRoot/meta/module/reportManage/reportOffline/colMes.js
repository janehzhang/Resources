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
 *       2012-04-20
 ********************************************************/
/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var pageSize = 20;
var queryForm;
var grid;
var issueId;
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();
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


var init = function () {
	var dhxLayout = new dhtmlXLayoutObject(document.body, "1C");
    dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var cell = dhxLayout.cells("a");
    cell.hideHeader();
    grid = cell.attachGrid();
    grid.setHeader("字段名,别名,字段分类,字段属性,已支撑维度粒度,维度值");
    grid.setInitWidthsP("15,15,15,10,25,20");
    grid.setColAlign("left,left,left,left,left,left");
    grid.setHeaderAlign("center,center,center,center,center,center")
    grid.setColTypes("ro,ro,ro,ro,ro,ro");
    grid.setColSorting("na,na,na,na,na,na");
    grid.enableResizing("true,true,true,true,true,true");
    grid.enableMultiselect(false);
    grid.enableCtrlC();
    grid.init();
    grid.load(dwrCaller.getColMes + "?issueId=" + issueId, "json");
}
dhx.ready(init);