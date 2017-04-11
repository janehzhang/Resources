<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%--作者:张伟--%>
<%--时间:2011-11-24--%>
<%--描述:--%>
<%@include file="../../../public/head.jsp" %>
<head>
    <title>差异分析</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DiffAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimMaintainAction.js"></script>

	<script type="text/javascript">
        var tableId='<%=request.getParameter("tableId")%>';
        var tableVersion='<%=request.getParameter("tableVersion")%>'
        var tableName='<%=request.getParameter("tableName")%>';
        var tableState='<%=request.getParameter("tableState")%>';
        DimMaintainAction.queryValidVersion(tableId, {
            callback:function (data) {
				tableVersion = data;
            },
            async:false
        });
    </script>
    <script type="text/javascript" src="diffAnalysis.js"></script>
</head>
<body>

</body>
</html>
