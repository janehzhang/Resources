/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       batchImport.js
 *Description：
 *       表类管理-批量导入表
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       吴喜丽
 *Date：
 *       2012-01-11
 ********************************************************/
/**
 * 表类已提供的宏变量
 */
var tblMacro = ['{YY}', '{YYYY}', '{MM}', '{DD}', '{HH}', '{MI}', '{SS}', '{M}', '{D}', '{YYYYMM}', '{YYYYMMN}', '{YYYYMMP}',
    '{YYYYMMDD}', '{YYYYMMDDP}', '{N_YY}', '{N_YYYY}', '{N_MM}', '{N_DD}', '{N_HH}', '{N_MI}', '{N_SS}', '{N_M}', '{N_D}',
    '{N_YYYYMM}', '{N_YYYYMMN}', '{N_YYYYMMP}', '{N_YYYYMMDD}', '{N_YYYYMMDDP}', '{LOCAL_CODE}', '{LOCAL_NAME}'];
/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var pageSize = 30;
var queryForm;
var grid;
var dhxWindow;
var typeValue;	//类型，区分是查询还是从模型中导入操作
//扩展一个事件，用于在展开/搜索之前调用
var expandMonolite = dhtmlXGridObject.prototype._expandMonolite;
dhtmlXGridObject.prototype._expandMonolite = function (n, show, hide) {
    var td = this.parentNode;
    var row = td.parentNode;
    var that = row.grid;
    that.callEvent("onSubRowOpenStart", [row.idd, (!!!row._expanded)]);
    expandMonolite.apply(this, arguments);
}
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();
//表类数据源相关Action
var tableDataSourceConverter = new dhtmlxComboDataConverter({
    valueColumn:"dataSourceId",
    textColumn:"dataSourceName",
    isAdd:true
//    onBeforeConverted:function(data){
//        data.unshift({dataSourceId:"",dataSourceName:""});
//    }

})
dwrCaller.addAutoAction("queryTableDataSource", "TableApplyAction.queryTableDataSource");
dwrCaller.addDataConverter("queryTableDataSource", tableDataSourceConverter);

var tableOwnerConverter = new dhtmlxComboDataConverter({
    valueColumn:"username",
    textColumn:"username"
})
dwrCaller.addAutoAction("getUserNameByDataSourceId", "TableApplyAction.getUserNameByDataSourceId");
dwrCaller.addDataConverter("getUserNameByDataSourceId", tableOwnerConverter);
var dataSourceChange = function (value) {
    dwrCaller.executeAction("getUserNameByDataSourceId", value, function (data) {
        //alert(data.length);
        dhx.env.isIE && CollectGarbage();
    })
}

dwrCaller.addAutoAction("queryDbUsers", "TableApplyAction.queryDbUsers");
dwrCaller.isShowProcess("queryDbUsers", false);

dwrCaller.addAutoAction("getTableColumns", "ImportBatchTableAction.getTableColumns");
dwrCaller.isShowProcess("getTableColumns", false);

dwrCaller.addAutoAction("getTableSpaces", "ImportBatchTableAction.getTableSpaces");
dwrCaller.isShowProcess("getTableSpaces", false);

dwrCaller.addAutoAction("queryTableGroup", "TableApplyAction.queryTableGroup");
dwrCaller.addDataConverter("queryTableGroup", new dhtmxTreeDataConverter({
    idColumn:"tableGroupId", pidColumn:"parGroupId", textColumn:"tableGroupName",
    isDycload:false
}));
dwrCaller.isShowProcess("queryTableGroup", false);

var param = new biDwrMethodParam();
param.setParamConfig([
    {
        index:0, type:"fun", value:function () {
        var data = queryForm.getFormData();
        data.dataSource = data.dataSource + "";
        if (!data.keyWord) {
            data.keyWord = "";
        }
        data.pageSize = pageSize + "";
        data.typeValue = typeValue + "";
        return data;
    }
    }
]);

/**
 * 查询表信息Action定义
 */
dwrCaller.addAutoAction("getQueryTables", "ImportBatchTableAction.getQueryTables", param, function (data) {
    dataRowSizes = new Array(data.rows.length);
});

//表空间数据
var tableSpaceData = "";
var tableSpaceData_str = "";
//层次分类
var tableTypeData = "";
var tableTypeData_str = "";

var treeData = {};
var lastTableType = null;//最后一次显示时的tableType.

dwrCaller.addDataConverter("getQueryTables", new dhtmxGridDataConverter({
    filterColumns:["checkBox", "", "pratablename", "tablename", "tabletypename", "tabletype", "tablegroup", "tablespace", "partitionsql", "tablebuscomment"],
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
        var dataSourceId = queryForm.getCombo("dataSource").getSelectedValue();
        if (dataSourceId == "")
            dataSourceId = 0;
        if (columnIndex == 0) {//第一列，checkbox列
            return 0;
        } else if (columnIndex == 1) {//第二列，获取sub.xml信息
            var owner = queryForm.getCombo("owner").getSelectedValue();
            /*子表点击事件*/
            return dwrCaller.getTableColumns + "&dataSourceId=" + dataSourceId + "&tableName=" + rowData.pratablename + "&owner=" + owner + "&rowIndex=" + rowIndex + "&typeValue=" + typeValue;
        } else if (columnName == 'partitionsql') {//分区SQL
            var name = columnName + rowIndex;
            var str = "<textarea onclick='cancelBubble()'  name=" + name + " id=" + name + " style='width: 155px;height:20px;'>"+(cellValue||"")+"</textarea>";
            return str;
        } else if (columnName == 'tablebuscomment') {//注释
            var name = columnName + rowIndex;
            var tablebuscomment = "";
            if (rowData.tablebuscomment != null && rowData.tablebuscomment != "null")
                tablebuscomment = rowData.tablebuscomment;

            if (tablebuscomment.length > 1000)
                tablebuscomment = tablebuscomment.substring(0, 1000);
            var str = "<textarea  onclick='cancelBubble()' name=" + name + " id=" + name + " style='width: 155px;height:20px;'>" + tablebuscomment + "</textarea>";
            return str;
        } else if (columnName == 'tablegroup') {//业务类型
            var name = columnName + rowIndex;
            var str = "<input name=" + name + " id=" + name + " type='text' style='width: 155px;' onclick=\"showTableGroupTree(this," + rowIndex + ");\"/>";
            str += "<input type=\"hidden\" name=\"" + columnName + "id" + rowIndex + "\" id=\"_" + columnName + "id" + rowIndex + "\"/>";
            return str;
        } else if (columnName == 'tabletypename') {//中文名称
            var name = columnName + rowIndex;
            var tabletypename = "";
            if (rowData.tabletypename != null && rowData.tabletypename != "null")
                tabletypename = rowData.tabletypename;

            if (tabletypename.length > 64)
                tabletypename = tabletypename.substring(0, 64);
            var str = "<input onclick='cancelBubble()'  name=" + name + " id=" + name + " type='text' style='width: 155px;' value='" + tabletypename + "'/>";
            return str;
        } else if (columnName == 'tablename') {//表类名称
            var name = columnName + rowIndex;
            var str = "<input onclick='cancelBubble()' name=" + name + " id=" + name + " type='text' style='width: 155px;' value='" + rowData.pratablename + "'/>";
            return str;
        } else if (columnName == 'tablespace') {//表空间列
            var name = columnName + rowIndex;
            if (tableSpaceData_str == "") {
                //异步加载表空间
                var str = "";
                str = "<select onclick='cancelBubble()' id=" + name + " name=" + name + " style='width: 155px;'>";
                var _str = "";
//                str += "<option value='0'>--请选择--</option>";
//                _str += "<option value='0'>--请选择--</option>";
                ImportBatchTableAction.getTableSpaces(dataSourceId, {
                    async:false,
                    callback:function (data) {
                        if (data != null && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                if (cellValue == data[i]) {
                                    str += "<option value='" + data[i] + "' selected>" + data[i] + "</option>";
                                    _str += "<option value='" + data[i] + "' selected>" + data[i] + "</option>";
                                } else {
                                    str += "<option value='" + data[i] + "'>" + data[i] + "</option>";
                                    _str += "<option value='" + data[i] + "'>" + data[i] + "</option>";
                                }
                            }
                        } else {
                            str += "<option value='" + cellValue + "' selected>" + cellValue + "</option>";
                            _str += "<option value='" + cellValue + "' selected>" + cellValue + "</option>";
                        }
                    }
                });
                str += "</select>";
                _str += "</select>";
                tableSpaceData_str = _str;
                return str;
            } else {
                var _str = "<select onclick='cancelBubble()' id=" + name + " name=" + name + " style='width: 155px;'>";
                tableSpaceData = _str + tableSpaceData_str;
                return tableSpaceData;
            }
        } else if (columnName == 'tabletype') {//层次分类
            var name = columnName + rowIndex;
            if (tableTypeData_str == "") {
                //异步加载层次分类
                var str = "";
                str = "<select onclick='cancelBubble()' id=" + name + " name=" + name + " style='width: 155px;' onChange=\"showTableGroup(this," + rowIndex + "," + (columnIndex + 1) + ");\">";
                var _str = "";
                //添加请选择
//                str += "<option value=''>--请选择--</option>";
//                _str += "<option value=''>--请选择--</option>";
                ImportBatchTableAction.getTableTypes({
                    async:false,
                    callback:function (data) {//data是二元数组
                        if (data != null && data.length > 0) {
                            for (var r = 0; r < data.length; r++) {
                                if (cellValue == data[r][0]) {
                                    str += "<option value='" + data[r][0] + "' selected>" + data[r][1] + "</option>";
                                    _str += "<option value='" + data[r][0] + "' selected>" + data[r][1] + "</option>";
                                } else {
                                    str += "<option value='" + data[r][0] + "'>" + data[r][1] + "</option>";
                                    _str += "<option value='" + data[r][0] + "'>" + data[r][1] + "</option>";
                                }
                            }
                        } else {
                            str += "<option value='" + cellValue + "' selected>" + cellValue + "</option>";
                            _str += "<option value='" + cellValue + "' selected>" + cellValue + "</option>";
                        }
                    }
                });
                str += "</select>";
                _str += "</select>";
                tableTypeData_str = _str;
                return str;
            } else {
                var _str = "<select onclick='cancelBubble()' id=" + name + " name=" + name + " style='width: 155px;' onChange=\"showTableGroup(this," + rowIndex + "," + (columnIndex + 1) + ");\">";
                tableTypeData = _str + tableTypeData_str;
                return tableTypeData;
            }
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    },
    onBeforeConverted:function (data) {
        if (data && data.length > 0) {
            var showData = data[0].errorstring;
            if (showData != undefined && showData != null) {
                dhx.alert(showData);
                return [];
            }
        }
    }
}));
function cancelBubble(e) {
    e = e || window.event;
    if (!e)return false;
    if (e.preventDefault) {
        e.preventDefault();
    }
    e.cancelBubble = true;
    return false;
}
function startSelect(){
    alert("1111");
    return true
}

