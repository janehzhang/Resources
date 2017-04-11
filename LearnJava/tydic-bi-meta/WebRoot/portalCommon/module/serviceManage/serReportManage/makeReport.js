/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user = getSessionAttribute("user");//当前用户
var loadRoleParam = new biDwrMethodParam();// Action参数设置。
var dwrCaller=new biDwrCaller();


/**
 * Report表Data转换器
 */
var reportConvertConfig = {
    idColumnName:"id",
    filterColumns:["","monthId","title","status","LINK"],
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
       if (columnIndex == 0) {//第一列，checkbox列
    	   return 0;
        }
       if (columnName == 'LINK') {
    	       var str ="";
    	       if(rowData["status"]=="0"){
	    		  str = "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=publish("+rowData["id"]+")>发 布</a></span>";
	    		}else{
	    		  str =	"<span style='padding-left:5px;'>发 布</span>";
	    		}
	    		return str;
	    }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}
var reportDataConverter = new dhtmxGridDataConverter(reportConvertConfig);

/**
 * 初始化页面加载方法
 */
var init = function() {
    var reportLayout = new dhtmlXLayoutObject(document.body, "2E");
    reportLayout.cells("a").setText("报告查询");
    reportLayout.cells("b").hideHeader();
    reportLayout.cells("a").setHeight(70);
    reportLayout.cells("a").fixSize(false, true);
    //reportLayout.hideConcentrate();
    //reportLayout.hideSpliter();//移除分界边框。
    //加载查询表单
    var queryform = reportLayout.cells("a").attachForm([
        {type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
        {type:"calendar",label:"月份：",name : "monthId", inputHeight:17,dateFormat : "%Y-%m",readonly:"readonly"},
        {type:"newcolumn"},
        {type:"input",   label:"标题：",name:"title",inputHeight:17,validate:"MaxLength[64]"},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"}
    ]);
    
    var monthDate = queryform.getCalendar('monthId');
        monthDate.loadUserLanguage('zh');
    queryform.defaultValidateEvent();
    //定义loadreport Action的参数来源于表单queryform数据。
    loadRoleParam.setParamConfig([
        {  index:0,type:"fun",value:function() {
            var formData=queryform.getFormData();
            formData.monthId=Tools.trim(queryform.getInput("monthId").value);
            formData.title=Tools.trim(queryform.getInput("title").value);
            return formData;
         }
        }
    ]);
   dwrCaller.addAutoAction("loadReport", "MakeReportAction.queryReports",loadRoleParam, function(data){});
   dwrCaller.addDataConverter("loadReport", reportDataConverter);
    var base = getBasePath();
    var buttons = {
        makeReport:{name:"makeReport",text:"制定报告",imgEnabled:base + "/meta/resource/images/addGroup.png",
            imgDisabled:base + "/meta/resource/images/addGroup.png",onclick:function(rowData) {
                      makeReport();
            }},
        deleteReport:{name:"deleteReport",text:"删除所选",imgEnabled:base + "/meta/resource/images/delete.png",
            imgDisabled :base + "/meta/resource/images/delete.png",onclick:function(rowData) {
                deleteReport(rowData.id);
            }}
    };
     
    // 按钮权限过滤,checkRule.
    var bottonRoleRow = ["makeReport","deleteReport"]; 
     //定义全局函数，用于获取有权限的button列表定义
    window["getButtons"] = function() {
        var res = [];
        for(var i=0;i<bottonRoleRow.length;i++){
            res.push(buttons[bottonRoleRow[i]]);
        }
        return res;
    };
    
    var buttonToolBar = reportLayout.cells("b").attachToolbar();
    var pos = 1;
    var filterButton = window["getButtons"]();
    for (var i = 0; i < filterButton.length; i++) {
        buttonToolBar.addButton(filterButton[i].name, pos++, filterButton[i].text,
                filterButton[i].imgEnabled, filterButton[i].imgDisabled);
        var button=buttonToolBar.getItemNodeById(filterButton[i].name);
        button.setAttribute("id",filterButton[i].name);
    }
   
    //添加datagrid
    mygrid = reportLayout.cells("b").attachGrid();
    mygrid.setHeader("{#checkBox},月份,标题,状态,操作");
    mygrid.setInitWidthsP("3,10,60,10,17");
    mygrid.setHeaderAlign("center,left,center,center,center");
    mygrid.setColAlign("center,left,left,center,center");
    mygrid.enableResizing("true,true,true,true,true");
    mygrid.setColTypes("ch,ro,ro,ro,ro");
    mygrid.setColSorting("na,na,na,na,na");
    mygrid.setColumnCustFormat(3, makeReportState);
    mygrid.enableMultiselect(true);
    mygrid.enableSelectCheckedBoxCheck(1);
    mygrid.enableCtrlC();
    mygrid.setColumnIds("'',monthId,title,status,''");
    mygrid.enableTooltips("true,true,true,true,true");
    mygrid.init();
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.loadReport, "json");
    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {
            mygrid.clearAll();
            mygrid.load(dwrCaller.loadReport, "json");
        }
    });
   
   //添加buttonToolBar事件
   buttonToolBar.attachEvent("onclick", function(id) {
        if (id == "makeReport") {//新增
            makeReport();
        }
        if (id == "deleteReport") {//删除角色
            var selecteddRowId = mygrid.getCheckedRows(0);
            if (selecteddRowId == null || selecteddRowId == "") {
                dhx.alert("请选择至少一行进行删除!");
                return;
            } else {
                 deleteReport(selecteddRowId);
            }
        }
        
        
     });
    
}

