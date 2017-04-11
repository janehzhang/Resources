/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user = getSessionAttribute("user");//当前用户
var loadRoleParam = new biDwrMethodParam();// Action参数设置。
var dwrCaller=new biDwrCaller();
var base = getBasePath();


/**
 * Report表Data转换器
 */
var reportConvertConfig = {
    idColumnName:"id",
    filterColumns:["monthId","zoneName","title","gzld","gzlid","gzjy","zgqk","createName","status","LINK"],
	cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
	    if(columnName == 'LINK'){
	    		var str = "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=detail("+rowData["id"]+")>查看详细</a></span>";
	    		return str;
	     }
	        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
	   }
}
var reportDataConverter = new dhtmxGridDataConverter(reportConvertConfig);

/**
 * 初始化页面加载方法
 */
var init = function(){
	var reportLayout = new dhtmlXLayoutObject(document.body, "2E");
    reportLayout.cells("a").setText("报告查询");
    reportLayout.cells("b").hideHeader();
    reportLayout.cells("a").setHeight(70);
    reportLayout.cells("a").fixSize(false, true);
    reportLayout.hideConcentrate();
    reportLayout.hideSpliter();//移除分界边框。
    //加载查询表单
    var queryform = reportLayout.cells("a").attachForm([
        {type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
        {type:"calendar",label:"月份：",name : "monthId", dateFormat : "%Y-%m",readonly:"readonly"},
        {type:"newcolumn"},
        {type:"input",   label:"地区：",name:"zone", inputHeight:17,inputWidth: 150},
        {type:"newcolumn"},
        {type:"input",   label:"标题：",name:"title",inputHeight:17,validate:"MaxLength[64]"},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"},
        {type:"newcolumn"},
        {type:"button",name:"export",value:"导出excel"}
    ]);
    var monthDate = queryform.getCalendar('monthId');
        monthDate.loadUserLanguage('zh');
     //加载地域树   
     loadZoneTreeChkBox('1',queryform);
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
    dwrCaller.addAutoAction("loadReport", "WriteReportAction.queryLookReports",loadRoleParam, function(data){});
    dwrCaller.addDataConverter("loadReport", reportDataConverter);
    //添加datagrid
    mygrid = reportLayout.cells("b").attachGrid();
    mygrid.setHeader("月份,地区,标题,服务热点,服务亮点,工作建议,整改情况,填报人,状态,操作");
    mygrid.setInitWidthsP("10,10,10,10,10,10,10,10,10,10");
    mygrid.setColAlign("left,center,left,center,center,center,center,center,center,center");
    mygrid.setHeaderAlign("left,center,center,center,center,center,center,center,center,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
    mygrid.setColSorting("na,na,na,na,na,na,na,na,na,na");
    mygrid.setColumnCustFormat(8, writeReportState);//第二列进行转义
    mygrid.enableResizing("true,true,true,true,true,true,true,true,true,true");
    mygrid.init();
    mygrid.defaultPaging(20);
    //数据加载
    mygrid.load(dwrCaller.loadReport, "json");
    
    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {//查询
            mygrid.clearAll();
            mygrid.load(dwrCaller.loadReport, "json");
        }
        if(id =="export"){
        	impExcel(queryform);
        }
    });
}


