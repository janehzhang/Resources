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
 *       2011-11-29
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
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();
var flow;
var linkData={};

var maintainConfig = {
    idColumnName:"tableId",
    filterColumns:["tableName","tableNameCn"],
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData) {
        var userData = {};
        userData.tableVersion=rowData.tableVersion;
        return userData;
    },
    /**
     * 获取下拉框Button的值
     * @param rowIndex
     * @param columnIndex
     * @param cellValue
     * @param rowData
     */
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
        if(rowData["related"]){
            return {style:"color: #808080;font-style: italic;text-decoration: line-through",value:cellValue};
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};
/**
 * 数据展现转换器
 */
var maintainConverter = new dhtmxGridDataConverter(maintainConfig);

//注册事件：根据表ID取表信息
dwrCaller.addAutoAction("queryTableByTableId",MaintainRelAction.queryTableByTableId,{dwrConfig:true,async:false});

//表列信息相关Action以及数据转换器 start
var colsConfig = {
    filterColumns:["colName","colDatatype","colNullabled","defaultVal","isPrimary","colBusComment","thisdim"],
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData) {
        var userData = {};
        return userData;
    }
};
var colsConverter = new dhtmxGridDataConverter(colsConfig);
dwrCaller.addAutoAction("queryColsByTableIdNode", "TableViewAction.queryColsByTableIdNode");
dwrCaller.addDataConverter("queryColsByTableIdNode", colsConverter);

//获取关联关系列表数据
var getRelData=function(){
    //列字段数据
    var columnData = [];
    var tableObj = $("_relContentTable");
    for(var i = 1; i < tableObj.rows.length ; i++){
        var rowData = {};
        for(var j = 0; j < tableObj.rows[i].cells.length; j++){
            var elements = tableObj.rows[i].cells[j].getElementsByTagName("input");
            for(var z = 0; z < elements.length; z++){
                if([elements[z].name]){
                    rowData[elements[z].name] = dwr.util.getValue(elements[z]);
                }
            }
        }
        columnData.push(rowData);
    }
    return columnData;
};

//表格形式维护血缘关系
dwrCaller.addAutoAction("queryRelForGrid", TableRelAction.queryRelForGrid);
//显示关系流程图Action
dwrCaller.addAutoAction("queryToFlow","TableRelAction.queryToFlow");

/**
 * 加载列树
 */
dwrCaller.addAutoAction("queryColTree",MaintainRelAction.queryColTree,{
    dwrConfig:true,
    isShowProcess:false
});
var colTreeConverter=new dhtmxTreeDataConverter({
    idColumn:"colId",pidColumn:0,
    isDycload:false,textColumn:"colName"
});
dwrCaller.addDataConverter("queryColTree",colTreeConverter);

/**
 * 添加查询表分类Action
 */
dwrCaller.addAutoAction("queryTableGroup", TableApplyAction.queryTableGroup);
dwrCaller.addDataConverter("queryTableGroup", new dhtmxTreeDataConverter({
    idColumn:"tableGroupId",pidColumn:"parGroupId",textColumn:"tableGroupName",
    isDycload:false
}));
dwrCaller.isShowProcess("queryTableGroup", false);

var treeData = {};
var lastTableType = null;//最后一次显示时的tableType.

var loadTableGroupTree = function(target, hide){
    //创建tree Div层
    var div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
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
    tree.attachEvent("onDblClick", function(nodeId){
        target.value = tree.getItemText(nodeId);
        hide.value = nodeId;
        //关闭树
        div.style.display = "none";
    });
    this.tree = tree
    this.showGroupTree = function(tableType){
        var show = function(){
            if(lastTableType != tableType){
                var childs=tree.getAllSubItems(0);
                if(childs){
                    if(isNaN(childs)){
                        var childIds=childs.split(",");
                        for(var i=0;i<childIds.length;i++){
                            tree.deleteItem(childIds[i]);
                        }
                    }else{
                        tree.deleteItem(childs);
                    }
                }
                tree.loadJSONObject(treeData[tableType]);
            }
        }
        if(!treeData[tableType]){
            dwrCaller.executeAction("queryTableGroup", tableType, function(data){
                treeData[tableType] = data;
                show();
                lastTableType=tableType;
            })
        } else{
            show();
            lastTableType=tableType;
        }
    }
};

