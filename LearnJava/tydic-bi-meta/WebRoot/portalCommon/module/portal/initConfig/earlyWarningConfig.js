/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       batchImport.js
 *Description：
 *       预警配置
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       刘弟伟
 *Date：
 *       2012-05-30
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

 /******************************配置查询预警信息  start**************************************************/
param.setParamConfig([
    {
       index:0,type:"fun",value:function() {
			var formData = queryForm.getFormData();
			return formData;
   	   	}
    }
]);

dwrCaller.addAutoAction("getPortalWarning","ReportConfigAction.getPortalWarning",param,function(data){dataNum = data.rows.length;});
dwrCaller.addDataConverter("getPortalWarning",new dhtmxGridDataConverter({
	isFormatColumn:false,
    filterColumns:["TAB_NAME","INDEX_NAME","COLUMN_NAME","WARING_TYPE","WARING_VALUE","WARING_VALUE2","LINK"],
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
    		var str = "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;' onclick=repModify("+rowData["TAB_ID"]+",'"+rowData["INDEX_CD"]+"','"+rowData["COLUMN_ID"]+"')>修改</a></span>";
    		str += "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'       onclick=repDel("+rowData["TAB_ID"]+",'"+rowData["INDEX_CD"]+"','"+rowData["COLUMN_ID"]+"')>删除</a></span>";
    		return str;
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}));
 /******************************配置查询预警信息  end**************************************************/


