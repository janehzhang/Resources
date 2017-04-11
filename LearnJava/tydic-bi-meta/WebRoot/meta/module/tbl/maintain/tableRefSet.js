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
 *       2011-12-29
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/

//维度关联信息：{tableId1_tableId2:'col1,dimcol2'}
var dimRel={}

/**
 * 新的双击箭头显示事件
 * @param from
 * @param to
 */
var newLinkDbClick = function(from,to){
    newRelColumnNum={};
    /**
     * 初始化被删除的临时关系
     */
    templateRemoveLinkData=[];
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("linkDetailWindow", 0, 0, 450, 360);
    var linkDetailWindow = dhxWindow.window("linkDetailWindow");
    linkDetailWindow.setModal(true);
    linkDetailWindow.stick();
    linkDetailWindow.setDimension(450, 360);
    linkDetailWindow.center();
    linkDetailWindow.setPosition(linkDetailWindow.getPosition()[0],linkDetailWindow.getPosition()[1]-100);
    linkDetailWindow.denyResize();
    linkDetailWindow.denyPark();
    linkDetailWindow.button("minmax1").hide();
    linkDetailWindow.button("park").hide();
    linkDetailWindow.button("stick").hide();
    linkDetailWindow.button("sticked").hide();
    linkDetailWindow.setText("表类关系维护");
    linkDetailWindow.keepInViewport(true);
    linkDetailWindow.show();
    var linkDetailLayout = new dhtmlXLayoutObject(linkDetailWindow, "1C");
    var tabbar=linkDetailLayout.cells("a").attachTabbar();
    tabbar.enableTabCloseButton(true);
    tabbar.attachEvent("onTabClose", function(id){
        var account=0;
        for(var key in tabbar._tabs){
            account++;
        }
        if(account==1){
            return false;
        }
        templateRemoveLinkData.push(id);
        return true;
    });

    /**
     * 如果全局箭头变量不存在
     */
    if(!linkData[from.replace("TABLE_","")+"_"+to.replace("TABLE_","")+"_"+0]){
        dwrCaller.executeAction("flowLinkDetail",{idFrom:from, idTo:to},function(data){
            if(!data||data==""){
                var fromData={};
                var toData={};
                dwrCaller.executeAction("queryTableByTableId",from.replace("TABLE_",""), function(data){
                    fromData=data;
                });
                dwrCaller.executeAction("queryTableByTableId",to.replace("TABLE_",""), function(data){
                    toData=data;
                });
                //构造一个新DATA数据
                data=[{
                    TABLE_ID1:fromData["TABLE_ID"],
                    T1NAME:fromData["TABLE_NAME"],
                    T1NAMECN:fromData["TABLE_NAME_CN"],
                    TABLE_ID2:toData["TABLE_ID"],
                    T2NAME:toData["TABLE_NAME"],
                    T2NAMECN:toData["TABLE_NAME_CN"],
                    COLTYPE1:"",
                    COLTYPE2:"",
                    COLINFO1:"",
                    COLINFO2:"",
                    TABLE_REL_TYPE:2,
                    TABLE_REL_DESC:"",
                    TABLE_ID1_COL_IDS:"",
                    TABLE_ID2_COL_IDS:"",
                    dimRel:""
                }];
            }
            var num=data.length;
            var tblId1;
            var tbl2Id;
            if(num > 0){
                tblId1=data[0]["TABLE_ID1"];
                tbl2Id=data[0]["TABLE_ID2"];
            }
            var data0 = data[0];

            for(var index=0; index<num;index++){
                var dimRel = data[index]["dimRel"];
                tableId1 = data[index]["TABLE_ID1"];
                tableId2 = data[index]["TABLE_ID2"];
                var onlyId = data[index]["TABLE_ID1"]+"_"+data[index]["TABLE_ID2"]+"_"+index;

                tabbar.addTab("tab_"+onlyId,"关系"+(index+1),"70px");
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
                        var addDimRel = "add";
                        $("_headOperate_"+onlyId).innerHTML='<img src="../../../resource/images/edit_add.png" title="增加" onclick="newAddRelColumnRow(null,true,\''+onlyId+'\',false,\''+addDimRel+'\')" style="width:16px;height: 16px;cursor: pointer">';
                        $("_dimRel_"+onlyId).value=dimRel;
                        if(data[index]["TABLE_REL_TYPE"]!=undefined && data[index]["TABLE_REL_TYPE"]!=null){
                            $("_newRelType_"+onlyId).value = data[index]["TABLE_REL_TYPE"];
                        }else{
                            $("_newRelType_"+onlyId).value = 2;
                        }
//                        if(data[index]["TABLE_REL_TYPE"]==1){
//                            $("_newRelType_"+onlyId).value=1;
//                        }else if(data[index]["TABLE_REL_TYPE"]==2){
//                            $("_newRelType_"+onlyId).value=2;
//                        }else if(data[index]["TABLE_REL_TYPE"]==3){
//                            $("_newRelType_"+onlyId).value=3;
//                        }else{
//                            $("_newRelType_"+onlyId).value=2;
//                        }
                        if(data[index]["TABLE_REL_DESC"]){
                            $("_newRelComm_"+onlyId).innerHTML=data[index]["TABLE_REL_DESC"];
                        }else{
                            $("_newRelComm_"+onlyId).innerHTML="";
                        }

                        var colId1Array = data[index]["TABLE_ID1_COL_IDS"].split(",");
                        var colId2Array = data[index]["TABLE_ID2_COL_IDS"].split(",");
                        if(colId1Array.length>0&&colId1Array.length==colId2Array.length){

                            if(colId1Array[0]==""&&colId2Array[0]==""&&colId1Array.length==1){
                                newAddRelColumnRow(null,true,onlyId,false,dimRel);
                            }else{
                                newAddRelColumnRow(null,false,onlyId,false,dimRel);
                            }
                            for(var j=0;j<colId1Array.length;j++){

                                var oneData={};//数据格式：{colId1:"",colName1:"",colId2:"",colName2:"",colType1:"",colType2:""}
                                oneData.colId1=colId1Array[j];
                                oneData.colName1=data[index]["COLINFO1"].split(",")[j];
                                oneData.colType1=data[index]["COLTYPE1"].split(",")[j];
                                oneData.colId2=colId2Array[j];
                                oneData.colName2=data[index]["COLINFO2"].split(",")[j];
                                oneData.colType2=data[index]["COLTYPE2"].split(",")[j];
                                newRelCopyColumnData(oneData,newRelColumnNum,onlyId);


                                if(j==colId1Array.length-1){
//                                    addRelColumnRow(null, true);
                                }else{
                                    //增加行操作
                                    newAddRelColumnRow(null,false,onlyId,false,dimRel);
                                }

                            }
                        }else{
                            newAddRelColumnRow(null,true,onlyId,false,dimRel);
                        }

                        var confirmButton = Tools.getButtonNode("确定",getBasePath()+'/meta/resource/images/ok.png');
                        confirmButton.style.cssFloat='left';
                        confirmButton.style.styleFloat='left';
                        confirmButton.onclick = function() {
                            if(newSaveLink(tblId1,tbl2Id,num)){
                                linkDetailWindow.close();
                            }
                        };
                        var cancelButton = Tools.getButtonNode("取消",getBasePath()+'/meta/resource/images/cancel.png');
                        cancelButton.style.cssFloat='left';
                        cancelButton.style.styleFloat='left';
                        cancelButton.onclick = function() {
                            linkDetailWindow.close();
                        };
                        var addTabButton = Tools.getButtonNode("添加关系",getBasePath()+'/meta/resource/images/addDim.png');
                        addTabButton.style.marginLeft='60px';
                        addTabButton.style.cssFloat='left';
                        addTabButton.style.styleFloat='left';
                        addTabButton.onclick = function() {
                            linkDetailWindow._numIndex=num;
                            //添加关系
                            newNewTab(tabbar, num, tblId1, tbl2Id, data0, linkDetailWindow);
                            num++;
                        };

                        $("_newConfirm_"+onlyId).appendChild(addTabButton);
                        $("_newConfirm_"+onlyId).appendChild(confirmButton);
                        $("_newConfirm_"+onlyId).appendChild(cancelButton);

                        break;
                    }
                }
            }



            if(num > 0){
                tabbar.setTabActive("tab_"+tblId1+"_"+tbl2Id+"_"+0);
            }
        });
    }else{
        var max=0;

        for(var i1=0; true;i1++){
            max=i1;
            if(!linkData[from.replace("TABLE_","")+"_"+to.replace("TABLE_","")+"_"+i1]){
                break;
            }
        }
        tableId1 = from.replace("TABLE_","");
        tableId2 = to.replace("TABLE_","");
        for(var index=0; true;index++){
            var tblId1=from.replace("TABLE_","");
            var tbl2Id=to.replace("TABLE_","");
            var onlyId = tblId1+"_"+tbl2Id+"_"+index;
            if(!linkData[onlyId]){
                return;
            }
            var dimRel=linkData[onlyId]["dimRel"];
            tabbar.addTab("tab_"+onlyId,"关系"+(index+1),"70px");
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

                    var addDimRel = "add";
                    $("_headOperate_"+onlyId).innerHTML='<img src="../../../resource/images/edit_add.png" title="增加" onclick="newAddRelColumnRow(null,true,\''+onlyId+'\',false,\''+addDimRel+'\')" style="width:16px;height: 16px;cursor: pointer">';
                    $("_dimRel_"+onlyId).value=dimRel;
                    if(linkData[onlyId]["relType"]==1){
                        $("_newRelType_"+onlyId).value=1;
                    }else if(linkData[onlyId]["relType"]==2){
                        $("_newRelType_"+onlyId).value=2;
                    }else if(linkData[onlyId]["relType"]==3){
                        $("_newRelType_"+onlyId).value=3;
                    }else{
                        $("_newRelType_"+onlyId).value=2;
                    }
                    $("_newRelComm_"+onlyId).innerHTML=linkData[onlyId]["relComm"];
                    var colId1Array = linkData[onlyId]["colName1Id"].split(",");
                    var colId2Array = linkData[onlyId]["colName2Id"].split(",");
                    if(colId1Array.length>0&&colId1Array.length==colId2Array.length){
                        newAddRelColumnRow(null,false,onlyId,false,dimRel);
                        for(var j=0;j<colId1Array.length;j++){

                            var oneData={};//数据格式：{colId1:"",colName1:"",colId2:"",colName2:"",colType1:"",colType2:""}
                            oneData.colId1=colId1Array[j];
                            oneData.colName1=linkData[onlyId]["colName1"].split(",")[j];
                            oneData.colType1=linkData[onlyId]["colType1"].split(",")[j];
                            oneData.colId2=colId2Array[j];
                            oneData.colName2=linkData[onlyId]["colName2"].split(",")[j];
                            oneData.colType2=linkData[onlyId]["colType2"].split(",")[j];
                            newRelCopyColumnData(oneData,newRelColumnNum,onlyId);
                            if(j==colId1Array.length-1){
//                                    addRelColumnRow(null, true);
                            }else{
                                //增加行操作
                                newAddRelColumnRow(null,false,onlyId,false,dimRel);
                            }

                        }
                    }
                    var confirmButton = Tools.getButtonNode("确定",getBasePath()+'/meta/resource/images/ok.png');
                    confirmButton.style.cssFloat='left';
                    confirmButton.style.styleFloat='left';
                    confirmButton.onclick = function() {
                        if(newSaveLink(tblId1,tbl2Id,index)){
                            linkDetailWindow.close();
                        }
                    };
                    var cancelButton = Tools.getButtonNode("取消",getBasePath()+'/meta/resource/images/cancel.png');
                    cancelButton.style.cssFloat='left';
                    cancelButton.style.styleFloat='left';
                    cancelButton.onclick = function() {
                        linkDetailWindow.close();
                    };
                    var addTabButton = Tools.getButtonNode("添加关系",getBasePath()+'/meta/resource/images/addDim.png');
                    addTabButton.style.marginLeft='60px';
                    addTabButton.style.cssFloat='left';
                    addTabButton.style.styleFloat='left';
                    var dataI=linkData[tblId1+"_"+tbl2Id+"_"+index];
                    addTabButton.onclick = function() {
                        linkDetailWindow._numIndex=max;
                        newNewTab(tabbar, max, tblId1, tbl2Id, dataI, linkDetailWindow);
                        max++;
                    };

                    $("_newConfirm_"+tblId1+"_"+tbl2Id+"_"+index).appendChild(addTabButton);
                    $("_newConfirm_"+tblId1+"_"+tbl2Id+"_"+index).appendChild(confirmButton);
                    $("_newConfirm_"+tblId1+"_"+tbl2Id+"_"+index).appendChild(cancelButton);

                    break;
                }
            }
            tabbar.setTabActive("tab_"+tblId1+"_"+tbl2Id+"_"+0);
        }




    }
};

