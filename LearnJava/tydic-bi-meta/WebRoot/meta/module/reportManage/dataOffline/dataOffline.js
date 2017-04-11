/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       batchImport.js
 *Description：
 *       报表管理-应用下线-模型下线
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       吴喜丽
 *Date：
 *       2012-03-14
 ********************************************************/
/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var pageSize = 20;
var queryForm;
var grid;
var dataOwnerName;
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();

var param = new biDwrMethodParam();
param.setParamConfig([
    {
       index:0,type:"fun",value:function() {
			var data = queryForm.getFormData();
			if(!data.dataSourceId){
				data.dataSourceId = "0";
			}
			return data;
   	   	}
    }
]);
//查询信息
dwrCaller.addAutoAction("getDataModelMes","DataOfflineAction.getDataModelMes",param);
dwrCaller.addDataConverter("getDataModelMes",new dhtmxGridDataConverter({
	isFormatColumn:false,
    filterColumns:["TABLE_ALIAS","TABLE_NAME","DATA_SOURCE_NAME","TABLE_GROUP_NAME","ISSUE_STATE","LINK"],
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
    		var str = "";
    		if(rowData["ISSUE_STATE"] == "0"){
    			str = "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;' " +
				" onclick=executeModelMes("+rowData["DATA_SOURCE_ID"]+","+rowData["ISSUE_ID"]+",'"+rowData["TABLE_ALIAS"]+"',"+rowData["ISSUE_STATE"]+",'"+rowData["TABLE_NAME"]+"','"+rowData["DATA_SOURCE_NAME"]+"',"+rowData["TABLE_ID"]+",0)>查看</a></span>";
    			return str;
    		}else if(rowData["ISSUE_STATE"] == "1"){
    			str += "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;' " +
					" onclick=executeModelMes("+rowData["DATA_SOURCE_ID"]+","+rowData["ISSUE_ID"]+",'"+rowData["TABLE_ALIAS"]+"',"+rowData["ISSUE_STATE"]+",'"+rowData["TABLE_NAME"]+"','"+rowData["DATA_SOURCE_NAME"]+"',"+rowData["TABLE_ID"]+",1)>下线</a></span>";
    		}
//    		else{
//    			str += "<span style='padding-left:5px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>";
//    		}
	    	return str;
        }else if(columnName == 'ISSUE_STATE'){
        	var str = "";
        	if(rowData["ISSUE_STATE"] == "0")
        		str = "已下线";
        	else if(rowData["ISSUE_STATE"] == "1")
        		str = "有效"
        	return str;
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}));
dwrCaller.addAutoAction("exeDataOffline","DataOfflineAction.exeDataOffline");
dwrCaller.isShowProcess("exeDataOffline",false);
executeModelMes = function(dataSourceId,issueId,tableAlias,issueState,tableName,dataSourceName,tableId,typeId){
	var str = "";
	if(issueState == "0")
       str = "查看";
    else if(issueState == "1")
       str = "下线"
	openMenu(tableAlias+"数据模型"+str,"meta/module/reportManage/dataOffline/exeDataOffline.jsp?dataSourceId="+dataSourceId+"&issueId="+issueId+"&tableAlias="+tableAlias+"&tableName="+tableName+"&dataSourceName="+dataSourceName+"&tableId="+tableId+"&typeId="+typeId,"top");
}

//查询数据源
dwrCaller.addAutoAction("queryTableDataSource", "TableViewAction.queryTableDataSource");
dwrCaller.addDataConverter("queryTableDataSource", new dhtmlxComboDataConverter({
	    valueColumn:"dataSourceId",
	    textColumn:"dataSourceName",
	    isAdd:true,
	    onBeforeConverted:function(data){
			dataOwnerName = data[0].DATA_SOURCE_USER;
	    }
	})
);
//查询对用数据源下的用户信息
dwrCaller.addAutoAction("queryDbUsers","TableApplyAction.queryDbUsers");
dwrCaller.isShowProcess("queryDbUsers",false);

/**
 * 加载业务类型树
 */
dwrCaller.addAutoAction("queryTableGroup", "TableApplyAction.queryTableGroup");
dwrCaller.addDataConverter("queryTableGroup", new dhtmxTreeDataConverter({
    idColumn:"tableGroupId",pidColumn:"parGroupId",textColumn:"tableGroupName",
    isDycload:false
}));
dwrCaller.isShowProcess("queryTableGroup", false);

dwrCaller.addAutoAction("queryGroupTree",TableViewAction.queryGroupTree);
var groupTreeConverter=new dhtmxTreeDataConverter({
    idColumn:"tableGroupId",pidColumn:"parGroupId",
    isDycload:false,textColumn:"tableGroupName"
});
dwrCaller.addDataConverter("queryGroupTree",groupTreeConverter);
var div = null,target = null;
/**
 * 加载业务类型树
 * @param selectGroup
 * @param form
 * @param levelRefID 层次管理ID
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
        if(selectGroup){
            tree.selectItem(selectGroup); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            //联动
            var tableTypeId = form.getCombo("tableTypeId").getSelectedValue();
            if(tableTypeId==-1||tableTypeId==""){
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
}
/**
 * 初始化界面
 */
