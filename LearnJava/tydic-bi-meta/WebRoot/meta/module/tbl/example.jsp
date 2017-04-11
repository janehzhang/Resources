<%--
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王春生
 * @description 
 * @date 12-5-10
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title></title>
    <%@include file="../../public/header.jsp"%>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/i18n.jsp?menuId=<%=menuId%>"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_TYPE"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TestWCSAction.js"></script>
    <script type="text/javascript" src="example.js"></script>
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
<%--查询页面布局模板--%>
<div id="pageContent" style="height:100%">
    <div style="width:100%;position:relative;margin:2px;" id="queryFormDIV">
        <%--查询条件表单模板--%>
        <table class="MetaTermTable" border="0" cellpadding="0" cellspacing="0">
            <colgroup>
                <col width="10%"><col width="15%">
                <col width="10%"><col width="15%">
                <col width="10%"><col width="15%">
            </colgroup>
            <tr>
                <th><%=I18nManager.getItemText(menuId,"","数据源:")%></th>
                <td><div id="dataSource"></div></td>
                <th><%=I18nManager.getItemText(menuId,"","所属用户:")%></th>
                <td id="owner"></td>
                <th><%=I18nManager.getItemText(menuId,"","层次分类:")%></th>
                <td id="tableType"></td>
            </tr>
            <tr>
                <th><%=I18nManager.getItemText(menuId,"","业务类型:")%></th>
                <td id="tableGroup"></td>
                <th><%=I18nManager.getItemText(menuId,"","关键字:")%></th>
                <td id="keyWord"></td>
                <td style="text-align:center" colspan="2">
                    <a href="javascript:void(0);" id="queryBtn" class="metaBtn"><span><%=I18nManager.getItemText(menuId,"","查询")%></span></a>
                </td>
            </tr>
            <tr>
                <th><%=I18nManager.getItemText(menuId,"","测试自实现combo:")%></th>
                <td><div id="classCombo"></div></td>
                <th><%=I18nManager.getItemText(menuId,"","测试自实现tree:")%></th>
                <td id="classTree"></td>
                <th><%=I18nManager.getItemText(menuId,"","测试码表:")%></th>
                <td id="testCode"></td>
            </tr>
        </table>
    </div>
    <div style="width:100%;position:relative;margin:2px;" id="dataDiv">
    </div>
</div>
<%--新增维护表单模板--%>
<div style="width:100%;height:100%;position:relative;display:none;margin:2px;overflow-y:auto;" id="tableFormDIV">
    <table class="MetaFormTable" style="display:block;" border="0" cellpadding="0" cellspacing="1">
        <colgroup>
            <col width="15%"><col width="35%">
            <col width="15%"><col width="35%">
        </colgroup>
        <tr>
            <th>字段一</th>
            <td><input type="text" name="aaa" value=""></td>
            <th>字段二</th>
            <td><select name="bbb"><option></option></select></td>
        </tr>
        <tr>
            <th>字段二</th>
            <td><select name="ccc"><option></option></select></td>
            <th>字段二</th>
            <td><select name="bbb"><option></option></select></td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:center;">
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <a href="javascript:void(0);" id="saveBtn" class="metaBtn"><span><%=I18nManager.getItemText(menuId,"","保存表单")%></span></a>
                <a href="javascript:void(0);" id="calBtn" class="metaBtn"><span><%=I18nManager.getItemText(menuId,"","取消关闭")%></span></a>
            </td>
        </tr>
    </table>
</div>
</body>
</html>