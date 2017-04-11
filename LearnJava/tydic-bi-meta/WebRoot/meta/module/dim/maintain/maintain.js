/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *         maintain.js
 *Description：
 *       维护维护视图主JS
 *Dependent：
 *       dhtmlx.js、dwr相关JS文件、dhtmlxExtend.js、tool。js等。
 *Author:
 *        张伟
 *Finished：
 *       2011-11-07
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
var dwrCaller = new biDwrCaller();
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();

var maintainConfig = {
    idColumnName:"tableId",
    filterColumns:["tableName","tableNameCn","tableGroupName","dataSourceName","tableOwner",
        "tableState","_buttons"],
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData){
        var userData = {};
        userData.tableNameCn = rowData.tableNameCn;
        userData.tableVersion=rowData.tableVersion;
        userData.dataSourceId = rowData.dataSourceId;
        return userData;
    },
    /**
     * 获取下拉框Button的值
     * @param rowIndex
     * @param columnIndex
     * @param cellValue
     * @param rowData
     */
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        if(columnName == '_buttons'){//如果是第3列。即操作按钮列
            return "getRoleButtonsCol";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};
/**
 * 数据展现转换器
 */
var maintainConverter = new dhtmxGridDataConverter(maintainConfig);
dwrCaller.addAutoAction("queryDimView", DimMaintainAction.queryDimView, {
    dwrConfig:true,
    converter:maintainConverter
});
var maintainInit = function(){
    var maintainLayout = new dhtmlXLayoutObject(document.body, "2E");
    maintainLayout.cells("a").setText("维护维度表信息");
    maintainLayout.cells("b").hideHeader();
    maintainLayout.cells("a").setHeight(80);
    maintainLayout.cells("a").fixSize(false, true);
    maintainLayout.hideConcentrate();
    maintainLayout.hideSpliter();//移除分界边框。

    //加载查询表单
    var queryform = maintainLayout.cells("a").attachForm([
        {type:"settings",position: "label-left", inputWidth: 120},
        {type:"input",label:"维度表类：",name:"tableGroup",inputHeight : 17},
        {type:"newcolumn"},
        {type:"input",label:"关键字：",name:"keyWord",inputHeight : 17},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询",offsetLeft:0}
    ]);
    var maintainParam = new biDwrMethodParam();
    maintainParam.setParamConfig([
        {
            index:0,type:"fun",value:function(){
            var formData = queryform.getFormData();
            formData.keyWord = Tools.trim(queryform.getInput("keyWord").value);
            return formData;
        }
        }
    ]);
    //加载部门树
    loadTableGroupTree(queryform);

    var base = getBasePath();
    //操作列按钮
    var buttons = {
        updateDim:{name:"updateDim",text:"修改维度表",imgEnabled:base + "/meta/resource/images/transparent.gif",
            imgDisabled:base + "/meta/resource/images/transparent.gif",onclick:function(rowData){
                //查询表类有效版本
                DimMaintainAction.queryValidVersion(rowData.id,function(tableVersion){
                    var url="/meta/module/tbl/dim/updateDim.jsp?tableId="+rowData.id+"&tableVersion="+tableVersion;
                    window.parent.closeTab("修改维度表");
                    openMenu("修改维度表",url,"top");
                })
            }
        },
        dimType:{name:"dimType",text:"归并类型",imgEnabled:base + "/meta/resource/images/transparent1.gif",
            imgDisabled:base + "/meta/resource/images/transparent1.gif",onclick:function(rowData){
                showPopUpWindow("dimType", rowData);
            }
        },
        maintainCodeData:{name:"maintainCodeData",text:"编码数据",imgEnabled:base + "/meta/resource/images/transparent2.gif",
            imgDisabled:base + "/meta/resource/images/transparent2.gif",onclick:function(rowData){
                showPopUpWindow("maintainCodeData", rowData);
            }
        },
        codeMapping:{name:"codeMapping",text:"编码映射",imgEnabled:base + "/meta/resource/images/transparent3.gif",
            imgDisabled:base + "/meta/resource/images/transparent3.gif",onclick:function(rowData){
                showPopUpWindow("codeMapping", rowData);
            }
        } ,
        intRelValue:{name:"intRelValue",text:"接口表查询",imgEnabled:base + "/meta/resource/images/transparent3.gif",
            imgDisabled:base + "/meta/resource/images/transparent3.gif",onclick:function(rowData){
                showPopUpWindow("intRelValue", rowData);
            }
        }
    };
    window.getRoleButtonsCol = function(){
        var res = [];
        for(var i in buttons){
            res.push(buttons[i]);
        }
        return res;
    };

    //添加datagrid
    mygrid = maintainLayout.cells("b").attachGrid();
    mygrid.setHeader("表类名称,中文名称,分类类型,表类数据源,所属用户,表类状态,操作");
    mygrid.setHeaderAlign("left,center,center,center,center,center,center");
    mygrid.setInitWidthsP("18,14,15,10,8,6,"+(window.screen.width>1024?"29":"50"));
    mygrid.setColAlign("left,left,left,left,left,center,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,ro,sb");
    mygrid.enableCtrlC();
    mygrid.enableResizing("true,true,true,true,true,true,false");
    mygrid.enableTooltips("true,true,true,true,true,true,false");
    mygrid.setColSorting("na,na,na,na,na,na,na");
    mygrid.setColumnCustFormat(5, function(value){
        switch(parseInt(value)){
            case 0:
                return '停用';
            case 1:
                return '有效';
            case 2:
                return '修改';
        }
        return value;
    });//第5列进行转义
    mygrid.enableMultiselect(false);
    mygrid.init();
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.queryDimView + maintainParam, "json");

    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id){
        if(id == "query"){
            //进行数据查询。
            mygrid.clearAll();
            mygrid.load(dwrCaller.queryDimView + maintainParam, "json");
        }
    });
    // 键盘Enter查询事件
    queryform.getInput("keyWord").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            mygrid.clearAll();
            mygrid.load(dwrCaller.queryDimView + maintainParam, "json");
        }
    }
    //新增维度表类图标按钮