var treeObj = new Object();
var showTableGroup = function (obj, rowId, colId) {
    //tablegroup
//    var str = "<input type=\"hidden\" name=\"tableGroupId"+rowId+"\" id=\"_tableGroupId"+rowId+"\"></input>";
//    str += "<input rowId='"+rowId+"' colId='"+colId+"' name=\"tableGroup"+rowId+"\" id=\"tableGroup"+rowId+"\" onclick=\"showTableGroupTree(this);\" type=\"text\" style=\"width: 155px;\"></input>"
//    grid.cells(rowId,colId).setValue(str);
    //表分类下拉树数据准备。
    if (!treeObj[rowId]) {
        treeObj[rowId] = new loadTableGroupTree($("tablegroup" + rowId), $("_tablegroupid" + rowId));
    }
}

var showTableGroupTree = function (input, rowId) {
    treeObj[rowId].showGroupTree(dwr.util.getValue("tabletype" + rowId));
    treeObj[rowId].treeDiv.style.display = "inline";
    Tools.divPromptCtrl(treeObj[rowId].treeDiv, input, true);
}

/**
 * 加载业务类型树形结构。
 * @param target
 * @param form
 */
var loadTableGroupTree = function (target, hide) {
    //创建tree Div层
    var div = dhx.html.create("div", {
        style:"display:none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
            "z-index:1000"
    });
    div.style.width = target.offsetWidth + "px";
    this.treeDiv = div;
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    target.readOnly = true;
    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    //树双击鼠标事件
    tree.attachEvent("onDblClick", function (nodeId) {
        target.value = tree.getItemText(nodeId);
        hide.value = nodeId;
        //关闭树
        div.style.display = "none";
    });
    //div.style.display = "none";
    this.tree = tree
    this.showGroupTree = function (tableType) {
        var that = this;
        var show = function () {
            var childs = tree.getAllSubItems(0);
            if (childs) {
                var childIds = (childs + "").split(",");
                for (var i = 0; i < childIds.length; i++) {
                    that.tree.deleteItem(childIds[i]);
                }
            }
            that.tree.loadJSONObject(treeData[tableType]);
        }
        if (!treeData[tableType]) {
            dwrCaller.executeAction("queryTableGroup", tableType, function (data) {
                treeData[tableType] = data;
                show();
                lastTableType = tableType;
            })
        } else {
            show();
            lastTableType = tableType;
        }
    }
}
var batchImportWin = function () {
//    var dhxWindow = new dhtmlXWindows();
    var batchWindow;
    if (dhxWindow.window("batchWindow") != null) {
        batchWindow = dhxWindow.window("batchWindow");
        batchWindow.show();
    } else {
        batchWindow = dhxWindow.createWindow("batchWindow", 0, 0, 300, 150);
        batchWindow.stick();
        batchWindow.button("minmax1").hide();
        batchWindow.button("park").hide();
        batchWindow.button("stick").hide();
        batchWindow.button("sticked").hide();
        batchWindow.center();
        batchWindow.denyResize();
        batchWindow.denyPark();
        batchWindow.setText("模型文件上传");
        batchWindow.keepInViewport(true);
        batchWindow.show();
        batchWindow.attachURL(urlEncode(getBasePath() + "/meta/module/tbl/import/modelUpload.jsp"));
    }
}
/*子表数据缓存数组*/
var subDataArray = {};
/**
 * 初始化界面
 */
