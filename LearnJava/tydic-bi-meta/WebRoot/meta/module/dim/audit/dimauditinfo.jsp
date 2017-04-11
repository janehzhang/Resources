<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimAuditNewAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript">
        var dimTableId = <%=request.getParameter("tableId")%>;
        var batchId = <%=request.getParameter("batchId")%>;
        var tableName = '<%=request.getParameter("tableName")%>';
        var tableDimPrefix = '<%=request.getParameter("tableDimPrefix")%>';
        var tableUser = '<%=request.getParameter("tableOwner")%>';
        <%--var dimTypeId = <%=request.getParameter("dimTypeId")%>;--%>
    </script>
    <script type="text/javascript" src="dimauditinfo.js"></script>
    <style type="text/css">
        #_dimTableForm{
            margin-right: auto;
            margin-left: auto;

            text-align: center;
            font-size: 12px;
            margin-top: 2px;
            border-bottom: #99b2c9 solid 1px;
        }
        #_dimTableForm tbody tr td {
            text-align: left;
            padding-left: 2px;
            line-height: 15px;
            background-color: #ffffff;
        }
        #_dimTableForm tbody tr th {
            white-space: nowrap;
            text-align: right;
            font-weight: normal;
            padding: 0px;
            margin: 0px;
        }
    </style>

</head>
<body style="width:100%;height:100%">
<div id="container" style="height: 100%;width: 100%">
    <div style="height: 40px" id = 'content'>
        <table style="table-layout: fixed;width: 100%">
            <tbody>
            <tr>
                <td  align="right" style="width:4%"><div style="background-color: #64B201;height: 12px;width: 12px;"></div></td>
                <td  align="left" style="width:12%">新增编码</td>
                <td  align="right" style="width:4%"><div style="background-color: #63B8FF;height: 12px;width: 12px;"></div></td>
                <td align="left"  style="width:12%" >归并编码</td>
                <td  align="right" style="width:4%"><div style="background-color: #EEC900;height: 12px;width: 12px;"></div></td>
                <td  align="left" style="width:12%">层级变更</td>
            </tr>
            <tr>
                <td  align="right" style="width:4%"><div style="background-color: #7A378B;height: 12px;width: 12px;"></div></td>
                <td align="left" style="width:12%">修改编码</td>
                <td  align="right" style="width:4%"><div style="background-color: red;height: 12px;width: 12px;"></div></td>
                <td align="left" style="width:12%">停用编码</td>
                <td  align="right" style="width:4%"><div style="background-color: #F29FB5;height: 12px;width: 12px;"></div></td>
                <td align="left" style="width:12%">启用编码</td>
            </tr>
            </tbody></table>
    </div>
</div>
<form action="" id="_dimTableForm" style="width:100%;height: 100%;">
    <table cellpadding="0" cellspacing="0" style="width:100%;height: 98%"
           class="dhtmlxInfoBarLabel formTable" id="_dimauditinfo">
        <tr style="height: 80%;width: 100%">
            <td style="width: 20%;text-align: right;" align="right" >审核意见：</td>
            <td style="width: 80%">
                <textarea name="auditMark" id="_auditMark" rows="3" style="width: 90%"></textarea><span style="color: red">*</span>
            </td>
        </tr>
        <tr style="height: 20%">
            <td style="height: 100%;width: 100%" colspan="2">
                <div style="height: 100%; width:100%; position: relative;" id="_submit"></div>
            </td>
        </tr>
    </table>
</form>


<%--<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;table-layout: fixed;" id="_dimauditinfo">--%>
<%--<!--维度映射表格显示区域-->--%>
<%--<tr style="height: 70%">--%>
<%--<td style="height: 100%">--%>
<%--<div style="height: 100%;width: 100%;" id="_dimDataGrid"></div>--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr style="height: 20%">--%>
<%--<td>--%>
<%--审核意见：<textarea name="auditinfo" id="_auditinfo" rows="3" style="width: 90%"></textarea>--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr style="height: 10%">--%>
<%--<td>--%>
<%--<div style="height: 30px; width:100%; position: relative;" id="_submit"></div>--%>
<%--</td>--%>
<%--</tr>--%>
<%--</table>--%>


</body>
</html>
