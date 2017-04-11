<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <%@ page import="java.util.*"%> 
 <%@ page import="tydic.meta.acc.Encrypt"%>
 <%@ page import="tydic.meta.module.mag.login.LoginConstant"%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%
	String systemId = "kffq";
	Map<String, Object> userData = (Map<String, Object>) session.getAttribute("user");
    String account = (String)userData.get("userNameen");
    String userName = (String)userData.get("userNamecn");
    //String key = "appId=CSAS&subStaffId="+subStaffId;
    //String authToken = Encrypt.encryptUrl(key);
    String url = "http://132.121.64.43:9595/EDA-EP/myDocument/modelQuery!toModelQueryPage.action?"
                 +"modelList_id=4743b2c4add4444ea4b927aad8a4f7c6&account="+account+"&flag=&systemId="+systemId+"&corpId=200&userName="+userName+"&modelType=1";
    //System.out.println(url);
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客服分析系统对接多维报表智能云平台</title>
</head>
<body>
    <script>
    	//alert(1);
    </script>
    <iframe src="<%=url%>"  width="100%" height="100%">
	</iframe>
</body>
</html>