//设置每行关联的表的表名，列名
var setRowTCValue=function(row_Index,tableData,onlyId){
    var colsStr2="";
    var colsIds2="";
    var colsIds1="";
    var colsStr1="";
    var colsType1="";//列1类型
    var colsType2="";//列2类型

    //列字段数据
    var columnData = [];
    var tableObj = $("_newRelContentTable_Grid_"+onlyId);
    for(var i = 1; i < tableObj.rows.length ; i++){
        var rowData = {};
        for(var j = 0; j < tableObj.rows[i].cells.length; j++){
            var elements = tableObj.rows[i].cells[j].getElementsByTagName("input");
            for(var z = 0; z < elements.length; z++){
                if([elements[z].name]){
                    rowData[elements[z].name] = dwr.util.getValue(elements[z]);
                }
            }
        }
        columnData.push(rowData);
    }
    for(var i=0; i < columnData.length; i++){
        colsStr2 = colsStr2 + columnData[i].newColinfo2 + ",";
        colsIds2 = colsIds2 + columnData[i].newColinfo2Id + ",";
        colsIds1 = colsIds1 + columnData[i].newColinfo1Id + ",";
        colsStr1 = colsStr1 + columnData[i].newColinfo1 + ",";
        colsType1 = colsType1 + columnData[i].newColinfo1Type + ",";
        colsType2 = colsType2 + columnData[i].newColinfo2Type + ",";
        if(i==columnData.length-1){
            colsStr2 = colsStr2.substring(0,colsStr2.length-1);
            colsIds2 = colsIds2.substring(0,colsIds2.length-1);
            colsIds1 = colsIds1.substring(0,colsIds1.length-1);
            colsStr1 = colsStr1.substring(0,colsStr1.length-1);
            colsType1 = colsType1.substring(0, colsType1.length-1);
            colsType2 = colsType2.substring(0, colsType2.length-1);
        }
    }
    var colinfo1TypeArray = colsType1.split(",");
    var colinfo2TypeArray = colsType2.split(",");
    if(colinfo2TypeArray.length==0||colinfo1TypeArray.length==0){
        dhx.alert("请将列信息填写完整！");
        return false;
    }
    if(colinfo2TypeArray.length != colinfo1TypeArray.length){
        dhx.alert("请将列信息填写完整！");
        return false;
    }
    //添加判断
    for(var kk = 0; kk < colinfo2TypeArray.length; kk++){
        if(colinfo2TypeArray[kk]==""||colinfo1TypeArray[kk]==""
            ||colinfo1TypeArray[kk]=="undefined"||colinfo2TypeArray[kk]=="undefined"){
            dhx.alert("请将列信息填写完整！");
            return false;
        }
        if(colinfo2TypeArray[kk]=="DATE"){
            dhx.alert("列"+(colsStr2.split(","))[kk]+"是DATE类型的列，不能设置关联！");
            return false;
        }
        if(colinfo1TypeArray[kk]=="DATE"){
            dhx.alert("列"+(colsStr1.split(","))[kk]+"是DATE类型的列，不能设置关联！");
            return false;
        }
        if((colinfo2TypeArray[kk]=="NUMBER"&&colinfo1TypeArray[kk]!="NUMBER")
            ||(colinfo1TypeArray[kk]=="NUMBER"&&colinfo2TypeArray[kk]!="NUMBER")){

            dhx.alert("列"+(colsStr2.split(","))[kk]+"数据类型为"+colinfo2TypeArray[kk]+"，不能与列"+(colsStr1.split(","))[kk]+"数据类型为"+colinfo1TypeArray[kk]+"相关联！");
            return false;
        }
    }

    $("_t2name"+row_Index).value=tableData.tableName;
    $("_t2nameId"+row_Index).value=tableData.tableId;
    $("_colinfo2"+row_Index).value=colsStr2;
    $("_colinfo2Id"+row_Index).value=colsIds2;
    $("_colinfo1"+row_Index).value=colsStr1;
    $("_colinfo1Id"+row_Index).value=colsIds1;
    return true;
};

/**
 * 查询列信息Action定义
 */
dwrCaller.addAutoAction("queryColumnsByTableID","TableApplyAction.queryColumnsByTableID");
dwrCaller.addDataConverter("queryColumnsByTableID", new dhtmxGridDataConverter({
    idColumnName:"colId",
    filterColumns:["","colName","colDatatype","colNullabled","isPrimary","defaultVal","colBusType","colBusComment"],
    userData:function(rowIndex, rowData){
        return rowData;
    }
}));

/**
 * 系统表下拉数据
 */
var tableTypeData = null;
var tableRelTypeData = null;

//是否已加载过关系类型下拉列表
var isInitRelTypeDate = false;

/**
 * 查询业务表类信息Action定义
 */
dwrCaller.addAutoAction("queryBusinessTables","TableApplyAction.queryBusinessTables");
dwrCaller.addDataConverter("queryBusinessTables", new dhtmxGridDataConverter({
    idColumnName:"tableId",
    filterColumns:["","tableName","tableNameCn","tableBusComment"],
    userData:function(rowIndex, rowData){
        return rowData;
    }
}));

