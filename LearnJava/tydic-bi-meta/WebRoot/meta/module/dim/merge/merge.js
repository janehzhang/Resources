/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       merge.js
 *Description：
 *       编码归并JS文件
 *Dependent：
 *         dhtmlx.js、dwr相关JS文件、dhtmlxExtend.js、tool。js等。
 *Author:
 *        张伟
 *Finished：
 *       2011-11-08
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
var dwrCaller = new biDwrCaller();
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dataSourceId = window.parent.dataSourceId;
var isAllAduit = false;
var batchWindow = null;
var dimTableName = null;
var dimTablePrefix = null;
var clickCount = 0;
var dimItemNameArr = [];
var parCode = "0";
var dhxWindow = null;

/*
 全局变量定义区。
 */
//当前选择的归并类型
var currentDimType = null;
//归并状态
var dimTypeStatus = 0;
//当前选择的归并类型名称
var currentDimTypeName = null;
//归并类型定义，以归并类型ID作为键值，value作为归并类型信息，如:{1:{dimTypeId:1,dimTypeName:"行政区域"...}}
var dimTypeInfo = {};
//归并扩展表信息，包含键值与表META_DIM_TABLES定义相同
var dimInfo = null;
//完整维表树形数据，该数据中包含数据库中定义好得完整的树形结构，以dimTypeId作为键值，value为其树结构数据，此数据为数据中查询的原生态，没经过数据转换器
//进行转换，如果用户在界面上动态添加、移动、删除、禁用等操作，此数据跟着改变，保持最新。
var completeTreeData = {}
//更新的节点数据，以维表ID作为键值，维表列数据作为Value。这里的更新数据包括修改节点、禁用节点、启用节点的数据。其中OPEARATE_TYPE表示修改类型。
var updateData = {}
//新增的编码数据，以父节点ID作为键值，列数据作为Value便于查找。
var insertData = {};
//移动节点的数据，如果是新增的数据，直接被删除，不保存层级变更的数据，如果是数据库中已存在的数据，记录其变更的记录
//以归并类型为第一主键，以其ID作为第二主键，缓存数据包括{srcParId:newParId}.
var changeLevelData = {};
//各种归并类型完成的树对象。
var treeObject = null;
//以维表CODE为键值，value为其归并类型，数组格式。例{028：[1,2..]},此为一个代码归并的一个完整类型映射。
var codeMapping = {};
//以维表CODE作为键值，VAlUE为其归并data数据，此变量缓存用户在界面操作的编码映射，非数据库中所有，这里要有所区分。
var codeModifyMapping = {};
//移动数据，记录格式：以rowId为主键，value排序具体值。
var orderData = {}
//定义停用、新增、节点移动的样式
var modifyStyle = {
    inserted:"font-weight:bold;color:#64B201;",
    merge:"font-weight:bold;color:#63B8FF",
    move:"font-weight:bold;color:#EEC900",
    modify:"font-weight:bold;color:#7A378B",
    stopUse:"font-weight:bold;color:red",
    clear:"font-weight:normal;font-style::normal;text-decoration:none;color:black;border-bottom:0px;",
    cancelStopUse:"color: #F29FB5;"
}
var dymaicColData = [];//动态字段信息
var dymaicColNames = [];//动态字段名称
var rootData = {};
//当前点击展开的rowID
var currentOpenRowId = 0;

//查询对应编码所有归并类型Action定义
dwrCaller.addAutoAction("queryDimTypesByCode", DimMergeAction.queryDimTypesByCode, tableId, tableName, parent.tableUser, {
    dwrConfig:true,
    isShowProcess:false,
    converter:new columnNameConverter()
});
dwrCaller.addAutoAction("queryPathNameByCode", DimMergeAction.queryPathNameByCode, {
    dwrConfig:true,
    isShowProcess:false,
    async:false
});
//查询对应归并类型下是否有未审核的数据的action
dwrCaller.addAutoAction("isAllAudit", DimMergeAction.isAllAudit, {
    async:false,
    dwrConfig:true,
    isShowProcess:false
})
/**
 * 判断是否存在某个表Action定义
 */
dwrCaller.addAutoAction("isExitsTable", DimMergeAction.isExitsTable, {
    dwrConfig:true,
    async:false
});

