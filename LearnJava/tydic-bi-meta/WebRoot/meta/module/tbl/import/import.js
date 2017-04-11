/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       import.js
 *Description：
 *       表类管理-系统初始化导入表
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       谭红涛
 *Date：
 *       2011-11-10
 ********************************************************/

/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();

//表类数据源相关Action
dwrCaller.addAutoAction("queryTableDataSource", "TableApplyAction.queryTableDataSource");
dwrCaller.addDataConverter("queryTableDataSource", new dhtmlxComboDataConverter({
	    valueColumn:"dataSourceId",
	    textColumn:"dataSourceName"
	})
);

/**
 * 查询用户信息Action定义
 */
dwrCaller.addAutoAction("queryDbUsers","TableApplyAction.queryDbUsers");
dwrCaller.isShowProcess("queryDbUsers",false);
var pageSize = 20;
var queryForm;
var param = new biDwrMethodParam();
param.setParamConfig([
    {
       index:0,type:"fun",value:function() {
    			var data = queryForm.getFormData();
    			data.dataSource=data.dataSource+"";
    			if(!data.keyWord){
    				data.keyWord="";
    			}
    			data.pageSize=pageSize+"";
				return data;
   	   		}
    }
]);

/**
 * 查询表信息Action定义
 */
dwrCaller.addAutoAction("queryDbTables","TableApplyAction.queryDbTables",param,function(data){});
dwrCaller.addDataConverter("queryDbTables",new dhtmxGridDataConverter({
	idColumnName:"tableName",
    filterColumns:["","tableName","tableTypeName"],
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
    /**
     * 获取下拉框Button的值
     * @param rowIndex
     * @param columnIndex
     * @param cellValue
     * @param rowData
     */
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
    	if(columnName=='tableTypeName'){
    		return rowData['tableName'];
    	}else if (columnIndex == 0) {//第一列，checkbox列
            return 1;
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}));


/**
 * 初始化界面
 */
var init=function(){
	var dhxLayout = new dhtmlXLayoutObject(document.body, "3E");
	dhxLayout.hideConcentrate();
	dhxLayout.hideSpliter();
	var topCell = dhxLayout.cells("a");
	topCell.setText("初始化导入表");
	topCell.setHeight(80); 
	topCell.fixSize(true,true);
	queryForm = topCell.attachForm(
		[
	        {type:"settings",position: "label-left", labelWidth: 70, inputWidth: 120},
	        {type:"combo",label:"数据源：",name:"dataSource",validate: "NotEmpty"},
	        {type:"newcolumn"},
	        {type:"combo",label:"用户名：",name:"owner",validate: "NotEmpty"},
	        {type:"newcolumn"},
	        {type:"input",label:"表名特征：",name:"keyWord"},
	        {type:"newcolumn"},
	        {type:"button",name:"queryBtn",value:"查询"}
	    ]
    );
	queryForm.getCombo("dataSource").readonly(true,false);
	queryForm.getCombo("owner").readonly(true,false);
	queryForm.getCombo("dataSource").loadXML(dwrCaller.queryTableDataSource, function(){});
	
	queryForm.getCombo("dataSource").attachEvent("onChange", function(){
		var val = queryForm.getCombo("dataSource").getSelectedValue();
		if(val){
			queryForm.getCombo("owner").clearAll(true);
			dwrCaller.executeAction("queryDbUsers",val,function(data){
	            var options=[];
	            for(var i=0;i<data.length;i++){
	                options.push({text:data[i],value:data[i]});
	            }
	            queryForm.getCombo("owner").addOption(options);
	        });
		}
	});  
	
	var bottomCell = dhxLayout.cells("c");
	bottomCell.setHeight(50);
	bottomCell.fixSize(true,true);
	bottomCell.hideHeader();
	bottomCell.attachForm(
		[	{
				type:"block",list:[
					{type:"button",name:"importBtn",value:"导入",offsetLeft:document.body.clientWidth-150}
				]
			}
		]
	)
	
	var middleCell = dhxLayout.cells("b");
	middleCell.fixSize(true,true);
	middleCell.hideHeader();
	var grid = middleCell.attachGrid();
	grid.setHeader("{#checkBox},表名,归为表类");
	grid.setInitWidthsP("3,48,49");
	grid.setColAlign("center,left,left");
	grid.setHeaderAlign("center,left,left")
	grid.setColTypes("ch,ro,ed");
	grid.setColSorting("na,na,na");
	grid.enableResizing("false,true,false");
	grid.setEditable(true);
	grid.enableMultiselect(true);
    grid.enableSelectCheckedBoxCheck(2);
	grid.enableCtrlC();
    grid.setColumnIds("'',tableName,tableTypeName");
	grid.init();
	grid.defaultPaging(pageSize,true);
	
	queryForm.attachEvent("onButtonClick", function(id) {
        if (id == "queryBtn") {
            //进行数据查询。
        	if(queryForm.validate()){
        		grid.clearAll();
            	grid.load(dwrCaller.queryDbTables, "json");
        	}
        }
    });
}
dhx.ready(init);