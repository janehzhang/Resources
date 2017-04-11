/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       diffAnalysis.js
 *Description：
 *       差异分析JS文件
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *        张伟
 *Finished：
 *       2011-11-24
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
 if(window.parent&&window.parent.tableVersion){
    tableVersion=window.parent.tableVersion;
 }
 
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
//表类数据源相关Action
var dwrCaller = new biDwrCaller();
dwrCaller.addAutoAction("queryInsts", DiffAction.queryInsts, tableId);
dwrCaller.addDataConverter("queryInsts", new dhtmlxComboDataConverter({
    valueColumn:"tableInstId",
    textColumn:"tableName",
    textFormat:function (rowData, value, text) {
        if (rowData.tableOwner) {
            return  rowData.tableOwner.toUpperCase() + "." + text;
        }
        return text;
    }
}));
dwrCaller.isShowProcess("queryInsts", false);
var currentDiffCount = 0;//当前差异数。
//差异分析Action定义
dwrCaller.addAutoAction("diffAnalysis", DiffAction.diffAnalysis, tableId, tableVersion, {
    dwrConfig:true,
    converter:new dhtmxGridDataConverter({
        filterColumns:["instColumnName", "instDataType", "instIsPrimary", "instColNullabled", "instDefaultVal",
            "configColumnName", "configDataType", "configIsPrimary", "configColNullabled", "configDefaultVal", "configColumnNameCn", "diff"],
        onBeforeConverted:function (data) {
            //判断是否是出错信息。
            if (data && data.length == 1) {
                if (data[0]._errorMessage) {
                    dhx.alert(data[0]._errorMessage);
                    return Tools.EmptyList;
                }
            }
            if (data) {
                $("_diffCount").innerHTML = data[data.length - 1].DIFF_COUNT;
                data.splice(data.length - 1, 1);
            }
            //进行排序，将新增和删除的字段列于最前
            data.sort(function (value1, value2) {
                //删除最前
                if (value1 && value1.DIFF == 2) {
                    return -1;
                }
                if (value2 && value2.DIFF == 2) {
                    return 1;
                }
                //其次新增
                if (value1 && value1.DIFF == 1) {
                    return -1;
                }
                if (value2 && value2.DIFF == 1) {
                    return 1;
                }
                //其次类型差异
                if (value1 && value1.DIFF == 3) {
                    return -1;
                }
                if (value2 && value2.DIFF == 3) {
                    return 1;
                }
                //其次字段名修改
                if (value1 && value1.DIFF == 7) {
                    return -1;
                }
                if (value2 && value2.DIFF == 7) {
                    return 1;
                }
                //其次默认值不同
                if (value1 && value1.DIFF == 6) {
                    return -1;
                }
                if (value2 && value2.DIFF == 6) {
                    return 1;
                }
                //其次主键不同
                if (value1 && value1.DIFF == 5) {
                    return -1;
                }
                if (value2 && value2.DIFF == 5) {
                    return 1;
                }
                return 0;
            })

        },
        userData:function (rowIndex, rowData) {
            if (rowData.diff && (rowData.diff == "2" || rowData.diff == "1")) {
                rowData.match = false;
            } else {
                rowData.match = true;
            }
            return rowData;
        },
        cellDataFormat:function (rowIndex, columnIndex, columnName, cellValue, rowData) {
            var formatValue = {};
            if ((columnName == 'configIsPrimary' || columnName == 'configColNullabled'
                || columnName == 'instIsPrimary' || columnName == 'instIsPrimary') && cellValue == "--") {//如果是checkBox列，且无值
                formatValue = {type:"ro", value:cellValue};
            }
            if (columnName != "diff") {//差异加上背景色
                if (rowData.diff) {
                    if (rowData.diff == 1 || rowData.diff == 2) { //新增和删除列
                        formatValue.style = "background-color: #fdffc3;";
                        formatValue.value = cellValue;
                    } else {
                        switch (columnName) {
                            case  "configDataType":
                            case "instDataType":
                            { //字段类型差异
                                if (rowData.diff.indexOf("3") != -1) {
                                    formatValue.style = "background-color: #fdffc3;";
                                    formatValue.value = cellValue;
                                }
                                break;
                            }
                            //主键差异
                            case "instIsPrimary" :
                            case "configIsPrimary":
                            {
                                if (rowData.diff.indexOf("5") != -1) {
                                    formatValue.style = "background-color: #fdffc3;";
                                    formatValue.value = cellValue;
                                }
                                break;
                            }
                            //是否空置差异
                            case "configColNullabled" :
                            case "instColNullabled":
                            {
                                if (rowData.diff.indexOf("4") != -1) {
                                    formatValue.style = "background-color:#fdffc3;";
                                    formatValue.value = cellValue;
                                }
                                break;
                            }
                            //默认值差异
                            case "instDefaultVal" :
                            case "configDefaultVal":
                            {
                                if (rowData.diff.indexOf("6") != -1) {
                                    formatValue.style = "background-color: #fdffc3;";
                                    formatValue.value = cellValue;
                                }
                                break;
                            }
                        }
                    }
                }
            }
            if (Tools.isEmptyObject(formatValue)) {
                return cellValue
            } else {
                return formatValue;
            }
        }
    }),
    processMessage:"正在进行差异分析，耗时可能较长..."
})

