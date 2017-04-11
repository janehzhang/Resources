/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *
 *Description：指标与表类关系维护
 *
 *Dependent：
 *
 *Author:
 *        刘斌
 *Finished：
 *       2012-06-05
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/

dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var flow = null;
var dwrCaller = new biDwrCaller();

//基础指标的源表ID
var gdlSrcTableId=0;
var gdlVersion = null;
var gdlType = null;
var gdlCode = null;

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

function tableRefInit(){
    var relLayout = new dhtmlXLayoutObject(document.getElementById("detail"), "3J");
    relLayout.hideConcentrate();
    relLayout.hideSpliter();
    relLayout.cells("a").hideHeader();
    relLayout.cells("b").hideHeader();
    relLayout.cells("c").hideHeader();
    relLayout.cells("a").setWidth(400);
    relLayout.cells("a").setHeight(80);
    //加载查询表单
    var queryform = relLayout.cells("a").attachForm([
        {type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
        {type:"combo",label:"层次分类：",name:"tableTypeId",options:[{text:'全部',value:""}],inputWidth:120},
        {type:"input",label:"关&nbsp;键&nbsp;字&nbsp;：",name:"keyWord",inputWidth:120},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"},
        {type:"hidden",name:"gdlId",value:gdlId}
    ]);
    //加载层次分类
    queryform.getCombo("tableTypeId").addOption(getComboByRemoveValue("TABLE_TYPE").options);
    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {
            //进行数据查询。
            relLeftGrid.clearAll();
            relLeftGrid.load(dwrCaller.queryTables, "json");
        }
    });
    var maintainParam = new biDwrMethodParam();
    maintainParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
                return queryform.getFormData();
            }
        }
    ]);

    dwrCaller.addAutoAction("queryTables", "GdlRelAction.queryLeftTable", maintainParam);
    dwrCaller.addDataConverter("queryTables", maintainConverter);

    relLeftGrid = relLayout.cells("c").attachGrid();
    relLeftGrid.setHeader(LoaclInfo.tableNameDbClick+","+LoaclInfo.tableNameCn);
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
        if(!flow){
            return;
        }
        var allFlowData = flow.getAllNodes();
        for(var i=0;i<allFlowData["nodes"].length;i++){
            if(allFlowData["nodes"][i]["uid"].replace("tbl_","")==rowId){
                flow.removeNode("tbl_"+rowId);
                relLeftGrid.setRowTextStyle(rowId, "font-weight:normal;font-style::normal;text-decoration:none;color:black;border-bottom:0px;");
                return;
            }
        }
        relLeftGrid.setRowTextStyle(rowId, "color: #808080;font-style: italic;text-decoration: line-through");
        var rowData = relLeftGrid.getRowData(rowId);
        var node={"uid" : "tbl_"+rowId, "type" : "node", "text" : rowData.data[0],tooltip : rowData.data[1],x:50+startXY,y:30+startXY};
        flow.addNode(node);
        startXY = startXY + 10;
        if(startXY > 100){
            startXY = 0;
        }
    });

    GdlRelAction.queryToFlowRelTable(gdlId,function(data){
        if(data.gdlSrcTableId){
            gdlSrcTableId = data.gdlSrcTableId;
        }
        if(data.gdlType!=null){
            gdlType = data.gdlType;
        }
        if(data.gdlVersion!=null){
            gdlVersion = data.gdlVersion;
        }
        if(data.gdlCode !=null){
            gdlCode = data.gdlCode;
        }
        //装载DATA
        if(!flow){
            flow = new Flow({
                div:'flow_div',
                layout:'queue',
                swf:getBasePath() + '/meta/resource/swf/Flow.swf',
                onReady:function(flow){
                    flow.hidePalette();
                    flow.hideEditBtn();
                    flow.hideRemoveBtn();
                    flow.loadNodes(data);
                },
                onNodeDblClick:function(nodeId){
                    //节点双击事件：显示具体表信息
                flowNodeDbClk(nodeId);
                },onLinkDblClick:function(from,to){
                    //箭头双击事件：显示具体关联的列信息
                    linkDbClick(from,to,true);
                },onBeforeLink:function(from,to){
                    if(from.replace("gdl_","")!=gdlId&&to.replace("gdl_","")!=gdlId){
                        dhx.alert("不能建立两表的关系，只允许建立跟指标之间的关系！");
                        return false;
                    }
                    linkDbClick(from,to,false);
                    return true;
                }
            });
        }else{
            flow.loadNodes(data);
        }
        relLayout.cells("b").attachObject($("flow_div"));
    });

}

