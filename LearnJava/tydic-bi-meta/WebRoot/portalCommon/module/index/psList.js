/**
 * 
 * 主单待处理js文件
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
//DWR
var dwrCaller = new biDwrCaller();
var queryPsParams = new biDwrMethodParam();//查询批示信息参数
var user= getSessionAttribute("user");
var queryform;
/**
 * 初始化函数
 */
var PsInit = function() {
	//第一步，先建立一个Layout
	var logLayout = new dhtmlXLayoutObject(document.body, "2E");
	logLayout.cells("a").setText("批示处理");
	logLayout.cells("b").hideHeader();
	logLayout.cells("a").setHeight(75);
	logLayout.cells("a").fixSize(false, true);
	logLayout.hideConcentrate();
	logLayout.hideSpliter();
	//添加查询表单
    queryform = logLayout.cells("a").attachForm( [ {
        type : "setting",
        position : "label-left"
        },
        {type : "calendar",
            label : "日期从：",
            name : "startDate",
            dateFormat : "%Y-%m-%d",
            weekStart : "7",
            value: firstDay(),
            inputWidth: "80",
            readonly:"readonly"
        },
        {type : "newcolumn"},
        {type : "calendar",
            label : "至：",
            name : "endDate",
            dateFormat : "%Y-%m-%d",
            weekStart : "7",
            value : GetDateStr(1),
            inputWidth: "80",
            readonly:"readonly"
        },
        {type:"newcolumn"},
        {type : "combo",label : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;批示状态：",name : "isread",options:[{value:"",text:"全部",selected:true},{value:"1",text:"已读"},{value:"0",text:"未读"}],inputWidth:120,inputHeight:12,readonly:true},
        {type : "newcolumn"},
        {
            type : "button",
            name : "query",
            value : "查询",
            offsetLeft:10
        }
        ]); 
	var startDate = queryform.getCalendar("startDate");
	var endDate = queryform.getCalendar("endDate");	
	//将日历控件语言设置成中文
	startDate.loadUserLanguage('zh');
	endDate.loadUserLanguage('zh');
	//将未来的日期设定为不可操作
	var today = new Date();
	var tomarrow = new Date();
	tomarrow.setDate(tomarrow.getDate() + 2);
	startDate.setInsensitiveRange(tomarrow, null);
	endDate.setInsensitiveRange(tomarrow, null);
	//日历改变事件，使startDate不晚于endDate，endDate不早于startDate
	startDate.attachEvent("onClick", function(date) {
		endDate.setSensitiveRange(date, today);
	});
	endDate.attachEvent("onClick", function(date) {
		date.setDate(date.getDate()+1);
		startDate.setInsensitiveRange(date, null);
	});
	/**
	 * 查询批示信息的响应函数
	 * @param {Object} data
	 */
	dwrCaller.addAutoAction("queryPs", "PortalInstructionAction.getPsListByPara",
			queryPsParams, function(data) {
			});
	dwrCaller.addDataConverter("queryPs", new dhtmxGridDataConverter( {
		idColumnName : "id",
		filterColumns : [ "title","indexName","userName","sendTime","isread","content","reportName"],
		cellDataFormat : function(rowIndex, columnIndex, columnName, cellValue,
				rowData) {
             if(columnName=="title"){
				var str ="<a href=\"javascript:void(0)\"  onclick=\"openDetailWin('"+rowData.id+"')\" >"+cellValue+"</a>";
	    		return str;
			}
				return this._super.cellDataFormat(rowIndex, columnIndex,
						columnName, cellValue, rowData);
		}

	}));

	//设置查询参数，来自于queryform表单
	queryPsParams.setParamConfig( [ {
		index : 0,
		type : "fun",
		value : function() {
			return queryform.getFormData();
		}
	} ]);	
	//添加grid,用于将查询出来的日志记录显示出来
	logGrid = logLayout.cells("b").attachGrid();
	logGrid.setHeader("批示主题,批示指标,批示人,批示日期,批示状态");
	logGrid.setInitWidthsP("20,20,20,20,20");
	logGrid.setColAlign("center,center,center,right,center");
	logGrid.setHeaderAlign("center,center,center,center,center");
	logGrid.setColTypes("ro,ro,ro,ro,ro");
    logGrid.enableCtrlC();
	logGrid.setColSorting("na,na,na,na,na");
	logGrid.setEditable(false);
	logGrid.setColumnIds("title,indexName,userName,sendTime,isread,content,reportName");
	logGrid.enableTooltips("true,true,true,true,true");
	logGrid.init();
    logGrid.defaultPaging(20);
	logGrid.load(dwrCaller.queryPs, "json");
	//查询响应函数
	queryform.attachEvent("onButtonClick", function(id) {
		if (id == "query") {
			logGrid.clearAll();
			logGrid.load(dwrCaller.queryPs, "json");
		}
	});
};
dwrCaller.addAutoAction("psIsRead","PortalInstructionAction.psIsRead");
dwrCaller.addAutoAction("psDeal","PortalInstructionAction.psDeal");
/**
 * 查看详情
 */
var openDetailWin=function(psId){
	var rowData = logGrid.getRowData(psId);//获取行数据
    //初始化详情弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("detailWindow", 0, 0, 350, 1000);
    var detailWindow = dhxWindow.window("detailWindow");
    detailWindow.setModal(true);
    detailWindow.stick();
    detailWindow.setDimension(550, 300);
    detailWindow.center();
    detailWindow.setPosition(detailWindow.getPosition()[0],detailWindow.getPosition()[1]-50);
    detailWindow.denyResize();
    detailWindow.denyPark();
    detailWindow.button("minmax1").hide();
    detailWindow.button("park").hide();
    detailWindow.button("stick").hide();
    detailWindow.button("sticked").hide();
    detailWindow.setText("批示详情");
    detailWindow.keepInViewport(true);
    detailWindow.show(); 
    querydetail = detailWindow.attachForm( [ 
            {type:"label",offsetLeft:80,label:"报表名称："+rowData.data[6],name:"reportName"},
            {type:"label",offsetLeft:80,label:"批示日期："+rowData.data[3],name:"sendTime"},
            {type:"label",offsetLeft:80,label:"批示人："+rowData.data[2],name:"createName"},
            {type:"label",offsetLeft:80,label:"指标："+rowData.data[1],name:"indexName"},
            {type:"label",offsetLeft:80,label:"批示内容："+rowData.data[5],name:"content"},
            {type:"block",list:[
            {type:"label",offsetLeft:80,label:"处理意见："},
            {type:"input",offsetLeft:80,rows:4,inputWidth: 370,name:"dealOpinion"}
            ]},
            {type:"block",offsetTop:10,list:[
            {type:"button",label:"关闭",offsetLeft:120,name:"close",value:"关闭"},
            {type:"newcolumn"},
            {type:"button",label:"标为已读",offsetLeft:20,name:"read",value:"标为已读"},
            {type:"newcolumn"},
            {type:"button",label:"处理完成",offsetLeft:20,name:"deal",value:"处理完成"}
            ]}  
        ]);
    querydetail.setFormData( {
		"id" : psId
	});
    querydetail.attachEvent("onButtonClick", function(id) {
    	var data = querydetail.getFormData();
        if(id == "read"){        
	         //处理
	        dwrCaller.executeAction("psIsRead",data,function(data){
	            if(data.type == "error" || data.type == "invalid"){
	                dhx.alert("对不起，标为已读失败，请重试！");
	            }
	            else{
	            	dhx.alert("标为已读成功");
	            	detailWindow.close();
                    dhxWindow.unload();
                    logGrid.clearAll();
                    logGrid.load(dwrCaller.queryPs, "json");
	            }
	        })
	        
	        
        }
        if(id == "deal"){   
	         //处理
        	if(data.dealOpinion==null){
        		alert("请填写处理意见");
        		return false;
        	}else{
	        dwrCaller.executeAction("psDeal",data,function(data){
	            if(data.type == "error" || data.type == "invalid"){
	                dhx.alert("对不起，处理失败，请重试！");
	            }
	            else{
	            	dhx.alert("处理成功");
	            	detailWindow.close();
                   dhxWindow.unload();
                   logGrid.clearAll();
                   logGrid.load(dwrCaller.queryPs, "json");
	            }
	        })
        	} 
       }
        if(id == "close"){
        	detailWindow.close();
            dhxWindow.unload();
        }
    });
}
function GetDateStr(AddDayCount) {
    var dd = new Date();
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();
    return y+"-"+m+"-"+d;
}
var firstDay = function(){ 
	var Nowdate=new Date(); 
	var MonthFirstDay=(new Date(Nowdate.getFullYear(),Nowdate.getMonth(),1)); 
	return MonthFirstDay; 
}
dhx.ready(PsInit);