var dataSourceInfo = null;
//根据表类信息查询到当前表类数据源Action定义
dwrCaller.addAutoAction("queryDataSourceByTable", DiffAction.queryDataSourceByTable, tableId, tableVersion,
    {
        dwrConfig:true,
        callback:function (data) {
            dataSourceInfo = data;
        },
        async:false,
        isShowProcess:false
    }
);
var queryDiff = function (instId) {
    diffAnalysisGrid.clearAll();
    if (instId == null || instId == undefined) {
        instId = dwr.util.getValue("_insts");
    }
    diffAnalysisGrid.load(dwrCaller.diffAnalysis + "&instId=" + instId, "json");
}
var diffAnalysisInit = function () {
    dwrCaller.executeAction("queryDataSourceByTable");
    var diffAnalysisLayout = new dhtmlXLayoutObject(document.body, "2E");
    diffAnalysisLayout.cells("a").setText("差异同步到实例");
    diffAnalysisLayout.cells("b").hideHeader();
    diffAnalysisLayout.cells("b").setHeight(30);
    diffAnalysisLayout.cells("b").fixSize(true, true);
    diffAnalysisLayout.hideConcentrate();
    diffAnalysisLayout.hideSpliter();//移除分界边框。
    var buttonForm = diffAnalysisLayout.cells("b").attachForm([
        {type:"settings", position:"label-left", labelWidth:70, inputWidth:120},
        {type:"button", value:"仅同步结构", name:"synchronousStruct", offsetLeft:diffAnalysisLayout.cells("b").getWidth() / 2 - 80},
        {type:"newcolumn"},
        {type:"button", value:"同步结构&数据", name:"synchronousStructData"}
    ]);
    buttonForm.attachEvent("onButtonClick", function (id) {
        if (id == "synchronousStruct") {
            showSysStructWindow();
        } else {
            showSysStructAndDataWindow();
        }
    })
    //加入grid
    diffAnalysisGrid = diffAnalysisLayout.cells("a").attachGrid();
    diffAnalysisGrid.setHeader("表类实例：&nbsp;&nbsp;<select id='_insts' style='width:300px' onchange='queryDiff()'></select>,#cspan,#cspan,#cspan,#cspan," +
        "表类名：" + tableName + ",#cspan,#cspan,#cspan,#cspan,#cspan,差异描述", null, ["text-align:center;", "text-align:center", "text-align:center;vertical-align: middle;"]);
    diffAnalysisGrid.attachHeader("列名,列类型,主键,可为空,默认值,列名,列类型,主键,可为空,默认值,列中文名,#rspan"
        , ["text-align:center;", "text-align:center;", "text-align:center", "text-align:center;", "text-align:center"
            , "text-align:center;", "text-align:center;", "text-align:center", "text-align:center;", "text-align:center"]);
    diffAnalysisGrid.setInitWidthsP("10,10,6,6,8,10,10,6,6,8,10,10");
    diffAnalysisGrid.enableResizing("true,true,true,true,true,true,true,true,true,true,true,true");
    diffAnalysisGrid.setColTypes("ro,ro,ch,ch,ro,ro,ro,ch,ch,ro,ro,ro");
    diffAnalysisGrid.setColAlign("left,left,center,center,center,left,left,center,center,center,left,left");
    diffAnalysisGrid.setColSorting("na,na,na,na,na,na,na,na,na,na,na,na");
    diffAnalysisGrid.init();
    diffAnalysisGrid.setColumnCustFormat(11, function (value) {
        var mapping = {1:"字段新增", 2:"字段删除", 3:"类型差异", 4:"可为空不同", 5:"主键不同", 6:"默认值不同", 7:"字段名修改"};
        if (value) {
            return (value + "").replace(/(\d)(,)?/g, function ($0, $1, $2) {
                return mapping[$1] + ($2 ? $2 : "");
            })
        }
        return "";
    });//第1列进行转义
    diffAnalysisGrid.attachFooter("差异数：,#cspan,<span id='_diffCount'>" + currentDiffCount + "</span>,#cspan,注：新增列和删除列的差异可以拖动调整差异映射，以便进行数据同步，如果拖动出错，双击即可还原。" +
        ",#cspan,#cspan,#cspan,#cspan,#cspan,#cspan", ["text-align:right;", "text-align:left;", "text-align:left;", "text-align:left;", "text-align:center;"])
    diffAnalysisGrid.setEditable(false);
    diffAnalysisGrid.enableDragAndDrop(true);//设置可以接受拖拽

    dwrCaller.executeAction("queryInsts", function (data) {
        Tools.addOption("_insts", data);
        if (data && data.options.length > 0) {
            queryDiff(dwr.util.getValue("_insts"))
        }
    });

    diffAnalysisGrid.attachEvent("onDragIn", function (dId, toId, sObject, tObject) {
        if (diffAnalysisGrid.getUserData(toId, "match")) {//如果已匹配，不允许拖动
            return false;
        }
        return true;//树之间不允许拖动
    });
    diffAnalysisGrid.attachEvent("onBeforeDrag", function (sId) {
        //只有差异类型为新增的和删除的才能拖动
        if (diffAnalysisGrid.getUserData(sId, "match")) {
            return false;
        }
        return true;
    });
    diffAnalysisGrid.attachEvent("onDrag", function (sId, tId, sObj, tObj, sInd, tInd) {
        //两者差异类型不同，才能拖动
        if (diffAnalysisGrid.getUserData(sId, "diff") != diffAnalysisGrid.getUserData(tId, "diff")) {
            if (diffAnalysisGrid.getUserData(sId, "diff") == "1") {//新增列
                diffAnalysisGrid.cells(tId, 5).setValue(diffAnalysisGrid.getUserData(sId, "configColumnName"));
                diffAnalysisGrid.cells(tId, 6).setValue(diffAnalysisGrid.getUserData(sId, "configDataType"));
                diffAnalysisGrid.setCellExcellType(tId, 7, "ch");
                diffAnalysisGrid.setCellExcellType(tId, 8, "ch");
                diffAnalysisGrid.cells(tId, 7).setValue(diffAnalysisGrid.getUserData(sId, "configIsPrimary"));
                diffAnalysisGrid.cells(tId, 8).setValue(diffAnalysisGrid.getUserData(sId, "configColNullabled"));
                diffAnalysisGrid.cells(tId, 9).setValue(diffAnalysisGrid.getUserData(sId, "configDefaultVal"));
                diffAnalysisGrid.cells(tId, 10).setValue(diffAnalysisGrid.getUserData(sId, "configColumnNameCn"));
                diffAnalysisGrid.cells(tId, 11).setValue("7");
                diffAnalysisGrid.setUserData(tId, "diff", "7");
                //缓存源ID
                diffAnalysisGrid.setUserData(tId, "_sId", sId);
                //新增列COL_ID
                diffAnalysisGrid.setUserData(tId, "configColumnId", diffAnalysisGrid.getUserData(sId, "configColumnId"));
                //隐藏原列，模仿删除。
                diffAnalysisGrid.setRowHidden(sId, true);
                diffAnalysisGrid.setUserData(sId, "hidden", true);
                $("_diffCount").innerHTML = parseInt($("_diffCount").innerHTML) - 1;
                // 在差异类型为7(字段修改的时候不能拖动)，因为隐藏原列，模仿删除会出错
            } else if (diffAnalysisGrid.getUserData(sId, "diff") != "7") {//删除列
                diffAnalysisGrid.cells(tId, 0).setValue(diffAnalysisGrid.getUserData(sId, "instColumnName"));
                diffAnalysisGrid.cells(tId, 1).setValue(diffAnalysisGrid.getUserData(sId, "instDataType"));
                diffAnalysisGrid.setCellExcellType(tId, 2, "ch");
                diffAnalysisGrid.setCellExcellType(tId, 3, "ch");
                diffAnalysisGrid.cells(tId, 2).setValue(diffAnalysisGrid.getUserData(sId, "instIsPrimary"));
                diffAnalysisGrid.cells(tId, 3).setValue(diffAnalysisGrid.getUserData(sId, "instColNullabled"));
                diffAnalysisGrid.cells(tId, 4).setValue(diffAnalysisGrid.getUserData(sId, "instDefaultVal"));
                diffAnalysisGrid.cells(tId, 11).setValue("7");
                diffAnalysisGrid.setUserData(tId, "diff", "7");
                //缓存源ID
                diffAnalysisGrid.setUserData(tId, "_sId", sId);
                diffAnalysisGrid.setUserData(tId, "instColumnName", diffAnalysisGrid.getUserData(sId, "instColumnName"));
                //隐藏原列，模仿删除。
                diffAnalysisGrid.setRowHidden(sId, true);
                diffAnalysisGrid.setUserData(sId, "hidden", true);
            }
        }
        return false;
    });
    //双击还原映射
    diffAnalysisGrid.attachEvent("onRowDblClicked", function (rId, cInd) {
        var sId = diffAnalysisGrid.getUserData(rId, "_sId");
        if (sId) {
            diffAnalysisGrid.setRowHidden(sId, false);
            diffAnalysisGrid.setUserData(sId, "hidden", false);
            $("_diffCount").innerHTML = parseInt($("_diffCount").innerHTML) + 1;
            if (diffAnalysisGrid.getUserData(sId, "diff") == 1) {//原来列为新增列。
                diffAnalysisGrid.cells(rId, 5).setValue("--");
                diffAnalysisGrid.cells(rId, 6).setValue("--");
                diffAnalysisGrid.setCellExcellType(rId, 7, "ro");
                diffAnalysisGrid.setCellExcellType(rId, 8, "ro");
                diffAnalysisGrid.cells(rId, 7).setValue("--");
                diffAnalysisGrid.cells(rId, 8).setValue("--");
                diffAnalysisGrid.cells(rId, 9).setValue("--");
                diffAnalysisGrid.cells(rId, 10).setValue("--");
                diffAnalysisGrid.cells(rId, 11).setValue("2");
                diffAnalysisGrid.setUserData(rId, "diff", "2");
                //缓存源ID
                diffAnalysisGrid.setUserData(rId, "_sId", undefined);
                diffAnalysisGrid.setUserData(rId, "configColumnId", undefined);
            } else {
                diffAnalysisGrid.cells(rId, 0).setValue("--");
                diffAnalysisGrid.cells(rId, 1).setValue("--");
                diffAnalysisGrid.setCellExcellType(rId, 2, "ro");
                diffAnalysisGrid.setCellExcellType(rId, 3, "ro");
                diffAnalysisGrid.cells(rId, 2).setValue("--");
                diffAnalysisGrid.cells(rId, 3).setValue("--");
                diffAnalysisGrid.cells(rId, 4).setValue("--");
                diffAnalysisGrid.cells(rId, 11).setValue("1");
                diffAnalysisGrid.setUserData(rId, "diff", "1");
                //缓存源ID
                diffAnalysisGrid.setUserData(rId, "_sId", undefined);
                diffAnalysisGrid.setUserData(tId, "instColumnName", "--");
            }
        }
    });


}
/**
 * 获取映射的差异列信息
 * @return 返回一个对象，包含三个键值：
 * modifyColumn:[{configColumnId:1,instColumnName:""}...],
 * addColumn:[...];//记录新增的列Id
 * deleteColumn:[...];//记录删除的实例表列名
 * columnMapping:[{configColumnId:1,instColumnName:""}...]://记录其列映射（从配置到实例之间的列映射）
 */
