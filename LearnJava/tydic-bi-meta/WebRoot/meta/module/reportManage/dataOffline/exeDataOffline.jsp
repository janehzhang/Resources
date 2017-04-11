<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html>
  <head>
    <title>数据下线操作</title>
	<script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_TYPE,DATA_TYPE,REPORT_STATE,COL_BUS_TYPE"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/DataOfflineAction.js"></script>
	<script type="text/javascript" src="exeDataOffline.js"></script>
	<script type="text/javascript">
		var dataSourceId=<%=request.getParameter("dataSourceId")%>;			
		var issueId=<%=request.getParameter("issueId")%>;			
     	var tableAlias='<%=request.getParameter("tableAlias")%>';	
     	var typeId = <%=request.getParameter("typeId")%>;
     	var tableId = <%=request.getParameter("tableId")%>;
	</script>
	<style type="text/css">
    	#_tableBaseInfo{
			margin-right: auto;
			margin-left: auto;
			border-left: #99b2c9 solid 1px;
			text-align: center;
			font-size: 12px;
			margin-top: 2px;
			border-bottom: #99b2c9 solid 1px;
		}
		#_tableBaseInfo tbody tr td {
			border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px; 
			text-align: left; 
			padding-left: 2px; 
			line-height: 15px; 
			background-color: #ffffff; 
		}
		#_tableBaseInfo tbody tr th { 
			white-space: nowrap; 
			text-align: right; 
			font-weight: normal; 
			background-color: #E2EFFF; 
			border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px;  
			padding: 0px; 
			margin: 0px;
		}
        #icoTable {
			background: url('<%=rootPath%>/meta/resource/images/ico-h.gif');
			background-position: -50px 0;
			width: 28px;
			height: 24px;
			float: left;
			text-align: right;
		}
		#tableMeg{
			font-weight: bold;
		}
	</style>
  </head>
  
<body style="height: 100%;width: 100%;">
	<div id="_exeDataOfflineMes">
	<!--  
	<div style="width:100%;height:30px;line-height:20px;" id="_queryDataModelTitle"></div>
	-->
	<table cellpadding="0" cellspacing="0" style="width:100%;height: 150px;" id="_tableBaseInfo">
    	<tr>
    		<th width="15%">模型名称：</th>
    		<td width="35%">
    			<span id="_tableAlias">&nbsp;</span>
    		</td>
    		<th>表类名称：</th>
    		<td width="35%">
    			<span id="_tableInfo">&nbsp;</span>
    		</td>
    	</tr>
    		
    	<tr>
    		<th width="15%">数据审核方式：</th>
    		<td width="35%">
    			<span id="_auditType">&nbsp;</span>
    		</td>
    		<th>数据周期：</th>
    		<td>
    			<span id="_dataC">&nbsp;</span>
    		</td>
    	</tr>
    		
    	<tr>
    		<th>应用约定：</th>
    		<td>
    			<span id="_maxDate">&nbsp;</span>
    		</td>
    		<th>模型有效状态：</th>
    		<td>
    			<span id="_issueState">&nbsp;</span>
    		</td>
    	</tr>
    		
    	<tr>
    		<th>关键字：</th>
    		<td colspan="3">
    			<span id="_keyWord">&nbsp;</span>
    		</td>
    	</tr>
    	<tr>
    		<th>数据模型说明：</th>
    		<td colspan="3">
    			<span id="_issueNote">&nbsp;</span>
    		</td>
    	</tr>
    </table>
    <div id="_colMesId" style="height: 98px;"></div>
    </div>
</body>
</html>
