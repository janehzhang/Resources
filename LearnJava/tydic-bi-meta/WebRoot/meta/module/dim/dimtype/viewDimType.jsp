<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%--作者:张伟--%>
<%--时间:2011-11-21--%>
<%--描述:归并类型查看--%>
<%@ include file="../../../public/head.jsp"%>
<head>
    <title>查看归并类型</title>
    <script type="text/javascript">
        var tableId='<%=request.getParameter("tableId")%>';
        var tableVersion='<%=request.getParameter("tableVersion")%>'
        var tableNameCn='<%=URLDecoder.decode(request.getParameter("tableNameCn"),"UTF-8")%>';
        var tableName='<%=request.getParameter("tableName")%>';
    </script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimTypeAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="viewDimType.js"></script>
</head>
<body>

</body>
</html>