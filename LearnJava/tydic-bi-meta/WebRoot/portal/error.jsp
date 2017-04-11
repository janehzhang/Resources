<%@page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en" style="width:100%;height: 100%">
	<%
		String errmsg="";
		String errmsgCode = (String) request.getParameter("errmsgCode");
		switch (Integer.parseInt(errmsgCode)){
            case  101:
                 errmsg="请输入Email、中文名或者手机号码！";
                 break;
             case 102:
                 errmsg="非法登录,验证不通过！";
                 break;            
             case 103:
                 errmsg="用户所属系统状态失效，请联系管理员！";
                 break;      
            case 104:
                 errmsg="该用户已被禁用，不能使用！";
                 break;  
            case 105:
                 errmsg="该用户已被锁定，不能使用！";
                 break;                               
            case 106:
                 errmsg="该用户正在被审核中，暂不能使用！";
                 break;  
            case 107:
                 errmsg="用户名或密码错误，请确认！";
                 break;  
            case 108:
                 errmsg="您输入的中文名重复，请使用Email登录！";
                 break;  
            case 109:
                 errmsg="您输入的验证码错误，请重新输入！";
                 break;  
            case 110:
                 errmsg="验证码已过期，请重新输入！";
                 break;  
            default:
                 errmsg="未知错误!";
                 break;
        }
	%>
	<head>
		<title>广东省电信客户服务分析系统</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body>
		<div align="center" id="errorMessages">
	     <fieldset class="totalfie"> 
          <legend><h1>出错信息 </h1></legend>
          <font size="6" style="color: black;"><%=errmsg %> </font>
        </fieldset>
		</div>
	</body>
</html>
