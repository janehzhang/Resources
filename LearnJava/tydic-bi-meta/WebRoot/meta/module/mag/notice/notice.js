
/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var base = getBasePath();
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();


/**
 * Report表Data转换器
 */
var noticeConvertConfig = {
    idColumnName:"noticeId",
    filterColumns:["noticeTitle","noticeFunction","levelName","isShow","updateDate","failureDate","userNamecn","noticeContent","_buttons"],
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
	   if (columnName == '_buttons') {
		         var str ="";
		         if(rowData["isShow"]=="拟稿"){
	                   str  ="<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=tj("+rowData["noticeId"]+")>提交</a></span>";
	                   str +="<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=modifyNotice("+rowData["noticeId"]+")>修改</a></span>";
	                   str +="<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=deleteNotice("+rowData["noticeId"]+")>删除</a></span>";
	    		 }else{
	    			   str  ="<span style='padding-left:5px;'>提交</span>";
	                   str +="<span style='padding-left:5px;'>修改</span>";
	                   str +="<span style='padding-left:5px;'>删除</span>";
	    		 }

		              str +="<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=viewNotice("+rowData["noticeId"]+")>查看</a></span>";
	             return str;
	    }
	
    
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}
var noticeDataConverter = new dhtmxGridDataConverter(noticeConvertConfig);

/**
 * 初始化界面
 */
var noticeInit=function(){
	var noticeLayout = new dhtmlXLayoutObject(document.body, "2E");
    noticeLayout.cells("a").setText("业务系统");
    noticeLayout.cells("b").hideHeader();
    noticeLayout.cells("a").setHeight(75);
    noticeLayout.cells("a").fixSize(false, true);
    noticeLayout.hideConcentrate();
    noticeLayout.hideSpliter();//移除分界边框。

    //加载查询表单
    var queryform = noticeLayout.cells("a").attachForm([
        {type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
        {type:"input",label:"公告名称：",name:"noticeTitle"},
        {type:"newcolumn"},
        {type:"combo",label:"公告层级：",name:"noticeLevel",options:[{value:"",text:"全部",selected:true}],inputWidth:100,inputHeight:22,readonly:true},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"}
    ]);
    //加载层级类型
    var tableTypeData = getComboByRemoveValue("NOTICE_LEVEL");
    queryform.getCombo("noticeLevel").addOption(tableTypeData.options);
    var loadNoticeParam = new biDwrMethodParam();
    loadNoticeParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
            var formData=queryform.getFormData();
            formData.noticeTitle=Tools.trim(queryform.getInput("noticeTitle").value);
            return formData;
        }
        }
    ]);
    
   dwrCaller.addAutoAction("loadNoticeUrl", "NoticeAction.queryNotice",loadNoticeParam, function(data){});
   dwrCaller.addDataConverter("loadNoticeUrl", noticeDataConverter);
   var buttons = {
        addNotice:{name:"addNotice",text:"新增",imgEnabled:base + "/meta/resource/images/addGroup.png",
            imgDisabled:base + "/meta/resource/images/addGroup.png",onclick:function(rowData) {
                addNotice();
            }}
     };
   
       // 按钮权限过滤,checkRule.
    var bottonRoleRow = ["addNotice"]; 
     //定义全局函数，用于获取有权限的button列表定义
    window["getButtons"] = function() {
        var res = [];
        for(var i=0;i<bottonRoleRow.length;i++){
            res.push(buttons[bottonRoleRow[i]]);
        }
        return res;
    };
    var buttonToolBar = noticeLayout.cells("b").attachToolbar();
    var pos = 1;
    var filterButton = window["getButtons"]();
    for (var i = 0; i < filterButton.length; i++) {
        buttonToolBar.addButton(filterButton[i].name, pos++, filterButton[i].text,
                filterButton[i].imgEnabled, filterButton[i].imgDisabled);
        var button=buttonToolBar.getItemNodeById(filterButton[i].name);
        button.setAttribute("id",filterButton[i].name);
    }
     //添加datagrid
    mygrid = noticeLayout.cells("b").attachGrid();
    mygrid.setHeader("<strong>公告标题</strong>,<strong>公告类型</strong>,<strong>公告层级</strong>,<strong>公告状态</strong>,<strong>上次更新时间</strong>,<strong>失效时间</strong>,<strong>创建人</strong>,<strong>公告内容</strong>,<strong>操作</strong>");
    mygrid.setInitWidthsP("15,10,10,10,10,10,10,10,15");
    mygrid.setHeaderAlign("center,center,center,center,center,center,center,center,center");
    mygrid.setColAlign("left,center,center,center,center,center,left,left,center");
    mygrid.enableResizing("true,true,true,true,true,true,true,true,true");
    mygrid.enableCtrlC();
    mygrid.enableTooltips("true,true,true,true,true,true,true,true,true");
    mygrid.init();
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.loadNoticeUrl, "json");
    
   //查询表单事件处理
   queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {
            mygrid.clearAll();
            mygrid.load(dwrCaller.loadNoticeUrl, "json");
        }
    });
  //添加buttonToolBar事件
   buttonToolBar.attachEvent("onclick", function(id) {
        if(id=="addNotice"){
            addNotice();
        }
     });
   
    
}

