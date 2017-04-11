/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *
 *Description：
 *
 *Dependent：
 *
 *Author:
 *        刘斌
 *Finished：
 *       2012-04-12
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/

/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller = new biDwrCaller();

/**
 * 数据转换Convert
 */
var convertConfig = {
    idColumnName:"reportId",
    filterColumns:["reportName","createTime","countOpen","countComment","grade","_buttons"],
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        if(columnName=="reportName" && cellValue){
            var tmp = "'"+cellValue+"'";
            return '<a href="javascript:openReport('+rowData.reportId+','+tmp+')">'+cellValue+'</a>'+(rowData.reportState==0?"（已下线）":"");
        }
        if(columnName == '_buttons') {
            if(rowData.reportState==0){
                return "getButtonsCol";
            }else{
                return "getButtonsColOffLine";
            }
        }
        if(columnName == 'countOpen' || columnName == 'countComment'){
            return cellValue + '次';
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};

var convert = new dhtmxGridDataConverter(convertConfig);


/**
 * 初始化方法
 */
var myCreateInit = function(){
    var myCreateLayout = new dhtmlXLayoutObject(document.getElementById("container"), "2E");
    myCreateLayout.cells("a").setText("我创建的报表");
    myCreateLayout.cells("b").hideHeader();
    myCreateLayout.cells("a").setHeight(75);
    myCreateLayout.cells("a").fixSize(false, true);
    myCreateLayout.hideConcentrate();
    myCreateLayout.hideSpliter();//移除分界边框。

    //加载查询表单
    var queryform = myCreateLayout.cells("a").attachForm([
        {type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
        {type:"input",label:"报表名称：",name:"keyWord"},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"},
        {type:"hidden",name:"template"}
    ]);

    var loadParam = new biDwrMethodParam();
    loadParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
            var formData=queryform.getFormData();
            formData.keyWord=Tools.trim(queryform.getInput("keyWord").value);
            return formData;
        }
        }
    ]);
    dwrCaller.addAutoAction("queryMyCreateReport", "MyCreateAction.queryMyCreateReport", loadParam);
    dwrCaller.addDataConverter("queryMyCreateReport", convert);

    window["getButtonsCol"]=function(){
        return [
            {name:"modify",text:"复制",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
                imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                    copyReport(rowData);
                }
            },
            {name:"modify",text:"修改",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
                imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                    modifyReport(rowData);
                }
            },
            {name:"share",text:"共享",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
                imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                    shareReport(rowData);
                }
            }
        ];
    };

    window["getButtonsColOffLine"]=function(){
        return [
            {name:"modify",text:"复制",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
                imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                    copyReport(rowData);
                }
            },
            {name:"modify",text:"修改",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
                imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                    modifyReport(rowData);
                }
            },
            {name:"share",text:"共享",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
                imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                    shareReport(rowData);
                }
            },
            {name:"offLine",text:"下线",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
                imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                    offLineReport(rowData);
                }
            }
        ];
    };


    mygrid = myCreateLayout.cells("b").attachGrid();
    mygrid.setHeader("报表名称,创建时间,被打开,被评论,综合评分,操作");
    mygrid.setInitWidthsP("20,20,15,15,15,15");
    mygrid.setColAlign("left,center,center,center,center,center");
    mygrid.setHeaderAlign("left,center,center,center,center,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,sb");
//    mygrid.setSkin("xp");
    mygrid.init();
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.queryMyCreateReport, "json");

        //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {
            //进行数据查询。
            mygrid.clearAll();
            mygrid.load(dwrCaller.queryMyCreateReport, "json");
        }
    });
    // 添加Enter查询事件
    queryform.getInput("keyWord").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            mygrid.clearAll();
            mygrid.load(dwrCaller.queryMyCreateReport, "json");
        }
    }

};

var shareWindow = null;
/**
 * 分享报表
 * @param rowData
 */
