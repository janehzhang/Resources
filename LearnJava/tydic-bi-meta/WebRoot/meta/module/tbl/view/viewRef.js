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
 *       2012-01-04
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
//显示关系流程图Action
dwrCaller.addAutoAction("queryToFlow","TableRelAction.queryToFlow",{
	dwrConfig:true,
    isShowProcess:false
});
var flow = null;
/**
 * 拓扑图节点双击事件
 * @param nodeId
 */
var flowNodeDbClk=function(nodeId){
    var tableIdTmp = nodeId.replace("TABLE_","");
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("nodeDetailWindow", 0, 0, 550, 400);
    var nodeDetailWindow = dhxWindow.window("nodeDetailWindow");
    nodeDetailWindow.setModal(true);
    nodeDetailWindow.stick();
    nodeDetailWindow.setDimension(550, 400);
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
    });
    var nodeDetailLayout = new dhtmlXLayoutObject(nodeDetailWindow, "2E");
    nodeDetailLayout.cells("a").hideHeader();
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
            nodeDetailLayout.cells("a").setHeight(80);
            $("_goToNewView").innerHTML='<a href="javascript:goToNewView(\''+data["TABLE_ID"]+'\',\''+data["TABLE_NAME"]+'\',\''+data["TABLE_VERSION"]+'\')">以此表类为中心信息，查看全息视图</a>';
        }else{
            nodeDetailLayout.cells("a").setHeight(62);
        }
    });
    var colsGrid= nodeDetailLayout.cells("b").attachGrid();
    colsGrid.setHeader("名称,中文名,类型,允许空,默认值,主键,注释");
    colsGrid.setHeaderAlign("left,left,left,center,center,center,center");
    colsGrid.setInitWidthsP("14,18,14,11,14,13,13");
    colsGrid.setColTypes("ro,ro,ro,ch,ro,ch,ro");
    colsGrid.enableCtrlC();
    colsGrid.enableResizing("true,true,true,true,true,true,true");
    colsGrid.setColumnIds("colName","colNameCn","colDatatype","colNullabled","defaultVal","isPrimary","colBusComment");
    colsGrid.setColAlign("left,left,left,center,center,left");
    colsGrid.setEditable(false);
    colsGrid.init();
    colsGrid.load(dwrCaller.queryColsByTableIdNode+"&tableId="+tableIdTmp, "json");
};

