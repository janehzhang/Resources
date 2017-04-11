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
    filterColumns:["","monthId","title","status","LINK"],
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
       if (columnIndex == 0) {//第一列，checkbox列
    	   return 0;
        }
       if (columnName == 'LINK') {
    	       	var str = "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=detail("+rowData["id"]+")>查看详细</a></span>";
    	       if(rowData["status"]=="0"){
	    		  str += "<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=nextStep("+ rowData["id"]+","+rowData["makeId"]+")>填写上报</a></span>";
	    		}else{
	    		  str +="<span style='padding-left:5px;'>填写上报</span>";
	    		}
	    		return str;
	    }
       if (columnName != 'status') {
			var str="";
			if (rowData["endTime"] == "true" && rowData["status"]=="0") {
				str = "<span style='color:red'>" + rowData[columnName] + "</span>";
			} else {
				str = rowData[columnName];
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
    reportLayout.cells("a").setText("报告查询");
    reportLayout.cells("b").hideHeader();
    reportLayout.cells("a").setHeight(70);
    reportLayout.cells("a").fixSize(false, true);
    //加载查询表单
    var queryform = reportLayout.cells("a").attachForm([
        {type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
        {type:"calendar",label:"月份：",name : "monthId", dateFormat : "%Y-%m",readonly:"readonly"},
        {type:"newcolumn"},
        {type:"input",   label:"标题：",name:"title",inputHeight:17,validate:"MaxLength[64]"},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"}
    ]);
    
    var monthDate = queryform.getCalendar('monthId');
        monthDate.loadUserLanguage('zh');
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
   
    dwrCaller.addAutoAction("loadReport", "WriteReportAction.queryWriteReports",loadRoleParam, function(data){});
    dwrCaller.addDataConverter("loadReport", reportDataConverter);
    //添加datagrid
    mygrid = reportLayout.cells("b").attachGrid();
    mygrid.setHeader("{#checkBox},月份,标题,状态,操作");
    mygrid.setInitWidthsP("3,10,60,10,17");
    mygrid.setHeaderAlign("center,left,center,center,center");
    mygrid.setColAlign("center,left,left,center,center");
    mygrid.enableResizing("true,true,true,true,true");
    mygrid.setColTypes("ch,ro,ro,ro,ro");
    mygrid.setColSorting("na,na,na,na,na");
    mygrid.setColumnCustFormat(3, writeReportState);//第二列进行转义
    mygrid.enableMultiselect(true);
    mygrid.enableSelectCheckedBoxCheck(1);
    mygrid.enableCtrlC();
    mygrid.setColumnIds("'',monthId,title,status,''");
    mygrid.enableTooltips("true,true,true,true,true");
    mygrid.init();
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.loadReport,"json");
    
    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {
            mygrid.clearAll();
            mygrid.load(dwrCaller.loadReport, "json");
        }
    });

}

