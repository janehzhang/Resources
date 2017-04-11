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
 *       2012-04-11
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
    filterColumns:["reportName","userNamecn","zoneName","deptName","favTime","pushTime","sendBaseTime","sendSequnce","_buttons"],
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        if(columnName=="reportName"){
            var tmp = "'"+cellValue+"'";
            return '<a href="javascript:openReport('+rowData.reportId+','+tmp+')">'+cellValue+'</a>'+(rowData.reportState==0?"（已下线）":"");
        }
        if(columnName == '_buttons') {
            if(rowData.isPushed){//是否订阅
                return "getButtonsCol_CANCLE";//已订阅
            }else{
                return "getButtonsCol_PUSH";//未订阅
            }
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    },
    userData:function(rowIndex, rowData) {
        var userData = {};
        userData["reportName"] = rowData["reportName"];
        userData["favTime"] = rowData["favTime"];
        userData["isPushed"] = rowData["isPushed"];
        userData["pushConfigId"] = rowData["pushConfigId"];
        userData["pushTime"] = rowData["pushTime"];
        userData["pushType"] = rowData["pushType"];
        userData["reportFavoriteId"] = rowData["reportFavoriteId"];
        userData["reportId"] = rowData["reportId"];
        userData["reportName"] = rowData["reportName"];
        userData["reportNote"] = rowData["reportNote"];
        userData["reportState"] = rowData["reportState"];
        userData["sendBaseTime"] = rowData["sendBaseTime"];
        userData["sendSequnce"] = rowData["sendSequnce"];
        userData["userNamecn"] = rowData["userNamecn"];
        userData["zoneName"] = rowData["zoneName"];
        return userData;
    }
};

var convert = new dhtmxGridDataConverter(convertConfig);

/**
 * 初始化方法
 */
var favoriteInit = function(){
    var favoriteLayout = new dhtmlXLayoutObject(document.getElementById("container"), "2E");
    favoriteLayout.cells("a").setText("我收藏的报表");
    favoriteLayout.cells("b").hideHeader();
    favoriteLayout.cells("a").setHeight(75);
    favoriteLayout.cells("a").fixSize(false, true);
    favoriteLayout.hideConcentrate();
    favoriteLayout.hideSpliter();//移除分界边框。

    //加载查询表单
    var queryform = favoriteLayout.cells("a").attachForm([
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
    dwrCaller.addAutoAction("queryMyFavolite", "MyFavoliteAction.queryMyFavolite", loadParam);
    dwrCaller.addDataConverter("queryMyFavolite", convert);

    window["getButtonsCol_PUSH"]=function(){
        return [
            {name:"deleteFav",text:"删除收藏",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
                imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                    deleteFav(rowData);
                }
            },
            {name:"doPush",text:"订阅报表",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
                imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                    doPush(rowData);
                }
            }
        ];
    };
    window["getButtonsCol_CANCLE"]=function(){
        return [
            {name:"deleteFav",text:"删除收藏",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
                imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                    deleteFav(rowData);
                }
            },
            {name:"cancelPush",text:"取消订阅",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
                imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                    cancelPush(rowData);
                }
            }
        ];
    };

    mygrid = favoriteLayout.cells("b").attachGrid();
    mygrid.setHeader("报表名称,创建人,创建地域,创建部门,收藏时间,订阅时间,发送时间,发送频率,操作");
    mygrid.setInitWidthsP("15,9,10,10,12,12,12,7,13");
    mygrid.setColAlign("left,left,left,left,center,center,center,left,center");
    mygrid.setHeaderAlign("left,center,center,center,center,center,center,center,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,sb");
//    mygrid.setSkin("xp");
    mygrid.setColumnCustFormat(7,sendSeqTran);
    mygrid.init();
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.queryMyFavolite, "json");

        //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {
            //进行数据查询。
            mygrid.clearAll();
            mygrid.load(dwrCaller.queryMyFavolite, "json");
        }
    });
    // 添加Enter查询事件
    queryform.getInput("keyWord").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            mygrid.clearAll();
            mygrid.load(dwrCaller.queryMyFavolite, "json");
        }
    }
};

var pushWindow = null;

/**
 * 执行订阅
 * @param rowData
 */