var importWindowRel=null;
//从元数据表类复制
var importFormTableRel=function(img){
//    alert($("_t2name"+img.parentNode.parentNode._index).value);
//    alert($("_colinfo2"+img.parentNode.parentNode._index).value);
    var row_index=img.parentNode.parentNode._index;
    var queryTableGrid=null;
    //参数收集，参数来源于查询表单_queryDimForm
    var param = new biDwrMethodParam([
        {
            index:0,type: "ui",value:"_queryTablesRel"
        }
    ]);
    //查询。
    var queryTables = function(){
        queryTableGrid.clearAll();
//        queryColumnGrid.clearAll();
        queryTableGrid.load(dwrCaller.queryBusinessTables + param, "json");
        dhtmlxValidation.clearAllTip();
    }
    if(!importWindowRel){
        //加载所有的表类型。
//        dwrCaller.executeAction("queryTableType", function(data){
//            data=data.options||data;
//            alert(data);
            //层次分类、业务类型数据及联动关系设置
            Tools.addOption("_querytableTypeRel", getComboByRemoveValue("TABLE_TYPE"));
//        });
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("importWindowRel", 0, 0, 450, 400);
        importWindowRel = dhxWindow.window("importWindowRel");
        importWindowRel.stick();
        importWindowRel.setModal(true);
        importWindowRel.setDimension(600, 450);
        importWindowRel.button("minmax1").hide();
        importWindowRel.button("park").hide();
        importWindowRel.button("stick").hide();
        importWindowRel.button("sticked").hide();
        importWindowRel.center();
        importWindowRel.denyResize();
        importWindowRel.denyPark();
        importWindowRel.setText("导入表类信息");
        importWindowRel.keepInViewport(true);
        //添加查询内容
        var layout = new dhtmlXLayoutObject(importWindowRel, "2E");
        layout.cells("a").setHeight(220);
        layout.cells("a").fixSize(false, true);
        layout.cells("a").firstChild.style.height = (layout.cells("a").firstChild.offsetHeight + 5) + "px";
        layout.cells("a").hideHeader();
        layout.cells("b").hideHeader();
        layout.cells("a").attachObject("_importFromTableRel");
        //添加查询按钮，
        var queryButton = Tools.getButtonNode("查询");
        queryButton.onclick=queryTables;
        $("_queryTableButtonRel").appendChild(queryButton);


        //业务类型下拉树数据准备。
        var tableGroupTree = new loadTableGroupTree($("_queryTableGroupRel"), $("_queryTableGroupIdRel"));
        //为_tableGroup添加事件
        Tools.addEvent($("_queryTableGroupRel"), "click", function(){
            var tableType = dwr.util.getValue("_querytableTypeRel");
            if(!tableType){
                dhx.alert("请您选选择一个层次分类！");
                return;
            }
            tableGroupTree.treeDiv.style.width = $("_queryTableGroupRel").offsetWidth + 'px';
            Tools.divPromptCtrl(tableGroupTree.treeDiv, $("_queryTableGroupRel"), true);
            tableGroupTree.treeDiv.style.display = "block";
            tableGroupTree.showGroupTree(tableType);
        });
        Tools.addEvent($("_querytableTypeRel"), "change", function(){
            $("_queryTableGroupRel").value="";
        });

        queryTableGrid = new dhtmlXGridObject("_queryTableGridRel");
        queryTableGrid.setHeader(",被关联表名,中文名,注释");
        queryTableGrid.setInitWidthsP("6,30,30,34");
        queryTableGrid.setColAlign("center,left,left,left");
        queryTableGrid.setHeaderAlign("center,center,center,center");
        queryTableGrid.setColTypes("ra,ro,ro,ro");
        queryTableGrid.setColSorting("na,na,na,na");
        queryTableGrid.setEditable(true);
        queryTableGrid.enableMultiselect(false);
        queryTableGrid.load(dwrCaller.queryBusinessTables + param,"json");
        queryTableGrid.defaultPaging(5);
        queryTableGrid.enableCtrlC();
        queryTableGrid.init();

        //添加radio点击事件。查询其列字段
        queryTableGrid.attachEvent("onCheck", function(rId, cInd, state){
            if(state){
                tableId1 = tableId;
                tableId2 = rId;
                var onlyId = tableId+"_"+rId+"_"+importWindowRel._index;
                var rowData= queryTableGrid.getRowData(rId);
                var newDiv = document.createElement("div");
                newDiv.style.cssText="height:100%;width:100%";
                newDiv.innerHTML=$("newLinkDetail_Grid").innerHTML.replace(/\{index\}/g,onlyId);

                for(var i = 0; i < newDiv.childNodes.length; i++){
                    if(newDiv.childNodes[i].nodeName.toLowerCase()=="form"){
                        var tmpLayout=layout.cells("b").attachLayout("1C");
                        tmpLayout.cells("a").hideHeader();
                        tmpLayout.cells("a").attachObject(newDiv.childNodes[i]);
                        if(dhx.env.ie!=7.0){
                            $("_newRelColumnHeadTable_"+onlyId).style.width= $("_newRelContentTable_Div_"+onlyId).clientWidth+"px";
                        }
                        //设置CSS
                        dhx.html.addCss($("_newRelTableDiv_Grid_"+onlyId), global.css.gridTopDiv);
                        $("_headOperate_Grid_"+onlyId).innerHTML='<img src="../../../resource/images/edit_add.png" title="增加" onclick="newAddRelColumnRow(null,true,\''+onlyId+'\',true)" style="width:16px;height: 16px;cursor: pointer">';
                        $("_CurrentCol_"+onlyId).innerHTML = tableName+"列";
                        if(gridDatas&&gridDatas[importWindowRel._index-1]&&rId==gridDatas[importWindowRel._index-1].TABLE_ID2){//有数据
                            var colId1Array = gridDatas[importWindowRel._index-1].TABLE_ID1_COL_IDS.split(",");
                            var colId2Array = gridDatas[importWindowRel._index-1].TABLE_ID2_COL_IDS.split(",");
                            if(colId1Array.length>0&&colId1Array.length==colId2Array.length){
                                if(colId1Array[0]==""&&colId2Array[0]==""&&colId1Array.length==1){
                                    newAddRelColumnRow(null,true,onlyId,true);
                                }else{
                                    newAddRelColumnRow(null,false,onlyId,true);
                                }
                                for(var j=0;j<colId1Array.length;j++){

                                    var oneData={};//数据格式：{colId1:"",colName1:"",colId2:"",colName2:"",colType1:"",colType2:""}
                                    oneData.colId1=colId1Array[j];
                                    oneData.colName1=gridDatas[importWindowRel._index-1].COLINFO1.split(",")[j];
                                    oneData.colType1=gridDatas[importWindowRel._index-1].COLTYPE1.split(",")[j];
                                    oneData.colId2=colId2Array[j];
                                    oneData.colName2=gridDatas[importWindowRel._index-1].COLINFO2.split(",")[j];
                                    oneData.colType2=gridDatas[importWindowRel._index-1].COLTYPE2.split(",")[j];
                                    newRelCopyColumnData(oneData,newRelColumnNum,onlyId);


                                    if(j==colId1Array.length-1){
//                                    addRelColumnRow(null, true);
                                    }else{
                                        //增加行操作
                                        newAddRelColumnRow(null,false,onlyId,true);
                                    }

                                }
                            }else{
                                newAddRelColumnRow(null,true,onlyId,true);
                            }
                        }else{
                            newAddRelColumnRow(null,true,onlyId,true);
                        }

                        var confirmButton = Tools.getButtonNode("确定",getBasePath()+'/meta/resource/images/ok.png');
                        confirmButton.style.marginLeft='190px';
                        confirmButton.style.cssFloat='left';
                        confirmButton.style.styleFloat='left';
                        confirmButton.onclick = function() {
                            // 确定保存主表Grid
                            var tableData=queryTableGrid.getRowData(rId).userdata;
                            if(setRowTCValue(importWindowRel._index,tableData,onlyId)){
                                importWindowRel.close();
                            }
                        };
                        var cancelButton = Tools.getButtonNode("取消",getBasePath()+'/meta/resource/images/cancel.png');
                        cancelButton.style.cssFloat='left';
                        cancelButton.style.styleFloat='left';
                        cancelButton.onclick = function() {
                            importWindowRel.close();
                        };

                        $("_newConfirm_Grid_"+onlyId).appendChild(confirmButton);
                        $("_newConfirm_Grid_"+onlyId).appendChild(cancelButton);

                        break;
                    }
                }
            }
        });

        queryTableGrid.attachEvent("onRowSelect", function(rId,ind){
            queryTableGrid.cells(rId,0).setValue(1);
            tableId1 = tableId;
            tableId2 = rId;
            var onlyId = tableId+"_"+rId+"_"+importWindowRel._index;
            var rowData= queryTableGrid.getRowData(rId);
            var newDiv = document.createElement("div");
            newDiv.style.cssText="height:100%;width:100%";
            newDiv.innerHTML=$("newLinkDetail_Grid").innerHTML.replace(/\{index\}/g,onlyId);

            for(var i = 0; i < newDiv.childNodes.length; i++){
                if(newDiv.childNodes[i].nodeName.toLowerCase()=="form"){
                    var tmpLayout=layout.cells("b").attachLayout("1C");
                    tmpLayout.cells("a").hideHeader();
                    tmpLayout.cells("a").attachObject(newDiv.childNodes[i]);
                    if(dhx.env.ie!=7.0){
                        $("_newRelColumnHeadTable_"+onlyId).style.width= $("_newRelContentTable_Div_"+onlyId).clientWidth+"px";
                    }
                    //设置CSS

                    dhx.html.addCss($("_newRelTableDiv_Grid_"+onlyId), global.css.gridTopDiv);
                    $("_headOperate_Grid_"+onlyId).innerHTML='<img src="../../../resource/images/edit_add.png" title="增加" onclick="newAddRelColumnRow(null,true,\''+onlyId+'\',true)" style="width:16px;height: 16px;cursor: pointer">';
//                        alert(gridDatas[importWindowRel._index-1]);
                    $("_CurrentCol_"+onlyId).innerHTML = tableName+"列";
                    if(gridDatas&&gridDatas[importWindowRel._index-1]&&rId==gridDatas[importWindowRel._index-1].TABLE_ID2){//有数据
                        var colId1Array = gridDatas[importWindowRel._index-1].TABLE_ID1_COL_IDS.split(",");
                        var colId2Array = gridDatas[importWindowRel._index-1].TABLE_ID2_COL_IDS.split(",");
                        if(colId1Array.length>0&&colId1Array.length==colId2Array.length){
                            if(colId1Array[0]==""&&colId2Array[0]==""&&colId1Array.length==1){
                                newAddRelColumnRow(null,true,onlyId,true);
                            }else{
                                newAddRelColumnRow(null,false,onlyId,true);
                            }
                            for(var j=0;j<colId1Array.length;j++){

                                var oneData={};//数据格式：{colId1:"",colName1:"",colId2:"",colName2:"",colType1:"",colType2:""}
                                oneData.colId1=colId1Array[j];
                                oneData.colName1=gridDatas[importWindowRel._index-1].COLINFO1.split(",")[j];
                                oneData.colType1=gridDatas[importWindowRel._index-1].COLTYPE1.split(",")[j];
                                oneData.colId2=colId2Array[j];
                                oneData.colName2=gridDatas[importWindowRel._index-1].COLINFO2.split(",")[j];
                                oneData.colType2=gridDatas[importWindowRel._index-1].COLTYPE2.split(",")[j];
                                newRelCopyColumnData(oneData,newRelColumnNum,onlyId);


                                if(j==colId1Array.length-1){
//                                    addRelColumnRow(null, true);
                                }else{
                                    //增加行操作
                                    newAddRelColumnRow(null,false,onlyId,true);
                                }

                            }
                        }else{
                            newAddRelColumnRow(null,true,onlyId,true);
                        }
                    }else{
                        newAddRelColumnRow(null,true,onlyId,true);
                    }

                    var confirmButton = Tools.getButtonNode("确定",getBasePath()+'/meta/resource/images/ok.png');
                    confirmButton.style.marginLeft='190px';
                    confirmButton.style.cssFloat='left';
                    confirmButton.style.styleFloat='left';
                    confirmButton.onclick = function() {
                        // 确定保存主表Grid
                        var tableData=queryTableGrid.getRowData(rId).userdata;
                        if(setRowTCValue(importWindowRel._index,tableData,onlyId)){
                            importWindowRel.close();
                        }
                    };
                    var cancelButton = Tools.getButtonNode("取消",getBasePath()+'/meta/resource/images/cancel.png');
                    cancelButton.style.cssFloat='left';
                    cancelButton.style.styleFloat='left';
                    cancelButton.onclick = function() {
                        importWindowRel.close();
                    };

                    $("_newConfirm_Grid_"+onlyId).appendChild(confirmButton);
                    $("_newConfirm_Grid_"+onlyId).appendChild(cancelButton);

                    break;
                }
            }
        });


        layout.hideConcentrate();
        layout.hideSpliter();//移除分界边框。
        importWindowRel.show();
        importWindowRel.attachEvent("onClose", function(){
            importWindowRel.hide();
            importWindowRel.setModal(false);
            return false;
        })
    }
    importWindowRel._index=row_index;
    importWindowRel.setModal(true);
    importWindowRel.show();
};


