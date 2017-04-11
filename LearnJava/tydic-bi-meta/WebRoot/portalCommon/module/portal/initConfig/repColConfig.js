/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       batchImport.js
 *Description：
 *       通用门户--报表列配置
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       吴喜丽
 *Date：
 *       2012-05-17
 ********************************************************/
/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var queryForm;
var grid;
var base = getBasePath();
var maxRowIndex;
var rowIds = [];//保存主键的值（tabId）
var isRowIdChange = false;
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();
repColMesConverter = new dhtmxGridDataConverter({
	isFormatColumn:false,
    filterColumns:["COL_CHN_NAME","SHOW_ORDER_ID","COL_EN_NAME","COLUMN_COMPANY","COLUMN_COMPANY_POS","LINK"],
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
    	var name = columnName + rowIndex;
    	if(columnName == 'LINK'){
    		var str = "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;' onclick=colModify("+rowData["COL_ID"]+")>修改</a></span>";
    		str += "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;' onclick=colDel("+rowData["COL_ID"]+")>删除</a></span>";
    		return str;
        }else if(columnName == "COL_CHN_NAME"){
        	var name = "COL_ID"+rowIndex;
        	var str = "<input type='hidden' name='"+name+"' id='"+name+"' value='"+rowData["COL_ID"]+"'>";
        	str += rowData["COL_CHN_NAME"];
        	return str;
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    },
    onBeforeConverted:function (data) {
    	if (data && data.length > 0) {
    		maxRowIndex = data.length+1;
    	}else
    		maxRowIndex = 1;
    }
});
dwrCaller.addAutoAction("getRepColMesByTabId", "ReportConfigAction.getRepColMesByTabId",{
    dwrConfig:true,
    converter:repColMesConverter,
    async:false,
    isShowProcess:false
});
colModify = function(colId){
	ReportConfigAction.getColMesByColId(colId,function(res){
		openWin("modify",res);
	});
}
colDel = function(colId){
	dhx.confirm("确定删除？删除后，只能从新配置！",function(r){
		if(r){
			ReportConfigAction.delColMesByColId(colId,function(rs){
				if(rs){
					dhx.alert("删除成功！");
					grid.clearAll();
	            	grid.load(dwrCaller.getRepColMesByTabId+"?tabId="+tabId, "json");
				}else
					dhx.alert("删除失败，请重试！");
			});
		}
	});
}
var init = function(){
	var dhxLayout = new dhtmlXLayoutObject(document.body, "3E");
	dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.setText("报表列配置");
    topCell.setHeight(80);
    topCell.fixSize(true,true);
    queryForm = topCell.attachForm([
        {type:"setting"},
        {type:"combo",label:"报表名称：",name:"repName",inputWidth:270,inputHeight:17,readonly:true},
        {type:"newcolumn"},
        //{type:"button",name:"queryBtn",value:"查询"},
        {type:"hidden",name:"template"}
    ]);
    queryForm.getCombo("repName").enableFilteringMode(false);
    //加载报表名称
    var options = [];
    var selectIndex = 0;
    var tabName = "";
    ReportConfigAction.getReportTabMes(null,{
    	async:false,
    	callback:function(list) {
	    	for(var i = 0; i < list.length; i++){
	    		if(tabId == null || tabId == "null")
	    			tabId = list[selectIndex].TAB_ID;
	    		if(1*tabId == list[i].TAB_ID)
	    			selectIndex = i;
	    		tabName = list[selectIndex].TAB_NAME;
	    		options.push({text:list[i].TAB_NAME,value:list[i].TAB_ID});
	    	}
    	}
    });
    queryForm.getCombo("repName").addOption(options);
    queryForm.getCombo("repName").selectOption(selectIndex,false,false);
    var midCell = dhxLayout.cells("b");
    midCell.setHeight(130);
    midCell.fixSize(true,true);
    midCell.setText(tabName);
    var dhxAttachObject = midCell.attachObject("_repTabMes");
    loadDate(tabId);
 /*
    queryForm.attachEvent("onButtonClick", function(id) {
        if (id == "queryBtn") {
        	var tableId = queryForm.getCombo("repName").getSelectedValue();
        	tabId = tableId;
        	tabName = queryForm.getCombo("repName").getComboText();
        	midCell.setText(tabName);
        	loadDate(tabId);
            //进行数据查询。
        	grid.clearAll();
            grid.load(dwrCaller.getRepColMesByTabId+"?tabId="+tabId, "json");
        }
    });
 */
    queryForm.attachEvent("onChange", function (id, value){
    	tabId = value;
        tabName = queryForm.getCombo("repName").getComboText();
        midCell.setText(tabName);
        loadDate(tabId);
        //进行数据查询。
        grid.clearAll();
        grid.load(dwrCaller.getRepColMesByTabId+"?tabId="+tabId, "json");
	});
    
    var footCell = dhxLayout.cells("c");
    //初始化按钮
    var toolbar = footCell.attachToolbar();
    toolbar.addText("levelSelect",1,"报表列");
    toolbar.addSeparator("tableName",2);
    toolbar.addButton('addRepCol', 3, '新增', base + "/meta/resource/images/addMenu.png", base + "/meta/resource/images/addMenu.png");
    toolbar.addButton('changeOrderById', 4, '调整排序', base + "/meta/resource/images/changeLevel.png", base + "/meta/resource/images/changeLevel.png");
    toolbar.addText("levelSelect5",5,"<font style=\"font-size:11px;color:858585;\">注：下面表格可拖动进行排序</font>");
    toolbar.addSpacer("tableName");
    footCell.fixSize(true,false);
    footCell.hideHeader();
    grid = footCell.attachGrid();
    grid.setHeader("列中文名,排序,数据来源列名,指标单位,列单位位置,操作");
    grid.setInitWidthsP("20,10,20,10,10,30");
    grid.setColAlign("left,right,left,left,right,center");
    grid.setHeaderAlign("center,center,center,center,center,center");
    grid.setColTypes("ro,ro,ro,ro,ro,ro");
    grid.setColSorting("na,na,na,na,na,na");
    grid.enableResizing("true,true,true,true,true,true");
    grid.enableDragAndDrop(true);//设置拖拽
    grid.setDragBehavior("complex");
    grid.init();
    grid.load(dwrCaller.getRepColMesByTabId+"?tabId="+tabId, "json");
    /**
     *添加工具栏button事件处理
     */
    toolbar.attachEvent("onclick", function(id){
    	if(id == "addRepCol"){
    		openWin("add",null);
    	}else if(id == "changeOrderById"){
    		modifyOrderId();
    	}
    });
    grid.attachEvent("onDrag",function(sId,tId,sObj,tObj,sInd,tInd){
    	var ids = grid.getAllRowIds().split(',');//数组
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
    						rowIds[i] = document.getElementById("COL_ID" + (i+1)).value;
    					else
    						rowIds[i] = document.getElementById("COL_ID" + (i-1*tId+1*sId)).value;
    				}else
    					rowIds[i] = document.getElementById("COL_ID" + i).value;
    			}else{//从下往上
    				if(i >= (tId+1)){
	    				if(i == (tId+1))
	    					rowIds[i] = document.getElementById("COL_ID" + sId).value;
	    				else
	    					rowIds[i] = document.getElementById("COL_ID" + (i-1)).value;
	    			}else{
	    				rowIds[i] = document.getElementById("COL_ID" + i).value;
	    			}
    			}
    		}
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
			ReportConfigAction.modifyColOrderId(data,function(rs){
				if(rs){
					dhx.alert("调整排序成功！");
					grid.clearAll();
	            	grid.load(dwrCaller.getRepColMesByTabId+"?tabId="+tabId, "json");
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
		rowData["colId"] = rowIds[i];
		columnData.push(rowData);
	}
	return columnData;
}
var openWin = function(type,repdata){
	var dhxWindow = new dhtmlXWindows();
    var winSize=Tools.propWidthDycSize(15,20,15,20);
    winSize.width=550;
    dhxWindow.createWindow("addWindow", 0, 0, 750, 480);
    var addWindow = dhxWindow.window("addWindow");
    addWindow.setModal(true);
    addWindow.setDimension(winSize.width, 200);
    addWindow.center();
    addWindow.button("minmax1").hide();
    addWindow.button("minmax2").hide();
    addWindow.button("park").hide();
    addWindow.denyResize();
    addWindow.denyPark();
    var options = [{text:"日报",value:1},{text:"月报",value:2}];
    var buttonstr = "";
	if("add" == type){
		addWindow.setText("新增报表列");
		buttonstr = '';
	}else{
		addWindow.setText("修改报表列");
	}
	addWindow.keepInViewport(true);
    addWindow.show();
    var addMenuLayout = new dhtmlXLayoutObject(addWindow, "1C");
    addMenuLayout.cells("a").hideHeader();
    addMenuLayout.cells("a").fixSize(true, true);
    var maxDataNum = maxRowIndex;
    //赋值
    var colChnName = "";
    var showOrderId = 0;
    var colEnName = "";
    var colId = "";
    var columnCompany ="";
    var columnCompanyPos =0;
    if(repdata != null){
    	if(repdata["COL_CHN_NAME"] != null && repdata["COL_CHN_NAME"] != "null")
    		colChnName = repdata["COL_CHN_NAME"];
    	if(repdata["SHOW_ORDER_ID"] != null && repdata["SHOW_ORDER_ID"] != "null")
    		showOrderId = 1*repdata["SHOW_ORDER_ID"];
    	if(repdata["COL_EN_NAME"] != null && repdata["COL_EN_NAME"] != "null")
    		colEnName = repdata["COL_EN_NAME"];
    	if(repdata["COLUMN_COMPANY"] != null && repdata["COLUMN_COMPANY"] != "null")
    		columnCompany = repdata["COLUMN_COMPANY"];
    	if(repdata["COLUMN_COMPANY_POS"] != null && repdata["COLUMN_COMPANY_POS"] != "null")
    		columnCompanyPos =  1*repdata["COLUMN_COMPANY_POS"];
    	colId = repdata["COL_ID"];
    	tabId = repdata["TAB_ID"];
    }
    if(1*showOrderId > 0)
    	maxDataNum = showOrderId;
    var menuFormData;
    if(maxDataNum == 1){
    	menuFormData = [
	        {type: "settings", position: "label-left"},
	        {type:"block",className:"blockStyle",list:[
	            {type:"input",label:"<span style=\"color:#000;font-size:12px;padding-left:90px;\">列中文名：</span>",name:"colChnName",value:""+colChnName+"",inputWidth:150,validate:"NotEmpty,MaxLength[100]",className:"inputStyle"},
	            {type:"newcolumn"},
	            {type:"label",label:'<span style="color:#A1A1A1">请输入列中文名</span>'},
	            {type:"newcolumn"},
	            {type:"newcolumn"}
	        ]},
	        {type:"block",className:"blockStyle",list:[
	            {type:"input",label:"<span style=\"color:#000;font-size:12px;padding-left:90px;\">指标单位：</span>",name:"columnCompany",value:""+columnCompany+"",inputWidth:150,validate:"MaxLength[50]",className:"inputStyle"},
	            {type:"newcolumn"},
	            {type:"label",label:'<span style="color:#A1A1A1">请输入指标单位</span>'},
	            {type:"newcolumn"},
	            {type:"newcolumn"}
	        ]},
	        {type:"block",className:"blockStyle",list:[
	            {type:"input",label:"<span style=\"color:#000;font-size:12px;padding-left:90px;\">单位位置：</span>",name:"columnCompanyPos",value:""+columnCompanyPos+"",inputWidth:150,validate:"MaxLength[1],ValidInteger",className:"inputStyle"},
	            {type:"newcolumn"},
	            {type:"label",label:'<span style="color:#A1A1A1">0:位于表头;1:位于数据后</span>'},
	            {type:"newcolumn"},
	            {type:"newcolumn"}
	        ]},
	        {type:"hidden",label:"showOrderId",name:"showOrderId",value:""+maxDataNum+""},
	        {type:"hidden",label:"colEnName",name:"colEnName",value:""},
	        {type:"block",offsetTop:20,inputTop :20,list:[
	            {type: "settings", position: "label-left"},
	            {type:"button",label:"保存",name:"save",value:"保存",offsetLeft:190},
	            {type:"newcolumn"},
	            {type:"button",label:"关闭",name:"close",value:"关闭"}
	        ]},
	        {type:"hidden",label:"type",name:"type",value:""+type+""},
	        {type:"hidden",label:"tabId",name:"tabId",value:""+tabId+""},
	        {type:"hidden",label:"colId",name:"colId",value:""+colId+""}
	    ]
    }else{
    	menuFormData = [
	        {type: "settings", position: "label-left"},
	        {type:"block",className:"blockStyle",list:[
	            {type:"input",label:"<span style=\"color:#000;font-size:12px;padding-left:90px;\">列中文名：</span>",name:"colChnName",value:""+colChnName+"",inputWidth:150,validate:"NotEmpty,MaxLength[100]",className:"inputStyle"},
	            {type:"newcolumn"},
	            {type:"label",label:'<span style="color:#A1A1A1">请输入列中文名</span>'},
	            {type:"newcolumn"},
	            {type:"newcolumn"}//,
	        ]},
	        {type:"hidden",label:"showOrderId",name:"showOrderId",value:""+maxDataNum+""},
	        {type:"block",className:"blockStyle",list:[
	            {type:"input",label:"<span style=\"color:#000;font-size:12px;padding-left:66px;\">数据来源列名：</span>",name:"colEnName",value:""+colEnName+"",inputWidth:150,validate:"NotEmpty,MaxLength[50]",className:"inputStyle"},
	            {type:"newcolumn"},
	            {type:"label",label:'<span style="color:#A1A1A1">请输入数据来源列名</span>'},
	            {type:"newcolumn"},
	            {type:"newcolumn"}//,
	        ]},
	        {type:"block",className:"blockStyle",list:[
	            {type:"input",label:"<span style=\"color:#000;font-size:12px;padding-left:90px;\">指标单位：</span>",name:"columnCompany",value:""+columnCompany+"",inputWidth:150,validate:"NotEmpty,MaxLength[100]",className:"inputStyle"},
	            {type:"newcolumn"},
	            {type:"label",label:'<span style="color:#A1A1A1">请输入指标单位</span>'},
	            {type:"newcolumn"},
	            {type:"newcolumn"}
	        ]},
	        {type:"block",className:"blockStyle",list:[
	            {type:"input",label:"<span style=\"color:#000;font-size:12px;padding-left:90px;\">列单位位置：</span>",name:"columnCompanyPos",value:""+columnCompanyPos+"",inputWidth:150,validate:"NotEmpty,MaxLength[100]",className:"inputStyle"},
	            {type:"newcolumn"},
	            {type:"label",label:'<span style="color:#A1A1A1">0：位于表头;1:位于数据后</span>'},
	            {type:"newcolumn"},
	            {type:"newcolumn"}
	        ]},
	        {type:"block",offsetTop:20,inputTop :20,list:[
	            {type: "settings", position: "label-left"},
	            {type:"button",label:"保存",name:"save",value:"保存",offsetLeft:190},
	            {type:"newcolumn"},
	            {type:"button",label:"关闭",name:"close",value:"关闭"}
	        ]},
	        {type:"hidden",label:"type",name:"type",value:""+type+""},
	        {type:"hidden",label:"tabId",name:"tabId",value:""+tabId+""},
	        {type:"hidden",label:"colId",name:"colId",value:""+colId+""}
	    ]
    }
    	
    var addForm = addMenuLayout.cells("a").attachForm(menuFormData);
    addForm.defaultValidateEvent();
    addForm.attachEvent("onButtonClick", function(id){
    	if(id == "close"){
            addWindow.hide();
            addForm.unload();
            dhxWindow.unload();
        }else if(id == "save"){//保存
            if(addForm.validate()){
            	if(type == "add"){
            		ReportConfigAction.saveColMes(addForm.getFormData(),function(rs){
	            		if (rs) {
	            			dhx.alert("保存成功！");
			                addWindow.hide();
				            addForm.unload();
				            dhxWindow.unload();
				            grid.clearAll();
	            			grid.load(dwrCaller.getRepColMesByTabId+"?tabId="+tabId, "json");
			            } else {
			            	dhx.alert("保存失败，请重试！");
			            }
	            	});
            	}else{
            		ReportConfigAction.updateColMes(addForm.getFormData(),function(rs){
	            		if (rs) {
	            			dhx.alert("修改成功！");
			                addWindow.hide();
				            addForm.unload();
				            dhxWindow.unload();
				            grid.clearAll();
	            			grid.load(dwrCaller.getRepColMesByTabId+"?tabId="+tabId, "json");
			            } else {
			            	dhx.alert("修改失败，请重试！");
			            }
	            	});
            	}
            }
        }
    });
}
var loadDate = function(tabId){
	ReportConfigAction.getReportTabById(tabId,{callback:function(res){
		var repType = "";
		if(res.RPT_TYPE == "1")
			repType = "日报";
		else if(res.RPT_TYPE == "2")
			repType = "月报";
		$('_repType').innerText = repType;
		$('_orderId').innerText = res.ORDER_ID;
		$('_rolldownLayer').innerText = res.ROLLDOWN_LAYER;
		$('_defaultGrid').innerText = res.DEFAULT_GRID;
		var tabDesc = "";
		if(res.TAB_DESC != null && res.TAB_DESC != "null")
			tabDesc = res.TAB_DESC;
		$('_tabDesc').innerText = tabDesc;
	}});
}
dhx.ready(init);