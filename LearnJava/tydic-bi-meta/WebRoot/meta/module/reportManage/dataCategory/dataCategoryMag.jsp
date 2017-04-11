<%--
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王春生
 * @description 
 * @date 12-4-5
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title></title>
    <%@include file="../../../public/head.jsp"%>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=DATA_CATEGORY_TYPE,CATEGORY_SHOW_MODE"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DataCategoryMagAction.js"></script>
    <script type="text/javascript" src="dataCategoryMag.js"></script>
    <style type="text/css">
        input[type="button"]{
            border:#9DCAEE 1px solid;
            line-height:18px;
            cursor:pointer;
            margin:0 10px;
        }
        img[onclick]{
            cursor:pointer;
        }
    </style>
</head>
<body style='width:100%;height:100%;'>
<form action="#" id="queryForm" style="width:100%;height: 100%;display: none;">
    <table cellpadding="0" cellspacing="0" style="width:100%;position:relative;padding-top: 10px;">
        <tr>
            <td style="text-align: right;width:120px">分类名称：</td>
            <td style="text-align: left;width:150px;">
                <input name="keyWord" id="_keyWord" type="text" style="width:100%;">
            </td>
            <td align="left" style="padding-left:50px;" id="_queryButton">
                <input type="button" value="查询" onclick="rtGrid.reLoad();">
            </td>
            <td>&nbsp;</td>
        </tr>
    </table>
</form>
<form action="#" id="categoryForm" style="width:100%;height: 100%;display: none">
    <table cellpadding="0" cellspacing="0" style="width:100%;position:relative;padding-top: 10px;">
        <tr>
            <td style="text-align: right;width:100px;">上级分类：</td>
            <td style="text-align: left;width:200px;">
                <input type="hidden" name="id" id="_id">
                <input type="hidden" name="parentId" id="_parentId">
                <input name="parentName" id="_parentName" type="text" readonly="readonly" style="width:150px;color: #a9a9a9;">
            </td>
        </tr>
        <tr>
            <td style="text-align: right;">分类名称：</td>
            <td style="text-align: left;width:200px;">
                <input name="categoryName" id="_categoryName" type="text" style="width:150px;">
            </td>
        </tr>
        <tr id="showModeTr" style="height: 40px;">
            <td style="text-align: center;" colspan="2">
                <div id="showModeDIV">
                    <%--<input type="radio" id="showMode1" value="1" name="showMode" checked="checked">首页展示&nbsp;--%>
                    <%--<input type="radio" id="showMode2" value="2" name="showMode">展示为更多&nbsp;--%>
                    <%--<input type="radio" id="showMode3" value="3" name="showMode">不展示--%>
                </div>
            </td>
        </tr>
        <tr style="height:40px;">
            <td style="text-align:center;vertical-align: middle;" colspan="2" id="_saveButton">
                <input type="button" value="保存" onclick="saveCat()">&nbsp;
                <input type="button" value="取消" onclick="aouWin.close();">
            </td>
        </tr>
    </table>
</form>
<table id="relTable" cellpadding="0" cellspacing="0" style="width:100%;height:100%;position:relative;display: none">
    <tr>
        <td style="text-align: left;height: 50px;" colspan="3">
            <div style="width:100%;">
                <div style="float: left;margin-left: 10px;" id="typeDIV"></div>
                <div style="float: left;margin-left: 10px;">
                    <input name="kwd" id="_kwd" type="text" style="width:180px; height:18px;"/>
                </div>
                <div style="float: left;margin-left: 40px;" id="_qButton">
                    <input type="button" value="查询" onclick="qGrid.reLoad();">
                </div>
            </div>
        </td>
    </tr>
    <tr style="height: 280px">
        <td style="text-align: left;">
            <span>&nbsp;</span>
            <div id="queryDiv" style="width:340px;height:100%;border:1px #0176fe solid;margin:5px"></div>
        </td>
        <td style="text-align: center;vertical-align:middle;">
            <img src="../../../resource/images/arrow_right.png" alt="" onclick="jiaData();">
            <br>
            <img src="../../../resource/images/arrow_right_double.png" alt="" onclick="jiaAllData();">
            <br>
            <img src="../../../resource/images/arrow_left.png" alt="" onclick="jianData();">
            <br>
            <img src="../../../resource/images/arrow_left_double.png" alt="" onclick="jianAllData();">
        </td>
        <td>
            <span>分类  已关联数据</span>
            <div id="dataDiv" style="width:340px;height:100%;border:1px #0176fe solid;margin:5px"></div>
        </td>
    </tr>
    <tr style="height: 60px;">
        <td style="text-align: center;" colspan="3" id="_saveRelButton">
            <input type="button" value="提交" onclick="saveRel()">&nbsp;
            <input type="button" value="取消" onclick="relWin.close();">
        </td>
    </tr>
</table>
</body>
</html>