var relColumnNum = 0;//关联关系表格行索引
var relTypeCombo = {};//关联关系类型
//添加一行关联信息
var addRelColumnRow = function(rowIndex,isNullTree){
    rowIndex = (rowIndex == undefined || rowIndex == null) ? (++relColumnNum) : rowIndex;
    if(!$("_relColumnRow" + rowIndex)){
        if(relColumnNum < rowIndex){
            relColumnNum = rowIndex;
        }
        var tr = document.createElement("tr");
        tr._index = rowIndex;
        tr.id = "_relColumnRow" + rowIndex;
        $("_relContentBody").appendChild(tr);

        //关联表类名
        var td = tr.insertCell(0);
        td.align = "center";
        var input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"t2name",id:"_t2name" +
                rowIndex,type:"TEXT",style:"width: 85%;",readOnly:true});
        var inputId = dhx.html.create("input", {name:"t2nameId", id:"_t2nameId"+rowIndex, type:"HIDDEN"});
        td.appendChild(input);
        td.appendChild(inputId);
        td.innerHTML += '&nbsp;<img src="../../../resource/images/tableImport.png" title="从元数据表类复制" onclick="importFormTableRel(this)">';

        //关联类型
        td = tr.insertCell(1);
        td.align = "center";
        var inputDiv = dhx.html.create("select", {style:"width:80%",id:"_relType" + rowIndex,name:"relType"});
        td.appendChild(inputDiv);
        var combo = new dhtmlXCombo(inputDiv);
        combo.addOption(tableRelTypeData.options);
        combo.selectOption(1,true,true);
        relTypeCombo[rowIndex]=combo;

        //列名
        td = tr.insertCell(2);
        td.align = "center";
        var input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"colinfo1",id:"_colinfo1" +
            rowIndex,type:"TEXT",style:"width: 90%;",readOnly:true});
        var inputId = dhx.html.create("input", {name:"colinfo1Id", id:"_colinfo1Id"+rowIndex, type:"HIDDEN"});
        td.appendChild(input);
        td.appendChild(inputId);
        if(isNullTree){
//            newQueryColTree(tableId,$("_colinfo1"+rowIndex),null,$("_colinfo1Id"+rowIndex));
        }


        //关联列名
        td = tr.insertCell(3);
        td.align = "center";
        var input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"colinfo2",id:"_colinfo2" +
                rowIndex,type:"TEXT",style:"width: 90%;",readOnly:true});
        var inputId = dhx.html.create("input", {name:"colinfo2Id", id:"_colinfo2Id"+rowIndex, type:"HIDDEN"});
        td.appendChild(input);
        td.appendChild(inputId);

        //说明
        td = tr.insertCell(4);
        td.align = "center";
        var input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"relDesc",id:"_relDesc" +
                rowIndex,type:"TEXT",style:"width: 90%;"});
        td.appendChild(input);

        //操作列
        td = tr.insertCell(5);
        td.align = "center";
        //新增一行不能删除。
        td.innerHTML = "&nbsp;";
        //已经编辑的行可以删除。
