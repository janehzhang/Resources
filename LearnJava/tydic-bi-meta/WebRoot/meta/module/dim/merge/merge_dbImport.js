/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *
 *Description：  导入数据库中对应横，纵表的维表数据
 *
 *Dependent：
 *
 *Author:
 *        刘斌
 *Finished：
 *       2012-05-07
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/

dwrCaller.addAutoAction("queryDbTables", "TableApplyAction.queryDbTables");
dwrCaller.addDataConverter("queryDbTables", new dhtmxGridDataConverter({
    idColumnName:"tableName",
    filterColumns:["", "tableName", "tableBusComment"],
    userData:function (rowIndex, rowData) {
        rowData.dataSourceID = dwr.util.getValue("_tableDataSource");
        return rowData;
    }
}));
// 定义owner变量
var owner = "";
// 从数据库表导入信息的用户转换器
var ownerConverter = new dhtmlxComboDataConverter({
    valueColumn:"username",
    textColumn:"username",
    userData:function (rowData) {
        if (owner == "") {
            if (rowData.defaultName) {
                owner = rowData.defaultName;
            }
        }
        return rowData;
    }
})
/**
 * 查询用户信息Action定义
 */
dwrCaller.addAutoAction("queryDbUsers", "TableApplyAction.getUserNameByDataSourceId");
dwrCaller.addDataConverter("queryDbUsers", ownerConverter);
dwrCaller.isShowProcess("queryDbUsers", false);

//表类数据源相关Action
var tableDataSourceConverter = new dhtmlxComboDataConverter({
    valueColumn:"dataSourceId",
    textColumn:"dataSourceName"
})
dwrCaller.addAutoAction("queryTableDataSource", "TableApplyAction.queryTableDataSource");
dwrCaller.addDataConverter("queryTableDataSource", tableDataSourceConverter);

var importWindow = null;
var dbTableName = null;
var dbDataSourceId = null;
var dbTableOwner = null;

