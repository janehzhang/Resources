/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       import.js
 *Description：
 *       报表管理-审核日志
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       李国民
 *Date：
 *       2012-03-08
 ********************************************************/

/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var isM = 22;		//月标识
var isD = 11;		//天标识
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();

var pageSize = 20;
var queryForm;
var param = new biDwrMethodParam();
param.setParamConfig([{
       index:0,type:"fun",value:function() {
			if(dataPeriod==isD){//按日时使用控件
				if(queryForm.getInput("dataDate").value){
	            	return queryForm.getItemValue("dataDate");
				}else{
					return null;
				}
			}else{
				var year = queryForm.getItemValue("dataDate1");
				if(year!="-1"){
					var month = queryForm.getItemValue("dataDate2");
					var dataDate = new Date();
					dataDate.setYear(year);
					dataDate.setMonth(month-1);
					return dataDate;
				}else{
					return null;
				}
			}
   	   	}},{
        index:1,type:"fun",value:function(){
            return issueId+"";
        }},{
        index:2,type:"fun",value:function(){
            return dataPeriod+"";
        }
    }
]);

//查询信息
dwrCaller.addAutoAction("getViewData","AuditDataAction.getViewData",param);
dwrCaller.addDataConverter("getViewData", new dhtmxGridDataConverter({
	    idColumnName:"auditLogId",
	    filterColumns:["dataDate","auditTime","auditConclude","showOpinion","showMaxDate","link"],
	    
		/**
	     * 实现 userData，将一些数据作为其附加属性
	     */
	    userData:function(rowIndex, rowData) {
	        var userData = {};
	        return userData;
	    },
	    
	    /**
	     * 添加连接
	     */
	    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
	    	if(columnName == 'showMaxDate'){
	    		if(dataPeriod==isM){	//按月审核
	    			return "前一月";
	    		}else{
	    			return rowData["showMaxDate"];
	    		}
	    	}else if(columnName=='link'){
				var str = "<span style='padding-right:20px;'><a style='cursor:pointer;color:blue;' " +
				" onclick=openViewData("+rowData["issueId"]+",'"+rowData["dataDate"]+"','"+rowData["maxDate"]+"')>查看</a></span>";
				str += "<span style='padding-left:20px;'><a style='cursor:pointer;color:blue;' " +
				" onclick=openAuditNewData("+rowData["issueId"]+",'"+rowData["dataDate"]+"','"+rowData["maxDate"]+"')>审核数据</a></span>";
	    		return str;
	    	}
	        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
	    }
}));

openViewData = function(issueId,dataDate,maxDate){
	openMenu("查看审核信息","/meta/module/reportManage/auditData/showAuditData.jsp?issueId="+issueId+"&dataDate="+dataDate+"&dataPeriod="+dataPeriod+"&auditProp="+auditProp+"&maxDate="+maxDate,"top");
}

openAuditNewData = function(issueId,dataDate,maxDate){
	openMenu("审核数据","/meta/module/reportManage/auditData/auditNewData.jsp?issueId="+issueId+"&dataDate="+dataDate+"&dataPeriod="+dataPeriod+"&auditProp="+auditProp+"&maxDate="+maxDate,"top");
}

/**
 * 初始化界面
 */