var newRelColumnNum = {};//关联关系表格行索引

//添加一行关联信息
var newAddRelColumnRow = function(rowIndex,isNullTree,tabId,isFromFrid,dimRel){
    if(dimRel=="add"){
        dimRel = $("_dimRel_"+tabId).value + ",false";
    }
    if($("_dimRel_"+tabId)){
        $("_dimRel_"+tabId).value=dimRel;
    }
    if(!newRelColumnNum[tabId]){
        newRelColumnNum[tabId] = 0;
    }
    var dimArray = null;
    if(dimRel){
        dimArray = dimRel.split(",");
    }
    rowIndex = (rowIndex == undefined || rowIndex == null) ? (++(newRelColumnNum[tabId])) : rowIndex;
    if(!isFromFrid||isFromFrid=="null"||isFromFrid=="undefined"){
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
            var input
            if(dimArray && dimArray[rowIndex-1] && dimArray[rowIndex-1] != 'false'){
                input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"newColinfo1",id:"_newColinfo1_" +
                    tabId+"_"+rowIndex,type:"TEXT",style:"width: 90%;",disabled:true});
            }else{
                input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"newColinfo1",id:"_newColinfo1_" +
                    tabId+"_"+rowIndex,type:"TEXT",style:"width: 90%;"});
            }
            var inputId = dhx.html.create("input", {name:"newColinfo1Id", id:"_newColinfo1Id_"+tabId+"_"+rowIndex, type:"HIDDEN"});
            td.appendChild(input);
            td.appendChild(inputId);
            //字段类型
            var inputType = dhx.html.create("input", {name:"newColinfo1Type", id:"_newColinfo1Type_"+tabId+"_"+rowIndex, type:"HIDDEN"});
            td.appendChild(inputType);
            if(isNullTree){
                newQueryColTree(tableId1,$("_newColinfo1_"+tabId+"_"+rowIndex),null,$("_newColinfo1Id_"+tabId+"_"+rowIndex),$("_newColinfo1Type_"+tabId+"_"+rowIndex));
            }

            //第二列(关联表的列)
            td = tr.insertCell(1);
            td.align = "center";
            var input;
            if(dimArray && dimArray[rowIndex-1] && dimArray[rowIndex-1] != 'false'){
                input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"newColinfo2",id:"_newColinfo2_" +
                    tabId+"_"+rowIndex,type:"TEXT",style:"width: 90%;",disabled:true});
            }else{
                input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"newColinfo2",id:"_newColinfo2_" +
                    tabId+"_"+rowIndex,type:"TEXT",style:"width: 90%;"});
            }

            var inputId = dhx.html.create("input", {name:"newColinfo2Id", id:"_newColinfo2Id_"+tabId+"_"+rowIndex, type:"HIDDEN"});
            td.appendChild(input);
            td.appendChild(inputId);
            //字段类型
            inputType = dhx.html.create("input", {name:"newColinfo2Type", id:"_newColinfo2Type_"+tabId+"_"+rowIndex, type:"HIDDEN"});
            td.appendChild(inputType);
            if(isNullTree){
                newQueryColTree(tableId2,$("_newColinfo2_"+tabId+"_"+rowIndex),null,$("_newColinfo2Id_"+tabId+"_"+rowIndex),$("_newColinfo2Type_"+tabId+"_"+rowIndex));
            }


            //操作列
            td = tr.insertCell(2);
            td.align = "center";
            //新增一行不能删除。
            td.innerHTML = "&nbsp;";
            //已经编辑的行可以删除。
