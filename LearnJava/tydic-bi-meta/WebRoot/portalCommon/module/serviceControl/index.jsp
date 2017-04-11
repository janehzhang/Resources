<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en" style="width:100%;height: 100%" >
<%
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
String rootPath = request.getContextPath();
String base = request.getScheme() + "://" +  request.getServerName() + ":" + request.getServerPort() +  request.getContextPath();

String toPath=request.getParameter("toPath");

%>
<head>
    <title>天源迪科信息数据管理平台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/meta/resource/dhtmlx/dhtmlx.css">
</head>
<body>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/util.js'></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/dwr/interface/LoginAction.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/meta/resource/dhtmlx/dhtmlx.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/meta/resource/js/dhtmlxExtend.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/meta/resource/js/dhtmlxMessage.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/meta/resource/js/dhtmlxDataConverter.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/meta/resource/js/dhtmlx_i18n_zh.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/meta/resource/js/commonFormater.js"></script>
<script>
    var userName="admin";
    var toPath="<%=toPath %>";
    var getBasePath = function() { //获取根目录
        return "<%=rootPath%>";
    }
    var data=new Object();
    data.oldPortal=userName;
    data.src='oa';
    LoginAction.login(data,"oldoa",function(data){
              window.location.href="<%=request.getContextPath() %>/portalCommon/module/serviceControl/"+toPath;
   });
</script>
</body>
</html>