var geDiffColumData = function () {
    var modifyColumn = [];
    var addColumn = [];
    var deleteColumn = [];
    var columnMapping = [];
    diffAnalysisGrid.forEachRow(function (id) {
        var isHidden = (true == diffAnalysisGrid.getUserData(id, "hidden"));
        if (!isHidden) {
            var diffType = diffAnalysisGrid.getUserData(id, "diff");
            if (diffType) { //如果有差异
                switch (diffType) {
                    case "1":
                    {//新增列
                        addColumn.push(diffAnalysisGrid.getUserData(id, "configColumnId"));
                        break;
                    }
                    case "2":
                    {//删除列
                        deleteColumn.push(diffAnalysisGrid.getUserData(id, "instColumnName"));
                        break;
                    }
                    default:
                    {//修改列
                        modifyColumn.push({configColumnId:diffAnalysisGrid.getUserData(id, "configColumnId"),
                            instColumnName:diffAnalysisGrid.getUserData(id, "instColumnName")});
                        break;
                    }
                }
            }
            if (diffAnalysisGrid.getUserData(id, "configColumnId")
                && diffAnalysisGrid.getUserData(id, "instColumnName") != "--") {
                columnMapping.push({configColumnId:diffAnalysisGrid.getUserData(id, "configColumnId"),
                    instColumnName:diffAnalysisGrid.getUserData(id, "instColumnName")});
            }
        }
    });
    var result = {};
    modifyColumn.length > 0 && (result.modifyColumn = modifyColumn);
    addColumn.length > 0 && (result.addColumn = addColumn);
    deleteColumn.length > 0 && (result.deleteColumn = deleteColumn);
    result.columnMapping = columnMapping;
    return result;
}
/**
 * 同步结构SQL action定义
 */
