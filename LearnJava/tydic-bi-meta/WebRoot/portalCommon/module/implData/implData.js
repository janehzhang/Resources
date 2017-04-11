
/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user = getSessionAttribute("user");
var dwrCaller = new biDwrCaller();
dwrCaller.addAutoAction("importData", "ImplDataAction.importData");
var init = function(){ 
    var applyWindow = new dhtmlXLayoutObject(document.body,"1C");
	var applyWindows = applyWindow.cells("a");
	    applyWindows.setText("数据导入");
	    
    //加载查询表单
    var queryform = applyWindows.attachForm([
        {type:"setting",position: "label-left", labelWidth: 150, inputWidth: 150},
        {type : "calendar",label : "开始时间：",name : "startDate",dateFormat : "%Y-%m-%d",weekStart : "7"
            ,value : firstDay(),readonly:"readonly"
        },
        {type:"newcolumn"},
        {type : "calendar",label : "结束时间：",name :  "endDate",dateFormat : "%Y-%m-%d", weekStart : "7"
            ,value : endDay(),readonly:"readonly"
        },
        {type:"newcolumn"},
        {type:"button",name:"implData",value:"数据导入"}
    ]);
    var startDate = queryform.getCalendar('startDate');
    var endDate = queryform.getCalendar('endDate');
    startDate.loadUserLanguage('zh');
    endDate.loadUserLanguage('zh');
    
    
	//将未来的日期设定为不可操作
	var today = new Date();
	var tomarrow = new Date();
	tomarrow.setDate(tomarrow.getDate());
	startDate.setInsensitiveRange(tomarrow, null);
	endDate.setInsensitiveRange(tomarrow, null);

	//日历改变事件，使startDate不晚于endDate，endDate不早于startDate
	startDate.attachEvent("onClick", function(date) {
		endDate.setSensitiveRange(date, today);
	});
	endDate.attachEvent("onClick",   function(date) {
		date.setDate(date.getDate()+1);
		startDate.setInsensitiveRange(date, null);
	});
    
    //事件处理
    queryform.attachEvent("onButtonClick", function(id){
        if(id=="implData"){
        	    //批量生成数据。
    			dhx.showProgress("正在执行数据导入");
            	ImplDataAction.importData(queryform.getFormData(),function(rs)
            	{
            		dhx.closeProgress();
            		if (rs) 
            		{
		                dhx.alert("数据导入成功！");
		            } else 
		            {
		                dhx.alert("数据导入失败，请重试！");
		            }
            	});
        	
        }
    });
}

//得到本月第一天 
var firstDay = function(){ 
	var Nowdate=new Date(); 
	var MonthFirstDay=(new Date(Nowdate.getFullYear(),Nowdate.getMonth(),1)); 
	return MonthFirstDay; 
}
var endDay = function(){ 
	var Nowdate=new Date(); 
	var MonthFirstDay=(new Date(Nowdate.getFullYear(),Nowdate.getMonth(),Nowdate.getDate()-1)); 
	return MonthFirstDay; 
}
dhx.ready(init);