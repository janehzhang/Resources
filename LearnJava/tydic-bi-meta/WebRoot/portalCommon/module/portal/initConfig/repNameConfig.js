/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       batchImport.js
 *Description：
 *       通用门户--首页配置
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       吴喜丽
 *Date：
 *       2012-05-15
 ********************************************************/
/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var queryForm;
var grid;
var base = getBasePath();
var dataNum;//数据条数
var rowIds = [];//保存主键的值（tabId）
var isRowIdChange = false;
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();
//查询信息
var param = new biDwrMethodParam();
param.setParamConfig([
    {
       index:0,type:"fun",value:function() {
			var formData = queryForm.getFormData();
			formData.repName=Tools.trim(queryForm.getInput("repName").value);
			return formData;
   	   	}
    }
]);
dwrCaller.addAutoAction("getReportTabMes","ReportConfigAction.getReportTabMes",param,function(data){dataNum = data.rows.length;});
dwrCaller.addDataConverter("getReportTabMes",new dhtmxGridDataConverter({
	isFormatColumn:false,
    filterColumns:["TAB_NAME","ORDER_ID","ROLLDOWN_LAYER","DEFAULT_GRID","RPT_TYPE","TAB_DESC","LINK"],
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
    	if(columnName == 'LINK'){
    		var str = "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;' onclick=repModify("+rowData["TAB_ID"]+")>修改</a></span>";
    		str += "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;' onclick=repDel("+rowData["TAB_ID"]+")>删除</a></span>";
    		str += "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;' onclick=confCol("+rowData["TAB_ID"]+",'"+rowData["TAB_NAME"]+"')>配置列</a></span>";
    		str += "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;' onclick=confChart("+rowData["TAB_ID"]+",'"+rowData["TAB_NAME"]+"')>配置图形</a></span>";
    		return str;
        }else if(columnName == 'RPT_TYPE'){
        	var str = "";
        	if(rowData["RPT_TYPE"] == "1")
        		str = "日报";
        	else if(rowData["RPT_TYPE"] == "2")
        		str = "月报"
        	return str;
        }else if(columnName == "TAB_NAME"){
        	var name = "TAB_ID"+rowIndex;
        	var str = "<input type='hidden' name='"+name+"' id='"+name+"' value='"+rowData["TAB_ID"]+"'>";
        	str += rowData["TAB_NAME"];
        	return str;
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}));
confCol = function(tabId,repName){
	openMenu("配置“"+repName+"”列","/portalCommon/module/portal/initConfig/repColConfig.jsp?tabId="+tabId,"top");
}
confChart = function(tabId,repName){
	openMenu("配置“"+repName+"”图形及指标解释","/portalCommon/module/portal/initConfig/guildlineConfig.jsp?tabId="+tabId,"top");
}
repModify = function(tabId){
	ReportConfigAction.getReportTabById(tabId,function(res){
		openWin("modify",res);
	});
}
repDel = function(tabId){
	dhx.confirm("确定删除？删除后，只能从新配置与灌输数据！",function(r){
		if(r){
			ReportConfigAction.delReportTabById(tabId,function(rs){
				if(rs){
					dhx.alert("删除成功！");
					grid.clearAll();
	            	grid.load(dwrCaller.getReportTabMes, "json");
				}else
					dhx.alert("删除失败，请重试！");
			});
		}
	});
}
/**
 * 初始化界面
 */
var init=function(){
	var dhxLayout = new dhtmlXLayoutObject(document.body, "2E");
	dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.setText("报表名称配置");
    topCell.setHeight(80);
    topCell.fixSize(true,true);
    queryForm = topCell.attachForm([
        {type:"setting"},
        {type:"input",label:"报表名称：",name:"repName",inputWidth:350,inputHeight:17},
        {type:"newcolumn"},
        {type:"button",name:"queryBtn",value:"查询"},
        {type:"hidden",name:"template"}
    ]);
    queryForm.attachEvent("onButtonClick", function(id) {
        if (id == "queryBtn") {
            //进行数据查询。
        	grid.clearAll();
            grid.load(dwrCaller.getReportTabMes, "json");
        }
    });
    // 添加Enter查询事件
    queryForm.getInput("repName").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            grid.clearAll();
            grid.load(dwrCaller.getReportTabMes, "json");
        }
    }
    //初始化按钮
    var toolbar = dhxLayout.cells("b").attachToolbar();
    toolbar.addButton('addRepId', 1, '新增', base + "/meta/resource/images/addMenu.png", base + "/meta/resource/images/addMenu.png");
    toolbar.addButton('changeOrderById', 2, '调整排序', base + "/meta/resource/images/changeLevel.png", base + "/meta/resource/images/changeLevel.png");
    toolbar.addText("levelSelect",3,"<font style=\"font-size:11px;color:858585;\">注：下面表格可拖动进行排序</font>");
    //数据表单
    var dataCell = dhxLayout.cells("b");
    dataCell.fixSize(true,false);
    dataCell.hideHeader();
    grid = dataCell.attachGrid();
    grid.setHeader("报表名称,排序,钻取层级,图形默认取数列横纵坐标,报表类型,描述,操作");
    grid.setInitWidthsP("15,10,10,15,10,20,20");
    grid.setColAlign("left,right,right,left,left,left,center");
    grid.setHeaderAlign("center,center,center,center,center,center,center");
    grid.setColTypes("ro,ro,ro,ro,ro,ro,ro");
    grid.setColSorting("na,na,na,na,na,na,na");
    grid.enableResizing("true,true,true,true,true,true,true");
    grid.enableDragAndDrop(true);//设置拖拽
    grid.setDragBehavior("complex");
    grid.init();
    //数据加载
    grid.load(dwrCaller.getReportTabMes, "json");
    /**
     *添加工具栏button事件处理
     */
    toolbar.attachEvent("onclick", function(id){
    	if(id == "addRepId"){
    		openWin("add",null);
    	}else if(id == "changeOrderById"){
    		modifyOrderId();
    	}
    });
    grid.attachEvent("onDrag",function(sId,tId,sObj,tObj,sInd,tInd){
    	var ids = grid.getAllRowIds().split(',');//数组
//    	alert("sId="+sId+" tId="+tId);
    	if(tId == undefined)
    		tId = -1;
    	for (var i = 0; i < ids.length; i++) {
    		if(tId == sId-1 || tId == sId){
    			isRowIdChange = false;
    		}else{
    			isRowIdChange = true;
    			if(tId > sId){//从上往下
    				if(i >= sId && i <= tId){
    					if(i < tId)
    						rowIds[i] = document.getElementById("TAB_ID" + (i+1)).value;
    					else
    						rowIds[i] = document.getElementById("TAB_ID" + (i-1*tId+1*sId)).value;
    				}else
    					rowIds[i] = document.getElementById("TAB_ID" + i).value;
    			}else{//从下往上
    				if(i >= (tId+1)){
	    				if(i == (tId+1))
	    					rowIds[i] = document.getElementById("TAB_ID" + sId).value;
	    				else
	    					rowIds[i] = document.getElementById("TAB_ID" + (i-1)).value;
	    			}else{
	    				rowIds[i] = document.getElementById("TAB_ID" + i).value;
	    			}
    			}
    		}
//    		alert("rowIds["+i+"]=="+rowIds[i]);
    	}
    	return true;
    });
}