dwrCaller.addAutoAction("synchronousStructSql", DiffAction.synchronousStructSql, tableId, tableVersion);
//执行一段SQL语句Action定义
dwrCaller.addAutoAction("executeSQL", DiffAction.executeSQL, {
    dwrConfig:true,
    isDealOneParam:false,
    isShowProcess:false
});
/**
 * 同步成功之后Action定义
 */
dwrCaller.addAutoAction("afterSync", DiffAction.afterSync, tableId, tableVersion);
/**
 * 查询具体差异信息。
 */
var showSysStructWindow = function () {
//    if($("_diffCount").innerHTML=="0"){
//        dhx.alert("无差异，无需同步！");
//        return;
//    }
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("showDiffWindow", 0, 0, 500, 220);
    var showDiffWindow = dhxWindow.window("showDiffWindow");
    showDiffWindow.setModal(true);
    showDiffWindow.stick();
    showDiffWindow.setDimension(500, 300);
    showDiffWindow.center();
    showDiffWindow.denyResize();
    showDiffWindow.denyPark();
    //关闭一些不用的按钮。
    showDiffWindow.button("minmax1").hide();
    showDiffWindow.button("park").hide();
    showDiffWindow.button("stick").hide();
    showDiffWindow.button("sticked").hide();
    showDiffWindow.setText("仅同步表结构SQL");
    showDiffWindow.keepInViewport(true);
    showDiffWindow.show();
    //layout
    var diffLayout = showDiffWindow.attachLayout("2E");
    diffLayout.cells("a").setHeight("250");
    diffLayout.cells("a").fixSize(true, true);
    diffLayout.cells("b").hideHeader();
    diffLayout.hideSpliter();
    diffLayout.hideConcentrate();
    diffLayout.cells("a").hideHeader();
    var div = document.createElement("div");
    div.style.cssText = "width:99%;height:100%;padding:0px;margin:0px;";
    div.innerHTML = '<table style="width: 100%;height:100%;" cellpadding="0" cellspacing="0"><tr><td><textarea rows="13" cols="3" style="width:100%;height: 100%;" id="_executeSQL"></textarea></td></tr></table>'
    diffLayout.cells("a").attachObject(div);
    var instName = dwr.util.getText("_insts");
    instName = (instName.indexOf(".") == -1) ? instName : instName.split(".")[1];
    dwrCaller.executeAction("synchronousStructSql", instName, function (sql) {
        $("_executeSQL").value = sql.replace(/<br\/>/gi, "\r\n").replace(/<\/br>/gi, "\r\n")
//        dwr.util.setValue("_executeSQL",sql.replace(/<br\/>/gi,"\r\n").replace(/<\/br>/gi,"\r\n"));
    })
//    showDiffWindow.attachEvent("onClose", function(win){
//        dhxWindow.unload();
//    });

    var buttonForm = diffLayout.cells("b").attachForm([
        {type:"settings", position:"label-left", labelWidth:70, inputWidth:120},
        {type:"button", value:"执行", name:"execute", offsetLeft:200},
        {type:"newcolumn"},
        {type:"button", value:"关闭", name:"close"}
    ]);
    buttonForm.attachEvent("onButtonClick", function (id) {
        if (id == "execute") {
            dhx.confirm("此操作会将数据全部删除，您确定要进行同步吗？", function (r) {
                if (r) {
                    var statusWindow = null;
                    var winDiv = null;
                    //获取到要执行的SQL
                    var sqls = dwr.util.getValue("_executeSQL");
                    //sql根据;分段。
                    sqls = sqls.split(";");
                    if (!sqls[sqls.length - 1] || Tools.trim(sqls[sqls.length - 1]) == "") {
                        sqls.splice(sqls.length - 1, 1);
                    }
                    if (!statusWindow) {
                        dhxWindow.createWindow("statusWindow", 0, 0, 400, 220);
                        statusWindow = dhxWindow.window("statusWindow");
                        statusWindow.setModal(true);
                        statusWindow.stick();
                        statusWindow.setDimension(650, 400);
                        statusWindow.center();
                        statusWindow.denyResize();
                        statusWindow.denyPark();
                        //关闭一些不用的按钮。
                        statusWindow.button("minmax1").hide();
                        statusWindow.button("park").hide();
                        statusWindow.button("stick").hide();
                        statusWindow.button("sticked").hide();
                        statusWindow.button("close").hide();
                        statusWindow.setText("SQL执行情况");
                        statusWindow.keepInViewport(true);
                        winDiv = document.createElement("div");
                        winDiv.style.width = "680px";
                        winDiv.style.height = "360px";
                        winDiv.style.position = "relative";
                        winDiv.style.overflow = "auto";
                        winDiv.innerHTML = "SQL开始执行...</br>";
                        statusWindow.attachObject(winDiv);
                        statusWindow.show();
                    }
                    var isSycSuccess = true;
                    var executeSql = function (i) {
                        winDiv.innerHTML += "正在执行SQL:" + (i == 0 ? "</br>" : "");
                        winDiv.innerHTML += sqls[i].replace(/\r\n/g, "</br>").replace(/\n/g, "</br>") + "</br>"
                        dwrCaller.executeAction("executeSQL", sqls[i], dataSourceInfo, function (data) {
                            if (data.result) {//执行SQl，
                                winDiv.innerHTML += ((i == sqls.length - 1) ? "SQL执行完成！</br>" : "此段SQL执行成功，准备执行下一段SQL</br>");
                                if (i != sqls.length - 1) {
                                    executeSql(i + 1);
                                } else {
                                    statusWindow.button("close").show();
                                }
                            } else {
                                winDiv.innerHTML += "SQL执行出错，程序异常退出，出错信息:</br>" + data.errorMessage;
                                isSycSuccess = false;
                                if (i != sqls.length - 1) {
                                    dhx.confirm("当前SQL执行出错，是否继续执行后面SQL?", function (r) {
                                        if (r) {
                                            executeSql(i + 1);
                                        } else {
                                            statusWindow.button("close").show();
                                        }
                                    })
                                } else {
                                    statusWindow.button("close").show();
                                }
                            }
                            winDiv.scrollTop = winDiv.scrollHeight;
                            //SQL全部执行完成之后的操作
                            if (i == sqls.length - 1) {
                                if (isSycSuccess) {//成功执行后关掉主窗口。
                                    showDiffWindow.close();
                                    dwrCaller.executeAction("afterSync", dwr.util.getValue("_insts"));
                                    statusWindow.attachEvent("onClose", function () {
                                        window.location.reload();
                                    });
                                }
                            }
                        });
                    }
                    executeSql(0);
                }
            });
        } else if (id == "close") {
            showDiffWindow.close();
        }
    })
}
/**
 * 是否正在执行同步操作。
 */
