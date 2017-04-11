/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       import.js
 *Description：
 *       表类管理-导入实体表
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       李国民
 *Date：
 *       2012-01-09
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

//表类数据源相关Action
dwrCaller.addAutoAction("queryTableDataSource", "TableApplyAction.queryTableDataSource");
dwrCaller.addDataConverter("queryTableDataSource", new dhtmlxComboDataConverter({
	    valueColumn:"dataSourceId",
	    textColumn:"dataSourceName",
	    onBeforeConverted:function(data){
	         data.unshift({dataSourceId:"",dataSourceName:""});
	    }
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
dwrCaller.addAutoAction("queryDbTables","ImportTableAction.queryDbTables",param,function(data){});
dwrCaller.addDataConverter("queryDbTables",new dhtmxGridDataConverter({
	idColumnName:"tableName",
    filterColumns:["checkBox","tableName","metaTables","tableOwner","tableSpace"],
    
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
    	if(columnName=='metaTables'){
			var name = columnName+rowIndex;
			var str = "<select id="+name+" name="+name+" style='width:240px;' ";
			str +=" onchange=changeValue("+rowIndex+",'"+rowData['tableName']+"')>";
			for(var i=0; i<cellValue.length; i++){
				str += "<option value='"+cellValue[i][0]+"'>"+cellValue[i][1]+"</option>";
			}
			str += "</select>";
			str += "<span id=diff"+rowIndex+" name=diff"+rowIndex+">";
			if(rowData['diffCheck']){
				str += "<span style='color:red'> *</span>实体表与表类的表结构不同，";
				str += "<a style='cursor:pointer;color:blue;' onclick=openDiff("+rowIndex+",'"+rowData['tableName']+"')>查看详情</a>";
			}
			str += "</span>";
    		return str;
    	}else if (columnIndex == 0) {//第一列，checkbox列
            return 1;
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}));

//表类选择改变事件
changeValue= function(rowIndex,tableName){
	var data = {};
	data["dataSourceID"]=selectDataSourceId+"";
	data["owner"]=selectOwner;
	data["tableName"] = tableName;
	var metaValue = document.getElementById("metaTables"+rowIndex).value;
	var metaValues = metaValue.split(';');
	data["tableId"] = metaValues[0];
	data["tableVersion"] = metaValues[1];
	ImportTableAction.isDiffCheck(data,function(rs){
		if(rs){
			document.getElementById("diff"+rowIndex).innerHTML="<span style='color:red'> *</span>实体表与表类的表结构不同，" +
				"<a style='cursor:pointer;color:blue;' onclick=openDiff("+rowIndex+",'"+tableName+"')>查看详情</a>";
		}else{
			document.getElementById("diff"+rowIndex).innerHTML="";
		}
	});
}

//点击详细查看事件
openDiff = function(rowIndex,tableName){
	var dataSourceID = selectDataSourceId+"";	//数据源id
	var owner = selectOwner;		//表拥有者
	var obj=document.getElementById("metaTables"+rowIndex);	//选中的表类
	var index=obj.selectedIndex; //序号，取当前选中选项的序号
	var metaValue = obj.options[index].value;
	var metaValues = metaValue.split(';');
	var tableId = metaValues[0];			//表类id
	var tableVersion = metaValues[1];		//表类版本
	var metaTableName = obj.options[index].text	//表类名
	openMenu("差异分析","meta/module/tbl/imptab/imptabOperate.jsp?dataSourceID="+dataSourceID+"&metaTableName="+metaTableName+
		"&owner="+owner+"&tableName="+tableName+"&tableId="+tableId+"&tableVersion="+tableVersion,"top");
}


/**
 * 初始化界面
 */
var init=function(){
	var dhxLayout = new dhtmlXLayoutObject(document.body, "3E");
	dhxLayout.hideConcentrate();
	dhxLayout.hideSpliter();
	var topCell = dhxLayout.cells("a");
	topCell.setText("导入实体表");
	topCell.setHeight(80); 
	topCell.fixSize(true,true);
	queryForm = topCell.attachForm(
		[
	        {type:"settings",position: "label-left", inputWidth:120},
	        {type:"combo",label:"数据源：",name:"dataSource",inputHeight : 22},
	        {type:"newcolumn"},
	        {type:"combo",label:"所属用户：",name:"owner",validate: "NotEmpty",inputHeight : 22},
	        {type:"newcolumn"},
	        {type:"input",label:"关键字：",name:"keyWord",inputHeight : 17},
	        {type:"newcolumn"},
	        {type:"button",name:"queryBtn",value:"查询",offsetLeft : 0,offsetTop : 0}
	    ]
    );
	queryForm.getCombo("dataSource").enableFilteringMode(true);
//	queryForm.getCombo("owner").enableFilteringMode(true);
	queryForm.getCombo("dataSource").loadXML(dwrCaller.queryTableDataSource, function(){});
	
	queryForm.getCombo("dataSource").attachEvent("onChange", function(){
		var val = queryForm.getCombo("dataSource").getSelectedValue();
		var text = queryForm.getCombo("dataSource").getSelectedText();
        if(val == ""&&text != ""){
            val = 0;
        }else if(val == ""&&text == ""){
            val = -1;
        }
		queryForm.getCombo("owner").clearAll(true);
		dwrCaller.executeAction("queryDbUsers",val,function(data){
	    	var options=[];
            if(data){
                data=dhx.isArray(data)?data:[data];
                for(var i=0;i<data.length;i++){
                    options.push({text:data[i],value:data[i]});
                }
            }
	  		queryForm.getCombo("owner").addOption(options);
            TableApplyAction.queryDefaultUserByDataSource(val, function(name){
                if(queryForm.getCombo("owner").optionsArr&&queryForm.getCombo("owner").optionsArr.length){
                    for(var i=0; i<queryForm.getCombo("owner").optionsArr.length; i++){
                        if(queryForm.getCombo("owner").optionsArr[i].text==name){
                            queryForm.getCombo("owner").selectOption(i,false,false);
                            break;
                        }
                    }
                }
            });

	 	});
	});  
	
	var middleCell = dhxLayout.cells("b");
	middleCell.fixSize(true,true);
	middleCell.setHeight(400);
	middleCell.hideHeader();
	var grid = middleCell.attachGrid();
	grid.setHeader("{#checkBox},实体表名,表类,'',''");
	grid.setInitWidthsP("3,48,49,0,0");
	grid.setColAlign("center,left,left,left,left");			//列值显示位置
	grid.setHeaderAlign("center,left,left,left,left")		//头显示位置
	grid.setColTypes("ch,ro,ro,ro,ro");						//列显示类型
	grid.setColSorting("na,na,na,na,na");					//列值类型
	grid.enableResizing("false,true,false,false,false");   	//能否改变大小
	grid.setEditable(true);									//是否可以编辑
	grid.enableMultiselect(true);							//是否可以多选
    grid.enableSelectCheckedBoxCheck(2);
	grid.enableCtrlC();
	grid.enablePaging(false);
    grid.setColumnIds("checkBox,tableName,metaTables,tableOwner,tableSpace");
	grid.init();
	
	queryForm.attachEvent("onButtonClick", function(id) {
        if (id == "queryBtn") {
            //进行数据查询。
        	if(queryForm.validate()){
				selectDataSourceId = queryForm.getCombo("dataSource").getSelectedValue();
				selectOwner = queryForm.getCombo("owner").getSelectedValue();
        		grid.clearAll();
            	grid.load(dwrCaller.queryDbTables, "json");
        	}
        }
    });
	
	var bottomCell = dhxLayout.cells("c");
	bottomCell.setHeight(40);
	bottomCell.fixSize(true,true);
	bottomCell.hideHeader();
	importForm = bottomCell.attachForm(
		[	{
				type:"block",list:[
					{type:"button",name:"importBtn",value:"导入",offsetLeft:document.body.clientWidth-150}
				]
			}
		]
	);
	importForm.attachEvent("onButtonClick", function(id) {
        if (id == "importBtn") {
        	insertInstTable(grid);
        }
    });
	
}

//导入实体表映射关系数据
var insertInstTable = function(grid){
	//返回0表示没有选中数据，1表示选中数据中有存在差异，2表示正常
	var checkDiff = checkDiffData(grid);
	var str="";
	if(checkDiff=="0"){
		return;
	}else if(checkDiff=="1"){
		str="所选中的数据中存在差异！确定要继续导入选中的数据？";
	}else if(checkDiff=="2"){
		str="是否要导入选中的数据？";
	}
	dhx.confirm(str,function(r){
		if(r){
			var data = getData(grid);
			ImportTableAction.insertInstTable(data,function(rs){
				grid.clearAll();
				grid.load(dwrCaller.queryDbTables, "json");
				if(rs){
					dhx.alert("导入成功！");
				}else{
					dhx.alert("导入失败，请重试！");
				}
			});
		}
	});
}

//提交验证。返回0表示没有选中数据，1表示选中数据中有存在差异，2表示正常
var checkDiffData = function(grid){
	var checkRows = grid.getCheckedRows(0);
	if(checkRows){
		var rowsId = checkRows.split(',');
		for(var i=0;i<rowsId.length;i++){
			var rowIndex = grid.getRowIndex(rowsId[i]);
			if(document.getElementById("diff"+rowIndex).innerHTML != ""){
				return "1";
			};
		}
		return "2";
	}else{
		alert("请至少选择一条需要导入的数据！");
		return "0";
	}
}

//得到选中需要导入的数据
var getData = function(grid){
	var checkRows = grid.getCheckedRows(0);
	if(checkRows){
    	var columnData = [];
		var rowsId = checkRows.split(',');
		for(var i=0;i<rowsId.length;i++){
        	var rowData = {};
			var data = grid.getRowData(rowsId[i]).data;
            rowData["tableName"] = data[1];
            rowData["tableOwner"] = data[3];
            rowData["tableSpace"] = data[4];
			var rowIndex = grid.getRowIndex(rowsId[i]);
			var metaValue = document.getElementById("metaTables"+rowIndex).value;
			var metaValues = metaValue.split(';');
            rowData["tableId"] = metaValues[0];
            rowData["tableVersion"] = metaValues[1];
        	columnData.push(rowData);
		}
		return columnData;
	}
}

dhx.ready(init);