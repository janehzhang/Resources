<%--
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author  刘弟伟
 * @description 
 * @date 12-6-5
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title></title>
    <%@include file="../../../public/header.jsp"%>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/i18n.jsp?menuId=<%=menuId%>"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_STATE,TABLE_TYPE"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlExamineAction.js"></script>
    <script type="text/javascript" src="examine.js"></script>
    <script type="text/javascript">
        var Debug = function(txt){
            $("deg").innerHTML = txt+"<br>"+$("deg").innerHTML;
        };
        var MenuLoaclInfo = {

        };
        toLocal(MenuLoaclInfo);
    </script>
</head>
<body style='width:100%;height:100%;'>
<div id="pageContent" style="position:absolute;top:0px;left:0px;right: 0px;bottom: 0px;overflow: auto;">
    <div style="padding:5px;" id="queryFormDIV">
        <%--查询条件表单模板--%>
        <table class="MetaTermTable" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="10%" style="font-weight: bold;text-align:right;"><%=I18nManager.getItemText(menuId,"","审核状态：")%></td>
                <td width="15%"> <div id="gdlState"></div></td>
                <td width="10%" style="font-weight: bold;text-align:right;"><%=I18nManager.getItemText(menuId,"","指标分类：")%></td>
                <td width="40%" colspan="3"><div id="gdlType"></div></td>
                <td width="25%"></td>
                
            </tr>
             <tr>
                <td style="font-weight: bold;text-align:right;"><%=I18nManager.getItemText(menuId,"","变更类型：")%></td>
                <td><div id="alertType"></div></td>
                <td style="font-weight: bold;text-align:right;"><%=I18nManager.getItemText(menuId,"","关键字：")%></td>
                <td colspan="3"><div id="keyWord"></div></td>
                <td>
                    <input type="button" value="<%=I18nManager.getItemText(menuId,"","查询")%>" id="queryBtn" class="btn_2">
                </td>
            </tr>
        </table>
    </div>
    <div style="min-height:200px;min-width:800px;border-top:1px solid #D0E5FF;position:absolute;top:69px;left:0px;bottom: 0px;right: 0px;" id="dataDiv">
    </div>
</div>
</body>
</html>