var linkSetWin = null;

/**
 * 箭头双击事件
 * @param from
 * @param to
 */
function linkDbClick(from, to, isUpdate){
    if(!linkSetWin){
        //初始化新增弹出窗口。
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("linkSetWin", 0, 0, 550, 360);
        linkSetWin = dhxWindow.window("linkSetWin");
        linkSetWin.setModal(true);
        linkSetWin.stick();
        linkSetWin.setDimension(550, 360);
        linkSetWin.center();
        linkSetWin.denyResize();
        linkSetWin.denyPark();
        linkSetWin.button("minmax1").hide();
        linkSetWin.button("park").hide();
        linkSetWin.button("stick").hide();
        linkSetWin.button("sticked").hide();
        linkSetWin.setModal(true);
        linkSetWin.keepInViewport(true);
        linkSetWin.attachEvent("onClose", function(){
            linkSetWin.hide();
            linkSetWin.setModal(false);
            return false;
        });

        linkSetWin.setText("指标与表类关联字段选择");
        linkSetWin.show();
        var addLayout = new dhtmlXLayoutObject(linkSetWin, "1C");
        addLayout.cells("a").hideHeader();
        addLayout.cells("a").attachObject($("linkSetDiv"));
        checkButton = Tools.getButtonNode("保存",getBasePath()+'/meta/resource/images/link.png');
        checkButton.style.marginLeft='190px';
        checkButton.onclick=saveBtnClk;
        checkButton.style.cssFloat='left';
        checkButton.style.paddingTop="5px";
        checkButton.style.styleFloat='left';

        cancelButton = Tools.getButtonNode("删除",getBasePath()+'/meta/resource/images/cancel.png');
        cancelButton.style.cssFloat='left';
        cancelButton.style.styleFloat='left';
        cancelButton.style.paddingTop="5px";
        cancelButton.onclick = deleteBtnClick;
        $("_addButtonDiv").appendChild(checkButton);
        $("_addButtonDiv").appendChild(cancelButton);

        //加入TAB
        linkTabbar = new dhtmlXTabBar("linkDiv", "top");
        linkTabbar.addTab("a1", "关联列", "100px");
        linkTabbar.addTab("a2", "绑定维度", "100px");
        linkTabbar.addTab("a3", "支撑维度", "100px");
        linkTabbar.setTabActive("a1");
    }
    linkSetWin._isUpdate = isUpdate;
    linkSetWin._tableId = from.replace("tbl_","");
    initLinkDetail(from.replace("tbl_",""),isUpdate);
    linkSetWin.setModal(true);
    linkSetWin.show();
}

var linkTabbar = null;
//关联表列信息列表
var relColGrid = null;
//绑定维度信息列表
var bandDimGrid = null;
//支撑维度信息列表
var supportDimGrid = null;

var relColConfig = {
    idColumnName:"colId",
    filterColumns:["","colName","colNameCn","colDatatype"]
};
var relColConverter = new dhtmxGridDataConverter(relColConfig);

var bandDimConverter = new dhtmxGridDataConverter({filterColumns:["dimNameCn","dimTypeName","dimCode"]});

var supportDimConvert = new dhtmxGridDataConverter({filterColumns:["dimNameCn","groupMethod"]});

/**
 * 初始化关联关系维护界面
 */