//导入维表数据初始化
var dBImport = function () {
    if (!importWindow) {
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("importWindow", 0, 0, 540, 430);
        importWindow = dhxWindow.window("importWindow");
        importWindow.setModal(true);
        importWindow.stick();
        importWindow.setDimension(540, 430);
        importWindow.center();
        importWindow.denyResize();
        importWindow.denyPark();
        importWindow.button("minmax1").hide();
        importWindow.button("park").hide();
        importWindow.button("stick").hide();
        importWindow.button("sticked").hide();
        importWindow.setModal(true);
        importWindow.keepInViewport(true);
        importWindow.setText("导入数据库维表数据");
        importWindow.attachEvent("onClose", function () {
            importWindow.hide();
            importWindow.setModal(false);
            return false;
        });
        importWindow.attachEvent("onHide", function(win){
            // code here
            if(dymaicColSetWindow){
                dymaicColSetWindow.close();
            }
            return true;
        });
        importWindow.show();
        var importLayout = new dhtmlXLayoutObject(importWindow, "2E");
        importLayout.cells("a").hideHeader();
        importLayout.cells("a").setHeight(200);
        importLayout.cells("a").fixSize(false, true);
        importLayout.cells("a").attachObject("_importFromDb");
        importLayout.cells("b").hideHeader();
        importLayout.cells("b").fixSize(true, true);
        var queryTableGrid = null;
        //参数收集，参数来源于查询表单_queryDimForm
        var param = new biDwrMethodParam([
            {
                index:0, type:"ui", value:"_queryTablesFromDb"
            }
        ]);
        var userInit = false;//用户数据是否初始化完成。
        //查询。
        var queryTables = function () {
            if (userInit) {
                queryTableGrid.clearAll();
                queryTableGrid.load(dwrCaller.queryDbTables + param, "json");
                dhtmlxValidation.clearAllTip();
            } else {
                setTimeout(arguments.callee, 50);
            }
        }
        //查询用户信息
        var queryUsers = function (dataSourceId) {
            dwrCaller.executeAction("queryDbUsers", dataSourceId, function (data) {
                userInit = true;
                $("_owner").innerHTML = "";
                if (data) {
                    if (!(data.options.length == 1 && !data.options[0].text)) {
                        Tools.addOption("_owner", data);
                        var temp = document.getElementById("_owner");
                        for (var i = 0; i < temp.options.length; i++) {
                            if (temp.options[i].text == owner) {
                                document.getElementById("_owner").options[i].selected = true;
                                owner = "";
                                break;
                            }
                        }
                    }
                }
            });
        }

        //添加查询按钮，
        var queryButton = Tools.getButtonNode("查询");
        queryButton.onclick = queryTables;
        $("_tableKeyWordFromDb").onkeypress = function (e) {
            if ((e || window.event).keyCode == 13) {
                queryTables();
            }
        }
        $("_queryTableButtonFromDb").appendChild(queryButton);
        //数据源数据装载
        dwrCaller.executeAction("queryTableDataSource", function (data) {
            Tools.addOption("_tableDataSource", data);
            queryUsers(dwr.util.getValue($("_tableDataSource")));
            dhx.env.isIE && CollectGarbage();
        });

        $("_tableDataSource").onchange = function () {
            queryUsers(dwr.util.getValue($("_tableDataSource")));
        }
        queryTableGrid = new dhtmlXGridObject("_queryTableGridFromDb");
        queryTableGrid.setHeader(",表名,注释");
        queryTableGrid.setInitWidthsP("6,45,49");
        queryTableGrid.setColAlign("center,left,left");
        queryTableGrid.setHeaderAlign("center,center,center");
        queryTableGrid.setColTypes("ra,ro,ro");
        queryTableGrid.setColSorting("na,na,na");
        queryTableGrid.setEditable(true);
        queryTableGrid.enableMultiselect(false);
        queryTableGrid.defaultPaging(5);
        queryTables();
        queryTableGrid.enableCtrlC();
        queryTableGrid.init();
        queryTableGrid.attachEvent("onRowSelect", function (id, ind) {
            dbTableName = queryTableGrid.getRowData(id).data[1];
            queryTableGrid.cells(id, 0).setValue(1);
            clearRows();
            addTypeRow();
            initVerFun();
            clearDyRowData(true);
        });
        queryTableGrid.attachEvent("onCheck", function (id, cInd, state) {
            queryTableGrid.selectRowById(id);
            dbTableName = queryTableGrid.getRowData(id).data[1];
            queryTableGrid.cells(id, 0).setValue(1);
            clearRows();
            addTypeRow();
            initVerFun();
            clearDyRowData(true);
        });


        importLayout.cells("b").attachObject($("_nodeDetailInfo"));
        dhx.html.addCss($("_typeTable"), global.css.gridTopDiv);
        dhx.html.addCss($("_verTable"), global.css.gridTopDiv);
        var ok = Tools.getButtonNode("确定");
        ok.style.marginLeft = "190px";
        ok.style.marginTop = "5px";
        ok.style.cssFloat = "left";
        ok.style.styleFloat = "left";
        ok.onclick = function () {
            if (!dbTableName) {
                dhx.alert("请先选择一个维度表");
                return;
            }
            if(!checkDyCol()){
                return;
            }
            //提交方法
            var duSub = function(){
                if (validateType()) {
                var rowDatas = getTypeRowData();//list
                var isImport = true;
                if (dimTypeInfo && dimTypeInfo[currentDimType] && dimTypeInfo[currentDimType].maxLevel
                    && dimTypeInfo[currentDimType].maxLevel < rowDatas.length) {
                    dhx.confirm("导入数据的维层级数" + rowDatas.length + "大于当前维度归并类型的最大维层级数" + dimTypeInfo[currentDimType].maxLevel + "，确定要导入吗？", function (r) {
                        isImport = r;
                        if (isImport) {
                            var confData = {
                                dimType:currentDimType,
                                tableId:tableId,
                                dataSourceId:dbDataSourceId,
                                tableOwner:dbTableOwner,
                                tableName:dbTableName,
                                tableType:$("tableType").value,
                                dataType:$("dataType").value,
                                codeType:$("codeType").value,
                                dyColData:dyColData
                            };//map

                            var diff = {};
                            for (var i = 0; i < rowDatas.length; i++) {
                                if (rowDatas[i].codeCol) {
                                    if (diff[rowDatas[i].codeCol]) {
                                        dhx.alert("列" + rowDatas[i].codeCol + "重复，不能导入！");
                                        return;
                                    } else {
                                        diff[rowDatas[i].codeCol] = 1;
                                    }
                                }
                                if (rowDatas[i].nameCol) {
                                    if (diff[rowDatas[i].nameCol]) {
                                        dhx.alert("列" + rowDatas[i].nameCol + "重复，不能导入！");
                                        return;
                                    } else {
                                        diff[rowDatas[i].nameCol] = 1;
                                    }
                                }
                                if (rowDatas[i].descCol) {
                                    if (diff[rowDatas[i].descCol]) {
                                        dhx.alert("列" + rowDatas[i].descCol + "重复，不能导入！");
                                        return;
                                    } else {
                                        diff[rowDatas[i].descCol] = 1;
                                    }
                                }
                            }
                            for (var i = 0; i < dyColData.length; i++) {
                                if (dyColData[i].dyColDb) {
                                    if (diff[dyColData[i].dyColDb]) {
                                        dhx.alert("动态字段" + dyColData[i].dyColDb + "重复，不能导入！");
                                        return;
                                    } else {
                                        diff[dyColData[i].dyColDb] = 1;
                                    }
                                }
                            }

                            DBImportAction.haveDupCodeOfSoucre(rowDatas, confData, function (r) {
                                if (!r) {
                                    if ($("dataType").value == 2) {//清空数据
                                        dhx.closeProgress(null,0);
                                        dhx.showProgress("正在导入...", "正在导入维度表数据，请稍后。");
                                        DBImportAction.importDimInfo(rowDatas, confData, function (msg) {
                                            if (msg) {
                                                dhx.alert(msg);
                                            } else {
                                                dhx.alert("导入成功！", function () {
                                                    self.location.reload();
                                                });
                                                importWindow.close();
                                            }
                                            dhx.closeProgress();
                                        })
                                    } else {//保留数据
                                        DBImportAction.haveDupCode(rowDatas, confData, function (r) {
                                            if (!r) {//不存在重复的编码
                                                dhx.closeProgress(null,0);
                                                dhx.showProgress("正在导入...", "正在导入维度表数据，请稍后。");
                                                DBImportAction.importDimInfo(rowDatas, confData, function (msg) {
                                                    if (msg) {
                                                        dhx.alert(msg);
                                                    } else {
                                                        dhx.alert("导入成功！", function () {
                                                            self.location.reload();
                                                        });
                                                        importWindow.close();
                                                    }
                                                    dhx.closeProgress();
                                                })
                                            } else {
                                                dhx.alert("选择的横表与当前维度表有重复编码，不能导入！", function () {
                                                    showDupCode(rowDatas, confData);
                                                });
                                            }

                                        })
                                    }
                                } else {
                                    dhx.alert("查询结果有重复编码，不能导入！", function () {
                                        showDupCodeSource(rowDatas, confData);
                                    });
                                }
                            })


                        }
                    });
                } else {
                    if (isImport) {
                        var confData = {
                            dimType:currentDimType,
                            tableId:tableId,
                            dataSourceId:dbDataSourceId,
                            tableOwner:dbTableOwner,
                            tableName:dbTableName,
                            tableType:$("tableType").value,
                            dataType:$("dataType").value,
                            codeType:$("codeType").value,
                            dyColData:dyColData
                        };//map

                        var diff = {};
                        for (var i = 0; i < rowDatas.length; i++) {
                            if (rowDatas[i].codeCol) {
                                if (diff[rowDatas[i].codeCol]) {
                                    dhx.alert("列" + rowDatas[i].codeCol + "重复，不能导入！");
                                    return;
                                } else {
                                    diff[rowDatas[i].codeCol] = 1;
                                }
                            }
                            if (rowDatas[i].nameCol) {
                                if (diff[rowDatas[i].nameCol]) {
                                    dhx.alert("列" + rowDatas[i].nameCol + "重复，不能导入！");
                                    return;
                                } else {
                                    diff[rowDatas[i].nameCol] = 1;
                                }
                            }
                            if (rowDatas[i].descCol) {
                                if (diff[rowDatas[i].descCol]) {
                                    dhx.alert("列" + rowDatas[i].descCol + "重复，不能导入！");
                                    return;
                                } else {
                                    diff[rowDatas[i].descCol] = 1;
                                }
                            }
                        }
                        for (var i = 0; i < dyColData.length; i++) {
                            if (dyColData[i].dyColDb) {
                                if (diff[dyColData[i].dyColDb]) {
                                    dhx.alert("动态字段" + dyColData[i].dyColDb + "重复，不能导入！");
                                    return;
                                } else {
                                    diff[dyColData[i].dyColDb] = 1;
                                }
                            }
                        }

                        DBImportAction.haveDupCodeOfSoucre(rowDatas, confData, function (r) {
                            if (!r) {
                                if ($("dataType").value == 2) {//清空数据
                                    dhx.closeProgress(null,0);
                                    dhx.showProgress("正在导入...", "正在导入维度表数据，请稍后。");
                                    DBImportAction.importDimInfo(rowDatas, confData, function (msg) {
                                        if (msg) {
                                            dhx.alert(msg);
                                        } else {
                                            dhx.alert("导入成功！", function () {
                                                self.location.reload();
                                            });
                                            importWindow.close();
                                        }
                                        dhx.closeProgress();
                                    })
                                } else {//保留数据
                                    DBImportAction.haveDupCode(rowDatas, confData, function (r) {
                                        if (!r) {//不存在重复的编码
                                            dhx.closeProgress(null,0);
                                            dhx.showProgress("正在导入...", "正在导入维度表数据，请稍后。");
                                            DBImportAction.importDimInfo(rowDatas, confData, function (msg) {
                                                if (msg) {
                                                    dhx.alert(msg);
                                                } else {
                                                    dhx.alert("导入成功！", function () {
                                                        self.location.reload();
                                                    });
                                                    importWindow.close();
                                                }
                                                dhx.closeProgress();
                                            })
                                        } else {
                                            dhx.alert("选择的横表与当前维度表有重复编码，不能导入！", function () {
                                                showDupCode(rowDatas, confData);
                                            });
                                        }

                                    })
                                }
                            } else {
                                dhx.alert("查询结果有重复编码，不能导入！", function () {
                                    showDupCodeSource(rowDatas, confData);
                                });
                            }
                        })


                    }
                }


            }
            };
            var dyColStr = "";
            for (var i = 0; i < dymaicColData.length; i++) {
                if (dymaicColData[i].colNullabled == 0) {//如果动态字段不能为空，则必须判断是否设置该动态字段
                    for(var j=0; j<dyColData.length; j++){
                        if(dyColData[j].dyCol == dymaicColData[i].colName){
                            if(dyColData[j].dyColDb){
                                dyColStr = dyColStr + dyColData[j].dyColDb + ",";
                            }
                        }
                    }
                }
            }
            if(dyColStr == ""){
                dhx.showProgress("正在检查...", "正在检查数据合法性，请稍后。");
                duSub();
            }else{
                var confData = {
                    dimType:currentDimType,
                    tableId:tableId,
                    dataSourceId:dbDataSourceId,
                    tableOwner:dbTableOwner,
                    tableName:dbTableName,
                    tableType:$("tableType").value,
                    dataType:$("dataType").value,
                    codeType:$("codeType").value,
                    dyColData:dyColData
                };//map
                DBImportAction.haveNullValue(confData,dyColStr.substring(0,dyColStr.length-1),function(r){
                    if(r){
                        dhx.alert("源表中动态字段有空值，不能导入到当前表的非空动态字段中！");
                    }else{
                        dhx.showProgress("正在检查...", "正在检查数据合法性，请稍后。");
                        duSub();
                    }
                })
            }
//            doSub();
        };
        $("_typeWindowButton").appendChild(ok);
        var cancel = Tools.getButtonNode("关闭");
        cancel.style.styleFloat = "left";
        cancel.style.cssFloat = "left";
        cancel.style.marginTop = "5px";
        cancel.style.marginLeft = "20px";
        cancel.onclick = function () {
            importWindow.hide();
            importWindow.setModal(false);
        };
        $("_typeWindowButton").appendChild(cancel);

        dhtmlxValidation.addValidation($("_verContent"), [
            {target:"verCodeCol", rule:'NotEmpty'},
            {target:"verParCodeCol", rule:'NotEmpty'},
            {target:"verNameCol", rule:'NotEmpty'}
        ]);


        var ok1 = Tools.getButtonNode("确定");
        ok1.style.marginLeft = "190px";
        ok1.style.marginTop = "5px";
        ok1.style.cssFloat = "left";
        ok1.style.styleFloat = "left";
        ok1.onclick = function () {
            if(!checkDyCol()){
                return;
            }
            //提交操作
            var doSubVer = function(){
                if (validateVer()) {
                var rowDatas = getVerRowData();
                var confData = {
                    dimType:currentDimType,
                    tableId:tableId,
                    dataSourceId:dbDataSourceId,
                    tableOwner:dbTableOwner,
                    tableName:dbTableName,
                    tableType:$("tableType").value,
                    dataType:$("dataType").value,
                    codeType:$("codeType").value,
                    dyColData:dyColData
                };//map

                var diff = {};
                for (var i = 0; i < rowDatas.length; i++) {
                    if (rowDatas[i].verNameCol) {
                        if (diff[rowDatas[i].verNameCol]) {
                            dhx.alert("列" + rowDatas[i].verNameCol + "重复，不能导入！");
                            return;
                        } else {
                            diff[rowDatas[i].verNameCol] = 1;
                        }
                    }
                    if (rowDatas[i].verParCodeCol) {
                        if (diff[rowDatas[i].verParCodeCol]) {
                            dhx.alert("列" + rowDatas[i].verParCodeCol + "重复，不能导入！");
                            return;
                        } else {
                            diff[rowDatas[i].verParCodeCol] = 1;
                        }
                    }
                    if (rowDatas[i].verCodeCol) {
                        if (diff[rowDatas[i].verCodeCol]) {
                            dhx.alert("列" + rowDatas[i].verCodeCol + "重复，不能导入！");
                            return;
                        } else {
                            diff[rowDatas[i].verCodeCol] = 1;
                        }
                    }
                    if (rowDatas[i].verDescCol) {
                        if (diff[rowDatas[i].verDescCol]) {
                            dhx.alert("列" + rowDatas[i].verDescCol + "重复，不能导入！");
                            return;
                        } else {
                            diff[rowDatas[i].verDescCol] = 1;
                        }
                    }
                }
                for (var i = 0; i < dyColData.length; i++) {
                    if (dyColData[i].dyColDb) {
                        if (diff[dyColData[i].dyColDb]) {
                            dhx.alert("动态字段" + dyColData[i].dyColDb + "重复，不能导入！");
                            return;
                        } else {
                            diff[dyColData[i].dyColDb] = 1;
                        }
                    }
                }
                //查询源表是否存在编码重复
                DBImportAction.haveDupCodeOfSoucre(rowDatas, confData, function (r) {
                    if (!r) {
                        if ($("dataType").value == 2) {//清空数据
                            //检查断链的父节点
                            DBImportAction.haveDisLinked(rowDatas, confData, function (r) {
                                if (r) {
                                    dhx.alert("纵表中有断链的节点，不能导入！", function () {
                                        showDisLinked(rowDatas, confData);
                                    });
                                } else {
                                    dhx.closeProgress(null,0);
                                    dhx.showProgress("正在导入...", "正在导入维度表数据，请稍后。");
                                    DBImportAction.importDimInfo(rowDatas, confData, function (msg) {
                                        if (msg) {
                                            dhx.alert(msg);
                                        } else {
                                            dhx.alert("导入成功！", function () {
                                                self.location.reload();
                                            });
                                            importWindow.close();
                                        }
                                        dhx.closeProgress();
                                    })
                                }
                            });
                        } else {//不清空数据，进行重复编码检查以及断链的父节点检查
                            DBImportAction.haveDisLinked(rowDatas, confData, function (r) {
                                if (r) {
                                    dhx.alert("纵表中有断链的节点，不能导入！", function () {
                                        showDisLinked(rowDatas, confData);
                                    });
                                } else {
                                    //检查纵表中编码是否与该维度表编码有重复
                                    DBImportAction.haveDupCode(rowDatas, confData, function (r) {
                                        if (!r) {//不存在重复编码
                                            dhx.closeProgress(null,0);
                                            dhx.showProgress("正在导入...", "正在导入维度表数据，请稍后。");
                                            DBImportAction.importDimInfo(rowDatas, confData, function (msg) {
                                                if (msg) {
                                                    dhx.alert(msg);
                                                } else {
                                                    dhx.alert("导入成功！", function () {
                                                        self.location.reload();
                                                    });
                                                    importWindow.close();
                                                }
                                                dhx.closeProgress();
                                            })
                                        } else {
                                            dhx.alert("选择的纵表与当前维度表有重复编码，不能导入！", function () {
                                                showDupCode(rowDatas, confData);
                                            });
                                        }
                                    })
                                }
                            });
                        }
                    } else {
                        dhx.alert("查询结果有重复编码，不能导入！", function () {
                            showDupCodeSource(rowDatas, confData)
                        });
                    }


                });


            }
            };

            var dyColStr = "";
            for (var i = 0; i < dymaicColData.length; i++) {
                if (dymaicColData[i].colNullabled == 0) {//如果动态字段不能为空，则必须判断是否设置该动态字段
                    for(var j=0; j<dyColData.length; j++){
                        if(dyColData[j].dyCol == dymaicColData[i].colName){
                            if(dyColData[j].dyColDb){
                                dyColStr = dyColStr + dyColData[j].dyColDb + ",";
                            }
                        }
                    }
                }
            }
            if(dyColStr == ""){
                dhx.showProgress("正在检查...", "正在检查数据合法性，请稍后。");
                doSubVer();
            }else{
                var confData = {
                    dimType:currentDimType,
                    tableId:tableId,
                    dataSourceId:dbDataSourceId,
                    tableOwner:dbTableOwner,
                    tableName:dbTableName,
                    tableType:$("tableType").value,
                    dataType:$("dataType").value,
                    codeType:$("codeType").value,
                    dyColData:dyColData
                };//map
                dhx.showProgress("正在检查...", "正在检查数据合法性，请稍后。");
                DBImportAction.haveNullValue(confData,dyColStr.substring(0,dyColStr.length-1),function(r){
                    if(r){
                        dhx.closeProgress(null,0);
                        dhx.alert("源表中动态字段有空值，不能导入到当前表的非空动态字段中！");
                    }else{
                        doSubVer();
                    }
                })
            }
//            doSubVer();
        };
        $("_verWindowButton").appendChild(ok1);
        var cancel1 = Tools.getButtonNode("关闭");
        cancel1.style.styleFloat = "left";
        cancel1.style.cssFloat = "left";
        cancel1.style.marginTop = "5px";
        cancel1.style.marginLeft = "20px";
        cancel1.onclick = function () {
            importWindow.hide();
            importWindow.setModal(false);
        };
        $("_verWindowButton").appendChild(cancel1);

    } else {
        importWindow.setModal(true);
        importWindow.show();
    }

};

