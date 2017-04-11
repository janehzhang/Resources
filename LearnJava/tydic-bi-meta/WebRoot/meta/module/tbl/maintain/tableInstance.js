/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        basicInfo.js
 *Description：
 *       维护表类具体信息--表类实例JS
 *Dependent：
 *       dhtmlx.js、dwr相关JS文件、dhtmlxExtend.js、tool。js等。
 *Author:
 *        熊久亮
 *Finished：
 *       2011-11-14
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

********************************************************/
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller = new biDwrCaller();

var tableInstId;

/**------------------------------表类实例 begin----------------------------------*/
	var instConfig = {
    idColumnName:"tableInstId",
    filterColumns:["tableName","dataSourceName","tableSpace","tableOwner","tableRecords","tableDate","_buttons"],
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
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
        if (columnName == '_buttons') {
            return "getRoleButtonsCol";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};
var instConvert = new dhtmxGridDataConverter(instConfig);
dwrCaller.addAutoAction("queryTableInstanceByTableId","TableViewAction.queryTableInstanceByTableId",tableId,tableVersion,{
     dwrConfig:true,
    isShowProcess:false
});
dwrCaller.addDataConverter("queryTableInstanceByTableId", instConvert);

var initPage = function()
{
	 //表类实例查询
    var base = getBasePath();
    //操作列按钮
    var instButtons = {
        instBtn1:{name:"instBtn1",text:"字段数据分析",imgEnabled:base + "/meta/resource/images/addGroup.png",
            imgDisabled:base + "/meta/resource/images/addGroup.png",onclick:function(rowData) {
                dataAnalysis(rowData);
            }},
        instBtn2:{name:"instBtn2",text:"分区数据状况",imgEnabled:base + "/meta/resource/images/editGroup.png",
            imgDisabled:base + "/meta/resource/images/editGroup.png",onclick:function(rowData) {
                partStatus(rowData);
            }}
    };
    var buttonRoleCol = ["instBtn1", "instBtn2"];

    window["getRoleButtonsCol"]=function(){
        var res=[];
        for(var i=0;i<buttonRoleCol.length;i++){
            res.push(instButtons[buttonRoleCol[i]]);
        }
        return res;
    };

    var tableInstanceLayout = new dhtmlXLayoutObject(document.getElementById("detail"), "2E");
    tableInstanceLayout.cells("a").hideHeader();
    var tabbar=tableInstanceLayout.cells("a").attachTabbar();
    
//    var tableInstanceLayout=tabbar.cells("tableInstance").attachLayout("2E");
    tableInstanceLayout.hideConcentrate();//隐藏缩放按钮
    tableInstanceLayout.hideSpliter();
    tableInstanceLayout.cells("a").hideHeader();
    tableInstanceLayout.cells("a").setHeight("192");
    tableInstanceLayout.cells("b").hideHeader();
    var instGrid = tableInstanceLayout.cells("a").attachGrid();
    instGrid.setHeader("实例表名,数据源,所在表空间,所属用户,记录数,最后更新日期,操作");
    instGrid.setHeaderAlign("left,center,center,center,center,center,center");
    instGrid.setInitWidthsP("15,15,15,15,10,15,15");
    instGrid.setColTypes("ro,ro,ro,ro,ro,ro,sb");
    instGrid.enableCtrlC();
    instGrid.enableResizing("true,true,true,true,true,true,true");
    instGrid.setColumnIds("tableName","dataSourceName","tableSpace","tableOwner","tableRecords","tableDate","");
    instGrid.setColAlign("left,left,center,left,center,left,center");
    instGrid.attachEvent("onRowSelect", viewFChar);
    instGrid.init();
    instGrid.defaultPaging(10);
    instGrid.load(dwrCaller.queryTableInstanceByTableId, "json");
    tableInstanceLayout.cells("b").attachObject($("_charForm"));
    dwrCaller.executeAction("queryChartData",0,tableId,tableVersion,function(data){
        var chart = new FusionCharts(getBasePath()+"/portal/resource/swf/FusionCharts/MSLine.swf","ChartId","1111",$("chartdiv").offsetHeight+"");
        chart.setDataXML(data[0]);
        chart.render("chartdiv");
        $("chartRight").innerHTML="";
    });
}
    
/**------------------------------表类实例 begin----------------------------------*/

/**
 * 表实例分区图
 *
 * @param id
 *
 * 取得Form 的Data例子：
 * var tableData = Tools.getFormValues("_baseInfoForm");
    //表状态属于修改状态。
    tableData.tableState = 1;
    //表状态属于0
    tableData.tableVersion = 1;
    tableData.tableTypeId=2;//维度类型。
 *
 */
var viewFChar=function(id){
//    $("chartdiv").innerHTML="AAA_"+id;
    dwrCaller.executeAction("queryChartData",id,tableId,tableVersion,function(data){
        var chart = new FusionCharts(getBasePath()+"/portal/resource/swf/FusionCharts/MSLine.swf","ChartId","1111",$("chartdiv").offsetHeight+"");
        chart.setDataXML(data[0]);
        chart.render("chartdiv");
        $("chartRight").innerHTML=data[1];
        tableInstId = id;
    });
};

/**
 * 本地网线段显示
 * @param data
 */
var localSelected=function(data){
    var localData = Tools.getFormValues("_charForm");
    localData["tableId"]=tableId;
    localData["tableInstId"]=tableInstId;
    dwrCaller.executeAction("changeChartData",localData,function(data){
        var chart = new FusionCharts(getBasePath()+"/portal/resource/swf/FusionCharts/MSLine.swf","ChartId","1111",$("chartdiv").offsetHeight+"");
        chart.setDataXML(data);
        chart.render("chartdiv");
    });
};
//FusionCharts显示改变时间
dwrCaller.addAutoAction("changeChartData","TableFusionChartsAction.changeChartData");
//FusionCharts显示事件
dwrCaller.addAutoAction("queryChartData","TableFusionChartsAction.queryChartData",{
	dwrConfig:true,
    isShowProcess:false
});

var transToNone=function(value){
	if(value==""||value==null){
		return "无";
	}else{
		return value;
	}
}

var sql = "";
/**
 * 字段数据分析
 * // 实现
 * @param rowData
 */

    //注册表名查询action
var _dwrCallTableName = new biDwrCaller({
    queryTableName:{methodName:"TableViewAction.queryTableName",
        converter:new dhtmlxComboDataConverter({
            valueColumn:"tableInstId",textColumn:"tableName",isAdd:true
        })}
});

/**
 * 数据转换器，与表格列相同
 * @param {Object} id
 */
var partStatusConvertConfig = {
    idColumnName:"tableInstDataId",
    filterColumns:["partition","subpartition","dataCycleNo","zoneName","stateDate","tableInstDataState"]
}

//注册数据转换器
var parStatusConfig = new dhtmxGridDataConverter(partStatusConvertConfig);

//注册后台action,按表ID查询区分状态
dwrCaller.addAutoAction("loadPartStartus","TableViewAction.queryTablePartStatueById");
dwrCaller.addDataConverter("loadPartStartus",parStatusConfig);//查询的数据按标准形式转换并显示


var dataAnalysis = function(rowData){
	var _instanTableName = rowData.data[0];
	var _parentTableName = tableName;
	var _tableOwner = rowData.data[3];

	var dataAnaltsisConverConfig = {
	    idColumnName:"colId",
	    filterColumns:["","colName","_buttons"]
	}
	
	//注册数据转换器
	var dataAnalysisConfig = new dhtmxGridDataConverter(dataAnaltsisConverConfig);
		
	//注册后台action,按表ID查询区分状态
    dwrCaller.addAutoAction("loadDataAnaltsis","TableViewAction.queryDataAnalysisById",
            function(data){
//				alert(data);
            });
    dwrCaller.addDataConverter("loadDataAnaltsis",dataAnalysisConfig);//查询的数据按标准形式转换并显示
    
	//注册后台action,根据实例表ID查询所有列名
	dwrCaller.addAutoAction("loadDataAnaClos","TableViewAction.queryDataClosBySql");

	//	创建窗口
    var dhxWindow = new dhtmlXWindows();	//实例dhtml窗口组建
    var loadPartParam = new biDwrMethodParam();
    dhxWindow.createWindow("dataAnalysisWindow",0,0,650,350);	//组件ID，宽度、高度
    var dataAnalysisWindow = dhxWindow.window("dataAnalysisWindow");	//初始化窗口参数
    dataAnalysisWindow.setModal(true);
    dataAnalysisWindow.setDimension(650,350);
    dataAnalysisWindow.center();
    dataAnalysisWindow.denyResize();
    dataAnalysisWindow.denyPark();
    dataAnalysisWindow.setText('字段数据分析');
    dataAnalysisWindow.keepInViewport(true);
    //隐藏最小化和放大缩小按钮
    dataAnalysisWindow.button("minmax1").hide();
    dataAnalysisWindow.button("park").hide();
    dataAnalysisWindow.button("stick").hide();
    dataAnalysisWindow.button("sticked").hide();
    dataAnalysisWindow.show();
 
    //创建布局
    var dataAnalysisLayOut = new dhtmlXLayoutObject(dataAnalysisWindow,"2E")
	dataAnalysisLayOut.cells("a").hideHeader(true);	//隐藏布局头
    dataAnalysisLayOut.cells("b").hideHeader(true);
    dataAnalysisLayOut.cells("a").setHeight(144);	//设置布局A的高度
    dataAnalysisLayOut.cells("a").fixSize(false,false); //特定布局各自的宽度和高度
    dataAnalysisLayOut.hideSpliter();//移除分界边框。
    dataAnalysisLayOut.hideConcentrate();
    
   	//布局A中添加表格
    var dataAnalysisGird = dataAnalysisLayOut.cells("a").attachGrid();
    dataAnalysisGird.setHeader("{#checkBox},显示字段,函数（双击选择）")
    dataAnalysisGird.setInitWidthsP("5,50,45");	//列宽度
    dataAnalysisGird.setColAlign("center,left,left");	//列的对其方式
    dataAnalysisGird.setHeaderAlign("center,center,center");	//头的对其方式
    dataAnalysisGird.setColTypes("ch,ro,coro");	//修改方式：RO只读
    dataAnalysisGird.setColSorting("na,na,na");	//排序类型
    dataAnalysisGird.setColumnIds("","colName","functionName");	//列的ID
    dataAnalysisGird.enableCtrlC();
    dataAnalysisGird.enableMultiselect(true);
    dataAnalysisGird.enableSelectCheckedBoxCheck(1);
	dataAnalysisGird.setColumnCustFormat(2, transToNone);//第二列进行转义
	dataAnalysisGird.getCombo(2).put("none","无");
	dataAnalysisGird.getCombo(2).put("sum","SUM-求和");
	dataAnalysisGird.getCombo(2).put("max","MAX-最大值");
	dataAnalysisGird.getCombo(2).put("min","MIN-最小值");
	dataAnalysisGird.getCombo(2).put("count","COUNT-数量");
	dataAnalysisGird.getCombo(2).put("avg","AVG-平均值");
    dataAnalysisGird.init();
    dataAnalysisGird.load(dwrCaller.loadDataAnaltsis+"?tableId="+tableId,"json");	//注册action
    
    /**
     * 自定义窗口
     * @param {Object} rowData
     */
    var sqlExecuteWin = dhx.html.create("div");
    sqlExecuteWin.innerHTML = '<table style="width:100%"><thead>' +
						    '<tr><input type="button" value="生成SQL" onclick="getSelectCoun()" class="but" style="margin-left: 85%"/></tr></thead>' +
						    '<tbody><tr><td style="width: 10%;">可执行sql</td><td style="width: 90%;"><textarea id="sqlInput_id" rows="6" style="width: 90%; height: 80%" readonly="readonly"></textarea></td></tr></tbody>' +
						    '<tfoot><tr><td></td><td align="right"><input type="button" value=" 确认执行" onclick="isExecuteSql()" class="but"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
						    '<input type="button" value="关闭" onclick="closeWin()" class="but"/></td></tr></tfoot>' +
						    '</table>';
   sqlExecuteWin.style.height="100%";
   sqlExecuteWin.style.width="100%";
   dataAnalysisLayOut.cells("b").attachObject(sqlExecuteWin)
   
   getSelectCoun = function (){
	   //获取选中表格行号
	 var selectCoun =  dataAnalysisGird.getCheckedRows(0);
	 if(selectCoun == "" || selectCoun == 0 || selectCoun == null)
	 {
		dhx.alert("请选择一行进行数据分析！"); 
	 }
	 else
	 {
		 getDataAnalysisSql(selectCoun);
	 }
   }
   
   var getDataAnalysisSql = function (selectCoun)
   {
	   //确保每次生成的SQL是最新的。
	   sql = "";
	   var fieldName = "";	//选中的字段
	   var funName = ""
	   var sqlTemp = "";
	   var fieldTemp = "";
	   for(var i = 0;i < selectCoun.split(',').length;i++){
		   //获取选中行的字段值
		  fieldName =  (dataAnalysisGird.getRowData(selectCoun.split(',')[i])).data[1];
		  //获取选中行数的下拉列表值
		  funName = dataAnalysisGird.cells(selectCoun.split(',')[i],2).getValue();
		  if(funName == "" || funName == "none" || funName == "无"){
			  sqlTemp += fieldName;
		  }
		  else{
			   sqlTemp += funName+"("+fieldName+")";
		  }
		    sqlTemp += ",";
	   
		   fieldTemp += "A."+fieldName;
		   fieldTemp += ",";
	   }
	   sql = "SELECT "+sqlTemp.substring(sqlTemp.length-1,-1)+" FROM "+_tableOwner+"."+_instanTableName+" A GROUP BY "+fieldTemp+"";
	   sql = sql.substring(sql.length-1,-1);
//	   sql = "SELECT count(col_name),sum(col_size) FROM META_TABLE_COLS  A GROUP BY COL_NAME,COL_SIZE";
	   var inputId = document.getElementById("sqlInput_id").value = sql;//.substring(sql.length-1,-1);
   }
   
   isExecuteSql = function(){
	   if(sql == ""){
			 dhx.alert("请先生成需要执行的SQL！");
			 return;
		}else{
		     dhx.confirm("此SQL执行可能占用较多资源，确认执行？",function(r){
	   			if(r){
				    dwrCaller.executeAction("loadDataAnaClos",sql,tableId,tableVersion,function(data){
				    	dwrCaller.addAutoAction("queryDataClos","TableViewAction.queryDataClos",sql,tableId,tableVersion);
				    	tableBasicInfo = data;
				    	//列名
				    	 var colNames = tableBasicInfo["colNames"];
						 colNames = colNames.toString().substring(colNames.length-1,-1);//截掉最后一个逗号
				    	//字段转换
				    	var _instConfig = {
				    		filterColumns:tableBasicInfo["colIds"]};
				    	//注册action
				    	dwrCaller.addDataConverter("queryDataClos",new dhtmxGridDataConverter(_instConfig));
				    	//表头
						var tableHead = tableBasicInfo["tableHead"];
						//对其方式
						var alignType = tableBasicInfo["alignType"];
						alignType = alignType.toString().substring(alignType.length-1,-1);
						var onlyRead = tableBasicInfo["onlyRead"];
						onlyRead = onlyRead.toString().substring(onlyRead.length-1,-1);
						//只读
				    	if("SQLERROR" == data["ERROR"]){
				    		dhx.alert("执行SQL出错！ 出错信息："+data["ERRORMSG"]);
				    		return;
				    	}else{
				    	dataAnalysisWindow.close();
				    	var _dhxWindow = new dhtmlXWindows();	//实例dhtml窗口组建
					    _dhxWindow.createWindow("showdataAnalysisWin",0,0,650,350);	//组件ID，宽度、高度
					    var showdataAnalysisWin = _dhxWindow.window("showdataAnalysisWin");	//初始化窗口参数
					    showdataAnalysisWin.setModal(true);
					    showdataAnalysisWin.setDimension(650,500);
					    showdataAnalysisWin.center();
					    showdataAnalysisWin.denyResize();
					    showdataAnalysisWin.denyPark();
					    showdataAnalysisWin.setText('实例数据查询');
					    showdataAnalysisWin.keepInViewport(true);
					    //隐藏最小化和放大缩小按钮
					    showdataAnalysisWin.button("minmax1").hide();
					    showdataAnalysisWin.button("park").hide();
					    showdataAnalysisWin.button("stick").hide();
					    showdataAnalysisWin.button("sticked").hide();
					    showdataAnalysisWin.show();
				    	
					      //创建布局
					    var showDataLayOut = new dhtmlXLayoutObject(showdataAnalysisWin,"2E")
						showDataLayOut.cells("a").hideHeader(true);	//隐藏布局头
					    showDataLayOut.cells("b").hideHeader(true);
					    showDataLayOut.cells("a").setHeight(50);	//设置布局A的高度
					    showDataLayOut.cells("a").fixSize(false,false); //特定布局各自的宽度和高度
					    showDataLayOut.hideSpliter();//移除分界边框。
					    showDataLayOut.hideConcentrate();
	    
					        //创建查询表单
					    var showDataQueryForm = showDataLayOut.cells("a").attachForm([
					    	{type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
					    	{type:"label",label:"表类名："+_parentTableName,name:"tableName"},
					        {type:"newcolumn"},
					        {type:"label",label:"实例表："+_instanTableName,name:"instanTable",inputWidth:120}
				    	]);
	    				
    
					    var showDataGird = showDataLayOut.cells("b").attachGrid();
							showDataGird.setHeader(tableHead)
						    showDataGird.setColAlign(alignType);	//列的对其方式
						    showDataGird.setHeaderAlign(alignType);	//头的对其方式
//						    showDataGird.setColTypes(onlyRead);	//修改方式：RO只读
						    showDataGird.setColumnIds(colNames);	//列的ID
						    showDataGird.enableCtrlC();
						    showDataGird.enableMultiselect(true);
						    showDataGird.init();
						    showDataGird.load(dwrCaller.queryDataClos,"json");
						    }
				    });
	   			}
    	     });
		}
   }
   //关闭窗口
   closeWin = function(){
	   dataAnalysisWindow.close();
   }
}
	
/**
 * 分区数据状况
 * @param rowData
 */
var partStatus = function(rowData){
	var _tableInstId = rowData.id;
	var _parentTableName = tableName;
	var _instanTableName = rowData.data[0];

	//创建分区状态窗口
    var dhxWindow = new dhtmlXWindows();	//实例dhtml窗口组建
    var loadPartParam = new biDwrMethodParam();
    dhxWindow.createWindow("partStatusWindow",0,0,650,400);	//组件ID，宽度、高度
    var partStatusWindow = dhxWindow.window("partStatusWindow");	//初始化窗口参数
    partStatusWindow.setModal(true);
    partStatusWindow.setDimension(650,400);
    partStatusWindow.center();
    partStatusWindow.denyResize();
    partStatusWindow.denyPark();
    partStatusWindow.setText('分区数据状态');
    partStatusWindow.keepInViewport(true);
    //隐藏最小化和放大缩小按钮
    partStatusWindow.button("minmax1").hide();
    partStatusWindow.button("park").hide();
    partStatusWindow.button("stick").hide();
    partStatusWindow.button("sticked").hide();
    partStatusWindow.show();
    partStatusWindow.attachEvent("onClose", function(){
        queryPartStatusForm.getCombo("instanTable").closeAll();
        return true;
    });
    
    //分区窗口布局。添加查询表单
    var partStatusLayout = new dhtmlXLayoutObject(partStatusWindow,"2E");	//创建布局，分成2部分
    partStatusLayout.cells("a").hideHeader(true);	//隐藏布局头
    partStatusLayout.cells("b").hideHeader(true);
    partStatusLayout.cells("a").setHeight(30);	//设置布局A的高度
    partStatusLayout.cells("a").fixSize(false,true); //特定布局各自的宽度和高度
    partStatusLayout.hideSpliter();//移除分界边框。
    partStatusLayout.hideConcentrate();

    //为分区窗口布局A添加查询表单
    var queryPartStatusForm = partStatusLayout.cells("a").attachForm([
    	{type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
    	{type:"label",label:"表类名："+_parentTableName,name:"tableName"},
        {type:"newcolumn"},
        {type:"combo",label:'<span style="margin-left:5px;font:bold 12px">实例表：</span>',name:"instanTable",inputWidth:160,labelWidth:40},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询",offsetLeft :20},
        {type:"hidden",name:"tableInstId"}
    	]);

    //为查询表单添加点击事件，执行action中的查询方法
    queryPartStatusForm.attachEvent("onButtonClick", function(id){
    	//获取下拉框选中的value
    	var instanTableId = queryPartStatusForm.getCombo("instanTable").getSelectedValue();
        if(id=="query"){
            //进行数据查询。
            partStatusGirid.clearAll();
            var param = new biDwrMethodParam();
            param.setParam(0,instanTableId);
            partStatusGirid.load(dwrCaller.loadPartStartus+param,"json");
        }
    });
    var instCombo = queryPartStatusForm.getCombo("instanTable");
    instCombo.enableFilteringMode(true);

    //想分区窗口布局B中添加表格
    var partStatusGirid = partStatusLayout.cells("b").attachGrid();
    partStatusGirid.setHeader("分区,子分区,数据周期值,数据地域,最后更新日期,数据状态");	//表头
    partStatusGirid.setInitWidthsP("20,15,15,15,25,10");	//列宽度
    partStatusGirid.setColAlign("left,center,center,center,center,center");	//列的对其方式
    partStatusGirid.setHeaderAlign("left,center,center,center,center,center");	//头的对其方式
    partStatusGirid.setColTypes("ro,ro,ro,ro,ro,ro");	//修改方式：RO只读
    partStatusGirid.setColSorting("na,na,na,na,na,na");	//排序类型
    partStatusGirid.enableMultiselect(true);		//设置表格单选或者多选
    partStatusGirid.setColumnIds("partition","subpartition","dataCycleNo","zoneName","stateDate","tableInstDataState");	//列的ID
    partStatusGirid.enableCtrlC();
    partStatusGirid.init();
    partStatusGirid.setColumnCustFormat(5,tablePartState);//对状态列进行转义
    partStatusGirid.defaultPaging(10);
    var param = new biDwrMethodParam();
    param.setParam(0,_tableInstId);
    partStatusGirid.load(dwrCaller.loadPartStartus+param,"json");	//注册action

    //加载表名下拉列表
    var sysInit = false;
    queryPartStatusForm.getCombo("instanTable").loadXML(_dwrCallTableName.queryTableName+"?tableId="+tableId, function() {
        sysInit = true;
        queryPartStatusForm.setFormData({instanTable:_instanTableName});
    });
}
/**------------------------------表类实例 end----------------------------------*/
dhx.ready(initPage);