//查看详情
var detail=function(id){
	 WriteReportAction.getWriteReportById(id,function(repdata){
	            //初始化新增弹出窗口
			    var dhxWindow = new dhtmlXWindows();
			    dhxWindow.createWindow("addWindow", 0, 0, 250, 220);
			    var addWindow = dhxWindow.window("addWindow");
			    addWindow.setModal(true);
			    addWindow.stick();
			    addWindow.setDimension(550, 470);
			    addWindow.center();
			    addWindow.setPosition(addWindow.getPosition()[0],addWindow.getPosition()[1]-100);
			    addWindow.denyResize();
			    addWindow.denyPark();
			    addWindow.button("minmax1").hide();
			    addWindow.button("park").hide();
			    addWindow.button("stick").hide();
			    addWindow.button("sticked").hide();
			    addWindow.setText("查看报告");
			    addWindow.keepInViewport(true);
			    addWindow.show();
				var monthId = "";
	            var title = "";
				var gzld="";     
				var gzlid="";     
				var gzjy="";       
				var zgqk="";
				var createTime="";
				var createName="";
			    var affixaddress="";
				var affixname="没有附件";
		    if(repdata != null){
	    	      if(repdata["MONTHID"] != null && repdata["MONTHID"] != "null")
	    		     monthId = repdata["MONTHID"];
	    	      if(repdata["TITLE"] != null && repdata["TITLE"] != "null")
	    		     title = repdata["TITLE"];
	    	      if(repdata["GZLD"] != null && repdata["GZLD"] != "null")
	    		     gzld= repdata["GZLD"];
	    	      if(repdata["GZLID"] != null && repdata["GZLID"] != "null")
	    		     gzlid= repdata["GZLID"];
	    	      if(repdata["GZJY"] != null && repdata["GZJY"] != "null")
	    		     gzjy= repdata["GZJY"];
	    	      if(repdata["ZGQK"] != null && repdata["ZGQK"] != "null")
	    		     zgqk= repdata["ZGQK"];
	    	      if(repdata["CREATETIME"] != null && repdata["CREATETIME"] != "null")
	    		     createTime= repdata["CREATETIME"];
	    	      if(repdata["CREATENAME"] != null && repdata["CREATENAME"] != "null")
	    		     createName= repdata["CREATENAME"];
	    	      if(repdata["AFFIXADDRESS"] != null && repdata["AFFIXADDRESS"] != "null")
	    		     affixaddress= repdata["AFFIXADDRESS"];
	    	      if(repdata["AFFIXNAME"] != null && repdata["AFFIXNAME"] != "null")
	    		     affixname= repdata["AFFIXNAME"];
	    	      
	    	}
         var writeFormData=[
				{type:"block",offsetTop:15,list:[
			        {type:"label",offsetLeft:40,label:"月  份："+monthId}
			    ]},
			    {type:"block",list:[
			        {type:"label",offsetLeft:40,label:"标  题："+title}
			    ]},
			    {type:"block", list:[
			        {type:"label",offsetLeft:40,rows:4,label:"服务热点： "+gzld}
			    ]},
			    {type:"block",list:[
			        {type:"label",offsetLeft:40,rows:4,label:"服务亮点： "+gzlid}
			    ]},
			    {type:"block",list:[
			        {type:"label",offsetLeft:40,rows:4,label:"工作建议： "+gzjy}
			    ]},
			    {type:"block",list:[
			        {type:"label",offsetLeft:40,rows:4,label:"整改情况： "+zgqk}
			    ]},
			   {type:"block",list:[
			        {type:"label",offsetLeft:40,rows:4,label:"填报时间： "+createTime}
			    ]},
			   {type:"block",list:[
			        {type:"label",offsetLeft:40,rows:4,label:"填报人： "+createName}
			    ]},
	           {type:"block",className:"blockStyle",list:[
                  {type:"label",offsetLeft:40,label:"下载附件：<a href=\"#\" onclick=\"downloadattrs('"+affixaddress+"')\">"+affixname+"</a>",  labelWidth:370}
                ]},
			    {type:"block",offsetTop:10,list:[
				        {type:"button",label:"关闭",name:"close", value:"关闭",offsetLeft:240}
			    ]}
			];
	           var nextForm = addWindow.attachForm(writeFormData);
	           nextForm.attachEvent("onButtonClick", function(id) {
			        if(id == "close"){
			               addWindow.close();
                           dhxWindow.unload();
			        }
		     });
      });
}
//下载附件信息
var downloadattrs=function(attchmetPath){
	if(attchmetPath != ""){
    document.forms[0].target="hiddenFrame";
 	var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ attchmetPath;
	window.open(url,"hiddenFrame","");
	}else{
		 dhx.alert("对不起，没有附件可下载！");
	}
}

