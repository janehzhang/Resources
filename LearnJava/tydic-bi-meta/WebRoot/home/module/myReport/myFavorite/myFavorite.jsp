<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../../meta/public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title>我收藏的报表</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/CommentRptAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/MyFavoliteAction.js"></script>
    <script type="text/javascript" src="myFavorite.js"></script>
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
<div id="container" style="height: 100%;width: 100%"></div>
<div id="pushDetail">
    <form action="" id="_pushDetail">
        <table cellpadding="0" cellspacing="0" style="width:100%;"
               class="" id="pushDetailInfo">
            <tr style="height: auto; display: none;">
                <th style="height: 0px; width: 30%"></th>
                <th style="height: 0px; width: 35%"></th>
                <th style="height: 0px; width: 35%"></th>
            </tr>
            <tr>
                <td class="leftDim" >报表名称：</td>
                <td colspan="2" style="text-align: left;padding: 2px;width: 70%;border: 1px #D0E5FF solid;" >
                    <div id="reportName"></div>
                </td>
            </tr>
            <tr>
                <td class="leftDim" style="height: 40px;" >报表说明：</td>
                <td colspan="2" style="text-align: left;padding: 5px;width: 80%;border: 1px #D0E5FF solid;height: 40px;" >
                    <textarea rows="2" cols="" style="width: 95%; height: 40px;" id="reportNote" readonly="readonly"></textarea>
                </td>
            </tr>
            <tr>
                <td class="leftDim" >发送方式：</td>
                <td colspan="2" style="text-align: left;padding: 2px;width: 70%;border: 1px #D0E5FF solid;" >
                    <input type="checkbox" id="isMail" style="display:none;" ><span id="isMailSpan" style="display:none;" >邮件</span>&nbsp;
                    <input type="checkbox" id="isMMS" style="display:none;"  ><span id="isMMSpan" style="display:none;" >彩信</span>&nbsp;
                    <input type="checkbox" id="isMS" style="display:none;" ><span id="isMSpan" style="display:none;" >短信</span>&nbsp;&nbsp;
                </td>
            </tr>
            <tr>
                <td class="leftDim" >发送类型：</td>
                <td colspan="2" style="text-align: left;padding: 2px;width: 70%;border: 1px #D0E5FF solid;" >
                    <select id="sendType" style="width: 100px;" onchange="sendTypeChange(this)">
                        <option value="1" >一次发送</option>
                        <option value="2" selected="selected">定时发送</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="leftDim" >起始发送时间：</td>
                <td colspan="2" style="text-align: left;padding: 2px;width: 70%;border: 1px #D0E5FF solid;" >
                    <input type="text" style="height: 80%;" id="sendBaseTime" value="" readonly="readonly"></td>
            </tr>
            <tr id="sendSeqTr" >
                <td class="leftDim" >发送频度：</td>
                <td colspan="2" style="text-align: left;padding: 2px;width: 70%;border: 1px #D0E5FF solid;" >
                    <select id="sendSequnce" style="width: 100px;">
                        <option value="5" selected="selected">每天一次</option>
                        <option value="4">每周一次</option>
                        <option value="3">每月一次</option>
                        <option value="2">半年一次</option>
                        <option value="1">一年一次</option>
                    </select>
                </td>
            </tr>
            <tr id="tmpSendTr" style="display: none;">
                <td class="leftDim" >发送频度：</td>

                <td colspan="2" style="text-align: left;padding: 2px;width: 70%;border: 1px #D0E5FF solid;" >
                    <select id="tmpSend" style="width: 100px;" disabled="disabled">
                        <option value="5" selected="selected">无</option>
                    </select>
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