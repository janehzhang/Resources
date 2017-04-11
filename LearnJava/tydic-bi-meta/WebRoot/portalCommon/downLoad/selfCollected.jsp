<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <%@ page import="java.util.*"%> 
 <%@ page import="tydic.meta.acc.Encrypt"%>
 <%@ page import="tydic.meta.module.mag.login.LoginConstant"%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%
	String sysCode = "CSAS";
	Map<String, Object> userData = (Map<String, Object>) session.getAttribute("user");
    String subStaffId = (String)userData.get("userNameen");
    String key = "appId=CSAS&subStaffId="+subStaffId;
    String authToken = Encrypt.encryptUrl(key);
    String url = "http://132.121.91.48:8004/acc/verInfoMgr/staffInfo/toStaffCollect4AbspPage.action?appId="+sysCode+"&subStaffId="+subStaffId+"&authToken="+authToken;
    //System.out.println(url);
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客服分析系统ACC认证自助采集</title>
</head>
<body>
    <script>
    	//alert(1);
    </script>
    <iframe src="http://132.121.91.48:8004/acc/verInfoMgr/staffInfo/toStaffCollect4AbspPage.action?appId=<%=sysCode%>&subStaffId=<%=subStaffId%>&authToken=<%=authToken%>"  width="100%" height="100%">
	</iframe>        
</body>
</html>
