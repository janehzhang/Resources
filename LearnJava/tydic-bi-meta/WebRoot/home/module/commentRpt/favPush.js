/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        favPush.js.js
 *Description：
 *
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

dhtmlXCalendarObject.prototype.langData["zh"] = {
    dateformat:'%Y%m%d', //2011-08
    monthesFNames:["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    monthesSNames:["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"],
    daysFNames:["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
    daysSNames:["日", "一", "二", "三", "四", "五", "六"],
    weekstart:7
};

/**
 * 生成一个日期选择框
 */
var buildCalander = function (inp,fmt) {
    var div = document.createElement("div");
    div.style.cssText = "height: 200px;width: 200px;position: absolute;z-index: 1000;display:none";
    div.id = "_timeCalader_" + dhx.uid();
    document.body.appendChild(div);
    var myCalendar = new dhtmlXCalendarObject(div);
    myCalendar.loadUserLanguage('zh');
    myCalendar.show();
    myCalendar.setDateFormat(fmt||'%Y-%m-%d %H:%i:%s');
    myCalendar.attachEvent("onClick", function(date){
        var time = this.getDate(true);
        div.style.display = "none";
        $(inp).value = time;
    });
    Tools.addEvent($(inp), "click", function(){
        Tools.divPromptCtrl(div, $(inp), true);
        div.style.display = "block";
    });
    return myCalendar;
};

//提交收藏订阅操作
var submitFavOpt = function(){
    var data = getData();
    if(data!=null){
        data.rptId = rptId_VAL;
        if(rptFavInfo_VAL){
            data.favId = rptFavInfo_VAL["favId"];
            if(rptFavInfo_VAL["pushId"]){
                data.pushId = rptFavInfo_VAL["pushId"] ;
            }
        }
        dhx.showProgress("提交数据中");
        CommentRptAction.favPushOpt(data,function(ret){
            dhx.closeProgress();
            hideFavPushRpt();
            if(ret!=-1){
                alert("操作成功");
                initFavPushInfo();
                if(data.optId.indexOf("0")!=-1){
                    rptFavTree_VAL.reLoad();
                }
            }else{
                alert("出错");
            }
        });
    }
};

//初始收藏信息(从后台)
var initFavPushInfo = function(){
    CommentRptAction.getRptFavPushInfo(rptId_VAL,function(data){
        if(data){
            rptFavInfo_VAL = {};
            rptFavInfo_VAL["favId"] = data["REPORT_FAVORITE_ID"];
            rptFavInfo_VAL["favTypeId"] = data["FAVORITE_ID"];
            rptFavInfo_VAL["favTypeName"] = data["FAVORITE_NAME"];
            if(data["PUSH_CONFIG_ID"]){
                rptFavInfo_VAL["pushId"] = data["PUSH_CONFIG_ID"];
                rptFavInfo_VAL["send1"] = data["sendMethod1"];
                rptFavInfo_VAL["send2"] = data["sendMethod2"];
                rptFavInfo_VAL["send3"] = data["sendMethod3"];
                rptFavInfo_VAL["sendFlag"] = data["SEND_SEQUNCE"];
                rptFavInfo_VAL["sendTime"] = data["sendTime"];
            }
        }else{
            rptFavInfo_VAL = null;
        }
        initFavPush();
    });
};

//弹出层收藏订阅报表
var showFavPushRpt = function(){
    var favDIV = document.getElementById("favDIV");
    document.getElementById("touming").style.display = "block";
    favDIV.style.display = "block";
    if(!rptFavInfo_VAL){
        if(!document.getElementById("favTypeTreeDIV")){
            var mw = document.body.offsetWidth;
            var mh = document.body.offsetHeight;
            mh = Math.max(document.body.clientHeight,mh);
            mh = Math.max(document.documentElement.clientHeight,mh);
            favDIV.style.left = (mw-favDIV.offsetWidth)/2  + "px";
            favDIV.style.top = ((mh-favDIV.offsetHeight)/2 - 150) + "px";

            if(rptSubscribeType_VAL){
                if(rptSubscribeType_VAL.indexOf("1")!=-1){
                    document.getElementById("fs1").style.display = "block";
                }
                if(rptSubscribeType_VAL.indexOf("2")!=-1){
                    document.getElementById("fs2").style.display = "block";
                }
                if(rptSubscribeType_VAL.indexOf("3")!=-1){
                    document.getElementById("fs3").style.display = "block";
                }
            }else{
                document.getElementById("fs1").style.display = "block";
                document.getElementById("fs2").style.display = "block";
                document.getElementById("fs3").style.display = "block";
            }

            var reportSendType = getComboByRemoveValue("PERORT_SEND_TYPE",0);//0一次，1年，2半年，3月，4周，5天
            Tools.addOption(document.getElementById("sendSequnce"),reportSendType.options);

//            document.getElementById("sendSequnce").options[0].value = rptAuditType_VAL;
//            document.getElementById("sendSequnce").options[0].innerHTML = (rptAuditType_VAL==11?"按天":"按月");
            var tomorrow = new Date();
            tomorrow.setDate(new Date().getDate()+1);
            var dataCalendar = buildCalander("fixedTime");
            dataCalendar.setSensitiveRange(tomorrow, null);
            var dataCalendar1 = buildCalander("sendTime");
            dataCalendar1.setSensitiveRange(tomorrow, null);

            rptFavTree_VAL = loadFavTypeTree();
        }

        initFavPushInfo();
    }else{
        initFavPush();
    }
};
//初始收藏订阅信息
//opt0、1、2、3、4  分别表示（收藏，取消收藏，订阅，取消订阅,修改）
var initFavPush = function(){
    if(rptAuditType_VAL==11){
        document.getElementById("sendSequnce").value = "5";
    }else if(rptAuditType_VAL==22){
        document.getElementById("sendSequnce").value = "3";
    }else{
        document.getElementById("sendSequnce").value = "5";//默认每天发
    }
    if(rptFavInfo_VAL){  //已收藏
        document.getElementById("favTypeId").value = rptFavInfo_VAL["favTypeId"];
        document.getElementById("favTypeName").value = rptFavInfo_VAL["favTypeName"];
        document.getElementById("favTypeName").readOnly = true;
        if(rptFavInfo_VAL["pushId"]){//已订阅
            document.getElementById("favtitle").innerHTML = "已收藏，已订阅";
            document.getElementById("sendMethod1").checked = (rptFavInfo_VAL["send1"]!=null);
            document.getElementById("sendMethod2").checked = (rptFavInfo_VAL["send2"]!=null);
            document.getElementById("sendMethod3").checked = (rptFavInfo_VAL["send3"]!=null);
            if(rptFavInfo_VAL["sendFlag"]>0){
                document.getElementById("typeFlag1").checked = true;
                document.getElementById("sendSequnce").value = rptFavInfo_VAL["sendFlag"];
                document.getElementById("sendTime").value = rptFavInfo_VAL["sendTime"];
                document.getElementById("fixedTime").value = "";
                clickSendFlag(document.getElementById("typeFlag1"));
            }else{
                document.getElementById("typeFlag2").checked = true;
                document.getElementById("fixedTime").value = rptFavInfo_VAL["sendTime"];
                document.getElementById("sendTime").value = "";
                clickSendFlag(document.getElementById("typeFlag2"));
            }

            document.getElementById("os0").style.display = "none";
            document.getElementById("os1").style.display = "block";
            document.getElementById("os2").style.display = "none";
            document.getElementById("os3").style.display = "block";
            document.getElementById("os4").style.display = "block";
            document.getElementById("opt0").checked = false;
            document.getElementById("opt1").checked = false;
            document.getElementById("opt2").checked = false;
            document.getElementById("opt3").checked = false;
            document.getElementById("opt4").checked = true;
            document.getElementById("pushinfo").style.display = "";
        }else{//未订阅
            document.getElementById("favtitle").innerHTML = "已收藏，未订阅";
            document.getElementById("sendMethod1").checked = true;
            document.getElementById("sendMethod2").checked = false;
            document.getElementById("sendMethod3").checked = false;
            document.getElementById("typeFlag1").checked = true;
            clickSendFlag(document.getElementById("typeFlag1"));
            document.getElementById("sendTime").value = "";
            document.getElementById("fixedTime").value = "";


            document.getElementById("os0").style.display = "none";
            document.getElementById("os1").style.display = "block";
            document.getElementById("os2").style.display = "block";
            document.getElementById("os3").style.display = "none";
            document.getElementById("os4").style.display = "none";
            document.getElementById("opt0").checked = false;
            document.getElementById("opt1").checked = false;
            document.getElementById("opt2").checked = false;
            document.getElementById("opt3").checked = false;
            document.getElementById("opt4").checked = true;
            document.getElementById("pushinfo").style.display = "none";
        }
    }else{  //未收藏
        document.getElementById("favTypeId").value = "0";
        document.getElementById("favTypeName").value = "";
        document.getElementById("favtitle").innerHTML = "未收藏，未订阅";
        document.getElementById("sendMethod1").checked = true;
        document.getElementById("sendMethod2").checked = false;
        document.getElementById("sendMethod3").checked = false;
        document.getElementById("typeFlag1").checked = true;
        clickSendFlag(document.getElementById("typeFlag1"));
        document.getElementById("sendTime").value = "";
        document.getElementById("fixedTime").value = "";

        document.getElementById("os0").style.display = "block";
        document.getElementById("os1").style.display = "none";
        document.getElementById("os2").style.display = "block";
        document.getElementById("os3").style.display = "none";
        document.getElementById("os4").style.display = "none";
        document.getElementById("opt0").checked = true;
        document.getElementById("opt1").checked = false;
        document.getElementById("opt2").checked = false;
        document.getElementById("opt3").checked = false;
        document.getElementById("opt4").checked = false;
        document.getElementById("pushinfo").style.display = "none";
        document.getElementById("favTypeName").readOnly = false;
    }
};
//点击不同操作联动
var clickOptSet = function(chk,optid){
    if(optid==0 && !chk.checked){  //收藏未选中
        document.getElementById("opt2").checked = false;
        document.getElementById("pushinfo").style.display = "none";
    }else if(optid==1 && chk.checked){    //取消收藏选中
        document.getElementById("opt2").checked = false;
        document.getElementById("opt4").checked = false;
        document.getElementById("pushinfo").style.display = "none";
        if(document.getElementById("os3").style.display=="block"){
            document.getElementById("opt3").checked = true;
        }
    }else if(optid==2 && chk.checked){//订阅选中
        document.getElementById("opt1").checked = false;
        document.getElementById("pushinfo").style.display = "";
        if(document.getElementById("os0").style.display=="block"){
            document.getElementById("opt0").checked = true;
        }
    }else if(optid==2 && !chk.checked){//订阅未选中
        document.getElementById("opt4").checked = true;
        document.getElementById("pushinfo").style.display = "none";
    }else if(optid==3 && chk.checked){//取消订阅选中
        document.getElementById("opt4").checked = false;
        document.getElementById("pushinfo").style.display = "none";
    }else if(optid==3 && !chk.checked){//取消订阅未选中
        if(document.getElementById("opt1").checked){
            document.getElementById("opt3").checked = true;
            document.getElementById("pushinfo").style.display = "none";
        }
    }else if(optid==4 && chk.checked){ //修改选中
        document.getElementById("opt1").checked = false;
        document.getElementById("opt3").checked = false;
        if(rptFavInfo_VAL["pushId"]){
            document.getElementById("pushinfo").style.display = "";
        }
    }
};
//点击发送方式切换
var clickSendFlag = function(chk){
    if(chk.value=="1"){
        document.getElementById("dstd").className = "rightTd";
        document.getElementById("gdtd").className = "rightTd hs";
        document.getElementById("fixedTime").disabled = true;
        document.getElementById("sendSequnce").disabled = false;
        document.getElementById("sendTime").disabled = false;
    }else{
        document.getElementById("dstd").className = "rightTd hs";
        document.getElementById("gdtd").className = "rightTd";
        document.getElementById("fixedTime").disabled = false;
        document.getElementById("sendSequnce").disabled = true;
        document.getElementById("sendTime").disabled = true;
    }
};
//得到收藏并订阅储存数据
var getData = function(){
    var data = {};
    var optId = "";
    data.favTypeId = document.getElementById("favTypeId").value;
    data.favTypeName = trim(document.getElementById("favTypeName").value);
    if(document.getElementById("opt0").checked){
        optId += "0,";
        if(data.favTypeName==""){
            alert("请选择或输入一个分类！");
            return null;
        }
    }
    if(document.getElementById("opt1").checked){
        optId += "1,";
    }
    if(document.getElementById("opt2").checked){
        optId += "2,";
        data = setDyData(data);
        if(data==null){
            return null;
        }
    }
    if(document.getElementById("opt3").checked){
        optId += "3,";
    }
    if(document.getElementById("opt4").checked){
        optId += "4,";
        if(rptFavInfo_VAL["pushId"]){
            data = setDyData(data);
            if(data==null){
                return null;
            }
        }
    }
    if(optId==""){
        alert("请选择一种操作");
        return null;
    }else{
        data.optId = optId.substr(0,optId.length-1);
        if(data.optId.indexOf("1")!=-1){
            if(!confirm("你确定取消收藏么？"+(data.optId.indexOf("3")!=-1?"取消将连同订阅一起取消！":""))){
                return null;
            }
        }else{
            if(data.optId.indexOf("3")!=-1){
                if(!confirm("你确定取消订阅么？")){
                    return null;
                }
            }
        }
    }
    return data;
};
//获取订阅信息
var setDyData = function(data){
    var pushType = "";
    if(document.getElementById("sendMethod1").checked){	//如果选中，添加值
        pushType += "1,";
    }
    if(document.getElementById("sendMethod2").checked){	//如果选中，添加值
        pushType += "2,";
    }
    if(document.getElementById("sendMethod3").checked){	//如果选中，添加值
        pushType += "3,";
    }
    if(pushType==""){
        alert("请至少选择一种发送方式！");
        return null;
    }
    data.pushType = pushType.substr(0,pushType.length-1);			//保存发送方式
    var typeFlag = document.getElementById("typeFlag1").checked;
    if(typeFlag){
        var sendSequnce = document.getElementById("sendSequnce").value;
        if(!sendSequnce){
            alert("请选择发送频度！");
            return null;
        }
        var sendTime = document.getElementById("sendTime").value;
        if(!sendTime){
            alert("请选择初次发送时间！");
            return null;
        }
        data.sendBaseTime = sendTime;		//保存初次发送时间
        data.sendSequnce = sendSequnce;
    }else{
        var fixedTime = document.getElementById("fixedTime").value;
        if(!fixedTime){
            alert("请选择固定发送时间！");
            return null;
        }
        data.sendBaseTime = fixedTime;		//保存固定发送时间
        data.sendSequnce = 0;				//发送频度为固定时间发送
    }
    return data;
};
//关闭收藏
var hideFavPushRpt = function(){
    var favDIV = document.getElementById("favDIV");
    favDIV.style.display = "none";
    document.getElementById("touming").style.display = "none";
};
//加载收藏分类树
var loadFavTypeTree = function(){
    var div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 1px #a9a9a9 solid;height:200px;overflow: auto;padding: 0;margin: 0;" +
            "z-index:1000"
    });
    document.body.appendChild(div);
    div.id = "favTypeTreeDIV";
    div.style.display = "none";
    var target = document.getElementById("favTypeName");
    var tr = new Tree(div, {
        afterLoad:function(tr){
            tr.openAllItems(0);//展开所有节点
            Tools.addEvent(target, "click", function(){
                div.style.width = (target.offsetWidth-2) + 'px';
                Tools.divPromptCtrl(div, target, true);
                div.style.display = "block";
            });
        }
    });
    tr.enableSingleRadioMode(true);
    tr.attachEvent("onClick",function(nodeId){
        var favT = this.getUserData(nodeId, "typeFlag");
        if(!this.hasChildren(nodeId)){
            if(favT == 0){
                document.getElementById("favTypeId").value = nodeId;
                target.value = this.getItemText(nodeId);
                //关闭树
                div.style.display = "none";
            }else{
                alert("无报表分类，请手动输入");
            }
        }
    });

    var favDirUrl = Tools.dwr({
        dwrMethod:FavoriteAction.queryFavoriteTypeAndDir,
        param:[user.userId,2],
        showProcess:false,
        converter:new dhtmxTreeDataConverter({
            idColumn : "favoriteId",
            pidColumn : "parentId",
            textColumn : "favoriteName"
        })
    });
    tr.loadData(favDirUrl);

    return tr;

};