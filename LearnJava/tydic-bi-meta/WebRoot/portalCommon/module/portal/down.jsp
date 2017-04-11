<%@ page language="java" import="java.util.*,tydic.portalCommon.PortalCtrlr" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title>首页角色分工作台跳转页面</title>
    <%@include file="../../public/head.jsp" %>
    <%@include file="../../public/include.jsp" %>
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
        PortalCtrlr pc=new PortalCtrlr();
        String newDate= (String)pc.getNewDate().substring(0,10);
        String newMonth=(String)pc.getNewMonth().substring(0,7);
    %>
</head>
<body onload="loadSys()">
	<form name="form1" method="POST">
	   <input type="hidden" id="newDate" name="newDate"  value="<%=newDate %>" />
	   <input type="hidden" id=newMonth  name="newMonth"  value="<%=newMonth %>" />
	</form>
</body>
<script type="text/javascript">
	//全局用户数据
	var dataLocalCode = userInfo['localCode'];
	var dataAreaId = userInfo['areaId'];
	var userRoleId= userInfo['roleId'];
	var url="";
	if(userRoleId =="213542"){//决策层
		  url="<%=request.getContextPath() %>/portalCommon/module/portal/index_jc.jsp";
	}
    if(userRoleId =="213543"){//管理层
		  url="<%=request.getContextPath() %>/portalCommon/module/portal/index_gl.jsp";
	}
    if(userRoleId =="213544"){//执行层
		  url="<%=request.getContextPath() %>/portalCommon/module/portal/othersDown.jsp";
	}
    //window.location.href=url;
   function loadSys(){
			document.form1.action=url;
			document.form1.submit();
	}
</script>  
</html>
