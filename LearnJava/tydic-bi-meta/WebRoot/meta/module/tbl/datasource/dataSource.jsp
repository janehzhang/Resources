<%@ page import="tydic.frame.SystemVariable" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title>数据源管理</title>
    <script type="text/javascript">
        var deffaultMinCon='<%=SystemVariable.getInt("db.dynamic.minSize", 5)%>';
    </script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=DATA_SOURCE_TYPE"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DataSourceAction.js"></script>
    <script type="text/javascript" src="dataSource.js"></script>
    <style type="text/css">
        .leftNode{
            text-align: right;
            padding: 5px;
            width: 20%;
            border: 1px #D0E5FF solid;
            background-color: #E9F5FE;
        }
        .rightCol{
            text-align: left;
            padding: 0px;
            width: 30%;
            border: 1px #D0E5FF solid;
        }
    </style>
</head>
<body>
<div id="container" style="height: 100%;width: 100%"></div>
<form action="" id="_nodeDetail" style="width:100%;height: 100%;">
    <table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;" id="_nodeDetailInfo">
        <tr style="height: auto; display: none;">
            <th style="height: 0px; width: 20%"></th>
            <th style="height: 0px; width: 30%"></th>
            <th style="height: 0px; width: 20%"></th>
            <th style="height: 0px; width: 30%"></th>
        </tr>
        <tr>
            <td class="leftNode" >数据源名称：</td>
            <td class="rightCol" >
                <input name="dataSourceName" id="_dataSourceName" size="22">
                <input type="hidden" id="_dataSourceId" name="dataSourceId" value="-1">
            </td>
            <td class="leftNode" >主机名称：</td>
            <td class="rightCol" >
                <input name="dataSourceOraname" id="_dataSourceOraname" size="22">
            </td>
        </tr>
        <tr>
            <td class="leftNode" >用户名：</td>
            <td class="rightCol" >
                <input name="dataSourceUser" id="_dataSourceUser" size="22">
            </td>
            <td class="leftNode" >登录密码：</td>
            <td class="rightCol" >
                <input name="dataSourcePass" id="_dataSourcePass" size="22">
            </td>
        </tr>
        <tr>
            <td class="leftNode" >最小连接数：</td>
            <td class="rightCol" >
                <input name="dataSourceMinCount" id="_dataSourceMinCount" size="22" value="5">
            </td>
            <td class="leftNode" >所属系统：</td>
            <td class="rightCol" style="padding-left: 2px">
                <select name="sysId" id="_sysId" style="width: 90%;padding: 0px;">

                </select>
            </td>
        </tr>
        <tr>
            <td class="leftNode" >数据源类型：</td>
            <td class="rightCol" colspan="3" style="padding-left: 2px">
                <select onchange="typeChange(this)" name="dataSourceType" id="_dataSourceType" style="width: 35%;">
                    <%--<option value="TABLE">数据库</option>--%>
                    <%--<option value="FILE">文件</option>--%>
                </select>
            </td>
        </tr>
        <tr>
            <td class="leftNode" >数据源规则：</td>
            <td class="rightCol" colspan="3" style="padding-left: 2px">
                <div id="_ruleTable">
                    <%--<input name="dataSourceRule" id="_dataSourceRule" title="jdbc:oracle:thin:@IP地址:端口号:数据库实例名" style="width: 90%;color: #808080; font-style: italic;" value="jdbc:oracle:thin:@IP地址:端口号:数据库实例名" onclick="dataSourceRuleClk(this)">--%>
                    jdbc:oracle:thin:@<input name="ruleIp" id='_ruleIp' size='15' style="color: #808080; font-style: italic;" value='IP地址' title="IP地址" onclick="dataSourceRuleClk(this)" onblur="dataSourceRuleBlu(this)">:<input name="rulePort" id="_rulePort" size="2" value='1521' title="端口号" >:<input name="ruleInst" id="_ruleInst" size="10" style="color: #808080; font-style: italic;" value='数据库实例名' title="数据库实例名" onclick="dataSourceRuleClk(this)" onblur="dataSourceRuleBlu(this)">
                </div>
                <div id="_ruleFile" style="display: none;">
                    <input type="checkbox" name="ftp" id="_ftp" checked="">FTP
                    <input type="checkbox" name="zip" id="_zip">ZIP
                    <input type="checkbox" name="binary" id="_binary">BINARY
                    <input type="radio" name="fileType" id="_fileDmp" value="dmp" checked >DMP
                    <input type="radio" name="fileType" id="_fileTxt" value="txt" >TXT
                    <input type="text" name="fieldSplit" id="_fieldSplit" title="字段分隔符" size="6" style="color: #808080; font-style: italic;" value="字段分隔符" onclick="fieldSplitClk(this)">
                    <input type="text" name="rowSplit" id="_rowSplit" title="行分隔符" size="6" style="color: #808080; font-style: italic;" value="行分隔符" onclick="rowSplitClk(this)">
                </div>
            </td>
        </tr>
        <tr>
            <td class="leftNode" >数据源说明：</td>
            <td class="rightCol" colspan="3" style="padding: 5px;">
                <textarea name="dataSourceIntro" id="_dataSourceIntro" rows="3" style="width: 90%;">
                </textarea>
            </td>
        </tr>
        <tr>
            <td colspan="4" style="text-align: center;">
                <div id="_addButtonDiv"></div>
            </td>

        </tr>
    </table>
</form>
</body>
</html>