var treeGridConverterConfig = {
    isDycload:false,
    isFormatColumn:false,
    /**
     * 列转义，设置第五列的值固定为getRoleButtons，此为操作列，这个值为一个JS函数，用于获取下拉button的值。
     * @param rowIndex   当前行的id
     * @param columnIndex  特别说明，如果实现了filterColumns（）方法，这里的columnIndex是filterColumns后的index
     * @param columnName   当前列的名字
     * @param cellValue   单元格的值
     * @param rowData   当前行的数据
     * @return
     */
    cellDataFormat:function (rowIndex, columnIndex, columnName, cellValue, rowData) {
        if (columnName == "_buttons") {
            return "editButton";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    },
    userData:function (rowIndex, rowData) {
        return rowData;
    }
}
var treeGridConverter = new dhtmlxTreeGridDataConverter(treeGridConverterConfig);
//数转换器
var treeConverterConfig = {
    isDycload:false,
    isFormatColumn:false,
    userData:function (rowIndex, rowData) {
        return {rowData:rowData};
    }
}
var treeConverter = new dhtmxTreeDataConverter(treeConverterConfig);
/*
 * 加载维度数据Action定义
 */
dwrCaller.addAutoAction("queryDimData", DimMergeAction.queryDimData, {
    dwrConfig:true,
    converter:treeGridConverter,
    isShowProcess:true,
    isDealOneParam:false
});
/**
 * 加载子数据
 */
dwrCaller.addAutoAction("querySub", DimMergeAction.querySub, {
    dwrConfig:true,
    isDealOneParam:false,
    onBeforeLoad:function (params) {
        params[3] = params[3].id;
        //params[4] = dimInfo.tableOwner;
        params[4] = parent.tableUser;
    }
})

//加载完整树形结构数据Action定义
dwrCaller.addAutoAction("queryCompleteTreeData", DimMergeAction.queryCompleteTreeData, tableName, dataSourceId, {
    dwrConfig:true,
    isDealOneParam:false,
    callback:function (data) {
        //对数据进行归类。缓存。
        if (data) {
            for (var i = 0; i < data.length; i++) {
                var rowData = data[i];
                if (completeTreeData[rowData.DIM_TYPE_ID]) {
                    completeTreeData[rowData.DIM_TYPE_ID].push(rowData);
                } else {
                    completeTreeData[rowData.DIM_TYPE_ID] = [rowData];
                }
                var codeStr = (dimInfo.tableDimPrefix + "_CODE").toUpperCase();
                //缓存代码和DimType的映射。
                if (codeMapping[rowData[codeStr]]) {
                    var isFind = false;
                    for (var j = 0; j < codeMapping[rowData[codeStr]].length; j++) {
                        if (codeMapping[rowData[codeStr]][j] == rowData.DIM_TYPE_ID) {
                            isFind = true;
                            break;
                        }
                    }
                    !isFind && codeMapping[rowData[codeStr]].push(rowData.DIM_TYPE_ID);
                } else {
                    codeMapping[rowData[codeStr]] = [rowData.DIM_TYPE_ID];
                }
            }
        }
    }
})


/**
 * 维度表固定字段列名定义
 */
var idCloumnName = undefined;//ID列列名
var nameColumnName = undefined;//名称列列名
var codeColumnName = undefined;//编码列列名
var descColumnName = undefined;//描述列列名
var parIdColumnName = undefined;//父级列列名
var parCodeColumnName = undefined;//父编码列
/*
 对一个归并类型的归并操作界面规则定义如下：
 --如何算作新增：
 1、数据库中存在此编码但是此编码的归并数量应该大于1
 2、数据库中不存在此编码。
 --如何算作是节点移动：
 1、数据中存在此归并类型的编码，改变了其父节点。
 2、数据库中不存在此归并类型的编码，且归并数量为0，将其归并到此归并类型
 --如何算作是修改
 1、数据中存在此编码，不管是否有归并，修改了此节点编码数据，但不包括其状态。
 --如何算作是停用
 数据库中存在此编码，不管是否有归并。
 */

var mergeInit = function () {
    var isContinue = true;
    dwrCaller.executeAction("isExitsTable", tableName, parent.tableUser, function (data) {
        isContinue = data;
    });
    if (!isContinue) {
        dhx.alert("元数据管理库中不存在维度表类对应实例表:[" + tableName + "],不能做归并！");
        return;
    }
    dhxWindow = new dhtmlXWindows()
    dimInfo = window.parent.dimInfo;
    dimTypeInfo = window.parent.dimTypeInfo;
    //维度表固定字段列名初始化
    idCloumnName = (dimInfo.tableDimPrefix + "_ID").toUpperCase();
    nameColumnName = (dimInfo.tableDimPrefix + "_NAME").toUpperCase();
    codeColumnName = (dimInfo.tableDimPrefix + "_CODE").toUpperCase();
    descColumnName = (dimInfo.tableDimPrefix + "_DESC").toUpperCase();
    parIdColumnName = (dimInfo.tableDimPrefix + "_PAR_ID").toUpperCase();
    parCodeColumnName = (dimInfo.tableDimPrefix + "_PAR_CODE").toUpperCase();
    //根据归并类型初始化updateData，insertData数据。
    for (var key in dimTypeInfo) {
        updateData[key] = {};
        insertData[key] = {};
        changeLevelData[key] = {};
    }
    dymaicColData = window.parent.dymaicColData;
    var mergeLayout = new dhtmlXLayoutObject(document.body, "2E");
    mergeLayout.cells("a").setText("<span style='font-weight: normal;'>维护归并编码：</span>" + tableNameCn);
    mergeLayout.cells("b").hideHeader();
    mergeLayout.cells("a").setHeight(70);
    mergeLayout.cells("a").fixSize(false, true);
    mergeLayout.hideConcentrate();
    mergeLayout.hideSpliter();//移除分界边框。

    //加载查询表单
    queryform = mergeLayout.cells("a").attachForm([
        {type:"settings", position:"label-left", labelWidth:70, inputWidth:120},
        {type:"select", label:"归并类型：", name:"dimType"},
        {type:"newcolumn"},
        {type:"select", label:"归并状态：", name:"dimTypeStatus", options:[
//            {text:"所有",value:"0" },
            {text:"已归并", value:"2" },
            {text:"尚未归并", value:"1" }
        ]},
        {type:"newcolumn"},
        {type:"input", label:"关键字：", name:"keyWord"},
        {type:"newcolumn"},
        {type:"button", name:"query", value:"查询"}
    ]);
    //向归并类型下拉框内添加数据
    var reloadDimType = function (dimType) {
        dimTypeInfo = dimType;
        var dimTypeSelect = queryform.getSelect("dimType");
        var dimCount = 0;
        for (var key in dimTypeInfo) {
            if (key != 'length') {
                dimTypeSelect.options[dimCount++] = new Option(dimTypeInfo[key].dimTypeName, dimTypeInfo[key].dimTypeId);
            }
        }
    }
    window.parent.onDimTypeInfoReload.push(reloadDimType);
    reloadDimType(dimTypeInfo);

    mergeLayout.cells("b").attachObject($("_layout"));
    $("_middleArea").style.height = ($("_layout").offsetHeight - 125) + "px";
    $("_middleArea").parentNode.style.height = ($("_layout").offsetHeight - 125) + "px";
    $("_dimDataGrid").style.height = $("_middleArea").offsetHeight + "px";
    $("_dimTypeGrid").style.height = ($("_dimDataGrid").offsetHeight - 80) + "px";
    //提交按钮配置
    var sumbit = Tools.getButtonNode("提交审核");
    $("_button").appendChild(sumbit);
    sumbit.style.marginLeft = (Math.round(($("_button").offsetWidth - 100) / 2)) + "px";
    sumbit.style.marginTop = ($("_button").offsetHeight - 20) / 2 + "px";
    sumbit.style.cssFloat = "left";
    sumbit.style.styleFloat = "left";
    sumbit.onclick = submitAudit;
    currentDimType = dwr.util.getValue(queryform.getSelect("dimType"));
    //声明按钮
    var buttons = {
        upOrder:{name:"upOrder", text:"升级", imgEnabled:getBasePath() + "/meta/resource/images/undo.png",
            imgDisabled:getBasePath() + "/meta/resource/images/undo.png", onclick:function (rowData) {
                if (!rowData) {
                    dhx.alert("请您至少选择一行！");
                    return;
                }
                moveUp(rowData.id);

            }},
        downOrder:{name:"downOrder", text:"降级", imgEnabled:getBasePath() + "/meta/resource/images/redo.png",
            imgDisabled:getBasePath() + "/meta/resource/images/redo.png", onclick:function (rowData) {
                if (!rowData) {
                    dhx.alert("请您至少选择一行！");
                    return;
                }
                moveDown(rowData.id);

            }},
        moveUp:{name:"moveUp", text:"上移", imgEnabled:getBasePath() + "/meta/resource/images/move_up.png",
            imgDisabled:getBasePath() + "/meta/resource/images/move_up.png", onclick:function (rowData) {
                if (!rowData) {
                    dhx.alert("请您至少选择一行！");
                    return;
                }
                orderUp(rowData.id);
            }},
        moveDown:{name:"moveDown", text:"下移", imgEnabled:getBasePath() + "/meta/resource/images/move_down.png",
            imgDisabled:getBasePath() + "/meta/resource/images/move_down.png", onclick:function (rowData) {
                if (!rowData) {
                    dhx.alert("请您至少选择一行！");
                    return;
                }
                orderDown(rowData.id);
            }},
        addCode:{name:"addCode", text:"新增编码", imgEnabled:getBasePath() + "/meta/resource/images/addSubmenu.png",
            imgDisabled:getBasePath() + "/meta/resource/images/addSubmenu.png", onclick:function (rowData) {
                window.addCode(rowData ? rowData.userdata : null);
            }},
        batchImport:{name:"batchImport", text:"批量导入", imgEnabled:getBasePath() + "/meta/resource/images/addSubmenu.png",
            imgDisabled:getBasePath() + "/meta/resource/images/addSubmenu.png", onclick:function (rowData) {
                batchImportWin();
            }},
        dBImport:{name:"dBImport", text:"数据导入", imgEnabled:getBasePath() + "/meta/resource/images/addSubmenu.png",
            imgDisabled:getBasePath() + "/meta/resource/images/addSubmenu.png", onclick:function (rowData) {
                dBImport();
            }}
    };
    var editButtons = [
        {name:"modifyCode", text:"修改", imgEnabled:getBasePath() + "/meta/resource/images/addUser.png",
            imgDisabled:getBasePath() + "/meta/resource/images/addUser.png", onclick:function (rowData) {
            modifyCode(rowData.userdata);
        }},
        {name:"stopUse", text:"禁用", imgEnabled:getBasePath() + "/meta/resource/images/auditUser.png",
            imgDisabled:getBasePath() + "/meta/resource/images/auditUser.png", onclick:function (rowData) {
            stopUse(rowData.userdata);
        }},
        {name:"cancelStopUse", text:"启用", imgEnabled:getBasePath() + "/meta/resource/images/auditUser.png",
            imgDisabled:getBasePath() + "/meta/resource/images/auditUser.png", onclick:function (rowData) {
            cancelStopUse(rowData.userdata);
        }}
    ];
    window['editButton'] = function (rowId, cellId, rowData) {
        if (isAllAudit) {
            var state = null;
            if (rowData && rowData.userdata) {
                state = rowData.userdata.STATE;
            } else {
                state = this.cells(rowId, 3).cell.innerText == "有效" ? 1 : 0;
            }
            if (state == 0) {//已经禁用
                return [editButtons[0], editButtons[2]];
            } else {
                return [editButtons[0], editButtons[1]];
            }
        } else {
            return [];
        }
    }
    // 初始化工具条
    $("_toolbar").style.width = $("_toolbar").parentNode.offsetWidth - 2 + "px";
    var toolBar = new dhtmlXToolbarObject("_toolbar");
    var pos = 1;
    for (var key in buttons) {
        toolBar.addButton(key, pos++, buttons[key].text, buttons[key].imgEnabled, buttons[key].imgDisabled);
    }
    toolBar.attachEvent("onClick", function (id) {
        var rowId = treeDataGrid.getSelectedRowId();
        buttons[id].onclick(rowId ? treeDataGrid.getRowData(rowId) : null);
    })
    //编码工具条
    var codeToolbar = new dhtmlXToolbarObject("_codeToolbar");
    codeToolbar.addText("text", 1, "所有归并类型编码");
    $("_codeToolbar").style.width = $("_toolbar").parentNode.offsetWidth - 2 + "px";
    //模板HTML设置样式
    dhx.html.addCss($("_columnDimTableDiv_{template}"), global.css.gridTopDiv);
    //初始化归并类型编码Grid
    var template = $("_template").innerHTML.replace(/\{template\}/g, "right");
    $("_dimTypeGrid").innerHTML = template;
    $("_clumnDimContentDiv_right").style.height = ($("_dimTypeGrid").offsetHeight - 30) + "px";
    $("_columnDimTableDiv_right").style.marginTop = "-6px";
    //参数控件初始化
    var mergeParam = new biDwrMethodParam();
    mergeParam.setParamConfig([
        {
            index:0, type:"fun", value:function () { //索引0位的参数来源由表单提供。
            var queryData = queryform.getFormData();
            queryData.lastLevelFlag = dimInfo.lastLevelFlag;
            queryData.dataSourceId = dataSourceId;
            return queryData;
        }},
        {
            index:1, type:"static", value:tableName
        }//第二个参数为表名
    ]);
    //判断当前的归并类型下是否由数据未审核,如果没有审核的话,那么页面是不能被操作的
    //currTypeId 当前下拉框的归并类型id
    var isAllAuditFun = function (currTypeId) {
        dwrCaller.executeAction("isAllAudit", tableId, currTypeId, function (rs) {
            isAllAudit = rs;
            if (!rs) {
                dhx.alert("此维度表的当前归并下有数据未审核,数据展现会不一致,审核之前不能提交数据");
            }
        })
    }
    var batchImportWin = function () {
        batchWindow = dhxWindow.createWindow("batchWindow", 0, 0, 300, 150);
        batchWindow.stick();
        batchWindow.button("minmax1").hide();
        batchWindow.button("park").hide();
        batchWindow.button("stick").hide();
        batchWindow.button("sticked").hide();
        batchWindow.center();
        batchWindow.denyResize();
        batchWindow.denyPark();
        batchWindow.setText("批量导入编码");
        batchWindow.keepInViewport(true);
        batchWindow.show();
        batchWindow.attachURL(urlEncode(getBasePath() + "/meta/module/dim/merge/codeUpload.jsp?dimTypeId=" + dwr.util.getValue(queryform.getSelect("dimType")) + "&dimTableId=" + tableId + "&dimTableName=" + tableName + "&dimTablePrefix=" + dimInfo.tableDimPrefix + "&tableOwner=" + parent.tableUser));
        batchWindow.attachEvent("onClose", function () {
            window.location.reload();
        })
    }
    var query = function () {
        if (dimInfo && queryform.getSelect("dimType").options.length > 0 && treeDataGrid) {
            //第三个和第四个参数运行时确定
            treeDataGrid.clearAll();
            currentDimType = dwr.util.getValue(queryform.getSelect("dimType"));
            currentDimTypeName = dwr.util.getText(queryform.getSelect("dimType"));
            treeDataGrid.load(dwrCaller.queryDimData + mergeParam, "json");
            dimTypeStatus = dwr.util.getValue(queryform.getSelect("dimTypeStatus"));
            //如果归并状态为未归并，置按钮上移下移等失效
            if (dimTypeStatus == 1) {
                toolBar.disableItem("upOrder");
                toolBar.disableItem("downOrder");
                toolBar.disableItem("moveUp");
                toolBar.disableItem("moveDown");
                toolBar.disableItem("batchImport");
                toolBar.disableItem("dBImport");
            } else {
                toolBar.enableItem("upOrder");
                toolBar.enableItem("downOrder");
                toolBar.enableItem("moveUp");
                toolBar.enableItem("moveDown");
                toolBar.enableItem("addCode");
                toolBar.enableItem("batchImport");
                toolBar.enableItem("dBImport");
            }
            if (!isAllAudit) {
                toolBar.disableItem("upOrder");
                toolBar.disableItem("downOrder");
                toolBar.disableItem("moveUp");
                toolBar.disableItem("moveDown");
                toolBar.disableItem("addCode");
                toolBar.disableItem("batchImport");
                toolBar.disableItem("dBImport");
            }
        } else {
            setTimeout(arguments.callee, 50);
        }
    }
    queryform.attachEvent("onButtonClick", function (id) {
        //添加业务逻辑:如果当前归并类型下有数据没有审核,那么页面上的数据只能看,不能操作
        if (id == "query") {
            currentDimType = dwr.util.getValue(queryform.getSelect("dimType"));
            isAllAuditFun(currentDimType);
            query();
            currentOpenRowId = 0;
            var tableObj = $("_clumnDimContentTable_right");
            var i = 1;
            while (i < tableObj.rows.length) {
                tableObj.deleteRow(i);
            }
        }
    })
    Tools.addEvent(queryform.getSelect("dimType"), "change", function () {
        currentDimType = dwr.util.getValue(queryform.getSelect("dimType"));
        isAllAuditFun(currentDimType);
        query();
        currentOpenRowId = 0;
        var tableObj = $("_clumnDimContentTable_right");
        var i = 1;
        while (i < tableObj.rows.length) {
            tableObj.deleteRow(i);
        }
    });
    currentDimType = dwr.util.getValue(queryform.getSelect("dimType"));
    isAllAuditFun(currentDimType);
    var initTreeGrid = function (columnData) {
        if (dimInfo) {
            var imagePath = dhtmlx.image_path + "csh_" + dhtmlx.skin + "/";
            treeDataGrid = new dhtmlXGridObject("_dimDataGrid");
            treeDataGrid.setImagePath(imagePath);
            treeDataGrid.setIconPath(imagePath);
            var header = "编码,名称,归并数量,状态,操作,";
            var alginStr = "left,left,center,center,center,";
            var colTypeStr = "tree,ro,ro,ro,sb,";
            var colSortStr = "na,na,na,na,na,";
            //total是指以多少开始计算，当动态字段数目>3个时，出横向滚动条。
            var total = 100 + (dymaicColData.length > 3 ? (dymaicColData.length - 3) * 20 : 0);
            var initP = [3, 2.5, 1.5, 1, 2];//比例
            var totalY = 10;
            var colParam = [];
            var filterColumns = [codeColumnName,
                nameColumnName
                , "DIMTYPE_CNT", "STATE", "_buttons"];
            //初始化百分比
            for (var i = 0; i < dymaicColData.length; i++) {
                initP.push(2);
                totalY += 2;
                //动态显示列：如果中文名为空时，显示其字段名称。
                if (dymaicColData[i].colNameCn) {
                    header += dymaicColData[i].colNameCn + ",";
                }
                else {
                    header += dymaicColData[i].colName + ",";
                }
                alginStr += "left,";
                colTypeStr += "ro,";
                colSortStr += "na,";
                colParam.push(dymaicColData[i].colName);
                filterColumns.push(dymaicColData[i].colName);
            }
            //转换器初始化
            treeGridConverter.setIdColumn(idCloumnName);
            treeGridConverter.setPidColumn(parIdColumnName);
            treeGridConverter.setFilterColumns(filterColumns);
            treeGridConverter.isOpen = function (rowData) {
                return false;
            }
            treeGridConverter.haveChild = function (rowIndex, rowData) {
                return rowData["CHILDREN"] && parseInt(rowData["CHILDREN"]) >= 1;
            }
            //初始化树根节点
            rootData[(dimInfo.tableDimPrefix).toUpperCase() + "_ID"] = -1;
            rootData[(dimInfo.tableDimPrefix).toUpperCase() + "_PAR_ID"] = 0;
            rootData[(dimInfo.tableDimPrefix).toUpperCase() + "_NAME"] = '根节点';
            //展开第一级
            treeGridConverter.addTreeRowConfig = function (rowIndex, rowData) {
                var rs;
                if (rowData.OPER_FLAG) {//说明此数据有做改动
                    rs = {style:modifyStyle[(rowData.OPER_FLAG).toString().split(",")[0]]}
                } else {
                    rs = Tools.EmptyObject;
                }
                return rs;
            }
            treeGridConverter.onBeforeConverted = function (data) {
                //如果有新增的数据，直接加入
                data = data || [];
                //加入页面临时操作的数据
                var appendTempData = function (parId) {
                    var isAppend = false;
                    if (insertData[currentDimType] && insertData[currentDimType][parId]) {
//                        var tempParId=parId;
                        for (var key in insertData[currentDimType][parId]) {
                            data.push(insertData[currentDimType][parId][key]);
                            arguments.callee.call(this, key); //递归遍历数据
                            isAppend = true;
//                            tempParId=insertData[currentDimType][tempParId][idCloumnName];
                        }
                    }
                    return isAppend;
                };
                var ids = {}; //此次加载的无子节点有ID集合。
                if (data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        //如果页面上已经对其进行了层次变更，即父节点已经不是现在
                        //有的父节点，删除此节点
                        var id = data[i][idCloumnName];
                        var parId = data[i][parIdColumnName];
                        if (changeLevelData[currentDimType][id]
                            && changeLevelData[currentDimType][id].newParId != parId) {
                            data.splice(i--, 1);
                            continue;
                        }
                        if (!data[i].CHILDREN) {//无子节点时，判断有页面操作的临时数据
                            appendTempData(id) && (data[i].CHILDREN = 1);
                            ids[id] = id;
                        }
                    }
                    appendTempData(data[0][parIdColumnName]);
                }
                if (this.isDycload && (!data || data.length == 0)) {//如果是动态加载，且数据库查询无子节点。
                    // 只加载当前展开父节点的子节点
                    appendTempData(currentOpenRowId);
                } else if (!this.isDycload) {
                    appendTempData(0);//加入新增的根节点
                    appendTempData(-1);
                }
                ids[currentOpenRowId] = currentOpenRowId;
                //加入层级变更数据。
                currentOpenRowId = 0;//重置点击的currentOpenRowId。
            }
            /**
             * 重写getRowData方法，覆盖数据。
             * @param data
             */
            treeGridConverter._getRowData = function (data, rowIndex) {
                var dimType = data[rowIndex].DIM_TYPE_ID;
                return (updateData[dimType] &&
                    updateData[dimType][data[rowIndex][idCloumnName]]) ||
                    data[rowIndex];
            }
            var tempConverter = dhx.extend({}, treeGridConverter, true);
            tempConverter.isDycload = true;
            dwrCaller.addDataConverter("querySub", tempConverter);
            treeConverter.setIdColumn(treeGridConverter.idColumnName);
            treeConverter.setPidColumn(treeGridConverter.pidColumn);
            treeConverter.setTextColumn(filterColumns[1]);
            treeConverter.isOpen = function () {
                return false;//默认所有均不展开
            }
            treeConverter.addItemConfig = function (rowIndex, rowData) {
                if (rowData.OPER_FLAG) {//说明此数据有做改动
                    return {style:modifyStyle[(rowData.OPER_FLAG).toString().split(",")[0]]}
                } else {
                    return Tools.EmptyObject;
                }
            }
            treeConverter.compare = treeGridConverter.compare;
//            treeConverter.afterCoverted =  function(data){
//                if(data){
//                    for(var i = 0; i < data.length; i++){
//                        data[i].open = data[i].item ? true : false;
//                    }
//                }
//                return data;
//            };
            treeConverter._getRowData = treeGridConverter._getRowData;
            //查找完整树
            dwrCaller.executeAction("queryCompleteTreeData", dimInfo.tableDimPrefix, parent.tableUser, dimInfo.lastLevelFlag, colParam);
            dymaicColNames = colParam;
            //设置加载数据的第三个参数，动态列名
            mergeParam.setParam(2, colParam, false);
            //设置加载数据的第四个参数，表字段前缀
            mergeParam.setParam(3, dimInfo.tableDimPrefix, false);
            mergeParam.setParam(4, parent.tableUser, false);
            header = header.substr(0, header.length - 1);
            alginStr = alginStr.substr(0, alginStr.length - 1);
            colTypeStr = colTypeStr.substr(0, colTypeStr.length - 1);
            colSortStr = colSortStr.substr(0, colSortStr.length - 1);
//            initP.push(2);
//            totalY += 2;
            //计算每个百分比
            var strInitP = "";
            for (var i = 0; i < initP.length; i++) {
                strInitP += (initP[i] * total / totalY) + (i == initP.length - 1 ? "" : ",");
            }
            treeDataGrid.setHeader(header);
            treeDataGrid.setInitWidthsP(strInitP);
            treeDataGrid.setColAlign(alginStr);
            treeDataGrid.setHeaderAlign(alginStr);
            treeDataGrid.setColTypes(colTypeStr);
            treeDataGrid.setColSorting(colSortStr);
            treeDataGrid.enableTreeGridLines();
            treeDataGrid.setEditable(false);
            treeDataGrid.setColumnCustFormat(3, validOrNot);
            treeDataGrid.enableDragAndDrop(true);//设置拖拽
            treeDataGrid.setDragBehavior("complex");
            treeDataGrid.init();
            treeDataGrid.enableMercyDrag(false);
            treeDataGrid.enableDragOrder(true);
            var currRowdata = {}; //移动的时候，绑定在td节点的data会移除，所以，需要重新绑定数据，这些数据对后面的操作
            //非常有用，这个变量保存当前移动节点及其子节点的数据，便于重置。以rowId作为键值。
            treeDataGrid.attachEvent("onDrop", function (sId, tId, dId, sObj, tObj, sCol, tCol) {
                var sparId = treeDataGrid.getParentId(sId);
                var tparId = treeDataGrid.getParentId(tId);
                var oldParentId = currRowdata[sId].getParentId();	//移动节点原父节点ID。
                if (sparId != tparId) { //父子节点拖动
                    treeDataGrid.setItemImage(tId, "folderOpen.gif");
                    //记录数据
                    dealMoveData(sId, tId, currRowdata[sId], treeDataGrid.getRowData(tId),
                        parseInt(treeDataGrid.getRowData(tId).userdata["DIM_LEVEL"]) + 1);
                } else {//同级移动
//                    var x = treeDataGrid._h2.get[sId];
//                    if(currRowdata[sId]._orgIndex == x.index){ //节点移回原来位置
//                        delete orderData[sId];
//                    } else{//orderId比toId的orderID加1.
//                        var order = treeDataGrid.getRowData(tId).userdata.ORDER_ID;
//                        order = (order == null || order == undefined) ? 0 : order;
//                        orderData[sId] = ++order;
//                    }
                }

                /**
                 * 原父节点ID和移动变更后的父ID不相同，表示两个父节点下节点之间的移动。例如：绵阳下的将有移动到成都下。
                 * 修改问题单：00000512
                 */
//                if (tparId != oldParentId) {
//                	
//                	alert("111tparId==="+tparId+"===oldParentId==="+oldParentId);
//                    dealMoveData(sId, tparId, currRowdata[sId], treeDataGrid.getRowData(tId),
//                        parseInt(treeDataGrid.getRowData(tId).userdata["DIM_LEVEL"]));
//                }
                try {
                    treeDataGrid.setItemImage(sparId, "folderOpen.gif");
                } catch (exception) {/*alert("exception.name 1 = "+exception.name+" exception.message ="+exception.message)*/
                }
                restoreGridData(sId, currRowdata);
                orderByOfTree(tparId);
            });
            treeDataGrid.attachEvent("onOpenStart", function (id, state) {
                (state == -1) && (currentOpenRowId = id);//当是收缩状态且未加载时，记录当前点击的父ID
                return true;
            });
            treeDataGrid.attachEvent("onDrag", function (sId, tId, sObj, tObj, sInd, tInd) {
                if (!isMerge(tId)) {
                    dhx.alert("目标节点没有归并到：" + currentDimTypeName + "，不能拖动！");
                    return false;
                }
                if (tObj.dragContext.dropmode == "child") {//父子节点拖动。
                    if (parseInt(dimTypeInfo[tObj.getUserData(tId, "DIM_TYPE_ID")].maxLevel) <
                        parseInt(tObj.getUserData(tId, "DIM_LEVEL")) + 1) {
                        dhx.alert("超越了" + dimTypeInfo[tObj.getUserData(tId, "DIM_TYPE_ID")].dimTypeName + "规定的最大层级：" +
                            dimTypeInfo[treeDataGrid.getUserData(tId, "DIM_TYPE_ID")].maxLevel + "，不能作为其子节点！");
                        return false;
                    }
                }
                var x = treeDataGrid._h2.get[sId];
                //缓存TD节点以及子节点数据
                currRowdata = backUpGridData(sId);
                //记录节点原来位置
                if (currRowdata[sId]._orgIndex == null || currRowdata[sId]._orgIndex == undefined) {
                    currRowdata[sId]._orgIndex = x.index;
                }
                //如果源ID的父节点只有一个子节点了，将其图标设置为叶子节点的图标。
                var sparId = treeDataGrid.getParentId(sId);
                if (treeDataGrid.getSubItems(sparId).split(",").length == 1) {
                    treeDataGrid.setItemImage(sparId, "leaf.gif");
                }
                return true;
            });
            //查询数据
            query();
            treeDataGrid.entBox.style.marginTop = "-4px";
            //查询子菜单Param参数控件
            var param = new biDwrMethodParam();
            param.setParam(0, tableName);
            param.setParam(1, dimInfo.tableDimPrefix);
            param.setParam(2, dymaicColNames);
            treeDataGrid.kidsXmlFile = dwrCaller.querySub + param;
            treeDataGrid.attachEvent("onBeforeSelect", function (id) {
                var selectRow = treeDataGrid.getSelectedRowId();
                if (selectRow && selectRow == id) { //如果已经选中，并且选择的ID==点击的ID，那么再次点击取消选择。
                    treeDataGrid.clearSelection();
                    return false;
                }
                return true;
            });
            //选中行事件，加载右边所有的归并类型并显示
            treeDataGrid.attachEvent("onRowSelect", function (id) {
                var scollLeft = treeDataGrid.objBox.scrollLeft;
                clearDimType()
                selectDimType['right'] = {};
                var selectRowData = treeDataGrid.getRowData(id);
                //编码名称，要求构造其递归路径
                try {
                    var itemCodeName = selectRowData.data[1];
                    var itemCode = selectRowData.data[0];
                    var itemParCode = selectRowData.userdata[dimInfo.tableDimPrefix + "_PAR_CODE"];
                } catch (exception) {/*alert("exception.name 2= "+exception.name+" exception.message ="+exception.message)*/
                }
                var parId = treeDataGrid.getParentId(id);
                while (parId) {
                    itemCodeName = treeDataGrid.getRowData(parId).data[1] + "->" + itemCodeName;
                    parId = treeDataGrid.getParentId(parId);
                }
                if (codeMapping[itemCode]) {
                    var data = null;
                    var itemPathName = null;
                    if (codeMapping[itemCode].length > 0) {
                        data = [];
                        for (var i = 0; i < codeMapping[itemCode].length; i++) {
                            if (dimTypeInfo[codeMapping[itemCode][i]].dimTypeId == currentDimType
                                && dimTypeStatus != 1) {
                                data[i] = dimTypeInfo[codeMapping[itemCode][i]];
                                data[i].itemCode = itemCode;
                                data[i].itemCodeName = itemCodeName;
                                data[i].itemParCode = itemParCode;
                                addDimColumnRow("right", data[i]);
                            } else {
                                dwrCaller.executeAction("queryPathNameByCode", tableName, dimInfo.tableDimPrefix, parent.tableUser, selectRowData.data[0], dimTypeInfo[codeMapping[itemCode][i]].dimTypeId, function (data) {
                                    if (data != null && data != "") {
                                        //dimItemNameArr[data[i].itemCode+"_"+dimTypeInfo[codeMapping[itemCode][i]].dimTypeId]=data;
                                        itemPathName = data
                                    }
                                })
                                data[i] = dimTypeInfo[codeMapping[itemCode][i]];
                                data[i].itemCode = itemCode;
                                data[i].itemCodeName = itemPathName;
                                data[i].itemParCode = itemParCode;
                                addDimColumnRow("right", data[i]);
                            }
                        }
                    }
                    if (codeMapping[itemCode].length < dimTypeInfo.length) {
                        addDimColumnRow("right", null);
                    }
                }
                treeDataGrid.objBox.scrollLeft = scollLeft;
            });
        } else {
            var arg = arguments;
            setTimeout(function () {
                arg.callee.call(this, columnData);
            }, 50)
        }
    }
    initTreeGrid(window.parent.columnDatas);
}

