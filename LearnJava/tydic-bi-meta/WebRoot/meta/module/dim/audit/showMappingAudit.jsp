<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%--作者:李国民--%>
<%--时间:2012-02-08--%>
<%--描述:审核编码映射JSP--%>
<%@include file="../../../public/head.jsp" %>
<head>
    <title>查看编码映射审核结果</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/ShowMappingAuditAction.js"></script>
    <script type="text/javascript" src="showMappingAudit.js"></script>    <script type="text/javascript">
		var batchId='<%=request.getParameter("batchId")%>';
		var dimTableId='<%=request.getParameter("dimTableId")%>';
    </script>
</head>

<body style="height: 100%;width: 100%">
</body>
</html>