/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        mapping.js
 *Description：
 *       查看编码映射审核JS文件
 *Dependent：
 *        dhtmlx.js、dwr相关JS文件、dhtmlxExtend.js、tool。js等。
 *Author:
 *        李国民
 *Finished：
 *       2012-02-08
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var selectDataSourceId = "";	//数据源
var selectOwner = "";			//表用户
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();

//参数定义
var queryForm;
var param = new biDwrMethodParam();
param.setParamConfig([
    {
       index:0,type:"fun",value:function() {
    		var data = {};
    		data.batchId = batchId+"";					//批次号
    		data.dimTableId = dimTableId+"";			//维度表id
			return data;
   	   	}
    }
]);

/**
 * 查询表信息Action定义
 */
dwrCaller.addAutoAction("queryMapping","ShowMappingAuditAction.queryMapping",param,function(data){});
dwrCaller.addDataConverter("queryMapping",new dhtmxGridDataConverter({
    filterColumns:["flag","auditDesc","auditUser","srcName","srcCode","itemCode","itemName"],
    
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData) {
        var userData = {};
        userData.modMark = rowData["modMark"];
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
    	if(columnName=='itemCode'&&rowData['deleteFlag']==1){	//维度编码并且为删除标记时。
    		var str = "<div style='text-decoration:line-through;color:#aaa;'>"+cellValue+"</div>";
    		return str;
    	}else if(columnName=='itemName'&&rowData['deleteFlag']==1){	//编码值并且为删除标记时。
    		var str = "<div style='text-decoration:line-through;color:#aaa;'>"+cellValue+"</div>";
    		return str;
    	}
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}));



/**
 * 初始化界面
 */
var init=function(){
	var dhxLayout = new dhtmlXLayoutObject(document.body, "2E");
	dhxLayout.hideConcentrate();
	dhxLayout.hideSpliter();
	
	var topCell = dhxLayout.cells("a");
	topCell.fixSize(true,true);
	topCell.setHeight(400);
	topCell.hideHeader();
	var grid = topCell.attachGrid();
	grid.setHeader("操作类型,审核结果,审核人,应用系统,#cspan,元数据维度,#cspan",null,
		["text-align:center;vertical-align:middle;","text-align:center;vertical-align:middle;",
			"text-align:center;vertical-align:middle;","text-align:center","text-align:center"]);
	grid.attachHeader("#rspan,#rspan,#rspan,编码名称,编码,维度编码,编码值",
		["text-align:center;","text-align:center;","text-align:center;",
			"text-align:center","text-align:center;","text-align:center","text-align:center"]);
    grid.setInitWidthsP("11,9,8,15,15,21,21");				//列宽度
	grid.setColAlign("center,center,left,left,left,left,left");	//列值显示位置
	grid.setColTypes("ro,ro,ro,ro,ro,ro,ro");					//列显示类型
	grid.setColSorting("na,na,na,na,na,na,na");				//列值类型
	grid.enableResizing("true,true,true,true,true,true,true");	//能否改变大小
	grid.enableCtrlC();
	grid.enablePaging(true);
    grid.setColumnIds("flag,auditDesc,auditUser,srcName,srcCode,itemCode,itemName");
	grid.init();
    grid.defaultPaging(20);
    grid.load(dwrCaller.queryMapping,"json");

	grid.attachEvent("onRowSelect", function(id,ind){
		var rowData = grid.getRowData(id).userdata;
		queryForm.setItemValue("modMark", rowData["modMark"]);
	});
	
	var bottomCell = dhxLayout.cells("b");
	bottomCell.setHeight(40);
	bottomCell.fixSize(true,true);
	bottomCell.hideHeader();
	queryForm = bottomCell.attachForm(
		[
	        {type:"input",label:"审核意见：",name:"modMark",readonly:"true",
	        	inputWidth:800,rows :5}
	    ]
    );

}

dhx.ready(init);