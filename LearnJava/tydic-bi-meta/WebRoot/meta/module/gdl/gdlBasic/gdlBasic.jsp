<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title>基础指标申请</title>
    <%@include file="../../../public/header.jsp"%>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlBasicMagAction.js"></script>
    <script type="text/javascript" src="gdlBasic.js"></script>
</head>

<body style="height: 100%;width: 100%">
<div id="pageContent" style="position:absolute;top:0px;left:0px;right: 0px;bottom: 0px;overflow: auto;">
	<div style="padding:5px;border-bottom:1px solid #D0E5FF;" id="queryFormDIV">
		<%--查询条件表单模板--%>
        <table class="MetaTermTable" style="" border="0" cellpadding="0" cellspacing="0">
            <tr>
            	<td style="width: 8%; text-align: right;font-weight: bold;">数据源：</td>
            	<td style="width: 16%; text-align: left;"><div id="dataSourceId"></div></td>
                <td style="width: 8%; text-align: right;font-weight: bold;">所属用户：</td>
            	<td style="width: 16%; text-align: left;"><div id="tableOwner"></div></td> 
                <td style="width: 8%; text-align: right;font-weight: bold;">关键字：</td>
            	<td style="width: 16%; text-align: left;"><div id="keywords"></div></td>
                <td style="width: 28%; text-align: center">
                   <input name="" id="queryBtn" type="button" value="查询" class="btn_2" />
                </td>
            </tr>
        </table>
	</div>
	<div style="left:0px;bottom: 0px;right: 0px;" id="dataGridDiv">
    </div>
</div>
</body>
</html>