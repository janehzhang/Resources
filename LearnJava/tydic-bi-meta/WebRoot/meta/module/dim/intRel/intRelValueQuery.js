/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       intRelValueQuery.js
 *Description：
 *        维度接口值查询
 *Dependent：
 *         dhtmlx.js、dwr相关JS文件、dhtmlxExtend.js、tool。js等。
 *Author:
 *        张伟
 *Finished：
 *       2011-11-19
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
var dwrCaller = new biDwrCaller();
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
dwrCaller.addAutoAction("queryDimType", TableApplyAction.queryDimType, tableId, {
    dwrConfig:true,
    converter:new dhtmlxComboDataConverter({
        valueColumn:"dimTypeId",
        textColumn:"dimTypeName"
    }),
    async:false
})
dwrCaller.addAutoAction("queryDimTablesInfoById", TableDimAction.queryDimTablesInfoById, tableId,
    {
        dwrConfig:true,
        converter:new columnNameConverter(),
        callback:function (data) {
            dimInfo = data;

            dimInfo.tableOwner = tableOwner;
        },
        async:false
    }
)
/**
 * 查询系统信息Action定义
 */
dwrCaller.addAutoAction("queryAllSystem", DimIntRelAction.queryAllSystem, {
    dwrConfig:true,
    converter:new dhtmlxComboDataConverter({
        valueColumn:"groupId",
        textColumn:"groupName"
    }),
    async:false
})
var treeGridConverterConfig = {
    isDycload:true,
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
        if (columnName == "_buttons") {//如果是第五列。即操作按钮列
            return "editButton";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    },
    userData:function (rowIndex, rowData) {
        return rowData;
    }
}
var treeGridConverter = new dhtmlxTreeGridDataConverter(treeGridConverterConfig);

