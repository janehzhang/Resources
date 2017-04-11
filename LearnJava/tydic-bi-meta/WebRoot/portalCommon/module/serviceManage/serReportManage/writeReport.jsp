<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html lang="en" xml:lang="en">
<head>
    <title>填写报告</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/WriteReportAction.js"></script>
    <style type="text/css">
       .combo {
            padding: 0px;
            margin: 0px;
        }
        .combo div {
            float: left;
        }
        .label_anou div {
            width: 250px;
        }
        div.dhtmlx_wins_body_inner .dhxlist_obj_dhx_skyblue div.dhxlist_base {
            margin: 0px;
        }
        .zero{
            padding: 0px;margin: 0px;
        }
    </style>
</head>
<body style="height: 100%;width: 100%">
    <form id="queryform" name="queryform">
	</form>
	<iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="writeReport.js"></script>