var clearDimType = function () {
    var tableObj = $("_clumnDimContentTable_right");
    var i = 1;
    while (i < tableObj.rows.length) {
        tableObj.deleteRow(i);
    }
}
var moveDataSrcParId = {};//移动过节点位置的原始父节点数据缓存，key为Id，value为parId
/**
 * 禁用某个编码。
 * @param rowData
 */
var stopUse = function (rowData) {
    var code = rowData[codeColumnName];
    var tempConverter = dhx.extend(new dhtmlxTreeGridDataConverter(), treeGridConverter, true);
    tempConverter.isDycload = true;
    tempConverter.onBeforeConverted = function () {
    };
    if (rowData["OPER_FLAG"] == "inserted") {//如果此编码为新增，停用将直接删除此编码。
        dhx.confirm("此编码为新增，禁用此编码就直接删除此编码，是否继续？", function (r) {
            if (r) {
                deleteModifyData(rowData);
                var rowId = rowData[idCloumnName];
                var subs = treeDataGrid.getAllSubItems(rowId);
                subs = subs ? subs.split(",") : [];
                if (subs.length > 0) {
                    for (var i = 0; i < subs.length; i++) {
                        deleteModifyData(treeDataGrid.getRowData(subs[i]).userdata);
                    }
                }
                treeDataGrid.deleteRow(rowData[idCloumnName]);
            }
        });
        return;
    }
    dhx.confirm("子节点都会被禁用，新增编码/归并编码数据会直接删除，确认禁用此编码？", function (r) {
        if (r) {
            var tempDimType = rowData["DIM_TYPE_ID"];
            var tempModify = rowData;
            var rowId = rowData[idCloumnName];
            //查询其所有子节点。
            DimMergeAction.queryAllSub(tableName, dimInfo.tableDimPrefix, parent.tableUser, dymaicColNames, rowId, function (rsData) {
                rsData = rsData || [];
                rsData.push(tempModify);
                //如果树有子节点，加入其节点数据
                var subNodeData = {};
                var subs = treeDataGrid.getAllSubItems(rowId);
                subs = subs ? subs.split(",") : [];
                if (subs.length > 0) {
                    for (var i = 0; i < subs.length; i++) {
                        subNodeData[subs[i]] = treeDataGrid.getRowData(subs[i]).userdata;
                    }
                }
                var allData = [];
                for (var i = 0; i < rsData.length; i++) {
                    var modify = rsData[i];
                    var modifyId = modify[idCloumnName];
                    //如果子节点存在，用子节点数据替换
                    if (subNodeData[modifyId]) {
                        modify = subNodeData[modifyId];
                        delete subNodeData[modifyId];
                    }
                    //如果此数据有改动，用改动的数据替换。
                    if (updateData[tempDimType][modifyId]) {
                        modify = updateData[tempDimType][modifyId];
                    }
                    allData.push(modify);
                }
                //加入子节点数据。
                for (var key in subNodeData) {
                    allData.push(updateData[tempDimType][key] || subNodeData[key]);
                }
                //循环所有的子节点数据，禁用所有的子节点
                for (var j = 0; j < allData.length; j++) {
                    var tempData = allData[j];
                    if (tempData.STATE == 1) {
                        var id = tempData[idCloumnName];
                        //如果有新增类数据，直接删除
                        if (parseInt(id) < 0) {
                            treeDataGrid.deleteRow(id);
                            //删除数据映射
                            deleteModifyData(tempData);
                            continue;
                        }
                        //修改其STATE
                        tempData.STATE = 0;
                        dealModFlag(tempData, "stopUse");//做标记处理
                        //如果有stopUse操作，缓存数据。
                        if (tempData['OPER_FLAG'].indexOf("stopUse") > -1) {
                            pushModifyData(tempData);
                        } else {//启用禁用抵消，删除映射
                            deleteModifyData(tempData);
                        }
                        if (treeDataGrid.doesRowExist(tempData[idCloumnName])) {
                            treeDataGrid.updateGrid(tempConverter.convert(tempData), "update");
                            //做样式颜色的变更。
                            if (tempData['OPER_FLAG']) {
                                treeDataGrid.setRowTextStyle(tempData[idCloumnName],
                                    modifyStyle[tempData['OPER_FLAG'].split(",")[0]]);
                            } else {//否则还原
                                treeDataGrid.setRowTextStyle(tempData[idCloumnName],
                                    modifyStyle.clear);
                            }
                        }
                    }
                }
            });
        }
    })
}
/**
 * 修改数据映射，缓存，以CODE为键值。
 * @param data
 * @param dimType
 * @param code
 * @param isLevelChange 是否变更层级
 */
