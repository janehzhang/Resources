<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <script type="text/javascript">
        var tableId='<%=request.getParameter("tableId")%>';
        var tableVersion='<%=request.getParameter("tableVersion")%>';
    </script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_STATE"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableViewAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/MaintainRelAction.js"></script>
    <script type="text/javascript" src="changeHistory.js"></script>
</head>
<body style="height: 100%;width: 100%">
<div id="detail" style="height: 100%;width: 100%"></div>
</body>
</html>