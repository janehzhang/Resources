<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%@include file="../../../public/head.jsp" %>
<head>

</head>
<body style="background: #ffffff;">
<div id="main" style="overflow-y:auto;" class="gridbox gridbox_dhx_skyblue">
    <div id="typehead" style="height:11%;width:100%;" class="xhdr">
        <table cellpadding="0" cellspacing="0" style="width:100%;" class="hdr">
            <thead>
            <tr style="height: 0px; ">
                <th style="height: 0px; width: 15% "></th>
                <th style="height: 0px; width: 15%"></th>
                <th style="height: 0px; width: 15% "></th>
                <th style="height: 0px; width: 15% "></th>
                <th style="height: 0px; width: 40% "></th>
            </tr>
            <tr>
                <td style="text-align:center">出错维度编码</td>
                <td style="text-align:center">出错维度编码名称</td>
                <td style="text-align:center">出错原编码</td>
                <td style="text-align:center">出错原编码名称</td>
                <td style="text-align:center">出错原因</td>
            </tr>
            </thead>
        </table>
    </div>
    <!--end head  -->
    <div id="typebody" style="width: 100%;height:89%;overflow-y:auto;" class="objbox">
        <table id='dimtypetable' cellpadding="0" cellspacing="0" style="width: 100%;" class="obj">
            <tbody>
            <tr style="height: 0px;">
                <th style="height: 0px; width: 15% "></th>
                <th style="height: 0px; width: 15%"></th>
                <th style="height: 0px; width: 15% "></th>
                <th style="height: 0px; width: 15% "></th>
                <th style="height: 0px; width: 40% "></th>
            </tr>
            <%
                List<Map<String, Object>> errorList = (List<Map<String, Object>>) session.getAttribute("errorList");
                if (errorList != null && errorList.size() != 0) {
                    for (int i = 0; i < errorList.size(); i++) {
                        Map<String, Object> map = errorList.get(i);
                        if (map != null) {%>
            <tr>
                <td><%=map.get("itemName")%>
                </td>
                <td><%=map.get("itemCode")%>
                </td>
                <td><%=map.get("SRC_CODE")%>
                </td>
                <td><%=map.get("SRC_NAME")%>
                </td>
                <td><%=map.get("errorInfo").toString()%>
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