var pushModifyData = function (data) {
    var id = data[idCloumnName];
    var code = data[codeColumnName];
    var dimType = data["DIM_TYPE_ID"];
    var parId = data[parIdColumnName];
    codeModifyMapping[code] = codeModifyMapping[code] || [];
    var isFind = false;
    for (var i = 0; i < codeModifyMapping[code].length; i++) {
        if (codeModifyMapping[code][i][idCloumnName]
            == id) {
            isFind = true;
            codeModifyMapping[code][i] = data;
            break;
        }
    }
    if (!isFind) {
        codeModifyMapping[code].push(data);
    }
    //如果是修改的数据，加入updateData
    if (parseInt(id) > 0) {//新增的数据ID<0
        updateData[dimType][id] = data;
    } else {
        !insertData[dimType][parId] && (insertData[dimType][parId] = {});
        insertData[dimType][parId][id] = data;
    }
}
/**
 * 删除一个数据缓存
 * @param data
 */
var deleteModifyData = function (data) {
    var id = data[idCloumnName];
    var code = data[codeColumnName];
    var dimType = data["DIM_TYPE_ID"];
    var parId = data[parIdColumnName];
    codeModifyMapping[code] = codeModifyMapping[code] || [];
    for (var i = 0; i < codeModifyMapping[code].length; i++) {
        if (codeModifyMapping[code][i][idCloumnName]
            == id) {
            codeModifyMapping[code].splice(i, 1);
            break;
        }
    }
    try {
        updateData[dimType][id] = null;
        delete  updateData[dimType][id];
        insertData[dimType][parId][id] = null;
        delete insertData[dimType][parId][id];
    } catch (e) {
    }
}
/**
 * 启用
 * @param rowData
 */
var cancelStopUse = function (rowData) {
    var code = rowData[codeColumnName];
    var tempConverter = dhx.extend(new dhtmlxTreeGridDataConverter(), treeGridConverter, true);
    tempConverter.isDycload = true;
    tempConverter.onBeforeConverted = function () {
    };
    dhx.confirm("启用此编码,父编码也将被启用，是否继续？", function (r) {
        if (r) {
            var rowId = rowData[idCloumnName];
            //递归其父节点，如果父节点被禁用，依次启用。
            while (treeDataGrid.doesRowExist(rowId)) {
                rowData = treeDataGrid.getRowData(rowId).userdata;
                if (rowData.STATE == 0) {//如果被禁用了，才能被启用
                    rowData.STATE = 1;
                    dealModFlag(rowData, "cancelStopUse");
                    pushModifyData(rowData);
                    //如果有stopUse操作，缓存数据。
                    if (rowData['OPER_FLAG'].indexOf("cancelStopUse") > -1) {
                        pushModifyData(rowData);
                    } else {//启用禁用抵消，删除映射
                        deleteModifyData(rowData);
                    }
                }
                treeDataGrid.updateGrid(tempConverter.convert(rowData), "update");
                //做样式颜色的变更。
                if (rowData['OPER_FLAG']) {
                    treeDataGrid.setRowTextStyle(rowData[idCloumnName],
                        modifyStyle[rowData['OPER_FLAG'].split(",")[0]]);
                } else {//否则还原
                    treeDataGrid.setRowTextStyle(rowData[idCloumnName],
                        modifyStyle.clear);
                }
                rowId = treeDataGrid.getParentId(rowId);
            }
        }
    })
}
/**
 * 节点移动数据处理，这里没有对节点是否能够移动的逻辑进行处理，这里只是节点移动完成之后做数据的缓存操作。
 * @param sId
 * @param tId
 */
var dealMoveData = function (sId, tId, sRowData, tRowData, level) {
    var moveData = sRowData.userdata;
    var dimType = moveData.DIM_TYPE_ID;
    var sourceParId = moveData[parIdColumnName];
    var sourceParCode = moveData[parCodeColumnName];
    //获取移动后的父节点数据
    var toRowData = treeDataGrid.getRowData(tId);
    toRowData = toRowData && toRowData.userdata;
    try {
        //删除原来的缓存
        insertData[dimType][sourceParId] && (delete insertData[dimType][sourceParId][sId]);
        changeLevelData[dimType][sourceParId] && (delete changeLevelData[dimType][sourceParId][sId])
        moveData[parIdColumnName] = tId;
        moveData[parCodeColumnName] = (toRowData && toRowData[codeColumnName] || '0');
        moveData["ITEM_PAR_CODE"] = moveData[parCodeColumnName];
    } catch (exception) {/*alert("exception.name 4 =" + exception.name+" exception.message ="+exception.message)*/
    }
    //先删除原来保存的层级变更数据。
    delete changeLevelData[dimType][sId];
    if (moveData["DIMTYPE_CNT"] == 0) {//如果归并数量为0，
        moveData["DIMTYPE_CNT"] += 1;
        //更新codemapping
        codeMapping[moveData[codeColumnName]] = [currentDimType];
    }
    var parId = treeDataGrid.getParentId(sId);
    var itemCodeName = moveData[nameColumnName];
    while (parId) {
        itemCodeName = treeDataGrid.getRowData(parId).data[1] + "->" + itemCodeName;
        parId = treeDataGrid.getParentId(parId);
    }
    moveData.itemCodeName = itemCodeName;
    //    记录原始父节点值
    if (moveDataSrcParId[sId] == undefined || moveDataSrcParId[sId] == null) {
        moveDataSrcParId[sId] = sRowData.getParentId();
    }
    //节点再次移动，比较节点原来的父ID与现在的父ID是否相同，相同则已经移到了原来的位置
    if (moveDataSrcParId[sId] == tId) {
//        updateData[currentDimType][sId] = null;
//        delete updateData[currentDimType][sId];
        if (sId > 0) {
            treeDataGrid.setRowTextStyle(sId, modifyStyle.clear);
            moveDataSrcParId[sId] = null;
            delete moveDataSrcParId[sId];
        } else {
            treeDataGrid.setRowTextStyle(sId, modifyStyle[moveData['OPER_FLAG']].split(",")[0]);
        }
        return;
    }
    dealModFlag(moveData, "move");
    moveData["DIM_LEVEL"] = ((toRowData && toRowData["DIM_LEVEL"]) || 0) + 1;
//    updateData[dimType][sId] = moveData;
    treeDataGrid.setRowTextStyle(sId, modifyStyle.move);
    pushModifyData(moveData);
    //记录层级变更数据
    changeLevelData[dimType][sId] = {srcParId:moveDataSrcParId[sId], newParId:tId, srcParCode:sourceParCode, newParCode:moveData[parCodeColumnName]};
}
/**
 * 处理操作操作标识，这里判断操作类型是否追加，或者覆盖等逻辑
 * @param modifyData
 * @param modifyFlag
 */
var dealModFlag = function (modifyData, modifyFlag) {
    try {
        var orgFlag = modifyData["OPER_FLAG"] || "";
    } catch (exception) {/*alert("exception.name 5 = "+exception.name +" exception.message ="+exception.message)*/
    }
    switch (modifyFlag) {
        case "stopUse":
        { //如果有修改记录，以停用为准。
            if (modifyData["OPER_FLAG"]) {
                modifyData._orgOpr = modifyData["OPER_FLAG"];
            }
            //如果有启用操作，启用停用为一对，相互抵消。
            if (modifyData["OPER_FLAG"] && modifyData["OPER_FLAG"].indexOf("cancelStopUse") != -1) {
                modifyData["OPER_FLAG"] = modifyData["OPER_FLAG"].replace(/cancelStopUse/g, "");
                modifyData["OPER_FLAG"] = modifyData["OPER_FLAG"].replace(/,,/, ",");
                break;
            }
            modifyData["OPER_FLAG"] = "stopUse";//修改类型为停用。
            break;
        }
        case "cancelStopUse":
        {
            //如果有停用操作，启用停用为一对，相互抵消。
            if (modifyData["OPER_FLAG"] && modifyData["OPER_FLAG"].indexOf("stopUse") != -1) {
                modifyData["OPER_FLAG"] = modifyData["OPER_FLAG"].replace(/stopUse/g, "");
                modifyData["OPER_FLAG"] = modifyData["OPER_FLAG"].replace(/,,/, ",");
                break;
            }
        }
        case "move":
        case "modify":
        {
            try {
                if (orgFlag == "inserted" || orgFlag == "merge") {
                    break;
                } else if (orgFlag.indexOf(modifyFlag) == -1) {
                    modifyData["OPER_FLAG"] = orgFlag ? (modifyFlag + "," + orgFlag) : modifyFlag;
                }
                break;
            } catch (exception) {/*alert("exception.name 6 = "+exception.name+" exception.message ="+exception.message)*/
            }
        }
        case "inserted":
        case "merge":
        {
            modifyData["OPER_FLAG"] = modifyFlag;
            break;
        }
    }
}

/**
 * 备份treegrid数据
 * @param id
 */
var backUpGridData = function (id) {
    //数据备份
    var ids = [id];
    var currRowdata = {};
    var subIds = treeDataGrid.getSubItems(id);
    if (subIds) {
        ids = ids.concat(subIds.split(","));
    }
    for (var i = 0; i < ids.length; i++) {
        currRowdata[ids[i]] = treeDataGrid.getRowData(ids[i]);
    }
    return currRowdata;
}
/**
 * 还原TreeGrid数据
 * @param id
 */
var restoreGridData = function (id, currRowdata) {
    var ids = [id];
    var subIds = treeDataGrid.getSubItems(id);
    if (subIds) {
        ids = ids.concat(subIds.split(","));
    }
    for (var i = 0; i < ids.length; i++) {
        var level = treeDataGrid.getLevel(id) + 1;
        currRowdata[ids[i]].DIM_LEVEL = level;
        treeDataGrid.cells(ids[i], 0).cell.parentNode._attrs = currRowdata[ids[i]];
        treeDataGrid.setUserData(ids[i], "DIM_LEVEL", level);
    }
}
/**
 * 节点向上移动。即提高层级。
 * @param id
 */
var moveUp = function (id) {
    //先进行节点移动合法性检查。
    if (!treeDataGrid.getParentId(id)) {//如果是父节点不能上升层级。
        dhx.alert("当前节点已经处于" + dimTypeInfo[treeDataGrid.getUserData(id, "DIM_TYPE_ID")].dimTypeName + "规定的最大层级：" +
            dimTypeInfo[treeDataGrid.getUserData(id, "DIM_TYPE_ID")].maxLevel + "，不能再提高层级！")
        return;
    }
    var data = backUpGridData(id);
    //向上底层层级。
    var parId = treeDataGrid.getParentId(id);
    treeDataGrid.moveRowTo(id, parId, "move");
    treeDataGrid.moveRowUp(id);
    treeDataGrid.selectRowById(id);
    restoreGridData(id, data);
    dealMoveData(id, treeDataGrid.getParentId(id), data[id],
        parId ? treeDataGrid.getRowData(parId).userdata['DIM_LEVEL'] : 0);
    if (treeDataGrid.getSubItems(parId).split(",").length == 1) {
        treeDataGrid.setItemImage(parId, "leaf.gif");
    }

}
/**
 * 向下移动，也即往下调级。
 * @param id
 */
var moveDown = function (id) {
    //判断当前节点是否为最大层级
    if (parseInt(dimTypeInfo[treeDataGrid.getUserData(id, "DIM_TYPE_ID")].maxLevel) ==
        parseInt(treeDataGrid.getUserData(id, "DIM_LEVEL"))) {
        dhx.alert("当前节点已经处于" + dimTypeInfo[treeDataGrid.getUserData(id, "DIM_TYPE_ID")].dimTypeName + "规定的最大层级：" +
            dimTypeInfo[treeDataGrid.getUserData(id, "DIM_TYPE_ID")].maxLevel + "，不能再降低层级！");
        return false;
    }
    //寻找下一个相邻节点。
    var x = treeDataGrid._h2.get[id];
    var next = x.parent.childs[x.index + 1];
    if (!next) {
        return;
    }
    if (!isMerge(next.id)) {
        dhx.alert("下移节点没有归并到：" + currentDimTypeName + "，不能降低层级！");
        return false;
    }
    //进行降低层级操作
    var data = backUpGridData(id);
    var parId = next.id;
    treeDataGrid.moveRowTo(id, parId, "move", "child");
//    treeDataGrid.moveRowUp(id);
    treeDataGrid.selectRowById(id, false, true, true);
    restoreGridData(id, data);
    dealMoveData(id, treeDataGrid.getParentId(id), data[id], treeDataGrid.getRowData(parId).userdata['DIM_LEVEL'] + 1);
    treeDataGrid.setItemImage(parId, "folderOpen.gif");
    treeDataGrid.openItem(parId);
}

/**
 * 显示Commbotree
 * @param target
 * @param hide
 */
