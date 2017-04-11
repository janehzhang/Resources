<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title>创建基础指标</title>
    <%@include file="../../../public/header.jsp"%>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=GDL_GROUP_METHOD,GDL_NUMFORMAT"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlBasicMagAction.js"></script>
    <script type="text/javascript" src="addGdlBasic.js"></script>
	<script type="text/javascript">
		var tableId=<%=request.getParameter("tableId")%>;			//表类id
	</script>
</head>

<body style="height: 100%;width: 100%">
<div id="pageContent" style="height:100%">
    <div style="width:100%;margin:2px;" id="dataGridDiv">
    </div>
    <div style="width:100%;text-align: center;vertical-align: middle;height: 35px;" id="buttonDiv">
    	<input name="" id="nextStep" type="button" value="下一步" class="btn_2" />
    </div>
    
    <div style="width:100%;margin:2px;" id="dataGridDiv2">
    </div>
    <div style="width:100%;text-align: center;vertical-align: middle;height: 35px;" id="buttonDiv2">
	    <input name="" id="upStep" type="button" value="上一步" class="btn_2" />
	    <input name="" id="submitAudit" type="button" value="提交审核" class="btn_4" />
    </div>
</div>
</body>
</html>