repModify = function(tabId,indexCd,columnId){
	ReportConfigAction.getWarningId(tabId,indexCd,columnId,function(res){
		var repName = queryForm.getCombo("repName").getSelectedValue();
        res["tabId"] = repName;
		openWin("modify",res);
	});
	//alert("正在开发中.....,请先删除,再新增！");
}
repDel = function(tabId,indexCd,columnId){
	dhx.confirm("确定删除？删除后，只能从新配置！",function(r){
		if(r){
			ReportConfigAction.deleteWarningId(tabId,indexCd,columnId,function(rs){
				if(rs){
					dhx.alert("删除成功！");
					grid.clearAll();
	            	grid.load(dwrCaller.getPortalWarning, "json");
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
    topCell.setText("预警配置");
    topCell.setHeight(80);
    topCell.fixSize(true,true);
    queryForm = topCell.attachForm([
        {type:"setting"},
        {type:"combo",label:"报表名称：",name:"repName",inputWidth:270,inputHeight:17,readonly:true},
        {type:"newcolumn"},
        {type:"hidden",name:"template"}
    ]);
     queryForm.getCombo("repName").enableFilteringMode(false);
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
    queryForm.attachEvent("onChange", function(id) {
            //进行数据查询。
        	grid.clearAll();
            grid.load(dwrCaller.getPortalWarning, "json");
    });
    
    //初始化按钮
    var toolbar = dhxLayout.cells("b").attachToolbar();
    toolbar.addButton('addRepId', 1, '新增', base + "/meta/resource/images/addMenu.png", base + "/meta/resource/images/addMenu.png");
    //数据表单
    var dataCell = dhxLayout.cells("b");
    dataCell.fixSize(true,false);
    dataCell.hideHeader();
    grid = dataCell.attachGrid();
    grid.setHeader("报表名称,指标名称,列名,预警类型,预警值1,预警值2,操作");
    grid.setInitWidthsP("20,25,15,13,6,6,15");
    grid.setColAlign("left,left,left,left,right,right,center");
    grid.setHeaderAlign("center,center,center,center,center,center,center");
    grid.setColTypes("ro,ro,ro,ro,ro,ro,ro");
    grid.setColSorting("na,na,na,na,na,na,na");
    grid.enableResizing("true,true,true,true,true,true,true");
    //grid.enableDragAndDrop(true);//设置拖拽
    //grid.setDragBehavior("complex");
    grid.init();
    //数据加载
    grid.load(dwrCaller.getPortalWarning, "json");
    /**
     *添加工具栏button事件处理
     */
    toolbar.attachEvent("onclick", function(id){
    	if(id == "addRepId"){
    		var repName = queryForm.getCombo("repName").getSelectedValue();
    		var repdata =[];
    		repdata["tabId"] = repName;
    		openWin("add",repdata);
    	}
    });
}

var openWin = function(type,repdata){
	var dhxWindow = new dhtmlXWindows();
    var winSize=Tools.propWidthDycSize(15,20,15,20);
    dhxWindow.createWindow("addWindow", 0, 0, 750, 900);
    var addWindow = dhxWindow.window("addWindow");
    addWindow.setModal(true);
    addWindow.setDimension(450, 260);	
    addWindow.center();
    addWindow.denyResize();	
    addWindow.denyPark();
    var options = [{text:"=",value:"="},{text:">=",value:">="},{text:"<",value:"<"},{text:"<=",value:"<="},{text:"!=",value:"!="},{text:"BTWEEN AND",value:"BTWEEN AND"}];
    var buttonstr = "";
	if("add" == type){
		addWindow.setText("新增预警配置");
		buttonstr = '';
	}else{
		addWindow.setText("修改预警配置");
	}
	addWindow.keepInViewport(true);
    addWindow.show();
    var addMenuLayout = new dhtmlXLayoutObject(addWindow, "1C");
    addMenuLayout.cells("a").hideHeader();
    addMenuLayout.cells("a").fixSize(true, true);
    var maxDataNum = (1*dataNum+1);
    
    //赋值
    var indexCd =  "";
    var columnId = "";
    var waringType = "";
    var waringValue =0;
    var waringValue2=0;
    var tabId = 1;
    if(repdata != null){
    	if(repdata["INDEX_CD"]  != null && repdata["INDEX_CD"]  != "null")
    		indexCd =  repdata["INDEX_CD"];
    	if(repdata["COLUMN_ID"] != null && repdata["COLUMN_ID"] != "null")
    		columnId = repdata["COLUMN_ID"];
    	if(repdata["WARING_TYPE"] != null && repdata["WARING_TYPE"] != "null")
    		waringType = repdata["WARING_TYPE"];
    	if(repdata["WARING_VALUE"] != null && repdata["WARING_VALUE"] != "null"){
    		waringValue = 1*repdata["WARING_VALUE"];
    	}
       if(repdata["WARING_VALUE2"] != null && repdata["WARING_VALUE2"] != "null"){
    		waringValue2 = 1*repdata["WARING_VALUE2"];
    	}
    	tabId = repdata["tabId"];
    }
   
    var menuFormData = [
        {type: "settings", position: "label-left"},
        {type:"block",className:"blockStyle",offsetTop :20,list:[
        	{type:"combo",label:"<span style=\"color:#000;font-size:12px;padding:20px;\">指标名称：</span>",name:"indexCd", inputWidth:250,className:"inputStyle",readonly:true}, 
            {type:"newcolumn"}
        ]},
        {type:"block",className:"blockStyle",list:[
            {type:"combo",label:"<span style=\"color:#000;font-size:12px;padding:20px;\">列&nbsp;&nbsp;名&nbsp;称：</span>",name:"columnId", inputWidth:250,className:"inputStyle",readonly:true}, 
            {type:"newcolumn"}
        ]},
        {type:"block",className:"blockStyle",list:[
            {type:"combo",label:"<span style=\"color:#000;font-size:12px;padding:20px;\">预警类型：</span>",name:"waringType",inputWidth:250,className:"inputStyle",readonly:true,
        	options:[
			{value: "=", text: "="},
			{value: ">=", text: ">="},
			{value: "<", text: "<"},
			{value: "<=", text: "<="},
			{value: "!=", text: "!="},
			{value: "BTWEEN AND", text: "BTWEEN AND"}]},
            {type:"newcolumn"},
            {type:"newcolumn"},
            {type:"newcolumn"}
        ]},
        {type:"block",className:"blockStyle",list:[
            {type:"input",label:"<span style=\"color:#000;font-size:12px;padding:20px;\">预&nbsp;&nbsp;警&nbsp;值：</span>",name:"waringValue",inputWidth:250,validate:"NotEmpty",className:"inputStyle",value:""+waringValue+""},
            {type:"newcolumn"}
        ]},
        {type:"block",className:"blockStyle",list:[
            {type:"input",label:"<span style=\"color:#000;font-size:12px;padding:20px;\">预警值&nbsp;2：</span>",name:"waringValue2",inputWidth:250,className:"inputStyle",value:""+waringValue2+""},
            {type:"newcolumn"}
        ]},
        {type:"block",offsetTop:30,inputTop :20,list:[
            {type: "settings", position: "label-left"},
            {type:"button",label:"保存",name:"save",value:"保存",offsetLeft:140},
            {type:"newcolumn"},
            {type:"button",label:"关闭",name:"close",value:"关闭"}
        ]},
        {type:"hidden",rows:3,label:"type",name:"type",value:""+type+""},
        {type:"hidden",rows:3,label:"tabId",name:"tabId",value:""+tabId+""}
    ]
    
    
    var addForm = addMenuLayout.cells("a").attachForm(menuFormData);
    addForm.defaultValidateEvent();
    addForm.hideItem("waringValue2");
    switch(waringType)
    {
      case '=': addForm.getCombo("waringType").selectOption(0,false,false);break;
      case '>=': addForm.getCombo("waringType").selectOption(1,false,false);break;
      case '<': addForm.getCombo("waringType").selectOption(2,false,false);break;
      case '<=': addForm.getCombo("waringType").selectOption(3,false,false);break;
      case '!=': addForm.getCombo("waringType").selectOption(4,false,false);break;
      case 'BTWEEN AND': 
    	    addForm.getCombo("waringType").selectOption(5,false,false);
    	    addForm.showItem("waringValue2");
            break;
      Default:break;
      
    }
   
    if("modify" == type){
		addForm.disableItem("indexCd");
		addForm.disableItem("columnId");
	}
    addForm.attachEvent("onChange", function (id, value){
    	if(id !='waringType'){
    		return;
    	}
    	if(id=='waringType' && value == 'BTWEEN AND') {
			addForm.showItem("waringValue2");
    	}else {
    		addForm.setItemValue("waringValue2","");
			addForm.hideItem("waringValue2");
			
			
    	}
	});
    
     //加载该报表下的所有指表
    var index_options = [];
     var selectindex = 0;
     ReportConfigAction.getIndexInfo(tabId,{
    	async:false,
    	callback:function(list) {
	    	for(var i = 0; i < list.length; i++){
	    		if(indexCd == null || indexCd == "")
	    			selectindex = 0;
	    		if(indexCd == list[i].INDEX_CD)
	    			selectindex = i;
	    		index_options.push({text:list[i].INDEX_NAME,value:list[i].INDEX_CD});
	    	}
    	}
    });
    addForm.getCombo("indexCd").addOption(index_options);
    addForm.getCombo("indexCd").selectOption(selectindex,false,false);
    
     //加载该报表下的列名称
   var col_options = [];
   var selectcol = 0;
   if(tabId == 1)
   { 
	   col_options=[{text:"当日值",value:"VALUE2"},{text:"本月累计",value:"VALUE3"},{text:"上月同期累计",value:"VALUE4"},
		              {text:"环比",value:"VALUE5"},{text:"上年同期累计",value:"VALUE6"},{text:"同比",value:"VALUE7"},{text:"本月累计平均值",value:"VALUE8"}];
   }
   else
   {
	   col_options=[{text:"当月值",value:"VALUE2"},{text:"上月值",value:"VALUE3"},{text:"环比",value:"VALUE4"},
		              {text:"上年同期累计",value:"VALUE6"},{text:"同比",value:"VALUE7"},{text:"本年平均值",value:"VALUE8"}];
   }
        if(columnId == null || columnId == "")
			selectcol = 0;
       
        for(i=0;i < col_options.length ; i++)
        {
           if( columnId == col_options[i].value )
			   selectcol = i;
        }
    addForm.getCombo("columnId").addOption(col_options);
    addForm.getCombo("columnId").selectOption(selectcol,false,false);
    
       //预警值输入验证
    var validData = function()
    {
    	var waring = addForm.getCombo("waringType").getSelectedValue();
    	var waringValue = addForm.getItemValue("waringValue");
    	var waringValue2 = addForm.getItemValue("waringValue2");
    	
    	if(waring == 'BTWEEN AND'){
    		if(isNaN(waringValue) || isNaN(waringValue2)){
				dhx.alert("预警值只能输入数字，请重新输入！")
    			return false;	
			}else if(waringValue >= waringValue2){
				dhx.alert("预警值2必须大于预警值1，请重新输入！")
    			return false;
			}else{
				return true;
			}
    	}else{
    		if(isNaN(waringValue))
    		{
    			dhx.alert("预警值只能输入数字，请重新输入！")
    			return false;
    		}
    	}
    	return true;
    }
    addForm.attachEvent("onButtonClick", function(id){
    	if(id == "close"){
            addWindow.hide();
            addForm.unload();
            dhxWindow.unload();
        }else if(id == "save"){//保存
        	if(!validData()){
        			return;
        		}
            if(addForm.validate()){
            	if(type == "add"){
            		ReportConfigAction.saveWarningInfo(addForm.getFormData(),function(rs){
	            	 {
	            			dhx.alert(rs);
			                addWindow.hide();
				            addForm.unload();
				            dhxWindow.unload();
				            grid.clearAll();
	            			grid.load(dwrCaller.getPortalWarning, "json");
			            }
	            	});
            	}else{
            		if(!validData()){
        				return;
        			}
            		ReportConfigAction.updateWarning(addForm.getFormData(),function(rs){
	            		if (rs) {
	            			dhx.alert("修改成功！");
			                addWindow.hide();
				            addForm.unload();
				            dhxWindow.unload();
				            grid.clearAll();
	            			grid.load(dwrCaller.getPortalWarning, "json");
			            } else {
			            	dhx.alert("修改失败，请重试！");
			            }
	            	});
            	}
            }
        }
    });
}

dhx.ready(init);