var showDimComboTree = function (target, hide, dimType, callback, selectId) {
    if (!treeObject) {
        //创建tree Div层
        var div = dhx.html.create("div", {
            style:"display;none;position:absolute;border: 1px #eee solid;height: 100px;overflow: auto;padding: 0;margin: 0;" +
                "z-index:1000"
        });
        treeObject = new Object();
        treeObject.treeDiv = div;
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
        treeObject.tree = tree;
    } else {
        var childs = treeObject.tree.getSubItems(0);
        if (childs) {
            var childIds = (childs + "").split(",");
            for (var i = 0; i < childIds.length; i++) {
                treeObject.tree.deleteItem(childIds[i]);
            }
        }
    }
    treeObject.tree.detachAllEvents();
    treeObject.treeDiv.style.width = "150px";
    //树双击鼠标事件
    treeObject.tree.attachEvent("onDblClick", function (nodeId) {
        var parId = treeObject.tree.getParentId(nodeId);
        var itemCodeName = treeObject.tree.getItemText(nodeId);
        while (parId) {
            itemCodeName = treeObject.tree.getItemText(parId) + "->" + itemCodeName;
            parId = treeObject.tree.getParentId(parId);
        }
        //添加表单的itemname
        var formData = Tools.getFormValues("_addCodeForm_insert");
        var temp = treeDataGrid.getUserData(treeDataGrid.getSelectedRowId(),
            nameColumnName);
        if (temp == "" || temp == null) {

        } else {
            itemCodeName += "->" + (temp == null ? " " : temp);
        }

        target.value = itemCodeName;
        target.parCode = treeObject.tree.getUserData(nodeId, "rowData")[dimInfo.tableDimPrefix + "_CODE"];
        hide.value = nodeId;

        //关闭树
        treeObject.treeDiv.style.display = "none";
        if (callback) {
            callback(nodeId, dimType, target.parentNode.parentNode._index);
        }
    });
    Tools.autoPosition(treeObject.treeDiv, target, true, 20, 20);
    var treeData = [];
    if (completeTreeData[dimType]) {
        treeData = completeTreeData[dimType].slice();
    }   //如果有新增数据，直接加入
    if (insertData[dimType]) {
        for (var key in insertData[dimType]) {
            for (var key1 in insertData[dimType][key]) {
                treeData.push(insertData[dimType][key][key1]);
            }
        }
    }
    /*任何情况下均添加根节点。*/
    treeData.push(rootData);
    treeObject.tree.loadJSONObject(treeConverter.convert(treeData));
    if (selectId) {
        treeObject.tree.selectItem(selectId);
    }
}
/**
 * 显示关联的维度字段
 * @param target
 * @param hide
 * @param treeData
 */
var loadRefComboTree = function (target, hide, dimId, dimLevel, dimType) {
    var div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
            "z-index:1000"
    });
    document.body.appendChild(div);
    div.style.width = target.offsetWidth + "px";
    target.readOnly = true;
    div.style.display = "none";
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
    this.treeDiv = div;
    this.tree = tree;
    this.target = target;
    var that = this;
    //加载关联维表数据
    dwrCaller.executeAction("queryRefDimData", dimId, parent.tableUser, dimLevel || 1, dimType, function (treeData) {
        that.tree.loadJSONObject(treeData);
        div.style.display = "none";
    });
}


//判断某个节点是否已归并。
var isMerge = function (id) {
    if (id == 0) {
        return true;
    }
    var rowData = treeDataGrid.getRowData(id);
    try {
        return !!rowData.userdata.DIM_TYPE_ID && rowData.userdata.DIM_TYPE_ID == currentDimType;
    } catch (exception) {/*alert("exception.name 7 = "+exception.name+" exception.message ="+exception.message)*/
    }
}

/**
 * 向上排序
 * @param rowData
 */
var orderUp = function (rowId) {
    if (!isMerge(rowId)) {
        dhx.alert("该节点没有归并到指定类型：" + currentDimTypeName + ",不能移动");
        return;
    }
    var x = treeDataGrid._h2.get[rowId];
    var p = x.parent.childs[x.index - 1];
    //不是同级不可以移动。
    if ((!p) || (p.parent != x.parent)) {
        return;
    }
    var rowIndex = x.index;
    var rowData = treeDataGrid.getRowData(rowId);
    if (rowData._orgIndex == null || rowData._orgIndex == undefined) {
        rowData._orgIndex = rowIndex;
    } else {//已经至少移动过一次
        if (rowData._orgIndex == p.index) {//说明已移回原来位置
            delete orderData[rowId];
        }
    }
    var nextId = x.buff.idd;
    var order = treeDataGrid.getRowData(nextId).userdata.ORDER_ID;
    order = (order == null || order == undefined) ? 0 : order;
    orderData[rowId] = --order;
    treeDataGrid.moveRowUp(rowId);
}
/**
 * 向下排序
 * @param rowId
 */
var orderDown = function (rowId) {
    if (!isMerge(rowId)) {
        dhx.alert("该节点没有归并到指定类型：" + currentDimTypeName + ",不能移动");
        return;
    }
    var x = treeDataGrid._h2.get[rowId];
    var p = x.parent.childs[x.index + 1];
    //不是同级不可以移动。
    if ((!p) || (p.parent != x.parent)) {
        return;
    }
    var rowIndex = x.index;
    var rowData = treeDataGrid.getRowData(rowId);
    if (rowData._orgIndex == null || rowData._orgIndex == undefined) {
        rowData._orgIndex = rowIndex;
    } else {//已经至少移动过一次
        if (rowData._orgIndex == p.index) {//说明已移回原来位置
            delete orderData[rowId];
        }
    }
    var nextId = x.buff.idd;
    var order = treeDataGrid.getRowData(nextId).userdata.ORDER_ID;
    order = (order == null || order == undefined) ? 0 : order;
    orderData[rowId] = --order;
    treeDataGrid.moveRowDown(rowId);
}
/**
 * 提交审核Action定义。
 */
dwrCaller.addAutoAction("sumbitAudit", DimMergeAction.sumbitAudit, {
    dwrConfig:true,
    isDealOneParam:false,
    processMessage:{message:"正在提交审核，请稍后...", title:"数据审核"}
});
/**
 * 提交审核
 */
var submitAudit = function () {
    var batchId = dhx.uid();
    dhx.confirm("确认提交审核？审核过程中不可修改数据！", function (r) {
        if (r) {
            //获取数据。
            var auditData = {};
            auditData.dimTypeId = dwr.util.getValue(queryform.getSelect("dimType"));
            ;//获取当前的归并类型的
            //添加维表数据
            auditData.dimInfo = dimInfo;
            dimInfo.tableOwner = parent.tableUser;
            //排序数据
            auditData.orderData = orderData;
            //类型映射
            var mapping = {modify:1, stopUse:5, cancelStopUse:7, merge:6, inserted:4, move:3}
            //修改数据。
            if (!Tools.isEmptyObject(codeModifyMapping)) {
                var tempData = [];
                for (var key in codeModifyMapping) {
                    for (var i = 0; i < codeModifyMapping[key].length; i++) {
                        var row = codeModifyMapping[key][i];
                        row.ITEM_ID = row[idCloumnName];
                        row.ITEM_NAME = row[nameColumnName];
                        row.ITEM_CODE = row[codeColumnName];
                        row.ITEM_DESC = row[descColumnName];
                        //row.ITEM_PAR_CODE = row[parCodeColumnName]
                        row.BATCH_ID = batchId;
                        row.ITEM_PAR_ID = row[parIdColumnName];
                        /**该注释代码修复修改编码信息提交审核后再次打开修改界面，数据为null的情况*/
//                delete row[idCloumnName];
//                delete row[nameColumnName];
//                delete row[codeColumnName];
//                delete row[descColumnName];
//                delete row[parIdColumnName];
                        row.MOD_FLAG = row.OPER_FLAG.replace(/(\w+)(,?)/g, function ($0, $1, $2) {
                            return mapping[$1] + $2;
                        });
                        row.DIM_TABLE_ID = tableId;
                        row.AUDIT_FLAG = 0;//未审核。
                        //动态字段。
                        for (var j = 1; j <= dymaicColNames.length; j++) {
                            row["COL" + j] = "colName:" + dymaicColNames[j - 1] + ",value:" + row[dymaicColNames[j - 1]];
                            delete row[dymaicColNames[j - 1]];
                        }
                        tempData.push(row);
                    }
                }
                auditData.modifyData = tempData;
            }
            var isOnlyOrder=false;
            auditData.dymaicCloCount = dymaicColData ? dymaicColData.length : 0;
            if (!auditData.modifyData || auditData.modifyData.length == 0) {
                if(!Tools.isEmptyObject(auditData.orderData)){
                    dhx.alert("您只做了排序操作，排序会直接生效，无需审核！")
                    isOnlyOrder=true;
                }else{
                    dhx.alert("您未做任何申请，不能提交审核！");
                    isOnlyOrder=false;
                    return;
                }
            }
            //提交审核
            dwrCaller.executeAction("sumbitAudit", auditData, function (data) {
                if (data) {
                    codeModifyMapping = {};
                    codeMapping = {}
                    updateData = {};
                    insertData = {};
                    var message=isOnlyOrder?"已为您成功排序！": "提交审核成功！待审核后生效";
                    dhx.alert(message);
                } else {
                    dhx.alert("提交审核失败，请重试！这有可能是由于当前归并类型已经有数据待审核中");
                }
            });
        }
    })
}

/**
 * 整理层级Action定义。
 */
dwrCaller.addAutoAction("dealDimLevel", DimMergeAction.dealDimLevel, {
    dwrConfig:true,
    isDealOneParam:false
});
//关联字段数据查询Action定义。
dwrCaller.addAutoAction("queryRefDimData", DimMergeAction.queryRefDimData);

var dealLevel = function () {
    if (dimInfo) {
        dwrCaller.executeAction("dealDimLevel", dimInfo.tableName, dimInfo.tableDimPrefix, parent.tableUser, function (data) {
            if (data) {
                dhx.alert("处理成功！");
            } else {
                dhx.alert("处理失败！");
            }
        });
    } else {
        setTimeout(arguments.callee, 50);
    }
}
//新增编码的时候，已经选择的归并类型数据缓存。
var insertDataTemplate = {size:0};//key 为归并类型，value为rowIndex
/**
 * 为选中的节点新增子节点，如果parId值为NULL，新增根节点。
 * @param parId
 */
