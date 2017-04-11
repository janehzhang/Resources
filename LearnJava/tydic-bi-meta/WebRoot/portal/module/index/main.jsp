<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@page import="tydic.meta.web.session.SessionManager"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%--作者:张伟--%>
<%--时间:2011-09-15--%>
<%--描述:菜单主JSP--%>
<head>
    <title></title>
    <%@include file="../../public/head.jsp"%>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/MenuAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/LoginAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/MenuVisitLogAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/PortalCtrlr.js"></script>
    <style type="text/css">
        *{
            font-size: 12px;
        }
        .bottom { background:#A6C8ED url(./images/title-bg.gif) repeat-x 0 -252px; line-height:19px; height:19px; border:1px solid #A6C8ED; }
        
    </style>
</head>
<body style="height: 100%;width: 100%" onload="refreshOnlineCount('<%=SessionManager.getOnlineUserCount()%>');" oncontextmenu=self.event.returnValue=false>
   <div id="div_src" style="position:absolute;width:100%;height:100%;z-index:-1;top:0px;left:0px;"></div>
   <div id="main_tabbar" style="width:100%;height:100%"></div>
	<table id = "bottom" width="100%"  border = "0" cellspacing = "0" cellpadding = "0" class = "bottom mT5" align="center">
	    <tr>
	        <td height = "20">
	            <table width="100%" border = "0" cellspacing = "0" cellpadding = "0" align = "center">
	                <tr>
	                	<td width="30%">&nbsp;</td>
	                    <td width="40%" align="center" valign="bottom">本周门户值班人员：<nobr id="duty_man"/><nobr>&nbsp;&nbsp;在线用户数：</nobr><NOBR id="online_user_num"/></td>
	                	<td width="30%">&nbsp;</td>
	                </tr>
	            </table>
	        </td>
	    </tr>
	</table>
</body>
<script type="text/javascript">
var tabHeight,tabWidth;
if(window.parent)
{
	tabHeight=window.parent.tabHeight-31;
	tabWidth=window.parent.tabWidth;
}
else
{
 	tabHeight=$("div_src").offsetHeight-31;
	tabWidth=$("div_src").offsetWidth;
}
</script>
</html>
<script type="text/javascript" src="main.js"></script>