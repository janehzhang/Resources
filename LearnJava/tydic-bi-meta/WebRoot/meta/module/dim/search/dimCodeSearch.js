/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        dimCodeSearch.js
 *Description：
 *       维度值查询JS文件
 *Dependent：
 *        dhtmlx.js、dwr相关JS文件、dhtmlxExtend.js、tool.js等。
 *Author:
 *        tanwc
 *Finished：
 *       2012-5-17
 *
 ********************************************************/
var dwrCaller = new biDwrCaller();
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();

var treeGridConverterConfig = {
    isDycload:false,
    isFormatColumn:false,
	//filterColumns:["DATE_ID","DATE_PAR_ID","DATE_CODE","DATE_NAME","DATE_DESC","DIM_TABLE_ID","STATE","DIM_LEVEL","MOD_FLAG","ORDER_ID"],
    cellDataFormat:function (rowIndex, columnIndex, columnName, cellValue, rowData) {
        if (columnName == "STATE") {
             if(rowData["STATE"] == 1) {
             	return "有效";
             } else {
             	return "无效";
             }
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    },
    userData:function (rowIndex, rowData) {
        return rowData;
    }
}
var treeGridConverter = new dhtmlxTreeGridDataConverter(treeGridConverterConfig);
 
dwrCaller.addAutoAction("queryDimCode", DimCodeSearchAction.queryDimCode, {
    dwrConfig:true,
    converter:treeGridConverter,
    isShowProcess:true,
    isDealOneParam:false
});
dwrCaller.addAutoAction("querySubDimCode", DimCodeSearchAction.querySubDimCode, {
    dwrConfig:true,
    isDealOneParam:false,
    onBeforeLoad:function (params) {
        params[0] = searchData;
    }
});

dwrCaller.addAutoAction("queryDimInfo", "TableApplyAction.queryDimInfo");
dwrCaller.addDataConverter("queryDimInfo", new dhtmxGridDataConverter({
    idColumnName:"tableId",
    filterColumns:["","tableName","tableNameCn","tableBusComment"],
    userData:function(rowIndex, rowData){
        return rowData;
    }
}));

var searchData = {};
var queryform = null;
var layout = null;

var init = function() {
	layout = new dhtmlXLayoutObject(document.body, "2E");

    //构建字段树区域
    layout.cells("a").hideHeader();
    layout.cells("a").setHeight(50);
    layout.cells('a').fixSize(true,true);
    layout.cells("b").setText("维度值查询");
    layout.hideConcentrate();
    queryform = layout.cells("a").attachForm([
    	{
    		type : "setting",
	        position : "label-left",
	        labelWidth : 120,
	        inputWidth : 120
    	}, 
    	{	
    		type : "input",
	        label : "维度表：",
	        name : "dimName",
	        readonly:true,
	        value:""
    	}, 
		{	
    		type : "newcolumn"
    	}, 
    	{	
    		type : "select",
	        label : "归并类型：",
	        name : "dimType",
	        options:[{text: "请选择归并类型", value: "-1"}]
    	},
		{	
    		type : "newcolumn"
    	}, 
    	{
    		type : "input",
    		label: "关键字",
    		name:	"keyword"
    	},
    	{	
    		type : "newcolumn"
    	}, 
    	{
	    	type : "button",
			name : "query",
	        value : "查询",
	        offsetLeft :0,
	        offsetTop : 0
    	}, 
    	{
    		type:"hidden", name:"template"
    	}
    ]);
    
	queryform.getInput("dimName").onclick=function(e){
		selectDim(this);
    }
    
   	queryform.attachEvent("onButtonClick", function(id) {
		if (id == "query") {
			queryData();
		}
	});
	
    queryform.getInput("keyword").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
        	queryData();
        }
    }
}