//注册添加系统Action
dwrCaller.addAutoAction("insertNotice","NoticeAction.insertNotice");
dwrCaller.addAutoAction("updateNotice","NoticeAction.updateNotice");
dwrCaller.addAutoAction("deleteNotice","NoticeAction.deleteNotice");

/**
 * 新增公告
 */
var addNotice=function(){
    //初始化新增弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("addWindow", 0, 0, 250, 400);
    var addWindow = dhxWindow.window("addWindow");
    addWindow.setModal(true);
    addWindow.stick();
    addWindow.setDimension(550, 400);
    addWindow.center();
    addWindow.setPosition(addWindow.getPosition()[0],addWindow.getPosition()[1]-50);
    addWindow.denyResize();
    addWindow.denyPark();
    addWindow.button("minmax1").hide();
    addWindow.button("park").hide();
    addWindow.button("stick").hide();
    addWindow.button("sticked").hide();
    addWindow.setText("新增公告");
    addWindow.keepInViewport(true);
    addWindow.show();
    //建立表单。
    var addForm = addWindow.attachForm(addFormData);
    loadZoneTreeChkBox(user.zoneId,addForm);
    //加载层级类型
    var levelData = getComboByRemoveValue("NOTICE_LEVEL");
    addForm.getCombo("noticeLevel").addOption(levelData.options);
    addForm.setFormData({noticeLevel:1,noticeType:1,noticeState:0});
    //添加验证
    addForm.defaultValidateEvent();
    //查询表单事件处理
    $("_uploadForm").setAttribute("action",urlEncode(base+"/portalCommon/module/serviceManage/serProManage/attachmentPost.jsp"));	   
    addForm.attachEvent("onButtonClick", function(id) {
        if (id == "save") {
            if(addForm.validate()){
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
                                	  addForm.setItemValue("affixName",originalFilename);
                                	  addForm.setItemValue("iconUrl",window.frames[iframeName].fileName);
                                      insertNotice(addForm);
                                });
                            } else {
                                ifr.onload = function(){
                                    addForm.setItemValue("affixName", originalFilename);
                                	addForm.setItemValue("iconUrl",window.frames[iframeName].fileName);
                                    insertNotice(addForm);
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
                                insertNotice(addForm);
                }
            }
        }
        if(id == "close"){
            addWindow.close();
            dhxWindow.unload();
        }
    });
    
      //Form 提交
	var insertNotice = function(addForm) {
		var data = addForm.getFormData();
		data.effectDate = addForm.getInput("effectDate").value;
		data.failureDate = addForm.getInput("failureDate").value;
		//保存
		dwrCaller.executeAction("insertNotice", data, function(rs) {
			if(rs){
				addWindow.close();
				dhxWindow.unload();
			    //window.parent.location.reload();
			    location.href=location.href;
			    dhx.alert("新增成功");
			} else {
                dhx.alert("对不起，新增出错，请重试！");
			}
		})
	}

}


