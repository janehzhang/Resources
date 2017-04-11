<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<%@ page import="tydic.meta.common.Constant" %>
<%@ page import="tydic.frame.SystemVariable" %>
<%@ page import="java.util.Map" %>

<%
	String path = request.getContextPath();
	String issueId = request.getParameter("issueId");
	Map<String,Object> user=(Map<String,Object>)session.getAttribute(LoginConstant.SESSION_KEY_USER);
	String stationId = user.get("stationId").toString();
	String zoneId = user.get("zoneId").toString();
	String deptId = user.get("deptId").toString();
	String userId = user.get("userId").toString();
	String dimZoneId = SystemVariable.getString("dimZoneId", "");	//地域维度表id
	String dimDateId = SystemVariable.getString("dimDateId", "");	//时间维度表id
 %>
<html>
  <head>
  	<script type="text/javascript">
		var issueId = '<%= issueId%>';
		var userId 	= '<%= userId%>';
		var zoneId 	= '<%= zoneId%>';
		var deptId 	= '<%= deptId%>';
		var stationId = '<%= stationId%>';
		var dimZoneId = '<%= dimZoneId%>';
		var dimDateId = '<%= dimDateId%>';
	</script>
    <title>查看发布数据模型</title>
	<script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_TYPE,DATA_TYPE,ISSUE_AUDIT_TYPE,DATA_CYCLE,PRT_AGREED,AUDIT_PROP"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/IssueDataModelAction.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/ColTypeAction.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/DeptAction.js"></script>
	<script type="text/javascript" src="<%=path %>/portal/resource/js/OPString.js"></script>
	<script type="text/javascript" src="searchPublishDataModel.js"></script>
	<style type="text/css">
		.pub_search{
			padding-top: 10px;
		}
		.formAnnotation{
    		  color:#FFA500;
    	}
    	#issueDataModelInfo,#issueDataModelInfo_search,#issueDataModelInfo_modify,#issueDataModelInfo_offline{
			margin-right: auto;
			margin-left: auto;
			border-left: #99b2c9 solid 1px;
			text-align: center;
			font-size: 12px;
			margin-top: 2px;
			border-bottom: #99b2c9 solid 1px;
		}
		#issueDataModelInfo tr td ,#issueDataModelInfo_search tr td,#issueDataModelInfo_modify tr td,#issueDataModelInfo_offline tr td{
			border-top: #99b2c9 solid 1px; 
			border-right: #99b2c9 solid 1px; 
			text-align: left; 
			padding-left: 2px; 
			line-height: 20px; 
			height:20px;
			background-color: #ffffff; 
		}
		#issueDataModelInfo tr th,#issueDataModelInfo_search tr th,#issueDataModelInfo_modify tr th,#issueDataModelInfo_offline tr th { 
			white-space: nowrap; 
			text-align: right; 
			font-weight: normal; 
			background-color: #f6f9fa; 
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
		label {
			cursor:pointer;
		}
		.poster_btn{
			cursor:pointer;
			display:inline-block;
			font-size:12px;
			width:50px;
			height:25px;
			line-height:20px;
			overflow:hidden;
			top:5px;
			right:0;
			padding-bottom:2px;
			border-radius:2px;
			color: black;
			border: 1px solid #198EB4;
			background-color: #D6E8FF;
			background-position: 0 0!important;
		}
	</style>
  </head>
  
<body style="height: 100%;width: 100%">
   <div id="issueDataModel_search">
	<table cellpadding="0" cellspacing="0" style="width:100%;height: 150px;" class="dhtmlxInfoBarLabel formTable" id="issueDataModelInfo_search">
		<tr>
			<th style="height:20px;" width="15%">表类信息：</th>
			<td colspan="3">
				<span style="width:330px;" id="tableTypeInfo_search"></span>
		  	</td>
		</tr>
		<tr>
			<th style="height:20px;" width="15%">模型别名：</th>
			<td width="35%">
				<span style="width:330px;" id="dataAlias_search"></span>
		  	</td>
		  	<th width="15%">数据审核方式：</th>
			<td width="35%">
				<span style="width:330px;" id="auditType_search"></span>
		  	</td>
		</tr>
		<tr>
		  	<th style="height:20px;">数据周期：</th>
		  	<td>
				<span style="width:330px;" id="dataCycle_search"></span>
			</td>
			
			<th style="height:20px;">应用约定：</th>
		  	<td>
				<span style="width:330px;" id="appRule_search"></span>
			</td>
		</tr>
		<tr>
		  	<th style="height:20px;">审核模式：</th>
			<td>
				<span style="width:330px;" id="auditProp_search"></span>
		  	</td>
		  	<th style="height:20px;">模型类型：</th>
			<td>
				<span style="width:330px;" id="isListing_search"></span>
		  	</td>
		</tr>	
		<tr>
		  	<th style="height:20px;">关键字：</th>
		  	<td>
				<span style="width:330px;" id="_keyword_search"></span>
			</td>
			
			<th style="height:20px;">生效时间：</th>
		  	<td>
				<span style="width:330px;" id="effectTime_serach"></span>
			</td>
		</tr>
		<tr>
			<th style="height:20px;">模型数据说明：</th>
			<td colspan="3">
				<span style="width:330px;" id="_dataRemark_search"></span>
			</td>
		</tr>
    </table>
    <div id="issueDataModelTable_search" style="height:180px;width:100%;border: #99b2c9 solid 1px;"></div>
    </div>
</body>
</html>
