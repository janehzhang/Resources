<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%--作者:张伟--%>
<%--时间:2011-11-19--%>
<%--描述:接口表查询JSP--%>
<%@include file="../../../public/head.jsp" %>
<head>
    <title>接口表查询</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimIntRelAction.js"></script>
    <script type="text/javascript" src="intRelQuery.js"></script>
</head>
<body>
<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;table-layout: fixed;"   id="_layout">
    <tbody>
    <tr style="height: 60%;width:100%">
        <td style="height: 60%;width:100%;">
            <div style="height: 100%;width: 100%;position:relative;padding: 0px" id="_grid"></div>
        </td>

    </tr>

    <tr style="height: 30px;width:100%">
        <td style="height: 30px;width: 100%">
            <div style="height: 100%;width: 100%;position:relative;padding: 0px" id="_toolbar">
            </div>
        </td>
    </tr>
    <tr style="width:100%;">
        <td style="width: 100%;">
            <table  cellpadding="0" cellspacing="0" style="width:100%;height: 100%;margin-top:-32px;" id="_sqlInfo" >
                <tr style="height: 30%;width: 100%;">
                    <td style="text-align: right;vertical-align: middle;width: 15%;" class="leftCol tdBorder">
                        规则说明：
                    </td>
                    <td style="text-align:left;width: 70%;" id="" class="tdBorder">
						<div id="_ruleComment" style="overflow: auto;width: 98%;height: 90%;padding-left: 2xp;"></div>
                    </td>
                </tr>
                <tr style="height: 70%;width: 100%">
                    <td style="text-align: right;vertical-align: middle;" class="leftCol tdBorder">
                        映射规则SQL：
                    </td>
                    <td style="text-align:left;" class="tdBorder" id="">
						<div style="overflow: auto;width: 98%;height: 90%;padding-left: 2xp;" id="_mappingSQL"></div>
                    </td>
                </tr>
            </table>
            <%--<div style="height: 100%;width: 100%;position:relative;padding: 0px" id="_sqlInfo"></div>--%>
        </td>
    </tr>
    </tbody>
</table>

</body>
</html>