var addCode = function (parCodeData) {
    if (parCodeData != null) {
        var parCodeName = dimInfo.tableDimPrefix + "_CODE";
        parCode = parCodeData.parCodeName;
    }
    insertDataTemplate = {size:0};//key 为归并类型，value为rowIndex
    var dimType = currentDimType;
    if (parCodeData && dimType) {  //改为树的层级树>层级规定的数
        if (parseInt(dimTypeInfo[dimType].maxLevel) < parseInt(parCodeData["DIM_LEVEL"]) + 1) {
            dhx.alert("超越了" + dimTypeInfo[dimType].dimTypeName + "规定的最大层级：" + dimTypeInfo[dimType].maxLevel +
                "，不能作为其子节点！");
            return;
        }
    }

    window.validateCode = function (modifyCode) {
        if (modifyCode != parCodeData && codeMapping[modifyCode]) {
            return "您已添加此编码，不能重复添加";
        }
        return true;
    }
    var winSize = Tools.propWidthDycSize(10, 25, 10, 25);
    dhxWindow.createWindow("addCodeWindow", 0, 0, 450, 400);
    var addCodeWindow = dhxWindow.window("addCodeWindow");
    addCodeWindow.stick();
    addCodeWindow.setDimension(600, winSize.height);
    addCodeWindow.button("minmax1").hide();
    addCodeWindow.button("park").hide();
    addCodeWindow.button("stick").hide();
    addCodeWindow.button("sticked").hide();
    addCodeWindow.center();
    addCodeWindow.denyResize();
    addCodeWindow.denyPark();
    addCodeWindow.setText("新增编码");
    addCodeWindow.keepInViewport(true);
    addCodeWindow.show();
//    addCodeWindow.attachEvent("onClose", function () {
//        dhxWindow.unload();
//    })
    //建立一个DIV，最高为body高度，当超过body高度时，用滚动条。
    var winDiv = document.createElement("div");
    winDiv.style.width = "100%"
    winDiv.style.height = document.body.offsetHeight > 500 ? "100%" : ((winSize.height - 40) + "px");
    winDiv.style.overflow = "auto";
    addCodeWindow.attachObject(winDiv);
    //新增Layout
    var addCodeLayout = new dhtmlXLayoutObject(winDiv, "3E");
    addCodeLayout.cells("a").setText('新增维表数据');
    addCodeLayout.cells("a").setHeight(winSize.height / 2);
    addCodeLayout.cells("c").setHeight(20);
    addCodeLayout.cells("a").fixSize(false, true);
    var div = document.createElement("div");
    div.style.cssText = "width:100%;height:170px;overflow-y:auto;";
    div.innerHTML = $("_addCodeTemplate").innerHTML.replace(/\{template\}/g, "insert");
    addCodeLayout.cells("a").attachObject(div);
    addCodeLayout.cells("b").setText("选择归并类型");
    div = document.createElement("div");
    div.style.cssText = "width:100%;height:86px;overflow-y:auto;overflow-x:hidden;";
    div.innerHTML = $("_template").innerHTML.replace(/\{template\}/g, "addCode");
    addCodeLayout.cells("b").attachObject(div);
    $("_columnDimForm_addCode").style.width = ($("_columnDimForm_addCode").offsetWidth - 2) + "px";
    $("_clumnDimContentDiv_addCode").style.height = (div.offsetHeight - 30) + "px";
    //加入一个按钮DOV
    var buttonDiv = document.createElement("div");
    buttonDiv.style.cssText = "width:100%;height:30px;position: relative;";
    var okButton = Tools.getButtonNode("确定");
    okButton.style.marginLeft = "200px";
    okButton.style.marginTop = "10px";
    okButton.style.cssFloat = "left";
    okButton.style.styleFloat = "left";
    //窗口关闭事件
    okButton.onclick = function () {
        if (parCode == undefined) {
            parCode = "0";
        }
        if (dhtmlxValidation.validate("_addCodeForm_insert")) {
            if (insertDataTemplate.size == 0) {
                dhx.alert("您至少需要为新增的编码选择一个归并类型！");
                return;
            } else {
                var formData = Tools.getFormValues("_addCodeForm_insert");
                //封装新增数据
                var currentTypeData = null;
                for (var key in insertDataTemplate) {
                    if (key == "size") {
                        continue;
                    }
                    var newData = dhx.extend({}, formData);
                    var parCodeInput = $("_itemCodeName" + "addCode" + insertDataTemplate[key]);
                    var parId = ($("_parIdaddCode" + insertDataTemplate[key]) &&
                        $("_parIdaddCode" + insertDataTemplate[key]).value) ||
                        parCodeData[idCloumnName];
                    parId = parseInt(parId) == -1 ? 0 : parId;
                    newData[parIdColumnName] = parId;
//                    newData["OPER_FLAG"] = 'inserted';//代表新增
                    newData["DIM_TYPE_ID"] = key;
                    newData["ORDER_ID"] = 0;//初始化ORDER_ID==0;
                    newData["STATE"] = 1;
                    newData["ITEM_PAR_CODE"] = (parCodeInput && parCodeInput.parCode) || (parCodeData && parCodeData[dimInfo.tableDimPrefix + "_CODE"]) || parCode;
                    newData["DIM_LEVEL"] = (parCodeData ? (parseInt(parCodeData["DIM_LEVEL"]) + 1)
                        : (treeObject.tree.getLevel(parId) + 1)) || 1;
                    newData["DIMTYPE_CNT"] = insertDataTemplate.size;
                    dealModFlag(newData, "inserted");
                    newData.itemCodeName = (parCodeInput && parCodeInput.value) ||
                        (parCodeInput && parCodeInput.innerHTML) + "->" + formData.itemName;
                    newData[idCloumnName] = 0 - parseInt(dhx.uid());
                    newData[codeColumnName] = formData.itemCode;
                    newData[nameColumnName] = formData.itemName;
                    newData[descColumnName] = formData.itemDesc;
                    insertData[key] = insertData[key] || {};
                    insertData[key][parId] = insertData[key][parId] || {};
                    insertData[key][parId][newData[idCloumnName]] = newData;
                    //codeMapping
                    var codeKey = formData.itemCode;
                    codeMapping[codeKey] = codeMapping[codeKey] || [];
                    codeMapping[codeKey].push(key);
                    pushModifyData(newData);
                    if (dimType && key == dimType) {
                        currentTypeData = newData;
                    }
                }
                //向页面添加编码数据，parCodeData不为空时表示已经选中根节点，为空时把当前节点做为根节点显示在界面
                if (currentTypeData) {
                    var parId = currentTypeData[parIdColumnName];
                    var tempConverter = dhx.extend(new dhtmlxTreeGridDataConverter(), treeGridConverter, true);
                    tempConverter.isDycload = true;
                    tempConverter.onBeforeConverted = function () {
                    };
                    if (parId == 0 || treeDataGrid.doesRowExist(parId)) {
                        if (treeDataGrid.hasChildren(parId) && !treeDataGrid.getOpenState(parId)) {
                            //如果树未展开而且没有子节点，那么不需要新增节点，将其展开就可以看到新增的节点。
                            treeDataGrid.openItem(parId);
                        } else {
                            treeDataGrid.updateGrid(tempConverter.convert(currentTypeData), "insert");
                            parId && treeDataGrid.setItemImage(parId, "folderOpen.gif");
                        }
                    }
                }
                addCodeWindow.close();
                //清空选择.
                $("_parIdaddCode" + insertDataTemplate[key]) && ($("_parIdaddCode" + insertDataTemplate[key]).value = "");
            }
        }
    }
    buttonDiv.appendChild(okButton);
    //取消
    var close = Tools.getButtonNode("关闭");
    close.style.styleFloat = "left";
    close.style.cssFloat = "left";
    close.style.marginTop = "10px";
    close.style.marginLeft = "5px";
    close.onclick = function () {
        addCodeWindow.close();
    }
    buttonDiv.appendChild(close);
    addCodeLayout.cells("c").hideHeader();
//    div.appendChild(buttonDiv);
    addCodeLayout.cells("c").attachObject(buttonDiv);
    addCodeLayout.hideSpliter();
    addCodeLayout.hideConcentrate();
    //添加动态字段
    var validate = [
        {target:"_itemName_insert", rule:"NotEmpty,MaxLength[32]"},
        {target:"_itemCode_insert", rule:"NotEmpty,MaxLength[20],ValidAplhaNumeric,ValidByCallBack[validateCode]"},
        {target:"_itemDesc_insert", rule:"MaxLength[256]" }
    ];
    var tempConverter = new dhtmxTreeDataConverter();
    tempConverter.isDycload = false;
    tempConverter.setPidColumn(nameColumnName);
    tempConverter.setIdColumn(codeColumnName);
    tempConverter.setTextColumn(nameColumnName);
    tempConverter.isFormatColumn = false;
    dwrCaller.addDataConverter("queryRefDimData", tempConverter);
    var treeObj = {};
    for (var i = 0; i < dymaicColData.length; i++) {
        var isDimCol = (dymaicColData[i].colBusType == 0); //是否是维度字段
        var tr = $("_addCodeTable_insert").insertRow(i + 3);
        var td = tr.insertCell(0);
        td.innerHTML = "";
        //动态字段显示，中文名为null显示其字段名。
        if (dymaicColData[i].colNameCn) {
            td.innerHTML = dymaicColData[i].colNameCn + "：";
        }
        else {
            td.innerHTML = dymaicColData[i].colName + "：";
        }
        td.className = "leftCol";
        td = tr.insertCell(1);
        td.innerHTML = ' <input class="dhxlist_txt_textarea" name="' + dymaicColData[i].colName +
            (isDimCol ? "_input" : "") + '" id=' + '"_' + dymaicColData[i].colName +
            (isDimCol ? "_input" : "") + '_addCode" type="TEXT" style="width: 98%;height: 100%;font-size:12px;font-family:"宋体">';

        /**
         * 添加动态字段的动态验证，如果数据类型为date时，只验证其时间格式合法性。其他需验证长度
         * @param {Object} e
         * @memberOf {TypeName}
         */
        var dataType = dymaicColData[i].colDatatype.replace(/,/g, "\\,"); //“，”号用"\,"转义。
        var rule = "ValidByCallBack[validateDataType";
        if (dymaicColData[i].colDatatype.toString().search("DATE") > -1) {
            rule += "&" + dataType + "]";
        }
        else {
            rule += "&" + dataType + "," + dymaicColData[i].colSize + "," + dymaicColData[i].colPrec + "]";
        }
        //不允许为空
        if (dymaicColData[i].colNullabled == 0) {
            rule += ",NotEmpty";
        }
        validate.push({target:"_" + dymaicColData[i].colName + "_addCode", rule:rule});
        td = tr.insertCell(2);
        if (isDimCol) {//如果是维度指标字段,加入请选择按钮
            //加入一个隐藏域。
            var hidden = dhx.html.create("input", {type:"hidden", name:dymaicColData[i].colName,
                id:'_' + dymaicColData[i].colName + '_addCode'});
            td.appendChild(hidden);
            var choose = Tools.getButtonNode("请选择");
            choose.setAttribute("id", "_choose_" + i);
            choose._index = i;
            treeObj[i] = new loadRefComboTree($('_' + dymaicColData[i].colName + "_input_addCode"), hidden,
                dymaicColData[i].dimTableId, dymaicColData[i].dimLevel, dymaicColData[i].dimTypeId);
            choose.onclick = function (e) {
                treeObj[this._index].treeDiv.style.display = "inline";
                Tools.autoPosition(treeObj[this._index].treeDiv, treeObj[this._index].target, true);
            };
            td.appendChild(choose);
        } else {
            td.innerHTML = '&nbsp;'
        }
    }
    //添加表单验证。
    dhtmlxValidation.addValidation("_addCodeForm_insert", validate);
    selectDimType['addCode'] = {};
    /**
     * 当归并编码点击时的事件。
     */
    var onItemNameClick = function (e) {
        e = e || window.event;
        var target = e.srcElement || e.target;
        var tr = target.parentNode.parentNode;
        var rowIndex = tr._index;
        var dimType = dwr.util.getValue($("_dimTypeIdaddCode" + rowIndex));
        if (!dimType) {
            dhx.alert("请您先选择一个归并类型！");
            return;
        }
        //选择时新增的数据缓存到局部变量中，之后再同步到全局变量中。
        showDimComboTree(target, $("_parIdaddCode" + rowIndex), dimType, function (parId, _selectDimType, selectIndex) {
            //子编码数据
            var dimTypeName = dwr.util.getText("_dimTypeId" + "addCode" + selectIndex);
            //父编码数据
            var parCodeData = treeObject.tree.getUserData(parId, "rowData");
            //parCode =parCodeData[dimInfo.tableDimPrefix+"_CODE"];
            if (parseInt(dimTypeInfo[_selectDimType].maxLevel) < parseInt(parCodeData["DIM_LEVEL"]) + 1) {
                dhx.alert("超越了" + dimTypeName + "规定的最大层级：" + dimTypeInfo[_selectDimType].maxLevel + "，不能作为其子节点！");
                return false;
            }
            insertDataTemplate[_selectDimType] = selectIndex;
            insertDataTemplate.size++;
            if ($("_clumnDimContentTable_addCode").rows.length - 1 < dimTypeInfo.length) {
                addDimColumnRow("addCode", null, null, arguments.callee);
            }
//            $("_canceladdCode" + selectIndex)
//                    .innerHTML = '<img src="../../../resource/images/cancel.png" alt="删除" id="_img_addCode_' +
//                                 selectIndex + '" style="width:16px;height: 16px;cursor: pointer">';
//            $("_img_addCode_" + selectIndex).onclick = function(){
//                $("_img_addCode_" + selectIndex).parentNode.parentNode.parentNode.removeChild($("_img_addCode_" + selectIndex)
//                        .parentNode.parentNode);
//                insertDataTemplate[_selectDimType] = null;
//                insertDataTemplate.size--;
//                alert( insertDataTemplate.size);
//                if(insertDataTemplate.size==1){
//                	$("_canceladdCode" + selectIndex).innerHTML = '';
//                }
//            }
            return true;
        });
    }
    //新增类型编码。
    if (parCodeData && parCodeData.DIM_TYPE_ID == currentDimType) {
        var itemCode = parCodeData[codeColumnName];
        var data = dimTypeInfo[parCodeData.DIM_TYPE_ID];
        data.itemCode = itemCode;
        addDimColumnRow("addCode", data);
        insertDataTemplate[parCodeData.DIM_TYPE_ID] = dimColumnNum;
        insertDataTemplate.size++;
        $("_itemCodeNameaddCode" + dimColumnNum) &&
        ($("_itemCodeNameaddCode" + dimColumnNum).onclick = onItemNameClick);
        if (codeMapping[itemCode].length < dimTypeInfo.length) {
            addDimColumnRow("addCode", null);
            $("_itemCodeNameaddCode" + dimColumnNum) &&
            ($("_itemCodeNameaddCode" + dimColumnNum).onclick = onItemNameClick);
        }
    } else {
        addDimColumnRow("addCode", null);
        $("_itemCodeNameaddCode" + dimColumnNum) &&
        ($("_itemCodeNameaddCode" + dimColumnNum).onclick = onItemNameClick);
    }
}
/**
 * 根据编码查询维表信息Action定义
 */
dwrCaller.addAutoAction("queryDimDataByCode", DimMergeAction.queryDimDataByCode, {
    dwrConfig:true,
    isDealOneParam:false,
    isShowProcess:false,
    async:false
});
dwrCaller.addAutoAction("valiHasCode", DimMergeAction.valiHasCode, {
    dwrConfig:true,
    isDealOneParam:false,
    isShowProcess:false,
    async:false
});

/**
 * 修改编码
 * @param rowData
 */