//        var lastTr=Tools.findPreviousSibling(tr);
        td.innerHTML = '<img src="../../../resource/images/cancel.png" title="删除" onclick="removeRowRel(this)" style="width:16px;height: 16px;cursor: pointer">';

        //为tr添加验证机制
        dhtmlxValidation.addValidation(tr, [
            {target:"_colinfo1"+rowIndex, rule:"NotEmpty"},
            {target:relTypeCombo[rowIndex].DOMelem_input, rule:"NotEmpty"},
            {target:"_t2name"+rowIndex, rule:"NotEmpty"},
            {target:"_colinfo2"+rowIndex, rule:"NotEmpty"}
        ]);

    }
};

//关联关系维护中删除一行
var removeRowRel=function(img){
    //获取行得index
    var rowIndex = img.parentNode.parentNode._index;
    var trId = "_relColumnRow" + rowIndex;
    $(trId).parentNode.removeChild($(trId));

};

var initRelGrid=false;

/**
 * 加载表格关联信息
 * @param tableData
 */
var relCopyColumnData=function(data,rowIndex){
    if(data.COLINFO1){
//        newQueryColTree(tableId,$("_colinfo1"+rowIndex),data.TABLE_ID1_COL_IDS,$("_colinfo1Id"+rowIndex));
        dwr.util.setValue("_colinfo1" + rowIndex, data.COLINFO1);
    }
    if(data.TABLE_ID1_COL_IDS){
        dwr.util.setValue("_colinfo1Id" + rowIndex, data.TABLE_ID1_COL_IDS);
    }
    if(data.TABLE_REL_TYPE){
        relTypeCombo[rowIndex].setComboValue(data.TABLE_REL_TYPE);
    }
    if(data.T2NAME){
        dwr.util.setValue("_t2name" + rowIndex, data.T2NAME);
    }
    if(data.TABLE_ID2){
        dwr.util.setValue("_t2nameId" + rowIndex, data.TABLE_ID2);
    }
    if(data.COLINFO2){
        dwr.util.setValue("_colinfo2" + rowIndex, data.COLINFO2);
    }
    if(data.TABLE_ID2_COL_IDS){
        dwr.util.setValue("_colinfo2Id" + rowIndex, data.TABLE_ID2_COL_IDS);
    }
    if(data.TABLE_REL_DESC){
        dwr.util.setValue("_relDesc" + rowIndex, data.TABLE_REL_DESC);
    }

};