//        var lastTr=Tools.findPreviousSibling(tr);
            if(!dimArray || !dimArray[rowIndex-1] || dimArray[rowIndex-1] == 'false'){
                td.innerHTML = '<img src="../../../resource/images/cancel.png" title="删除" onclick="newRemoveRowRel(this,\''+tabId+'\',\''+isFromFrid+'\')" style="width:16px;height: 16px;cursor: pointer">';
            }

        }
    }else{
        if(!$("_newRelColumnRow_Grid_" + tabId + "_" + rowIndex)){//表示子窗体tabId的第rowIndex行
            if(newRelColumnNum[tabId] < rowIndex){
                newRelColumnNum[tabId] = rowIndex;
            }
            var tr = document.createElement("tr");
            tr._index = rowIndex;
            tr.id = "_newRelColumnRow_Grid_"+ tabId + "_" + rowIndex;
            $("_newRelContentBody_Grid_" + tabId).appendChild(tr);

            //第一列(主表的列)
            var td = tr.insertCell(0);
            td.align = "center";
            var input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"newColinfo1",id:"_newColinfo1_" +
                tabId+"_"+rowIndex,type:"TEXT",style:"width: 90%;"});
            var inputId = dhx.html.create("input", {name:"newColinfo1Id", id:"_newColinfo1Id_"+tabId+"_"+rowIndex, type:"HIDDEN"});
            td.appendChild(input);
            td.appendChild(inputId);
            //字段类型
            var inputType = dhx.html.create("input", {name:"newColinfo1Type", id:"_newColinfo1Type_"+tabId+"_"+rowIndex, type:"HIDDEN"});
            td.appendChild(inputType);
            if(isNullTree){
                newQueryColTree(tableId1,$("_newColinfo1_"+tabId+"_"+rowIndex),null,$("_newColinfo1Id_"+tabId+"_"+rowIndex),$("_newColinfo1Type_"+tabId+"_"+rowIndex));
            }

            //第二列(关联表的列)
            td = tr.insertCell(1);
            td.align = "center";
            var input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"newColinfo2",id:"_newColinfo2_" +
                tabId+"_"+rowIndex,type:"TEXT",style:"width: 90%;"});
            var inputId = dhx.html.create("input", {name:"newColinfo2Id", id:"_newColinfo2Id_"+tabId+"_"+rowIndex, type:"HIDDEN"});
            td.appendChild(input);
            td.appendChild(inputId);
            inputType= dhx.html.create("input", {name:"newColinfo2Type", id:"_newColinfo2Type_"+tabId+"_"+rowIndex, type:"HIDDEN"});
            td.appendChild(inputType);
            if(isNullTree){
                newQueryColTree(tableId2,$("_newColinfo2_"+tabId+"_"+rowIndex),null,$("_newColinfo2Id_"+tabId+"_"+rowIndex),$("_newColinfo2Type_"+tabId+"_"+rowIndex));
            }


            //操作列
            td = tr.insertCell(2);
            td.align = "center";
            //新增一行不能删除。
            td.innerHTML = "&nbsp;";
            //已经编辑的行可以删除。
