/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        maintain.js
 *Description：
 *       维护表类JS
 *Dependent：
 *       dhtmlx.js、dwr相关JS文件、dhtmlxExtend.js、tool。js等。
 *Author:
 *        张伟
 *Finished：
 *       2011-11-04
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
    filterColumns:["tableName","tableNameCn","codeName","tableGroupName","dataSourceName","tableOwner","_buttons"],
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData) {
        var userData = {};
        userData.tableNameCn=rowData.tableNameCn;
        userData.tableVersion=rowData.tableVersion;
        userData.tableTypeId=rowData.tableTypeId;
        userData.tableState=rowData.tableState;
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
        if (columnName == '_buttons') {//如果是第3列。即操作按钮列
            if(rowData.tableState==0){//无效表
                return "getRoleButtonsColOff";
            }else{
                return "getRoleButtonsColOn";
            }
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};
/**
 * 数据展现转换器
 */
var maintainConverter = new dhtmxGridDataConverter(maintainConfig);


//表类数据源相关Action
var tableDataSourceConverter = new dhtmlxComboDataConverter({
    valueColumn:"dataSourceId",
    textColumn:"dataSourceName"
});
dwrCaller.addAutoAction("queryTableDataSource", "TableViewAction.queryTableDataSource");
dwrCaller.addDataConverter("queryTableDataSource", tableDataSourceConverter);
dwrCaller.addAutoAction("offlineTable", MaintainRelAction.offlineTable);
dwrCaller.addAutoAction("onlineTable", MaintainRelAction.onlineTable);
//层次分类下拉框Action
var tableTypeData = null;

var maintainInit=function(){
    var maintainLayout = new dhtmlXLayoutObject(document.body, "2E");
    maintainLayout.cells("a").setText("维护表类");
    maintainLayout.cells("b").hideHeader();
    maintainLayout.cells("a").setHeight(80);
    maintainLayout.cells("a").fixSize(false, true);
    maintainLayout.hideConcentrate();
    maintainLayout.hideSpliter();//移除分界边框。
    //加载查询表单
    var queryform = maintainLayout.cells("a").attachForm([
        {type:"settings",position: "label-left", inputWidth: 120},
        {type:"combo",label:"数据源：",name:"dataSourceId", labelWidth: 43,inputHeight : 22,options:[{value:"",text:"全部",selected:true}],readonly:true},
        {type:"newcolumn"},
        {type:"combo",label:"层次分类：",name:"tableTypeId",inputHeight : 22, labelWidth: 55,options:[{value:"",text:"全部",selected:true}],readonly:true},
        {type:"newcolumn"},
        {type:"input",label:"业务类型：", labelWidth: 55,inputHeight : 17,name:"tableGroup"},
        {type:"newcolumn"},
        {type:"input",label:"关键字：", labelWidth: 43,inputHeight : 17,name:"keyWord"},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询",offsetLeft : 0,offsetTop : 0}
    ]);
    var maintainParam = new biDwrMethodParam();
    maintainParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
        	var formData=queryform.getFormData();
            formData.keyWord=Tools.trim(queryform.getInput("keyWord").value);
            return formData;
        }
        }
    ]);
    //加载所有的表类数据源
    queryform.getCombo("dataSourceId").loadXML(dwrCaller.queryTableDataSource, function() {
        queryform.setFormData({dataSourceId:""});//初始化页面默认显示“----”
    });
    //加载层次分类
    tableTypeData = getComboByRemoveValue("TABLE_TYPE");
    queryform.getCombo("tableTypeId").addOption(tableTypeData.options);

	queryform.getCombo("tableTypeId").attachEvent('onChange',function(){//绑定层次分类combo数据改变的时候业务类型combo的同步
    	queryform.setFormData({tableGroup:""});
	});
    //加载部门树
    queryGroupTree(null,queryform);

    var base = getBasePath();
    //操作列按钮
    var buttons = {
        basicInfo:{name:"basicInfo",text:"修改基本信息",imgEnabled:base + "/meta/resource/images/transparent.gif",
            imgDisabled:base + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                showPopUpWindow("basicInfo",rowData);
            }
        },
        tableRef:{name:"tableRef",text:"修改关系信息",imgEnabled:base + "/meta/resource/images/transparent1.gif",
            imgDisabled:base + "/meta/resource/images/transparent1.gif",onclick:function(rowData) {
                showPopUpWindow("tableRef",rowData);
            }
        },
        lookTable:{name:"lookTable",text:"查看实例信息",imgEnabled:base + "/meta/resource/images/transparent1.gif",
            imgDisabled:base + "/meta/resource/images/transparent1.gif",onclick:function(rowData) {
                showPopUpWindow("tableInstance",rowData);
            }
        },
        tableInstance:{name:"tableInstance",text:"同步到实例",imgEnabled:base + "/meta/resource/images/transparent2.gif",
            imgDisabled:base + "/meta/resource/images/transparent2.gif",onclick:function(rowData) {
                showPopUpWindow("diffAnalysis",rowData);
            }
        },
        offlineTable:{name:"offlineTable",text:"表类下线",imgEnabled:base + "/meta/resource/images/transparent3.gif",
            imgDisabled:base + "/meta/resource/images/transparent3.gif",onclick:function(rowData) {
                dhx.confirm("确认将"+rowData.data[0]+"下线？",function(r){
                    if(r){
                        offlineTable(rowData.id, rowData.userdata.tableVersion);
                    }
                });
            }
        },
        onlineTable:{name:"onlineTable",text:"表类上线",imgEnabled:base + "/meta/resource/images/transparent3.gif",
            imgDisabled:base + "/meta/resource/images/transparent3.gif",onclick:function(rowData) {
                dhx.confirm("确认将"+rowData.data[0]+"上线？",function(r){
                    if(r){
                        onlineTable(rowData.id, rowData.userdata.tableVersion);
                    }
                });
            }
        }
    };
    //过滤列按钮摆放
    var buttonRoleCol = ['basicInfo','tableRef','lookTable','tableInstance','offlineTable'];
    var buttonRoleColOff = ['basicInfo','tableRef','lookTable','tableInstance','onlineTable'];
    //getRoleButtonsColOn
    window["getRoleButtonsColOn"]=function(){
        var res=[];
        for(var i=0;i<buttonRoleCol.length;i++){
            res.push(buttons[buttonRoleCol[i]]);
        }
        return res;
    };

    window["getRoleButtonsColOff"]=function(){
        var res=[];
        for(var i=0;i<buttonRoleColOff.length;i++){
            res.push(buttons[buttonRoleColOff[i]]);
        }
        return res;
    };

    //添加datagrid
    mygrid = maintainLayout.cells("b").attachGrid();
    mygrid.setHeader("表类名称,中文名称,层次分类,业务类型,数据源,所属用户,操作");
    mygrid.setHeaderAlign("center,center,center,center,center,center,center");
    mygrid.setInitWidthsP("16,12,8,8,8,10,"+(window.screen.width>1024?"38":"50"));
    mygrid.setColAlign("left,left,left,left,left,left,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,txt,sb");
    mygrid.enableCtrlC();
    mygrid.enableResizing("true,true,true,true,true,false,false");
    mygrid.setColSorting("na,na,na,na,na,na,na");
    mygrid.enableMultiselect(false);
    mygrid.setColumnIds("tableName","tableNameCn","codeName","tableGroupName","dataSourceName","tableOwner","");
    mygrid.enableTooltips("true,true,true,true,true,true,false");
    mygrid.init();
    mygrid.defaultPaging(20);