var shareReport = function(rowData){
    if(!shareWindow){
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("shareWindow", 0, 0, 500, 320);
        shareWindow = dhxWindow.window("shareWindow");
        shareWindow.setModal(true);
        shareWindow.stick();
        shareWindow.setDimension(500, 320);
        shareWindow.center();
        shareWindow.denyResize();
        shareWindow.denyPark();
        shareWindow.button("minmax1").hide();
        shareWindow.button("park").hide();
        shareWindow.button("stick").hide();
        shareWindow.button("sticked").hide();
        shareWindow.setModal(true);
        shareWindow.keepInViewport(true);
        shareWindow.attachEvent("onClose", function(){
            shareWindow.hide();
            shareWindow.setModal(false);
            return false;
        });
        shareWindow.setText("报表共享设置");
        shareWindow.show();
        var addLayout = new dhtmlXLayoutObject(shareWindow, "2E");
        addLayout.cells("a").hideHeader();
        addLayout.cells("a").attachObject($("_shareDetail"));
        addLayout.cells("a").setHeight(260);
        addLayout.cells("b").hideHeader();
        addLayout.hideConcentrate();
        addLayout.hideSpliter();//移除分界边框。
        var btnFormData =
            [
                {type: "settings", position: "label-left"},
                {type:"block",offsetTop:5,inputTop :5,list:[
                    {type: "settings", position: "label-left"},
                    {type:"button",label:"保存",name:"save",value:"保存",offsetLeft:180},
                    {type:"newcolumn"},
                    {type:"button",label:"关闭",name:"close",value:"关闭"}
                ]}
            ];
        var btnForm = addLayout.cells("b").attachForm(btnFormData);
        btnForm.attachEvent("onButtonClick", function(id){
            if(id=="save"){
                var data = {
                    refUserIds:$("refUserIds").value,
                    rightLevel:$("rightLevel").value,
                    reportId:reportId
                };
                dhx.showProgress();
                MyCreateAction.shareUser(data, function(r){
                    dhx.closeProgress();
                    if(r){
                        dhx.alert("共享成功！");
                        shareWindow.close();
                    }else{
                        dhx.alert("共享失败，请重试！")

                    }
                });
            }
            if(id=="close"){
                shareWindow.close();
            }
        });
    }else{
        shareWindow.setModal(true);
        shareWindow.show();
    }
    MyCreateAction.queryReportByReportId(rowData.id, function(data){
        $("reportName").innerHTML = rowData.userdata.reportName;
        reportId = data.REPORT_ID;
        if(data.REPORT_NOTE){
            $("reportNote").value = data.REPORT_NOTE;
        }
        if(data.refUserStr){
            $("reportUser").innerHTML = data.refUserStr;
        }else{
            $("reportUser").innerHTML = "";
        }
        if(data.userIds){
            $("refUserIds").value = data.userIds;
        }
        if(data.RIGHT_LEVEL){
            $("rightLevel").value=data.RIGHT_LEVEL;
            $("shareContentDiv").style.cssText="display:;";
            $("fixShare").style.cssText="display:none;";
        }else{
            $("rightLevel").value=0;
            $("shareContentDiv").style.cssText="display:none;";
            $("fixShare").style.cssText="display:;";
        }

    });

};

/**
 * 打开报表链接
 * @param reportId
 */
var openReport = function(reportId, reportName){
    window.open(urlEncode(getBasePath()+"/home/module/commentRpt/commentRpt.jsp?rptId="+reportId));
};

/**
 * 修改报表
 * @param rowData
 */
var modifyReport = function(rowData){
    window.open(urlEncode(getBasePath()+"/meta/module/reportManage/build/reportConfig.jsp?reportId="+rowData.userdata.reportId));
};

/**
 * 报表下线
 * @param rowData
 */
var offLineReport = function(rowData){
    window.open(urlEncode(getBasePath()+"/meta/module/reportManage/reportOffline/exeReportOffline.jsp?typeId=1&reportId="+rowData.userdata.reportId));
};

/**
 * 报表复制
 * @param rowData
 */
var copyReport = function(rowData){
    window.open(urlEncode(getBasePath()+"/meta/module/reportManage/build/reportConfig.jsp?reportId="+rowData.userdata.reportId+"&copyFlag=1"));
};

/**
 * 共享方式变化
 * @param obj
 */
var rightChange = function(obj){
    if(obj.value == 0){
        $("shareContentDiv").style.cssText="display:none;";
        $("fixShare").style.cssText="display:;";

    }else{
        $("shareContentDiv").style.cssText="display:;";
        $("fixShare").style.cssText="display:none;";
    }

};

var reportId = null;

/**
 * 新增共享人员
 */
