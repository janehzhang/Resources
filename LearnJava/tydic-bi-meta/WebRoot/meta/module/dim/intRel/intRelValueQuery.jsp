<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%--作者:张伟--%>
<%--时间:2011-11-19--%>
<%--描述:维度接口值查询--%>
<%@include file="../../../public/head.jsp" %>
<head>
    <title></title>
    <script type="text/javascript">
        var tableName='<%=request.getParameter("tableName")%>';
        var tableNameCn='<%=URLDecoder.decode(request.getParameter("tableNameCn"),"UTF-8")%>';
        var focus='<%=request.getParameter("focus")%>';
        var tableId='<%=request.getParameter("tableId")%>';
        var tableVersion='<%=request.getParameter("tableVersion")%>'
        var sysId='<%=request.getParameter("sysId")==null?"":request.getParameter("sysId")%>'
        var sysName='<%=URLDecoder.decode(request.getParameter("sysName")==null?"":request.getParameter("sysName"),"UTF-8")%>'
        var tableOwner='<%=request.getParameter("tableOwner")%>';
    </script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimIntRelAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableDimAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="intRelValueQuery.js"></script>
</head>
<body>

</body>
</html>