//    var gridData = Tools.dwr({
//        dwrMethod : "TableViewAction.queryTablesMatain",
//        param : maintainParam,
//        converter:maintainConverter
//    });
    var gridDataUrl = Tools.dwr(TableViewAction.queryTablesMatain, maintainConverter,maintainParam);
    mygrid.load(gridDataUrl,"json");

    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {
            //进行数据查询。
            mygrid.clearAll();
            mygrid.load(gridDataUrl, "json");
        }
    });
    // 添加Enter查询事件
    queryform.getInput("keyWord").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            mygrid.clearAll();
            mygrid.load(gridDataUrl, "json");
        }
    }
}
/**
 * 加载业务类型树
 */
dwrCaller.addAutoAction("queryGroupTree",TableViewAction.queryGroupTree);
var groupTreeConverter=new dhtmxTreeDataConverter({
    idColumn:"tableGroupId",pidColumn:"parGroupId",
    isDycload:false,textColumn:"tableGroupName"
});
dwrCaller.addDataConverter("queryGroupTree",groupTreeConverter);
//树动态加载Action
dwrCaller.addAction("querySubGroup",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},groupTreeConverter,false);
    TableViewAction.querySubGroup(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
/**
 * 加载业务类型树
 * @param selectGroup
 * @param form
 */
var queryGroupTree=function(selectGroup,form){
    selectGroup=selectGroup|| global.constant.defaultRoot;
    var beginId=global.constant.defaultRoot;
    //创建tree Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target=form.getInput("tableGroup");
    target.readOnly=true;
    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
//    tree.enableSmartRendering();
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubGroup);
    //树双击鼠标事件
    tree.attachEvent("onDblClick",function(nodeId){
        form.setFormData({tableGroup:tree.getItemText(nodeId),tableGroupId:nodeId});
        //关闭树
        div.style.display="none";
    });
    div.style.display="none";
    dwrCaller.executeAction("queryGroupTree",beginId,selectGroup,function(data){
//        tree.loadJSONObject(data);
        if(selectGroup){
            tree.selectItem(selectGroup); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            //联动
            var tableTypeId = form.getCombo("tableTypeId").getSelectedValue();
            //alert(tableTypeId);
            if(!tableTypeId||tableTypeId==""){
                dhx.alert("请先选择一个层次分类！");
                return;
            }
            var childs = tree.getSubItems(0);
            if(childs){
                var childIds = (childs + "").split(",");
                for(var i = 0; i < childIds.length; i++){
                    tree.deleteItem(childIds[i]);
                }
            }
            dwrCaller.executeAction("queryTableGroup",tableTypeId,function(data){

                tree.loadJSONObject(data);
            });

            div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    });
};

/**
 * 添加查询表分类Action
 */
dwrCaller.addAutoAction("queryTableGroup", "TableApplyAction.queryTableGroup");
dwrCaller.addDataConverter("queryTableGroup", new dhtmxTreeDataConverter({
    idColumn:"tableGroupId",pidColumn:"parGroupId",textColumn:"tableGroupName",
    isDycload:false
}));
dwrCaller.isShowProcess("queryTableGroup", false);


/**
 * 点击操作列的弹出窗口
 * @param value
 */
var showPopUpWindow=function(value, rowData){
    //TODO 判断是否是维度表
	if(rowData.userdata.tableTypeId != 2){
	    openMenu(rowData.data[0]+"表类维护","/meta/module/tbl/maintain/maintainDetail.jsp?focus="+value+
	        "&tableId="+rowData.id+"&tableName="+rowData.data[0]+"&tableVersion="+rowData.userdata.tableVersion+"&tableState="+rowData.userdata.tableState,
	        "top");
    }else{
        openMenu(rowData.data[0]+"表类维护","/meta/module/tbl/maintain/maintainDetail.jsp?focus="+value+
            "&tableId="+rowData.id+"&tableName="+rowData.data[0]+"&tableVersion="+rowData.userdata.tableVersion+"&tableState="+rowData.userdata.tableState+"&isDimTable=Y",
            "top");

    }
};
/**
 * 表类下线
 * @param offId
 */
var offlineTable=function(offId, version){
    dwrCaller.executeAction("offlineTable", offId, version, function(data){
        if(data.type=="error"||data.type=="invalid"){
            dhx.alert("对不起，表类下线出错，请重试！");
        }else{
            dhx.alert("表类下线成功");
            // 及时刷新
            mygrid.updateGrid(maintainConverter.convert(data.successData),"update");
        }
    });
};

/**
 * 表类上线
 * @param onId
 * @param version
 */
var onlineTable=function(onId, version){
    dwrCaller.executeAction("onlineTable", onId, version, function(data){
        if(data.type=="error"||data.type=="invalid"){
            dhx.alert("对不起，表类上线出错，请重试！");
        }else{
            dhx.alert("表类上线成功");
            // 及时刷新
            mygrid.updateGrid(maintainConverter.convert(data.successData),"update");
        }
    });
};
dhx.ready(maintainInit);