//        var lastTr=Tools.findPreviousSibling(tr);
            td.innerHTML = '<img src="../../../resource/images/cancel.png" title="删除" onclick="newRemoveRowRel(this,\''+tabId+'\',\''+isFromFrid+'\')" style="width:16px;height: 16px;cursor: pointer">';
        }
    }


};

/**
 * 加载表格关联信息
 * 这里data是一个Map，数据格式：{colId1:"",colName1:"",colId2:"",colName2:"",colType1:"",colType2:""}
 * @param tableData
 */
var newRelCopyColumnData=function(data,rowIndex,tabId){
    //设置列1输入框
    if(data.colName1){
        newQueryColTree(tableId1,$("_newColinfo1_"+tabId+"_"+rowIndex[tabId]),data.colId1,$("_newColinfo1Id_"+tabId+"_"+rowIndex[tabId]),$("_newColinfo1Type_"+tabId+"_"+rowIndex[tabId]));
        dwr.util.setValue("_newColinfo1_"+tabId+"_"+rowIndex[tabId], data.colName1);
    }
    //设置列1Id
    if(data.colId1){
        dwr.util.setValue("_newColinfo1Id_"+tabId+"_"+rowIndex[tabId], data.colId1);
    }
    //设置列1Type
    if(data.colType1){
        dwr.util.setValue("_newColinfo1Type_"+tabId+"_"+rowIndex[tabId], data.colType1);
    }
    //设置列2输入框
    if(data.colName2){
        newQueryColTree(tableId2,$("_newColinfo2_"+tabId+"_"+rowIndex[tabId]),data.colId2,$("_newColinfo2Id_"+tabId+"_"+rowIndex[tabId]),$("_newColinfo2Type_"+tabId+"_"+rowIndex[tabId]));
        dwr.util.setValue("_newColinfo2_"+tabId+"_"+rowIndex[tabId], data.colName2);
    }
    //设置列2Id
    if(data.colId2){
        dwr.util.setValue("_newColinfo2Id_"+tabId+"_"+rowIndex[tabId], data.colId2);
    }
    //设置列2Type
    if(data.colType2){
        dwr.util.setValue("_newColinfo2Type_"+tabId+"_"+rowIndex[tabId], data.colType2);
    }
};