//以某表类为中心 查看全息视图
var goToNewView=function(tableId,tableName,tableVersion){
    window.parent.openMenu(tableName+"全息视图","/meta/module/tbl/view/viewDetail.jsp?focus=tableRef&tableId="+tableId
        +"&tableName="+tableName+"&tableVersion="+tableVersion,"top");

};

//注册拓扑图节点双击事件
dwrCaller.addAutoAction("flowNodeDetail","TableRelAction.flowNodeDetail");
/**
 * 拓扑图节点双击事件
 * @param nodeId
 */
var flowNodeDbClk=function(nodeId){
    var tableIdTmp = nodeId.replace("TABLE_","");
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("nodeDetailWindow", 0, 0, 550, 350);
    var nodeDetailWindow = dhxWindow.window("nodeDetailWindow");
    nodeDetailWindow.setModal(true);
    nodeDetailWindow.stick();
    nodeDetailWindow.setDimension(550, 350);
    nodeDetailWindow.center();
    nodeDetailWindow.denyResize();
    nodeDetailWindow.denyPark();
    nodeDetailWindow.setText("表类详细信息");
    nodeDetailWindow.keepInViewport(true);

    //关闭一些不用的按钮。
    nodeDetailWindow.button("minmax1").hide();
    nodeDetailWindow.button("park").hide();
    nodeDetailWindow.button("stick").hide();
    nodeDetailWindow.button("sticked").hide();
    nodeDetailWindow.show();
    nodeDetailWindow.attachEvent("onClose", function(){
        nodeDetailWindow.hide();
        nodeDetailWindow.setModal(false);
        return false;
    })
    var nodeDetailLayout = new dhtmlXLayoutObject(nodeDetailWindow, "2E");
    nodeDetailLayout.cells("a").hideHeader();
    nodeDetailLayout.cells("a").setHeight(80);
    nodeDetailLayout.cells("b").hideHeader();
    nodeDetailLayout.cells("a").attachObject($("_nodeDetail"));
    nodeDetailLayout.hideConcentrate();
    nodeDetailLayout.hideSpliter();//移除分界边框。
    dwrCaller.executeAction("flowNodeDetail",nodeId, function(data){
        if(data["TABLE_OWNER"])
        $("_tableOwner").innerHTML=data["TABLE_OWNER"];
        if(data["TABLE_NAME"])
        $("_nodeTableName").innerHTML=data["TABLE_NAME"];
        if(data["TABLE_NAME_CN"])
        $("_nodeTableNameCn").innerHTML=data["TABLE_NAME_CN"];
        if(data["TABLE_GROUP_NAME"])
        $("_nodeTableGroupName").innerHTML=data["TABLE_GROUP_NAME"];
        if(data["CODE_NAME"])
        $("_nodeTableCodeName").innerHTML=data["CODE_NAME"];
        if(data["DATA_SOURCE_NAME"])
        $("_nodeTableDataSourceName").innerHTML=data["DATA_SOURCE_NAME"];
        if(tableId!=data["TABLE_ID"]){
            $("_goToNewView").innerHTML='<a href="javascript:goToNewView(\''+data["TABLE_ID"]+'\',\''+data["TABLE_NAME"]+'\',\''+data["TABLE_VERSION"]+'\')">以此表类为中心信息，查看全息视图</a>';
        }else{
            $("_goToNewView").innerHTML='';
        }
    });
    var colsGrid= nodeDetailLayout.cells("b").attachGrid();
    colsGrid.setHeader("名称,类型,允许空,默认值,主键,注释");
    colsGrid.setHeaderAlign("left,center,center,center,center,center");
    colsGrid.setInitWidthsP("17,17,15,17,16,15");
    colsGrid.setColTypes("ro,ro,ch,ro,ch,ro");
    colsGrid.enableCtrlC();
    colsGrid.enableResizing("true,true,true,true,true,true");
    colsGrid.setColumnIds("colName","colDatatype","colNullabled","defaultVal","isPrimary","colBusComment");
    colsGrid.setColAlign("left,left,center,center,center,left");
    colsGrid.setEditable(false);
    colsGrid.init();
    colsGrid.load(dwrCaller.queryColsByTableIdNode+"&tableId="+tableIdTmp, "json");
};