var makeReport= function() {
    //初始化新增弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("addWindow", 0, 0, 250, 220);
    var addWindow = dhxWindow.window("addWindow");
    addWindow.setModal(true);
    addWindow.stick();
    addWindow.setDimension(550, 450);
    addWindow.center();
    addWindow.setPosition(addWindow.getPosition()[0],addWindow.getPosition()[1]-100);
    addWindow.denyResize();
    addWindow.denyPark();
    addWindow.button("minmax1").hide();
    addWindow.button("park").hide();
    addWindow.button("stick").hide();
    addWindow.button("sticked").hide();
    addWindow.setText("制定报告");
    addWindow.keepInViewport(true);
    addWindow.show();

    //建立表单。
   var addForm = addWindow.attachForm(addFormData);
    
   var monthDate = addForm.getCalendar('monthId');
       monthDate.loadUserLanguage('zh');
   var monthDate = addForm.getCalendar('endTime');
       monthDate.loadUserLanguage('zh');
    //添加验证
    addForm.defaultValidateEvent();
    
    //事件
    addForm.attachEvent("onChange", function(id) {
       if(id=="gzld"){
		    	  if(addForm.isItemChecked("gzld")){
		    		   addForm.getInput("ld").style.display = "block";
		    	  }else{
		    		   addForm.getInput("ld").style.display = "none";
		    	  }
       }
       
       if(id=="gzlid"){
		    	  if(addForm.isItemChecked("gzlid")){
		    		   addForm.getInput("lid").style.display = "block";
		    	  }else{
		    		   addForm.getInput("lid").style.display = "none";
		    	  }
       }
       
       if(id=="gzjy"){
		    	  if(addForm.isItemChecked("gzjy")){
		    		   addForm.getInput("jy").style.display = "block";
		    	  }else{
		    		   addForm.getInput("jy").style.display = "none";
		    	  }
       }
       
       if(id=="zgqk"){
		    	  if(addForm.isItemChecked("zgqk")){
		    		   addForm.getInput("qk").style.display = "block";
		    	  }else{
		    		   addForm.getInput("qk").style.display = "none";
		    	  }
       }
       
       
    });

    //查询表单事件处理
    addForm.attachEvent("onButtonClick", function(id) {
         //保存
    	if (id == "save") {
            if(addForm.validate()){
                var data = addForm.getFormData();
                MakeReportAction.addReport(data,function(rs){
                    if(rs){
                        dhx.alert("保存成功");
                        addWindow.close();
                        dhxWindow.unload();
                        mygrid.clearAll();
                        mygrid.load(dwrCaller.loadReport, "json");
                    }else{
                        dhx.alert("对不起，保存出错，请重试！");
                    }
                });
            }
        }
    	//发布
        if(id=="submit"){
            if(addForm.validate()){
                var data = addForm.getFormData();
                MakeReportAction.publishReport(data,function(rs){
                    if(rs){
                        dhx.alert("发布成功");
                        addWindow.close();
                        dhxWindow.unload();
                        mygrid.clearAll();
                        mygrid.load(dwrCaller.loadReport, "json");
                    }else{
                        dhx.alert("对不起，发布出错，请重试！");
                    }
                });
            }
        }
        if(id == "close"){
            addWindow.close();
            dhxWindow.unload();
        }
    });
}
//删除
var deleteReport = function(rowDatas) {
    dhx.confirm("是否确认删除您所选择的记录？", function(data){
        if(data){
                  MakeReportAction.deleteReport(rowDatas,function(rs){
                    if(rs){
                        dhx.alert("删除成功");
                        mygrid.clearAll();
                        mygrid.load(dwrCaller.loadReport, "json");
                    }else{
                        dhx.alert("对不起，删除出错，请重试！");
                    }
                });
        }
    })
};