var addShareUser = function(){
    var loadUserParam=new biDwrMethodParam();
    var dhxWindow=new dhtmlXWindows();
    dhxWindow.createWindow("refUserWindow",0,0,600,400);
    var refUserWindow=dhxWindow.window("refUserWindow");
    ///refUserWindow.stick();
    refUserWindow.setModal(true);
    //refUserWindow.stick();
    refUserWindow.setDimension(600,400);
    refUserWindow.center();
    refUserWindow.denyResize();
    refUserWindow.denyPark();
    refUserWindow.setText('选择共享用户');
    refUserWindow.keepInViewport(true);
    //关闭一些不用的按钮。
    refUserWindow.button("minmax1").hide();
    refUserWindow.button("park").hide();
    refUserWindow.button("stick").hide();
    refUserWindow.button("sticked").hide();
    refUserWindow.show();
    var refUserLayout = new dhtmlXLayoutObject(refUserWindow,"3E");
    refUserLayout.cells("a").hideHeader(true);
    refUserLayout.cells("b").hideHeader(true);
    refUserLayout.cells("c").hideHeader(true);
    refUserLayout.cells("a").setHeight(20);
    refUserLayout.cells("a").fixSize(false,true);
    refUserLayout.cells("c").setHeight(20);
    refUserLayout.hideSpliter();//移除分界边框。
    refUserLayout.hideConcentrate();
    //加载查询表单
    var queryform = refUserLayout.cells("a").attachForm([
        {type:"settings",position: "label-left", labelWidth: 30, inputWidth: 50},
        {type:"input",label:"姓名：",name:"userName"},
        {type:"newcolumn"},
        {type:"input",label:"地域：",name:"zone"},
        {type:"newcolumn"},
        {type:"input",label:"部门：",name:"dept"},
        {type:"newcolumn"},
        {type:"input",label:"岗位：",name:"station"},
        {type:"newcolumn"},
        {type:"hidden",name:"reportId",value:reportId},
        {type:"button",name:"query",value:"查询"}
    ]);
    //定义loadUser Action的参数来源于表单queryform数据。
    loadUserParam.setParamConfig([{
        index:0,type:"fun",value:function(){
            var formData = queryform.getFormData();
            formData.userName = Tools.trim(queryform.getInput("userName").value);
            return formData;
        }
    }]);
    //添加加载用户事件
    dwrCaller.addAutoAction("queryRefUser", "MyCreateAction.queryRefUser",loadUserParam);
    dwrCaller.addDataConverter("queryRefUser",userDataConverter);
        //加载部门树
    loadDeptTreeChkbox(null,queryform);
    //加载地域树
    //alert(user.zoneId);

    loadZoneTreeChkBox(user.zoneId,queryform);
    //加载岗位树
    loadStationTreeChkBox(null,queryform);

    var btnFormData =
        [
            {type: "settings", position: "label-left"},
            {type:"block",offsetTop:5,inputTop :5,list:[
                {type: "settings", position: "label-left"},
                {type:"button",label:"确定",name:"save",value:"确定",offsetLeft:255}
            ]}
        ];
    var btnForm = refUserLayout.cells("c").attachForm(btnFormData);
    btnForm.attachEvent("onButtonClick", function(id){
        if(id=="save"){
            var ids = refUserGrid.getCheckedRows(0);
//            $("refUserIds").value = ids+",";
            if(ids != ""){
                var idArray = [];
                if(ids.indexOf(',')>0){
                    idArray = ids.split(",");
                }else{
                    idArray = [ids];
                }
                var refUserStr = $("reportUser").innerHTML;
                var refIds = $("refUserIds").value;
                for(var i=0; i<idArray.length; i++){
                    if($("refUserIds").value.indexOf(idArray[i]+",")>-1){
                        continue;
                    }
                    refIds = refIds + idArray[i] + ",";
                    var refRowData = refUserGrid.getRowData(idArray[i]);
                    var tmpStr = '<div id="#ID">#VALUE</div>';
                    var valStr = refRowData.data[1] + "--" + refRowData.userdata.zoneName
                        + "--" + refRowData.userdata.deptName + "--" + refRowData.userdata.stationName
                        + '<img src="cancel.png" height="16" onclick="removeOneUser('+idArray[i]+')" width="16" alt="删除该用户" title="删除该用户">'+'；';
                    tmpStr = tmpStr.replaceAll("#ID",'U_'+idArray[i]).replaceAll("#VALUE",valStr);
                    refUserStr = refUserStr + tmpStr;
                }
                $("refUserIds").value = refIds;
                $("reportUser").innerHTML = refUserStr;
            }
            refUserWindow.close();
        }
    });

    var refUserGrid=refUserLayout.cells("b").attachGrid();
    refUserGrid.setHeader("{#checkBox},姓名,地域,部门,岗位");
    refUserGrid.setHeaderBold();
    refUserGrid.setInitWidthsP("8,22,20,25,25");
    refUserGrid.setColAlign("center,left,left,left,left");
    refUserGrid.setHeaderAlign("center,left,center,center,center");
    refUserGrid.setColTypes("ch,ro,ro,ro,ro");
    refUserGrid.enableCtrlC();
    refUserGrid.setColSorting("na,na,na,na,na");
    refUserGrid.enableMultiselect(true);
    refUserGrid.enableSelectCheckedBoxCheck(1);
//    refUserGrid.setSkin("xp");
    refUserGrid.init();
    refUserGrid.defaultPaging(10);
    refUserGrid.load(dwrCaller.queryRefUser,"json");

    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {
            //进行数据查询。
            refUserGrid.clearAll();
            refUserGrid.load(dwrCaller.queryRefUser, "json");
        }
    });
    // 添加Enter查询事件
    queryform.getInput("userName").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            refUserGrid.clearAll();
            refUserGrid.load(dwrCaller.queryRefUser, "json");
        }
    }
};