var init = function () {
    dhxWindow = new dhtmlXWindows();
    var dhxLayout = new dhtmlXLayoutObject(document.body, "3E");
    dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.setText("表类批量导入");
    topCell.setHeight(80);
    topCell.fixSize(true, true);
    queryForm = topCell.attachForm(
        [
            {type:"settings", position:"label-left", inputWidth:120},
            {type:"combo", label:"数据源：", name:"dataSource", inputHeight:22},
            {type:"newcolumn"},
            {type:"combo", label:"所属用户：", name:"owner", validate:"NotEmpty", inputHeight:17},
            {type:"newcolumn"},
            {type:"input", label:"关键字：", name:"keyWord", inputHeight:17},
            {type:"newcolumn"},
            {type:"button", name:"queryBtn", value:"查询", offsetLeft:0, offsetTop:0},
            {type:"newcolumn"},
            {type:"button", name:"importModel", value:"从模型导入", offsetLeft:0, offsetTop:0}
        ]
    );
    //queryForm.getCombo("dataSource").enableFilteringMode(true);
    //queryForm.defaultValidateEvent();
    queryForm.getCombo("dataSource").enableFilteringMode(true);
//    queryForm.getCombo("owner").enableFilteringMode(true);
    queryForm.getCombo("dataSource").loadXML(dwrCaller.queryTableDataSource, function () {
    });
    queryForm.getCombo("dataSource").attachEvent("onChange", function () {
        var val = queryForm.getCombo("dataSource").getSelectedValue();
        var text = queryForm.getCombo("dataSource").getSelectedText();
        if (val == "" && text != "") {
            val = 0;
        } else if (val == "" && text == "") {
            val = -1;
        }
        if (val || val == 0) {
            queryForm.getCombo("owner").clearAll(true);
            dwrCaller.executeAction("queryDbUsers", val, function (data) {
                var options = [];
                if (data) {
                    data = dhx.isArray(data) ? data : [data];
                    for (var i = 0; i < data.length; i++) {
                        options.push({text:data[i], value:data[i]});
                    }
                }
                queryForm.getCombo("owner").addOption(options);
                TableApplyAction.queryDefaultUserByDataSource(val, function (name) {
                    if (queryForm.getCombo("owner").optionsArr && queryForm.getCombo("owner").optionsArr.length) {
                        for (var i = 0; i < queryForm.getCombo("owner").optionsArr.length; i++) {
                            if (queryForm.getCombo("owner").optionsArr[i].text == name) {
                                queryForm.getCombo("owner").selectOption(i, false, false);
                                break;
                            }
                        }
                    }
                });
            });
        }
    });
    //queryForm.defaultValidateEvent();

    //实体表表单
    var middleCell = dhxLayout.cells("b");
    middleCell.fixSize(true, false);
    middleCell.setHeight(427);
    middleCell.hideHeader();
    var bottomCell = dhxLayout.cells("c");
    bottomCell.setHeight(55);
    bottomCell.fixSize(true, true);
    bottomCell.hideHeader();
    grid = middleCell.attachGrid();
    grid.setHeader("{#checkBox},&nbsp;,实体表名称,表类名称,中文名称,层次分类,业务类型,表空间,分区SQL,注释");
    grid.setInitWidthsP("3,3,16,16,16,16,16,16,16,16");
    grid.setColAlign("center,left,left,left,left,left,left,left,left");
    grid.setHeaderAlign("center,center,center,center,center,center,center,center,center,center")
    grid.setColTypes("ch,sub_row_ajax,ro,ro,ro,ro,ro,ro,txt,txt");
    grid.setColSorting("na,na,na,na,na,na,na,na,na,na");
    grid.enableResizing("false,false,true,true,true,true,true,true,true,true");
    grid.setEditable(true);
    grid.enableMultiselect(true);
    grid.enableSelectCheckedBoxCheck(2);
    grid.setColumnIds("'','',praTableName,tableName,tableTypeName,tableType,tableGroup,tableSpace,partitionSql,tableBusComment");
    grid.init();
    grid.defaultPaging(pageSize);
//    grid.load(dwrCaller.getQueryTables, "json");
    grid.attachEvent("onXLE", function (grid_obj, count) {
        var state = grid_obj.getStateOfView();
        for (var i = state[1]; i < Math.min(grid_obj.rowsBufferOutSize + state[1], grid_obj.getRowsNum()); i++) {
            dhtmlxValidation.addValidation(grid_obj.cells(i, 0).cell.parentNode, [
                {target:"tablename" + i, rule:"NotEmpty,MaxLength[1000],ValidByCallBack[tblNameCheck]"},
                {target:"tabletypename" + i, rule:"NotEmpty,MaxLength[64]"},
                {target:"tabletype" + i, rule:"NotEmpty"},
                {target:"tablegroup" + i, rule:"NotEmpty"},
                {target:"tablespace" + i, rule:"NotEmpty"},
                {target:"partitionsql" + i, rule:"MaxLength[1000]"},
                {target:"tablebuscomment" + i, rule:"MaxLength[1000]"}
            ]);
            showTableGroup($("tabletype" + i),i);
        }
    });
    grid.attachEvent("onSubRowOpenStart", function (id, state) {
        if (!state) {//展开
//        	alert("展开！");
            getColumnData(id);
        }
    });
    grid.entBox.onselectstart=function(){
        return true;
    }
    /*分页事件*/
//	grid.attachEvent("onPageChanged", function(ind,fInd,lInd){
//		alert(ind);
//		alert(fInd);
//		alert(lInd);
//	});
    grid.attachEvent("onBeforePageChanged", function (ind, count) {
        if (ind != count)
            getPageData(ind, grid.rowsBufferOutSize);
        return true;
    });
//	grid.attachEvent("onPaging", function(count){
//		alert(count);
//	});


    queryForm.attachEvent("onButtonClick", function (id) {
        if (id == "queryBtn") {
            //进行数据查询。
            if (queryForm.validate()) {
                typeValue = "search";
                grid.clearAll();
                grid.load(dwrCaller.getQueryTables, "json");
            }
        } else if (id == "importModel") {
            if (queryForm.validate()) {
                batchImportWin();
            }
        }
    });
    importForm = bottomCell.attachForm(
        [
            {
                type:"block", list:[
                {type:"button", name:"importBtn", value:"导入", offsetLeft:document.body.clientWidth - 150}
            ]
            }
        ]
    );
    importForm.attachEvent("onButtonClick", function (id) {
        if (id == "importBtn") {
            saveAllMes(grid);
        }
    });

    if (!dataTypeData) {
        dwrCaller.executeAction("queryColumnDataType", function () {
        });
    }
    dhx.env.isIE && CollectGarbage();
}
dhx.ready(init);
/*页面缓存数据*/
var _hiddenPageData = [];
/*取页数据(必须是选择后的数据)*/
var getPageData = function (currentPage, pcounts) {
    var checkRows = grid.getCheckedRows(0);
    if (checkRows && (1 * pcounts) > 0) {
        var rowsId = checkRows.split(',');
//		alert("获取页数据时选择条数>>>>"+rowsId.length);
        //下标起始
        var sflag = (1 * currentPage - 1) * (1 * pcounts);
        //下标结束
        var eflag = (1 * currentPage - 1) * (1 * pcounts) + (1 * pcounts) - 1;
        for (var id = 0; id < rowsId.length; id++) {
            var rowIndex = grid.getRowIndex(rowsId[id]);//父表的行号
//			alert("rowIndex="+rowIndex);
//			alert("sflag=="+sflag);
//			alert("eflag=="+eflag);
            if (1 * sflag <= 1 * rowIndex && 1 * rowIndex <= 1 * eflag) {//在当前页内
                _hiddenPageData["praTableName" + rowIndex] = grid.cells(rowIndex, 2).getValue();
                _hiddenPageData["tableTypeName" + rowIndex] = document.getElementById("tabletypename" + rowIndex).value;
                _hiddenPageData["tableType" + rowIndex] = document.getElementById("tabletype" + rowIndex).value;
                _hiddenPageData["tableGroup" + rowIndex] = document.getElementById("_tablegroupid" + rowIndex).value;
                _hiddenPageData["tableSpace" + rowIndex] = document.getElementById("tablespace" + rowIndex).value;
                _hiddenPageData["partitionSql" + rowIndex] = document.getElementById("partitionsql" + rowIndex).value;
                _hiddenPageData["tableBusComment" + rowIndex] = document.getElementById("tablebuscomment" + rowIndex).value;
                _hiddenPageData["tableName" + rowIndex] = document.getElementById("tablename" + rowIndex).value;
                _hiddenPageData[rowIndex] = currentPage;//保存页码
            }
        }
    }
//	if((1*pcounts) > 0){
//		//在每次存放前，先清除对应值
////		_hiddenPageData[currentPage] = [];
//		for(var i = 0; i < (1*pcounts); i++){
////			var pageData = {};
//			//下标起始
//			var xflag = (1*currentPage-1)*(1*pcounts)+i;
//			_hiddenPageData["praTableName"+xflag] = grid.cells(i, 2).getValue();
//			_hiddenPageData["tableTypeName"+xflag] = document.getElementById("tabletypename"+xflag).value;
//            _hiddenPageData["tableType"+xflag] = document.getElementById("tabletype"+xflag).value;
//            _hiddenPageData["tableGroup"+xflag] = document.getElementById("_tablegroupid"+xflag).value;
//            _hiddenPageData["tableSpace"+xflag] = document.getElementById("tablespace"+xflag).value;
//            _hiddenPageData["partitionSql"+xflag] = document.getElementById("partitionsql"+xflag).value;
//            _hiddenPageData["tableBusComment"+xflag] = document.getElementById("tablebuscomment"+xflag).value;
//            _hiddenPageData["tableName"+xflag] = document.getElementById("tablename"+xflag).value;
////            _hiddenPageData.push(pageData);
//		}
//	}
}
/*取列数据*/
var getColumnData = function (rowIndex) {
    //取出子表所有值
    if (typeof dataRowSizes[rowIndex] != "undefined" && dataRowSizes[rowIndex] > 0) {
        //在每次存放前，先清除对应值
        subDataArray["colDetail" + rowIndex] = [];
        for (var r = 0; r < dataRowSizes[rowIndex]; r++) {
            var colData = {};
            //获取子表值
            for (var c = 0; c < subColFieldId.length; c++) {
                //按行号保存子表值
                if (c == 2) {//数据类型
                    //colData[subColFieldName[c]] = dataTypeCombo[rowIndex][(r+1)].getComboText();


                    //数据类型处理。
                    var reg = /(\w+)(\((\d+)((,)(\-?\d+))?\))*/
                    var match = reg.exec(dataTypeCombo[rowIndex][(r + 1)].getComboText());
                    var fullDataType = dataTypeCombo[rowIndex][(r + 1)].getComboText();
                    if (fullDataType.toUpperCase().indexOf("DATA") > 0) {
                        fullDataType = "DATE";//ORACLE建表语句DATE类型不能有长度
                    }
                    colData.fullDataType = fullDataType;
                    colData[subColFieldName[c]] = match[1].toUpperCase();
                    switch (match[1].toLowerCase()) {
                        case "varchar":
                        case "varchar2":
                        case "nvarchar":
                        case "nvarchar2":
                        case "raw":
                        case "char":
                        {
                            colData.colSize = match[3];
                            break;
                        }
                        case "number":
                        {
                            colData.colSize = match[3] ? match[3] : "";
                            colData.colPrec = match[6] ? match[6] : "";
                            break;
                        }
                        case "date":
                        {
                            if (!colData.colSize) {
                                colData.colSize = 7;
                            }
                            break;
                        }
                        default:
                            break;
                    }
                } else if (c == 3 || c == 4) {//主键列 或 是否为空列
                    colData[subColFieldName[c]] = dwr.util.getValue(subColFieldId[c] + rowIndex + (r + 1)) == true ? 1 : 0;
                } else {
                    colData[subColFieldName[c]] = dwr.util.getValue(subColFieldId[c] + rowIndex + (r + 1));
                }
            }
            //获取维度值
            colData["colBusType"] = $("_colBusType" + rowIndex + (r + 1)).value;
            colData["dimLevel"] = $("_dimLevel" + rowIndex + (r + 1)).value;
            colData["dimColId"] = $("_dimColId" + rowIndex + (r + 1)).value;
            colData["dimTableId"] = $("_dimTableId" + rowIndex + (r + 1)).value;
            colData["dimTypeId"] = $("_dimTypeId" + rowIndex + (r + 1)).value;
            subDataArray["colDetail" + rowIndex].push(colData);
        }
    }
}

