<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="../../../public/head.jsp" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="tydic.meta.module.dim.merge.CodeBean" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>

</head>
<body>
<div id="main" style="height: 100%;width: 100%">
    <div id="error" style="height: 100%;width: 100%; overflow-y:auto;" class="gridbox gridbox_dhx_skyblue">
        <div id="errorhead" style="height:30px;width:100%;" class="xhdr">
            <table cellpadding="0" cellspacing="0" style="width:100%;" class="hdr">
                <thead>
                <tr style="height: 0px; ">
                    <th style="height: 0px; width: 25% "></th>
                    <th style="height: 0px; width: 25%"></th>
                    <th style="height: 0px; width: 50% "></th>
                </tr>
                <tr>
                    <td style="text-align:center">出错节点名称</td>
                    <td style="text-align:center">出错节点编码</td>
                    <td style="text-align:center">出错原因</td>
                </tr>
                </thead>
            </table>
        </div>
        <!--end head  -->
        <div id="errorbody" style="width: 100%;height:89%;overflow-y:auto;" class="objbox">
            <table id='errortable' cellpadding="0" cellspacing="0" style="width: 100%;" class="obj">
                <tbody>
                <tr style="height: 0px;">
                    <th style="height: 0px; width: 25% "></th>
                    <th style="height: 0px; width: 25% "></th>
                    <th style="height: 0px; width: 50% "></th>
                </tr>
                <%
                    List<CodeBean> errorList = (List<CodeBean>) session.getAttribute("errorList");
                    if (errorList != null && errorList.size() != 0) {
                        for (int i = 0; i < errorList.size(); i++) {
                            CodeBean codeBean = errorList.get(i);
                            if (codeBean != null) {%>
                <tr>
                    <td><%=codeBean.getItemName()%>
                    </td>
                    <td><%=codeBean.getItemCode()%>
                    </td>
                    <td><%=codeBean.getErrorInfo()%>
                    </td>
                </tr>
                <%
                            }
                        }
                    }
                %>
                </tbody>
            </table>
        </div>
        <!--end body  -->
    </div>
    <!--end typeDiv  -->
</div>
</body>
</html>