var typeRowIndex = 0;
var level = 1;

/**
 * 新增层级
 */
var addTypeRow = function () {
    if (!dbTableName) {
        dhx.alert("请先选择一个维度表");
        return;
    }
    dbDataSourceId = $("_tableDataSource").value;
    dbTableOwner = $("_owner").value;
    var tr = document.createElement("tr");
    $("_typeContent").appendChild(tr);
    tr.id = "_typeContentRow_" + (typeRowIndex++);
    tr.typeRowIndex = typeRowIndex;
    var td = tr.insertCell(0);
    td.id = "_level_" + typeRowIndex;
    td.align = "center";
    td.innerHTML = level;
    level++;
    td = tr.insertCell(1);
    td.align = "left";
    var input = dhx.html.create("input", {type:'text', name:"codeCol", readonly:"true", id:"_codeCol_" + typeRowIndex, style:"width: 90%"});
    td.appendChild(input);
    newQueryColTree(dbDataSourceId, dbTableName, dbTableOwner, $("_codeCol_" + typeRowIndex));
    td = tr.insertCell(2);
    td.align = "left";
    var input = dhx.html.create("input", {type:'text', name:"nameCol", readonly:"true", id:"_nameCol_" + typeRowIndex, style:"width: 90%"});
    td.appendChild(input);
    newQueryColTree(dbDataSourceId, dbTableName, dbTableOwner, $("_nameCol_" + typeRowIndex));
    td = tr.insertCell(3);
    td.align = "left";
    var input = dhx.html.create("input", {type:'text', name:"descCol", readonly:"true", id:"_descCol_" + typeRowIndex, style:"width: 90%"});
    td.appendChild(input);
    newQueryColTree(dbDataSourceId, dbTableName, dbTableOwner, $("_descCol_" + typeRowIndex));
    td = tr.insertCell(4);
    td.align = "center";
    td.innerHTML = '<img src="../../../resource/images/cancel.png" title="删除" onclick="removeType(this)" style="width:16px;height: 16px;cursor: pointer">';
    dhtmlxValidation.addValidation(tr, [
        {target:"_codeCol_" + typeRowIndex, rule:'MaxLength[32],NotEmpty'},
        {target:"_nameCol_" + typeRowIndex, rule:'MaxLength[32],NotEmpty'}
    ]);
};