var queryData = function() {
	if(!searchData.tableId) {
		dhx.alert("请先选择维度表！");
		return;
	}
	searchData.keyword 		= queryform.getInput("keyword").value;
	searchData.dimTypeId 	= dwr.util.getValue(queryform.getSelect("dimType"));
	DimCodeSearchAction.queryValidCols(searchData.tableId, function (colInfo) {
		DimCodeSearchAction.queryDimData(searchData.tableId, function (dimTableInfo) {
			loadData(colInfo,dimTableInfo.TABLE_DIM_PREFIX.toUpperCase());
		});
	});
}
var dimWindow = null;
var dimLayout = null;
var selectDim = function(input){
    var rowIndex = input.parentNode.parentNode._index;
    //参数收集，参数来源于查询表单_queryDimForm
    var param = new biDwrMethodParam([
        {
            index:0,type: "ui",value:"_queryDimForm"
        }
    ]);
    //查询。
    var queryDimInfo = function(){
        queryDimGrid.clearAll();
        queryDimGrid.load(dwrCaller.queryDimInfo + param, "json");
    }
    if(!dimWindow){
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("dimWindow", 0, 0, 450, 400);
        dimWindow = dhxWindow.window("dimWindow");
        dimWindow.stick();
        dimWindow.setDimension(550, 390);
        dimWindow.button("minmax1").hide();
        dimWindow.button("park").hide();
        dimWindow.button("stick").hide();
        dimWindow.button("sticked").hide();
        dimWindow.center();
        dimWindow.denyResize();
        dimWindow.denyPark();
        dimWindow.setText("选择维表");
        dimWindow.keepInViewport(true);
        dimWindow.show();
        dimWindow.attachEvent("onClose", function(){
            dimWindow.hide();
            dimWindow.setModal(false);
            return false;
        })
        //添加查询内容
        dimLayout = new dhtmlXLayoutObject(dimWindow, "2E");
        dimLayout.hideConcentrate();
        dimLayout.cells("b").hideHeader();
        dimLayout.cells("a").setHeight(310);
        dimLayout.cells("a").fixSize(true, true);
        dimLayout.cells("a").firstChild.style.height = (dimLayout.cells("a").firstChild.offsetHeight + 5) + "px";
        dimLayout.cells("a").hideHeader();
        dimLayout.hideSpliter();//移除分界边框。
        //添加查询按钮
        var queryButton = Tools.getButtonNode("查询");
        //为button绑定事件
        queryButton.onclick = queryDimInfo;
        dimLayout.cells("a").attachObject("_dimInfoTable");
        $("_queryDimButton").appendChild(queryButton);
        queryDimGrid = new dhtmlXGridObject("_queryDimGrid");
        queryDimGrid.setHeader(",表名,中文名,注释");
        queryDimGrid.setInitWidthsP("6,30,30,34");
        queryDimGrid.setColAlign("center,center,center,center");
        queryDimGrid.setHeaderAlign("center,center,center,center");
        queryDimGrid.setColTypes("ra,ro,ro,ro");
        queryDimGrid.setColSorting("na,na,na,na");
        queryDimGrid.setEditable(true);
        queryDimGrid.enableMultiselect(false);
        queryDimGrid.enableCtrlC();
        queryDimGrid.init();
        queryDimGrid.defaultPaging(10);
        var radioCheck=function(rId){
        }
        //添加radio点击事件。
        queryDimGrid.attachEvent("onCheck", function(rId, cInd, state){
            if(state){
                radioCheck(rId);
            }
        });
        
        $('_queryDimTableName').onkeyup = function(e) {
			if (!e) {
	            e = window.event;
	        }
	        if (e.keyCode == 13) {
	        	queryDimInfo();
	        }
        }
        // 添加行点击事件
        queryDimGrid.attachEvent("onRowSelect",function(id,ind){
        	queryDimGrid.cells(id,0).setValue(1);
        	radioCheck(id);
        });

        //设置确定和关闭按钮。
        var ok = Tools.getButtonNode("确定");
        ok.style.marginLeft = "205px";
        ok.style.marginTop = "10px";
        ok.style.cssFloat="left";
        ok.style.styleFloat="left";
        //确定按钮事件
        ok.onclick = function(){
	        var rowData = queryDimGrid.getRowData(queryDimGrid.getCheckedRows(0).split(",")[0]);
	        if(rowData) {
				searchData.tableName = rowData.userdata.tableName;
				searchData.tableOwner = rowData.userdata.tableOwner;
		        searchData.preName = rowData.userdata.tableDimPrefix;
		        searchData.tableId = rowData.userdata.tableId;
		        searchData.dataSourceId = rowData.userdata.dataSourceId;
		        queryform.getInput("dimName").value = searchData.tableName;
		        //查询归并类型
		        TableApplyAction.queryDimType(rowData.userdata.tableId,function(data){
		        	queryform.getSelect("dimType").options.length = 0;
		        	for(var i=0;i<data.length;i++) {
       					queryform.getSelect("dimType").options[i] = new Option(data[i].DIM_TYPE_NAME, data[i].DIM_TYPE_ID);
		        	}
		        });
	        }
			if(treeDataGrid) {
				treeDataGrid.clearAll(true);
			}
			queryform.getInput("keyword").value = "";
	        dimWindow.close();
        }
        var close = Tools.getButtonNode("关闭");
        close.style.styleFloat = "left";
        close.style.cssFloat = "left";
        close.style.marginTop = "10px";
        dimLayout.cells("b").attachObject("_dimInfoWindowButton");
        $("_dimInfoWindowButton").appendChild(ok);
        $("_dimInfoWindowButton").appendChild(close);
        //添加close事件
        close.onclick = function(){
            dimWindow.close();
            dhtmlxValidation.clearAllTip();
        }
        queryDimInfo();
    }
    dimWindow.setModal(true);
    dimWindow.show();
}

