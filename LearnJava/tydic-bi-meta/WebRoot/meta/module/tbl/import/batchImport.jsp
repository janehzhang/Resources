<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title>系统初始化批量导入表</title>
    <%@include file="../../../public/head.jsp" %>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/ImportBatchTableAction.js"></script>
    <script type="text/javascript" src="batchImport.js"></script>
</head>

<body style="height: 100%;width: 100%">
<%--选择维表页面布局--%>
<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;position: relative;"
       class="dhtmlxInfoBarLabel formTable" id="_dimInfoTable">

    <form action="" id="_queryDimForm" onsubmit="return false;">
        <tr style="height: 0px;">
	        <th style="height: 0px; width: 15% "></th>
	        <th style="height: 0px; width: 25% "></th>
	        <th style="height: 0px; width: 15% "></th>
	        <th style="height: 0px; width: 25% "></th>
	        <th style="height: 0px; width: 20% "></th>
	    </tr>
        <%--查询表单行--%>
        <tr style="height: 10%">
            <td  style="text-align: right;padding: 2px ">查询维表：</td>
            <td style="text-align: left;padding: 2px">
                <input type="hidden" name="tableGroupId" id="_queryDimId">
                <%--隐藏表类型ID--%>
                <input type="hidden" name="tableTypeId" value="2">
                <input class="dhxlist_txt_textarea" name="tableGroup" id="_queryDimName" type="text" style="padding-right: 0px;width: 100%">
            </td>
            <td style="text-align: right;padding: 2px">名称：</td>
            <td style="text-align: left;padding: 2px">
                <input class="dhxlist_txt_textarea" name="queryMessage" id="_queryDimTableName" type="text" style="width: 100%; ">
            </td>
            <td style="text-align: center;padding-left:20px;" id="_queryDimButton"></td>
        </tr>
    </form>
    <!--维度表查询grid-->
    <tr>
        <td colspan="5" style="width:100%;height: 90%;">
            <div id="_queryDimGrid" style="width:100%;height:100%;"> </div>
        </td>
    </tr>
</table>

<%--维度选择页面维表关联属性布局--%>
<table cellpadding="0" cellspacing="0" style="width:100%;height: auto;position: relative;"
       class="dhtmlxInfoBarLabel formTable" id="_dimRefAttrTable">
    <form action="" id="_dimRefAttrForm" onsubmit="return false;">
        <tr style="height: 0px; ">
            <th style="height: 0px; width: 20% "></th>
            <th style="height: 0px; width: 30% "></th>
            <th style="height: 0px; width: 20% "></th>
            <th style="height: 0px; width: 30% "></th>
        </tr>
        <tr>
            <td  style="text-align: right;padding: 5px "><span style="color: red">*</span>关联字段：</td>
            <td style="text-align: left;padding: 5px">
                <select style="width: 100%;" name="refColumn" id="_refColumn">
                </select>
            </td>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
            <td  style="text-align: right;padding: 5px "><span style="color: red">*</span>维度归并类型：</td>
            <td style="text-align: left;padding: 5px">
                <select style="width: 100%;" name="dimType" id="_dimType">
                </select>
            </td>
            <td style="text-align: right;padding: 5px"><span style="color: red">*</span>维层级：</td>
            <td style="text-align: left;padding: 5px">
                <select style="width: 100%;" name="selectLevel" id="_selectLevel">
                </select>
            </td>
        </tr>
        <tr>
            <%--窗口关闭确定按钮TD--%>
            <td colspan="4" style="text-align: center;" id="_dimInfoWindowButton">
            </td>
        </tr>
    </form>
</table>
</body>
</html>