var modifyCode = function (rowData) {
    var code = rowData[codeColumnName];
    window.validateCode = function (modifyCode) {
        if (modifyCode != code && codeMapping[modifyCode]) {
            return "您已添加此编码，不能重复添加";
        }
        return true;
    }
    var id = rowData[idCloumnName];
    dhxWindow.createWindow("modifyCodeWindow", 0, 0, 450, 400);
    var modifyCodeWindow = dhxWindow.window("modifyCodeWindow");
    modifyCodeWindow.stick();
    modifyCodeWindow.setDimension(600, 350);
    modifyCodeWindow.button("minmax1").hide();
    modifyCodeWindow.button("park").hide();
    modifyCodeWindow.button("stick").hide();
    modifyCodeWindow.button("sticked").hide();
    modifyCodeWindow.center();
    modifyCodeWindow.denyResize();
    modifyCodeWindow.denyPark();
    modifyCodeWindow.setText("修改编码");
    modifyCodeWindow.keepInViewport(true);
    modifyCodeWindow.show();
//    modifyCodeWindow.attachEvent("onClose", function () {
//        dhxWindow.unload();
//    });
    //建立一个DIV，最高为body高度，当超过body高度时，用滚动条。
    var winDiv = document.createElement("div");
    winDiv.style.width = "100%"
//    winDiv.style.height = document.body.offsetHeight > 350 ? "100%" : (document.body.offsetHeight + "px");
    winDiv.style.overflow = "auto";
    modifyCodeWindow.attachObject(winDiv);
    winDiv.innerHTML = $("_addCodeTemplate").innerHTML.replace(/\{template\}/g, "update");

    var validate = [
        {target:"_itemName_update", rule:"NotEmpty,MaxLength[32]"},
        {target:"_itemCode_update", rule:"NotEmpty,MaxLength[20],ValidAplhaNumeric,ValidByCallBack[validateCode]"},
        {target:"_itemDesc_update", rule:"MaxLength[256]" }
    ];
    var tempConverter = new dhtmxTreeDataConverter();
    tempConverter.isDycload = false;
    tempConverter.setPidColumn(nameColumnName);
    tempConverter.setIdColumn(codeColumnName);
    tempConverter.setTextColumn(nameColumnName);
    tempConverter.isFormatColumn = false;
    dwrCaller.addDataConverter("queryRefDimData", tempConverter);
    var treeObj = {};
    for (var i = 0; i < dymaicColData.length; i++) {
        var isDimCol = (dymaicColData[i].colBusType == 0); //是否是维度字段
        var tr = $("_addCodeTable_update").insertRow(i + 3);
        var td = tr.insertCell(0);
        if (dymaicColData[i].colNameCn) {
            td.innerHTML += dymaicColData[i].colNameCn + "：";
        }
        else {
            td.innerHTML += dymaicColData[i].colName + "：";
        }
        td.className = "leftCol";
        td = tr.insertCell(1);
        td.innerHTML = ' <input class="dhxlist_txt_textarea" name="' + dymaicColData[i].colName +
            (isDimCol ? "_input" : "") + '" id=' + '"_' + dymaicColData[i].colName +
            (isDimCol ? "_input" : "") + '_update" type="TEXT" style="width: 100%;height: 100%">';
        /**
         * 添加动态字段的动态验证，如果数据类型为date时，只验证其时间格式合法性。其他需验证长度
         * @param {Object} e
         * @memberOf {TypeName}
         */
        var dataType = dymaicColData[i].colDatatype.replace(/,/g, "\\,"); //“，”号用"\,"转义。
        if (dymaicColData[i].colDatatype.toString().search("DATE") > -1) {
            validate.push({target:"_" + dymaicColData[i].colName + "_update", rule:"ValidByCallBack[validateDataType&" + dataType + "]"});
        }
        else {
            validate.push({target:"_" + dymaicColData[i].colName + "_update", rule:"ValidByCallBack[validateDataType&" + dataType + "&" + dymaicColData[i].colSize + "&" + dymaicColData[i].colPrec + "]"});
        }

        td = tr.insertCell(2);
        if (isDimCol) {//如果是维度指标字段,加入请选择按钮
            //加入一个隐藏域。
            var hidden = dhx.html.create("input", {type:"hidden", name:dymaicColData[i].colName,
                id:'_' + dymaicColData[i].colName + '_update'});
            td.appendChild(hidden);
            var choose = Tools.getButtonNode("请选择");
            choose.setAttribute("id", "_choose_" + i);
            choose._index = i;
            treeObj[i] = new loadRefComboTree($('_' + dymaicColData[i].colName + "_input_update"), hidden,
                dymaicColData[i].dimTableId, dymaicColData[i].dimLevel, dymaicColData[i].dimTypeId);
            choose.onclick = function (e) {
                treeObj[this._index].treeDiv.style.display = "inline";
                Tools.autoPosition(treeObj[this._index].treeDiv, treeObj[this._index].target, true);
            };
            td.appendChild(choose);
        } else {
            td.innerHTML = '&nbsp;'
        }
    }
    var tr = $("_addCodeTable_update").insertRow($("_addCodeTable_update").rows.length);
    var td = tr.insertCell(0);
    td.setAttribute("colSpan", 3);
    td.style.textAlign = "center";
    td.innerHTML = "注：本操作将影响该编码所有数据";

    var buttonDiv = document.createElement("div");
    buttonDiv.style.cssText = "width:100%;height:30px;position: relative;";
    var okButton = Tools.getButtonNode("确定");
    okButton.style.marginLeft = "200px";
    okButton.style.marginTop = "5px";
    okButton.style.cssFloat = "left";
    okButton.style.styleFloat = "left";
    //窗口关闭事件
    okButton.onclick = function () {

        if (dhtmlxValidation.validate("_addCodeForm_update")) {
            var formData = Tools.getFormValues("_addCodeForm_update");
            var updateCode = document.getElementById("_itemCode_update").value;
            updateCode == codeMapping;
            dwrCaller.executeAction("queryDimDataByCode", code, tableName, parent.tableUser, dimInfo.tableDimPrefix, dymaicColNames,
                function (codeData) {
                    var dataBaseCodeData = {};//数据库中原始的信息。以DIM_TYPE_ID作为主键映射
                    var currentData = {};
                    //先寻找该编码所有的数据
                    if (codeData) {
                        for (var i = 0; i < codeData.length; i++) {
                            dataBaseCodeData[codeData[i]["DIM_TYPE_ID"]] = codeData[i];
                        }
                    }
                    var modifyCodeData = {};
                    if (codeModifyMapping[code]) {
                        for (var i = 0; i < codeModifyMapping[code].length; i++) {
                            modifyCodeData[codeModifyMapping[code][i]["DIM_TYPE_ID"]] = codeModifyMapping[code][i];
                        }
                    }
                    //对所有的编码进行更改。
                    for (var i = 0; i < codeMapping[code].length; i++) {
                        var tempModify = {};
                        var tempDimType = codeMapping[code][i];
                        if (dataBaseCodeData[tempDimType]) {
                            dhx.extend(tempModify, dataBaseCodeData[tempDimType], true);
                        }
                        if (modifyCodeData[tempDimType]) {
                            dhx.extend(tempModify, modifyCodeData[tempDimType], true);
                        }
                        dhx.extend(tempModify, formData, true);
                        //加入修改的关键数据。
                        tempModify[codeColumnName] = formData.itemCode;
                        tempModify[nameColumnName] = formData.itemName;
                        tempModify[descColumnName] = formData.itemDesc;
                        tempModify["ITEM_PAR_CODE"] = tempModify[parCodeColumnName];
                        //判断是否是数据库原生数据
                        dealModFlag(tempModify, "modify");
                        //如果不是页面的新增的数据，做数据缓存。
                        pushModifyData(tempModify);
                        if (tempModify["OPER_FLAG"] != "merge" && tempModify["OPER_FLAG"] != "inserted") {
                            updateData[tempDimType] = updateData[tempDimType] || {};
                            updateData[tempDimType][tempModify[(dimInfo.tableDimPrefix + "_ID")
                                .toUpperCase()]] = tempModify;
                        }
                        if (tempModify[idCloumnName] ==
                            rowData[idCloumnName]) {
                            currentData = tempModify;
                        }
                    }
                    //数据更新。
                    var tempConverter = dhx.extend(new dhtmlxTreeGridDataConverter(), treeGridConverter, true);
                    tempConverter.isDycload = true;
                    tempConverter.onBeforeConverted = function () {
                    };
                    treeDataGrid.updateGrid(tempConverter.convert(currentData), "update");
                    treeDataGrid.setRowTextStyle(rowData[idCloumnName],
                        modifyStyle[currentData['OPER_FLAG']].split(",")[0]);
//                        dhx.alert("修改成功，待数据提交审核后生效!");
                    modifyCodeWindow.close();
                })
        }
    }
    buttonDiv.appendChild(okButton);
    //取消
    var close = Tools.getButtonNode("关闭");
    close.style.styleFloat = "left";
    close.style.cssFloat = "left";
    close.style.marginTop = "5px";
    close.style.marginLeft = "5px";
    close.onclick = function () {
        modifyCodeWindow.close();
    }
    buttonDiv.appendChild(close);
    winDiv.appendChild(buttonDiv);
    $("_addCodeForm_update").style.height = "260px";
    //添加表单验证。
    dhtmlxValidation.addValidation("_addCodeForm_update", validate);
    //赋值操作
    dwr.util.setValue("_itemName_update", rowData[nameColumnName]);
    dwr.util.setValue("_itemCode_update", rowData[codeColumnName]);
    dwr.util.setValue("_itemDesc_update", (rowData[descColumnName]) == null ? "" : (rowData[descColumnName]));
    for (var i = 0; i < dymaicColData.length; i++) {
        dwr.util.setValue("_" + dymaicColData[i].colName + "_update", rowData[dymaicColData[i].colName]);
    }
}
var dimColumnNum = 0;
var selectDimType = {};//当前状态已选择的归并类型。
/**
 * 添加归并编码列信息
 * @param template 替换的模板名称
 * @param rowData 行数据
 * @param rowIndex
 * @param treeOnclick
 */
