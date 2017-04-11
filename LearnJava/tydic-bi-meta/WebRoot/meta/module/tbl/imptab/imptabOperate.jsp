<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="../../../public/head.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableExamineAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableViewAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DiffAction.js"></script>
    <script type="text/javascript">
     var dataSourceID='<%=request.getParameter("dataSourceID")%>';
     var owner='<%=request.getParameter("owner")%>';
     var tableName='<%=request.getParameter("tableName")%>';
     var tableId='<%=request.getParameter("tableId")%>';
     var tableVersion='<%=request.getParameter("tableVersion")%>'
     var metaTableName='<%=request.getParameter("metaTableName")%>';
    </script>
    <title>差异分析</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/ImportTableAction.js"></script>
    <script type="text/javascript" src="imptabOperate.js"></script>
</head>
<body>

</body>
</html>