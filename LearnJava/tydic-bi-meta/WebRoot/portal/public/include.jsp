<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<style>
    .css_tabTitle
    {
        font-size:12px;
        padding:0px;
        margin:0px;
        padding-top:5px;
    }
    html body{
        background-color: #fff;
    }
</style>
<link type="text/css" rel="stylesheet" href="<%=path %>/portal/resource/css/sc.css">

<script type='text/javascript' src='<%=path %>/portal/resource/js/basic.js'></script>
<script type='text/javascript' src='<%=path %>/portal/resource/js/Common.js'></script>
<script type="text/javascript" src="<%=path %>/portal/resource/js/OPString.js"></script>
<script type="text/javascript" src="<%=path%>/dwr/interface/SessionManager.js"></script>
<script type="text/javascript">
    var user=null;
    SessionManager.getAttribute("user",{async:false,callback:function(data){
        user=data;
    }});
    var userInfo={};
    userInfo['userId']=user.userId;
    userInfo['email']=user.userEmail;
    userInfo['nameCN']=user.userNamecn;
    userInfo['nameEN']=user.userNameen;
    userInfo['mobile']=user.userMobile;
    userInfo['deptId']=user.deptId;
    userInfo['stationName']=user.stationId;
    userInfo['zoneId']=user.zoneId;
    var zoneInfo = top.getSessionAttribute('zoneInfo');
    userInfo['localCode']=zoneInfo.zoneCode;
    userInfo['areaId']=zoneInfo.areaId;

    window.focus();
    var maxTabSize=10;
    var minTabLen=100;
    var charPoint=8.2;

    dhtmlx.skin = "dhx_skyblue";
    function formatTabname(str)
    {
        return "<span class='css_tabTitle'>"+str+"</span>";
    }
</script>
