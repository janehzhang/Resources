<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<% 
String path = request.getContextPath();
%>
<html>
  <head>
    <title>查看审核信息</title>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_TYPE,DATA_TYPE"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/AuditDataAction.js"></script>
	<script type="text/javascript" src="<%=path %>/portal/resource/js/OPString.js"></script>
	<script type="text/javascript" src="showAuditData.js"></script>
	
	<script type="text/javascript">
		var issueId=<%=request.getParameter("issueId")%>;			//模型id
		var dataDate=<%=request.getParameter("dataDate")%>;			//数据日期
     	var dataPeriod='<%=request.getParameter("dataPeriod")%>';	//按月还是按天
     	var auditProp='<%=request.getParameter("auditProp")%>';		//审核模式（全省审还是分公司审）
     	var maxDate='<%=request.getParameter("maxDate")%>';			//审核数据最大天数
	</script>
	<style type="text/css">
    	.tableBaseInfo{
			margin-right: auto;
			margin-left: auto;
			border-left: #99b2c9 solid 1px;
			text-align: center;
			font-size: 12px;
			margin-top: 2px;
			border-bottom: #99b2c9 solid 1px;
		}
		.tableTd{
			border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px; 
			text-align: left; 
			padding-left: 2px; 
			line-height: 24px;
			background-color: #ffffff; 
		}
		.tableTh { 
			white-space: nowrap; 
			text-align: right; 
			font-weight: normal; 
			background-color: #E2EFFF; 
			border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px;  
			padding: 0px; 
			margin: 0px;
			line-height: 24px;
		}
	</style>
  </head>

<body style="overflow-x:hidden;overflow-y:auto;padding:0px;margin: 0px;*position: relative;">
	<div id="form4Hidden" style="display:none;"></div>
	<table cellpadding="0" cellspacing="0" style="width:99%;" id="_tableBaseInfo" class="tableBaseInfo">
    	<tr>
    		<th class="tableTh" width=15%>数据日期：</th>
    		<td class="tableTd" width=35%>
    			<span id="showD"><input type="text" id="dataDate" readonly="readonly"/></span>
    			<span id="showM" style="display:none;"><select id="dataDate1" style="width:80px;"></select>
    			<select id="dataDate2" style="width:80px;"></select></span>
    		</td>
    		<th class="tableTh" width=15%>地域：</th>
    		<td class="tableTd" width=35%>
    		 	<select id="localCode" style="width: 180px;" ></select>
    		</td>
    	</tr>
    		
    	<tr>
    		<th class="tableTh">数据别名：</th>
    		<td class="tableTd" >
    			<span id="tableAlias"></span>
    		</td>
    		<th class="tableTh">实体表：</th>
    		<td class="tableTd" >
    		 	<select id="tableInst" style="max-width:250px;width: 180px;" ></select>
    		</td>
    	</tr>
    	
    	<tr>
    		<th class="tableTh">表类信息：</th>
    		<td class="tableTd" colspan="3">
    			<span id="tableInfo"></span>
    		</td>
    	</tr>
    		
    	<tr>
    		<th class="tableTh">数据周期：</th>
    		<td class="tableTd">
    			<span id="dataPeriod"></span>
    		</td>
    		<th class="tableTh">应用约定：</th>
    		<td class="tableTd">
    			<span id="maxDate"></span>
    		</td>
    	</tr>
    	
    	<tr>
    		<th class="tableTh">数据说明：</th>
    		<td class="tableTd"  colspan="3">
    			<textarea id="issueNote" rows="3" style="width: 76%" readonly="readonly"></textarea>
    		</td>
    	</tr>

    	<tr>
    		<td class="tableTd" colspan="4">
			    <div id="tableInst_show" style="height:280px;width:100%"></div>
    		</td>
    	</tr>
		
		<tr>
    		<th class="tableTh">历史审核信息：</th>
    		<td class="tableTd" colspan="3">
    			<div style="width: 100%" id="auditMark"></div>
    		</td>
    	</tr>
    	<tr>
    		<th class="tableTh">审核结论：</th>
    		<td class="tableTd" colspan="3">
				<span id="isAuditConclude"></span>
    		</td>
    	</tr>
    	<tr>
    		<th class="tableTh">提示信息：</th>
    		<td class="tableTd" colspan="3">
				<textarea id="showOpinion" rows="2" style="width: 73%" readonly="readonly"></textarea>
    		</td>
    	</tr>
    </table>
</body>
</html>
