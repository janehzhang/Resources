/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        mapping.js
 *Description：
 *       审核编码映射JS文件
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
    		data.tableName = tableName+"";           //维度表名称
    		data.tableOwner = tableOwner;
    		data.tableDimPrefix = tableDimPrefix+"";	//前缀
			return data;
   	   	}
    }
]);

/**
 * 查询表信息Action定义
 */
dwrCaller.addAutoAction("queryMapping","MappingAuditAction.queryMappingAudit",param,function(data){});
dwrCaller.addDataConverter("queryMapping",new dhtmxGridDataConverter({
    filterColumns:["checkBox","flag","srcName","srcCode","showItemCode","showItemName"],
    
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData) {
        var userData = {};
        userData.itemId = rowData["itemId"];
        userData.itemName = rowData["itemName"];
        userData.itemParId = rowData["itemParId"];
        userData.itemCode = rowData["itemCode"];
        userData.dimTypeId = rowData["dimTypeId"];
        userData.dimLevel = rowData["dimLevel"];
        userData.modFlag = rowData["modFlag"];
        userData.modMark = rowData["modMark"];
        userData.modDate = rowData["modDate"];
        userData.userId = rowData["userId"];
        userData.dimTableId = rowData["dimTableId"];
        userData.srcCode = rowData["srcCode"];
        userData.srcName = rowData["srcName"];
        userData.col1 = rowData["col1"];
        userData.col2 = rowData["col2"];
        userData.col3 = rowData["col3"];
        userData.col4 = rowData["col4"];
        userData.itemDesc = rowData["itemDesc"];
        userData.orderId = rowData["orderId"];
        userData.state = rowData["state"];
        userData.auditFlag = rowData["auditFlag"];
        userData.auditUserId = rowData["auditUserId"];
        userData.srcSysId = rowData["srcSysId"];
        userData.hisId = rowData["hisId"];
        userData.batchId = rowData["batchId"];
        userData.col5 = rowData["col5"];
        userData.showItemCode = rowData["showItemCode"];
        userData.showItemName = rowData["showItemName"];
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
    	if(columnName=='showItemCode'&&rowData['deleteFlag']==1){	//维度编码并且为删除标记时。
    		var str = "<div style='text-decoration:line-through;color:#aaa;'>"+cellValue+"</div>";
    		return str;
    	}else if(columnName=='showItemName'&&rowData['deleteFlag']==1){	//编码值并且为删除标记时。
    		var str = "<div style='text-decoration:line-through;color:#aaa;'>"+cellValue+"</div>";
    		return str;
    	}
    	if (columnIndex == 0) {//第一列，checkbox列
            return 1;
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}));



/**
 * 初始化界面
 */
var init=function(){
	window.parent.addTabRefreshOnActiveByName("维度管理");
	var dhxLayout = new dhtmlXLayoutObject(document.body, "2E");
	dhxLayout.hideConcentrate();
	dhxLayout.hideSpliter();
	
	var topCell = dhxLayout.cells("a");
	topCell.fixSize(true,true);
	topCell.setHeight(400);
	topCell.hideHeader();
	var grid = topCell.attachGrid();
	grid.setHeader("{#checkBox},操作类型,应用系统,#cspan,元数据维度,#cspan",null,
		["text-align:center;","text-align:center;vertical-align:middle;","text-align:center","text-align:center"]);
	grid.attachHeader("#rspan,#rspan,编码名称,编码,维度编码,编码值",
		["text-align:center;","text-align:center;","text-align:center","text-align:center;","text-align:center","text-align:center"]);
    grid.setInitWidthsP("3,15,15,15,26,26");				//列宽度
	grid.setColAlign("center,center,left,left,left,left");	//列值显示位置
	grid.setColTypes("ch,ro,ro,ro,ro,ro");					//列显示类型
	grid.setColSorting("na,na,na,na,na,na");				//列值类型
	grid.enableResizing("true,true,true,true,true,true");	//能否改变大小
	grid.enableCtrlC();
	grid.enablePaging(true);
    grid.setColumnIds("checkBox,flag,srcName,srcCode,showItemCode,showItemName");
	grid.init();
    grid.defaultPaging(20);
    grid.load(dwrCaller.queryMapping,"json");

	
	var bottomCell = dhxLayout.cells("b");
	bottomCell.setHeight(40);
	bottomCell.fixSize(true,true);
	bottomCell.hideHeader();
	queryForm = bottomCell.attachForm(
		[
	        {type:"input",label:"审核意见：",name:"opinion",id:"_opinion",validate:"NotEmpty,MaxLength[1000]",
	        	inputWidth:800,rows :5},
	        {type:"newrow"},
	        {type:"button",value:"审核通过",name:"auditPass",position:"absolute",
	        	inputLeft:460,inputTop:100},
	        {type:"button",value:"驳回",name:"auditRejected",position:"absolute",
	        	inputLeft:560,inputTop:100}
	    ]
    );
	queryForm.defaultValidateEvent();		//form内数据验证方法
	
	queryForm.attachEvent("onButtonClick", function(id) {
		if(queryForm.validate()){
	        if (id == "auditPass") {
	        	commitData(grid,1,"审核");
	        }else if(id == "auditRejected"){
	        	commitData(grid,2,"驳回");
	        }
		}
    });
	
}

//操作选中的数据
var commitData = function(grid,auditFlag,str){
	var checkRows = grid.getCheckedRows(0);
	if(!checkRows){
		alert("请至少选择一条需要"+str+"的数据！");
		return
	}
	dhx.confirm("您确定要对选中的数据进行"+str+"？",function(r){
		if(r){
			var res = getData(grid,auditFlag);
			MappingAuditAction.commitMappingAudit(res,function(rs){
				grid.clearAll();
				queryForm.setItemValue("opinion", "");
	    		grid.load(dwrCaller.queryMapping,"json");
				if(rs){
					dhx.alert("处理成功！");
				}else{
					dhx.alert("处理失败，请重试！");
				}
			});
		}
	});
}

//得到选中需要导入的数据
var getData = function(grid,auditFlag){
	var checkRows = grid.getCheckedRows(0);
	if(checkRows){
    	var columnData = [];
		var rowsId = checkRows.split(',');
		for(var i=0;i<rowsId.length;i++){
			var rowData = grid.getRowData(rowsId[i]).userdata;
        	columnData.push(rowData);
		}
		var tableData = {};
    	tableData.dimTableName = tableName;				//维度表名称
    	tableData.dimTablePrefix = tableDimPrefix;		//前缀
    	tableData.auditFlag = auditFlag;				//审核为通过还是驳回标识
    	tableData.dimTableId = dimTableId;				//维度表id
    	tableData.tableOwner = tableOwner;				//用户
    	tableData.modMask = queryForm.getItemValue("opinion");
	    var res = {};
	    res.tableData = tableData;
	    res.columnDatas = columnData;
	    return res;
	}
}

dhx.ready(init);