var treeDataGrid = null;
var loadData = function(colInfo,tablePrefix) {
	var imagePath = dhtmlx.image_path + "csh_" + dhtmlx.skin + "/";
	treeDataGrid = layout.cells("b").attachGrid();
	treeDataGrid.setImagePath(imagePath);
	treeDataGrid.setIconPath(imagePath);
	
	var head = new Array();
	var filterColumns = new Array();
	var colTypes = new Array();
	var colSortStr = new Array();
	var count = 1 ;
	for(var i=0;i<colInfo.length;i++) {
		if(colInfo[i].COL_NAME == ""+tablePrefix+"_ID" 
			|| colInfo[i].COL_NAME == ""+tablePrefix+"_PAR_ID" 
			||colInfo[i].COL_NAME == "DIM_TABLE_ID"
			||colInfo[i].COL_NAME == "DIM_TYPE_ID"
			||colInfo[i].COL_NAME == "DIM_LEVEL"
			||colInfo[i].COL_NAME == "MOD_FLAG"
			||colInfo[i].COL_NAME == "ORDER_ID"
			||colInfo[i].COL_NAME == ""+tablePrefix+"_PAR_CODE" 
			)
		{
			continue;
		}
		
		if(!colInfo[i].COL_NAME_CN) {
			head.push(colInfo[i].COL_NAME);
		}else {
			head.push(colInfo[i].COL_NAME_CN);
		}
		filterColumns.push(colInfo[i].COL_NAME);
		if( count==1 ) {
			colTypes.push("tree");
		}else {
			colTypes.push("ro");
		}
		colSortStr.push("na");
		count ++;
	}
	treeGridConverter.setIdColumn(""+tablePrefix+"_ID");
	treeGridConverter.setPidColumn(""+tablePrefix+"_PAR_ID");
	treeDataGrid.setHeader(head.join(","));
	treeDataGrid.setColumnIds(head.join(","));
	treeGridConverter.setFilterColumns(filterColumns);
   	treeDataGrid.setColTypes(colTypes.join(","));
	treeDataGrid.setColSorting(colSortStr.join(","));
    treeDataGrid.enableTreeGridLines();
    treeDataGrid.setEditable(false);
    treeDataGrid.enableDragAndDrop(false);//设置拖拽
    treeDataGrid.setDragBehavior("complex");
    treeDataGrid.init();
    treeDataGrid.enableMercyDrag(false);
    
    var param = new biDwrMethodParam();
    param.setParam(0, searchData);
    
    var tempConverter = dhx.extend({}, treeGridConverter, true);
	tempConverter.isDycload = true;
	dwrCaller.addDataConverter("querySubDimCode", tempConverter);
    treeDataGrid.kidsXmlFile = dwrCaller.querySubDimCode;
	dwrCaller.executeAction("queryDimCode", searchData,function(data){
		var obj = data;
		treeDataGrid.parse(data, "json");
	});
	
	treeDataGrid.attachEvent("onOpenStart", function(id,state){
		(state == -1) && (searchData.parId = id);
		return true;
	});
	treeDataGrid.attachEvent("onOpenEnd", function(id,state){
		searchData.parId = 0;
		return true;
	});
	
	

}

dhx.ready(init);