//以某表类为中心 查看全息视图
var goToNewView=function(tableId,tableName,tableVersion){
    window.parent.openMenu(tableName+"全息视图","/meta/module/tbl/view/viewDetail.jsp?focus=tableRef&tableId="+tableId
        +"&tableName="+tableName+"&tableVersion="+tableVersion,"top");

};
//表列信息相关Action以及数据转换器 start
var colsConfig = {
    filterColumns:["colName","colNameCn","colDatatype","colNullabled","defaultVal","isPrimary","colBusComment","thisdim"],
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
//注册拓扑图节点双击事件
dwrCaller.addAutoAction("flowNodeDetail","TableRelAction.flowNodeDetail");
//注册拓扑图箭头双击事件
dwrCaller.addAutoAction("flowLinkDetail","TableRelAction.flowLinkDetail");
/**
 * 拓扑图箭头双击事件
 */
var flowLinkDbClk=function(from,to){
    dwrCaller.executeAction("flowLinkDetail",{idFrom:from, idTo:to},function(data){
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("linkDetailWindow", 0, 0, 450, 350);
        var linkDetailWindow = dhxWindow.window("linkDetailWindow");
        linkDetailWindow.setModal(true);
        linkDetailWindow.stick();
        linkDetailWindow.setDimension(450, 350);
        linkDetailWindow.center();
        linkDetailWindow.setPosition(linkDetailWindow.getPosition()[0],linkDetailWindow.getPosition()[1]-100);
        linkDetailWindow.denyResize();
        linkDetailWindow.denyPark();
        linkDetailWindow.button("minmax1").hide();
        linkDetailWindow.button("park").hide();
        linkDetailWindow.button("stick").hide();
        linkDetailWindow.button("sticked").hide();
        linkDetailWindow.setText("关联关系查看");
        linkDetailWindow.keepInViewport(true);
        linkDetailWindow.show();
        var linkDetailLayout = new dhtmlXLayoutObject(linkDetailWindow, "1C");
        var tabbar=linkDetailLayout.cells("a").attachTabbar();
        var num=data.length;
        var tblId1=data[0]["TABLE_ID1"];
        var tbl2Id=data[0]["TABLE_ID2"];
        for(var index=0; index<num;index++){
            tableId1 = data[index]["TABLE_ID1"];
            tableId2 = data[index]["TABLE_ID2"];
            var onlyId = tblId1+"_"+tbl2Id+"_"+index;
            tabbar.addTab("tab_"+onlyId,"关系"+(index+1),"100px");
            var newDiv = document.createElement("div");
            newDiv.style.cssText="height:100%;width:100%";
            newDiv.innerHTML=$("newLinkDetail").innerHTML.replace(/\{index\}/g,onlyId);
            for(var i = 0; i < newDiv.childNodes.length; i++){
                if(newDiv.childNodes[i].nodeName.toLowerCase()=="form"){
                    var tmpLayout=tabbar.cells("tab_"+onlyId).attachLayout("1C");
                    tmpLayout.cells("a").hideHeader();
                    tmpLayout.cells("a").attachObject(newDiv.childNodes[i]);
                    //设置CSS
                    if(dhx.env.ie!=7.0){
                    $("_newRelColumnHeadTable_"+onlyId).style.width
                        = $("_newRelContentTable_Div_"+onlyId).clientWidth+"px";
                    }
                    dhx.html.addCss($("_newRelTableDiv_"+onlyId), global.css.gridTopDiv);

                    if(data[index]["TABLE_REL_TYPE"]!=undefined && data[index]["TABLE_REL_TYPE"]!=null){
                        $("_newRelType_"+onlyId).value = data[index]["TABLE_REL_TYPE"];
                    }

                    if(data[index]["TABLE_REL_DESC"])
                    $("_newRelComm_"+onlyId).innerHTML=data[index]["TABLE_REL_DESC"];

                    var colId1Array = data[index]["TABLE_ID1_COL_IDS"].split(",");
                    var colId2Array = data[index]["TABLE_ID2_COL_IDS"].split(",");

                    if(colId1Array.length>0&&colId1Array.length==colId2Array.length){
                        if(colId1Array[0]==""&&colId2Array[0]==""&&colId1Array.length==1){
                            newAddRelColumnRow(null,true,onlyId);
                        }else{
                            newAddRelColumnRow(null,false,onlyId);
                        }

                    }
                    for(var j=0;j<colId1Array.length;j++){

                        var oneData={};//数据格式：{colId1:"",colName1:"",colId2:"",colName2:""}
                        oneData.colId1=colId1Array[j];
                        oneData.colName1=data[index]["COLINFO1"].split(",")[j];
                        oneData.colId2=colId2Array[j];
                        oneData.colName2=data[index]["COLINFO2"].split(",")[j];
                        newRelCopyColumnData(oneData,newRelColumnNum,onlyId);


                        if(j==colId1Array.length-1){
//                                    addRelColumnRow(null, true);
                        }else{
                            //增加行操作
                            newAddRelColumnRow(null,false,onlyId);
                        }

                    }

                    break;
                }
            }
        }
        if(num > 0){
            tabbar.setTabActive("tab_"+tblId1+"_"+tbl2Id+"_"+0);
        }
    });

};

/**
 * 加载表格关联信息
 * 这里data是一个Map，数据格式：{colId1:"",colName1:"",colId2:"",colName2:""}
 * @param tableData
 */
var newRelCopyColumnData=function(data,rowIndex,tabId){
    //设置列1输入框
    if(data.colName1){
        newQueryColTree(tableId1,$("_newColinfo1_"+tabId+"_"+rowIndex[tabId]),data.colId1,$("_newColinfo1Id_"+tabId+"_"+rowIndex[tabId]));
        dwr.util.setValue("_newColinfo1_"+tabId+"_"+rowIndex[tabId], data.colName1);
    }
    //设置列1Id
    if(data.colId1){
        dwr.util.setValue("_newColinfo1Id_"+tabId+"_"+rowIndex[tabId], data.colId1);
    }
    //设置列2输入框
    if(data.colName2){
        newQueryColTree(tableId2,$("_newColinfo2_"+tabId+"_"+rowIndex[tabId]),data.colId2,$("_newColinfo2Id_"+tabId+"_"+rowIndex[tabId]));
        dwr.util.setValue("_newColinfo2_"+tabId+"_"+rowIndex[tabId], data.colName2);
    }
    //设置列2Id
    if(data.colId2){
        dwr.util.setValue("_newColinfo2Id_"+tabId+"_"+rowIndex[tabId], data.colId2);
    }
};


var newRelColumnNum = {};//关联关系表格行索引
//添加一行关联信息
var newAddRelColumnRow = function(rowIndex,isNullTree,tabId){
    if(!newRelColumnNum[tabId]){
        newRelColumnNum[tabId] = 0;
    }
    rowIndex = (rowIndex == undefined || rowIndex == null) ? (++(newRelColumnNum[tabId])) : rowIndex;
    if(!$("_newRelColumnRow_" + tabId + "_" + rowIndex)){//表示子窗体tabId的第rowIndex行
        if(newRelColumnNum[tabId] < rowIndex){
            newRelColumnNum[tabId] = rowIndex;
        }
        var tr = document.createElement("tr");
        tr._index = rowIndex;
        tr.id = "_newRelColumnRow_"+ tabId + "_" + rowIndex;
        $("_newRelContentBody_" + tabId).appendChild(tr);

        //第一列(主表的列)
        var td = tr.insertCell(0);
        td.align = "center";
        var input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"newColinfo1",id:"_newColinfo1_" +
            tabId+"_"+rowIndex,type:"TEXT",style:"width: 80%;"});
        var inputId = dhx.html.create("input", {name:"newColinfo1Id", id:"_newColinfo1Id_"+tabId+"_"+rowIndex, type:"HIDDEN"});
        td.appendChild(input);
        td.appendChild(inputId);
        if(isNullTree){
            newQueryColTree(tableId1,$("_newColinfo1_"+tabId+"_"+rowIndex),null,$("_newColinfo1Id_"+tabId+"_"+rowIndex));
        }

        //第二列(关联表的列)
        td = tr.insertCell(1);
        td.align = "center";
        var input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"newColinfo2",id:"_newColinfo2_" +
            tabId+"_"+rowIndex,type:"TEXT",style:"width: 80%;"});
        var inputId = dhx.html.create("input", {name:"newColinfo2Id", id:"_newColinfo2Id_"+tabId+"_"+rowIndex, type:"HIDDEN"});
        td.appendChild(input);
        td.appendChild(inputId);
        if(isNullTree){
            newQueryColTree(tableId2,$("_newColinfo2_"+tabId+"_"+rowIndex),null,$("_newColinfo2Id_"+tabId+"_"+rowIndex));
        }
    }

};

