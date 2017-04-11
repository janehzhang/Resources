<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="tydic.meta.common.JsonUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="tydic.meta.module.mag.login.LoginConstant" %>
<%@ page import="tydic.meta.common.Constant" %>
<%@ page import="tydic.meta.module.mag.user.UserConstant" %>
<%@ page import="tydic.meta.web.session.SessionManager"%>
<%@ page import="tydic.frame.SystemVariable" %>
<%
	response.setHeader("Pragma","No-cache"); 
	response.setHeader("Cache-Control","no-cache"); 
	response.setDateHeader("Expires", 0); 

    String rootPath = request.getContextPath();
    String menuId=request.getParameter("menuId");
%>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
<script type="text/javascript">
    var menuId='<%=menuId%>'
    //此段JS用于通知父窗体做些初始化的动作。
    var win=window.opener||window.parent;
    if(win.globalInit){//如果是弹出窗口
        win.globalInit(window,'<%=menuId%>');
    }
    //刷新当前在线人数
    if(win.refreshOnlineCount){
        win.refreshOnlineCount('<%=SessionManager.getOnlineUserCount()%>');
    }
    var roleFilter=function(){
        document.getElementById("1").style.display="none";
        var buttons=document.getElementsByName("1");
        if(buttons&&buttons.length>0){
            for(var i=0;i<buttons.length;i++){
                buttons[i].style.display="none";
            }
        }
    }
    
    function cg(){
    	window.setTimeout(function(){
	    	CollectGarbage();
	    	cg();
	    },1000);
    }
</script>
<%--作者:张伟--%>
<%--时间:2011-09-15--%>
<%--描述:公共头JSP文件--%>
<%--dhtmlx CSS--%>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/meta/resource/dhtmlx/dhtmlx.css">
<%--公共验证CSS--%>
<link type="text/css" rel="stylesheet" href="<%=rootPath%>/meta/resource/css/validation.css">
<link type="text/css" rel="stylesheet" href="<%=rootPath%>/meta/resource/css/icon.css">
<link type="text/css" rel="stylesheet" href="<%=rootPath%>/meta/resource/css/meta.css">
<%--公共JS文件导入
<script type='text/javascript' src='<%=rootPath%>/role.js?menuId=<%=menuId%>'></script>
--%>
<script type='text/javascript' src='<%=rootPath%>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=rootPath%>/dwr/util.js'></script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/dhtmlx/dhtmlx.js"></script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/js/tool.js"></script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/js/dhtmlxExtend.js"></script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/js/dhtmlxMessage.js"></script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/js/dhtmlxDataConverter.js"></script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/js/dhtmlx_i18n_zh.js"></script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/js/commonFormater.js"></script>
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/SessionManager.js"></script>


<%--<script type="text/javascript" src="<%=rootPath%>/meta/resource/js/dhtmlxform.js"></script>--%>

<style type="text/css">

    .combo{
        padding: 0px ;
        margin: 0px;
    }

    .label_anou div{
        width: 250px;
    }

    *,.dhtmlxInfoBarLabel{
        font:12px/1.5em Tahoma, Verdana, Simsun, Microsoft YaHei, Arial Unicode MS, Mingliu, Arial, Helvetica;
    }
    
    <%-- 谭万昌add   修改combo错位问题--%>
    .dhxlist_obj_dhx_skyblue div.item_label_left div.dhxlist_cont{
    	display:inline-block;float:none;display:inline\0;
    }
    .dhxlist_obj_dhx_skyblue div.dhx_list_btn td.btn_m div.btn_txt{
    	font-size:inherit;font-family:Tahoma;color:#000;padding:1px 10px;overflow:hidden;white-space:nowrap;cursor:pointer;
    }
</style>
<%--公共JS方法，必须写在页面的方法--%>
<script type="text/javascript">
    var getSkin = function() {
        //TODO 根据session中的skin配置获取用户的皮肤设置。
        return "dhx_skyblue";
    }
    var getDefaultImagePath = function() {
        return "<%=rootPath%>/meta/resource/dhtmlx/imgs/";
    }
    var getBasePath = function() { //获取根目录
        return "<%=rootPath%>";
    }
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
    //for Ie7、IE6 在框架下获取其offsetWidth为0的BUG,重新设置body高和宽。
    var resizeHandler = function(){
        document.body.style.width='100%';
        document.body.style.height='100%';
        var div = dhx.html.create('div');
        div.style.position='absolute';
        div.style.left='0';
        div.style.top='0';
        div.style.width='100%';
        div.style.height='100%';
        div.style.zIndex='-10000';
        //div.style.border='1px solid red';
        document.body.appendChild(div);
        document.body.style.width = div.clientWidth;
        document.body.style.height = div.clientHeight;
        document.body.removeChild(div);
    }


    dhx.ready(function(){
        if(dhx.env.isIE){
            resizeHandler();
            if(window.self==top){
                window.onresize=resizeHandler;
            }
        }
    })
    /**
     *  url编码
     * @param winUrl
     */
     var urlEncode=function(winUrl){
        var getSessionAttribute=window.getSessionAttribute||window.parent.getSessionAttribute;
        var user=getSessionAttribute('user');
        if (winUrl.toLowerCase().indexOf("http://") == -1) {
            //加入权限标识
            var strKey=Tools.base64encode(user.userId+user.userPass);
            winUrl=Tools.trim(winUrl) + (winUrl.indexOf("?") == -1 ? "?" : "&") +"_strKey_="+strKey;
        }
        return winUrl;
    }

    //取消DWR默认异常处理，抛出异常便于JS调试
    dwr.engine.setErrorHandler(function(errorMsg,error){
        dhx.closeProgress();
        //dhx.alert("出现异常："+error);
        throw error;
    })
</script>
<script type="text/javascript" src="<%=rootPath%>/meta/resource/js/constant.js"></script>