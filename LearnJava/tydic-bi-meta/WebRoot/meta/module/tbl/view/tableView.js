/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        tableView.js
 *Description：
 *       表类查询-全息视图
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *       刘斌
 *Date：
 *       2011-10-31-10-31

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

var tableViewConfig = {
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
            return "getRoleButtonsCol";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};
/**
 * 数据展现转换器
 */
var tableViewConverter = new dhtmxGridDataConverter(tableViewConfig);


//表类数据源相关Action
var tableDataSourceConverter = new dhtmlxComboDataConverter({
    valueColumn:"dataSourceId",
    textColumn:"dataSourceName"
});
dwrCaller.addAutoAction("queryTableDataSource", "TableViewAction.queryTableDataSource");
dwrCaller.addDataConverter("queryTableDataSource", tableDataSourceConverter);


//层次分类数据源(初始时来自码表）
var tableTypeData = null;

/**
 * 初始化界面
 */
var tableViewInit=function(){
    var tableViewLayout = new dhtmlXLayoutObject(document.getElementById("container"), "2E");
    tableViewLayout.cells("a").setText("全息视图");
    tableViewLayout.cells("b").hideHeader();
    tableViewLayout.cells("a").setHeight(80);
    tableViewLayout.cells("a").fixSize(false, true);
    tableViewLayout.hideConcentrate();
    tableViewLayout.hideSpliter();//移除分界边框。
    //加载查询表单
    //实现下拉框树形展示
    var queryform = tableViewLayout.cells("a").attachForm([
        {type:"settings",position: "label-left", inputWidth: 120},
        {type:"combo",label:"数据源：",name:"dataSourceId",inputHeight : 22, labelWidth: 44,options:[{value:"",text:"全部",selected:true}],readonly:true},
        {type:"newcolumn"},
        {type:"combo",label:"层次分类：",name:"tableTypeId",inputHeight : 22, labelWidth: 55,options:[{value:"",text:"全部",selected:true}],readonly:true},
        {type:"newcolumn"},
        {type:"input",label:"业务类型：", labelWidth: 55,inputHeight : 17,name:"tableGroup"},
        {type:"newcolumn"},
        {type:"input",label:"关键字：", labelWidth: 44,inputHeight : 17,name:"keyWord"},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询",offsetLeft : 0,offsetTop : 0}
    ]);
    var tableViewParam = new biDwrMethodParam();
    tableViewParam.setParamConfig([
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

    //加载部门树
    queryGroupTree(null,queryform);
    queryform.getCombo("tableTypeId").attachEvent("onChange", function(){
        if(tableGroupId=="null"){
            queryform.setFormData({tableGroup:""});
        }else{
            if(queryform.getItemValue("tableTypeId")==tableTypeId){
                queryform.setFormData({tableGroupId:tableGroupId,tableGroup:tableGroupName});
            }else{
                queryform.setFormData({tableGroup:"",tableGroupId:""});
            }
        }

    });


    dwrCaller.addAutoAction("queryTables", "TableViewAction.queryTables", tableViewParam,
        function(data) {

        }
    );
    dwrCaller.addDataConverter("queryTables", tableViewConverter);

    var base = getBasePath();
    //操作列按钮
    var buttons = {
        basicInfo:{name:"basicInfo",text:"基本信息",imgEnabled:base + "/meta/resource/images/transparent.gif",
            imgDisabled:base + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                showPopUpWindow("basicInfo",rowData);
            }
        },
        tableRef:{name:"tableRef",text:"表类关系",imgEnabled:base + "/meta/resource/images/transparent1.gif",
            imgDisabled:base + "/meta/resource/images/transparent1.gif",onclick:function(rowData) {
                showPopUpWindow("tableRef",rowData);
            }
        },
        tableInstance:{name:"tableInstance",text:"表类实例",imgEnabled:base + "/meta/resource/images/transparent2.gif",
            imgDisabled:base + "/meta/resource/images/transparent2.gif",onclick:function(rowData) {
                showPopUpWindow("tableInstance",rowData);
            }
        },
        changeHistory:{name:"changeHistory",text:"变动历史",imgEnabled:base + "/meta/resource/images/transparent3.gif",
            imgDisabled:base + "/meta/resource/images/transparent3.gif",onclick:function(rowData) {
                showPopUpWindow("changeHistory",rowData);
            }
        }
    };
    //过滤列按钮摆放
//    var buttonRoleCol =(getRoleButtons||window.parent.getRoleButtons).call(this);
    //getRoleButtonsCol
    window["getRoleButtonsCol"]=function(){
       var res = [];
        for(var i in buttons){
            res.push(buttons[i]);
        }
        return res;
    };

    //添加datagrid
    mygrid = tableViewLayout.cells("b").attachGrid();
    mygrid.setHeader("表类名称,中文名称,层次分类,业务类型,数据源,所属用户,操作");
    mygrid.setHeaderAlign("center,center,center,center,center,center,center");
    mygrid.setInitWidthsP("20,17,10,11,10,7,25");
    mygrid.setColAlign("left,left,left,left,left,left,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,ro,sb");
    mygrid.enableCtrlC();
    mygrid.enableResizing("true,true,true,true,true,true,false");
    mygrid.setColSorting("na,na,na,na,na,na,na");
    mygrid.enableMultiselect(false);
    mygrid.setColumnIds("tableName","tableNameCn","codeName","tableGroupName","dataSourceName","tableOwner","");
    mygrid.enableTooltips("true,true,true,true,true,true,false");
    mygrid.init();
    mygrid.defaultPaging(20);
    if(tableGroupId=="null"){
        mygrid.load(dwrCaller.queryTables, "json");
    }else{
        queryform.setFormData({tableGroupId:tableGroupId,tableGroup:tableGroupName,tableTypeId:tableTypeId});
        mygrid.load(dwrCaller.queryTables, "json");
    }

    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {
            //进行数据查询。
            mygrid.clearAll();
            mygrid.load(dwrCaller.queryTables, "json");
        }
    });
    queryform.getInput("keyWord").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            mygrid.clearAll();
            mygrid.load(dwrCaller.queryTables, "json");
        }
    }
}

/**
 * 加载业务类型树
 */
dwrCaller.addAutoAction("queryGroupTree","TableViewAction.queryGroupTree");
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

    dwrCaller.executeAction("queryGroupTree",beginId,selectGroup,function(data){
        //tree.loadJSONObject(data);
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
        });
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
    //TODO 判断是否是维度表 tableTypeId==2
    if(rowData.userdata.tableTypeId != 2){
        openMenu(rowData.data[0]+"全息视图","/meta/module/tbl/view/viewDetail.jsp?focus="+value+
            "&tableId="+rowData.id+"&tableName="+rowData.data[0]+"&tableVersion="+rowData.userdata.tableVersion,
            "top","tableView_"+rowData.id);
    }else{

        openMenu(rowData.data[0]+"全息视图","/meta/module/tbl/view/viewDetail.jsp?focus="+value+
            "&tableId="+rowData.id+"&tableName="+rowData.data[0]+"&tableVersion="+rowData.userdata.tableVersion+"&isDimTable=Y",
            "top","tableView_"+rowData.id);

    }
}



dhx.ready(tableViewInit);