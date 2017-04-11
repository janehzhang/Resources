<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en" style="width:100%;height: 100%" >
<%
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
String rootPath = request.getContextPath();
String base = request.getScheme() + "://" +  request.getServerName() + ":" + request.getServerPort() +  request.getContextPath();

String  headIe=request.getHeader("USER-AGENT" ).toLowerCase();
System.out.println(headIe);
boolean isIE=headIe.indexOf("msie") >  0  ?  true  :  false;
boolean isOverIE8=false;
if(isIE){
	headIe=headIe.substring(headIe.indexOf("msie")+4, headIe.length());
	isOverIE8=Double.parseDouble((headIe.substring(0, headIe.indexOf(";"))).trim()) >= 7 ? true : false;
}

if(!isIE){
	boolean isIE11=headIe.indexOf("rv:") >  0  ?  true  :  false;
		if(isIE11){
			isOverIE8=true;
		}
}
             
%>
<head>
    <title>广东电信客户服务分析系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/meta/resource/dhtmlx/dhtmlx.css">
</head>
<%
Cookie[] cookies = request.getCookies();
String UserAccount ="";   
if (cookies != null) {   
    for (Cookie coo : cookies) {   
         if((coo.getName()).equals("UserAccount")){
        	 UserAccount=coo.getValue();
         } 
         
    }   
}      
%>
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
    var isOverIE8="<%=isOverIE8 %>";
    var userName="<%=UserAccount%>";
    var getBasePath = function() { //获取根目录
        return "<%=rootPath%>";
    }
    var data=new Object();
    data.oldPortal=userName;
    data.src='oa';
    var msgCode="";
    var msg="您目前使用的IE浏览器版本较低,可能会影响到您的工作空间页面效果查看布局 ," +
            "建议您点击“确认”是否立即升级到IE8以上的版本."; 
    var isMsg =true;
    if(isOverIE8 == "false"){
    	if (confirm(msg))  { 
	        window.location.href="<%=request.getContextPath() %>/portal/resource/other/IE8.exe";
	        isMsg=false;
    	}
    }
  
  if(isMsg){
    if (userName=='null' || userName==''){
   	     msgCode="101";
   	     window.location.href="<%=request.getContextPath() %>/portal/error.jsp?errmsgCode="+msgCode;
    }else{
            LoginAction.login(data,"oldoa",function(data){
               var date = new Date();
                switch(data){
                    case "ERROR_GROUP_DISENBLE":{
                        //dhx.alert("用户所属系统状态失效，请联系管理员！");
                        msgCode="103";
                        window.location.href="<%=request.getContextPath() %>/portal/error.jsp?errmsgCode="+msgCode;
                        break;
                    }
                    case "ERROR_DISABLED":{
                        //dhx.alert("该用户已被禁用，不能使用！");
                        msgCode="104";
                        window.location.href="<%=request.getContextPath() %>/portal/error.jsp?errmsgCode="+msgCode; 
                        break;
                    }
                    case "ERROR_LOCKING":{
                        //dhx.alert("该用户已被锁定，不能使用！");
                        msgCode="105";
                        window.location.href="<%=request.getContextPath() %>/portal/error.jsp?errmsgCode="+msgCode;
                        break;
                    }
                    case "ERROR_AUDITING":{
                        //dhx.alert("该用户正在被审核中，暂不能使用！");
                        msgCode="106";
                        window.location.href="<%=request.getContextPath() %>/portal/error.jsp?errmsgCode="+msgCode;   
                        break;
                    }
                    case "ERROR_USER_PASSWD":{
                        //dhx.alert("用户名或密码错误，请确认！");
                        msgCode="107";
                        window.location.href="<%=request.getContextPath() %>/portal/error.jsp?errmsgCode="+msgCode;
                        break;
                    }
                    case "ERROR_NAME_REPEAT":{
                        //dhx.alert("您输入的中文名重复，请使用Email登录！");
                        msgCode="108";
                        window.location.href="<%=request.getContextPath() %>/portal/error.jsp?errmsgCode="+msgCode;                           
                        break;
                    }
                    case "ERROR_VALIDATECODE":{
                        //dhx.alert("您输入的验证码错误，请重新输入！");
                        msgCode="109";
                        window.location.href="<%=request.getContextPath() %>/portal/error.jsp?errmsgCode="+msgCode;                          
                        break;
                    }
                    case "ERROR_VALIDATEOVERDUE":{
                       	//dhx.alert("验证码已过期，请重新输入！");
                        msgCode="110";
                        window.location.href="<%=request.getContextPath() %>/portal/error.jsp?errmsgCode="+msgCode;                          	
                       	break;
                    }
                    default:{
                            window.location.href="<%=request.getContextPath() %>/meta/module/index/index.jsp?"+date.getTime();
                    }
                }
        
            });
   	
   	      }
   	   }    
</script>
</body>
</html>