var dataTypeCombo = {};//数据类型combo对象。
var dataTypeData = undefined;//缓存DATATYPE数据
dwrCaller.addAutoAction("queryColumnDataType", "TableApplyAction.queryColumnDataType", function (data) {
    dataTypeData = data;
});
/**
 * 系统表下拉数据转换器
 */
var sysCodeSelectConverter = new dhtmlxComboDataConverter({
    valueColumn:'codeItem',
    textColumn:"codeName"
})
dwrCaller.addDataConverter("queryColumnDataType", sysCodeSelectConverter);

/*清除对应维度列信息*/
var clearComment = function (rowIndex, subRowIndex) {
    var input = $("_dimInfo" + rowIndex + subRowIndex);
    input.value = "";
    //清除隐藏列值
    input = $("_colBusType" + rowIndex + subRowIndex);
    input.value = "";
    input = $("_dimLevel" + rowIndex + subRowIndex);
    input.value = "";
    input = $("_dimColId" + rowIndex + subRowIndex);
    input.value = "";
    input = $("_dimTableId" + rowIndex + subRowIndex);
    input.value = "";
    input = $("_dimTypeId" + rowIndex + subRowIndex);
    input.value = "";
}
/**
 * 表类名验证。
 * @param value
 */
var tblNameCheck = function (value) {
    //宏变量正则表达式。
    var macro = tblMacro.join("|");
    var tblNameRegStr = "^(" + macro + "|\\w)+$";
    var reg = new RegExp(tblNameRegStr);
    var res = reg.test(value) ? true : "表类名不符合规范！";
    if (res != true) {
        return res;
    }
    if (value.length > 30) {
        return "表类名长度超出30个字符！";
    }
    var dataSourceId = queryForm.getCombo("dataSource").getSelectedValue();
    if (dataSourceId == "")
        dataSourceId = 0;
    var tableOwner = queryForm.getCombo("owner").getSelectedValue();

    //远程验证是否表已在数据库存在。
    TableApplyAction.isExistsMatchTables(value, dataSourceId, tableOwner, {
        async:false,
        callback:function (data) {
            if (data) {
                res = "您输入的表名在数据库中已存在，请重新输入！";
            } else {
                res = true;
            }
        }
    })
    dhx.env.isIE && CollectGarbage();
    return res;
}
/**
 * 数据类型验证
 * @param value
 */
var supportStr = ""
var dataTypeValidate = function (value) {
    //先找到支持的数据类型，构造正则表达式
    if (!supportStr) {
        if (dataTypeData && dataTypeData.options.length > 0) {
            for (var i = 0; i < dataTypeData.options.length; i++) {
                supportStr += dataTypeData.options[i].text + ((i == dataTypeData.options.length - 1) ? "" : "|");
            }
//            supportStr=supportStr.replace(/NUMBER/ig,"");
            supportStr = supportStr.replace(/VARCHAR2/ig, "");
            supportStr = supportStr.replace(/VARCHAR/ig, "");
            supportStr = supportStr.replace(/CHAR/ig, "");
            supportStr = supportStr.replace(/RAW/ig, "");
//            supportStr=supportStr.replace(/DATE/ig,"");
            //number 特殊处理
            supportStr += "|NUMBER\\(\\d+,\\-?\\d+\\)|NUMBER\\(\\d+\\)";
            //varchar2
            supportStr += "|VARCHAR2\\(\\d+\\)";
            //varchar
            supportStr += "|VARCHAR\\(\\d+\\)";
            //char
            supportStr += "|CHAR\\(\\d+\\)";
            //raw
            supportStr += "|RAW\\(\\d+\\)";
        } else {
            return "未找到支持的数据类型，请先配置相应的数据类型！";
        }
    }
    var reg = new RegExp("^(" + supportStr + ")$", "i");
    var res = reg.exec(value);
    if (!res) {
        return "不支持此数据类型或者未填长度大小！";
    }
    reg = /(\w+)(\((\d+)((,)(\-?\d+))?\))*/
    var match = reg.exec(value)
    switch (match[1].toLowerCase()) {
        case "varchar":
        case "varchar2":
        case "nvarchar":
        case "nvarchar2":
        {
            if (match[3] > 4000 || match[3] < 1) {
                return "VARCHAR、VARCHAR2、VARCHAR、NVARCHAR2取值范围为1至4000";
            }
            break;
        }
        case "raw":
        case "char":
        {
            if (match[3] > 2000 || match[3] < 1) {
                return "CHAR长度取值范围为1至2000";
            }
            break;
        }
        case "number":
        {
            if (match[3] && (match[3] > 38 || match[3] < 1)) {
                return "NUMBER类型精度取值范围为1至38";
            }
            if (match[6] && (match[6] > 127 || match[6] < -84)) {
                return "NUMBER类型刻度取值范围为-84至127";
            }
            break;
        }
        default:
            break;
    }
    return true;
}
/**
 * 新增一行列配置
 * rowIndex:父行序列号（从0开始）
 * subRowE：子表最后行数值（从1开始）
 */
