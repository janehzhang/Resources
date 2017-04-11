<%--
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王春生
 * @description 
 * @date 12-6-4
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title></title>
    <%@include file="../../../public/header.jsp"%>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/i18n.jsp?menuId=<%=menuId%>"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlGroupAction.js"></script>
    <script type="text/javascript" src="gdlGroup.js"></script>
    <script type="text/javascript">
        var MenuLoaclInfo = {
            QUERY_KEY:"关键字",
            QUERY_BTN:"查询",
            COL_TITLE:"指标分类",
            COL_OPT:"操作",
            NEWROOT_BTN:"新增根分类",
            NEWTYPE_BTN:"新增同级分类",
            NEWCHILD_BTN:"新增下级分类",
            LEVEL_BTN:"确认分类层次",
            MODIFY_A:"修改",
            DELETE_A:"删除",
            WIN_TITLE_1:"新增分类",
            WIN_TITLE_2:"编辑分类",
            GROUP_NAME:"分类名称",
            GROUP_PAR:"上级分类",
            GROUP_ROOT:"根",
            SAVE_BTN:"保存",
            CAL_BTN:"取消",
            ALERT_GROUPNAME:"请填写分类名称!",
            ALERT_OPT_OK:"操作成功!",
            ALERT_OPT_ER:"报错，请重试!",
            ALERT_NAMECF:"名称重复!",
            ALERT_DELETE_0:"你确定删除吗?",
            ALERT_DELETE_1:"被引用，无法删除!",
            ALERT_ERROR:"发生错误!"
        };
        toLocal(MenuLoaclInfo);
    </script>
</head>
<body style='width:100%;height:100%;'>
<div id="pageContent" style="position:absolute;top:0px;left:0px;right: 0px;bottom: 0px;overflow: auto;">
	<div style="padding:5px;border-bottom:1px solid #D0E5FF;" id="queryFormDIV">
		<%--查询条件表单模板--%>
        <table class="MetaTermTable" style="" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td style="width: 15%; text-align: right;font-weight: bold;"><%=I18nManager.getItemText(menuId,"QUERY_KEY","关键字:")%></td>
                <td style="width: 35%; text-align: left;"><div id="kwd"></div></td>
                <td style="width: 50%; text-align: center">
                    <input name="" id="queryBtn" type="button" value="<%=I18nManager.getItemText(menuId,"QUERY_BTN","查 询")%>" class="btn_2" />
                	<input name="" id="newBtn" type="button" value="<%=I18nManager.getItemText(menuId,"NEWROOT_BTN","新增根分类")%>" class="btn_6" />
                    <input name="" id="levelBtn" type="button" value="<%=I18nManager.getItemText(menuId,"LEVEL_BTN","确认分类层次")%>" class="btn_6" />
                </td>
            </tr>
        </table>
	</div>
	<div style="min-height:200px;min-width:800px;left:0px;bottom: 0px;right: 0px;" id="dataDiv">
    </div>

    <div style="overflow-y:auto;display:none;" id="groupFormDIV">
        <form action="#" id="groupForm">
            <table class="MetaFormTable" border="0" cellpadding="0" cellspacing="1">
                <tr>
                    <td style="text-align: right;"><%=I18nManager.getItemText(menuId,"GROUP_PAR","上级分类:")%></td>
                    <td><input type="text" discs="1" id="parentName" readonly="readonly" value=""/>
                        <input type="hidden" id="parentId" value="0"/></td>
                </tr>
                <tr>
                    <td style="text-align: right;"><%=I18nManager.getItemText(menuId,"GROUP_NAME","分类名称:")%></td>
                    <td><input type="text" id="groupName" value=""/>
                        <input type="hidden" id="groupId" value="0"/></td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align:center;padding:23px 0 5px 0;">
                        <input name="" id="saveBtn" type="button" value="<%=I18nManager.getItemText(menuId,"SAVE_BTN","保 存")%>" class="btn_2" />
                        <input name="" id="calBtn" type="button" value="<%=I18nManager.getItemText(menuId,"CAL_BTN","取 消")%>" class="btn_2" />
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>
</html>