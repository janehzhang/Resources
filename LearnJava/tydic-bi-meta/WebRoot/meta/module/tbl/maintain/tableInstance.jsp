<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<style type="css/test">
   .fontStyle{
      font:bold 12px;
      color:#fff;
   }
</style>
    <script type="text/javascript">
        var tableName='<%=URLDecoder.decode(request.getParameter("tableName"),"UTF-8")%>';
        var focus='<%=request.getParameter("focus")%>';
        var tableId='<%=request.getParameter("tableId")%>';
        var tableVersion='<%=request.getParameter("tableVersion")%>';
        var isDimTable='<%=request.getParameter("isDimTable")%>';
    </script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableDimAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableDimAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/MaintainRelAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableViewAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableRelAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableFusionChartsAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/resource/js/flow.js"></script>
    <script type="text/javascript" src="<%=rootPath %>/portal/resource/js/FusionCharts.js"></script>
    <script type="text/javascript" src="tableInstance.js"></script>
</head>
<body style="height: 100%;width: 100%">
	<div id="detail" style="height: 100%;width: 100%"></div>
	<form action="" id="_charForm" style="height: 100%;width: 100%">
    <table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;"
           class="" id="_charTable">
        <tr style="height: auto; display: none;">
            <th style="height: 0px; width: 90%"></th>
            <th style="height: 0px; width: 10%"></th>
        </tr>

        <tr style="width: 100%;height: 100%;">
            <td style="width: 90%;height: 100%;">
                <div id="chartdiv" style="height: 100%;width: 90% "></div>
            </td>
            <td style="width: 10%;height: 100%;">
                <div id="chartRight"></div>
            </td>
        </tr>
    </table>
</form>
</body>
</html>