/**
 * 加载列树
 * @param selectGroup
 * @param form
 */
var newQueryColTree=function(tableId,target,checkedValue,idTarget){
    tableId=tableId|| global.constant.defaultRoot;
    var div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 1px #eee solid;height: 240px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;"
    });
    div.appendChild(treeDiv);

    //移动节点位置至指定节点下。
//    var target=form.getInput("tableGroup");
    target.readOnly=true;
    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
//    tree.enableSmartRendering();
    tree.enableHighlighting(true);
    tree.enableRadioButtons(true);
    tree.setDataMode("json");

    var buttonDiv = dhx.html.create("div", {
        style:"position:relative;border: 1px #eee solid;height: 30px;overflow: auto;padding-top:0px;margin-top: 6px;" +
            "z-index:1000;"
    })
    div.appendChild(buttonDiv);
    //创建一个button
    var button = Tools.getButtonNode("确定",getBasePath()+'/meta/resource/images/ok.png');
    button.onclick = function() {
        var checkedData = [];
        var allChecked = tree.getAllChecked();
        if (allChecked) {
            var nodes = typeof allChecked == "string" ? allChecked.split(",") : [allChecked];
            var textValue="";
            var idStr="";
            for (var i = 0; i < nodes.length; i++) {
                checkedData.push({id:nodes[i],text:tree.getItemText(nodes[i])});
                textValue=textValue+tree.getItemText(nodes[i])+",";
                idStr=idStr+nodes[i]+",";
                if(i==nodes.length-1){
                    textValue=textValue.substring(0,textValue.length-1);
                    idStr=idStr.substring(0,idStr.length-1);
                }
            }
            target.value=textValue;
            if(idTarget){
                idTarget.value=idStr;
            }
        }else{
            target.value="";
        }


        div.style.display = "none";
    };
    buttonDiv.appendChild(button);
