<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlRelAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/i18n.jsp?menuId=<%=menuId%>"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_TYPE,GDL_GROUP_METHOD"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/resource/js/flow.js"></script>
    <script type="text/javascript" src="relTableView.js"></script>
    <style type="text/css">
        .leftNode{
            text-align: right;
            padding: 0px;
            width: 40%;
            border: 1px #D0E5FF solid;
            background-color: #E3EFFF;
        }
        .rightCol{
            text-align: left;
            padding: 0px;
            width: 60%;
            border: 1px #D0E5FF solid;
        }
    </style>
    <script type="text/javascript" >
        //指标ID
        var gdlId = <%=request.getParameter("gdlId")!=null?request.getParameter("gdlId"):0 %>;

    </script>
</head>
<body style="height: 100%;width: 100%">
<div id="flow_div" style="height: 100%;width: auto;"></div>
<div id="linkSetDiv">
<form action="" id="_nodeDetail" style="width:100%;height: 100%;">
    <table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;" id="_nodeDetailInfo">
        <tr style="height: auto; display: none;">
            <th style="height: 0px; width: 40%"></th>
            <th style="height: 0px; width: 60%"></th>
        </tr>
        <tr>
            <td colspan="2" style="height: 255px;">
                <div id="linkDiv" style="height: 100%; width: 100%; overflow: auto;"></div>
            </td>
        </tr>
        <tr>
            <td class="leftNode" >已设置的指标关联列：</td>
            <td class="rightCol">
                <input name="relCol" id="_relCol" style="width: 90%;" readonly="readonly">
                <input type="hidden" id="_relColId" name="relColId" value="-1">
            </td>
        </tr>
        <tr>
            <td colspan="2" style="text-align: center;">
                <div id="_addButtonDiv" style="height: 30px;"></div>
            </td>
        </tr>
    </table>
</form>
</div>
<!-- 维度列grid -->
<div id="relColGridDiv" style="width: 530px;height: 100%;"></div>
<!-- 绑定维度grid -->
<div id="bandDimGridDiv" style="width: 530px;height: 100%"></div>
<!-- 支撑维度grid -->
<div id="supportDimGridDiv" style="width: 530px;height: 100%"></div>
</body>
</html>