var gridConverter = new dhtmxGridDataConverter({
    isFormatColumn:false,
    cellDataFormat:function (rowIndex, columnIndex, columnName, cellValue, rowData) {
        if (columnName == "_buttons") {//如果是第五列。即操作按钮列
            return "editButton";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
})
dwrCaller.addAutoAction("queryIntRelValueInfo", DimIntRelAction.queryIntRelValueInfo, {
    dwrConfig:true,
    converter:gridConverter,
    isDealOneParam:false
})

dwrCaller.addAutoAction("queryAllIntRelValue", DimIntRelAction.queryAllIntRelValue, {
    dwrConfig:true,
    converter:treeGridConverter
})
var intRelValueQueryInit = function () {
    if (tableOwner == "null") {
        tableOwner = parent.tableUser;
    }
    dwrCaller.executeAction("queryDimTablesInfoById");
    treeGridConverter.setIdColumn((dimInfo.tableDimPrefix + "_ID").toUpperCase());
    treeGridConverter.setPidColumn((dimInfo.tableDimPrefix + "_PAR_ID").toUpperCase());
    treeGridConverter.setFilterColumns([(dimInfo.tableDimPrefix + "_NAME").toUpperCase(),
        (dimInfo.tableDimPrefix + "_CODE").toUpperCase(), "DIM_LEVEL", "ISMAPPING", (dimInfo.tableDimPrefix + "_DESC").toUpperCase(),
        "SRC_CODE", "SRC_NAME"]);
    gridConverter.setFilterColumns([(dimInfo.tableDimPrefix + "_NAME").toUpperCase(),
        (dimInfo.tableDimPrefix + "_CODE").toUpperCase(), "DIM_LEVEL", "ISMAPPING", (dimInfo.tableDimPrefix + "_DESC").toUpperCase(),
        "SRC_CODE", "SRC_NAME"]);
    treeGridConverter.isOpen = function () {
        return false;//默认所有均不展开
    }
    //展开第一级
//    treeGridConverter.afterCoverted = function (data) {
//        var mappingStatus = queryform.getItemValue("mappingStatus");
//        if (data) {
//            for (var i = 0; i < data.length; i++) {
//                if (mappingStatus == 1 && data[i]._pid_temp_ != 0) {
//                    data.splice(i--, 1);
//                } else {
//                    data[i].open = data[i].rows ? true : false;
//                }
//            }
//        }
//        var replaceData = [];
//        //如果末级不显示且查询状态为所有，递归树，最后一级隐藏，且关联关系设置在其父级上。
//        if (mappingStatus == 1 || mappingStatus == 2) {
//            var scanTree = function (parentNode, node) {
//                if (node.rows && node.rows.length > 0) {//非末级，继续遍历
//                    for (var i = 0; i < node.rows.length; i++) {
//                        arguments.callee.call(this, node, node.rows[i]);
//                    }
//                    if (!dimInfo.lastLevelFlag && node.last) {
//                        node.data[3] = node.data[3] > 0 ? node.data[3] : 0;
//                        node.data[0].image = "leaf.gif";
//                        node.rows = null;
//                        delete node.rows;
//                    }
//                } else {//是末级。移除末级，设置其父节点SRC_NAME,SRC_CODE.
//                    if (!dimInfo.lastLevelFlag) {
//                        var rowData = node.userdata;
//                        if (rowData.ISMAPPING == 1) {//已映射。
//                            if (parentNode) {
//                                parentNode.data[3] = 1;
//                                parentNode.data[5] = parentNode.data[5] || "";
//                                parentNode.data[5] += (parentNode.data[5] ? "," : "") + node.userdata.SRC_CODE;
//                                parentNode.data[6] = parentNode.data[6] || "";
//                                parentNode.data[6] += (parentNode.data[6] ? "," : "") + node.userdata.SRC_NAME;
//                            }
//                        } else {
//                            if (rowData.MOD_FLAG == 1) {
//                                node.last = true;
//                            }
//                        }
//                        //无该属性，界面报错。
//                        //parentNode.last=true;
//                    } else {
//                        node.last = true;
//                    }
//                }
//                if (mappingStatus == 2) {
//                    if (node.data[3] == 0 && node.last) {//将所有未映射的
//                        replaceData.push(node);
//                    }
//                }
//                if (!node.last) {
//                    node.data[5] = "--";
//                    node.data[6] = "--";
//                }
//            }
//            for (var j = 0; j < data.length; j++) {
//                scanTree(null, data[j]);
//            }
//        }
//        data = mappingStatus == 2 ? replaceData : data;
//        return data;
//    };
    //根据ORDER_ID进行排序
    treeGridConverter.compare
        = function (data1, data2) {
        if (data1.userdata.ORDER_ID == undefined || data1.userdata.ORDER_ID == null) {
            return false;
        }
        if (data2.userdata.ORDER_ID == undefined || data2.userdata.ORDER_ID == null) {
            return false;
        }
        return data1.userdata.ORDER_ID <= data2.userdata.ORDER_ID
    }
    var intRelLayout = new dhtmlXLayoutObject(document.body, "2E");
    intRelLayout.cells("a").setText("<span style='font-weight: normal;'>查询编码接口表：</span>" + tableNameCn);
    intRelLayout.cells("b").hideHeader();
    intRelLayout.cells("a").setHeight(70);
    intRelLayout.cells("a").fixSize(false, true);
//    intRelLayout.hideConcentrate();
//    intRelLayout.hideSpliter();//移除分界边框。
    var formConfig = [
        {type:"settings", position:"label-left", inputWidth:120},
        {type:"select", label:"归并类型：", name:"dimTypeId", offsetLeft:20},
        {type:"newcolumn"},
        {type:"select", label:"映射状态：", name:"mappingStatus", options:[
            {text:"未映射", value:2},
            {text:"未审核", value:3},
            {text:"已映射", value:4},
            {text:"所有", value:1}
        ]},
        {type:"newcolumn"},
        {type:"input", label:"编码值：", name:"keyWord"},
        {type:"newcolumn"}
    ];
    window.labelTemplate = function (value) {
        return "<span style='color: #000000;font-size: 12px;font-family: '宋体'>"
            + sysName + "</span>"
    }
    //判断是否有系统值
    if (sysId) {
        formConfig.push({type:"template", label:"源系统：", name:"system", value:sysId, format:labelTemplate});
        formConfig.push({type:"newcolumn"});
    } else {//产生下拉框
        formConfig.push({type:"select", label:"源系统：", name:"system"});
        formConfig.push({type:"newcolumn"});
    }
    formConfig.push({type:"button", name:"query", value:"查询"});
    //加载查询表单
    queryform = intRelLayout.cells("a").attachForm(formConfig);
    //加载编码类型。
    dwrCaller.executeAction("queryDimType", function (data) {
        Tools.addOption(queryform.getSelect("dimTypeId"), data);
    })
    //加载应用系统
    if (!sysId) {
        dwrCaller.executeAction("queryAllSystem", function (data) {
            Tools.addOption(queryform.getSelect("system"), data);
        })
    }
    var mappingParam = new biDwrMethodParam();
    mappingParam.setParamConfig([
        {
            index:1, type:"fun", value:function () { //索引0位的参数来源由表单提供。
            var formData = queryform.getFormData();
            formData.keyWord = Tools.trim(queryform.getInput("keyWord").value);
            return formData;
        }},
        {
            index:0, type:"static", value:dimInfo
        }
    ]);
    var query = function () {
        var imagePath = dhtmlx.image_path + "csh_" + dhtmlx.skin + "/";
        var mappingStatus = queryform.getItemValue("mappingStatus");
        intRelGrid = intRelLayout.cells("b").attachGrid();
        intRelGrid.setHeader("元数据,#cspan,#cspan,#cspan,#cspan,源系统,#cspan"
            , null, ["text-align:center;", "text-align:center"]);
        intRelGrid.attachHeader("编码名称,编码项,层级,映射状态,描述,接口ID值,接口名称"
            , ["text-align:center;", "text-align:center;", "text-align:center", "text-align:center;", "text-align:center;",
                "text-align:center;", "text-align:center;"]);
        intRelGrid.setInitWidthsP("20,10,10,10,30,10,10");
        intRelGrid.setColAlign("left,center,center,center,left,center,left");
        if (mappingStatus == 1) {
            intRelGrid.setColTypes("tree,ro,ro,ro,ro,ro,ro");
        } else {
            intRelGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro");
        }
        intRelGrid.setColSorting("na,na,na,na,na,na,na");
        intRelGrid.enableResizing("true,true,true,true,true,true,true");
        intRelGrid.setEditable(false);
        intRelGrid.setColumnCustFormat(3, function (value) {
            switch (value) {
                case -1:
                    return "--";
                case 0:
                    return "未映射";
                case 1:
                    return "已映射";
                default:
                    return value;
            }
        });
        intRelGrid.enableCtrlC();
        intRelGrid.init();
        if (mappingStatus != 1) {
            intRelGrid.defaultPaging(10);
        }
        intRelGrid.clearAll();
        if (mappingStatus == 1) {
            intRelGrid.setImagePath(imagePath);
            intRelGrid.load(dwrCaller.queryAllIntRelValue + mappingParam, "json");
            intRelGrid.kidsXmlFile=dwrCaller.queryAllIntRelValue + mappingParam;
        } else {
            intRelGrid.load(dwrCaller.queryIntRelValueInfo + mappingParam, "json");
        }
    }
    query();
    queryform.attachEvent("onButtonClick", function (id) {
        if (id == "query") {
            query();
        }
    });
    queryform.getInput("keyWord").onkeypress = function (e) {
        e = e || window.event;
        var keyCode = e.keyCode;
        if (keyCode == 13) {
            query();
        }
    }
}
dhx.ready(intRelValueQueryInit);