var isOnSys = false;
/**
 * 同步数据与结构Action定义
 */
dwrCaller.addAutoAction("synchronousStructDataSql", DiffAction.synchronousStructDataSql, tableId, tableVersion, {
    dwrConfig:true,
    isDealOneParam:false
});
/**
 * 展示同步结构与数据window
 */
var showSysStructAndDataWindow = function () {
    var diffData = geDiffColumData();
//    if($("_diffCount").innerHTML=="0"){
//        dhx.alert("无差异，无需同步！");
//        return;
//    }
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("showDiffWindow", 0, 0, 400, 220);
    var showDiffWindow = dhxWindow.window("showDiffWindow");
    showDiffWindow.setModal(true);
    showDiffWindow.stick();
    showDiffWindow.setDimension(500, 300);
    showDiffWindow.center();
    showDiffWindow.denyResize();
    showDiffWindow.denyPark();
    //关闭一些不用的按钮。
    showDiffWindow.button("minmax1").hide();
    showDiffWindow.button("park").hide();
    showDiffWindow.button("stick").hide();
    showDiffWindow.button("sticked").hide();
    showDiffWindow.setText("同步表结构&数据SQL");
    showDiffWindow.keepInViewport(true);
    showDiffWindow.show();
    //layout
    var diffLayout = showDiffWindow.attachLayout("2E");
    diffLayout.cells("a").setHeight("250");
    diffLayout.cells("a").fixSize(true, true);
    diffLayout.cells("b").hideHeader();
    diffLayout.hideSpliter();
    diffLayout.hideConcentrate();
    diffLayout.cells("a").hideHeader();
    var div = document.createElement("div");
    div.style.cssText = "width:100%;height:100%";
    div.innerHTML = '<table style="width: 100%;height: 100%" cellpadding="0" cellspacing="0"><tr>' +
        '<td style="width:90%"><textarea rows="13" cols="3" style="width: 98%;height: 98%" id="_executeSQL" readonly="true"></textarea></td></tr></table>'
    diffLayout.cells("a").attachObject(div);
    var instName = dwr.util.getText("_insts");
    instName = (instName.indexOf(".") == -1) ? instName : instName.split(".")[1];
    var operateTableName = null;
    var backTableName = null;
    dwrCaller.executeAction("synchronousStructDataSql", instName, diffData, function (data) {
        if (data && data.length) {
            dwr.util.setValue("_executeSQL", data[0].replace(/<br\/>/gi, "\r\n").replace(/<\/br>/gi, "\r\n"));
            operateTableName = data[1];
            backTableName = data[2];
        } else {
            dhx.alert("生成同步SQL失败");
        }
    })
//    showDiffWindow.attachEvent("onClose", function(win){
//        dhxWindow.unload();
//    });

    var buttonForm = diffLayout.cells("b").attachForm([
        {type:"settings", position:"label-left", labelWidth:70, inputWidth:120},
        {type:"button", value:"执行", name:"execute", offsetLeft:300},
        {type:"newcolumn"},
        {type:"button", value:"关闭", name:"close"}
    ]);
    buttonForm.attachEvent("onButtonClick", function (id) {
        if (id == "execute") {
            dhx.confirm("您确定要进行同步吗？", function (r) {
                if (r) {
                    isOnSys = true;
                    var statusWindow = null;
                    var winDiv = null;
                    //获取到要执行的SQL
                    var sqls = dwr.util.getValue("_executeSQL");
                    //sql根据;分段。
                    sqls = sqls.split(";");
                    if (!sqls[sqls.length - 1] || Tools.trim(sqls[sqls.length - 1]) == "") {
                        sqls.splice(sqls.length - 1, 1);
                    }
                    if (!statusWindow) {
                        dhxWindow.createWindow("statusWindow", 0, 0, 400, 220);
                        statusWindow = dhxWindow.window("statusWindow");
                        statusWindow.setModal(true);
                        statusWindow.stick();
                        statusWindow.setDimension(700, 400);
                        statusWindow.center();
                        statusWindow.denyResize();
                        statusWindow.denyPark();
                        //关闭一些不用的按钮。
                        statusWindow.button("minmax1").hide();
                        statusWindow.button("park").hide();
                        statusWindow.button("stick").hide();
                        statusWindow.button("sticked").hide();
                        statusWindow.button("close").hide();
                        statusWindow.setText("SQL执行情况");
                        statusWindow.keepInViewport(true);
                        statusWindow.show();

                        winDiv = document.createElement("div");
                        winDiv.style.width = "680px";
                        winDiv.style.height = "360px";
                        winDiv.style.position = "relative";
                        winDiv.style.overflow = "auto";
                        winDiv.innerHTML = "SQL开始执行...</br>";
                        statusWindow.attachObject(winDiv);
                    }
                    var isSycSuccess = true;
                    var executeSql = function (i) {
                        winDiv.innerHTML += "正在执行SQL:" + (i == 0 ? "</br>" : "");
                        winDiv.innerHTML += sqls[i].replace(/\r\n/g, "</br>").replace(/\n/g, "</br>") + "</br>"
                        dwrCaller.executeAction("executeSQL", sqls[i], dataSourceInfo, function (data) {
                            if (data.result) {//执行SQl，
                                winDiv.innerHTML += ((i == sqls.length - 1) ? "SQL执行完成！</br>" : "此段SQL执行成功，准备执行下一段SQL</br>");
                                if (i != sqls.length - 1) {
                                    executeSql(i + 1);
                                } else {
                                    isOnSys = false;
                                    statusWindow.button("close").show();
                                }
                            } else {
                                winDiv.innerHTML += "SQL执行出错，程序异常退出，出错信息:</br>" + data.errorMessage + "</br>";
                                isSycSuccess = false;
                                statusWindow.button("close").show();
                                winDiv.innerHTML += "程序正在执行回退...";
                                //执行回退。
                                DiffAction.rollBack([operateTableName, backTableName], dataSourceInfo);
                                winDiv.innerHTML += "回退完成...";
                                isOnSys = false;
                            }
                            winDiv.scrollTop = winDiv.scrollHeight;
                            //SQL全部执行完成之后的操作
                            if (i == sqls.length - 1) {
                                if (isSycSuccess) {//成功执行后关掉主窗口。
                                    showDiffWindow.close();
                                    dwrCaller.executeAction("afterSync", dwr.util.getValue("_insts"));
                                    statusWindow.attachEvent("onClose", function () {
                                        window.location.reload();
                                    });
                                }
                            }
                        });
                    }
                    executeSql(0);
                }
            });
        } else if (id == "close") {

            showDiffWindow.close();
        }
    })
}
window.parent.unloadOnClose = function () {
    if (isOnSys) {
        dhx.alert("正在进行数据同步，不能关闭！");
        return false;
    }
    return true;
}
dhx.ready(diffAnalysisInit);
