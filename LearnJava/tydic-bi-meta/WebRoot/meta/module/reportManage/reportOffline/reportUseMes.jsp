<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html>
  <head>
    <title>报表使用信息</title>
	<script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_TYPE,DATA_TYPE"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/DataOfflineAction.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/ReportOfflineAction.js"></script>
	<script type="text/javascript" src="reportUseMes.js"></script>
	<script type="text/javascript">
		var reportId=<%=request.getParameter("reportId")%>;			
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
    <div id="_reportUseMes">
	<table cellpadding="0" cellspacing="0" style="width:100%;height: 150px;" id="_tableBaseInfo">
    	<tr>
    		<th width="15%">收藏数：</th>
    		<td width="35%">
    			<span id="_scnum">&nbsp;</span>
    		</td>
    		
    		<th width="15%">订阅数：</th>
    		<td width="35%">
    			<span id="_dynum">&nbsp;</span>
    		</td>
    	</tr>
    		
    	<tr>
    		<th>最近访问者：</th>
    		<td>
    			<span id="_zjAccess">&nbsp;</span>
    		</td>
    		
    		<th>最近访问时间：</th>
    		<td>
    			<span id="_zjAccessTime">&nbsp;</span>
    		</td>
    	</tr>
    		
    	<tr>
    		<th>联系电话：</th>
    		<td>
    			<span id="_tel">&nbsp;</span>
    		</td>
    		<th>联系邮箱：</th>
    		<td>
    			<span id="_email">&nbsp;</span>
    		</td>
    	</tr>
    </table>
    </div>
</body>
</html>