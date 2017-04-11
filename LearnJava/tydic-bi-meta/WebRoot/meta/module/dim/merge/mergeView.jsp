<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%--作者:张伟--%>
<%--时间:2011-11-22--%>
<%--描述:查看归并编码JSP文件--%>
<head>
    <title>查看归并编码</title>
    <%@include file="../../../public/head.jsp" %>
    <script type="text/javascript">
        var tableId='<%=request.getParameter("tableId")%>';
        var tableVersion='<%=request.getParameter("tableVersion")%>'
        var tableNameCn='<%=URLDecoder.decode(request.getParameter("tableNameCn"),"UTF-8")%>';
        var tableName='<%=request.getParameter("tableName")%>';
        var tableOwner='<%=request.getParameter("tableOwner")%>';
    </script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableDimAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimMergeAction.js"></script>
    <script type="text/javascript" src="mergeView.js"></script>
</head>
<body>
<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;table-layout: fixed;"   id="_layout">
    <tbody>
    <%--布局为两列--%>
    <%--工具条列--%>
    <tr style="height: 30px">
        <td style="height: 30px;">
            <div style="height: 100%;width: 98%;position:relative;padding: 0px;" id="_toolbar"></div>
        </td>
        <td style="height: 30px;">
            <div style="height: 100%;width: 100%;position:relative;padding: 0px" id="_codeToolbar"></div>
        </td>
    </tr>
    <%--显示区域grid--%>
    <tr >
        <%--维度编码信息显示区域--%>
        <td rowspan="3" id="_middleArea" style="vertical-align: top;">
            <div style="height: 100%;width: 100%;" id="_dimDataGrid"></div>
        </td>
        <%--归并类型编码--%>
        <td>
            <div style="height: 100%;width: 100%;" id="_dimTypeGrid"></div>
        </td>
    </tr>
    </tbody>
</table>

</body>
</html>