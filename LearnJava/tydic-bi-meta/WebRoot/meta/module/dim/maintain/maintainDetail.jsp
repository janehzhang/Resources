<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title>维度维护视图</title>
    <script type="text/javascript">
        var tableName='<%=request.getParameter("tableName")%>';
        var tableNameCn='<%=URLDecoder.decode(request.getParameter("tableNameCn"),"UTF-8")%>';
        var focus='<%=request.getParameter("focus")%>';
        var tableId='<%=request.getParameter("tableId")%>';
        var tableVersion='<%=request.getParameter("tableVersion")%>';
        var dataSourceId = '<%=request.getParameter("dataSourceId")%>';
        var tableUser = '<%=request.getParameter("tableUser")%>';
    </script>

    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableDimAction.js"></script>
     <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimMergeAction.js"></script>
    <script type="text/javascript" src="maintainDetail.js"></script>
    <style type="text/css">
        .leftCol{
            text-align: right;
            padding: 0px;
            width: 10%;
            border: 1px #D0E5FF solid;
            background-color: #FEFEFF;
            height: 10px;
            line-height: 0px;
            font-size: 11px;
        }
        .centerCol{
            text-align: left;
            padding: 0px;
            width: 45%;
            border: 1px #D0E5FF solid;
            height: 10px;
            line-height: 0px;
            font-size: 11px;
        }
        .rightCol{
            text-align: left;
            padding: 0px;
            width: 45%;
            border: 1px #D0E5FF solid;
            background-color: #FAFAFA;
            height: 10px;
            line-height: 0px;
            font-size: 11px;
        }
        .leftDim{
            text-align: right;
            padding: 5px;
            width: 30%;
            border: 1px #D0E5FF solid;
            background-color: #E9F5FE;
        }

    </style>

</head>
</html>