var addColumnRow = function (rowIndex, subRowE, isShowOper) {
    dataRowSizes[rowIndex] = 1 * dataRowSizes[rowIndex] + 1;
    subRowE = subRowE + 1;
    if (!$("_columnRow" + rowIndex + subRowE)) {
        var tr = document.createElement("tr");
        tr._index = rowIndex;
        tr._subRow = subRowE;
        tr.id = "_columnRow" + rowIndex + subRowE;
        $("_clumnContentBody" + rowIndex).appendChild(tr);
        //名称列
        var td = tr.insertCell(0);
        td.align = "center";
        var input = dhx.html.create("input", {className:"dhxlist_txt_textarea", name:"colName" + rowIndex + subRowE, id:"_colName" + rowIndex +
            subRowE, type:"TEXT", style:"width: 90%;height:15px;line-height:15px;text-transform: uppercase"});
        //添加事件，开始输入时，新增一行。
        Tools.addEvent(input, "keyup", function (e) {
            e = (e || window.event);
            var element = e.srcElement || e.target;
            var thisTr = element.parentNode.parentNode;
            //新增空白行。
            if (!thisTr.nextSibling) {
                addColumnRow(thisTr._index, thisTr._subRow, true);
            }
        })
        td.appendChild(input);
        //中文名列
        td = tr.insertCell(1);
        td.align = "center";
        input = dhx.html.create("input", {name:"colNameCn" + rowIndex + subRowE, id:"_colNameCn" + rowIndex +
            subRowE, className:"dhxlist_txt_textarea", type:"TEXT", style:"width: 90%;height:15px;line-height:15px;"})
        td.appendChild(input);
        //类型列。
        td = tr.insertCell(2);
        td.align = "center";
        var inputDiv = dhx.html.create("select", {id:"_colDatatype" + rowIndex + subRowE, name:"colDatatype" + rowIndex + subRowE, style:"width:90%;height:15px;line-height:15px;"});
        td.appendChild(inputDiv);
        var dataCombo = new dhtmlXCombo(inputDiv);
        if (dataTypeData) {
            dataCombo.addOption(dataTypeData.options);
        } else {
            dataCombo.loadXML(dwrCaller.queryColumnDataType, function () {
                dataCombo.setComboText("NUMBER");
            });
        }
        dataCombo.setComboText("NUMBER");
        dataTypeCombo[rowIndex][subRowE] = dataCombo;
        //主键列。
        td = tr.insertCell(3);
        td.align = "center";
        input = dhx.html.create("input",
            {name:"isPrimary" + rowIndex + subRowE, id:"_isPrimary" + rowIndex + subRowE, type:"checkbox", style:"width: 90%;"});
        //主键选择事件
        input.onclick = function (e) {
            e = e || window.event;
            var target = e.target || e.srcElement;
            var clickIndex = target.parentNode.parentNode._index;
            var clickSubRow = target.parentNode.parentNode._subRow;
            if (target.checked) {//如果是主键。不能允许空
                $("_colNullabled" + clickIndex + clickSubRow).checked = false;
            }
        }
        td.appendChild(input);
        //是否允许空列
        td = tr.insertCell(4);
        td.align = "center";
        input = dhx.html.create("input",
            {name:"colNullabled" + rowIndex + subRowE, id:"_colNullabled" + rowIndex + subRowE, type:"checkbox", style:"width: 90%;"})
        input.onclick = function (e) {
            e = e || window.event;
            var target = e.target || e.srcElement;
            var clickIndex = target.parentNode.parentNode._index;
            var clickSubRow = target.parentNode.parentNode._subRow;
            if (target.checked && $("_isPrimary" + clickIndex + clickSubRow).checked) {//如果运行为null
                dhx.alert("主键不允许为空！");
                target.checked = false;
            }
        }
        td.appendChild(input);
        //默认值列
        td = tr.insertCell(5);
        td.align = "center";
        input = dhx.html.create("input", {name:"defaultVal" + rowIndex + subRowE, id:"_defaultVal" + rowIndex +
            subRowE, className:"dhxlist_txt_textarea", type:"TEXT", style:"width: 90%;height:15px;line-height:15px;"})

        td.appendChild(input);


        //注释列
        td = tr.insertCell(6);
        td.align = "center";
        input = dhx.html.create("textarea", {name:"colBusComment" + rowIndex + subRowE, id:"_colBusComment" + rowIndex +
            subRowE, className:"dhxlist_txt_textarea"});//,style:"width: 65%;height:15px;line-height:15px;"
        td.appendChild(input);
        //td.innerHTML += '<img src="../../../resource/images/more.png" alt="更多" onclick="openComment(this)">';
        //维度列
        td = tr.insertCell(7);
        td.align = "center";
//        //for Ie7
        td.innerHTML = '<input type="text" id="_dimInfo' + rowIndex + subRowE + '" name="dimInfo' + rowIndex + subRowE + '" class="dhxlist_txt_textarea" readOnly="true" style="width:70%" onclick="getDimInfo(\'' + rowIndex + '\',\'' + subRowE + '\');">';
        td.innerHTML += '<img src="../../../resource/images/cancel.png" style="width:12px;height:12px;" title="清除维度信息" onclick="clearComment(' + rowIndex + ',' + subRowE + ');">';

        //新增维度有关的hidden隐藏域
        td.appendChild(dhx.html.create("input", {type:"hidden", name:"colBusType" + rowIndex + subRowE, value:1, id:"_colBusType" + rowIndex + subRowE}));//默认非维度，指标
        td.appendChild(dhx.html.create("input", {type:"hidden", name:"dimLevel" + rowIndex + subRowE, id:"_dimLevel" + rowIndex + subRowE}));//维度层级
        td.appendChild(dhx.html.create("input", {type:"hidden", name:"dimColId" + rowIndex + subRowE, id:"_dimColId" + rowIndex + subRowE}));//关联的列ID。
        td.appendChild(dhx.html.create("input", {type:"hidden", name:"dimTableId" + rowIndex + subRowE, id:"_dimTableId" + rowIndex + subRowE}));//关联的维度表ID
        td.appendChild(dhx.html.create("input", {type:"hidden", name:"dimTypeId" + rowIndex + subRowE, id:"_dimTypeId" + rowIndex + subRowE}));
        td = tr.insertCell(8);
        td.align = "center";
        td.innerHTML = '<img src="../../../resource/images/cancel.png" alt="删除" onclick="removeRow(' + rowIndex + ',' + subRowE + ')" style="width:16px;height: 16px;cursor: pointer">';
        //为tr添加验证机制
        dhtmlxValidation.addValidation(tr, [
            {target:"_colName" + rowIndex + subRowE, rule:"NotEmpty,MaxLength[32],ValidAplhaNumeric,ValidByCallBack[columnNameCheck&" + rowIndex + "&" + subRowE + "]"},
            {target:"_colNameCn" + rowIndex + subRowE, rule:"MaxLength[32]"},
            {target:dataTypeCombo[rowIndex][subRowE].DOMelem_input, rule:"NotEmpty,ValidByCallBack[dataTypeValidate]"},
            {target:"_colBusComment" + rowIndex + subRowE, rule:"MaxLength[1000]"},
            {target:"_defaultVal" + rowIndex + subRowE, rule:"ValidByCallBack[defaultValValidate&" + rowIndex + "&" + subRowE + "]"}
        ]);
    }
    return subRowE;
}
/*收束后列名验证*/
var columnNameCheckByColse = function (value, rowIndex) {
    var reg = /^[a-zA-Z]\w*/;
    if (reg.exec(value)) {
        //判断列名是否有重复
        var count = 0;
        for (var i = 0; i < subDataArray["colDetail" + rowIndex].length; i++) {
            if (subDataArray["colDetail" + rowIndex][i].colName == value)
                count++;
        }
        return count == 1 ? true : "列名有重复";
    } else
        return "列名不符合规范！";
}
/**
 * 列名验证。
 * @param value
 */
var columnNameCheck = function (value, rowIndex, subRowIndex) {
    var reg = /^[a-zA-Z]\w*/;
    if (reg.exec(value)) {
        //判断列名是否有重复
        var count = 0;
        var tableObj = $("_clumnContentTable" + rowIndex);
        for (var i = 1; i < tableObj.rows.length - 1; i++) {
            if ($("_colName" + rowIndex + i + "").value == value) {
                count++
            }
        }
        return count == 1 ? true : "列名有重复";
    }
    return "列名不符合规范！";
}
/**默认值验证*/
var defaultValValidate = function (value, rowIndex, subRowIndex) {
    // 根据行索引获得数据类型信息
    var dataType = dataTypeCombo[rowIndex][subRowIndex].getComboText();
    //数据类型为number，验证合法性。
//	alert(dataType)
    if (dataType.search("NUMBER") > -1) {
        if (dataType.indexOf("(") != -1) {
            var _dataType = dataType.split("(")[1].split(")")[0];
        } else {
            var _dataType = dataType;
        }
        //数字验证（包含小数）。
        var reg = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/;
        // 获得数据类型信息
        if (!reg.exec(value)) {
            return "数据类型不匹配！"
        } else if (_dataType.indexOf(",") != -1) {// 1-数据类型有小数验证
            if (value.indexOf(".") != -1) {// 1.1-默认值有小数
                var _inputValue = value.toString().split(".");
                if (_inputValue[0].toString().length > parseInt(_dataType.split(",")[0]) || _inputValue[1].toString().length > parseInt(_dataType.split(",")[1])) {
                    return "数据精度错误！";
                }
            } else if (!value.indexOf(".") != -1) {// 1.2-默认值没有小数
                if (value.toString().length > parseInt(_dataType.split(",")[0])) {
                    return "数据精度错误！";
                }
            }
        } else if (!_dataType.toString().indexOf(".") != -1) {// 2-数据类型没有小数验证
            if (_dataType == "NUMBER") {// 数据默认长度22不匹配
                if (value.toString().length > 22) {
                    return "数据默认长度大于22！";
                }
            }
            if (value.toString().indexOf(".") != -1) {// 2.1-默认值有小数
                return "数据精度错误！";
            } else if (!value.toString().indexOf(".") != -1) {// 2.2-默认值没有小数
                if (value.toString().length > parseInt(_dataType)) {
                    return "数据精度错误！";
                }
            }
        }
    } else if (dataType.search("CHAR") > -1) {
        var _dataType = dataType.split("(")[1].split(")")[0];
        if (value.toString().length > parseInt(_dataType)) {
            return "数据长度错误！"
        }
    }
    return true;
}
/**
 * 清空列信息表单数据
 * 修改人：吴喜丽
 * 修改时间：2012-01-05
 * 修改原因：增加confirm提示信息
 */
var clearAllColumnData = function (boo, rowIndex) {
    var tableObj = $("_clumnContentTable" + rowIndex);
    if (boo == true) {
        dhx.confirm("确定清除？清除后只能重新操作！", function (b) {
            if (b) {
                dataRowSizes[rowIndex] = 0;//qingchusuoyou
                for (var rowIndex = 1; rowIndex < tableObj.rows.length - 1; rowIndex++) {
                    var row = tableObj.rows[rowIndex--];
                    row.parentNode.removeChild(row);
                }
                /*
                 var tableType = dwr.util.getValue("_tableType");
                 if(tableType == 3){
                 initCol();
                 }
                 */
            }
        })
    } else {
        for (var rowIndex = 1; rowIndex < tableObj.rows.length - 1; rowIndex++) {
            var row = tableObj.rows[rowIndex--];
            row.parentNode.removeChild(row);
        }
        /*
         var tableType = dwr.util.getValue("_tableType");
         if(tableType == 3){
         initCol();
         }
         */
    }
}

/**
 *子表方法
 */
/*
 *子表全局变量
 */