var init=function(){
	var dhxLayout = new dhtmlXLayoutObject(document.body, "2E");
	dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.setText("模型下线情况查询");
    topCell.setHeight(94);
    topCell.fixSize(true,true);
    queryForm = topCell.attachForm(
            [
            	{type:"setting",position: "label-left",inputWidth: 120},
            	{type:"block",list:[
            		{type:"settings",position: "label-left", inputWidth: 120},
            		{type:"combo",label:"<span style='text-align:right;width:70px;display:block;'>数据源：</span>",name:"dataSourceId",inputHeight:22,inputWidth:110,readonly:true},
	                {type:"newcolumn"},
	                {type:"combo",label:"<span style='text-align:right;width:70px;display:block;'>用户：</span>",name:"tableOwner",inputHeight:22,inputWidth:100,readonly:true},
	                {type:"newcolumn"},
	                {type:"combo",label:"<span style='text-align:right;width:95px;display:block;'>层次分类：</span>",name:"tableTypeId",inputWidth:140,inputHeight:22,readonly:true},
		    	    {type:"newcolumn"},
		    	    {type:'input',label:"<span style='text-align:right;width:95px;display:block;'>业务类型：</span>",name:'tableGroup',inputWidth:120,inputHeight:17,readonly:true}
            	]},
                {type:"block",list:[
                	{type:"settings",position: "label-left", inputWidth: 120},
            		{type:'combo',label:"<span style='text-align:right;width:70px;display:block;'>模型状态：</span>",name:'issueState',inputWidth:110,inputHeight:22,readonly:true,options:[{value:"-1",text:"全部",selected:true},{value:"0",text:"下线"},{value:"1",text:"正常"}]},
            		{type:"newcolumn"},
            		{type:"input",label:"<span style='text-align:right;width:70px;display:block;'>关键字：</span>",name:"keyword",inputWidth:350,inputHeight:17},
            		{type:"newcolumn"},
	        		{type:"button",name:"queryBtn",value:"查询",offsetLeft:25,offsetTop:0}
            	]}
            ]
    );
    queryForm.getCombo("dataSourceId").enableFilteringMode(false);
	queryForm.getCombo("tableOwner").enableFilteringMode(false);
	//加载数据源
	queryForm.getCombo("dataSourceId").loadXML(dwrCaller.queryTableDataSource, function(){
//		queryForm.getCombo("dataSourceId").selectOption(1); //初始化页面默认显示第一条数据
		queryForm.getCombo("dataSourceId").getSelectedText();
	});
	
	//添加数据源选择事件，查询对应数据源下的所属用户
	queryForm.getCombo("dataSourceId").attachEvent("onChange", function(){
		var val = queryForm.getCombo("dataSourceId").getSelectedValue();
		val = val==""?0:val;
		queryForm.getCombo("tableOwner").clearAll(true);
		if(val != -1){
			dwrCaller.executeAction("queryDbUsers",val,function(data){
		    	var options=[];
		      	for(var i=0;i<data.length;i++){
		      		options.push({text:data[i],value:data[i]});
		    	}
		  		queryForm.getCombo("tableOwner").addOption(options);
		  		for(var i=0;i<data.length;i++){
		      		if(dataOwnerName != "" && dataOwnerName != "null" && dataOwnerName.toUpperCase() == data[i].toUpperCase()){
		      			queryForm.getCombo("tableOwner").selectOption(i);
		      		}
		    	}
		 	});
		}
	});  
	//加载层次分类
    tableTypeData = getComboByRemoveValue("TABLE_TYPE");
    queryForm.getCombo("tableTypeId").addOption(tableTypeData.options);

    //绑定层次分类combo数据改变的时候业务类型combo的同步
	queryForm.getCombo("tableTypeId").attachEvent('onChange',function(){
    	queryForm.setFormData({tableGroup:""});
	});
    //加载业务树
	queryGroupTree(null,queryForm,queryForm.getCombo("tableTypeId").getSelectedValue());
	
	queryForm.attachEvent("onButtonClick", function(id) {
        if (id == "queryBtn") {
            //进行数据查询。
        	if(queryForm.validate()){
        		grid.clearAll();
            	grid.load(dwrCaller.getDataModelMes, "json");
        	}
        }
    });
    //查询数据表单
    var dataCell = dhxLayout.cells("b");
    dataCell.fixSize(true,false);
    dataCell.hideHeader();
    grid = dataCell.attachGrid();
    grid.setHeader("模型名称,表类名称,数据源,业务类型,模型状态,操作");
    grid.setInitWidthsP("20,15,15,15,15,20");
    grid.setColAlign("left,left,left,left,left,center");
    grid.setHeaderAlign("center,center,center,center,center,center");
    grid.setColTypes("ro,ro,ro,ro,ro,ro");
    grid.setColSorting("na,na,na,na,na,na");
    grid.enableResizing("true,true,true,true,true,true");
    grid.enableMultiselect(false);
    grid.enableCtrlC();
//    grid.setColumnIds("tablename,modulename,datasourcename,modulestate,execute");
    grid.init();
    grid.defaultPaging(pageSize);
    //数据加载
    grid.load(dwrCaller.getDataModelMes, "json");
    
}
dhx.ready(init);