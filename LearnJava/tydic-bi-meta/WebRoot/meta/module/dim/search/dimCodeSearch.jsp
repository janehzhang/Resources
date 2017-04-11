<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title>维度值查询</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimCodeSearchAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DimMergeAction.js"></script>
    <script type="text/javascript" src="dimCodeSearch.js"></script>
</head>
<body>
	<div id="dimCodeSearch" style="display:none;">
		<span>维度表：</span><select><option value="META_DIM_USER_DEPT">META_DIM_USER_DEPT</option><option value="META_DIM_USER_STATION">META_DIM_USER_STATION</option></select>
		<span>归并类型：</span><select><option>默认归并</option></select>
		<span>关键字：</span><input type="text" id="keyword"/>
	</div>
	
	<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;position: relative;display:none;"
       class="dhtmlxInfoBarLabel formTable" id="_dimInfoTable">

    <form action="" id="_queryDimForm" onsubmit="return false;">
        <tr style="height:10%">
            <td  style="text-align: right;padding: 2px ;display:none;" >查询维表：</td>
            <td style="text-align: left;padding: 2px;display:none;">
                <input type="hidden" name="tableGroupId" id="_queryDimId" />
                <input type="hidden" name="tableTypeId" value="2" />
                <input class="dhxlist_txt_textarea" name="tableGroup" id="_queryDimName" type="text" style="padding-right: 0px;width: 100%" />
            </td>
            <td style="text-align: right;padding: 2px;padding-left:50px;" nowrap='nowrap'>关键字：</td>
            <td style="text-align: left;padding: 2px">
                <input class="dhxlist_txt_textarea" name="queryMessage" id="_queryDimTableName" type="text" style="width: 300px; " />
            </td>
            <td style="text-align: center;padding-left:20px;width:110px; " id="_queryDimButton"></td>
        </tr>
    </form>
    <!--维度表查询grid-->
    <tr>
        <td colspan="5" style="width:100%;height: 90%;">
            <div id="_queryDimGrid" style="width:100%;height:100%;"> </div>
        </td>
    </tr>
</table>
<div id="_dimInfoWindowButton"></div>
<div id="_dimDataGrid"></div>
</body>
</html>