//    if(new RegExp(buttonRoleCol.join("|")).exec("addDim")){ //拥有新增维度表权限
//        var addDimDiv = dhx.html.create("div",
//                {style:"position: absolute;right: 5px;top:6px;width: 100px;height: 18px;cursor: pointer"},
//                "<img src='" + getBasePath() +
//                "/meta/resource/images/addDim.png' width='20px' height='20px' border='0' style='float:left'>");
//        addDimDiv
//                .innerHTML += "<div style='margin-top: 0px;position: relative;float: left;margin-left: 2px;'>新增维度表类</div>"
//        maintainLayout.polyObj["a"].childNodes[0].firstChild.appendChild(addDimDiv);
//        addDimDiv.onclick = function(){
//            openMenu("新增维度表类", "meta/module/tbl/dim/addDim.jsp", "top");
//        }
//    }
}
/**
 * 添加查询表分类Action
 */
dwrCaller.addAutoAction("queryTableGroup", TableApplyAction.queryTableGroup, {
    dwrConfig:true,
    converter: new dhtmxTreeDataConverter({
        idColumn:"tableGroupId",pidColumn:"parGroupId",textColumn:"tableGroupName",
        isDycload:false
    })
    ,isShowProcess:false,
    isDealOneParam:true
});
/**
 * 加载业务类型树形结构。
 * @param form
 */
var loadTableGroupTree = function(form){
    //创建tree Div层
    var div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    this.treeDiv = div;
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target = form.getInput("tableGroup");
    target.readOnly = true;
    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    //树双击鼠标事件
    tree.attachEvent("onClick", function(nodeId){
        if(form.getFormData().tableGroupId == nodeId){
            target.value = "";
            form.setFormData({tableGroup:"",tableGroupId:""});
        }else{
            target.value = tree.getItemText(nodeId);
            form.setFormData({tableGroup:tree.getItemText(nodeId),tableGroupId:nodeId});
        }

        //关闭树
        // div.style.display = "none";
    });
    div.style.display = "none";
    dwrCaller.executeAction("queryTableGroup", 2, function(data){
        tree.loadJSONObject(data);
        //为div添加事件
        Tools.addEvent(target, "click", function(){
            div.style.width = target.offsetWidth + 'px';
            Tools.divPromptCtrl(div, target, true);
            div.style.display = "block";
        })
    });
}
/**
 * 点击操作列的弹出窗口
 * @param value
 */
var showPopUpWindow = function(value, rowData){
    //一、维度表错误判断，1、判断维度表是否存在实例表，2、判断维度表实例表在数据中是否存在。3、判断实例表与表类是否有差异。
    DimMaintainAction.queryValidVersion(rowData.id,function(tableVersion){
        openMenu("维护维度:"+rowData.data[0], "/meta/module/dim/maintain/maintainDetail.jsp?focus=" + value + "&tableId=" + rowData.id + "&tableUser=" + rowData.data[4]+ "&tableName=" +
                                          rowData.data[0]+"&tableVersion="+tableVersion+"&dataSourceId="+rowData.userdata.dataSourceId+"&tableNameCn="+encodeURIComponent(encodeURIComponent(rowData.userdata.tableNameCn)), "top");
    });
};


window.onbeforeunload=function(){
//    maintainConfig=null;
//    maintainConverter.unload();
//    dwrCaller.unload();
//    maintainConverter=null;
//    maintainInit=null;
//    mygrid=null;
//    loadTableGroupTree=null;
//    showPopUpWindow=null;
}

dhx.ready(maintainInit);