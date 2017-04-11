<%@ page import="tydic.meta.common.Constant" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <title>中国四川电信企业数据门户</title>
    <%@include file="../../public/head.jsp"%>
    <style type="text/css">
        *{
            padding: 0px;
            margin: 0px;
        }
        body{
            background-color: #A6C8ED;
        }
        #nav{
            background: url('<%=rootPath%>/meta/resource/images/title-bg.gif');
            padding-left:10px;
            height: 45px;
            padding: 0px;
            margin: 0px;
            margin-top:2px;
            padding-left: 10px;
        }

        #navUl{
            height: 38px;
            padding:0px;
            margin:0px;
            list-style: none;
        }
        #navUl li{
            float:left;
            padding:0px;
            margin:0px;
            text-align:center;
            padding-top:5px;
            padding-right:5px;
            height:38px;
            width:110px;
            background: url('<%=rootPath%>/meta/resource/images/menu-line.png') no-repeat;
            background-position: right;
        }

        #navUl li a{
            text-align:center;
            text-decoration: none;
            font-size: 12px;
            color: black;
            width:100px;
            height:38px;
            float: right;
            border: none;
            padding-top: 8px;
            border: none;
        }

        #navUl li a img{
            max-width:16px;
            min-height:16px;
            border: none;
            margin-right: 2px;
        }

        #_menu_toolbar{
            position:relative;
            right:10px;
            width:400px;
            top:10px;
            vertical-align:middle;
        }

        #_menu_toolbar a{
            text-decoration: none;
            font-size: 13px;
            color: black;
        }

        #_menu_toolbar a span{
            padding:0px;
            margin:0px;
            height:100%;
        }

        #_menu_toolbar a img{
            padding:0px;
            margin:0px;
            padding-left: 2px;
            padding-right: 1px;
            width: 16px;
            height: 16px;
            border: none;
        }
        table.dhtmlxLayoutPolyContainer_dhx_skyblue td.dhtmlxLayoutPolySplitterHorInactive{
            height: 1px;
            border: none;
        }

        table.dhtmlxLayoutPolyContainer_dhx_skyblue td.dhtmlxLayoutPolySplitterHorInactive div{
            display: none;
        }
        table.dhtmlxLayoutPolyContainer_dhx_skyblue td.dhtmlxLayoutSinglePoly div.dhxcont_global_content_area{
            border: none;
        }
    </style>
    <%
        Map<String,Object> sysInfo=(Map<String,Object>)session.getAttribute(LoginConstant.SESSION_META_SYSTEM_INFO);
        String systemId=sysInfo.get("groupId").toString();
        if(systemId==null){//如果系统ID为null，从Session中获取当前用户的默认系统ID
            Map<String,Object> userInfo=(Map<String,Object>)session.getAttribute(LoginConstant.SESSION_KEY_USER);
            systemId=userInfo.get("groupId")==null
                    ?String.valueOf(Constant.DEFAULT_META_SYSTEM_ID):userInfo.get("groupId").toString();
        }
        out.println(
                "<script type=\"text/javascript\">var getSystemId=function(){var systemId='"
                        +(systemId==null? Constant.DEFAULT_META_SYSTEM_ID:systemId)+"'; " +
                        "return parseInt(systemId||1);}</script>"
        );//打印一段JS代码，用于返回当前systemId
    %>


</head>


<body style="padding: 0px; margin: 0px;">

