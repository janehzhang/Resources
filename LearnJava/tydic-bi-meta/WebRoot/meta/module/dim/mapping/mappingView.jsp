<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%--作者:张伟--%>
<%--时间:2011-11-22--%>
<%--描述:--%>
<%@include file="../../../public/head.jsp" %>
<script type="text/javascript">
    var tableId='<%=request.getParameter("tableId")%>';
    var tableVersion='<%=request.getParameter("tableVersion")%>'
    var tableNameCn='<%=URLDecoder.decode(request.getParameter("tableNameCn"),"UTF-8")%>';
    var tableName='<%=request.getParameter("tableName")%>';
    var tableOwner='<%=request.getParameter("tableOwner")%>';
</script>
<head>
    <title>查看编码映射</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimMappingAction.js"></script>
    <script type="text/javascript" src="mappingView.js"></script>
</head>
<body>

</body>
</html>