function initLinkDetail(tableId,isUpdate){
    if(!isInit){
        linkTabbar.cells("a1").attachObject($("relColGridDiv"));
        linkTabbar.cells("a2").attachObject($("bandDimGridDiv"));
        linkTabbar.cells("a3").attachObject($("supportDimGridDiv"));
    }
    dwrCaller.addAutoAction("queryRelCol", "GdlRelAction.queryGdlColByTableId",tableId);
    dwrCaller.addDataConverter("queryRelCol", relColConverter);
    dwrCaller.addAutoAction("queryBandDim", "GdlRelAction.queryBandDim",gdlId,tableId);
    dwrCaller.addDataConverter("queryBandDim", bandDimConverter);
    dwrCaller.addAutoAction("querySupportDim", "GdlRelAction.querySupportDim",gdlId,tableId);
    dwrCaller.addDataConverter("querySupportDim", supportDimConvert);
    //初始化表列信息
    if(!relColGrid){
        relColGrid = new dhtmlXGridObject('relColGridDiv');
        relColGrid.setHeader(",列名,列中文名,类型");
        relColGrid.setInitWidthsP("6,30,30,34");
        relColGrid.setColAlign("center,left,left,left");
        relColGrid.setColTypes("ra,ro,ro,ro");
        relColGrid.setHeaderAlign("center,center,center,center");
        relColGrid.init();
        relColGrid.defaultPaging(10);
        relColGrid.load(dwrCaller.queryRelCol, "json");
        relColGrid.attachEvent("onCheck", function(rId, cInd, state){
            if(state){
                relColRadioCheck(rId);
            }
        });
        relColGrid.attachEvent("onRowSelect", function(id,ind){
            relColGrid.cells(id,0).setValue(1);
            relColRadioCheck(id);
        })
    }else{
        relColGrid.clearAll();
        relColGrid.load(dwrCaller.queryRelCol, "json");
    }
    //初始化绑定维度信息列表
    if(!bandDimGrid){
        bandDimGrid = new dhtmlXGridObject('bandDimGridDiv');
        bandDimGrid.setHeader("维度名,归并类型,维度值");
        bandDimGrid.setInitWidthsP("33,33,33");
        bandDimGrid.setColAlign("left,left,left");
        bandDimGrid.setColTypes("ro,ro,ro");
        bandDimGrid.setHeaderAlign("center,center,center");
        bandDimGrid.init();
        bandDimGrid.defaultPaging(10);
        bandDimGrid.load(dwrCaller.queryBandDim, "json");
    }else{
        //重新加载
        bandDimGrid.clearAll();
        bandDimGrid.load(dwrCaller.queryBandDim, "json");
    }

    //初始化支撑维度信息列表
    if(!supportDimGrid){
        supportDimGrid = new dhtmlXGridObject('supportDimGridDiv');
        supportDimGrid.setHeader("维度名,分组计算方法");
        supportDimGrid.setInitWidthsP("50,50");
        supportDimGrid.setColAlign("left,left");
        supportDimGrid.setColTypes("ro,ro");
        supportDimGrid.setHeaderAlign("center,center");
        supportDimGrid.setColumnCustFormat(1,transGroupMethod);
        supportDimGrid.init();
        supportDimGrid.defaultPaging(10);
        supportDimGrid.load(dwrCaller.querySupportDim, "json");
    }else{
        //重新加载
        supportDimGrid.clearAll();
        supportDimGrid.load(dwrCaller.querySupportDim, "json");
    }
    if(isUpdate){//修改界面，初始化选择的列信息
        GdlRelAction.queryInfoByTableIdAndGdlId(tableId, gdlId, function(data){
            if(data.COL_NAME){
                $("_relCol").value=data.COL_NAME;
            }
            if(data.COL_ID){
                $("_relColId").value=data.COL_ID;
                relColGrid.cells(data.COL_ID,0).setValue(1);
            }
        });
    }

    isInit = true;
}

var isInit = false;

/**
 * 关联列信息表列选中事件
 * @param id
 */
function relColRadioCheck(id){
    var rowData=relColGrid.getRowData(id);
    $("_relCol").value = rowData.userdata.colName + " " + (rowData.userdata.colNameCn==null?"":rowData.userdata.colNameCn);
    $("_relColId").value = rowData.userdata.colId;

}

function transGroupMethod(val){
    return getNameByTypeValue("GDL_GROUP_METHOD",val);
}

