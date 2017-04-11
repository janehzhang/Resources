/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       reportApplication.js
 *Description：
 *       报表管理-
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       吴喜丽
 *Date：
 *       2012-02-27
 ********************************************************/
/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var pageSize = 20;
var queryForm;
var grid;
var tabId;
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();
var param = new biDwrMethodParam();
param.setParamConfig([
    {
        index:0,type:"fun",value:function() {
    		var data = queryForm.getFormData();
	        data.pageSize=pageSize+"";
	        if(tabId == "proCo")
	        	tabId = 1+"";
	        else if(tabId == "parCo")
	        	tabId = 2+"";
	        else if(tabId == "couCo")
	        	tabId = 3+"";
	        alert("tabId=="+tabId);
	        data.DIM_LEVEL = tabId;
	        return data;
    	}	
    }
]);
/**
 * 查询表信息Action定义
 */
dwrCaller.addAutoAction("getRepAppStaAnaMes","ReportStatisAction.getRepAppStaAnaMes",param,function(data){
});
dwrCaller.addDataConverter("getRepAppStaAnaMes",new dhtmxGridDataConverter({
	isFormatColumn:false,
    filterColumns:["DEPT_NAME","CREATETYPENUM","READTYPENUM","OPENTYPENUM"],
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
    getRowID:function(rowIndex, rowData){
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
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}));
/**
 * 初始化界面
 */
var zoneInfo;//地域信息
var dhxLayout;
var init=function(){
	dhxLayout = new dhtmlXLayoutObject(document.body, "3E");
    dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.setText("使用情况统计");
    topCell.setHeight(80);
    topCell.fixSize(true,true);
    queryForm = topCell.attachForm(
            [
                {type:"settings",position: "label-left", labelWidth: 70, inputWidth: 120},
                {type:"combo",label:"统计时间：",name:"dateTime",options:[{value:"1",text:"当天",selected:true},{value:"2",text:"本周"},{value:"3",text:"本月"}]},
                {type:"newcolumn"},
                {type:"input",label:"报表分类：",name:"repType"},//,validate: "NotEmpty"
                {type:"newcolumn"},
                {type:"input",label:"报表：",name:"repName"},
                {type:"newcolumn"},
                {type:"button",name:"queryBtn",value:"统计"}
            ]
    );
    loadReportTypeTree(queryForm);
    //数据表单
    var middleCell = dhxLayout.cells("b");
    middleCell.fixSize(true,false);
    middleCell.hideHeader();//隐藏下面的布局title
    var tabbar = middleCell.attachTabbar();
//    tabbar.setHrefMode("iframes");
    tabbar.setHrefMode("iframes-on-demand");
    //获取地域信息
    zoneInfo = getSessionAttribute('zoneInfo');
    if(zoneInfo.dimLevel == 1){//省公司
    	tabId = "proCo";
    	tabbar.addTab("proCo", "省公司部门", "120px");
    	tabbar.addTab("parCo", "分公司部门", "120px");
    }else if(zoneInfo.dimLevel == 2){//分公司
    	tabId = "parCo";
    	tabbar.addTab("parCo", "分公司部门", "120px");
    	tabbar.addTab("couCo", "县公司部门", "120px");
    }else if(zoneInfo.dimLevel == 3){//县公司
    	tabId = "couCo";
    	tabbar.addTab("couCo", "县公司部门", "120px");
    }
    tabbar.attachEvent("onSelect", function(id,last_id){
    	if(id != null){
    		tabId = id;
		    grid = tabbar.cells(tabId).attachGrid();
		    grid.setHeader("部门,创建数,订阅数,打开数");
		    grid.setInitWidthsP("25,25,25,25");
		    grid.setColAlign("left,right,right,right");
		    grid.setHeaderAlign("center,center,center,center")
		    grid.setColTypes("ro,ro,ro,ro");
		    grid.setColSorting("na,na,na,na");
		    grid.enableResizing("true,true,true,true");
		    grid.enableMultiselect(false);
		    grid.enableCtrlC();
		    grid.setColumnIds("usedept,createnum,readnum,opennum");
		    grid.init();
		    grid.defaultPaging(pageSize);
		    return true;
    	}else
    		return false;
    });
    tabbar.setTabActive(tabId);
    /*点击后需要加载数据*/
	grid.load(dwrCaller.getRepAppStaAnaMes, "json");
    /*查询按钮加载数据*/
    queryForm.attachEvent("onButtonClick", function(id) {
        if (id == "queryBtn") {
        	tabId = tabbar.getActiveTab();
            //进行数据查询。
        	grid.clearAll();
        	grid.load(dwrCaller.getRepAppStaAnaMes, "json");
        }
    });
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
    })
    ,isShowProcess:false,
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
var createGrid = function(showContent){
	 //数据表单
    var middleCell = dhxLayout.cells("b");
//    middleCell.attachTabbar();
    middleCell.attachObject("con");
    middleCell.fixSize(true,false);
    middleCell.hideHeader();//隐藏下面的布局title
    middleCell.hideStatusBar();
    middleCell.hideToolbar();
	grid = new dhtmlXGridObject(showContent);
    grid.setHeader("部门,创建数,订阅数,打开数");
    grid.setInitWidthsP("25,25,25,25");
    grid.setColAlign("left,right,right,right");
    grid.setHeaderAlign("center,center,center,center")
    grid.setColTypes("ro,ro,ro,ro");
    grid.setColSorting("na,na,na,na");
    grid.enableResizing("true,true,true,true");
    grid.enableMultiselect(false);
    grid.enableCtrlC();
    grid.setColumnIds("usedept,createnum,readnum,opennum");
    grid.init();
//    grid.defaultPaging(pageSize);
}
function select_zbb(showContent,selfObj){
	var tag = document.getElementById("tabs").getElementsByTagName("li");
	var taglength = tag.length;
	for(i=0; i<taglength; i++){
		tag[i].className = "";
	}
	selfObj.parentNode.className = "buttom-tab";
	for(i=0; j=document.getElementById("tab"+i); i++){
		j.style.display = "none";
	}
	createGrid(showContent);
	document.getElementById(showContent).style.display = "block";
}