var deleteNum = {};

//关联关系维护中删除一行
var newRemoveRowRel=function(img,tabId,isFromGrid){
    if(!isFromGrid||isFromGrid=="null"||isFromGrid=="undefined"||isFromGrid=="false"){
        var dimRel = $("_dimRel_"+tabId).value;
        if(!deleteNum[tabId]){
            deleteNum[tabId] = 0;
        }
        deleteNum[tabId] ++;
        //获取行得index
        var rowIndex = img.parentNode.parentNode._index;
        var trId = "_newRelColumnRow_" +tabId+"_"+ rowIndex;
        $(trId).parentNode.removeChild($(trId));
        var dimRArray = dimRel.split(",");
        if(dimRArray.length>=rowIndex){
            dimRArray.splice(rowIndex-1,1);
        }
        dimRel = dimRArray.toString();
        $("_dimRel_"+tabId).value = dimRel;

    }else{
        var rowIndex = img.parentNode.parentNode._index;
        var trId = "_newRelColumnRow_Grid_" +tabId+"_"+ rowIndex;
        $(trId).parentNode.removeChild($(trId));
    }

};

/**
 * 加载列树
 * @param selectGroup
 * @param form
 */
var newQueryColTree=function(tableId,target,checkedValue,idTarget,typeTarget){
    tableId=tableId|| global.constant.defaultRoot;
    var div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 2px #eee solid;height: 210px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white;"
    });
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height: 210px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white;"
    });
    document.body.appendChild(div);
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
    tree.attachEvent("onSelect",function(id){
        tree.setCheck(id,1);
        var checkedData = [];
        var allChecked = tree.getAllChecked();
        if (allChecked) {
            var nodes = typeof allChecked == "string" ? allChecked.split(",") : [allChecked];
            var textValue="";
            var idStr="";
            var typeStr="";
            for (var i = 0; i < nodes.length; i++) {
                checkedData.push({id:nodes[i],text:tree.getItemText(nodes[i])});
                textValue=textValue+(tree.getItemText(nodes[i]).split(" 类型："))[0]+",";
                idStr=idStr+nodes[i]+",";
                typeStr=typeStr+(tree.getItemText(nodes[i]).split(" 类型："))[1]+",";
                if(i==nodes.length-1){
                    textValue=textValue.substring(0,textValue.length-1);
                    idStr=idStr.substring(0,idStr.length-1);
                    typeStr=typeStr.substring(0,typeStr.length-1);
                }
            }
            target.value=textValue;
            if(idTarget){
                idTarget.value=idStr;
            }
            if(typeTarget){
                typeTarget.value=typeStr;
            }
        }else{
            target.value="";
        }
//        div.style.display = "none";
    });
    tree.attachEvent("onCheck",function(id){
        var checkedData = [];
        var allChecked = tree.getAllChecked();
        if (allChecked) {
            var nodes = typeof allChecked == "string" ? allChecked.split(",") : [allChecked];
            var textValue="";
            var idStr="";
            var typeStr="";
            for (var i = 0; i < nodes.length; i++) {
                checkedData.push({id:nodes[i],text:tree.getItemText(nodes[i])});
                textValue=textValue+(tree.getItemText(nodes[i]).split(" 类型："))[0]+",";
                idStr=idStr+nodes[i]+",";
                typeStr=typeStr+(tree.getItemText(nodes[i]).split(" 类型："))[1]+",";
                if(i==nodes.length-1){
                    textValue=textValue.substring(0,textValue.length-1);
                    idStr=idStr.substring(0,idStr.length-1);
                    typeStr=typeStr.substring(0,typeStr.length-1);
                }
            }
            target.value=textValue;
            if(idTarget){
                idTarget.value=idStr;
            }
            if(typeTarget){
                typeTarget.value=typeStr;
            }
        }else{
            target.value="";
        }
    });

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
        Tools.addEvent(target,"click",function(){
            div.style.width = '250px';
//            if(isCheckBox){
//                Tools.divPromptCtrl(div,target,true);
//            }else{
            Tools.divPromptCtrl(div,target,true);
//            }
            div.style.display="block";
        })
    });

};