<div id="div_src" style="position:absolute;width:100%;height:100%;z-index:-1;top:0px;left:0px;">
</div>
<div  id="north" style="overflow: hidden;background-color: #A6C8ED;left:0px;bottom:0px;background-image: url('<%=rootPath%>/portal/resource/images/logo.jpg');background-repeat: no-repeat;"  >
    <div style="height: 45px;" align="right">
        <div id="_menu_toolbar" valign="middle" style="width:60%">
            <a href="javascript:loadOperMan()">
                <img src="<%=rootPath%>/portal/resource/images/manual_icon.png" alt="" align="middle"  style="height: 35px;width: 35px"/>
                <span>操作手册</span>
            </a>

            <a href="javascript:openVisitRank()">
                <img src="<%=rootPath%>/portal/resource/images/yhmin.gif" alt="" align="middle"  style="height: 35px;width: 35px"/>
                <span>访问排名</span>
            </a>

            <a href="javascript:openAppRank()">
                <img src="<%=rootPath%>/portal/resource/images/yhmin2.gif" alt="" align="middle"  style="height: 35px;width: 35px"/>
                <span>应用排名</span>
            </a>

            <a href="javascript:openLogRep()">
                <img src="<%=rootPath%>/portal/resource/images/visit.png" alt="" align="middle"  style="height: 35px;width: 35px"/>
                <span>访问统计</span>
            </a>

            <a href="javascript:changePassword()">
                <img src="<%=rootPath%>/portal/resource/images/buttont_top1.jpg" alt="" align="middle" style="height: 35px;width: 35px"/>
                <span>修改密码</span>
            </a>

            <a href="javascript:logout()">
                <img src="<%=rootPath%>/portal/resource/images/buttont_top2.jpg" alt="" align="middle" style="height: 35px;width: 35px"/>
                <span>退出系统</span>
            </a>
        </div>
    </div>
    <div id="nav">
        <ul id="navUl">
        </ul>
    </div>
</div>
</body>
</html>
<script type="text/javascript">
    var _sessionInfo = {};
    var getSessionAttribute=function(attr,realTime){
        if(realTime||!_sessionInfo[attr]){
            SessionManager.getAttribute(attr,{async:false,callback:function(data){
                _sessionInfo[attr]=data;
            }
            })
        }
        return _sessionInfo[attr];
    };
    if (window.screen) {//判断浏览器是否支持window.screen判断浏览器是否支持screen
        var myw = screen.availWidth;   //定义一个myw，接受到当前全屏的宽
        var myh = screen.availHeight;  //定义一个myw，接受到当前全屏的高
        try{
            window.moveTo(0, 0);           //把window放在左上脚
            window.resizeTo(myw, myh);     //把当前窗体的长宽跳转为myw和myh
        }catch(e){
        }
        if(window.outerWidth!=screen.availWidth || window.outerHeight!=screen.availHeight){
            window.outerWidth=screen.availWidth;
            window.outerHeight=screen.availHeight;
        }

    }    /**
     * 根据菜单数据配置处理URL信息。
     * @param url
     * @param userAttr 用户属性字符串。
     */
    var urlDeal=function(url){
        var user=getSessionAttribute("user");
        var zone=getSessionAttribute("zoneInfo");
        var dept=getSessionAttribute("deptInfo");
        var station=getSessionAttribute("stationInfo");
        var winUrl = url;
        //检测url,如果不是以http开头的，默认是本系统路径
        if (winUrl) {
            if (winUrl.toLowerCase().indexOf("http://") == -1) {
                winUrl = getBasePath() + "/" + winUrl;
            }
            //替换宏变量
            winUrl=winUrl.replace(/(\w+)=\[(([0-9a-zA-Z]+):)?(\w+)\]/g,function($0,$1,$2,$3,$4){
                if(!$3||$3.toLowerCase()=="<%=LoginConstant.URL_MARCO_USER%>"){//用户属性
                    return $1+"="+encodeURIComponent(user[Tools.tranColumnToJavaName($4)]);
                }else if($3.toLowerCase()=="<%=LoginConstant.URL_MARCO_ZONE%>"){
                    return $1+"="+encodeURIComponent(zone[Tools.tranColumnToJavaName($4)]);
                }else if($3.toLowerCase()=="<%=LoginConstant.URL_MARCO_DEPT%>"){
                    return $1+"="+encodeURIComponent(dept[Tools.tranColumnToJavaName($4)]);
                }else if($3.toLowerCase()=="<%=LoginConstant.URL_MARCO_STATION%>"){
                    return $1+"="+encodeURIComponent(station[Tools.tranColumnToJavaName($4)]);
                }
            });
        }
        return winUrl;
    }
    var tabHeight,tabWidth;
    tabHeight=$("div_src").offsetHeight-88;
    tabWidth=$("div_src").offsetWidth;

</script>
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/LoginAction.js"></script>
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/UserAction.js"></script>
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/MenuAction.js"></script>
<script type="text/javascript" src="index.js"></script>