var addDimColumnRow = function (template, rowData, rowIndex, treeOnclick) {
    rowIndex = (rowIndex == undefined || rowIndex == null) ? (++dimColumnNum) : rowIndex;
    if (!$("_dimColumnRow" + template + rowIndex)) {
        if (dimColumnNum < rowIndex) {
            dimColumnNum = rowIndex;
        }
        //判断是否是数据库中原来的归并类型，并非修改，修改的可以显示编辑框
        var canEdit = true;
        var cancancel = false; //是否可以显示删除图标
        var selectTreeData = null;
        if (rowData && rowData.itemCode) {
            canEdit = false;
            cancancel = false;
//            if(codeModifyMapping[rowData.itemCode]){
//                for(var i = 0; i < codeModifyMapping[rowData.itemCode].length; i++){
//                    if(codeModifyMapping[rowData.itemCode][i].DIM_TYPE_ID == rowData.dimTypeId){
//                        selectTreeData = codeModifyMapping[rowData.itemCode][i];
////                        canEdit = true;
//                        cancancel = true;
//                        break;
//                    }
//                }
//            }
        }
        var tr = document.createElement("tr");
        tr._isEdit = true;//默认可以编辑
        tr._index = rowIndex;
        tr._template = template;
        tr.id = "_dimColumnRow" + template + rowIndex;
        $("_clumDimContentBody_" + template).appendChild(tr);

        //拥有的归并类型名称列
        var td = tr.insertCell(0);
        //归并编码列。
        td.align = "center";
        td.innerHTML = "<span id='_dimTypeCode" + template + rowIndex + "'>" +
            ((rowData && rowData.dimTypeCode) || "&nbsp;") + "</span>";
        //编码名称列
        td = tr.insertCell(1);
        td.align = "center";
        if (!canEdit) { //如果有编码名称和编码名，初始化状态，以Label显示，不可以编辑。
            td.innerHTML = "<span id=_dimType" + template + rowIndex + ">" + rowData.dimTypeName + "</span>";
            td.appendChild(dhx.html.create("input", {type:"hidden", name:"dimTypeId", id:"_dimTypeId" + template + rowIndex, value:rowData.dimTypeId}));
            selectDimType[template][rowData.dimTypeId] = rowData.dimTypeId;
        } else {
            var select = dhx.html.create("select",
                {style:"width:100%", id:"_dimTypeId" + template + rowIndex, name:"dimTypeId"});
            var options = queryform.getSelect("dimType").options;
            for (var i = 0; i < options.length; i++) {
                if (!selectDimType[template][options[i].value]) {
                    var option = new Option(options[i].text, options[i].value);
                    if (rowData && rowData.dimTypeId == option.value) {
                        option.selected = 1;
                    }
                    select.options[select.options.length] = option;
                }
            }
            var tempSelectDimType = dwr.util.getValue(select);
            //记录初始化选择的归并类型。
            selectDimType[template][tempSelectDimType] = tempSelectDimType;
            select._selectType = tempSelectDimType;
            select.onchange = function () {
                var changeSelect = function () {
                    var selectType = dwr.util.getValue(select);
                    //如果是新增编码操作，切换其已经选择了的缓存
                    if (insertDataTemplate[select._selectType]) {
                        delete insertDataTemplate[select._selectType];
                        insertDataTemplate.size--;
                    }
                    if (selectType) {
                        if (selectDimType[template][selectType]) {
                            dhx.alert("您已经选择了此归并类型，不能再次选择。");
                            //还原已经的选择。
                            dwr.util.setValue(select, select._selectType);
                            return;
                        }
                        $("_dimTypeCode" + template + rowIndex).innerHTML = dimTypeInfo[selectType].dimTypeCode;
                        $("_dimTypeDesc" + template + rowIndex).innerHTML = dimTypeInfo[selectType].dimTypeDesc == null ? "&nbsp;" : dimTypeInfo[selectType].dimTypeDesc;
                        $("_itemCodeName" + template + rowIndex).value = "";
                    } else {
                        $("_dimTypeCode" + template + rowIndex).innerHTML = "&nbsp;";
                        $("_dimTypeDesc" + template + rowIndex).innerHTML = "&nbsp;";
                    }
                    delete selectDimType[template][select._selectType];
                    selectDimType[template][selectType] = selectType;
                    select._selectType = selectType;
                }
                var selectId = $("_currId" + template + rowIndex).value;
                var parId = $("_parId" + template + rowIndex).value;
                if (select._selectType && parId) {//原先有选择项。
                    var selectMode = "insert";//删除模式
                    var selectCodeData = insertData[select._selectType] && insertData[select._selectType][parId]
                    if (!selectCodeData) {
                        selectCodeData = updateData[select._selectType] && updateData[select._selectType][selectId];
                        selectMode = "update";
                    }
                    dhx.confirm("更换归并类型时，其对应选择的编码将被取消，是否继续？", function (rs) {
                        if (rs) {
                            deleteSelectCode(selectCodeData, selectMode);
                            $("_currId" + template + rowIndex).value = "";
                            $("_parId" + template + rowIndex).value = "";
                            changeSelect();
                        } else {
                            dwr.util.setValue(select, select._selectType);
                        }
                    });
                } else {
                    changeSelect();
                }
            }
            td.appendChild(select);
        }
        //编码描述列
        td = tr.insertCell(2);
        td.align = "center";
        td.innerHTML = "<span id=_dimTypeDesc" + template + rowIndex + ">" +
            ((rowData && rowData.dimTypeDesc) == 'null' ? "&nbsp;" : (rowData && rowData.dimTypeDesc) || "&nbsp;") + "</span>";
        //父级节点。
        td = tr.insertCell(3);
        td.align = "center";
        if (!canEdit) {//如果有父级ID和父及名称，以label显示，不可以编辑。
            td.innerHTML = "<span id='_itemCode" + template + rowIndex + "'>" + rowData.itemCodeName + "</span>";
            //为TR打上标记，标识此TR为初始化，不可以编辑，不可以操作
            tr._isEdit = false;
        } else {
            var par = dhx.html.create("input", {type:"hidden", name:"parId", id:"_parId" + template + rowIndex});
            td.appendChild(par);
            var currId = dhx.html.create("input", {type:"hidden", name:"currId", id:"_currId" + template + rowIndex});
            td.appendChild(currId);
            var input = dhx.html.create("input",
                {className:"dhxlist_txt_textarea", name:"itemCodeName", readOnly:true, id:"_itemCodeName" + template +
                    rowIndex, type:"TEXT", style:"width: 90%;", onclick:"javascript:selectLevel(this)"});
            td.appendChild(input);
            if (selectTreeData) {
                par.value = selectTreeData[parIdColumnName];
                currId.value = selectTreeData[idCloumnName];
                input.value = selectTreeData.itemCodeName;
            }
            input.onclick = function () {
                var dimType = dwr.util.getValue($("_dimTypeId" + template + rowIndex));
                if (!dimType) {
                    dhx.alert("请您先选择一个归并类型！");
                    return;
                }
                //当值改变时默认为对应的归并类型新增了一个节点，进行这里的重点是在进行数据的更新。主要逻辑如下：
                //1、选中时要节点上要记录原来选择的父节点，便于再次选择时重新赋值。
                //2、伪造一个一行数据，关键字段是归并类型，父节点ID，编码项，编码值
                //3、判断是否还有归并类型未选择，有则新增一行数据（这里的逻辑应注意是第一次选中时作此判断）。
                //4、进行数据更新，包括新增节点数据,节点映射数据codeMapping,完整树形数据completeTreeData
                treeOnclick = treeOnclick || function (parId) {
                    //子编码数据
                    var childCodeData = treeDataGrid.getRowData(treeDataGrid.getSelectedRowId()).userdata;
                    var dimTypeName = dwr.util.getText("_dimTypeId" + template + rowIndex);
                    //父编码数据
                    var parCodeData = treeObject.tree.getUserData(parId, "rowData");
                    parCode = "0";
                    if (parseInt(dimTypeInfo[dimType].maxLevel) < parseInt(parCodeData["DIM_LEVEL"]) + 1) {
                        dwr.util.setValue($("_itemCodeName" + template + rowIndex), "");
                        dhx.alert("超越了" + dimTypeName + "规定的最大层级：" + dimTypeInfo[dimType].maxLevel + "，不能作为其子节点！");

                        return false;
                    }
                    var newData = dhx.extend({}, childCodeData);
                    newData[parIdColumnName] = parId == -1 ? 0 : parId;
//                    newData["OPER_FLAG"] = 'merge';//代表新增
                    newData["DIM_TYPE_ID"] = dimType;
                    newData["ORDER_ID"] = 0;//初始化ORDER_ID==0;
                    newData["STATE"] = 1;
                    newData["ISNEW"] = 1;//新增代码申请标识位。
                    newData["DIM_LEVEL"] = (parseInt(parCodeData["DIM_LEVEL"]) || 0) + 1;
                    newData["DIMTYPE_CNT"] = (parseInt(newData["DIMTYPE_CNT"]) || 0) + 1;//归并数量+1；
                    newData["CHILDREN"] = 0;
                    newData["ITEM_PAR_CODE"] = parCodeData == null ? parCode : (parCodeData[dimInfo.tableDimPrefix + "_CODE"]);
                    dealModFlag(newData, "merge");
                    newData.itemCodeName = input.value;
                    var codeKey = childCodeData[codeColumnName];
                    if (input._orgSelect) {//已经选择了一次，这次是更改。
                        for (var key in insertData[dimType][input._orgSelect]) {
                            if (insertData[dimType][input._orgSelect][key][(dimInfo.tableDimPrefix + "_CODE")
                                .toUpperCase()] == input._orgSelectCode) {
                                insertData[dimType][input._orgSelect][key] = null;
                                delete[input._orgSelect][key];
                            }
                        }
                    } else {//初次选择。
                        input._orgSelect = parId;
                        input._orgSelectCode = codeKey;
                        codeMapping[codeKey].push(dimType);
                        codeModifyMapping[codeKey] = codeModifyMapping[codeKey] || [];
                        codeModifyMapping[codeKey].push(newData);
                    }
                    if (childCodeData["DIMTYPE_CNT"] == 0) {//如果其归并类型==0，算作节点移动，不能算作新增。
                        dealModFlag(newData, "move");
//                        newData["OPER_FLAG"] = 'move';//代表节点移动
                        updateData[dimType] = updateData[dimType] || {};
                        //移动数据treegrid跟着移动
                        var moveId = treeDataGrid.getSelectedRowId();
                        treeDataGrid.deleteRow(moveId);
                        var tempConverter = dhx.extend(new dhtmlxTreeGridDataConverter(), treeGridConverter, true);
                        tempConverter.isDycload = true;
                        tempConverter.onBeforeConverted = function () {
                        };
                        treeDataGrid.parse(tempConverter.convert(newData), "json");
                        updateData[dimType][moveId] = newData;
                        treeDataGrid.setItemImage(parId, "folderOpen.gif");
                        $("_currId" + template + rowIndex).value = moveId;
                    } else {//新增数据
                        newData[idCloumnName] = 0 - parseInt(dhx.uid());
                        insertData[dimType] = insertData[dimType] || {};
                        insertData[dimType][parId] = insertData[dimType][parId] || {};
                        insertData[dimType][parId][newData[idCloumnName]] = newData;
                        $("_currId" + template + rowIndex).value = treeDataGrid.getSelectedRowId();
                    }
                    if (codeMapping[childCodeData[codeColumnName]].length <
                        dimTypeInfo.length) {
                        addDimColumnRow(template, null);
                    }
                    $("_cancel" + template + rowIndex)
                        .innerHTML = '<img src="../../../resource/images/cancel.png" alt="删除" onclick="removeSelectCode(this,\'' +
                        template + '\',\'' + rowIndex + '\')"' +
                        ' style="width:16px;height: 16px;cursor: pointer">';
                    return true;
                }
                showDimComboTree(input, par, dimType, treeOnclick,
                    selectTreeData && selectTreeData[idCloumnName]);
            }
        }
        //操作列
        td = tr.insertCell(4);
        td.id = "_cancel" + template + rowIndex;
        td.align = "center";
        if (cancancel) {
            td.innerHTML = '<img src="../../../resource/images/cancel.png" alt="删除" onclick="removeSelectCode(this,\'' +
                template + '\',\'' + rowIndex + '\')"' + ' style="width:16px;height: 16px;cursor: pointer">';
        } else {
            td.innerHTML = "&nbsp;";
        }
    }
}
/**
 * 删除已经选择的归并
 * @param img
 * @param template
 * @param rowIndex
 */
var removeSelectCode = function (img, template, rowIndex) {
    dhx.confirm("进行此操作会将你已经为此编码添加的子节点一并取消，是否继续？", function (rs) {
        if (rs) {
            var selectParId = $("_parId" + template + rowIndex).value;
            var selectId = $("_currId" + template + rowIndex).value;
            var selectType = $("_dimTypeId" + template + rowIndex)._selectType;
            var selectMode = "insert";//删除模式
            var selectCodeData = insertData[selectType] && insertData[selectType][selectParId]
            if (!selectCodeData) {
                selectCodeData = updateData[selectType] && updateData[selectType][selectId];
                selectMode = "update";
            }
            $("_itemCodeName" + template + rowIndex) && $(("_itemCodeName" + template + rowIndex).value = "")
            deleteSelectCode(selectCodeData, selectMode);
            delete selectDimType[template][selectType]
            var tbObj = img.parentNode.parentNode.parentNode.parentNode;
            var option = new Option(dimTypeInfo[selectType].dimTypeName, selectType);
            img.parentNode.parentNode.parentNode.removeChild(img.parentNode.parentNode);
            for (var i = 1; i < tbObj.rows.length; i++) {
                var tr = tbObj.rows[i];
                var loopIndex = tr._index;
                if (tr._isEdit) {
                    $("_dimTypeId" + template + loopIndex).options[$("_dimTypeId" + template + loopIndex).options.length] = option;
                }
            }
        }
    });
}

/**
 * 删除已经选择了的编码
 * @param selectCodeData
 * @param selectMode 选择模式
 */
var deleteSelectCode = function (selectCodeDatas, selectMode) {
    var deleteCode = function (selectCodeData) {
        if (selectCodeData) {
            var id = selectCodeData[idCloumnName];
            var parId = selectCodeData[parIdColumnName];
            var code = selectCodeData[codeColumnName];
            if (selectCodeData.DIMTYPE_CNT == 1) {//如果归并类型为1,为移动，非新增
                updateData[selectCodeData.DIM_TYPE_ID][id] = null;
                delete updateData[selectCodeData.DIM_TYPE_ID][id];
                //如果当前的树有此节点，进行节点移动。移动到根节点
                if (treeDataGrid.doesRowExist(id)) {
                    var x = treeDataGrid._h2.get[0];
                    var nextRootId = x.childs[x.childs.length - 1].id;
                    var data = backUpGridData(id);
                    if (treeDataGrid.getSubItems(parId).split(",").length == 1) {
                        treeDataGrid.setItemImage(parId, "leaf.gif");
                    }
                    treeDataGrid.moveRowTo(id, nextRootId, "move", "sibling");
                    restoreGridData(id, data);
                }
            } else {
                //删除insertData中的数据缓存。
                parId = parId || -1;//如果parId==0.新增的数据中记录其parId==-1
                if (insertData[selectCodeData.DIM_TYPE_ID]
                    && insertData[selectCodeData.DIM_TYPE_ID][parId] && insertData[selectCodeData.DIM_TYPE_ID][parId][id]) {
                    insertData[selectCodeData.DIM_TYPE_ID][parId][id] = null;
                    delete insertData[selectCodeData.DIM_TYPE_ID][parId][id];
                }
                ;
            }
            for (var i = 0; i < codeMapping[code].length; i++) {
                if (codeMapping[code][i] == selectCodeData.DIM_TYPE_ID) {
                    codeMapping[code].splice(i--, 1);
                }
            }
            if (codeModifyMapping[code]) {
                for (var i = 0; i < codeModifyMapping[code].length; i++) {
                    if (codeModifyMapping[code][i].DIM_TYPE_ID == selectCodeData.DIM_TYPE_ID) {
                        codeModifyMapping[code].splice(i--, 1);
                    }
                }
            }
        }
    }
    if (selectMode == "insert") {//递归删除其下属子节点
        var nextscan = selectCodeDatas;
        while (!Tools.isEmptyObject(nextscan)) {
            var tempScan = {};
            for (var key in nextscan) {
                var selectCode = nextscan[key];
                var dimType = selectCode.DIM_TYPE_ID;
                //查询是否还有子节点。
                if (insertData[dimType][selectCode[idCloumnName]]) {
                    dhx.extend(tempScan,
                        insertData[dimType][selectCode[idCloumnName]]);
                }
                deleteCode(selectCode);
            }
            nextscan = tempScan;
        }
    } else {
        deleteCode(selectCodeDatas);
    }
}

/**
 * 拖动节点排序处理
 * @param {Object} tId  父级ID
 */
var orderByOfTree = function (tId) {
    var childCount = treeDataGrid.hasChildren(tId);
    if (childCount > 0) {
        for (var i = 0; i < childCount; i++) {
            //获取父节点下的所有子节点
            var childId = parseInt(treeDataGrid.getChildItemIdByIndex(tId, i));
            if (treeDataGrid.getRowData(childId).userdata["ORDER_ID"] != (i + 1)) {
                orderData[childId] = i + 1;
            } else {
                delete  orderData[childId];
            }
        }
    }
}

/**
 * 验证动态字段字段类型
 * @param {Object} dataType
 */
var validateDataType = function (value, dataType, colSize, colPrec) {
    //如果是NUMBER，验证其只能输入数字（包括小数）
    if (dataType.toString().search("NUMBER") > -1) {
        var regNumber = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/;
        if (!regNumber.exec(value)) {
            return "只能添加数字！"
        }
        //number有精度时。
        if (dataType.toString().indexOf("(") != -1) {
            //输入的值有小数点时，验证小数点前后长度
            if (value.indexOf(".") != -1) {
                if (dataType.toString().indexOf(",") != -1) {
                    if (value.split(".")[0].toString().length > colSize) {
                        return "长度应小于最大精度" + colSize + "！";
                    }
                    if (value.split(".")[1].toString().length > colPrec) {
                        return "长度应小于最大精度" + colPrec + "！";
                    }
                }
            } else {    //不输入小数点只验证colSize
                if (value.split(".")[0].toString().length > colSize) {
                    return "长度应小于最大精度" + colSize + "！";
                }
            }
        }
        else {
            if (value.indexOf(".") != -1) {
                return "该类型只能输入整数！"
            }
        }
    } else if (dataType.toString().search("DATE") > -1) {
        var regDate = /^(\d{4})-(\d{2})-(\d{2})\s(\d{2}):(\d{2}):(\d{2})$/;
        if (!regDate.exec(value)) {
            var date = new Date();
            return "时间格式错误！格式为：YYYY-MM-DD HH:MM:SS";
        } else {
            return true
        }
    }
    return true;
}
dhx.ready(mergeInit);