function saveBtnClk(){
    if(gdlSrcTableId==linkSetWin._tableId){
        dhx.alert("该宽表为基础指标来源宽表，不允许更改！");
        return;
    }
    if(flow.getNode("tbl_"+linkSetWin._tableId).level>0){
        dhx.alert("该宽表不是指标的直接数据来源宽表，不允许更改！");
        return;
    }
    if($("_relColId").value==-1){
        dhx.alert("请选择关联的列！");
        return;
    }
    if(!linkSetWin._isUpdate){//新增关系窗口
        GdlRelAction.insertTableRel(
            {gdlId:gdlId, tableId:linkSetWin._tableId, colId:$("_relColId").value},
            function(msg){
                if(!msg){
                    dhx.alert("设置成功！",function(){
                        linkSetWin.close();
                    })
                }else{
                    dhx.alert("设置失败！出错信息："+msg);
                }
            }
        )
    }else{//维护关系窗口
        GdlRelAction.updateTableRel(
            {gdlId:gdlId, tableId:linkSetWin._tableId, colId:$("_relColId").value},
            function(msg){
                if(!msg){
                    flow.updateLinkAttr("tbl_"+linkSetWin._tableId,"gdl_"+gdlId,"text",$("_relCol").value);
                    dhx.alert("设置成功！",function(){
                        linkSetWin.close();
                    })
                }else{
                    dhx.alert("设置失败！出错信息："+msg);
                }
            }
        );
    }
}

function deleteBtnClick(){
    if(gdlSrcTableId==linkSetWin._tableId){
        dhx.alert("该宽表为基础指标来源宽表，不允许删除！");
        return;
    }
    if(flow.getNode("tbl_"+linkSetWin._tableId).level>0){
        dhx.alert("该宽表不是指标的直接数据来源宽表，不允许删除！");
        return;
    }
    if(linkSetWin._isUpdate){
        dhx.confirm("确认删除指标与该表类的关联关系吗？",function(r){
            if(r){
                GdlRelAction.deleteTableRel(
                    {gdlId:gdlId, tableId:linkSetWin._tableId, colId:$("_relColId").value},
                    function(msg){
                        if(!msg){
                            flow.removeNode("tbl_"+linkSetWin._tableId);
                            relLeftGrid.setRowTextStyle(linkSetWin._tableId, "font-weight:normal;font-style::normal;text-decoration:none;color:black;border-bottom:0px;");
                            dhx.alert("删除成功！",function(){
                                linkSetWin.close();
                            });
                        }else{
                            dhx.alert("删除失败！出错信息："+msg);
                        }
                    }
                )
            }
        });
    }else{
        flow.removeNode("tbl_"+linkSetWin._tableId);
        relLeftGrid.setRowTextStyle(linkSetWin._tableId, "font-weight:normal;font-style::normal;text-decoration:none;color:black;border-bottom:0px;");
        linkSetWin.close();
    }
}

/**
 * 节点双击事件
 * @param nodeId
 */
function flowNodeDbClk(nodeId){
    if(nodeId.indexOf("gdl_")>=0){//指标查看
        if(gdlType==0){
            //基础指标查看
            openMenu("基础指标("+gdlCode+")","/meta/module/gdl/gdlBasic/viewGdlBasic.jsp?gdlId="+gdlId+"&gdlVersion="+gdlVersion,"top","bsgdlview_"+gdlId);
        }else if(gdlType==1){
            //复合指标
            openMenu("复合指标("+gdlCode+")","/meta/module/gdl/composite/compositeGdlView.jsp?gdlId="+gdlId+"&gdlVersion="+gdlVersion,"top","comgdlview_"+gdlId);
        }else if(gdlType==2){
            //计算
            openMenu("计算指标("+gdlCode+")","/meta/module//gdl/calc/calcGdlView.jsp?gdlId="+gdlId+"&gdlVersion="+gdlVersion,"top","calcgdlview_"+gdlId);
        }
    }else{//表类查看
        GdlRelAction.flowNodeDetail(nodeId, function(data){
            if(data.TABLE_TYPE_ID != 2){//不是维表
                openMenu(data.TABLE_NAME+"全息视图","/meta/module/tbl/view/viewDetail.jsp?focus=basicInfo"+
                    "&tableId="+data.TABLE_ID+"&tableName="+data.TABLE_NAME+"&tableVersion="+data.TABLE_VERSION,
                    "top","relTableView_"+data.TABLE_ID);
            }else{
                openMenu(data.TABLE_NAME+"全息视图","/meta/module/tbl/view/viewDetail.jsp?focus=basicInfo"+
                    "&tableId="+data.TABLE_ID+"&tableName="+data.TABLE_NAME+"&tableVersion="+data.TABLE_VERSION+"&isDimTable=Y",
                    "top","relTableView_"+data.TABLE_ID);
            }
        });
    }
}

dhx.ready(tableRefInit);