/**
 * 修改一条公告
 * @param dataId
 * @param bool 不为空表示是流浪
 */
 var affixPath="";
 var affixName="";
var modifyNotice=function(rowId){
    var rowData = mygrid.getRowData(rowId);//获取行数据
    //初始化新增弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("modifyWindow", 0, 0, 250, 400);
    var modifyWindow = dhxWindow.window("modifyWindow");
    modifyWindow.setModal(true);
    modifyWindow.stick();
    modifyWindow.setDimension(550, 400);
    modifyWindow.center();
    modifyWindow.setPosition(modifyWindow.getPosition()[0],modifyWindow.getPosition()[1]-50);
    modifyWindow.denyResize();
    modifyWindow.denyPark();
    modifyWindow.button("minmax1").hide();
    modifyWindow.button("park").hide();
    modifyWindow.button("stick").hide();
    modifyWindow.button("sticked").hide();
    modifyWindow.setText("修改公告");
    modifyWindow.keepInViewport(true);
    modifyWindow.show();
    //建立表单。
          affixPath= rowData.userdata.affixPath;
          affixName= rowData.userdata.affixName;
         
         var affixStr=affixName+"</a>&nbsp;&nbsp;&nbsp;&nbsp;<img alt='删除附件' src='"+base+"/meta/resource/images/no.png' onclick='delAffix(this)' />";
		 
         var updateFormData=[
		    {type:"block",offsetTop:15,list:[
		        {type:"input",offsetLeft:40,label:"公告名称：",inputWidth: 370,name:"noticeTitle",validate:"NotEmpty,MaxLength[64]"},
		        {type:"newcolumn"},
		        {type:"label",label:"<span style='color: red'>*</span>"}
		    ]},
		    {type:"hidden",name:"noticeId"},
		    {type:"hidden",name:"noticeUser",value:user.userId},
		    {type:"block",list:[
		        {type:"input",offsetLeft:40,rows:4,label:"公告内容：",inputWidth: 370,name:"noticeContent",validate:"NotEmpty,MaxLength[600]" },
		        {type:"newcolumn"},
		        {type:"label",label:"<span style='color: red'>*</span>"}
		    ]},
		    {type:"hidden", name:"noticeType"},
		    {type:"hidden", name:"noticeState"},
		    {type:"block",list:[
		        {type:"combo",offsetLeft:40,label:"公告层级：",inputWidth: 200,name:"noticeLevel",readonly:true}
		    ]},
		    {type:"block",list:[
		        {type:"input",offsetLeft:40,label:"有效地市：",inputWidth: 200,name:"noticeDisplayZone",readonly:true},
		        {type:"hidden",name:"noticeDisplayZones"}
		    ]},
		    {type:"block",list:[
		        {type : "calendar",offsetLeft:40,label : "生效时间：",inputWidth: 200,name : "effectDate",dateFormat : "%Y-%m-%d",weekStart : "7",readonly:true}
		    ]},
		    {type:"block",list:[
		        {type : "calendar",offsetLeft:40,label : "失效时间：",inputWidth: 200,name : "failureDate",dateFormat : "%Y-%m-%d",weekStart : "7",readonly:true}
		    ]},
		    {type:"block",className:"blockStyle",list:[
		        {type:"label",offsetLeft:10,label:'<div style="padding-left: 26px; " class="item_label_left"><div class="dhxlist_txt_label align_left">' +
		                    '<label for="_fileupload">上传附件：</label></div><div class="dhxlist_cont" style="height:20px;">' +
		                    '<form enctype="multipart/form-data" action="upload" name="uploadForm" id="_uploadForm" method="post">' +
		                    '<input style="width:250px;" class="dhxlist_txt_textarea" name="iconUrl" id="_fileupload" type="FILE" accept="image/*"></form></div></div>',name:"file",labelWidth:370},
		        {type:'hidden',name: "iconUrl"},
		        {type:'hidden',name: "affixName"}
		        
		    ]},
		    {type:"label",offsetLeft:40,label:"<a href=\"#\" onclick=\"downloadattrs('"+affixPath+"')\">"+(affixName=='没有附件'?"":affixStr),  labelWidth:370},
		    {type:"block",offsetTop:10,list:[
		        {type:"button",label:"保存",name:"save",value:"保存",offsetLeft:200},
		        {type:"newcolumn"},
		        {type:"button",label:"关闭",name:"close",value:"关闭"}
		    ]}
		];    
    
    var modifyForm = modifyWindow.attachForm(updateFormData);
    loadZoneTreeChkBox(user.zoneId,modifyForm,rowData.userdata.noticeDisplayZones);
    //加载层级类型
    var levelData = getComboByRemoveValue("NOTICE_LEVEL");
    modifyForm.getCombo("noticeLevel").addOption(levelData.options);
    //添加验证
    modifyForm.defaultValidateEvent();
    //初始化表单信息
    var initForm = function() {
        var initData = {
            //"noticeTitle","noticeLevel","isShow","noticeFunction","updateDate","noticeContent","_buttons"],
            noticeId:rowId,
            noticeTitle:rowData.data[0],//名称
            noticeLevel:rowData.userdata.noticeLevel,//状态
            noticeContent:rowData.data[7],
            noticeType:rowData.userdata.noticeType,
            noticeState:rowData.userdata.noticeState,
            effectDate:rowData.userdata.effectDate,
            failureDate:rowData.userdata.failureDate
           // iconUrl:affixPath,
           // affixName:affixName
        }
        if(rowData.userdata.effectDate == null || Tools.trim(rowData.userdata.effectDate) == ""){
            delete initData.effectDate;
        }
        if(rowData.userdata.failureDate == null || Tools.trim(rowData.userdata.failureDate) == ""){
            delete initData.failureDate;
        }
        modifyForm.setFormData(initData);
//        modifyForm.disableItem("groupSn");
    }
    initForm();
    $("_uploadForm").setAttribute("action",urlEncode(base+"/portalCommon/module/serviceManage/serProManage/attachmentPost.jsp"));	
    modifyForm.attachEvent("onButtonClick", function(id) {
        if (id == "save") {
            modifyForm.setItemValue("iconUrl",affixPath);
            modifyForm.setItemValue("affixName",  affixName=="没有附件"?"":affixName);
        	
            if(modifyForm.validate()){
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
                                	  modifyForm.setItemValue("affixName",originalFilename);
                                	  modifyForm.setItemValue("iconUrl",window.frames[iframeName].fileName);
                                      modifyNotice(modifyForm);
                                });
                            } else {
                                ifr.onload = function(){
                                    modifyForm.setItemValue("affixName", originalFilename);
                                	modifyForm.setItemValue("iconUrl",window.frames[iframeName].fileName);
                                    modifyNotice(modifyForm);
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
                                modifyNotice(modifyForm);
                }
            }
        }
        if(id == "close"){
            modifyWindow.close();
            dhxWindow.unload();
        }
    });
    //Form 提交
	var modifyNotice = function(modifyForm) {
                var data = modifyForm.getFormData();
                data.effectDate = modifyForm.getInput("effectDate").value;
                data.failureDate = modifyForm.getInput("failureDate").value;
                //修改
                dwrCaller.executeAction("updateNotice",data,function(rs){
                	if(rs){
			            modifyWindow.close();
			            dhxWindow.unload();
			            location.href=location.href;
			            dhx.alert("修改成功");
                    }else{
                         dhx.alert("对不起，修改出错，请重试！");
                    }
                })
	 }
}


