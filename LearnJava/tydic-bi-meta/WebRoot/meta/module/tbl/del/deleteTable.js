/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        deleteTable.js
 *Description：表类维度删除界面
 *
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

var dwrCaller = new biDwrCaller();
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();

var tablesConfig = {
    idColumnName:"tableId",
    filterColumns:["tableName","tableNameCn","codeName","tableGroupName","dataSourceName","tableOwner","_buttons"],
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
var tablesConverter = new dhtmxGridDataConverter(tablesConfig);


//表类数据源相关Action
var tableDataSourceConverter = new dhtmlxComboDataConverter({
    valueColumn:"dataSourceId",
    textColumn:"dataSourceName"
});
dwrCaller.addAutoAction("queryTableDataSource", "TableViewAction.queryTableDataSource");
dwrCaller.addDataConverter("queryTableDataSource", tableDataSourceConverter);
//层次分类下拉框Action
var tableTypeData = null;

var tablesInit=function(){
    var tablesLayout = new dhtmlXLayoutObject(document.body, "2E");
    tablesLayout.cells("a").setText("删除表类&nbsp;<span style='color:red;'>请谨慎操作此页面!</span>");
    tablesLayout.cells("b").hideHeader();
    tablesLayout.cells("a").setHeight(80);
    tablesLayout.cells("a").fixSize(false, true);
    tablesLayout.hideConcentrate();
    tablesLayout.hideSpliter();//移除分界边框。
    //加载查询表单
    var queryform = tablesLayout.cells("a").attachForm([
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
    var tablesParam = new biDwrMethodParam();
    tablesParam.setParamConfig([
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
        delTable:{name:"delTable",text:"删除",imgEnabled:base + "/meta/resource/images/transparent.gif",
            imgDisabled:base + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                confirmDelTable(rowData);
            }
        }
    };
    //过滤列按钮摆放
    var buttonRoleCol = ['delTable'];
    var buttonRoleColOff = ['delTable'];
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
    mygrid = tablesLayout.cells("b").attachGrid();
    mygrid.setHeader("表类名称,中文名称,层次分类,业务类型,数据源,所属用户,操作");
    mygrid.setHeaderAlign("center,center,center,center,center,center,center");
    mygrid.setInitWidthsP("25,20,10,13,12,10,10");
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

    var qf = Tools.dwr(DeleteTableAction.queryTablesMatain, tablesConverter,tablesParam);
    mygrid.load(qf,"json");

    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {
            //进行数据查询。
            mygrid.clearAll();
            mygrid.load(qf, "json");
        }
    });
    // 添加Enter查询事件
    queryform.getInput("keyWord").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            mygrid.clearAll();
            mygrid.load(qf, "json");
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

var confirmObj = null;
var deleteR = null;
var confirmDelTable = function(rowData){
    deleteR = rowData;
    if(!confirmObj){
        confirmObj = new Object();
        var confirmWins = new dhtmlXWindows();
        confirmWins.setImagePath(getBasePath() + "/meta/resource/images/");
        var id = "confirmdelwin";
        confirmObj.win = confirmWins.createWindow(id, 100, 100, 400, 200); //定义一个具体的window对象,默认是隐藏状态
        confirmObj.win.button("minmax1").hide();
        confirmObj.win.button("minmax2").hide();
        confirmObj.win.button("park").hide();
        confirmObj.win.denyResize();
        confirmObj.win.denyPark();
        confirmObj.win.center();//设置居中
        confirmObj.win.setModal(true);//加载mask
        confirmObj.win.setText("确认");
        confirmObj.win.setIcon('title_question.png');

        var msgDiv = document.createElement("Div");//创建win装载层
        msgDiv.setAttribute("id", "conDiv"+id);
        document.body.appendChild(msgDiv);
        msgDiv.style.cssText = "height:150px;width:400px;background-color:#fff;position:relative";
        var attachDiv = document.getElementById("conDiv" + id);
        attachDiv.style.css = "height:150px;width:360px;";
        var textDiv = document.createElement("Div"); //创建window的文字层
        var imgDiv = document.createElement("Div");//创建图片层
        imgDiv.innerHTML = '<input type="image" src="' + getBasePath() + '/meta/resource/images/question.png"/>';
        imgDiv.style.cssText = "float:left;width:50px;margin-top:10px;margin-left:10px;";
        textDiv.setAttribute("id", "conTxtDiv" + id);
        textDiv.style.cssText = "font-size:13px;height:80px;width:250px;padding-top:18px;float:right;margin-right:60px;color:red;";
        textDiv.innerHTML = "";
        attachDiv.appendChild(textDiv);
        var chkdiv = document.createElement("DIV");
        chkdiv.style.cssText = "width:350px;top:80px;left:90px;position:absolute;background-color:#ffffff;color:blue;font-weight:bold";
        chkdiv.innerHTML = "<input type='checkbox' id='chkdelfalg_' title='删除关联实体表'>删除实体表(会删除真实数据表,请谨慎操作)!";
        attachDiv.appendChild(chkdiv);
        confirmObj.textDiv = textDiv;
        attachDiv.appendChild(imgDiv);
        var btnDiv = document.createElement("Div"); //创建window的按钮层
        btnDiv.setAttribute("id", "conBtnDiv" + id);
        btnDiv.style.cssText = "width:310px;";
        attachDiv.appendChild(btnDiv);
        confirmObj.win.attachObject(attachDiv);
        var data = [
            {
                type:"label",
                list:[
                    {type:"button",
                        value:"确定",
                        name:'ok',
                        offsetLeft:80,
                        offsetTop:10
                    },
                    {
                        type:"newcolumn"
                    },
                    {
                        type:"button",
                        value:"取消",
                        name:'cancel',
                        offsetLeft:10,
                        offsetTop:10
                    }
                ]
            }
        ]
        var myForm = new dhtmlXForm("conBtnDiv" + id, data);
        myForm.attachEvent("onButtonClick", function (name, command) {
            confirmObj.win.close();
            confirmObj.win.setModal(false);
            if(name=="ok"){
                var delInstance = document.getElementById("chkdelfalg_").checked;
                dhx.showProgress("请求中","正在删除数据!");
                DeleteTableAction.deleteTable(deleteR.id,deleteR.data[0],deleteR.userdata.tableTypeId,delInstance,function(data){
                    dhx.closeProgress();
                    if(data.flag=="1"){
                        mygrid.deleteRow(deleteR.id,"delete");
                        dhx.alert("删除完成！");
                    }else{
                        dhx.alert("删除失败，发生异常:"+data.msg);
                    }
                    deleteR = null;
                });
            }
        });
        confirmObj.win.attachEvent("onShow", function (w) {
            confirmObj.win.setModal(true);//加载mask
            document.getElementById("chkdelfalg_").checked = false;
        });
        confirmObj.win.attachEvent("onClose", function (w) {
            w.hide();
            w.setModal(false);
        });
        confirmObj.win.hide();
    }
    confirmObj.textDiv.innerHTML = "此操作将会直接在数据库删掉所有版本、关系、实例等信息，且无法通过界面恢复！确定吗？";
    confirmObj.win.show();
};

dhx.ready(tablesInit);