<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="com.scct.security.decode.DecodeLTPA" %>
<%@page import="tydic.meta.module.mag.user.UserDAO" %>
<%@page import="java.util.*" %>
<%@page import="tydic.meta.module.mag.login.LoginAction" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

    String name = null;
    String value = null;
    String uid = null;
    Enumeration headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
        name = (String) headerNames.nextElement();
        if ("cookie".equals(name)) {
            value = request.getHeader(name);
            int start = value.indexOf("LtpaToken=");
            if (start != -1) {
                value = value.substring(start + 10);
                uid = DecodeLTPA.getUserId(value);
            }

        }

    }
//uid=request.getParameter("name");
    Map<String, Object> logDate = new HashMap<String, Object>();
    UserDAO userDAO = new UserDAO();
    LoginAction loginAction = new LoginAction();
    int id = userDAO.findUserByNewOAUserName(uid);
    if (id == -1) {
        //id = 6666;
//    Map<String,Object>  userDate = new HashMap<String, Object>();
//    userDate.put("uid",id);
//    ILoginType.LoginResult loginResult = loginAction.login(userDate, "oa");

        System.out.println(id);
        //String newOaUserNamecn = request.getParameter("newOaUserNamecn");
        String newOaUserNamecn = request.getParameter("newOaUserNamecn") == null ? "" : request.getParameter("newOaUserNamecn");
        if (!newOaUserNamecn.trim().equals("true")) {
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <base href="<%=basePath%>">

    <title>My JSP 'view_table.jsp' starting page</title>
    <script type="text/javascript" src="<%=path%>/meta/resource/dhtmlx/dhtmlx.js"></script>
    <script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
    <script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/LoginAction.js"></script>
    <script type="text/javascript" src="<%=path %>/dwr/interface/PortalCtrlr.js"></script>
    <script type="text/javascript" src="<%=path %>/portal/resource/js/FusionCharts.js"></script>
    <script type="text/javascript" src="<%=path %>/dwr/interface/MenuVisitLogAction.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=path%>/meta/resource/dhtmlx/dhtmlx.css">
    <style type="text/css">
        a:link {
            text-decoration: none;
        }

        　a:active {
            text-decoration: none
        }

        　a:hover {
            text-decoration: none;
        }

        　a:visited {
            text-decoration: none;
        }
    </style>
</head>

<body style="margin: 0px;padding: 0px;">
<%
    String chatIndex = "";
    try {
        chatIndex = request.getParameter("chartIndexCd").substring(0, 4);
    } catch (Exception e) {
    }
    String chatAll = "";
    if (chatIndex.toUpperCase().trim().equals("YHFZ")) {
        chatAll = 10 + "";
    } else if (chatIndex.toUpperCase().trim().equals("GWYH")) {
        chatAll = 12 + "";
    } else if (chatIndex.toUpperCase().trim().equals("YWLL")) {
        chatAll = 11 + "";
    } else if (chatIndex.toUpperCase().trim().equals("SRYC")) {
        chatAll = 13 + "";
    }

    String strWidth = request.getParameter("width");
    if (strWidth == null || strWidth.equals(""))
        strWidth = "800";

    String strHeight = request.getParameter("height");
    if (strHeight == null || strHeight.equals(""))
        strHeight = "400";
    String chartTabDes;
    if (request.getHeader("User-Agent").contains("MSIE") || request.getHeader("User-Agent").contains("Firefox")) {
        //response.setCharacterEncoding("utf-8");
        //chartTabDes = request.getParameter("chartTabDes");
        chartTabDes = new String(request.getParameter("chartTabDes").getBytes("ISO-8859-1"), "UTF-8");
    } else {
        chartTabDes = new String(request.getParameter("chartTabDes").getBytes("ISO-8859-1"), "UTF-8");
    }
    chartTabDes = (chartTabDes == null) ? "" : chartTabDes;

%>
<input type="hidden" id="localCode_"
       value="<%=request.getParameter("localCode")==null?"":request.getParameter("localCode") %>"/>
<input type="hidden" id="dataDateNo_"
       value="<%=request.getParameter("dataDateNo")==null?"":request.getParameter("dataDateNo") %>"/>
<input type="hidden" id="title_name_" value="<%=chatAll%>"/>
<input type="hidden" id="chartIndexCd_"
       value="<%=request.getParameter("chartIndexCd")==null?"":request.getParameter("chartIndexCd") %>"/>
<input type="hidden" id="chartFieldName_"
       value="<%=request.getParameter("chartFieldName")==null?"":request.getParameter("chartFieldName") %>"/>
<input type="hidden" id="userId" value="<%=id%>">

<div id=charId_ style="width:170px;">
    <div id="_content" style="width:500px;height:400px;padding:0;marggin:0;">
        <div id='chart' style="padding:0px;margin:0px;">
        </div>
        <div style="width:100%;height:20px;padding:0px;margin:0px;display: inline;" id='charRado'>
            <!--`<a href="javascript:void(0)" id='' onclick="chartRadio(this.id)"/><font size="2">当日新增汇总</font></a> -->
            <font size="2" style="color:black;">当日新增（户）：</font><a href="javascript:void(0)" id='YHFZ_IND_1,VALUE2,10'
                                                                  onclick="chartRadio(this.id)"/><font id="1" size="2"
                                                                                                       style="color:red;">移动语音</font></a>
            <a href="javascript:void(0)" id='YHFZ_IND_1,VALUE3,10' onclick="chartRadio(this.id)"/><font id="2" size="2"
                                                                                                        style="color:black;">智能机终端</font></a>
            <a href="javascript:void(0)" id='YHFZ_IND_1,VALUE4,10' onclick="chartRadio(this.id)"/><font id="3" size="2"
                                                                                                        style="color:black;">有线宽带</font></a>
            <a href="javascript:void(0)" id='YHFZ_IND_1,VALUE5,10' onclick="chartRadio(this.id)"/><font id="4" size="2"
                                                                                                        style="color:black;">其中4M以上用户</font></a>
            <br/>
            <font size="2" style="color:black;">当日到达（户）：</font><a href="javascript:void(0)" id='YHFZ_IND_4,VALUE2,10'
                                                                  onclick="chartRadio(this.id)"/><font id="5" size="2"
                                                                                                       style="color:black;">移动语音</font></a>
            <%--<a href="javascript:void(0)" id='YHFZ_IND_4,VALUE3,10' onclick="chartRadio(this.id)"/><font id="6" size="2" style="color:black;">智能机终端</font></a>--%>
            <a href="javascript:void(0)" id='YHFZ_IND_4,VALUE4,10' onclick="chartRadio(this.id)"/><font id="KD7"
                                                                                                        size="2"
                                                                                                        style="color:black;">有线宽带</font></a>
            <a href="javascript:void(0)" id='YHFZ_IND_4,VALUE5,10' onclick="chartRadio(this.id)"/><font id="KD8"
                                                                                                        size="2"
                                                                                                        style="color:black;">其中4M以上用户</font></a>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id='audit10' style='text-align: center;font-size:14px'></span>

        </div>
        <div style="width:100%;height:20px;padding:0px;margin:0px;display: none;text-align: center" align="center"
             id='charRado2'>
            <font size="2" style="color:black;">通话时长（万分钟）：</font>
            <a href="javascript:void(0)" id='YWLL_IND_1,VALUE2,11' onclick="chartRadio(this.id)"/><font id="" size="2"
                                                                                                        style="color:red;">移动</font></a>
            <%--<a href="javascript:void(0)" id='YWLL_IND_1,VALUE3,11' onclick="chartRadio(this.id)"/><font id="GH1" size="2" style="color:black;">3G终端</font></a>--%>
            <br/>
            <font size="2" style="color:black;">当日流量（GB）：</font>
            <a href="javascript:void(0)" id='YWLL_IND_2,VALUE2,11' onclick="chartRadio(this.id)"/><font id="KD1"
                                                                                                        size="2"
                                                                                                        style="color:black;">移动</font></a>
            <a href="javascript:void(0)" id='YWLL_IND_2,VALUE3,11' onclick="chartRadio(this.id)"/><font id="KD4"
                                                                                                        size="2"
                                                                                                        style="color:black;">EVDO流量</font></a>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id='audit11' style='font-size:14px'></span>
        </div>
        <div style="width:100%;height:20px;padding:0px;margin:0px;display: none;text-align: center" align="center"
             id='charRado3'>
            <font size="2" style="color:black;">月净增（户）：</font><a href="javascript:void(0)" id='GWYH_IND_3,VALUE3,12'
                                                                 onclick="chartRadio(this.id)"/><font id="CW1" size="2"
                                                                                                      style="color:red;">电信</font></a>
            <a href="javascript:void(0)" id='GWYH_IND_3,VALUE4,12' onclick="chartRadio(this.id)"/><font id="GH1"
                                                                                                        size="2"
                                                                                                        style="color:black;">移动</font></a>
            <a href="javascript:void(0)" id='GWYH_IND_3,VALUE5,12' onclick="chartRadio(this.id)"/><font id="KD1"
                                                                                                        size="2"
                                                                                                        style="color:black;">联通</font></a>
            &nbsp;<font size="2" style="color:black;">月净增份额（%）：</font><a href="javascript:void(0)"
                                                                         id='GWYH_IND_4,VALUE3,12'
                                                                         onclick="chartRadio(this.id)"/><font id="KD4"
                                                                                                              size="2"
                                                                                                              style="color:black;">电信</font></a>
            <a href="javascript:void(0)" id='GWYH_IND_4,VALUE4,12' onclick="chartRadio(this.id)"/><font id="KD1"
                                                                                                        size="2"
                                                                                                        style="color:black;">移动</font></a>
            <a href="javascript:void(0)" id='GWYH_IND_4,VALUE5,12' onclick="chartRadio(this.id)"/><font id="KD4"
                                                                                                        size="2"
                                                                                                        style="color:black;">联通</font></a>
            <br/>
            <font size="2" style="color:black;">月到达（户）：</font><a href="javascript:void(0)" id='GWYH_IND_5,VALUE3,12'
                                                                 onclick="chartRadio(this.id)"/><font id="CW1" size="2"
                                                                                                      style="color:black;">电信</font></a>
            <a href="javascript:void(0)" id='GWYH_IND_5,VALUE4,12' onclick="chartRadio(this.id)"/><font id="GH1"
                                                                                                        size="2"
                                                                                                        style="color:black;">移动</font></a>
            <a href="javascript:void(0)" id='GWYH_IND_5,VALUE5,12' onclick="chartRadio(this.id)"/><font id="KD1"
                                                                                                        size="2"
                                                                                                        style="color:black;">联通</font></a>
            &nbsp;<font size="2" style="color:black;">月到达份额（%）：</font><a href="javascript:void(0)"
                                                                         id='GWYH_IND_6,VALUE3,12'
                                                                         onclick="chartRadio(this.id)"/><font id="KD4"
                                                                                                              size="2"
                                                                                                              style="color:black;">电信</font></a>
            <a href="javascript:void(0)" id='GWYH_IND_6,VALUE4,12' onclick="chartRadio(this.id)"/><font id="KD1"
                                                                                                        size="2"
                                                                                                        style="color:black;">移动</font></a>
            <a href="javascript:void(0)" id='GWYH_IND_6,VALUE5,12' onclick="chartRadio(this.id)"/><font id="KD4"
                                                                                                        size="2"
                                                                                                        style="color:black;">联通</font></a>
            <span id='audit12' style='margin-left:-8px;padding-left:0px;font-size:8px'></span>
        </div>
        <div style="width:100%;height:20px;padding:0px;margin:0px;display: none;" id='charRado4'>
            <font size="2" style="color:black;">终端销售量(户)：</font>
            <!-- 用字段拼接id @表示为空， 顺序依次为累计、dragon_channel_type字段值、bt_code字段值-->
            <a href="javascript:void(0)" id='0,@,@' onclick="iphoneRodio(this.id)"/><font size="2" style="color:red;">当日新增</font></a>
            <a href="javascript:void(0)" id='1,@,@' onclick="iphoneRodio(this.id)"/><font size="2" style="color:black;">累计到达</font></a>
            &nbsp;<font size="2" style="color:black;">其中电子渠道(户)：</font>
            <a href="javascript:void(0)" id='0,2,@' onclick="iphoneRodio(this.id)"/><font size="2" style="color:black;">当日新增</font></a>
            <a href="javascript:void(0)" id='1,2,@' onclick="iphoneRodio(this.id)"/><font size="2" style="color:black;">累计到达</font></a>
            <br/>
            <font size="2" style="color:black;">其中存费送机(户)：</font>
            <a href="javascript:void(0)" id='0,@,1' onclick="iphoneRodio(this.id)"/><font size="2" style="color:black;">当日新增</font></a>
            <a href="javascript:void(0)" id='1,@,1' onclick="iphoneRodio(this.id)"/><font size="2" style="color:black;">累计到达</font></a>
            <font size="2" style="color:black;">其中购机送费(户)：</font>
            <a href="javascript:void(0)" id='0,@,2' onclick="iphoneRodio(this.id)"/><font size="2" style="color:black;">当日新增</font></a>
            <a href="javascript:void(0)" id='1,@,2' onclick="iphoneRodio(this.id)"/><font size="2" style="color:black;">累计到达</font></a>
        </div>
    </div>

</div>

</body>
<script type="text/javascript">
<%--var loading = function(){--%>
<%--var newOaUserNamecn = '<%=newOaUserNamecn%>';--%>
<%--if(newOaUserNamecn == 'true'){--%>
<%--LoginAction.login({uid:<%=id%>},"oa",function(data){--%>
<%--switch(data){--%>
<%--case "SUCCESS":{--%>
<%--window.location.href="<%=request.getContextPath() %>/meta/module/index/index.jsp";--%>
<%--break;--%>
<%----%>
<%--}--%>
<%--default:{--%>
<%--window.location.href="<%=request.getContextPath() %>/";--%>
<%----%>
<%--}--%>
<%--}--%>
<%--});--%>
<%--};--%>
<%--};--%>

//
//    var preId='YHFZ_IND_1,VALUE2,10';
Date.prototype.format = function(format) {
    var o = {
        "M+" : this.getMonth() + 1, //month
        "d+" : this.getDate(),    //day
        "h+" : this.getHours(),   //hour
        "m+" : this.getMinutes(), //minute
        "s+" : this.getSeconds(), //second
        "q+" : Math.floor((this.getMonth() + 3) / 3),  //quarter
        "S" : this.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
            (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
                RegExp.$1.length == 1 ? o[k] :
                        ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}
var isLogin = false;
var loginNum = 1;
var preId = "YHFZ_IND_1,VALUE2,10";
var chartRadio = function(ids) {
    if (preId) {
        document.getElementById(preId).childNodes[0].style.color = 'black';
    }
    preId = ids;
    document.getElementById(ids).childNodes[0].style.color = 'red';
    var id = ids.split(",");
    fchart(id[0], id[1], id[2]);
}
var iphoneRodio = function(iphoneIds) {
    if (preId) {
        document.getElementById(preId).childNodes[0].style.color = 'black';
    }
    preId = iphoneIds;
    document.getElementById(iphoneIds).childNodes[0].style.color = 'red';
    var id = iphoneIds.split(",");
    fchart1(id[0], id[1], id[2]);
}
//选项卡接受内容,以及维度和指标的内容，
var content = ['用户发展','业务量与流量','行业移动过网','iPhone4S'];
var contentId = [11192,11193,11194,11195];
var chartIndexCd_ = document.getElementById('chartIndexCd_').value.toString().split(",");
var chartFieldName_ = document.getElementById("chartFieldName_").value.toString().split(",");

//判断维度质变的长度是否相等，这里必须相等，不然返回
//  	if(content.length != chartIndexCd_.length || chartIndexCd_.length != chartFieldName_.length
//  			|| chartFieldName_.length != content.length){
//  		alert("请传入正确的参数");
//  		//return false;
//  	}
var tabbar = new dhtmlXTabBar("charId_", "top");
tabbar.setSize("<%=strWidth%>", "<%=Integer.parseInt(strHeight)%>");
tabbar.setSkin('dhx_skyblue');
tabbar.setImagePath("<%=path%>/meta/resource/dhtmlx/imgs/");
tabbar.setHrefMode("ajax-html");
tabbar.enableTabCloseButton(false);
tabbar.enableScroll(false);

for (var i = 0; i < content.length; i++) {
    tabbar.addTab(i, content[i], "100px");
}
/****tabbar.addTab("a1", "用户发展", "100px");
 tabbar.addTab("a2", "业务量与流量", "100px");
 tabbar.addTab("a3", "收入预测", "100px");
 tabbar.addTab("a4", "过网用户", "100px");***/
tabbar.setTabActive("0");
//初始界面默认为  YHFZ_IND_1    "VALUE1 ,10
fchart('YHFZ_IND_1', 'VALUE2', '10');
tabbar.setContent("0", document.getElementById("_content"));
tabbar.attachEvent("onSelect", function(id, last_id) {
    loginNum++;
	if(loginNum == 2){
        LoginAction.login({uid:<%=id%>}, "oa", function(data) {
            MenuVisitLogAction.writeMenuLog({userId:<%=id%>, menuId:contentId[id]});
        });
    }else{
        MenuVisitLogAction.writeMenuLog({userId:<%=id%>, menuId:contentId[id]});
    }
    //MenuVisitLogAction.writeMenuLog({userId:<%=id%>, menuId:contentId[id]});
    document.getElementById(preId).childNodes[0].style.color = 'black';
    if (id == 0) {
        preId = 'YHFZ_IND_1,VALUE2,10';
        document.getElementById('charRado').style.display = "inline";
        document.getElementById('charRado').style.textAlign = "center";
        fchart("YHFZ_IND_1", "VALUE2", "10");
    } else {
        document.getElementById('charRado').style.display = "none";
    }
    if (id == 1) {
        preId = 'YWLL_IND_1,VALUE2,11';
        document.getElementById('charRado2').style.display = "inline";
        document.getElementById('charRado2').style.textAlign = "center";
        fchart("YWLL_IND_1", "VALUE2", "11");
    } else {
        document.getElementById('charRado2').style.display = "none";
    }
    if (id == 2) {
        preId = 'GWYH_IND_3,VALUE3,12';
        document.getElementById('charRado3').style.display = "inline";
        document.getElementById('charRado3').style.textAlign = "center";
        fchart('GWYH_IND_3', 'VALUE3', '12');
    } else {
        document.getElementById('charRado3').style.display = "none";
    }
    if (id == 3) {
        preId = '0,@,@';
        document.getElementById('charRado4').style.display = "inline";
        document.getElementById('charRado4').style.textAlign = "center";
        fchart1('0', '@', '@');
    } else {
        document.getElementById('charRado4').style.display = "none";
    }
    document.getElementById(preId).childNodes[0].style.color = 'red';
    tabbar.setContent(id, document.getElementById("_content"));
    tabbar.setSize("<%=strWidth%>", "<%=strHeight%>");
    return true;
});
//刷新图标

function fchart(chartIndexCd_, chartFieldName_, indexTypeId) {
//    if (isLogin) {
        fcharts(chartIndexCd_, chartFieldName_, indexTypeId);
    <%--} else {--%>
        <%--LoginAction.login({uid:<%=id%>}, "oa", function(data) {--%>
            <%--switch (data) {--%>
                <%--case "SUCCESS":--%>
                <%--{--%>
                    <%--isLogin = true;--%>
                    <%--fcharts(chartIndexCd_, chartFieldName_, indexTypeId);--%>
                    <%--break;--%>
                <%--}--%>
                <%--default:--%>
                <%--{--%>
                    <%--isLogin = true;--%>
                    <%--fcharts(chartIndexCd_, chartFieldName_, indexTypeId);--%>
                    <%--window.location.href = "<%=request.getContextPath() %>/portal/error/errorState.jsp";--%>
                <%--}--%>
            <%--}--%>
        <%--});--%>
    <%--}--%>
}
function fcharts(chartIndexCd_, chartFieldName_, indexTypeId) {
    chartIndexCd_ = (chartIndexCd_ == null) ? "YHFZ_IND_1" : chartIndexCd_;
    chartFieldName_ = (chartFieldName_ == null) ? "VALUE2" : chartFieldName_;
    var dataNo = new Date();
    var dataNoNeed = (new Date(dataNo.getFullYear(), dataNo.getMonth(), dataNo.getDate() - 1)).format("yyyyMMdd");
    var reportLevelId = 1;
    var localCode = "0000";
    if (localCode != "0000") {
        reportLevelId = 2;
    }
    var areaId = 0;
    PortalCtrlr.getDataAudit('1', indexTypeId, indexTypeId, dataNoNeed, '0000', function(res){ // 以全省则只传 '0000' dataLocalCode {
        if (res && parseInt(res[0]) == 0) {

            if (indexTypeId != '12' && indexTypeId != '11') {
//                    $('audit'+indexTypeId).innerHTML = dataNoNeed.toString().substring(dataNoNeed.toString().length-2,dataNoNeed.toString().length)+"号数据未审核";
                dataNoNeed = (new Date(dataNo.getFullYear(), dataNo.getMonth(), dataNo.getDate() - 2)).format("yyyyMMdd");
            }
            PortalCtrlr.getTableChart(indexTypeId, reportLevelId, chartIndexCd_,
                    localCode, 0 + "", chartFieldName_, 1, dataNoNeed + "$", function(res) {
                        //alert("aaaa");
                        if (!res || res == null) {
                            alert("读取数据发生错误,请输入有效的参数!");
                            //hostory.go(-1);
                        }
                        <%--$("chart").style.width="<%=(Integer.parseInt(strWidth)+100)%>px";--%>
                        <%--$("chart").style.height="<%=(Integer.parseInt(strHeight)+100)%>px";--%>
                        var chart = new FusionCharts("<%=path%>/portal/resource/swf/FusionCharts/MSLine.swf", "ChartId", "<%=strWidth%>", "<%=Integer.parseInt(strHeight)-70%>");
                        //alert("ChartId"+"-----"+$("rpt_tabbar").offsetWidth);
                        //chart.setDataURL("Data/MSLine.xml");
                        //alert("aa");
                        chart.setDataXML(res[0]);
                        chart.render("chart");
                    });
        } else {
            PortalCtrlr.getTableChart(indexTypeId, reportLevelId, chartIndexCd_,
                    localCode, 0 + "", chartFieldName_, 1, dataNoNeed + "$", function(res) {
                        //alert("aaaa");
                        if (!res || res == null) {
                            alert("读取数据发生错误,请输入有效的参数!");
                            //hostory.go(-1);
                        }
                        <%--$("chart").style.width="<%=(Integer.parseInt(strWidth)+100)%>px";--%>
                        <%--$("chart").style.height="<%=(Integer.parseInt(strHeight)+100)%>px";--%>
                        var chart = new FusionCharts("<%=path%>/portal/resource/swf/FusionCharts/MSLine.swf", "ChartId", "<%=strWidth%>", "<%=Integer.parseInt(strHeight)-70%>");
                        //alert("ChartId"+"-----"+$("rpt_tabbar").offsetWidth);
                        //chart.setDataURL("Data/MSLine.xml");
                        //alert("aa");
                        chart.setDataXML(res[0]);
                        chart.render("chart");
                    });
        }
    });
}
function fchart1(type, channel_type, bt_code) {
//    if (isLogin) {
        fchart1s(type, channel_type, bt_code);
    <%--} else {--%>
        <%--LoginAction.login({uid:<%=id%>}, "oa", function(data) {--%>
            <%--switch (data) {--%>
                <%--case "SUCCESS":--%>
                <%--{--%>
                    <%--isLogin = true;--%>
                    <%--fchart1s(type, channel_type, bt_code);--%>
                    <%--break;--%>
                <%--}--%>
                <%--default:--%>
                <%--{--%>
                    <%--isLogin = true;--%>
                    <%--fchart1s(type, channel_type, bt_code)--%>
                    <%--window.location.href = "<%=request.getContextPath() %>/portal/error/errorState.jsp";--%>
                <%--}--%>
            <%--}--%>
        <%--});--%>
    <%--}--%>

}
function fchart1s(type, channel_type, bt_code) {
    type = (type == '@' ? "" : type);
    channel_type = (channel_type == '@' ? "" : channel_type);
    bt_code = (bt_code == '@' ? "" : bt_code);
    var dataNo = new Date();
    var dataNoNeed = (new Date(dataNo.getFullYear(), dataNo.getMonth(), dataNo.getDate()-1 )).format("yyyyMMdd");
    var reportLevelId = 1;
    var localCode = "0000";
    if (localCode != "0000") {
        reportLevelId = 2;
    }
    var areaId = 0;
    PortalCtrlr.getTableChart1(type, channel_type, bt_code,
            localCode, dataNoNeed + "$", function(res) {
                //alert("aaaa");
                if (!res || res == null) {
                    alert("读取数据发生错误,请输入有效的参数!");
                    //hostory.go(-1);
                }
                <%--$("chart").style.width="<%=(Integer.parseInt(strWidth)+100)%>px";--%>
                <%--$("chart").style.height="<%=(Integer.parseInt(strHeight)+100)%>px";--%>
                var chart = new FusionCharts("<%=path%>/portal/resource/swf/FusionCharts/MSLine.swf", "ChartId", "<%=strWidth%>", "<%=Integer.parseInt(strHeight)-70%>");
                //alert("ChartId"+"-----"+$("rpt_tabbar").offsetWidth);
                //chart.setDataURL("Data/MSLine.xml");
                //alert("aa");
                chart.setDataXML(res[0]);
                chart.render("chart");
            });
}
var getData = function(id, dataNow) {
    PortalCtrlr.getDataAudit('1', id, id, dataNow, '0000', function(res){ // 以全省则只传 '0000' dataLocalCode {
        if (res && parseInt(res[0]) == 0) {
            return true;
        } else {
            return false;
        }
    });
}


</script>
</html>
<%
} else {
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head><title></title></head>
<script type="text/javascript" src="<%=path%>/meta/resource/dhtmlx/dhtmlx.js"></script>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type="text/javascript" src="<%=path%>/dwr/interface/LoginAction.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/meta/resource/dhtmlx/dhtmlx.css">
<script type="text/javascript">
    var newOaUserNamecn = '<%=newOaUserNamecn%>';
    var date = new Date();
    LoginAction.login({uid:<%=id%>}, "oa", function(data) {
        switch (data) {
            case "ERROR_GROUP_DISENBLE":
            {
                alert("用户所属系统状态失效，请联系管理员！");
                window.location.href = "<%=request.getContextPath() %>/";
                break;
            }
            case "ERROR_DISABLED":
            {
                alert("该用户已被禁用，不能使用！");
                window.location.href = "<%=request.getContextPath() %>/";
                break;
            }
            case "ERROR_LOCKING":
            {
                alert("该用户已被锁定，不能使用！");
                window.location.href = "<%=request.getContextPath() %>/";
                break
            }
            case "ERROR_AUDITING":
            {
                alert("该用户正在被审核中，暂不能使用！");
                window.location.href = "<%=request.getContextPath() %>/";
                break;
            }
            case "ERROR_USER_PASSWD":
            {
                alert("用户名或密码错误，请确认！");
                window.location.href = "<%=request.getContextPath() %>/";
                break;
            }
            case "ERROR_NAME_REPEAT":
            {
                alert("您输入的中文名重复，请使用Email登录！");
                window.location.href = "<%=request.getContextPath() %>/";
                break;
            }
            case "USER_FIRST_LOGIN":
            {
                alert("感谢使用本系统，请先修改密码，谢谢！");
                setTimeout(function() {
                    window.location.href = "<%=request.getContextPath() %>/portal/module/portal/modifyPwd.jsp?isLoginFirst=true&"+date.getTime();
                }, 2000);
                break;
            }
            case "USER_FORCE_MODIFY_PASS":
            {
                alert("你已超过九十天未修改密码，请修改密码");
                setTimeout(function() {
                    window.location.href = "<%=request.getContextPath() %>/portal/module/portal/modifyPwd.jsp?isLoginFirst=true&"+date.getTime();
                }, 2000);
                break;
            }
            case "USER_TIP_MODIFY_PASS":
            {
                alert("你已超过八十天未修改密码,超过90天我们将强制修改");
                setTimeout(function() {
                    window.location.href = "<%=request.getContextPath() %>/meta/module/index/index.jsp?"+date.getTime();
                }, 2000);
                break;
            }
            default:
            {
                window.location.href = "<%=request.getContextPath() %>/meta/module/index/index.jsp?"+date.getTime();
            }
        }
    });
</script>
<body></body>

</html>
<%
    }


} else {
%>
<html>
<head><title></title></head>
<body style="margin: 0px;padding: 0px;">
<p><font size="2" style="color:black;">对不起，您没有数据门户权限，请与管理员确认！</font></p>
</body>
</html>
<%
    }
    userDAO.close();
    userDAO = null;
    loginAction = null;
%>