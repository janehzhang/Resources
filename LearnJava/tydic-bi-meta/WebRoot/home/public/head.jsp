<%--
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王春生
 * @description 
 * @date 12-4-12
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="tydic.meta.common.JsonUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="tydic.meta.module.mag.login.LoginConstant" %>
<%@ page import="tydic.meta.common.Constant" %>
<%@ page import="tydic.meta.module.mag.user.UserConstant" %>
<%@ page import="tydic.meta.web.session.SessionManager" %>
<%@ page import="tydic.frame.SystemVariable" %>
<%
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires", 0);
    String rootPath = request.getContextPath();
%>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
<META HTTP-EQUIV="X-UA-COMPATIBLE" CONTENT="IE=EDGE" >

<link rel="stylesheet" type="text/css" href="<%=rootPath%>/meta/resource/dhtmlx/dhtmlx.css">
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/home/resource/css/base_home.css">
<link type="text/css" rel="stylesheet" href="<%=rootPath%>/meta/resource/css/validation.css">
<link type="text/css" rel="stylesheet" href="<%=rootPath%>/meta/resource/css/icon.css">
<link type="text/css" rel="stylesheet" href="<%=rootPath%>/meta/resource/css/meta.css">
<style type="text/css">
    html,body{
        overflow:auto;
    }
</style>
<%--公共JS文件导入--%>
<script type='text/javascript' src='<%=rootPath%>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=rootPath%>/dwr/util.js'></script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/dhtmlx/dhtmlx.js"></script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/js/dhtmlxExtend.js"></script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/js/component.js"></script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/js/dhtmlxMessage.js"></script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/js/dhtmlxDataConverter.js"></script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/js/tool.js"></script>
<script type="text/javascript" src="<%=rootPath%>/home/resource/js/OPString.js"></script>
<script type="text/javascript" src="<%=rootPath%>/home/resource/js/home_common.js"></script>
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/SessionManager.js"></script>

<%--公共JS方法，必须写在页面的方法--%>
<script type="text/javascript">
    var getSkin = function() {
        //TODO 根据session中的skin配置获取用户的皮肤设置。
        return "dhx_skyblue";
    };
    var getDefaultImagePath = function() {
        return "<%=rootPath%>/meta/resource/dhtmlx/imgs/";
    };
    var getBasePath = function() { //获取根目录
        return "<%=rootPath%>";
    };

    var _sessionInfo = {};
    var getSessionAttribute = function(attr,realTime){
        if(realTime||!_sessionInfo[attr]){
            SessionManager.getAttribute(attr,{
                async:false,
                callback:function(data){
                    _sessionInfo[attr]=data;
                }
            });
        }
        return _sessionInfo[attr];
    };

    /*
     JS全局常量定义区，以global开头
     */
    var global=new Object();
    //常量定义
    global.constant=new Object();
    //默认管理员ID
    global.constant.adminId=<%=UserConstant.ADMIN_USERID%>;
    //默认系统ID
    global.constant.defaultSystemId=<%=Constant.DEFAULT_META_SYSTEM_ID%>;
    //默认系统根节点
    global.constant.defaultRoot=<%=Constant.DEFAULT_ROOT_PARENT%>;
    //每页显示条数
    global.constant.pageSize=<%=SystemVariable.getInt("meta.page.size", 15) %>;

    /**
     *  url编码
     * @param winUrl
     */
    var urlEncode=function(winUrl){
        user = user || getSessionAttribute('user');
        if (winUrl.toLowerCase().indexOf("http://") == -1) {
            //加入权限标识
            var strKey=Tools.base64encode(user.userId+user.userPass);
            winUrl=Tools.trim(winUrl) + (winUrl.indexOf("?") == -1 ? "?" : "&") +"_strKey_="+strKey;
        }
        return winUrl;
    };
</script>