//删除一行
var removeType = function (obj) {
    var trId = obj.parentNode.parentNode.id;
//    alert(obj.parentNode.parentNode.typeRowIndex);
    $(trId).parentNode.removeChild($(trId));
    refreshLevel();
};

/**
 * 刷新层级
 */
var refreshLevel = function () {
    level = 1;
    for (var i = 1; i <= typeRowIndex; i++) {
        if ($("_level_" + i)) {
            $("_level_" + i).innerHTML = level;
            level++;
        }
    }
};

var clearRows = function () {
    typeRowIndex = 0;
    level = 1;

    var tableObj = $("_typeContentTable");
    for (var i = 1; i < tableObj.rows.length; i++) {
        var row = tableObj.rows[i--];
        row.parentNode.removeChild(row);
    }

};

/**
 * 取得横表导入行数据
 */
var getTypeRowData = function () {
    var rtnData = [];
    var tableObj = $("_typeContent");
    for (var i = 0; i < tableObj.rows.length; i++) {
        var rowData = {};
        for (var j = 0; j < tableObj.rows[i].cells.length; j++) {
            var elements = tableObj.rows[i].cells[j].getElementsByTagName("input");
            for (var z = 0; z < elements.length; z++) {
                if ([elements[z].name]) {
                    rowData[elements[z].name] = dwr.util.getValue(elements[z]);
                }
            }
        }
        if (rowData.codeCol && rowData.nameCol) {
            rtnData.push(rowData);
        }
    }
    return rtnData;
};