var delAffix=function(obj){
	dhx.confirm("确定要删除附件吗？", function(r){
      if(r){
		  affixPath="";
	      affixName="";
		  obj.parentNode.parentNode.removeChild(obj.parentNode);
	      }
    }); 
}
/**
 * 添加公告  表单
 */
var addFormData=[
    {type:"block",offsetTop:15,list:[
        {type:"input",offsetLeft:40,label:"公告名称：",inputWidth: 370,name:"noticeTitle",validate:"NotEmpty,MaxLength[64]"},
        {type:"newcolumn"},
        {type:"label",label:"<span style='color: red'>*</span>"}
    ]},
    {type:"hidden",name:"noticeId"},
    {type:"hidden",name:"noticeUser",value:user.userId},
    {type:"block",list:[
        {type:"input",offsetLeft:40,rows:4,label:"公告内容：",inputWidth: 370,name:"noticeContent",validate:"NotEmpty,MaxLength[600]" },
        {type:"newcolumn"},
        {type:"label",label:"<span style='color: red'>*</span>"}
    ]},
    {type:"hidden", name:"noticeType"},
    {type:"hidden", name:"noticeState"},
    {type:"block",list:[
        {type:"combo",offsetLeft:40,label:"公告层级：",inputWidth: 200,name:"noticeLevel",readonly:true}
    ]},
    {type:"block",list:[
        {type:"input",offsetLeft:40,label:"有效地市：",inputWidth: 200,name:"noticeDisplayZone",readonly:true},
        {type:"hidden",name:"noticeDisplayZones"}
    ]},
    {type:"block",list:[
        {type : "calendar",offsetLeft:40,label : "生效时间：",inputWidth: 200,name : "effectDate",dateFormat : "%Y-%m-%d",weekStart : "7",readonly:true}
    ]},
    {type:"block",list:[
        {type : "calendar",offsetLeft:40,label : "失效时间：",inputWidth: 200,name : "failureDate",dateFormat : "%Y-%m-%d",weekStart : "7",readonly:true}
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
        {type:"button",label:"保存",name:"save",value:"保存",offsetLeft:200},
        {type:"newcolumn"},
        {type:"button",label:"关闭",name:"close",value:"关闭"}
    ]}
];


    
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPath");
var zoneConverter=new dhtmxTreeDataConverter({
    idColumn:"zoneId",pidColumn:"zoneParId",
   textColumn:"zoneName"
});
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
var loadZoneTreeChkBox=function(selectZone,form,selectedZoneId){
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
    var target=form.getInput("noticeDisplayZone");
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
        form.setFormData({noticeDisplayZone:zones,noticeDisplayZones:zonesId});
    });
    tree.attachEvent("onSelect",function(id){
        tree.setCheck(id,1);
        var checkedData = tree.getAllChecked();
        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
        var zones = "";
        var zonesId ="";
        for (var i = 0;i< nodes.length;i++){
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
        form.setFormData({noticeDisplayZone:zones,noticeDisplayZones:zonesId});
    });
    dwrCaller.executeAction("loadZoneTree",beginId,999999999,function(data){
        tree.loadJSONObject(data);
        if(selectedZoneId){
            var selectedZoneIds = selectedZoneId.split(",");
            var zones = "";
            var zonesId = "";
            for(var j=0; j<selectedZoneIds.length; j++){
                tree.setCheck(selectedZoneIds[j],1);
                if(zones == ""){
                    zones = tree.getItemText(selectedZoneIds[j]).toString();
                }else{
                    zones = zones + "," + tree.getItemText(selectedZoneIds[j]).toString();
                }
                if(zonesId == ""){
                    zonesId = selectedZoneIds[j].toString();
                }else{
                    zonesId = zonesId   + "," + selectedZoneIds[j].toString();
                }
                target.value=tree.getSelectedItemText();
            }
            if(zones == 0){
                zones = "";
                zonesId = null;
            }
            form.setFormData({noticeDisplayZone:zones,noticeDisplayZones:zonesId});
        }else{
            if(selectZone){
                tree.selectItem(selectZone); //选中指定节点
                //将input框选中
                target.value=tree.getSelectedItemText();
            }
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
//提交
var tj=function(id){
        dhx.confirm("是否要把您所选择的公告提交给后台管理员审核吗？",function(r){
            if(r){
              NoticeAction.updateNoticeCtrlr(id,'1',function(rs){
                    if(rs){
		                //mygrid.clearAll();
		                //mygrid.load(dwrCaller.loadNoticeUrl, "json");
                         location.href=location.href;
                         dhx.alert("用户操作成功！");
                    }else{
                         dhx.alert("对不起，操作出错，请重试！");
                    }
                });
            }
        });
}

var viewNotice=function(rowId){
	var rowData = mygrid.getRowData(rowId);//获取行数据
    //初始化新增弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("modifyWindow", 0, 0, 250, 400);
    var modifyWindow = dhxWindow.window("modifyWindow");
    modifyWindow.setModal(true);
    modifyWindow.stick();
    modifyWindow.setDimension(400, 400);
    modifyWindow.center();
    modifyWindow.setPosition(modifyWindow.getPosition()[0],modifyWindow.getPosition()[1]-50);
    modifyWindow.denyResize();
    modifyWindow.denyPark();
    modifyWindow.button("minmax1").hide();
    modifyWindow.button("park").hide();
    modifyWindow.button("stick").hide();
    modifyWindow.button("sticked").hide();
    modifyWindow.setText("查看公告");
    modifyWindow.keepInViewport(true);
    modifyWindow.show();
    //建立表单。
    var modifyForm = modifyWindow.attachForm([
            {type:"label",offsetLeft:40,label:"公告名称："+rowData.data[0],name:"noticeTitle"},
            {type:"label",offsetLeft:40,label:"公告内容："+rowData.data[7],name:"noticeContent"},
            {type:"label",offsetLeft:40,label:"公告层级："+getNameByTypeValue("NOTICE_LEVEL",rowData.userdata.noticeLevel),name:"noticeLevel"},
            {type:"label",offsetLeft:40,label:"有效地市："+(rowData.userdata.noticeDisplayZone==null?"":rowData.userdata.noticeDisplayZone),name:"noticeDisplayZone"},
            {type:"label",offsetLeft:40,label:"生效时间："+(rowData.userdata.effectDate==null?"":rowData.userdata.effectDate),name:"effectDate"} ,
            {type:"label",offsetLeft:40,label:"失效时间："+(rowData.userdata.failureDate==null?"":rowData.userdata.failureDate),name : "failureDate"},
            {type:"label",offsetLeft:40,label:"下载附件：<a href=\"#\" onclick=\"downloadattrs('"+rowData.userdata.affixPath+"')\">"+(rowData.userdata.affixName==null?"":rowData.userdata.affixName)+"</a>",  labelWidth:370},
            {type:"button",label:"关闭",offsetLeft:60,name:"close",value:"关闭"}
    ]);
    //加载层级类型
//    var levelData = getComboByRemoveValue("NOTICE_LEVEL");
//    modifyForm.getCombo("noticeLevel").addOption(levelData.options);
//    //添加验证
//    modifyForm.defaultValidateEvent();
//    //初始化表单信息
//    var initForm = function() {
//        var initData = {
//            noticeLevel:rowData.userdata.noticeLevel,//状态
//            noticeType:rowData.userdata.noticeType,
//            noticeState:rowData.userdata.noticeState
//        }
//        modifyForm.setFormData(initData);
//    }
//    initForm();
    modifyForm.attachEvent("onButtonClick", function(id) {
        if(id == "close"){
            modifyWindow.close();
            dhxWindow.unload();
        }
    });
}

/**
 * 删除一条公告
 * @param dataId
 */
var deleteNotice=function(dataId){
    dhx.confirm("确定要删除该记录吗？", function(r){
        if(r){
            dwrCaller.executeAction("deleteNotice",dataId,function(data){
                if(data.type == "error" || data.type == "invalid"){
                    dhx.alert("对不起，删除失败，请重试！");
                }else{
                    dhx.alert("删除成功！");
                    var dataArray = [];
                    for (var i = 0; i < data.length; i ++) {
                        dataArray.push({id:data[i].sid});
                    }
                    mygrid.updateGrid(dataArray, "delete");
                }
            });
        }
    })
}

//下载附件信息
var downloadattrs=function(attchmetPath){
	if(attchmetPath != "" && attchmetPath != "null"){
    document.forms[0].target="hiddenFrame";
 	var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ attchmetPath;
	window.open(url,"hiddenFrame","");
	}else{
		 dhx.alert("对不起，没有附件可下载！");
	}
}

dhx.ready(noticeInit);