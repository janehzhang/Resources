<%@ page import="tydic.frame.SystemVariable" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title>指标分类管理</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/_GdlGroupAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=GDL_GROUP_TYPE"></script>
    <script type="text/javascript" src="_gdlGroup.js"></script>
</head>
<body>
<div id="container" style="height: 100%;width: 100%"></div>
<div id="typeContext">
    <form action="" id="_typeForm" style="width:100%;height: 100%" onsubmit="return false;">
        <div style="height: 100%; width:100%; ">
            <div style="width:100%" class="gridbox" id="_typeTable">
                <div style="width: 100%; overflow-x: hidden; overflow-y: hidden; position: relative; height: 30px; "
                     class="xhdr">
                    <table cellpadding="0" cellspacing="0" style="width: 100%;height: 100%; table-layout: fixed;" class="hdr">
                        <tbody>
                        <tr style="height: auto; ">
                            <%--定义表头列宽--%>
                            <th style="height: 0px; width: 75% "></th>
                            <th style="height: 0px; width: 25% "></th>
                        </tr>
                        <tr>
                            <td style="text-align: center; ">分类类型名称</td>
                            <td style="text-align: center; "><img src="../../../resource/images/edit_add.png" title="增加" onclick="addTypeRow(null)" style="width:16px;height: 16px;cursor: pointer"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div style="width: 100%; height: 270px; overflow: auto;" class="objbox" >
                    <table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" class="obj" id="_typeContentTable">

                        <tbody id="_typeContent">
                        <%--定义表头列宽--%>
                        <th style="height: 0px; width: 75% "></th>
                        <th style="height: 0px; width: 25% "></th>
                        </tbody>
                    </table>
                </div>
            </div>
            <div id="_typeWindowButton" style="height: 30px"></div>
        </div>
    </form>
</div>
</body>
</html>