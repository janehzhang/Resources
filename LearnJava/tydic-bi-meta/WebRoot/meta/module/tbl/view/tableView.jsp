<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title>表类全息视图</title>
    <script type="text/javascript">
        var tableGroupId='<%=request.getParameter("tableGroupId")%>';
        var tableTypeId='<%=request.getParameter("tableTypeId")%>';
        var tableGroupName='<%=request.getParameter("tableGroupName")!=null?URLDecoder.decode(request.getParameter("tableGroupName"),"UTF-8"):""%>';
    </script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_TYPE"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableViewAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="tableView.js"></script>
    <style type="text/css">
    .queryItem *{
     	padding: 0px;
     	margin: 0px;
     	float: left;
     	display:block;
     	clear:both;
     }
    </style>
</head>
<body style="height: 100%;width: 100%">
<div id="container" style="height: 100%;width: 100%"></div>
</body>
</html>