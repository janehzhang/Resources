
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
		         var   str ="";
		         if(rowData["isShow"]=="未审核"){
		                 str +="<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=modifyNotice("+rowData["noticeId"]+")>审核</a></span>";
		         } else{
		        	 if(rowData["isShow"]=="上线"){
		        		 str +="<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=tj(3,"+rowData["noticeId"]+")>下线</a></span>";
		        	 }
                     if(rowData["isShow"]=="下线"){
                    	 str +="<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=tj(2,"+rowData["noticeId"]+")>上线</a></span>";
		        	 }
		        	 
		        	 
		         }    
		         
		             str +="<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=deleteNotice("+rowData["noticeId"]+")>删除</a></span>";   
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
    
   dwrCaller.addAutoAction("loadNoticeUrl", "NoticeAction.queryNotice1",loadNoticeParam, function(data){});
   dwrCaller.addDataConverter("loadNoticeUrl", noticeDataConverter);

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
    
}

//注册添加系统Action
dwrCaller.addAutoAction("spNotice","NoticeAction.spNotice");
dwrCaller.addAutoAction("deleteNotice","NoticeAction.deleteNotice");



/**
 * 审核公告
 * @param dataId
 * @param bool 不为空表示是流浪
 */
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
    modifyWindow.setText("审核公告");
    modifyWindow.keepInViewport(true);
    modifyWindow.show();
    //建立表单。
    var modifyForm = modifyWindow.attachForm(addFormData);
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
            
        }
        if(rowData.userdata.effectDate == null || Tools.trim(rowData.userdata.effectDate) == ""){
            delete initData.effectDate;
        }
        if(rowData.userdata.failureDate == null || Tools.trim(rowData.userdata.failureDate) == ""){
            delete initData.failureDate;
        }
         modifyForm.setFormData(initData);
   }
    initForm();
    modifyForm.attachEvent("onChange", function (id, value){
    	if(id=="spStatus"){
    		var str=value==0?"不同意":"同意";
    		modifyForm.setItemValue("spIdea",str);
    	}
	});
   
   modifyForm.attachEvent("onButtonClick", function(id) {
        if (id == "save") {
            if(modifyForm.validate()){
                 var data = modifyForm.getFormData();
                //保存
                dwrCaller.executeAction("spNotice",data,function(rs){
                	if(rs){
                        dhx.alert("操作成功!");
                        modifyWindow.close();
                        dhxWindow.unload();
                        mygrid.clearAll();
                        mygrid.load(dwrCaller.loadNoticeUrl, "json");
                    }else{
                         dhx.alert("对不起，操作出错，请重试！");
                    }
                });
            }
        }
        if(id == "close"){
            modifyWindow.close();
            dhxWindow.unload();
        }
    });
}

/**
 * 添加公告  表单
 */
var addFormData=[
    {type:"block",offsetTop:15,list:[
        {type:"input",offsetLeft:40,label:"公告名称：",inputWidth: 370,name:"noticeTitle",validate:"NotEmpty,MaxLength[64]",readonly:"readonly"}
    ]},
    {type:"hidden",name:"noticeId"},
    {type:"block",list:[
        {type:"input",offsetLeft:40,rows:4,label:"公告内容：",inputWidth: 370,name:"noticeContent",validate:"NotEmpty,MaxLength[600]",readonly:"readonly" }
    ]},
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
    {type:"block",list:[
        {type:"combo",offsetLeft:40,label:"审核状态：",inputWidth: 200,name:"spStatus",readonly:true,options:[
            {value:"2",text:"同意",  selected:true},
            {value:"0",text:"不同意",selected:false}
        ]}
    ]},
    {type:"block",list:[
        {type:"input",offsetLeft:40,rows:4,label:"审核意见：",inputWidth: 370,name:"spIdea",value:"同意" ,validate:"NotEmpty,MaxLength[600]"}
    ]},
    
    {type:"block",offsetTop:10,list:[
        {type:"button",label:"提交",name:"save",value:"提交",offsetLeft:200},
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
    modifyForm.attachEvent("onButtonClick", function(id) {
        if(id == "close"){
            modifyWindow.close();
            dhxWindow.unload();
        }
    });
}

//上线
var tj=function(status,id){
	var str="是否要把您所选择的公告";
	if(status==3){
	    str +="下线吗？";
	}
	if(status==2){
		str +="上线吗？";
	}
   dhx.confirm(str,function(r){
         if(r){
           NoticeAction.updateNoticeCtrlr(id,status,function(rs){
                 if(rs){
                     dhx.alert("用户操作成功！");
		                mygrid.clearAll();
		                mygrid.load(dwrCaller.loadNoticeUrl, "json");
                 }else{
                      dhx.alert("对不起，操作出错，请重试！");
                 }
             });
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