/**
 * 取得纵表导入行数据
 */
var getVerRowData = function () {
    var rtnData = [];
    var tableObj = $("_verContent");
    for (var i = 0; i < tableObj.rows.length; i++) {
        var rowData = {};
        for (var j = 0; j < tableObj.rows[i].cells.length; j++) {
            var elements = tableObj.rows[i].cells[j].getElementsByTagName("input");
            for (var z = 0; z < elements.length; z++) {
                if ([elements[z].name]) {
                    rowData[elements[z].name] = dwr.util.getValue(elements[z]);
                }
            }
        }
        if (rowData.verCodeCol && rowData.verParCodeCol) {
            rtnData.push(rowData);
        }
    }
    return rtnData;
};

/**
 * 有效性验证
 */
var validateType = function () {
    var tableObj = $("_typeContent");
    var validateRes = true;
    for (var i = 1; i < tableObj.rows.length; i++) {
        validateRes = dhtmlxValidation.validate(tableObj.rows[i]) && validateRes;
    }
    return validateRes;
};

/**
 * 有效性验证
 */
var validateVer = function () {
    var tableObj = $("_verContent");
    var validateRes = true;
    validateRes = dhtmlxValidation.validate(tableObj) && validateRes;
    return validateRes;
};

/**
 * 横纵表转换
 * @param obj
 */
var tableTypeChange = function (obj) {
    if (obj.value == 2) {//纵表
        $("horTable").style.display = 'none';
        $("verTable").style.display = '';
        $("codeType").value = 1;
        $("codeType").disabled = "true";
    }
    if (obj.value == 1) {//横表
        $("horTable").style.display = '';
        $("verTable").style.display = 'none';
        $("codeType").disabled = "";
    }

};

/**
 * 纵表导入初始化方法
 */
var initVerFun = function () {
    newQueryColTree(dbDataSourceId, dbTableName, dbTableOwner, $("verCodeCol"));
    newQueryColTree(dbDataSourceId, dbTableName, dbTableOwner, $("verParCodeCol"));
    newQueryColTree(dbDataSourceId, dbTableName, dbTableOwner, $("verNameCol"));
    newQueryColTree(dbDataSourceId, dbTableName, dbTableOwner, $("verDescCol"));
    $("verCodeCol").value="";
    $("verParCodeCol").value="";
    $("verNameCol").value="";
    $("verDescCol").value="";
    $("verRootVal").value = '';
    dhtmlxValidation.addValidation($("_verContent"), [
        {target:"verCodeCol", rule:'NotEmpty'},
        {target:"verParCodeCol", rule:'NotEmpty'},
        {target:"verNameCol", rule:'NotEmpty'}
    ]);
};

/**
 * 展示重复编码
 * @param rowDatas
 * @param confData
 */