/**
 * 装载树
 */

dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPath");
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"zoneId",pidColumn:"zoneParId",
    isDycload:false,textColumn:"zoneName"
});
var zoneConverter=dhx.extend({idColumn:"zoneId",pidColumn:"zoneParId",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
//树动态加载Action
dwrCaller.addAction("querySubZone",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZone(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
var loadZoneTreeChkBox=function(selectZone,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectZone=selectZone|| global.constant.defaultRoot;
    var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)
            || global.constant.defaultRoot;
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;width: 180px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:"_zoneDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height:200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;"
    });
    div.appendChild(treeDiv);


    //移动节点位置至指定节点下。
    var target=form.getInput("zone");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableCheckBoxes(true);
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubZone);
    tree.attachEvent("onCheck",function(id,state){
        var checkedData = tree.getAllChecked();
        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
        var zones = "";
        var zonesId ="";
        for (i = 0;i< nodes.length;i++){
            if(zones == ""){
                zones =  tree.getItemText(nodes[i]).toString();
            }else{
                zones =   zones + "," +  tree.getItemText(nodes[i]).toString() ;
            }
            if(zonesId == ""){
                zonesId = nodes[i].toString();
            }else{
                zonesId = zonesId   + "," + nodes[i].toString();
            }
        }
        if(zones == 0){
            zones = "";
            zonesId = null;
        }
        form.setFormData({zone:zones,zoneId:zonesId});
    });


    tree.attachEvent("onSelect",function(id){
        tree.setCheck(id,1);
        var checkedData = tree.getAllChecked();
        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
        var zones = "";
        var zonesId ="";
        for (i = 0;i< nodes.length;i++){
            if(zones == ""){
                zones =  tree.getItemText(nodes[i]).toString();
            }else{
                zones =   zones + "," +  tree.getItemText(nodes[i]).toString() ;
            }
            if(zonesId == ""){
                zonesId = nodes[i].toString();
            }else{
                zonesId = zonesId   + "," + nodes[i].toString();
            }
        }
        if(zones == 0){
            zones = "";
            zonesId = null;
        }
        form.setFormData({zone:zones,zoneId:zonesId});
    });
    dwrCaller.executeAction("loadZoneTree",beginId,selectZone,function(data){
        tree.loadJSONObject(data);
        globZoneTree = tree;
        if(selectZone){
            tree.selectItem(selectZone); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            // div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    })
    div.style.display="none";
}
//导出EXCEL
var impExcel=function(queryform){
    var formData=queryform.getFormData();
	$('zoneId').value=formData.zoneId;
	var dateTime=formData.monthId;
	if(dateTime !="" ){
		dateTime=utcToDate(formData.monthId);
	}
	$('monthId').value=dateTime;
 	var url = base+"/portalCommon/module/serviceManage/serReportManage/impExcel/list_imp_excel.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
var utcToDate=function(utcCurrTime) {
            utcCurrTime = utcCurrTime + "";
            var date = "";
            var month = new Array();
            month["Jan"] = 1;
            month["Feb"] = 2;
            month["Mar"] = 3;
            month["Apr"] = 4;
            month["May"] = 5;
            month["Jun"] = 6;
            month["Jul"] = 7;
            month["Aug"] = 8;
            month["Sep"] = 9;
            month["Oct"] = 10;
            month["Nov"] = 11;
            month["Dec"] = 12;
            var week = new Array();
            week["Mon"] = "一";
            week["Tue"] = "二";
            week["Wed"] = "三";
            week["Thu"] = "四";
            week["Fri"] = "五";
            week["Sat"] = "六";
            week["Sun"] = "日";
            str = utcCurrTime.split(" ");
            date = str[5] + "-";
            date = date + month[str[1]];
            return date;
}

dhx.ready(init);