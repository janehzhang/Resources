<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="tydic.meta.common.JsonUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="tydic.meta.module.mag.login.LoginConstant" %>
<%@ page import="tydic.meta.common.Constant" %>
<%@ page import="tydic.meta.module.mag.user.UserConstant" %>
<%@ page import="tydic.meta.web.session.SessionManager"%>
<%@ page import="tydic.meta.web.MenuDataInit"%>
<%@ page import="tydic.frame.SystemVariable" %>
<%
    Map<String, Object> userData = (Map<String, Object>) session.getAttribute(LoginConstant.SESSION_KEY_USER);
	String debugFlag = userData.get("debugFlag") == null ? "0" : userData.get("debugFlag").toString().trim();
%>

<div id="sqlStr" ></div>
<script type="text/javascript">
    var debugFlag='<%=debugFlag%>';

    if(debugFlag!='1'){
   	 var nodeBtn=document.getElementById("sqlStr");
   	 nodeBtn.parentNode.removeChild(nodeBtn);
   }
</script>