var showDupCode = function (rowDatas, confData) {
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("dupCodeWindow", 0, 0, 650, 300);
    var dupCodeWindow = dhxWindow.window("dupCodeWindow");
    dupCodeWindow.setModal(true);
    dupCodeWindow.stick();
    dupCodeWindow.setDimension(650, 300);
    dupCodeWindow.center();
    dupCodeWindow.denyResize();
    dupCodeWindow.denyPark();
    dupCodeWindow.setText('重复编码');
    dupCodeWindow.keepInViewport(true);

    //关闭一些不用的按钮。
    dupCodeWindow.button("minmax1").hide();
    dupCodeWindow.button("park").hide();
    dupCodeWindow.button("stick").hide();
    dupCodeWindow.button("sticked").hide();
    dupCodeWindow.show();
    var dupCodeLayout = new dhtmlXLayoutObject(dupCodeWindow, "1C");
    dupCodeLayout.cells("a").hideHeader();

    //展示表格
    var dupCodeGrid = dupCodeLayout.cells("a").attachGrid();
    var header = dimInfo.tableDimPrefix.toUpperCase() + '_NAME,' + dimInfo.tableDimPrefix.toUpperCase() + '_CODE,';
    var headerIds = tranColumnToJavaName(dimInfo.tableDimPrefix.toUpperCase() + '_NAME')
        + "," + tranColumnToJavaName(dimInfo.tableDimPrefix.toUpperCase() + '_CODE') + ',';
    if (confData.tableType == 1) {//横表
        for (var i = 0; i < rowDatas.length; i++) {
            header = header + rowDatas[i].nameCol + "," + rowDatas[i].codeCol;
            headerIds = headerIds + tranColumnToJavaName(rowDatas[i].nameCol) + ","
                + tranColumnToJavaName(rowDatas[i].codeCol);
            if (i != rowDatas.length - 1) {
                header = header + ",";
                headerIds = headerIds + ",";
            }
        }
    } else {//纵表
        header = header + rowDatas[0].verNameCol + "," + rowDatas[0].verCodeCol + "," + rowDatas[0].verParCodeCol;
        headerIds = headerIds +
            tranColumnToJavaName(rowDatas[0].verNameCol) + "," +
            tranColumnToJavaName(rowDatas[0].verCodeCol) + "," +
            tranColumnToJavaName(rowDatas[0].verParCodeCol);
    }
    dupCodeGrid.setHeader(header);
    var _CovertConfig = {
        filterColumns:headerIds.split(",")
    };
    dwrCaller.addAutoAction("queryDupData", "DBImportAction.queryDupData", rowDatas, confData);
    dwrCaller.addDataConverter("queryDupData", new dhtmxGridDataConverter(_CovertConfig));
    dwrCaller.isDealOneParam("queryDupData", false);
    dupCodeGrid.setColumnIds(headerIds);
    dupCodeGrid.enableCtrlC();
    dupCodeGrid.init();
    dupCodeGrid.load(dwrCaller.queryDupData, "json");
};

/**
 * 纵表导入时显示断链的纵表信息
 * @param rowDatas
 * @param confData
 */
var showDisLinked = function (rowDatas, confData) {
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("showDisLinkedWindow", 0, 0, 650, 300);
    var showDisLinkedWindow = dhxWindow.window("showDisLinkedWindow");
    showDisLinkedWindow.setModal(true);
    showDisLinkedWindow.stick();
    showDisLinkedWindow.setDimension(650, 300);
    showDisLinkedWindow.center();
    showDisLinkedWindow.denyResize();
    showDisLinkedWindow.denyPark();
    showDisLinkedWindow.setText('断链数据');
    showDisLinkedWindow.keepInViewport(true);

    //关闭一些不用的按钮。
    showDisLinkedWindow.button("minmax1").hide();
    showDisLinkedWindow.button("park").hide();
    showDisLinkedWindow.button("stick").hide();
    showDisLinkedWindow.button("sticked").hide();
    showDisLinkedWindow.show();
    var showDisLinkedLayout = new dhtmlXLayoutObject(showDisLinkedWindow, "1C");
    showDisLinkedLayout.cells("a").hideHeader();

    //展示表格
    var showDisLinkedGrid = showDisLinkedLayout.cells("a").attachGrid();
    var header = rowDatas[0].verNameCol + ',' + rowDatas[0].verCodeCol + ',' + rowDatas[0].verParCodeCol;
    var headerIds = tranColumnToJavaName(rowDatas[0].verNameCol) + ','
        + tranColumnToJavaName(rowDatas[0].verCodeCol) + ','
        + tranColumnToJavaName(rowDatas[0].verParCodeCol);

    showDisLinkedGrid.setHeader(header);
    var _CovertConfig = {
        filterColumns:headerIds.split(",")
    };
    dwrCaller.addAutoAction("queryDisLinked", "DBImportAction.queryDisLinked", rowDatas, confData);
    dwrCaller.addDataConverter("queryDisLinked", new dhtmxGridDataConverter(_CovertConfig));
    dwrCaller.isDealOneParam("queryDisLinked", false);
    showDisLinkedGrid.setColumnIds(headerIds);
    showDisLinkedGrid.enableCtrlC();
    showDisLinkedGrid.init();
    showDisLinkedGrid.load(dwrCaller.queryDisLinked, "json");

};

/**
 * 展示源表中相同编码的数据
 * @param rowDatas
 * @param confData
 */
var showDupCodeSource = function (rowDatas, confData) {
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("showDupCodeSourceWindow", 0, 0, 650, 300);
    var showDupCodeSourceWindow = dhxWindow.window("showDupCodeSourceWindow");
    showDupCodeSourceWindow.setModal(true);
    showDupCodeSourceWindow.stick();
    showDupCodeSourceWindow.setDimension(650, 300);
    showDupCodeSourceWindow.center();
    showDupCodeSourceWindow.denyResize();
    showDupCodeSourceWindow.denyPark();
    showDupCodeSourceWindow.setText('源表重复编码');
    showDupCodeSourceWindow.keepInViewport(true);

    //关闭一些不用的按钮。
    showDupCodeSourceWindow.button("minmax1").hide();
    showDupCodeSourceWindow.button("park").hide();
    showDupCodeSourceWindow.button("stick").hide();
    showDupCodeSourceWindow.button("sticked").hide();
    showDupCodeSourceWindow.show();
    var showDupCodeSourceLayout = new dhtmlXLayoutObject(showDupCodeSourceWindow, "1C");
    showDupCodeSourceLayout.cells("a").hideHeader();

    //展示表格
    var showDupCodeSourceGrid = showDupCodeSourceLayout.cells("a").attachGrid();
    var header = "";
    var headerIds = "";
    if (confData.tableType == 1) {//横表
        for (var i = 0; i < rowDatas.length; i++) {
            header = header + rowDatas[i].nameCol + "," + rowDatas[i].codeCol;
            headerIds = headerIds + tranColumnToJavaName(rowDatas[i].nameCol) + ","
                + tranColumnToJavaName(rowDatas[i].codeCol);
            if (i != rowDatas.length - 1) {
                header = header + ",";
                headerIds = headerIds + ",";
            }
        }
    } else {//纵表
        header = header + rowDatas[0].verNameCol + "," + rowDatas[0].verCodeCol;
        headerIds = headerIds +
            tranColumnToJavaName(rowDatas[0].verNameCol) + "," +
            tranColumnToJavaName(rowDatas[0].verCodeCol);
    }
    showDupCodeSourceGrid.setHeader(header);
    var _CovertConfig = {
        filterColumns:headerIds.split(",")
    };
    dwrCaller.addAutoAction("queryDupCodeOfSoucre", "DBImportAction.queryDupCodeOfSoucre", rowDatas, confData);
    dwrCaller.addDataConverter("queryDupCodeOfSoucre", new dhtmxGridDataConverter(_CovertConfig));
    dwrCaller.isDealOneParam("queryDupCodeOfSoucre", false);
    showDupCodeSourceGrid.setColumnIds(headerIds);
    showDupCodeSourceGrid.enableCtrlC();
    showDupCodeSourceGrid.init();
    showDupCodeSourceGrid.defaultPaging(20);
    showDupCodeSourceGrid.load(dwrCaller.queryDupCodeOfSoucre, "json");

};