var typeArr = undefined;
var doPush = function(rowData){
    //根据 报表先取得报表的发送方式
	var rptSubscribeType_VAL = null;
	CommentRptAction.getRptInfo(rowData.id,	
		{async:false,
		callback:function(data){
        if(data){
            rptSubscribeType_VAL = data.SUBSCRIBE_TYPE;
            if(rptSubscribeType_VAL!=null){   //获取当前的发送方式
             typeArr = rptSubscribeType_VAL.split(",");
            }else{
            	alert("当前报表未设置发送方式,将显示所有的发送方式,如有问题,请联系管理员");
            	$("isMail").style.display="inline";
        	    $("isMailSpan").style.display="inline";
        	    $("isMMS").style.display="inline";
        		$("isMMSpan").style.display="inline";
        		$("isMS").style.display="inline";
        		$("isMSpan").style.display="inline";
            }
        }}});
    if(!pushWindow){
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("pushWindow", 0, 0, 500, 285);
        pushWindow = dhxWindow.window("pushWindow");
        pushWindow.setModal(true);
        pushWindow.stick();
        pushWindow.setDimension(500, 285);
        pushWindow.center();
        pushWindow.denyResize();
        pushWindow.denyPark();
        pushWindow.button("minmax1").hide();
        pushWindow.button("park").hide();
        pushWindow.button("stick").hide();
        pushWindow.button("sticked").hide();
        pushWindow.setModal(true);
        pushWindow.keepInViewport(true);
        pushWindow.attachEvent("onClose", function(){
            pushWindow.hide();
            pushWindow.setModal(false);
            return false;
        });
        pushWindow.setText("报表订阅设置");
        pushWindow.show();
        var pushLayout = new dhtmlXLayoutObject(pushWindow, "2E");
        pushLayout.cells("a").hideHeader();
        pushLayout.cells("a").attachObject($("_pushDetail"));
        pushLayout.cells("a").setHeight(260);
        pushLayout.cells("b").hideHeader();
        pushLayout.hideConcentrate();
        pushLayout.hideSpliter();//移除分界边框。
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
        var btnForm = pushLayout.cells("b").attachForm(btnFormData);
        btnForm.attachEvent("onButtonClick", function(id){
            if(id=="save"){
                var pushType = "";
                if($("isMail").checked){
                    pushType = pushType + "1,";
                }
                if($("isMMS").checked){
                    pushType = pushType + "2,";
                }
                if($("isMS").checked){
                    pushType = pushType + "3,";
                }
                if(pushType.length > 0){
                    pushType = pushType.substring(0, pushType.length - 1);
                }
                if(pushType == ""){
                    dhx.alert("请选择一个发送方式！");
                    return;
                }
                if($("sendBaseTime").value==""){
                    dhx.alert("请选择一个起始发送时间！");
                    return;
                }
                var sendSequnce = "";
                if($("sendType").value=="1"){
                    sendSequnce = 0;
                }else{
                    sendSequnce = $("sendSequnce").value;
                }
                var data = {
                    sendBaseTime:$("sendBaseTime").value,
                    pushType:pushType,
                    sendSequnce:sendSequnce,
                    reportFavoriteId:pushWindow._reportFavoriteId,
                    reportId:pushWindow._reportId
                };
                MyFavoliteAction.doPush(data, function(data){
                    if(data){
                        dhx.alert("订阅成功！");
                        mygrid.updateGrid(convert.convert(data), "update");
                        pushWindow.close();
                    }else{
                        alert("订阅失败，请重试！");
                    }
                });
            }
            if(id=="close"){
                pushWindow.close();
            }
        });
    }else{
        pushWindow.setModal(true);
        pushWindow.show();
    }
    if(typeArr!=undefined){
        	for(var i= 0 ;i<typeArr.length;i++){
        		var value = typeArr[i];
        		if(value==1){
        			$("isMail").style.display="inline";
        			$("isMailSpan").style.display="inline";
        		}
        		if(value==2){
        			$("isMMS").style.display="inline";
        			$("isMMSpan").style.display="inline";
        		}
        		if(value==3){
        			$("isMS").style.display="inline";
        			$("isMSpan").style.display="inline";
        		}
        	}
        }
    pushWindow._reportFavoriteId = rowData.userdata.reportFavoriteId;
    pushWindow._reportId = rowData.id;
    //初始化日期控件
    var calander = buildCalander(function () {
        $("sendBaseTime").value = calander.calendar.getDate(true);
        calander.style.display = "none";
    });
    //日期控件展现事件
    $("sendBaseTime").onclick = function (e) {
        e = e || window.event;
        var target = e.srcElement || e.target;
        Tools.divPromptCtrl(calander, target, true);
    };
    if(rowData.userdata.reportName){
        $("reportName").innerHTML = rowData.userdata.reportName;
    }
    if(rowData.userdata.reportNote){
        $("reportNote").value = rowData.userdata.reportNote;
    }else{
        $("reportNote").value = "";
    }

    if(rowData.userdata.pushType){
        if(rowData.userdata.pushType.indexOf("1")>-1){
            $("isMail").cheched = true;
        }
        if(rowData.userdata.pushType.indexOf("2")>-1){
            $("isMMS").cheched = true;
        }
        if(rowData.userdata.pushType.indexOf("3")>-1){
            $("isMS").cheched = true;
        }
    }
    if(rowData.userdata.sendSequnce != null){
        if(rowData.userdata.sendSequnce == 0){//一次发送
            $("sendType").value = 1;
            $("sendSeqTr").style.cssText = "display:none;";
            $("tmpSendTr").style.cssText = "display:;";
        }
        if(rowData.userdata.sendSequnce > 0){
            $("sendType").value = 2;
            $("sendSeqTr").style.cssText = "display:;"; //显示频度
            $("tmpSendTr").style.cssText = "display:none;";
            if(rowData.userdata.sendSequnce){
                $("sendSequnce").value=rowData.userdata.sendSequnce;
            }
        }
    }
    if(rowData.userdata.sendBaseTime){
        $("sendBaseTime").value = rowData.userdata.sendBaseTime;
    }
    typeArr = undefined;
};

