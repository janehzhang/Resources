<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../../meta/public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title>我创建的报表</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/MyCreateAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DeptAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/StationAction.js"></script>
    <script type="text/javascript" src="myCreate.js"></script>
    <style type="text/css">
        .leftDim{
            text-align: right;
            padding: 2px;
            width: 30%;
            border: 1px #D0E5FF solid;
            background-color: #E9F5FE;
        }
    </style>
</head>
<body>
<div id="container" style="height: 100%;width: 100%; bor"></div>
<div id="shareDetail">
    <form action="" id="_shareDetail">
        <table cellpadding="0" cellspacing="0" style="width:100%;"
               class="" id="shareDetailInfo">
            <tr style="height: auto; display: none;">
                <th style="height: 0px; width: 30%"></th>
                <th style="height: 0px; width: 35%"></th>
                <th style="height: 0px; width: 35%"></th>
            </tr>
            <tr>
                <td class="leftDim" >报表名称：</td>
                <td colspan="2" style="text-align: left;padding: 2px;width: 70%;border: 1px #D0E5FF solid;" >
                    <div id="reportName"></div>
                    <%--<div id="_relType"></div>--%>
                </td>
            </tr>
            <tr>
                <td class="leftDim" style="height: 40px;" >报表说明：</td>
                <td colspan="2" style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;height: 40px;" >
                    <%--<div id="_relComm"></div>--%>
                    <textarea rows="2" cols="" style="width: 90%; height: 40px;" id="reportNote" readonly="readonly"></textarea>
                </td>
            </tr>
            <tr>
                <td class="leftDim" >共享设置：</td>
                <td colspan="2" style="text-align: left;padding: 2px;width: 70%;border: 1px #D0E5FF solid;" >
                    <select id='rightLevel' style="width: 80px;" onchange="rightChange(this)">
                        <option value='0'>公共</option>
                        <option value='1'>指定用户</option>
                    </select>
                </td>
            </tr>
            <tr id="shareContentDiv">
                <td class="leftDim" style="height: 105px;" >共享给：</td>
                <td colspan="2" style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;height: 105px;" >
                    <%--<div id="_relComm"></div>--%>
                    <%--<textarea readonly="readonly" rows="2" cols="" style="width: 80%; height: 90px;" id="reportUser" ></textarea>--%>
                    <div style="width: 92%; height: 90px; overflow: auto; float: left; border-right: 1px #D0E5FF solid;" id="reportUser"></div>
                    <div style="float: left; margin-top: 65px;"><img src="../../../resource/images/edit_add.png" title="添加人员" onclick="addShareUser()"></div>
                    <div style="clear: both;"></div>
                    <input type="hidden" id="refUserIds"></td>
            </tr>
            <tr id="fixShare">
                <td class="leftDim" style="height: 105px;" >共享给：</td>
                <td colspan="2" style="text-align: left;padding: 5px;width: 70%;border: 1px #D0E5FF solid;height: 105px;" >
                    <textarea disabled="" readonly="readonly" rows="2" cols="" style="width: 90%; height: 90px;" id="textATemp"></textarea>
                </td>
            </tr>
            <tr>
                <td style="text-align: center;" colspan="2"><div id="buttonDiv"></div></td>
            </tr>

        </table>

    </form>
</div>
</body>
</html>