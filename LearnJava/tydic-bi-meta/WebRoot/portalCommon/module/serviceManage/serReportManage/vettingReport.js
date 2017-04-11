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
    	      var str ="";
    	       if(rowData["status"]=="1"){
	    		  str = "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=spReport("+rowData["id"]+","+rowData["makeId"]+")>审 核</a></span>";
	    		}else{
	    		  str =	"<span style='padding-left:5px;'>审 核</span>";
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
var init = function(){
	var reportLayout = new dhtmlXLayoutObject(document.body, "2E");
    reportLayout.cells("a").setText("审核查询");
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
        {type:"button",name:"query",value:"查询"}
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
    dwrCaller.addAutoAction("loadReport", "WriteReportAction.queryVettintReports",loadRoleParam, function(data){});
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
        if (id == "query") {
            mygrid.clearAll();
            mygrid.load(dwrCaller.loadReport, "json");
        }
    });
}



//审核报告
var spReport=function(id,makeId){
   WriteReportAction.queryMakeReportById(id,makeId,function(repdata){
		       //初始化新增弹出窗口。
			    var dhxWindow = new dhtmlXWindows();
			    dhxWindow.createWindow("addWindow", 0, 0, 250, 220);
			    var addWindow = dhxWindow.window("addWindow");
			    addWindow.setModal(true);
			    addWindow.stick();
			    addWindow.setDimension(550, 500);
			    addWindow.center();
			    addWindow.setPosition(addWindow.getPosition()[0],addWindow.getPosition()[1]-100);
			    addWindow.denyResize();
			    addWindow.denyPark();
			    addWindow.button("minmax1").hide();
			    addWindow.button("park").hide();
			    addWindow.button("stick").hide();
			    addWindow.button("sticked").hide();
			    addWindow.setText("审核报告");
			    addWindow.keepInViewport(true);
			    addWindow.show();
				var monthId = "";
	            var title = "";
	            var gzld="none";     
				var gzlid="none";     
				var gzjy="none";       
				var zgqk="none"; 
				var gzldValue="";     
				var gzlidValue="";     
				var gzjyValue="";       
				var zgqkValue="";
				var affixaddress="";
				var affixname="没有附件";
		    if(repdata != null){
	    	      if(repdata["MONTHID"] != null && repdata["MONTHID"] != "null")
	    		     monthId = repdata["MONTHID"];
	    	      if(repdata["TITLE"] != null && repdata["TITLE"] != "null")
	    		     title = repdata["TITLE"];
	    	      if(repdata["GZLD"] != null && repdata["GZLD"] != "null")
	    		     gzld = repdata["GZLD"]==0?"none":"block";
	    	      if(repdata["GZLID"] != null && repdata["GZLID"] != "null")
	    		     gzlid = repdata["GZLID"]==0?"none":"block";
	    	      if(repdata["GZJY"] != null && repdata["GZJY"] != "null")
	    		     gzjy = repdata["GZJY"]==0?"none":"block";
	    	      if(repdata["ZGQK"] != null && repdata["ZGQK"] != "null")
	    		     zgqk = repdata["ZGQK"]==0?"none":"block";
	    	      if(repdata["GZLD_VALUE"] != null && repdata["GZLD_VALUE"] != "null")
	    		     gzldValue= repdata["GZLD_VALUE"];
	    	      if(repdata["GZLID_VALUE"] != null && repdata["GZLID_VALUE"] != "null")
	    		     gzlidValue= repdata["GZLID_VALUE"];
	    	      if(repdata["GZJY_VALUE"] != null && repdata["GZJY_VALUE"] != "null")
	    		     gzjyValue= repdata["GZJY_VALUE"];
	    	      if(repdata["ZGQK_VALUE"] != null && repdata["ZGQK_VALUE"] != "null")
	    		     zgqkValue= repdata["ZGQK_VALUE"];
	    	      if(repdata["AFFIXADDRESS"] != null && repdata["AFFIXADDRESS"] != "null")
	    		     affixaddress= repdata["AFFIXADDRESS"];
	    	      if(repdata["AFFIXNAME"] != null && repdata["AFFIXNAME"] != "null")
	    		     affixname= repdata["AFFIXNAME"];
	    	}
 var writeFormData=[
				{type:"block",offsetTop:15,list:[
			        {type:"input",offsetLeft:40,label:"月  份：", inputWidth: 392,name : "monthId",value:""+monthId+"", readonly:"readonly"}
			    ]},
			    {type:"block",list:[
			        {type:"input",offsetLeft:40,label:"标  题：",inputWidth: 392,name:"title", value:""+title+"",readonly:"readonly"}
			    ]},
			    {type:""+gzld+"", list:[
			        {type:"input",offsetLeft:40,rows:4,label:"服务热点：",inputWidth: 370,name:"gzld",value:""+gzldValue+"",readonly:"readonly"}
			    ]},
			    {type:""+gzlid+"",list:[
			        {type:"input",offsetLeft:40,rows:4,label:"服务亮点：",inputWidth: 370,name:"gzlid",value:""+gzlidValue+"",readonly:"readonly"}
			    ]},
			    {type:""+gzjy+"",list:[
			        {type:"input",offsetLeft:40,rows:4,label:"工作建议：",inputWidth: 370,name:"gzjy",value:""+gzjyValue+"",readonly:"readonly"}
			    ]},
			    {type:""+zgqk+"",list:[
			        {type:"input",offsetLeft:40,rows:4,label:"整改情况：",inputWidth: 370,name:"zgqk",value:""+zgqkValue+"",readonly:"readonly"}
			    ]},
	            {type:"block",className:"blockStyle",list:[
                  {type:"label",offsetLeft:40,label:"下载附件：<a href=\"#\" onclick=\"downloadattrs('"+affixaddress+"')\">"+affixname+"</a>",  labelWidth:370}
                ]},
			    {type:"block",list:[
			        {type:"input",offsetLeft:40,rows:4,label:"填写意见：",inputWidth: 200,name:"agree"}
			    ]},
			    {type:"block",offsetTop:10,list:[
				        {type:"button",label:"同意",name:"agree",  value:"同意",offsetLeft:180},
				        {type:"newcolumn"},
				        {type:"button",label:"同意",name:"disAgree",value:"不同意",offsetLeft:10},
				        {type:"newcolumn"},
				        {type:"button",label:"关闭",name:"close", value:"关闭",offsetLeft:10}
			    ]},
			    
			    {type:"hidden",name:"makeId",value:makeId},
			    {type:"hidden",name:"id",value:id}
			];
	  var nextForm = addWindow.attachForm(writeFormData);
	   //查询表单事件处理
     nextForm.attachEvent("onButtonClick", function(id) {
    	 //同意
    	 if (id == "agree") {
               var data = nextForm.getFormData();
                WriteReportAction.agreeReport(data,function(rs){
                    if(rs){
                        dhx.alert("审核成功");
			            addWindow.close();
			            dhxWindow.unload();
                        mygrid.clearAll();
                        mygrid.load(dwrCaller.loadReport,"json");
                    }else{
                        dhx.alert("对不起，审核出错，请重试！");
                    }
                });
        }
        //不同意
        if (id == "disAgree") {
               var data = nextForm.getFormData();
                WriteReportAction.disAgreeReport(data,function(rs){
                    if(rs){
                        dhx.alert("审核成功");
			            addWindow.close();
			            dhxWindow.unload();
                        mygrid.clearAll();
                        mygrid.load(dwrCaller.loadReport,"json");
                    }else{
                        dhx.alert("对不起，审核出错，请重试！");
                    }
                });
        }
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
dhx.ready(init);