<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%--作者:张伟--%>
<%--时间:2011-11-16--%>
<%--描述:编码映射JSP--%>
<%@include file="../../../public/head.jsp" %>
<script type="text/javascript">
    var tableId='<%=request.getParameter("tableId")%>';
    var tableVersion='<%=request.getParameter("tableVersion")%>'
    var tableNameCn='<%=URLDecoder.decode(request.getParameter("tableNameCn"),"UTF-8")%>';
    var tableName='<%=request.getParameter("tableName")%>';

</script>
<head>
    <title>编码映射维护</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimMappingAction.js"></script>
    <script type="text/javascript" src="mapping.js"></script>
</head>
<body>
<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;table-layout: fixed;"   id="_layout">
    <tbody>
    <%--布局为两列--%>
    <tr style="height: 0px;">
        <th style="height: 0px; width: 80%; "></th>
        <th style="height: 0px; width: 20% "></th>
    </tr>

    <tr style="height: 100%;width:100%">
        <td style="height: 100%;width:100%;vertical-align: text-top; ">
            <div style="height: 100%;width: 100%;position:relative;padding: 0px" id="_treeGrid"></div>
            <%--treeGrid显示区域 --%>
            <%--<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;table-layout: fixed;" id="_treeGridLayout">--%>
            <%--<tbody>--%>
            <%--工具条列--%>
            <%--<tr style="height: 30px;width:100%">--%>
            <%--<td style=vertical-align: text-top; "height: 30px;width: 100%">--%>
            <%--<div style="height: 100%;width: 100%;position:relative;padding: 0px" id="_toolbar"></div>--%>
            <%--</td>--%>
            <%--</tr>--%>
            <%--<tr style="width:100%;height: 100%">--%>
            <%--<td style="width: 100%;height: 100%">--%>
            <%--<div style="height: 100%;width: 100%;position:relative;padding: 0px" id="_treeGrid"></div>--%>
            <%--</td>--%>
            <%--</tr>--%>
            <%--</tbody>--%>
            <%--</table>--%>
        </td>
        <td style="height: 100%">
            <table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;table-layout: fixed;" id="_treeGridLayout">
                <tbody>
                <%--归并类型--%>
                <tr style="height: 30px;width:100%">
                    <td style="height: 30px;width: 100%">
                        <div style="height: 100%;width: 100%;position:relative;padding: 0px" id="_toolbar">
                        </div>
                    </td>
                </tr>
                <tr style="height: 20px;width:100%">
                    <td style="height: 20px;width: 100%">
                        <div style="height: 100%;width: 100%; line-height:20px;:relative;padding: 0px 8px;">
                        	<input type="text" style="width: 88%;display: none;" id="searchCode" />
                        </div>
                    </td>
                </tr>
                <tr style="width:100%;">
                    <td style="width: 100%;">
                        <div style="height: 100%;width: 100%;position:relative;padding: 0px" id="_tree"></div>
                    </td>
                </tr>
                <tr style="height: 30px;width:100%">
                    <td style="height: 30px;width: 100%">
                        <div style="height: 100%;width: 100%;position:relative;padding: 0px;" id="_button">
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>

</body>
</html>