//修改排序
var modifyOrderId = function(){
	if(!isRowIdChange){
		dhx.alert("您没有做排序操作，需要拖动表格才能确认调整顺序！");
	}else{
		var data = getData();
		if(data != null && data.length > 0){
			ReportConfigAction.modifyRepTabOrderId(data,function(rs){
				if(rs){
					dhx.alert("调整排序成功！");
					grid.clearAll();
	            	grid.load(dwrCaller.getReportTabMes, "json");
				}else{
					dhx.alert("调整排序失败！");
				}
			});
		}else{
			dhx.alert("您没有做排序操作，需要拖动表格才能确认调整顺序！");
		}
	}
}
var getData = function () {
	var ids = grid.getAllRowIds().split(',');//数组
	var columnData = [];
	for (var i = 0; i < ids.length; i++) {
		var rowData = {};
		rowData["orderId"] = i+1;
		rowData["tabId"] = rowIds[i];
		columnData.push(rowData);
	}
	return columnData;
}
var openWin = function(type,repdata){
	var dhxWindow = new dhtmlXWindows();
    var winSize=Tools.propWidthDycSize(15,20,15,20);
    winSize.width=550;
    dhxWindow.createWindow("addWindow", 0, 0, 750, 900);
    var addWindow = dhxWindow.window("addWindow");
    addWindow.setModal(true);
    addWindow.setDimension(winSize.width, 300);
    addWindow.center();
    addWindow.button("minmax1").hide();
    addWindow.button("minmax2").hide();
    addWindow.button("park").hide();
    addWindow.denyResize();
    addWindow.denyPark();
    var options = [{text:"日报",value:1},{text:"月报",value:2}];
    var buttonstr = "";
	if("add" == type){
		addWindow.setText("新增报表名称");
		buttonstr = '';
	}else{
		addWindow.setText("修改报表名称");
	}
	addWindow.keepInViewport(true);
    addWindow.show();
    var addMenuLayout = new dhtmlXLayoutObject(addWindow, "1C");
    addMenuLayout.cells("a").hideHeader();
    addMenuLayout.cells("a").fixSize(true, true);
    var maxDataNum = (1*dataNum+1);
    //赋值
    var tabName = "";
    var orderId = 0;
    var rolldownLayer = "";
    var rptType = 1;
    var defaultGrid = "";
    var tabDesc = "";
    var tabId = "";
    if(repdata != null){
    	if(repdata["TAB_NAME"] != null && repdata["TAB_NAME"] != "null")
    		tabName = repdata["TAB_NAME"];
    	if(repdata["ORDER_ID"] != null && repdata["ORDER_ID"] != "null")
    		orderId = 1*repdata["ORDER_ID"];
    	if(repdata["ROLLDOWN_LAYER"] != null && repdata["ROLLDOWN_LAYER"] != "null")
    		rolldownLayer = repdata["ROLLDOWN_LAYER"];
    	if(repdata["RPT_TYPE"] != null && repdata["RPT_TYPE"] != "null"){
    		rptType = repdata["RPT_TYPE"];
    	}
    	if(repdata["DEFAULT_GRID"] != null && repdata["DEFAULT_GRID"] != "null"){
    		defaultGrid = repdata["DEFAULT_GRID"];
    	}
    	if(repdata["TAB_DESC"] != null && repdata["TAB_DESC"] != "null"){
    		tabDesc = repdata["TAB_DESC"];
    	}
    	tabId = repdata["TAB_ID"];
    }
    if(1*orderId > 0)
    	maxDataNum = orderId;
    if(defaultGrid == "")
    	defaultGrid = "0,1";
    var menuFormData = [
        {type: "settings", position: "label-left"},
        {type:"block",className:"blockStyle",list:[
            {type:"input",label:"<span style=\"color:#000;font-size:12px;padding-left:90px;\">报表名称：</span>",name:"tabName",value:""+tabName+"",inputWidth:150,validate:"NotEmpty,MaxLength[200]",className:"inputStyle"},
            {type:"newcolumn"},
            {type:"label",label:'<span style="color:#A1A1A1">请输入报表名称</span>'},
            {type:"newcolumn"},
            {type:"newcolumn"}
        ]},
        {type:"hidden",rows:3,label:"orderId",name:"orderId",value:""+maxDataNum+""},
        {type:"block",className:"blockStyle",list:[
            {type:"input",label:"<span style=\"color:#000;font-size:12px;padding-left:90px;\">钻取层级：</span>",name:"rolldownLayer",value:""+rolldownLayer+"",inputWidth:150,validate:"NotEmpty,ValidInteger",className:"inputStyle"},
            {type:"newcolumn"},
            {type:"label",label:'<span style="color:#A1A1A1">整数:通过地域钻取到第几层</span>'},
            {type:"newcolumn"},
            {type:"newcolumn"}
        ]},
        {type:"block",className:"blockStyle",list:[
            {type:"combo",label:"<span style=\"color:#000;font-size:12px;padding-left:90px;\">报表类型：</span>",name:"rptType",inputWidth:150,className:"inputStyle"},
            {type:"newcolumn"},
            {type:"newcolumn"},
            {type:"newcolumn"}
        ]},
        {type:"block",className:"blockStyle",list:[
            {type:"input",label:"<span style=\"color:#000;font-size:12px;padding-left:5px;\">图形默认取数列横纵坐标：</span>",name:"defaultGrid",inputWidth:150,validate:"NotEmpty,ValidByCallBack[validateFormat]",className:"inputStyle",value:""+defaultGrid+""},
            {type:"newcolumn"},
            {type:"label",label:'<span style="color:#A1A1A1">下标从(0,0)开始</span>'},
            {type:"newcolumn"}
        ]},
        {type:"block",className:"blockStyle",list:[
			{type:"input",rows:3,label:'<span style="color:#000;font-size:12px;margin-left:113px;width:80px;">描述：</span>',name:"tabDesc",value:""+tabDesc+"",inputWidth:250,validate:"MaxLength[100]",className:"inputStyle"},
            {type:"newcolumn"}
        ]},
        {type:"block",offsetTop:30,inputTop :20,list:[
            {type: "settings", position: "label-left"},
            {type:"button",label:"保存",name:"save",value:"保存",offsetLeft:190},
            {type:"newcolumn"},
            {type:"button",label:"关闭",name:"close",value:"关闭"}
        ]},
        {type:"hidden",rows:3,label:"type",name:"type",value:""+type+""},
        {type:"hidden",rows:3,label:"tabId",name:"tabId",value:""+tabId+""}
    ]
    var addForm = addMenuLayout.cells("a").attachForm(menuFormData);
    addForm.defaultValidateEvent();
    addForm.getCombo("rptType").addOption(options);
    if(1*rptType == 1)
    	addForm.getCombo("rptType").selectOption(0,false,false);
    else
    	addForm.getCombo("rptType").selectOption(1,false,false);
    addForm.attachEvent("onButtonClick", function(id){
    	if(id == "close"){
            addWindow.hide();
            addForm.unload();
            dhxWindow.unload();
        }else if(id == "save"){//保存
            if(addForm.validate()){
            	if(type == "add"){
            		ReportConfigAction.saveReportTab(addForm.getFormData(),function(rs){
	            		if (rs) {
	            			dhx.alert("保存成功！");
			                addWindow.hide();
				            addForm.unload();
				            dhxWindow.unload();
				            grid.clearAll();
	            			grid.load(dwrCaller.getReportTabMes, "json");
			            } else {
			            	dhx.alert("保存失败，请重试！");
			            }
	            	});
            	}else{
            		ReportConfigAction.updateReportTabById(addForm.getFormData(),function(rs){
	            		if (rs) {
	            			dhx.alert("修改成功！");
			                addWindow.hide();
				            addForm.unload();
				            dhxWindow.unload();
				            grid.clearAll();
	            			grid.load(dwrCaller.getReportTabMes, "json");
			            } else {
			            	dhx.alert("修改失败，请重试！");
			            }
	            	});
            	}
            }
        }
    });
    validateFormat = function(){
    	var value = addForm.getInput("defaultGrid").value;
    	var regu = "([0-9][\,][0-9]+$){1}";
  		var re = new RegExp(regu);
  		if(!re.test(value)){
  			return "请输入“数字,数字”格式数据！";
  		}else
  			return true;
    }
}

dhx.ready(init);