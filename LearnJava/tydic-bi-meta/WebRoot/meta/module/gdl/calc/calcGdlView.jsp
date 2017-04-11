<%--
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王春生
 * @description 
 * @date 12-6-18
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title></title>
    <%@include file="../../../public/header.jsp"%>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/i18n.jsp?menuId=<%=menuId%>"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlCalcAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlAction.js"></script>
    <script type="text/javascript">
        <%
            String gdlId = request.getParameter("gdlId");
            String gdlVersion = request.getParameter("gdlVersion");
            if(gdlId==null)
                gdlId = request.getParameter("gdlID");
            if(gdlId==null)
                gdlId = request.getParameter("gdlid");
            if(gdlId==null)
                gdlId = request.getParameter("gdl_id");
            if(gdlId==null)
                gdlId = request.getParameter("GDL_ID");

        %>
        var gdlId = <%=Convert.toInt(gdlId,0)%>;
        var gdlVersion = <%=Convert.toInt(gdlVersion,0)%>;
        var MenuLoaclInfo = {
            GDL_INFO:"指标基本信息",
            BTN_CHECK:"校验",
            BTN_SEARCH:"搜索",
            BTN_SAVE:"保存",
            BTN_BACK:"返回",
            BTN_CLEAR:"清空",
            CALC_EXPR:"计算表达式",
            SUP_DIMS:"支持的维度"
        };
        toLocal(MenuLoaclInfo);
    </script>
    <script type="text/javascript" src="calcGdlView.js"></script>
</head>
<body style='width:100%;height:100%;'>
<div style="overflow:auto;">
    <div id="gdlInfoDIV" style="text-align:center;margin:10px 100px;min-width:900px">
        <div style="font-size:16px;font-weight:bold;height:25px;margin-top:10px;width:500px;text-align:left;">
            <span class="icoTable"></span>
            <span class="icoTitle"><%=I18nManager.getItemText(menuId,"GDL_INFO","指标基本信息")%></span>
        </div>
        <%--查询条件表单模板--%>
        <table class="MetaFormTable" id='gdlFormTable' style="padding:0;margin-top:10px;width:880px;min-width:875px" border="0" cellpadding="0" cellspacing="1">
            <tr>
                <th style="width:15%;"><%=I18nManager.getItemText(menuId, "GDL_NAME", "指标名称")%>:</th>
                <td style="width:35%;"><div id="gdlName"></div></td>
                <th style="width:15%;"><%=I18nManager.getItemText(menuId,"GDL_CODE","指标编码")%>:</th>
                <td style="width:35%;"><div id="gdlCode"></div></td>
            </tr>
            <tr>
                <th><%=I18nManager.getItemText(menuId, "GDL_TYPE", "指标分类")%>:</th>
                <td><div id="gdlType"></div></td>
                <th><%=I18nManager.getItemText(menuId,"GDL_STATE","指标状态")%>:</th>
                <td><div id="gdlState"></div></td>
            </tr>
            <tr>
                <th><%=I18nManager.getItemText(menuId, "NUM_FORMAT", "默认展示格式")%>:</th>
                <td><div id="gdlNumFormat"></div></td>
                <th><%=I18nManager.getItemText(menuId,"GDL_NAME","指标单位")%>:</th>
                <td><div id="gdlUnit"></div></td>
            </tr>
            <tr>
                <th><%=I18nManager.getItemText(menuId,"GDL_CREATOR","创建人")%>:</th>
                <td><div id="gdlCreator"></div></td>
                <th><%=I18nManager.getItemText(menuId,"GDL_CREATE_DATE","创建时间")%>:</th>
                <td><div id="gdlCreateTime"></div></td>
            </tr>
            <tr>
                <th><%=I18nManager.getItemText(menuId,"GDL_CODE","指标分类")%>:</th>
                <td colspan="3" style="white-space:normal;"><div id="gdlGroup"></div></td>
            </tr>
            <tr>
                <th><%=I18nManager.getItemText(menuId,"GDL_DESC","指标解释")%>:</th>
                <td colspan="3" style="white-space:normal;min-height:50px;vertical-align:top"><div id="gdlDesc"></div></td>
            </tr>
        </table>
    </div>
    <div id="gdlCalcDIV" style="text-align:center;margin:10px 100px;min-width:900px;" >
        <div style="font-size:16px;font-weight:bold;height:25px;width:500px;text-align:left">
            <span class="icoTable"></span>
            <span class="icoTitle"><%=I18nManager.getItemText(menuId,"CALC_EXPR","表达式信息")%></span>
        </div>
        <table class="MetaFormTable" id="calcTable" style="padding:0;margin-top:10px;width:880px;min-width:875px" border="0" cellpadding="0" cellspacing="1">
            <tr>
                <th width="15%"><%=I18nManager.getItemText(menuId,"CALC_EXPR","计算表达式")%>:</th>
                <td colspan="3" style="padding:3px 0 0 3px;">
                    <div id="exprDIV" style="float:left;width:600px;height:100px;overflow:auto;">
                        <iframe id="exprFrame" style="width:100%;height:97%;" marginWidth="0" marginHeight="0"></iframe>
                    </div>
                </td>
            </tr>
            <tr>
                <th style="min-height:24px;height:24px;"><%=I18nManager.getItemText(menuId,"SUP_DIMS","支持的维度")%>:</th>
                <td colspan="3" style="white-space:normal;font-size:14px;" id="supportDimTD"></td>
            </tr>
        </table>
    </div>
    <div style="text-align:center;padding-top:30px">
        <input name="" id="closeBtn" type="button" value="<%=I18nManager.getItemText(menuId,"BTN_BACK","关 闭")%>" class="btn_2" />
    </div>
</div>
</body>
</html>