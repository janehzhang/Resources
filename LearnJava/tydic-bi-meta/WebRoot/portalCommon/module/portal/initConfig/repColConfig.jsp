<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp"%>
<%@include file="../../../public/include.jsp"%>
<html>
  <head>
    <title>配置报表列</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=path%>/dwr/interface/ReportConfigAction.js"></script>
	<script type="text/javascript" src="repColConfig.js"></script>
	<script type="text/javascript">
		var tabId=<%=request.getParameter("tabId")%>;			
	</script>
	<style type="text/css">
    	.formAnnotation{
    		  color:#FFA500;
    	}
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
		#_tableBaseInfo tbody tr td input {
			width: 155px;height:15px;line-height:15px;
		}
		#_tableBaseInfo tbody tr th { 
			white-space: nowrap; 
			text-align: right; 
			font-weight: normal; 
			background-color: #e2efff; 
			border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px;  
			padding: 0px; 
			margin: 0px;
		}
        #icoTable {
			background: url('<%=path%>/meta/resource/images/ico-h.gif');
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
  	<div id="_repTabMes">
  		<table cellpadding="0" cellspacing="0" style="width:100%;height: 100px;" id="_tableBaseInfo">
	  		<tr>
	    		<th width="15%">报表类型：</th>
	    		<td width="35%">
	    			<span id="_repType">&nbsp;</span>
	    		</td>
	    		<th>排序序号：</th>
	    		<td width="35%">
	    			<span id="_orderId">&nbsp;</span>
	    		</td>
	    	</tr>
	    		
	    	<tr>
	    		<th width="15%">钻取层级：</th>
	    		<td width="35%">
	    			<span id="_rolldownLayer">&nbsp;</span>
	    		</td>
	    		<th>图形默认取数列横纵坐标：</th>
	    		<td>
	    			<span id="_defaultGrid">&nbsp;</span>
	    		</td>
	    	</tr>
	    	<tr>
	    		<th>描述：</th>
	    		<td colspan="3">
	    			<span id="_tabDesc">&nbsp;</span>
	    		</td>
	    	</tr>
  		</table>
  	</div>
  </body>
</html>