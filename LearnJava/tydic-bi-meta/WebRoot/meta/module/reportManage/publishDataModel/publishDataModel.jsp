<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<%@ page import="tydic.meta.common.Constant" %>
<%@ page import="tydic.frame.SystemVariable" %>
<%@ page import="java.util.Map" %>

<%
	String path = request.getContextPath();
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
		var userId = '<%= userId%>';
		var zoneId = '<%= zoneId%>';
		var deptId = '<%= deptId%>';
		var stationId = '<%= stationId%>';
		var dimZoneId = '<%= dimZoneId%>';
		var dimDateId = '<%= dimDateId%>';
	</script>
    <title>发布数据模型</title>
	<script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=TABLE_TYPE,DATA_TYPE,ISSUE_AUDIT_TYPE,DATA_CYCLE,PRT_AGREED,AUDIT_PROP,PUSH_TYPE"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/TableApplyAction.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/IssueDataModelAction.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/ColTypeAction.js"></script>
	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/DeptAction.js"></script>
	<script type="text/javascript" src="<%=path %>/portal/resource/js/OPString.js"></script>
	<script type="text/javascript" src="publishDataModel.js"></script>
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
	<div id="form4Hidden" style="display:none;"></div>
	<div id="modelForm4Hidden" style="display:none;"></div>
	<div id="publishDataModel" style="height: 100%;width: 100%"></div>
	<div id="pub_serach" class="pub_search">
		<div style="padding-left:8px;display:inline;"><span style="line-height: 20px">数据源：</span><select id="data_source" style="width:130px;height:20px;"><option value="-1"></option></select></div>
		<div style="padding-left:12px;display:inline;"><span style="line-height: 20px">所属用户：</span><select id="userByDB" style="width:110px;height:20px;"><option value="-1" style="color: #aaa;font-style: italic;">请先选择数据源</option></select></div>
		<div style="padding-left:12px;display:inline;"><span style="line-height: 20px">层次分类：</span><select style="width:100px;height:20px;" id="levelType"><option value="">--请选择--</option></select></div>
		<div style="padding-left:12px;display:inline;"><span style="line-height: 20px">业务类型：</span><input id="businessType" style="width:110px;height: 14px;color: #aaa;" type='text' value="请先选择层次分类" /><input type="hidden" name="businessTypeId" id="businessTypeId" /></div>
		<div style="padding-left:12px;display:inline;"><span style="line-height: 20px">模型名：</span><input id="keyword" style="height:14px;" type='text'/></div>
		<div style="padding-left:12px;display:inline;"><input id="search" class="poster_btn" type='button' style="height: 21px;" value='查询'/></div>
	</div>
	
	<div id="issueDataModel">
	<table cellpadding="0" cellspacing="0" style="width:100%;height: 150px;" class="dhtmlxInfoBarLabel formTable" id="issueDataModelInfo">
		<tr>
			<th width="15%" style="height:25px;">表类选择：</th>
			<td colspan="3" style="height:25px;">	
				<span style="width:330px;" id="tableTypeInfo"></span>
		  		<input class="poster_btn" style="margin-left:100px;width:50px;height:20px;" type="button" value="选择" id="selectTableType" />
		  	</td>
		</tr>
		<tr>
			<th width="15%" style="height:20px;">模型别名：</th>
			<td width="35%" style="height:20px;">
		  		<input name="dataAlias" id="dataAlias" type="text" style="width: 200px;height:15px;line-height:15px;"></input>
		  		<span style="color: red;display:inline;" >*</span>
		  	</td>
		  	
		  	<th width="15%" style="height:20px;">数据审核方式：</th>
			<td width="35%" style="height:20px;">
		  		<input checked='checked' name="auditType" id="artificial" type="radio" value="1" /><span style="position:relative;top:-2px;margin-right:15px;"><label  for="artificial">人工</label></span>
		  		<input name="auditType" id="Automatic" type="radio" value="0" /><span style="position:relative;top:-2px;margin-right:15px;"><label for="Automatic">自动</label></span>
		  	</td>
		  		
		</tr>
		<tr>
		  	<th style="height:20px;">数据周期：</th>
		  	<td style="height:20px;">
		  		<input  checked='checked' name="dataCycle" id="forDate" type="radio" value="11" onclick="cheangeAuditType(11,'insert');" /><span style="position:relative;top:-2px;margin-right:15px;"><label  for="forDate">按天</label></span>
		  		<input  name="dataCycle" id="forMonth" type="radio" value="22" onclick="cheangeAuditType(22,'insert');" /><span style="position:relative;top:-2px;margin-right:15px;"><label for="forMonth">按月</label></span>
			</td>
			
			<th style="height:20px;">应用约定：</th>
		  	<td style="height:20px;">
		  		<input 	checked='checked' name="appRule" id="before1Day" type="radio" value="-1" /><span style="position:relative;top:-2px;margin-right:3px;"><label id="before1DayLabel" for="before1Day">前一天</label></span>
		  		<input  name="appRule" id="before2Day" type="radio" value="-2" /><span style="position:relative;top:-2px;margin-right:3px;"><label id="before2DayLabel" for="before2Day">前两天</label></span>
		  		<input  name="appRule" id="before3Day" type="radio" value="-3" /><span style="position:relative;top:-2px;margin-right:3x;"><label id="before3DayLabel" for="before3Day">前三天</label></span>
			</td>

		</tr>
		<tr>
		  	<th style="height:20px;">审核模式：</th>
			<td style="height:20px;">
		  		<input checked='checked' name="auditProp" id="province" type="radio" value="1" /><span style="position:relative;top:-2px;margin-right:15px;"><label  for="province">全省审</label></span>
		  		<input  name="auditProp" id="branch" type="radio" value="2" /><span style="position:relative;top:-2px;margin-right:15px;"><label for="branch">分公司审</label></span>
		  	</td>
		  	<th style="height:20px;">模型类型：</th>
			<td style="height:20px;">
		  		<input name="isListing" id="isNO" type="radio" value="0" checked='checked' onclick="cheangeIssueType(0,'insert');" /><span style="position:relative;top:-2px;margin-right:15px;"><label for="isNO">报表</label></span>
		  		<input name="isListing" id="isYes" type="radio" value="1" onclick="cheangeIssueType(1,'insert');" /><span style="position:relative;top:-2px;margin-right:15px;"><label  for="isYes">清单</label></span>
		  	</td>
		</tr>
		<tr>
			<th style="height:20px;">生效时间：</th>
		  	<td style="height:20px;">
		  		<input readonly='readonly' name="effectTime" id="effectTime" type="text" style="width: 155px;height:15px;line-height:15px;"></input>
			</td>
		  	<th style="height:20px;">支持的发送方式：</th>
			<td style="height:20px;">
			  	<div id="subType"></div>
		  	</td>
		</tr>
		<tr>
			<th style="height:20px;">关键字：</th>
		  	<td style="height:20px;" colspan="3">
		  		<input name="_keyword" id="_keyword" type="text" style="width: 80%;height:15px;line-height:15px;"></input>
			</td>
		</tr>
		<tr>
			<th style="height:50px;">模型数据说明：</th>
			<td style="height:50px;" colspan="3">
				<textarea name="_dataRemark" id="_dataRemark" rows="3" style="width: 80%;"></textarea>
				<span style="color: red;display:inline;top:-15px;position:relative;" >*</span>
			</td>
		</tr>
    </table>
    <div id="issueDataModelTable" style="height:158px;border: #99b2c9 solid 1px;"></div>
    <div style="height:20px;">
    	<input class="poster_btn" style="margin-top:6px;position:relative;left:750px;top:-2px;" id="publishSubmit" type="button" value="发布" />
    	<input class="poster_btn" style="margin-top:6px;position:relative;left:750px;top:-2px;" id="publishCancel" type="button" value="取消" />
    </div>
    </div>
    
   <div id="issueDataModel_search">
	<table cellpadding="0" cellspacing="0" style="width:100%;height: 190px;" class="dhtmlxInfoBarLabel formTable" id="issueDataModelInfo_search">
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
			<th style="height:20px;">生效时间：</th>
		  	<td style="height:20px;">
				<span style="width:330px;" id="effectTime_serach"></span>
			</td>
		  	<th style="height:20px;">支持的发送方式：</th>
			<td style="height:20px;">
			  	<div id="subType_serach"></div>
		  	</td>
		</tr>
		<tr>
			<th style="height:20px;">关键字：</th>
		  	<td colspan="3">
				<span style="width:330px;" id="_keyword_search"></span>
			</td>
		</tr>
		<tr>
			<th style="height:20px;">模型数据说明：</th>
			<td colspan="3">
				<div style="width: 100%;max-height:55px; overflow-y:auto;overflow-x:hidden;" id="_dataRemark_search"></div>
			</td>
		</tr>
    </table>
    <div id="issueDataModelTable_search" style="height:160px;border: #99b2c9 solid 1px;"></div>
    <div style="height:20px;">
    	<input class="poster_btn" style="margin-top:10px;position:relative;left:800px;top:-5px;" id="publishCancel_search" type="button" value="关闭" />
    </div>
    </div>
    
    <div id="issueDataModel_modify">
	<table cellpadding="0" cellspacing="0" style="width:100%;height: 150px;" class="dhtmlxInfoBarLabel formTable" id="issueDataModelInfo_modify">
		<tr>
			<th style="height:20px;" width="15%">表类信息：</th>
			<td colspan="3">
				<span style="width:330px;" id="tableTypeInfo_modify"></span>
		  	</td>
		</tr>
		<tr>
			<th style="height:20px;" width="15%">模型别名：</th>
			<td width="35%">
		  		<input name="dataAlias_modify" id="dataAlias_modify" type="text" style="width: 200px;height:15px;line-height:15px;"></input>
		  		<span style="color: red;display:inline;" >*</span>
		  	</td>
		  	
		  	<th width="15%">数据审核方式：</th>
			<td width="35%">
		  		<input name="auditType_modify" id="artificial_modify" type="radio" value="1" /><span style="position:relative;top:-2px;margin-right:15px;"><label  for="artificial_modify">人工</label></span>
		  		<input name="auditType_modify" id="Automatic_modify" type="radio" value="0" /><span style="position:relative;top:-2px;margin-right:15px;"><label for="Automatic_modify">自动</label></span>
		  	</td>	
		</tr>
		<tr>
		  	<th style="height:20px;">数据周期：</th>
		  	<td>
		  		<input name="dataCycle_modify" id="forDate_modify" type="radio" value="11" onclick="cheangeAuditType(11,'update');" /><span style="position:relative;top:-2px;margin-right:15px;"><label  for="forDate_modify">按天</label></span>
		  		<input name="dataCycle_modify" id="forMonth_modify" type="radio" value="22" onclick="cheangeAuditType(22,'update');" /><span style="position:relative;top:-2px;margin-right:15px;"><label for="forMonth_modify">按月</label></span>
			</td>
			
			<th>应用约定：</th>
		  	<td>
		  		<input name="appRule_modify" id="before1Day_modify" type="radio" value="-1" /><span style="position:relative;top:-2px;margin-right:3px;"><label id="before1DayLabel_modify" for="before1Day_modify">前一天</label></span>
		  		<input name="appRule_modify" id="before2Day_modify" type="radio" value="-2" /><span style="position:relative;top:-2px;margin-right:3px;"><label id="before2DayLabel_modify" for="before2Day_modify">前两天</label></span>
		  		<input name="appRule_modify" id="before3Day_modify" type="radio" value="-3" /><span style="position:relative;top:-2px;margin-right:3x;"><label id="before3DayLabel_modify" for="before3Day_modify">前三天</label></span>
			</td>

		</tr>
		<tr>
		  	<th style="height:20px;">审核模式：</th>
			<td>
		  		<input name="auditProp_modify" id="province_modify" type="radio" value="1" /><span style="position:relative;top:-2px;margin-right:15px;"><label  for="province_modify">全省审</label></span>
		  		<input name="auditProp_modify" id="branch_modify" type="radio" value="2" /><span style="position:relative;top:-2px;margin-right:15px;"><label for="branch_modify">分公司审</label></span>
		  	</td>
		  	<th>模型类型：</th>
			<td>
		  		<input name="isListing_modify" id="isNO_modify" type="radio" value="0" onclick="cheangeIssueType(0,'update');"/><span style="position:relative;top:-2px;margin-right:15px;"><label for="isNO_modify">报表</label></span>
		  		<input name="isListing_modify" id="isYes_modify" type="radio" value="1" onclick="cheangeIssueType(1,'update');"/><span style="position:relative;top:-2px;margin-right:15px;"><label  for="isYes_modify">清单</label></span>
		  	</td>
		</tr>	
		<tr>
			<th style="height:20px;">生效时间：</th>
		  	<td style="height:20px;">
				<span style="width:330px;" id="effectTime_modify"></span>
		  	  <!-- 	<input readonly="readonly" name="effectTime_modify" id="effectTime_modify" type="text" style="width: 155px;height:15px;line-height:15px;"></input> -->
			</td>
		  	<th style="height:20px;">支持的发送方式：</th>
			<td style="height:20px;">
			  	<div id="subType_modify"></div>
		  	</td>
		</tr>
		<tr>
			<th style="height:20px;">关键字：</th>
		  	<td style="height:20px;" colspan="3">
		  		<input name="_keyword_modify" id="_keyword_modify" type="text" style="width: 80%;height:15px;line-height:15px;"></input>
			</td>
		</tr>
		<tr>
			<th style="height:50px;">模型数据说明：</th>
			<td colspan="3">
				<textarea name="_dataRemark_modify" id="_dataRemark_modify" rows="3" style="width: 80%;line-height: 17px;"></textarea>
				<span style="color: red;display:inline;top:-15px;position:relative;" >*</span>
			</td>
		</tr>
    </table>
    <div id="issueDataModelTable_modify" style="height:160px;border: #99b2c9 solid 1px;"></div>
    <div style="height:20px;">
    	<input class="poster_btn" style="margin-top:10px;position:relative;left:750px;top:-2px;" id="publishSubmit_modify" type="button" value="修改" />
    	<input class="poster_btn" style="margin-top:10px;position:relative;left:750px;top:-2px;" id="publishCancel_modify" type="button" value="取消" />
    </div>
    </div>

    <div id="codeDiv" style="display:none;left:28%;top:20%;position:absolute;width:450px;height:300px;overflow:auto;padding:0;margin:0;z-index:1000;"></div>
</body>
</html>
