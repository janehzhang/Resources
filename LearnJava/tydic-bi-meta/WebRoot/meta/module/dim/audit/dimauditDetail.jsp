<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title></title>
    <script type="text/javascript">
        var dimTableId = <%=request.getParameter("tableId")%>;
        var batchId = <%=request.getParameter("batchId")%>;
    </script>

    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimAuditNewAction.js"></script>
    <script type="text/javascript" src="dimauditDetail.js"></script>
</head>
</html>