//填写上报
var nextStep=function(id,makeId){
	   WriteReportAction.getMakeReportById(makeId,function(repdata){
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
			    addWindow.setText("填写报告");
			    addWindow.keepInViewport(true);
			    addWindow.show();
				var monthId = "";
	            var title = "";
	            var gzld="none";     
				var gzlid="none";     
				var gzjy="none";       
				var zgqk="none";   
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
	    	}
 var writeFormData=[
				{type:"block",offsetTop:15,list:[
			        {type:"input",offsetLeft:40,label:"月  份：", inputWidth: 392,name : "monthId",value:""+monthId+"", readonly:"readonly"}
			    ]},
			    {type:"block",list:[
			        {type:"input",offsetLeft:40,label:"标  题：",inputWidth: 392,name:"title", value:""+title+"", readonly:"readonly"}
			    ]},
			    {type:""+gzld+"", list:[
			        {type:"input",offsetLeft:40,rows:4,label:"服务热点：",inputWidth: 370,name:"gzld"}
			    ]},
			    {type:""+gzlid+"",list:[
			        {type:"input",offsetLeft:40,rows:4,label:"服务亮点：",inputWidth: 370,name:"gzlid"}
			    ]},
			    {type:""+gzjy+"",list:[
			        {type:"input",offsetLeft:40,rows:4,label:"工作建议：",inputWidth: 370,name:"gzjy"}
			    ]},
			    {type:""+zgqk+"",list:[
			        {type:"input",offsetLeft:40,rows:4,label:"整改情况：",inputWidth: 370,name:"zgqk"}
			    ]},
			   
			    {type:"block",className:"blockStyle",list:[
                    {type:"label",offsetLeft:10,label:'<div style="padding-left: 26px; " class="item_label_left"><div class="dhxlist_txt_label align_left">' +
                                '<label for="_fileupload">上传附件：</label></div><div class="dhxlist_cont" style="height:20px;">' +
                                '<form enctype="multipart/form-data" action="upload" name="uploadForm" id="_uploadForm" method="post">' +
                                '<input style="width:250px;" class="dhxlist_txt_textarea" name="iconUrl" id="_fileupload" type="FILE" accept="image/*"></form></div></div>',name:"file",labelWidth:370},
                    {type:'hidden',name: "iconUrl"},
                    {type:'hidden',name: "affixName"}
                    
                ]},
            
			    {type:"block",offsetTop:10,list:[
			        {type:"button",label:"提交",name:"save",value:"提交",offsetLeft:200},
			        {type:"newcolumn"},
			        {type:"button",label:"关闭",name:"close",value:"关闭"}
			    ]},
			    {type:"hidden",name:"makeId",value:makeId},
			    {type:"hidden",name:"id",value:id}
			];
 
	 var nextForm = addWindow.attachForm(writeFormData);
	 
    dwrCaller.addAction("fillReport", function(afterCall, param){
        dhx.closeProgress();
        //执行Dwr方法
               var data = nextForm.getFormData();
                WriteReportAction.fillReport(data,function(rs){
                    if(rs){
                        dhx.alert("填写成功");
			            addWindow.close();
			            dhxWindow.unload();
                        mygrid.clearAll();
                        mygrid.load(dwrCaller.loadReport, "json");
                    }else{
                        dhx.alert("对不起，填写出错，请重试！");
                    }
                });
      });
	 $("_uploadForm").setAttribute("action",urlEncode(base+"/portalCommon/module/serviceManage/serProManage/attachmentPost.jsp"));
	   //查询表单事件处理
     nextForm.attachEvent("onButtonClick", function(id) {
        if (id == "save") {
                //先上传文件
                var file=null;
                var supportFile=['txt','doc','docx','xls','xlsx','pdf'];
                if(file=dwr.util.getValue("_fileupload")){
                    if(new RegExp("["+supportFile.join("|")+"]$","i").test(file)){
                    	var originalFilename =file.substring(file.lastIndexOf("\\")+1,file.length);
                    	var iframeName="_uploadFrame";
                        var ifr=$(iframeName);
                        //生成一个隐藏iframe，并设置form的target为该iframe，模拟ajax效果
                        if(!ifr){
                            ifr=document.createElement("iframe");
                            ifr.name=iframeName;
                            ifr.id=iframeName;
                            ifr.style.display="none";
                            document.body.appendChild(ifr);
                            if (ifr.attachEvent){
                                ifr.attachEvent("onload", function(){
                                	 nextForm.setItemValue("affixName",originalFilename);
                                	 nextForm.setItemValue("iconUrl",window.frames[iframeName].fileName);
                                     nextForm.send(dwrCaller.fillReport);
                                });
                            } else {
                                ifr.onload = function(){
                                    nextForm.setItemValue("affixName", originalFilename);
                                	nextForm.setItemValue("iconUrl",window.frames[iframeName].fileName);
                                    nextForm.send(dwrCaller.fillReport);
                                };
                            }
                        }
                        var fm = $("_uploadForm");
                        fm.target = iframeName;
                        $("_uploadForm").submit();
                    }else{
                        dhx.alert("请选择一个图片文件，支持格式txt/doc/docx/xls/xlsx/pdf");
                        return;
                    }
                }else{
                    //dhx.showProgress("菜单管理", "正在提交表单数据...");
                    nextForm.send(dwrCaller.fillReport);
                }
                
                
        }
        if(id == "close"){
            addWindow.close();
            dhxWindow.unload();
        }
       
    });   
		   
    });
}

//删除
var deleteReport = function(rowDatas) {
    dhx.confirm("是否确认删除您所选择的记录？", function(data){
        if(data){
                  WriteReportAction.deleteReport(rowDatas,function(rs){
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
			        {type:"label",offsetLeft:40,rows:4,label:"发布时间： "+createTime}
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

dhx.ready(init);