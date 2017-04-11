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
    <%@include file="../../../public/header.jsp"%>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/i18n.jsp?menuId=<%=menuId%>"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=GDL_NUMFORMAT"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlCalcAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlAction.js"></script>
    <script type="text/javascript">
        <%
            String gdlId = request.getParameter("gdlId");
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
        var MenuLoaclInfo = {
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
    <script type="text/javascript" src="calcGdlMag.js"></script>
</head>
<body style='width:100%;height:100%;'>
<table>
    <tr>
        <td style="width:220px;vertical-align:top;padding:10px 2px;min-width:220px">
        <%--树--%>
        <div style="margin-bottom:10px;">
            <input type="text" id="keyWord" style="width:140px;">
            <input type="button" id="searchBtn" class="btn_2" value="<%=I18nManager.getItemText(menuId,"BTN_SEARCH","搜 索")%>">
        </div>
        <div id="gdlTreeDIV" style="width:215px;min-height:480px;border:1px solid #a9a9a9;"></div>
        </td>
        <td style="vertical-align:top;">
        <%--表单--%>
        <table class="MetaFormTable" id='gdlFormTable' style="padding:0;margin-top:10px;width:880px;min-width:875px" border="0" cellpadding="0" cellspacing="1">
            <tr>
                <th width="15%"><%=I18nManager.getItemText(menuId,"CALC_EXPR","计算表达式")%>:<font style="color:red;font-weight:normal;">*</font></th>
                <td colspan="3" style="padding:3px 0 0 3px;">
                    <div id="exprDIV" style="float:left;width:550px;height:100px;overflow:auto;">
                        <iframe id="exprFrame" style="width:100%;height:97%;" marginWidth="0" marginHeight="0"></iframe>
                    </div>
                    <div style="float:left;width:180px;">
                        <div>小提示:<br><font color="red" style="font-weight:bold;">双击右边指标节点选入指标</font>
                            <br><br>
                            编写如:(a+b/100)*c格式的表达式
                            <br>(其中a,b,c代表指标)
                        </div>
                        <input name="" style="margin-top:0px;margin-left:10px;" id="checkBtn" type="button"
                               value="<%=I18nManager.getItemText(menuId,"BTN_CHECK","校验")%>" class="btn_2" />
                    </div>
                </td>
            </tr>
            <tr>
            <th style="min-height:24px;height:24px;"><%=I18nManager.getItemText(menuId,"SUP_DIMS","支持的维度")%>:&nbsp;</th>
            <td colspan="3" style="white-space:normal;font-size:15px;" id="supportDimTD"></td>
            </tr>
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
                <td><input type="text" id="gdlUnit"></td>
            </tr>
            <tr>
                <th><%=I18nManager.getItemText(menuId,"GDL_CODE","指标分类")%>:<font style="color:red;font-weight:normal;">*</font></th>
                <td colspan="3"><input type="text" id="gdlGroup"></td>
            </tr>
            <tr>
                <th><%=I18nManager.getItemText(menuId,"GDL_DESC","指标解释")%>:<font style="color:red;font-weight:normal;">*</font></th>
                <td colspan="3"><textarea style="width:550px;height:80px;" id="gdlDesc"></textarea></td>
            </tr>
        </table>
        <div style="text-align:center;padding-top:30px">
            <input name="" id="newBtn" type="button" value="<%=I18nManager.getItemText(menuId,"SAVE_BTN","保 存")%>" class="btn_2" />
            &nbsp;&nbsp;
            <input name="" id="clearBtn" type="button" value="<%=I18nManager.getItemText(menuId,"BTN_CLEAR","重 置")%>" class="btn_2" />
        </div>
    </td></tr>
</table>
</body>
</html>