//    }

    div.style.display="none";
    dwrCaller.executeAction("queryColTree",tableId,function(data){
        tree.loadJSONObject(data);
        if(checkedValue){
            //选中已选节点
            var cols=checkedValue.split(',');
            for(var i=0;i<cols.length;i++){
                tree.setCheck(cols[i],true);
            }

        }
        //为div添加事件
//        Tools.addEvent(target,"click",function(){
//            div.style.width = target.offsetWidth+'px';
////            if(isCheckBox){
////                Tools.divPromptCtrl(div,target,true);
////            }else{
//            Tools.divPromptCtrl(div,target,true);
////            }
//            div.style.display="block";
//        })
    });

};

/**
 * 加载列树
 */
dwrCaller.addAutoAction("queryColTree",MaintainRelAction.queryColTree);
var colTreeConverter=new dhtmxTreeDataConverter({
    idColumn:"colId",pidColumn:0,
    isDycload:false,textColumn:"colName"
});
dwrCaller.addDataConverter("queryColTree",colTreeConverter);

/**
 * 表类关系查看（表格）
 * 2010-11-03
 * 熊小平
 */

dwrCaller.addAutoAction("queryTableRels", "TableRelAction.queryTableRels",{tableId:tableId, tmp:""},{
	   dwrConfig:true,
       isShowProcess:false
    });
dwrCaller.addDataConverter("queryTableRels", new dhtmxGridDataConverter( {
    filterColumns : [ "colinfo1", "t2name", "colinfo2",
        "tableRelType", "tableRelDesc" ],
    cellDataFormat : function(rowIndex, columnIndex, columnName, cellValue,
                              rowData) {
        if (columnName == "tableRelType") {
            //关联类型转义
            if(cellValue==1){
                return "一对一";
            } else if(cellValue==2) {
                return "一对多";
            } else if(cellValue==3) {
                return "多对一";
            } else {
                return "未定义";
            }
        } else {
            return this._super.cellDataFormat(rowIndex, columnIndex,
                columnName, cellValue, rowData);
        }
    }

}));

/**
 * 拓扑图层级改变
 * @param level
 */
var levelChange=function(data){
    var level = data.value;
    dwrCaller.executeAction("queryToFlow", {tableId:tableId, level:level} ,function(data){
        //装载DATA
        if(!flow){
            flow = new Flow({
            div:'flow_div',
            layout:'queue',
            swf:getBasePath() + '/meta/resource/swf/Flow.swf',
            onReady:function(flow){
                flow.hidePalette();
                flow.hideSelectBtn();
                flow.hideLinkBtn();
                flow.hideEditBtn();
                flow.hideRemoveBtn();
                flow.loadNodes(data);
            },
            onNodeDblClick:function(nodeId){
                //节点双击事件：显示具体表信息
                flowNodeDbClk(nodeId);
            },onLinkDblClick:function(from,to){
                //箭头双击事件：显示具体关联的列信息
                flowLinkDbClk(from,to)
            }
        });
        }else{
            flow.loadNodes(data);
        }
//        relLayout.cells("a").attachObject($("flow_div"));
    });
};