var init=function(){
	var dhxLayout = new dhtmlXLayoutObject(document.body, "2E");
    dhxLayout.cells("a").setText("审核数据");
    dhxLayout.cells("a").setHeight(70);
    dhxLayout.cells("a").fixSize(false, true);
	dhxLayout.hideSpliter();
	dhxLayout.hideConcentrate();
	var topCell = dhxLayout.cells("a");
	if(dataPeriod==isD){
		//按日时使用控件
		queryForm = topCell.attachForm(	
			[
	            {type:"calendar",label:"数据日期：", inputHeight:17,name:"dataDate",weekStart:"7",skin: "dhx_skyblue",readonly:true},
	            {type:"newcolumn"},
		        {type:"button",name:"queryBtn",value:"查询",offsetLeft:0,offsetTop:0},
		        {type:"newcolumn"},
		        {type:"button",name:"clearBtn",value:"重置",offsetLeft:0,offsetTop:0}
		    ]
	    );
		var dataCalendar = queryForm.getCalendar("dataDate");
		dataCalendar.setDateFormat("%Y-%m-%d");
		    //将日历控件语言设置成中文
		dataCalendar.loadUserLanguage('zh');
		var today = new Date();
		var tomorrow = new Date();
		tomorrow.setDate(today.getDate()+1);
		dataCalendar.setInsensitiveRange(tomorrow,null);//设置可选最大日期为当天
	}else{
		//按月时使用下拉列表
		queryForm = topCell.attachForm(	
			[
	            {type:"combo",label:"数据日期：",name:"dataDate1",options:[{value:"-1",text:"全部"},{value:"2002",text:"2002"},
	            	{value:"2003",text:"2003"},{value:"2004",text:"2004"},{value:"2005",text:"2005"},{value:"2006",text:"2006"},
	            	{value:"2007",text:"2007"},{value:"2008",text:"2008"},{value:"2009",text:"2009"},{value:"2010",text:"2010"},
	            	{value:"2011",text:"2011"},{value:"2012",text:"2012"},{value:"2013",text:"2013"},{value:"2014",text:"2014"},
	            	{value:"2015",text:"2015"},{value:"2016",text:"2016"},{value:"2017",text:"2017"},{value:"2018",text:"2018"},
	            	{value:"2019",text:"2019"},{value:"2020",text:"2020"},{value:"2021",text:"2021"},{value:"2022",text:"2022"},
	            	{value:"2023",text:"2023"},{value:"2024",text:"2024"}],inputHeight:20,inputWidth:80,readonly:true},
	            {type:"newcolumn"},
	            {type:"combo",label:"",name:"dataDate2",inputHeight:20,inputWidth:80,readonly:true},
	            {type:"newcolumn"},
		        {type:"button",name:"queryBtn",value:"查询",offsetLeft:0,offsetTop:0}
		    ]
	    );
		
		//添加年选择事件
		queryForm.getCombo("dataDate1").attachEvent("onChange", function(){
			var val = queryForm.getCombo("dataDate1").getSelectedValue();
			queryForm.getCombo("dataDate2").clearAll(true);
			if(val != -1){
			    var options=[{value:"1",text:"1"},{value:"2",text:"2"},{value:"3",text:"3"},{value:"4",text:"4"},
			    	{value:"5",text:"5"},{value:"6",text:"6"},{value:"7",text:"7"},{value:"8",text:"8"},
	            	{value:"9",text:"9"},{value:"10",text:"10"},{value:"11",text:"11"},{value:"12",text:"12"}];
			  	queryForm.getCombo("dataDate2").addOption(options);
			  	queryForm.getCombo("dataDate2").selectOption(0);
			}
		}); 
	}
	
	queryForm.attachEvent("onButtonClick", function(id) {
        if (id == "queryBtn") {
            //进行数据查询。
        	if(queryForm.validate()){
        		grid.clearAll();
            	grid.load(dwrCaller.getViewData, "json");
        	}
        }
        if (id == "clearBtn") {
        	queryForm.getInput("dataDate").value = "";
        }
    });
	
	var middleCell = dhxLayout.cells("b");
	middleCell.hideHeader();
	var grid = middleCell.attachGrid();
	grid.setHeader("数据日期,审核时间,审核结论,提示信息,应用约定,<span>操作</span>" );
	grid.setInitWidthsP("16,16,16,16,16,20");
	grid.setColAlign("center,center,center,left,center,center");
	grid.setHeaderAlign("center,center,center,center,center,center");
	grid.enableResizing("true,true,true,true,true,true");
	grid.setColTypes("ro,ro,ro,ro,ro,ro");
	grid.setColSorting("na,na,na,na,na,na");
	grid.enableTooltips("true,true,true,true,true,true");
	grid.setEditable(false);	
	grid.init();
    grid.defaultPaging(20);
	grid.load(dwrCaller.getViewData, "json");
}

dhx.ready(init);