//字段列名称
var subColFieldName = new Array('colName', 'colNameCn', 'colDatatype', 'isPrimary', 'colNullabled', 'defaultVal', 'colBusComment', 'dimInfo');
//字段列ID名称
var subColFieldId = new Array('_colName', '_colNameCn', '_colDatatype', '_isPrimary', '_colNullabled', '_defaultVal', '_colBusComment', '_dimInfo');
//列总数
var dataRowSizes = [];
/*主键与非空处理*/
var showColNullAbled = function (rowIndex, subRowId, obj) {
    if (obj.checked) {
        document.getElementById("_isPrimaryDiv" + rowIndex + subRowId).innerHTML = "<input style='width: 90%;' type='checkbox' name='isPrimary" + rowIndex + subRowId + "' id='_isPrimary" + rowIndex + subRowId + "' value='1' checked onclick=\"showColNullAbled(" + rowIndex + "," + subRowId + ",this);\">";
        document.getElementById("_colNullabledDiv" + rowIndex + subRowId).innerHTML = "<input style='width: 90%;' type='checkbox' name='colNullabled" + rowIndex + subRowId + "' id='_colNullabled" + rowIndex + subRowId + "' value='0' onclick=\"isPrimary(" + rowIndex + "," + subRowId + ",this);\">";
    } else {
        document.getElementById("_isPrimaryDiv" + rowIndex + subRowId).innerHTML = "<input style='width: 90%;' type='checkbox' name='isPrimary" + rowIndex + subRowId + "' id='_isPrimary" + rowIndex + subRowId + "' value='0' onclick=\"showColNullAbled(" + rowIndex + "," + subRowId + ",this);\">";
    }
}
var isPrimary = function (rowIndex, subRowId, obj) {
    if (obj.checked) {
        //判断是否为主键，如果是：提醒主键不能为空；否则允许
        var checkValue = 1 * (document.getElementById("_isPrimary" + rowIndex + subRowId).value);
        if (checkValue == 1) {
            dhx.alert("主键不允许为空！");
            document.getElementById("_colNullabledDiv" + rowIndex + subRowId).innerHTML = "<input style='width: 90%;' type='checkbox' name='colNullabled" + rowIndex + subRowId + "' id='_colNullabled" + rowIndex + subRowId + "' value='0' onclick=\"isPrimary(" + rowIndex + "," + subRowId + ",this);\">";
        }
    }
}
/**
 * 删除一行
 * @param img
 */
var removeRow = function (parRowId, rowIndex) {
    dhx.confirm("确定删除？删除后只能重新操作！", function (b) {
        if (b) {
            var trId = "_columnRow" + parRowId + rowIndex;
            $(trId).parentNode.removeChild($(trId));
            dataTypeCombo[parRowId][rowIndex] = null;
            delete dataTypeCombo[parRowId][rowIndex];
            /*必须相应减掉dataRowSizes[rowIndex] = 1*dataRowSizes[rowIndex]-1;*/
//            alert("前>>>"+dataRowSizes[parRowId]);
            dataRowSizes[parRowId] = 1 * dataRowSizes[parRowId] - 1;
//            alert("后>>>"+dataRowSizes[parRowId]);
        }
    })
}

var saveAllMes = function (grid) {
    var data = getData(grid);
    if (data) {
        ImportBatchTableAction.saveAllMes(data, typeValue, function (rs) {
            grid.clearAll();
            if (rs) {
                dhx.alert("导入成功！");
            } else {
                dhx.alert("导入失败，请重试！");
            }
        });
    } else {
        return;
    }
}

/**
 * 查询维度信息Action定义
 */
dwrCaller.addAutoAction("queryDimInfo", "TableApplyAction.queryDimInfo");
dwrCaller.addDataConverter("queryDimInfo", new dhtmxGridDataConverter({
    idColumnName:"tableId",
    filterColumns:["", "tableName", "tableNameCn", "tableBusComment"],
    userData:function (rowIndex, rowData) {
        return rowData;
    }
}))
/**
 * 查询维度归并类型Action定义
 */
dwrCaller.addAutoAction("queryDimType", "TableApplyAction.queryDimType");
dwrCaller.addDataConverter("queryDimType", new dhtmlxComboDataConverter({
    valueColumn:'dimTypeId',
    textColumn:"dimTypeName"
}));
dwrCaller.isShowProcess("queryDimType", false);
/**
 * 查询维度层级Action定义
 */
dwrCaller.addAutoAction("queryDimLevels", "TableApplyAction.queryDimLevels");
dwrCaller.addDataConverter("queryDimLevels", new dhtmlxComboDataConverter({
    valueColumn:'dimLevel',
    textColumn:"dimLevelName",
    textFormat:function (rowData, value, text) {
        return value + "--" + text;
    }
}));
dwrCaller.isShowProcess("queryDimLevels", false);
/**
 * 查询维度表列Action定义
 */
dwrCaller.addAutoAction("queryDimCols", "TableApplyAction.queryDimCols");
dwrCaller.addDataConverter("queryDimCols", new dhtmlxComboDataConverter({
    valueColumn:'colId',
    textColumn:"colName"
}));
dwrCaller.isShowProcess("queryDimCols", false);
var dimWindow = null;
/**
 * 选择维度信息。
 * @param input
 */
var getDimInfo = function (parRowId, rowIndex) {
//    var rowIndex = input.parentNode.parentNode._index;
    //参数收集，参数来源于查询表单_queryDimForm
    var param = new biDwrMethodParam([
        {
            index:0, type:"ui", value:"_queryDimForm"
        }
    ]);
    //查询。
    var queryDimInfo = function () {
        queryDimGrid.clearAll();
        queryDimGrid.load(dwrCaller.queryDimInfo + param, "json");
        $("_refColumn").innerHTML = "";
        $("_dimType").innerHTML = "";
        $("_selectLevel").innerHTML = "";
        dhtmlxValidation.clearAllTip();
    }
    if (!dimWindow) {
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("dimWindow", 0, 0, 450, 400);
        dimWindow = dhxWindow.window("dimWindow");
        dimWindow.stick();
        dimWindow.setDimension(550, 490);
        dimWindow.button("minmax1").hide();
        dimWindow.button("park").hide();
        dimWindow.button("stick").hide();
        dimWindow.button("sticked").hide();
        dimWindow.center();
        dimWindow.denyResize();
        dimWindow.denyPark();
        dimWindow.setText("选择维表");
        dimWindow.keepInViewport(true);
        dimWindow.show();
        dimWindow.attachEvent("onClose", function () {
            dimWindow.hide();
            dimWindow.setModal(false);
            return false;
        })
        //添加查询内容
        var dimLayout = new dhtmlXLayoutObject(dimWindow, "2E");
        dimLayout.cells("b").setText("设置维表关联属性");
        dimLayout.cells("a").setHeight(310);
        dimLayout.cells("a").fixSize(false, true);
        dimLayout.hideConcentrate();
        dimLayout.cells("a").firstChild.style.height = (dimLayout.cells("a").firstChild.offsetHeight + 5) + "px";
        dimLayout.cells("a").hideHeader();
        dimLayout.hideSpliter();//移除分界边框。
        //添加查询按钮
        var queryButton = Tools.getButtonNode("查询");
        //为button绑定事件
        queryButton.onclick = queryDimInfo;
        dimLayout.cells("a").attachObject("_dimInfoTable");
        //表分类下拉树数据准备。
        var dimGroupTree = new loadTableGroupTree($("_queryDimName"), $("_queryDimId"));
        //为_queryDimId添加事件
        Tools.addEvent($("_queryDimName"), "click", function () {
            dimGroupTree.treeDiv.style.width = $("_queryDimName").offsetWidth + 'px';
            Tools.divPromptCtrl(dimGroupTree.treeDiv, $("_queryDimName"), true);
            dimGroupTree.treeDiv.style.display = "block";
            dimGroupTree.showGroupTree(2);
        });

        $("_queryDimButton").appendChild(queryButton);
        //生成查询grid;
        queryDimGrid = new dhtmlXGridObject("_queryDimGrid");
        queryDimGrid.setHeader(",表名,中文名,注释");
        queryDimGrid.setInitWidthsP("6,30,30,34");
        queryDimGrid.setColAlign("center,center,center,center");
        queryDimGrid.setHeaderAlign("center,center,center,center");
        queryDimGrid.setColTypes("ra,ro,ro,ro");
        queryDimGrid.setColSorting("na,na,na,na");
        queryDimGrid.setEditable(true);
        queryDimGrid.enableMultiselect(false);
        queryDimGrid.enableCtrlC();
        queryDimGrid.init();
        queryDimGrid.defaultPaging(10);
        //添加radio点击事件。
        queryDimGrid.attachEvent("onCheck", function (rId, cInd, state) {
            if (state) {
                //先清空关联字段、归并类型、维层级下拉框数据。
                $("_refColumn").innerHTML = "";
                $("_dimType").innerHTML = "";
                $("_selectLevel").innerHTML = "";
                var colId = $("_dimColId" + parRowId + rowIndex).value;
                var dimType = $("_dimTypeId" + parRowId + rowIndex).value;
                //组装关联字段数据。
                dwrCaller.executeAction("queryDimCols", rId, queryDimGrid.getUserData(rId, "tableVersion"), function (data) {
                    Tools.addOption("_refColumn", data);
                    if (colId) {
                        dwr.util.setValue("_refColumn", colId);
                    }
                });
                //加载维度归并类型数据
                dwrCaller.executeAction("queryDimType", rId, function (data) {
                    Tools.addOption("_dimType", data);
                    if (dimType) {
                        dwr.util.setValue("_dimType", dimType);
                    }
                    $("_dimType").onchange.apply(window); //触发onchange事件
                })
            }
        });
        queryDimGrid.attachEvent("onXLE", function () {
            var dimTableId = dwr.util.getValue("_dimTableId" + parRowId + rowIndex);
            if (dimTableId) {
                if (queryDimGrid.doesRowExist(dimTableId)) {
                    queryDimGrid.cells(dimTableId, 0).changeState(true);
                }
            }
        });
        //设置维表关联属性。
        dimLayout.cells("b").attachObject("_dimRefAttrTable");

        //设置确定和关闭按钮。
        var ok = Tools.getButtonNode("确定");
        ok.style.marginLeft = "180px";
        ok.style.marginTop = "10px";
        ok.style.cssFloat = "left";
        ok.style.styleFloat = "left";
        //确定按钮事件
        ok.onclick = function () {
            if (dhtmlxValidation.validate("_dimRefAttrForm")) {
                var rowData = queryDimGrid.getRowData(queryDimGrid.getCheckedRows(0).split(",")[0]);
                var rowIndex = dimWindow._rowIndex;
                var parRowId = dimWindow._parRowId;
                //依次设置选择的值
                $("_colBusType" + parRowId + rowIndex).value = 0;
                $("_dimLevel" + parRowId + rowIndex).value = dwr.util.getValue("_selectLevel");
                $("_dimColId" + parRowId + rowIndex).value = dwr.util.getValue("_refColumn");
                $("_dimTableId" + parRowId + +rowIndex).value = rowData.id;
                $("_dimTypeId" + parRowId + rowIndex).value = dwr.util.getValue("_dimType");
                //设置Input框的值。
                $("_dimInfo" + parRowId + rowIndex).value = rowData.data[1] + "," + dwr.util.getText("_refColumn") + "," +
                    dwr.util.getText("_dimType") + "," + dwr.util.getText("_selectLevel")
                dimWindow.close();
            }
        }
        var close = Tools.getButtonNode("关闭");
        close.style.styleFloat = "left";
        close.style.cssFloat = "left";
        close.style.marginTop = "10px";
        //添加close事件
        close.onclick = function () {
            dimWindow.close();
            dhtmlxValidation.clearAllTip();
        }
        //维度归并类型下拉框选择事件响应。
        $("_dimType").onchange = function () {
            var level = $("_dimLevel" + parRowId + rowIndex).value;
            $("_selectLevel").innerHTML = "";
            var rowData = queryDimGrid.getRowData(queryDimGrid.getCheckedRows(0).split(",")[0]);
            //查询出维层级。
            dwrCaller.executeAction("queryDimLevels", rowData.id,
                dwr.util.getValue("_dimType"), function (data) {
                    Tools.addOption("_selectLevel", data);
                    if (level) {
                        dwr.util.setValue("_selectLevel", level);
                    }
                })
        }
        $("_dimInfoWindowButton").appendChild(ok);
        $("_dimInfoWindowButton").appendChild(close);
        //添加验证
        dhtmlxValidation.addValidation("_dimRefAttrForm", [
            {target:"_refColumn", rule:"NotEmpty"},
            {target:"_dimType", rule:"NotEmpty"},
            {target:"_selectLevel", rule:"NotEmpty" }
        ]);
    }
    queryDimInfo();
    dimWindow.setModal(true);
    dimWindow._rowIndex = rowIndex;
    dimWindow._parRowId = parRowId;
    dimWindow.show();
}
/**
 * 提交数据验证
 */
