<%--
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王春生
 * @description 
 * @date 12-6-6
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.meta.module.gdl.GdlConstant" %>
<html>
<head>
    <title></title>
    <%@include file="../../../public/header.jsp"%>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/i18n.jsp?menuId=<%=menuId%>"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=GDL_NUMFORMAT"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlCompositeMagAction.js"></script>
    <script type="text/javascript">
        var gdlId = "";
        var optFlag = "0";//操作标识，0新增，1修改
        var DIM_CODE_VIEWFLAG = 5;//维度展示checkbox伐值
        <%
            String gdlId = request.getParameter("gdlId");
            if(gdlId==null || "".equals(gdlId))
                gdlId = request.getParameter("gdlID");
            if(gdlId==null || "".equals(gdlId))
                gdlId = request.getParameter("gdl_id");
            if(gdlId==null || "".equals(gdlId))
                gdlId = request.getParameter("gdl_ID");
                
            String optFlag = request.getParameter("optFlag");
            out.println("gdlId = "+Convert.toInt(gdlId,0)+";");
            out.println("optFlag = "+Convert.toInt(optFlag,0)+";");
            out.println("DIM_CODE_VIEWFLAG="+GdlConstant.DIM_CODE_VIEWFLAG+";");
        %>
        var MenuLoaclInfo = {
            DIM_TERM_TITLE:"维度条件信息",
            DIM_TYPE:"归并类型",
            GDL_INFO:"指标基本信息",
            GDL_NAME:"指标名称",
            GDL_CODE:"指标编码",
            GDL_UNIT:"指标单位",
            GDL_GROUP:"指标类型",
            GDL_DESC:"指标解释",
            BTN_SAVE:"保存",
            BTN_BACK:"返回",
            BTN_RESET:"重置",
            BTN_CLEAR:"清空"
        };
        toLocal(MenuLoaclInfo);
    </script>

    <script type="text/javascript" src="compositeGdlMag.js"></script>
</head>
<body style='width:100%;height:100%;'>
<div style="overflow:auto;">
<div id="dimtermDIV" style="text-align:center;margin:10px 100px;min-width:900px;" >
    <div style="font-size:16px;font-weight:bold;height:25px;width:500px;text-align:left">
        <span class="icoTable"></span>
        <span class="icoTitle"><%=I18nManager.getItemText(menuId,"DIM_TERM_TITLE","维度条件信息")%></span>
    </div>
    <table class="MetaFormTable" id="dimtermTable" style="padding:0;margin-top:10px;width:90%;" border="0" cellpadding="0" cellspacing="1">
        <colgroup>
            <col width="15%">
            <col width="110px;">
            <col width="85%">
        </colgroup>
    </table>
</div>
<br>
<div id="gdlInfoDIV" style="text-align:center;margin:10px 100px;min-width:900px">
    <div style="font-size:16px;font-weight:bold;height:25px;margin-top:10px;width:500px;text-align:left;">
        <span class="icoTable"></span>
        <span class="icoTitle"><%=I18nManager.getItemText(menuId,"GDL_INFO","指标基本信息")%></span>
    </div>
    <%--查询条件表单模板--%>
    <table class="MetaFormTable" id="gdlFormTable" style="padding:0;margin-top:10px;width:90%;" border="0" cellpadding="0" cellspacing="1">
        <colgroup>
            <col width="15%">
            <col width="35%">
            <col width="15%">
            <col width="35%">
        </colgroup>
        <tr>
            <th><%=I18nManager.getItemText(menuId,"GDL_NAME","指标名称")%>:<font style="color:red;font-weight:normal;">*</font></th>
            <td><input type="text" id="gdlName"></td>
            <th><%=I18nManager.getItemText(menuId,"GDL_CODE","指标编码")%>:<font style="color:red;font-weight:normal;">*</font></th>
            <td><input type="text" id="gdlCode"></td>
        </tr>
        <tr>
            <th><%=I18nManager.getItemText(menuId,"NUM_FORMAT","默认展示格式")%>:<font style="color:red;font-weight:normal;">*</font></th>
            <td><select id="gdlNumFormat"></select></td>
            <th><%=I18nManager.getItemText(menuId,"GDL_NAME","指标单位")%>:<font style="color:red;font-weight:normal;">*</font></th>
            <td><input readonly="readonly" discs="1" type="text" id="gdlUnit"></td>
        </tr>
        <tr>
            <th><%=I18nManager.getItemText(menuId,"GDL_CODE","指标分类")%>:<font style="color:red;font-weight:normal;">*</font></th>
            <td colspan="3"><input type="text" id="gdlGroup"></td>
        </tr>
        <tr>
            <th><%=I18nManager.getItemText(menuId,"GDL_DESC","指标解释")%>:<font style="color:red;font-weight:normal;">*</font></th>
            <td colspan="3"><textarea style="width:610px;height:80px;" id="gdlDesc"></textarea></td>
        </tr>
    </table>
    <div style="text-align:center;padding-top:30px">
        <input name="" id="newBtn" type="button" value="<%=I18nManager.getItemText(menuId,"SAVE_BTN","保 存")%>" class="btn_2" />
        &nbsp;&nbsp;
        <input name="" id="backBtn" type="button" value="<%=I18nManager.getItemText(menuId,"BTN_RESET","重 置")%>" class="btn_2" />
    </div>
</div>
</div>
</body>
</html>