//关系下拉列表数据
var tableRelTypeData = null;
//是否已加载过关系类型下拉列表
var isInitRelTypeDate = false;

/**
 * 页面初始化
 */
var tableRefInit=function(){
    var relLayout = new dhtmlXLayoutObject(document.getElementById("detail"), "1C");
    relLayout.hideConcentrate();
    relLayout.hideSpliter();

    relLayout.cells("a").fixSize(false,true);
//    relLayout.cells("a").attachObject($("_tableRelView"));

    if(!isInitRelTypeDate){
        tableRelTypeData = getComboByRemoveValue("TABLE_REL_TYPE");
//        Tools.addOption("_relType_{index}",tableRelTypeData);
        Tools.addOption("_newRelType_{index}",tableRelTypeData);
        isInitRelTypeDate = true;
    }

    relLayout.cells("a").hideHeader();
    var toolBar = relLayout.cells("a").attachToolbar();
    toolBar.addSeparator("tableName",1);
    toolBar.addText("levelSelect",2,'<select id="level" onchange="levelChange(this)" style="margin-top: -10px;width: 40px;" dir="ltr"><option value="1">1</option><option value="2">2</option></select>：选择层级');
    toolBar.addButton("operate",3,"关联关系 - 表格",getBasePath()+"/meta/resource/images/lookup.png");
    toolBar.addSpacer("tableName");

//    toolBar.addSpacer("operate");
    var base = getBasePath();

    dwrCaller.executeAction("queryToFlow", {tableId:tableId, level:1} ,function(data){
        //装载DATA
        if(!flow){
            flow = new Flow({
            div:'flow_div',
            layout:'queue',
            swf:base + '/meta/resource/swf/Flow.swf',
            onReady:function(flow){
                flow.hidePalette();
                flow.hideSelectBtn();
                flow.hideLinkBtn();
                flow.hideEditBtn();
                flow.hideRemoveBtn();
                flow.loadNodes(data);
            },
            onNodeDblClick:function(nodeId){
                //节点双击事件：显示具体表信息
                flowNodeDbClk(nodeId);
            },onLinkDblClick:function(from,to){
                //箭头双击事件：显示具体关联的列信息
                flowLinkDbClk(from,to)
            }
        });
        }else{
            flow.loadNodes(data);
        }
        relLayout.cells("a").attachObject($("flow_div"));
    });

    //添加点击事件
    toolBar.attachEvent("onClick", function(id){
        if(id=="operate"){
            if(toolBar.getItemText("operate")=="关联关系 - 图形"){
                // 切换到图形
                relLayout.cells("a").detachObject();
                toolBar.setItemText("operate","关联关系 - 表格"); //levelSelect
                toolBar.showItem("levelSelect");
                relLayout.cells("a").attachObject($("flow_div"));
            } else if(toolBar.getItemText("operate")=="关联关系 - 表格"){
                toolBar.hideItem("levelSelect");
                //切换到表格
                relLayout.cells("a").detachObject();

                //添加datagrid，用于显示数据（表格形式）
                var relGrid = relLayout.cells("a").attachGrid();
                relGrid.setHeader("列名,关联表类名,关联列名,关联类型,关联说明");
                relGrid.setHeaderAlign("center,center,center,center,center");
                relGrid.setInitWidthsP("20,20,30,10,20");
                relGrid.setColAlign("left,left,left,center,left");
                relGrid.setColTypes("ro,ro,ro,ro,ro");
                relGrid.enableCtrlC();
                relGrid.enableResizing("true,true,true,true,true");
                relGrid.setColSorting("na,na,na,na,na");
                relGrid.enableMultiselect(false);
                relGrid.setColumnIds("colinfo1", "t2name", "colinfo2",
                    "tableRelType", "TableRelDesc");
                relGrid.init();
                relGrid.defaultPaging(20);
                relGrid.load(dwrCaller.queryTableRels, "json");

                //更改按钮值
                toolBar.setItemText("operate","关联关系 - 图形");
            }
        }
    });



};

dhx.ready(tableRefInit);