var dymaicColSetWindow = null;
var dyColData=[];

/**
 * 动态字段设置
 */
var dymaicColSet = function(){
    if (!dbTableName) {
        dhx.alert("请先选择一个维度表");
        return;
    }
    if(!dymaicColSetWindow){
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("dymaicColSetWindow", 0, 0, 300, 300);
        dymaicColSetWindow = dhxWindow.window("dymaicColSetWindow");
        dymaicColSetWindow.setModal(true);
        dymaicColSetWindow.stick();
        dymaicColSetWindow.setDimension(300, 300);
        dymaicColSetWindow.center();
        dymaicColSetWindow.denyResize();
        dymaicColSetWindow.denyPark();
        dymaicColSetWindow.button("minmax1").hide();
        dymaicColSetWindow.button("park").hide();
        dymaicColSetWindow.button("stick").hide();
        dymaicColSetWindow.button("sticked").hide();
        dymaicColSetWindow.setModal(true);
        dymaicColSetWindow.keepInViewport(true);
        dymaicColSetWindow.setText("动态字段设置");
        dymaicColSetWindow.attachEvent("onClose", function () {
            dymaicColSetWindow.hide();
            dymaicColSetWindow.setModal(false);
            return false;
        });
        dymaicColSetWindow.show();
        var dymaicColSetLayout = new dhtmlXLayoutObject(dymaicColSetWindow, "1C");
        dymaicColSetLayout.cells("a").hideHeader();
        dymaicColSetLayout.cells("a").attachObject($("dymaicColTable"));
        dhx.html.addCss($("_dymaicColTable"), global.css.gridTopDiv);
        initDymaicCol();
        var ok = Tools.getButtonNode("确定");
        ok.style.marginLeft = "80px";
        ok.style.marginTop = "5px";
        ok.style.cssFloat = "left";
        ok.style.styleFloat = "left";
        ok.onclick = function () {
            if(checkDyType()){
                dyColData = getDyRowData();
                dymaicColSetWindow.close();
            }
        };
        $("_dymaicColWindowButton").appendChild(ok);
        var cancel = Tools.getButtonNode("重置");
        cancel.style.styleFloat = "left";
        cancel.style.cssFloat = "left";
        cancel.style.marginTop = "5px";
        cancel.style.marginLeft = "10px";
        cancel.onclick = function () {
            clearDyRowData(true);
        };
        $("_dymaicColWindowButton").appendChild(cancel);

    } else {
        if(dyColData.length == 0){
            clearDyRowData(false);
        }
        dymaicColSetWindow.setModal(true);
        dymaicColSetWindow.show();
    }

};

/**
 * 初始化动态字段列设置
 */
var initDymaicCol = function(){
    for (var i = 0; i < dymaicColData.length; i++) {
        var tr = document.createElement("tr");
        $("_dymaicColContent").appendChild(tr);
        tr.id = "_dymaicColContentRow_" + i;
        var td = tr.insertCell(0);
        td.align = "center";
        if(dymaicColData[i].colNullabled == 0){//不允许为空
            td.innerHTML = '<span style="color: red;">*&nbsp;</span>'+dymaicColData[i].colName;
        }else{
            td.innerHTML = dymaicColData[i].colName;
        }
        var input = dhx.html.create("input", {type:'hidden', name:"dyCol", id:"_dyCol_" + i, value:dymaicColData[i].colName, style:"width: 90%"});
        td.appendChild(input);
        td = tr.insertCell(1);
        td.align = "left";
        input = dhx.html.create("input", {type:'text', name:"dyColDb", readonly:"true", id:"_dyColDb_" + i, style:"width: 90%"});
        input._colType = dymaicColData[i].colDatatype;
        td.appendChild(input);
        newQueryColTree(dbDataSourceId, dbTableName, dbTableOwner, $("_dyColDb_" + i));
    }
};

var checkDyType = function(){
    var tableObj = $("_dymaicColContent");
    for (var i = 0; i < tableObj.rows.length; i++) {
        for (var j = 0; j < tableObj.rows[i].cells.length; j++) {
            var elements = tableObj.rows[i].cells[j].getElementsByTagName("input");
            for (var z = 0; z < elements.length; z++) {
                if(elements[z].name=='dyColDb'){
                    if(elements[z]._dbColType
                        &&elements[z]._colType.indexOf(elements[z]._dbColType)==-1){
                        dhx.alert("源表字段"+dwr.util.getValue(elements[z])+"类型为"+elements[z]._dbColType
                            +"，与动态字段类型"+elements[z]._colType+"不匹配，不能设置！");
                        elements[z].value='';
                        elements[z]._dbColType='';
                        return false;
                    }
                }
            }
        }
    }
    return true;
};

/**
 * 动态字段设置
 */
var getDyRowData = function () {
    var rtnData = [];
    var tableObj = $("_dymaicColContent");
    for (var i = 0; i < tableObj.rows.length; i++) {
        var rowData = {};
        for (var j = 0; j < tableObj.rows[i].cells.length; j++) {
            var elements = tableObj.rows[i].cells[j].getElementsByTagName("input");
            for (var z = 0; z < elements.length; z++) {
                if ([elements[z].name]) {
                    rowData[elements[z].name] = dwr.util.getValue(elements[z]);
                }
            }
        }
        if (rowData.dyColDb&&rowData.dyCol) {
            rtnData.push(rowData);
        }
    }
    return rtnData;
};