var validate = function (obj_tr) {
    //提交数据前进行数据验证。
    var validateRes = true;
    validateRes = dhtmlxValidation.validate(obj_tr) && validateRes;
    return validateRes;
}
/*子表验证*/
var subValidate = function (rowIndex, isOpen) {
    var validateRes = true;
    if (typeof dataRowSizes[rowIndex] != "undefined" && dataRowSizes[rowIndex] > 0) {
        if (isOpen) {//展开
            for (var subRowIndex = 1; subRowIndex <= 1 * dataRowSizes[rowIndex]; subRowIndex++) {
                validateRes = dhtmlxValidation.validate($('_columnRow' + rowIndex + subRowIndex)) && validateRes;
                if (!validateRes) {
                    return validateRes;
                    break;
                }
            }
        } else {//收束
            if (typeof subDataArray["colDetail" + rowIndex] != "undefined" && subDataArray["colDetail" + rowIndex].length > 0) {
                for (var subRowIndex = 0; subRowIndex < subDataArray["colDetail" + rowIndex].length; subRowIndex++) {
                    //colName验证
                    validateRes = dhtmlxValidation.checkValue(subDataArray["colDetail" + rowIndex][subRowIndex].colName, "NotEmpty,MaxLength[32],ValidAplhaNumeric,ValidByCallBack[columnNameCheckByColse&" + rowIndex + "]");//&"+subRowIndex+"
                    if (!validateRes) {
                        return validateRes;
                        break;
                    }
                    //colNameCn
                    validateRes = dhtmlxValidation.checkValue(subDataArray["colDetail" + rowIndex][subRowIndex].colNameCn, "MaxLength[32]");
                    if (!validateRes) {
                        return validateRes;
                        break;
                    }
                    //类型
                    validateRes = dhtmlxValidation.checkValue(dataTypeCombo[rowIndex][subRowIndex + 1].DOMelem_input, "NotEmpty,ValidByCallBack[dataTypeValidate]");
                    if (!validateRes) {
                        return validateRes;
                        break;
                    }
                    //colBusComment
                    validateRes = dhtmlxValidation.checkValue(subDataArray["colDetail" + rowIndex][subRowIndex].colBusComment, "MaxLength[1000]");
                    if (!validateRes) {
                        return validateRes;
                        break;
                    }
                    //defaultVal
                    validateRes = dhtmlxValidation.checkValue(subDataArray["colDetail" + rowIndex][subRowIndex].defaultVal, "ValidByCallBack[defaultValValidate&" + rowIndex + "&" + subRowIndex + "]");
                    if (!validateRes) {
                        return validateRes;
                        break;
                    }
                }
            }
        }
    }
    return validateRes;
}
var validateTabName = function (rows, value, currentPageNum) {
    var count = 0;
    for (var i = 0; i < rows.length; i++) {
        var rowIndex = grid.getRowIndex(rows[i]);//父表的行号
        var tableValue = ""
        if (typeof _hiddenPageData[rowIndex] != "undefined" && (1 * _hiddenPageData[rowIndex] != 1 * currentPageNum)) {
            tableValue = _hiddenPageData["tableName" + rowIndex];
        } else
            tableValue = document.getElementById("tablename" + rowIndex).value;
        if (value == tableValue)
            count++;
    }
    var boo = count == 1 ? true : false;
    if (boo) {//去数据库找
        dwr.engine.setAsync(false);
        ImportBatchTableAction.getCountsByTableName(value, function (counts) {
            boo = 1 * counts == 0 ? true : false;
        });
    }
    return boo;
}
/*
 var validateTabName = function(tabName,rowIndex,owner){
 var validate = true;
 ImportBatchTableAction.validateTabName(tabName,owner,{
 async:false,
 callback:function(boo){
 if(!boo){
 dhx.alert("该“"+tabName+"”名称已经存在，请重新命名，谢谢！");
 //document.getElementById("tablename"+rowIndex).focus();
 validate = false;
 }
 }
 });
 return validate;
 }
 */
