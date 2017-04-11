<%--
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王春生
 * @description 
 * @date 12-6-11
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title></title>
    <%@include file="../../public/header.jsp"%>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/i18n.jsp?menuId=<%=menuId%>"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=GDL_TYPE,GDL_STATE"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlAction.js"></script>
    <script type="text/javascript">
        var viewFlag = <%=Convert.toInt(request.getParameter("viewFlag"),0)%>;
        var MenuLoaclInfo = {
            GDL_GROUP:"指标分类",
            GDL_TYPE:"指标类型",
            GDL_STATE:"状态",
            QUERY_KEY:"关键字",
            QUERY_BTN:"查询",
            BTN_NEW_CALC:"创建计算指标"
        };
        toLocal(MenuLoaclInfo);
    </script>
    <script type="text/javascript" src="gdllist.js"></script>
</head>
<body style='width:100%;height:100%;'>
<div id="pageContent" style="position:absolute;top:0px;left:0px;right: 0px;bottom: 0px;overflow: auto;">
    <div style="padding:5px;" id="queryFormDIV">
        <%--查询条件表单模板--%>
        <table class="MetaTermTable" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="10%" style="font-weight: bold;text-align:right;"><%=I18nManager.getItemText(menuId,"GDL_TYPE","指标类型")%>:</td>
                <td width="15%"><div id="gdlType"></div></td>
                <td width="10%" style="font-weight: bold;text-align:right;"><%=I18nManager.getItemText(menuId,"GDL_GROUP","指标分类")%>:</td>
                <td width="40%" colspan="3"><div id="gdlGroup"></div></td>
                <td width="25%"></td>
            </tr>
            <tr>
                <td style="font-weight: bold;text-align:right;"><%=I18nManager.getItemText(menuId,"GDL_STATE","状态")%>:</td>
                <td><div id="gdlState"></div></td>
                <td style="font-weight: bold;text-align:right;"><%=I18nManager.getItemText(menuId,"QUERY_KEY","关键字")%>:</td>
                <td colspan="3"><div id="kwd"></div></td>
                <td>
                    <input name="" id="queryBtn" type="button" value="<%=I18nManager.getItemText(menuId,"QUERY_BTN","查 询")%>" class="btn_2" />
                    &nbsp;&nbsp;
                    <input name="" id="newBtn" type="button" value="<%=I18nManager.getItemText(menuId,"BTN_NEW_CALC","创建计算指标")%>" class="btn_6" />
                    &nbsp;
                </td>
            </tr>
        </table>
    </div>
    <div style="min-height:200px;min-width:800px;border-top:1px solid #D0E5FF;position:absolute;top:69px;left:0px;bottom: 0px;right: 0px;" id="dataDiv">
    </div>
</div>
</body>
</html>