//临时删除的节点关系
var templateRemoveLinkData=[];
//注册拓扑图箭头双击事件
dwrCaller.addAutoAction("flowLinkDetail",TableRelAction.flowLinkDetail,{dwrConfig:true,async:false});

//注册保存血缘关系事件
dwrCaller.addAutoAction("saveRelInfo", MaintainRelAction.saveRelInfo,{
    dwrConfig:true,
    isDealOneParam:false
});

//关联关系维护中——从元数据表类复制
dwrCaller.addAutoAction("saveRelFromGrid", MaintainRelAction.saveRelFromGrid, {dwrConfig:true,isDealOneParam:false});

var gridDatas;

//加载流程图方法
var initRelFlowFun=function(){


};

var initRelGridFun=function(){
    var relLayout = new dhtmlXLayoutObject(document.getElementById("detail"), "1C");
    relLayout.hideConcentrate();
    relLayout.hideSpliter();
    relLayout.cells("a").hideHeader();
    var toolBar = relLayout.cells("a").attachToolbar();
    toolBar.addButton("operate",2,"关联关系 - 图形",getBasePath()+"/meta/resource/images/lookup.png");
    toolBar.addButton("save",1,"保存",getBasePath()+"/meta/resource/images/ok.png");
    toolBar.setAlign("right");
    toolBar.attachEvent("onClick", function(id){
        if(id=="save"){
            var relData = getRelData();
            dwrCaller.executeAction("saveRelFromGrid",tableId,relData,function(r){
                if(r){
                    dhx.alert("保存成功！");
                }else{
                    dhx.alert("保存失败，请重试！");
                }
            });
        }
        if(id=="operate"){
            tableRefInit();
        }
    });

    // 表格对关联关系的维护
    relLayout.cells("a").attachObject($("_relFormDiv"));
    //设置CSS
    if(dhx.env.ie!=7.0){
        $("_relColumnHeadTable").style.width= $("_relContentDiv").clientWidth+"px";
    }
    dhx.html.addCss($("_relTableDiv"), global.css.gridTopDiv);
    $("_headOperate_").innerHTML='<img src="../../../resource/images/edit_add.png" title="增加" onclick="addRelColumnRow(null,true)" style="width:16px;height: 16px;cursor: pointer">';

    if(!initRelGrid){
        dwrCaller.executeAction("queryRelForGrid",tableId,function(relColumnDatas){


            if(relColumnDatas&&relColumnDatas.length>0){
                gridDatas = relColumnDatas;
                addRelColumnRow();
                for(var i=0;i<relColumnDatas.length;i++){
                    var relColumnData=relColumnDatas[i];
                    relCopyColumnData(relColumnData,relColumnNum);

                    if(i==relColumnDatas.length-1){
//                                    addRelColumnRow(null, true);
                    }else{
                        addRelColumnRow(null);
                    }
                }

            }else{
                addRelColumnRow(null, true);
            }

            initRelGrid=true;
        });
    }
    toolBar.setItemText("operate","关联关系 - 图形");
};

var isInitFlow=false;
/**
 * 页面初始化
 */