/**
 * 添加一个关系
 * @param tabbar
 * @param num
 */
var newNewTab = function(tabbar,num,tblId1,tbl2Id,data,linkDetailWindow){
    var onlyId = tblId1+"_"+tbl2Id+"_"+num;
    tabbar.addTab("tab_"+onlyId, "新建关系", "70px");
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
            $("_headOperate_"+onlyId).innerHTML='<img src="../../../resource/images/edit_add.png" title="增加" onclick="newAddRelColumnRow(null,true,\''+onlyId+'\',false)" style="width:16px;height: 16px;cursor: pointer">';
            $("_newRelType_"+onlyId).value=2;
            $("_newRelComm_"+onlyId).innerHTML="";
            newAddRelColumnRow(null,true,onlyId,false);

            var confirmButton = Tools.getButtonNode("确定",getBasePath()+'/meta/resource/images/ok.png');
//            confirmButton.style.marginLeft='110px';
            confirmButton.style.cssFloat='left';
            confirmButton.style.styleFloat='left';
            confirmButton.onclick = function() {
                if(newSaveLink(tblId1,tbl2Id,num+1)){
                    linkDetailWindow.close();
                }
            };
            var cancelButton = Tools.getButtonNode("取消",getBasePath()+'/meta/resource/images/cancel.png');
            cancelButton.style.cssFloat='left';
            cancelButton.style.styleFloat='left';
            cancelButton.onclick = function() {
                linkDetailWindow.close();
            };
            var addTabButton = Tools.getButtonNode("添加关系",getBasePath()+'/meta/resource/images/addDim.png');
            addTabButton.style.marginLeft='60px';
            addTabButton.style.cssFloat='left';
            addTabButton.style.styleFloat='left';
            addTabButton.onclick = function() {
                var tabNum = linkDetailWindow._numIndex;
                //添加关系
                newNewTab(tabbar, tabNum+1, tblId1, tbl2Id, null, linkDetailWindow);
                linkDetailWindow._numIndex ++;
            };
            $("_newConfirm_"+onlyId).appendChild(addTabButton);
            $("_newConfirm_"+onlyId).appendChild(confirmButton);
            $("_newConfirm_"+onlyId).appendChild(cancelButton);
            break;
        }
    }
    tabbar.setTabActive("tab_"+onlyId);
};