var removeOneUser = function(obj){
    $("reportUser").removeChild($("U_"+obj));
    $("refUserIds").value = $("refUserIds").value.replaceAll(obj+',',"");
};





/**
 * 定义全局的部门、岗位、地域树
 */
var globDeptTree=null;
var globStationTree = null;
var globZoneTree = null;
var user= getSessionAttribute("user");

/**
 * User表Data转换器
 */
var userConvertConfig = {
    idColumnName:"userId",
    filterColumns:["ischecked","userNamecn","zoneName","deptName","stationName"]
};
/**
 * JS内部类 用于数据转换
 */
var userDataConverter=new dhtmxGridDataConverter(userConvertConfig);


dwrCaller.addAutoAction("loadDeptTree","DeptAction.queryDeptByPath");
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptId",pidColumn:"parentId",
    isDycload:false,textColumn:"deptName"
});
dwrCaller.addDataConverter("loadDeptTree",treeConverter);
//树动态加载Action
dwrCaller.addAction("querySubDept",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},treeConverter,false);
    DeptAction.querySubDept(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
/**
 * 部门树input框Html。
 * @param selectDept 已经选择了的部门。
 */
var loadDeptTree=function(selectDept,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectDept=selectDept|| global.constant.defaultRoot;
//    var beginId=global.constant.defaultRoot;
    var beginId=1;
    //创建tree Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target=form.getInput("dept");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
//    tree.enableSmartRendering();
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubDept);
    //树双击鼠标事件
    tree.attachEvent("onDblClick",function(nodeId){
        form.setFormData({dept:tree.getItemText(nodeId),deptId:nodeId});
        //关闭树
        div.style.display="none";
    });
    dwrCaller.executeAction("loadDeptTree",beginId,selectDept,function(data){
        tree.loadJSONObject(data);
        globDeptTree = tree;
        if(selectDept){
            tree.selectItem(selectDept); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"focus",function(){
            div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    });
    div.style.display="none";
}

//为查询时可多选，新添加的TREE
var loadDeptTreeChkbox=function(selectDept,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectDept=selectDept|| global.constant.defaultRoot;
//    var beginId=global.constant.defaultRoot;
    var beginId=1;
    //创建tree Div层
    var div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 1px #eee solid;height: 190px;width: 180px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:'_deptDiv'
    });
    document.body.appendChild(div);
    //创建tree div
    var treeDiv = dhx.html.create("div", {
        style:"position:relative;height: 190px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    div.appendChild(treeDiv);
    //创建一个按钮div层
//    var buttonDiv = dhx.html.create("div", {
//        style:"position:relative;border: 1px #eee solid;height: 30px;overflow: auto;padding-top:0px;margin-top: 6px;" +
//              "z-index:1000;padding-left:50px"
//    })
//    div.appendChild(buttonDiv);
    //创建一个button


    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableCheckBoxes(true);
    tree.enableThreeStateCheckboxes(1);
    tree.enableHighlighting(true);
    tree.setDataMode("json");
    tree.attachEvent("onCheck",function(id,state){
        var checkedData = [];
        var allChecked = tree.getAllChecked();
        //寻找ID所有子节点

        var nodes = typeof allChecked == "string" ? allChecked.split(",") : [allChecked];
        var depts = "";
        var deptsId = "";
        for (i = 0;i < nodes.length; i++){
            var scanIds = typeof tree.getAllSubItems(nodes[i]) == "string" ? tree.getAllSubItems(nodes[i]).split(","): [tree.getAllSubItems(nodes[i])];
            if( scanIds.length > 1 ){
                for (j = 0; j< scanIds.length; j++){
                    if(depts == ""){
                        depts =  tree.getItemText(scanIds[j]).toString();
                    }else{

                        depts =   depts + "," +  tree.getItemText(scanIds[j]).toString() ;
                    }
                    if(deptsId == ""){
                        deptsId = scanIds[j].toString();
                    }else{
                        deptsId = deptsId   + "," + scanIds[j].toString();
                    }
                }
            }else{
                if(depts == ""){
                    depts =  tree.getItemText(nodes[i]).toString();
                }else{
                    depts =   depts + "," +  tree.getItemText(nodes[i]).toString() ;
                }
                if(deptsId == ""){
                    deptsId = nodes[i].toString();
                }else{
                    deptsId = deptsId   + "," + nodes[i].toString();
                }
            }



        }
        if(depts == 0){
            depts = "";
            deptsId = null;
        }

        form.setFormData({dept:depts,deptId:deptsId});
//        div.style.display = "none";
    });
    tree.attachEvent("onSelect",function(id){
        tree.setCheck(id,1);
        var checkedData = [];
        var allChecked = tree.getAllChecked();
        //寻找ID所有子节点

        var nodes = typeof allChecked == "string" ? allChecked.split(",") : [allChecked];
        var depts = "";
        var deptsId = "";
        for (i = 0;i < nodes.length; i++){
            var scanIds = typeof tree.getAllSubItems(nodes[i]) == "string" ? tree.getAllSubItems(nodes[i]).split(","): [tree.getAllSubItems(nodes[i])];
            if( scanIds.length > 1 ){
                for (j = 0; j< scanIds.length; j++){
                    if(depts == ""){
                        depts =  tree.getItemText(scanIds[j]).toString();
                    }else{

                        depts =   depts + "," +  tree.getItemText(scanIds[j]).toString() ;
                    }
                    if(deptsId == ""){
                        deptsId = scanIds[j].toString();
                    }else{
                        deptsId = deptsId   + "," + scanIds[j].toString();
                    }
                }
            }else{
                if(depts == ""){
                    depts =  tree.getItemText(nodes[i]).toString();
                }else{
                    depts =   depts + "," +  tree.getItemText(nodes[i]).toString() ;
                }
                if(deptsId == ""){
                    deptsId = nodes[i].toString();
                }else{
                    deptsId = deptsId   + "," + nodes[i].toString();
                }
            }



        }
        if(depts == 0){
            depts = "";
            deptsId = null;
        }

        form.setFormData({dept:depts,deptId:deptsId});
//        div.style.display = "none";
    });


    tree.setXMLAutoLoading(dwrCaller.querySubDept);
//    div.style.display = "none";
    var that = this;

    //移动节点位置至指定节点下。
    var target=form.getInput("dept");
    //target.readOnly=true;


    dwrCaller.executeAction("loadDeptTree",beginId,selectDept,function(data){
        tree.loadJSONObject(data);
        globDeptTree = tree;
        if(selectDept){
            tree.selectItem(selectDept); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"focus",function(){
            //div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    });
    div.style.display="none";
}

dwrCaller.addAutoAction("loadStationTree","StationAction.queryStationByPath");
var stationConverter=dhx.extend({idColumn:"stationId",pidColumn:"parStationId",
    textColumn:"stationName"
},treeConverter,false);
dwrCaller.addDataConverter("loadStationTree",stationConverter);
//树动态加载Action
dwrCaller.addAction("querySubStation",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},stationConverter,false);
    StationAction.querySubStation(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});

/**
 * 岗位input输入框Html
 * @param name
 * @param value
 */
var loadStationTree=function(selectStation,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectStation=selectStation|| global.constant.defaultRoot;
//    var beginId=global.constant.defaultRoot;
    var beginId=1;
    //创建tree Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target=form.getInput("station");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
//    tree.enableSmartRendering();
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubStation);
    //树双击鼠标事件
    tree.attachEvent("onDblClick",function(nodeId){
        form.setFormData({station:tree.getItemText(nodeId),stationId:nodeId});
        //关闭树
        div.style.display="none";
    });
    dwrCaller.executeAction("loadStationTree",beginId,selectStation,function(data){
        tree.loadJSONObject(data);
        globStationTree = tree;
        if(selectStation){
            tree.selectItem(selectStation); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"focus",function(){
            div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    })
    div.style.display="none";
}
//添加岗位可以多选
var loadStationTreeChkBox=function(selectStation,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectStation=selectStation|| global.constant.defaultRoot;
//    var beginId=global.constant.defaultRoot;
    var beginId=1;
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;width: 180px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:"_stationDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"display;none;position:relaive;height:200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    div.appendChild(treeDiv);
//    //创建buttondiv
//    var butDiv=dhx.html.create("div",{
//        style:"position:relative;height:30px;overflow:auto;padding:0;margin:0;padding-top:0xp;margin-top:6xp" +
//              "z-index:1000;margin-left:55px"
//    })
//    div.appendChild(butDiv);

    //移动节点位置至指定节点下。
    var target=form.getInput("station");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableCheckBoxes(true);
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.attachEvent("onCheck",function(id,state){
        var checkedData = tree.getAllChecked();
        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
        var stations = "";
        var stationsId ="";
        for (i = 0;i< nodes.length;i++){
            if(stations == ""){
                stations =  tree.getItemText(nodes[i]).toString();
            }else{
                stations =   stations + "," +  tree.getItemText(nodes[i]).toString() ;
            }
            if(stationsId == ""){
                stationsId = nodes[i].toString();
            }else{
                stationsId = stationsId   + "," + nodes[i].toString();
            }
        }
        if(stations == 0){
            stations = "";
            stationsId = null;
        }
        form.setFormData({station:stations,stationId:stationsId});
//        div.style.display = "none";
    });
    tree.attachEvent("onSelect",function(id){
        tree.setCheck(id,1);
        var checkedData = tree.getAllChecked();
        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
        var stations = "";
        var stationsId ="";
        for (i = 0;i< nodes.length;i++){
            if(stations == ""){
                stations =  tree.getItemText(nodes[i]).toString();
            }else{
                stations =   stations + "," +  tree.getItemText(nodes[i]).toString() ;
            }
            if(stationsId == ""){
                stationsId = nodes[i].toString();
            }else{
                stationsId = stationsId   + "," + nodes[i].toString();
            }
        }
        if(stations == 0){
            stations = "";
            stationsId = null;
        }
        form.setFormData({station:stations,stationId:stationsId});
//        div.style.display = "none";
    });


    tree.setXMLAutoLoading(dwrCaller.querySubStation);
    dwrCaller.executeAction("loadStationTree",beginId,selectStation,function(data){
        tree.loadJSONObject(data);
        globStationTree = tree;
        if(selectStation){
            tree.selectItem(selectStation); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"focus",function(){
            //div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    })
    div.style.display="none";
}


dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPath");
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
    //创建buttondiv
//    var butDiv=dhx.html.create("div",{
//        style:"position:relative;border: 1px #eee solid;border-bottom:0px;height:30px;overflow:auto;;padding-top:0xp;margin-top:6xp" +
//              "z-index:1000;padding-left:55px"
//    })
//    div.appendChild(butDiv);

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

var loadZoneTree=function(selectZone,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectZone=selectZone|| global.constant.defaultRoot;
    var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)
            || global.constant.defaultRoot;
    //创建tree Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target=form.getInput("zone");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
//    tree.enableSmartRendering();
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubZone);
    //树双击鼠标事件
    tree.attachEvent("onDblClick",function(nodeId){
        form.setFormData({zone:tree.getItemText(nodeId),zoneId:nodeId});
        //关闭树
        div.style.display="none";
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
        Tools.addEvent(target,"focus",function(){
            div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    })
    div.style.display="none";
}

dhx.ready(myCreateInit);
