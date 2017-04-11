/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        personalApplication.js
 *Description：
 *       个人应用情况，包括：显示自己、所在部门、所在部门最高、所在岗位、所在岗位最高的收藏数、订阅数、打开数等
 * 						能按日期(当天、本周、本月)、报表分类和报表名称进行组合查询,默认为对当天进行统计
 * 						能点击各个统计值，进入不同（收藏、订阅、打开）情况查询此报表,若有已经收藏，若没有权限，则不能收藏，或打开
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        wuxl
 ********************************************************/

/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var pageSize = 20;
var queryForm;
var grid;
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();
var param = new biDwrMethodParam();
param.setParamConfig([
    {
        index:0,type:"fun",value:function() {
        var data = queryForm.getFormData();
//        data.dateTime=data.dateTime+"";
//        if(!data.keyWord){
//            data.keyWord="";
//        }
        data.pageSize=pageSize+"";
        return data;
    }
    }
]);
/**
 * 查询表信息Action定义
 */
dwrCaller.addAutoAction("getQueryTables","ReportStatisAction.getQueryTables",param,function(data){
});
dwrCaller.addDataConverter("getQueryTables",new dhtmxGridDataConverter({
	//isFormatColumn:false,
//	idColumnName:"reportTypeId",
    filterColumns:["userNamecn","createTypeNum","READTYPENUM","OPENTYPENUM"],
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData) {
        var userData = {};
        return userData;
    },
    getRowID:function(rowIndex, rowData) {
        return rowIndex;
    },
    /**
     * 获取下拉框Button的值
     * @param rowIndex
     * @param columnIndex
     * @param cellValue
     * @param rowData
     */
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
    	if (columnName == 'userNamecn') {
    		return '<div onClick="openDialog("+"rowData["userNamecn"]"+")"></div>';
//            return '<div onClick="openDialog("'+rowData["userNamecn"]+'")">'</div>';
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}));
/**
 * 点击查看详情
 */
var openDialog=function(){
	alert(1)
}
/**
 * 初始化界面
 */
var init=function(){
	var dhxLayout = new dhtmlXLayoutObject(document.body, "2E");
    dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.setText("我的使用情况统计");
    topCell.setHeight(80);
    topCell.fixSize(true,true);
    queryForm = topCell.attachForm(
            [
                {type:"settings",position: "label-left", labelWidth: 70, inputWidth: 120},
                {type:"combo",label:"日期：",name:"dateTime",readonly:true,options:[{value:"1",text:"当天",selected:true},{value:"2",text:"本周"},{value:"3",text:"本月"}]},
                {type:"newcolumn"},
                {type:"input",label:"报表分类：",name:"repType"},//,validate: "NotEmpty"
                {type:"newcolumn"},
                {type:"input",label:"报表：",name:"repName"},
                {type:"newcolumn"},
                {type:"button",name:"queryBtn",value:"统计"}
            ]
    );
    //查询数据表单
    var dataCell = dhxLayout.cells("b");
    dataCell.fixSize(true,false);
    dataCell.hideHeader();
    grid = dataCell.attachGrid();
    grid.setHeader("使用者,创建数,订阅数,打开数");
    grid.setInitWidthsP("25,25,25,25");
    grid.setColAlign("left,right,right,right");
    grid.setHeaderAlign("center,center,center,center")
    grid.setColTypes("ro,ro,ro,ro");
    grid.setColSorting("na,na,na,na");
    grid.enableResizing("true,true,true,true");
    grid.enableMultiselect(false);
    grid.enableCtrlC();
    grid.setColumnIds("useuser,createnum,readnum,opennum");
    grid.init();
    //加载数据
    grid.load(dwrCaller.getQueryTables, "json");
//    grid.defaultPaging(pageSize);
    // 点击统计按钮事件
    queryForm.attachEvent("onButtonClick", function(id) {
        if (id == "queryBtn") {
            //进行数据查询。
            if(queryForm.validate()){
                grid.clearAll();
                grid.load(dwrCaller.getQueryTables, "json");
            }
        }
    });
    // 加载报表分类
    loadReportTypeTree(queryForm);
    dhx.env.isIE&&CollectGarbage();
}
dhx.ready(init);
/**
 * 添加查询报表分类Action
 */
dwrCaller.addAutoAction("queryReportType", ReportStatisAction.queryReportType, {
    dwrConfig:true,
    converter: new dhtmxTreeDataConverter({
        idColumn:"REPORT_TYPE_ID",pidColumn:"PARENT_ID",textColumn:"REPORT_TYPE_NAME",
        isDycload:false,
        isFormatColumn:false
    }),
    isShowProcess:false,
    isDealOneParam:true
});
/**
 * 加载报表分类树形结构。
 * @param form
 */
var loadReportTypeTree = function(form){
    //创建tree Div层
    var div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    this.treeDiv = div;
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target = form.getInput("repType");
    target.readOnly = true;
    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(dhtmlx.image_path + "csh_" + dhtmlx.skin + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    //树双击鼠标事件
    tree.attachEvent("onDblClick", function(nodeId){
        target.value = tree.getItemText(nodeId);
        form.setFormData({repType:tree.getItemText(nodeId),repTypeId:nodeId});
        //关闭树
        div.style.display = "none";
    });
    div.style.display = "none";
    dwrCaller.executeAction("queryReportType", function(data){
        tree.loadJSONObject(data);
        //为div添加事件
        Tools.addEvent(target, "click", function(){
            div.style.width = target.offsetWidth + 'px';
            Tools.divPromptCtrl(div, target, true);
            div.style.display = "block";
        })
    });
}