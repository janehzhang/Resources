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

var globMenuTree = null;//菜单树






 /******************************配置查询预警信息  start**************************************************/


dwrCaller.addDataConverter("getReportWarnPage",new dhtmxGridDataConverter({
	isFormatColumn:false,
    filterColumns:["MENU_NAME","INDEX_NAME","COLUMN_NAME","WARING_TYPE","WARING_VALUE","WARING_VALUE2","LINK"],
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
    		var str = "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'   onclick=repModify("+rowData["REPORT_ID"]+")>修改</a></span>";
    		str += "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'         onclick=repDel("+rowData["REPORT_ID"]+")>删除</a></span>";
    		return str;
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}));
 /******************************配置查询预警信息  end**************************************************/


repModify = function(reportId){
	ReportConfigAction.getReportWarningId(reportId,function(res){
		res["REPORT_ID"]=reportId;
		openWin("modify",res);
	});
	
}
repDel = function(reportId){
	dhx.confirm("确定删除？删除后，只能从新配置！",function(r){
		if(r){
			ReportConfigAction.deleteReportWarning(reportId,function(rs){
				if(rs){
					dhx.alert("删除成功！");
					grid.clearAll();
	            	grid.load(dwrCaller.getReportWarnPage, "json");
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
    topCell.setText("查询条件");
    topCell.setHeight(90);
    topCell.fixSize(true,true);
    queryForm = topCell.attachForm([
        {type:"setting"},
        {type:"input",label:"报表名称：",name:"repName",inputWidth:270,inputHeight:17},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"}
    ]);
    
    queryForm.attachEvent("onButtonClick", function(id) {
    	 if (id == "query") {
            //进行数据查询。
        	grid.clearAll();
            grid.load(dwrCaller.getReportWarnPage, "json");
            }
    });
    
    //初始化按钮
    var toolbar = dhxLayout.cells("b").attachToolbar();
    toolbar.addButton('addRepId', 1, '新增', base + "/meta/resource/images/addMenu.png", base + "/meta/resource/images/addMenu.png");
    //数据表单
   queryForm.setFormData({
		 "repId":10001
	 });
    param.setParamConfig([
	    {
	       index:0,type:"fun",value:function() {
				var formData = queryForm.getFormData();
				return formData;
	   	   	}
	    }
	]);

	dwrCaller.addAutoAction("getReportWarnPage","ReportConfigAction.getReportWarnPage",param,function(data){
		dataNum = data.rows.length;
	});
    var dataCell = dhxLayout.cells("b");
    dataCell.fixSize(true,false);
    dataCell.hideHeader();
    grid = dataCell.attachGrid();
    grid.setHeader("报表名称,指标名称,字段列名,预警类型,预警值1,预警值2,操作");
    grid.setInitWidthsP("30,13,20,13,7,7,10");
    grid.setColAlign("left,left,left,left,right,right,center");
    grid.setHeaderAlign("center,center,center,center,center,center,center");
    grid.setColTypes("ro,ro,ro,ro,ro,ro,ro");
    grid.setColSorting("na,na,na,na,na,na,na");
    grid.enableResizing("true,true,true,true,true,true,true");
    grid.init();
    //数据加载
    grid.load(dwrCaller.getReportWarnPage, "json");
    /**
     *添加工具栏button事件处理
     */
    toolbar.attachEvent("onclick", function(id){
    	if(id == "addRepId"){
    		var repdata =[];
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
	if("add" == type){
		addWindow.setText("新增预警配置");
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
    var reportId="";
    var reportName="==请选择==";
    var menuInfoId="";//最顶层菜单ID
    var indexName =  "";
    var columnName = "";
    var waringType = "";
    var waringValue =0;
    var waringValue2=0;
    if(repdata != null){
        if(repdata["MENU_NAME"]  != null && repdata["MENU_NAME"]  != "null")
    		reportName =  repdata["MENU_NAME"];
        if(repdata["MENU_ID"]  != null && repdata["MENU_ID"]  != "null")
    		menuInfoId =  repdata["MENU_ID"];
    	if(repdata["INDEX_NAME"]  != null && repdata["INDEX_NAME"]  != "null")
    		indexName =  repdata["INDEX_NAME"];
    	if(repdata["COLUMN_NAME"] != null && repdata["COLUMN_NAME"] != "null")
    		columnName = repdata["COLUMN_NAME"];
    	if(repdata["WARING_TYPE"] != null && repdata["WARING_TYPE"] != "null")
    		waringType = repdata["WARING_TYPE"];
    	if(repdata["WARING_VALUE"] != null && repdata["WARING_VALUE"] != "null"){
    		waringValue = 1*repdata["WARING_VALUE"];
    	}
       if(repdata["WARING_VALUE2"] != null && repdata["WARING_VALUE2"] != "null"){
    		waringValue2 = 1*repdata["WARING_VALUE2"];
    	}
        reportId=repdata["REPORT_ID"];
    }
   
    var menuFormData = [
        {type: "settings", position: "label-left"},
        {type:"block",className:"blockStyle",offsetTop :20,list:[
        	{type:"input",label:"<span style=\"color:#000;font-size:12px;padding:20px;\">报表名称：</span>",name:"reportName",inputWidth:250,inputHeight:17,className:"inputStyle",value:""+reportName+""}, 
            {type:"newcolumn"}
        ]},
        {type:"block",className:"blockStyle",list:[
        	{type:"input",label:"<span style=\"color:#000;font-size:12px;padding:20px;\">指标名称：</span>",name:"indexName",inputWidth:250,inputHeight:17,className:"inputStyle",value:""+indexName+""}, 
            {type:"newcolumn"}
        ]},
       {type:"block",className:"blockStyle",list:[
            {type:"input",label:"<span style=\"color:#000;font-size:12px;padding:20px;\">列&nbsp;&nbsp;名&nbsp;称：</span>",name:"columnName", inputWidth:250,inputHeight:17,className:"inputStyle",value:""+columnName+""}, 
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
        {type:"hidden",rows:3,label:"type",name:"type",  value:""+type+""},
        {type:"hidden",rows:3,label:"reportId",name:"reportId",value:""+reportId+""},
        {type:"hidden",rows:3,label:"menuInfoId",name:"menuInfoId",value:""+menuInfoId+""}
    ]
    
    var addForm = addMenuLayout.cells("a").attachForm(menuFormData);
    addForm.defaultValidateEvent();
    addForm.hideItem("waringValue2");
    
    
    loadMenuTree(menuInfoId , addForm); //菜单树
   
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
		//addForm.disableItem("reportName");
		//addForm.disableItem("indexName");
		//addForm.disableItem("columnName");
	}
    addForm.attachEvent("onChange", function (id, value){
    	if(id !='waringType'){
    		return;
    	}
    	if(id=='waringType' && value == 'BTWEEN AND') {
			addForm.showItem("waringValue2");
    	}else {
    		addForm.setItemValue("waringValue2","0");
			addForm.hideItem("waringValue2");
			
			
    	}
	});
       //预警值输入验证
    var validData = function()
    {
    	
    	var waring = addForm.getCombo("waringType").getSelectedValue();
    	var waringValue = addForm.getItemValue("waringValue");
    	var waringValue2 = addForm.getItemValue("waringValue2");
     if("add" == type){
    	var menuInfoId= addForm.getInput("menuInfoId").value;
    	if(menuInfoId== ""){dhx.alert("请您选取报表！");return false;}
    	  }
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
            		ReportConfigAction.saveReportWarning(addForm.getFormData(),function(rs){
	            	 {
	            			dhx.alert(rs);
			                addWindow.hide();
				            addForm.unload();
				            dhxWindow.unload();
				            grid.clearAll();
	            			grid.load(dwrCaller.getReportWarnPage, "json");
			            }
	            	});
            	}else{
            		if(!validData()){
        				return;
        			}
            		ReportConfigAction.updateReportWarning(addForm.getFormData(),function(rs){
	            		if (rs) {
	            			dhx.alert("修改成功！");
			                addWindow.hide();
				            addForm.unload();
				            dhxWindow.unload();
				            grid.clearAll();
	            			grid.load(dwrCaller.getReportWarnPage, "json");
			            } else {
			            	dhx.alert("修改失败，请重试！");
			            }
	            	});
            	}
            }
        }
    });
}
dwrCaller.addAutoAction("loadMenuTree","MenuAction.queryMenuByPath");
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"menuId",pidColumn:"parMenuId",
    isDycload:false,textColumn:"menuName"
});
var stationConverter=dhx.extend({idColumn:"menuId",pidColumn:"parMenuId",textColumn:"menuName"},treeConverter,false);
dwrCaller.addDataConverter("loadMenuTree",stationConverter);
//树动态加载Action
dwrCaller.addAction("querySubMenu",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},stationConverter,false);
    MenuAction.querySubMenuInfo(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});


var loadMenuTree=function(selectStation, form){
	
    //加载部门树数据。加载用户所在部门及其子部门。
    selectStation=selectStation|| global.constant.defaultRoot; 
//    var beginId=global.constant.defaultRoot;
    var beginId=0;
    //创建tree Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target=form.getInput("reportName");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
//    tree.enableSmartRendering();
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubMenu);
    //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
        form.setFormData({reportName:tree.getItemText(nodeId),menuInfoId:nodeId});
        //关闭树
        div.style.display="none";
    });
   
    dwrCaller.executeAction("loadMenuTree",beginId,selectStation,function(data){
        tree.loadJSONObject(data);
        globMenuTree = tree;
        if(selectStation){
            tree.selectItem(selectStation); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
            //menuInfoId  =tree.getSelectedItemId();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    })
    div.style.display="none";
}

dhx.ready(init);