//发送类型变化
var sendTypeChange = function(obj){
    if(obj.value==1){//一次发送
        $("sendSeqTr").style.cssText = "display:none;";
        $("tmpSendTr").style.cssText = "display:;";
    }else{
        $("sendSeqTr").style.cssText = "display:;"; //显示频度
        $("tmpSendTr").style.cssText = "display:none;";
    }
};

//取消订阅
var cancelPush = function(rowData){
    dhx.confirm("是否取消该订阅？",function(r){
        if(r){
            MyFavoliteAction.cancelPush(rowData.userdata.pushConfigId, rowData.userdata.reportId, function(data){
                if(data){
                    dhx.alert("取消成功！");
                    mygrid.updateGrid(convert.convert(data), "update");
                }else{
                    dhx.alert("操作失败，请重试！");
                }
            });
        }
    });
};

/**
 * 删除收藏
 * @param rowData
 */
var deleteFav = function(rowData){
    dhx.confirm("是否删除该收藏？", function(r){
        if(r){
            MyFavoliteAction.deleteFav(rowData.userdata.reportFavoriteId,rowData.userdata.pushConfigId?rowData.userdata.pushConfigId:-1,
                rowData.userdata.reportId,
                function(r){
                if(r){
                    dhx.alert("删除成功！");
                    mygrid.updateGrid([{id:rowData.userdata.reportId}], "delete");
                }else{
                    dhx.alert("删除失败，请重试！");
                }
            });
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
 * 生成一个日期选择框
 */
var buildCalander = function (callback) {
    var div = document.createElement("div");
    div.style.cssText = "height: 200px;width: 200px;position: absolute;z-index: 1000;display:none";
    div.id = "_timeCalader_" + dhx.uid();
    document.body.appendChild(div);
    var myCalendar = new dhtmlXCalendarObject(div);
    myCalendar.loadUserLanguage('zh');
    myCalendar.setDateFormat("%Y-%m-%d %H:%i");
    myCalendar.setSensitiveRange(new Date(), null);
    myCalendar.show();
    myCalendar.attachEvent("onClick", callback);
    div.calendar = myCalendar;
    return div;
};

var sendSeqTran = function(data){
    if(data==0){
        return '只发送一次';
    }else if(data==5){
        return '每天一次';
    }else if(data==4){
        return '每周一次';
    }else if(data==3){
        return '每月一次';
    }else if(data==2){
        return '半年一次';
    }else if(data==1){
        return '一年一次';
    }else{
        return '';
    }


}
dhx.ready(favoriteInit);