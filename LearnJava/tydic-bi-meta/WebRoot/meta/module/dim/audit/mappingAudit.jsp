<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%--作者:李国民--%>
<%--时间:2012-02-08--%>
<%--描述:审核编码映射JSP--%>
<%@include file="../../../public/head.jsp" %>
<head>
    <title>审核编码映射</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/MappingAuditAction.js"></script>
    <script type="text/javascript" src="mappingAudit.js"></script>    <script type="text/javascript">
		var batchId='<%=request.getParameter("batchId")%>';
		var dimTableId='<%=request.getParameter("dimTableId")%>';
		var tableName='<%=request.getParameter("tableName")%>';
		var tableDimPrefix='<%=request.getParameter("tableDimPrefix")%>';
		var tableOwner='<%=request.getParameter("tableOwner")%>';
    </script>
</head>

<body style="height: 100%;width: 100%">
</body>
</html>