var validatePageData = function (rowIndex) {
    var res = "";
    var pageNum = "";
    var praTableName = "";
    var validateRes = true;
    if (_hiddenPageData.length > 0) {
        for (var i = 0; i < _hiddenPageData.length; i++) {
            if (typeof _hiddenPageData[rowIndex] != "undefined") {//判断值
                pageNum = _hiddenPageData[rowIndex];
                praTableName = _hiddenPageData["praTableName" + rowIndex];
                validateRes = dhtmlxValidation.checkValue(_hiddenPageData["tableName" + rowIndex], "NotEmpty,MaxLength[1000],ValidByCallBack[tblNameCheck]");
                if (!validateRes) {
                    break;
                }
                validateRes = dhtmlxValidation.checkValue(_hiddenPageData["tableTypeName" + rowIndex], "NotEmpty,MaxLength[64]");
                if (!validateRes) {
                    break;
                }
                validateRes = dhtmlxValidation.checkValue(_hiddenPageData["tableType" + rowIndex], "NotEmpty");
                if (!validateRes) {
                    break;
                }
                validateRes = dhtmlxValidation.checkValue(_hiddenPageData["tableGroup" + rowIndex], "NotEmpty");
                if (!validateRes) {
                    break;
                }
                validateRes = dhtmlxValidation.checkValue(_hiddenPageData["tableSpace" + rowIndex], "NotEmpty");
                if (!validateRes) {
                    break;
                }
                validateRes = dhtmlxValidation.checkValue(_hiddenPageData["partitionSql" + rowIndex], "MaxLength[1000]");
                if (!validateRes) {
                    break;
                }
                validateRes = dhtmlxValidation.checkValue(_hiddenPageData["tableBusComment" + rowIndex], "MaxLength[1000]");
                if (!validateRes) {
                    break;
                }
            }
        }
        if (!validateRes) {
            res = "第" + pageNum + "页，实体表名称为：" + praTableName + "有错，请验证，谢谢！";
        } else
            res = "";
        return res;
    } else
        return res;
}
//得到选中需要导入的数据
var getData = function (grid) {
    var checkRows = grid.getCheckedRows(0);
    if (checkRows) {
        var columnData = [];
        var rowsId = checkRows.split(',');
        //下标起始
        var sflag = (1 * grid.getStateOfView()[0] - 1) * (1 * grid.rowsBufferOutSize);
        //下标结束
        var eflag = (1 * grid.getStateOfView()[0] - 1) * (1 * grid.rowsBufferOutSize) + (1 * grid.rowsBufferOutSize) - 1;
        for (var i = 0; i < rowsId.length; i++) {
            var rowIndex = grid.getRowIndex(rowsId[i]);//父表的行号
            var rowData = {};
            rowData["owner"] = queryForm.getCombo("owner").getSelectedValue();
            if (1 * rowIndex >= 1 * sflag && 1 * rowIndex <= 1 * eflag) {
                var data = grid.getRowData(rowsId[i]).data;
                if (validate(grid.cells(rowIndex, 0).cell.parentNode)) {//父行验证
                    /*判断表名在数据库中是否存在*/
                    rowData["tableName"] = document.getElementById("tablename" + rowIndex).value;
                    //验证表类名称是否重复
                    if (validateTabName(rowsId, rowData["tableName"], grid.getStateOfView()[0])) {
                        if (subValidate(rowIndex, grid.cells((1 * rowIndex), 1).isOpen())) {//验证子行
                            rowData["praTableName"] = data[2];
                            rowData["tableTypeName"] = document.getElementById("tabletypename" + rowIndex).value;
                            rowData["tableType"] = document.getElementById("tabletype" + rowIndex).value;
                            rowData["tableGroup"] = document.getElementById("_tablegroupid" + rowIndex).value;
                            rowData["tableSpace"] = document.getElementById("tablespace" + rowIndex).value;
                            rowData["partitionSql"] = document.getElementById("partitionsql" + rowIndex).value;//grid.cells(rowIndex,8).getValue();
                            rowData["tableBusComment"] = document.getElementById("tablebuscomment" + rowIndex).value;//grid.cells(rowIndex,9).getValue();
                            /*
                             *根据行号获取列信息:判断是否展开修改过：(dataRowSizes数组如果为空证明没有展开过；如果是行数，那么展开过)
                             *（1）如果是，通过传回的行数获取子表的列信息
                             *（2）否则默认，那么后台根据信息直接查询列信息
                             */
                            //保存被选的行号
                            rowData["rowIndex"] = rowIndex;
                            if (typeof dataRowSizes[rowIndex] != "undefined" && dataRowSizes[rowIndex] > 0) {
                                rowData["colDetail"] = [];//父行号对应的列信息
                                if (grid.cells((1 * rowIndex), 1).isOpen()) {//展开
                                    getColumnData(rowIndex);
                                    rowData["colDetail"].push(subDataArray["colDetail" + rowIndex]);
                                } else {//收束
                                    rowData["colDetail"].push(subDataArray["colDetail" + rowIndex]);
                                }
                            }

                            //保存数据源，拥有者，关键字
                            rowData["dataSourceId"] = queryForm.getCombo("dataSource").getSelectedValue();
                            if (rowData["dataSourceId"] == "")
                                rowData["dataSourceId"] = 0;
                            rowData["keyWord"] = queryForm.getItemValue("keyWord");
                            columnData.push(rowData);
                        } else {
                            dhx.alert(data[2] + "所在行有列信息不规范，请展开验证，谢谢！");
                            return false;
                        }
                    } else {
                        dhx.alert("第" + (1 * rowsId + 1) + "行，表类名称有重复");
                        //"第"+(grid.getStateOfView()[0])+页、
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (validatePageData(rowIndex) == "") {
                //加载数据
                rowData["tableName"] = _hiddenPageData["tableName" + rowIndex];
                rowData["praTableName"] = _hiddenPageData["praTableName" + rowIndex];
                //验证表类名称是否重复
                if (validateTabName(rowsId, rowData["tableName"], grid.getStateOfView()[0])) {
                    if (subValidate(rowIndex, grid.cells((1 * rowIndex), 1).isOpen())) {//验证子行
                        rowData["tableTypeName"] = _hiddenPageData["tableTypeName" + rowIndex];
                        rowData["tableType"] = _hiddenPageData["tableType" + rowIndex];
                        rowData["tableGroup"] = _hiddenPageData["tableGroup" + rowIndex];
                        rowData["tableSpace"] = _hiddenPageData["tableSpace" + rowIndex];
                        rowData["partitionSql"] = _hiddenPageData["partitionSql" + rowIndex];
                        rowData["tableBusComment"] = _hiddenPageData["tableBusComment" + rowIndex];
                        rowData["rowIndex"] = rowIndex;
                        if (typeof dataRowSizes[rowIndex] != "undefined" && dataRowSizes[rowIndex] > 0) {
                            rowData["colDetail"] = [];//父行号对应的列信息
                            if (grid.cells((1 * rowIndex), 1).isOpen()) {//展开
                                getColumnData(rowIndex);
                                rowData["colDetail"].push(subDataArray["colDetail" + rowIndex]);
                            } else {//收束
                                rowData["colDetail"].push(subDataArray["colDetail" + rowIndex]);
                            }
                        }
                        rowData["dataSourceId"] = queryForm.getCombo("dataSource").getSelectedValue();
                        if (rowData["dataSourceId"] == "")
                            rowData["dataSourceId"] = 0;
                        rowData["keyWord"] = queryForm.getItemValue("keyWord");
                        columnData.push(rowData);
                    } else {
                        dhx.alert(rowData["praTableName"] + "所在行有列信息不规范，请展开验证，谢谢！");
                        return false;
                    }
                } else {
                    dhx.alert("第" + (1 * rowsId + 1) + "行，表类名称有重复");
                    return false;
                }
            } else {
                dhx.alert(validatePageData(rowIndex));
                return false;
            }
        }
        return columnData;
    } else {
        dhx.alert("请至少选择一条需要导入的实体表！");
        return false;
    }
}
/*创建子表DIV*/
var createDiv = function (rowIndex) {
    var str = document.createElement("div");
    str.id = "_subMesHiddenDiv" + rowIndex;
    document.body.appendChild(str);
    $("_subMesHiddenDiv" + rowIndex).style.display = "none";//隐藏
}
var createSubValidate = function (rowIndex) {
    if (typeof dataRowSizes[rowIndex] != "undefined" && dataRowSizes[rowIndex] > 0) {
        for (var r = 1; r <= dataRowSizes[rowIndex]; r++) {
            dhtmlxValidation.addValidation($("_columnRow" + rowIndex + r + ""), [
                {target:"_colName" + rowIndex + r, rule:"NotEmpty,MaxLength[32],ValidAplhaNumeric,ValidByCallBack[columnNameCheck&" + rowIndex + "&" + r + "]"},
                {target:"_colNameCn" + rowIndex + r, rule:"MaxLength[32]"},
                {target:dataTypeCombo[rowIndex][r].DOMelem_input, rule:"NotEmpty,ValidByCallBack[dataTypeValidate]"},
                {target:"_colBusComment" + rowIndex + r, rule:"MaxLength[1000]"},
                {target:"_defaultVal" + rowIndex + r, rule:"ValidByCallBack[defaultValValidate&" + rowIndex + "&" + r + "]"}
            ]);
        }
    }
}
var subInit = function (rowIndex, dataColSize) {
    dataTypeCombo[rowIndex] = {};
    for (var i = 1; i <= dataColSize; i++) {
        var dataCombo = new dhtmlXCombo($("_colDatatype" + rowIndex + i + ""));
        dataTypeCombo[rowIndex][i] = dataCombo;
    }
}

//从模型文件上传成功后，调用方法
var uploadOver = function (isOk) {
    dhx.closeProgress();
    if (isOk) {
        dhxWindow.window("batchWindow").close();
        typeValue = "upload";
        grid.clearAll();
        grid.load(dwrCaller.getQueryTables, "json");
    } else {
        dhxWindow.window("batchWindow").close();
    }
}