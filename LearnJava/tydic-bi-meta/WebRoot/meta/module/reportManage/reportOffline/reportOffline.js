/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       batchImport.js
 *Description：
 *       报表管理-报表下线
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       吴喜丽
 *Date：
 *       2012-03-20
 ********************************************************/
/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var pageSize = 20;
var queryForm;
var grid;
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();
var param = new biDwrMethodParam();
param.setParamConfig([
    {
       index:0,type:"fun",value:function() {
			var data = queryForm.getFormData();
			data.reportId = reportId;
			return data;
   	   	}
    }
]);
//查询信息
dwrCaller.addAutoAction("getReportMes","ReportOfflineAction.getReportMes",param);
dwrCaller.addDataConverter("getReportMes",new dhtmxGridDataConverter({
	isFormatColumn:false,
    filterColumns:["REPORT_NAME","USER_NAMECN","OPERATE_TIME","REPORT_STATE","LINK"],
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
        return rowData.REPORT_ID;
    },
    /**
     * 获取下拉框Button的值
     * @param rowIndex
     * @param columnIndex
     * @param cellValue
     * @param rowData
     */
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
    	if(columnName == 'REPORT_NAME'){
    		var str = "";
    		if(rowData["REPORT_NAME"] == "null" || rowData["REPORT_NAME"] == null)
    			return str;
    		str = "<a href=\"javascript:openReport("+rowData["REPORT_ID"]+",'"+rowData["REPORT_NAME"]+"')\">"+rowData["REPORT_NAME"]+"</a>";
    		return str;
    	}else if(columnName == 'LINK'){
    		return "getButtons";
        }else if(columnName == 'USER_NAMECN'){
        	var str = rowData["DEPT_NAME"]+rowData["STATION_NAME"]+rowData["USER_NAMECN"];
        	if(str == "0")
        		str = "";
        	return str;
        }else if(columnName == 'REPORT_STATE'){
        	var str = "";
        	if(rowData["REPORT_STATE"] == "0")
        		str = "已下线";
        	else if(rowData["REPORT_STATE"] == "1")
        		str = "有效"
        	return str;
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}));
var openReport = function(reportId,reportName){
	openMenu(reportName+"信息","/meta/public/reportDesign/report.jsp?initType=2&rptId="+reportId,"top");
}
var executeReportMes = function(reportId,reoprtName,type){
	var p = reoprtName.indexOf(">",0);
	var s = reoprtName.indexOf("<",p);
	reoprtName = reoprtName.substring((1*p+1),s);
	openMenu(reoprtName+"信息","meta/module/reportManage/reportOffline/exeReportOffline.jsp?reportId="+reportId+"&typeId="+type,"top");
}
var buttons;
var buttonFun = function(isOffline){
	if(!isOffline){
		buttons = [
		         {name:"offlineMes",text:"下线",onclick:function(rowData) {
		               executeReportMes(rowData.id,rowData.data[0],1);
            }}];
	}else{
		buttons = [
		        {name:"queryMes",text:"查看",onclick:function(rowData) {
		                executeReportMes(rowData.id,rowData.data[0],0);
		            }},
		         ];
	}
}
/**
 * 初始化界面
 */
var init=function(){
	window.getButtons=function(a,b,c,d){
		if(c.data[3] == "已下线"){
			buttonFun(true);
		}else
			buttonFun(false);
		return buttons
	}
	var dhxLayout = new dhtmlXLayoutObject(document.body, "2E");
	dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.setText("报表下线情况查询");
    topCell.setHeight(80);
    topCell.fixSize(true,true); 
    queryForm = topCell.attachForm(
            [
                {type:"settings",position: "label-left", inputWidth: 120},
                {type:"combo",label:"报表状态：",name:"reportState",inputHeight : 22,readonly:true,options:[{value:"-1",text:"全部",selected:true},{value:"0",text:"已下线"},{value:"1",text:"正常"}]},
                {type:"newcolumn"},
                {type:"input",label:"关键字：",name:"keyWord",inputWidth:405,inputHeight : 17},
                {type:"newcolumn"},
                {type:"button",name:"queryBtn",value:"查询",offsetLeft : 0,offsetTop : 0}
            ]
    );
    queryForm.getCombo("reportState").enableFilteringMode(false);
    queryForm.attachEvent("onButtonClick", function(id) {
        if (id == "queryBtn") {
            //进行数据查询。
        	if(queryForm.validate()){
        		grid.clearAll();
            	grid.load(dwrCaller.getReportMes, "json");
        	}
        }
    });
    //查询数据表单
    var dataCell = dhxLayout.cells("b");
    dataCell.fixSize(true,false);
    dataCell.hideHeader();
    grid = dataCell.attachGrid();
    grid.setHeader("报表名称,下线操作人,下线时间,状态,操作");
    grid.setInitWidthsP("25,20,15,15,25");
    grid.setColAlign("left,left,left,left,center");
    grid.setHeaderAlign("center,center,center,center,center");
    grid.setColTypes("ro,ro,ro,ro,sb");
    grid.setColSorting("na,na,na,na,na");
    grid.enableResizing("true,true,true,true,true");
    grid.enableMultiselect(false);
    grid.enableCtrlC();
    grid.init();
    grid.defaultPaging(pageSize);
    //数据加载
    grid.load(dwrCaller.getReportMes, "json");
}
dhx.ready(init);