var publish = function(id){
     var data=new Object();
     data.id=id;
	MakeReportAction.publishReport(data,function(rs){
        if(rs){
            dhx.alert("发布成功!");
            mygrid.clearAll();
            mygrid.load(dwrCaller.loadReport, "json");
        }else{
            dhx.alert("对不起，发布出错，请重试！");
        }
    });
}

/**
 * 添加FORM
 */
var addFormData=[
	{type:"block",offsetTop:15,list:[
        {type:"calendar",offsetLeft:40,label:"月 份：",    inputWidth: 160,name : "monthId", dateFormat : "%Y-%m", validate:"NotEmpty" , readonly:"readonly"},
        {type:"newcolumn"},
        {type:"calendar",              label:"结束日期：",inputWidth: 160,name :  "endTime", dateFormat : "%Y-%m-%d",validate:"NotEmpty", readonly:"readonly"}
    ]},
   
    {type:"block",list:[
        {type:"input",offsetLeft:40,label:"标  题：",inputWidth: 392,name:"title",validate:"NotEmpty,MaxLength[64]"},
        {type:"newcolumn"},
        {type:"label",label:"<span style='color: red'>*</span>"}
    ]},
    
   {type:"block",list:[
    	    {type:"label",offsetLeft:25,label:'<span style="color:#000;font-size:12px;padding-left:16px;font-weight:normal"> 制定表单：</span>',labelWidth:90},
            {type:"newcolumn"},
            {type:"checkbox",className:"checkBox",name:"gzld",value:1,checked:true},
            {type:"newcolumn"},
            {type:"label",label:'<span style="color:#000;font-size:12px;margin-left:-2px;">服务热点</span>'},
            {type:"newcolumn"},
            {type:"checkbox",className:"checkBox",name:"gzlid",value:1,checked:true},
            {type:"newcolumn"},
            {type:"label",label:'<span style="color:#000;font-size:12px;margin-left:-2px;">服务亮点</span>'},
            {type:"newcolumn"},
            {type:"checkbox",className:"checkBox",name:"gzjy",value:1,checked:true},
            {type:"newcolumn"},
            {type:"label",label:'<span style="color:#000;font-size:12px;margin-left:-2px;">工作建议</span>'},
            {type:"newcolumn"},
            {type:"checkbox",className:"checkBox",name:"zgqk",value:1,checked:true},
            {type:"newcolumn"},
            {type:"label",label:'<span style="color:#000;font-size:12px;margin-left:-2px;">整改情况</span>'}
    ]},
    
    {type:"block",list:[
        {type:"input",offsetLeft:40,rows:4,label:"服务热点：",inputWidth: 370,name:"ld",  disabled:"disabled"}
    ]},
    {type:"block",list:[
        {type:"input",offsetLeft:40,rows:4,label:"服务亮点：",inputWidth: 370,name:"lid", disabled:"disabled"}
    ]},
    {type:"block",list:[
        {type:"input",offsetLeft:40,rows:4,label:"工作建议：",inputWidth: 370,name:"jy",  disabled:"disabled"}
    ]},
    {type:"block",list:[
        {type:"input",offsetLeft:40,rows:4,label:"整改情况：",inputWidth: 370,name:"qk",  disabled:"disabled"}
    ]},
    
    {type:"block",offsetTop:10,list:[
        {type:"button",label:"保存",name:"save",  value:"保存",offsetLeft:180},
        {type:"newcolumn"},
        {type:"button",label:"发布",name:"submit",value:"发布",offsetLeft:10},
        {type:"newcolumn"},
        {type:"button",label:"关闭",name:"close", value:"关闭",offsetLeft:10}
    ]}
];
dhx.ready(init);