/**
 * 清空动态字段设置列信息
 */
var clearDyRowData = function(isReTree){
    dyColData = [];
    var tableObj = $("_dymaicColContent");
    for(var i = 0; i < tableObj.rows.length; i++) {
        for (var j = 0; j < tableObj.rows[i].cells.length; j++) {
            var elements = tableObj.rows[i].cells[j].getElementsByTagName("input");
            for (var z = 0; z < elements.length; z++) {
                if (elements[z].name=='dyColDb') {
                    elements[z].value='';
                    if(isReTree){
                        newQueryColTree(dbDataSourceId, dbTableName, dbTableOwner, elements[z]);
                    }
                }
            }
        }
    }
};

/**
 * 动态字段检查
 */
var checkDyCol = function(){
    for (var i = 0; i < dymaicColData.length; i++) {
        if (dymaicColData[i].colNullabled == 0) {//如果动态字段不能为空，则必须判断是否设置该动态字段
            if(dyColData.length == 0){
                dhx.alert("该维度表类动态字段" + dymaicColData[i].colName + "不允许为空，请设置动态字段后导入！");
                return false;
            }else{
                var check = false;
                for(var j=0; j<dyColData.length; j++){
                    if(dyColData[j].dyCol == dymaicColData[i].colName){
                        if(!dyColData[j].dyColDb){

                        }else{
                            check = true;
                        }
                    }
                }
                if(!check){
                    dhx.alert("该维度表类动态字段" + dymaicColData[i].colName + "不允许为空，请设置动态字段后导入！");
                    return false;
                }
            }
        }
    }
    return true;
};















/**
 * 加载列树
 */
dwrCaller.addAutoAction("queryColTree", TableApplyAction.queryDbTableColumns, {
    dwrConfig:true,
    isShowProcess:false
});
var colTreeConverter = new dhtmxTreeDataConverter({
    idColumn:"colName", pidColumn:0,
    isDycload:false, textColumn:"colName"
});
dwrCaller.addDataConverter("queryColTree", colTreeConverter);

/**
 * 加载列树
 * @param selectGroup
 * @param form
 */
var newQueryColTree = function (dataSourceId, tableName, tableOwner, target) {
    var divHeight = 210;
    var divWidth = 200;
    var div = null;
    if (!target.propDiv) {
        var div = dhx.html.create("div", {
            style:"display;none;position:absolute;border: 2px #eee solid;overflow: auto;padding: 0;margin: 0;" +
                "z-index:1000;background-color:white;"
        });
        //创建tree Div层
        var treeDiv = dhx.html.create("div", {
            style:"position:relative;overflow: auto;padding: 0;margin: 0;" +
                "z-index:1000;background-color:white;"
        });
        document.body.appendChild(div);
        div.appendChild(treeDiv);
        div.style.width = divWidth + "px";
        div.style.height = divHeight + "px";
        treeDiv.style.width = divWidth + "px";
        treeDiv.style.height = divHeight + "px";
        target.propDiv = div;
        target.treeDiv = treeDiv;
    } else {
        div = target.propDiv;
    }
//    target.readOnly=false;
    if (!target.tree) {
        //生成树
        var tree = new dhtmlXTreeObject(target.treeDiv, divWidth + "px", divHeight + "px", 0);
        tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
        tree.enableThreeStateCheckboxes(true);
//    tree.enableSmartRendering();
        tree.enableHighlighting(true);
        tree.enableRadioButtons(true);
        tree.setDataMode("json");
        tree.attachEvent("onSelect", function (id) {
            tree.setCheck(id, 1);
            var allChecked = tree.getAllChecked();
            if (allChecked) {
                var nodes = typeof allChecked == "string" ? allChecked.split(",") : [allChecked];
                var textValue = "";
                for (var i = 0; i < nodes.length; i++) {
                    textValue = textValue + tree.getItemText(nodes[i]) + ",";
                    if (i == nodes.length - 1) {
                        textValue = textValue.substring(0, textValue.length - 1);
                    }
                }
                target.value = textValue;
                target._dbColType=tree.getUserData(id, "dataType");
            } else {
                target.value = "";
            }
            dhtmlxValidation.validate(target);
        });
        tree.attachEvent("onCheck", function (id) {
            tree.setCheck(id, 1);
            var allChecked = tree.getAllChecked();
            if (allChecked) {
                var nodes = typeof allChecked == "string" ? allChecked.split(",") : [allChecked];
                var textValue = "";
                for (var i = 0; i < nodes.length; i++) {
                    textValue = textValue + tree.getItemText(nodes[i]) + ",";
                    if (i == nodes.length - 1) {
                        textValue = textValue.substring(0, textValue.length - 1);
                    }
                }
                target.value = textValue;
                target._dbColType=tree.getUserData(id, "dataType");
            } else {
                target.value = "";
            }
            dhtmlxValidation.validate(target);
        });
        target.tree = tree;
    }
    div.style.display = "none";
    dwrCaller.executeAction("queryColTree", tableName, tableOwner, dataSourceId, function (data) {
        target.tree.clearAll();
        target.tree.loadJSONObject(data);
        //为div添加事件
        Tools.addEvent(target, "click", function (ev) {
            var e = ev || window.event;
            var target = e.srcElement || e.target;
            var div = target.propDiv;
            Tools.divPromptCtrl(div, target, true);
            div.style.display = "block";
        });
    });
};

var tranColumnToJavaName = function (columnName, isTranWhenNospliter) {
    //匹配XXX_格式的命名
    var reg = /([A-Za-z_])([A-Za-z0-9]*)_?/g;
    var match = null;
    var count = 0;
    var javaName = "";
    while ((match = reg.exec(columnName))) {
        //变量第一个字母小写
        if (count++ == 0) {
            javaName += match[1].toLowerCase();
        } else { //以下划线分割的第一个字母大写
            javaName += match[1].toUpperCase();
        }
        javaName += match[2].toLowerCase();
    }
    return isTranWhenNospliter ? (count == 1 ? columnName : javaName) : javaName;
}