/**
 * 保存箭头数据
 * @param tblId1
 * @param tbl2Id
 * @param index
 */
var newSaveLink = function(tblId1, tbl2Id, index){

    var offset=0;

    for(var i=0; i<index; i++){
        var onlyTabId = tblId1+"_"+tbl2Id+"_"+i;
        var deleteN=deleteNum[onlyTabId];
        var canAdd=true;
        for(var j=0;j<templateRemoveLinkData.length;j++){
            if(templateRemoveLinkData[j].replace("tab_","")==onlyTabId){
                linkData[onlyTabId] = null;
                delete linkData[onlyTabId];
                canAdd=false;
                break;
            }
        }
        var colinfo1Str = "";
        var colinfo1IdStr = "";
        //列1的版本
        var colinfo1TypeStr = "";

        var colinfo2Str = "";
        var colinfo2IdStr = "";
        //列2的版本
        var colinfo2TypeStr = "";

        for(var ii=1; true; ii++){
            if($("_newColinfo1_"+onlyTabId+"_"+ii)&&$("_newColinfo1Id_"+onlyTabId+"_"+ii)
                &&$("_newColinfo2_"+onlyTabId+"_"+ii)&&$("_newColinfo2Id_"+onlyTabId+"_"+ii)){
                colinfo1Str = colinfo1Str + $("_newColinfo1_"+onlyTabId+"_"+ii).value + ",";
                colinfo1IdStr = colinfo1IdStr + $("_newColinfo1Id_"+onlyTabId+"_"+ii).value + ",";
                colinfo2Str = colinfo2Str + $("_newColinfo2_"+onlyTabId+"_"+ii).value + ",";
                colinfo2IdStr = colinfo2IdStr + $("_newColinfo2Id_"+onlyTabId+"_"+ii).value + ",";
                colinfo1TypeStr = colinfo1TypeStr + $("_newColinfo1Type_"+onlyTabId+"_"+ii).value + ",";
                colinfo2TypeStr = colinfo2TypeStr + $("_newColinfo2Type_"+onlyTabId+"_"+ii).value + ",";
            }else{
//                if(deleteNum[onlyTabId] && deleteNum[onlyTabId] > 0){
//                    deleteNum[onlyTabId] --;
//                    continue;
//                }
                if(deleteN && deleteN > 0){
                    deleteN --;
                    continue;
                }

                if(colinfo1Str.length>0&&colinfo1IdStr.length>0&&colinfo2Str.length>0&&colinfo2IdStr.length>0){
                    colinfo1Str = colinfo1Str.substring(0, colinfo1Str.length - 1);
                    colinfo1IdStr = colinfo1IdStr.substring(0, colinfo1IdStr.length - 1);
                    colinfo2Str = colinfo2Str.substring(0, colinfo2Str.length - 1);
                    colinfo2IdStr = colinfo2IdStr.substring(0, colinfo2IdStr.length - 1);
                    colinfo1TypeStr = colinfo1TypeStr.substring(0, colinfo1TypeStr.length - 1);
                    colinfo2TypeStr = colinfo2TypeStr.substring(0, colinfo2TypeStr.length - 1);
                }
                break;
            }
        }
//        alert("colinfo1TypeStr : " + colinfo1TypeStr);
//        alert("colinfo2TypeStr : " + colinfo2TypeStr);
        var colinfo1TypeArray = colinfo1TypeStr.split(",");
        var colinfo2TypeArray = colinfo2TypeStr.split(",");
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
            if(colinfo2TypeArray[kk]==""||colinfo1TypeArray[kk]==""){
                if(canAdd){
                    dhx.alert("请将列信息填写完整！");
                    return false;
                }
            }
            if(colinfo2TypeArray[kk]=="DATE"){
                dhx.alert("列"+(colinfo2Str.split(","))[kk]+"是DATE类型的列，不能设置关联！");
                return false;
            }
            if(colinfo1TypeArray[kk]=="DATE"){
                dhx.alert("列"+(colinfo1Str.split(","))[kk]+"是DATE类型的列，不能设置关联！");
                return false;
            }
            if((colinfo2TypeArray[kk]=="NUMBER"&&colinfo1TypeArray[kk]!="NUMBER")||
                (colinfo1TypeArray[kk]=="NUMBER"&&colinfo2TypeArray[kk]!="NUMBER")){
                dhx.alert("列"+(colinfo2Str.split(","))[kk]+"数据类型为"+colinfo2TypeArray[kk]+"，不能与列"+(colinfo1Str.split(","))[kk]+"数据类型为"+colinfo1TypeArray[kk]+"相关联！");
                return false;
            }
        }
        //判断完全相同的两列只能出现一次
        var colinfo1Array = colinfo1Str.split(",");
        var colinfo2Array = colinfo2Str.split(",");
        for(var i1=0; i1<colinfo1Array.length; i1++){
            for(var j1=i1+1; j1<colinfo1Array.length; j1++){
                if(colinfo1Array[i1]==colinfo1Array[j1]){
                    if(colinfo2Array[i1]==colinfo2Array[j1]){
                        dhx.alert("不能设置完全重复的两条关联列信息：列"+colinfo1Array[i1]+"和列"+colinfo2Array[i1]);
                        return false;
                    }
                }
            }
        }
        //判断关系说明超长
        if(document.getElementById("_newRelComm_"+onlyTabId)&&
            document.getElementById("_newRelComm_"+onlyTabId).value.length>256){
            dhx.alert("关系说明输入内容超出最大长度256字符！");
            return false;
        }
        deleteNum[onlyTabId] = 0;
        if(canAdd){
            if(document.getElementById("_dimRel_"+onlyTabId).value=="undefined"){
                document.getElementById("_dimRel_"+onlyTabId).value=false;
            }
            linkData[tblId1+"_"+tbl2Id+"_"+offset]={
                colName1:colinfo1Str,
                colName1Id:colinfo1IdStr,
                colType1:colinfo1TypeStr,
                colName2:colinfo2Str,
                colName2Id:colinfo2IdStr,
                colType2:colinfo2TypeStr,
                relType:document.getElementById("_newRelType_"+onlyTabId).value,
                relComm:document.getElementById("_newRelComm_"+onlyTabId).value,
                dimRel:document.getElementById("_dimRel_"+onlyTabId).value
            };
            offset++;
        }
    }
    delete linkData[tblId1+"_"+tbl2Id+"_"+offset];
    return true;
};