var tableRefInit=function(){
    var relLayout = new dhtmlXLayoutObject(document.getElementById("detail"), "3J");
    relLayout.hideConcentrate();
    relLayout.hideSpliter();
    relLayout.cells("a").hideHeader();
    relLayout.cells("b").hideHeader();
    relLayout.cells("c").hideHeader();

    if(!isInitRelTypeDate){
        tableRelTypeData = getComboByRemoveValue("TABLE_REL_TYPE");
//        Tools.addOption("_relType_{index}",tableRelTypeData);
        Tools.addOption("_newRelType_{index}",tableRelTypeData);
        isInitRelTypeDate = true;
    }

    var toolBar = relLayout.cells("b").attachToolbar();
    toolBar.addButton("operate",2,"关联关系 - 表格",getBasePath()+"/meta/resource/images/lookup.png");
    toolBar.addButton("save",1,"保存",getBasePath()+"/meta/resource/images/ok.png");
    toolBar.setAlign("right");

    toolBar.attachEvent("onClick", function(id){
        if(id=="save"){
            //流程图保存
            if(toolBar.getItemText("operate")=="关联关系 - 表格"){
                dwrCaller.executeAction("saveRelInfo",flow.getAllNodes(),linkData,tableId,function(r){
                    if(r){
                        dhx.alert("保存成功！");
                    }else{
                        dhx.alert("保存失败，请重试！");
                    }
                });
            }else if(toolBar.getItemText("operate")=="关联关系 - 图形"){// 表格保存
                var relData = getRelData();
                dwrCaller.executeAction("saveRelFromGrid",tableId,relData,function(r){
                    if(r){
                        dhx.alert("保存成功！");
                    }else{
                        dhx.alert("保存失败，请重试！");
                    }
                });
            }
        }
        if(id=="operate"){
            if(toolBar.getItemText("operate")=="关联关系 - 图形"){
                relLayout.cells("b").detachObject();
                toolBar.setItemText("operate","关联关系 - 表格");
//                relLayout.cells("b").attachObject($("flow_div"));
                iniFlow();
            } else if(toolBar.getItemText("operate")=="关联关系 - 表格"){
                initRelGridFun();
            }
        }
    });

    var iniFlow=function(){
        relLayout.cells("a").setWidth(400);
        relLayout.cells("a").setHeight(80);
        //加载查询表单
        var queryform = relLayout.cells("a").attachForm([
            {type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
            {type:"combo",label:"层次分类：",name:"tableTypeId",options:[{text:'全部',value:""}],inputWidth:120},
            {type:"input",label:"关&nbsp;键&nbsp;字&nbsp;：",name:"keyWord",inputWidth:120},
            {type:"newcolumn"},
            {type:"button",name:"query",value:"查询"},
            {type:"hidden",name:"excludTableId",value:tableId}
        ]);
        //加载层次分类
        tableTypeData = getComboByRemoveValue("TABLE_TYPE");
        queryform.getCombo("tableTypeId").addOption(tableTypeData.options);

        var maintainParam = new biDwrMethodParam();
        maintainParam.setParamConfig([
            {
                index:0,type:"fun",value:function() {
                return queryform.getFormData();
            }
            }
        ]);

        dwrCaller.addAutoAction("queryTables", "TableRelAction.queryLeftTable", maintainParam);
        dwrCaller.addDataConverter("queryTables", maintainConverter);

        relLeftGrid = relLayout.cells("c").attachGrid();
        relLeftGrid.setHeader("表类名（双击选择）,中文名");
        relLeftGrid.setHeaderAlign("left,center");
        relLeftGrid.setInitWidthsP("60,40");
        relLeftGrid.setColAlign("left,left");
        relLeftGrid.setColTypes("ro,ro");
        relLeftGrid.enableCtrlC();
        relLeftGrid.enableResizing("true,true");
        relLeftGrid.enableMultiselect(false);
        relLeftGrid.setColumnIds("tableName","tableNameCn");
        relLeftGrid.init();
        //进入页面不自动加载数据
        relLeftGrid.defaultPaging(20);
        relLeftGrid.load(dwrCaller.queryTables, "json");

        var startXY = 0;
        //双击列事件
        relLeftGrid.attachEvent("onRowDblClicked",function(rowId,cInd){
            var allFlowData = flow.getAllNodes();
            for(var i=0;i<allFlowData["nodes"].length;i++){
                if(allFlowData["nodes"][i]["uid"].replace("TABLE_","")==rowId){
//                    dhx.alert("该表类已经在右侧，不能重复选择");
                    if(rowId==tableId){
                        dhx.alert("不能删除主表的节点！");
                        return false;
                    }
                    flow.removeNode("TABLE_"+rowId);
                    relLeftGrid.setRowTextStyle(rowId, "font-weight:normal;font-style::normal;text-decoration:none;color:black;border-bottom:0px;");
                    return;
                }
            }
            relLeftGrid.setRowTextStyle(rowId, "color: #808080;font-style: italic;text-decoration: line-through");
            var rowData = relLeftGrid.getRowData(rowId);
            var node={"uid" : "TABLE_"+rowId, "type" : "node", "text" : rowData.data[0],tooltip : rowData.data[1],x:50+startXY,y:30+startXY};
            flow.addNode(node);
            startXY = startXY + 10;
            if(startXY > 100){
                startXY = 0;
            }
        });

        //查询表单事件处理
        queryform.attachEvent("onButtonClick", function(id) {
            if (id == "query") {
                //进行数据查询。
                relLeftGrid.clearAll();
                relLeftGrid.load(dwrCaller.queryTables, "json");
            }
        });
        dwrCaller.executeAction("queryToFlow", {tableId:tableId, level:1} ,function(data){
            //装载DATA
            if(!isInitFlow){
                flow = new Flow({
                    div:'flow_div',
                    layout:'queue',
                    swf:getBasePath() + '/meta/resource/swf/Flow.swf',
                    onReady:function(flow){
                        flow.hidePalette();
                        flow.hideEditBtn();
                        flow.loadNodes(data);
                    },
                    onNodeDblClick:function(nodeId){
                        //节点双击事件：显示具体表信息
                        flowNodeDbClk(nodeId);
                    },onLinkDblClick:function(from,to){
                        //箭头双击事件：显示具体关联的列信息
                        newLinkDbClick(from,to)
                    },onAfterDeleteNode:function(nodeId){
                        relLeftGrid.setRowTextStyle(nodeId.substring(6,nodeId.length), "font-weight:normal;font-style::normal;text-decoration:none;color:black;border-bottom:0px;");
                    },onBeforeLink:function(from,to){
                        if(from.replace("TABLE_","")!=tableId&&to.replace("TABLE_","")!=tableId){
                            dhx.alert("不能建立两表的关系，只允许建立跟主表之间的关系！");
                            return false;
                        }
                        newLinkDbClick(from,to);
                        return true;
                    },onBeforeDeleteNode:function(nodeId){
                        if(nodeId.replace("TABLE_","")==tableId){
                            dhx.alert("不能删除主表的节点！");
                            return false;
                        }else{
                            var canDel = false;
                            TableRelAction.isRelDim(nodeId.replace("TABLE_",""), tableId,{
                                    async:false,
                                    callback:
                                        function(r){
                                            if(r){
                                                dhx.alert("两表有维度列相关联，不能删除！");
                                                canDel = false;
                                            }else{
                                                canDel = true;
                                            }
                                        }}
                            );
                            return canDel;
                        }
                    }
                });
                isInitFlow = true;
            }

            relLayout.cells("b").attachObject($("flow_div"));
        });
    };
    iniFlow();
};

dhx.ready(tableRefInit);